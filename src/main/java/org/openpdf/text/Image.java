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

import org.openpdf.text.error_messages.MessageLocalization;
import org.openpdf.text.pdf.PdfDictionary;
import org.openpdf.text.pdf.PdfIndirectReference;
import org.openpdf.text.pdf.PdfStream;
import org.openpdf.text.pdf.PdfTemplate;
import java.net.URL;


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
        this.interpolation = image.interpolation;
        this.originalType = image.originalType;
        this.originalData = image.originalData;
        this.deflated = image.deflated;
        this.dpiX = image.dpiX;
        this.dpiY = image.dpiY;
        this.XYRatio = image.XYRatio;

        this.colorspace = image.colorspace;
        this.invert = image.invert;
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

    /**
     * Gets the alternative text for the image.
     *
     * @return a <CODE>String</CODE>
     */

    /**
     * Sets the alternative information for the image.
     *
     * @param alt the alternative information
     */

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

    // widthpercentage (for the moment only used in ColumnText)

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

    // Optional Content

    // interpolation

    // original type and data

    /**
     * Sets the spacing before this image.
     *
     * @param spacing the new spacing
     */

    /**
     * Sets the spacing after this image.
     *
     * @param spacing the new spacing
     */

    // the following values are only set for specific types of images.

    // DPI info

    /**
     * Getter for property interpolation.
     *
     * @return Value of property interpolation.
     */
    public boolean isInterpolation() {
        return interpolation;
    }

    // XY Ratio

    // color, colorspaces and transparency

    /**
     * Getter for property deflated.
     *
     * @return Value of property deflated.
     */
    public boolean isDeflated() {
        return this.deflated;
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
     * Getter for the dictionary with additional information.
     *
     * @return a PdfDictionary with additional information.
     */
    public PdfDictionary getAdditional() {
        return this.additional;
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
     * Returns the transparency.
     *
     * @return the transparency values
     */

    public int[] getTransparency() {
        return transparency;
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
