/*
 * Copyright 2005 by Paulo Soares.
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
import java.util.HashMap;
import java.util.Map;

/**
 * A class to support Type3 fonts.
 */
public class Type3Font extends BaseFont {

    private boolean[] usedSlot;
    private IntHashtable widths3 = new IntHashtable();
    private Map<Integer, Type3Glyph> char2glyph = new HashMap<>();
    private PdfWriter writer;
    private float llx = Float.NaN, lly, urx, ury;
    private PageResources pageResources = new PageResources();
    private boolean colorized;

    /**
     * Creates a Type3 font.
     *
     * @param writer    the writer
     * @param chars     an array of chars corresponding to the glyphs used (not used, present for compatibility only)
     * @param colorized if <CODE>true</CODE> the font may specify color, if <CODE>false</CODE> no color commands are
     *                  allowed and only images as masks can be used
     */
    public Type3Font(PdfWriter writer, char[] chars, boolean colorized) {
        this(writer, colorized);
    }

    /**
     * Creates a Type3 font. This implementation assumes that the /FontMatrix is [0.001 0 0 0.001 0 0] or a 1000-unit
     * glyph coordinate system. An example:
     * <pre>
     * Document document = new Document(PageSize.A4);
     * PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("type3.pdf"));
     * document.open();
     * Type3Font t3 = new Type3Font(writer, false);
     * PdfContentByte g = t3.defineGlyph('a', 1000, 0, 0, 750, 750);
     * g.rectangle(0, 0, 750, 750);
     * g.fill();
     * g = t3.defineGlyph('b', 1000, 0, 0, 750, 750);
     * g.moveTo(0, 0);
     * g.lineTo(375, 750);
     * g.lineTo(750, 0);
     * g.fill();
     * Font f = new Font(t3, 12);
     * document.add(new Paragraph("ababab", f));
     * document.close();
     * </pre>
     *
     * @param writer    the writer
     * @param colorized if <CODE>true</CODE> the font may specify color, if <CODE>false</CODE> no color commands are
     *                  allowed and only images as masks can be used
     */
    public Type3Font(PdfWriter writer, boolean colorized) {
        this.writer = writer;
        this.colorized = colorized;
        fontType = FONT_TYPE_T3;
        usedSlot = new boolean[256];
    }

    /**
     * Defines a glyph. If the character was already defined it will return the same content
     *
     * @param c   the character to match this glyph.
     * @param wx  the advance this character will have
     * @param llx the X lower left corner of the glyph bounding box. If the <CODE>colorize</CODE> option is
     *            <CODE>true</CODE> the value is ignored
     * @param lly the Y lower left corner of the glyph bounding box. If the <CODE>colorize</CODE> option is
     *            <CODE>true</CODE> the value is ignored
     * @param urx the X upper right corner of the glyph bounding box. If the <CODE>colorize</CODE> option is
     *            <CODE>true</CODE> the value is ignored
     * @param ury the Y upper right corner of the glyph bounding box. If the <CODE>colorize</CODE> option is
     *            <CODE>true</CODE> the value is ignored
     * @return a content where the glyph can be defined
     */
    public PdfContentByte defineGlyph(char c, float wx, float llx, float lly, float urx, float ury) {
        if (c == 0 || c > 255) {
            throw new IllegalArgumentException(
                    MessageLocalization.getComposedMessage("the.char.1.doesn.t.belong.in.this.type3.font", (int) c));
        }
        usedSlot[c] = true;
        Integer ck = (int) c;
        Type3Glyph glyph = char2glyph.get(ck);
        if (glyph != null) {
            return glyph;
        }
        widths3.put(c, (int) wx);
        if (!colorized) {
            if (Float.isNaN(this.llx)) {
                this.llx = llx;
                this.lly = lly;
                this.urx = urx;
                this.ury = ury;
            } else {
                this.llx = Math.min(this.llx, llx);
                this.lly = Math.min(this.lly, lly);
                this.urx = Math.max(this.urx, urx);
                this.ury = Math.max(this.ury, ury);
            }
        }
        glyph = new Type3Glyph(writer, pageResources, wx, llx, lly, urx, ury, colorized);
        char2glyph.put(ck, glyph);
        return glyph;
    }

    public String[][] getFamilyFontName() {
        return getFullFontName();
    }

    public float getFontDescriptor(int key, float fontSize) {
        return 0;
    }

    public String[][] getFullFontName() {
        return new String[][]{{"", "", "", ""}};
    }

    /**
     * @since 2.0.8
     */
    public String[][] getAllNameEntries() {
        return new String[][]{{"4", "", "", "", ""}};
    }

    public int getKerning(int char1, int char2) {
        return 0;
    }

    public String getPostscriptFontName() {
        return "";
    }

    public void setPostscriptFontName(String name) {
    }

    protected int[] getRawCharBBox(int c, String name) {
        return null;
    }

    int getRawWidth(int c, String name) {
        return 0;
    }

    public boolean hasKernPairs() {
        return false;
    }

