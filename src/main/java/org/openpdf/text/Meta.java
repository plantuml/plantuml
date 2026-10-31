/*
 * $Id: Meta.java 3373 2008-05-12 16:21:24Z xlv $
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


/**
 * This is an <CODE>Element</CODE> that contains some meta information about the document.
 * <p>
 * An object of type <CODE>Meta</CODE> can not be constructed by the user. User defined meta information should be
 * placed in a <CODE>Header</CODE>-object.
 * <CODE>Meta</CODE> is reserved for: Subject, Keywords, Author, Title, Producer
 * and Creationdate information.
 *
 * @see Element
 */

public class Meta implements Element {

    // membervariables

    /**
     * This is the type of Meta-information this object contains.
     */
    private int type;

    /**
     * This is the content of the Meta-information.
     */
    private StringBuffer content;

    // constructors

    /**
     * Constructs a <CODE>Meta</CODE>.
     *
     * @param type    the type of meta-information
     * @param content the content
     */
    Meta(int type, String content) {
        this.type = type;
        this.content = new StringBuffer(content);
    }

    // implementation of the Element-methods

    /**
     * Gets the type of the text element.
     *
     * @return a type
     */
    public int type() {
        return type;
    }

    /**
     * @see org.openpdf.text.Element#isContent()
     * @since iText 2.0.8
     */
    public boolean isContent() {
        return false;
    }

    // methods

    /**
     * @see org.openpdf.text.Element#isNestable()
     * @since iText 2.0.8
     */
    public boolean isNestable() {
        return false;
    }

    // methods to retrieve information

    /**
     * Returns the content of the meta information.
     *
     * @return a <CODE>String</CODE>
     */
    public String getContent() {
        return content.toString();
    }

    /**
     * Returns the name of the meta information.
     *
     * @return a <CODE>String</CODE>
     */

}