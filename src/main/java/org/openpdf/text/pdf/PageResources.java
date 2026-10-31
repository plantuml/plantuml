/*
 * $Id: PageResources.java 3117 2008-01-31 05:53:22Z xlv $
 *
 * Copyright 2003-2005 by Paulo Soares.
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

import java.util.HashMap;

class PageResources {

    protected PdfDictionary fontDictionary = new PdfDictionary();
    protected PdfDictionary xObjectDictionary = new PdfDictionary();
    protected PdfDictionary colorDictionary = new PdfDictionary();
    protected PdfDictionary patternDictionary = new PdfDictionary();
    protected PdfDictionary shadingDictionary = new PdfDictionary();
    protected PdfDictionary extGStateDictionary = new PdfDictionary();
    protected PdfDictionary propertyDictionary = new PdfDictionary();
    protected HashMap<PdfName, ?> forbiddenNames;
    protected PdfDictionary originalResources;
    protected int[] namePtr = {0};
    protected HashMap<PdfName, PdfName> usedNames;

    PageResources() {
    }

    PdfName translateName(PdfName name) {
        PdfName translated = name;
        if (forbiddenNames != null) {
            translated = usedNames.get(name);
            if (translated == null) {
                do {
                    translated = new PdfName("Xi" + (namePtr[0]++));
                } while (forbiddenNames.containsKey(translated));
                usedNames.put(name, translated);
            }
        }
        return translated;
    }

    PdfName addFont(PdfName name, PdfIndirectReference reference) {
        name = translateName(name);
        fontDictionary.put(name, reference);
        return name;
    }

    PdfName addXObject(PdfName name, PdfIndirectReference reference) {
        name = translateName(name);
        xObjectDictionary.put(name, reference);
        return name;
    }

    PdfName addColor(PdfName name, PdfIndirectReference reference) {
        name = translateName(name);
        colorDictionary.put(name, reference);
        return name;
    }

    void addDefaultColor(PdfDictionary dic) {
        colorDictionary.merge(dic);
    }

    void addDefaultColorDiff(PdfDictionary dic) {
        colorDictionary.mergeDifferent(dic);
    }

    PdfName addPattern(PdfName name, PdfIndirectReference reference) {
        name = translateName(name);
        patternDictionary.put(name, reference);
        return name;
    }

    PdfName addExtGState(PdfName name, PdfIndirectReference reference) {
        name = translateName(name);
        extGStateDictionary.put(name, reference);
        return name;
    }



    PdfDictionary getResources() {
        PdfResources resources = new PdfResources();
        if (originalResources != null) {
            resources.putAll(originalResources);
        }
        //resources.put(PdfName.PROCSET, new PdfLiteral("[/PDF /Text /ImageB /ImageC /ImageI]"));
        resources.add(PdfName.FONT, fontDictionary);
        resources.add(PdfName.XOBJECT, xObjectDictionary);
        resources.add(PdfName.COLORSPACE, colorDictionary);
        resources.add(PdfName.PATTERN, patternDictionary);
        resources.add(PdfName.SHADING, shadingDictionary);
        resources.add(PdfName.EXTGSTATE, extGStateDictionary);
        resources.add(PdfName.PROPERTIES, propertyDictionary);
        return resources;
    }
}
