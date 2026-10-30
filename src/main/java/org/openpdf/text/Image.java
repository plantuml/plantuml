/*
 * $Id: Image.java 4065 2009-09-16 23:09:11Z psoares33 $
 *
 * Copyright 1999, 2000, 2001, 2002 by Bruno Lowagie.
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

import org.openpdf.text.utils.UrlStreams;
import org.openpdf.text.error_messages.MessageLocalization;
import org.openpdf.text.pdf.PdfArray;
import org.openpdf.text.pdf.PdfContentByte;
import org.openpdf.text.pdf.PdfDictionary;
import org.openpdf.text.pdf.PdfIndirectReference;
import org.openpdf.text.pdf.PdfName;
import org.openpdf.text.pdf.PdfNumber;
import org.openpdf.text.pdf.PdfObject;
import org.openpdf.text.pdf.PdfStream;
import org.openpdf.text.pdf.PdfString;
import org.openpdf.text.pdf.PdfTemplate;
import org.openpdf.text.pdf.PdfWriter;
import java.awt.Graphics2D;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;


/**
 * An <CODE>Image</CODE> is the representation of a graphic element (JPEG, PNG or GIF) that has to be inserted into the
 * document
 *
 * Security consideration: This method accepts input and processes it without built-in validation,
 * filtering, or access restrictions. It may load resources such as local files or external URLs,
 * depending on the input. This can introduce security risks including unauthorized file access,
 * directory traversal, or server-side request forgery (SSRF).
 *
 * It is the responsibility of the calling application to validate and sanitize all input
 * before passing it to this method, especially when handling user-controlled or external data.
 * Refer to established secure coding guidelines to ensure safe usage.
 *
 * @see Element
 * @see Rectangle
 */

public abstract class Image extends Rectangle {

    /**
     * Image alignment: Default alignment (same as LEFT).
     * The image will be placed on the left side of the page.
     */
    public static final int DEFAULT = 0;

    /**
     * Image alignment: Right-align the image.
     * The image will be placed on the right side of the page.
     * Can be combined with TEXTWRAP or UNDERLYING using bitwise OR.
     * Example: {@code image.setAlignment(Image.RIGHT | Image.TEXTWRAP);}
     */
    public static final int RIGHT = 2;

    /**
     * Image alignment: Left-align the image.
     * The image will be placed on the left side of the page.
     * Can be combined with TEXTWRAP or UNDERLYING using bitwise OR.
     * Example: {@code image.setAlignment(Image.LEFT | Image.TEXTWRAP);}
     */
    public static final int LEFT = 0;

    /**
     * Image alignment: Center the image horizontally on the page.
     * The image will be placed in the middle of the page width.
     */
    public static final int MIDDLE = 1;

    /**
     * Image alignment modifier: Allow text to wrap around the image.
     * When combined with LEFT or RIGHT, text will flow around the image on the opposite side.
     * This is useful for creating inline images where text continues alongside the image
     * instead of the image occupying its own line.
     * <p>
     * Example for an image with text wrapping on the right:
     * {@code image.setAlignment(Image.LEFT | Image.TEXTWRAP);}
     * <p>
     * Example for an image with text wrapping on the left:
     * {@code image.setAlignment(Image.RIGHT | Image.TEXTWRAP);}
     */
    public static final int TEXTWRAP = 4;

    /**
     * Image alignment modifier: Place the image behind (underneath) the text.
     * When combined with other alignment flags, the image will be rendered as a background
     * element with text overlaying it. This is useful for watermarks, seals, or stamps
     * that should appear behind text content.
     * <p>
     * Example for a background image on the left:
     * {@code image.setAlignment(Image.LEFT | Image.UNDERLYING);}
     * <p>
     * Example for a seal that appears behind text:
     * {@code image.setAlignment(Image.RIGHT | Image.UNDERLYING);}
     */
    public static final int UNDERLYING = 8;

    /**
     * This represents a coordinate in the transformation matrix.
     */
    public static final int AX = 0;

    /**
     * This represents a coordinate in the transformation matrix.
     */
    public static final int AY = 1;

    /**
     * This represents a coordinate in the transformation matrix.
     */
    public static final int BX = 2;

    /**
     * This represents a coordinate in the transformation matrix.
     */
    public static final int BY = 3;

    /**
     * This represents a coordinate in the transformation matrix.
     */
    public static final int CX = 4;

    /**
     * This represents a coordinate in the transformation matrix.
     */
    public static final int CY = 5;

    /**
     * This represents a coordinate in the transformation matrix.
     */
    public static final int DX = 6;

    /**
     * This represents a coordinate in the transformation matrix.
     */
    public static final int DY = 7;

    /**
     * type of image
     */
    public static final int ORIGINAL_NONE = 0;

    /**
     * type of image
     */
    public static final int ORIGINAL_JPEG = 1;

    /**
     * type of image
     */
    public static final int ORIGINAL_PNG = 2;

    /**
     * type of image
     */
    public static final int ORIGINAL_GIF = 3;

    /**
     * type of image
     */
    public static final int ORIGINAL_BMP = 4;

    /**
     * type of image
     */
    public static final int ORIGINAL_TIFF = 5;

    /**
     * type of image
     */
    public static final int ORIGINAL_WMF = 6;

    /**
     * type of image
     */
    public static final int ORIGINAL_PS = 7;

    /**
     * type of image
     */
    public static final int ORIGINAL_JPEG2000 = 8;

    /**
     * type of image
     *
     * @since 2.1.5
     */
    public static final int ORIGINAL_JBIG2 = 9;

    // member variables
    public static final int[] PNGID = {137, 80, 78, 71, 13, 10, 26, 10};
    private static final int INDEXED_NO_TRANSPARENCY = -1;
    private static final int INDEXED_UNSUPPORTED_TRANSPARENCY = -2;
    /**
     * a static that is used for attributing a unique id to each image.
     */
    static long serialId = 0;
    /**
     * The image type.
     */
    protected int type;
    /**
     * The URL of the image.
     */
    protected URL url;
    /**
     * The raw data of the image.
     */
    protected byte[] rawData;
    /**
     * The bits per component of the raw image. It also flags a CCITT image.
     */
    protected int bpc = 1;
    /**
     * The template to be treated as an image.
     */
    protected PdfTemplate[] template = new PdfTemplate[1];
    /**
     * The alignment of the Image.
     */
    protected int alignment;
    /**
     * Text that can be shown instead of the image.
     */
    protected String alt;
    /**
     * This is the absolute X-position of the image.
     */
    protected float absoluteX = Float.NaN;
    /**
     * This is the absolute Y-position of the image.
     */
    protected float absoluteY = Float.NaN;
    /**
     * This is the width of the image without rotation.
     */
    protected float plainWidth;
    /**
     * This is the width of the image without rotation.
     */
    protected float plainHeight;
    /**
     * This is the scaled width of the image taking rotation into account.
     */
    protected float scaledWidth;
    /**
     * This is the original height of the image taking rotation into account.
     */
    protected float scaledHeight;
    /**
     * The compression level of the content streams.
     *
     * @since 2.1.3
     */
    protected int compressionLevel = PdfStream.DEFAULT_COMPRESSION;

