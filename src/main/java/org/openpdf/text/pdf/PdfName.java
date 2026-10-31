/*
 * $Id: PdfName.java 4082 2009-10-25 14:18:28Z psoares33 $
 *
 * Copyright 1999-2006 Bruno Lowagie
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

/**
 * <CODE>PdfName</CODE> is an object that can be used as a name in a PDF-file.
 * <p>
 * A name, like a string, is a sequence of characters. It must begin with a slash followed by a sequence of ASCII
 * characters in the range 32 through 136 except %, (, ), [, ], &lt;, &gt;, {, }, / and #. Any character except 0x00 may
 * be included in a name by writing its two character hex code, preceded by #. The maximum number of characters in a
 * name is 127.<BR> This object is described in the 'Portable Document Format Reference Manual version 1.7' section
 * 3.2.4 (page 56-58).
 * </P>
 *
 * @see PdfObject
 * @see PdfDictionary
 * @see BadPdfFormatException
 */

public class PdfName extends PdfObject implements Comparable<PdfName> {

    /**
     * (Optional) An action that shall be performed when the annotation is activated.
     */
    public static final PdfName A = new PdfName("A");
    /**
     * (Optional) An additional-actions dictionary defining the field's / annotations's behaviour in response to various
     * trigger events. This entry has exactly the same meaning as the AA entry in an annotation dictionary.
     */
    public static final PdfName AA = new PdfName("AA");
    /**
     * A name
     */
    public static final PdfName AC = new PdfName("AC");
    /**
     * (Optional; PDF 2.0) An array of one or more file specification dictionaries which denote the associated files for
     * this annotation.
     */
    public static final PdfName AF = new PdfName("AF");
    /**
     * A name
     */
    public static final PdfName ANNOTS = new PdfName("Annots");
    /**
     * (Optional) An appearance dictionary specifying how the annotation shall be presented visually on the page. A PDF
     * writer shall include an appearance dictionary when writing or updating an annotation dictionary except for the
     * two cases listed below. Every annotation (including those whose Subtype value is Widget, as used for form
     * fields), except for the two cases listed below, shall have at least one appearance dictionary. Exclusions:
     * <ul>
     * <li>Annotations where the value of the Rect key consists of an array where the value at index 1 is equal to the
     * value at index 3 or the value at index 2 is equal to the value at index 4</li>
     * <li>Annotations whose Subtype value is Popup, Projection or Link.</li>
     * </ul>
     */
    public static final PdfName AP = new PdfName("AP");

