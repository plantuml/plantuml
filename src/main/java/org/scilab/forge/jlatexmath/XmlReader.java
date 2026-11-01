/* XmlReader.java
 * =========================================================================
 * This file is NOT part of upstream JLaTeXMath, it was added for PLAN.md
 * (phase 3, step 3d). It reads the XML files of the library without javax.xml (not in the TeaVM class library).
 * It is released under the same terms as the library: GNU GPL version 2 or
 * later, with the GNU linking exception (see the header of the other files of
 * this package).
 * =========================================================================
 */

package org.scilab.forge.jlatexmath;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * A reader for the XML files of the library (font metrics, symbols, macros,
 * glue): elements, attributes, comments, processing instructions, and the
 * references {@code &#NNN;}, {@code &#xHHH;}, {@code &lt;} {@code &gt;}
 * {@code &amp;} {@code &quot;} {@code &apos;} in the values of the attributes.
 * It is not a validating parser: no DTD, no entity declaration, no namespace
 * (a name with a colon is a name), the text between the elements is dropped.
 * The file is in UTF-8, as it is when it has no encoding declaration.
 * <p>
 * Without {@code javax.xml.parsers}, which the TeaVM class library does not have.
 * The tests compare its result with the one of the JDK on every XML file of the
 * library.
 */
public final class XmlReader {

    private final String text;
    private final int length;
    private int pos;

    private XmlReader(String text) {
        this.text = text;
        this.length = text.length();
    }

    /**
     * @return the root element
     * @throws IOException if the stream cannot be read
     * @throws IllegalArgumentException if there is no stream, or the file is not
     *             what this reader knows
     */
    public static XmlElement parse(InputStream in) throws IOException {
        if (in == null) {
            throw new IllegalArgumentException("No XML file");
        }
        return new XmlReader(new String(readAll(in), StandardCharsets.UTF_8)).readDocument();
    }

    public static XmlElement parse(String xml) {
        return new XmlReader(xml).readDocument();
    }

    private static byte[] readAll(InputStream in) throws IOException {
        try {
            byte[] buffer = new byte[1 << 16];
            int size = 0;
            while (true) {
                if (size == buffer.length) {
                    final byte[] bigger = new byte[buffer.length * 2];
                    System.arraycopy(buffer, 0, bigger, 0, size);
                    buffer = bigger;
                }
                final int n = in.read(buffer, size, buffer.length - size);
                if (n < 0) {
                    break;
                }
                size += n;
            }
            final byte[] result = new byte[size];
            System.arraycopy(buffer, 0, result, 0, size);
            return result;
        } finally {
            in.close();
        }
    }

    private XmlElement readDocument() {
        if (pos < length && text.charAt(pos) == '﻿') {
            pos++; // byte order mark
        }
        skipMisc();
        if (pos >= length || text.charAt(pos) != '<') {
            throw error("No root element");
        }
        final XmlElement root = readElement();
        skipMisc();
        if (pos < length) {
            throw error("Content after the root element");
        }
        return root;
    }

    /** Spaces, comments, processing instructions and the DOCTYPE. */
    private void skipMisc() {
        while (pos < length) {
            final char c = text.charAt(pos);
            if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                pos++;
            } else if (text.startsWith("<?", pos)) {
                skipTo("?>");
            } else if (text.startsWith("<!--", pos)) {
                skipTo("-->");
            } else if (text.startsWith("<!DOCTYPE", pos)) {
                skipDoctype();
            } else {
                return;
            }
        }
    }

    private void skipTo(String end) {
        final int i = text.indexOf(end, pos);
        if (i < 0) {
            throw error("Missing " + end);
        }
        pos = i + end.length();
    }

    private void skipDoctype() {
        int depth = 0;
        while (pos < length) {
            final char c = text.charAt(pos++);
            if (c == '[') {
                depth++;
            } else if (c == ']') {
                depth--;
            } else if (c == '>' && depth <= 0) {
                return;
            }
        }
        throw error("Unfinished DOCTYPE");
    }

    /** At a '&lt;' that starts an element. */
    private XmlElement readElement() {
        pos++; // '<'
        final XmlElement element = new XmlElement(readName());
        // attributes
        while (true) {
            skipSpaces();
            if (pos >= length) {
                throw error("Unfinished tag " + element.getTagName());
            }
            final char c = text.charAt(pos);
            if (c == '>') {
                pos++;
                break;
            }
            if (c == '/') {
                if (pos + 1 < length && text.charAt(pos + 1) == '>') {
                    pos += 2;
                    return element;
                }
                throw error("Bad end of tag " + element.getTagName());
            }
            final String name = readName();
            skipSpaces();
            if (pos >= length || text.charAt(pos) != '=') {
                throw error("No value for the attribute " + name);
            }
            pos++;
            skipSpaces();
            element.addAttribute(name, readValue());
        }
        // content
        while (true) {
            final int lt = text.indexOf('<', pos);
            if (lt < 0) {
                throw error("Missing </" + element.getTagName() + ">");
            }
            pos = lt; // the text before is dropped
            if (text.startsWith("</", pos)) {
                pos += 2;
                final String name = readName();
                skipSpaces();
                if (pos >= length || text.charAt(pos) != '>' || name.equals(element.getTagName()) == false) {
                    throw error("</" + name + "> where </" + element.getTagName() + "> is expected");
                }
                pos++;
                return element;
            } else if (text.startsWith("<!--", pos)) {
                skipTo("-->");
            } else if (text.startsWith("<![CDATA[", pos)) {
                skipTo("]]>");
            } else if (text.startsWith("<?", pos)) {
                skipTo("?>");
            } else {
                element.addChild(readElement());
            }
        }
    }

    private void skipSpaces() {
        while (pos < length) {
            final char c = text.charAt(pos);
            if (c != ' ' && c != '\t' && c != '\n' && c != '\r') {
                return;
            }
            pos++;
        }
    }

    private String readName() {
        final int start = pos;
        while (pos < length) {
            final char c = text.charAt(pos);
            if (c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == '=' || c == '>' || c == '/' || c == '<'
                    || c == '"' || c == '\'') {
                break;
            }
            pos++;
        }
        if (pos == start) {
            throw error("A name is expected");
        }
        return text.substring(start, pos);
    }

    private String readValue() {
        if (pos >= length) {
            throw error("A value is expected");
        }
        final char quote = text.charAt(pos);
        if (quote != '"' && quote != '\'') {
            throw error("A quote is expected");
        }
        pos++;
        final StringBuilder sb = new StringBuilder();
        while (true) {
            if (pos >= length) {
                throw error("Unfinished value");
            }
            final char c = text.charAt(pos++);
            if (c == quote) {
                return sb.toString();
            }
            if (c == '<') {
                throw error("'<' in a value");
            } else if (c == '&') {
                readReference(sb);
            } else if (c == '\r') {
                // a line end is one space (\r\n too), as in the attributes of a parsed file
                if (pos < length && text.charAt(pos) == '\n') {
                    pos++;
                }
                sb.append(' ');
            } else if (c == '\n' || c == '\t') {
                sb.append(' ');
            } else {
                sb.append(c);
            }
        }
    }

    /** After a '&amp;'. */
    private void readReference(StringBuilder sb) {
        final int semicolon = text.indexOf(';', pos);
        if (semicolon < 0) {
            throw error("Unfinished reference");
        }
        final String ref = text.substring(pos, semicolon);
        pos = semicolon + 1;
        if (ref.equals("lt")) {
            sb.append('<');
        } else if (ref.equals("gt")) {
            sb.append('>');
        } else if (ref.equals("amp")) {
            sb.append('&');
        } else if (ref.equals("quot")) {
            sb.append('"');
        } else if (ref.equals("apos")) {
            sb.append('\'');
        } else if (ref.startsWith("#")) {
            final int codePoint;
            try {
                if (ref.startsWith("#x") || ref.startsWith("#X")) {
                    codePoint = Integer.parseInt(ref.substring(2), 16);
                } else {
                    codePoint = Integer.parseInt(ref.substring(1));
                }
            } catch (NumberFormatException e) {
                throw error("Bad reference &" + ref + ";");
            }
            if (codePoint < 0 || codePoint > 0x10FFFF) {
                throw error("Bad reference &" + ref + ";");
            }
            if (codePoint >= 0x10000) {
                final int v = codePoint - 0x10000;
                sb.append((char) (0xD800 + (v >> 10)));
                sb.append((char) (0xDC00 + (v & 0x3FF)));
            } else {
                sb.append((char) codePoint);
            }
        } else {
            throw error("Unknown reference &" + ref + ";");
        }
    }

    private IllegalArgumentException error(String message) {
        int line = 1;
        for (int i = 0; i < pos && i < length; i++) {
            if (text.charAt(i) == '\n') {
                line++;
            }
        }
        return new IllegalArgumentException("XML, line " + line + ": " + message);
    }
}
