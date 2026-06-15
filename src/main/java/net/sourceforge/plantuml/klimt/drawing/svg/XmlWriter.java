/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 *
 * If you like this project or if you find it useful, you can support us at:
 *
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 *
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 *
 *
 */
package net.sourceforge.plantuml.klimt.drawing.svg;

import java.util.Arrays;

/**
 * A minimal, dependency-free streaming XML/SVG writer.
 * <p>
 * Designed to run under TeaVM (no DOM, no {@code javax.xml}). It keeps a stack
 * of currently open element names so it can auto-close them and produce
 * self-closing tags ({@code <g/>}) when an element has no children.
 * <p>
 * The API is fluent: calls can be chained. Element text and attribute values
 * are XML-escaped, with the minimal escaping required for each context (text
 * content only needs {@code &} and {@code <}; attribute values additionally
 * need {@code "}).
 */
public class XmlWriter {

	private final StringBuilder out = new StringBuilder();
	private final int indentSpaces;

	// Lightweight stack of open tag names. A plain array avoids the autoboxing
	// and per-push allocation of Stack/ArrayDeque, which matters when emitting
	// large diagrams under TeaVM.
	private String[] openTags = new String[16];
	private int depth = 0;

	// True once startElement() has written "<name" but not yet the closing ">"
	// of the start tag, so the next call can decide between attributes, a child,
	// inline text, or a self-closing "/>".
	private boolean isPendingClose = false;

	// True when the current element's content is inline text (no child element),
	// so endElement() does not indent the closing tag onto its own line.
	private boolean hasInlineContent = false;

	public XmlWriter(int indentSpaces) {
		this.indentSpaces = indentSpaces;
	}

	public XmlWriter startElement(String name) {
		closePendingStartTag(true);
		indent(depth);
		out.append('<').append(name);
		push(name);
		isPendingClose = true;
		hasInlineContent = false;
		return this;
	}

	public XmlWriter attribute(String name, String value) {
		if (!isPendingClose)
			throw new IllegalStateException("Cannot add an attribute outside of an open start tag.");

		out.append(' ').append(name).append("=\"");
		escapeAttribute(value);
		out.append('"');
		return this;
	}

	public XmlWriter text(String value) {
		closePendingStartTag(false);
		hasInlineContent = true;
		escapeText(value);
		return this;
	}

	public XmlWriter endElement() {
		if (depth == 0)
			throw new IllegalStateException("No element to close.");

		final String name = openTags[--depth];
		if (isPendingClose) {
			// Empty element: collapse to a self-closing tag, e.g. <g/>.
			out.append("/>");
			newline();
			isPendingClose = false;
		} else {
			if (!hasInlineContent)
				indent(depth);
			out.append("</").append(name).append('>');
			newline();
		}
		hasInlineContent = false;
		return this;
	}

	public String getXml() {
		if (depth != 0)
			throw new IllegalStateException("Some elements were left open: " + Arrays.toString(Arrays.copyOf(openTags, depth)));

		return out.toString();
	}

	// Closes a dangling start tag. When the next thing is a child element or the
	// end of an empty element, openingChild is true and we may need a newline; for
	// inline text we just emit ">" with no extra whitespace.
	private void closePendingStartTag(boolean openingChild) {
		if (!isPendingClose)
			return;

		out.append('>');
		if (openingChild)
			newline();
		isPendingClose = false;
	}

	private void push(String name) {
		if (depth == openTags.length)
			openTags = Arrays.copyOf(openTags, openTags.length * 2);
		openTags[depth++] = name;
	}

	private void newline() {
		if (indentSpaces > 0)
			out.append('\n');
	}

	private void indent(int level) {
		if (indentSpaces <= 0)
			return;
		for (int i = 0, n = level * indentSpaces; i < n; i++)
			out.append(' ');
	}

	// Text content: only '&' and '<' are mandatory. '>' is escaped only as part
	// of the "]]>" sequence in real XML, which cannot occur here, so we leave it.
	private void escapeText(String input) {
		if (input == null)
			return;
		for (int i = 0; i < input.length(); i++) {
			final char c = input.charAt(i);
			switch (c) {
			case '&':
				out.append("&amp;");
				break;
			case '<':
				out.append("&lt;");
				break;
			default:
				out.append(c);
			}
		}
	}

	// Attribute value (always double-quoted): escape '&', '<' and '"'.
	private void escapeAttribute(String input) {
		if (input == null)
			return;
		for (int i = 0; i < input.length(); i++) {
			final char c = input.charAt(i);
			switch (c) {
			case '&':
				out.append("&amp;");
				break;
			case '<':
				out.append("&lt;");
				break;
			case '"':
				out.append("&quot;");
				break;
			default:
				out.append(c);
			}
		}
	}
}