    /**
     * A name
     */
    public static final PdfName ARTBOX = new PdfName("ArtBox");
    /**
     * A name
     */
    public static final PdfName ASCENT = new PdfName("Ascent");
    /**
     * (Required if the appearance dictionary AP contains one or more subdictionaries) The annotation's appearance
     * state, which selects the applicable appearance stream from an appearance subdictionary.
     */
    public static final PdfName AS = new PdfName("AS");
    /**
     * A name
     */
    public static final PdfName AUTHOR = new PdfName("Author");
    /**
     * A name
     */
    public static final PdfName B = new PdfName("B");
    /**
     * A name
     */
    public static final PdfName BASEFONT = new PdfName("BaseFont");
    /**
     * A name
     */
    public static final PdfName BBOX = new PdfName("BBox");
    /**
     * A name
     */
    public static final PdfName BC = new PdfName("BC");
    /**
     * A name
     */
    public static final PdfName BITSPERCOMPONENT = new PdfName("BitsPerComponent");
    /**
     * A name
     */
    public static final PdfName BLACKIS1 = new PdfName("BlackIs1");
    /**
     * A name
     */
    public static final PdfName BLEEDBOX = new PdfName("BleedBox");
    /**
     * A name
     */
    public static final PdfName BOX = new PdfName("Box");
    /**
     * A name
     */
    public static final PdfName BS = new PdfName("BS");
    /**
     * (Optional) An array of numbers in the range 0.0 to 1.0, representing a colour used for the following purposes:
     * <ul><li>The background of the annotation's icon when closed </li>
     * <li>The title bar of the annotation's popup window </li>
     * <li>The border of a link annotation </li>
     * </ul>
     * <p>
     * The number of array elements determines the colour space in which the colour shall be defined:
     * 0=No colour; transparent 1=DeviceGray 3=DeviceRGB 4=DeviceCMYK
     */
    public static final PdfName C = new PdfName("C");
    /**
     * A name
     */
    public static final PdfName C0 = new PdfName("C0");
    /**
     * A name
     */
    public static final PdfName C1 = new PdfName("C1");
    /**
     * (Optional; PDF 2.0) When regenerating the annotation's appearance stream, this is the opacity value that shall be
     * used for stroking all visiblea elements of the annotation in its closed state, including its background and
     * border, but not the popup window that appears when the annotation is opened. If a ca entry is not present in this
     * dictionary, then the value of this CA entry shall also be used for nonstroking operations as well.<p> Default
     * Value: 1.0 <p>The specified value shall not be used if the annotation has an appearance stream; in that case, the
     * appearance stream shall specify any transparency.
     */
    public static final PdfName CA = new PdfName("CA");
    /**
     * (Optional; PDF 2.0) When regenerating the annotation's appearance stream, this is the opacity value that shall be
     * used for all nonstroking operations on all visible elements of the annotation in its closed state (including its
     * background and border) but not the popup window that appears when the annotation is opened. Default value: 1.0
     * The specified value shall not be used if the annotation has an appearance stream in that case, the appearance
     * stream shall specify any transparency.
     */
    public static final PdfName ca = new PdfName("ca");
    /**
     * A name
     */
    public static final PdfName CAPHEIGHT = new PdfName("CapHeight");
    /**
     * A name
     */
    public static final PdfName CATALOG = new PdfName("Catalog");
    /**
     * A name
     */
    public static final PdfName CCITTFAXDECODE = new PdfName("CCITTFaxDecode");
    /**
     * A name
     */
    public static final PdfName CI = new PdfName("CI");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName CMD = new PdfName("CMD");
    /**
     * A name
     */
    public static final PdfName COLORSPACE = new PdfName("ColorSpace");
    /**
     * A name
     */
    public static final PdfName COLUMNS = new PdfName("Columns");
    /**
     * A name
     */
    public static final PdfName CONTENT = new PdfName("Content");
    /**
     * Used in several places:
     * <ul><li>
     * 1) Annotations: (Optional) Text that shall be displayed for the annotation or, if this type of annotation does not display text,
     * an alternative description of the annotation's contents in human-readable form. In either case, this text is useful when extracting
     * the document's contents in support of accessibility to users with disabilities or for other purposes. Might be different for each
     * annotation type.</li>
     * <li>
     * 2) Part of the signature dictionary. The signature value. The value is a hexadecimal string representing the
     * value of the byte range digest. (Required) </li></ul>
     */
    public static final PdfName CONTENTS = new PdfName("Contents");
    /**
     * A name
     */
    public static final PdfName COUNT = new PdfName("Count");
    /**
     * A name of a base 14 type 1 font
     */
    public static final PdfName COURIER = new PdfName("Courier");
    /**
     * A name of a base 14 type 1 font
     */
    public static final PdfName COURIER_BOLD = new PdfName("Courier-Bold");
    /**
     * A name of a base 14 type 1 font
     */
    public static final PdfName COURIER_OBLIQUE = new PdfName("Courier-Oblique");
    /**
     * A name of a base 14 type 1 font
     */
    public static final PdfName COURIER_BOLDOBLIQUE = new PdfName("Courier-BoldOblique");
    /**
     * A name
     */
    public static final PdfName CREATIONDATE = new PdfName("CreationDate");
    /**
     * A name
     */
    public static final PdfName CREATOR = new PdfName("Creator");
    /**
     * A name
     */
    public static final PdfName CROPBOX = new PdfName("CropBox");
    /**
     * A name
     */
    public static final PdfName CS = new PdfName("CS");
    /**
     * A name
     */
    public static final PdfName D = new PdfName("D");
    /**
     * Part of the Build Data Dictionary. The software module build date.
     */
    public static final PdfName DATE = new PdfName("Date");

