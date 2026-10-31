/*
 * $Id: PdfStream.java 4065 2009-09-16 23:09:11Z psoares33 $
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

import org.openpdf.text.DocWriter;
import org.openpdf.text.Document;
import org.openpdf.text.ExceptionConverter;
import org.openpdf.text.error_messages.MessageLocalization;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;


/**
 * <CODE>PdfStream</CODE> is the Pdf stream object.
 * <p>
 * A stream, like a string, is a sequence of characters. However, an application can read a small portion of a stream at
 * a time, while a string must be read in its entirety. For this reason, objects with potentially large amounts of data,
 * such as images and page descriptions, are represented as streams.<BR> A stream consists of a dictionary that
 * describes a sequence of characters, followed by the keyword <B>stream</B>, followed by zero or more lines of
 * characters, followed by the keyword <B>endstream</B>.<BR> All streams must be <CODE>PdfIndirectObject</CODE>s. The
 * stream dictionary must be a direct object. The keyword <B>stream</B> that follows the stream dictionary should be
 * followed by a carriage return and linefeed or just a linefeed.<BR> Remark: In this version only the
 * FLATEDECODE-filter is supported.<BR> This object is described in the 'Portable Document Format Reference Manual
 * version 1.7' section 3.2.7 (page 60-63).<BR>
 *
 * @see PdfObject
 * @see PdfDictionary
 */

public class PdfStream extends PdfDictionary {

    // membervariables

    /**
     * A possible compression level.
     *
     * @since 2.1.3
     */
    public static final int DEFAULT_COMPRESSION = -1;
    /**
     * A possible compression level.
     *
     * @since 2.1.3
     */
    public static final int NO_COMPRESSION = 0;
    /**
     * A possible compression level.
     *
     * @since 2.1.3
     */
    public static final int BEST_COMPRESSION = 9;
    static final byte[] STARTSTREAM = DocWriter.getISOBytes("stream\n");
    static final byte[] ENDSTREAM = DocWriter.getISOBytes("\nendstream");
    /**
     * is the stream compressed?
     */
    protected boolean compressed = false;
    /**
     * The level of compression.
     *
     * @since 2.1.3
     */
    protected int compressionLevel = NO_COMPRESSION;
    protected ByteArrayOutputStream streamBytes = null;
    protected InputStream inputStream;
    protected PdfIndirectReference ref;
    protected long inputStreamLength = -1;
    protected PdfWriter writer;
    protected long rawLength;

    // constructors

    /**
     * Constructs a <CODE>PdfStream</CODE>-object.
     *
     * @param bytes content of the new <CODE>PdfObject</CODE> as an array of <CODE>byte</CODE>.
     */

    public PdfStream(byte[] bytes) {
        super();
        type = STREAM;
        this.bytes = bytes;
        rawLength = bytes.length;
        put(PdfName.LENGTH, new PdfNumber(bytes.length));
    }

    /**
     * Constructs a <CODE>PdfStream</CODE>-object.
     */

    protected PdfStream() {
        super();
        type = STREAM;
    }

//    public int getStreamLength(PdfWriter writer) {
//        if (dicBytes == null)
//            toPdf(writer);
//        if (streamBytes != null)
//            return streamBytes.size() + dicBytes.length + SIZESTREAM;
//        else
//            return bytes.length + dicBytes.length + SIZESTREAM;
//    }

    protected void superToPdf(PdfWriter writer, OutputStream os) throws IOException {
        super.toPdf(writer, os);
    }

    /**
     * Writes the data content to an <CODE>OutputStream</CODE>.
     *
     * @param os the destination to write to
     * @throws IOException on error
     */
    public void writeContent(OutputStream os) throws IOException {
        if (streamBytes != null) {
            streamBytes.writeTo(os);
        } else if (bytes != null) {
            os.write(bytes);
        }
    }

    /**
     * @see org.openpdf.text.pdf.PdfObject#toString()
     */
    public String toString() {
        if (get(PdfName.TYPE) == null) {
            return "Stream";
        }
        return "Stream of type: " + get(PdfName.TYPE);
    }




    /**
     * Compresses the stream.
     *
     * @param compressionLevel the compression level (0 = best speed, 9 = best compression, -1 is default)
     * @since 2.1.3
     */
    public void flateCompress(int compressionLevel) {
        // NO_COMPRESSION: the stream stays readable (used for deterministic, diffable output)
        if (!Document.compress || compressionLevel == NO_COMPRESSION) {
            return;
        }
        // check if the flateCompress-method has already been
        if (compressed) {
            return;
        }
        this.compressionLevel = compressionLevel;
        if (inputStream != null) {
            compressed = true;
            return;
        }
        // check if a filter already exists
        PdfObject filter = get(PdfName.FILTER);
        if (filter != null) {
            if (filter.isName()) {
                if (PdfName.FLATEDECODE.equals(filter)) {
                    return;
                }
            } else if (filter.isArray()) {
                if (((PdfArray) filter).contains(PdfName.FLATEDECODE)) {
                    return;
                }
            } else {
                throw new RuntimeException(MessageLocalization.getComposedMessage(
                        "stream.could.not.be.compressed.filter.is.not.a.name.or.array"));
            }
        }
        try {
            // compress
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Deflater deflater = new Deflater(compressionLevel);
            DeflaterOutputStream zip = new DeflaterOutputStream(stream, deflater);
            if (streamBytes != null) {
                streamBytes.writeTo(zip);
            } else {
                zip.write(bytes);
            }
            zip.close();
            deflater.end();
            // update the object
            streamBytes = stream;
            bytes = null;
            put(PdfName.LENGTH, new PdfNumber(streamBytes.size()));
            if (filter == null) {
                put(PdfName.FILTER, PdfName.FLATEDECODE);
            } else {
                PdfArray filters = new PdfArray(filter);
                filters.add(PdfName.FLATEDECODE);
                put(PdfName.FILTER, filters);
            }
            compressed = true;
        } catch (IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }

    /**
     * @see org.openpdf.text.pdf.PdfDictionary#toPdf(org.openpdf.text.pdf.PdfWriter, java.io.OutputStream)
     */
    public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
        if (inputStream != null && compressed) {
            put(PdfName.FILTER, PdfName.FLATEDECODE);
        }
        superToPdf(writer, os);
        os.write(STARTSTREAM);
        if (inputStream != null) {
            rawLength = 0;
            DeflaterOutputStream def = null;
            OutputStreamCounter osc = new OutputStreamCounter(os);
            OutputStream fout = osc;
            Deflater deflater = null;
            if (compressed) {
                deflater = new Deflater(compressionLevel);
                fout = def = new DeflaterOutputStream(fout, deflater, 0x8000);
            }

            byte[] buf = new byte[4192];
            while (true) {
                int n = inputStream.read(buf);
                if (n <= 0) {
                    break;
                }
                fout.write(buf, 0, n);
                rawLength += n;
            }
            if (def != null) {
                def.finish();
                deflater.end();
            }
            inputStreamLength = osc.getCounter();
        } else {
            if (streamBytes != null) {
                streamBytes.writeTo(os);
            } else {
                os.write(bytes);
            }
        }
        os.write(ENDSTREAM);
    }
}
