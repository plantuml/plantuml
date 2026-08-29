/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
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
package net.sourceforge.plantuml.style.parser2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.style.PName;

/**
 * One {@code selector { ... }} block, exactly as written in a .skin file: its selector
 * alternatives (or, for an {@code @media} block, the raw condition text), the properties
 * declared directly inside it, and its nested blocks.
 *
 * This is a literal, unmerged reading of the file: the same selector path can appear
 * several times across a file (a skin can redeclare {@code mindmapDiagram} twice, and a
 * whole {@code @media (prefers-color-scheme:dark)} block redeclares several top-level
 * selectors already used above it). Merging those occurrences, expanding comma lists into
 * individual paths, and turning {@code @media} into a cascade are all left to the
 * in-memory style model built from this tree, not done here.
 */
public final class RawStyleRule {

	private final List<RawSelector> selectors;
	private final boolean star;
	private final String mediaCondition;
	private final Map<PName, String> properties = new LinkedHashMap<PName, String>();
	private final List<RawStyleRule> children = new ArrayList<RawStyleRule>();

	private RawStyleRule(List<RawSelector> selectors, boolean star, String mediaCondition) {
		this.selectors = selectors;
		this.star = star;
		this.mediaCondition = mediaCondition;
	}

	public static RawStyleRule forSelectors(List<RawSelector> selectors, boolean star) {
		return new RawStyleRule(selectors, star, null);
	}

	/** {@code rawCondition} is the at-rule header text, e.g. {@code "media (prefers-color-scheme:dark)"}. */
	public static RawStyleRule forMedia(String rawCondition) {
		return new RawStyleRule(Collections.<RawSelector>emptyList(), false, rawCondition);
	}

	public boolean isMediaBlock() {
		return mediaCondition != null;
	}

	/** Null for a regular selector block; see {@link #isMediaBlock()}. */
	public String getMediaCondition() {
		return mediaCondition;
	}

	/** Empty for an {@code @media} block; see {@link #isMediaBlock()}. */
	public List<RawSelector> getSelectors() {
		return selectors;
	}

	/** Whether the selector list was followed by a trailing {@code *}. */
	public boolean isStar() {
		return star;
	}

	public void putProperty(PName name, String value) {
		properties.put(name, value);
	}

	/** Only the properties declared directly in this block, in file order. */
	public Map<PName, String> getProperties() {
		return properties;
	}

	public void addChild(RawStyleRule child) {
		children.add(child);
	}

	public List<RawStyleRule> getChildren() {
		return children;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		print(sb, 0);
		return sb.toString();
	}

	private void print(StringBuilder sb, int indent) {
		indent(sb, indent);
		if (isMediaBlock())
			sb.append('@').append(mediaCondition);
		else {
			sb.append(selectors);
			if (star)
				sb.append(" *");
		}
		sb.append(" {\n");
		for (Map.Entry<PName, String> ent : properties.entrySet()) {
			indent(sb, indent + 1);
			sb.append(ent.getKey()).append(" = ").append(ent.getValue()).append('\n');
		}
		for (RawStyleRule child : children)
			child.print(sb, indent + 1);
		indent(sb, indent);
		sb.append("}\n");
	}

	private static void indent(StringBuilder sb, int level) {
		for (int i = 0; i < level; i++)
			sb.append("  ");
	}

}
