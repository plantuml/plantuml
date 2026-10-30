/*
 * $Id: PdfAnnotation.java 4065 2009-09-16 23:09:11Z psoares33 $
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

import org.openpdf.text.Rectangle;
import org.openpdf.text.error_messages.MessageLocalization;
import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A <CODE>PdfAnnotation</CODE> is a note that is associated with a page.
 *
 * @see PdfDictionary
 */

public class PdfAnnotation extends PdfDictionary {

    /**
     * highlight attributename
     */
    public static final PdfName HIGHLIGHT_NONE = PdfName.N;
    /**
     * highlight attributename
     */
    public static final PdfName HIGHLIGHT_INVERT = PdfName.I;
    /**
     * highlight attributename
     */
    public static final PdfName HIGHLIGHT_OUTLINE = PdfName.O;
    /**
     * highlight attributename
     */
    public static final PdfName HIGHLIGHT_PUSH = PdfName.P;
    /**
     * highlight attributename
     */
    public static final PdfName HIGHLIGHT_TOGGLE = PdfName.T;
    /**
     * flagvalue
     */
    public static final int FLAGS_INVISIBLE = 1;
    /**
     * flagvalue
     */
    public static final int FLAGS_HIDDEN = 2;
    /**
     * flagvalue
     */
    public static final int FLAGS_PRINT = 4;
    /**
     * flagvalue
     */
    public static final int FLAGS_NOZOOM = 8;
    /**
     * flagvalue
     */
    public static final int FLAGS_NOROTATE = 16;
    /**
     * flagvalue
     */
    public static final int FLAGS_NOVIEW = 32;
    /**
     * flagvalue
     */
    public static final int FLAGS_READONLY = 64;
    /**
     * flagvalue
     */
    public static final int FLAGS_LOCKED = 128;
    /**
     * flagvalue
     */
    public static final int FLAGS_TOGGLENOVIEW = 256;
    /**
     * appearance attributename
     */
    public static final PdfName APPEARANCE_NORMAL = PdfName.N;
    /**
     * appearance attributename
     */
    public static final PdfName APPEARANCE_ROLLOVER = PdfName.R;
    /**
     * appearance attributename
     */
    public static final PdfName APPEARANCE_DOWN = PdfName.D;
    /**
     * attributevalue
     */
    public static final PdfName AA_ENTER = PdfName.E;
    /**
     * attributevalue
     */
    public static final PdfName AA_EXIT = PdfName.X;
    /**
     * attributevalue
     */
    public static final PdfName AA_DOWN = PdfName.D;
    /**
     * attributevalue
     */
    public static final PdfName AA_UP = PdfName.U;
    /**
     * attributevalue
     */
    public static final PdfName AA_FOCUS = PdfName.FO;
    /**
     * attributevalue
     */
    public static final PdfName AA_BLUR = PdfName.BL;
    /**
     * attributevalue
     */
    public static final PdfName AA_JS_KEY = PdfName.K;
    /**
     * attributevalue
     */
    public static final PdfName AA_JS_FORMAT = PdfName.F;
    /**
     * attributevalue
     */
    public static final PdfName AA_JS_CHANGE = PdfName.V;
    /**
     * attributevalue
     */
    public static final PdfName AA_JS_OTHER_CHANGE = PdfName.C;
    /**
     * attributevalue
     */
    public static final int MARKUP_HIGHLIGHT = 0;
    /**
     * attributevalue
     */
    public static final int MARKUP_UNDERLINE = 1;
    /**
     * attributevalue
     */
    public static final int MARKUP_STRIKEOUT = 2;
    /**
     * attributevalue
     *
     * @since 2.1.3
     */
    public static final int MARKUP_SQUIGGLY = 3;

    protected PdfWriter writer;
    /**
     * Reference to this annotation.
     *
     * @since 2.1.6; was removed in 2.1.5, but restored in 2.1.6
     */
    protected PdfIndirectReference reference;
    protected Map<PdfTemplate, Object> templates;
    protected boolean form = false;
    protected boolean annotation = true;

    /**
     * Holds value of property used.
     */
    protected boolean used = false;

    /**
     * Holds value of property placeInPage.
     */
    private int placeInPage = -1;

