/*
 * $Id: ByteBuffer.java 4065 2009-09-16 23:09:11Z psoares33 $
 * $Name$
 *
 * Copyright 2000, 2001, 2002 by Paulo Soares.
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
import org.openpdf.text.error_messages.MessageLocalization;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Acts like a <CODE>StringBuffer</CODE> but works with <CODE>byte</CODE> arrays. Floating point is converted to a
 * format suitable to the PDF.
 *
 * @author Paulo Soares (psoares@consiste.pt)
 */

public class ByteBuffer extends OutputStream {

    public static final byte ZERO = (byte) '0';
    private static final char[] chars = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static final byte[] bytes = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
    private static final DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    /**
     * If <CODE>true</CODE> always output floating point numbers with 6 decimal digits. If <CODE>false</CODE> uses the
     * faster, although less precise, representation.
     */
    public static boolean HIGH_PRECISION = false;
    private static int byteCacheSize = 0;
    private static byte[][] byteCache = new byte[byteCacheSize][];
    /**
     * The count of bytes in the buffer.
     */
    protected int count;
    /**
     * The buffer where the bytes are stored.
     */
    protected byte[] buf;

    /**
     * Creates new ByteBuffer with capacity 128
     */
    public ByteBuffer() {
        this(128);
    }

    /**
     * Creates a byte buffer with a certain capacity.
     *
     * @param size the initial capacity
     */
    public ByteBuffer(int size) {
        if (size < 1) {
            size = 128;
        }
        buf = new byte[size];
    }

    /**
     * Sets the cache size.
     * <p>
     * This can only be used to increment the size. If the size that is passed through is smaller than the current size,
     * nothing happens.
     *
     * @param size the size of the cache
     */

    public static void setCacheSize(int size) {
        if (size > 3276700) {
            size = 3276700;
        }
        if (size <= byteCacheSize) {
            return;
        }
        byte[][] tmpCache = new byte[size][];
        System.arraycopy(byteCache, 0, tmpCache, 0, byteCacheSize);
        byteCache = tmpCache;
        byteCacheSize = size;
    }

    /**
     * You can fill the cache in advance if you want to.
     *
     * @param decimals decimals
     */

    public static void fillCache(int decimals) {
        int step = 1;
        switch (decimals) {
            case 0:
                step = 100;
                break;
            case 1:
                step = 10;
                break;
        }
        for (int i = 1; i < byteCacheSize; i += step) {
            if (byteCache[i] != null) {
                continue;
            }
            byteCache[i] = convertToBytes(i);
        }
    }

    /**
     * Converts an double (multiplied by 100 and cast to an int) into an array of bytes.
     *
     * @param i the int
     * @return a byte array
     */

    private static byte[] convertToBytes(int i) {
        int size = (int) Math.floor(Math.log(i) / Math.log(10));
        if (i % 100 != 0) {
            size += 2;
        }
        if (i % 10 != 0) {
            size++;
        }
        if (i < 100) {
            size++;
            if (i < 10) {
                size++;
            }
        }
        size--;
        byte[] cache = new byte[size];
        size--;
        if (i < 100) {
            cache[0] = (byte) '0';
        }
        if (i % 10 != 0) {
            cache[size--] = bytes[i % 10];
        }
        if (i % 100 != 0) {
            cache[size--] = bytes[(i / 10) % 10];
            cache[size--] = (byte) '.';
        }
        size = (int) Math.floor(Math.log(i) / Math.log(10)) - 1;
        int add = 0;
        while (add < size) {
            cache[add] = bytes[(i / (int) Math.pow(10, size - add + 1)) % 10];
            add++;
        }
        return cache;
    }

    /**
     * Outputs a <CODE>double</CODE> into a format suitable for the PDF.
     *
     * @param d a double
     * @return the <CODE>String</CODE> representation of the <CODE>double</CODE>
     */
    public static String formatDouble(double d) {
        return formatDouble(d, null);
    }

