/*
 * $Id: PdfNumber.java 4065 2009-09-16 23:09:11Z psoares33 $
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
import java.util.Objects;

/**
 * <CODE>PdfNumber</CODE> provides two types of numbers, integer and real.
 * <p>
 * Integers may be specified by signed or unsigned constants. Reals may only be in decimal format.<BR> This object is
 * described in the 'Portable Document Format Reference Manual version 1.7' section 3.3.2 (page 52-53).
 *
 * @see PdfObject
 * @see BadPdfFormatException
 */
public class PdfNumber extends PdfObject implements Comparable<PdfNumber> {

    // CLASS VARIABLES

    /**
     * actual value of this <CODE>PdfNumber</CODE>, represented as a
     * <CODE>double</CODE>
     */
    private double value;

    // CONSTRUCTORS

    /**
     * Constructs a <CODE>PdfNumber</CODE>-object.
     *
     * @param content value of the new <CODE>PdfNumber</CODE>-object
     */
    public PdfNumber(String content) {
        super(NUMBER);
        try {
            value = Double.parseDouble(content.trim());
            setContent(content);
        } catch (NumberFormatException nfe) {
            throw new RuntimeException(
                    MessageLocalization.getComposedMessage("1.is.not.a.valid.number.2", content, nfe.toString()));
        }
    }

    /**
     * Constructs a new <CODE>PdfNumber</CODE>-object of type integer.
     *
     * @param value value of the new <CODE>PdfNumber</CODE>-object
     */
    public PdfNumber(int value) {
        super(NUMBER);
        this.value = value;
        setContent(String.valueOf(value));
    }

    /**
     * Constructs a new <CODE>PdfNumber</CODE>-object of type long.
     *
     * @param value value of the new <CODE>PdfNumber</CODE>-object
     */
    public PdfNumber(long value) {
        super(NUMBER);
        this.value = value;
        setContent(String.valueOf(value));
    }

    /**
     * Constructs a new <CODE>PdfNumber</CODE>-object of type real.
     *
     * @param value value of the new <CODE>PdfNumber</CODE>-object
     */
    public PdfNumber(double value) {
        super(NUMBER);
        this.value = value;
        setContent(ByteBuffer.formatDouble(value));
    }

    /**
     * Constructs a new <CODE>PdfNumber</CODE>-object of type real.
     *
     * @param value value of the new <CODE>PdfNumber</CODE>-object
     */
    public PdfNumber(float value) {
        this((double) value);
    }

    // methods returning the value of this object

    /**
     * Returns the primitive <CODE>int</CODE> value of this object.
     *
     * @return The value as <CODE>int</CODE>
     */
    public int intValue() {
        return (int) value;
    }

    /**
     * Returns the primitive <CODE>double</CODE> value of this object.
     *
     * @return The value as <CODE>double</CODE>
     */
    public double doubleValue() {
        return value;
    }

    /**
     * Returns the primitive <CODE>float</CODE> value of this object.
     *
     * @return The value as <CODE>float</CODE>
     */
    public float floatValue() {
        return (float) value;
    }

    // other methods

    /**
     * Increments the value of the <CODE>PdfNumber</CODE>-object by 1.
     */
    public void increment() {
        value += 1.0;
        setContent(ByteBuffer.formatDouble(value));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PdfNumber)) {
            return false;
        }
        PdfNumber pdfNumber = (PdfNumber) o;
        return Double.compare(pdfNumber.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int compareTo(PdfNumber o) {
        Objects.requireNonNull(o, "PdfNumber is null, can't be compared to current instance.");
        if (this == o) {
            return 0;
        }
        return Double.compare(o.value, value);
    }
}
