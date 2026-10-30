/*
 * $Id: PdfDashPattern.java 3373 2008-05-12 16:21:24Z xlv $
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

import java.io.IOException;
import java.io.OutputStream;

/**
 * A <CODE>PdfDashPattern</CODE> defines a dash pattern as described in the PDF Reference Manual version 1.3 p 325
 * (section 8.4.3).
 *
 * @see PdfArray
 */

public class PdfDashPattern extends PdfArray {

    // membervariables

    /**
     * This is the length of a dash.
     */
    private float dash = -1;

    /**
     * This is the length of a gap.
     */
    private float gap = -1;

    /**
     * This is the phase.
     */
    private float phase = -1;

    // constructors

    /**
     * Constructs a new <CODE>PdfDashPattern</CODE>.
     */

    public PdfDashPattern() {
        super();
    }

    /**
     * Constructs a new <CODE>PdfDashPattern</CODE>.
     *
     * @param dash {@link PdfDashPattern#dash}
     */
    public PdfDashPattern(float dash) {
        super(new PdfNumber(dash));
        this.dash = dash;
    }

    /**
     * Constructs a new <CODE>PdfDashPattern</CODE>.
     *
     * @param dash {@link PdfDashPattern#dash}
     * @param gap  {@link PdfDashPattern#gap}
     */
    public PdfDashPattern(float dash, float gap) {
        super(new PdfNumber(dash));
        add(new PdfNumber(gap));
        this.dash = dash;
        this.gap = gap;
    }

    /**
     * Constructs a new <CODE>PdfDashPattern</CODE>.
     *
     * @param dash  {@link PdfDashPattern#dash}
     * @param gap   {@link PdfDashPattern#gap}
     * @param phase {@link PdfDashPattern#phase}
     */
    public PdfDashPattern(float dash, float gap, float phase) {
        super(new PdfNumber(dash));
        add(new PdfNumber(gap));
        this.dash = dash;
        this.gap = gap;
        this.phase = phase;
    }

    public void add(float n) {
        add(new PdfNumber(n));
    }

    /**
     * Returns the PDF representation of this <CODE>PdfArray</CODE>.
     */

    public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
        os.write('[');

        if (dash >= 0) {
            new PdfNumber(dash).toPdf(writer, os);
            if (gap >= 0) {
                os.write(' ');
                new PdfNumber(gap).toPdf(writer, os);
            }
        }
        os.write(']');
        if (phase >= 0) {
            os.write(' ');
            new PdfNumber(phase).toPdf(writer, os);
        }
    }
}