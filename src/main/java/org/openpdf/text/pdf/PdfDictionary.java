/*
 * $Id: PdfDictionary.java 3762 2009-03-06 16:53:44Z blowagie $
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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * <CODE>PdfDictionary</CODE> is the Pdf dictionary object.
 * <p>
 * A dictionary is an associative table containing pairs of objects. The first element of each pair is called the
 * <I>key</I> and the second element is called the <I>value</I>. Unlike dictionaries in the PostScript language, a key
 * must be a
 * <CODE>PdfName</CODE>.
 * A value can be any kind of <CODE>PdfObject</CODE>, including a dictionary. A dictionary is generally used to collect
 * and tie together the attributes of a complex object, with each key-value pair specifying the name and value of an
 * attribute.<BR> A dictionary is represented by two left angle brackets ({@literal <<}), followed by a sequence of
 * key-value pairs, followed by two right angle brackets ({@literal >>}).<BR> This object is described in the 'Portable
 * Document Format Reference Manual version 1.7' section 3.2.6 (page 59-60).
 *
 * @see PdfObject
 * @see PdfName
 * @see BadPdfFormatException
 */
public class PdfDictionary extends PdfObject {

    // CONSTANTS

    /**
     * This is a possible type of dictionary
     */
    public static final PdfName FONT = PdfName.FONT;

    /**
     * This is a possible type of dictionary
     */
    public static final PdfName OUTLINES = PdfName.OUTLINES;

    /**
     * This is a possible type of dictionary
     */
    public static final PdfName PAGE = PdfName.PAGE;

    /**
     * This is a possible type of dictionary
     */
    public static final PdfName PAGES = PdfName.PAGES;

    /**
     * This is a possible type of dictionary
     */
    public static final PdfName CATALOG = PdfName.CATALOG;

    // CLASS VARIABLES
    /**
     * This is the hashmap that contains all the values and keys of the dictionary
     */
    protected Map<PdfName, PdfObject> hashMap;
    /**
     * This is the type of this dictionary
     */
    private PdfName dictionaryType = null;

    // CONSTRUCTORS

    /**
     * Constructs an empty <CODE>PdfDictionary</CODE>-object.
     */
    public PdfDictionary() {
        super(DICTIONARY);
        hashMap = new LinkedHashMap<>();
    }

    /**
     * Constructs a <CODE>PdfDictionary</CODE>-object of a certain type.
     *
     * @param type a <CODE>PdfName</CODE>
     */
    public PdfDictionary(PdfName type) {
        this();
        dictionaryType = type;
        put(PdfName.TYPE, dictionaryType);
    }

    // METHODS OVERRIDING SOME PDFOBJECT METHODS

