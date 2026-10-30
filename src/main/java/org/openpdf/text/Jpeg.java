/*
 * $Id: Jpeg.java 4074 2009-10-05 17:17:26Z psoares33 $
 *
 * Copyright 1999, 2000, 2001, 2002 by Bruno Lowagie.
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

import org.openpdf.text.utils.UrlStreams;
import org.openpdf.text.error_messages.MessageLocalization;
import java.awt.color.ICC_Profile;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * An <CODE>Jpeg</CODE> is the representation of a graphic element (JPEG) that has to be inserted into the document
 *
 * @see Element
 * @see Image
 */

public class Jpeg extends Image {

    // public static final membervariables

    /**
     * This is a type of marker.
     */
    public static final int NOT_A_MARKER = -1;

    /**
     * This is a type of marker.
     */
    public static final int VALID_MARKER = 0;

    /**
     * Acceptable Jpeg markers.
     */
    public static final int[] VALID_MARKERS = {0xC0, 0xC1, 0xC2};

    /**
     * This is a type of marker.
     */
    public static final int UNSUPPORTED_MARKER = 1;

    /**
     * Unsupported Jpeg markers.
     */
    public static final int[] UNSUPPORTED_MARKERS = {0xC3, 0xC5, 0xC6, 0xC7, 0xC8, 0xC9, 0xCA, 0xCB, 0xCD, 0xCE, 0xCF};

    /**
     * This is a type of marker.
     */
    public static final int NOPARAM_MARKER = 2;

    /**
     * Jpeg markers without additional parameters.
     */
    public static final int[] NOPARAM_MARKERS = {0xD0, 0xD1, 0xD2, 0xD3, 0xD4, 0xD5, 0xD6, 0xD7, 0xD8, 0x01};

    /**
     * Marker value
     */
    public static final int M_APP0 = 0xE0;
    /**
     * Marker value
     */
    public static final int M_APP2 = 0xE2;
    /**
     * Marker value
     */
    public static final int M_APPE = 0xEE;

    /**
     * sequence that is used in all Jpeg files
     */
    public static final byte[] JFIF_ID = {0x4A, 0x46, 0x49, 0x46, 0x00};

    private byte[][] icc;
    // Constructors

    Jpeg(Image image) {
        super(image);
    }

    /**
     * Constructs a <CODE>Jpeg</CODE>-object, using an <VAR>url</VAR>.
     *
     * @param url the <CODE>URL</CODE> where the image can be found
     * @throws BadElementException on error
     * @throws IOException         on error
     */
    public Jpeg(URL url) throws BadElementException, IOException {
        super(url);
        processParameters();
    }

    /**
     * Constructs a <CODE>Jpeg</CODE>-object from memory.
     *
     * @param img the memory image
     * @throws BadElementException on error
     * @throws IOException         on error
     */
    public Jpeg(byte[] img) throws BadElementException, IOException {
        super((URL) null);
        rawData = img;
        originalData = img;
        processParameters();
    }

    /**
     * Constructs a <CODE>Jpeg</CODE>-object from memory.
     *
     * @param img    the memory image.
     * @param width  the width you want the image to have
     * @param height the height you want the image to have
     * @throws BadElementException on error
     * @throws IOException         on error
     */
    public Jpeg(byte[] img, float width, float height) throws BadElementException, IOException {
        this(img);
        scaledWidth = width;
        scaledHeight = height;
    }

    // private static methods

    /**
     * Reads a short from the <CODE>InputStream</CODE>.
     *
     * @param is the <CODE>InputStream</CODE>
     * @return an int
     * @throws IOException on error
     */
    private static int getShort(InputStream is) throws IOException {
        return (is.read() << 8) + is.read();
    }

    /**
     * Returns a type of marker.
     *
     * @param marker an int
     * @return a type: <code>VALID_MARKER</code>, <code>UNSUPPORTED_MARKER</code> or <code>NOPARAM_MARKER</code>
     */
    private static int marker(int marker) {
        for (int validMarker : VALID_MARKERS) {
            if (marker == validMarker) {
                return VALID_MARKER;
            }
        }
        for (int noparamMarker : NOPARAM_MARKERS) {
            if (marker == noparamMarker) {
                return NOPARAM_MARKER;
            }
        }
        for (int unsupportedMarker : UNSUPPORTED_MARKERS) {
            if (marker == unsupportedMarker) {
                return UNSUPPORTED_MARKER;
            }
        }
        return NOT_A_MARKER;
    }

    // private methods

