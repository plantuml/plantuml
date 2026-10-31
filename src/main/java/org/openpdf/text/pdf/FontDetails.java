/*
 * $Id: FontDetails.java 4024 2009-07-12 00:33:57Z xlv $
 *
 * Copyright 2001, 2002 by Paulo Soares.
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

import org.openpdf.text.ExceptionConverter;
import org.openpdf.text.TextRenderingOptions;

/**
 * Each font used in a document has an instance of this class. This class stores the characters used in the document
 * and other specifics unique to the current working document.
 * <p>
 * This pruned version only supports the single byte fonts (Type1, the 14 built-in fonts).
 */
class FontDetails {

    /**
     * Indicates if all the glyphs and widths for that particular encoding should be included in the document.
     */
    protected boolean subset = true;
    /**
     * The indirect reference to this font
     */
    PdfIndirectReference indirectReference;
    /**
     * The font name that appears in the document body stream
     */
    PdfName fontName;
    /**
     * The font
     */
    BaseFont baseFont;
    /**
     * The array used with single byte encodings
     */
    byte[] shortTag;
    /**
     * The font type
     */
    int fontType;

    /**
     * Each font used in a document has an instance of this class.
     *
     * @param fontName          the font name
     * @param indirectReference the indirect reference to the font
     * @param baseFont          the <CODE>BaseFont</CODE>
     */
    FontDetails(PdfName fontName, PdfIndirectReference indirectReference, BaseFont baseFont) {
        this.fontName = fontName;
        this.indirectReference = indirectReference;
        this.baseFont = baseFont;
        fontType = baseFont.getFontType();
        shortTag = new byte[256];
    }

    /**
     * Gets the indirect reference to this font.
     *
     * @return the indirect reference to this font
     */
    PdfIndirectReference getIndirectReference() {
        return indirectReference;
    }

    /**
     * Gets the font name as it appears in the document body.
     *
     * @return the font name
     */
    PdfName getFontName() {
        return fontName;
    }

    /**
     * Gets the <CODE>BaseFont</CODE> of this <CODE>PdfDocument</CODE>.
     *
     * @return the <CODE>BaseFont</CODE> of this <CODE>PdfDocument</CODE>
     */
    BaseFont getBaseFont() {
        return baseFont;
    }

    /**
     * Converts the text into bytes to be placed in the document. The conversion is done according to the font and the
     * encoding and the characters used are stored.
     *
     * @param text the text to convert
     * @return the conversion
     */
    byte[] convertToBytes(String text, TextRenderingOptions options) {
        byte[] b = baseFont.convertToBytes(text);
        for (byte b1 : b) {
            shortTag[b1 & 0xff] = 1;
        }
        return b;
    }

    /**
     * Writes the font definition to the document.
     *
     * @param writer the <CODE>PdfWriter</CODE> of this document
     */
    void writeFont(PdfWriter writer) {
        try {
            int firstChar;
            int lastChar;
            for (firstChar = 0; firstChar < 256; ++firstChar) {
                if (shortTag[firstChar] != 0) {
                    break;
                }
            }
            for (lastChar = 255; lastChar >= firstChar; --lastChar) {
                if (shortTag[lastChar] != 0) {
                    break;
                }
            }
            if (firstChar > 255) {
                firstChar = 255;
                lastChar = 255;
            }
            baseFont.writeFont(writer, indirectReference, new Object[]{firstChar, lastChar, shortTag, subset});
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

}
