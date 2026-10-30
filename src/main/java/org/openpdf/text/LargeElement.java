/*
 * $Id: LargeElement.java 3514 2008-06-27 09:26:36Z blowagie $
 *
 * Copyright 2007 by Bruno Lowagie.
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
 * Interface implemented by Element objects that can potentially consume a lot of memory. Objects implementing the
 * LargeElement interface can be added to a Document more than once. If you have invoked setComplete(false), they will
 * be added partially and the content that was added will be removed until you've invoked setComplete(true);
 *
 * @since iText 2.0.8
 */

public interface LargeElement extends Element {

    /**
     * Indicates if the element is complete or not.
     *
     * @return indicates if the element is complete according to the user.
     * @since iText 2.0.8
     */
    boolean isComplete();

    /**
     * If you invoke setComplete(false), you indicate that the content of the object isn't complete yet; it can be added
     * to the document partially, but more will follow. If you invoke setComplete(true), you indicate that you won't add
     * any more data to the object.
     *
     * @param complete false if you'll be adding more data after adding the object to the document.
     * @since iText 2.0.8
     */
    void setComplete(boolean complete);

    /**
     * Flushes the content that has been added.
     */
    void flushContent();
}
