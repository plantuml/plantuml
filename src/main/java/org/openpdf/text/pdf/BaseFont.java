/*
 * $Id: BaseFont.java 4065 2009-09-16 23:09:11Z psoares33 $
 *
 * Copyright 2000-2006 by Paulo Soares.
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

import org.openpdf.text.DocumentException;
import org.openpdf.text.error_messages.MessageLocalization;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Base class for the several font types supported
 *
 * @author Paulo Soares (psoares@consiste.pt)
 */

public abstract class BaseFont {

    /**
     * This is a possible value of a base 14 type 1 font
     */
    public static final String COURIER = "Courier";

    /**
     * This is a possible value of a base 14 type 1 font
     */
    public static final String COURIER_BOLD = "Courier-Bold";

    /**
     * This is a possible value of a base 14 type 1 font
     */
    public static final String COURIER_OBLIQUE = "Courier-Oblique";

    /**
     * This is a possible value of a base 14 type 1 font
     */
    public static final String COURIER_BOLDOBLIQUE = "Courier-BoldOblique";

    /**
     * This is a possible value of a base 14 type 1 font
     */
    public static final String HELVETICA = "Helvetica";

    /**
     * This is a possible value of a base 14 type 1 font
     */
    public static final String HELVETICA_BOLD = "Helvetica-Bold";

    /**
     * This is a possible value of a base 14 type 1 font
     */
    public static final String HELVETICA_OBLIQUE = "Helvetica-Oblique";

    /**
     * This is a possible value of a base 14 type 1 font
     */
    public static final String HELVETICA_BOLDOBLIQUE = "Helvetica-BoldOblique";

    /**
     * This is a possible value of a base 14 type 1 font
     */
    public static final String SYMBOL = "Symbol";

    /**
     * This is a possible value of a base 14 type 1 font
     */
    public static final String TIMES_ROMAN = "Times-Roman";

    /**
     * This is a possible value of a base 14 type 1 font
     */
    public static final String TIMES_BOLD = "Times-Bold";

    /**
     * This is a possible value of a base 14 type 1 font
     */
    public static final String TIMES_ITALIC = "Times-Italic";

    /**
     * This is a possible value of a base 14 type 1 font
     */
    public static final String TIMES_BOLDITALIC = "Times-BoldItalic";

    /**
     * This is a possible value of a base 14 type 1 font
     */
    public static final String ZAPFDINGBATS = "ZapfDingbats";

    /**
     * The maximum height above the baseline reached by glyphs in this font, excluding the height of glyphs for accented
     * characters.
     */
    public static final int ASCENT = 1;
    /**
     * The y coordinate of the top of flat capital letters, measured from the baseline.
     */
    public static final int CAPHEIGHT = 2;
    /**
     * The maximum depth below the baseline reached by glyphs in this font. The value is a negative number.
     */
    public static final int DESCENT = 3;
    /**
     * The angle, expressed in degrees counterclockwise from the vertical, of the dominant vertical strokes of the font.
     * The value is negative for fonts that slope to the right, as almost all italic fonts do.
     */
    public static final int ITALICANGLE = 4;
    /**
     * The lower left x glyph coordinate.
     */
    public static final int BBOXLLX = 5;
    /**
     * The lower left y glyph coordinate.
     */
    public static final int BBOXLLY = 6;
    /**
     * The upper right x glyph coordinate.
     */
    public static final int BBOXURX = 7;
    /**
     * The upper right y glyph coordinate.
     */
    public static final int BBOXURY = 8;

    /**
     * java.awt.Font property
     */
    public static final int AWT_ASCENT = 9;
    /**
     * java.awt.Font property
     */
    public static final int AWT_DESCENT = 10;
    /**
     * java.awt.Font property
     */
    public static final int AWT_LEADING = 11;
    /**
     * java.awt.Font property
     */
    public static final int AWT_MAXADVANCE = 12;
    /**
     * The underline position. Usually a negative value.
     */
    public static final int UNDERLINE_POSITION = 13;
    /**
     * The underline thickness.
     */
    public static final int UNDERLINE_THICKNESS = 14;
    /**
     * The strikethrough position.
     */
    public static final int STRIKETHROUGH_POSITION = 15;
    /**
     * The strikethrough thickness.
     */
    public static final int STRIKETHROUGH_THICKNESS = 16;
    /**
     * The recommended vertical size for subscripts for this font.
     */
    public static final int SUBSCRIPT_SIZE = 17;
    /**
     * The recommended vertical offset from the baseline for subscripts for this font. Usually a negative value.
     */
    public static final int SUBSCRIPT_OFFSET = 18;
    /**
     * The recommended vertical size for superscripts for this font.
     */
    public static final int SUPERSCRIPT_SIZE = 19;
    /**
     * The recommended vertical offset from the baseline for superscripts for this font.
     */
    public static final int SUPERSCRIPT_OFFSET = 20;
    /**
     * The font is Type 1.
     */
    public static final int FONT_TYPE_T1 = 0;
    /**
     * The font is True Type with a standard encoding.
     */
    public static final int FONT_TYPE_TT = 1;
    /**
     * The font is CJK.
     */
    public static final int FONT_TYPE_CJK = 2;
    /**
     * The font is True Type with a Unicode encoding.
     */
    public static final int FONT_TYPE_TTUNI = 3;
    /**
     * A font already inside the document.
     */
    public static final int FONT_TYPE_DOCUMENT = 4;
    /**
     * A Type3 font.
     */
    public static final int FONT_TYPE_T3 = 5;
    /**
     * The Unicode encoding with horizontal writing.
     */
    public static final String IDENTITY_H = "Identity-H";
    /**
     * The Unicode encoding with vertical writing.
     */
    public static final String IDENTITY_V = "Identity-V";

    /**
     * A possible encoding.
     */
    public static final String CP1250 = "Cp1250";

    /**
     * A possible encoding.
     */
    public static final String CP1252 = "Cp1252";

    /**
     * A possible encoding.
     */
    public static final String CP1257 = "Cp1257";

    /**
     * A possible encoding.
     */
    public static final String WINANSI = "Cp1252";

    /**
     * A possible encoding.
     */
    public static final String MACROMAN = "MacRoman";

    public static final int[] CHAR_RANGE_LATIN = {0, 0x17f, 0x2000, 0x206f,
            0x20a0, 0x20cf, 0xfb00, 0xfb06};
    public static final int[] CHAR_RANGE_ARABIC = {0, 0x7f, 0x0600, 0x067f,
            0x20a0, 0x20cf, 0xfb50, 0xfbff, 0xfe70, 0xfeff};
    public static final int[] CHAR_RANGE_HEBREW = {0, 0x7f, 0x0590, 0x05ff,
            0x20a0, 0x20cf, 0xfb1d, 0xfb4f};
    public static final int[] CHAR_RANGE_CYRILLIC = {0, 0x7f, 0x0400, 0x052f,
            0x2000, 0x206f, 0x20a0, 0x20cf};