    /**
     * Writes the PDF representation of this <CODE>PdfDictionary</CODE> as an array of <CODE>byte</CODE> to the given
     * <CODE>OutputStream</CODE>.
     *
     * @param writer for backwards compatibility
     * @param os     the <CODE>OutputStream</CODE> to write the bytes to.
     * @throws IOException on error
     */
    public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
        os.write('<');
        os.write('<');
        // loop over all the object-pairs in the HashMap
        for (PdfName pdfName : hashMap.keySet()) {
            PdfObject value = hashMap.get(pdfName);
            pdfName.toPdf(writer, os);
            int type = value.type();
            if (type != PdfObject.ARRAY && type != PdfObject.DICTIONARY && type != PdfObject.NAME
                    && type != PdfObject.STRING) {
                os.write(' ');
            }
            value.toPdf(writer, os);
        }
        os.write('>');
        os.write('>');
    }

    /**
     * Returns a string representation of this <CODE>PdfDictionary</CODE>.
     * <p>
     * The string doesn't contain any of the content of this dictionary. Rather the string "dictionary" is returned,
     * possibly followed by the type of this <CODE>PdfDictionary</CODE>, if set.
     *
     * @return the string representation of this <CODE>PdfDictionary</CODE>
     * @see org.openpdf.text.pdf.PdfObject#toString()
     */
    public String toString() {
        if (get(PdfName.TYPE) == null) {
            return "Dictionary";
        }
        return "Dictionary of type: " + get(PdfName.TYPE);
    }

    // DICTIONARY CONTENT METHODS

    /**
     * Associates the specified <CODE>PdfObject</CODE> as <VAR>value</VAR> with the specified <CODE>PdfName</CODE> as
     * <VAR>key</VAR> in this map.
     * <p>
     * If the map previously contained a mapping for this <VAR>key</VAR>, the old <VAR>value</VAR> is replaced. If the
     * <VAR>value</VAR> is
     * <CODE>null</CODE> or <CODE>PdfNull</CODE> the key is deleted.
     *
     * @param key    a <CODE>PdfName</CODE>
     * @param object the <CODE>PdfObject</CODE> to be associated with the
     *               <VAR>key</VAR>
     */
    public void put(PdfName key, PdfObject object) {
        if (object == null || object.isNull()) {
            hashMap.remove(key);
        } else {
            hashMap.put(key, object);
        }
    }

    /**
     * Associates the specified <CODE>PdfObject</CODE> as value to the specified <CODE>PdfName</CODE> as key in this
     * map.
     * <p>
     * If the <VAR>value</VAR> is a <CODE>PdfNull</CODE>, it is treated just as any other <CODE>PdfObject</CODE>. If
     * the
     * <VAR>value</VAR> is
     * <CODE>null</CODE> however nothing is done.
     *
     * @param key   a <CODE>PdfName</CODE>
     * @param value the <CODE>PdfObject</CODE> to be associated to the
     *              <VAR>key</VAR>
     */
    public void putEx(PdfName key, PdfObject value) {
        if (value == null) {
            return;
        }
        put(key, value);
    }

    /**
     * Copies all of the mappings from the specified <CODE>PdfDictionary</CODE> to this <CODE>PdfDictionary</CODE>.
     * <p>
     * These mappings will replace any mappings previously contained in this
     * <CODE>PdfDictionary</CODE>.
     *
     * @param dic The <CODE>PdfDictionary</CODE> with the mappings to be copied over
     */
    public void putAll(PdfDictionary dic) {
        hashMap.putAll(dic.hashMap);
    }

    /**
     * Removes a <CODE>PdfObject</CODE> and its <VAR>key</VAR> from the
     * <CODE>PdfDictionary</CODE>.
     *
     * @param key a <CODE>PdfName</CODE>
     */
    public void remove(PdfName key) {
        hashMap.remove(key);
    }

    /**
     * Returns the <CODE>PdfObject</CODE> associated to the specified
     * <VAR>key</VAR>.
     *
     * @param key a <CODE>PdfName</CODE>
     * @return the <CODE>PdfObject</CODE> previously associated to the
     * <VAR>key</VAR>
     */
    public PdfObject get(PdfName key) {
        return hashMap.get(key);
    }

    /**
     * Get all keys that are set.
     *
     * @return <CODE>true</CODE> if it is, otherwise <CODE>false</CODE>.
     */
    public Set<PdfName> getKeys() {
        return hashMap.keySet();
    }

    /**
     * Returns the number of <VAR>key</VAR>-<VAR>value</VAR> mappings in this
     * <CODE>PdfDictionary</CODE>.
     *
     * @return the number of <VAR>key</VAR>-<VAR>value</VAR> mappings in this
     * <CODE>PdfDictionary</CODE>.
     */
    public int size() {
        return hashMap.size();
    }

    /**
     * Returns <CODE>true</CODE> if this <CODE>PdfDictionary</CODE> contains a mapping for the specified
     * <VAR>key</VAR>.
     *
     * @param key key
     * @return <CODE>true</CODE> if the key is set, otherwise <CODE>false</CODE>.
     */
    public boolean contains(PdfName key) {
        return hashMap.containsKey(key);
    }

    // DICTIONARY TYPE METHODS

    // OTHER METHODS

    public void merge(PdfDictionary other) {
        hashMap.putAll(other.hashMap);
    }

    public void mergeDifferent(PdfDictionary other) {
        for (PdfName key : other.hashMap.keySet()) {
            if (!hashMap.containsKey(key)) {
                hashMap.put(key, other.hashMap.get(key));
            }
        }
    }



    /**
     * Returns the <CODE>PdfObject</CODE> associated to the specified
     * <VAR>key</VAR>, resolving a possible indirect reference to a direct
     * object.
     * <p>
     * This method will never return a <CODE>PdfIndirectReference</CODE> object.
     *
     * @param key A key for the <CODE>PdfObject</CODE> to be returned
     * @return A direct <CODE>PdfObject</CODE> or <CODE>null</CODE>
     */
    public PdfObject getDirectObject(PdfName key) {
        return get(key);
    }



    // DOWNCASTING GETTERS
    // @author Mark A Storer (2/17/06)

    /**
     * Returns a <CODE>PdfObject</CODE> as a <CODE>PdfDictionary</CODE>, resolving indirect references.
     * <p>
     * The object associated with the <CODE>PdfName</CODE> given is retrieved and resolved to a direct object. If it is
     * a <CODE>PdfDictionary</CODE>, it is cast down and returned as such. Otherwise <CODE>null</CODE> is returned.
     *
     * @param key A <CODE>PdfName</CODE>
     * @return the associated <CODE>PdfDictionary</CODE> object, or <CODE>null</CODE>
     */
    public PdfDictionary getAsDict(PdfName key) {
        PdfDictionary dict = null;
        PdfObject orig = getDirectObject(key);
        if (orig != null && orig.isDictionary()) {
            dict = (PdfDictionary) orig;
        }
        return dict;
    }









    /**
     * Returns a <CODE>PdfObject</CODE> as a <CODE>PdfNumber</CODE>, resolving indirect references.
     * <p>
     * The object associated with the <CODE>PdfName</CODE> given is retrieved and resolved to a direct object. If it is
     * a <CODE>PdfNumber</CODE>, it is cast down and returned as such. Otherwise <CODE>null</CODE> is returned.
     *
     * @param key A <CODE>PdfName</CODE>
     * @return the associated <CODE>PdfNumber</CODE> object, or <CODE>null</CODE>
     */
    public PdfNumber getAsNumber(PdfName key) {
        PdfNumber number = null;
        PdfObject orig = getDirectObject(key);
        if (orig != null && orig.isNumber()) {
            number = (PdfNumber) orig;
        }
        return number;
    }



    /**
     * Returns a <CODE>PdfObject</CODE> as a <CODE>PdfName</CODE>, resolving indirect references.
     * <p>
     * The object associated with the <CODE>PdfName</CODE> given is retrieved and resolved to a direct object. If it is
     * a <CODE>PdfName</CODE>, it is cast down and returned as such. Otherwise <CODE>null</CODE> is returned.
     *
     * @param key A <CODE>PdfName</CODE>
     * @return the associated <CODE>PdfName</CODE> object, or <CODE>null</CODE>
     */
    public PdfName getAsName(PdfName key) {
        PdfName name = null;
        PdfObject orig = getDirectObject(key);
        if (orig != null && orig.isName()) {
            name = (PdfName) orig;
        }
        return name;
    }



}