    /**
     * A name
     */
    public static final PdfName DC = new PdfName("DC");
    /**
     * A name
     */
    public static final PdfName DECODE = new PdfName("Decode");
    /**
     * A name
     */
    public static final PdfName DECODEPARMS = new PdfName("DecodeParms");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName DEFAULT = new PdfName("Default");
    /**
     * A name
     */
    public static final PdfName DESCENT = new PdfName("Descent");
    /**
     * A name
     */
    public static final PdfName DEST = new PdfName("Dest");
    /**
     * A name
     */
    public static final PdfName DEVICEGRAY = new PdfName("DeviceGray");
    /**
     * A name
     */
    public static final PdfName DEVICERGB = new PdfName("DeviceRGB");
    /**
     * A name
     */
    public static final PdfName DEVICECMYK = new PdfName("DeviceCMYK");
    /**
     * A name
     */
    public static final PdfName DIFFERENCES = new PdfName("Differences");
    /**
     * A name
     */
    public static final PdfName DL = new PdfName("DL");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName DOCUMENT = new PdfName("Document");
    /**
     * A name
     */
    public static final PdfName DP = new PdfName("DP");
    /**
     * A name
     */
    public static final PdfName DR = new PdfName("DR");
    /**
     * (Optional) A default style string; described in the PDF Annex M.
     */
    public static final PdfName DS = new PdfName("DS");

    /**
     * Entry in a structure element dictionary: The expanded form of an abbreviation or an acronym.
     * <p>OR<p>
     * Entry in an annotation’s additional-actions dictionary: An action that shall be performed when the cursor enters
     * theannotation’s active area.
     * <p>OR<p>
     * Entry in a collection field dictionary: A flag indicating whether the interactive PDF processor should provide
     * support for editing the field value.
     * <p>OR more...
     */
    public static final PdfName E = new PdfName("E");
    /**
     * A name
     */
    public static final PdfName EF = new PdfName("EF");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName EMBEDDED = new PdfName("Embedded");
    /**
     * A name
     */
    public static final PdfName ENCODEDBYTEALIGN = new PdfName("EncodedByteAlign");
    /**
     * A name
     */
    public static final PdfName ENCODING = new PdfName("Encoding");
    /**
     * A name
     */
    public static final PdfName ENCRYPT = new PdfName("Encrypt");
    /**
     * A name
     */
    public static final PdfName ENDOFBLOCK = new PdfName("EndOfBlock");
    /**
     * A name
     */
    public static final PdfName ENDOFLINE = new PdfName("EndOfLine");

    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName EXTENSIONS = new PdfName("Extensions");
    /**
     * A name
     */
    public static final PdfName EXTGSTATE = new PdfName("ExtGState");
    /**
     * (Optional) A set of flags specifying various characteristics of the annotation Default value: 0.
     */
    public static final PdfName F = new PdfName("F");
    /**
     * A name
     */
    public static final PdfName FILEID = new PdfName("FileId");
    /**
     * A name
     */
    public static final PdfName FILTER = new PdfName("Filter");
    /**
     * A name
     */
    public static final PdfName FIRST = new PdfName("First");
    /**
     * A name
     */
    public static final PdfName FIRSTCHAR = new PdfName("FirstChar");
    /**
     * A name
     */
    public static final PdfName FIRSTPAGE = new PdfName("FirstPage");
    /**
     * A name
     */
    public static final PdfName FLAGS = new PdfName("Flags");
    /**
     * A name
     */
    public static final PdfName FLATEDECODE = new PdfName("FlateDecode");
    /**
     * A name
     */
    public static final PdfName FONT = new PdfName("Font");
    /**
     * A name
     */
    public static final PdfName FONTBBOX = new PdfName("FontBBox");
    /**
     * A name
     */
    public static final PdfName FONTDESCRIPTOR = new PdfName("FontDescriptor");
    /**
     * A name
     */
    public static final PdfName FONTFILE = new PdfName("FontFile");
    /**
     * (Required) The PostScript name of the font. This name shall be the same as the value of BaseFont in the font or
     * CIDFont dictionary that refers to this font descriptor.
     */
    public static final PdfName FONTNAME = new PdfName("FontName");

