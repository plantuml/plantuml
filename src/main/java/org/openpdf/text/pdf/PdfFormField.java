/*
 * Copyright 2002 by Paulo Soares.
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
import java.util.ArrayList;
import java.util.List;

/**
 * Implements form fields.
 *
 * @author Paulo Soares (psoares@consiste.pt)
 */
public class PdfFormField extends PdfAnnotation {

    public static final int FF_READ_ONLY = 1;
    public static final int FF_REQUIRED = 2;
    public static final int FF_NO_EXPORT = 4;
    public static final int FF_NO_TOGGLE_TO_OFF = 16384;
    public static final int FF_RADIO = 32768;
    public static final int FF_PUSHBUTTON = 65536;
    public static final int FF_MULTILINE = 4096;
    public static final int FF_PASSWORD = 8192;
    public static final int FF_COMBO = 131072;
    public static final int FF_EDIT = 262144;
    public static final int FF_FILESELECT = 1048576;
    public static final int FF_MULTISELECT = 2097152;
    public static final int FF_DONOTSPELLCHECK = 4194304;
    public static final int FF_DONOTSCROLL = 8388608;
    public static final int FF_COMB = 16777216;
    public static final int FF_RADIOSINUNISON = 1 << 25;
    public static final int Q_LEFT = 0;
    public static final int Q_CENTER = 1;
    public static final int Q_RIGHT = 2;
    public static final int MK_NO_ICON = 0;
    public static final int MK_NO_CAPTION = 1;
    public static final int MK_CAPTION_BELOW = 2;
    public static final int MK_CAPTION_ABOVE = 3;
    public static final int MK_CAPTION_RIGHT = 4;
    public static final int MK_CAPTION_LEFT = 5;
    public static final int MK_CAPTION_OVERLAID = 6;
    public static final PdfName IF_SCALE_ALWAYS = PdfName.A;
    public static final PdfName IF_SCALE_BIGGER = PdfName.B;
    public static final PdfName IF_SCALE_SMALLER = PdfName.S;
    public static final PdfName IF_SCALE_NEVER = PdfName.N;
    public static final PdfName IF_SCALE_ANAMORPHIC = PdfName.A;
    public static final PdfName IF_SCALE_PROPORTIONAL = PdfName.P;
    public static final boolean MULTILINE = true;
    public static final boolean SINGLELINE = false;
    public static final boolean PLAINTEXT = false;
    public static final boolean PASSWORD = true;
    static PdfName[] mergeTarget = {PdfName.FONT, PdfName.XOBJECT, PdfName.COLORSPACE, PdfName.PATTERN};

    /**
     * Holds value of property parent.
     */
    protected PdfFormField parent;

    protected List<PdfFormField> kids;

    /**
     * Constructs a new <CODE>PdfAnnotation</CODE> of subtype link (Action).
     *
     * @param writer the PdfWriter
     * @param llx    lower-left x-coordinated
     * @param lly    lower-left y-coordinate
     * @param urx    upper-right x-coordinate
     * @param ury    upper-right y-coordinate
     * @param action the PdfAction
     */
    public PdfFormField(PdfWriter writer, float llx, float lly, float urx, float ury, PdfAction action) {
        super(writer, llx, lly, urx, ury, action);
        put(PdfName.TYPE, PdfName.ANNOT);
        put(PdfName.SUBTYPE, PdfName.WIDGET);
        annotation = true;
    }

    /**
     * Creates new PdfFormField
     *
     * @param writer the PdfWriter to associated to the PdfFormField
     */
    protected PdfFormField(PdfWriter writer) {
        super(writer, null);
        form = true;
        annotation = false;
    }

    public static PdfFormField createEmpty(PdfWriter writer) {
        PdfFormField field = new PdfFormField(writer);
        return field;
    }

    protected static PdfFormField createButton(PdfWriter writer, int flags) {
        PdfFormField field = new PdfFormField(writer);
        field.setButton(flags);
        return field;
    }

    public static PdfFormField createPushButton(PdfWriter writer) {
        return createButton(writer, FF_PUSHBUTTON);
    }

    public static PdfFormField createCheckBox(PdfWriter writer) {
        return createButton(writer, 0);
    }

    public static PdfFormField createRadioButton(PdfWriter writer, boolean noToggleToOff) {
        return createButton(writer, FF_RADIO + (noToggleToOff ? FF_NO_TOGGLE_TO_OFF : 0));
    }

    public static PdfFormField createTextField(PdfWriter writer, boolean multiline, boolean password, int maxLen) {
        PdfFormField field = new PdfFormField(writer);
        field.put(PdfName.FT, PdfName.TX);
        int flags = (multiline ? FF_MULTILINE : 0);
        flags += (password ? FF_PASSWORD : 0);
        field.put(PdfName.FF, new PdfNumber(flags));
        if (maxLen > 0) {
            field.put(PdfName.MAXLEN, new PdfNumber(maxLen));
        }
        return field;
    }

    /**
     * Allows creation of a simple textfield.
     *
     * @param writer pdf writer object
     * @return PdfFormField the created textfield
     */
    public static PdfFormField createTextField(PdfWriter writer) {
        PdfFormField field = new PdfFormField(writer);
        field.put(PdfName.FT, PdfName.TX);
        return field;
    }

