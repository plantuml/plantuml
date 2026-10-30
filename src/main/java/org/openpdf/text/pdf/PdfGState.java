/*
 * Copyright 2003 Paulo Soares
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
package org.openpdf.text.pdf;

/**
 * The graphic state dictionary.
 *
 * @author Paulo Soares (psoares@consiste.pt)
 */
public class PdfGState extends PdfDictionary {

    /**
     * A possible blend mode
     */
    public static final PdfName BM_NORMAL = new PdfName("Normal");
    /**
     * A possible blend mode
     */
    public static final PdfName BM_COMPATIBLE = new PdfName("Compatible");
    /**
     * A possible blend mode
     */
    public static final PdfName BM_MULTIPLY = new PdfName("Multiply");
    /**
     * A possible blend mode
     */
    public static final PdfName BM_SCREEN = new PdfName("Screen");
    /**
     * A possible blend mode
     */
    public static final PdfName BM_OVERLAY = new PdfName("Overlay");
    /**
     * A possible blend mode
     */
    public static final PdfName BM_DARKEN = new PdfName("Darken");
    /**
     * A possible blend mode
     */
    public static final PdfName BM_LIGHTEN = new PdfName("Lighten");
    /**
     * A possible blend mode
     */
    public static final PdfName BM_COLORDODGE = new PdfName("ColorDodge");
    /**
     * A possible blend mode
     */
    public static final PdfName BM_COLORBURN = new PdfName("ColorBurn");
    /**
     * A possible blend mode
     */
    public static final PdfName BM_HARDLIGHT = new PdfName("HardLight");
    /**
     * A possible blend mode
     */
    public static final PdfName BM_SOFTLIGHT = new PdfName("SoftLight");
    /**
     * A possible blend mode
     */
    public static final PdfName BM_DIFFERENCE = new PdfName("Difference");
    /**
     * A possible blend mode
     */
    public static final PdfName BM_EXCLUSION = new PdfName("Exclusion");

    /**
     * Sets the flag whether to apply overprint for stroking.
     *
     * @param ov true to apply overprint for stroking false otherwise
     */
    public void setOverPrintStroking(boolean ov) {
        put(PdfName.OP, ov ? PdfBoolean.PDFTRUE : PdfBoolean.PDFFALSE);
    }

    /**
     * Sets the flag whether to apply overprint for non stroking painting operations.
     *
     * @param ov true to apply overprint for non stroking painting operations false otherwise
     */
    public void setOverPrintNonStroking(boolean ov) {
        put(PdfName.op, ov ? PdfBoolean.PDFTRUE : PdfBoolean.PDFFALSE);
    }

    /**
     * Sets the flag whether to toggle knockout behavior for overprinted objects.
     *
     * @param ov - accepts 0 or 1
     */
    public void setOverPrintMode(int ov) {
        put(PdfName.OPM, new PdfNumber(ov == 0 ? 0 : 1));
    }

    /**
     * Sets the current stroking alpha constant, specifying the constant shape or constant opacity value to be used for
     * stroking operations in the transparent imaging model.
     *
     * @param n the stroking alpha constant
     */
    public void setStrokeOpacity(float n) {
        put(PdfName.CA, new PdfNumber(n));
    }

    /**
     * Sets the current fill alpha constant, specifying the constant shape or constant opacity value to be used for
     * filling operations in the transparent imaging model.
     *
     * @param n the fll alpha constant
     */
    public void setFillOpacity(float n) {
        put(PdfName.ca, new PdfNumber(n));
    }

    /**
     * The alpha source flag specifying whether the current soft mask and alpha constant are to be interpreted as shape
     * values (true) or opacity values (false).
     *
     * @param v true if the alpha source flag and alpha constant should be interpreted as shape value false if they
     *          should be interpreted as opacity values
     */
    public void setAlphaIsShape(boolean v) {
        put(PdfName.AIS, v ? PdfBoolean.PDFTRUE : PdfBoolean.PDFFALSE);
    }

    /**
     * Determines the behavior of overlapping glyphs within a text object in the transparent imaging model.
     *
     * @param v boolean for overlapping glyphs behavior
     */
    public void setTextKnockout(boolean v) {
        put(PdfName.TK, v ? PdfBoolean.PDFTRUE : PdfBoolean.PDFFALSE);
    }

    /**
     * The current blend mode to be used in the transparent imaging model.
     *
     * @param bm the blend mode
     */
    public void setBlendMode(PdfName bm) {
        put(PdfName.BM, bm);
    }

}
