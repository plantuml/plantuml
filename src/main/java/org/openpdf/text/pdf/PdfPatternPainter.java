/*
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

import org.openpdf.text.DocumentException;
import org.openpdf.text.Image;
import org.openpdf.text.Rectangle;
import org.openpdf.text.error_messages.MessageLocalization;
import java.awt.Color;

/**
 * Implements the pattern.
 */

public final class PdfPatternPainter extends PdfTemplate {

    float xstep, ystep;
    boolean stencil = false;
    Color defaultColor;

    /**
     * Creates a <CODE>PdfPattern</CODE>.
     */

    private PdfPatternPainter() {
        super();
        type = TYPE_PATTERN;
    }

    /**
     * Creates new PdfPattern
     *
     * @param wr the <CODE>PdfWriter</CODE>
     */

    PdfPatternPainter(PdfWriter wr) {
        super(wr);
        type = TYPE_PATTERN;
    }

    PdfPatternPainter(PdfWriter wr, Color defaultColor) {
        this(wr);
        stencil = true;
        if (defaultColor == null) {
            this.defaultColor = Color.gray;
        } else {
            this.defaultColor = defaultColor;
        }
    }

    /**
     * Returns the horizontal interval when repeating the pattern.
     *
     * @return a value
     */
    public float getXStep() {
        return this.xstep;
    }

    /**
     * Sets the horizontal interval of this pattern.
     *
     * @param xstep the xstep in horizontal painting
     */

    public void setXStep(float xstep) {
        this.xstep = xstep;
    }

    /**
     * Returns the vertical interval when repeating the pattern.
     *
     * @return a value
     */
    public float getYStep() {
        return this.ystep;
    }

    /**
     * Sets the vertical interval of this pattern.
     *
     * @param ystep in vertical painting
     */

    public void setYStep(float ystep) {
        this.ystep = ystep;
    }

    /**
     * Tells you if this pattern is colored/uncolored (stencil = uncolored, you need to set a default color).
     *
     * @return true if the pattern is an uncolored tiling pattern (stencil).
     */
    public boolean isStencil() {
        return stencil;
    }

    /**
     * Sets the transformation matrix for the pattern.
     *
     * @param a an element of the transformation matrix
     * @param b an element of the transformation matrix
     * @param c an element of the transformation matrix
     * @param d an element of the transformation matrix
     * @param e an element of the transformation matrix
     * @param f an element of the transformation matrix
     */
    public void setPatternMatrix(float a, float b, float c, float d, float e, float f) {
        setMatrix(a, b, c, d, e, f);
    }

    /**
     * Gets the stream representing this pattern
     *
     * @return the stream representing this pattern
     */
    PdfPattern getPattern() {
        return new PdfPattern(this);
    }

    /**
     * Gets the stream representing this pattern
     *
     * @param compressionLevel the compression level of the stream
     * @return the stream representing this pattern
     * @since 2.1.3
     */
    PdfPattern getPattern(int compressionLevel) {
        return new PdfPattern(this, compressionLevel);
    }

    /**
     * Gets a duplicate of this <CODE>PdfPatternPainter</CODE>. All the members are copied by reference but the buffer
     * stays different.
     *
     * @return a copy of this <CODE>PdfPatternPainter</CODE>
     */

    public PdfContentByte getDuplicate() {
        PdfPatternPainter tpl = new PdfPatternPainter();
        tpl.writer = writer;
        tpl.pdf = pdf;
        tpl.thisReference = thisReference;
        tpl.pageResources = pageResources;
        tpl.bBox = new Rectangle(bBox);
        tpl.xstep = xstep;
        tpl.ystep = ystep;
        tpl.matrix = matrix;
        tpl.stencil = stencil;
        tpl.defaultColor = defaultColor;
        return tpl;
    }

    /**
     * Returns the default color of the pattern.
     *
     * @return a Color
     */
    public Color getDefaultColor() {
        return defaultColor;
    }

    /**
     * @see org.openpdf.text.pdf.PdfContentByte#setGrayFill(float)
     */
    public void setGrayFill(float gray) {
        checkNoColor();
        super.setGrayFill(gray);
    }

    /**
     * @see org.openpdf.text.pdf.PdfContentByte#resetGrayFill()
     */
    public void resetGrayFill() {
        checkNoColor();
        super.resetGrayFill();
    }

    /**
     * @see org.openpdf.text.pdf.PdfContentByte#setGrayStroke(float)
     */
    public void setGrayStroke(float gray) {
        checkNoColor();
        super.setGrayStroke(gray);
    }

    /**
     * @see org.openpdf.text.pdf.PdfContentByte#resetGrayStroke()
     */
    public void resetGrayStroke() {
        checkNoColor();
        super.resetGrayStroke();
    }

    /**
     * @see org.openpdf.text.pdf.PdfContentByte#setRGBColorFillF(float, float, float)
     */
    public void setRGBColorFillF(float red, float green, float blue) {
        checkNoColor();
        super.setRGBColorFillF(red, green, blue);
    }

    /**
     * @see org.openpdf.text.pdf.PdfContentByte#resetRGBColorFill()
     */
    public void resetRGBColorFill() {
        checkNoColor();
        super.resetRGBColorFill();
    }

    /**
     * @see org.openpdf.text.pdf.PdfContentByte#setRGBColorStrokeF(float, float, float)
     */
    public void setRGBColorStrokeF(float red, float green, float blue) {
        checkNoColor();
        super.setRGBColorStrokeF(red, green, blue);
    }

