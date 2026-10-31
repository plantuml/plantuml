/*
 * $Id: Phrase.java 4065 2009-09-16 23:09:11Z psoares33 $
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

import java.util.ArrayList;
import java.util.Collection;

/**
 * A <CODE>Phrase</CODE> is a series of <CODE>Chunk</CODE>s.
 * <p>
 * A <CODE>Phrase</CODE> has a main <CODE>Font</CODE>, but some chunks within the phrase can have a <CODE>Font</CODE>
 * that differs from the main <CODE>Font</CODE>. All the <CODE>Chunk</CODE>s in a <CODE>Phrase</CODE> have the same
 * <CODE>leading</CODE>.
 * <p>
 * Example:
 * <BLOCKQUOTE><PRE>
 * // When no parameters are passed, the default leading = 16
 * <STRONG>Phrase phrase0 = new Phrase();</STRONG>
 * <STRONG>Phrase phrase1 = new Phrase("this is a phrase");</STRONG>
 * // In this example the leading is passed as a parameter
 * <STRONG>Phrase phrase2 = new Phrase(16, "this is a phrase with leading 16");</STRONG>
 * // When a Font is passed (explicitly or embedded in a chunk), the default leading = 1.5 * size of the font
 * <STRONG>Phrase phrase3 = new Phrase("this is a phrase with a red, normal font Courier, size 12",
 * FontFactory.getFont(FontFactory.COURIER, 12, Font.NORMAL, new Color(255, 0, 0)));</STRONG>
 * <STRONG>Phrase phrase4 = new Phrase(new Chunk("this is a phrase"));</STRONG>
 * <STRONG>Phrase phrase5 = new Phrase(18, new Chunk("this is a phrase", FontFactory.getFont(FontFactory.HELVETICA, 16,
 * Font.BOLD, new Color(255, 0, 0)));</STRONG>
 * </PRE></BLOCKQUOTE>
 *
 * @see Element
 * @see Chunk
 * @see Paragraph
 * @see Anchor
 */

public class Phrase extends ArrayList<Element> implements TextElementArray {

    // constants
    private static final long serialVersionUID = 2643594602455068231L;

    // membervariables
    /**
     * This is the leading of this phrase.
     */
    protected float leading = Float.NaN;

    /**
     * This is the font of this phrase.
     */
    protected Font font;

    // constructors

    // implementation of the Element-methods

    // overriding some of the ArrayList-methods

    /**
     * Gets the type of the text element.
     *
     * @return a type
     */
    @Override
    public int type() {
        return Element.PHRASE;
    }

    /**
     * @see org.openpdf.text.Element#isContent()
     * @since iText 2.0.8
     */
    @Override
    public boolean isContent() {
        return true;
    }

    /**
     * @see org.openpdf.text.Element#isNestable()
     * @since iText 2.0.8
     */
    @Override
    public boolean isNestable() {
        return true;
    }

    /**
     * Adds a collection of <CODE>Chunk</CODE>s to this <CODE>Phrase</CODE>.
     *
     * @param collection a collection of <CODE>Chunk</CODE>s, <CODE>Anchor</CODE>s and <CODE>Phrase</CODE>s.
     * @return <CODE>true</CODE> if the action succeeded, <CODE>false</CODE> if not.
     * @throws ClassCastException when you try to add something that isn't a <CODE>Chunk</CODE>, <CODE>Anchor</CODE> or
     *                            <CODE>Phrase</CODE>
     */
    @Override
    public boolean addAll(Collection<? extends Element> collection) {
        for (Element o : collection) {
            this.add(o);
        }
        return true;
    }

    /**
     * Sets the leading of this phrase.
     *
     * @param leading the new leading
     */

    // kept for historical reasons; people should use FontSelector
    // eligible for deprecation, but the methods are mentioned in the book p277.

    /**
     * Checks is this <CODE>Phrase</CODE> contains no or 1 empty <CODE>Chunk</CODE>.
     *
     * @return <CODE>false</CODE> if the <CODE>Phrase</CODE>
     * contains more than one or more non-empty<CODE>Chunk</CODE>s.
     */
    @Override
    public boolean isEmpty() {
        switch (size()) {
            case 0:
                return true;
            case 1:
                Element element = get(0);
                return element.type() == Element.CHUNK && ((Chunk) element).isEmpty();
            default:
                return false;
        }
    }

}
