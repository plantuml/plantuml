/*
 * $Id: PdfVersion.java 3811 2009-03-23 18:15:13Z blowagie $
 *
 * Copyright 2006 Bruno Lowagie
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

package org.openpdf.text.pdf.interfaces;

import org.openpdf.text.pdf.PdfDeveloperExtension;
import org.openpdf.text.pdf.PdfName;

/**
 * The PDF version is described in the PDF Reference 1.7 p92 (about the PDF Header) and page 139 (the version entry in
 * the Catalog). You'll also find info about setting the version in the book 'iText in Action' sections 2.1.3 (PDF
 * Header) and 3.3 (Version history).
 *
 * For OpenPDF 3.0 this was updated to support PDF 2.0, and specifying PDF version using String "2.0" instead of char.
 */

public interface PdfVersion {

    /**
     * If the PDF Header hasn't been written yet, this changes the version as it will appear in the PDF Header. If the
     * PDF header was already written to the OutputStream, this changes the version as it will appear in the Catalog.
     *
     * @param version a String representing the PDF version
     */
    void setPdfVersion(String version);

    /**
     * If the PDF Header hasn't been written yet, this changes the version as it will appear in the PDF Header, but only
     * if the parameter refers to a higher version. If the PDF header was already written to the OutputStream, this
     * changes the version as it will appear in the Catalog.
     *
     * @param version a String representing the PDF version
     */
    void setAtLeastPdfVersion(String version);

    /**
     * Sets the PDF version as it will appear in the Catalog. Note that this only has effect if you use a later version
     * than the one that appears in the header; this method ignores the parameter if you try to set a lower version.
     *
     * @param version the PDF name that will be used for the Version key in the catalog
     */
    void setPdfVersion(PdfName version);

    /**
     * Adds a developer extension to the Extensions dictionary in the Catalog.
     *
     * @param de an object that contains the extensions prefix and dictionary
     * @since 2.1.6
     */
    void addDeveloperExtension(PdfDeveloperExtension de);
}
