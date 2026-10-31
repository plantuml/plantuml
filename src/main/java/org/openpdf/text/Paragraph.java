/*
 * $Id: Paragraph.java 3668 2009-02-01 09:08:50Z blowagie $
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

import org.openpdf.text.pdf.PdfWriter;

/**
 * A <CODE>Paragraph</CODE> is a series of <CODE>Chunk</CODE>s and/or <CODE>Phrases</CODE>.
 * <p>
 * A <CODE>Paragraph</CODE> has the same qualities of a <CODE>Phrase</CODE>, but also some additional
 * layout-parameters:
 * <UL>
 * <LI>the indentation
 * <LI>the alignment of the text
 * </UL>
 * <p>
 * Example:
 * <BLOCKQUOTE><PRE>
 * <STRONG>Paragraph p = new Paragraph("This is a paragraph",
 * FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLDITALIC, new Color(0, 0, 255)));</STRONG>
 * </PRE></BLOCKQUOTE>
 *
 * @see Element
 * @see Phrase
 * @see ListItem
 */

public class Paragraph extends Phrase {

    // constants
    private static final long serialVersionUID = 7852314969733375514L;

    // membervariables

    /**
     * The alignment of the text.
     */
    protected int alignment = Element.ALIGN_UNDEFINED;

    /**
     * The base run direction of paragraph
     */
    protected int runDirection = PdfWriter.RUN_DIRECTION_DEFAULT;

    /**
     * The indentation of this paragraph on the left side.
     */
    protected float indentationLeft;

    /**
     * The indentation of this paragraph on the right side.
     */
    protected float indentationRight;
    /**
     * The spacing before the paragraph.
     */
    protected float spacingBefore;
    /**
     * The spacing after the paragraph.
     */
    protected float spacingAfter;
    // constructors

    // implementation of the Element-methods

    /**
     * Gets the type of the text element.
     *
     * @return a type
     */
    public int type() {
        return Element.PARAGRAPH;
    }

    // setting the membervariables

    // methods to retrieve information

}