    protected static PdfFormField createChoice(PdfWriter writer, int flags, PdfArray options, int topIndex) {
        PdfFormField field = new PdfFormField(writer);
        field.put(PdfName.FT, PdfName.CH);
        field.put(PdfName.FF, new PdfNumber(flags));
        field.put(PdfName.OPT, options);
        if (topIndex > 0) {
            field.put(PdfName.TI, new PdfNumber(topIndex));
        }
        return field;
    }

    public static PdfFormField createList(PdfWriter writer, String[] options, int topIndex) {
        return createChoice(writer, 0, processOptions(options), topIndex);
    }

    public static PdfFormField createList(PdfWriter writer, String[][] options, int topIndex) {
        return createChoice(writer, 0, processOptions(options), topIndex);
    }

    public static PdfFormField createCombo(PdfWriter writer, boolean edit, String[] options, int topIndex) {
        return createChoice(writer, FF_COMBO + (edit ? FF_EDIT : 0), processOptions(options), topIndex);
    }

    public static PdfFormField createCombo(PdfWriter writer, boolean edit, String[][] options, int topIndex) {
        return createChoice(writer, FF_COMBO + (edit ? FF_EDIT : 0), processOptions(options), topIndex);
    }

    protected static PdfArray processOptions(String[] options) {
        PdfArray array = new PdfArray();
        for (String option : options) {
            array.add(new PdfString(option, PdfObject.TEXT_UNICODE));
        }
        return array;
    }

    protected static PdfArray processOptions(String[][] options) {
        PdfArray array = new PdfArray();
        for (String[] subOption : options) {
            PdfArray ar2 = new PdfArray(new PdfString(subOption[0], PdfObject.TEXT_UNICODE));
            ar2.add(new PdfString(subOption[1], PdfObject.TEXT_UNICODE));
            array.add(ar2);
        }
        return array;
    }

    public static PdfFormField createSignature(PdfWriter writer) {
        PdfFormField field = new PdfFormField(writer);
        field.put(PdfName.FT, PdfName.SIG);
        return field;
    }

    public static PdfAnnotation shallowDuplicate(PdfAnnotation annot) {
        PdfAnnotation dup;
        if (annot.isForm()) {
            dup = new PdfFormField(annot.writer);
            PdfFormField dupField = (PdfFormField) dup;
            PdfFormField srcField = (PdfFormField) annot;
            dupField.parent = srcField.parent;
            dupField.kids = srcField.kids;
        } else {
            dup = new PdfAnnotation(annot.writer, null);
        }
        dup.merge(annot);
        dup.form = annot.form;
        dup.annotation = annot.annotation;
        dup.templates = annot.templates;
        return dup;
    }

    public void setWidget(Rectangle rect, PdfName highlight) {
        put(PdfName.TYPE, PdfName.ANNOT);
        put(PdfName.SUBTYPE, PdfName.WIDGET);
        put(PdfName.RECT, new PdfRectangle(rect));
        annotation = true;
        if (highlight != null && !highlight.equals(HIGHLIGHT_INVERT)) {
            put(PdfName.H, highlight);
        }
    }

    public void setButton(int flags) {
        put(PdfName.FT, PdfName.BTN);
        if (flags != 0) {
            put(PdfName.FF, new PdfNumber(flags));
        }
    }

    /**
     * Getter for property parent.
     *
     * @return Value of property parent.
     */
    public PdfFormField getParent() {
        return parent;
    }

    public void addKid(PdfFormField field) {
        field.parent = this;
        if (kids == null) {
            kids = new ArrayList<>();
        }
        kids.add(field);
    }

    public List<PdfFormField> getKidFields() {
        return kids;
    }

    public int setFieldFlags(int flags) {
        PdfNumber obj = (PdfNumber) get(PdfName.FF);
        int old;
        if (obj == null) {
            old = 0;
        } else {
            old = obj.intValue();
        }
        int v = old | flags;
        put(PdfName.FF, new PdfNumber(v));
        return old;
    }

    public void setValueAsString(String s) {
        put(PdfName.V, new PdfString(s, PdfObject.TEXT_UNICODE));
    }

    public void setValueAsName(String s) {
        put(PdfName.V, new PdfName(s));
    }

    public void setDefaultValueAsString(String s) {
        put(PdfName.DV, new PdfString(s, PdfObject.TEXT_UNICODE));
    }

    public void setDefaultValueAsName(String s) {
        put(PdfName.DV, new PdfName(s));
    }

    public void setFieldName(String s) {
        if (s != null) {
            put(PdfName.T, new PdfString(s, PdfObject.TEXT_UNICODE));
        }
    }

    public void setUserName(String s) {
        put(PdfName.TU, new PdfString(s, PdfObject.TEXT_UNICODE));
    }

    public void setMappingName(String s) {
        put(PdfName.TM, new PdfString(s, PdfObject.TEXT_UNICODE));
    }

    public void setQuadding(int v) {
        put(PdfName.Q, new PdfNumber(v));
    }
}
