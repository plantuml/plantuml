/*
 * Copyright 2005 Paulo Soares
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
 * Implements the PostScript XObject.
 */
public class PdfPSXObject extends PdfTemplate {

    /**
     * Creates a new instance of PdfPSXObject
     */
    protected PdfPSXObject() {
        super();
    }

    /**
     * Constructs a PSXObject
     *
     * @param wr the PdfWriter
     */
    public PdfPSXObject(PdfWriter wr) {
        super(wr);
    }

    /**
     * Gets the stream representing this object.
     *
     * @param compressionLevel the compressionLevel
     * @return the stream representing this template
     * @since 2.1.3    (replacing the method without param compressionLevel)
     */

    PdfStream getFormXObject(int compressionLevel) {
        PdfStream s = new PdfStream(content.toByteArray());
        s.put(PdfName.TYPE, PdfName.XOBJECT);
        s.put(PdfName.SUBTYPE, PdfName.PS);
        s.flateCompress(compressionLevel);
        return s;
    }

    /**
     * Gets a duplicate of this <CODE>PdfPSXObject</CODE>. All the members are copied by reference but the buffer stays
     * different.
     *
     * @return a copy of this <CODE>PdfPSXObject</CODE>
     */

    public PdfContentByte getDuplicate() {
        PdfPSXObject tpl = new PdfPSXObject();
        tpl.writer = writer;
        tpl.pdf = pdf;
        tpl.thisReference = thisReference;
        tpl.pageResources = pageResources;
        tpl.separator = separator;
        return tpl;
    }
}