    // image from file or URL
    /**
     * an iText attributed unique id for this image.
     */
    protected Long mySerialId = getSerialId();
    /**
     * This is the rotation of the image in radians.
     */
    protected float rotationRadians;
    /**
     * the indentation to the left.
     */
    protected float indentationLeft = 0;
    /**
     * the indentation to the right.
     */
    protected float indentationRight = 0;
    /**
     * The spacing before the image.
     */
    protected float spacingBefore;
    /**
     * The spacing after the image.
     */
    protected float spacingAfter;
    /**
     * if the annotation is not null the image will be clickable.
     */
    protected Annotation annotation = null;
    /**
     * Holds value of property interpolation.
     */
    protected boolean interpolation;

    // images from a PdfTemplate
    /**
     * Holds value of property originalType.
     */
    protected int originalType = ORIGINAL_NONE;

    // images from a java.awt.Image
    /**
     * Holds value of property originalData.
     */
    protected byte[] originalData;
    /**
     * Holds value of property deflated.
     */
    protected boolean deflated = false;
    /**
     * Holds value of property dpiX.
     */
    protected int dpiX = 0;
    /**
     * Holds value of property dpiY.
     */
    protected int dpiY = 0;

    // image from indirect reference
    /**
     * this is the colorspace of a jpeg-image.
     */
    protected int colorspace = -1;
    /**
     * Image color inversion
     */
    protected boolean invert = false;
    /**
     * ICC Profile attached
     */
    protected ICC_Profile profile = null;
    /**
     * Is this image a mask?
     */
    protected boolean mask = false;

    // copy constructor
    /**
     * The image that serves as a mask for this image.
     */
    protected Image imageMask;
    /**
     * this is the transparency information of the raw image
     */
    protected int[] transparency;

    // implementation of the Element interface
    /**
     * Holds value of property directReference. An image is embedded into a PDF as an Image XObject. This object is
     * referenced by a PdfIndirectReference object.
     */
    private PdfIndirectReference directReference;
    /**
     * Holds value of property initialRotation.
     */
    private float initialRotation;

    // checking the type of Image
    /**
     * Holds value of property widthPercentage.
     */
    private float widthPercentage = 100;
    /**
     * Holds value of property XYRatio.
     */
    private float XYRatio = 0;
    /**
     * a dictionary with additional information
     */
    private PdfDictionary additional = null;

    // getters and setters
    /**
     * Holds value of property smask.
     */
    private boolean smask;

    /**
     * Constructs an <CODE>Image</CODE> -object, using an <VAR>url </VAR>.
     *
     * @param url the <CODE>URL</CODE> where the image can be found.
     */
    public Image(URL url) {
        super(0, 0);
        this.url = url;
        this.alignment = DEFAULT;
        rotationRadians = 0;
    }

    /**
     * Constructs an <CODE>Image</CODE> -object, using an <VAR>url </VAR>.
     *
     * @param image another Image object.
     */
    protected Image(Image image) {
        super(image);
        this.type = image.type;
        this.url = image.url;
        this.rawData = image.rawData;
        this.bpc = image.bpc;
        this.template = image.template;
        this.alignment = image.alignment;
        this.alt = image.alt;
        this.absoluteX = image.absoluteX;
        this.absoluteY = image.absoluteY;
        this.plainWidth = image.plainWidth;
        this.plainHeight = image.plainHeight;
        this.scaledWidth = image.scaledWidth;
        this.scaledHeight = image.scaledHeight;
        this.mySerialId = image.mySerialId;

        this.directReference = image.directReference;

        this.rotationRadians = image.rotationRadians;
        this.initialRotation = image.initialRotation;
        this.indentationLeft = image.indentationLeft;
        this.indentationRight = image.indentationRight;
        this.spacingBefore = image.spacingBefore;
        this.spacingAfter = image.spacingAfter;

        this.widthPercentage = image.widthPercentage;
        this.annotation = image.annotation;
        this.interpolation = image.interpolation;
        this.originalType = image.originalType;
        this.originalData = image.originalData;
        this.deflated = image.deflated;
        this.dpiX = image.dpiX;
        this.dpiY = image.dpiY;
        this.XYRatio = image.XYRatio;

        this.colorspace = image.colorspace;
        this.invert = image.invert;
        this.profile = image.profile;
        this.additional = image.additional;
        this.mask = image.mask;
        this.imageMask = image.imageMask;
        this.smask = image.smask;
        this.transparency = image.transparency;
    }

    /**
     * Gets an instance of an Image in raw mode.
     *
     * @param width      the width of the image in pixels
     * @param height     the height of the image in pixels
     * @param components 1,3 or 4 for GrayScale, RGB and CMYK
     * @param data       the image data
     * @param bpc        bits per component
     * @return an object of type <CODE>ImgRaw</CODE>
     * @throws BadElementException on error
     */
    public static Image getInstance(int width, int height, int components,
            int bpc, byte[] data) throws BadElementException {
        return Image.getInstance(width, height, components, bpc, data, null);
    }

    /**
     * Creates a JBIG2 Image.
     *
     * @param width   the width of the image
     * @param height  the height of the image
     * @param data    the raw image data
     * @param globals JBIG2 globals
     * @return an <code>Image</code> Object
     * @since 2.1.5
     */
    public static Image getInstance(int width, int height, byte[] data, byte[] globals) {
        Image img = new ImgJBIG2(width, height, data, globals);
        return img;
    }

    /**
     * Creates an Image with CCITT G3 or G4 compression. It assumes that the data bytes are already compressed.
     *
     * @param width       the exact width of the image
     * @param height      the exact height of the image
     * @param reverseBits reverses the bits in <code>data</code>. Bit 0 is swapped with bit 7 and so on
     * @param typeCCITT   the type of compression in <code>data</code>. It can be CCITTG4, CCITTG31D, CCITTG32D
     * @param parameters  parameters associated with this stream. Possible values are CCITT_BLACKIS1,
     *                    CCITT_ENCODEDBYTEALIGN, CCITT_ENDOFLINE and CCITT_ENDOFBLOCK or a combination of them
     * @param data        the image data
     * @return an Image object
     * @throws BadElementException on error
     */
    public static Image getInstance(int width, int height, boolean reverseBits,
            int typeCCITT, int parameters, byte[] data)
            throws BadElementException {
        return Image.getInstance(width, height, reverseBits, typeCCITT,
                parameters, data, null);
    }