    /**
     * if the font has to be embedded
     */
    public static final boolean EMBEDDED = true;

    /**
     * if the font doesn't have to be embedded
     */
    public static final boolean NOT_EMBEDDED = false;
    /**
     * if the font has to be cached
     */
    public static final boolean CACHED = true;
    /**
     * if the font doesn't have to be cached
     */
    public static final boolean NOT_CACHED = false;

    /**
     * The path to the font resources.
     */
    public static final String RESOURCE_PATH = "org/openpdf/text/pdf/fonts/";
    /**
     * The fake CID code that represents a newline.
     */
    public static final char CID_NEWLINE = '\u7fff';
    /**
     * a not defined character in a custom PDF encoding
     */
    public static final String notdef = ".notdef";
    /**
     * list of the 14 built in fonts.
     */
    protected static final HashMap<String, PdfName> BuiltinFonts14 = new HashMap<>();
    /**
     * cache for the fonts already used.
     */
    protected static ConcurrentHashMap<String, BaseFont> fontCache = new ConcurrentHashMap<>(
            500, 0.85f, 64);

    static {
        BuiltinFonts14.put(COURIER, PdfName.COURIER);
        BuiltinFonts14.put(COURIER_BOLD, PdfName.COURIER_BOLD);
        BuiltinFonts14.put(COURIER_BOLDOBLIQUE, PdfName.COURIER_BOLDOBLIQUE);
        BuiltinFonts14.put(COURIER_OBLIQUE, PdfName.COURIER_OBLIQUE);
        BuiltinFonts14.put(HELVETICA, PdfName.HELVETICA);
        BuiltinFonts14.put(HELVETICA_BOLD, PdfName.HELVETICA_BOLD);
        BuiltinFonts14
                .put(HELVETICA_BOLDOBLIQUE, PdfName.HELVETICA_BOLDOBLIQUE);
        BuiltinFonts14.put(HELVETICA_OBLIQUE, PdfName.HELVETICA_OBLIQUE);
        BuiltinFonts14.put(SYMBOL, PdfName.SYMBOL);
        BuiltinFonts14.put(TIMES_ROMAN, PdfName.TIMES_ROMAN);
        BuiltinFonts14.put(TIMES_BOLD, PdfName.TIMES_BOLD);
        BuiltinFonts14.put(TIMES_BOLDITALIC, PdfName.TIMES_BOLDITALIC);
        BuiltinFonts14.put(TIMES_ITALIC, PdfName.TIMES_ITALIC);
        BuiltinFonts14.put(ZAPFDINGBATS, PdfName.ZAPFDINGBATS);
    }

    protected ArrayList<int[]> subsetRanges;
    /**
     * table of characters widths for this encoding
     */
    protected int[] widths = new int[256];
    /**
     * encoding names
     */
    protected String[] differences = new String[256];
    /**
     * same as differences but with the unicode codes
     */
    protected char[] unicodeDifferences = new char[256];
    protected int[][] charBBoxes = new int[256][];
    /**
     * encoding used with this font
     */
    protected String encoding;
    /**
     * true if the font is to be embedded in the PDF
     */
    protected boolean embedded;
    /**
     * The compression level for the font stream.
     *
     * @since 2.1.3
     */
    protected int compressionLevel = PdfStream.DEFAULT_COMPRESSION;
    /**
     * true if the font must use its built in encoding. In that case the
     * <CODE>encoding</CODE> is only used to map a char to the position inside
     * the font, not to the expected char name.
     */
    protected boolean fontSpecific = true;
    /**
     * Forces the output of the width array. Only matters for the 14 built-in fonts.
     */
    protected boolean forceWidthsOutput = false;

    /**
     * Converts <CODE>char</CODE> directly to <CODE>byte</CODE> by casting.
     */
    protected boolean directTextToByte = false;

    /**
     * Indicates if all the glyphs and widths for that particular encoding should be included in the document.
     */
    protected boolean subset = true;

    protected boolean fastWinansi = false;

    /**
     * Custom encodings use this map to key the Unicode character to the single byte code.
     */
    protected IntHashtable specialMap;

    /**
     * Used to build a randomized prefix for a subset name
     */
    protected SecureRandom secureRandom;

    /**
     * Indicates if a CIDSet stream should be included in the document.
     */
    protected boolean includeCidSet = true;
    /**
     * The font type.
     */
    int fontType;

    /**
     * Creates new BaseFont
     */
    protected BaseFont() {
    }

    /**
     * Creates a new font. This will always be the default Helvetica font (not embedded). This method is introduced
     * because Helvetica is used in many examples.
     *
     * @return a BaseFont object (Helvetica, Winansi, not embedded)
     * @throws IOException       This shouldn't occur ever
     * @throws DocumentException This shouldn't occur ever
     * @since 2.1.1
     */
    public static BaseFont createFont() throws DocumentException, IOException {
        return createFont(BaseFont.HELVETICA, BaseFont.WINANSI,
                BaseFont.NOT_EMBEDDED);
    }

    /**
     * Creates a new font. This font can be one of the 14 built in types, a Type1 font referred to by an AFM or PFM
     * file, a TrueType font (simple or collection) or a CJK font from the Adobe Asian Font Pack. TrueType fonts and CJK
     * fonts can have an optional style modifier appended to the name. These modifiers are: Bold, Italic and BoldItalic.
     * An example would be "STSong-Light,Bold". Note that this modifiers do not work if the font is embedded. Fonts in
     * TrueType collections are addressed by index such as "msgothic.ttc,1". This would get the second font (indexes
     * start at 0), in this case "MS PGothic".
     * <p>
     * The fonts are cached and if they already exist they are extracted from the cache, not parsed again.
     * <p>
     * Besides the common encodings described by name, custom encodings can also be made. These encodings will only work
     * for the single byte fonts Type1 and TrueType. The encoding string starts with a '#' followed by "simple" or
     * "full". If "simple" there is a decimal for the first character position and then a list of hex values
     * representing the Unicode codes that compose that encoding.<br> The "simple" encoding is recommended for TrueType
     * fonts as the "full" encoding risks not matching the character with the right glyph if not done with care.<br> The
     * "full" encoding is specially aimed at Type1 fonts where the glyphs have to be described by non standard names
     * like the Tex math fonts. Each group of three elements compose a code position: the one byte code order in decimal
     * or as 'x' (x cannot be the space), the name and the Unicode character used to access the glyph. The space must be
     * assigned to character position 32 otherwise text justification will not work.
     * <p>
     * Example for a "simple" encoding that includes the Unicode character space, A, B and ecyrillic:
     *
     * <PRE>
     * &quot;# simple 32 0020 0041 0042 0454&quot;
     * </PRE>
     * <p>
     * Example for a "full" encoding for a Type1 Tex font:
     *
     * <PRE>
     * &quot;# full 'A' nottriangeqlleft 0041 'B' dividemultiply 0042 32 space 0020&quot;
     * </PRE>
     * <p>
     * This method calls:<br>
     *
     * <PRE>
     * createFont(name, encoding, embedded, true, null, null);
     * </PRE>
     *
     * @param name     the name of the font or its location on file
     * @param encoding the encoding to be applied to this font
     * @param embedded true if the font is to be embedded in the PDF
     * @return returns a new font. This font may come from the cache
     * @throws DocumentException the font is invalid
     * @throws IOException       the font file could not be read
     */
    public static BaseFont createFont(String name, String encoding,
            boolean embedded) throws DocumentException, IOException {
        return createFont(name, encoding, embedded, true, null, null, false);
    }

