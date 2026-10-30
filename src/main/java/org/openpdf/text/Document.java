/*
 * $Id: Document.java 4106 2009-11-27 12:59:39Z blowagie $
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
import org.openpdf.text.pdf.NativePlatform;
import org.openpdf.text.pdf.PdfDate;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;


/**
 * A generic Document class.
 * <p>
 * All kinds of Text-elements can be added to a <CODE>HTMLDocument</CODE>. The <CODE>Document</CODE> signals all the
 * listeners when an element has been added.
 * <p>
 * Remark:
 * <OL>
 * <LI>Once a document is created you can add some meta information.
 * <LI>You can also set the headers/footers.
 * <LI>You have to open the document before you can write content.
 * <LI>You can only write content (no more meta-formation!) once a document is
 * opened.
 * <LI>When you change the header/footer on a certain page, this will be
 * effective starting on the next page.
 * <LI>After closing the document, every listener (as well as its <CODE>
 * OutputStream</CODE>) is closed too.
 * </OL>
 * Example: <BLOCKQUOTE>
 *
 * <PRE>// creation of the document with a certain size and certain margins
 * <STRONG>Document document = new Document(PageSize.A4, 50, 50, 50, 50);
 * </STRONG> try {
 * // creation of the different writers HtmlWriter.getInstance(<STRONG>document </STRONG>, System.out);
 * PdfWriter.getInstance(<STRONG>document </STRONG>, new FileOutputStream("text.pdf")); // we add some meta information
 * to the document
 * <STRONG>document.addAuthor("Bruno Lowagie"); </STRONG>
 * <STRONG>document.addSubject("This is the result of a Test."); </STRONG>
 * // we open the document for writing
 * <STRONG>document.open(); </STRONG>
 * <STRONG>document.add(new Paragraph("Hello world"));</STRONG>
 * } catch(DocumentException de) { System.err.println(de.getMessage()); }
 * <STRONG>document.close();</STRONG>
 * </PRE>
 *
 * </BLOCKQUOTE>
 */

public class Document implements DocListener {

    // membervariables
    private static final String VERSION_PROPERTIES = "org/openpdf/text/version.properties";
    private static final String OPENPDF = "OpenPDF";
    private static final String RELEASE;
    private static final String OPENPDF_VERSION;
    /**
     * Allows the pdf documents to be produced without compression for debugging purposes.
     */
    public static boolean compress = true;
    /**
     * When true the file access is not done through a memory mapped file. Use it if the file is too big to be mapped in
     * your address space.
     */
    public static boolean plainRandomAccess = false;
    /**
     * Scales the WMF font size. The default value is 0.86.
     */
    public static float wmfFontCorrection = 0.86f;

    static {
        RELEASE = getVersionNumber();
        OPENPDF_VERSION = OPENPDF + " " + RELEASE;
    }

    /**
     * The DocListener.
     */
    private final List<DocListener> listeners = new ArrayList<>();

    /**
     * Is the document open or not?
     */
    protected boolean open;
    /**
     * Has the document already been closed?
     */
    protected boolean close;
    /**
     * The size of the page.
     */
    protected Rectangle pageSize;

    // membervariables concerning the layout
    /**
     * margin in x direction starting from the left
     */
    protected float marginLeft = 0;
    /**
     * margin in x direction starting from the right
     */
    protected float marginRight = 0;
    /**
     * margin in y direction starting from the top
     */
    protected float marginTop = 0;
    /**
     * margin in y direction starting from the bottom
     */
    protected float marginBottom = 0;
    /**
     * mirroring of the left/right margins
     */
    protected boolean marginMirroring = false;
    /**
     * mirroring of the top/bottom margins
     *
     * @since 2.1.6
     */
    protected boolean marginMirroringTopBottom = false;
    /**
     * Content of JavaScript onLoad function
     */
    protected String javaScript_onLoad = null;
    /**
     * Content of JavaScript onUnLoad function
     */
    protected String javaScript_onUnLoad = null;
    /**
     * Style class in HTML body tag
     */
    protected String htmlStyleClass = null;
    /**
     * Current pagenumber
     */
    protected int pageN = 0;
    /**
     * This is a chapter number in case ChapterAutoNumber is used.
     */
    protected int chapternumber = 0;
    /**
     * Text rendering options, including the default language of the document and a flag to enable font glyph
     * substitution (if FOP is available)
     *
     * @since 3.1.15
     */
    TextRenderingOptions textRenderingOptions = new TextRenderingOptions();