    /**
     * Outputs a <CODE>double</CODE> into a format suitable for the PDF.
     *
     * @param d   a double
     * @param buf a ByteBuffer
     * @return the <CODE>String</CODE> representation of the <CODE>double</CODE> if
     * <CODE>buf</CODE> is <CODE>null</CODE>. If <CODE>buf</CODE> is <B>not</B> <CODE>null</CODE>,
     * then the double is appended directly to the buffer and this methods returns <CODE>null</CODE>.
     */
    public static String formatDouble(double d, ByteBuffer buf) {
        if (HIGH_PRECISION) {
            DecimalFormat dn = new DecimalFormat("0.######", dfs);
            String sform = dn.format(d);
            if (buf == null) {
                return sform;
            } else {
                buf.append(sform);
                return null;
            }
        }
        boolean negative = false;
        if (Math.abs(d) < 0.000015) {
            if (buf != null) {
                buf.append(ZERO);
                return null;
            } else {
                return "0";
            }
        }
        if (d < 0) {
            negative = true;
            d = -d;
        }
        if (d < 1.0) {
            d += 0.000005;
            if (d >= 1) {
                if (negative) {
                    if (buf != null) {
                        buf.append((byte) '-');
                        buf.append((byte) '1');
                        return null;
                    } else {
                        return "-1";
                    }
                } else {
                    if (buf != null) {
                        buf.append((byte) '1');
                        return null;
                    } else {
                        return "1";
                    }
                }
            }
            if (buf != null) {
                int v = (int) (d * 100000);

                if (negative) {
                    buf.append((byte) '-');
                }
                buf.append((byte) '0');
                buf.append((byte) '.');

                buf.append((byte) (v / 10000 + ZERO));
                if (v % 10000 != 0) {
                    buf.append((byte) ((v / 1000) % 10 + ZERO));
                    if (v % 1000 != 0) {
                        buf.append((byte) ((v / 100) % 10 + ZERO));
                        if (v % 100 != 0) {
                            buf.append((byte) ((v / 10) % 10 + ZERO));
                            if (v % 10 != 0) {
                                buf.append((byte) ((v) % 10 + ZERO));
                            }
                        }
                    }
                }
                return null;
            } else {
                int x = 100000;
                int v = (int) (d * x);

                StringBuilder res = new StringBuilder();
                if (negative) {
                    res.append('-');
                }
                res.append("0.");

                while (v < x / 10) {
                    res.append('0');
                    x /= 10;
                }
                res.append(v);
                int cut = res.length() - 1;
                while (res.charAt(cut) == '0') {
                    --cut;
                }
                res.setLength(cut + 1);
                return res.toString();
            }
        } else if (d <= 32767) {
            d += 0.005;
            int v = (int) (d * 100);

            if (v < byteCacheSize && byteCache[v] != null) {
                if (buf != null) {
                    if (negative) {
                        buf.append((byte) '-');
                    }
                    buf.append(byteCache[v]);
                    return null;
                } else {
                    String tmp = PdfEncodings.convertToString(byteCache[v], null);
                    if (negative) {
                        tmp = "-" + tmp;
                    }
                    return tmp;
                }
            }
            if (buf != null) {
                if (v < byteCacheSize) {
                    //create the cachebyte[]
                    byte[] cache;
                    int size = 0;
                    if (v >= 1000000) {
                        //the original number is >=10000, we need 5 more bytes
                        size += 5;
                    } else if (v >= 100000) {
                        //the original number is >=1000, we need 4 more bytes
                        size += 4;
                    } else if (v >= 10000) {
                        //the original number is >=100, we need 3 more bytes
                        size += 3;
                    } else if (v >= 1000) {
                        //the original number is >=10, we need 2 more bytes
                        size += 2;
                    } else if (v >= 100) {
                        //the original number is >=1, we need 1 more bytes
                        size += 1;
                    }

                    //now we must check if we have a decimal number
                    if (v % 100 != 0) {
                        //yes, do not forget the "."
                        size += 2;
                    }
                    if (v % 10 != 0) {
                        size++;
                    }
                    cache = new byte[size];
                    int add = 0;
                    if (v >= 1000000) {
                        cache[add++] = bytes[(v / 1000000)];
                    }
                    if (v >= 100000) {
                        cache[add++] = bytes[(v / 100000) % 10];
                    }
                    if (v >= 10000) {
                        cache[add++] = bytes[(v / 10000) % 10];
                    }
                    if (v >= 1000) {
                        cache[add++] = bytes[(v / 1000) % 10];
                    }
                    if (v >= 100) {
                        cache[add++] = bytes[(v / 100) % 10];
                    }

                    if (v % 100 != 0) {
                        cache[add++] = (byte) '.';
                        cache[add++] = bytes[(v / 10) % 10];
                        if (v % 10 != 0) {
                            cache[add++] = bytes[v % 10];
                        }
                    }
                    byteCache[v] = cache;
                }

                if (negative) {
                    buf.append((byte) '-');
                }
                if (v >= 1000000) {
                    buf.append(bytes[(v / 1000000)]);
                }
                if (v >= 100000) {
                    buf.append(bytes[(v / 100000) % 10]);
                }
                if (v >= 10000) {
                    buf.append(bytes[(v / 10000) % 10]);
                }
                if (v >= 1000) {
                    buf.append(bytes[(v / 1000) % 10]);
                }
                if (v >= 100) {
                    buf.append(bytes[(v / 100) % 10]);
                }

                if (v % 100 != 0) {
                    buf.append((byte) '.');
                    buf.append(bytes[(v / 10) % 10]);
                    if (v % 10 != 0) {
                        buf.append(bytes[v % 10]);
                    }
                }
                return null;
            } else {
                StringBuilder res = new StringBuilder();
                if (negative) {
                    res.append('-');
                }
                if (v >= 1000000) {
                    res.append(chars[(v / 1000000)]);
                }
                if (v >= 100000) {
                    res.append(chars[(v / 100000) % 10]);
                }
                if (v >= 10000) {
                    res.append(chars[(v / 10000) % 10]);
                }
                if (v >= 1000) {
                    res.append(chars[(v / 1000) % 10]);
                }
                if (v >= 100) {
                    res.append(chars[(v / 100) % 10]);
                }

                if (v % 100 != 0) {
                    res.append('.');
                    res.append(chars[(v / 10) % 10]);
                    if (v % 10 != 0) {
                        res.append(chars[v % 10]);
                    }
                }
                return res.toString();
            }
        } else {
            StringBuilder res = new StringBuilder();
            if (negative) {
                res.append('-');
            }
            d += 0.5;
            long v = (long) d;
            return res.append(v).toString();
        }
    }

