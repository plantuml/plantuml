/*
 * $Id: PdfBoolean.java 4065 2009-09-16 23:09:11Z psoares33 $
 * $Name$
 *
 * Copyright 1999, 2000, 2001, 2002 Bruno Lowagie
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

import org.openpdf.text.error_messages.MessageLocalization;

/**
 * <CODE>PdfBoolean</CODE> is the boolean object represented by the keywords <VAR>true</VAR> or <VAR>false</VAR>.
 * <p>
 * This object is described in the 'Portable Document Format Reference Manual version 1.7' section 3.2.1 (page 52).
 *
 * @see PdfObject
 * @see BadPdfFormatException
 */

public class PdfBoolean extends PdfObject {

    // static membervariables (possible values of a boolean object)
    public static final PdfBoolean PDFTRUE = new PdfBoolean(true);
    public static final PdfBoolean PDFFALSE = new PdfBoolean(false);
    /**
     * A possible value of <CODE>PdfBoolean</CODE>
     */
    public static final String TRUE = "true";

    /**
     * A possible value of <CODE>PdfBoolean</CODE>
     */
    public static final String FALSE = "false";

    // membervariables

    /**
     * the boolean value of this object
     */
    private boolean value;

    // constructors

    /**
     * Constructs a <CODE>PdfBoolean</CODE>-object.
     *
     * @param value the value of the new <CODE>PdfObject</CODE>
     */

    public PdfBoolean(boolean value) {
        super(BOOLEAN);
        if (value) {
            setContent(TRUE);
        } else {
            setContent(FALSE);
        }
        this.value = value;
    }

    /**
     * Constructs a <CODE>PdfBoolean</CODE>-object.
     *
     * @param value the value of the new <CODE>PdfObject</CODE>, represented as a <CODE>String</CODE>
     * @throws BadPdfFormatException thrown if the <VAR>value</VAR> isn't '<CODE>true</CODE>' or '<CODE>false</CODE>'
     */

    public PdfBoolean(String value) throws BadPdfFormatException {
        super(BOOLEAN, value);
        if (value.equals(TRUE)) {
            this.value = true;
        } else if (value.equals(FALSE)) {
            this.value = false;
        } else {
            throw new BadPdfFormatException(
                    MessageLocalization.getComposedMessage("the.value.has.to.be.true.of.false.instead.of.1", value));
        }
    }

    // methods returning the value of this object

    /**
     * Returns the primitive value of the <CODE>PdfBoolean</CODE>-object.
     *
     * @return the actual value of the object.
     */

    public boolean booleanValue() {
        return value;
    }

    public String toString() {
        return value ? TRUE : FALSE;
    }
}
