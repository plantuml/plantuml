/*
 * $Id: PdfArray.java 3761 2009-03-06 16:33:57Z blowagie $
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

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <CODE>PdfArray</CODE> is the PDF Array object.
 * <p>
 * An array is a sequence of PDF objects. An array may contain a mixture of object types. An array is written as a left
 * square bracket ([), followed by a sequence of objects, followed by a right square bracket (]).<BR> This object is
 * described in the 'Portable Document Format Reference Manual version 1.7' section 3.2.5 (page 58).
 *
 * @see PdfObject
 */
public class PdfArray extends PdfObject {

    // CLASS VARIABLES

    /**
     * this is the actual array of PdfObjects
     */
    protected List<PdfObject> arrayList;

    // constructors

    /**
     * Constructs an empty <CODE>PdfArray</CODE>-object.
     */
    public PdfArray() {
        super(ARRAY);
        arrayList = new ArrayList<>();
    }

    /**
     * Constructs an <CODE>PdfArray</CODE>-object, containing 1
     * <CODE>PdfObject</CODE>.
     *
     * @param object a <CODE>PdfObject</CODE> that has to be added to the array
     */
    public PdfArray(PdfObject object) {
        this();
        arrayList.add(object);
    }

    /**
     * Constructs a <CODE>PdfArray</CODE>-object, containing all
     * <CODE>float</CODE> values in a specified array.
     * <p>
     * The <CODE>float</CODE> values are internally converted to
     * <CODE>PdfNumber</CODE> objects.
     *
     * @param values an array of <CODE>float</CODE> values to be added
     */
    public PdfArray(float[] values) {
        this();
        add(values);
    }

    /**
     * Constructs a <CODE>PdfArray</CODE>-object, containing all
     * <CODE>int</CODE> values in a specified array.
     * <p>
     * The <CODE>int</CODE> values are internally converted to
     * <CODE>PdfNumber</CODE> objects.
     *
     * @param values an array of <CODE>int</CODE> values to be added
     */
    public PdfArray(int[] values) {
        this();
        add(values);
    }

    /**
     * Constructs a <CODE>PdfArray</CODE>, containing all elements of a specified <CODE>List</CODE>.
     *
     * @param pdfObjectList an <CODE>List</CODE> with <CODE>PdfObject</CODE>s to be added to the array
     * @since 2.1.3
     */
    public PdfArray(List<? extends PdfObject> pdfObjectList) {
        this();
        if (pdfObjectList != null) {
            arrayList.addAll(pdfObjectList);
        }
    }

    // METHODS OVERRIDING SOME PDFOBJECT METHODS

    /**
     * Writes the PDF representation of this <CODE>PdfArray</CODE> as an array of <CODE>byte</CODE> to the specified
     * <CODE>OutputStream</CODE>.
     *
     * @param writer for backwards compatibility
     * @param os     the <CODE>OutputStream</CODE> to write the bytes to.
     */
    @Override
    public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
        os.write('[');

