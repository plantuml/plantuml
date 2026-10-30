/*
 * $Id: DocListener.java 3939 2009-05-27 13:09:45Z blowagie $
 *
 * Copyright (c) 1999, 2000, 2001, 2002 Bruno Lowagie.
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

package org.openpdf.text;

/**
 * A class that implements <CODE>DocListener</CODE> will perform some actions when some actions are performed on a
 * <CODE>Document</CODE>.
 *
 * @see ElementListener
 * @see Document
 * @see DocWriter
 */

public interface DocListener extends ElementListener, AutoCloseable {

    // methods

    /**
     * Signals that the <CODE>Document</CODE> has been opened and that
     * <CODE>Elements</CODE> can be added.
     */

    void open(); // [L1]

    /**
     * Signals that the <CODE>Document</CODE> was closed and that no other
     * <CODE>Elements</CODE> will be added.
     * <p>
     * The outputstream of every writer implementing <CODE>DocListener</CODE> will be closed.
     */

    void close(); // [L2]

    /**
     * Signals that an new page has to be started.
     *
     * @return <CODE>true</CODE> if the page was added, <CODE>false</CODE> if not.
     */

    boolean newPage(); // [L3]

    /**
     * Sets the pagesize.
     *
     * @param pageSize the new pagesize
     * @return a <CODE>boolean</CODE>
     */

    boolean setPageSize(Rectangle pageSize); // [L4]

    /**
     * Sets the margins.
     *
     * @param marginLeft   the margin on the left
     * @param marginRight  the margin on the right
     * @param marginTop    the margin on the top
     * @param marginBottom the margin on the bottom
     * @return a <CODE>boolean</CODE>
     */

    boolean setMargins(float marginLeft, float marginRight, float marginTop, float marginBottom);  // [L5]

    /**
     * Allows you to do left/right margin mirroring (odd/even pages)
     *
     * @param marginMirroring left/right margin mirroring (odd/even pages)
     * @return true if successful
     */
    boolean setMarginMirroring(boolean marginMirroring); // [L6]

    /**
     * Parameter that allows you to do top/bottom margin mirroring (odd/even pages)
     *
     * @param marginMirroringTopBottom top/bottom margin mirroring (odd/even pages)
     * @return true if successful
     * @since 2.1.6
     */
    boolean setMarginMirroringTopBottom(boolean marginMirroringTopBottom); // [L6]

    /**
     * Sets the page number.
     *
     * @param pageN the new page number
     */

    void setPageCount(int pageN); // [L7]

    /**
     * Sets the page number to 0.
     */

    void resetPageCount(); // [L12]

}