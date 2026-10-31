/*
 * $Id: Section.java 4065 2009-09-16 23:09:11Z psoares33 $
 * $Name$
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

import org.openpdf.text.error_messages.MessageLocalization;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A <CODE>Section</CODE> is a part of a <CODE>Document</CODE> containing other <CODE>Section</CODE>s,
 * <CODE>Paragraph</CODE>s, <CODE>List</CODE> and/or <CODE>Table</CODE>s.
 * <p>
 * Remark: you can not construct a <CODE>Section</CODE> yourself. You will have to ask an instance of
 * <CODE>Section</CODE> to the
 * <CODE>Chapter</CODE> or <CODE>Section</CODE> to which you want to
 * add the new <CODE>Section</CODE>.
 * <p>
 * Example:
 * <BLOCKQUOTE><PRE>
 * Paragraph title2 = new Paragraph("This is Chapter 2", FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLDITALIC,
 * new Color(0, 0, 255))); Chapter chapter2 = new Chapter(title2, 2); Paragraph someText = new Paragraph("This is some
 * text"); chapter2.add(someText); Paragraph title21 = new Paragraph("This is Section 1 in Chapter 2",
 * FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new Color(255, 0, 0)));
 * <STRONG>Section section1 = chapter2.addSection(title21);</STRONG>
 * Paragraph someSectionText = new Paragraph("This is some silly paragraph in a chapter and/or section. It contains some
 * text to test the functionality of Chapters and Section.");
 * <STRONG>section1.add(someSectionText);</STRONG>
 * Paragraph title211 = new Paragraph("This is SubSection 1 in Section 1 in Chapter 2",
 * FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, new Color(255, 0, 0)));
 * <STRONG>Section section11 = section1.addSection(40, title211, 2);</STRONG>
 * <STRONG>section11.add(someSectionText);</STRONG>
 * </PRE></BLOCKQUOTE>
 */

public class Section extends ArrayList<Element> implements TextElementArray, LargeElement {
    // constant
    /**
     * A possible number style. The default number style: "1.2.3."
     *
     * @since iText 2.0.8
     */
    public static final int NUMBERSTYLE_DOTTED = 0;
    /**
     * A possible number style. For instance: "1.2.3"
     *
     * @since iText 2.0.8
     */
    public static final int NUMBERSTYLE_DOTTED_WITHOUT_FINAL_DOT = 1;

    /**
     * A serial version uid.
     */
    private static final long serialVersionUID = 3324172577544748043L;

    // member variables

    /**
     * The title of this section.
     */
    protected Paragraph title;

    /**
     * The number of sectionnumbers that has to be shown before the section title.
     */
    protected int numberDepth;

    /**
     * The style for sectionnumbers.
     *
     * @since iText 2.0.8
     */
    protected int numberStyle = NUMBERSTYLE_DOTTED;

    /**
     * The indentation of this section on the left side.
     */
    protected float indentationLeft;

    /**
     * The indentation of this section on the right side.
     */
    protected float indentationRight;

    /**
     * The additional indentation of the content of this section.
     */
    protected float indentation;

    /**
     * This is the complete list of sectionnumbers of this section and the parents of this section.
     */
    protected java.util.List<Integer> numbers = null;

    /**
     * Indicates if the Section will be complete once added to the document.
     *
     * @since iText 2.0.8
     */
    protected boolean complete = true;

    /**
     * Indicates if the Section was added completely to the document.
     *
     * @since iText 2.0.8
     */
    protected boolean addedCompletely = false;

    /**
     * Indicates if this is the first time the section was added.
     *
     * @since iText 2.0.8
     */
    protected boolean notAddedYet = true;

    // constructors

    // implementation of the Element-methods

    /**
     * Constructs a Paragraph that will be used as title for a Section or Chapter.
     *
     * @param title       the title of the section
     * @param numbers     a list of sectionnumbers
     * @param numberDepth how many numbers have to be shown
     * @param numberStyle the numbering style
     * @return a Paragraph object
     * @since iText 2.0.8
     */
    public static Paragraph constructTitle(Paragraph title, java.util.List<Integer> numbers, int numberDepth,
            int numberStyle) {
        if (title == null) {
            return null;
        }

        int depth = Math.min(numbers.size(), numberDepth);
        if (depth < 1) {
            return title;
        }
        StringBuilder buf = new StringBuilder(" ");
        for (int i = 0; i < depth; i++) {
            buf.insert(0, ".");
            buf.insert(0, numbers.get(i).intValue());
        }
        if (numberStyle == NUMBERSTYLE_DOTTED_WITHOUT_FINAL_DOT) {
            buf.deleteCharAt(buf.length() - 2);
        }
        Paragraph result = new Paragraph(title);
        result.add(0, new Chunk(buf.toString(), title.getFont()));
        return result;
    }

    /**
     * Processes the element by adding it (or the different parts) to an
     * <CODE>ElementListener</CODE>.
     *
     * @param listener the <CODE>ElementListener</CODE>
     * @return <CODE>true</CODE> if the element was processed successfully
     */
    public boolean process(ElementListener listener) {
        try {
            Element element;
            for (Object o : this) {
                element = (Element) o;
                listener.add(element);
            }
            return true;
        } catch (DocumentException de) {
            return false;
        }
    }

    /**
     * Gets the type of the text element.
     *
     * @return a type
     */
    public int type() {
        return Element.SECTION;
    }