    /**
     * Creates a new font. This font can be one of the 14 built in types, a Type1 font referred to by an AFM or PFM
     * file, a TrueType font (simple or collection) or a CJK font from the Adobe Asian Font Pack. TrueType fonts and CJK
     * fonts can have an optional style modifier appended to the name. These modifiers are: Bold, Italic and BoldItalic.
     * An example would be "STSong-Light,Bold". Note that this modifiers do not work if the font is embedded. Fonts in
     * TrueType collections are addressed by index such as "msgothic.ttc,1". This would get the second font (indexes
     * start at 0), in this case "MS PGothic".
     * <p>
     * The fonts are cached and if they already exist they are extracted from the cache, not parsed again.
     * <p>
     * Besides the common encodings described by name, custom encodings can also be made. These encodings will only work
     * for the single byte fonts Type1 and TrueType. The encoding string starts with a '#' followed by "simple" or
     * "full". If "simple" there is a decimal for the first character position and then a list of hex values
     * representing the Unicode codes that compose that encoding.<br> The "simple" encoding is recommended for TrueType
     * fonts as the "full" encoding risks not matching the character with the right glyph if not done with care.<br> The
     * "full" encoding is specially aimed at Type1 fonts where the glyphs have to be described by non standard names
     * like the Tex math fonts. Each group of three elements compose a code position: the one byte code order in decimal
     * or as 'x' (x cannot be the space), the name and the Unicode character used to access the glyph. The space must be
     * assigned to character position 32 otherwise text justification will not work.
     * <p>
     * Example for a "simple" encoding that includes the Unicode character space, A, B and ecyrillic:
     *
     * <PRE>
     * &quot;# simple 32 0020 0041 0042 0454&quot;
     * </PRE>
     * <p>
     * Example for a "full" encoding for a Type1 Tex font:
     *
     * <PRE>
     * &quot;# full 'A' nottriangeqlleft 0041 'B' dividemultiply 0042 32 space 0020&quot;
     * </PRE>
     * <p>
     * This method calls:<br>
     *
     * <PRE>
     * createFont(name, encoding, embedded, true, null, null);
     * </PRE>
     *
     * @param name      the name of the font or its location on file
     * @param encoding  the encoding to be applied to this font
     * @param embedded  true if the font is to be embedded in the PDF
     * @param forceRead in some cases (TrueTypeFont, Type1Font), the full font file will be read and kept in memory if
     *                  forceRead is true
     * @return returns a new font. This font may come from the cache
     * @throws DocumentException the font is invalid
     * @throws IOException       the font file could not be read
     * @since 2.1.5
     */
    public static BaseFont createFont(String name, String encoding,
            boolean embedded, boolean forceRead) throws DocumentException,
            IOException {
        return createFont(name, encoding, embedded, true, null, null, forceRead);
    }

    /**
     * Creates a new font. This font can be one of the 14 built in types, a Type1 font referred to by an AFM or PFM
     * file, a TrueType font (simple or collection) or a CJK font from the Adobe Asian Font Pack. TrueType fonts and CJK
     * fonts can have an optional style modifier appended to the name. These modifiers are: Bold, Italic and BoldItalic.
     * An example would be "STSong-Light,Bold". Note that this modifiers do not work if the font is embedded. Fonts in
     * TrueType collections are addressed by index such as "msgothic.ttc,1". This would get the second font (indexes
     * start at 0), in this case "MS PGothic".
     * <p>
     * The fonts may or may not be cached depending on the flag
     * <CODE>cached</CODE>. If the <CODE>byte</CODE> arrays are present the font
     * will be read from them instead of the name. A name is still required to identify the font type.
     * <p>
     * Besides the common encodings described by name, custom encodings can also be made. These encodings will only work
     * for the single byte fonts Type1 and TrueType. The encoding string starts with a '#' followed by "simple" or
     * "full". If "simple" there is a decimal for the first character position and then a list of hex values
     * representing the Unicode codes that compose that encoding.<br> The "simple" encoding is recommended for TrueType
     * fonts as the "full" encoding risks not matching the character with the right glyph if not done with care.<br> The
     * "full" encoding is specially aimed at Type1 fonts where the glyphs have to be described by non standard names
     * like the Tex math fonts. Each group of three elements compose a code position: the one byte code order in decimal
     * or as 'x' (x cannot be the space), the name and the Unicode character used to access the glyph. The space must be
     * assigned to character position 32 otherwise text justification will not work.
     * <p>
     * Example for a "simple" encoding that includes the Unicode character space, A, B and ecyrillic:
     *
     * <PRE>
     * &quot;# simple 32 0020 0041 0042 0454&quot;
     * </PRE>
     * <p>
     * Example for a "full" encoding for a Type1 Tex font:
     *
     * <PRE>
     * &quot;# full 'A' nottriangeqlleft 0041 'B' dividemultiply 0042 32 space 0020&quot;
     * </PRE>
     *
     * @param name     the name of the font or its location on file
     * @param encoding the encoding to be applied to this font
     * @param embedded true if the font is to be embedded in the PDF
     * @param cached   true if the font comes from the cache or is added to the cache if new, false if the font is
     *                 always created new
     * @param ttfAfm   the true type font or the afm in a byte array
     * @param pfb      the pfb in a byte array
     * @return returns a new font. This font may come from the cache but only if cached is true, otherwise it will
     * always be created new
     * @throws DocumentException the font is invalid
     * @throws IOException       the font file could not be read
     * @since iText 0.80
     */
    public static BaseFont createFont(String name, String encoding,
            boolean embedded, boolean cached, byte[] ttfAfm, byte[] pfb)
            throws DocumentException, IOException {
        return createFont(name, encoding, embedded, cached, ttfAfm, pfb, false);
    }