    /**
     * Creates an Image with CCITT G3 or G4 compression. It assumes that the data bytes are already compressed.
     *
     * @param width        the exact width of the image
     * @param height       the exact height of the image
     * @param reverseBits  reverses the bits in <code>data</code>. Bit 0 is swapped with bit 7 and so on
     * @param typeCCITT    the type of compression in <code>data</code>. It can be CCITTG4, CCITTG31D, CCITTG32D
     * @param parameters   parameters associated with this stream. Possible values are CCITT_BLACKIS1,
     *                     CCITT_ENCODEDBYTEALIGN, CCITT_ENDOFLINE and CCITT_ENDOFBLOCK or a combination of them
     * @param data         the image data
     * @param transparency transparency information in the Mask format of the image dictionary
     * @return an Image object
     * @throws BadElementException on error
     */
    public static Image getInstance(int width, int height, boolean reverseBits,
            int typeCCITT, int parameters, byte[] data, int[] transparency)
            throws BadElementException {
        if (transparency != null && transparency.length != 2) {
            throw new BadElementException(
                    MessageLocalization.getComposedMessage("transparency.length.must.be.equal.to.2.with.ccitt.images"));
        }
        Image img = new ImgCCITT(width, height, reverseBits, typeCCITT,
                parameters, data);
        img.transparency = transparency;
        return img;
    }

    /**
     * gets an instance of an Image
     *
     * @param template a PdfTemplate that has to be wrapped in an <code>Image</code> object
     * @return an Image object
     * @throws BadElementException on error
     */
    public static Image getInstance(PdfTemplate template)
            throws BadElementException {
        return new ImgTemplate(template);
    }

    /**
     * Gets an instance of an Image from a java.awt.Image.
     *
     * @param image   the <CODE>java.awt.Image</CODE> to convert
     * @param color   if different from <CODE>null</CODE> the transparency pixels are replaced by this color
     * @param forceBW if <CODE>true</CODE> the image is treated as black and white
     * @return an object of type <CODE>ImgRaw</CODE>
     * @throws BadElementException on error
     * @throws IOException         on error
     */
    public static Image getInstance(java.awt.Image image, java.awt.Color color,
            boolean forceBW) throws BadElementException, IOException {

        if (image instanceof BufferedImage) {
            BufferedImage bi = (BufferedImage) image;
            if (bi.getType() == BufferedImage.TYPE_BYTE_BINARY && bi.getColorModel().getNumColorComponents() <= 2) {
                forceBW = true;
            }

            // Handle indexed color images when transparency can be represented directly.
            // More complex alpha (for example semi-transparency) must fallback to the generic RGB+SMask path below
            if (bi.getColorModel() instanceof IndexColorModel icm && !forceBW && color == null) {
                int transparentIndex = getIndexedTransparentPaletteIndex(icm);
                if (transparentIndex != INDEXED_UNSUPPORTED_TRANSPARENCY) {
                    int mapSize = icm.getMapSize();
                    int bitsPerPixel = icm.getPixelSize();

                    // Ensure bits per pixel is valid (1, 2, 4, or 8)
                    // For PDF indexed images, bpc should be the bits needed to index the palette
                    if (bitsPerPixel > 8 || bitsPerPixel == 0) {
                        bitsPerPixel = 8;
                    } else if (bitsPerPixel > 4) {
                        bitsPerPixel = 8;
                    } else if (bitsPerPixel > 2) {
                        bitsPerPixel = 4;
                    } else if (bitsPerPixel > 1) {
                        bitsPerPixel = 2;
                    } else {
                        bitsPerPixel = 1;
                    }

                    // Extract palette data
                    byte[] reds = new byte[mapSize];
                    byte[] greens = new byte[mapSize];
                    byte[] blues = new byte[mapSize];
                    icm.getReds(reds);
                    icm.getGreens(greens);
                    icm.getBlues(blues);

                    // Build palette as RGB byte array
                    byte[] palette = new byte[mapSize * 3];
                    for (int i = 0; i < mapSize; i++) {
                        palette[i * 3] = reds[i];
                        palette[i * 3 + 1] = greens[i];
                        palette[i * 3 + 2] = blues[i];
                    }

                    // Extract pixel indices
                    int width = bi.getWidth();
                    int height = bi.getHeight();
                    byte[] pixelData = generateIndexedColorPixelData(width, bitsPerPixel, height, bi.getRaster());
                    // Create indexed image with palette
                    Image img = Image.getInstance(width, height, 1, bitsPerPixel, pixelData);

                    // Set up indexed colorspace: [/Indexed /DeviceRGB maxIndex palette]
                    PdfArray indexed = new PdfArray();
                    indexed.add(PdfName.INDEXED);
                    indexed.add(PdfName.DEVICERGB);
                    indexed.add(new PdfNumber(mapSize - 1));
                    indexed.add(new PdfString(palette));

                    PdfDictionary additional = new PdfDictionary();
                    additional.put(PdfName.COLORSPACE, indexed);
                    img.setAdditional(additional);
                    if (transparentIndex >= 0) {
                        img.setTransparency(new int[]{transparentIndex, transparentIndex});
                    }

                    return img;
                }
                // Unsupported indexed transparency falls through to the generic RGB+SMask path below.
            }
        }

        java.awt.image.PixelGrabber pg = new java.awt.image.PixelGrabber(image,
                0, 0, -1, -1, true);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // restore interrupted status
            throw new IOException(
                    MessageLocalization.getComposedMessage("java.awt.image.interrupted.waiting.for.pixels"), e);
        }