    /**
     * Gets all the chunks in this element.
     *
     * @return an <CODE>ArrayList</CODE>
     */
    public ArrayList<Element> getChunks() {
        ArrayList<Element> tmp = new ArrayList<>();
        for (Element o : this) {
            tmp.addAll(o.getChunks());
        }
        return tmp;
    }

    /**
     * @see org.openpdf.text.Element#isContent()
     * @since iText 2.0.8
     */
    public boolean isContent() {
        return true;
    }

    // overriding some of the ArrayList-methods

    /**
     * @see org.openpdf.text.Element#isNestable()
     * @since iText 2.0.8
     */
    public boolean isNestable() {
        return false;
    }

    /**
     * Adds a <CODE>Paragraph</CODE>, <CODE>List</CODE> or <CODE>Table</CODE> to this <CODE>Section</CODE>.
     *
     * @param index index at which the specified element is to be inserted
     * @param o     an object of type <CODE>Paragraph</CODE>, <CODE>List</CODE> or <CODE>Table</CODE>=
     * @throws ClassCastException if the object is not a <CODE>Paragraph</CODE>, <CODE>List</CODE> or
     *                            <CODE>Table</CODE>
     */
    public void add(int index, Element o) {
        if (isAddedCompletely()) {
            throw new IllegalStateException(
                    MessageLocalization.getComposedMessage("this.largeelement.has.already.been.added.to.the.document"));
        }
        try {
            Element element = o;
            if (element.isNestable()) {
                super.add(index, element);
            } else {
                throw new ClassCastException(MessageLocalization.getComposedMessage("you.can.t.add.a.1.to.a.section",
                        element.getClass().getName()));
            }
        } catch (ClassCastException cce) {
            throw new ClassCastException(
                    MessageLocalization.getComposedMessage("insertion.of.illegal.element.1", cce.getMessage()));
        }
    }

    // methods that return a Section

    /**
     * Adds a collection of <CODE>Element</CODE>s to this <CODE>Section</CODE>.
     *
     * @param collection a collection of <CODE>Paragraph</CODE>s, <CODE>List</CODE>s and/or <CODE>Table</CODE>s
     * @return <CODE>true</CODE> if the action succeeded, <CODE>false</CODE> if not.
     * @throws ClassCastException if one of the objects isn't a <CODE>Paragraph</CODE>, <CODE>List</CODE>,
     *                            <CODE>Table</CODE>
     */
    public boolean addAll(Collection<? extends Element> collection) {
        for (Element o : collection) {
            this.add(o);
        }
        return true;
    }

    // public methods

    /**
     * Returns the title, preceded by a certain number of sectionnumbers.
     *
     * @return a <CODE>Paragraph</CODE>
     */
    public Paragraph getTitle() {
        return constructTitle(title, numbers, numberDepth, numberStyle);
    }

    /**
     * Returns the indentation of this <CODE>Section</CODE> on the left side.
     *
     * @return the indentation
     */
    public float getIndentationLeft() {
        return indentationLeft;
    }

    /**
     * Sets the indentation of this <CODE>Section</CODE> on the left side.
     *
     * @param indentation the indentation
     */
    public void setIndentationLeft(float indentation) {
        indentationLeft = indentation;
    }

    /**
     * Returns the indentation of this <CODE>Section</CODE> on the right side.
     *
     * @return the indentation
     */
    public float getIndentationRight() {
        return indentationRight;
    }

    /**
     * Sets the indentation of this <CODE>Section</CODE> on the right side.
     *
     * @param indentation the indentation
     */
    public void setIndentationRight(float indentation) {
        indentationRight = indentation;
    }

    /**
     * Changes the Chapter number.
     *
     * @param number new chapter number
     */
    public void setChapterNumber(int number) {
        numbers.set(numbers.size() - 1, number);
        Object s;
        for (Object o : this) {
            s = o;
            if (s instanceof Section) {
                ((Section) s).setChapterNumber(number);
            }
        }
    }

    // private methods

    /**
     * Sets the indication if the section was already added to the document.
     *
     * @param notAddedYet {@link Section#notAddedYet}
     * @since iText2.0.8
     */
    public void setNotAddedYet(boolean notAddedYet) {
        this.notAddedYet = notAddedYet;
    }

    /**
     * @return boolean, <code>true</code> if section is added completely, else <code>false</code>
     * @since iText 2.0.8
     */
    protected boolean isAddedCompletely() {
        return addedCompletely;
    }

    /**
     * @param addedCompletely {@link Section#addedCompletely}
     * @since iText 2.0.8
     */
    protected void setAddedCompletely(boolean addedCompletely) {
        this.addedCompletely = addedCompletely;
    }

    /**
     * @see org.openpdf.text.LargeElement#flushContent()
     * @since iText 2.0.8
     */
    public void flushContent() {
        setNotAddedYet(false);
        title = null;
        Element element;
        for (Iterator i = iterator(); i.hasNext(); ) {
            element = (Element) i.next();
            if (element instanceof Section) {
                Section s = (Section) element;
                if (!s.isComplete() && size() == 1) {
                    s.flushContent();
                    return;
                } else {
                    s.setAddedCompletely(true);
                }
            }
            i.remove();
        }
    }

    /**
     * @see org.openpdf.text.LargeElement#isComplete()
     * @since iText 2.0.8
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * @see org.openpdf.text.LargeElement#setComplete(boolean)
     * @since iText 2.0.8
     */
    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    /**
     * Adds a new page to the section.
     *
     * @since 2.1.1
     */
    public void newPage() {
        this.add(Chunk.NEXTPAGE);
    }
}
