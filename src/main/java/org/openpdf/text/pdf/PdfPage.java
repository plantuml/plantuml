/*
 * $Id: PdfPage.java 3373 2008-05-12 16:21:24Z xlv $
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

import java.util.HashMap;

/**
 * <CODE>PdfPage</CODE> is the PDF Page-object.
 * <p>
 * A Page object is a dictionary whose keys describe a single page containing text, graphics, and images. A Page object
 * is a leaf of the Pages tree.<BR> This object is described in the 'Portable Document Format Reference Manual version
 * 1.3' section 6.4 (page 73-81)
 *
 * @see PdfPages
 */

public class PdfPage extends PdfDictionary {

    /**
     * value of the <B>Rotate</B> key for a page in PORTRAIT
     */
    public static final PdfNumber PORTRAIT = new PdfNumber(0);
    /**
     * value of the <B>Rotate</B> key for a page in LANDSCAPE
     */
    public static final PdfNumber LANDSCAPE = new PdfNumber(90);
    // membervariables
    /**
     * value of the <B>Rotate</B> key for a page in INVERTEDPORTRAIT
     */
    public static final PdfNumber INVERTEDPORTRAIT = new PdfNumber(180);
    /**
     * value of the <B>Rotate</B> key for a page in SEASCAPE
     */
    public static final PdfNumber SEASCAPE = new PdfNumber(270);
    private static final String[] boxStrings = {"crop", "trim", "art", "bleed"};
    private static final PdfName[] boxNames = {PdfName.CROPBOX, PdfName.TRIMBOX, PdfName.ARTBOX, PdfName.BLEEDBOX};
    /**
     * value of the <B>MediaBox</B> key
     */
    PdfRectangle mediaBox;

    // constructors

    /**
     * Constructs a <CODE>PdfPage</CODE>.
     *
     * @param mediaBox  a value for the <B>MediaBox</B> key
     * @param resources an indirect reference to a <CODE>PdfResources</CODE>-object
     * @param rotate    a value for the <B>Rotate</B> key
     */

    PdfPage(PdfRectangle mediaBox, HashMap<String, ? extends PdfObject> boxSize, PdfDictionary resources, int rotate) {
        super(PAGE);
        this.mediaBox = mediaBox;
        put(PdfName.MEDIABOX, mediaBox);
        put(PdfName.RESOURCES, resources);
        if (rotate != 0) {
            put(PdfName.ROTATE, new PdfNumber(rotate));
        }
        for (int k = 0; k < boxStrings.length; ++k) {
            PdfObject rect = boxSize.get(boxStrings[k]);
            if (rect != null) {
                put(boxNames[k], rect);
            }
        }
    }

    /**
     * Constructs a <CODE>PdfPage</CODE>.
     *
     * @param mediaBox  a value for the <B>MediaBox</B> key
     * @param resources an indirect reference to a <CODE>PdfResources</CODE>-object
     */

    PdfPage(PdfRectangle mediaBox, HashMap<String, ? extends PdfObject> boxSize, PdfDictionary resources) {
        this(mediaBox, boxSize, resources, 0);
    }

    /**
     * Checks if this page element is a tree of pages.
     * <p>
     * This method always returns <CODE>false</CODE>.
     *
     * @return <CODE>false</CODE> because this is a single page
     */

    public boolean isParent() {
        return false;
    }

    // methods

    /**
     * Adds an indirect reference pointing to a <CODE>PdfContents</CODE>-object.
     *
     * @param contents an indirect reference to a <CODE>PdfContents</CODE>-object
     */

    void add(PdfIndirectReference contents) {
        put(PdfName.CONTENTS, contents);
    }

    /**
     * Rotates the mediabox, but not the text in it.
     *
     * @return a <CODE>PdfRectangle</CODE>
     */

    PdfRectangle rotateMediaBox() {
        this.mediaBox = mediaBox.rotate();
        put(PdfName.MEDIABOX, this.mediaBox);
        return this.mediaBox;
    }

    /**
     * Returns the MediaBox of this Page.
     *
     * @return a <CODE>PdfRectangle</CODE>
     */

    PdfRectangle getMediaBox() {
        return mediaBox;
    }
}