    /**
     * Creates a new font. This font can be one of the 14 built in types, a Type1 font referred to by an AFM or PFM
     * file, a TrueType font (simple or collection) or a CJK font from the Adobe Asian Font Pack. TrueType fonts and CJK
     * fonts can have an optional style modifier appended to the name. These modifiers are: Bold, Italic and BoldItalic.
     * An example would be "STSong-Light,Bold". Note that this modifiers do not work if the font is embedded. Fonts in
     * TrueType collections are addressed by index such as "msgothic.ttc,1". This would get the second font (indexes
     * start at 0), in this case "MS PGothic".
     * <p>
     * The fonts may or may not be cached depending on the flag
     * <CODE>cached</CODE>. If the <CODE>byte</CODE> arrays are present the font
     * will be read from them instead of the name. A name is still required to identify the font type.
     * <p>
     * Besides the common encodings described by name, custom encodings can also be made. These encodings will only work
     * for the single byte fonts Type1 and TrueType. The encoding string starts with a '#' followed by "simple" or
     * "full". If "simple" there is a decimal for the first character position and then a list of hex values
     * representing the Unicode codes that compose that encoding.<br> The "simple" encoding is recommended for TrueType
     * fonts as the "full" encoding risks not matching the character with the right glyph if not done with care.<br> The
     * "full" encoding is specially aimed at Type1 fonts where the glyphs have to be described by non standard names
     * like the Tex math fonts. Each group of three elements compose a code position: the one byte code order in decimal
     * or as 'x' (x cannot be the space), the name and the Unicode character used to access the glyph. The space must be
     * assigned to character position 32 otherwise text justification will not work.
     * <p>
     * Example for a "simple" encoding that includes the Unicode character space, A, B and ecyrillic:
     *
     * <PRE>
     * &quot;# simple 32 0020 0041 0042 0454&quot;
     * </PRE>
     * <p>
     * Example for a "full" encoding for a Type1 Tex font:
     *
     * <PRE>
     * &quot;# full 'A' nottriangeqlleft 0041 'B' dividemultiply 0042 32 space 0020&quot;
     * </PRE>
     *
     * @param name     the name of the font or its location on file
     * @param encoding the encoding to be applied to this font
     * @param embedded true if the font is to be embedded in the PDF
     * @param cached   true if the font comes from the cache or is added to the cache if new, false if the font is
     *                 always created new
     * @param ttfAfm   the true type font or the afm in a byte array
     * @param pfb      the pfb in a byte array
     * @param noThrow  if true will not throw an exception if the font is not recognized and will return null, if false
     *                 will throw an exception if the font is not recognized. Note that even if true an exception may be
     *                 thrown in some circumstances. This parameter is useful for FontFactory that may have to check
     *                 many invalid font names before finding the right one
     * @return returns a new font. This font may come from the cache but only if cached is true, otherwise it will
     * always be created new
     * @throws DocumentException the font is invalid
     * @throws IOException       the font file could not be read
     * @since 2.0.3
     */
    public static BaseFont createFont(String name, String encoding,
            boolean embedded, boolean cached, byte[] ttfAfm, byte[] pfb,
            boolean noThrow) throws DocumentException, IOException {
        return createFont(name, encoding, embedded, cached, ttfAfm, pfb, false,
                false);
    }

    /**
     * Gets the name without the modifiers Bold, Italic or BoldItalic.
     *
     * @param name the full name of the font
     * @return the name without the modifiers Bold, Italic or BoldItalic
     */
    protected static String getBaseName(String name) {
        if (name.endsWith(",Bold")) {
            return name.substring(0, name.length() - 5);
        } else if (name.endsWith(",Italic")) {
            return name.substring(0, name.length() - 7);
        } else if (name.endsWith(",BoldItalic")) {
            return name.substring(0, name.length() - 11);
        } else {
            return name;
        }
    }

    /**
     * Normalize the encoding names. "winansi" is changed to "Cp1252" and "macroman" is changed to "MacRoman".
     *
     * @param enc the encoding to be normalized
     * @return the normalized encoding
     */
    protected static String normalizeEncoding(String enc) {
        if (enc.equals("winansi") || enc.equals("")) {
            return CP1252;
        } else if (enc.equals("macroman")) {
            return MACROMAN;
        } else {
            return enc;
        }
    }

    private static volatile ResourceProvider resourceProvider;

    /**
     * Installs a source of resources that is tried before the class loaders (and the only
     * one in a native executable).
     *
     * @param provider the provider, or {@code null} to remove it
     */
    public static void setResourceProvider(ResourceProvider provider) {
        resourceProvider = provider;
    }

    /**
     * Gets the font resources.
     *
     * @param key the full name of the resource
     * @return the <CODE>InputStream</CODE> to get the resource or
     * <CODE>null</CODE> if not found
     */
    public static InputStream getResourceStream(String key) {
        return getResourceStream(key, null);
    }

    /**
     * Gets the font resources.
     *
     * @param key    the full name of the resource
     * @param loader the ClassLoader to load the resource or null to try the ones available
     * @return the <CODE>InputStream</CODE> to get the resource or
     * <CODE>null</CODE> if not found
     */
    public static InputStream getResourceStream(String key, ClassLoader loader) {
        if (key.startsWith("/")) {
            key = key.substring(1);
        }
        final ResourceProvider provider = resourceProvider;
        if (provider != null) {
            try {
                final InputStream fromProvider = provider.open(key);
                if (fromProvider != null) {
                    return new BufferedInputStream(fromProvider);
                }
            } catch (IOException e) {
                // fall through
            }
        }
        // Native executable (TeaVM C): no class loader, only the provider above.
        // The marker is tested here, so that TeaVM prunes the class loader code below.
        if (NativePlatform.isC()) {
            return null;
        }
        InputStream is = null;
        if (loader != null) {
            is = loader.getResourceAsStream(key);
            if (is != null) {
                return new BufferedInputStream(is);
            }
        }
        // Try to use Context Class Loader to load the properties file.
        try {
            ClassLoader contextClassLoader = Thread.currentThread()
                    .getContextClassLoader();
            if (contextClassLoader != null) {
                is = contextClassLoader.getResourceAsStream(key);
            }
        } catch (Throwable e) {
        }

        if (is == null) {
            is = BaseFont.class.getResourceAsStream("/" + key);
        }
        if (is == null) {
            is = ClassLoader.getSystemResourceAsStream(key);
        }
        return is == null ? null : new BufferedInputStream(is);
    }

    // ia>