    /**
     * Constructs a new <CODE>Document</CODE> -object with the default page size as <CODE>PageSize.A4</CODE> .
     */
    public Document() {
        this(PageSize.A4);
    }

    // constructor

    /**
     * Constructs a new <CODE>Document</CODE> -object using the given rectangle as page size and default margin of 36.
     *
     * @param pageSize the pageSize
     */
    public Document(Rectangle pageSize) {
        this(pageSize, 36, 36, 36, 36);
    }

    /**
     * Constructs a new <CODE>Document</CODE> -object.
     *
     * @param pageSize     the pageSize
     * @param marginLeft   the margin on the left
     * @param marginRight  the margin on the right
     * @param marginTop    the margin on the top
     * @param marginBottom the margin on the bottom
     */
    public Document(Rectangle pageSize, float marginLeft, float marginRight,
            float marginTop, float marginBottom) {
        this.pageSize = pageSize;
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
        this.marginTop = marginTop;
        this.marginBottom = marginBottom;
    }

    private static String getVersionNumber() {
        String releaseVersion = "UNKNOWN";
        // The marker is tested here, so that TeaVM (C) prunes the class loader code
        if (NativePlatform.isC()) {
            return releaseVersion;
        }
        try (InputStream input = Document.class.getClassLoader()
                .getResourceAsStream(VERSION_PROPERTIES)) {
            if (input != null) {
                Properties prop = new Properties();
                prop.load(input);
                releaseVersion = prop.getProperty("bundleVersion", releaseVersion);
            }
        } catch (IOException ignored) {
            // ignore this and leave the default
        }
        return releaseVersion;
    }

    // listener methods

    /**
     * Gets the product name.
     *
     * @return the product name
     * @since 2.1.6
     */
    public static String getProduct() {
        return OPENPDF;
    }

    /**
     * Gets the release number.
     *
     * @return the product name
     * @since 2.1.6
     */
    public static String getRelease() {
        return RELEASE;
    }

    // methods implementing the DocListener interface

    /**
     * Gets the iText version.
     *
     * @return iText version
     */
    public static String getVersion() {
        return OPENPDF_VERSION;
    }

