/*
 * $Id: PdfXConformanceImp.java 4065 2009-09-16 23:09:11Z psoares33 $
 *
 * Copyright 2006 Bruno Lowagie (based on code by Paulo Soares)
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * https://github.com/LibrePDF/OpenPDF
 */

package org.openpdf.text.pdf.internal;

import org.openpdf.text.error_messages.MessageLocalization;
import org.openpdf.text.pdf.BaseFont;
import org.openpdf.text.pdf.ExtendedColor;
import org.openpdf.text.pdf.PatternColor;
import org.openpdf.text.pdf.PdfArray;
import org.openpdf.text.pdf.PdfDictionary;
import org.openpdf.text.pdf.PdfGState;
import org.openpdf.text.pdf.PdfImage;
import org.openpdf.text.pdf.PdfName;
import org.openpdf.text.pdf.PdfNumber;
import org.openpdf.text.pdf.PdfObject;
import org.openpdf.text.pdf.PdfString;
import org.openpdf.text.pdf.PdfWriter;
import org.openpdf.text.pdf.PdfXConformanceException;
import org.openpdf.text.pdf.ShadingColor;
import org.openpdf.text.pdf.SpotColor;
import org.openpdf.text.pdf.interfaces.PdfXConformance;
import java.awt.Color;

public class PdfXConformanceImp implements PdfXConformance {

    /**
     * A key for an aspect that can be checked for PDF/X Conformance.
     */
    public static final int PDFXKEY_COLOR = 1;
    /**
     * A key for an aspect that can be checked for PDF/X Conformance.
     */
    public static final int PDFXKEY_CMYK = 2;
    /**
     * A key for an aspect that can be checked for PDF/X Conformance.
     */
    public static final int PDFXKEY_RGB = 3;
    /**
     * A key for an aspect that can be checked for PDF/X Conformance.
     */
    public static final int PDFXKEY_FONT = 4;
    /**
     * A key for an aspect that can be checked for PDF/X Conformance.
     */
    public static final int PDFXKEY_IMAGE = 5;
    /**
     * A key for an aspect that can be checked for PDF/X Conformance.
     */
    public static final int PDFXKEY_GSTATE = 6;
    /**
     * A key for an aspect that can be checked for PDF/X Conformance.
     */
    public static final int PDFXKEY_LAYER = 7;

    /**
     * The value indicating if the PDF has to be in conformance with PDF/X.
     */
    protected int pdfxConformance = PdfWriter.PDFXNONE;