    /**
     * A name
     */
    public static final PdfName FORM = new PdfName("Form");
    /**
     * A name
     */
    public static final PdfName FORMTYPE = new PdfName("FormType");
    /**
     * A name
     */
    public static final PdfName FS = new PdfName("FS");
    /**
     * A name of an attribute.
     */
    public static final PdfName GOTO = new PdfName("GoTo");
    /**
     * A name of an attribute.
     */
    public static final PdfName GOTOR = new PdfName("GoToR");
    /**
     * A name of an attribute.
     */
    public static final PdfName GROUP = new PdfName("Group");
    /**
     * Optional) The annotation's highlighting mode, the visual effect that shall be used when the mouse button is
     * pressed or held down inside its active area:<ul>
     * <li>N (None) No highlighting.
     * <li>I (Invert) Invert the colours used to display the contents of the annotation rectangle.
     * <li>O (Outline) Stroke the colours used to display the annotation border.
     * <li>P (Push) Display the annotation's down appearance, if any. If no down appearance is defined, the contents of
     * the annotation rectangle shall be offset to appear as if it were beingpushed below the surface of the page.
     * <li>T (Toggle) Same as P (which is preferred).
     * </ul>
     * A highlighting mode other than P shall override any down appearance defined for the annotation. Default value:
     * I.
     */
    public static final PdfName H = new PdfName("H");
    /**
     * A name of an attribute.
     */
    public static final PdfName HEIGHT = new PdfName("Height");
    /**
     * A name of a base 14 type 1 font
     */
    public static final PdfName HELVETICA = new PdfName("Helvetica");
    /**
     * A name of a base 14 type 1 font
     */
    public static final PdfName HELVETICA_BOLD = new PdfName("Helvetica-Bold");
    /**
     * A name of a base 14 type 1 font
     */
    public static final PdfName HELVETICA_OBLIQUE = new PdfName("Helvetica-Oblique");
    /**
     * A name of a base 14 type 1 font
     */
    public static final PdfName HELVETICA_BOLDOBLIQUE = new PdfName("Helvetica-BoldOblique");
    /**
     * (Sometimes required, otherwise optional) For choice fields that allow multiple selection (MultiSelect flag set),
     * an array of integers, sorted in ascending order, representing the zero-based indices in the Opt array of the
     * currently selected option items. This entry shall be used when two or more elements in the Opt array have
     * different names but the same export value or when the value of the choice field is an array. If the items
     * identified by this entry differ from those in the V entry of the field dictionary (see discussion following this
     * Table), the V entry shall be used.
     */
    public static final PdfName I = new PdfName("I");
    /**
     * A name
     */
    public static final PdfName ID = new PdfName("ID");
    /**
     * A name
     */
    public static final PdfName IDENTITY = new PdfName("Identity");
    /**
     * A name
     */
    public static final PdfName IF = new PdfName("IF");
    /**
     * A name
     */
    public static final PdfName IMAGE = new PdfName("Image");
    /**
     * A name
     */
    public static final PdfName IMAGEMASK = new PdfName("ImageMask");
    /**
     * A name
     */
    public static final PdfName INDEX = new PdfName("Index");
    /**
     * A name
     */
    public static final PdfName INDEXED = new PdfName("Indexed");
    /**
     * A name
     */
    public static final PdfName INFO = new PdfName("Info");
    /**
     * A name
     */
    public static final PdfName INTENT = new PdfName("Intent");
    /**
     * A name
     */
    public static final PdfName INTERPOLATE = new PdfName("Interpolate");
    /**
     * A name
     */
    public static final PdfName ISMAP = new PdfName("IsMap");
    /**
     * A name
     */
    public static final PdfName ITALICANGLE = new PdfName("ItalicAngle");