    // constructors
    public PdfAnnotation(PdfWriter writer, Rectangle rect) {
        this.writer = writer;
        if (rect != null) {
            put(PdfName.RECT, new PdfRectangle(rect));
        }
        addFKey();
    }

    /**
     * Constructs a new <CODE>PdfAnnotation</CODE> of subtype text.
     *
     * @param writer  pdf writer
     * @param llx     lower-left-x
     * @param lly     lower-left-y
     * @param urx     upper-right-x
     * @param ury     upper-right-y
     * @param title   title
     * @param content content
     */
    public PdfAnnotation(PdfWriter writer, float llx, float lly, float urx, float ury, PdfString title,
            PdfString content) {
        this.writer = writer;
        put(PdfName.SUBTYPE, PdfName.TEXT);
        put(PdfName.T, title);
        put(PdfName.RECT, new PdfRectangle(llx, lly, urx, ury));
        put(PdfName.CONTENTS, content);
        addFKey();
    }

    /**
     * Constructs a new <CODE>PdfAnnotation</CODE> of subtype link (Action).
     *
     * @param writer pdf writer
     * @param llx    lower-left-x
     * @param lly    lower-left-y
     * @param urx    upper-right-x
     * @param ury    upper-right-y
     * @param action {@link PdfAction}
     */

    public PdfAnnotation(PdfWriter writer, float llx, float lly, float urx, float ury, PdfAction action) {
        this.writer = writer;
        put(PdfName.SUBTYPE, PdfName.LINK);
        put(PdfName.RECT, new PdfRectangle(llx, lly, urx, ury));
        put(PdfName.A, action);
        put(PdfName.BORDER, new PdfBorderArray(0, 0, 0));
        put(PdfName.C, new PdfColor(0x00, 0x00, 0xFF));
        addFKey();
    }

    /**
     * @param writer   {@link PdfWriter}
     * @param rect     dimensions {@link Rectangle}
     * @param title    title for the link
     * @param contents content
     * @param open     open
     * @param icon     icon
     * @return a PdfAnnotation
     */
    public static PdfAnnotation createText(PdfWriter writer, Rectangle rect, String title, String contents,
            boolean open, String icon) {
        PdfAnnotation annot = new PdfAnnotation(writer, rect);
        annot.put(PdfName.SUBTYPE, PdfName.TEXT);
        if (title != null) {
            annot.put(PdfName.T, new PdfString(title, PdfObject.TEXT_UNICODE));
        }
        if (contents != null) {
            annot.put(PdfName.CONTENTS, new PdfString(contents, PdfObject.TEXT_UNICODE));
        }
        if (open) {
            annot.put(PdfName.OPEN, PdfBoolean.PDFTRUE);
        }
        if (icon != null) {
            annot.put(PdfName.NAME, new PdfName(icon));
        }
        return annot;
    }

    /**
     * Creates a link.
     *
     * @param writer    {@link PdfWriter}
     * @param rect      dimensions {@link Rectangle}
     * @param highlight the highlight property
     * @return A PdfAnnotation
     */
    protected static PdfAnnotation createLink(PdfWriter writer, Rectangle rect, PdfName highlight) {
        PdfAnnotation annot = new PdfAnnotation(writer, rect);
        annot.put(PdfName.SUBTYPE, PdfName.LINK);
        if (!highlight.equals(HIGHLIGHT_INVERT)) {
            annot.put(PdfName.H, highlight);
        }
        return annot;
    }

    /**
     * Creates an Annotation with an Action.
     *
     * @param writer    {@link PdfWriter}
     * @param rect      dimensions {@link Rectangle}
     * @param highlight title for the link
     * @param action    action for annotation
     * @return A PdfAnnotation
     */
    public static PdfAnnotation createLink(PdfWriter writer, Rectangle rect, PdfName highlight, PdfAction action) {
        PdfAnnotation annot = createLink(writer, rect, highlight);
        annot.putEx(PdfName.A, action);
        return annot;
    }

