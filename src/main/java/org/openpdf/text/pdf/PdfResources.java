/*
 * $Id: PdfResources.java 3712 2009-02-20 20:11:31Z xlv $
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
 * <CODE>PdfResources</CODE> is the PDF Resources-object.
 * <p>
 * The marking operations for drawing a page are stored in a stream that is the value of the
 * <B>Contents</B> key in the Page object's dictionary. Each marking context includes a list
 * of the named resources it uses. This resource list is stored as a dictionary that is the value of the context's
 * <B>Resources</B> key, and it serves two functions: it enumerates the named resources in the contents stream, and it
 * established the mapping from the names to the objects used by the marking operations.<BR> This object is described in
 * the 'Portable Document Format Reference Manual version 1.3' section 7.5 (page 195-197).
 *
 * @see PdfPage
 */

public class PdfResources extends PdfDictionary {

    // constructor

    /**
     * Constructs a PDF ResourcesDictionary.
     */

    public PdfResources() {
        super();
    }



    // methods

    void add(PdfName key, PdfDictionary resource) {
        if (resource.size() == 0) {
            return;
        }
        PdfDictionary dic = getAsDict(key);
        if (dic == null) {
            put(key, resource);
        } else {
            dic.putAll(resource);
        }
    }
}