    /**
     * A name
     */
    public static final PdfName IX = new PdfName("IX");
    /**
     * A name
     */
    public static final PdfName JAVASCRIPT = new PdfName("JavaScript");
    /**
     * A name
     *
     * @since 2.1.5
     */
    public static final PdfName JBIG2GLOBALS = new PdfName("JBIG2Globals");
    /**
     * A name
     */
    public static final PdfName JS = new PdfName("JS");
    /**
     * A name
     */
    public static final PdfName K = new PdfName("K");
    /**
     * A name
     */
    public static final PdfName KEYWORDS = new PdfName("Keywords");
    /**
     * (Sometimes required, as described) An array of indirect references to the immediate children of this field. In a
     * non-terminal field, the Kids array shall refer to field dictionaries that are immediate descendants of this
     * field. In a terminal field, the Kids array ordinarily shall refer to one or more separate widget annotations that
     * are associated with this field. However, if there is only one associated widget annotation, and its contents have
     * been merged into the field dictionary, Kids shall be omitted.
     */
    public static final PdfName KIDS = new PdfName("Kids");
    /**
     * A name
     */
    public static final PdfName L = new PdfName("L");
    /**
     * A name
     */
    public static final PdfName LASTCHAR = new PdfName("LastChar");
    /**
     * A name
     */
    public static final PdfName LASTPAGE = new PdfName("LastPage");
    /**
     * A name
     */
    public static final PdfName LENGTH = new PdfName("Length");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName LI = new PdfName("LI");
    /**
     * A name
     */
    public static final PdfName LINK = new PdfName("Link");
    /**
     * (Optional) The date and time when the annotation was most recently modified. The format should be a date string
     * (a text string containing no whitespace, of the form (D:YYYYMMDDHHmmSSOHH'mm)) but interactive PDF processors
     * shall accept and display a string in any format.
     */
    public static final PdfName M = new PdfName("M");
    /**
     * A name
     */
    public static final PdfName MATRIX = new PdfName("Matrix");
    /**
     * A name of an encoding
     */
    public static final PdfName MAC_ROMAN_ENCODING = new PdfName("MacRomanEncoding");
    /**
     * A name
     */
    public static final PdfName MARKED = new PdfName("Marked");
    /**
     * A name
     */
    public static final PdfName MASK = new PdfName("Mask");
    /**
     * A name
     */
    public static final PdfName MEDIABOX = new PdfName("MediaBox");
    /**
     * A name
     */
    public static final PdfName METADATA = new PdfName("Metadata");
    /**
     * A name
     */
    public static final PdfName MODDATE = new PdfName("ModDate");
    /**
     * A name
     */
    public static final PdfName N = new PdfName("N");
    /**
     * (optional) The name of the person or authority signing the document. This value should be used only when it is
     * not possible to extract the name from the signature.
     */
    public static final PdfName NAME = new PdfName("Name");
    /**
     * A name
     */
    public static final PdfName NAMED = new PdfName("Named");
    /**
     * A name
     */
    public static final PdfName NEXTPAGE = new PdfName("NextPage");

    /**
     * A name
     */
    public static final PdfName O = new PdfName("O");
    /**
     * A name
     */
    public static final PdfName OBJSTM = new PdfName("ObjStm");
    /**
     * (Optional) An optional content group or optional content membership dictionary specifying the optional content
     * properties for the annotation. Before the annotation is drawn, its visibility shall be determined based on this
     * entry as well as the annotation flags specified in the F entry. If it is determined to be invisible, the
     * annotation shall not be drawn.
     */
    public static final PdfName OC = new PdfName("OC");
    /**
     * A name
     */
    public static final PdfName OCG = new PdfName("OCG");
    /**
     * A name
     */
    public static final PdfName OFF = new PdfName("OFF");
    /**
     * A name
     */
    public static final PdfName ON = new PdfName("ON");
    /**
     * A name
     */
    public static final PdfName op = new PdfName("op");
    /**
     * Parts of the Build Data Dictionary. Indicates the operating system, such as Win10. Currently there is no specific
     * string format defined for the value of this attribute.
     */
    public static final PdfName OS = new PdfName("OS");