    /**
     * Creates an Annotation with an local destination.
     *
     * @param writer           {@link PdfWriter}
     * @param rect             dimensions {@link Rectangle}
     * @param highlight        title for the link
     * @param namedDestination destination for pdf annotation
     * @return A PdfAnnotation
     */
    public static PdfAnnotation createLink(PdfWriter writer, Rectangle rect, PdfName highlight,
            String namedDestination) {
        PdfAnnotation annot = createLink(writer, rect, highlight);
        annot.put(PdfName.DEST, new PdfString(namedDestination));
        return annot;
    }

    /**
     * Creates an Annotation with a PdfDestination.
     *
     * @param writer    {@link PdfWriter}
     * @param rect      dimensions {@link Rectangle}
     * @param highlight title for the link
     * @param page      page
     * @param dest      destination for pdf annotation
     * @return A PdfAnnotation
     */
    public static PdfAnnotation createLink(PdfWriter writer, Rectangle rect, PdfName highlight, int page,
            PdfDestination dest) {
        PdfAnnotation annot = createLink(writer, rect, highlight);
        PdfIndirectReference ref = writer.getPageReference(page);
        dest.addPage(ref);
        annot.put(PdfName.DEST, dest);
        return annot;
    }

    /**
     * Add some free text to the document.
     *
     * @param writer            pdf writer
     * @param rect              dimension
     * @param contents          content of free text
     * @param defaultAppearance default appearance
     * @return A PdfAnnotation
     */
    public static PdfAnnotation createFreeText(PdfWriter writer, Rectangle rect, String contents,
            PdfContentByte defaultAppearance) {
        PdfAnnotation annot = new PdfAnnotation(writer, rect);
        annot.put(PdfName.SUBTYPE, PdfName.FREETEXT);
        annot.put(PdfName.CONTENTS, new PdfString(contents, PdfObject.TEXT_UNICODE));
        annot.setDefaultAppearanceString(defaultAppearance);
        return annot;
    }

    /**
     * Adds a line to the document. Move over the line and a tooltip is shown.
     *
     * @param writer   {@link PdfWriter}
     * @param rect     dimensions {@link Rectangle}
     * @param contents content for the file
     * @param x1       x-coordinate
     * @param y1       y-coordinate
     * @param x2       x-coordinate
     * @param y2       y-coordinate
     * @return A PdfAnnotation
     */
    public static PdfAnnotation createLine(PdfWriter writer, Rectangle rect, String contents, float x1, float y1,
            float x2, float y2) {
        PdfAnnotation annot = new PdfAnnotation(writer, rect);
        annot.put(PdfName.SUBTYPE, PdfName.LINE);
        annot.put(PdfName.CONTENTS, new PdfString(contents, PdfObject.TEXT_UNICODE));
        PdfArray array = new PdfArray(new PdfNumber(x1));
        array.add(new PdfNumber(y1));
        array.add(new PdfNumber(x2));
        array.add(new PdfNumber(y2));
        annot.put(PdfName.L, array);
        return annot;
    }

    /**
     * Adds a circle or a square that shows a tooltip when you pass over it.
     *
     * @param writer   pdf writer
     * @param rect     dimension of the tooltip
     * @param contents The tooltip
     * @param square   true if you want a square, false if you want a circle
     * @return A PdfAnnotation
     */
    public static PdfAnnotation createSquareCircle(PdfWriter writer, Rectangle rect, String contents, boolean square) {
        PdfAnnotation annot = new PdfAnnotation(writer, rect);
        if (square) {
            annot.put(PdfName.SUBTYPE, PdfName.SQUARE);
        } else {
            annot.put(PdfName.SUBTYPE, PdfName.CIRCLE);
        }
        annot.put(PdfName.CONTENTS, new PdfString(contents, PdfObject.TEXT_UNICODE));
        return annot;
    }

    public static PdfAnnotation createMarkup(PdfWriter writer, Rectangle rect, String contents, int type,
            float[] quadPoints) {
        PdfAnnotation annot = new PdfAnnotation(writer, rect);
        PdfName name = PdfName.HIGHLIGHT;
        switch (type) {
            case MARKUP_UNDERLINE:
                name = PdfName.UNDERLINE;
                break;
            case MARKUP_STRIKEOUT:
                name = PdfName.STRIKEOUT;
                break;
            case MARKUP_SQUIGGLY:
                name = PdfName.SQUIGGLY;
                break;
        }
        annot.put(PdfName.SUBTYPE, name);
        annot.put(PdfName.CONTENTS, new PdfString(contents, PdfObject.TEXT_UNICODE));
        PdfArray array = new PdfArray();
        for (float quadPoint : quadPoints) {
            array.add(new PdfNumber(quadPoint));
        }
        annot.put(PdfName.QUADPOINTS, array);
        return annot;
    }

