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
 * A minimal, dependency-free document, replicating just the subset of
 * {@code org.w3c.dom.Document} that the SVG generation code relies on: a
 * factory for {@link XmlNode} elements plus a single root.
 * <p>
 * It is the TeaVM-friendly counterpart of the {@code org.w3c.dom.Document} used
 * by {@code SvgGraphics}, designed so that the dependency-free SVG generator can
 * be a near-mechanical port where {@code Document}/{@code Element} become
 * {@code XmlDocument}/{@code XmlNode}.
 */
public class XmlDocument extends PortableSvgDocument {

	private XmlNode root;

	public XmlNode createElement(String name) {
		return new XmlNode(name);
	}

	/** Sets the root element; subsequent serialization starts from it. */
	public void setRoot(XmlNode root) {
		this.root = root;
	}

	public XmlNode getRoot() {
		return root;
	}

	/** Serializes the whole document, starting at the root element. */
	public String toXml(int indentSpaces) {
		if (root == null)
			throw new IllegalStateException("No root element set.");

		final XmlWriter w = new XmlWriter(indentSpaces);
		root.writeTo(w);
		return w.getXml();
	}
}
