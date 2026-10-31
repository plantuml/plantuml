/*
 * $Id: PdfWriter.java 4095 2009-11-12 14:40:41Z blowagie $
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

import org.openpdf.text.DocListener;
import org.openpdf.text.DocWriter;
import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.ExceptionConverter;
import org.openpdf.text.Image;
import org.openpdf.text.ImgJBIG2;
import org.openpdf.text.Rectangle;
import org.openpdf.text.error_messages.MessageLocalization;
import org.openpdf.text.pdf.interfaces.PdfAnnotations;
import org.openpdf.text.pdf.interfaces.PdfDocumentActions;
import org.openpdf.text.pdf.interfaces.PdfPageActions;
import org.openpdf.text.pdf.interfaces.PdfRunDirection;
import org.openpdf.text.pdf.interfaces.PdfVersion;
import org.openpdf.text.pdf.interfaces.PdfViewerPreferences;
import org.openpdf.text.pdf.interfaces.PdfXConformance;
import org.openpdf.text.pdf.internal.PdfVersionImp;
import org.openpdf.text.pdf.internal.PdfXConformanceImp;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * A <CODE>DocWriter</CODE> class for PDF.
 * <p>
 * When this <CODE>PdfWriter</CODE> is added to a certain <CODE>PdfDocument</CODE>, the PDF representation of every
 * Element added to this Document will be written to the outputstream.</P>
 */

