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

/**
 * A leaf node inside an {@link XmlNode}: text, a comment, a processing
 * instruction, a CDATA section or raw markup. The kind is fixed at
 * construction and selects how the content is written out by {@link XmlWriter}.
 */
class XmlLeaf implements XmlContent {

	enum Kind {
		TEXT, COMMENT, PROCESSING_INSTRUCTION, CDATA, RAW
	}

	private final Kind kind;
	// For PROCESSING_INSTRUCTION, "first" is the target and "second" the data.
	// For every other kind, only "first" is used.
	private final String first;
	private final String second;

	private XmlLeaf(Kind kind, String first, String second) {
		this.kind = kind;
		this.first = first;
		this.second = second;
	}

	static XmlLeaf text(String value) {
		return new XmlLeaf(Kind.TEXT, value, null);
	}

	static XmlLeaf comment(String value) {
		return new XmlLeaf(Kind.COMMENT, value, null);
	}

	static XmlLeaf processingInstruction(String target, String data) {
		return new XmlLeaf(Kind.PROCESSING_INSTRUCTION, target, data);
	}

	static XmlLeaf cdata(String value) {
		return new XmlLeaf(Kind.CDATA, value, null);
	}

	static XmlLeaf raw(String markup) {
		return new XmlLeaf(Kind.RAW, markup, null);
	}

	@Override
	public void writeTo(XmlWriter w) {
		switch (kind) {
		case TEXT:
			w.text(first);
			break;
		case COMMENT:
			w.comment(first);
			break;
		case PROCESSING_INSTRUCTION:
			w.processingInstruction(first, second);
			break;
		case CDATA:
			w.cdata(first);
			break;
		case RAW:
			w.raw(first);
			break;
		}
	}
}
