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

import java.util.List;
import java.util.Map;

/**
 * The literal result of parsing one .skin file: the CSS variables declared with
 * {@code --name: value;} (keyed without the leading {@code --}), and the top-level
 * {@code selector { ... }} / {@code @media { ... }} blocks, in the order they appear in
 * the file.
 *
 * Every property value already has its {@code var(--name)} references substituted, since
 * that is a purely textual step. Turning this tree into a queryable, cascaded style model
 * -- merging duplicate selectors, expanding comma lists, applying {@code @media} as an
 * override layer -- is done in a later pass, not here.
 */
public final class RawStyleSheet {

	private final Map<String, String> variables;
	private final List<RawStyleRule> rules;

	RawStyleSheet(Map<String, String> variables, List<RawStyleRule> rules) {
		this.variables = variables;
		this.rules = rules;
	}

	/** CSS custom properties declared anywhere in the file (e.g. {@code common-background}). */
	public Map<String, String> getVariables() {
		return variables;
	}

	/** The top-level blocks, in file order. */
	public List<RawStyleRule> getRules() {
		return rules;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("variables=").append(variables).append('\n');
		for (RawStyleRule rule : rules)
			sb.append(rule);
		return sb.toString();
	}

}