    /**
     * Adds a Stamp to your document. Move over the stamp and a tooltip is shown
     *
     * @param writer   {@link PdfWriter}
     * @param rect     dimensions {@link Rectangle}
     * @param contents content for the file
     * @param name     name of the annotation
     * @return A PdfAnnotation
     */
    public static PdfAnnotation createStamp(PdfWriter writer, Rectangle rect, String contents, String name) {
        PdfAnnotation annot = new PdfAnnotation(writer, rect);
        annot.put(PdfName.SUBTYPE, PdfName.STAMP);
        annot.put(PdfName.CONTENTS, new PdfString(contents, PdfObject.TEXT_UNICODE));
        annot.put(PdfName.NAME, new PdfName(name));
        return annot;
    }

    public static PdfAnnotation createInk(PdfWriter writer, Rectangle rect, String contents, float[][] inkList) {
        PdfAnnotation annot = new PdfAnnotation(writer, rect);
        annot.put(PdfName.SUBTYPE, PdfName.INK);
        annot.put(PdfName.CONTENTS, new PdfString(contents, PdfObject.TEXT_UNICODE));
        PdfArray outer = new PdfArray();
        for (float[] floats : inkList) {
            PdfArray inner = new PdfArray();
            float[] deep = floats;
            for (float v : deep) {
                inner.add(new PdfNumber(v));
            }
            outer.add(inner);
        }
        annot.put(PdfName.INKLIST, outer);
        return annot;
    }

    /**
     * Adds a popup to your document.
     *
     * @param writer   {@link PdfWriter}
     * @param rect     dimensions {@link Rectangle}
     * @param contents content for the file
     * @param open     if popup is open
     * @return A PdfAnnotation
     */
    public static PdfAnnotation createPopup(PdfWriter writer, Rectangle rect, String contents, boolean open) {
        PdfAnnotation annot = new PdfAnnotation(writer, rect);
        annot.put(PdfName.SUBTYPE, PdfName.POPUP);
        if (contents != null) {
            annot.put(PdfName.CONTENTS, new PdfString(contents, PdfObject.TEXT_UNICODE));
        }
        if (open) {
            annot.put(PdfName.OPEN, PdfBoolean.PDFTRUE);
        }
        return annot;
    }

    public static PdfArray getMKColor(Color color) {
        PdfArray array = new PdfArray();
        int type = ExtendedColor.getType(color);
        switch (type) {
            case ExtendedColor.TYPE_GRAY: {
                array.add(new PdfNumber(((GrayColor) color).getGray()));
                break;
            }
            case ExtendedColor.TYPE_CMYK: {
                CMYKColor cmyk = (CMYKColor) color;
                array.add(new PdfNumber(cmyk.getCyan()));
                array.add(new PdfNumber(cmyk.getMagenta()));
                array.add(new PdfNumber(cmyk.getYellow()));
                array.add(new PdfNumber(cmyk.getBlack()));
                break;
            }
            case ExtendedColor.TYPE_SEPARATION:
            case ExtendedColor.TYPE_PATTERN:
            case ExtendedColor.TYPE_SHADING:
                throw new RuntimeException(
                        MessageLocalization.getComposedMessage(
                                "separations.patterns.and.shadings.are.not.allowed.in.mk.dictionary"));
            default:
                array.add(new PdfNumber(color.getRed() / 255f));
                array.add(new PdfNumber(color.getGreen() / 255f));
                array.add(new PdfNumber(color.getBlue() / 255f));
        }
        return array;
    }

    /**
     * Returns an indirect reference to the annotation
     *
     * @return the indirect reference
     */
    public PdfIndirectReference getIndirectReference() {
        if (reference == null) {
            reference = writer.getPdfIndirectReference();
        }
        return reference;
    }