    /**
     * Creates the <CODE>widths</CODE> and the <CODE>differences</CODE> arrays
     */
    protected void createEncoding() {
        if (encoding.startsWith("#")) {
            specialMap = new IntHashtable();
            StringTokenizer tok = new StringTokenizer(encoding.substring(1),
                    " ,\t\n\r\f");
            if (tok.nextToken().equals("full")) {
                while (tok.hasMoreTokens()) {
                    String order = tok.nextToken();
                    String name = tok.nextToken();
                    char uni = (char) Integer.parseInt(tok.nextToken(), 16);
                    int orderK;
                    if (order.startsWith("'")) {
                        orderK = order.charAt(1);
                    } else {
                        orderK = Integer.parseInt(order);
                    }
                    orderK %= 256;
                    specialMap.put(uni, orderK);
                    differences[orderK] = name;
                    unicodeDifferences[orderK] = uni;
                    widths[orderK] = getRawWidth(uni, name);
                    charBBoxes[orderK] = getRawCharBBox(uni, name);
                }
            } else {
                int k = 0;
                if (tok.hasMoreTokens()) {
                    k = Integer.parseInt(tok.nextToken());
                }
                while (tok.hasMoreTokens() && k < 256) {
                    String hex = tok.nextToken();
                    int uni = Integer.parseInt(hex, 16) % 0x10000;
                    String name = GlyphList.unicodeToName(uni);
                    if (name != null) {
                        specialMap.put(uni, k);
                        differences[k] = name;
                        unicodeDifferences[k] = (char) uni;
                        widths[k] = getRawWidth(uni, name);
                        charBBoxes[k] = getRawCharBBox(uni, name);
                        ++k;
                    }
                }
            }
            for (int k = 0; k < 256; ++k) {
                if (differences[k] == null) {
                    differences[k] = notdef;
                }
            }
        } else if (fontSpecific) {
            for (int k = 0; k < 256; ++k) {
                widths[k] = getRawWidth(k, null);
                charBBoxes[k] = getRawCharBBox(k, null);
            }
        } else {
            String s;
            String name;
            char c;
            byte[] b = new byte[1];
            for (int k = 0; k < 256; ++k) {
                b[0] = (byte) k;
                s = PdfEncodings.convertToString(b, encoding);
                if (s.length() > 0) {
                    c = s.charAt(0);
                } else {
                    c = '?';
                }
                name = GlyphList.unicodeToName(c);
                if (name == null) {
                    name = notdef;
                }
                differences[k] = name;
                unicodeDifferences[k] = c;
                widths[k] = getRawWidth(c, name);
                charBBoxes[k] = getRawCharBBox(c, name);
            }
        }
    }

    /**
     * Gets the width from the font according to the Unicode char <CODE>c</CODE> or the <CODE>name</CODE>. If the
     * <CODE>name</CODE> is null it's a symbolic font.
     *
     * @param c    the unicode char
     * @param name the glyph name
     * @return the width of the char
     */
    abstract int getRawWidth(int c, String name);

    /**
     * Gets the kerning between two Unicode chars.
     *
     * @param char1 the first char
     * @param char2 the second char
     * @return the kerning to be applied in normalized 1000 units
     */
    public abstract int getKerning(int char1, int char2);

    /**
     * Sets the kerning between two Unicode chars.
     *
     * @param char1 the first char
     * @param char2 the second char
     * @param kern  the kerning to apply in normalized 1000 units
     * @return <code>true</code> if the kerning was applied, <code>false</code>
     * otherwise
     */
    public abstract boolean setKerning(int char1, int char2, int kern);

    /**
     * Gets the width of a <CODE>char</CODE> in normalized 1000 units.
     *
     * @param char1 the unicode <CODE>char</CODE> to get the width of
     * @return the width in normalized 1000 units
     */
    public int getWidth(int char1) {
        if (fastWinansi) {
            if (char1 < 128 || char1 >= 160 && char1 <= 255) {
                return widths[char1];
            } else {
                return widths[PdfEncodings.winansi.get(char1)];
            }
        } else {
            int total = 0;
            byte[] mbytes = convertToBytes((char) char1);
            for (byte mbyte : mbytes) {
                total += widths[0xff & mbyte];
            }
            return total;
        }
    }

    /**
     * Gets the width of a <CODE>String</CODE> in normalized 1000 units.
     *
     * @param text the <CODE>String</CODE> to get the width of
     * @return the width in normalized 1000 units
     */
    public int getWidth(String text) {
        int total = 0;
        if (fastWinansi) {
            int len = text.length();
            for (int k = 0; k < len; ++k) {
                char char1 = text.charAt(k);
                if (char1 < 128 || char1 >= 160 && char1 <= 255) {
                    total += widths[char1];
                } else {
                    total += widths[PdfEncodings.winansi.get(char1)];
                }
            }
            return total;
        } else {
            byte[] mbytes = convertToBytes(text);
            for (byte mbyte : mbytes) {
                total += widths[0xff & mbyte];
            }
        }
        return total;
    }

    /**
     * Gets the descent of a <CODE>String</CODE> in normalized 1000 units. The descent will always be less than or equal
     * to zero even if all the characters have an higher descent.
     *
     * @param text the <CODE>String</CODE> to get the descent of
     * @return the descent in normalized 1000 units
     */
    public int getDescent(String text) {
        int min = 0;
        char[] chars = text.toCharArray();
        for (char c : chars) {
            int[] bbox = getCharBBox(c);
            if (bbox != null && bbox[1] < min) {
                min = bbox[1];
            }
        }
        return min;
    }

    /**
     * Gets the ascent of a <CODE>String</CODE> in normalized 1000 units. The ascent will always be greater than or
     * equal to zero even if all the characters have a lower ascent.
     *
     * @param text the <CODE>String</CODE> to get the ascent of
     * @return the ascent in normalized 1000 units
     */
    public int getAscent(String text) {
        int max = 0;
        char[] chars = text.toCharArray();
        for (char c : chars) {
            int[] bbox = getCharBBox(c);
            if (bbox != null && bbox[3] > max) {
                max = bbox[3];
            }
        }
        return max;
    }

    /**
     * Gets the descent of a <CODE>String</CODE> in points. The descent will always be less than or equal to zero even
     * if all the characters have an higher descent.
     *
     * @param text     the <CODE>String</CODE> to get the descent of
     * @param fontSize the size of the font
     * @return the descent in points
     */
    public float getDescentPoint(String text, float fontSize) {
        return getDescent(text) * 0.001f * fontSize;
    }

    /**
     * Gets the ascent of a <CODE>String</CODE> in points. The ascent will always be greater than or equal to zero even
     * if all the characters have a lower ascent.
     *
     * @param text     the <CODE>String</CODE> to get the ascent of
     * @param fontSize the size of the font
     * @return the ascent in points
     */
    public float getAscentPoint(String text, float fontSize) {
        return getAscent(text) * 0.001f * fontSize;
    }