public class PdfWriter extends DocWriter implements
        PdfVersion,
        PdfXConformance,
        PdfRunDirection {

    /**
     * The highest generation number possible.
     *
     * @since iText 2.1.6
     */
    public static final int GENERATION_MAX = 65535;

// INNER CLASSES
    /**
     * possible PDF version (header)
     */
    public static final String VERSION_1_2 = "1.2";
    /**
     * possible PDF version (header)
     */
    public static final String VERSION_1_3 = "1.3";

//    ESSENTIALS

//    Construct a PdfWriter instance
    /**
     * possible PDF version (header)
     */
    public static final String VERSION_1_4 = "1.4";
    /**
     * possible PDF version (header)
     */
    public static final String VERSION_1_5 = "1.5";
    /**
     * possible PDF version (header)
     */
    public static final String VERSION_1_6 = "1.6";
    /**
     * possible PDF version (header)
     */
    public static final String VERSION_1_7 = "1.7";

    
    /**
     * PDF version 2.0.
     */
    public static final String VERSION_2_0 = "2.0";


//    the PdfDocument instance
    /**
     * possible PDF version (catalog)
     */
    public static final PdfName PDF_VERSION_1_2 = new PdfName("1.2");
    /**
     * possible PDF version (catalog)
     */
    public static final PdfName PDF_VERSION_1_3 = new PdfName("1.3");
    /**
     * possible PDF version (catalog)
     */
    public static final PdfName PDF_VERSION_1_4 = new PdfName("1.4");
    /**
     * possible PDF version (catalog)
     */
    public static final PdfName PDF_VERSION_1_5 = new PdfName("1.5");
    /**
     * possible PDF version (catalog)
     */
    public static final PdfName PDF_VERSION_1_6 = new PdfName("1.6");

//    the PdfDirectContentByte instances

    /*
     * You should see Direct Content as a canvas on which you can draw
     * graphics and text. One canvas goes on top of the page (getDirectContent),
     * the other goes underneath (getDirectContentUnder).
     * You can always the same object throughout your document,
     * even if you have moved to a new page. Whatever you add on
     * the canvas will be displayed on top or under the current page.
     */
    /**
     * possible PDF version (catalog)
     */
    public static final PdfName PDF_VERSION_1_7 = new PdfName("1.7");


    /**
     * possible PDF version (catalog)
     */
    public static final PdfName PDF_VERSION_2_0 = new PdfName("2.0");


    /**
     * A viewer preference
     */
    public static final int PageLayoutSinglePage = 1;
    /**
     * A viewer preference
     */
    public static final int PageLayoutOneColumn = 2;
    /**
     * A viewer preference
     */
    public static final int PageLayoutTwoColumnLeft = 4;
    /**
     * A viewer preference
     */
    public static final int PageLayoutTwoColumnRight = 8;

//    PDF body

    /*
     * A PDF file has 4 parts: a header, a body, a cross-reference table, and a trailer.
     * The body contains all the PDF objects that make up the PDF document.
     * Each element gets a reference (a set of numbers) and the byte position of
     * every object is stored in the cross-reference table.
     * Use these methods only if you know what you're doing.
     */
    /**
     * A viewer preference
     */
    public static final int PageLayoutTwoPageLeft = 16;
    /**
     * A viewer preference
     */
    public static final int PageLayoutTwoPageRight = 32;
    /**
     * A viewer preference
     */
    public static final int PageModeUseNone = 64;
    /**
     * A viewer preference
     */
    public static final int PageModeUseOutlines = 128;
    /**
     * A viewer preference
     */
    public static final int PageModeUseThumbs = 256;
    /**
     * A viewer preference
     */
    public static final int PageModeFullScreen = 512;
    /**
     * A viewer preference
     */
    public static final int PageModeUseOC = 1024;
    /**
     * A viewer preference
     */
    public static final int PageModeUseAttachments = 2048;
    /**
     * A viewer preference
     */
    public static final int HideToolbar = 1 << 12;
    /**
     * A viewer preference
     */
    public static final int HideMenubar = 1 << 13;
    /**
     * A viewer preference
     */
    public static final int HideWindowUI = 1 << 14;

//    PDF Catalog

    /*
     * The Catalog is also called the root object of the document.
     * Whereas the Cross-Reference maps the objects number with the
     * byte offset so that the viewer can find the objects, the
     * Catalog tells the viewer the numbers of the objects needed
     * to render the document.
     */
    /**
     * A viewer preference
     */
    public static final int FitWindow = 1 << 15;
    /**
     * A viewer preference
     */
    public static final int CenterWindow = 1 << 16;
    /**
     * A viewer preference
     */
    public static final int DisplayDocTitle = 1 << 17;

//    PdfPages

    /*
     * The page root keeps the complete page tree of the document.
     * There's an entry in the Catalog that refers to the root
     * of the page tree, the page tree contains the references
     * to pages and other page trees.
     */
    /**
     * A viewer preference
     */
    public static final int NonFullScreenPageModeUseNone = 1 << 18;
    /**
     * A viewer preference
     */
    public static final int NonFullScreenPageModeUseOutlines = 1 << 19;
    /**
     * A viewer preference
     */
    public static final int NonFullScreenPageModeUseThumbs = 1 << 20;
    /**
     * A viewer preference
     */
    public static final int NonFullScreenPageModeUseOC = 1 << 21;
    /**
     * A viewer preference
     */
    public static final int DirectionL2R = 1 << 22;
    /**
     * A viewer preference
     */
    public static final int DirectionR2L = 1 << 23;
    /**
     * A viewer preference
     */
    public static final int PrintScalingNone = 1 << 24;
    /**
     * action value
     */
    public static final PdfName DOCUMENT_CLOSE = PdfName.WC;
    /**
     * action value
     */
    public static final PdfName WILL_SAVE = PdfName.WS;
    /**
     * action value
     */
    public static final PdfName DID_SAVE = PdfName.DS;
    /**
     * action value
     */
    public static final PdfName WILL_PRINT = PdfName.WP;
    /**
     * action value
     */
    public static final PdfName DID_PRINT = PdfName.DP;
    /**
     * signature value
     */
    public static final int SIGNATURE_EXISTS = 1;

//    page events

    /*
     * Page events are specific for iText, not for PDF.
     * Upon specific events (for instance when a page starts
     * or ends), the corresponding method in the page event
     * implementation that is added to the writer is invoked.
     */
    /**
     * signature value
     */
    public static final int SIGNATURE_APPEND_ONLY = 2;
    /**
     * A PDF/X level.
     */
    public static final int PDFXNONE = 0;
    /**
     * A PDF/X level.
     */
    public static final int PDFX1A2001 = 1;

//    Open and Close methods + method that create the PDF
    /**
     * A PDF/X level.
     */
    public static final int PDFX32002 = 2;
    /**
     * PDFA-1A level.
     */
    public static final int PDFA1A = 3;
    /**
     * PDFA-1B level.
     */
    public static final int PDFA1B = 4;
    /**
     * PDFA-2A level.
     */
    public static final int PDFA2A = 5;
    /**
     * PDFA-2B level.
     */
    public static final int PDFA2B = 6;
    /**
     * PDFA-2U level.
     */
    public static final int PDFA2U = 7;
    /**
     * PDFA-3A level.
     */
    public static final int PDFA3A = 8;
    /**
     * PDFA-3B level.
     */
    public static final int PDFA3B = 9;
    /**
     * PDFA-3U level.
     */
    public static final int PDFA3U = 10;
    /**
     * No encryption
     */
    public static final int ENCRYPTION_NONE = -1;

// Root data for the PDF document (used when composing the Catalog)

//  [C1] Outlines (bookmarks)
    /**
     * Type of encryption
     */
    public static final int STANDARD_ENCRYPTION_40 = 0;
    /**
     * Type of encryption
     */
    public static final int STANDARD_ENCRYPTION_128 = 1;
    /**
     * Type of encryption
     */
    public static final int ENCRYPTION_AES_128 = 2;
    /**
     * Type of encryption
     */
    public static final int ENCRYPTION_AES_256_V3 = 4;

//    [C2] PdfVersion interface
    /**
     * Add this to the mode to keep the metadata in clear text
     */
    public static final int DO_NOT_ENCRYPT_METADATA = 8;
    /**
     * Add this to the mode to keep encrypt only the embedded files.
     *
     * @since 2.1.3
     */
    public static final int EMBEDDED_FILES_ONLY = 24;
    /**
     * The operation permitted when the document is opened with the user password
     *
     * @since 2.0.7
     */
    public static final int ALLOW_PRINTING = 4 + 2048;
    /**
     * The operation permitted when the document is opened with the user password
     *
     * @since 2.0.7
     */
    public static final int ALLOW_MODIFY_CONTENTS = 8;
    /**
     * The operation permitted when the document is opened with the user password
     *
     * @since 2.0.7
     */
    public static final int ALLOW_COPY = 16;
    /**
     * The operation permitted when the document is opened with the user password
     *
     * @since 2.0.7
     */
    public static final int ALLOW_MODIFY_ANNOTATIONS = 32;
    /**
     * The operation permitted when the document is opened with the user password
     *
     * @since 2.0.7
     */
    public static final int ALLOW_FILL_IN = 256;
    /**
     * The operation permitted when the document is opened with the user password
     *
     * @since 2.0.7
     */
    public static final int ALLOW_SCREENREADERS = 512;
    /**
     * The operation permitted when the document is opened with the user password
     *
     * @since 2.0.7
     */
    public static final int ALLOW_ASSEMBLY = 1024;
    /**
     * The operation permitted when the document is opened with the user password
     *
     * @since 2.0.7
     */
    public static final int ALLOW_DEGRADED_PRINTING = 4;
    /**
     * action value
     */
    public static final PdfName PAGE_OPEN = PdfName.O;
    /**
     * action value
     */
    public static final PdfName PAGE_CLOSE = PdfName.C;
    /**
     * The default space-char ratio.
     */
    public static final float SPACE_CHAR_RATIO_DEFAULT = 2.5f;
    /**
     * Disable the inter-character spacing.
     */
    public static final float NO_SPACE_CHAR_RATIO = 10000000f;
    /**
     * Use the default run direction.
     */
    public static final int RUN_DIRECTION_DEFAULT = 0;
    /**
     * Do not use bidirectional reordering.
     */
    public static final int RUN_DIRECTION_NO_BIDI = 1;
    /**
     * Use bidirectional reordering with left-to-right preferential run direction.
     */
    public static final int RUN_DIRECTION_LTR = 2;
    /**
     * Use bidirectional reordering with right-to-left preferential run direction.
     */
    public static final int RUN_DIRECTION_RTL = 3;

//  [C3] PdfViewerPreferences interface

    // page layout (section 13.1.1 of "iText in Action")
    /**
     * Mask to separate the encryption type from the encryption mode.
     */
    static final int ENCRYPTION_MASK = 7;
    /**
     * Stores the PDF/X level.
     */
    private final PdfXConformanceImp pdfxConformance = new PdfXConformanceImp();
    /**
     * This is the list with all the images in the document.
     */
    private final HashMap<Long, PdfName> images = new LinkedHashMap<>();
    /**
     * the pdfdocument object.
     */
    protected PdfDocument pdf;
    /**
     * The direct content in this document.
     */
    protected PdfContentByte directContent;
    /**
     * The direct content under in this document.
     */
    protected PdfContentByte directContentUnder;

    // page mode (section 13.1.2 of "iText in Action")
    /**
     * body of the PDF document
     */
    protected PdfBody body;
    /**
     * Holds value of property extraCatalog this is used for Output Intents.
     */
    protected PdfDictionary extraCatalog;
    /**
     * The root of the page tree.
     */
    protected PdfPages root = new PdfPages(this);
    /**
     * The PdfIndirectReference to the pages.
     */
    protected ArrayList<PdfIndirectReference> pageReferences = new ArrayList<>();
    /**
     * The current page number.
     */
    protected int currentPageNumber = 1;
    /**
     * The value of the Tabs entry in the page dictionary.
     *
     * @since 2.1.5
     */
    protected PdfName tabs = null;

    // values for setting viewer preferences in iText versions older than 2.x
    /**
     * A number referring to the previous Cross-Reference Table.
     */
    protected int prevxref = 0;
    protected List newBookmarks;
    /**
     * Stores the version information for the header and the catalog.
     */
    protected PdfVersionImp pdf_version = new PdfVersionImp();
    /**
     * XMP Metadata for the document.
     */
    protected byte[] xmpMetadata = null;
    /**
     * Contains the business logic for cryptography.
     */
    /**
     * Holds value of property fullCompression.
     */
    protected boolean fullCompression = false;
    /**
     * The compression level of the content streams.
     *
     * @since 2.1.3
     */
    protected int compressionLevel = PdfStream.DEFAULT_COMPRESSION;

    /**
     * When not null, the file identifier ({@code /ID}) of the trailer: the same bytes every time, instead of a
     * random one. Used to produce reproducible files.
     */
    private byte[] fixedFileId = null;

    /**
     * Makes the file identifier ({@code /ID}) of the trailer the given bytes (16 are used), instead of a random value,
     * so that the same document gives the same file.
     *
     * @param fileId the identifier, or null for a random one (the default)
     */
    public void setFixedFileId(byte[] fileId) {
        this.fixedFileId = fileId;
    }
    /**
     * The fonts of this document
     */
    protected LinkedHashMap<BaseFont, FontDetails> documentFonts = new LinkedHashMap<>();
    /**
     * The font number counter for the fonts in the document.
     */
    protected int fontNumber = 1;
    /**
     * The form XObjects in this document. The key is the xref and the value is Object[]{PdfName, template}.
     */
    protected LinkedHashMap<PdfIndirectReference, Object[]> formXObjects = new LinkedHashMap<>();
    /**
     * The name counter for the form XObjects name.
     */
    protected int formXObjectsCounter = 1;
    /**
     * The colors of this document
     */
    protected HashMap<PdfSpotColor, ColorDetails> documentColors = new LinkedHashMap<>();
    /**
     * The color number counter for the colors in the document.
     */
    protected int colorNumber = 1;

//  [C4] Page labels
    /**
     * The patterns of this document
     */
    protected HashMap<PdfPatternPainter, PdfName> documentPatterns = new LinkedHashMap<>();

//  [C5] named objects: named destinations, javascript, embedded files
    /**
     * The pattern number counter for the colors in the document.
     */
    protected int patternNumber = 1;
    protected HashMap<PdfShadingPattern, Object> documentShadingPatterns = new LinkedHashMap<>();
    protected HashMap<PdfShading, Object> documentShadings = new LinkedHashMap<>();
    protected HashMap<PdfDictionary, PdfObject[]> documentExtGState = new LinkedHashMap<>();
    protected HashMap<Object, PdfObject[]> documentProperties = new LinkedHashMap<>();
    protected boolean tagged = false;
    /**
     * A group attributes dictionary specifying the attributes of the page's page group for use in the transparent
     * imaging model
     */
    protected PdfDictionary group;
    protected int runDirection = RUN_DIRECTION_NO_BIDI;
    protected float userunit = 0f;
    protected PdfDictionary defaultColorspace = new PdfDictionary();
    protected HashMap<ColorDetails, ColorDetails> documentSpotPatterns = new LinkedHashMap<>();
    protected ColorDetails patternColorspaceRGB;
    protected ColorDetails patternColorspaceGRAY;

    //  [C7] portable collections
    protected ColorDetails patternColorspaceCMYK;

//  [C8] AcroForm
    /**
     * Dictionary, containing all the images of the PDF document
     */
    protected PdfDictionary imageDictionary = new PdfDictionary();
    /**
     * A HashSet with Stream objects containing JBIG2 Globals
     *
     * @since 2.1.5
     */
    protected HashMap<PdfStream, PdfIndirectReference> JBIG2Globals = new LinkedHashMap<>();
    /**
     * The <CODE>PdfPageEvent</CODE> for this document.
     */
    private PdfPageEvent pageEvent;
    /**
     * The ratio between the extra word spacing and the extra character spacing. Extra word spacing will grow
     * <CODE>ratio</CODE> times more than extra character spacing.
     */
    private float spaceCharRatio = SPACE_CHAR_RATIO_DEFAULT;
    /**
     * A flag indicating the presence of structure elements that contain user properties attributes.
     */
    private boolean userProperties;
    /**
     * Holds value of property RGBTranparency.
     */
    private boolean rgbTransparencyBlending;

    /**
     * Constructs a <CODE>PdfWriter</CODE>.
     */
    protected PdfWriter() {
    }

//  [C9] Metadata

    /**
     * Constructs a <CODE>PdfWriter</CODE>.
     * <p>
     * Remark: a PdfWriter can only be constructed by calling the method
     * <CODE>getInstance(Document document, OutputStream os)</CODE>.
     *
     * @param document The <CODE>PdfDocument</CODE> that has to be written
     * @param os       The <CODE>OutputStream</CODE> the writer has to write to.
     */

    protected PdfWriter(PdfDocument document, OutputStream os) {
        super(document, os);
        pdf = document;
        directContent = new PdfContentByte(this);
        directContentUnder = new PdfContentByte(this);
    }

    /**
     * Use this method to get an instance of the <CODE>PdfWriter</CODE>.
     *
     * @param document The <CODE>Document</CODE> that has to be written
     * @param os       The <CODE>OutputStream</CODE> the writer has to write to.
     * @return a new <CODE>PdfWriter</CODE>
     * @throws DocumentException on error
     */

    public static PdfWriter getInstance(Document document, OutputStream os)
            throws DocumentException {
        PdfDocument pdf = new PdfDocument();
        pdf.setTextRenderingOptions(document.getTextRenderingOptions());
        document.addDocListener(pdf);
        PdfWriter writer = new PdfWriter(pdf, os);
        pdf.addWriter(writer);
        return writer;
    }

    /**
     * Use this method to get an instance of the <CODE>PdfWriter</CODE>.
     *
     * @param document The <CODE>Document</CODE> that has to be written
     * @param os       The <CODE>OutputStream</CODE> the writer has to write to.
     * @param listener A <CODE>DocListener</CODE> to pass to the PdfDocument.
     * @return a new <CODE>PdfWriter</CODE>
     * @throws DocumentException on error
     */

    public static PdfWriter getInstance(Document document, OutputStream os, DocListener listener)
            throws DocumentException {
        PdfDocument pdf = new PdfDocument();
        pdf.setTextRenderingOptions(document.getTextRenderingOptions());
        pdf.addDocListener(listener);
        document.addDocListener(pdf);
        PdfWriter writer = new PdfWriter(pdf, os);
        pdf.addWriter(writer);
        return writer;
    }

//  [C10] PDFX Conformance

    /**
     * Gets the <CODE>PdfDocument</CODE> associated with this writer.
     *
     * @return the <CODE>PdfDocument</CODE>
     */

    PdfDocument getPdfDocument() {
        return pdf;
    }

    /**
     * Use this method to get the info dictionary if you want to change it directly (add keys and values to the info
     * dictionary).
     *
     * @return the info dictionary
     */
    public PdfDictionary getInfo() {
        return pdf.getInfo();
    }

    /**
     * Use this method to get the direct content for this document. There is only one direct content, multiple calls to
     * this method will allways retrieve the same object.
     *
     * @return the direct content
     */

    public PdfContentByte getDirectContent() {
        if (!open) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("the.document.is.not.open"));
        }
        return directContent;
    }

    /**
     * Use this method to get the direct content under for this document. There is only one direct content, multiple
     * calls to this method will always retrieve the same object.
     *
     * @return the direct content
     */

    public PdfContentByte getDirectContentUnder() {
        if (!open) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("the.document.is.not.open"));
        }
        return directContentUnder;
    }

    /**
     * Resets all the direct contents to empty. This happens when a new page is started.
     */
    void resetContent() {
        directContent.reset();
        directContentUnder.reset();
    }

    /**
     * Use this method to add a PDF object to the PDF body. Use this method only if you know what you're doing!
     *
     * @param object the PDF object to add
     * @return a PdfIndirectObject
     * @throws IOException thrown when an I/O operation fails
     */
    public PdfIndirectObject addToBody(PdfObject object) throws IOException {
        return body.add(object);
    }

    /**
     * Use this method to add a PDF object to the PDF body. Use this method only if you know what you're doing!
     *
     * @param object   the PDF object to add
     * @param inObjStm a boolean
     * @return a PdfIndirectObject
     * @throws IOException thrown when an I/O operation fails
     */
    public PdfIndirectObject addToBody(PdfObject object, boolean inObjStm) throws IOException {
        return body.add(object, inObjStm);
    }

