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
    public static final int NORMAL = 0;

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

    /**
     * this is a possible style.
     */
    public static final int BOLDITALIC = BOLD | ITALIC;

    // static membervariables

    /**
     * the value of an undefined attribute.
     */
    public static final int UNDEFINED = -1;

    /**
     * the value of the default size.
     */
    public static final int DEFAULTSIZE = 12;

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
     * Gets the size that can be used with the calculated <CODE>BaseFont
     * </CODE>.
     *
     * @return the size that can be used with the calculated <CODE>BaseFont
     * </CODE>
     */
    public float getCalculatedSize() {
        float s = this.size;
        if (s == UNDEFINED) {
            s = DEFAULTSIZE;
        }
        return s;
    }

    /**
     * Gets the leading that can be used with this font.
     *
     * @param linespacing a certain linespacing
     * @return the height of a line
     */
    public float getCalculatedLeading(float linespacing) {
        return linespacing * getCalculatedSize();
    }

    /**
     * Gets the style of this font.
     *
     * @return a size
     */
    public int getStyle() {
        return style;
    }

    /**
     * checks if this font is Bold.
     *
     * @return a <CODE>boolean</CODE>
     */
    public boolean isBold() {
        if (style == UNDEFINED) {
            return false;
        }
        return (style & BOLD) == BOLD;
    }

    /**
     * checks if this font is italic.
     *
     * @return a <CODE>boolean</CODE>
     */
    public boolean isItalic() {
        if (style == UNDEFINED) {
            return false;
        }
        return (style & ITALIC) == ITALIC;
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

    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Sets the color.
     *
     * @param red   the red-value of the new color
     * @param green the green-value of the new color
     * @param blue  the blue-value of the new color
     */
    public void setColor(int red, int green, int blue) {
        this.color = new Color(red, green, blue);
    }

    // BASEFONT

    /**
     * Gets the <CODE>BaseFont</CODE> inside this object.
     *
     * @return the <CODE>BaseFont</CODE>
     */
    public BaseFont getBaseFont() {
        return baseFont;
    }

    /**
     * Gets the <CODE>BaseFont</CODE> this class represents. For the built-in fonts a <CODE>BaseFont</CODE> is
     * calculated.
     *
     * @param specialEncoding <CODE>true</CODE> to use the special encoding for Symbol and
     *                        ZapfDingbats, <CODE>false</CODE> to always use <CODE>Cp1252
     *                        </CODE>
     * @return the <CODE>BaseFont</CODE> this class represents
     */
    public BaseFont getCalculatedBaseFont(boolean specialEncoding) {
        if (baseFont != null) {
            return baseFont;
        }
        int style = this.style;
        if (style == UNDEFINED) {
            style = NORMAL;
        }
        String fontName = BaseFont.HELVETICA;
        String encoding = BaseFont.WINANSI;
        BaseFont cfont = null;
        switch (family) {
            case COURIER:
                switch (style & BOLDITALIC) {
                    case BOLD:
                        fontName = BaseFont.COURIER_BOLD;
                        break;
                    case ITALIC:
                        fontName = BaseFont.COURIER_OBLIQUE;
                        break;
                    case BOLDITALIC:
                        fontName = BaseFont.COURIER_BOLDOBLIQUE;
                        break;
                    default:
                        //case NORMAL:
                        fontName = BaseFont.COURIER;
                        break;
                }
                break;
            case TIMES_ROMAN:
                switch (style & BOLDITALIC) {
                    case BOLD:
                        fontName = BaseFont.TIMES_BOLD;
                        break;
                    case ITALIC:
                        fontName = BaseFont.TIMES_ITALIC;
                        break;
                    case BOLDITALIC:
                        fontName = BaseFont.TIMES_BOLDITALIC;
                        break;
                    default:
                    case NORMAL:
                        fontName = BaseFont.TIMES_ROMAN;
                        break;
                }
                break;
            case SYMBOL:
                fontName = BaseFont.SYMBOL;
                if (specialEncoding) {
                    encoding = BaseFont.SYMBOL;
                }
                break;
            case ZAPFDINGBATS:
                fontName = BaseFont.ZAPFDINGBATS;
                if (specialEncoding) {
                    encoding = BaseFont.ZAPFDINGBATS;
                }
                break;
            default:
            case Font.HELVETICA:
                switch (style & BOLDITALIC) {
                    case BOLD:
                        fontName = BaseFont.HELVETICA_BOLD;
                        break;
                    case ITALIC:
                        fontName = BaseFont.HELVETICA_OBLIQUE;
                        break;
                    case BOLDITALIC:
                        fontName = BaseFont.HELVETICA_BOLDOBLIQUE;
                        break;
                    default:
                    case NORMAL:
                        fontName = BaseFont.HELVETICA;
                        break;
                }
                break;
        }
        try {
            cfont = BaseFont.createFont(fontName, encoding, false);
        } catch (Exception ee) {
            throw new ExceptionConverter(ee);
        }
        return cfont;
    }

    // Helper methods

}
