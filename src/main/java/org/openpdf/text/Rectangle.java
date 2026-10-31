/*
 * $Id: Rectangle.java 3742 2009-03-03 16:42:09Z blowagie $
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

import java.awt.Color;

/**
 * A <CODE>Rectangle</CODE> is the representation of a geometric figure.
 * <p>
 * Rectangles support constant width borders using {@link #setBorderWidth(float)}and {@link #setBorder(int)}. They also
 * support borders that vary in width/color on each side using methods like {@link #setBorderWidthLeft(float)}or
 * {@link #setBorderColorLeft(java.awt.Color)}.
 *
 * @see Element
 * @see Table
 * @see Cell
 * @see HeaderFooter
 */
public class Rectangle implements Element {

    // CONSTANTS:

    /**
     * This is the value that will be used as <VAR>undefined </VAR>.
     */
    public static final int UNDEFINED = -1;

    /**
     * This represents one side of the border of the <CODE>Rectangle</CODE>.
     */
    public static final int TOP = 1;

    /**
     * This represents one side of the border of the <CODE>Rectangle</CODE>.
     */
    public static final int BOTTOM = 2;

    /**
     * This represents one side of the border of the <CODE>Rectangle</CODE>.
     */
    public static final int LEFT = 4;

    /**
     * This represents one side of the border of the <CODE>Rectangle</CODE>.
     */
    public static final int RIGHT = 8;

    /**
     * This represents a rectangle without borders.
     */
    public static final int NO_BORDER = 0;

    /**
     * This represents a type of border.
     */
    public static final int BOX = TOP + BOTTOM + LEFT + RIGHT;

    // MEMBER VARIABLES:

    /**
     * the lower left x-coordinate.
     */
    protected float llx;

    /**
     * the lower left y-coordinate.
     */
    protected float lly;

    /**
     * the upper right x-coordinate.
     */
    protected float urx;

    /**
     * the upper right y-coordinate.
     */
    protected float ury;

    /**
     * The rotation of the Rectangle
     */
    protected int rotation = 0;

    /**
     * This is the color of the background of this rectangle.
     */
    protected Color backgroundColor = null;

    /**
     * This represents the status of the 4 sides of the rectangle.
     */
    protected int border = UNDEFINED;

    /**
     * Whether variable width/color borders are used.
     */
    protected boolean useVariableBorders = false;

    /**
     * This is the width of the border around this rectangle.
     */
    protected float borderWidth = UNDEFINED;

    /**
     * The width of the left border of this rectangle.
     */
    protected float borderWidthLeft = UNDEFINED;

    /**
     * The width of the right border of this rectangle.
     */
    protected float borderWidthRight = UNDEFINED;

    /**
     * The width of the top border of this rectangle.
     */
    protected float borderWidthTop = UNDEFINED;

    /**
     * The width of the bottom border of this rectangle.
     */
    protected float borderWidthBottom = UNDEFINED;

    /**
     * The color of the border of this rectangle.
     */
    protected Color borderColor = null;

    /**
     * The color of the left border of this rectangle.
     */
    protected Color borderColorLeft = null;

    /**
     * The color of the right border of this rectangle.
     */
    protected Color borderColorRight = null;

    /**
     * The color of the top border of this rectangle.
     */
    protected Color borderColorTop = null;

    /**
     * The color of the bottom border of this rectangle.
     */
    protected Color borderColorBottom = null;

    // CONSTRUCTORS:

    /**
     * Constructs a <CODE>Rectangle</CODE> -object.
     *
     * @param llx lower left x
     * @param lly lower left y
     * @param urx upper right x
     * @param ury upper right y
     */
    public Rectangle(float llx, float lly, float urx, float ury) {
        this.llx = llx;
        this.lly = lly;
        this.urx = urx;
        this.ury = ury;
    }

    /**
     * Constructs a <CODE>Rectangle</CODE> -object starting from the origin (0, 0).
     *
     * @param urx upper right x
     * @param ury upper right y
     */
    public Rectangle(float urx, float ury) {
        this(0, 0, urx, ury);
    }