    /**
     * Gets the width of a <CODE>String</CODE> in points taking kerning into account.
     *
     * @param text     the <CODE>String</CODE> to get the width of
     * @param fontSize the font size
     * @return the width in points
     */
    public float getWidthPointKerned(String text, float fontSize) {
        float size = getWidth(text) * 0.001f * fontSize;
        if (!hasKernPairs()) {
            return size;
        }
        int len = text.length() - 1;
        int kern = 0;
        char[] c = text.toCharArray();
        for (int k = 0; k < len; ++k) {
            kern += getKerning(c[k], c[k + 1]);
        }
        return size + kern * 0.001f * fontSize;
    }

    /**
     * Gets the width of a <CODE>String</CODE> in points.
     *
     * @param text     the <CODE>String</CODE> to get the width of
     * @param fontSize the font size
     * @return the width in points
     */
    public float getWidthPoint(String text, float fontSize) {
        return getWidth(text) * 0.001f * fontSize;
    }

    /**
     * Gets the width of a <CODE>char</CODE> in points.
     *
     * @param char1    the <CODE>char</CODE> to get the width of
     * @param fontSize the font size
     * @return the width in points
     */
    public float getWidthPoint(int char1, float fontSize) {
        return getWidth(char1) * 0.001f * fontSize;
    }

    /**
     * Converts a <CODE>String</CODE> to a <CODE>byte</CODE> array according to the font's encoding.
     *
     * @param text the <CODE>String</CODE> to be converted
     * @return an array of <CODE>byte</CODE> representing the conversion according to the font's encoding
     */
    byte[] convertToBytes(String text) {
        if (directTextToByte) {
            return PdfEncodings.convertToBytes(text, null);
        }
        if (specialMap != null) {
            byte[] b = new byte[text.length()];
            int ptr = 0;
            int length = text.length();
            for (int k = 0; k < length; ++k) {
                char c = text.charAt(k);
                if (specialMap.containsKey(c)) {
                    b[ptr++] = (byte) specialMap.get(c);
                }
            }
            if (ptr < length) {
                byte[] b2 = new byte[ptr];
                System.arraycopy(b, 0, b2, 0, ptr);
                return b2;
            } else {
                return b;
            }
        }
        return PdfEncodings.convertToBytes(text, encoding);
    }

    /**
     * Converts a <CODE>char</CODE> to a <CODE>byte</CODE> array according to the font's encoding.
     *
     * @param char1 the <CODE>char</CODE> to be converted
     * @return an array of <CODE>byte</CODE> representing the conversion according to the font's encoding
     */
    byte[] convertToBytes(int char1) {
        if (directTextToByte) {
            return PdfEncodings.convertToBytes((char) char1, null);
        }
        if (specialMap != null) {
            if (specialMap.containsKey(char1)) {
                return new byte[]{(byte) specialMap.get(char1)};
            } else {
                return new byte[0];
            }
        }
        return PdfEncodings.convertToBytes((char) char1, encoding);
    }

    /**
     * Outputs to the writer the font dictionaries and streams.
     *
     * @param writer the writer for this document
     * @param ref    the font indirect reference
     * @param params several parameters that depend on the font type
     * @throws IOException       on error
     * @throws DocumentException error in generating the object
     */
    abstract void writeFont(PdfWriter writer, PdfIndirectReference ref,
            Object[] params) throws DocumentException, IOException;

    /**
     * Returns a PdfStream object with the full font program (if possible). This method will return null for some types
     * of fonts (CJKFont, Type3Font) or if there is no font program available (standard Type 1 fonts).
     *
     * @return a PdfStream with the font program
     * @throws IOException       on error
     * @throws DocumentException on error
     * @since 2.1.3
     */
    abstract PdfStream getFullFontStream() throws IOException,
            DocumentException;

    /**
     * Gets the encoding used to convert <CODE>String</CODE> into
     * <CODE>byte[]</CODE>.
     *
     * @return the encoding name
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * Gets the font parameter identified by <CODE>key</CODE>. Valid values for
     * <CODE>key</CODE> are <CODE>ASCENT</CODE>, <CODE>AWT_ASCENT</CODE>,
     * <CODE>CAPHEIGHT</CODE>, <CODE>DESCENT</CODE>, <CODE>AWT_DESCENT</CODE>,
     * <CODE>ITALICANGLE</CODE>, <CODE>BBOXLLX</CODE>, <CODE>BBOXLLY</CODE>,
     * <CODE>BBOXURX</CODE> and <CODE>BBOXURY</CODE>.
     *
     * @param key      the parameter to be extracted
     * @param fontSize the font size in points
     * @return the parameter in points
     */
    public abstract float getFontDescriptor(int key, float fontSize);

    /**
     * Gets the font type. The font types can be: FONT_TYPE_T1, FONT_TYPE_TT, FONT_TYPE_CJK and FONT_TYPE_TTUNI.
     *
     * @return the font type
     */
    public int getFontType() {
        return fontType;
    }

    /**
     * Gets the embedded flag.
     *
     * @return <CODE>true</CODE> if the font is embedded.
     */
    public boolean isEmbedded() {
        return embedded;
    }

    /**
     * Gets the symbolic flag of the font.
     *
     * @return <CODE>true</CODE> if the font is symbolic
     */
    public boolean isFontSpecific() {
        return fontSpecific;
    }

    /**
     * Creates a unique subset prefix to be added to the font name when the font is embedded and subset.
     *
     * @return the subset prefix
     */
    protected String createSubsetPrefix() {
        String s = "";
        // Native (TeaVM C) build: no SecureRandom. The marker is tested here, so that TeaVM
        // prunes the SecureRandom code below.
        if (NativePlatform.isC()) {
            final java.util.Random random = new java.util.Random(System.nanoTime());
            for (int k = 0; k < 6; ++k) {
                s += (char) (random.nextDouble() * 26 + 'A');
            }
            return s + "+";
        }
        SecureRandom secureRandom = getSecureRandom();
        for (int k = 0; k < 6; ++k) {
            s += (char) (secureRandom.nextDouble() * 26 + 'A');
        }
        return s + "+";
    }

    /**
     * Returns a defined SecureRandom implementation. If nothing is set, returns a new
     *
     * @return {@link SecureRandom}
     */
    protected SecureRandom getSecureRandom() {
        if (secureRandom != null) {
            return secureRandom;
        }
        return new SecureRandom();
    }

    /**
     * Sets the SecureRandom instance to be used for a subset's prefix generation
     *
     * @param secureRandom {@link SecureRandom}
     */
    public void setSecureRandom(SecureRandom secureRandom) {
        this.secureRandom = secureRandom;
    }

