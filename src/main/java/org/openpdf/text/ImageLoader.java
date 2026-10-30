/*
 *
 * Copyright 2018 Andreas Rosdal
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
 */

package org.openpdf.text;

import org.openpdf.text.utils.UrlStreams;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 * Loads image files such as PNG, JPEG, GIF, TIFF and BMP.
 * <p>
 * TODO: The goal of this class is to use Java ImageIO to parse images and metadata,
 * and embed the image in the PDF in the best way (the compressed image format, not the raw pixels).
 * <p>
 * <p>
 * We don't want to maintain our own image codecs.
 *
 * @author Andreas Rosdal
 */
public class ImageLoader {

    /**
     * Creates an Image from a PNG image file in an URL.
     *
     * @param url url of the image
     * @return an object of type <code>Image</code>
     */
    public static Image getPngImage(URL url) {
        try (InputStream is = UrlStreams.open(url)) {
            BufferedImage bufferedImage = ImageIO.read(is);
            return getImage(bufferedImage, Image.ORIGINAL_PNG);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public static Image getGifImage(URL url) {
        try (InputStream is = UrlStreams.open(url)) {
            BufferedImage bufferedImage = ImageIO.read(is);
            return getImage(bufferedImage, Image.ORIGINAL_GIF);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public static Image getTiffImage(URL url) {
        try (InputStream is = UrlStreams.open(url)) {
            BufferedImage bufferedImage = ImageIO.read(is);
            return getImage(bufferedImage, Image.ORIGINAL_TIFF);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }


    public static Image getBmpImage(URL url) {
        try (InputStream is = UrlStreams.open(url)) {
            BufferedImage bufferedImage = ImageIO.read(is);
            return getImage(bufferedImage, Image.ORIGINAL_BMP);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    /**
     * Creates an Image from a JPEG image file in an URL.
     *
     * @param url url of the image
     * @return an object of type <code>Image</code>
     */
    public static Image getJpegImage(URL url) {
        try (InputStream is = UrlStreams.open(url)) {
            byte[] imageBytes = Utilities.toByteArray(is);
            return new Jpeg(imageBytes);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public static Image getJpeg2000Image(URL url) {
        try (InputStream is = UrlStreams.open(url)) {
            byte[] imageBytes = Utilities.toByteArray(is);
            return new Jpeg2000(imageBytes);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public static Image getGifImage(byte[] imageData) {
        try (InputStream is = new ByteArrayInputStream(imageData)) {
            BufferedImage bufferedImage = ImageIO.read(is);
            return getImage(bufferedImage, Image.ORIGINAL_GIF);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public static Image getPngImage(byte[] imageData) {
        try (InputStream is = new ByteArrayInputStream(imageData)) {
            BufferedImage bufferedImage = ImageIO.read(is);
            return getImage(bufferedImage, Image.ORIGINAL_PNG);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public static Image getBmpImage(byte[] imageData) {
        try (InputStream is = new ByteArrayInputStream(imageData)) {
            BufferedImage bufferedImage = ImageIO.read(is);
            return getImage(bufferedImage, Image.ORIGINAL_BMP);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    /**
     * Creates an Image from an array of tiff image bytes.
     *
     * @param imageData bytes of the tiff image
     * @return an objet of type <code>Image</code>
     */
    public static Image getTiffImage(byte[] imageData) {
        try (InputStream is = new ByteArrayInputStream(imageData)) {
            BufferedImage bufferedImage = ImageIO.read(is);
            return getImage(bufferedImage, Image.ORIGINAL_TIFF);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    /**
     * Creates an Image from a JPEG image file in a byte array.
     *
     * @param imageData bytes of the image
     * @return an object of type <code>Image</code>
     */
    public static Image getJpegImage(byte[] imageData) {
        try {
            return new Jpeg(imageData);

        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public static Image getJpeg2000Image(byte[] imageData) {
        try {
            return new Jpeg2000(imageData);

        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    private static Image getImage(BufferedImage bufferedImage, int originalType) throws IOException {
        Image image = Image.getInstance(bufferedImage, null, false);
        image.setOriginalType(originalType);
        return image;
    }
}