    // OJO... Modificacion de
    // flopez-------------------------------------------------

    /**
     * Constructs a <CODE>Rectangle</CODE> -object.
     *
     * @param rect another <CODE>Rectangle</CODE>
     */
    public Rectangle(Rectangle rect) {
        this(rect.llx, rect.lly, rect.urx, rect.ury);
        cloneNonPositionParameters(rect);
    }

    // ******************************************************************************

    // IMPLEMENTATION OF THE ELEMENT INTERFACE:e

    /**
     * Gets the type of the text element.
     *
     * @return a type
     */
    @Override
    public int type() {
        return Element.RECTANGLE;
    }

    /**
     * @see org.openpdf.text.Element#isContent()
     * @since iText 2.0.8
     */
    @Override
    public boolean isContent() {
        return true;
    }

    /**
     * @see org.openpdf.text.Element#isNestable()
     * @since iText 2.0.8
     */
    @Override
    public boolean isNestable() {
        return false;
    }

    /**
     * Returns the lower left x-coordinate.
     *
     * @return the lower left x-coordinate
     */
    public float getLeft() {
        return llx;
    }

    // METHODS TO GET/SET THE DIMENSIONS:

    /**
     * Sets the lower left x-coordinate.
     *
     * @param llx the new value
     */
    public void setLeft(float llx) {
        this.llx = llx;
    }

    /**
     * Returns the upper right x-coordinate.
     *
     * @return the upper right x-coordinate
     */
    public float getRight() {
        return urx;
    }

    /**
     * Sets the upper right x-coordinate.
     *
     * @param urx the new value
     */
    public void setRight(float urx) {
        this.urx = urx;
    }

    /**
     * Returns the width of the rectangle.
     *
     * @return the width
     */
    public float getWidth() {
        return urx - llx;
    }

    /**
     * Returns the upper right y-coordinate.
     *
     * @return the upper right y-coordinate
     */
    public float getTop() {
        return ury;
    }

    /**
     * Sets the upper right y-coordinate.
     *
     * @param ury the new value
     */
    public void setTop(float ury) {
        this.ury = ury;
    }

    /**
     * Returns the lower left y-coordinate.
     *
     * @return the lower left y-coordinate
     */
    public float getBottom() {
        return lly;
    }

    /**
     * Sets the lower left y-coordinate.
     *
     * @param lly the new value
     */
    public void setBottom(float lly) {
        this.lly = lly;
    }

    /**
     * Returns the height of the rectangle.
     *
     * @return the height
     */
    public float getHeight() {
        return ury - lly;
    }

    /**
     * Gets the rotation of the rectangle
     *
     * @return a rotation value
     */
    public int getRotation() {
        return rotation;
    }

    // METHODS TO GET/SET THE ROTATION:

    // METHODS TO GET/SET THE BACKGROUND COLOR:

    /**
     * Gets the backgroundcolor.
     *
     * @return a <CODE>Color</CODE>
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Sets the backgroundcolor of the rectangle.
     *
     * @param backgroundColor a <CODE>Color</CODE>
     */

    // METHODS TO GET/SET THE BORDER:

    /**
     * Indicates whether some type of border is set.
     *
     * @return a boolean
     */
    public boolean hasBorders() {
        switch (border) {
            case UNDEFINED:
            case NO_BORDER:
                return false;
            default:
                return borderWidth > 0 || borderWidthLeft > 0 || borderWidthRight > 0
                        || borderWidthTop > 0 || borderWidthBottom > 0;
        }
    }

    /**
     * Indicates whether the specified type of border is set.
     *
     * @param type the type of border
     * @return a boolean
     */
    public boolean hasBorder(int type) {
        if (border == UNDEFINED) {
            return false;
        }
        return (border & type) == type;
    }

    /**
     * Indicates whether variable width borders are being used. Returns true if
     * <CODE>setBorderWidthLeft, setBorderWidthRight,
     * setBorderWidthTop, or setBorderWidthBottom</CODE> has been called.
     *
     * @return true if variable width borders are in use
     */
    public boolean isUseVariableBorders() {
        return useVariableBorders;
    }