    /**
     * Gets the Unicode character corresponding to the byte output to the pdf stream.
     *
     * @param index the byte index
     * @return the Unicode character
     */
    char getUnicodeDifferences(int index) {
        return unicodeDifferences[index];
    }

    /**
     * Gets the postscript font name.
     *
     * @return the postscript font name
     */
    public abstract String getPostscriptFontName();

    /**
     * Sets the font name that will appear in the pdf font dictionary. Use with care as it can easily make a font
     * unreadable if not embedded.
     *
     * @param name the new font name
     */
    public abstract void setPostscriptFontName(String name);

    /**
     * Gets the full name of the font. If it is a True Type font each array element will have {Platform ID, Platform
     * Encoding ID, Language ID, font name}. The interpretation of this values can be found in the Open Type
     * specification, chapter 2, in the 'name' table.<br> For the other fonts the array has a single element with {"",
     * "", "", font name}.
     *
     * @return the full name of the font
     */
    public abstract String[][] getFullFontName();

    /**
     * Gets all the entries of the names-table. If it is a True Type font each array element will have {Name ID,
     * Platform ID, Platform Encoding ID, Language ID, font name}. The interpretation of this values can be found in the
     * Open Type specification, chapter 2, in the 'name' table.<br> For the other fonts the array has a single element
     * with {"4", "", "", "", font name}.
     *
     * @return the full name of the font
     * @since 2.0.8
     */
    public abstract String[][] getAllNameEntries();

    /**
     * Gets the family name of the font. If it is a True Type font each array element will have {Platform ID, Platform
     * Encoding ID, Language ID, font name}. The interpretation of this values can be found in the Open Type
     * specification, chapter 2, in the 'name' table.<br> For the other fonts the array has a single element with {"",
     * "", "", font name}.
     *
     * @return the family name of the font
     */
    public abstract String[][] getFamilyFontName();

    /**
     * Gets the code pages supported by the font. This has only meaning with True Type fonts.
     *
     * @return the code pages supported by the font
     */
    public String[] getCodePagesSupported() {
        return new String[0];
    }

    /**
     * Gets the raw TrueType/OpenType table directory for fonts that expose one.
     *
     * @return the table directory keyed by table tag, or an empty map when unavailable
     */
    public Map<String, int[]> getTables() {
        return Map.of();
    }

    /**
     * Gets the font width array.
     *
     * @return the font width array
     */
    public int[] getWidths() {
        return widths;
    }

    /**
     * Gets the array with the names of the characters.
     *
     * @return the array with the names of the characters
     */
    public String[] getDifferences() {
        return differences;
    }

    /**
     * Gets the array with the unicode characters.
     *
     * @return the array with the unicode characters
     */
    public char[] getUnicodeDifferences() {
        return unicodeDifferences;
    }

    /**
     * Gets the state of the property.
     *
     * @return value of property forceWidthsOutput
     */
    public boolean isForceWidthsOutput() {
        return forceWidthsOutput;
    }

    /**
     * Set to <CODE>true</CODE> to force the generation of the widths array.
     *
     * @param forceWidthsOutput <CODE>true</CODE> to force the generation of the widths array
     */
    public void setForceWidthsOutput(boolean forceWidthsOutput) {
        this.forceWidthsOutput = forceWidthsOutput;
    }

    /**
     * Gets the direct conversion of <CODE>char</CODE> to <CODE>byte</CODE>.
     *
     * @return value of property directTextToByte.
     * @see #setDirectTextToByte(boolean directTextToByte)
     */
    public boolean isDirectTextToByte() {
        return directTextToByte;
    }

    /**
     * Sets the conversion of <CODE>char</CODE> directly to <CODE>byte</CODE> by casting. This is a low level feature to
     * put the bytes directly in the content stream without passing through String.getBytes().
     *
     * @param directTextToByte New value of property directTextToByte.
     */
    public void setDirectTextToByte(boolean directTextToByte) {
        this.directTextToByte = directTextToByte;
    }

    /**
     * Indicates if all the glyphs and widths for that particular encoding should be included in the document.
     *
     * @return <CODE>false</CODE> to include all the glyphs and widths.
     */
    public boolean isSubset() {
        return subset;
    }

    /**
     * Indicates if all the glyphs and widths for that particular encoding should be included in the document. When set
     * to <CODE>true</CODE> only the glyphs used will be included in the font. When set to
     * <CODE>false</CODE> and {@link #addSubsetRange(int[])} was not called the
     * full font will be included otherwise just the characters ranges will be included.
     *
     * @param subset new value of property subset
     */
    public void setSubset(boolean subset) {
        this.subset = subset;
    }

    /**
     * Gets the Unicode equivalent to a CID. The (nonexistent) CID <code>FF00</code> is translated as '\n'. It has only
     * meaning with CJK fonts with Identity encoding.
     *
     * @param c the CID code
     * @return the Unicode equivalent
     */
    public int getUnicodeEquivalent(int c) {
        return c;
    }

    /**
     * Gets the CID code given an Unicode. It has only meaning with CJK fonts.
     *
     * @param c the Unicode
     * @return the CID equivalent
     */
    public int getCidCode(int c) {
        return c;
    }

    /**
     * Checks if the font has any kerning pairs.
     *
     * @return <CODE>true</CODE> if the font has any kerning pairs
     */
    public abstract boolean hasKernPairs();

    /**
     * Checks if a character exists in this font.
     *
     * @param c the character to check
     * @return <CODE>true</CODE> if the character has a glyph,
     * <CODE>false</CODE> otherwise
     */
    public boolean charExists(int c) {
        byte[] b = convertToBytes(c);
        return b.length > 0;
    }

    /**
     * Sets the character advance.
     *
     * @param c       the character
     * @param advance the character advance normalized to 1000 units
     * @return <CODE>true</CODE> if the advance was set, <CODE>false</CODE>
     * otherwise
     */
    public boolean setCharAdvance(int c, int advance) {
        byte[] b = convertToBytes(c);
        if (b.length == 0) {
            return false;
        }
        widths[0xff & b[0]] = advance;
        return true;
    }

    /**
     * Gets the smallest box enclosing the character contours. It will return
     * <CODE>null</CODE> if the font has not the information or the character
     * has no contours, as in the case of the space, for example. Characters with no contours may also return
     * [0,0,0,0].
     *
     * @param c the character to get the contour bounding box from
     * @return an array of four floats with the bounding box in the format [llx,lly,urx,ury] or <code>null</code>
     */
    public int[] getCharBBox(int c) {
        byte[] b = convertToBytes(c);
        if (b.length == 0) {
            return null;
        } else {
            return charBBoxes[b[0] & 0xff];
        }
    }

    protected abstract int[] getRawCharBBox(int c, String name);