    public void setDefaultAppearanceString(PdfContentByte cb) {
        byte[] b = cb.getInternalBuffer().toByteArray();
        int len = b.length;
        for (int k = 0; k < len; ++k) {
            if (b[k] == '\n') {
                b[k] = 32;
            }
        }
        put(PdfName.DA, new PdfString(b));
    }

    public void setFlags(int flags) {
        if (flags == 0) {
            remove(PdfName.F);
        } else {
            put(PdfName.F, new PdfNumber(flags));
        }
    }

    /**
     * Adds one or more flag(s) to the existing annotation flags
     *
     * @param flags value to be added
     * @return int the new flag value
     */
    public int addFlags(int flags) {
        PdfNumber obj = (PdfNumber) get(PdfName.F);
        int old;
        if (obj == null) {
            old = 0;
        } else {
            old = obj.intValue();
        }
        int v = old | flags;
        put(PdfName.F, new PdfNumber(v));
        return old;
    }

    public void setBorder(PdfBorderArray border) {
        put(PdfName.BORDER, border);
    }

    public void setBorderStyle(PdfBorderDictionary border) {
        put(PdfName.BS, border);
    }

    /**
     * Sets the annotation's highlighting mode. The values can be
     * <CODE>HIGHLIGHT_NONE</CODE>, <CODE>HIGHLIGHT_INVERT</CODE>,
     * <CODE>HIGHLIGHT_OUTLINE</CODE> and <CODE>HIGHLIGHT_PUSH</CODE>;
     *
     * @param highlight the annotation's highlighting mode
     */
    public void setHighlighting(PdfName highlight) {
        if (highlight.equals(HIGHLIGHT_INVERT)) {
            remove(PdfName.H);
        } else {
            put(PdfName.H, highlight);
        }
    }

    public void setAppearance(PdfName ap, PdfTemplate template) {
        PdfDictionary dic = (PdfDictionary) get(PdfName.AP);
        if (dic == null) {
            dic = new PdfDictionary();
        }
        dic.put(ap, template.getIndirectReference());
        put(PdfName.AP, dic);
        if (!form) {
            return;
        }
        if (templates == null) {
            templates = new HashMap<>();
        }
        templates.put(template, null);
    }

    public void setAppearance(PdfName ap, String state, PdfTemplate template) {
        PdfDictionary dicAp = (PdfDictionary) get(PdfName.AP);
        if (dicAp == null) {
            dicAp = new PdfDictionary();
        }

        PdfDictionary dic;
        PdfObject obj = dicAp.get(ap);
        if (obj != null && obj.isDictionary()) {
            dic = (PdfDictionary) obj;
        } else {
            dic = new PdfDictionary();
        }
        dic.put(new PdfName(state), template.getIndirectReference());
        dicAp.put(ap, dic);
        put(PdfName.AP, dicAp);
        if (!form) {
            return;
        }
        if (templates == null) {
            templates = new HashMap<>();
        }
        templates.put(template, null);
    }

    public void setAppearanceState(String state) {
        if (state == null) {
            remove(PdfName.AS);
            return;
        }
        put(PdfName.AS, new PdfName(state));
    }

    public void setColor(Color color) {
        put(PdfName.C, new PdfColor(color));
    }

    public void setTitle(String title) {
        if (title == null) {
            remove(PdfName.T);
            return;
        }
        put(PdfName.T, new PdfString(title, PdfObject.TEXT_UNICODE));
    }

    public void setPopup(PdfAnnotation popup) {
        put(PdfName.POPUP, popup.getIndirectReference());
        popup.put(PdfName.PARENT, getIndirectReference());
    }

    public void setAction(PdfAction action) {
        put(PdfName.A, action);
    }

    public void setAdditionalActions(PdfName key, PdfAction action) {
        PdfDictionary dic;
        PdfObject obj = get(PdfName.AA);
        if (obj != null && obj.isDictionary()) {
            dic = (PdfDictionary) obj;
        } else {
            dic = new PdfDictionary();
        }
        dic.put(key, action);
        put(PdfName.AA, dic);
    }

    /**
     * Getter for property used.
     *
     * @return Value of property used.
     */
    public boolean isUsed() {
        return used;
    }

    /**
     * Setter for property used.
     */
    public void setUsed() {
        used = true;
    }

