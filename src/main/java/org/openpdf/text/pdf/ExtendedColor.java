/*
 * $Id: ExtendedColor.java 3373 2008-05-12 16:21:24Z xlv $
 *
 * Copyright 2001, 2002 by Paulo Soares.
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

import java.awt.Color;

/**
 * @author Paulo Soares (psoares@consiste.pt)
 */
public abstract class ExtendedColor extends Color {

    /**
     * a type of extended color.
     */
    public static final int TYPE_RGB = 0;
    /**
     * a type of extended color.
     */
    public static final int TYPE_GRAY = 1;
    /**
     * a type of extended color.
     */
    public static final int TYPE_CMYK = 2;
    /**
     * a type of extended color.
     */
    public static final int TYPE_SEPARATION = 3;
    /**
     * a type of extended color.
     */
    public static final int TYPE_PATTERN = 4;
    /**
     * a type of extended color.
     */
    public static final int TYPE_SHADING = 5;
    /**
     * the max int color value (255) expressed in int
     */
    public static final int MAX_COLOR_VALUE = 0xFF;
    /**
     * the max int color value (255) expressed in float
     */
    public static final float MAX_INT_COLOR_VALUE = 0xFF;
    /**
     * the max float color value (1) expressed in float
     */
    public static final float MAX_FLOAT_COLOR_VALUE = 0x1;
    private static final long serialVersionUID = 2722660170712380080L;
    protected int type;

    /**
     * Constructs an extended color of a certain type.
     *
     * @param type {@link ExtendedColor#type}
     */
    public ExtendedColor(int type) {
        super(0, 0, 0);
        this.type = type;
    }

    /**
     * Constructs an extended color of a certain type and a certain color.
     *
     * @param type  {@link ExtendedColor#type}
     * @param red   red quotient
     * @param green green quotient
     * @param blue  blue quotient
     */
    public ExtendedColor(int type, float red, float green, float blue) {
        this(type, normalize(red), normalize(green), normalize(blue), MAX_FLOAT_COLOR_VALUE);
    }

    /**
     * Constructs an extended color of a certain type and a certain color.
     *
     * @param type  {@link ExtendedColor#type}
     * @param red   red quotient
     * @param green green quotient
     * @param blue  blue quotient
     * @param alpha alpha quotient
     */
    public ExtendedColor(int type, float red, float green, float blue, float alpha) {
        super(normalize(red), normalize(green), normalize(blue), normalize(alpha));
        this.type = type;
    }

    /**
     * Gets the type of a given color.
     *
     * @param color an object of {@link Color}
     * @return one of the types (see constants)
     */
    public static int getType(Color color) {
        if (color instanceof ExtendedColor) {
            return ((ExtendedColor) color).getType();
        }
        return TYPE_RGB;
    }

    static final float normalize(float value) {
        if (value < 0f) {
            return 0f;
        }
        if (value > MAX_FLOAT_COLOR_VALUE) {
            return MAX_FLOAT_COLOR_VALUE;
        }
        return value;
    }

    static final int normalize(int value) {
        if (value < 0) {
            return 0;
        }
        if (value > (int) MAX_INT_COLOR_VALUE) {
            return (int) MAX_INT_COLOR_VALUE;
        }
        return value;
    }

    /**
     * Gets the type of this color.
     *
     * @return one of the types (see constants)
     */
    public int getType() {
        return type;
    }
}