    /**
     * This method checks if the image is a valid JPEG and processes some parameters.
     * <p>
     * TODO: Use Apache Commons Imaging to parse these parameters instead.
     *
     * @throws BadElementException
     * @throws IOException
     */
    private void processParameters() throws BadElementException, IOException {
        type = JPEG;
        originalType = ORIGINAL_JPEG;
        InputStream is = null;
        try {
            String errorID;
            if (rawData == null) {
                is = UrlStreams.open(url);
                errorID = url.toString();
            } else {
                is = new java.io.ByteArrayInputStream(rawData);
                errorID = "Byte array";
            }
            if (is.read() != 0xFF || is.read() != 0xD8) {
                throw new BadElementException(
                        MessageLocalization.getComposedMessage("1.is.not.a.valid.jpeg.file", errorID));
            }
            boolean firstPass = true;
            int len;
            while (true) {
                int v = is.read();
                if (v < 0) {
                    throw new IOException(MessageLocalization.getComposedMessage("premature.eof.while.reading.jpg"));
                }
                if (v == 0xFF) {
                    int marker;
                    do {
                        marker = is.read();
                    } while (marker == 0xFF); // Skip extra FF bytes, per JPEG spec B.1.1.2
                    if (firstPass && marker == M_APP0) {
                        firstPass = false;
                        len = getShort(is);
                        if (len < 16) {
                            Utilities.skip(is, len - 2);
                            continue;
                        }
                        byte[] bcomp = new byte[JFIF_ID.length];
                        int r = is.read(bcomp);
                        if (r != bcomp.length) {
                            throw new BadElementException(
                                    MessageLocalization.getComposedMessage("1.corrupted.jfif.marker", errorID));
                        }
                        boolean found = true;
                        for (int k = 0; k < bcomp.length; ++k) {
                            if (bcomp[k] != JFIF_ID[k]) {
                                found = false;
                                break;
                            }
                        }
                        if (!found) {
                            Utilities.skip(is, len - 2 - bcomp.length);
                            continue;
                        }
                        Utilities.skip(is, 2);
                        int units = is.read();
                        int dx = getShort(is);
                        int dy = getShort(is);
                        if (units == 1) {
                            dpiX = dx;
                            dpiY = dy;
                        } else if (units == 2) {
                            dpiX = (int) (dx * 2.54f + 0.5f);
                            dpiY = (int) (dy * 2.54f + 0.5f);
                        }
                        Utilities.skip(is, len - 2 - bcomp.length - 7);
                        continue;
                    }
                    if (marker == M_APPE) {
                        len = getShort(is) - 2;
                        byte[] byteappe = new byte[len];
                        for (int k = 0; k < len; ++k) {
                            byteappe[k] = (byte) is.read();
                        }
                        if (byteappe.length >= 12) {
                            String appe = new String(byteappe, 0, 5, StandardCharsets.ISO_8859_1);
                            if (appe.equals("Adobe")) {
                                invert = true;
                            }
                        }
                        continue;
                    }
                    if (marker == M_APP2) {
                        len = getShort(is) - 2;
                        byte[] byteapp2 = new byte[len];
                        for (int k = 0; k < len; ++k) {
                            byteapp2[k] = (byte) is.read();
                        }
                        if (byteapp2.length >= 14) {
                            String app2 = new String(byteapp2, 0, 11, StandardCharsets.ISO_8859_1);
                            if (app2.equals("ICC_PROFILE")) {
                                int order = byteapp2[12] & 0xff;
                                int count = byteapp2[13] & 0xff;
                                // some jpeg producers don't know how to count to 1
                                if (order < 1) {
                                    order = 1;
                                }
                                if (count < 1) {
                                    count = 1;
                                }
                                if (icc == null) {
                                    icc = new byte[count][];
                                }
                                icc[order - 1] = byteapp2;
                            }
                        }
                        continue;
                    }
                    firstPass = false;
                    int markertype = marker(marker);
                    if (markertype == VALID_MARKER) {
                        Utilities.skip(is, 2);
                        if (is.read() != 0x08) {
                            throw new BadElementException(
                                    MessageLocalization.getComposedMessage("1.must.have.8.bits.per.component",
                                            errorID));
                        }
                        scaledHeight = getShort(is);
                        setTop(scaledHeight);
                        scaledWidth = getShort(is);
                        setRight(scaledWidth);
                        colorspace = is.read();
                        bpc = 8;
                        break;
                    } else if (markertype == UNSUPPORTED_MARKER) {
                        throw new BadElementException(
                                MessageLocalization.getComposedMessage("1.unsupported.jpeg.marker.2", errorID,
                                        String.valueOf(marker)));
                    } else if (markertype != NOPARAM_MARKER) {
                        Utilities.skip(is, getShort(is) - 2);
                    }
                }
            }
        } finally {
            if (is != null) {
                is.close();
            }
        }
        plainWidth = getWidth();
        plainHeight = getHeight();
        if (icc != null) {
            int total = 0;
            for (int k = 0; k < icc.length; ++k) {
                if (icc[k] == null) {
                    icc = null;
                    return;
                }
                total += icc[k].length - 14;
            }
            byte[] ficc = new byte[total];
            total = 0;
            for (byte[] bytes : icc) {
                System.arraycopy(bytes, 14, ficc, total, bytes.length - 14);
                total += bytes.length - 14;
            }
            try {
                ICC_Profile icc_prof = ICC_Profile.getInstance(ficc);
                tagICC(icc_prof);
            } catch (IllegalArgumentException e) {
                // ignore ICC profile if it's invalid.
            }
            icc = null;
        }
    }
}
