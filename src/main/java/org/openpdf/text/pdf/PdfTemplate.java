/*
 * $Id: PdfTemplate.java 3929 2009-05-22 13:26:41Z blowagie $
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

import org.openpdf.text.Rectangle;
import java.io.IOException;

/**
 * Implements the form XObject.
 */

public class PdfTemplate extends PdfContentByte {

    public static final int TYPE_TEMPLATE = 1;
    public static final int TYPE_PATTERN = 3;
    protected int type;
    /**
     * The indirect reference to this template
     */
    protected PdfIndirectReference thisReference;

    /**
     * The resources used by this template
     */
    protected PageResources pageResources;


    /**
     * The bounding box of this template
     */
    protected Rectangle bBox = new Rectangle(0, 0);

    protected PdfArray matrix;

    protected PdfTransparencyGroup group;

    /**
     * Creates a <CODE>PdfTemplate</CODE>.
     */

    /**
     * Creates new PdfTemplate
     *
     * @param wr the <CODE>PdfWriter</CODE>
     */

    PdfTemplate(PdfWriter wr) {
        super(wr);
        type = TYPE_TEMPLATE;
        pageResources = new PageResources();
        pageResources.addDefaultColor(wr.getDefaultColorspace());
        thisReference = writer.getPdfIndirectReference();
    }

    /**
     * Gets the bounding width of this template.
     *
     * @return width the bounding width
     */
    public float getWidth() {
        return bBox.getWidth();
    }

    /**
     * Sets the bounding width of this template.
     *
     * @param width the bounding width
     */

    public void setWidth(float width) {
        bBox.setLeft(0);
        bBox.setRight(width);
    }

    /**
     * Gets the bounding height of this template.
     *
     * @return height the bounding height
     */

    public float getHeight() {
        return bBox.getHeight();
    }

    /**
     * Sets the bounding height of this template.
     *
     * @param height the bounding height
     */

    public void setHeight(float height) {
        bBox.setBottom(0);
        bBox.setTop(height);
    }

    public Rectangle getBoundingBox() {
        return bBox;
    }

    public void setBoundingBox(Rectangle bBox) {
        this.bBox = bBox;
    }

    PdfArray getMatrix() {
        return matrix;
    }

    /**
     * Gets the indirect reference to this template.
     *
     * @return the indirect reference to this template
     */

    public PdfIndirectReference getIndirectReference() {
        // uncomment the null check as soon as we're sure all examples still work
        if (thisReference == null /* && writer != null */) {
            thisReference = writer.getPdfIndirectReference();
        }
        return thisReference;
    }

    /**
     * Constructs the resources used by this template.
     *
     * @return the resources used by this template
     */

    PdfObject getResources() {
        return getPageResources().getResources();
    }

    /**
     * Gets the stream representing this template.
     *
     * @param compressionLevel the compressionLevel
     * @return the stream representing this template
     * @since 2.1.3    (replacing the method without param compressionLevel)
     */
    PdfStream getFormXObject(int compressionLevel) throws IOException {
        return new PdfFormXObject(this, compressionLevel);
    }

    /**
     * Gets a duplicate of this <CODE>PdfTemplate</CODE>. All the members are copied by reference but the buffer stays
     * different.
     *
     * @return a copy of this <CODE>PdfTemplate</CODE>
     */

    public int getType() {
        return type;
    }

    PageResources getPageResources() {
        return pageResources;
    }

    /**
     * Getter for property group.
     *
     * @return Value of property group.
     */
    public PdfTransparencyGroup getGroup() {
        return this.group;
    }

}