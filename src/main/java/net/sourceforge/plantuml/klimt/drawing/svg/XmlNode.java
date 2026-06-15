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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A minimal, dependency-free DOM element, replicating just the subset of
 * {@code org.w3c.dom.Element} that the SVG generation code relies on.
 * <p>
 * It exists so that an SVG document tree can be built and serialized under
 * TeaVM, where {@code javax.xml} / {@code org.w3c.dom} are not available. The
 * tree is built eagerly (like a DOM) so that attributes set late — once the
 * final canvas size is known — are honored, then serialized to text in one pass
 * via {@link XmlWriter}.
 * <p>
 * Attribute order is preserved (insertion order) to keep the output
 * deterministic.
 */
public class XmlNode {

	// A child of an element: either a nested element or a leaf (text, comment,
	// processing instruction, CDATA section or raw markup).
	private interface Child {
		void writeTo(XmlWriter w);
	}

	private final String tagName;
	private final Map<String, String> attributes = new LinkedHashMap<>();
	private final List<Child> children = new ArrayList<>();

	XmlNode(String tagName) {
		this.tagName = tagName;
	}

	public String getTagName() {
		return tagName;
	}

	public void setAttribute(String name, String value) {
		attributes.put(name, value);
	}

	public void appendChild(XmlNode child) {
		children.add(child);
	}

	/**
	 * Replaces all current content with a single text node, mirroring
	 * {@code org.w3c.dom.Node.setTextContent(String)}.
	 */
	public void setTextContent(String value) {
		children.clear();
		appendText(value);
	}

	public void appendText(String value) {
		children.add(w -> w.text(value));
	}

	public void appendComment(String value) {
		children.add(w -> w.comment(value));
	}

	public void appendProcessingInstruction(String target, String data) {
		children.add(w -> w.processingInstruction(target, data));
	}

	public void appendCData(String value) {
		children.add(w -> w.cdata(value));
	}

	/**
	 * Appends already-serialized markup, spliced verbatim (no escaping). Used for
	 * inlined SVG images.
	 */
	public void appendRaw(String markup) {
		children.add(w -> w.raw(markup));
	}

	/**
	 * Returns the first child if this element has any content, else {@code null}.
	 * Mirrors {@code org.w3c.dom.Node.getFirstChild()}, used only to test whether
	 * an element is empty (so empty groups/links can be dropped).
	 */
	public Object getFirstChild() {
		return children.isEmpty() ? null : children.get(0);
	}

	/** Serializes this element and its subtree into the given writer. */
	public void writeTo(XmlWriter w) {
		w.startElement(tagName);
		for (Map.Entry<String, String> ent : attributes.entrySet())
			w.attribute(ent.getKey(), ent.getValue());
		for (Child child : children)
			child.writeTo(w);
		w.endElement();
	}

	/** Serializes this element as a standalone document fragment. */
	public String toXml(int indentSpaces) {
		final XmlWriter w = new XmlWriter(indentSpaces);
		writeTo(w);
		return w.getXml();
	}
}
