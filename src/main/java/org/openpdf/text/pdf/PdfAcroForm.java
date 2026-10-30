/*
 * $Id: PdfAcroForm.java 3912 2009-04-26 08:38:15Z blowagie $
 *
 * Copyright 2002 Bruno Lowagie
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

import org.openpdf.text.ExceptionConverter;
import org.openpdf.text.Rectangle;
import java.util.HashMap;
import java.util.Map;

/**
 * Each PDF document can contain maximum 1 AcroForm.
 */

public class PdfAcroForm extends PdfDictionary {

    private PdfWriter writer;


    /**
     * This is a map containing FieldTemplates.
     */
    private Map<PdfTemplate, Object> fieldTemplates = new HashMap<>();

    /**
     * This is an array containing DocumentFields.
     */
    private PdfArray documentFields = new PdfArray();

    /**
     * This is an array containing the calculationorder of the fields.
     */
    private PdfArray calculationOrder = new PdfArray();

    /**
     * Contains the signature flags.
     */
    private int sigFlags = 0;

    /**
     * Creates new PdfAcroForm
     *
     * @param writer {@link PdfWriter}
     */
    public PdfAcroForm(PdfWriter writer) {
        this.writer = writer;
    }

    public void setNeedAppearances(boolean value) {
        put(PdfName.NEEDAPPEARANCES, new PdfBoolean(value));
    }


    /**
     * Adds fieldTemplates.
     *
     * @param ft field templates
     */

    public void addFieldTemplates(Map<PdfTemplate, Object> ft) {
        fieldTemplates.putAll(ft);
    }

    /**
     * Adds documentFields.
     *
     * @param ref an object of {@link PdfIndirectReference}
     */

    public void addDocumentField(PdfIndirectReference ref) {
        documentFields.add(ref);
    }

    /**
     * Adds an object to the calculationOrder.
     *
     * @param formField an object of {@link PdfFormField}
     */

    public void addCalculationOrder(PdfFormField formField) {
        calculationOrder.add(formField.getIndirectReference());
    }

    /**
     * Sets the signature flags.
     *
     * @param f signature flag
     */

    public void setSigFlags(int f) {
        sigFlags |= f;
    }

    /**
     * Adds a formfield to the AcroForm.
     *
     * @param formField an object of {@link PdfFormField}
     */

    public void addFormField(PdfFormField formField) {
        writer.addAnnotation(formField);
    }

    /**
     * @param button          an object of {@link PdfFormField}
     * @param characteristics characteristics
     * @param name            name of the field
     * @param value           value of the field
     */
    public void setButtonParams(PdfFormField button, int characteristics, String name, String value) {
        button.setButton(characteristics);
        button.setFlags(PdfAnnotation.FLAGS_PRINT);
        button.setPage();
        button.setFieldName(name);
        if (value != null) {
            button.setValueAsString(value);
        }
    }

    /**
     * @param name  name of the field
     * @param value value of the field
     * @return a PdfFormField
     */
    public PdfFormField addHiddenField(String name, String value) {
        PdfFormField hidden = PdfFormField.createEmpty(writer);
        hidden.setFieldName(name);
        hidden.setValueAsName(value);
        addFormField(hidden);
        return hidden;
    }

    /**
     * @param field an object of {@link PdfFormField}
     * @param text  text for the field
     * @param name  name of the field
     * @param llx   lower-left-x
     * @param lly   lower-left-y
     * @param urx   upper-right-x
     * @param ury   upper-right-y
     */
    public void setTextFieldParams(PdfFormField field, String text, String name, float llx, float lly, float urx,
            float ury) {
        field.setWidget(new Rectangle(llx, lly, urx, ury), PdfAnnotation.HIGHLIGHT_INVERT);
        field.setValueAsString(text);
        field.setDefaultValueAsString(text);
        field.setFieldName(name);
        field.setFlags(PdfAnnotation.FLAGS_PRINT);
        field.setPage();
    }

    /**
     * @param name   field name
     * @param status status
     * @param field  field, an object of {@link PdfFormField}
     * @param value  value of the field
     * @param llx    lower-left-x
     * @param lly    lower-left-y
     * @param urx    upper-right-x
     * @param ury    upper-right-y
     */
    public void setCheckBoxParams(PdfFormField field, String name, String value, boolean status, float llx, float lly,
            float urx, float ury) {
        field.setWidget(new Rectangle(llx, lly, urx, ury), PdfAnnotation.HIGHLIGHT_TOGGLE);
        field.setFieldName(name);
        if (status) {
            field.setValueAsName(value);
            field.setAppearanceState(value);
        } else {
            field.setValueAsName("Off");
            field.setAppearanceState("Off");
        }
        field.setFlags(PdfAnnotation.FLAGS_PRINT);
        field.setPage();
        field.setBorderStyle(new PdfBorderDictionary(1, PdfBorderDictionary.STYLE_SOLID));
    }

    /**
     * @param name          name of the field
     * @param defaultValue  default value
     * @param noToggleToOff noToggleToOff
     * @return a PdfFormField
     */
    public PdfFormField getRadioGroup(String name, String defaultValue, boolean noToggleToOff) {
        PdfFormField radio = PdfFormField.createRadioButton(writer, noToggleToOff);
        radio.setFieldName(name);
        radio.setValueAsName(defaultValue);
        return radio;
    }

    /**
     * @param radiogroup field
     */
    public void addRadioGroup(PdfFormField radiogroup) {
        addFormField(radiogroup);
    }

    /**
     * @param field        field, an object of {@link PdfFormField}
     * @param name         field name
     * @param defaultValue default calue
     * @param llx          lower-left-x
     * @param lly          lower-left-y
     * @param urx          upper-right-x
     * @param ury          upper-right-y
     */
    public void setChoiceParams(PdfFormField field, String name, String defaultValue, float llx, float lly, float urx,
            float ury) {
        field.setWidget(new Rectangle(llx, lly, urx, ury), PdfAnnotation.HIGHLIGHT_INVERT);
        if (defaultValue != null) {
            field.setValueAsString(defaultValue);
            field.setDefaultValueAsString(defaultValue);
        }
        field.setFieldName(name);
        field.setFlags(PdfAnnotation.FLAGS_PRINT);
        field.setPage();
        field.setBorderStyle(new PdfBorderDictionary(2, PdfBorderDictionary.STYLE_SOLID));
    }

    /**
     * @param name  field name
     * @param field field, an object of {@link PdfFormField}
     * @param llx   lower-left-x
     * @param lly   lower-left-y
     * @param urx   upper-right-x
     * @param ury   upper-right-y
     */
    public void setSignatureParams(PdfFormField field, String name,
            float llx, float lly, float urx, float ury) {
        field.setWidget(new Rectangle(llx, lly, urx, ury), PdfAnnotation.HIGHLIGHT_INVERT);
        field.setFieldName(name);
        field.setFlags(PdfAnnotation.FLAGS_PRINT);
        field.setPage();
        field.setMKBorderColor(java.awt.Color.black);
        field.setMKBackgroundColor(java.awt.Color.white);
    }
}