    /**
     * Adds a <CODE>DocListener</CODE> to the <CODE>Document</CODE>.
     *
     * @param listener the new DocListener.
     */
    public void addDocListener(DocListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a <CODE>DocListener</CODE> from the <CODE>Document</CODE>.
     *
     * @param listener the DocListener that has to be removed.
     */
    public void removeDocListener(DocListener listener) {
        listeners.remove(listener);
    }

    /**
     * Adds an <CODE>Element</CODE> to the <CODE>Document</CODE>.
     *
     * @param element the <CODE>Element</CODE> to add
     * @return <CODE>true</CODE> if the element was added, <CODE>false
     * </CODE> if not
     * @throws DocumentException when a document isn't open yet, or has been closed
     */
    public boolean add(Element element) throws DocumentException {
        if (close) {
            throw new DocumentException(
                    MessageLocalization.getComposedMessage("the.document.has.been.closed.you.can.t.add.any.elements"));
        }
        if (!open && element.isContent()) {
            throw new DocumentException(MessageLocalization.getComposedMessage(
                    "the.document.is.not.open.yet.you.can.only.add.meta.information"));
        }
        boolean success = false;
        if (element instanceof ChapterAutoNumber) {
            chapternumber = ((ChapterAutoNumber) element).setAutomaticNumber(chapternumber);
        }
        for (DocListener listener : listeners) {
            success |= listener.add(element);
        }
        if (element instanceof LargeElement e) {
            if (!e.isComplete()) {
                e.flushContent();
            }
        }
        return success;
    }

    /**
     * Opens the document.
     * <p>
     * Once the document is opened, you can't write any meta-information anymore. If you change the header/footer after
     * opening the document, the change will be effective starting from the second page. You have to open the document
     * before you can begin to add content to the body of the document.
     */
    public void open() {
        if (!close) {
            open = true;
        }
        for (DocListener listener : listeners) {
            listener.setPageSize(pageSize);
            listener.setMargins(marginLeft, marginRight, marginTop,
                    marginBottom);
            listener.open();
        }
    }

    /**
     * Sets the pagesize.
     * <p>
     * This change will be effective starting from the next page. If you want to change the page size of the first page,
     * you need to set it before opening the document.
     *
     * @param pageSize the new pagesize
     * @return a <CODE>boolean</CODE>
     */
    public boolean setPageSize(Rectangle pageSize) {
        this.pageSize = pageSize;
        for (DocListener listener : listeners) {
            listener.setPageSize(pageSize);
        }
        return true;
    }

    /**
     * Sets the margins.
     * <p>
     * This change will be effective starting from the next page. If you want to change margins on the first page, you
     * need to set it before opening the document.
     * <p>
     * A new page is created when you call:
     *     <ul>
     *         <li>Document.open()</li>
     *         <li>Document.newPage(), only if the current page has some content, otherwise no new page will be created</li>
     *     </ul>
     * </p>
     *
     * @param marginLeft   the margin on the left
     * @param marginRight  the margin on the right
     * @param marginTop    the margin on the top
     * @param marginBottom the margin on the bottom
     * @return a <CODE>boolean</CODE>
     */
    public boolean setMargins(float marginLeft, float marginRight,
            float marginTop, float marginBottom) {
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
        this.marginTop = marginTop;
        this.marginBottom = marginBottom;
        for (DocListener listener : listeners) {
            listener.setMargins(marginLeft, marginRight, marginTop,
                    marginBottom);
        }
        return true;
    }

    /**
     * Signals to all listeners, that a new page has to be started. New pages can only be added on already opened and
     * not yet closed documents.
     *
     * @return <CODE>true</CODE> if the page was added, <CODE>false</CODE>
     * if not.
     */
    public boolean newPage() {
        if (!open || close) {
            return false;
        }
        for (DocListener listener : listeners) {
            listener.newPage();
        }
        return true;
    }

    /**
     * Sets the page number to 0.
     * <p>
     * This change will be effective starting from the next page.
     */
    public void resetPageCount() {
        pageN = 0;
        for (DocListener listener : listeners) {
            listener.resetPageCount();
        }
    }

    // methods concerning the header or some meta information

    /**
     * Sets the page number.
     * <p>
     * This change will be effective starting from the next page.
     * <p>
     * <STRONG>The page number of the next new page will be: <CODE>pageN + 1</CODE></STRONG>
     *
     * @param pageN the new page number
     */
    public void setPageCount(int pageN) {
        this.pageN = pageN;
        for (DocListener listener : listeners) {
            listener.setPageCount(pageN);
        }
    }

    /**
     * Returns the current page number.
     *
     * @return the current page number
     */
    public int getPageNumber() {
        return this.pageN;
    }

    /**
     * Closes the document.
     * <p>
     * Once all the content has been written in the body, you have to close the body. After that nothing can be written
     * to the body anymore.
     */
    @Override
    public void close() {
        if (!close) {
            open = false;
            close = true;
        }
        for (DocListener listener : listeners) {
            listener.close();
        }
    }

    /**
     * Adds a user defined header to the document.
     * <p/>
     * Shortcut method to call: <CODE>add(new Header(name, content))</CODE>
     *
     * @param name    the name of the header
     * @param content the content of the header
     * @return <CODE>true</CODE> if successful, <CODE>false</CODE> otherwise
     */
    public boolean addHeader(String name, String content) {
        try {
            return add(new Header(name, content));
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    /**
     * Adds the title to a Document.
     * <p/>
     * In case of a PDF this will be visible in the document properties panel.
     * <p/>
     * In case of a HTML file this will be visible as the <CODE>title</CODE> <CODE>meta</CODE> tag in the
     * <CODE>HEAD</CODE> part of the file.
     * <p/>
     * Shortcut method to call: <CODE>add(new Meta(Element.TITLE, title))</CODE>
     *
     * @param title the title
     * @return <CODE>true</CODE> if successful, <CODE>false</CODE> otherwise
     */
    public boolean addTitle(String title) {
        try {
            return add(new Meta(Element.TITLE, title));
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    /**
     * Adds the subject to a Document.
     * <p/>
     * In case of a PDF this will be visible in the document properties panel.
     * <p/>
     * In case of a HTML file this will be visible as the <CODE>subject</CODE> <CODE>meta</CODE> tag in the
     * <CODE>HEAD</CODE> part of the file.
     *
     * @param subject the subject
     * @return <CODE>true</CODE> if successful, <CODE>false</CODE> otherwise
     */
    public boolean addSubject(String subject) {
        try {
            return add(new Meta(Element.SUBJECT, subject));
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    /**
     * Adds the keywords to a Document.
     * <p/>
     * In case of a PDF this will be visible in the document properties panel.
     * <p/>
     * In case of a HTML file this will be visible as the <CODE>keywords</CODE> <CODE>meta</CODE> tag in the
     * <CODE>HEAD</CODE> part of the file.
     *
     * @param keywords adds the keywords to the document
     * @return <CODE>true</CODE> if successful, <CODE>false</CODE> otherwise
     */
    public boolean addKeywords(String keywords) {
        try {
            return add(new Meta(Element.KEYWORDS, keywords));
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    /**
     * Adds the author to a Document.
     * <p/>
     * In case of a PDF this will be visible in the document properties panel.
     * <p/>
     * In case of a HTML file this will be visible as the <CODE>author</CODE> <CODE>meta</CODE> tag in the
     * <CODE>HEAD</CODE> part of the file.
     *
     * @param author the name of the author
     * @return <CODE>true</CODE> if successful, <CODE>false</CODE> otherwise
     */
    public boolean addAuthor(String author) {
        try {
            return add(new Meta(Element.AUTHOR, author));
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    /**
     * Adds the creator to a Document.
     * <p/>
     * In case of a PDF this will be visible in the document properties panel.
     * <p/>
     * In case of a HTML file this will be visible as comment in the <CODE>HEAD</CODE> part of the file.
     *
     * @param creator the name of the creator
     * @return <CODE>true</CODE> if successful, <CODE>false</CODE> otherwise
     */
    public boolean addCreator(String creator) {
        try {
            return add(new Meta(Element.CREATOR, creator));
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    /**
     * Adds the producer to a Document.
     * <p/>
     * In case of a PDF this will be visible in the document properties panel.
     * <p/>
     * In case of a HTML file this will be visible as comment in the <CODE>HEAD</CODE> part of the file.
     *
     * @return <CODE>true</CODE> if successful, <CODE>false</CODE> otherwise
     */
    public boolean addProducer() {
        return this.addProducer(getVersion());
    }

    /**
     * Adds the provided value as the producer to a Document.
     * <p/>
     * The default producer is <CODE>OpenPDF XX.YY.ZZ</CODE> where <CODE>XX.YY.ZZ</CODE> is the version of the OpenPDF
     * library used to produce the document
     * <p/>
     * In case of a PDF this will be visible in the document properties panel.
     * <p/>
     * In case of a HTML file this will be visible as comment in the <CODE>HEAD</CODE> part of the file.
     *
     * @param producer new producer line value
     * @return <CODE>true</CODE> if successful, <CODE>false</CODE> otherwise
     */
    public boolean addProducer(final String producer) {
        return add(new Meta(Element.PRODUCER, producer));
    }

    /**
     * Adds the current date and time to a Document.
     * <p/>
     * In case of a PDF this will be visible in the document properties panel.
     * <p/>
     * In case of a HTML file this will be visible as comment in the <CODE>HEAD</CODE> part of the file.
     *
     * @return <CODE>true</CODE> if successful, <CODE>false</CODE> otherwise
     */
    public boolean addCreationDate() {
        try {
            /* bugfix by 'taqua' (Thomas) */
            final SimpleDateFormat sdf = new SimpleDateFormat(
                    "EEE MMM dd HH:mm:ss zzz yyyy");
            return add(new Meta(Element.CREATIONDATE, sdf.format(new Date())));
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    // methods to get the layout of the document.

    /**
     * Adds the current date and time to a Document.
     *
     * @return <CODE>true</CODE> if successful, <CODE>false</CODE> otherwise
     */
    public boolean addCreationDate(PdfDate date) {
        try {
            return add(new Meta(Element.CREATIONDATE, date.toString()));
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    /**
     * Adds the current date and time to a Document.
     *
     * @return <CODE>true</CODE> if successful, <CODE>false</CODE> otherwise
     */
    public boolean addModificationDate() {
        try {
            return add(new Meta(Element.MODIFICATIONDATE, new PdfDate().toString()));
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    /**
     * Adds the current date and time to a Document.
     *
     * @return <CODE>true</CODE> if successful, <CODE>false</CODE> otherwise
     */
    public boolean addModificationDate(PdfDate date) {
        try {
            return add(new Meta(Element.MODIFICATIONDATE, date.toString()));
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    /**
     * Returns the left margin.
     *
     * @return the left margin
     */

    public float leftMargin() {
        return marginLeft;
    }

    /**
     * Return the right margin.
     *
     * @return the right margin
     */

    public float rightMargin() {
        return marginRight;
    }

    /**
     * Returns the top margin.
     *
     * @return the top margin
     */

    public float topMargin() {
        return marginTop;
    }

    /**
     * Returns the bottom margin.
     *
     * @return the bottom margin
     */

    public float bottomMargin() {
        return marginBottom;
    }

    /**
     * Returns the lower left x-coordinate.
     *
     * @return the lower left x-coordinate
     */

    public float left() {
        return pageSize.getLeft(marginLeft);
    }

    /**
     * Returns the upper right x-coordinate.
     *
     * @return the upper right x-coordinate
     */

    public float right() {
        return pageSize.getRight(marginRight);
    }

    /**
     * Returns the upper right y-coordinate.
     *
     * @return the upper right y-coordinate
     */

    public float top() {
        return pageSize.getTop(marginTop);
    }

    /**
     * Returns the lower left y-coordinate.
     *
     * @return the lower left y-coordinate
     */

    public float bottom() {
        return pageSize.getBottom(marginBottom);
    }

    /**
     * Returns the lower left x-coordinate considering a given margin.
     *
     * @param margin a margin
     * @return the lower left x-coordinate
     */

    public float left(float margin) {
        return pageSize.getLeft(marginLeft + margin);
    }

    /**
     * Returns the upper right x-coordinate, considering a given margin.
     *
     * @param margin a margin
     * @return the upper right x-coordinate
     */

    public float right(float margin) {
        return pageSize.getRight(marginRight + margin);
    }

    /**
     * Returns the upper right y-coordinate, considering a given margin.
     *
     * @param margin a margin
     * @return the upper right y-coordinate
     */

    public float top(float margin) {
        return pageSize.getTop(marginTop + margin);
    }

    /**
     * Returns the lower left y-coordinate, considering a given margin.
     *
     * @param margin a margin
     * @return the lower left y-coordinate
     */

    public float bottom(float margin) {
        return pageSize.getBottom(marginBottom + margin);
    }

    /**
     * Gets the pagesize.
     *
     * @return the page size
     */

    public Rectangle getPageSize() {
        return this.pageSize;
    }

    /**
     * Checks if the document is open.
     *
     * @return <CODE>true</CODE> if the document is open
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Gets the JavaScript onLoad command.
     *
     * @return the JavaScript onLoad command
     */

    public String getJavaScript_onLoad() {
        return this.javaScript_onLoad;
    }

    /**
     * Adds a JavaScript onLoad function to the HTML body tag
     *
     * @param code the JavaScript code to be executed on load of the HTML page
     */

    public void setJavaScript_onLoad(String code) {
        this.javaScript_onLoad = code;
    }

    /**
     * Gets the JavaScript onUnLoad command.
     *
     * @return the JavaScript onUnLoad command
     */

    public String getJavaScript_onUnLoad() {
        return this.javaScript_onUnLoad;
    }

    /**
     * Adds a JavaScript onUnLoad function to the HTML body tag
     *
     * @param code the JavaScript code to be executed on unload of the HTML page
     */

    public void setJavaScript_onUnLoad(String code) {
        this.javaScript_onUnLoad = code;
    }

    /**
     * Gets the style class of the HTML body tag
     *
     * @return the style class of the HTML body tag
     */

    public String getHtmlStyleClass() {
        return this.htmlStyleClass;
    }

    /**
     * Adds a style class to the HTML body tag
     *
     * @param htmlStyleClass the style class for the HTML body tag
     */

    public void setHtmlStyleClass(String htmlStyleClass) {
        this.htmlStyleClass = htmlStyleClass;
    }

    /**
     * Set the margin mirroring. It will mirror right/left margins for odd/even pages.
     * <p>
     * Note: it will not work with {@link Table}.
     *
     * @param marginMirroring <CODE>true</CODE> to mirror the margins
     * @return always <CODE>true</CODE>
     */
    public boolean setMarginMirroring(boolean marginMirroring) {
        this.marginMirroring = marginMirroring;
        for (DocListener listener : listeners) {
            listener.setMarginMirroring(marginMirroring);
        }
        return true;
    }

    /**
     * Set the margin mirroring. It will mirror top/bottom margins for odd/even pages.
     * <p>
     * Note: it will not work with {@link Table}.
     *
     * @param marginMirroringTopBottom <CODE>true</CODE> to mirror the margins
     * @return always <CODE>true</CODE>
     * @since 2.1.6
     */
    public boolean setMarginMirroringTopBottom(boolean marginMirroringTopBottom) {
        this.marginMirroringTopBottom = marginMirroringTopBottom;
        for (DocListener listener : listeners) {
            listener.setMarginMirroringTopBottom(marginMirroringTopBottom);
        }
        return true;
    }

    /**
     * Gets the margin mirroring flag.
     *
     * @return the margin mirroring flag
     */
    public boolean isMarginMirroring() {
        return marginMirroring;
    }

    /**
     * The default language of the document. Can be set to values like "en_US". This language is used in
     * FopGlyphProcessor to determine which glyphs are to be substituted.
     * <p/>
     * The default "dflt" means that all glyphs which can be replaced will be substituted.
     *
     * @return the current document language
     */
    public String getDocumentLanguage() {
        return textRenderingOptions.getDocumentLanguage();
    }

    /**
     * The new document language. This language is used in FopGlyphProcessor to determine which glyphs are to be
     * substituted.
     * <p/>
     * The default "dflt" means that all glyphs which can be replaced will be substituted.
     *
     * @param documentLanguage the wanted language
     */
    public void setDocumentLanguage(String documentLanguage) {
        textRenderingOptions.setDocumentLanguage(documentLanguage);
    }

    /**
     * Returns the glyph substitution enabled flag.
     *
     * @return the glyph substitution enabled flag
     * @see #setGlyphSubstitutionEnabled(boolean)
     */
    public boolean isGlyphSubstitutionEnabled() {
        return textRenderingOptions.isGlyphSubstitutionEnabled();
    }

    /**
     * Set a flag that determine whether glyph substion is enabled when FOP is available.
     *
     * @param glyphSubstitutionEnabled the glyph substitution enabled flag
     * @see #setDocumentLanguage(String)
     */
    public void setGlyphSubstitutionEnabled(boolean glyphSubstitutionEnabled) {
        textRenderingOptions.setGlyphSubstitutionEnabled(glyphSubstitutionEnabled);
    }

    /**
     * Gets the text rendering options.
     *
     * @return the text rendering options
     * @see #getDocumentLanguage()
     * @see #isGlyphSubstitutionEnabled()
     */
    public TextRenderingOptions getTextRenderingOptions() {
        return textRenderingOptions;
    }

    /**
     * Sets the text rendering options.
     *
     * @param textRenderingOptions the text rendering options
     * @see #setDocumentLanguage(String)
     * @see Document#setGlyphSubstitutionEnabled(boolean)
     */
    public void setTextRenderingOptions(TextRenderingOptions textRenderingOptions) {
        this.textRenderingOptions = textRenderingOptions == null ? new TextRenderingOptions() : textRenderingOptions;
    }
}
