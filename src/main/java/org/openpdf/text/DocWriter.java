/*
 * $Id: DocWriter.java 3937 2009-05-27 12:56:48Z blowagie $
 *
 * Copyright 1999, 2000, 2001, 2002 by Bruno Lowagie.
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

package org.openpdf.text;

import org.openpdf.text.pdf.OutputStreamCounter;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * An abstract <CODE>Writer</CODE> class for documents.
 * <p>
 * <CODE>DocWriter</CODE> is the abstract class of several writers such
 * as <CODE>PdfWriter</CODE> and <CODE>HtmlWriter</CODE>. A <CODE>DocWriter</CODE> can be added as a
 * <CODE>DocListener</CODE> to a certain <CODE>Document</CODE> by getting an instance (see method
 * <CODE>getInstance()</CODE> in the specific writer-classes).
 * Every <CODE>Element</CODE> added to the original <CODE>Document</CODE> will be written to the
 * <CODE>OutputStream</CODE> of the listening
 * <CODE>DocWriter</CODE>.
 *
 * @see Document
 * @see DocListener
 */

public abstract class DocWriter implements DocListener {

    /**
     * This is some byte that is often used.
     */
    public static final byte NEWLINE = (byte) '\n';

    /**
     * This is some byte that is often used.
     */
    public static final byte TAB = (byte) '\t';

    /**
     * This is some byte that is often used.
     */
    public static final byte QUOTE = (byte) '\"';

    // membervariables

    /**
     * The pageSize.
     */
    protected Rectangle pageSize;

    /**
     * This is the document that has to be written.
     */
    protected Document document;

    /**
     * The outputstream of this writer.
     */
    protected OutputStreamCounter os;

    /**
     * Is the writer open for writing?
     */
    protected boolean open = false;

    /**
     * Do we have to pause all writing actions?
     */
    protected boolean pause = false;

    /**
     * Closes the stream on document close
     */
    protected boolean closeStream = true;

    // constructor

    /**
     * Constructs a <CODE>DocWriter</CODE>.
     *
     * @param document The <CODE>Document</CODE> that has to be written
     * @param os       The <CODE>OutputStream</CODE> the writer has to write to.
     */

    protected DocWriter(Document document, OutputStream os) {
        this.document = document;
        this.os = new OutputStreamCounter(new BufferedOutputStream(os));
    }

    // implementation of the DocListener methods

    /**
     * Converts a <CODE>String</CODE> into a <CODE>Byte</CODE> array according to the ISO-8859-1 codepage.
     *
     * @param text the text to be converted
     * @return the conversion result
     */

    public static final byte[] getISOBytes(String text) {
        if (text == null) {
            return null;
        }
        int len = text.length();
        byte[] b = new byte[len];
        for (int k = 0; k < len; ++k) {
            b[k] = (byte) text.charAt(k);
        }
        return b;
    }

    /**
     * Signals that an <CODE>Element</CODE> was added to the <CODE>Document</CODE>.
     * <p>
     * This method should be overridden in the specific <CODE>DocWriter</CODE> classes derived from this abstract
     * class.
     *
     * @param element A high level object to add
     * @return <CODE>false</CODE>
     * @throws DocumentException when a document isn't open yet, or has been closed
     */

    public boolean add(Element element) throws DocumentException {
        return false;
    }

    /**
     * Signals that the <CODE>Document</CODE> was opened.
     */

    public void open() {
        open = true;
    }

    /**
     * Sets the pagesize.
     *
     * @param pageSize the new pagesize
     * @return a <CODE>boolean</CODE>
     */

    public boolean setPageSize(Rectangle pageSize) {
        this.pageSize = pageSize;
        return true;
    }

    /**
     * Sets the margins.
     * <p>
     * This does nothing. Has to be overridden if needed.
     *
     * @param marginLeft   the margin on the left
     * @param marginRight  the margin on the right
     * @param marginTop    the margin on the top
     * @param marginBottom the margin on the bottom
     * @return <CODE>false</CODE>
     */

    public boolean setMargins(float marginLeft, float marginRight, float marginTop, float marginBottom) {
        return false;
    }

    /**
     * Signals that an new page has to be started.
     * <p>
     * This does nothing. Has to be overridden if needed.
     *
     * @return <CODE>true</CODE> if the page was added, <CODE>false</CODE> if not.
     */

    public boolean newPage() {
        return open;
    }

    /**
     * Sets the page number to 0.
     * <p>
     * This method should be overridden in the specific <CODE>DocWriter</CODE> classes derived from this abstract class
     * if they actually support the use of pagenumbers.
     */

    /**
     * Sets the page number.
     * <p>
     * This method should be overridden in the specific <CODE>DocWriter</CODE> classes derived from this abstract class
     * if they actually support the use of pagenumbers.
     *
     * @param pageN the new page number
     */

    // methods

    /**
     * Signals that the <CODE>Document</CODE> was closed and that no other
     * <CODE>Elements</CODE> will be added.
     */

    public void close() {
        if (!open) {
            return;
        }
        open = false;
        try {
            os.flush();
            if (closeStream) {
                os.close();
            }
        } catch (IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }

    /**
     * Let the writer know that all writing has to be paused.
     */

    /**
     * Checks if writing is paused.
     *
     * @return <CODE>true</CODE> if writing temporarily has to be paused, <CODE>false</CODE> otherwise.
     */

    public boolean isPaused() {
        return pause;
    }

    /**
     * Let the writer know that writing may be resumed.
     */

    /**
     * Flushes the <CODE>BufferedOutputStream</CODE>.
     */

    public void flush() {
        try {
            os.flush();
        } catch (IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }

    /**
     * Writes a <CODE>String</CODE> to the <CODE>OutputStream</CODE>.
     *
     * @param string the <CODE>String</CODE> to write
     * @throws IOException if there is any error
     */

    /**
     * Writes a number of tabs.
     *
     * @param indent the number of tabs to add
     * @throws IOException if there is any error
     */

    /**
     * Writes a key-value pair to the outputstream.
     *
     * @param key   the name of an attribute
     * @param value the value of an attribute
     * @throws IOException if there is any error
     */

    /**
     * Writes a starttag to the outputstream.
     *
     * @param tag the name of the tag
     * @throws IOException if there is any error
     */

    /**
     * Writes an endtag to the outputstream.
     *
     * @param tag the name of the tag
     * @throws IOException if there is any error
     */

    /**
     * Writes an endtag to the outputstream.
     *
     * @throws IOException if there is any error
     */

}