    /**
     * Appends an <CODE>int</CODE>. The size of the array will grow by one.
     *
     * @param b the int to be appended
     * @return a reference to this <CODE>ByteBuffer</CODE> object
     */
    public ByteBuffer append_i(int b) {
        int newcount = count + 1;
        if (newcount > buf.length) {
            byte[] newbuf = new byte[Math.max(buf.length << 1, newcount)];
            System.arraycopy(buf, 0, newbuf, 0, count);
            buf = newbuf;
        }
        buf[count] = (byte) b;
        count = newcount;
        return this;
    }

    /**
     * Appends the subarray of the <CODE>byte</CODE> array. The buffer will grow by
     * <CODE>len</CODE> bytes.
     *
     * @param b   the array to be appended
     * @param off the offset to the start of the array
     * @param len the length of bytes to append
     * @return a reference to this <CODE>ByteBuffer</CODE> object
     */
    public ByteBuffer append(byte[] b, int off, int len) {
        if ((off < 0) || (off > b.length) || (len < 0) ||
                ((off + len) > b.length) || ((off + len) < 0) || len == 0) {
            return this;
        }
        int newcount = count + len;
        if (newcount > buf.length) {
            byte[] newbuf = new byte[Math.max(buf.length << 1, newcount)];
            System.arraycopy(buf, 0, newbuf, 0, count);
            buf = newbuf;
        }
        System.arraycopy(b, off, buf, count, len);
        count = newcount;
        return this;
    }

    /**
     * Appends an array of bytes.
     *
     * @param b the array to be appended
     * @return a reference to this <CODE>ByteBuffer</CODE> object
     */
    public ByteBuffer append(byte[] b) {
        return append(b, 0, b.length);
    }

    /**
     * Appends a <CODE>String</CODE> to the buffer. The <CODE>String</CODE> is converted according to the encoding
     * ISO-8859-1.
     *
     * @param str the <CODE>String</CODE> to be appended
     * @return a reference to this <CODE>ByteBuffer</CODE> object
     */
    public ByteBuffer append(String str) {
        if (str != null) {
            return append(DocWriter.getISOBytes(str));
        }
        return this;
    }