        Iterator<PdfObject> i = arrayList.iterator();
        PdfObject object;
        int type;
        if (i.hasNext()) {
            object = i.next();
            if (object == null) {
                object = PdfNull.PDFNULL;
            }
            object.toPdf(writer, os);
        }
        while (i.hasNext()) {
            object = i.next();
            if (object == null) {
                object = PdfNull.PDFNULL;
            }
            type = object.type();
            if (type != PdfObject.ARRAY && type != PdfObject.DICTIONARY
                    && type != PdfObject.NAME && type != PdfObject.STRING) {
                os.write(' ');
            }
            object.toPdf(writer, os);
        }
        os.write(']');
    }

    /**
     * Returns a string representation of this <CODE>PdfArray</CODE>.
     * <p>
     * The string representation consists of a list of all
     * <CODE>PdfObject</CODE>s contained in this <CODE>PdfArray</CODE>, enclosed
     * in square brackets ("[]"). Adjacent elements are separated by the characters ", " (comma and space).
     *
     * @return the string representation of this <CODE>PdfArray</CODE>
     */
    @Override
    public String toString() {
        return arrayList.toString();
    }

    // ARRAY CONTENT METHODS

    /**
     * Returns the number of entries in the array.
     *
     * @return the size of the List
     */
    public int size() {
        return arrayList.size();
    }

    /**
     * Returns <CODE>true</CODE> if the array is empty.
     *
     * @return <CODE>true</CODE> if the array is empty
     * @since 2.1.5
     */
    public boolean isEmpty() {
        return arrayList.isEmpty();
    }

    /**
     * Adds a <CODE>PdfObject</CODE> to the end of the <CODE>PdfArray</CODE>.
     * <p>
     * The <CODE>PdfObject</CODE> will be the last element.
     *
     * @param object <CODE>PdfObject</CODE> to add
     * @return always <CODE>true</CODE>
     */
    public boolean add(PdfObject object) {
        return arrayList.add(object);
    }

    /**
     * Adds an array of <CODE>float</CODE> values to end of the
     * <CODE>PdfArray</CODE>.
     * <p>
     * The values will be the last elements. The <CODE>float</CODE> values are internally converted to
     * <CODE>PdfNumber</CODE> objects.
     *
     * @param values An array of <CODE>float</CODE> values to add
     * @return always <CODE>true</CODE>
     */
    public boolean add(float[] values) {
        for (float value : values) {
            arrayList.add(new PdfNumber(value));
        }
        return true;
    }

    /**
     * Adds an array of <CODE>int</CODE> values to end of the
     * <CODE>PdfArray</CODE>.
     * <p>
     * The values will be the last elements. The <CODE>int</CODE> values are internally converted to
     * <CODE>PdfNumber</CODE> objects.
     *
     * @param values An array of <CODE>int</CODE> values to add
     * @return always <CODE>true</CODE>
     */
    public boolean add(int[] values) {
        for (int value : values) {
            arrayList.add(new PdfNumber(value));
        }
        return true;
    }

    /**
     * Checks if the <CODE>PdfArray</CODE> already contains a certain
     * <CODE>PdfObject</CODE>.
     *
     * @param object The <CODE>PdfObject</CODE> to check
     * @return <CODE>true</CODE>
     */
    public boolean contains(PdfObject object) {
        return arrayList.contains(object);
    }

    /**
     * Returns the <CODE>PdfObject</CODE> with the specified index.
     * <p>
     * A possible indirect references is not resolved, so the returned
     * <CODE>PdfObject</CODE> may be either a direct object or an indirect
     * reference, depending on how the object is stored in the
     * <CODE>PdfArray</CODE>.
     *
     * @param idx The index of the <CODE>PdfObject</CODE> to be returned
     * @return A <CODE>PdfObject</CODE>
     */
    public PdfObject getPdfObject(int idx) {
        return arrayList.get(idx);
    }



    /**
     * Returns the <CODE>PdfObject</CODE> with the specified index, resolving a possible indirect reference to a direct
     * object.
     * <p>
     * Thus this method will never return a <CODE>PdfIndirectReference</CODE> object.
     *
     * @param idx The index of the <CODE>PdfObject</CODE> to be returned
     * @return A direct <CODE>PdfObject</CODE> or <CODE>null</CODE>
     */
    public PdfObject getDirectObject(int idx) {
        return getPdfObject(idx);
    }



    // DOWNCASTING GETTERS
    // @author Mark A Storer (2/17/06)











    /**
     * Returns a <CODE>PdfObject</CODE> as a <CODE>PdfName</CODE>, resolving indirect references.
     * <p>
     * The object corresponding to the specified index is retrieved and resolved to a direct object. If it is a
     * <CODE>PdfName</CODE>, it is cast down and returned as such. Otherwise <CODE>null</CODE> is returned.
     *
     * @param idx The index of the <CODE>PdfObject</CODE> to be returned
     * @return the corresponding <CODE>PdfName</CODE> object, or
     * <CODE>null</CODE>
     */
    public PdfName getAsName(int idx) {
        PdfName name = null;
        PdfObject orig = getDirectObject(idx);
        if (orig != null && orig.isName()) {
            name = (PdfName) orig;
        }
        return name;
    }



}