//  [C11] Output intents

    /**
     * Use this method to add a PDF object to the PDF body. Use this method only if you know what you're doing!
     *
     * @param object the object to add
     * @param ref    the IndirectReference of that object
     * @return a PdfIndirectObject
     * @throws IOException thrown when an I/O operation fails
     */
    public PdfIndirectObject addToBody(PdfObject object, PdfIndirectReference ref) throws IOException {
        return body.add(object, ref);
    }

    /**
     * Use this method to add a PDF object to the PDF body. Use this method only if you know what you're doing!
     *
     * @param object   the object to add
     * @param ref      the indirect reference
     * @param inObjStm a boolean
     * @return a PdfIndirectObject
     * @throws IOException thrown when an I/O operation fails
     */
    public PdfIndirectObject addToBody(PdfObject object, PdfIndirectReference ref, boolean inObjStm)
            throws IOException {
        return body.add(object, ref, inObjStm);
    }

    /**
     * Use this method to add a PDF object to the PDF body. Use this method only if you know what you're doing!
     *
     * @param object    the object to add
     * @param refNumber the reference number
     * @return a PdfIndirectObject
     * @throws IOException thrown when an I/O operation fails
     */
    public PdfIndirectObject addToBody(PdfObject object, int refNumber) throws IOException {
        return body.add(object, refNumber);
    }

    /**
     * Use this method to add a PDF object to the PDF body. Use this method only if you know what you're doing!
     *
     * @param object    the object to add
     * @param refNumber the reference number
     * @param inObjStm  a boolean
     * @return a PdfIndirectObject
     * @throws IOException thrown when an I/O operation fails
     */
    public PdfIndirectObject addToBody(PdfObject object, int refNumber, boolean inObjStm) throws IOException {
        return body.add(object, refNumber, inObjStm);
    }

// PDF Objects that have an impact on the PDF body