        if ((pg.getStatus() & java.awt.image.ImageObserver.ABORT) != 0) {
            throw new IOException(MessageLocalization.getComposedMessage("java.awt.image.fetch.aborted.or.errored"));
        }
        int w = pg.getWidth();
        int h = pg.getHeight();
        int[] pixels = (int[]) pg.getPixels();
        if (forceBW) {
            int byteWidth = (w / 8) + ((w & 7) != 0 ? 1 : 0);
            byte[] pixelsByte = new byte[byteWidth * h];

            int index = 0;
            int size = h * w;
            int transColor = 1;
            if (color != null) {
                transColor = (color.getRed() + color.getGreen()
                        + color.getBlue() < 384) ? 0 : 1;
            }
            int[] transparency = null;
            int cbyte = 0x80;
            int wMarker = 0;
            int currByte = 0;
            if (color != null) {
                for (int j = 0; j < size; j++) {
                    int alpha = (pixels[j] >> 24) & 0xff;
                    if (alpha < 250) {
                        if (transColor == 1) {
                            currByte |= cbyte;
                        }
                    } else {
                        if ((pixels[j] & 0x888) != 0) {
                            currByte |= cbyte;
                        }
                    }
                    cbyte >>= 1;
                    if (cbyte == 0 || wMarker + 1 >= w) {
                        pixelsByte[index++] = (byte) currByte;
                        cbyte = 0x80;
                        currByte = 0;
                    }
                    ++wMarker;
                    if (wMarker >= w) {
                        wMarker = 0;
                    }
                }
            } else {
                for (int j = 0; j < size; j++) {
                    if (transparency == null) {
                        int alpha = (pixels[j] >> 24) & 0xff;
                        if (alpha == 0) {
                            transparency = new int[2];
                            /* bugfix by M.P. Liston, ASC, was: ... ? 1: 0; */
                            transparency[0] = transparency[1] = ((pixels[j] & 0x888) != 0) ? 0xff : 0;
                        }
                    }
                    if ((pixels[j] & 0x888) != 0) {
                        currByte |= cbyte;
                    }
                    cbyte >>= 1;
                    if (cbyte == 0 || wMarker + 1 >= w) {
                        pixelsByte[index++] = (byte) currByte;
                        cbyte = 0x80;
                        currByte = 0;
                    }
                    ++wMarker;
                    if (wMarker >= w) {
                        wMarker = 0;
                    }
                }
            }
            return Image.getInstance(w, h, 1, 1, pixelsByte, transparency);
        } else {
            byte[] pixelsByte = new byte[w * h * 3];
            byte[] smask = null;

            int index = 0;
            int size = h * w;
            int red = 255;
            int green = 255;
            int blue = 255;
            if (color != null) {
                red = color.getRed();
                green = color.getGreen();
                blue = color.getBlue();
            }
            int[] transparency = null;
            if (color != null) {
                for (int j = 0; j < size; j++) {
                    int alpha = (pixels[j] >> 24) & 0xff;
                    if (alpha < 250) {
                        pixelsByte[index++] = (byte) red;
                        pixelsByte[index++] = (byte) green;
                        pixelsByte[index++] = (byte) blue;
                    } else {
                        pixelsByte[index++] = (byte) ((pixels[j] >> 16) & 0xff);
                        pixelsByte[index++] = (byte) ((pixels[j] >> 8) & 0xff);
                        pixelsByte[index++] = (byte) ((pixels[j]) & 0xff);
                    }
                }
            } else {
                int transparentPixel = 0;
                smask = new byte[w * h];
                boolean shades = false;
                for (int j = 0; j < size; j++) {
                    byte alpha = smask[j] = (byte) ((pixels[j] >> 24) & 0xff);
                    /* bugfix by Chris Nokleberg */
                    if (!shades) {
                        if (alpha != 0 && alpha != -1) {
                            shades = true;
                        } else if (transparency == null) {
                            if (alpha == 0) {
                                transparentPixel = pixels[j] & 0xffffff;
                                transparency = new int[6];
                                transparency[0] = transparency[1] = (transparentPixel >> 16) & 0xff;
                                transparency[2] = transparency[3] = (transparentPixel >> 8) & 0xff;
                                transparency[4] = transparency[5] = transparentPixel & 0xff;
                                for (int prevPixel = 0; prevPixel < j; prevPixel++) {
                                    if ((pixels[prevPixel] & 0xffffff) == transparentPixel) {
                                        shades = true;
                                        break;
                                    }
                                }
                            }
                        } else if ((pixels[j] & 0xffffff) != transparentPixel && alpha == 0
                                || (pixels[j] & 0xffffff) == transparentPixel && alpha != 0) {
                            shades = true;
                        }
                    }
                    pixelsByte[index++] = (byte) ((pixels[j] >> 16) & 0xff);
                    pixelsByte[index++] = (byte) ((pixels[j] >> 8) & 0xff);
                    pixelsByte[index++] = (byte) ((pixels[j]) & 0xff);
                }
                if (shades) {
                    transparency = null;
                } else {
                    smask = null;
                }
            }
            Image img = Image.getInstance(w, h, 3, 8, pixelsByte, transparency);
            if (smask != null) {
                Image sm = Image.getInstance(w, h, 1, 8, smask);
                try {
                    sm.makeMask();
                    img.setImageMask(sm);
                } catch (DocumentException de) {
                    throw new ExceptionConverter(de);
                }
            }
            return img;
        }
    }

    /**
     * Generates PDF-compliant pixel data for indexed color images (IndexColorModel).
     * <p>
     * This method packs palette indices from a WritableRaster into a byte array that strictly adheres to
     * PDF specification requirements for indexed color image storage:
     * <ul>
     *   <li>Pixel indices are packed starting from the Most Significant Bit (MSB, bit 7) of each byte (PDF mandatory rule)</li>
     *   <li>Each row of pixel data is byte-aligned (padded with zeros to match calculated row stride)</li>
     *   <li>Supports standard indexed color bit depths: 1, 2, 4, 8 bits per pixel</li>
     *   <li>Normalizes palette indices to unsigned 0-255 range to prevent invalid negative values</li>
     * </ul>
     *
     * @param width Width of the indexed color image (in pixels)
     * @param bitsPerPixel Number of bits per pixel (must be 1, 2, 4, or 8 for valid indexed color)
     * @param height Height of the indexed color image (in pixels)
     * @param raster WritableRaster containing the indexed color pixel indices (from IndexColorModel BufferedImage)
     * @return Byte array of pixel data packed according to PDF indexed color specifications, with row-wise byte alignment
     * @see WritableRaster
     * @see IndexColorModel
     */
    private static byte[] generateIndexedColorPixelData(int width, int bitsPerPixel, int height, WritableRaster raster) {
        int rowStride = (width * bitsPerPixel + 7) / 8;
        byte[] pixelData = new byte[rowStride * height];

        int bytePos = 0;
        int bitOffset;

        for (int y = 0; y < height; y++) {
            bitOffset = 7;
            for (int x = 0; x < width; x++) {
                int pixelIndex = raster.getSample(x, y, 0);
                if (pixelIndex < 0) {
                    pixelIndex = 0;
                }
                pixelIndex = pixelIndex & 0xFF;

                bitOffset = packPixelByBitDepth(bitsPerPixel, pixelIndex, pixelData, bytePos, bitOffset);

                if (bitOffset < 0) {
                    bytePos++;
                    bitOffset = 7;
                }
            }
            int usedBytesInRow = bytePos - (y * rowStride);
            if (usedBytesInRow < rowStride) {
                int padBytes = rowStride - usedBytesInRow;
                bytePos += padBytes;
            }
        }
        return pixelData;
    }

    private static int getIndexedTransparentPaletteIndex(IndexColorModel colorModel) {
        int transparentIndex = INDEXED_NO_TRANSPARENCY;
        for (int index = 0; index < colorModel.getMapSize(); index++) {
            int alpha = colorModel.getAlpha(index);
            if (alpha == 0xFF) {
                continue;
            }
            if (alpha != 0x00 || transparentIndex != INDEXED_NO_TRANSPARENCY) {
                return INDEXED_UNSUPPORTED_TRANSPARENCY;
            }
            transparentIndex = index;
        }
        return transparentIndex;
    }

    /**
     * Packs a single pixel index into the target byte array based on specified bit depth (PDF MSB-first rule).
     */
    private static int packPixelByBitDepth(int bitsPerPixel, int pixelIndex, byte[] pixelData, int bytePos, int bitOffset) {
        int currentBitOffset = bitOffset;

        switch (bitsPerPixel) {
            case 1:
                if ((pixelIndex & 0x01) == 1) {
                    pixelData[bytePos] |= (byte) (1 << currentBitOffset);
                }
                currentBitOffset--;
                break;
            case 2:
                pixelData[bytePos] |= (byte) ((pixelIndex & 0x03) << (currentBitOffset - 1));
                currentBitOffset -= 2;
                break;
            case 4:
                pixelData[bytePos] |= (byte) ((pixelIndex & 0x0F) << (currentBitOffset - 3));
                currentBitOffset -= 4;
                break;
            case 8:
            default:
                pixelData[bytePos] = (byte) pixelIndex;
                currentBitOffset = -1;
                break;
        }
        return currentBitOffset;
    }

    /**
     * Gets an instance of an Image from a java.awt.Image.
     *
     * @param image the <CODE>java.awt.Image</CODE> to convert
     * @param color if different from <CODE>null</CODE> the transparency pixels are replaced by this color
     * @return an object of type <CODE>ImgRaw</CODE>
     * @throws BadElementException on error
     * @throws IOException         on error
     */
    public static Image getInstance(java.awt.Image image, java.awt.Color color)
            throws BadElementException, IOException {
        return Image.getInstance(image, color, false);
    }

    /**
     * gets an instance of an Image
     *
     * @param image an Image object
     * @return a new Image object
     */
    public static Image getInstance(Image image) {
        if (image == null) {
            return null;
        }
        try {
            Class<? extends Image> cs = image.getClass();
            Constructor<? extends Image> constructor = cs.getDeclaredConstructor(Image.class);
            return constructor.newInstance(image);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    /**
     * Creates a new serial id.
     *
     * @return a new serial id
     */
    static protected synchronized Long getSerialId() {
        ++serialId;
        return serialId;
    }

    /**
     * Getter for property directReference.
     *
     * @return Value of property directReference.
     */
    public PdfIndirectReference getDirectReference() {
        return this.directReference;
    }

    /**
     * Setter for property directReference.
     *
     * @param directReference New value of property directReference.
     */
    public void setDirectReference(PdfIndirectReference directReference) {
        this.directReference = directReference;
    }

    /**
     * Returns the type.
     *
     * @return a type
     */

    public int type() {
        return type;
    }

    /**
     * @see org.openpdf.text.Element#isNestable()
     * @since iText 2.0.8
     */
    public boolean isNestable() {
        return true;
    }

    /**
     * Returns <CODE>true</CODE> if the image is a <CODE>Jpeg</CODE> -object.
     *
     * @return a <CODE>boolean</CODE>
     */

    public boolean isJpeg() {
        return type == JPEG;
    }

    /**
     * Returns <CODE>true</CODE> if the image is a <CODE>ImgRaw</CODE> -object.
     *
     * @return a <CODE>boolean</CODE>
     */

    public boolean isImgRaw() {
        return type == IMGRAW;
    }

    /**
     * Returns <CODE>true</CODE> if the image is an <CODE>ImgTemplate</CODE> -object.
     *
     * @return a <CODE>boolean</CODE>
     */

    public boolean isImgTemplate() {
        return type == IMGTEMPLATE;
    }

    // serial stamping

    /**
     * Gets the <CODE>String</CODE> -representation of the reference to the image.
     *
     * @return a <CODE>String</CODE>
     */

    public URL getUrl() {
        return url;
    }

    /**
     * Sets the url of the image
     *
     * @param url the url of the image
     */
    public void setUrl(URL url) {
        this.url = url;
    }

    /**
     * Gets the raw data for the image.
     * <p>
     * Remark: this only makes sense for Images of the type <CODE>RawImage
     * </CODE>.
     *
     * @return the raw data
     */
    public byte[] getRawData() {
        return rawData;
    }

    // rotation, note that the superclass also has a rotation value.

    /**
     * Gets the bpc for the image.
     * <p>
     * Remark: this only makes sense for Images of the type <CODE>RawImage
     * </CODE>.
     *
     * @return a bpc value
     */
    public int getBpc() {
        return bpc;
    }

    /**
     * Gets the template to be used as an image.
     * <p>
     * Remark: this only makes sense for Images of the type <CODE>ImgTemplate
     * </CODE>.
     *
     * @return the template
     */
    public PdfTemplate getTemplateData() {
        return template[0];
    }

    /**
     * Sets data from a PdfTemplate
     *
     * @param template the template with the content
     */
    public void setTemplateData(PdfTemplate template) {
        this.template[0] = template;
    }

    /**
     * Gets the alignment for the image.
     * <p>
     * The returned value may be a combination of alignment flags combined using bitwise OR.
     * Use bitwise AND (&amp;) to check for specific flags:
     * <pre>{@code
     * int alignment = image.getAlignment();
     * boolean hasTextwrap = (alignment & Image.TEXTWRAP) != 0;
     * boolean isUnderlying = (alignment & Image.UNDERLYING) != 0;
     * }</pre>
     *
     * @return the alignment value (possibly a combination of flags)
     * @see #setAlignment(int)
     */
    public int getAlignment() {
        return alignment;
    }

    /**
     * Sets the alignment for the image.
     * <p>
     * The alignment parameter can be one of the following base values:
     * <ul>
     *   <li>{@link #LEFT} or {@link #DEFAULT} - Align the image to the left</li>
     *   <li>{@link #RIGHT} - Align the image to the right</li>
     *   <li>{@link #MIDDLE} - Center the image horizontally</li>
     * </ul>
     * <p>
     * The base alignment can be combined with modifiers using bitwise OR (|):
     * <ul>
     *   <li>{@link #TEXTWRAP} - Allow text to wrap around the image (useful for inline images)</li>
     *   <li>{@link #UNDERLYING} - Place the image behind text (useful for watermarks, seals, stamps)</li>
     * </ul>
     * <p>
     * Examples:
     * <pre>{@code
     * // Image aligned left with text wrapping around it on the right
     * image.setAlignment(Image.LEFT | Image.TEXTWRAP);
     * 
     * // Image aligned right with text wrapping around it on the left
     * image.setAlignment(Image.RIGHT | Image.TEXTWRAP);
     * 
     * // Seal or stamp that appears behind text on the right
     * image.setAlignment(Image.RIGHT | Image.UNDERLYING);
     * 
     * // Watermark that appears behind centered text
     * image.setAlignment(Image.MIDDLE | Image.UNDERLYING);
     * }</pre>
     *
     * @param alignment the alignment value (can be a combination of flags using bitwise OR)
     */

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    /**
     * Gets the alternative text for the image.
     *
     * @return a <CODE>String</CODE>
     */

    public String getAlt() {
        return alt;
    }

    /**
     * Sets the alternative information for the image.
     *
     * @param alt the alternative information
     */

    public void setAlt(String alt) {
        this.alt = alt;
    }

    // indentations

    /**
     * Sets the absolute position of the <CODE>Image</CODE>.
     *
     * @param absoluteX absolute x position
     * @param absoluteY absolute y position
     */

    public void setAbsolutePosition(float absoluteX, float absoluteY) {
        this.absoluteX = absoluteX;
        this.absoluteY = absoluteY;
    }

    /**
     * Checks if the <CODE>Images</CODE> has to be added at an absolute X position.
     *
     * @return a boolean
     */
    public boolean hasAbsoluteX() {
        return !Float.isNaN(absoluteX);
    }

    /**
     * Returns the absolute X position.
     *
     * @return a position
     */
    public float getAbsoluteX() {
        return absoluteX;
    }

    /**
     * Checks if the <CODE>Images</CODE> has to be added at an absolute position.
     *
     * @return a boolean
     */
    public boolean hasAbsoluteY() {
        return !Float.isNaN(absoluteY);
    }

    /**
     * Returns the absolute Y position.
     *
     * @return a position
     */
    public float getAbsoluteY() {
        return absoluteY;
    }

    /**
     * Gets the scaled width of the image.
     *
     * @return a value
     */
    public float getScaledWidth() {
        return scaledWidth;
    }

    /**
     * Gets the scaled height of the image.
     *
     * @return a value
     */
    public float getScaledHeight() {
        return scaledHeight;
    }

    /**
     * Gets the plain width of the image.
     *
     * @return a value
     */
    public float getPlainWidth() {
        return plainWidth;
    }

    /**
     * Gets the plain height of the image.
     *
     * @return a value
     */
    public float getPlainHeight() {
        return plainHeight;
    }

    /**
     * Scale the image to an absolute width and an absolute height.
     *
     * @param newWidth  the new width
     * @param newHeight the new height
     */
    public void scaleAbsolute(float newWidth, float newHeight) {
        plainWidth = newWidth;
        plainHeight = newHeight;
        float[] matrix = matrix();
        scaledWidth = matrix[DX] - matrix[CX];
        scaledHeight = matrix[DY] - matrix[CY];
        setWidthPercentage(0);
    }

    /**
     * Scale the image to an absolute width.
     *
     * @param newWidth the new width
     */
    public void scaleAbsoluteWidth(float newWidth) {
        plainWidth = newWidth;
        float[] matrix = matrix();
        scaledWidth = matrix[DX] - matrix[CX];
        scaledHeight = matrix[DY] - matrix[CY];
        setWidthPercentage(0);
    }

    /**
     * Scale the image to an absolute height.
     *
     * @param newHeight the new height
     */
    public void scaleAbsoluteHeight(float newHeight) {
        plainHeight = newHeight;
        float[] matrix = matrix();
        scaledWidth = matrix[DX] - matrix[CX];
        scaledHeight = matrix[DY] - matrix[CY];
        setWidthPercentage(0);
    }

    // widthpercentage (for the moment only used in ColumnText)

    /**
     * Scale the image to a certain percentage.
     *
     * @param percent the scaling percentage
     */
    public void scalePercent(float percent) {
        scalePercent(percent, percent);
    }

    /**
     * Scale the width and height of an image to a certain percentage.
     *
     * @param percentX the scaling percentage of the width
     * @param percentY the scaling percentage of the height
     */
    public void scalePercent(float percentX, float percentY) {
        plainWidth = (getWidth() * percentX) / 100f;
        plainHeight = (getHeight() * percentY) / 100f;
        float[] matrix = matrix();
        scaledWidth = matrix[DX] - matrix[CX];
        scaledHeight = matrix[DY] - matrix[CY];
        setWidthPercentage(0);
    }

    /**
     * Scales the image so that it fits a certain width and height.
     *
     * @param fitWidth  the width to fit
     * @param fitHeight the height to fit
     */
    public void scaleToFit(float fitWidth, float fitHeight) {
        scalePercent(100);
        float percentX = (fitWidth * 100) / getScaledWidth();
        float percentY = (fitHeight * 100) / getScaledHeight();
        scalePercent(percentX < percentY ? percentX : percentY);
        setWidthPercentage(0);
    }

    // annotation

    /**
     * Returns the transformation matrix of the image.
     *
     * @return an array [AX, AY, BX, BY, CX, CY, DX, DY]
     */
    public float[] matrix() {
        float[] matrix = new float[8];
        float cosX = (float) Math.cos(rotationRadians);
        float sinX = (float) Math.sin(rotationRadians);
        matrix[AX] = plainWidth * cosX;
        matrix[AY] = plainWidth * sinX;
        matrix[BX] = (-plainHeight) * sinX;
        matrix[BY] = plainHeight * cosX;
        if (rotationRadians < Math.PI / 2f) {
            matrix[CX] = matrix[BX];
            matrix[CY] = 0;
            matrix[DX] = matrix[AX];
            matrix[DY] = matrix[AY] + matrix[BY];
        } else if (rotationRadians < Math.PI) {
            matrix[CX] = matrix[AX] + matrix[BX];
            matrix[CY] = matrix[BY];
            matrix[DX] = 0;
            matrix[DY] = matrix[AY];
        } else if (rotationRadians < Math.PI * 1.5f) {
            matrix[CX] = matrix[AX];
            matrix[CY] = matrix[AY] + matrix[BY];
            matrix[DX] = matrix[BX];
            matrix[DY] = 0;
        } else {
            matrix[CX] = 0;
            matrix[CY] = matrix[AY];
            matrix[DX] = matrix[AX] + matrix[BX];
            matrix[DY] = matrix[BY];
        }
        return matrix;
    }

    /**
     * Returns a serial id for the Image (reuse the same image more than once)
     *
     * @return a serialId
     */
    public Long getMySerialId() {
        return mySerialId;
    }

    /**
     * Gets the current image rotation in radians.
     *
     * @return the current image rotation in radians
     */
    public float getImageRotation() {
        float d = 2.0F * (float) Math.PI;
        float rot = ((rotationRadians - initialRotation) % d);
        if (rot < 0) {
            rot += d;
        }
        return rot;
    }

    // Optional Content

    /**
     * Sets the rotation of the image in radians.
     *
     * @param r rotation in radians
     */
    public void setRotation(float r) {
        float d = 2.0F * (float) Math.PI;
        rotationRadians = ((r + initialRotation) % d);
        if (rotationRadians < 0) {
            rotationRadians += d;
        }
        float[] matrix = matrix();
        scaledWidth = matrix[DX] - matrix[CX];
        scaledHeight = matrix[DY] - matrix[CY];
    }

    /**
     * Sets the rotation of the image in degrees.
     *
     * @param deg rotation in degrees
     */
    public void setRotationDegrees(float deg) {
        float d = (float) Math.PI;
        setRotation(deg / 180 * d);
    }

    /**
     * Getter for property initialRotation.
     *
     * @return Value of property initialRotation.
     */
    public float getInitialRotation() {
        return this.initialRotation;
    }

    // interpolation

    /**
     * Some image formats, like TIFF may present the images rotated that have to be compensated.
     *
     * @param initialRotation New value of property initialRotation.
     */
    public void setInitialRotation(float initialRotation) {
        float old_rot = rotationRadians - this.initialRotation;
        this.initialRotation = initialRotation;
        setRotation(old_rot);
    }

    /**
     * Gets the left indentation.
     *
     * @return the left indentation
     */
    public float getIndentationLeft() {
        return indentationLeft;
    }

    /**
     * Sets the left indentation.
     *
     * @param f left indentation
     */
    public void setIndentationLeft(float f) {
        indentationLeft = f;
    }

    // original type and data

    /**
     * Gets the right indentation.
     *
     * @return the right indentation
     */
    public float getIndentationRight() {
        return indentationRight;
    }

    /**
     * Sets the right indentation.
     *
     * @param f right indentation
     */
    public void setIndentationRight(float f) {
        indentationRight = f;
    }

    /**
     * Gets the spacing before this image.
     *
     * @return the spacing
     */
    public float getSpacingBefore() {
        return spacingBefore;
    }

    /**
     * Sets the spacing before this image.
     *
     * @param spacing the new spacing
     */

    public void setSpacingBefore(float spacing) {
        this.spacingBefore = spacing;
    }

    /**
     * Gets the spacing before this image.
     *
     * @return the spacing
     */
    public float getSpacingAfter() {
        return spacingAfter;
    }

    /**
     * Sets the spacing after this image.
     *
     * @param spacing the new spacing
     */

    public void setSpacingAfter(float spacing) {
        this.spacingAfter = spacing;
    }

    // the following values are only set for specific types of images.

    /**
     * Getter for property widthPercentage.
     *
     * @return Value of property widthPercentage.
     */
    public float getWidthPercentage() {
        return this.widthPercentage;
    }

    /**
     * Setter for property widthPercentage.
     *
     * @param widthPercentage New value of property widthPercentage.
     */
    public void setWidthPercentage(float widthPercentage) {
        this.widthPercentage = widthPercentage;
    }

    /**
     * Gets the annotation.
     *
     * @return the annotation that is linked to this image
     */
    public Annotation getAnnotation() {
        return annotation;
    }

    // DPI info

    /**
     * Sets the annotation of this Image.
     *
     * @param annotation the annotation
     */
    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }

    /**
     * Getter for property interpolation.
     *
     * @return Value of property interpolation.
     */
    public boolean isInterpolation() {
        return interpolation;
    }

    /**
     * Sets the image interpolation. Image interpolation attempts to produce a smooth transition between adjacent sample
     * values.
     *
     * @param interpolation New value of property interpolation.
     */
    public void setInterpolation(boolean interpolation) {
        this.interpolation = interpolation;
    }

    // XY Ratio

    /**
     * Getter for property originalType.
     *
     * @return Value of property originalType.
     */
    public int getOriginalType() {
        return this.originalType;
    }

    /**
     * Setter for property originalType.
     *
     * @param originalType New value of property originalType.
     */
    public void setOriginalType(int originalType) {
        this.originalType = originalType;
    }

    /**
     * Getter for property originalData.
     *
     * @return Value of property originalData.
     */
    public byte[] getOriginalData() {
        return this.originalData;
    }

    // color, colorspaces and transparency

    /**
     * Setter for property originalData.
     *
     * @param originalData New value of property originalData.
     */
    public void setOriginalData(byte[] originalData) {
        this.originalData = originalData;
    }

    /**
     * Getter for property deflated.
     *
     * @return Value of property deflated.
     */
    public boolean isDeflated() {
        return this.deflated;
    }

    /**
     * Setter for property deflated.
     *
     * @param deflated New value of property deflated.
     */
    public void setDeflated(boolean deflated) {
        this.deflated = deflated;
    }

    /**
     * Gets the dots-per-inch in the X direction. Returns 0 if not available.
     *
     * @return the dots-per-inch in the X direction
     */
    public int getDpiX() {
        return dpiX;
    }

    /**
     * Gets the dots-per-inch in the Y direction. Returns 0 if not available.
     *
     * @return the dots-per-inch in the Y direction
     */
    public int getDpiY() {
        return dpiY;
    }

    /**
     * Sets the dots per inch value
     *
     * @param dpiX dpi for x coordinates
     * @param dpiY dpi for y coordinates
     */
    public void setDpi(int dpiX, int dpiY) {
        this.dpiX = dpiX;
        this.dpiY = dpiY;
    }

    /**
     * Gets the X/Y pixel dimensionless aspect ratio.
     *
     * @return the X/Y pixel dimensionless aspect ratio
     */
    public float getXYRatio() {
        return this.XYRatio;
    }

    /**
     * Sets the X/Y pixel dimensionless aspect ratio.
     *
     * @param XYRatio the X/Y pixel dimensionless aspect ratio
     */
    public void setXYRatio(float XYRatio) {
        this.XYRatio = XYRatio;
    }

    /**
     * Gets the colorspace for the image.
     * <p>
     * Remark: this only makes sense for Images of the type <CODE>Jpeg</CODE>.
     *
     * @return a colorspace value
     */
    public int getColorspace() {
        return colorspace;
    }

    /**
     * Getter for the inverted value
     *
     * @return true if the image is inverted
     */
    public boolean isInverted() {
        return invert;
    }

    /**
     * Sets inverted true or false
     *
     * @param invert true or false
     */
    public void setInverted(boolean invert) {
        this.invert = invert;
    }

    /**
     * Tags this image with an ICC profile.
     *
     * @param profile the profile
     */
    public void tagICC(ICC_Profile profile) {
        this.profile = profile;
    }

    /**
     * Checks is the image has an ICC profile.
     *
     * @return the ICC profile or <CODE>null</CODE>
     */
    public boolean hasICCProfile() {
        return (this.profile != null);
    }

    /**
     * Gets the images ICC profile.
     *
     * @return the ICC profile
     */
    public ICC_Profile getICCProfile() {
        return profile;
    }

    /**
     * Getter for the dictionary with additional information.
     *
     * @return a PdfDictionary with additional information.
     */
    public PdfDictionary getAdditional() {
        return this.additional;
    }

    /**
     * Sets the /Colorspace key.
     *
     * @param additional a PdfDictionary with additional information.
     */
    public void setAdditional(PdfDictionary additional) {
        this.additional = additional;
    }

    /**
     * Replaces CalRGB and CalGray colorspaces with DeviceRGB and DeviceGray.
     */
    public void simplifyColorspace() {
        if (additional == null) {
            return;
        }
        PdfArray value = additional.getAsArray(PdfName.COLORSPACE);
        if (value == null) {
            return;
        }
        PdfObject cs = simplifyColorspace(value);
        PdfObject newValue;
        if (cs.isName()) {
            newValue = cs;
        } else {
            newValue = value;
            PdfName first = value.getAsName(0);
            if (PdfName.INDEXED.equals(first)) {
                if (value.size() >= 2) {
                    PdfArray second = value.getAsArray(1);
                    if (second != null) {
                        value.set(1, simplifyColorspace(second));
                    }
                }
            }
        }
        additional.put(PdfName.COLORSPACE, newValue);
    }

    /**
     * Gets a PDF Name from an array or returns the object that was passed.
     */
    private PdfObject simplifyColorspace(PdfArray obj) {
        if (obj == null) {
            return obj;
        }
        PdfName first = obj.getAsName(0);
        if (PdfName.CALGRAY.equals(first)) {
            return PdfName.DEVICEGRAY;
        } else if (PdfName.CALRGB.equals(first)) {
            return PdfName.DEVICERGB;
        } else {
            return obj;
        }
    }

    /**
     * Returns <CODE>true</CODE> if this <CODE>Image</CODE> is a mask.
     *
     * @return <CODE>true</CODE> if this <CODE>Image</CODE> is a mask
     */
    public boolean isMask() {
        return mask;
    }

    /**
     * Make this <CODE>Image</CODE> a mask.
     *
     * @throws DocumentException if this <CODE>Image</CODE> can not be a mask
     */
    public void makeMask() throws DocumentException {
        if (!isMaskCandidate()) {
            throw new DocumentException(MessageLocalization.getComposedMessage("this.image.can.not.be.an.image.mask"));
        }
        mask = true;
    }

    /**
     * Returns <CODE>true</CODE> if this <CODE>Image</CODE> has the requisites to be a mask.
     *
     * @return <CODE>true</CODE> if this <CODE>Image</CODE> can be a mask
     */
    public boolean isMaskCandidate() {
        if (type == IMGRAW) {
            if (bpc > 0xff) {
                return true;
            }
        }
        return colorspace == 1;
    }

    /**
     * Gets the explicit masking.
     *
     * @return the explicit masking
     */
    public Image getImageMask() {
        return imageMask;
    }

    /**
     * Sets the explicit masking.
     *
     * @param mask the mask to be applied
     * @throws DocumentException on error
     */
    public void setImageMask(Image mask) throws DocumentException {
        if (this.mask) {
            throw new DocumentException(
                    MessageLocalization.getComposedMessage("an.image.mask.cannot.contain.another.image.mask"));
        }
        if (!mask.mask) {
            throw new DocumentException(
                    MessageLocalization.getComposedMessage("the.image.mask.is.not.a.mask.did.you.do.makemask"));
        }
        imageMask = mask;
        smask = (mask.bpc > 1 && mask.bpc <= 8);
    }

    /**
     * Getter for property smask.
     *
     * @return Value of property smask.
     */
    public boolean isSmask() {
        return this.smask;
    }

    /**
     * Setter for property smask.
     *
     * @param smask New value of property smask.
     */
    public void setSmask(boolean smask) {
        this.smask = smask;
    }

    /**
     * Returns the transparency.
     *
     * @return the transparency values
     */

    public int[] getTransparency() {
        return transparency;
    }

    /**
     * Sets the transparency values
     *
     * @param transparency the transparency values
     */
    public void setTransparency(int[] transparency) {
        this.transparency = transparency;
    }


    /**
     * Returns the compression level used for images written as a compressed stream.
     *
     * @return the compression level (0 = best speed, 9 = best compression, -1 is default)
     * @since 2.1.3
     */
    public int getCompressionLevel() {
        return compressionLevel;
    }

    /**
     * Sets the compression level to be used if the image is written as a compressed stream.
     *
     * @param compressionLevel a value between 0 (best speed) and 9 (best compression)
     * @since 2.1.3
     */
    public void setCompressionLevel(int compressionLevel) {
        if (compressionLevel < PdfStream.NO_COMPRESSION || compressionLevel > PdfStream.BEST_COMPRESSION) {
            this.compressionLevel = PdfStream.DEFAULT_COMPRESSION;
        } else {
            this.compressionLevel = compressionLevel;
        }
    }

    private static boolean isJBIG2(int[] array) {
        return array[0] == 0x97 && array[1] == 'J' && array[2] == 'B' && array[3] == '2' &&
                array[4] == '\r' && array[5] == '\n' && array[6] == 0x1a && array[7] == '\n';
    }

    private static boolean isGif(int[] array) {
        return array[0] == 'G' && array[1] == 'I' && array[2] == 'F';
    }

    private static boolean isPng(int[] array) {
        return array[0] == PNGID[0] && array[1] == PNGID[1] && array[2] == PNGID[2] && array[3] == PNGID[3];
    }

    private static boolean isBmp(int[] array) {
        return array[0] == 'B' && array[1] == 'M';
    }

    private static boolean isJpeg(int[] array) {
        return array[0] == 0xFF && array[1] == 0xD8;
    }

    private static boolean isJpeg2000(int[] array) {
        return (array[0] == 0x00 && array[1] == 0x00 && array[2] == 0x00 && array[3] == 0x0c) ||
                (array[0] == 0xff && array[1] == 0x4f && array[2] == 0xff && array[3] == 0x51);
    }

    private static boolean isTiff(int[] array) {
        return (array[0] == 'M' && array[1] == 'M' && array[2] == 0 && array[3] == 42) ||
                (array[0] == 'I' && array[1] == 'I' && array[2] == 42 && array[3] == 0);
    }

    private static boolean isWMF(int[] array) {
        return array[0] == 0xD7 && array[1] == 0xCD;
    }

    /**
     * Gets an instance of an Image from the bytes of an image file. This pruned version of OpenPDF has no image
     * decoders (PNG, JPEG, GIF...): only the raw images, see {@link #getInstance(int, int, int, int, byte[])}.
     *
     * @param imgb raw image date
     * @return never
     * @throws IOException always: image formats are not supported
     */
    public static Image getInstance(byte[] imgb) throws BadElementException, IOException {
        throw new IOException("Image formats are not supported");
    }



    /**
     * Gets an instance of an Image in raw mode.
     *
     * @param width        the width of the image in pixels
     * @param height       the height of the image in pixels
     * @param components   1,3 or 4 for GrayScale, RGB and CMYK
     * @param data         the image data
     * @param bpc          bits per component
     * @param transparency transparency information in the Mask format of the image dictionary
     * @return an object of type <CODE>ImgRaw</CODE>
     * @throws BadElementException on error
     */
    public static Image getInstance(int width, int height, int components,
            int bpc, byte[] data, int[] transparency)
            throws BadElementException {
        if (transparency != null && transparency.length != components * 2) {
            throw new BadElementException(
                    MessageLocalization.getComposedMessage("transparency.length.must.be.equal.to.componentes.2"));
        }
        Image img = new ImgRaw(width, height, components, bpc, data);
        img.transparency = transparency;
        return img;
    }
}