    /**
     * Business logic that checks if a certain object is in conformance with PDF/X.
     *
     * @param writer the writer that is supposed to write the PDF/X file
     * @param key    the type of PDF/X conformance that has to be checked
     * @param obj1   the object that is checked for conformance
     */
    public static void checkPDFXConformance(PdfWriter writer, int key, Object obj1) {
        if (writer == null || !writer.isPdfX()) {
            return;
        }
        int conf = writer.getPDFXConformance();
        switch (key) {
            case PDFXKEY_COLOR:
                switch (conf) {
                    case PdfWriter.PDFX1A2001:
                        if (obj1 instanceof ExtendedColor) {
                            ExtendedColor ec = (ExtendedColor) obj1;
                            switch (ec.getType()) {
                                case ExtendedColor.TYPE_CMYK:
                                case ExtendedColor.TYPE_GRAY:
                                    return;
                                case ExtendedColor.TYPE_RGB:
                                    throw new PdfXConformanceException(
                                            MessageLocalization.getComposedMessage("colorspace.rgb.is.not.allowed"));
                                case ExtendedColor.TYPE_SEPARATION:
                                    SpotColor sc = (SpotColor) ec;
                                    checkPDFXConformance(writer, PDFXKEY_COLOR,
                                            sc.getPdfSpotColor().getAlternativeCS());
                                    break;
                                case ExtendedColor.TYPE_SHADING:
                                    ShadingColor xc = (ShadingColor) ec;
                                    checkPDFXConformance(writer, PDFXKEY_COLOR,
                                            xc.getPdfShadingPattern().getShading().getColorSpace());
                                    break;
                                case ExtendedColor.TYPE_PATTERN:
                                    PatternColor pc = (PatternColor) ec;
                                    checkPDFXConformance(writer, PDFXKEY_COLOR, pc.getPainter().getDefaultColor());
                                    break;
                            }
                        } else if (obj1 instanceof Color) {
                            throw new PdfXConformanceException(
                                    MessageLocalization.getComposedMessage("colorspace.rgb.is.not.allowed"));
                        }
                        break;
                }
                break;
            case PDFXKEY_CMYK:
                break;
            case PDFXKEY_RGB:
                if (conf == PdfWriter.PDFX1A2001) {
                    throw new PdfXConformanceException(
                            MessageLocalization.getComposedMessage("colorspace.rgb.is.not.allowed"));
                }
                break;
            case PDFXKEY_FONT:
                if (!((BaseFont) obj1).isEmbedded()) {
                    throw new PdfXConformanceException(
                            MessageLocalization.getComposedMessage("all.the.fonts.must.be.embedded.this.one.isn.t.1",
                                    ((BaseFont) obj1).getPostscriptFontName()));
                }
                break;
            case PDFXKEY_IMAGE:
                PdfImage image = (PdfImage) obj1;
                if (image.get(PdfName.SMASK) != null) {
                    throw new PdfXConformanceException(
                            MessageLocalization.getComposedMessage("the.smask.key.is.not.allowed.in.images"));
                }
                switch (conf) {
                    case PdfWriter.PDFX1A2001:
                        PdfObject cs = image.get(PdfName.COLORSPACE);
                        if (cs == null) {
                            return;
                        }
                        if (cs.isName()) {
                            if (PdfName.DEVICERGB.equals(cs)) {
                                throw new PdfXConformanceException(
                                        MessageLocalization.getComposedMessage("colorspace.rgb.is.not.allowed"));
                            }
                        } else if (cs.isArray()) {
                            if (PdfName.CALRGB.equals(((PdfArray) cs).getPdfObject(0))) {
                                throw new PdfXConformanceException(
                                        MessageLocalization.getComposedMessage("colorspace.calrgb.is.not.allowed"));
                            }
                        }
                        break;
                }
                break;
            case PDFXKEY_GSTATE:
                PdfDictionary gs = (PdfDictionary) obj1;
                PdfObject obj = gs.get(PdfName.BM);
                if (obj != null && !PdfGState.BM_NORMAL.equals(obj) && !PdfGState.BM_COMPATIBLE.equals(obj)) {
                    throw new PdfXConformanceException(
                            MessageLocalization.getComposedMessage("blend.mode.1.not.allowed", obj.toString()));
                }
                obj = gs.get(PdfName.CA);
                double v = 0.0;
                if (obj != null && (v = ((PdfNumber) obj).doubleValue()) != 1.0) {
                    throw new PdfXConformanceException(
                            MessageLocalization.getComposedMessage("transparency.is.not.allowed.ca.eq.1",
                                    String.valueOf(v)));
                }
                obj = gs.get(PdfName.ca);
                v = 0.0;
                if (obj != null && (v = ((PdfNumber) obj).doubleValue()) != 1.0) {
                    throw new PdfXConformanceException(
                            MessageLocalization.getComposedMessage("transparency.is.not.allowed.ca.eq.1",
                                    String.valueOf(v)));
                }
                break;
            case PDFXKEY_LAYER:
                throw new PdfXConformanceException(MessageLocalization.getComposedMessage("layers.are.not.allowed"));
        }
    }

    /**
     * @see org.openpdf.text.pdf.interfaces.PdfXConformance#getPDFXConformance()
     */
    public int getPDFXConformance() {
        return pdfxConformance;
    }

    /**
     * @see org.openpdf.text.pdf.interfaces.PdfXConformance#setPDFXConformance(int)
     */
    public void setPDFXConformance(int pdfxConformance) {
        this.pdfxConformance = pdfxConformance;
    }

    /**
     * Checks if the PDF/X Conformance is necessary.
     *
     * @return true if the PDF has to be in conformance with any of the PDF/X specifications
     */
    public boolean isPdfX() {
        return pdfxConformance != PdfWriter.PDFXNONE;
    }

