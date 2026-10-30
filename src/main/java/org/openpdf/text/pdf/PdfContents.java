/*
 * $Id: PdfContents.java 3634 2008-12-23 18:44:04Z xlv $
 *
 * Copyright 1999, 2000, 2001, 2002 Bruno Lowagie
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

import org.openpdf.text.DocWriter;
import org.openpdf.text.Document;
import org.openpdf.text.Rectangle;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

/**
 * <CODE>PdfContents</CODE> is a <CODE>PdfStream</CODE> containing the contents (text + graphics) of a
 * <CODE>PdfPage</CODE>.
 */

class PdfContents extends PdfStream {

    static final byte[] SAVESTATE = DocWriter.getISOBytes("q\n");
    static final byte[] RESTORESTATE = DocWriter.getISOBytes("Q\n");
    static final byte[] ROTATE90 = DocWriter.getISOBytes("0 1 -1 0 ");
    static final byte[] ROTATE180 = DocWriter.getISOBytes("-1 0 0 -1 ");
    static final byte[] ROTATE270 = DocWriter.getISOBytes("0 -1 1 0 ");
    static final byte[] ROTATEFINAL = DocWriter.getISOBytes(" cm\n");
    // constructor

    /**
     * Constructs a <CODE>PdfContents</CODE>-object, containing text and general graphics.
     *
     * @param under         the direct content that is under all others
     * @param content       the graphics in a page
     * @param text          the text in a page
     * @param secondContent the direct content that is over all others
     * @throws BadPdfFormatException on error
     */

    PdfContents(PdfContentByte under, PdfContentByte content, PdfContentByte text, PdfContentByte secondContent,
            Rectangle page) throws BadPdfFormatException {
        super();
        try {
            PdfWriter writer = content.getPdfWriter();
            streamBytes = new ByteArrayOutputStream();

            OutputStream out;
            Deflater deflater = null;
            if (Document.compress) {
                compressed = true;
                compressionLevel = writer.getCompressionLevel();
                deflater = new Deflater(compressionLevel);
                out = new DeflaterOutputStream(streamBytes, deflater);
            } else {
                out = streamBytes;
            }

            int rotation = page.getRotation();
            switch (rotation) {
                case 90:
                    out.write(ROTATE90);
                    out.write(DocWriter.getISOBytes(ByteBuffer.formatDouble(page.getTop())));
                    out.write(' ');
                    out.write('0');
                    out.write(ROTATEFINAL);
                    break;
                case 180:
                    out.write(ROTATE180);
                    out.write(DocWriter.getISOBytes(ByteBuffer.formatDouble(page.getRight())));
                    out.write(' ');
                    out.write(DocWriter.getISOBytes(ByteBuffer.formatDouble(page.getTop())));
                    out.write(ROTATEFINAL);
                    break;
                case 270:
                    out.write(ROTATE270);
                    out.write('0');
                    out.write(' ');
                    out.write(DocWriter.getISOBytes(ByteBuffer.formatDouble(page.getRight())));
                    out.write(ROTATEFINAL);
                    break;
            }
            if (under.size() > 0) {
                out.write(SAVESTATE);
                under.getInternalBuffer().writeTo(out);
                out.write(RESTORESTATE);
            }
            if (content.size() > 0) {
                out.write(SAVESTATE);
                content.getInternalBuffer().writeTo(out);
                out.write(RESTORESTATE);
            }
            if (text != null) {
                out.write(SAVESTATE);
                text.getInternalBuffer().writeTo(out);
                out.write(RESTORESTATE);
            }
            if (secondContent.size() > 0) {
                secondContent.getInternalBuffer().writeTo(out);
            }
            out.close();
            if (deflater != null) {
                deflater.end();
            }

            put(PdfName.LENGTH, new PdfNumber(streamBytes.size()));
            if (compressed) {
                put(PdfName.FILTER, PdfName.FLATEDECODE);
            }
        } catch (Exception e) {
            throw new BadPdfFormatException(e.getMessage());
        }
    }
}