    /**
     * iText expects Arabic Diactrics (tashkeel) to have zero advance but some fonts, most notably those that come with
     * Windows, like times.ttf, have non-zero advance for those characters. This method makes those character to have
     * zero width advance and work correctly in the iText Arabic shaping and reordering context.
     */
    public void correctArabicAdvance() {
        for (char c = '\u064b'; c <= '\u0658'; ++c) {
            setCharAdvance(c, 0);
        }
        setCharAdvance('\u0670', 0);
        for (char c = '\u06d6'; c <= '\u06dc'; ++c) {
            setCharAdvance(c, 0);
        }
        for (char c = '\u06df'; c <= '\u06e4'; ++c) {
            setCharAdvance(c, 0);
        }
        for (char c = '\u06e7'; c <= '\u06e8'; ++c) {
            setCharAdvance(c, 0);
        }
        for (char c = '\u06ea'; c <= '\u06ed'; ++c) {
            setCharAdvance(c, 0);
        }
    }

    /**
     * Adds a character range when subsetting. The range is an <CODE>int</CODE> array where the first element is the
     * start range inclusive and the second element is the end range inclusive. Several ranges are allowed in the same
     * array.
     *
     * @param range the character range
     */
    public void addSubsetRange(int[] range) {
        if (subsetRanges == null) {
            subsetRanges = new ArrayList<>();
        }
        subsetRanges.add(range);
    }

    /**
     * Returns the compression level used for the font streams.
     *
     * @return the compression level (0 = best speed, 9 = best compression, -1 is default)
     * @since 2.1.3
     */
    public int getCompressionLevel() {
        return compressionLevel;
    }

    /**
     * Sets the compression level to be used for the font streams.
     *
     * @param compressionLevel a value between 0 (best speed) and 9 (best compression)
     * @since 2.1.3
     */
    public void setCompressionLevel(int compressionLevel) {
        if (compressionLevel < PdfStream.NO_COMPRESSION
                || compressionLevel > PdfStream.BEST_COMPRESSION) {
            this.compressionLevel = PdfStream.DEFAULT_COMPRESSION;
        } else {
            this.compressionLevel = compressionLevel;
        }
    }

    /**
     * Indicates if a CIDSet stream should be included in the document for {@link TrueTypeFontUnicode}.
     *
     * @return <CODE>true</CODE> to include a CIDSet stream.
     */
    public boolean isIncludeCidSet() {
        return includeCidSet;
    }

    /**
     * Indicates if a CIDSet stream should be included in the document for {@link TrueTypeFontUnicode}. When set to
     * <CODE>true</CODE>, a CIDSet stream will be included in the document. When set to <CODE>false</CODE>, and
     * {@link PdfWriter#getPDFXConformance()} does not require it, no CIDSet stream will be included.
     *
     * @param includeCidSet new value of {@link BaseFont#includeCidSet}
     */
    public void setIncludeCidSet(boolean includeCidSet) {
        this.includeCidSet = includeCidSet;
    }

    /**
     * Generates the PDF stream with the Type1 and Truetype fonts returning a PdfStream.
     */
    static class StreamFont extends PdfStream {

        /**
         * Generates the PDF stream with the Type1 and Truetype fonts returning a PdfStream.
         *
         * @param contents         the content of the stream
         * @param lengths          an array of int that describes the several lengths of each part of the font
         * @param compressionLevel the compression level of the Stream
         * @throws DocumentException error in the stream compression
         * @since 2.1.3 (replaces the constructor without param compressionLevel)
         */
        public StreamFont(byte[] contents, int[] lengths, int compressionLevel)
                throws DocumentException {
            try {
                bytes = contents;
                put(PdfName.LENGTH, new PdfNumber(bytes.length));
                for (int k = 0; k < lengths.length; ++k) {
                    put(new PdfName("Length" + (k + 1)), new PdfNumber(
                            lengths[k]));
                }
                flateCompress(compressionLevel);
            } catch (Exception e) {
                throw new DocumentException(e);
            }
        }

        /**
         * Generates the PDF stream for a font.
         *
         * @param contents         the content of a stream
         * @param subType          the subtype of the font.
         * @param compressionLevel the compression level of the Stream
         * @throws DocumentException error in the stream compression
         * @since 2.1.3 (replaces the constructor without param compressionLevel)
         */
        public StreamFont(byte[] contents, String subType, int compressionLevel)
                throws DocumentException {
            try {
                bytes = contents;
                put(PdfName.LENGTH, new PdfNumber(bytes.length));
                if (subType != null) {
                    put(PdfName.SUBTYPE, new PdfName(subType));
                }
                flateCompress(compressionLevel);
            } catch (Exception e) {
                throw new DocumentException(e);
            }
        }
    }

    /**
     * Creates a new font. In this pruned version of OpenPDF only the 14 built in types and the Type1 fonts referred to
     * by an AFM file are supported (no TrueType, no CJK).
     *
     * @param name      the name of the font or its location on file
     * @param encoding  the encoding to be applied to this font
     * @param embedded  true if the font is to be embedded in the PDF
     * @param cached    true if the font comes from the cache or is added to the cache if new, false if the font is
     *                  always created new
     * @param ttfAfm    the afm in a byte array
     * @param pfb       the pfb in a byte array
     * @param noThrow   if true will not throw an exception if the font is not recognized and will return null
     * @param forceRead if true, the full font file will be read and kept in memory
     * @return returns a new font
     * @throws DocumentException the font is invalid
     * @throws IOException       the font file could not be read
     */
    public static BaseFont createFont(String name, String encoding,
            boolean embedded, boolean cached, byte[] ttfAfm, byte[] pfb,
            boolean noThrow, boolean forceRead) throws DocumentException,
            IOException {
        encoding = normalizeEncoding(encoding);
        boolean isBuiltinFonts14 = BuiltinFonts14.containsKey(name);
        if (isBuiltinFonts14) {
            embedded = false;
        }
        BaseFont fontFound = null;
        BaseFont fontBuilt = null;
        String key = name + "\n" + encoding + "\n" + embedded;
        if (cached) {
            fontFound = fontCache.get(key);
            if (fontFound != null) {
                return fontFound;
            }
        }
        if (isBuiltinFonts14 || name.toLowerCase().endsWith(".afm")) {
            fontBuilt = new Type1Font(name, encoding, embedded, ttfAfm, pfb,
                    forceRead);
            fontBuilt.fastWinansi = encoding.equals(CP1252);
        } else if (noThrow) {
            return null;
        } else {
            throw new DocumentException(MessageLocalization.getComposedMessage(
                    "font.1.with.2.is.not.recognized", name, encoding));
        }
        if (cached) {
            fontCache.putIfAbsent(key, fontBuilt);
            return fontCache.get(key);
        }
        return fontBuilt;
    }
}
