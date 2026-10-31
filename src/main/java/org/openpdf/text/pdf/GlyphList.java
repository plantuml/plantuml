/*
 * $Id: GlyphList.java 3373 2008-05-12 16:21:24Z xlv $
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.StringTokenizer;

public class GlyphList {

    private static HashMap<Integer, String> unicode2names = new HashMap<>();
    private static HashMap<String, int[]> names2unicode = new HashMap<>();

    static {
        InputStream is = null;
        try {
            is = BaseFont.getResourceStream(BaseFont.RESOURCE_PATH + "glyphlist.txt");
            if (is == null) {
                String msg = "glyphlist.txt not found as resource. (It must exist as resource in the package org.openpdf.text.pdf.fonts)";
                throw new Exception(msg);
            }
            byte[] buf = new byte[1024];
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            while (true) {
                int size = is.read(buf);
                if (size < 0) {
                    break;
                }
                out.write(buf, 0, size);
            }
            is.close();
            is = null;
            String s = PdfEncodings.convertToString(out.toByteArray(), null);
            StringTokenizer tk = new StringTokenizer(s, "\r\n");
            while (tk.hasMoreTokens()) {
                String line = tk.nextToken();
                if (line.startsWith("#")) {
                    continue;
                }
                StringTokenizer t2 = new StringTokenizer(line, " ;\r\n\t\f");
                String name;
                String hex;
                if (!t2.hasMoreTokens()) {
                    continue;
                }
                name = t2.nextToken();
                if (!t2.hasMoreTokens()) {
                    continue;
                }
                hex = t2.nextToken();
                Integer num = Integer.valueOf(hex, 16);
                unicode2names.put(num, name);
                names2unicode.put(name, new int[]{num});
            }
        } catch (Exception e) {
            System.err.println("glyphlist.txt loading error: " + e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    // empty on purpose
                }
            }
        }
    }

    public static String unicodeToName(int num) {
        return unicode2names.get(num);
    }
}
