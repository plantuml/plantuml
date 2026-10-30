/*
 * $Id: PdfEncryption.java 4065 2009-09-16 23:09:11Z psoares33 $
 *
 * Copyright 2001-2006 Paulo Soares
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
import java.security.SecureRandom;
import org.openpdf.text.ExceptionConverter;

/**
 * Only the document identifier helpers are kept: the encryption of PDF documents is not supported by this pruned
 * version of OpenPDF.
 */
public class PdfEncryption {

    private PdfEncryption() {
    }

    /**
     * Created on first use (not at class initialization): the native (TeaVM C) build has no SecureRandom.
     */
    private static final class SecureRandomHolder {
        static final SecureRandom SECURE_RANDOM = new SecureRandom();
    }

    public static byte[] createDocumentId() {
        byte[] id = new byte[16];
        // Native (TeaVM C) build: no SecureRandom (it needs the crypto API of a browser).
        // The marker is tested here, so that TeaVM prunes the SecureRandom code below.
        if (NativePlatform.isC()) {
            new java.util.Random(System.nanoTime() ^ System.currentTimeMillis()).nextBytes(id);
            return id;
        }
        SecureRandomHolder.SECURE_RANDOM.nextBytes(id);
        return id;
    }

    public static PdfObject createInfoId(byte[] id) {
        return createInfoId(id, createDocumentId());
    }

    public static PdfObject createInfoId(byte[] idPartOne, byte[] idPartTwo) {
        try (ByteBuffer buf = new ByteBuffer(90)) {
            buf.append('[').append('<');
            for (int k = 0; k < 16 && k < idPartOne.length; ++k) {
                buf.appendHex(idPartOne[k]);
            }
            buf.append('>').append('<');
            for (int k = 0; k < 16 && k < idPartTwo.length; ++k) {
                buf.appendHex(idPartTwo[k]);
            }
            buf.append('>').append(']');
            return new PdfLiteral(buf.toByteArray());

        } catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }
}
