/*
 * $Id: PdfIndirectReference.java 3912 2009-04-26 08:38:15Z blowagie $
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

/**
 * <CODE>PdfIndirectReference</CODE> contains a reference to a <CODE>PdfIndirectObject</CODE>.
 * <p>
 * Any object used as an element of an array or as a value in a dictionary may be specified by either a direct object of
 * an indirect reference. An <I>indirect reference</I> is a reference to an indirect object, and consists of the
 * indirect object's object number, generation number and the <B>R</B> keyword.<BR> This object is described in the
 * 'Portable Document Format Reference Manual version 1.3' section 4.11 (page 54).
 *
 * @see PdfObject
 * @see PdfIndirectObject
 */

public class PdfIndirectReference extends PdfObject {

    // membervariables

    /**
     * the object number
     */
    protected int number;

    /**
     * the generation number
     */
    protected int generation = 0;

    // constructors

    protected PdfIndirectReference() {
        super(0);
    }

    /**
     * Constructs a <CODE>PdfIndirectReference</CODE>.
     *
     * @param type       the type of the <CODE>PdfObject</CODE> that is referenced to
     * @param number     the object number.
     * @param generation the generation number.
     */

    PdfIndirectReference(int type, int number, int generation) {
        super(0, number + " " + generation + " R");
        this.number = number;
        this.generation = generation;
    }

    /**
     * Constructs a <CODE>PdfIndirectReference</CODE>.
     *
     * @param type   the type of the <CODE>PdfObject</CODE> that is referenced to
     * @param number the object number.
     */

    PdfIndirectReference(int type, int number) {
        this(type, number, 0);
    }

    // methods

    /**
     * Returns the number of the object.
     *
     * @return a number.
     */

    public int getNumber() {
        return number;
    }

    /**
     * Returns the generation of the object.
     *
     * @return a number.
     */

    public int getGeneration() {
        return generation;
    }

    public String toString() {
        return new StringBuffer().append(number).append(" ").append(generation).append(" R").toString();
    }
}