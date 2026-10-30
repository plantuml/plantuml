/*
 * Copyright 2002 Paulo Soares
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
import java.io.IOException;


/**
 * Implements PDF functions.
 *
 * @author Paulo Soares (psoares@consiste.pt)
 */
public class PdfFunction {

    protected PdfWriter writer;

    protected PdfIndirectReference reference;

    protected PdfDictionary dictionary;

    /**
     * Creates new PdfFunction
     *
     * @param writer the PdfWriter to associate to the PdfFunction
     */
    protected PdfFunction(PdfWriter writer) {
        this.writer = writer;
    }

    public static PdfFunction type0(PdfWriter writer, float[] domain, float[] range, int[] size,
            int bitsPerSample, int order, float[] encode, float[] decode, byte[] stream) {
        PdfFunction func = new PdfFunction(writer);
        func.dictionary = new PdfStream(stream);
        ((PdfStream) func.dictionary).flateCompress(writer.getCompressionLevel());
        func.dictionary.put(PdfName.FUNCTIONTYPE, new PdfNumber(0));
        func.dictionary.put(PdfName.DOMAIN, new PdfArray(domain));
        func.dictionary.put(PdfName.RANGE, new PdfArray(range));
        func.dictionary.put(PdfName.SIZE, new PdfArray(size));
        func.dictionary.put(PdfName.BITSPERSAMPLE, new PdfNumber(bitsPerSample));
        if (order != 1) {
            func.dictionary.put(PdfName.ORDER, new PdfNumber(order));
        }
        if (encode != null) {
            func.dictionary.put(PdfName.ENCODE, new PdfArray(encode));
        }
        if (decode != null) {
            func.dictionary.put(PdfName.DECODE, new PdfArray(decode));
        }
        return func;
    }

    public static PdfFunction type2(PdfWriter writer, float[] domain, float[] range, float[] c0, float[] c1, float n) {
        PdfFunction func = new PdfFunction(writer);
        func.dictionary = new PdfDictionary();
        func.dictionary.put(PdfName.FUNCTIONTYPE, new PdfNumber(2));
        func.dictionary.put(PdfName.DOMAIN, new PdfArray(domain));
        if (range != null) {
            func.dictionary.put(PdfName.RANGE, new PdfArray(range));
        }
        if (c0 != null) {
            func.dictionary.put(PdfName.C0, new PdfArray(c0));
        }
        if (c1 != null) {
            func.dictionary.put(PdfName.C1, new PdfArray(c1));
        }
        func.dictionary.put(PdfName.N, new PdfNumber(n));
        return func;
    }

    public static PdfFunction type3(PdfWriter writer, float[] domain, float[] range, PdfFunction[] functions,
            float[] bounds, float[] encode) {
        PdfFunction func = new PdfFunction(writer);
        func.dictionary = new PdfDictionary();
        func.dictionary.put(PdfName.FUNCTIONTYPE, new PdfNumber(3));
        func.dictionary.put(PdfName.DOMAIN, new PdfArray(domain));
        if (range != null) {
            func.dictionary.put(PdfName.RANGE, new PdfArray(range));
        }
        PdfArray array = new PdfArray();
        for (PdfFunction function : functions) {
            array.add(function.getReference());
        }
        func.dictionary.put(PdfName.FUNCTIONS, array);
        func.dictionary.put(PdfName.BOUNDS, new PdfArray(bounds));
        func.dictionary.put(PdfName.ENCODE, new PdfArray(encode));
        return func;
    }

    public static PdfFunction type4(PdfWriter writer, float[] domain, float[] range, String postscript) {
        byte[] b = new byte[postscript.length()];
        for (int k = 0; k < b.length; ++k) {
            b[k] = (byte) postscript.charAt(k);
        }
        PdfFunction func = new PdfFunction(writer);
        func.dictionary = new PdfStream(b);
        ((PdfStream) func.dictionary).flateCompress(writer.getCompressionLevel());
        func.dictionary.put(PdfName.FUNCTIONTYPE, new PdfNumber(4));
        func.dictionary.put(PdfName.DOMAIN, new PdfArray(domain));
        func.dictionary.put(PdfName.RANGE, new PdfArray(range));
        return func;
    }

    PdfIndirectReference getReference() {
        try {
            if (reference == null) {
                reference = writer.addToBody(dictionary).getIndirectReference();
            }
        } catch (IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
        return reference;
    }
}
