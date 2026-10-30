/*
 * $Id: CMYKColor.java 3427 2008-05-24 18:32:31Z xlv $
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

/**
 * @author Paulo Soares (psoares@consiste.pt)
 */
public class CMYKColor extends ExtendedColor {

    private static final long serialVersionUID = 5940378778276468452L;
    float cyan;
    float magenta;
    float yellow;
    float black;

    /**
     * Constructs a CMYK Color based on 4 color values (values are integers from 0 to 255).
     *
     * @param intCyan    cyan value
     * @param intMagenta magenta value
     * @param intYellow  yellow value
     * @param intBlack   black value
     */
    public CMYKColor(int intCyan, int intMagenta, int intYellow, int intBlack) {
        this(normalize(intCyan) / MAX_INT_COLOR_VALUE, normalize(intMagenta) / MAX_INT_COLOR_VALUE,
                normalize(intYellow) / MAX_INT_COLOR_VALUE, normalize(intBlack) / MAX_INT_COLOR_VALUE);
    }

    /**
     * Constructs a CMYK Color based on 4 color values (values are integers from 0 to 255).
     *
     * @param intCyan    cyan value
     * @param intMagenta magenta value
     * @param intYellow  yellow value
     * @param intBlack   black value
     * @param intAlpha   alpha value
     */
    public CMYKColor(int intCyan, int intMagenta, int intYellow, int intBlack, int intAlpha) {
        this(normalize(intCyan) / MAX_INT_COLOR_VALUE, normalize(intMagenta) / MAX_INT_COLOR_VALUE,
                normalize(intYellow) / MAX_INT_COLOR_VALUE, normalize(intBlack) / MAX_INT_COLOR_VALUE,
                normalize(intAlpha) / MAX_INT_COLOR_VALUE);
    }

    /**
     * Construct a CMYK Color.
     *
     * @param floatCyan    cyan value
     * @param floatMagenta magenta value
     * @param floatYellow  yellow value
     * @param floatBlack   black value
     */
    public CMYKColor(float floatCyan, float floatMagenta, float floatYellow, float floatBlack) {
        this(floatCyan, floatMagenta, floatYellow, floatBlack, MAX_FLOAT_COLOR_VALUE);
    }

    /**
     * Construct a CMYK Color.
     *
     * @param floatCyan    cyan value
     * @param floatMagenta magenta value
     * @param floatYellow  yellow value
     * @param floatBlack   black value
     * @param floatAlpha   alpha value
     */
    public CMYKColor(float floatCyan, float floatMagenta, float floatYellow, float floatBlack, float floatAlpha) {
        super(TYPE_CMYK, MAX_FLOAT_COLOR_VALUE - normalize(floatCyan) - normalize(floatBlack),
                MAX_FLOAT_COLOR_VALUE - normalize(floatMagenta) - normalize(floatBlack),
                MAX_FLOAT_COLOR_VALUE - normalize(floatYellow) - normalize(floatBlack), normalize(floatAlpha));
        cyan = normalize(floatCyan);
        magenta = normalize(floatMagenta);
        yellow = normalize(floatYellow);
        black = normalize(floatBlack);
    }

    /**
     * @return the cyan value
     */
    public float getCyan() {
        return cyan;
    }

    /**
     * @return the magenta value
     */
    public float getMagenta() {
        return magenta;
    }

    /**
     * @return the yellow value
     */
    public float getYellow() {
        return yellow;
    }

    /**
     * @return the black value
     */
    public float getBlack() {
        return black;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CMYKColor)) {
            return false;
        }
        CMYKColor c2 = (CMYKColor) obj;
        return (cyan == c2.cyan && magenta == c2.magenta && yellow == c2.yellow && black == c2.black
                && getAlpha() == c2.getAlpha());
    }

    public int hashCode() {
        return Float.floatToIntBits(cyan)
                ^ Float.floatToIntBits(magenta)
                ^ Float.floatToIntBits(yellow)
                ^ Float.floatToIntBits(black)
                ^ Float.floatToIntBits(getAlpha())
                ;
    }

}
