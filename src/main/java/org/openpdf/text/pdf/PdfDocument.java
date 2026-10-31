/*
 * $Id: PdfDocument.java 4098 2009-11-16 13:27:45Z blowagie $
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

package org.openpdf.text.pdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Element;
import org.openpdf.text.ExceptionConverter;
import org.openpdf.text.Meta;
import org.openpdf.text.Rectangle;
import org.openpdf.text.error_messages.MessageLocalization;
import org.openpdf.text.Document;

/**
 * <CODE>PdfDocument</CODE> is the class that is used by <CODE>PdfWriter</CODE> to translate a <CODE>Document</CODE>
 * into a PDF with different pages.
 * <p>
 * This pruned version only manages pages (that are filled by the content of the writer), the information dictionary,
 * the catalog and the annotations of the pages. The layout of text, tables and images is not supported.
 *
 * @see org.openpdf.text.Document
 * @see PdfWriter
 */
public class PdfDocument extends Document {

    /**
     * The writer associated to this document.
     */
    protected PdfWriter writer;

    /**
     * The <CODE>PdfContentByte</CODE> object that contains the text, the graphics and the images of the page.
     */
    protected PdfContentByte text;
    protected PdfContentByte graphics;

    private int textEmptySize;

    /**
     * The info dictionary.
     */
    protected PdfInfo info = new PdfInfo();

    /**
     * Margins and page size of the next page.
     */
    private float nextMarginLeft;
    private float nextMarginRight;
    private float nextMarginTop;
    private float nextMarginBottom;
    private Rectangle nextPageSize = null;

    /**
     * The boxes of the pages (crop box...).
     */
    protected HashMap<String, PdfRectangle> thisBoxSize = new LinkedHashMap<>();
    protected HashMap<String, PdfRectangle> boxSize = new LinkedHashMap<>();

    /**
     * The resources of the current page.
     */
    protected PageResources pageResources;

    /**
     * The annotations of the current page.
     */
    private List<PdfAnnotation> annotations = new ArrayList<>();

    /**
     * Indicates if the current page is empty.
     */
    private boolean pageEmpty = true;

    /**
     * Constructs a new PDF document.
     */
    public PdfDocument() {
        super();
        addProducer();
        addCreationDate();
    }

    /**
     * Adds a <CODE>PdfWriter</CODE> to the <CODE>PdfDocument</CODE>.
     *
     * @param writer the <CODE>PdfWriter</CODE>
     * @throws DocumentException on error
     */
    public void addWriter(PdfWriter writer) throws DocumentException {
        if (this.writer == null) {
            this.writer = writer;
            return;
        }
        throw new DocumentException(
                MessageLocalization.getComposedMessage("you.can.only.add.a.writer.to.a.pdfdocument.once"));
    }