    /**
     * A name
     */
    public static final PdfName OUTLINES = new PdfName("Outlines");
    /**
     * (Optional) An indirect reference to the page object with which this annotation is associated. This entry shall be
     * present in screen annotations associated with rendition actions. OR (Optional; PDF 2.0) The access permissions
     * granted for this document. Valid values shall be 1,2,3
     */
    public static final PdfName P = new PdfName("P");
    /**
     * A name
     */
    public static final PdfName PAGE = new PdfName("Page");
    /**
     * A name
     */
    public static final PdfName PAGES = new PdfName("Pages");
    /**
     * (Required if this field is the child of another in the field hierarchy; absent otherwise) The field that is the
     * immediate parent of this one (the field, if any, whose Kids array includes this field). A field can have at most
     * one parent; that is, it can be included in the Kids array of at most one other field.
     */
    public static final PdfName PARENT = new PdfName("Parent");
    /**
     * A name
     */
    public static final PdfName PATTERN = new PdfName("Pattern");
    /**
     * A name
     */
    public static final PdfName PDF = new PdfName("PDF");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName PI = new PdfName("PI");
    /**
     * A name
     */
    public static final PdfName PREV = new PdfName("Prev");
    /**
     * A name
     */
    public static final PdfName PREVPAGE = new PdfName("PrevPage");
    /**
     * A name
     */
    public static final PdfName PRODUCER = new PdfName("Producer");