    public boolean setKerning(int char1, int char2, int kern) {
        return false;
    }

    void writeFont(PdfWriter writer, PdfIndirectReference ref, Object[] params)
            throws org.openpdf.text.DocumentException, java.io.IOException {
        if (this.writer != writer) {
            throw new IllegalArgumentException(
                    MessageLocalization.getComposedMessage("type3.font.used.with.the.wrong.pdfwriter"));
        }

        // Get first & lastchar ...
        int firstChar = 0;
        while (firstChar < usedSlot.length && !usedSlot[firstChar]) {
            firstChar++;
        }

        if (firstChar == usedSlot.length) {
            throw new DocumentException(MessageLocalization.getComposedMessage("no.glyphs.defined.for.type3.font"));
        }
        int lastChar = usedSlot.length - 1;
        while (lastChar >= firstChar && !usedSlot[lastChar]) {
            lastChar--;
        }

        int[] widths = new int[lastChar - firstChar + 1];
        int[] invOrd = new int[lastChar - firstChar + 1];

        int invOrdIndx = 0, w = 0;
        for (int u = firstChar; u <= lastChar; u++, w++) {
            if (usedSlot[u]) {
                invOrd[invOrdIndx++] = u;
                widths[w] = widths3.get(u);
            }
        }
        PdfArray diffs = new PdfArray();
        PdfDictionary charprocs = new PdfDictionary();
        int last = -1;
        for (int k = 0; k < invOrdIndx; ++k) {
            int c = invOrd[k];
            if (c > last) {
                last = c;
                diffs.add(new PdfNumber(last));
            }
            ++last;
            int c2 = invOrd[k];
            String s = GlyphList.unicodeToName(c2);
            if (s == null) {
                s = "a" + c2;
            }
            PdfName n = new PdfName(s);
            diffs.add(n);
            Type3Glyph glyph = char2glyph.get(c2);
            PdfStream stream = new PdfStream(glyph.toPdf(null));
            stream.flateCompress(compressionLevel);
            PdfIndirectReference refp = writer.addToBody(stream).getIndirectReference();
            charprocs.put(n, refp);
        }
        PdfDictionary font = new PdfDictionary(PdfName.FONT);
        font.put(PdfName.SUBTYPE, PdfName.TYPE3);
        if (colorized) {
            font.put(PdfName.FONTBBOX, new PdfRectangle(0, 0, 0, 0));
        } else {
            font.put(PdfName.FONTBBOX, new PdfRectangle(llx, lly, urx, ury));
        }
        font.put(PdfName.FONTMATRIX, new PdfArray(new float[]{0.001f, 0, 0, 0.001f, 0, 0}));
        font.put(PdfName.CHARPROCS, writer.addToBody(charprocs).getIndirectReference());
        PdfDictionary encoding = new PdfDictionary();
        encoding.put(PdfName.DIFFERENCES, diffs);
        font.put(PdfName.ENCODING, writer.addToBody(encoding).getIndirectReference());
        font.put(PdfName.FIRSTCHAR, new PdfNumber(firstChar));
        font.put(PdfName.LASTCHAR, new PdfNumber(lastChar));
        font.put(PdfName.WIDTHS, writer.addToBody(new PdfArray(widths)).getIndirectReference());
        if (pageResources.hasResources()) {
            font.put(PdfName.RESOURCES, writer.addToBody(pageResources.getResources()).getIndirectReference());
        }
        writer.addToBody(font, ref);
    }

    /**
     * Always returns null, because you can't get the FontStream of a Type3 font.
     *
     * @return null
     * @since 2.1.3
     */
    public PdfStream getFullFontStream() {
        return null;
    }


    byte[] convertToBytes(String text) {
        char[] cc = text.toCharArray();
        byte[] b = new byte[cc.length];
        int p = 0;
        for (char c : cc) {
            if (charExists(c)) {
                b[p++] = (byte) c;
            }
        }
        if (b.length == p) {
            return b;
        }
        byte[] b2 = new byte[p];
        System.arraycopy(b, 0, b2, 0, p);
        return b2;
    }

    byte[] convertToBytes(int char1) {
        if (charExists(char1)) {
            return new byte[]{(byte) char1};
        } else {
            return new byte[0];
        }
    }

    public int getWidth(int char1) {
        if (!widths3.containsKey(char1)) {
            throw new IllegalArgumentException(
                    MessageLocalization.getComposedMessage("the.char.1.is.not.defined.in.a.type3.font", char1));
        }
        return widths3.get(char1);
    }

    public int getWidth(String text) {
        char[] c = text.toCharArray();
        int total = 0;
        for (char c1 : c) {
            total += getWidth(c1);
        }
        return total;
    }

    public int[] getCharBBox(int c) {
        return null;
    }

    public boolean charExists(int c) {
        if (c > 0 && c < 256) {
            return usedSlot[c];
        } else {
            return false;
        }
    }

    public boolean setCharAdvance(int c, int advance) {
        return false;
    }

}
