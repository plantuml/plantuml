/*
 * $Id: ColorDetails.java 3514 2008-06-27 09:26:36Z blowagie $
 *
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

/**
 * Each spotcolor in the document will have an instance of this class
 *
 * @author Phillip Pan (phillip@formstar.com)
 */
class ColorDetails {

    /**
     * The indirect reference to this color
     */
    PdfIndirectReference indirectReference;
    /**
     * The color name that appears in the document body stream
     */
    PdfName colorName;
    /**
     * The color
     */
    PdfSpotColor spotcolor;

    /**
     * Each spot color used in a document has an instance of this class.
     *
     * @param colorName         the color name
     * @param indirectReference the indirect reference to the font
     * @param scolor            the <CODE>PDfSpotColor</CODE>
     */
    ColorDetails(PdfName colorName, PdfIndirectReference indirectReference, PdfSpotColor scolor) {
        this.colorName = colorName;
        this.indirectReference = indirectReference;
        this.spotcolor = scolor;
    }

    /**
     * Gets the indirect reference to this color.
     *
     * @return the indirect reference to this color
     */
    PdfIndirectReference getIndirectReference() {
        return indirectReference;
    }

    /**
     * Gets the color name as it appears in the document body.
     *
     * @return the color name
     */
    PdfName getColorName() {
        return colorName;
    }

    /**
     * Gets the <CODE>SpotColor</CODE> object.
     *
     * @return the <CODE>PdfSpotColor</CODE>
     */
    PdfObject getSpotColor(PdfWriter writer) {
        return spotcolor.getSpotObject(writer);
    }
}