    /**
     * A name
     */
    public static final PdfName PROPERTIES = new PdfName("Properties");
    /**
     * A name
     */
    public static final PdfName PS = new PdfName("PS");
    /**
     * (Optional; inheritable) A code specifying the form of quadding (justification) that shall be used in displaying
     * the text:
     * <ul>
     * <li>0 Left-justified (default) </li>
     * <li>1 Centered </li>
     * <li>2 Right-justified </li>
     * </ul>
     */
    public static final PdfName Q = new PdfName("Q");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName QUOTE = new PdfName("Quote");
    /**
     * Part of the Build Data Dictionary. The software module revision number
     */
    public static final PdfName R = new PdfName("R");
    /**
     * (Required) The annotation rectangle, defining the location of the annotation on the page in default user space
     * units.
     */
    public static final PdfName RECT = new PdfName("Rect");
    /**
     * A name
     */
    public static final PdfName RESOURCES = new PdfName("Resources");
    /**
     * A name
     */
    public static final PdfName RI = new PdfName("RI");
    /**
     * A name
     */
    public static final PdfName ROOT = new PdfName("Root");
    /**
     * A name
     */
    public static final PdfName ROTATE = new PdfName("Rotate");
    /**
     * A name
     */
    public static final PdfName ROWS = new PdfName("Rows");
    /**
     * A name
     */
    public static final PdfName S = new PdfName("S");
    /**
     * A name
     */
    public static final PdfName SHADING = new PdfName("Shading");
    /**
     * A name
     */
    public static final PdfName SIZE = new PdfName("Size");
    /**
     * A name
     */
    public static final PdfName SMASK = new PdfName("SMask");
    /**
     * A name
     */
    public static final PdfName STEMV = new PdfName("StemV");
    /**
     * A name
     */
    public static final PdfName SUBJECT = new PdfName("Subject");
    /**
     * (Required) The type of annotation that this dictionary describes.
     */
    public static final PdfName SUBTYPE = new PdfName("Subtype");
    /**
     * A name
     */
    public static final PdfName SW = new PdfName("SW");
    /**
     * A name of a base 14 type 1 font
     */
    public static final PdfName SYMBOL = new PdfName("Symbol");
    /**
     * (Required) The partial field name.
     */
    public static final PdfName T = new PdfName("T");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName TA = new PdfName("TA");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName TABLE = new PdfName("Table");
    /**
     * A name
     *
     * @since 2.1.5
     */
    public static final PdfName TABS = new PdfName("Tabs");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName TD = new PdfName("TD");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName TH = new PdfName("TH");
    /**
     * A name of a base 14 type 1 font
     */
    public static final PdfName TIMES_ROMAN = new PdfName("Times-Roman");
    /**
     * A name of a base 14 type 1 font
     */
    public static final PdfName TIMES_BOLD = new PdfName("Times-Bold");
    /**
     * A name of a base 14 type 1 font
     */
    public static final PdfName TIMES_ITALIC = new PdfName("Times-Italic");
    /**
     * A name of a base 14 type 1 font
     */
    public static final PdfName TIMES_BOLDITALIC = new PdfName("Times-BoldItalic");
    /**
     * A name
     */
    public static final PdfName TITLE = new PdfName("Title");
    /**
     * Optional) The mapping name that shall be used when exporting interactive form field data from the document.
     */
    public static final PdfName TM = new PdfName("TM");
    /**
     * A name
     */
    public static final PdfName TRANSPARENCY = new PdfName("Transparency");
    /**
     * A name
     */
    public static final PdfName TRIMBOX = new PdfName("TrimBox");
    /**
     * There are lots of Type s in the PDF specification. The following dictionaries use Type:
     *
     * <ul>
     *  <li>Crypt filters</li>
     *  <li>object stream dictionary</li>
     *  <li>cross-reference stream dictionary</li>
     *  <li>encrypted payload dictionary</li>
     *  <li>catalog dictionary</li>
     *  <li>page tree node</li>
     *  <li>page object</li>
     *  <li>file specification dictionary</li>
     *  <li>embedded file stream dictionary</li>
     *  <li>collection item dictionary</li>
     *  <li>collection subitem dictionary</li>
     *  <li>extensions dictionary</li>
     *  <li>developer extensions dictionary</li>
     *  <li>graphics state parameter dictionary</li>
     *  <li>Type 1 pattern dictionary</li>
     *  <li>DSS dictionary</li>
     *  <li>Type 2 pattern dictionary</li>
     *  <li>image dictionary</li>
     *  <li>Type 1 form dictionary</li>
     *  <li>group attributes dictionary</li>
     *  <li>content group dictionary</li>
     *  <li>content membership dictionary</li>
     *  <li>Type 1 font dictionary</li>
     *  <li>Type 3 font dictionary</li>
     *  <li>encoding dictionary</li>
     *  <li>outline dictionary</li>
     *  <li>annotation dictionaries</li>
     *  <li>field lock dictionary</li>
     *  <li>...</li>
     *
     *  </ul>
     */
    public static final PdfName TYPE = new PdfName("Type");
    /**
     * A name
     */
    public static final PdfName TYPE1 = new PdfName("Type1");
    /**
     * A name of an attribute.
     */
    public static final PdfName U = new PdfName("U");
    /**
     * (Required if R=6 (PDF 2.0)) A 32-byte string, based on the user password, that shall be used in computing the
     * file encryption key.
     */
    public static final PdfName UE = new PdfName("UE");
    /**
     * A name
     */
    public static final PdfName UR = new PdfName("UR");
    /**
     * A name
     */
    public static final PdfName URI = new PdfName("URI");
    /**
     * A name
     */
    public static final PdfName URL = new PdfName("URL");
    /**
     * A name
     */
    public static final PdfName USERUNIT = new PdfName("UserUnit");
    /**
     * (Optional; inheritable) The field value, whose format varies depending on the field type. See the descriptions of
     * individual field types for further information.
     */
    public static final PdfName V = new PdfName("V");
    /**
     * (Deprecated) The application shall ask the security handler for the file encryption key and shall implicitly
     * decrypt data with7.6.3.1, "Algorithm 1: Encryption of data using the RC4 or AESalgorithms", using the RC4
     * algorithm.
     */
    public static final PdfName V2 = new PdfName("V2");
    /**
     * A name
     */
    public static final PdfName VERSION = new PdfName("Version");
    /**
     * A name of an attribute.
     */
    public static final PdfName W = new PdfName("W");
    /**
     * A name of an attribute.
     */
    public static final PdfName WIDTH = new PdfName("Width");
    /**
     * A name
     */
    public static final PdfName WIDTHS = new PdfName("Widths");
    /**
     * A name of an encoding
     */
    public static final PdfName WIN_ANSI_ENCODING = new PdfName("WinAnsiEncoding");
    /**
     * A name of an encoding
     */
    public static final PdfName WS = new PdfName("WS");
    /**
     * A name
     */
    public static final PdfName X = new PdfName("X");
    /**
     * A name
     */
    public static final PdfName XML = new PdfName("XML");
    /**
     * A name
     */
    public static final PdfName XOBJECT = new PdfName("XObject");
    /**
     * A name
     */
    public static final PdfName XREF = new PdfName("XRef");
    /**
     * A name
     */
    public static final PdfName XYZ = new PdfName("XYZ");
    /**
     * A name of a base 14 type 1 font
     */
    public static final PdfName ZAPFDINGBATS = new PdfName("ZapfDingbats");
    private int hash = 0;

