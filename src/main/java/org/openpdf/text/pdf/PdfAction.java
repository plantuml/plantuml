/*
 * $Id: PdfAction.java 4065 2009-09-16 23:09:11Z psoares33 $
 *
 * Copyright 2000 by Bruno Lowagie.
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
import java.net.URL;

/**
 * A <CODE>PdfAction</CODE> defines an action that can be triggered from a PDF file.
 *
 * @see PdfDictionary
 */

public class PdfAction extends PdfDictionary {

    /**
     * A named action to go to the first page.
     */
    public static final int FIRSTPAGE = 1;
    /**
     * A named action to go to the previous page.
     */
    public static final int PREVPAGE = 2;
    /**
     * A named action to go to the next page.
     */
    public static final int NEXTPAGE = 3;
    /**
     * A named action to go to the last page.
     */
    public static final int LASTPAGE = 4;

    /**
     * A named action to open a print dialog.
     */
    public static final int PRINTDIALOG = 5;

    // constructors

    /**
     * Create an empty action.
     */
    public PdfAction() {
    }

    /**
     * Constructs a new <CODE>PdfAction</CODE> of Subtype URI.
     *
     * @param url the Url to go to
     */

    public PdfAction(URL url) {
        this(url.toExternalForm());
    }

    /**
     * Construct a new <CODE>PdfAction</CODE> of Subtype URI that accepts the x and y coordinate of the position that
     * was clicked.
     *
     * @param url   the URL
     * @param isMap true if it is a map, false otherwise
     */
    public PdfAction(URL url, boolean isMap) {
        this(url.toExternalForm(), isMap);
    }

    /**
     * Constructs a new <CODE>PdfAction</CODE> of Subtype URI.
     *
     * @param url the url to go to
     */

    public PdfAction(String url) {
        this(url, false);
    }

    /**
     * Construct a new <CODE>PdfAction</CODE> of Subtype URI that accepts the x and y coordinate of the position that
     * was clicked.
     *
     * @param url   the URL
     * @param isMap true if it is a map, false otherwise
     */
    public PdfAction(String url, boolean isMap) {
        put(PdfName.S, PdfName.URI);
        put(PdfName.URI, new PdfString(url));
        if (isMap) {
            put(PdfName.ISMAP, PdfBoolean.PDFTRUE);
        }
    }

    /**
     * Constructs a new <CODE>PdfAction</CODE> of Subtype GoTo.
     *
     * @param destination the destination to go to
     */

    PdfAction(PdfIndirectReference destination) {
        put(PdfName.S, PdfName.GOTO);
        put(PdfName.D, destination);
    }

    /**
     * Constructs a new <CODE>PdfAction</CODE> of Subtype GoToR.
     *
     * @param filename the file name to go to
     * @param name     the named destination to go to
     */

    public PdfAction(String filename, String name) {
        put(PdfName.S, PdfName.GOTOR);
        put(PdfName.F, new PdfString(filename));
        put(PdfName.D, new PdfString(name));
    }

    /**
     * Constructs a new <CODE>PdfAction</CODE> of Subtype GoToR.
     *
     * @param filename the file name to go to
     * @param page     the page destination to go to
     */

    public PdfAction(String filename, int page) {
        put(PdfName.S, PdfName.GOTOR);
        put(PdfName.F, new PdfString(filename));
        put(PdfName.D, new PdfLiteral("[" + (page - 1) + " /FitH 10000]"));
    }

    /**
     * Implements name actions. The action can be FIRSTPAGE, LASTPAGE, NEXTPAGE, PREVPAGE and PRINTDIALOG.
     *
     * @param named the named action
     */
    public PdfAction(int named) {
        put(PdfName.S, PdfName.NAMED);
        switch (named) {
            case FIRSTPAGE:
                put(PdfName.N, PdfName.FIRSTPAGE);
                break;
            case LASTPAGE:
                put(PdfName.N, PdfName.LASTPAGE);
                break;
            case NEXTPAGE:
                put(PdfName.N, PdfName.NEXTPAGE);
                break;
            case PREVPAGE:
                put(PdfName.N, PdfName.PREVPAGE);
                break;
            case PRINTDIALOG:
                put(PdfName.S, PdfName.JAVASCRIPT);
                put(PdfName.JS, new PdfString("this.print(true);\r"));
                break;
            default:
                throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.named.action"));
        }
    }

}