    /**
     * @see org.openpdf.text.pdf.PdfContentByte#resetRGBColorStroke()
     */
    public void resetRGBColorStroke() {
        checkNoColor();
        super.resetRGBColorStroke();
    }

    /**
     * @see org.openpdf.text.pdf.PdfContentByte#setCMYKColorFillF(float, float, float, float)
     */
    public void setCMYKColorFillF(float cyan, float magenta, float yellow, float black) {
        checkNoColor();
        super.setCMYKColorFillF(cyan, magenta, yellow, black);
    }

    /**
     * @see org.openpdf.text.pdf.PdfContentByte#resetCMYKColorFill()
     */
    public void resetCMYKColorFill() {
        checkNoColor();
        super.resetCMYKColorFill();
    }

    /**
     * @see org.openpdf.text.pdf.PdfContentByte#setCMYKColorStrokeF(float, float, float, float)
     */
    public void setCMYKColorStrokeF(float cyan, float magenta, float yellow, float black) {
        checkNoColor();
        super.setCMYKColorStrokeF(cyan, magenta, yellow, black);
    }

    /**
     * @see org.openpdf.text.pdf.PdfContentByte#resetCMYKColorStroke()
     */
    public void resetCMYKColorStroke() {
        checkNoColor();
        super.resetCMYKColorStroke();
    }

    /**
     * @see org.openpdf.text.pdf.PdfContentByte#addImage(org.openpdf.text.Image, float, float, float, float, float,
     * float)
     */
    public void addImage(Image image, float a, float b, float c, float d, float e, float f) throws DocumentException {
        if (stencil && !image.isMask()) {
            checkNoColor();
        }
        super.addImage(image, a, b, c, d, e, f);
    }

    /**
     * @see org.openpdf.text.pdf.PdfContentByte#setCMYKColorFill(int, int, int, int)
     */
    public void setCMYKColorFill(int cyan, int magenta, int yellow, int black) {
        checkNoColor();
        super.setCMYKColorFill(cyan, magenta, yellow, black);
    }

    /**
     * @see org.openpdf.text.pdf.PdfContentByte#setCMYKColorStroke(int, int, int, int)
     */
    public void setCMYKColorStroke(int cyan, int magenta, int yellow, int black) {
        checkNoColor();
        super.setCMYKColorStroke(cyan, magenta, yellow, black);
    }

    /**
     * @see org.openpdf.text.pdf.PdfContentByte#setRGBColorFill(int, int, int)
     */
    public void setRGBColorFill(int red, int green, int blue) {
        checkNoColor();
        super.setRGBColorFill(red, green, blue);
    }

    /**
     * @see org.openpdf.text.pdf.PdfContentByte#setRGBColorStroke(int, int, int)
     */
    public void setRGBColorStroke(int red, int green, int blue) {
        checkNoColor();
        super.setRGBColorStroke(red, green, blue);
    }

    /**
     * @see org.openpdf.text.pdf.PdfContentByte#setColorStroke(java.awt.Color)
     */
    public void setColorStroke(Color color) {
        checkNoColor();
        super.setColorStroke(color);
    }

    /**
     * @see org.openpdf.text.pdf.PdfContentByte#setColorFill(java.awt.Color)
     */
    public void setColorFill(Color color) {
        checkNoColor();
        super.setColorFill(color);
    }

    /**
     * @see org.openpdf.text.pdf.PdfContentByte#setColorFill(org.openpdf.text.pdf.PdfSpotColor, float)
     */
    public void setColorFill(PdfSpotColor sp, float tint) {
        checkNoColor();
        super.setColorFill(sp, tint);
    }

    /**
     * @see org.openpdf.text.pdf.PdfContentByte#setColorStroke(org.openpdf.text.pdf.PdfSpotColor, float)
     */
    public void setColorStroke(PdfSpotColor sp, float tint) {
        checkNoColor();
        super.setColorStroke(sp, tint);
    }

    /**
     * @see org.openpdf.text.pdf.PdfContentByte#setPatternFill(org.openpdf.text.pdf.PdfPatternPainter)
     */
    public void setPatternFill(PdfPatternPainter p) {
        checkNoColor();
        super.setPatternFill(p);
    }

    /**
     * @see org.openpdf.text.pdf.PdfContentByte#setPatternFill(org.openpdf.text.pdf.PdfPatternPainter, java.awt.Color,
     * float)
     */
    public void setPatternFill(PdfPatternPainter p, Color color, float tint) {
        checkNoColor();
        super.setPatternFill(p, color, tint);
    }

    /**
     * @see org.openpdf.text.pdf.PdfContentByte#setPatternStroke(org.openpdf.text.pdf.PdfPatternPainter, java.awt.Color,
     * float)
     */
    public void setPatternStroke(PdfPatternPainter p, Color color, float tint) {
        checkNoColor();
        super.setPatternStroke(p, color, tint);
    }

    /**
     * @see org.openpdf.text.pdf.PdfContentByte#setPatternStroke(org.openpdf.text.pdf.PdfPatternPainter)
     */
    public void setPatternStroke(PdfPatternPainter p) {
        checkNoColor();
        super.setPatternStroke(p);
    }

    void checkNoColor() {
        if (stencil) {
            throw new RuntimeException(
                    MessageLocalization.getComposedMessage("colors.are.not.allowed.in.uncolored.tile.patterns"));
        }
    }
}
