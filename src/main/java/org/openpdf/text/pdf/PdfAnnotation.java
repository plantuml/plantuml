/*
 * $Id: PdfAnnotation.java 4065 2009-09-16 23:09:11Z psoares33 $
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

import org.openpdf.text.Rectangle;

/**
 * A <CODE>PdfAnnotation</CODE> is a note that is associated with a page.
 *
 * @see PdfDictionary
 */

public class PdfAnnotation extends PdfDictionary {

    /**
     * highlight attributename
     */
    public static final PdfName HIGHLIGHT_INVERT = PdfName.I;
    /**
     * flagvalue
     */
    public static final int FLAGS_PRINT = 4;
    protected PdfWriter writer;
    /**
     * Reference to this annotation.
     *
     * @since 2.1.6; was removed in 2.1.5, but restored in 2.1.6
     */
    protected PdfIndirectReference reference;
    protected boolean annotation = true;

    // constructors
    public PdfAnnotation(PdfWriter writer, Rectangle rect) {
        this.writer = writer;
        if (rect != null) {
            put(PdfName.RECT, new PdfRectangle(rect));
        }
        addFKey();
    }

    /**
     * Constructs a new <CODE>PdfAnnotation</CODE> of subtype link (Action).
     *
     * @param writer pdf writer
     * @param llx    lower-left-x
     * @param lly    lower-left-y
     * @param urx    upper-right-x
     * @param ury    upper-right-y
     * @param action {@link PdfAction}
     */

    /**
     * Creates a link.
     *
     * @param writer    {@link PdfWriter}
     * @param rect      dimensions {@link Rectangle}
     * @param highlight the highlight property
     * @return A PdfAnnotation
     */
    protected static PdfAnnotation createLink(PdfWriter writer, Rectangle rect, PdfName highlight) {
        PdfAnnotation annot = new PdfAnnotation(writer, rect);
        annot.put(PdfName.SUBTYPE, PdfName.LINK);
        if (!highlight.equals(HIGHLIGHT_INVERT)) {
            annot.put(PdfName.H, highlight);
        }
        return annot;
    }

    /**
     * Creates an Annotation with an Action.
     *
     * @param writer    {@link PdfWriter}
     * @param rect      dimensions {@link Rectangle}
     * @param highlight title for the link
     * @param action    action for annotation
     * @return A PdfAnnotation
     */
    public static PdfAnnotation createLink(PdfWriter writer, Rectangle rect, PdfName highlight, PdfAction action) {
        PdfAnnotation annot = createLink(writer, rect, highlight);
        annot.putEx(PdfName.A, action);
        return annot;
    }

    /**
     * Creates an Annotation with an local destination.
     *
     * @param writer           {@link PdfWriter}
     * @param rect             dimensions {@link Rectangle}
     * @param highlight        title for the link
     * @param namedDestination destination for pdf annotation
     * @return A PdfAnnotation
     */
    public static PdfAnnotation createLink(PdfWriter writer, Rectangle rect, PdfName highlight,
            String namedDestination) {
        PdfAnnotation annot = createLink(writer, rect, highlight);
        annot.put(PdfName.DEST, new PdfString(namedDestination));
        return annot;
    }

    /**
     * Returns an indirect reference to the annotation
     *
     * @return the indirect reference
     */
    public PdfIndirectReference getIndirectReference() {
        if (reference == null) {
            reference = writer.getPdfIndirectReference();
        }
        return reference;
    }

    private void addFKey() {
        if (writer.isPdfA1()) {
            put(PdfName.F, new PdfNumber(FLAGS_PRINT));
        }
    }
}