    // METHODS TO GET/SET THE BORDER WIDTH:

    /**
     * Gets the borderwidth.
     *
     * @return a value
     */
    public float getBorderWidth() {
        return borderWidth;
    }

    /**
     * Helper function returning the border width of a specific side.
     *
     * @param variableWidthValue a variable width (could be undefined)
     * @param side               the border you want to check
     * @return the variableWidthValue if not undefined, otherwise the borderWidth
     */
    private float getVariableBorderWidth(float variableWidthValue, int side) {
        if ((border & side) != 0) {
            return variableWidthValue != UNDEFINED ? variableWidthValue : borderWidth;
        }
        return 0;
    }

    /**
     * Gets the width of the left border.
     *
     * @return a width
     */
    public float getBorderWidthLeft() {
        return getVariableBorderWidth(borderWidthLeft, LEFT);
    }

    /**
     * Gets the width of the right border.
     *
     * @return a width
     */
    public float getBorderWidthRight() {
        return getVariableBorderWidth(borderWidthRight, RIGHT);
    }

    /**
     * Gets the width of the top border.
     *
     * @return a width
     */
    public float getBorderWidthTop() {
        return getVariableBorderWidth(borderWidthTop, TOP);
    }

    /**
     * Gets the width of the bottom border.
     *
     * @return a width
     */
    public float getBorderWidthBottom() {
        return getVariableBorderWidth(borderWidthBottom, BOTTOM);
    }

    // METHODS TO GET/SET THE BORDER COLOR:

    /**
     * Gets the color of the border.
     *
     * @return a <CODE>Color</CODE>
     */
    public Color getBorderColor() {
        return borderColor;
    }

    /**
     * Gets the color of the left border.
     *
     * @return a <CODE>Color</CODE>
     */
    public Color getBorderColorLeft() {
        if (borderColorLeft == null) {
            return borderColor;
        }
        return borderColorLeft;
    }

    /**
     * Gets the color of the right border.
     *
     * @return a <CODE>Color</CODE>
     */
    public Color getBorderColorRight() {
        if (borderColorRight == null) {
            return borderColor;
        }
        return borderColorRight;
    }

    /**
     * Gets the color of the top border.
     *
     * @return a <CODE>Color</CODE>
     */
    public Color getBorderColorTop() {
        if (borderColorTop == null) {
            return borderColor;
        }
        return borderColorTop;
    }

    /**
     * Gets the color of the bottom border.
     *
     * @return a <CODE>Color</CODE>
     */
    public Color getBorderColorBottom() {
        if (borderColorBottom == null) {
            return borderColor;
        }
        return borderColorBottom;
    }

    // SPECIAL METHODS:

    /**
     * Copies each of the parameters, except the position, from a
     * <CODE>Rectangle</CODE> object
     *
     * @param rect <CODE>Rectangle</CODE> to copy from
     */
    public void cloneNonPositionParameters(Rectangle rect) {
        this.rotation = rect.rotation;
        this.backgroundColor = rect.backgroundColor;
        this.border = rect.border;
        this.useVariableBorders = rect.useVariableBorders;
        this.borderWidth = rect.borderWidth;
        this.borderWidthLeft = rect.borderWidthLeft;
        this.borderWidthRight = rect.borderWidthRight;
        this.borderWidthTop = rect.borderWidthTop;
        this.borderWidthBottom = rect.borderWidthBottom;
        this.borderColor = rect.borderColor;
        this.borderColorLeft = rect.borderColorLeft;
        this.borderColorRight = rect.borderColorRight;
        this.borderColorTop = rect.borderColorTop;
        this.borderColorBottom = rect.borderColorBottom;
    }

    /**
     * @return a String representation of the rectangle
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder("Rectangle: ");
        buf.append(getWidth());
        buf.append('x');
        buf.append(getHeight());
        buf.append(" (rot: ");
        buf.append(rotation);
        buf.append(" degrees)");
        return buf.toString();
    }

}
