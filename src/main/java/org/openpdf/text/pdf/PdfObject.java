/*
 * $Id: PdfObject.java 3912 2009-04-26 08:38:15Z blowagie $
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

/**
 * <CODE>PdfObject</CODE> is the abstract superclass of all PDF objects.
 * <p>
 * PDF supports seven basic types of objects: Booleans, numbers, strings, names, arrays, dictionaries and streams. In
 * addition, PDF provides a null object. Objects may be labeled so that they can be referred to by other objects.<BR>
 * All these basic PDF objects are described in the 'Portable Document Format Reference Manual version 1.3' Chapter 4
 * (pages 37-54).
 *
 * @see PdfNull
 * @see PdfBoolean
 * @see PdfNumber
 * @see PdfString
 * @see PdfName
 * @see PdfArray
 * @see PdfDictionary
 * @see PdfStream
 * @see PdfIndirectReference
 */
public abstract class PdfObject {

    // CONSTANTS

    /**
     * A possible type of <CODE>PdfObject</CODE>
     */
    public static final int BOOLEAN = 1;

    /**
     * A possible type of <CODE>PdfObject</CODE>
     */
    public static final int NUMBER = 2;

    /**
     * A possible type of <CODE>PdfObject</CODE>
     */
    public static final int STRING = 3;

    /**
     * A possible type of <CODE>PdfObject</CODE>
     */
    public static final int NAME = 4;

    /**
     * A possible type of <CODE>PdfObject</CODE>
     */
    public static final int ARRAY = 5;

    /**
     * A possible type of <CODE>PdfObject</CODE>
     */
    public static final int DICTIONARY = 6;

    /**
     * A possible type of <CODE>PdfObject</CODE>
     */
    public static final int STREAM = 7;

    /**
     * A possible type of <CODE>PdfObject</CODE>
     */
    public static final int NULL = 8;

    /**
     * A possible type of <CODE>PdfObject</CODE>
     */
    public static final int INDIRECT = 10;

    /**
     * An empty string used for the <CODE>PdfNull</CODE>-object and for an empty <CODE>PdfString</CODE>-object.
     */
    public static final String NOTHING = "";

    /**
     * This is the default encoding to be used for converting Strings into bytes and vice versa. The default encoding is
     * PdfDocEncoding.
     */
    public static final String TEXT_PDFDOCENCODING = "PDF";

    /**
     * This is the encoding to be used to output text in Unicode.
     */
    public static final String TEXT_UNICODE = "UnicodeBig";

    // CLASS VARIABLES

    /**
     * The content of this <CODE>PdfObject</CODE>
     */
    protected byte[] bytes;

    /**
     * The type of this <CODE>PdfObject</CODE>
     */
    protected int type;

    // CONSTRUCTORS

    /**
     * Constructs a <CODE>PdfObject</CODE> of a certain <VAR>type</VAR> without any <VAR>content</VAR>.
     *
     * @param type type of the new <CODE>PdfObject</CODE>
     */
    protected PdfObject(int type) {
        this.type = type;
    }

    /**
     * Constructs a <CODE>PdfObject</CODE> of a certain <VAR>type</VAR> with a certain <VAR>content</VAR>.
     *
     * @param type    type of the new <CODE>PdfObject</CODE>
     * @param content content of the new <CODE>PdfObject</CODE> as a
     *                <CODE>String</CODE>.
     */
    protected PdfObject(int type, String content) {
        this.type = type;
        bytes = PdfEncodings.convertToBytes(content, null);
    }

    /**
     * Constructs a <CODE>PdfObject</CODE> of a certain <VAR>type</VAR> with a certain <VAR>content</VAR>.
     *
     * @param type  type of the new <CODE>PdfObject</CODE>
     * @param bytes content of the new <CODE>PdfObject</CODE> as an array of
     *              <CODE>byte</CODE>.
     */
    protected PdfObject(int type, byte[] bytes) {
        this.bytes = bytes;
        this.type = type;
    }

    // methods dealing with the content of this object

