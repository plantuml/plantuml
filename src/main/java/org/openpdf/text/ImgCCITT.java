/*
 * $Id: ImgCCITT.java 4065 2009-09-16 23:09:11Z psoares33 $
 * $Name$
 *
 * Copyright 2000, 2001, 2002 by Paulo Soares.
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

package org.openpdf.text;

import org.openpdf.text.error_messages.MessageLocalization;
import java.net.URL;

/**
 * CCITT Image data that has to be inserted into the document
 *
 * @author Paulo Soares
 * @see Element
 * @see Image
 */

public class ImgCCITT extends Image {

    ImgCCITT(Image image) {
        super(image);
    }

    /**
     * Creates an Image with CCITT compression.
     *
     * @param width       the exact width of the image
     * @param height      the exact height of the image
     * @param reverseBits reverses the bits in <code>data</code>. Bit 0 is swapped with bit 7 and so on.
     * @param typeCCITT   the type of compression in <code>data</code>. It can be CCITTG4, CCITTG31D, CCITTG32D
     * @param parameters  parameters associated with this stream. Possible values are CCITT_BLACKIS1,
     *                    CCITT_ENCODEDBYTEALIGN, CCITT_ENDOFLINE and CCITT_ENDOFBLOCK or a combination of them
     * @param data        the image data
     * @throws BadElementException on error
     */

    public ImgCCITT(int width, int height, boolean reverseBits, int typeCCITT, int parameters, byte[] data)
            throws BadElementException {
        super((URL) null);
        if (typeCCITT != CCITTG4 && typeCCITT != CCITTG3_1D && typeCCITT != CCITTG3_2D) {
            throw new BadElementException(MessageLocalization.getComposedMessage(
                    "the.ccitt.compression.type.must.be.ccittg4.ccittg3.1d.or.ccittg3.2d"));
        }
        if (reverseBits) {
            throw new BadElementException("Reversing bits is not supported");
        }
        type = IMGRAW;
        scaledHeight = height;
        setTop(scaledHeight);
        scaledWidth = width;
        setRight(scaledWidth);
        colorspace = parameters;
        bpc = typeCCITT;
        rawData = data;
        plainWidth = getWidth();
        plainHeight = getHeight();
    }
}