    /**
     * Checks if the PDF has to be in conformance with PDF/X-1a:2001
     *
     * @return true of the PDF has to be in conformance with PDF/X-1a:2001
     */
    public boolean isPdfX1A2001() {
        return pdfxConformance == PdfWriter.PDFX1A2001;
    }

    /**
     * Checks if the PDF has to be in conformance with PDF/X-3:2002
     *
     * @return true of the PDF has to be in conformance with PDF/X-3:2002
     */
    public boolean isPdfX32002() {
        return pdfxConformance == PdfWriter.PDFX32002;
    }

    /**
     * Checks if the PDF has to be in conformance with PDFA1
     *
     * @return true of the PDF has to be in conformance with PDFA1
     */
    public boolean isPdfA1() {
        return pdfxConformance == PdfWriter.PDFA1A || pdfxConformance == PdfWriter.PDFA1B;
    }

    /**
     * Checks if the PDF has to be in conformance with PDFA1A
     *
     * @return true of the PDF has to be in conformance with PDFA1A
     */
    public boolean isPdfA1A() {
        return pdfxConformance == PdfWriter.PDFA1A;
    }

    /**
     * Checks if the PDF has to be in conformance with PDFA2
     *
     * @return true if the PDF has to be in conformance with PDFA2
     */
    public boolean isPdfA2() {
        return pdfxConformance == PdfWriter.PDFA2A
                || pdfxConformance == PdfWriter.PDFA2B
                || pdfxConformance == PdfWriter.PDFA2U;
    }

    /**
     * Checks if the PDF has to be in conformance with PDFA3
     *
     * @return true if the PDF has to be in conformance with PDFA3
     */
    public boolean isPdfA3() {
        return pdfxConformance == PdfWriter.PDFA3A
                || pdfxConformance == PdfWriter.PDFA3B
                || pdfxConformance == PdfWriter.PDFA3U;
    }

    /**
     * Checks if the PDF has to be in conformance with any PDF/A version
     *
     * @return true if the PDF has to be in conformance with any PDF/A version
     */
    public boolean isPdfA() {
        return isPdfA1() || isPdfA2() || isPdfA3();
    }

    public void completeInfoDictionary(PdfDictionary info) {
        if (isPdfX() && !isPdfA()) {
            if (info.get(PdfName.GTS_PDFXVERSION) == null) {
                if (isPdfX1A2001()) {
                    info.put(PdfName.GTS_PDFXVERSION, new PdfString("PDF/X-1:2001"));
                    info.put(new PdfName("GTS_PDFXConformance"), new PdfString("PDF/X-1a:2001"));
                } else if (isPdfX32002()) {
                    info.put(PdfName.GTS_PDFXVERSION, new PdfString("PDF/X-3:2002"));
                }
            }
            if (info.get(PdfName.TITLE) == null) {
                info.put(PdfName.TITLE, new PdfString("Pdf document"));
            }
            if (info.get(PdfName.CREATOR) == null) {
                info.put(PdfName.CREATOR, new PdfString("Unknown"));
            }
            if (info.get(PdfName.TRAPPED) == null) {
                info.put(PdfName.TRAPPED, new PdfName("False"));
            }
        }
    }

    public void completeExtraCatalog(PdfDictionary extraCatalog) {
        if (isPdfX() && !isPdfA()) {
            if (extraCatalog.get(PdfName.OUTPUTINTENTS) == null) {
                PdfDictionary out = new PdfDictionary(PdfName.OUTPUTINTENT);
                out.put(PdfName.OUTPUTCONDITION, new PdfString("SWOP CGATS TR 001-1995"));
                out.put(PdfName.OUTPUTCONDITIONIDENTIFIER, new PdfString("CGATS TR 001"));
                out.put(PdfName.REGISTRYNAME, new PdfString("http://www.color.org"));
                out.put(PdfName.INFO, new PdfString(""));
                out.put(PdfName.S, PdfName.GTS_PDFX);
                extraCatalog.put(PdfName.OUTPUTINTENTS, new PdfArray(out));
            }
        }
    }
}