    public HashMap<PdfTemplate, Object> getTemplates() {
        return (HashMap<PdfTemplate, Object>) templates;
    }

    /**
     * Getter for property form.
     *
     * @return Value of property form.
     */
    public boolean isForm() {
        return form;
    }

    /**
     * Getter for property annotation.
     *
     * @return Value of property annotation.
     */
    public boolean isAnnotation() {
        return annotation;
    }

    public void setPage(int page) {
        put(PdfName.P, writer.getPageReference(page));
    }

    public void setPage() {
        put(PdfName.P, writer.getCurrentPage());
    }

    /**
     * Getter for property placeInPage.
     *
     * @return Value of property placeInPage.
     */
    public int getPlaceInPage() {
        return placeInPage;
    }

    /**
     * Places the annotation in a specified page that must be greater or equal to the current one. With
     * <code>PdfStamper</code> the page can be any. The first page is 1.
     *
     * @param placeInPage New value of property placeInPage.
     */
    public void setPlaceInPage(int placeInPage) {
        this.placeInPage = placeInPage;
    }

    public void setRotate(int v) {
        put(PdfName.ROTATE, new PdfNumber(v));
    }

    PdfDictionary getMK() {
        PdfDictionary mk = (PdfDictionary) get(PdfName.MK);
        if (mk == null) {
            mk = new PdfDictionary();
            put(PdfName.MK, mk);
        }
        return mk;
    }

    public void setMKRotation(int rotation) {
        getMK().put(PdfName.R, new PdfNumber(rotation));
    }

    public void setMKBorderColor(Color color) {
        if (color == null) {
            getMK().remove(PdfName.BC);
        } else {
            getMK().put(PdfName.BC, getMKColor(color));
        }
    }

    public void setMKBackgroundColor(Color color) {
        if (color == null) {
            getMK().remove(PdfName.BG);
        } else {
            getMK().put(PdfName.BG, getMKColor(color));
        }
    }

    public void setMKNormalCaption(String caption) {
        getMK().put(PdfName.CA, new PdfString(caption, PdfObject.TEXT_UNICODE));
    }

    public void setMKRolloverCaption(String caption) {
        getMK().put(PdfName.RC, new PdfString(caption, PdfObject.TEXT_UNICODE));
    }

    public void setMKAlternateCaption(String caption) {
        getMK().put(PdfName.AC, new PdfString(caption, PdfObject.TEXT_UNICODE));
    }

    public void setMKNormalIcon(PdfTemplate template) {
        getMK().put(PdfName.I, template.getIndirectReference());
    }

    public void setMKRolloverIcon(PdfTemplate template) {
        getMK().put(PdfName.RI, template.getIndirectReference());
    }

    public void setMKAlternateIcon(PdfTemplate template) {
        getMK().put(PdfName.IX, template.getIndirectReference());
    }

    public void setMKIconFit(PdfName scale, PdfName scalingType, float leftoverLeft, float leftoverBottom,
            boolean fitInBounds) {
        PdfDictionary dic = new PdfDictionary();
        if (!scale.equals(PdfName.A)) {
            dic.put(PdfName.SW, scale);
        }
        if (!scalingType.equals(PdfName.P)) {
            dic.put(PdfName.S, scalingType);
        }
        if (leftoverLeft != 0.5f || leftoverBottom != 0.5f) {
            PdfArray array = new PdfArray(new PdfNumber(leftoverLeft));
            array.add(new PdfNumber(leftoverBottom));
            dic.put(PdfName.A, array);
        }
        if (fitInBounds) {
            dic.put(PdfName.FB, PdfBoolean.PDFTRUE);
        }
        getMK().put(PdfName.IF, dic);
    }

    public void setMKTextPosition(int tp) {
        getMK().put(PdfName.TP, new PdfNumber(tp));
    }

    /**
     * Sets the name of the annotation. With this name the annotation can be identified among all the annotations on a
     * page (it has to be unique).
     *
     * @param name name to be set for annotation
     */
    public void setName(String name) {
        put(PdfName.NM, new PdfString(name));
    }

    private void addFKey() {
        if (writer.isPdfA1()) {
            put(PdfName.F, new PdfNumber(FLAGS_PRINT));
        }
    }
}
