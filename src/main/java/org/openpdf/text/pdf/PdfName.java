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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    // CLASS CONSTANTS (a variety of standard names used in PDF))
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName _3D = new PdfName("3D");
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
     *
     * @since 2.1.5 renamed from ABSOLUTECALORIMETRIC
     */
    public static final PdfName ABSOLUTECOLORIMETRIC = new PdfName("AbsoluteColorimetric");
    /**
     * A name
     */
    public static final PdfName AC = new PdfName("AC");
    /**
     * A name
     */
    public static final PdfName ACROFORM = new PdfName("AcroForm");
    /**
     * (Required) A name which, in conjunction with Fields, indicates the set of fields that should be locked. The value
     * shall be one of the following: All, include or exclude.
     */
    public static final PdfName ACTION = new PdfName("Action");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName ACTIVATION = new PdfName("Activation");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName ADBE = new PdfName("ADBE");
    /**
     * a name used in PDF structure
     *
     * @since 2.1.6
     */
    public static final PdfName ACTUALTEXT = new PdfName("ActualText");
    /**
     * A name
     */
    public static final PdfName ADBE_PKCS7_DETACHED = new PdfName("adbe.pkcs7.detached");
    /**
     * A name
     */
    public static final PdfName ADBE_PKCS7_S4 = new PdfName("adbe.pkcs7.s4");
    /**
     * A name
     */
    public static final PdfName ADBE_PKCS7_S5 = new PdfName("adbe.pkcs7.s5");
    /**
     * A name
     */
    public static final PdfName ADBE_PKCS7_SHA1 = new PdfName("adbe.pkcs7.sha1");
    /**
     * A name
     */
    public static final PdfName ADBE_X509_RSA_SHA1 = new PdfName("adbe.x509.rsa_sha1");
    /**
     * A name
     */
    public static final PdfName ADOBE_PPKLITE = new PdfName("Adobe.PPKLite");
    /**
     * A name
     */
    public static final PdfName ADOBE_PPKMS = new PdfName("Adobe.PPKMS");
    /**
     * (PDF 1.6; deprecated) The application shall ask the security handler for the file encryption key and shall
     * implicitly decrypt data with 7.6.3.1, "Algorithm 1: Encryption of data using the RC4or AES algorithms", using the
     * AES algorithm in Cipher BlockChaining (CBC) mode with a 16-byte block size and an initialization vector that
     * shall be randomly generated and placed as the first 16 bytes in the stream or string. The key size(Length) shall
     * be 128 bits.
     */
    public static final PdfName AESV2 = new PdfName("AESV2");
    /**
     * (PDF 2.0) The application shall ask the security handler for the file encryption key and shall implicitly decrypt
     * data with 7.6.3.2, "Algorithm 1.A: Encryption of data using the AES algorithms", using the AES-256 algorithm in
     * Cipher Block Chaining (CBC)with padding mode with a 16-byte block size and an initialization vector that is
     * randomly generated and placed as the first 16 bytes in the stream or string. The key size (Length)shall be 256
     * bits.
     */
    public static final PdfName AESV3 = new PdfName("AESV3");
    /**
     * (Optional; PDF 2.0) An array of one or more file specification dictionaries which denote the associated files for
     * this annotation.
     */
    public static final PdfName AF = new PdfName("AF");
    /**
     * A name
     */
    public static final PdfName AIS = new PdfName("AIS");

    /**
     * Stands for "Lock all fields in the document" which is one possible value of the Action attribute in a signature
     * field lock dictionary
     */
    public static final PdfName ALL = new PdfName("All");

    /**
     * A name
     */
    public static final PdfName ALLPAGES = new PdfName("AllPages");
    /**
     * A name
     */
    public static final PdfName ALT = new PdfName("Alt");
    /**
     * A name
     */
    public static final PdfName ALTERNATE = new PdfName("Alternate");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName ANIMATION = new PdfName("Animation");
    /**
     * A name
     */
    public static final PdfName ANNOT = new PdfName("Annot");
    /**
     * A name
     */
    public static final PdfName ANNOTS = new PdfName("Annots");
    /**
     * A name
     */
    public static final PdfName ANTIALIAS = new PdfName("AntiAlias");
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
     * A build data dictionary for a description of the signature APP.
     */
    public static final PdfName APP = new PdfName("App");

    /**
     * A name
     */
    public static final PdfName APPDEFAULT = new PdfName("AppDefault");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName ART = new PdfName("Art");
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
    public static final PdfName ASCII85DECODE = new PdfName("ASCII85Decode");
    /**
     * A name
     */
    public static final PdfName ASCIIHEXDECODE = new PdfName("ASCIIHexDecode");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName ASSET = new PdfName("Asset");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName ASSETS = new PdfName("Assets");
    /**
     * A name
     */
    public static final PdfName AUTHEVENT = new PdfName("AuthEvent");
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
     *
     * @since 2.1.6
     */
    public static final PdfName BACKGROUND = new PdfName("Background");
    /**
     * A name
     */
    public static final PdfName BASEENCODING = new PdfName("BaseEncoding");
    /**
     * A name
     */
    public static final PdfName BASEFONT = new PdfName("BaseFont");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName BASEVERSION = new PdfName("BaseVersion");
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
    public static final PdfName BG = new PdfName("BG");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName BIBENTRY = new PdfName("BibEntry");
    /**
     * A name
     */
    public static final PdfName BIGFIVE = new PdfName("BigFive");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName BINDING = new PdfName("Binding");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName BINDINGMATERIALNAME = new PdfName("BindingMaterialName");
    /**
     * A name
     */
    public static final PdfName BITSPERCOMPONENT = new PdfName("BitsPerComponent");
    /**
     * A name
     */
    public static final PdfName BITSPERSAMPLE = new PdfName("BitsPerSample");
    /**
     * A name
     */
    public static final PdfName BL = new PdfName("Bl");
    /**
     * A name
     */
    public static final PdfName BLACKIS1 = new PdfName("BlackIs1");
    /**
     * A name
     */
    public static final PdfName BLACKPOINT = new PdfName("BlackPoint");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName BLOCKQUOTE = new PdfName("BlockQuote");
    /**
     * A name
     */
    public static final PdfName BLEEDBOX = new PdfName("BleedBox");
    /**
     * A name
     */
    public static final PdfName BLINDS = new PdfName("Blinds");
    /**
     * (Optional; PDF 2.0) The blend mode that shall be used when painting the annotation onto the page. If this key is
     * not present, blending shall take place using the Normal blend mode. The value shall be a name object, designating
     * one of the standard blend modes.
     */
    public static final PdfName BM = new PdfName("BM");
    /**
     * Optional) An array specifying the characteristics of the annotation's border, which shall be drawn as a rounded
     * rectangle.
     */
    public static final PdfName BORDER = new PdfName("Border");
    /**
     * A name
     */
    public static final PdfName BOUNDS = new PdfName("Bounds");
    /**
     * A name
     */
    public static final PdfName BOX = new PdfName("Box");
    /**
     * A name
     */
    public static final PdfName BS = new PdfName("BS");
    /**
     * A name
     */
    public static final PdfName BTN = new PdfName("Btn");
    /**
     * An array of pairs of integers (starting byte offset, length in bytes) describing the exact byte range for the
     * digest calculation. (Required)
     */
    public static final PdfName BYTERANGE = new PdfName("ByteRange");
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
    public static final PdfName CALGRAY = new PdfName("CalGray");
    /**
     * A name
     */
    public static final PdfName CALRGB = new PdfName("CalRGB");
    /**
     * A name
     */
    public static final PdfName CAPHEIGHT = new PdfName("CapHeight");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName CAPTION = new PdfName("Caption");
    /**
     * A name
     */
    public static final PdfName CATALOG = new PdfName("Catalog");
    /**
     * A name
     */
    public static final PdfName CATEGORY = new PdfName("Category");
    /**
     * A name
     */
    public static final PdfName CCITTFAXDECODE = new PdfName("CCITTFaxDecode");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName CENTER = new PdfName("Center");
    /**
     * A name
     */
    public static final PdfName CENTERWINDOW = new PdfName("CenterWindow");
    /**
     * A name
     */
    public static final PdfName CERT = new PdfName("Cert");

    /**
     * An array of indirect reference to streams, each containing one DER-encoded X.509 certificate (see RFC 5280). This
     * array contains certificates that maybe used in the validation of any signatures in the document.
     */
    public static final PdfName CERTS = new PdfName("Certs");

    /**
     * A name
     */
    public static final PdfName CF = new PdfName("CF");
    /**
     * A name
     */
    public static final PdfName CFM = new PdfName("CFM");
    /**
     * A name
     */
    public static final PdfName CH = new PdfName("Ch");
    /**
     * A name
     */
    public static final PdfName CHARPROCS = new PdfName("CharProcs");
    /**
     * A name
     */
    public static final PdfName CHECKSUM = new PdfName("CheckSum");
    /**
     * A name
     */
    public static final PdfName CI = new PdfName("CI");
    /**
     * A name
     */
    public static final PdfName CIDFONTTYPE0 = new PdfName("CIDFontType0");
    /**
     * A name
     */
    public static final PdfName CIDFONTTYPE2 = new PdfName("CIDFontType2");
    /**
     * A name
     *
     * @since 2.0.7
     */
    public static final PdfName CIDSET = new PdfName("CIDSet");
    /**
     * A name
     */
    public static final PdfName CIDSYSTEMINFO = new PdfName("CIDSystemInfo");
    /**
     * A name
     */
    public static final PdfName CIDTOGIDMAP = new PdfName("CIDToGIDMap");
    /**
     * A name
     */
    public static final PdfName CIRCLE = new PdfName("Circle");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName CMD = new PdfName("CMD");
    /**
     * A name
     */
    public static final PdfName CO = new PdfName("CO");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName CODE = new PdfName("Code");
    /**
     * A name
     */
    public static final PdfName COLORS = new PdfName("Colors");
    /**
     * A name
     */
    public static final PdfName COLORSPACE = new PdfName("ColorSpace");
    /**
     * A name
     */
    public static final PdfName COLLECTION = new PdfName("Collection");
    /**
     * A name
     */
    public static final PdfName COLLECTIONFIELD = new PdfName("CollectionField");
    /**
     * A name
     */
    public static final PdfName COLLECTIONITEM = new PdfName("CollectionItem");
    /**
     * A name
     */
    public static final PdfName COLLECTIONSCHEMA = new PdfName("CollectionSchema");
    /**
     * A name
     */
    public static final PdfName COLLECTIONSORT = new PdfName("CollectionSort");
    /**
     * A name
     */
    public static final PdfName COLLECTIONSUBITEM = new PdfName("CollectionSubitem");
    /**
     * A name
     */
    public static final PdfName COLUMNS = new PdfName("Columns");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName CONDITION = new PdfName("Condition");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName CONFIGURATION = new PdfName("Configuration");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName CONFIGURATIONS = new PdfName("Configurations");
    /**
     * A name
     */
    public static final PdfName CONTACTINFO = new PdfName("ContactInfo");
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
    public static final PdfName COORDS = new PdfName("Coords");
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
    public static final PdfName CREATORINFO = new PdfName("CreatorInfo");

    /**
     * An array of indirect references to streams, each containing a DER-encoded Certificate Revocation List (CRL) (see
     * RFC 5280). This array contains CRLs that may be used in the validation of the signatures in the document.
     */
    public static final PdfName CRLS = new PdfName("CRLs");

    /**
     * A name
     */
    public static final PdfName CROPBOX = new PdfName("CropBox");
    /**
     * A name
     */
    public static final PdfName CRYPT = new PdfName("Crypt");
    /**
     * A name
     */
    public static final PdfName CS = new PdfName("CS");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName CUEPOINT = new PdfName("CuePoint");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName CUEPOINTS = new PdfName("CuePoints");
    /**
     * A name
     */
    public static final PdfName D = new PdfName("D");
    /**
     * (Required; inheritable) The default appearance string containing a sequence of valid page-content graphics or
     * text state operators that define such properties as the field's text size and colour.
     */
    public static final PdfName DA = new PdfName("DA");
    /**
     * A name
     */
    public static final PdfName DATA = new PdfName("Data");

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
    public static final PdfName DCTDECODE = new PdfName("DCTDecode");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName DEACTIVATION = new PdfName("Deactivation");
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
     *
     * @since 2.1.5 renamed from DEFAULTCRYPTFILER
     */
    public static final PdfName DEFAULTCRYPTFILTER = new PdfName("DefaultCryptFilter");
    /**
     * A name
     */
    public static final PdfName DEFAULTCMYK = new PdfName("DefaultCMYK");
    /**
     * A name
     */
    public static final PdfName DEFAULTGRAY = new PdfName("DefaultGray");
    /**
     * A name
     */
    public static final PdfName DEFAULTRGB = new PdfName("DefaultRGB");
    /**
     * A name
     */
    public static final PdfName DESC = new PdfName("Desc");
    /**
     * A name
     */
    public static final PdfName DESCENDANTFONTS = new PdfName("DescendantFonts");
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
    public static final PdfName DESTOUTPUTPROFILE = new PdfName("DestOutputProfile");
    /**
     * A name
     */
    public static final PdfName DESTS = new PdfName("Dests");
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
    public static final PdfName DI = new PdfName("Di");
    /**
     * A name
     */
    public static final PdfName DIFFERENCES = new PdfName("Differences");
    /**
     * A name
     */
    public static final PdfName DISSOLVE = new PdfName("Dissolve");
    /**
     * A name
     */
    public static final PdfName DIRECTION = new PdfName("Direction");
    /**
     * A name
     */
    public static final PdfName DISPLAYDOCTITLE = new PdfName("DisplayDocTitle");
    /**
     * A name
     */
    public static final PdfName DIV = new PdfName("Div");
    /**
     * A name
     */
    public static final PdfName DL = new PdfName("DL");
    /**
     * A name
     */
    public static final PdfName DM = new PdfName("Dm");
    /**
     * A name
     */
    public static final PdfName DOCMDP = new PdfName("DocMDP");
    /**
     * A name
     */
    public static final PdfName DOCOPEN = new PdfName("DocOpen");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName DOCUMENT = new PdfName("Document");
    /**
     * A name
     */
    public static final PdfName DOMAIN = new PdfName("Domain");
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
     * A DSS dictionary containing document-wide security information. See PDF 2.0 specification ch. 12.8.4.3 for
     * further details.
     */
    public static final PdfName DSS = new PdfName("DSS");

    /**
     * A name
     */
    public static final PdfName DUR = new PdfName("Dur");
    /**
     * A name
     */
    public static final PdfName DUPLEX = new PdfName("Duplex");
    /**
     * A name
     */
    public static final PdfName DUPLEXFLIPSHORTEDGE = new PdfName("DuplexFlipShortEdge");
    /**
     * A name
     */
    public static final PdfName DUPLEXFLIPLONGEDGE = new PdfName("DuplexFlipLongEdge");
    /**
     * (Optional; inheritable) The default value to which the field reverts when a reset-form action is executed. The
     * format of this value is the same as that of V.
     */
    public static final PdfName DV = new PdfName("DV");
    /**
     * A name
     */
    public static final PdfName DW = new PdfName("DW");
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
    public static final PdfName EARLYCHANGE = new PdfName("EarlyChange");
    /**
     * A name
     */
    public static final PdfName EF = new PdfName("EF");
    /**
     * A name
     *
     * @since 2.1.3
     */
    public static final PdfName EFF = new PdfName("EFF");
    /**
     * A name
     *
     * @since 2.1.3
     */
    public static final PdfName EFOPEN = new PdfName("EFOpen");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName EMBEDDED = new PdfName("Embedded");
    /**
     * A name
     */
    public static final PdfName EMBEDDEDFILE = new PdfName("EmbeddedFile");
    /**
     * A name
     */
    public static final PdfName EMBEDDEDFILES = new PdfName("EmbeddedFiles");
    /**
     * A name
     */
    public static final PdfName ENCODE = new PdfName("Encode");
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
    public static final PdfName ENCRYPTMETADATA = new PdfName("EncryptMetadata");
    /**
     * A name
     */
    public static final PdfName ENDOFBLOCK = new PdfName("EndOfBlock");
    /**
     * A name
     */
    public static final PdfName ENDOFLINE = new PdfName("EndOfLine");

    /**
     * Extension supplied by ETSI TS 102 778-4 V1.1.2 (2009-12)
     */
    public static final PdfName ESIC = new PdfName("ESIC");

    /**
     * Stands for "Exclude all fields except those specified in Fields array" which is one possible value of the Action
     * attribute in a signature field lock dictionary
     */
    public static final PdfName EXCLUDE = new PdfName("Exclude");

    /**
     * A name
     */
    public static final PdfName EXTEND = new PdfName("Extend");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName EXTENSIONS = new PdfName("Extensions");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName EXTENSIONLEVEL = new PdfName("ExtensionLevel");
    /**
     * A name
     */
    public static final PdfName EXTGSTATE = new PdfName("ExtGState");
    /**
     * A name
     */
    public static final PdfName EXPORT = new PdfName("Export");
    /**
     * A name
     */
    public static final PdfName EXPORTSTATE = new PdfName("ExportState");
    /**
     * A name
     */
    public static final PdfName EVENT = new PdfName("Event");
    /**
     * (Optional) A set of flags specifying various characteristics of the annotation Default value: 0.
     */
    public static final PdfName F = new PdfName("F");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName FAR = new PdfName("Far");
    /**
     * A name
     */
    public static final PdfName FB = new PdfName("FB");
    /**
     * A name
     */
    public static final PdfName FDECODEPARMS = new PdfName("FDecodeParms");
    /**
     * A name
     */
    public static final PdfName FDF = new PdfName("FDF");
    /**
     * (Optional; inheritable) A set of flags specifying various characteristics of the field. Default value: 0.
     */
    public static final PdfName FF = new PdfName("Ff");
    /**
     * A name
     */
    public static final PdfName FFILTER = new PdfName("FFilter");

    /**
     * The FieldMDP transform method shall be used to detect changes to the values of a list of form fields. The entries
     * in its transform parameters dictionary are Type, Actions, Field and V.
     */
    public static final PdfName FIELDMDP = new PdfName("FieldMDP");

    /**
     * A name
     */
    public static final PdfName FIELDS = new PdfName("Fields");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName FIGURE = new PdfName("Figure");
    /**
     * A name
     */
    public static final PdfName FILEATTACHMENT = new PdfName("FileAttachment");
    /**
     * A name
     */
    public static final PdfName FILEID = new PdfName("FileId");
    /**
     * A name
     */
    public static final PdfName FILESPEC = new PdfName("Filespec");
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
    public static final PdfName FIT = new PdfName("Fit");
    /**
     * A name
     */
    public static final PdfName FITH = new PdfName("FitH");
    /**
     * A name
     */
    public static final PdfName FITV = new PdfName("FitV");
    /**
     * A name
     */
    public static final PdfName FITR = new PdfName("FitR");
    /**
     * A name
     */
    public static final PdfName FITB = new PdfName("FitB");
    /**
     * A name
     */
    public static final PdfName FITBH = new PdfName("FitBH");
    /**
     * A name
     */
    public static final PdfName FITBV = new PdfName("FitBV");
    /**
     * A name
     */
    public static final PdfName FITWINDOW = new PdfName("FitWindow");
    /**
     * A name
     */
    public static final PdfName FLAGS = new PdfName("Flags");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName FLASH = new PdfName("Flash");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName FLASHVARS = new PdfName("FlashVars");
    /**
     * A name
     */
    public static final PdfName FLATEDECODE = new PdfName("FlateDecode");
    /**
     * The {@code /BrotliDecode} filter — being standardised as a PDF 2.0 stream filter
     * through ISO/TS 32001.
     */
    public static final PdfName BROTLIDECODE = new PdfName("BrotliDecode");
    /**
     * A name
     */
    public static final PdfName FO = new PdfName("Fo");
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
     * A byte string specifying the preferred font family name. E.g. for the font Times Bold Italic, the FontFamily is
     * Times.
     */
    public static final PdfName FONTFAMILY = new PdfName("FontFamily");
    /**
     * A name
     */
    public static final PdfName FONTFILE = new PdfName("FontFile");
    /**
     * A name
     */
    public static final PdfName FONTFILE2 = new PdfName("FontFile2");
    /**
     * A name
     */
    public static final PdfName FONTFILE3 = new PdfName("FontFile3");
    /**
     * A name
     */
    public static final PdfName FONTMATRIX = new PdfName("FontMatrix");
    /**
     * (Required) The PostScript name of the font. This name shall be the same as the value of BaseFont in the font or
     * CIDFont dictionary that refers to this font descriptor.
     */
    public static final PdfName FONTNAME = new PdfName("FontName");

    /**
     * (Optional); PDF 1.5; The weight (thickness) component of the fully-qualified font name or font specifier. The
     * possible values shall be 100, 200, 300, 400, 500, 600, 700, 800, or 900, where each number indicates a weight
     * that is at least as dark as its predecessor. A value of 400 shall indicate a normal weight; 700 shall indicate
     * bold. The specific interpretation of these values varies from font to font. E.g. 300 in one font may appear most
     * similar to 500 in another.
     */
    public static final PdfName FONTWEIGHT = new PdfName("FontWeight");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName FOREGROUND = new PdfName("Foreground");
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
     *
     * @since 2.1.6
     */
    public static final PdfName FORMULA = new PdfName("Formula");
    /**
     * A name
     */
    public static final PdfName FREETEXT = new PdfName("FreeText");
    /**
     * A name
     */
    public static final PdfName FRM = new PdfName("FRM");
    /**
     * A name
     */
    public static final PdfName FS = new PdfName("FS");
    /**
     * The type of field that this dictionary describes: Btn Button, Tx Text, Ch Choice, Sig Signature (Required;
     * inheritable)
     */
    public static final PdfName FT = new PdfName("FT");
    /**
     * A name
     */
    public static final PdfName FULLSCREEN = new PdfName("FullScreen");
    /**
     * A name
     */
    public static final PdfName FUNCTION = new PdfName("Function");
    /**
     * A name
     */
    public static final PdfName FUNCTIONS = new PdfName("Functions");
    /**
     * A name
     */
    public static final PdfName FUNCTIONTYPE = new PdfName("FunctionType");
    /**
     * A name of an attribute.
     */
    public static final PdfName GAMMA = new PdfName("Gamma");
    /**
     * A name of an attribute.
     */
    public static final PdfName GBK = new PdfName("GBK");
    /**
     * A name of an attribute.
     */
    public static final PdfName GLITTER = new PdfName("Glitter");
    /**
     * A name of an attribute.
     */
    public static final PdfName GOTO = new PdfName("GoTo");
    /**
     * A name of an attribute.
     */
    public static final PdfName GOTOE = new PdfName("GoToE");
    /**
     * A name of an attribute.
     */
    public static final PdfName GOTOR = new PdfName("GoToR");
    /**
     * A name of an attribute.
     */
    public static final PdfName GROUP = new PdfName("Group");
    /**
     * A name of an attribute.
     */
    public static final PdfName GTS_PDFA1 = new PdfName("GTS_PDFA1");
    /**
     * A name of an attribute.
     */
    public static final PdfName GTS_PDFX = new PdfName("GTS_PDFX");
    /**
     * A name of an attribute.
     */
    public static final PdfName GTS_PDFXVERSION = new PdfName("GTS_PDFXVersion");
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
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName H1 = new PdfName("H1");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName H2 = new PdfName("H2");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName H3 = new PdfName("H3");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName H4 = new PdfName("H4");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName H5 = new PdfName("H5");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName H6 = new PdfName("H6");

    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName HALIGN = new PdfName("HAlign");
    /**
     * A name of an attribute.
     */
    public static final PdfName HEIGHT = new PdfName("Height");
    /**
     * A name
     */
    public static final PdfName HELV = new PdfName("Helv");
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
     * A name
     */
    public static final PdfName HID = new PdfName("Hid");
    /**
     * A name
     */
    public static final PdfName HIDE = new PdfName("Hide");
    /**
     * A name
     */
    public static final PdfName HIDEMENUBAR = new PdfName("HideMenubar");
    /**
     * A name
     */
    public static final PdfName HIDETOOLBAR = new PdfName("HideToolbar");
    /**
     * A name
     */
    public static final PdfName HIDEWINDOWUI = new PdfName("HideWindowUI");
    /**
     * A name
     */
    public static final PdfName HIGHLIGHT = new PdfName("Highlight");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName HOFFSET = new PdfName("HOffset");
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
    public static final PdfName ICCBASED = new PdfName("ICCBased");
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
    public static final PdfName IMAGEB = new PdfName("ImageB");
    /**
     * A name
     */
    public static final PdfName IMAGEC = new PdfName("ImageC");
    /**
     * A name
     */
    public static final PdfName IMAGEI = new PdfName("ImageI");
    /**
     * A name
     */
    public static final PdfName IMAGEMASK = new PdfName("ImageMask");
    /**
     * Stands for "Include all fields specified in Fields array" which is one possible value of the Action attribute in
     * a signature field lock dictionary
     */
    public static final PdfName INCLUDE = new PdfName("Include");
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
    public static final PdfName INK = new PdfName("Ink");
    /**
     * A name
     */
    public static final PdfName INKLIST = new PdfName("InkList");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName INSTANCES = new PdfName("Instances");
    /**
     * A name
     */
    public static final PdfName IMPORTDATA = new PdfName("ImportData");
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
    public static final PdfName IRT = new PdfName("IRT");
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
    public static final PdfName JBIG2DECODE = new PdfName("JBIG2Decode");
    /**
     * A name
     *
     * @since 2.1.5
     */
    public static final PdfName JBIG2GLOBALS = new PdfName("JBIG2Globals");
    /**
     * A name
     */
    public static final PdfName JPXDECODE = new PdfName("JPXDecode");
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
    public static final PdfName L2R = new PdfName("L2R");
    /**
     * (Optional; PDF 2.0) A language identifier overriding the document's language identifier to specify the natural
     * language for all text in the annotation except where overridden by other explicit language specifications.
     */
    public static final PdfName LANG = new PdfName("Lang");
    /**
     * A name
     */
    public static final PdfName LANGUAGE = new PdfName("Language");
    /**
     * A name
     */
    public static final PdfName LAST = new PdfName("Last");
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
    public static final PdfName LAUNCH = new PdfName("Launch");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName LBL = new PdfName("Lbl");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName LBODY = new PdfName("LBody");
    /**
     * A name
     */
    public static final PdfName LENGTH = new PdfName("Length");
    /**
     * A name
     */
    public static final PdfName LENGTH1 = new PdfName("Length1");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName LI = new PdfName("LI");
    /**
     * A name
     */
    public static final PdfName LIMITS = new PdfName("Limits");
    /**
     * A name
     */
    public static final PdfName LINE = new PdfName("Line");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName LINEAR = new PdfName("Linear");
    /**
     * A name
     */
    public static final PdfName LINK = new PdfName("Link");
    /**
     * A name
     */
    public static final PdfName LISTMODE = new PdfName("ListMode");
    /**
     * A name
     */
    public static final PdfName LOCATION = new PdfName("Location");
    /**
     * (Optional; shall be an indirect reference) A signature field lock dictionary that specifies a set of form fields
     * that shall be locked when this signature field is signed.
     */
    public static final PdfName LOCK = new PdfName("Lock");
    /**
     * A name
     *
     * @since 2.1.2
     */
    public static final PdfName LOCKED = new PdfName("Locked");
    /**
     * A name
     */
    public static final PdfName LZWDECODE = new PdfName("LZWDecode");
    /**
     * (Optional) The date and time when the annotation was most recently modified. The format should be a date string
     * (a text string containing no whitespace, of the form (D:YYYYMMDDHHmmSSOHH'mm)) but interactive PDF processors
     * shall accept and display a string in any format.
     */
    public static final PdfName M = new PdfName("M");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName MATERIAL = new PdfName("Material");
    /**
     * A name
     */
    public static final PdfName MATRIX = new PdfName("Matrix");
    /**
     * A name of an encoding
     */
    public static final PdfName MAC_EXPERT_ENCODING = new PdfName("MacExpertEncoding");
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
    public static final PdfName MARKINFO = new PdfName("MarkInfo");
    /**
     * A name
     */
    public static final PdfName MASK = new PdfName("Mask");
    /**
     * A name
     *
     * @since 2.1.6 renamed from MAX
     */
    public static final PdfName MAX_LOWER_CASE = new PdfName("max");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName MAX_CAMEL_CASE = new PdfName("Max");
    /**
     * (Optional; inheritable) The maximum length of the field's text, in characters
     */
    public static final PdfName MAXLEN = new PdfName("MaxLen");
    /**
     * A name
     */
    public static final PdfName MEDIABOX = new PdfName("MediaBox");
    /**
     * A name
     */
    public static final PdfName MCID = new PdfName("MCID");
    /**
     * A name
     */
    public static final PdfName MCR = new PdfName("MCR");
    /**
     * A name
     */
    public static final PdfName METADATA = new PdfName("Metadata");
    /**
     * A name
     *
     * @since 2.1.6 renamed from MIN
     */
    public static final PdfName MIN_LOWER_CASE = new PdfName("min");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName MIN_CAMEL_CASE = new PdfName("Min");
    /**
     * (Optional) An appearance characteristics dictionary that shall be used in constructing a dynamic appearance
     * stream specifying the annotation's visual presentation on the page.
     */
    public static final PdfName MK = new PdfName("MK");
    /**
     * A name
     */
    public static final PdfName MMTYPE1 = new PdfName("MMType1");
    /**
     * A name
     */
    public static final PdfName MODDATE = new PdfName("ModDate");
    /**
     * A name
     */
    public static final PdfName N = new PdfName("N");
    /**
     * A name
     */
    public static final PdfName N0 = new PdfName("n0");
    /**
     * A name
     */
    public static final PdfName N1 = new PdfName("n1");
    /**
     * A name
     */
    public static final PdfName N2 = new PdfName("n2");
    /**
     * A name
     */
    public static final PdfName N3 = new PdfName("n3");
    /**
     * A name
     */
    public static final PdfName N4 = new PdfName("n4");
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
    public static final PdfName NAMES = new PdfName("Names");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName NAVIGATION = new PdfName("Navigation");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName NAVIGATIONPANE = new PdfName("NavigationPane");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName NEAR = new PdfName("Near");
    /**
     * A name
     */
    public static final PdfName NEEDAPPEARANCES = new PdfName("NeedAppearances");
    /**
     * A name
     */
    public static final PdfName NEWWINDOW = new PdfName("NewWindow");
    /**
     * A name
     */
    public static final PdfName NEXT = new PdfName("Next");
    /**
     * A name
     */
    public static final PdfName NEXTPAGE = new PdfName("NextPage");

    /**
     * The annotation name, a text string uniquely identifying it among all the annotations on its page.
     */
    public static final PdfName NM = new PdfName("NM");
    /**
     * A name
     */
    public static final PdfName NONE = new PdfName("None");

    /**
     * If there is a Legal dictionary in the catalog of the PDF file, and the NonEmbeddedFonts attribute (which
     * specifies the number of fonts not embedded) in that dictionary has a non-zero value, and the viewing application
     * has a preference set to suppress the display of the warning about fonts not being embedded, then the value of
     * this attribute will be set to true (meaning that no warning need be displayed)
     */
    public static final PdfName NONEFONTNOWARN = new PdfName("NonEFontNoWarn");

    /**
     * A name
     */
    public static final PdfName NONFULLSCREENPAGEMODE = new PdfName("NonFullScreenPageMode");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName NONSTRUCT = new PdfName("NonStruct");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName NOTE = new PdfName("Note");
    /**
     * A name
     */
    public static final PdfName NUMCOPIES = new PdfName("NumCopies");
    /**
     * A name
     */
    public static final PdfName NUMS = new PdfName("Nums");
    /**
     * A name
     */
    public static final PdfName O = new PdfName("O");
    /**
     * A name used with Document Structure
     *
     * @since 2.1.5
     */
    public static final PdfName OBJ = new PdfName("Obj");
    /**
     * a name used with Doucment Structure
     *
     * @since 2.1.5
     */
    public static final PdfName OBJR = new PdfName("OBJR");
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
    public static final PdfName OCGS = new PdfName("OCGs");
    /**
     * A name
     */
    public static final PdfName OCMD = new PdfName("OCMD");
    /**
     * A name
     */
    public static final PdfName OCPROPERTIES = new PdfName("OCProperties");
    /**
     * (Optional) The checked value for the Checkbox form field. The recommended name for the on state is Yes, but this
     * is not required.
     */
    public static final PdfName YES = new PdfName("Yes");

    /**
     * An array of indirect references to streams, each containing a DER-encoded Online Certificate Status Protocol
     * (OCSP) response (see RFC 6960). This array contains OCSPs that may be used in the validation of the signatures in
     * the document.
     */
    public static final PdfName OCSPS = new PdfName("OCSPs");

    /**
     * (Required if R is 6 (PDF 2.0)) A 32-byte string, based on the owner and userpassword, that shall be used in
     * computing the file encryption key.
     */
    public static final PdfName OE = new PdfName("OE");
    /**
     * A name
     */
    public static final PdfName Off = new PdfName("Off");
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
    public static final PdfName ONECOLUMN = new PdfName("OneColumn");
    /**
     * A name
     */
    public static final PdfName OPEN = new PdfName("Open");
    /**
     * A name
     */
    public static final PdfName OPENACTION = new PdfName("OpenAction");
    /**
     * A name
     */
    public static final PdfName OP = new PdfName("OP");
    /**
     * A name
     */
    public static final PdfName op = new PdfName("op");
    /**
     * A name
     */
    public static final PdfName OPM = new PdfName("OPM");
    /**
     * (Optional; inheritable) An array containing one entry for each widget annotation in the Kids array of the radio
     * button or check box field. Each entry shall be a text string representing the on state of the corresponding
     * widget annotation. When this entry is present, the names used to represent the on state in the AP dictionary of
     * each annotation may use numerical position (starting with 0) of the annotation in the Kids array, encoded as a
     * name object (for example: /0, /1). This allows distinguishing between the annotations even if two or more of them
     * have the same value in the Opt array.
     */
    public static final PdfName OPT = new PdfName("Opt");
    /**
     * A name
     */
    public static final PdfName ORDER = new PdfName("Order");
    /**
     * A name
     */
    public static final PdfName ORDERING = new PdfName("Ordering");
    /**
     * Parts of the Build Data Dictionary. Indicates the operating system, such as Win10. Currently there is no specific
     * string format defined for the value of this attribute.
     */
    public static final PdfName OS = new PdfName("OS");

    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName OSCILLATING = new PdfName("Oscillating");

    /**
     * A name
     */
    public static final PdfName OUTLINES = new PdfName("Outlines");
    /**
     * A name
     */
    public static final PdfName OUTPUTCONDITION = new PdfName("OutputCondition");
    /**
     * A name
     */
    public static final PdfName OUTPUTCONDITIONIDENTIFIER = new PdfName("OutputConditionIdentifier");
    /**
     * A name
     */
    public static final PdfName OUTPUTINTENT = new PdfName("OutputIntent");
    /**
     * A name
     */
    public static final PdfName OUTPUTINTENTS = new PdfName("OutputIntents");
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
    public static final PdfName PAGELABELS = new PdfName("PageLabels");
    /**
     * A name
     */
    public static final PdfName PAGELAYOUT = new PdfName("PageLayout");
    /**
     * A name
     */
    public static final PdfName PAGEMODE = new PdfName("PageMode");
    /**
     * A name
     */
    public static final PdfName PAGES = new PdfName("Pages");
    /**
     * A name
     */
    public static final PdfName PAINTTYPE = new PdfName("PaintType");
    /**
     * A name
     */
    public static final PdfName PANOSE = new PdfName("Panose");
    /**
     * A name
     */
    public static final PdfName PARAMS = new PdfName("Params");
    /**
     * (Required if this field is the child of another in the field hierarchy; absent otherwise) The field that is the
     * immediate parent of this one (the field, if any, whose Kids array includes this field). A field can have at most
     * one parent; that is, it can be included in the Kids array of at most one other field.
     */
    public static final PdfName PARENT = new PdfName("Parent");
    /**
     * A name
     */
    public static final PdfName PARENTTREE = new PdfName("ParentTree");
    /**
     * A name used in defining Document Structure.
     *
     * @since 2.1.5
     */
    public static final PdfName PARENTTREENEXTKEY = new PdfName("ParentTreeNextKey");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName PART = new PdfName("Part");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName PASSCONTEXTCLICK = new PdfName("PassContextClick");
    /**
     * A name
     */
    public static final PdfName PATTERN = new PdfName("Pattern");
    /**
     * A name
     */
    public static final PdfName PATTERNTYPE = new PdfName("PatternType");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName PC = new PdfName("PC");
    /**
     * A name
     */
    public static final PdfName PDF = new PdfName("PDF");
    /**
     * A name
     */
    public static final PdfName PDFDOCENCODING = new PdfName("PDFDocEncoding");
    /**
     * A name
     */
    public static final PdfName PERCEPTUAL = new PdfName("Perceptual");
    /**
     * A name
     */
    public static final PdfName PERMS = new PdfName("Perms");
    /**
     * A name
     */
    public static final PdfName PG = new PdfName("Pg");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName PI = new PdfName("PI");
    /**
     * A name
     */
    public static final PdfName PICKTRAYBYPDFSIZE = new PdfName("PickTrayByPDFSize");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName PLAYCOUNT = new PdfName("PlayCount");
    /**
     * (Required; barcode fields only; ExtensionLevel 3) The PaperMetaData generation parameters dictionary. The entries
     * of this dictionary are instructions to the barcode encoding software on how to generate the barcode image. (Part
     * of *dobe Supplement to the ISO 32000)
     */
    public static final PdfName PMD = new PdfName("PMD");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName PO = new PdfName("PO");
    /**
     * A name
     */
    public static final PdfName POPUP = new PdfName("Popup");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName POSITION = new PdfName("Position");
    /**
     * A name
     */
    public static final PdfName PREDICTOR = new PdfName("Predictor");
    /**
     * A name
     */
    public static final PdfName PREFERRED = new PdfName("Preferred");
    /**
     * Parts of the Build Data Dictionary. A flag that can be used by the signature handler or software module to
     * indicate that this signature was created with unreleased software.
     */
    public static final PdfName PRERELEASE = new PdfName("PreRelease");

    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName PRESENTATION = new PdfName("Presentation");
    /**
     * A name
     */
    public static final PdfName PRESERVERB = new PdfName("PreserveRB");
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
    public static final PdfName PRINT = new PdfName("Print");
    /**
     * A name
     */
    public static final PdfName PRINTAREA = new PdfName("PrintArea");
    /**
     * A name
     */
    public static final PdfName PRINTCLIP = new PdfName("PrintClip");
    /**
     * A name
     */
    public static final PdfName PRINTPAGERANGE = new PdfName("PrintPageRange");
    /**
     * A name
     */
    public static final PdfName PRINTSCALING = new PdfName("PrintScaling");
    /**
     * A name
     */
    public static final PdfName PRINTSTATE = new PdfName("PrintState");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName PRIVATE = new PdfName("Private");
    /**
     * A name
     */
    public static final PdfName PROCSET = new PdfName("ProcSet");
    /**
     * A name
     */
    public static final PdfName PRODUCER = new PdfName("Producer");

    /**
     * The build properties dictionary and all of its contents are required to be direct objects. The use of a build
     * properties dictionary is optional but highly recommended. The build properties dictionary may contain a build
     * data dictionary entry for each unique software module used to create the signature. The software modules involved
     * in the signing process will vary depending on the viewing application. All signing implementations should include
     * at least a Filter entry in the build properties dictionary.
     */
    public static final PdfName PROP_BUILD = new PdfName("Prop_Build");

    /**
     * A name
     */
    public static final PdfName PROPERTIES = new PdfName("Properties");
    /**
     * A name
     */
    public static final PdfName PS = new PdfName("PS");
    /**
     * A name
     */
    public static final PdfName PUBSEC = new PdfName("Adobe.PubSec");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName PV = new PdfName("PV");
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
     */
    public static final PdfName QUADPOINTS = new PdfName("QuadPoints");
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
     * A name
     */
    public static final PdfName R2L = new PdfName("R2L");
    /**
     * A name
     */
    public static final PdfName RANGE = new PdfName("Range");
    /**
     * A name
     */
    public static final PdfName RC = new PdfName("RC");
    /**
     * A name
     */
    public static final PdfName RBGROUPS = new PdfName("RBGroups");
    /**
     * A name
     */
    public static final PdfName REASON = new PdfName("Reason");
    /**
     * A name
     */
    public static final PdfName RECIPIENTS = new PdfName("Recipients");
    /**
     * (Required) The annotation rectangle, defining the location of the annotation on the page in default user space
     * units.
     */
    public static final PdfName RECT = new PdfName("Rect");
    /**
     * A name
     */
    public static final PdfName REFERENCE = new PdfName("Reference");
    /**
     * A name
     */
    public static final PdfName REGISTRY = new PdfName("Registry");
    /**
     * A name
     */
    public static final PdfName REGISTRYNAME = new PdfName("RegistryName");
    /**
     * A name
     *
     * @since 2.1.5 renamed from RELATIVECALORIMETRIC
     */
    public static final PdfName RELATIVECOLORIMETRIC = new PdfName("RelativeColorimetric");
    /**
     * A name
     */
    public static final PdfName RENDITION = new PdfName("Rendition");
    /**
     * A name
     */
    public static final PdfName RESETFORM = new PdfName("ResetForm");
    /**
     * A name
     */
    public static final PdfName RESOURCES = new PdfName("Resources");
    /**
     * A name
     */
    public static final PdfName RI = new PdfName("RI");
    /**
     * Part of the Build Data Dictionary when used as the App dictionary. A text string indicating the version of the
     * application implementation, as described by the Name attribute in this dictionary. When set by Adobe Acrobat,
     * this entry is  in the format: major.minor.micro (for example 7.0.7).
     */
    public static final PdfName REX = new PdfName("REx");

    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName RICHMEDIA = new PdfName("RichMedia");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName RICHMEDIAACTIVATION = new PdfName("RichMediaActivation");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName RICHMEDIAANIMATION = new PdfName("RichMediaAnimation");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName RICHMEDIACOMMAND = new PdfName("RichMediaCommand");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName RICHMEDIACONFIGURATION = new PdfName("RichMediaConfiguration");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName RICHMEDIACONTENT = new PdfName("RichMediaContent");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName RICHMEDIADEACTIVATION = new PdfName("RichMediaDeactivation");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName RICHMEDIAEXECUTE = new PdfName("RichMediaExecute");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName RICHMEDIAINSTANCE = new PdfName("RichMediaInstance");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName RICHMEDIAPARAMS = new PdfName("RichMediaParams");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName RICHMEDIAPOSITION = new PdfName("RichMediaPosition");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName RICHMEDIAPRESENTATION = new PdfName("RichMediaPresentation");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName RICHMEDIASETTINGS = new PdfName("RichMediaSettings");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName RICHMEDIAWINDOW = new PdfName("RichMediaWindow");
    /**
     * A name
     */
    public static final PdfName ROLEMAP = new PdfName("RoleMap");
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
     *
     * @since 2.1.6
     */
    public static final PdfName RUBY = new PdfName("Ruby");
    /**
     * A name
     */
    public static final PdfName RUNLENGTHDECODE = new PdfName("RunLengthDecode");
    /**
     * (Optional) A rich text string; described in the PDF Specification Annex M
     */
    public static final PdfName RV = new PdfName("RV");
    /**
     * A name
     */
    public static final PdfName S = new PdfName("S");
    /**
     * A name
     */
    public static final PdfName SATURATION = new PdfName("Saturation");
    /**
     * A name
     */
    public static final PdfName SCHEMA = new PdfName("Schema");
    /**
     * A name
     */
    public static final PdfName SCREEN = new PdfName("Screen");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName SCRIPTS = new PdfName("Scripts");
    /**
     * A name
     */
    public static final PdfName SECT = new PdfName("Sect");
    /**
     * A name
     */
    public static final PdfName SEPARATION = new PdfName("Separation");
    /**
     * A name
     */
    public static final PdfName SETOCGSTATE = new PdfName("SetOCGState");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName SETTINGS = new PdfName("Settings");
    /**
     * A name
     */
    public static final PdfName SHADING = new PdfName("Shading");
    /**
     * A name
     */
    public static final PdfName SHADINGTYPE = new PdfName("ShadingType");
    /**
     * A name
     */
    public static final PdfName SHIFT_JIS = new PdfName("Shift-JIS");
    /**
     * A name
     */
    public static final PdfName SIG = new PdfName("Sig");

    /**
     * The type name of a signature field lock dictionary
     */
    public static final PdfName SIGFIELDLOCK = new PdfName("SigFieldLock");

    /**
     * A name
     */
    public static final PdfName SIGFLAGS = new PdfName("SigFlags");
    /**
     * A name
     */
    public static final PdfName SIGREF = new PdfName("SigRef");
    /**
     * A name
     */
    public static final PdfName SIMPLEX = new PdfName("Simplex");
    /**
     * A name
     */
    public static final PdfName SINGLEPAGE = new PdfName("SinglePage");
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
    public static final PdfName SORT = new PdfName("Sort");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName SOUND = new PdfName("Sound");
    /**
     * A name
     */
    public static final PdfName SPAN = new PdfName("Span");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName SPEED = new PdfName("Speed");
    /**
     * A name
     */
    public static final PdfName SPLIT = new PdfName("Split");
    /**
     * A name
     */
    public static final PdfName SQUARE = new PdfName("Square");
    /**
     * A name
     *
     * @since 2.1.3
     */
    public static final PdfName SQUIGGLY = new PdfName("Squiggly");
    /**
     * A name
     */
    public static final PdfName ST = new PdfName("St");
    /**
     * A name
     */
    public static final PdfName STAMP = new PdfName("Stamp");
    /**
     * A name
     */
    public static final PdfName STANDARD = new PdfName("Standard");
    /**
     * (Optional; PDF 1.5) The state to which the original annotation should be set. Default: "Unmarked" if StateModel
     * is "Marked"; "None" if StateModel is "Review". (Additional entries specific to a text annotation)
     */
    public static final PdfName STATE = new PdfName("State");
    /**
     * (Required if State is present, otherwise optional; PDF 1.5) The state model corresponding to State; see
     * "Annotation States" above. (Additional entries specific to a text annotation)
     */
    public static final PdfName STATEMODEL = new PdfName("StateModel");
    /**
     * A name
     */
    public static final PdfName STDCF = new PdfName("StdCF");
    /**
     * A name
     */
    public static final PdfName STEMV = new PdfName("StemV");
    /**
     * A name
     */
    public static final PdfName STMF = new PdfName("StmF");
    /**
     * A name
     */
    public static final PdfName STRF = new PdfName("StrF");
    /**
     * A name
     */
    public static final PdfName STRIKEOUT = new PdfName("StrikeOut");
    /**
     * (Required if the annotation is a structural content item) The integer key of the annotation's entry in the
     * structural parent tree.
     */
    public static final PdfName STRUCTPARENT = new PdfName("StructParent");
    /**
     * A name
     */
    public static final PdfName STRUCTPARENTS = new PdfName("StructParents");
    /**
     * A name
     */
    public static final PdfName STRUCTTREEROOT = new PdfName("StructTreeRoot");
    /**
     * A name
     */
    public static final PdfName STYLE = new PdfName("Style");
    /**
     * A name
     */
    public static final PdfName SUBFILTER = new PdfName("SubFilter");
    /**
     * (Optional; PDF 1.5) Text representing a short description of the subject being addressed by the annotation.
     * (Additional entry specific to markup annotations)
     */
    public static final PdfName SUBJ = new PdfName("Subj");
    /**
     * A name
     */
    public static final PdfName SUBJECT = new PdfName("Subject");
    /**
     * A name
     */
    public static final PdfName SUBMITFORM = new PdfName("SubmitForm");
    /**
     * (Required) The type of annotation that this dictionary describes.
     */
    public static final PdfName SUBTYPE = new PdfName("Subtype");
    /**
     * A name
     */
    public static final PdfName SUPPLEMENT = new PdfName("Supplement");
    /**
     * (Optional; shall be an indirect reference) A seed value dictionary containing information that constrains the
     * properties of a signature that is applied to this field.
     */
    public static final PdfName SV = new PdfName("SV");
    /**
     * A name
     */
    public static final PdfName SW = new PdfName("SW");
    /**
     * A name of a base 14 type 1 font
     */
    public static final PdfName SYMBOL = new PdfName("Symbol");
    /**
     * (Required; ExtensionLevel 3) Specifies which barcode or glyph technology is to be used on this annotation.
     * Supported values are PDF417, QRCode, and DataMatrix. (Entries in a PaperMetaData generation parameters
     * dictionary)
     */
    public static final PdfName SYMBOLOGY = new PdfName("Symbology");
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
    public static final PdfName TBODY = new PdfName("TBody");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName TD = new PdfName("TD");
    /**
     * A name
     */
    public static final PdfName TEXT = new PdfName("Text");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName TFOOT = new PdfName("TFoot");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName TH = new PdfName("TH");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName THEAD = new PdfName("THead");
    /**
     * A name
     */
    public static final PdfName THUMB = new PdfName("Thumb");
    /**
     * A name
     */
    public static final PdfName THREADS = new PdfName("Threads");
    /**
     * (Optional) For scrollable list boxes, the top index (the index in the Opt array of the first option visible in
     * the list). Default value: 0.
     */
    public static final PdfName TI = new PdfName("TI");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName TIME = new PdfName("Time");
    /**
     * A name
     */
    public static final PdfName TILINGTYPE = new PdfName("TilingType");
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
     * A name
     */
    public static final PdfName TK = new PdfName("TK");
    /**
     * Optional) The mapping name that shall be used when exporting interactive form field data from the document.
     */
    public static final PdfName TM = new PdfName("TM");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName TOC = new PdfName("TOC");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName TOCI = new PdfName("TOCI");
    /**
     * A name
     */
    public static final PdfName TOGGLE = new PdfName("Toggle");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName TOOLBAR = new PdfName("Toolbar");
    /**
     * A name
     */
    public static final PdfName TOUNICODE = new PdfName("ToUnicode");
    /**
     * A name
     */
    public static final PdfName TP = new PdfName("TP");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName TABLEROW = new PdfName("TR");
    /**
     * A name
     */
    public static final PdfName TRANS = new PdfName("Trans");
    /**
     * A name
     */
    public static final PdfName TRANSFORMPARAMS = new PdfName("TransformParams");
    /**
     * A name
     */
    public static final PdfName TRANSFORMMETHOD = new PdfName("TransformMethod");
    /**
     * A name
     */
    public static final PdfName TRANSPARENCY = new PdfName("Transparency");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName TRANSPARENT = new PdfName("Transparent");
    /**
     * A name
     */
    public static final PdfName TRAPPED = new PdfName("Trapped");
    /**
     * A name
     */
    public static final PdfName TRIMBOX = new PdfName("TrimBox");
    /**
     * A name
     */
    public static final PdfName TRUETYPE = new PdfName("TrueType");

    /**
     * Parts of the Build Data Dictionary If the value is true, the application was in trusted mode when signing took
     * place. The default value is false. A viewing application is in trusted mode when only reviewed code is executing,
     * where reviewed code is code that does not affect the rendering of PDF files in ways that are not covered by the
     * PDF Reference.
     */
    public static final PdfName TRUSTEDMODE = new PdfName("TrustedMode");

    /**
     * (Optional) An alternative field name that shall be used in place of the actual field name wherever the field
     * shall be identified in the user interface (such as in error or status messages referring to the field). This text
     * is also useful when extracting the document's contents in support of accessibility to users with disabilities or
     * for other purposes.
     */
    public static final PdfName TU = new PdfName("TU");
    /**
     * A name
     */
    public static final PdfName TWOCOLUMNLEFT = new PdfName("TwoColumnLeft");
    /**
     * A name
     */
    public static final PdfName TWOCOLUMNRIGHT = new PdfName("TwoColumnRight");
    /**
     * A name
     */
    public static final PdfName TWOPAGELEFT = new PdfName("TwoPageLeft");
    /**
     * A name
     */
    public static final PdfName TWOPAGERIGHT = new PdfName("TwoPageRight");
    /**
     * A name
     */
    public static final PdfName TX = new PdfName("Tx");
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
    public static final PdfName TYPE0 = new PdfName("Type0");
    /**
     * A name
     */
    public static final PdfName TYPE1 = new PdfName("Type1");
    /**
     * A name of an attribute.
     */
    public static final PdfName TYPE3 = new PdfName("Type3");
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
     * A name of an attribute.
     */
    public static final PdfName UF = new PdfName("UF");
    /**
     * A name of an attribute.
     */
    public static final PdfName UHC = new PdfName("UHC");
    /**
     * A name of an attribute.
     */
    public static final PdfName UNDERLINE = new PdfName("Underline");
    /**
     * A name
     */
    public static final PdfName UR = new PdfName("UR");
    /**
     * A name
     */
    public static final PdfName UR3 = new PdfName("UR3");
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
    public static final PdfName USAGE = new PdfName("Usage");
    /**
     * A name
     */
    public static final PdfName USEATTACHMENTS = new PdfName("UseAttachments");
    /**
     * A name
     */
    public static final PdfName USENONE = new PdfName("UseNone");
    /**
     * A name
     */
    public static final PdfName USEOC = new PdfName("UseOC");
    /**
     * A name
     */
    public static final PdfName USEOUTLINES = new PdfName("UseOutlines");
    /**
     * A name
     */
    public static final PdfName USER = new PdfName("User");
    /**
     * A name
     */
    public static final PdfName USERPROPERTIES = new PdfName("UserProperties");
    /**
     * A name
     */
    public static final PdfName USERUNIT = new PdfName("UserUnit");
    /**
     * A name
     */
    public static final PdfName USETHUMBS = new PdfName("UseThumbs");
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
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName VALIGN = new PdfName("VAlign");
    /**
     * A name
     */
    public static final PdfName VERISIGN_PPKVS = new PdfName("VeriSign.PPKVS");
    /**
     * A name
     */
    public static final PdfName VERSION = new PdfName("Version");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName VIDEO = new PdfName("Video");
    /**
     * A name
     */
    public static final PdfName VIEW = new PdfName("View");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName VIEWS = new PdfName("Views");
    /**
     * A name
     */
    public static final PdfName VIEWAREA = new PdfName("ViewArea");
    /**
     * A name
     */
    public static final PdfName VIEWCLIP = new PdfName("ViewClip");
    /**
     * A name
     */
    public static final PdfName VIEWERPREFERENCES = new PdfName("ViewerPreferences");
    /**
     * A name
     */
    public static final PdfName VIEWSTATE = new PdfName("ViewState");
    /**
     * A name
     */
    public static final PdfName VISIBLEPAGES = new PdfName("VisiblePages");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName VOFFSET = new PdfName("VOffset");
    /**
     * This dictionary contains Signature VRI dictionaries (see PDF 2.0 ch. 12.8.4.4,"Validation-related information
     * (VRI)"). The key of each entry in this dictionary is the base-16-encoded (uppercase) SHA1 digest of the signature
     * to which it applies a and the value is the Signature VRI dictionary which contains the validation-related
     * information for that signature.
     */
    public static final PdfName VRI = new PdfName("VRI");

    /**
     * A name of an attribute.
     */
    public static final PdfName W = new PdfName("W");
    /**
     * A name of an attribute.
     */
    public static final PdfName W2 = new PdfName("W2");
    /**
     * A name
     *
     * @since 2.1.6
     */
    public static final PdfName WARICHU = new PdfName("Warichu");
    /**
     * A name of an attribute.
     */
    public static final PdfName WC = new PdfName("WC");
    /**
     * A name of an attribute.
     */
    public static final PdfName WIDGET = new PdfName("Widget");
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
    public static final PdfName WIN = new PdfName("Win");
    /**
     * A name of an encoding
     */
    public static final PdfName WIN_ANSI_ENCODING = new PdfName("WinAnsiEncoding");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName WINDOW = new PdfName("Window");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName WINDOWED = new PdfName("Windowed");
    /**
     * A name of an encoding
     */
    public static final PdfName WIPE = new PdfName("Wipe");
    /**
     * A name
     */
    public static final PdfName WHITEPOINT = new PdfName("WhitePoint");
    /**
     * A name
     */
    public static final PdfName WP = new PdfName("WP");
    /**
     * A name of an encoding
     */
    public static final PdfName WS = new PdfName("WS");
    /**
     * A name
     */
    public static final PdfName X = new PdfName("X");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName XA = new PdfName("XA");
    /**
     * A name.
     *
     * @since 2.1.6
     */
    public static final PdfName XD = new PdfName("XD");
    /**
     * A name
     */
    public static final PdfName XFA = new PdfName("XFA");
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
    public static final PdfName XSTEP = new PdfName("XStep");
    /**
     * A name
     */
    public static final PdfName XREF = new PdfName("XRef");
    /**
     * A name
     */
    public static final PdfName XREFSTM = new PdfName("XRefStm");
    /**
     * A name
     */
    public static final PdfName XYZ = new PdfName("XYZ");
    /**
     * A name
     */
    public static final PdfName YSTEP = new PdfName("YStep");
    /**
     * A name
     */
    public static final PdfName ZADB = new PdfName("ZaDb");
    /**
     * A name of a base 14 type 1 font
     */
    public static final PdfName ZAPFDINGBATS = new PdfName("ZapfDingbats");
    /**
     * A name
     */
    public static final PdfName ZOOM = new PdfName("Zoom");

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

    /**
     * Constructs a PdfName.
     *
     * @param bytes the byte representation of the name
     */
    public PdfName(byte[] bytes) {
        super(PdfObject.NAME, bytes);
    }

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
