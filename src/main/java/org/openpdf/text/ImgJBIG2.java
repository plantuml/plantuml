/*
 * $Id: ImgJBIG2.java 3962 2009-06-10 11:43:19Z psoares33 $
 *
 * Copyright 2009 by Nigel Kerr.
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
 * the Initial Developer are Copyright (C) 1999-2009 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000-2009 by Paulo Soares. All Rights Reserved.
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

import java.net.URL;
import java.security.MessageDigest;

/**
 * Support for JBIG2 images.
 *
 * @since 2.1.5
 */
public class ImgJBIG2 extends Image {

    /**
     * JBIG2 globals
     */
    private byte[] global;
    /**
     * A unique hash
     */
    private byte[] globalHash;

    /**
     * Copy contstructor.
     *
     * @param image another Image
     */
    ImgJBIG2(Image image) {
        super(image);
    }

    /**
     * Empty constructor.
     */
    public ImgJBIG2() {
        super((Image) null);
    }

    /**
     * Actual constructor for ImgJBIG2 images.
     *
     * @param width   the width of the image
     * @param height  the height of the image
     * @param data    the raw image data
     * @param globals JBIG2 globals
     */
    public ImgJBIG2(int width, int height, byte[] data, byte[] globals) {
        super((URL) null);
        type = JBIG2;
        originalType = ORIGINAL_JBIG2;
        scaledHeight = height;
        setTop(scaledHeight);
        scaledWidth = width;
        setRight(scaledWidth);
        bpc = 1;
        colorspace = 1;
        rawData = data;
        plainWidth = getWidth();
        plainHeight = getHeight();
        if (globals != null) {
            this.global = globals;
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("MD5");
                md.update(this.global);
                this.globalHash = md.digest();
            } catch (Exception e) {
                //ignore
            }

        }
    }

    /**
     * Getter for the JBIG2 global data.
     *
     * @return an array of bytes
     */
    public byte[] getGlobalBytes() {
        return this.global;
    }

    /**
     * Getter for the unique hash.
     *
     * @return an array of bytes
     */
    public byte[] getGlobalHash() {
        return this.globalHash;
    }

}
