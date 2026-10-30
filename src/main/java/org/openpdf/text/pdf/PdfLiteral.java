/*
 * $Id: PdfLiteral.java 3373 2008-05-12 16:21:24Z xlv $
 *
 * Copyright 2001, 2002 Paulo Soares
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

/**
 * a Literal
 */

public class PdfLiteral extends PdfObject {

    /**
     * Holds value of property position.
     */
    private long position;

    public PdfLiteral(String text) {
        super(0, text);
    }

    public PdfLiteral(byte[] b) {
        super(0, b);
    }

    public PdfLiteral(int size) {
        super(0, (byte[]) null);
        bytes = new byte[size];
        java.util.Arrays.fill(bytes, (byte) 32);
    }

    public PdfLiteral(int type, String text) {
        super(type, text);
    }

    public PdfLiteral(int type, byte[] b) {
        super(type, b);
    }

    public void toPdf(PdfWriter writer, java.io.OutputStream os) throws java.io.IOException {
        if (os instanceof OutputStreamCounter) {
            position = ((OutputStreamCounter) os).getCounter();
        }
        super.toPdf(writer, os);
    }

    /**
     * Getter for property position.
     *
     * @return Value of property position.
     */
    public long getPosition() {
        return this.position;
    }

    /**
     * Getter for property posLength.
     *
     * @return Value of property posLength.
     */
    public int getPosLength() {
        if (bytes != null) {
            return bytes.length;
        } else {
            return 0;
        }
    }

}