    /**
     * Writes the PDF representation of this <CODE>PdfObject</CODE> as an array of <CODE>byte</CODE>s to the writer.
     *
     * @param writer for backwards compatibility
     * @param os     The <CODE>OutputStream</CODE> to write the bytes to.
     * @throws IOException on error
     */
    public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
        if (bytes != null) {
            os.write(bytes);
        }
    }

    /**
     * Returns the <CODE>String</CODE>-representation of this
     * <CODE>PdfObject</CODE>.
     *
     * @return a <CODE>String</CODE>
     */
    public String toString() {
        if (bytes == null) {
            return super.toString();
        }
        return PdfEncodings.convertToString(bytes, null);
    }

    /**
     * Gets the presentation of this object in a byte array
     *
     * @return a byte array
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * Whether this object can be contained in an object stream.
     * <p>
     * PdfObjects of type STREAM OR INDIRECT can not be contained in an object stream.
     *
     * @return <CODE>true</CODE> if this object can be in an object stream.
     * Otherwise <CODE>false</CODE>
     */
    public boolean canBeInObjStm() {
        switch (type) {
            case NULL:
            case BOOLEAN:
            case NUMBER:
            case STRING:
            case NAME:
            case ARRAY:
            case DICTIONARY:
                return true;
            case STREAM:
            case INDIRECT:
            default:
                return false;
        }
    }

    /**
     * Returns the length of the PDF representation of the <CODE>PdfObject</CODE>.
     * <P>
     * In some cases, namely for <CODE>PdfString</CODE> and <CODE>PdfStream</CODE>,
     * this method differs from the method <CODE>length</CODE> because <CODE>length</CODE>
     * returns the length of the actual content of the <CODE>PdfObject</CODE>.</P>
     * <P>
     * Remark: the actual content of an object is in most cases identical to its representation.
     * The following statement is always true: length() &gt;= pdfLength().</P>
     *
     * @return a length
     */
//    public int pdfLength() {
//        return toPdf(null).length;
//    }

    /**
     * Returns the length of the actual content of the <CODE>PdfObject</CODE>.
     * <p>
     * In some cases, namely for <CODE>PdfString</CODE> and <CODE>PdfStream</CODE>, this method differs from the method
     * <CODE>pdfLength</CODE> because <CODE>pdfLength</CODE> returns the length of the PDF representation of the
     * object, not of the actual content as does the method <CODE>length</CODE>.</P>
     * <p>
     * Remark: the actual content of an object is in some cases identical to its representation. The following statement
     * is always true: length() &gt;= pdfLength().</P>
     *
     * @return The length as <CODE>int</CODE>
     */
    public int length() {
        return toString().length();
    }

    /**
     * Changes the content of this <CODE>PdfObject</CODE>.
     *
     * @param content the new content of this <CODE>PdfObject</CODE>
     */
    protected void setContent(String content) {
        bytes = PdfEncodings.convertToBytes(content, null);
    }

    // methods dealing with the type of this object

    /**
     * Returns the type of this <CODE>PdfObject</CODE>.
     * <p>
     * May be either of: - <VAR>NULL</VAR>: A <CODE>PdfNull</CODE> - <VAR>BOOLEAN</VAR>: A <CODE>PdfBoolean</CODE> -
     * <VAR>NUMBER</VAR>: A <CODE>PdfNumber</CODE> - <VAR>STRING</VAR>: A <CODE>PdfString</CODE> - <VAR>NAME</VAR>: A
     * <CODE>PdfName</CODE> - <VAR>ARRAY</VAR>: A <CODE>PdfArray</CODE> - <VAR>DICTIONARY</VAR>: A
     * <CODE>PdfDictionary</CODE> - <VAR>STREAM</VAR>: A <CODE>PdfStream</CODE> - <VAR>INDIRECT</VAR>: {@literal >}
     * <CODE>PdfIndirectObject</CODE>
     *
     * @return The type
     */
    public int type() {
        return type;
    }

    /**
     * Checks if this <CODE>PdfObject</CODE> is of the type
     * <CODE>PdfNull</CODE>.
     *
     * @return <CODE>true</CODE> or <CODE>false</CODE>
     */
    public boolean isNull() {
        return (type == NULL);
    }

    /**
     * Checks if this <CODE>PdfObject</CODE> is of the type
     * <CODE>PdfNumber</CODE>.
     *
     * @return <CODE>true</CODE> or <CODE>false</CODE>
     */
    public boolean isNumber() {
        return (type == NUMBER);
    }

    /**
     * Checks if this <CODE>PdfObject</CODE> is of the type
     * <CODE>PdfString</CODE>.
     *
     * @return <CODE>true</CODE> or <CODE>false</CODE>
     */
    public boolean isString() {
        return (type == STRING);
    }

    /**
     * Checks if this <CODE>PdfObject</CODE> is of the type
     * <CODE>PdfName</CODE>.
     *
     * @return <CODE>true</CODE> or <CODE>false</CODE>
     */
    public boolean isName() {
        return (type == NAME);
    }

    /**
     * Checks if this <CODE>PdfObject</CODE> is of the type
     * <CODE>PdfArray</CODE>.
     *
     * @return <CODE>true</CODE> or <CODE>false</CODE>
     */
    public boolean isArray() {
        return (type == ARRAY);
    }

    /**
     * Checks if this <CODE>PdfObject</CODE> is of the type
     * <CODE>PdfDictionary</CODE>.
     *
     * @return <CODE>true</CODE> or <CODE>false</CODE>
     */
    public boolean isDictionary() {
        return (type == DICTIONARY);
    }

}