    /**
     * Constructs a new <CODE>PdfName</CODE>. The name length will be checked.
     *
     * @param name the new name
     */
    public PdfName(String name) {
        this(name, true);
    }

    /**
     * Constructs a new <CODE>PdfName</CODE>.
     *
     * @param name        the new name
     * @param lengthCheck if <CODE>true</CODE> check the length validity, if <CODE>false</CODE> the name can have any
     *                    length
     */
    public PdfName(String name, boolean lengthCheck) {
        super(PdfObject.NAME);
        // The minimum number of characters in a name is 0, the maximum is 127 (the '/' not included)
        int length = name.length();
        if (lengthCheck && length > 127) {
            throw new IllegalArgumentException(
                    MessageLocalization.getComposedMessage("the.name.1.is.too.long.2.characters", name,
                            String.valueOf(length)));
        }
        bytes = encodeName(name);
    }

    // CLASS VARIABLES

    // CLASS METHODS

    /**
     * Encodes a plain name given in the unescaped form "AB CD" into "/AB#20CD".
     *
     * @param name the name to encode
     * @return the encoded name
     * @since 2.1.5
     */
    public static byte[] encodeName(String name) {
        int length = name.length();
        ByteBuffer buf = new ByteBuffer(length + 20);
        buf.append('/');
        char c;
        char[] chars = name.toCharArray();
        for (int k = 0; k < length; k++) {
            c = (char) (chars[k] & 0xff);
            // Escape special characters
            switch (c) {
                case ' ':
                case '%':
                case '(':
                case ')':
                case '<':
                case '>':
                case '[':
                case ']':
                case '{':
                case '}':
                case '/':
                case '#':
                    buf.append('#');
                    buf.append(Integer.toString(c, 16));
                    break;
                default:
                    if (c >= 32 && c <= 126) {
                        buf.append(c);
                    } else {
                        buf.append('#');
                        if (c < 16) {
                            buf.append('0');
                        }
                        buf.append(Integer.toString(c, 16));
                    }
                    break;
            }
        }
        return buf.toByteArray();
    }

    /**
     * Compares this object with the specified object for order. Returns a negative integer, zero, or a positive integer
     * as this object is less than, equal to, or greater than the specified object.<p>
     *
     * @param name the Object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than
     * the specified object.
     * @throws ClassCastException if the specified object's type prevents it from being compared to this Object.
     */
    public int compareTo(PdfName name) {
        byte[] myBytes = bytes;
        byte[] objBytes = name.bytes;
        int len = Math.min(myBytes.length, objBytes.length);
        for (int i = 0; i < len; i++) {
            if (myBytes[i] > objBytes[i]) {
                return 1;
            }
            if (myBytes[i] < objBytes[i]) {
                return -1;
            }
        }
        return Integer.compare(myBytes.length, objBytes.length);
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the obj
     * argument; <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof PdfName) {
            return compareTo((PdfName) obj) == 0;
        }
        return false;
    }

    /**
     * Returns a hash code value for the object. This method is supported for the benefit of hashtables such as those
     * provided by
     * <code>java.util.Hashtable</code>.
     *
     * @return a hash code value for this object.
     */
    public int hashCode() {
        int h = hash;
        if (h == 0) {
            int ptr = 0;
            int len = bytes.length;
            for (int i = 0; i < len; i++) {
                h = 31 * h + (bytes[ptr++] & 0xff);
            }
            hash = h;
        }
        return h;
    }
}
