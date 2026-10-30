/*
 * $Id: PdfSpotColor.java 4069 2009-09-19 14:50:32Z psoares33 $
 *
 * Copyright 2002 by Phillip Pan
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

import org.openpdf.text.error_messages.MessageLocalization;
import java.awt.Color;

/**
 * A <CODE>PdfSpotColor</CODE> defines a ColorSpace
 *
 * @see PdfDictionary
 */

public class PdfSpotColor {

    /**
     * The color name
     */
    public PdfName name;

    /**
     * The alternative color space
     */
    public Color altcs;
    // constructors

    /**
     * Constructs a new <CODE>PdfSpotColor</CODE>.
     *
     * @param name  a String value
     * @param altcs an alternative colorspace value
     */

    public PdfSpotColor(String name, Color altcs) {
        this.name = new PdfName(name);
        this.altcs = altcs;
    }

    /**
     * Gets the alternative ColorSpace.
     *
     * @return a Color
     */
    public Color getAlternativeCS() {
        return altcs;
    }

    protected PdfObject getSpotObject(PdfWriter writer) {
        PdfArray array = new PdfArray(PdfName.SEPARATION);
        array.add(name);
        PdfFunction func = null;
        if (altcs instanceof ExtendedColor) {
            int type = ((ExtendedColor) altcs).type;
            switch (type) {
                case ExtendedColor.TYPE_GRAY:
                    array.add(PdfName.DEVICEGRAY);
                    func = PdfFunction.type2(writer, new float[]{0, 1}, null, new float[]{0},
                            new float[]{((GrayColor) altcs).getGray()}, 1);
                    break;
                case ExtendedColor.TYPE_CMYK:
                    array.add(PdfName.DEVICECMYK);
                    CMYKColor cmyk = (CMYKColor) altcs;
                    func = PdfFunction.type2(writer, new float[]{0, 1}, null, new float[]{0, 0, 0, 0},
                            new float[]{cmyk.getCyan(), cmyk.getMagenta(), cmyk.getYellow(), cmyk.getBlack()}, 1);
                    break;
                default:
                    throw new RuntimeException(MessageLocalization.getComposedMessage(
                            "only.rgb.gray.and.cmyk.are.supported.as.alternative.color.spaces"));
            }
        } else {
            array.add(PdfName.DEVICERGB);
            func = PdfFunction.type2(writer, new float[]{0, 1}, null, new float[]{1, 1, 1},
                    new float[]{(float) altcs.getRed() / 255, (float) altcs.getGreen() / 255,
                            (float) altcs.getBlue() / 255}, 1);
        }
        array.add(func.getReference());
        return array;
    }
}
