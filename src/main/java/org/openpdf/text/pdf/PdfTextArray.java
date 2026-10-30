/*
 * $Id: PdfTextArray.java 3382 2008-05-15 10:18:45Z psoares33 $
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

import java.util.LinkedList;
import java.util.List;

/**
 * <CODE>PdfTextArray</CODE> defines an array with displacements and <CODE>PdfString</CODE>-objects.
 * <p>
 * A <CODE>TextArray</CODE> is used with the operator <VAR>TJ</VAR> in <CODE>PdfText</CODE>. The first object in this
 * array has to be a <CODE>PdfString</CODE>; see reference manual version 1.3 section 8.7.5, pages 346-347. OR see
 * reference manual version 1.6 section 5.3.2, pages 378-379.
 */

public class PdfTextArray {

    private List<Object> arrayList = new LinkedList<>();

    // To emit a more efficient array, we consolidate
    // repeated numbers or strings into single array entries.
    // "add( 50 ); add( -50 );" will REMOVE the combined zero from the array.
    // the alternative (leaving a zero in there) was Just Weird.
    // --Mark Storer, May 12, 2008
    private String lastStr;
    private Float lastNum;
    private boolean isRTL = false;

    // constructors
    public PdfTextArray(String str) {
        add(str);
    }

    public PdfTextArray() {
    }

    /**
     * Adds a <CODE>PdfNumber</CODE> to the <CODE>PdfArray</CODE>.
     *
     * @param number displacement of the string
     */
    public void add(PdfNumber number) {
        add((float) number.doubleValue());
    }

    public void add(float number) {
        if (number != 0) {
            if (lastNum != null) {
                lastNum = number + lastNum;
                if (lastNum != 0) {
                    replaceLast(lastNum);
                } else {
                    arrayList.remove(isRTL ? 0 : arrayList.size() - 1);
                }
            } else {
                lastNum = number;
                if (isRTL) {
                    arrayList.add(0, lastNum);
                } else {
                    arrayList.add(lastNum);
                }
            }

            lastStr = null;
        }
        // adding zero doesn't modify the TextArray at all
    }

    public void add(String str) {
        if (isRTL) {
            str = new StringBuffer(str).reverse().toString();
        }
        if (str.length() > 0) {
            if (lastStr != null) {
                if (isRTL) {
                    lastStr = str + lastStr;
                } else {
                    lastStr = lastStr + str;
                }
                replaceLast(lastStr);
            } else {
                lastStr = str;
                if (isRTL) {
                    arrayList.add(0, lastStr);
                } else {
                    arrayList.add(lastStr);
                }
            }
            lastNum = null;
        }
        // adding an empty string doesn't modify the TextArray at all
    }

    List getArrayList() {
        return arrayList;
    }

    private void replaceLast(Object obj) {
        // deliberately throw the IndexOutOfBoundsException if we screw up.
        if (isRTL) {
            arrayList.set(0, obj);
        } else {
            arrayList.set(arrayList.size() - 1, obj);
        }
    }

    public boolean getRTL() {
        return this.isRTL;
    }

    public void setRTL(boolean isRTL) {
        this.isRTL = isRTL;
    }
}