    /**
     * Appends a <CODE>char</CODE> to the buffer. The <CODE>char</CODE> is converted according to the encoding
     * ISO-8859-1.
     *
     * @param c the <CODE>char</CODE> to be appended
     * @return a reference to this <CODE>ByteBuffer</CODE> object
     */
    public ByteBuffer append(char c) {
        return append_i(c);
    }

    /**
     * Appends another <CODE>ByteBuffer</CODE> to this buffer.
     *
     * @param buf the <CODE>ByteBuffer</CODE> to be appended
     * @return a reference to this <CODE>ByteBuffer</CODE> object
     */
    public ByteBuffer append(ByteBuffer buf) {
        return append(buf.buf, 0, buf.count);
    }

    /**
     * Appends the string representation of an <CODE>int</CODE>.
     *
     * @param i the <CODE>int</CODE> to be appended
     * @return a reference to this <CODE>ByteBuffer</CODE> object
     */
    public ByteBuffer append(int i) {
        return append(String.valueOf(i));
    }

    /**
     * Appends the string representation of a <CODE>long</CODE>.
     *
     * @param l the <CODE>long</CODE> to be appended
     * @return a reference to this <CODE>ByteBuffer</CODE> object
     */
    public ByteBuffer append(long l) {
        return append(String.valueOf(l));
    }

    public ByteBuffer append(byte b) {
        return append_i(b);
    }

    public ByteBuffer appendHex(byte b) {
        append(bytes[(b >> 4) & 0x0f]);
        return append(bytes[b & 0x0f]);
    }

    /**
     * Appends a string representation of a <CODE>float</CODE> according to the Pdf conventions.
     *
     * @param i the <CODE>float</CODE> to be appended
     * @return a reference to this <CODE>ByteBuffer</CODE> object
     */
    public ByteBuffer append(float i) {
        return append((double) i);
    }

    /**
     * Appends a string representation of a <CODE>double</CODE> according to the Pdf conventions.
     *
     * @param d the <CODE>double</CODE> to be appended
     * @return a reference to this <CODE>ByteBuffer</CODE> object
     */
    public ByteBuffer append(double d) {
        append(formatDouble(d, this));
        return this;
    }

    /**
     * Sets the size to zero.
     */
    public void reset() {
        count = 0;
    }

    /**
     * Creates a newly allocated byte array. Its size is the current size of this output stream and the valid contents
     * of the buffer have been copied into it.
     *
     * @return the current contents of this output stream, as a byte array.
     */
    public byte[] toByteArray() {
        byte[] newbuf = new byte[count];
        System.arraycopy(buf, 0, newbuf, 0, count);
        return newbuf;
    }

    /**
     * Returns the current size of the buffer.
     *
     * @return the value of the <code>count</code> field, which is the number of valid bytes in this byte buffer.
     */
    public int size() {
        return count;
    }

    public void setSize(int size) {
        if (size > count || size < 0) {
            throw new IndexOutOfBoundsException(MessageLocalization.getComposedMessage(
                    "the.new.size.must.be.positive.and.lt.eq.of.the.current.size"));
        }
        count = size;
    }

    /**
     * Converts the buffer's contents into a string, translating bytes into characters according to the platform's
     * default character encoding.
     *
     * @return String translated from the buffer's contents.
     */
    public String toString() {
        return new String(buf, 0, count);
    }

    /**
     * Converts the buffer's contents into a string, translating bytes into characters according to the specified
     * character encoding.
     *
     * @param enc a character-encoding name.
     * @return String translated from the buffer's contents.
     * @throws UnsupportedEncodingException If the named encoding is not supported.
     */
    public String toString(String enc) throws UnsupportedEncodingException {
        return new String(buf, 0, count, enc);
    }

    /**
     * Writes the complete contents of this byte buffer output to the specified output stream argument, as if by calling
     * the output stream's write method using <code>out.write(buf, 0, count)</code>.
     *
     * @param out the output stream to which to write the data.
     * @throws IOException if an I/O error occurs.
     */
    public void writeTo(OutputStream out) throws IOException {
        out.write(buf, 0, count);
    }

    public void write(int b) {
        append((byte) b);
    }

    public void write(byte[] b, int off, int len) {
        append(b, off, len);
    }

    public byte[] getBuffer() {
        return buf;
    }
}
