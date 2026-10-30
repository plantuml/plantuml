/*
 * $Id: GrayColor.java 3427 2008-05-24 18:32:31Z xlv $
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
public class GrayColor extends ExtendedColor {

    public static final GrayColor GRAYBLACK = new GrayColor(0);
    public static final GrayColor GRAYWHITE = new GrayColor(MAX_FLOAT_COLOR_VALUE);
    private static final long serialVersionUID = -6571835680819282746L;
    private float gray;

    public GrayColor(int intGray) {
        this(normalize(intGray) / MAX_INT_COLOR_VALUE);
    }

    public GrayColor(float floatGray) {
        this(floatGray, MAX_FLOAT_COLOR_VALUE);
    }

    public GrayColor(int intGray, int alpha) {
        this(normalize(intGray) / MAX_INT_COLOR_VALUE, normalize(alpha) / MAX_INT_COLOR_VALUE);
    }

    public GrayColor(float floatGray, float alpha) {
        super(TYPE_GRAY, normalize(floatGray), normalize(floatGray), normalize(floatGray), normalize(alpha));
        gray = normalize(floatGray);
    }

    public float getGray() {
        return gray;
    }

    public boolean equals(Object obj) {
        return obj instanceof GrayColor && ((GrayColor) obj).gray == this.gray;
    }

    public int hashCode() {
        return Float.floatToIntBits(gray);
    }

}
