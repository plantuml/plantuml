/*
 * $Id: Font.java 4020 2009-07-10 05:13:14Z xlv $
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

import org.openpdf.text.pdf.BaseFont;
import java.awt.Color;


/**
 * Contains all the specifications of a font: fontfamily, size, style and color.
 * <p>
 * Example: <BLOCKQUOTE>
 *
 * <PRE>
 * <p>
 * Paragraph p = new Paragraph("This is a paragraph", <STRONG>new Font(Font.HELVETICA, 18, Font.BOLDITALIC, new Color(0,
 * 0, 255)) </STRONG>);
 *
 * </PRE>
 *
 * </BLOCKQUOTE>
 */

public class Font implements Comparable {

    // static membervariables for the different families

    /**
     * a possible value of a font family.
     */
    public static final int COURIER = 0;

    /**
     * a possible value of a font family.
     */
    public static final int HELVETICA = 1;

    /**
     * a possible value of a font family.
     */
    public static final int TIMES_ROMAN = 2;

    /**
     * a possible value of a font family.
     */
    public static final int SYMBOL = 3;

    /**
     * a possible value of a font family.
     */
    public static final int ZAPFDINGBATS = 4;

    // static membervariables for the different styles

    /**
     * this is a possible style.
     */
    public static final int BOLD = 1;

    /**
     * this is a possible style.
     */
    public static final int ITALIC = 2;

    /**
     * this is a possible style.
     */
    public static final int UNDERLINE = 4;

    // static membervariables

    /**
     * the value of an undefined attribute.
     */
    public static final int UNDEFINED = -1;

    // membervariables

    /**
     * the value of the fontfamily.
     */
    private int family = UNDEFINED;

    /**
     * the value of the fontsize.
     */
    private float size = UNDEFINED;

    /**
     * the value of the style.
     */
    private int style = UNDEFINED;

    /**
     * the value of the color.
     */
    private Color color = null;

    /**
     * the external font
     */
    private BaseFont baseFont = null;

    // constructors

    /**
     * Constructs a Font.
     *
     * @param family the family to which this font belongs
     * @param size   the size of this font
     * @param style  the style of this font
     * @param color  the <CODE>Color</CODE> of this font.
     */

    public Font(int family, float size, int style, Color color) {
        this.family = family;
        this.size = size;
        this.style = style;
        this.color = color;
    }

    /**
     * Constructs a Font.
     *
     * @param bf    the external font
     * @param size  the size of this font
     * @param style the style of this font
     * @param color the <CODE>Color</CODE> of this font.
     */

    public Font(BaseFont bf, float size, int style, Color color) {
        this.baseFont = bf;
        this.size = size;
        this.style = style;
        this.color = color;
    }

    /**
     * Constructs a Font.
     *
     * @param family the family to which this font belongs
     * @param size   the size of this font
     * @param style  the style of this font
     */

    /**
     * Constructs a Font.
     *
     * @param family the family to which this font belongs
     * @param size   the size of this font
     */

    /**
     * Constructs a Font.
     *
     * @param family the family to which this font belongs
     */

    /**
     * Constructs a Font.
     */

    public Font() {
        this(UNDEFINED, UNDEFINED, UNDEFINED, null);
    }

    // FAMILY

    /**
     * Compares this <CODE>Font</CODE> with another
     *
     * @param object the other <CODE>Font</CODE>
     * @return a value
     */
    public int compareTo(Object object) {
        if (object == null) {
            return -1;
        }
        Font font;
        try {
            font = (Font) object;
            if (baseFont != null && !baseFont.equals(font.getBaseFont())) {
                return -2;
            }
            if (this.family != font.getFamily()) {
                return 1;
            }
            if (this.size != font.getSize()) {
                return 2;
            }
            if (this.style != font.getStyle()) {
                return 3;
            }
            if (this.color == null) {
                if (font.color == null) {
                    return 0;
                }
                return 4;
            }
            if (font.color == null) {
                return 4;
            }
            if (this.color.equals(font.getColor())) {
                return 0;
            }
            return 4;
        } catch (ClassCastException cce) {
            return -3;
        }
    }

    /**
     * Gets the family of this font.
     *
     * @return the value of the family
     */
    public int getFamily() {
        return family;
    }

    /**
     * Gets the size of this font.
     *
     * @return a size
     */
    public float getSize() {
        return size;
    }

    // STYLE

    /**
     * Gets the style of this font.
     *
     * @return a size
     */
    public int getStyle() {
        return style;
    }

    // COLOR

    /**
     * Gets the color of this font.
     *
     * @return a color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the color.
     *
     * @param color the new color of the font
     */

    // BASEFONT

    /**
     * Gets the <CODE>BaseFont</CODE> inside this object.
     *
     * @return the <CODE>BaseFont</CODE>
     */
    public BaseFont getBaseFont() {
        return baseFont;
    }

    // Helper methods

}
