/*
 * $Id:  $
 *
 * Copyright 2009 by Bruno Lowagie.
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
 * the Initial Developer are Copyright (C) 1999-2009 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000-2009 by Paulo Soares. All Rights Reserved.
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
 * Beginning with BaseVersion 1.7, the extensions dictionary lets developers designate that a given document contains
 * extensions to PDF. The presence of the extension dictionary in a document indicates that it may contain
 * developer-specific PDF properties that extend a particular base version of the PDF specification. The extensions
 * dictionary enables developers to identify their own extensions relative to a base version of PDF. Additionally, the
 * convention identifies extension levels relative to that base version. The intent of this dictionary is to enable
 * developers of PDF-producing applications to identify company-specific specifications (such as this one) that
 * PDF-consuming applications use to interpret the extensions.
 *
 * @since 2.1.6
 */
public class PdfDeveloperExtension {

    /**
     * An instance of this class for Adobe 1.7 Extension level 3.
     */
    public static final PdfDeveloperExtension ADOBE_1_7_EXTENSIONLEVEL3 =
            new PdfDeveloperExtension(PdfName.ADBE, PdfWriter.PDF_VERSION_1_7, 3);

    /**
     * The prefix used in the Extensions dictionary added to the Catalog.
     */
    protected PdfName prefix;
    /**
     * The base version.
     */
    protected PdfName baseversion;
    /**
     * The extension level within the baseversion.
     */
    protected int extensionLevel;

    /**
     * Creates a PdfDeveloperExtension object.
     *
     * @param prefix         the prefix referring to the developer
     * @param baseversion    the number of the base version
     * @param extensionLevel the extension level within the baseverion.
     */
    public PdfDeveloperExtension(PdfName prefix, PdfName baseversion, int extensionLevel) {
        this.prefix = prefix;
        this.baseversion = baseversion;
        this.extensionLevel = extensionLevel;
    }

    /**
     * Gets the prefix name.
     *
     * @return a PdfName
     */
    public PdfName getPrefix() {
        return prefix;
    }

    /**
     * Gets the baseversion name.
     *
     * @return a PdfName
     */
    public PdfName getBaseversion() {
        return baseversion;
    }

    /**
     * Gets the extension level within the baseversion.
     *
     * @return an integer
     */
    public int getExtensionLevel() {
        return extensionLevel;
    }

    /**
     * Generations the developer extension dictionary corresponding with the prefix.
     *
     * @return a PdfDictionary
     */
    public PdfDictionary getDeveloperExtensions() {
        PdfDictionary developerextensions = new PdfDictionary();
        developerextensions.put(PdfName.BASEVERSION, baseversion);
        developerextensions.put(PdfName.EXTENSIONLEVEL, new PdfNumber(extensionLevel));
        return developerextensions;
    }
}