    /**
     * Opens the document.
     */
    @Override
    public void open() {
        if (!open) {
            super.open();
            writer.open();
        }
        try {
            initPage();
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    /**
     * Closes the document.
     */
    @Override
    public void close() {
        if (close) {
            return;
        }
        try {
            newPage();
            if (!annotations.isEmpty()) {
                throw new RuntimeException(MessageLocalization.getComposedMessage(
                        "not.all.annotations.could.be.added.to.the.document.the.document.doesn.t.have.enough.pages"));
            }
            super.close();
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
        writer.close();
    }

    /**
     * Signals that an <CODE>Element</CODE> was added to the <CODE>Document</CODE>. Only the meta information is
     * supported by this pruned version.
     *
     * @param element the element to add
     * @return <CODE>true</CODE> if the element was added, <CODE>false</CODE> if not.
     * @throws DocumentException when a document isn't open yet, or has been closed
     */
    @Override
    public boolean add(Element element) throws DocumentException {
        if (writer != null && writer.isPaused()) {
            return false;
        }
        switch (element.type()) {
            case Element.HEADER:
                info.addkey(((Meta) element).getName(), ((Meta) element).getContent());
                break;
            case Element.TITLE:
                info.addTitle(((Meta) element).getContent());
                break;
            case Element.SUBJECT:
                info.addSubject(((Meta) element).getContent());
                break;
            case Element.KEYWORDS:
                info.addKeywords(((Meta) element).getContent());
                break;
            case Element.AUTHOR:
                info.addAuthor(((Meta) element).getContent());
                break;
            case Element.CREATOR:
                info.addCreator(((Meta) element).getContent());
                break;
            case Element.PRODUCER:
                info.addProducer(((Meta) element).getContent());
                break;
            case Element.CREATIONDATE:
                // A PDF date (Document.addCreationDate(PdfDate)) is used as is, anything else
                // only resets the creation date to now
                if (((Meta) element).getContent().startsWith("D:"))
                    info.put(PdfName.CREATIONDATE, new PdfDate(((Meta) element).getContent()));
                else
                    info.addCreationDate();
                break;
            case Element.MODIFICATIONDATE:
                info.addModificationDate(new PdfDate(((Meta) element).getContent()));
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * Makes a new page and sends it to the <CODE>PdfWriter</CODE>.
     *
     * @return a <CODE>boolean</CODE>
     */
    @Override
    public boolean newPage() {
        if (isPageEmpty()) {
            setNewPageSizeAndMargins();
            resetText();
            return false;
        }
        if (!open || close) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("the.document.is.not.open"));
        }
        try {
            // page size and rotation
            int rotation = pageSize.getRotation();

            pageResources.addDefaultColorDiff(writer.getDefaultColorspace());
            if (writer.isRgbTransparencyBlending()) {
                PdfDictionary dcs = new PdfDictionary();
                dcs.put(PdfName.CS, PdfName.DEVICERGB);
                pageResources.addDefaultColorDiff(dcs);
            }
            PdfDictionary resources = pageResources.getResources();

            // we create the page dictionary
            PdfPage page = new PdfPage(new PdfRectangle(pageSize, rotation), thisBoxSize, resources, rotation);
            page.put(PdfName.TABS, writer.getTabs());

            if (writer.getUserunit() > 0f) {
                page.put(PdfName.USERUNIT, new PdfNumber(writer.getUserunit()));
            }

            // we add the annotations
            if (!annotations.isEmpty()) {
                PdfArray array = writeAnnotations();
                if (array.size() != 0) {
                    page.put(PdfName.ANNOTS, array);
                }
            }

            if (text.size() > textEmptySize) {
                text.endText();
            } else {
                text = null;
            }
            writer.add(page, new PdfContents(writer.getDirectContentUnder(), graphics, text, writer.getDirectContent(),
                    pageSize));
            // we initialize the new page
            initPage();
        } catch (DocumentException | IOException de) {
            // maybe this never happens, but it's better to check.
            throw new ExceptionConverter(de);
        }
        return true;
    }

    /**
     * Adds the annotations of the page to the body and returns the array of their references.
     */
    private PdfArray writeAnnotations() throws IOException {
        PdfArray array = new PdfArray();
        int rotation = pageSize.getRotation() % 360;
        for (PdfAnnotation dic : annotations) {
            array.add(dic.getIndirectReference());
            PdfRectangle rect = (PdfRectangle) dic.get(PdfName.RECT);
            if (rect != null) {
                switch (rotation) {
                    case 90:
                        dic.put(PdfName.RECT, new PdfRectangle(
                                pageSize.getTop() - rect.bottom(),
                                rect.left(),
                                pageSize.getTop() - rect.top(),
                                rect.right()));
                        break;
                    case 180:
                        dic.put(PdfName.RECT, new PdfRectangle(
                                pageSize.getRight() - rect.left(),
                                pageSize.getTop() - rect.bottom(),
                                pageSize.getRight() - rect.right(),
                                pageSize.getTop() - rect.top()));
                        break;
                    case 270:
                        dic.put(PdfName.RECT, new PdfRectangle(
                                rect.bottom(),
                                pageSize.getRight() - rect.left(),
                                rect.top(),
                                pageSize.getRight() - rect.right()));
                        break;
                    default:
                        break;
                }
            }
            writer.addToBody(dic, dic.getIndirectReference());
        }
        annotations = new ArrayList<>();
        return array;
    }

    /**
     * Sets the pagesize.
     *
     * @param pageSize the new pagesize
     * @return <CODE>true</CODE> if the page size was set
     */
    @Override
    public boolean setPageSize(Rectangle pageSize) {
        if (writer != null && writer.isPaused()) {
            return false;
        }
        nextPageSize = new Rectangle(pageSize);
        return true;
    }

    /**
     * Sets the margins.
     *
     * @param marginLeft   the margin on the left
     * @param marginRight  the margin on the right
     * @param marginTop    the margin on the top
     * @param marginBottom the margin on the bottom
     * @return a <CODE>boolean</CODE>
     */
    @Override
    public boolean setMargins(float marginLeft, float marginRight, float marginTop, float marginBottom) {
        if (writer != null && writer.isPaused()) {
            return false;
        }
        nextMarginLeft = marginLeft;
        nextMarginRight = marginRight;
        nextMarginTop = marginTop;
        nextMarginBottom = marginBottom;
        return true;
    }

    private void resetText() {
        text = new PdfContentByte(writer);
        text.reset();
        text.beginText();
        textEmptySize = text.size();
    }

    /**
     * Initializes a page.
     *
     * @throws DocumentException on error
     */
    protected void initPage() throws DocumentException {
        // the pagenumber is incremented
        pageN++;

        // initialization of some page objects
        pageResources = new PageResources();

        writer.resetContent();
        graphics = new PdfContentByte(writer);
        resetText();

        setNewPageSizeAndMargins();

        thisBoxSize = new LinkedHashMap<>(boxSize);
        pageEmpty = true;
    }

    protected void setNewPageSizeAndMargins() {
        pageSize = nextPageSize;
        marginLeft = nextMarginLeft;
        marginRight = nextMarginRight;
        marginTop = nextMarginTop;
        marginBottom = nextMarginBottom;
    }

    /**
     * Gets the info dictionary.
     *
     * @return the info dictionary
     */
    protected PdfInfo getInfo() {
        return info;
    }

    /**
     * Gets the catalog.
     *
     * @param pages the reference of the root of the pages tree
     * @return the catalog
     */
    PdfCatalog getCatalog(PdfIndirectReference pages) {
        PdfCatalog catalog = new PdfCatalog(pages, writer);
        writer.getPdfVersion().addToCatalog(catalog);
        return catalog;
    }

    /**
     * Adds an annotation to the current page.
     *
     * @param annot the annotation
     */
    void addAnnotation(PdfAnnotation annot) {
        pageEmpty = false;
        annotations.add(annot);
    }

    boolean isPageEmpty() {
        return writer == null || (writer.getDirectContent().size() == 0 && writer.getDirectContentUnder().size() == 0
                && (pageEmpty || writer.isPaused()));
    }

    PageResources getPageResources() {
        return pageResources;
    }

    /**
     * Meta information of the document.
     */
    public static class PdfInfo extends PdfDictionary {

        PdfInfo() {
            super();
        }

        void addTitle(String title) {
            put(PdfName.TITLE, new PdfString(title, PdfObject.TEXT_UNICODE));
        }

        void addSubject(String subject) {
            put(PdfName.SUBJECT, new PdfString(subject, PdfObject.TEXT_UNICODE));
        }

        void addKeywords(String keywords) {
            put(PdfName.KEYWORDS, new PdfString(keywords, PdfObject.TEXT_UNICODE));
        }

        void addAuthor(String author) {
            put(PdfName.AUTHOR, new PdfString(author, PdfObject.TEXT_UNICODE));
        }

        void addCreator(String creator) {
            put(PdfName.CREATOR, new PdfString(creator, PdfObject.TEXT_UNICODE));
        }

        void addProducer(final String producer) {
            put(PdfName.PRODUCER, new PdfString(producer));
        }

        void addCreationDate() {
            PdfString date = new PdfDate();
            put(PdfName.CREATIONDATE, date);
        }

        void addModificationDate(PdfDate date) {
            put(PdfName.MODDATE, date);
        }

        void addkey(String key, String value) {
            if (key.equals("Producer") || key.equals("CreationDate")) {
                return;
            }
            put(new PdfName(key), new PdfString(value, PdfObject.TEXT_UNICODE));
        }
    }

    /**
     * The catalog of the document.
     */
    static class PdfCatalog extends PdfDictionary {

        PdfWriter writer;

        PdfCatalog(PdfIndirectReference pages, PdfWriter writer) {
            super(CATALOG);
            this.writer = writer;
            put(PdfName.PAGES, pages);
        }
    }
}
