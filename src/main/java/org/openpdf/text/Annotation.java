/*
 * $Id: Annotation.java 3373 2008-05-12 16:21:24Z xlv $
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

import java.net.URL;

/**
 * An <CODE>Annotation</CODE> is a little note that can be added to a page on a document.
 *
 * @see Element
 * @see Anchor
 */

public class Annotation implements Element {

    // membervariables

    /**
     * This is a possible annotation type.
     */
    public static final int TEXT = 0;

    /**
     * This is a possible annotation type.
     */
    public static final int LAUNCH = 6;

    /**
     * This is a possible annotation type.
     */
    public static final int SCREEN = 7;

    /**
     * This is a possible attribute.
     */
    public static final String TITLE = "title";

    /**
     * This is a possible attribute.
     */
    public static final String CONTENT = "content";

    /**
     * This is a possible attribute.
     */
    public static final String URL = "url";

    /**
     * This is a possible attribute.
     */
    public static final String FILE = "file";

    /**
     * This is a possible attribute.
     */
    public static final String DESTINATION = "destination";

    /**
     * This is a possible attribute.
     */
    public static final String PAGE = "page";

    /**
     * This is a possible attribute.
     */
    public static final String NAMED = "named";

    /**
     * This is a possible attribute.
     */
    public static final String APPLICATION = "application";

    /**
     * This is a possible attribute.
     */
    public static final String PARAMETERS = "parameters";

    /**
     * This is a possible attribute.
     */
    public static final String OPERATION = "operation";

    /**
     * This is a possible attribute.
     */
    public static final String DEFAULTDIR = "defaultdir";

    /**
     * This is a possible attribute.
     */
    public static final String LLX = "llx";

    /**
     * This is a possible attribute.
     */
    public static final String LLY = "lly";

    /**
     * This is a possible attribute.
     */
    public static final String URX = "urx";

    /**
     * This is a possible attribute.
     */
    public static final String URY = "ury";

    /**
     * This is the lower left x-value
     */
    protected float llx = Float.NaN;

    /**
     * This is the lower left y-value
     */
    protected float lly = Float.NaN;

    /**
     * This is the upper right x-value
     */
    protected float urx = Float.NaN;

    /**
     * This is the upper right y-value
     */
    protected float ury = Float.NaN;

    // constructors

    // implementation of the Element-methods

    /**
     * Gets the type of the text element.
     *
     * @return a type
     */
    public int type() {
        return Element.ANNOTATION;
    }

    /**
     * Gets all the chunks in this element.
     *
     * @return an <CODE>ArrayList</CODE>
     */

    // methods

    // methods to retrieve information

    /**
     * @see org.openpdf.text.Element#isContent()
     * @since iText 2.0.8
     */
    public boolean isContent() {
        return true;
    }

    /**
     * @see org.openpdf.text.Element#isNestable()
     * @since iText 2.0.8
     */
    public boolean isNestable() {
        return true;
    }

}