//  [F1] PdfEncryptionSettings interface

    // types of encryption

    /**
     * Use this to get an <CODE>PdfIndirectReference</CODE> for an object that will be created in the future. Use this
     * method only if you know what you're doing!
     *
     * @return the <CODE>PdfIndirectReference</CODE>
     */

    public PdfIndirectReference getPdfIndirectReference() {
        return body.getPdfIndirectReference();
    }

    int getIndirectReferenceNumber() {
        return body.getIndirectReferenceNumber();
    }

    /**
     * Returns the outputStreamCounter.
     *
     * @return the outputStreamCounter
     */
    OutputStreamCounter getOs() {
        return os;
    }

    protected PdfDictionary getCatalog(PdfIndirectReference rootObj) {
        PdfDictionary catalog = pdf.getCatalog(rootObj);
        return catalog;
    }
    // ENCRYPTION_* = 3 reserved for implementation of the
    // meanwhile deprecated proprietary AES256 variant by Adobe

    /**
     * Sets extra keys to the catalog.
     *
     * @return the catalog to change
     */
    public PdfDictionary getExtraCatalog() {
        if (extraCatalog == null) {
            extraCatalog = new PdfDictionary();
        }
        return this.extraCatalog;
    }

    /**
     * Use this method to make sure the page tree has a linear structure (every leave is attached directly to the root).
     * Use this method to allow page reordering with method reorderPages.
     */
    public void setLinearPageMode() {
        root.setLinearMode(null);
    }

    /**
     * Use this method to reorder the pages in the document. A <CODE>null</CODE> argument value only returns the number
     * of pages to process. It is advisable to issue a <CODE>Document.newPage()</CODE> before using this method.
     *
     * @param order an array with the new page sequence. It must have the same size as the number of pages.
     * @return the total number of pages
     * @throws DocumentException if all the pages are not present in the array
     */
    public int reorderPages(int[] order) throws DocumentException {
        return root.reorderPages(order);
    }

    /**
     * Use this method to get a reference to a page existing or not. If the page does not exist yet the reference will
     * be created in advance. If on closing the document, a page number greater than the total number of pages was
     * requested, an exception is thrown.
     *
     * @param page the page number. The first page is 1
     * @return the reference to the page
     */
    public PdfIndirectReference getPageReference(int page) {
        --page;
        if (page < 0) {
            throw new IndexOutOfBoundsException(
                    MessageLocalization.getComposedMessage("the.page.number.must.be.gt.eq.1"));
        }
        PdfIndirectReference ref;
        if (page < pageReferences.size()) {
            ref = pageReferences.get(page);
            if (ref == null) {
                ref = body.getPdfIndirectReference();
                pageReferences.set(page, ref);
            }
        } else {
            int empty = page - pageReferences.size();
            for (int k = 0; k < empty; ++k) {
                pageReferences.add(null);
            }
            ref = body.getPdfIndirectReference();
            pageReferences.add(ref);
        }
        return ref;
    }

    // permissions

    /**
     * Gets the pagenumber of this document. This number can be different from the real pagenumber, if you have (re)set
     * the page number previously.
     *
     * @return a page number
     */
    public int getPageNumber() {
        return pdf.getPageNumber();
    }

    /**
     * Retrieves a reference to the current page of the document.
     * The current page is typically the last page that was modified or accessed.
     *
     * @return a {@link PdfIndirectReference} object pointing to the current page in the PDF document.
     */
    public PdfIndirectReference getCurrentPage() {
        return getPageReference(currentPageNumber);
    }

    /**
     * Returns the number of the current page in the document.
     * The current page is typically the last page that was modified or accessed.
     *
     * @return the current page number.
     */
    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    /**
     * Returns the value to be used for the Tabs entry in the page tree.
     *
     * @return a PdfName of the value of the Tabs entry in the page dictionnary
     * @since 2.1.5
     */
    public PdfName getTabs() {
        return tabs;
    }

    /**
     * Sets the value for the Tabs entry in the page tree.
     *
     * @param tabs Can be PdfName.R, PdfName.C or PdfName.S. Since the Adobe Extensions Level 3, it can also be
     *             PdfName.A or PdfName.W
     * @since 2.1.5
     */
    public void setTabs(PdfName tabs) {
        this.tabs = tabs;
    }

    /**
     * Adds some <CODE>PdfContents</CODE> to this Writer.
     * <p>
     * The document has to be open before you can begin to add content to the body of the document.
     *
     * @param page     the <CODE>PdfPage</CODE> to add
     * @param contents the <CODE>PdfContents</CODE> of the page
     * @return a <CODE>PdfIndirectReference</CODE>
     * @throws PdfException on error
     */

    PdfIndirectReference add(PdfPage page, PdfContents contents) throws PdfException {
        if (!open) {
            throw new PdfException(MessageLocalization.getComposedMessage("the.document.is.not.open"));
        }
        PdfIndirectObject object;
        try {
            object = addToBody(contents);
        } catch (IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
        page.add(object.getIndirectReference());
        // [U5]
        if (group != null) {
            page.put(PdfName.GROUP, group);
            group = null;
        } else if (rgbTransparencyBlending) {
            PdfDictionary pp = new PdfDictionary();
            pp.put(PdfName.TYPE, PdfName.GROUP);
            pp.put(PdfName.S, PdfName.TRANSPARENCY);
            pp.put(PdfName.CS, PdfName.DEVICERGB);
            page.put(PdfName.GROUP, pp);
        }
        root.addPage(page);
        currentPageNumber++;
        return null;
    }

    /**
     * Gets the <CODE>PdfPageEvent</CODE> for this document or <CODE>null</CODE> if none is set.
     *
     * @return the <CODE>PdfPageEvent</CODE> for this document or <CODE>null</CODE> if none is set
     */

    public PdfPageEvent getPageEvent() {
        return pageEvent;
    }

    /**
     * Signals that the <CODE>Document</CODE> has been opened and that
     * <CODE>Elements</CODE> can be added.
     * <p>
     * When this method is called, the PDF-document header is written to the outputstream.
     *
     * @see org.openpdf.text.DocWriter#open()
     */
    @Override
    public void open() {
        super.open();
        try {
            pdf_version.writeHeader(os);
            body = new PdfBody(this);
            if (pdfxConformance.isPdfX32002()) {
                PdfDictionary sec = new PdfDictionary();
                sec.put(PdfName.GAMMA, new PdfArray(new float[]{2.2f, 2.2f, 2.2f}));
                sec.put(PdfName.MATRIX, new PdfArray(
                        new float[]{0.4124f, 0.2126f, 0.0193f, 0.3576f, 0.7152f, 0.1192f, 0.1805f, 0.0722f, 0.9505f}));
                sec.put(PdfName.WHITEPOINT, new PdfArray(new float[]{0.9505f, 1f, 1.089f}));
                PdfArray arr = new PdfArray(PdfName.CALRGB);
                arr.add(sec);
                setDefaultColorspace(PdfName.DEFAULTRGB, addToBody(arr).getIndirectReference());
            }
        } catch (IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }

    /**
     * Signals that the <CODE>Document</CODE> was closed and that no other
     * <CODE>Elements</CODE> will be added.
     * <p>
     * The pages-tree is built and written to the outputstream. A Catalog is constructed, as well as an Info-object, the
     * reference table is composed and everything is written to the outputstream embedded in a Trailer.
     *
     * @see org.openpdf.text.DocWriter#close()
     */
    @Override
    public void close() {
        if (open) {
            if (pdf != null && pdf.isOpen()) {
                throw new IllegalStateException(MessageLocalization
                        .getComposedMessage("you.should.call.document.close.instead"));
            }
            if ((currentPageNumber - 1) != pageReferences.size()) {
                // 2019-04-26: If you get this error, it could be that you are using OpenPDF or
                // another library such as flying-saucer's ITextRenderer in a non-threadsafe way.
                // ITextRenderer is not thread safe. So if you get this problem here, create a new
                // instance, rather than re-using it.
                // See: https://github.com/LibrePDF/OpenPDF/issues/164
                throw new RuntimeException("The page " + pageReferences.size() +
                        " was requested but the document has only " + (currentPageNumber - 1) + " pages.");
            }

            try {
                addSharedObjectsToBody();
                // add the root to the body
                PdfIndirectReference rootRef = root.writePageTree();
                // make the catalog-object and add it to the body
                PdfDictionary catalog = getCatalog(rootRef);
                // [C9] if there is XMP data to add: add it
                if (xmpMetadata != null) {
                    PdfStream xmp = new PdfStream(xmpMetadata);
                    xmp.put(PdfName.TYPE, PdfName.METADATA);
                    xmp.put(PdfName.SUBTYPE, PdfName.XML);
                    catalog.put(PdfName.METADATA, body.add(xmp).getIndirectReference());
                }
                // [C11] Output Intents
                if (extraCatalog != null) {
                    catalog.mergeDifferent(extraCatalog);
                }

                // add the Catalog to the body
                PdfIndirectObject indirectCatalog = addToBody(catalog, false);
                // add the info-object to the body
                PdfIndirectObject infoObj = addToBody(getInfo(), false);

                // [F1] encryption
                PdfIndirectReference encryption = null;
                PdfObject fileID;
                body.flushObjStm();
                if (getInfo().contains(PdfName.FILEID)) {
                    fileID = getInfo().get(PdfName.FILEID);
                } else {
                    // the same documentId shall be provided to the first version
                    byte[] documentId = fixedFileId != null ? fixedFileId : createDocumentId();
                    fileID = createInfoId(documentId, documentId);
                }

                // write the cross-reference table of the body
                body.writeCrossReferenceTable(os, indirectCatalog.getIndirectReference(),
                        infoObj.getIndirectReference(), encryption, fileID, prevxref);

                os.write(getISOBytes("startxref\n"));
                os.write(getISOBytes(String.valueOf(body.offset())));
                os.write(getISOBytes("\n%%EOF\n"));
                super.close();
            } catch (IOException ioe) {
                throw new ExceptionConverter(ioe);
            }
        }
    }

    protected void addSharedObjectsToBody() throws IOException {
        // [F3] add the fonts
        for (FontDetails details : documentFonts.values()) {
            details.writeFont(this);
        }
        // [F4] add the form XObjects
        for (Object[] objs : formXObjects.values()) {
            PdfTemplate template = (PdfTemplate) objs[1];
            if (template != null && template.getType() == PdfTemplate.TYPE_TEMPLATE) {
                addToBody(template.getFormXObject(compressionLevel), template.getIndirectReference());
            }
        }
        // [F6] add the spotcolors
        for (ColorDetails color : documentColors.values()) {
            addToBody(color.getSpotColor(this), color.getIndirectReference());
        }
        // [F7] add the pattern
        for (PdfPatternPainter pat : documentPatterns.keySet()) {
            addToBody(pat.getPattern(compressionLevel), pat.getIndirectReference());
        }
        // [F8] add the shading patterns
        for (PdfShadingPattern shadingPattern : documentShadingPatterns.keySet()) {
            shadingPattern.addToBody();
        }
        // [F9] add the shadings
        for (PdfShading shading : documentShadings.keySet()) {
            shading.addToBody();
        }
        // [F10] add the extgstate
        for (Map.Entry<PdfDictionary, PdfObject[]> entry : documentExtGState.entrySet()) {
            PdfDictionary gstate = entry.getKey();
            PdfObject[] obj = entry.getValue();
            addToBody(gstate, (PdfIndirectReference) obj[1]);
        }
        // [F11] add the properties
        for (Map.Entry<Object, PdfObject[]> entry : documentProperties.entrySet()) {
            Object prop = entry.getKey();
            PdfObject[] obj = entry.getValue();
            if (prop instanceof PdfDictionary) {
                addToBody((PdfDictionary) prop, (PdfIndirectReference) obj[1]);
            }
        }
    }

    /**
     * @see org.openpdf.text.pdf.interfaces.PdfVersion#setAtLeastPdfVersion(String)
     */
    public void setAtLeastPdfVersion(String version) {
        pdf_version.setAtLeastPdfVersion(version);
    }

    /**
     * @see org.openpdf.text.pdf.interfaces.PdfVersion#addDeveloperExtension(org.openpdf.text.pdf.PdfDeveloperExtension)
     * @since 2.1.6
     */
    public void addDeveloperExtension(PdfDeveloperExtension de) {
        pdf_version.addDeveloperExtension(de);
    }

    /**
     * Returns the version information.
     */
    PdfVersionImp getPdfVersion() {
        return pdf_version;
    }

    /**
     * @see org.openpdf.text.pdf.interfaces.PdfVersion#setPdfVersion(String)
     */
    public void setPdfVersion(String version) {
        pdf_version.setPdfVersion(version);
    }

//  [F3] adding fonts

    /**
     * @see org.openpdf.text.pdf.interfaces.PdfVersion#setPdfVersion(org.openpdf.text.pdf.PdfName)
     */
    public void setPdfVersion(PdfName version) {
        pdf_version.setPdfVersion(version);
    }

    /**
     * @see org.openpdf.text.pdf.interfaces.PdfAnnotations#addAnnotation(org.openpdf.text.pdf.PdfAnnotation)
     */
    public void addAnnotation(PdfAnnotation annot) {
        pdf.addAnnotation(annot);
    }

    void addAnnotation(PdfAnnotation annot, int page) {
        addAnnotation(annot);
    }

//  [F9] document shadings

    /**
     * Use this method to set the XMP Metadata.
     *
     * @param xmpMetadata The xmpMetadata to set.
     */
    public void setXmpMetadata(byte[] xmpMetadata) {
        this.xmpMetadata = xmpMetadata;
    }

//  [F11] adding properties (OCG, marked content)

    /**
     * @see org.openpdf.text.pdf.interfaces.PdfXConformance#getPDFXConformance()
     */
    public int getPDFXConformance() {
        return pdfxConformance.getPDFXConformance();
    }

    /**
     * @see org.openpdf.text.pdf.interfaces.PdfXConformance#setPDFXConformance(int)
     */
    public void setPDFXConformance(int pdfx) {
        if (pdfxConformance.getPDFXConformance() == pdfx) {
            return;
        }
        if (pdf.isOpen()) {
            throw new PdfXConformanceException(
                    MessageLocalization.getComposedMessage(
                            "pdfx.conformance.can.only.be.set.before.opening.the.document"));
        }
        if (pdfx == PDFA1A || pdfx == PDFA1B) {
            setPdfVersion(VERSION_1_4);
        } else if (pdfx == PDFA2A || pdfx == PDFA2B || pdfx == PDFA2U
                || pdfx == PDFA3A || pdfx == PDFA3B || pdfx == PDFA3U) {
            setPdfVersion(VERSION_1_7);
        } else if (pdfx != PDFXNONE) {
            setPdfVersion(VERSION_1_3);
        }
        pdfxConformance.setPDFXConformance(pdfx);
    }

    /**
     * @see org.openpdf.text.pdf.interfaces.PdfXConformance#isPdfX()
     */
    public boolean isPdfX() {
        return pdfxConformance.isPdfX();
    }

//  [F12] tagged PDF

    public boolean isPdfA1() {
        return pdfxConformance.isPdfA1();
    }

    /**
     * Checks if the PDF has to be in conformance with PDFA2
     *
     * @return true if the PDF has to be in conformance with PDFA2
     */
    public boolean isPdfA2() {
        return pdfxConformance.isPdfA2();
    }

    /**
     * Checks if the PDF has to be in conformance with PDFA3
     *
     * @return true if the PDF has to be in conformance with PDFA3
     */
    public boolean isPdfA3() {
        return pdfxConformance.isPdfA3();
    }

    /**
     * Checks if the PDF has to be in conformance with any PDF/A version
     *
     * @return true if the PDF has to be in conformance with any PDF/A version
     */
    public boolean isPdfA() {
        return pdfxConformance.isPdfA();
    }

    private static byte[] createDocumentId() {
        final byte[] id = new byte[16];
        // Not secure on purpose: the /ID of a PDF only has to be (very likely) unique.
        new java.util.Random(System.nanoTime() ^ System.currentTimeMillis()).nextBytes(id);
        return id;
    }

    private static PdfObject createInfoId(byte[] idPartOne, byte[] idPartTwo) {
        try (ByteBuffer buf = new ByteBuffer(90)) {
            buf.append('[').append('<');
            for (int k = 0; k < 16 && k < idPartOne.length; ++k) {
                buf.appendHex(idPartOne[k]);
            }
            buf.append('>').append('<');
            for (int k = 0; k < 16 && k < idPartTwo.length; ++k) {
                buf.appendHex(idPartTwo[k]);
            }
            buf.append('>').append(']');
            return new PdfLiteral(buf.toByteArray());
        } catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }

    /**
     * Use this method to find out if 1.5 compression is on.
     *
     * @return the 1.5 compression status
     */
    public boolean isFullCompression() {
        return this.fullCompression;
    }

    /**
     * Use this method to set the document's compression to the PDF 1.5 mode with object streams and xref streams. It
     * can be set at any time but once set it can't be unset.
     * <p>
     * If set before opening the document it will also set the pdf version to 1.5.
     */
    public void setFullCompression() {
        this.fullCompression = true;
        setAtLeastPdfVersion(VERSION_1_5);
    }

    /**
     * Returns the compression level used for streams written by this writer.
     *
     * @return the compression level (0 = best speed, 9 = best compression, -1 is default)
     * @since 2.1.3
     */
    public int getCompressionLevel() {
        return compressionLevel;
    }

    /**
     * Sets the compression level to be used for streams written by this writer.
     *
     * @param compressionLevel a value between 0 (best speed) and 9 (best compression)
     * @since 2.1.3
     */
    public void setCompressionLevel(int compressionLevel) {
        if (compressionLevel < PdfStream.NO_COMPRESSION || compressionLevel > PdfStream.BEST_COMPRESSION) {
            this.compressionLevel = PdfStream.DEFAULT_COMPRESSION;
        } else {
            this.compressionLevel = compressionLevel;
        }
    }

    void eliminateFontSubset(PdfDictionary fonts) {
        for (FontDetails ft : documentFonts.values()) {
            if (fonts.get(ft.getFontName()) != null) {
                ft.setSubset(false);
            }
        }
    }

//  User methods to change aspects of the page

//  [U1] page size

    /**
     * Use this method to gets the current document size. This size only includes the data already written to the output
     * stream, it does not include templates or fonts. It is useful if used with <CODE>freeReader()</CODE> when
     * concatenating many documents and an idea of the current size is needed.
     *
     * @return the approximate size without fonts or templates
     */
    public long getCurrentDocumentSize() {
        return body.offset() + (long) body.size() * 20L + 0x48;
    }

    PdfName getColorspaceName() {
        return new PdfName("CS" + (colorNumber++));
    }

//  [U2] take care of empty pages

    /**
     * Adds a <CODE>SpotColor</CODE> to the document but not to the page resources.
     *
     * @param spc the <CODE>SpotColor</CODE> to add
     * @return an <CODE>Object[]</CODE> where position 0 is a <CODE>PdfName</CODE> and position 1 is an
     * <CODE>PdfIndirectReference</CODE>
     */
    ColorDetails addSimple(PdfSpotColor spc) {
        ColorDetails ret = documentColors.get(spc);
        if (ret == null) {
            ret = new ColorDetails(getColorspaceName(), body.getPdfIndirectReference(), spc);
            documentColors.put(spc, ret);
        }
        return ret;
    }

    PdfName addSimplePattern(PdfPatternPainter painter) {
        PdfName name = documentPatterns.get(painter);
        try {
            if (name == null) {
                name = new PdfName("P" + patternNumber);
                ++patternNumber;
                documentPatterns.put(painter, name);
            }
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
        return name;
    }

//  [U3] page actions (open and close)

    void addSimpleShadingPattern(PdfShadingPattern shading) {
        if (!documentShadingPatterns.containsKey(shading)) {
            shading.setName(patternNumber);
            ++patternNumber;
            documentShadingPatterns.put(shading, null);
            addSimpleShading(shading.getShading());
        }
    }

    void addSimpleShading(PdfShading shading) {
        if (!documentShadings.containsKey(shading)) {
            documentShadings.put(shading, null);
            shading.setName(documentShadings.size());
        }
    }

    PdfObject[] addSimpleExtGState(PdfDictionary gstate) {
        if (!documentExtGState.containsKey(gstate)) {
            PdfXConformanceImp.checkPDFXConformance(this, PdfXConformanceImp.PDFXKEY_GSTATE, gstate);
            documentExtGState.put(gstate,
                    new PdfObject[]{new PdfName("GS" + (documentExtGState.size() + 1)), getPdfIndirectReference()});
        }
        return documentExtGState.get(gstate);
    }

    PdfObject[] addSimpleProperty(Object prop, PdfIndirectReference refi) {
        if (!documentProperties.containsKey(prop)) {
            documentProperties.put(prop, new PdfObject[]{new PdfName("Pr" + (documentProperties.size() + 1)), refi});
        }
        return documentProperties.get(prop);
    }

    boolean propertyExists(Object prop) {
        return documentProperties.containsKey(prop);
    }

//  [U4] Thumbnail image

    /**
     * Mark this document for tagging. It must be called before open.
     */
    public void setTagged() {
        if (open) {
            throw new IllegalArgumentException(
                    MessageLocalization.getComposedMessage("tagging.must.be.set.before.opening.the.document"));
        }
        tagged = true;
    }

//  [U5] Transparency groups

    /**
     * Check if the document is marked for tagging.
     *
     * @return <CODE>true</CODE> if the document is marked for tagging
     */
    public boolean isTagged() {
        return tagged;
    }

//  [U7] run direction (doesn't actually do anything)

    /**
     * Use this method to get the size of the media box.
     *
     * @return a Rectangle
     */
    public Rectangle getPageSize() {
        return pdf.getPageSize();
    }

    /**
     * Use this method to set the crop box. The crop box should not be rotated even if the page is rotated. This change
     * only takes effect in the next page.
     *
     * @param crop the crop box
     */
    public void setCropBoxSize(Rectangle crop) {
        pdf.setCropBoxSize(crop);
    }

    /**
     * Use this method to set the page box sizes. Allowed names are: "crop", "trim", "art" and "bleed".
     *
     * @param boxName the box size
     * @param size    the size
     */
    public void setBoxSize(String boxName, Rectangle size) {
        pdf.setBoxSize(boxName, size);
    }

    /**
     * Use this method to get the size of a trim, art, crop or bleed box, or null if not defined.
     *
     * @param boxName crop, trim, art or bleed
     * @return the Rectangle
     */
    public Rectangle getBoxSize(String boxName) {
        return pdf.getBoxSize(boxName);
    }

    /**
     * Checks if a newPage() will actually generate a new page.
     *
     * @return true if a new page will be generated, false otherwise
     * @since 2.1.8
     */
    public boolean isPageEmpty() {
        return pdf.isPageEmpty();
    }

    /**
     * Use this method to make sure a page is added, even if it's empty. If you use setPageEmpty(false), invoking
     * newPage() after a blank page will add a newPage. setPageEmpty(true) won't have any effect.
     *
     * @param pageEmpty the state
     */
    public void setPageEmpty(boolean pageEmpty) {
        if (pageEmpty) {
            return;
        }
        pdf.setPageEmpty(pageEmpty);
    }

// Miscellaneous topics

//  [M1] Color settings

    /**
     * Use this method to get the group dictionary.
     *
     * @return Value of property group.
     */
    public PdfDictionary getGroup() {
        return this.group;
    }

    /**
     * Use this method to set the group dictionary.
     *
     * @param group New value of property group.
     */
    public void setGroup(PdfDictionary group) {
        this.group = group;
    }

    /**
     * Use this method to gets the space/character extra spacing ratio for fully justified text.
     *
     * @return the space/character extra spacing ratio
     */
    public float getSpaceCharRatio() {
        return spaceCharRatio;
    }

//  [M2] spot patterns

    /**
     * Use this method to set the ratio between the extra word spacing and the extra character spacing when the text is
     * fully justified. Extra word spacing will grow <CODE>spaceCharRatio</CODE> times more than extra character
     * spacing. If the ratio is <CODE>PdfWriter.NO_SPACE_CHAR_RATIO</CODE> then the extra character spacing will be
     * zero.
     *
     * @param spaceCharRatio the ratio between the extra word spacing and the extra character spacing
     */
    public void setSpaceCharRatio(float spaceCharRatio) {
        this.spaceCharRatio = Math.max(spaceCharRatio, 0.001f);
    }

    /**
     * Use this method to set the run direction.
     *
     * @return the run direction
     */
    public int getRunDirection() {
        return runDirection;
    }

    /**
     * Use this method to set the run direction. This is only used as a placeholder as it does not affect anything.
     *
     * @param runDirection the run direction
     */
    public void setRunDirection(int runDirection) {
        if (runDirection < RUN_DIRECTION_NO_BIDI || runDirection > RUN_DIRECTION_RTL) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.run.direction.1", runDirection));
        }
        this.runDirection = runDirection;
    }

    /**
     * Use this method to get the user unit. A user unit is a value that defines the default user space unit. The
     * minimum UserUnit is 1 (1 unit = 1/72 inch). The maximum UserUnit is 75,000. Note that this userunit only works
     * starting with PDF1.6!
     *
     * @return Returns the userunit.
     */
    public float getUserunit() {
        return userunit;
    }

    /**
     * Use this method to set the user unit. A UserUnit is a value that defines the default user space unit. The minimum
     * UserUnit is 1 (1 unit = 1/72 inch). The maximum UserUnit is 75,000. Note that this userunit only works starting
     * with PDF1.6!
     *
     * @param userunit The userunit to set.
     * @throws DocumentException on error
     */
    public void setUserunit(float userunit) throws DocumentException {
        if (userunit < 1f || userunit > 75000f) {
            throw new DocumentException(
                    MessageLocalization.getComposedMessage("userunit.should.be.a.value.between.1.and.75000"));
        }
        this.userunit = userunit;
        setAtLeastPdfVersion(VERSION_1_6);
    }

//  [M3] Images

    /**
     * Use this method to get the default colorspaces.
     *
     * @return the default colorspaces
     */
    public PdfDictionary getDefaultColorspace() {
        return defaultColorspace;
    }

    /**
     * Use this method to sets the default colorspace that will be applied to all the document. The colorspace is only
     * applied if another colorspace with the same name is not present in the content.
     * <p>
     * The colorspace is applied immediately when creating templates and at the page end for the main document content.
     *
     * @param key the name of the colorspace. It can be <CODE>PdfName.DEFAULTGRAY</CODE>,
     *            <CODE>PdfName.DEFAULTRGB</CODE> or <CODE>PdfName.DEFAULTCMYK</CODE>
     * @param cs  the colorspace. A <CODE>null</CODE> or <CODE>PdfNull</CODE> removes any colorspace with the same name
     */
    public void setDefaultColorspace(PdfName key, PdfObject cs) {
        if (cs == null || cs.isNull()) {
            defaultColorspace.remove(key);
        }
        defaultColorspace.put(key, cs);
    }

    ColorDetails addSimplePatternColorspace(Color color) {
        int type = ExtendedColor.getType(color);
        if (type == ExtendedColor.TYPE_PATTERN || type == ExtendedColor.TYPE_SHADING) {
            throw new RuntimeException(MessageLocalization.getComposedMessage(
                    "an.uncolored.tile.pattern.can.not.have.another.pattern.or.shading.as.color"));
        }
        try {
            switch (type) {
                case ExtendedColor.TYPE_RGB:
                    if (patternColorspaceRGB == null) {
                        patternColorspaceRGB = new ColorDetails(getColorspaceName(), body.getPdfIndirectReference(),
                                null);
                        PdfArray array = new PdfArray(PdfName.PATTERN);
                        array.add(PdfName.DEVICERGB);
                        addToBody(array, patternColorspaceRGB.getIndirectReference());
                    }
                    return patternColorspaceRGB;
                case ExtendedColor.TYPE_CMYK:
                    if (patternColorspaceCMYK == null) {
                        patternColorspaceCMYK = new ColorDetails(getColorspaceName(), body.getPdfIndirectReference(),
                                null);
                        PdfArray array = new PdfArray(PdfName.PATTERN);
                        array.add(PdfName.DEVICECMYK);
                        addToBody(array, patternColorspaceCMYK.getIndirectReference());
                    }
                    return patternColorspaceCMYK;
                case ExtendedColor.TYPE_GRAY:
                    if (patternColorspaceGRAY == null) {
                        patternColorspaceGRAY = new ColorDetails(getColorspaceName(), body.getPdfIndirectReference(),
                                null);
                        PdfArray array = new PdfArray(PdfName.PATTERN);
                        array.add(PdfName.DEVICEGRAY);
                        addToBody(array, patternColorspaceGRAY.getIndirectReference());
                    }
                    return patternColorspaceGRAY;
                case ExtendedColor.TYPE_SEPARATION: {
                    ColorDetails details = addSimple(((SpotColor) color).getPdfSpotColor());
                    ColorDetails patternDetails = documentSpotPatterns.get(details);
                    if (patternDetails == null) {
                        patternDetails = new ColorDetails(getColorspaceName(), body.getPdfIndirectReference(), null);
                        PdfArray array = new PdfArray(PdfName.PATTERN);
                        array.add(details.getIndirectReference());
                        addToBody(array, patternDetails.getIndirectReference());
                        documentSpotPatterns.put(details, patternDetails);
                    }
                    return patternDetails;
                }
                default:
                    throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.color.type"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Writes a <CODE>PdfImage</CODE> to the outputstream.
     *
     * @param pdfImage the image to be added
     * @return a <CODE>PdfIndirectReference</CODE> to the encapsulated image
     * @throws PdfException when a document isn't open yet, or has been closed
     */

    PdfIndirectReference add(PdfImage pdfImage, PdfIndirectReference fixedRef) throws PdfException {
        if (!imageDictionary.contains(pdfImage.name())) {
            PdfXConformanceImp.checkPDFXConformance(this, PdfXConformanceImp.PDFXKEY_IMAGE, pdfImage);
            try {
                if (fixedRef == null) {
                    fixedRef = addToBody(pdfImage).getIndirectReference();
                } else {
                    addToBody(pdfImage, fixedRef);
                }
            } catch (IOException ioe) {
                throw new ExceptionConverter(ioe);
            }
            imageDictionary.put(pdfImage.name(), fixedRef);
            return fixedRef;
        }
        return (PdfIndirectReference) imageDictionary.get(pdfImage.name());
    }

    /**
     * return the <CODE>PdfIndirectReference</CODE> to the image with a given name.
     *
     * @param name the name of the image
     * @return a <CODE>PdfIndirectReference</CODE>
     */

    PdfIndirectReference getImageReference(PdfName name) {
        return (PdfIndirectReference) imageDictionary.get(name);
    }

    /**
     * Gets an indirect reference to a JBIG2 Globals stream. Adds the stream if it hasn't already been added to the
     * writer.
     *
     * @param content a byte array that may already been added to the writer inside a stream object.
     * @return the PdfIndirectReference
     * @since 2.1.5
     */
    protected PdfIndirectReference getReferenceJBIG2Globals(byte[] content) {
        if (content == null) {
            return null;
        }
        PdfStream stream;
        for (PdfStream pdfStream : JBIG2Globals.keySet()) {
            stream = pdfStream;
            if (Arrays.equals(content, stream.getBytes())) {
                return JBIG2Globals.get(stream);
            }
        }
        stream = new PdfStream(content);
        PdfIndirectObject ref;
        try {
            ref = addToBody(stream);
        } catch (IOException e) {
            return null;
        }
        JBIG2Globals.put(stream, ref.getIndirectReference());
        return ref.getIndirectReference();
    }
//  [F12] tagged PDF

    /**
     * Gets the flag indicating the presence of structure elements that contain user properties attributes.
     *
     * @return the user properties flag
     */
    public boolean isUserProperties() {
        return this.userProperties;
    }

    /**
     * Sets the flag indicating the presence of structure elements that contain user properties attributes.
     *
     * @param userProperties the user properties flag
     */
    public void setUserProperties(boolean userProperties) {
        this.userProperties = userProperties;
    }

    /**
     * Gets the transparency blending colorspace.
     *
     * @return <code>true</code> if the transparency blending colorspace is RGB, <code>false</code>
     * if it is the default blending colorspace
     * @since 2.1.0
     */
    public boolean isRgbTransparencyBlending() {
        return this.rgbTransparencyBlending;
    }

    /**
     * Sets the transparency blending colorspace to RGB. The default blending colorspace is CMYK and will result in
     * faded colors in the screen and in printing. Calling this method will return the RGB colors to what is expected.
     * The RGB blending will be applied to all subsequent pages until other value is set. Note that this is a generic
     * solution that may not work in all cases.
     *
     * @param rgbTransparencyBlending <code>true</code> to set the transparency blending colorspace to RGB,
     *                                <code>false</code>
     *                                to use the default blending colorspace
     * @since 2.1.0
     */
    public void setRgbTransparencyBlending(boolean rgbTransparencyBlending) {
        this.rgbTransparencyBlending = rgbTransparencyBlending;
    }

    /**
     * This class generates the structure of a PDF document.
     * <p>
     * This class covers the third section of Chapter 5 in the 'Portable Document Format Reference Manual version 1.3'
     * (page 55-60). It contains the body of a PDF document (section 5.14) and it can also generate a Cross-reference
     * Table (section 5.15).
     *
     * @see PdfWriter
     * @see PdfObject
     * @see PdfIndirectObject
     */
    public static class PdfBody {

        private static final int OBJSINSTREAM = 200;

        // membervariables

        /**
         * array containing the cross-reference table of the normal objects.
         */
        private final TreeSet<PdfCrossReference> xrefs;
        private final PdfWriter writer;
        private int refnum;
        /**
         * the current byte position in the body.
         */
        private long position;
        private ByteBuffer index;
        private ByteBuffer streamObjects;
        private int currentObjNum;
        private int numObj = 0;

        // constructors

        /**
         * Constructs a new <CODE>PdfBody</CODE>.
         *
         * @param writer
         */
        PdfBody(PdfWriter writer) {
            xrefs = new TreeSet<>();
            xrefs.add(new PdfCrossReference(0, 0, GENERATION_MAX));
            position = writer.getOs().getCounter();
            refnum = 1;
            this.writer = writer;
        }

        // methods

        void setRefnum(int refnum) {
            this.refnum = refnum;
        }

        private PdfWriter.PdfBody.PdfCrossReference addToObjStm(PdfObject obj, int nObj) throws IOException {
            if (numObj >= OBJSINSTREAM) {
                flushObjStm();
            }
            if (index == null) {
                index = new ByteBuffer();
                streamObjects = new ByteBuffer();
                currentObjNum = getIndirectReferenceNumber();
                numObj = 0;
            }
            int p = streamObjects.size();
            int idx = numObj++;
            obj.toPdf(writer, streamObjects);
            streamObjects.append(' ');
            index.append(nObj).append(' ').append(p).append(' ');
            return new PdfWriter.PdfBody.PdfCrossReference(2, nObj, currentObjNum, idx);
        }

        private void flushObjStm() throws IOException {
            if (numObj == 0) {
                return;
            }
            int first = index.size();
            index.append(streamObjects);
            PdfStream stream = new PdfStream(index.toByteArray());
            stream.flateCompress(writer.getCompressionLevel());
            stream.put(PdfName.TYPE, PdfName.OBJSTM);
            stream.put(PdfName.N, new PdfNumber(numObj));
            stream.put(PdfName.FIRST, new PdfNumber(first));
            add(stream, currentObjNum);
            index = null;
            streamObjects = null;
            numObj = 0;
        }

        /**
         * Adds a <CODE>PdfObject</CODE> to the body.
         * <p>
         * This methods creates a <CODE>PdfIndirectObject</CODE> with a certain number, containing the given
         * <CODE>PdfObject</CODE>. It also adds a <CODE>PdfCrossReference</CODE> for this object to an
         * <CODE>ArrayList</CODE> that will be used to build the Cross-reference Table.
         *
         * @param object a <CODE>PdfObject</CODE>
         * @return a <CODE>PdfIndirectObject</CODE>
         * @throws IOException
         */
        PdfIndirectObject add(PdfObject object) throws IOException {
            return add(object, getIndirectReferenceNumber());
        }

        PdfIndirectObject add(PdfObject object, boolean inObjStm) throws IOException {
            return add(object, getIndirectReferenceNumber(), inObjStm);
        }

        /**
         * Gets a PdfIndirectReference for an object that will be created in the future.
         *
         * @return a PdfIndirectReference
         */
        PdfIndirectReference getPdfIndirectReference() {
            return new PdfIndirectReference(0, getIndirectReferenceNumber());
        }

        int getIndirectReferenceNumber() {
            int n = refnum++;
            xrefs.add(new PdfCrossReference(n, 0, GENERATION_MAX));
            return n;
        }

        /**
         * Adds a <CODE>PdfObject</CODE> to the body given an already existing PdfIndirectReference.
         * <p>
         * This methods creates a <CODE>PdfIndirectObject</CODE> with the number given by
         * <CODE>ref</CODE>, containing the given <CODE>PdfObject</CODE>.
         * It also adds a <CODE>PdfCrossReference</CODE> for this object to an <CODE>ArrayList</CODE> that will be used
         * to build the Cross-reference Table.
         *
         * @param object a <CODE>PdfObject</CODE>
         * @param ref    a <CODE>PdfIndirectReference</CODE>
         * @return a <CODE>PdfIndirectObject</CODE>
         * @throws IOException
         */
        PdfIndirectObject add(PdfObject object, PdfIndirectReference ref) throws IOException {
            return add(object, ref.getNumber());
        }

        PdfIndirectObject add(PdfObject object, PdfIndirectReference ref, boolean inObjStm) throws IOException {
            return add(object, ref.getNumber(), inObjStm);
        }

        PdfIndirectObject add(PdfObject object, int refNumber) throws IOException {
            return add(object, refNumber, true); // to false
        }

        PdfIndirectObject add(PdfObject object, int refNumber, boolean inObjStm) throws IOException {
            if (inObjStm && object.canBeInObjStm() && writer.isFullCompression()) {
                PdfCrossReference pxref = addToObjStm(object, refNumber);
                PdfIndirectObject indirect = new PdfIndirectObject(refNumber, object, writer);
                if (!xrefs.add(pxref)) {
                    xrefs.remove(pxref);
                    xrefs.add(pxref);
                }
                return indirect;
            } else {
                PdfIndirectObject indirect = new PdfIndirectObject(refNumber, object, writer);
                PdfCrossReference pxref = new PdfCrossReference(refNumber, position);
                if (!xrefs.add(pxref)) {
                    xrefs.remove(pxref);
                    xrefs.add(pxref);
                }
                indirect.writeTo(writer.getOs());
                position = writer.getOs().getCounter();
                return indirect;
            }
        }

        /**
         * Returns the offset of the Cross-Reference table.
         *
         * @return an offset
         */
        long offset() {
            return position;
        }

        /**
         * Returns the total number of objects contained in the CrossReferenceTable of this <CODE>Body</CODE>.
         *
         * @return a number of objects
         */
        int size() {
            return Math.max((xrefs.last()).getRefnum() + 1, refnum);
        }

        /**
         * Returns the CrossReferenceTable of the <CODE>Body</CODE>.
         *
         * @param os
         * @param root
         * @param info
         * @param encryption
         * @param fileID
         * @param prevxref
         * @throws IOException
         */

        void writeCrossReferenceTable(OutputStream os, PdfIndirectReference root, PdfIndirectReference info,
                PdfIndirectReference encryption, PdfObject fileID, int prevxref) throws IOException {
            int refNumber = 0;
            // Old-style xref tables limit object offsets to 10 digits
            boolean useNewXrefFormat = writer.isFullCompression() || position > 9_999_999_999L;
            if (useNewXrefFormat) {
                flushObjStm();
                refNumber = getIndirectReferenceNumber();
                xrefs.add(new PdfCrossReference(refNumber, position));
            }
            PdfCrossReference entry = xrefs.first();
            int first = entry.getRefnum();
            int len = 0;
            ArrayList<Integer> sections = new ArrayList<>();
            for (PdfCrossReference xref1 : xrefs) {
                entry = xref1;
                if (first + len == entry.getRefnum()) {
                    ++len;
                } else {
                    sections.add(first);
                    sections.add(len);
                    first = entry.getRefnum();
                    len = 1;
                }
            }
            sections.add(first);
            sections.add(len);
            PdfTrailer trailer = new PdfTrailer(size(), root, info, encryption, fileID, prevxref);
            if (useNewXrefFormat) {
                int mid = 8 - (Long.numberOfLeadingZeros(position) >> 3);
                ByteBuffer buf = new ByteBuffer();

                for (PdfCrossReference xref : xrefs) {
                    entry = xref;
                    entry.toPdf(mid, buf);
                }
                PdfStream xr = new PdfStream(buf.toByteArray());
                xr.flateCompress(writer.getCompressionLevel());
                xr.putAll(trailer);
                xr.put(PdfName.W, new PdfArray(new int[]{1, mid, 2}));
                xr.put(PdfName.TYPE, PdfName.XREF);
                PdfArray idx = new PdfArray();
                for (Integer section : sections) {
                    idx.add(new PdfNumber(section));
                }
                xr.put(PdfName.INDEX, idx);
                PdfIndirectObject indirect = new PdfIndirectObject(refNumber, xr, writer);
                indirect.writeTo(writer.getOs());
            } else {
                os.write(getISOBytes("xref\n"));
                Iterator<PdfCrossReference> i = xrefs.iterator();
                for (int k = 0; k < sections.size(); k += 2) {
                    first = sections.get(k);
                    len = sections.get(k + 1);
                    os.write(getISOBytes(String.valueOf(first)));
                    os.write(getISOBytes(" "));
                    os.write(getISOBytes(String.valueOf(len)));
                    os.write('\n');
                    while (len-- > 0) {
                        entry = i.next();
                        entry.toPdf(os);
                    }
                }
                // make the trailer
                trailer.toPdf(writer, os);
            }
        }

        // inner classes

        /**
         * <CODE>PdfCrossReference</CODE> is an entry in the PDF Cross-Reference table.
         */
        public static class PdfCrossReference implements Comparable<PdfCrossReference> {

            /**
             * String template for cross-reference entry PDF representation.
             *
             * @see Formatter
             */
            private static final String CROSS_REFERENCE_ENTRY_FORMAT = "%010d %05d %c \n";

            // membervariables
            private final int type;

            /**
             * Byte offset in the PDF file.
             */
            private final long offset;

            private final int refnum;
            /**
             * generation of the object.
             */
            private final int generation;

            // constructors

            /**
             * Constructs a cross-reference element for a PdfIndirectObject.
             *
             * @param refnum     the reference number
             * @param offset     byte offset of the object
             * @param generation generation number of the object
             */
            public PdfCrossReference(int refnum, long offset, int generation) {
                type = 0;
                this.offset = offset;
                this.refnum = refnum;
                this.generation = generation;
            }

            /**
             * Constructs a cross-reference element for a PdfIndirectObject.
             *
             * @param refnum the reference number
             * @param offset byte offset of the object
             */
            public PdfCrossReference(int refnum, long offset) {
                type = 1;
                this.offset = offset;
                this.refnum = refnum;
                this.generation = 0;
            }

            public PdfCrossReference(int type, int refnum, long offset, int generation) {
                this.type = type;
                this.offset = offset;
                this.refnum = refnum;
                this.generation = generation;
            }

            int getRefnum() {
                return refnum;
            }

            /**
             * Writes PDF representation of cross-reference entry to passed output stream.
             *
             * @param os Output stream this entry to write to
             * @throws IOException If any I/O error occurs
             */
            public void toPdf(OutputStream os) throws IOException {
                // TODO: are generation number and 'In use' keyword bound that way?
                final char inUse = generation == GENERATION_MAX ? 'f' : 'n';
                os.write(getISOBytes(String.format(CROSS_REFERENCE_ENTRY_FORMAT, offset, generation, inUse)));
            }

            /**
             * Writes PDF syntax to the OutputStream
             *
             * @param midSize the mid size
             * @param os      the OutputStream
             * @throws IOException on error
             */
            public void toPdf(int midSize, OutputStream os) throws IOException {
                os.write((byte) type);
                int thisMidSize = midSize;
                while (--thisMidSize >= 0) {
                    os.write((byte) ((offset >>> (8 * thisMidSize)) & 0xff));
                }
                os.write((byte) ((generation >>> 8) & 0xff));
                os.write((byte) (generation & 0xff));
            }

            /**
             * Compares current {@link PdfCrossReference entry} with passed {@code reference} by PDF object number.
             */
            @Override
            public int compareTo(final PdfCrossReference reference) {
                return Integer.compare(refnum, reference.refnum);
            }

            /**
             * Checks if two entries are equal if their PDF object numbers are equal.
             *
             * @param obj Another cross-reference entry
             * @return If null, not of type {@link PdfCrossReference} or object numbers are not equal, returns false;
             * true otherwise
             */
            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof PdfCrossReference)) {
                    return false;
                }
                PdfCrossReference other = (PdfCrossReference) obj;

                return refnum == other.refnum;
            }

            @Override
            public int hashCode() {
                return refnum;
            }
        }
    }

    /**
     * <CODE>PdfTrailer</CODE> is the PDF Trailer object.
     * <p>
     * This object is described in the 'Portable Document Format Reference Manual version 1.3' section 5.16 (page
     * 59-60).
     */

    static class PdfTrailer extends PdfDictionary {

        // constructors

        /**
         * Constructs a PDF-Trailer.
         *
         * @param size       the number of entries in the <CODE>PdfCrossReferenceTable</CODE>
         * @param root       an indirect reference to the root of the PDF document
         * @param info       an indirect reference to the info object of the PDF document
         * @param encryption
         * @param fileID
         * @param prevxref
         */

        PdfTrailer(int size, PdfIndirectReference root, PdfIndirectReference info, PdfIndirectReference encryption,
                PdfObject fileID, int prevxref) {
            put(PdfName.SIZE, new PdfNumber(size));
            put(PdfName.ROOT, root);
            if (info != null) {
                put(PdfName.INFO, info);
            }
            if (encryption != null) {
                put(PdfName.ENCRYPT, encryption);
            }
            if (fileID != null) {
                put(PdfName.ID, fileID);
            }
            if (prevxref > 0) {
                put(PdfName.PREV, new PdfNumber(prevxref));
            }
        }

        /**
         * Returns the PDF representation of this <CODE>PdfObject</CODE>.
         *
         * @param writer
         * @param os
         * @throws IOException
         */
        @Override
        public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
            os.write(getISOBytes("trailer\n"));
            super.toPdf(null, os);
            os.write('\n');
        }
    }



    /**
     * Adds a <CODE>BaseFont</CODE> to the document but not to the page resources. It is used for templates.
     *
     * @param bf the <CODE>BaseFont</CODE> to add
     * @return an <CODE>Object[]</CODE> where position 0 is a <CODE>PdfName</CODE> and position 1 is an
     * <CODE>PdfIndirectReference</CODE>
     */

    FontDetails addSimple(BaseFont bf) {
        FontDetails ret = documentFonts.get(bf);
        if (ret == null) {
            PdfXConformanceImp.checkPDFXConformance(this, PdfXConformanceImp.PDFXKEY_FONT, bf);
            ret = new FontDetails(new PdfName("F" + (fontNumber++)), body.getPdfIndirectReference(), bf);
            documentFonts.put(bf, ret);
        }
        return ret;
    }



    /**
     * Adds a template to the document but not to the page resources.
     *
     * @param template   the template to add
     * @param forcedName the template name, rather than a generated one. Can be null
     * @return the <CODE>PdfName</CODE> for this template
     */

    PdfName addDirectTemplateSimple(PdfTemplate template, PdfName forcedName) {
        PdfIndirectReference ref = template.getIndirectReference();
        Object[] obj = formXObjects.get(ref);
        PdfName name;
        try {
            if (obj == null) {
                if (forcedName == null) {
                    name = new PdfName("Xf" + formXObjectsCounter);
                    ++formXObjectsCounter;
                } else {
                    name = forcedName;
                }
                formXObjects.put(ref, new Object[]{name, template});
            } else {
                name = (PdfName) obj[0];
            }
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
        return name;
    }



    /**
     * Use this method to adds an image to the document but not to the page resources. It is used with templates and
     * <CODE>Document.add(Image)</CODE>. Use this method only if you know what you're doing!
     *
     * @param image the <CODE>Image</CODE> to add
     * @return the name of the image added
     * @throws PdfException      on error
     * @throws DocumentException on error
     */
    public PdfName addDirectImageSimple(Image image) throws DocumentException {
        return addDirectImageSimple(image, null);
    }


    /**
     * Adds an image to the document but not to the page resources. It is used with templates and
     * <CODE>Document.add(Image)</CODE>. Use this method only if you know what you're doing!
     *
     * @param image    the <CODE>Image</CODE> to add
     * @param fixedRef the reference to used. It may be <CODE>null</CODE>, a <CODE>PdfIndirectReference</CODE> or a
     *                 <CODE>PRIndirectReference</CODE>.
     * @return the name of the image added
     * @throws PdfException      on error
     * @throws DocumentException on error
     */
    public PdfName addDirectImageSimple(Image image, PdfIndirectReference fixedRef) throws DocumentException {
        PdfName name;
        // if the images is already added, just retrieve the name
        if (images.containsKey(image.getMySerialId())) {
            name = images.get(image.getMySerialId());
        } else {
            // if it's a new image, add it to the document
            if (image.isImgTemplate()) {
                name = new PdfName("img" + images.size());
            } else {
                PdfIndirectReference dref = image.getDirectReference();
                if (dref != null) {
                    PdfName rname = new PdfName("img" + images.size());
                    images.put(image.getMySerialId(), rname);
                    imageDictionary.put(rname, dref);
                    return rname;
                }
                Image maskImage = image.getImageMask();
                PdfIndirectReference maskRef = null;
                if (maskImage != null) {
                    PdfName mname = images.get(maskImage.getMySerialId());
                    maskRef = getImageReference(mname);
                }
                PdfImage i = new PdfImage(image, "img" + images.size(), maskRef);
                add(i, fixedRef);
                name = i.name();
            }
            images.put(image.getMySerialId(), name);
        }
        return name;
    }
}
