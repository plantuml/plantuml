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
package net.sourceforge.plantuml.sequencediagram;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.StringUtils;

/**
 * Ordered hide/show rules for the stereotype portion of a sequence diagram.
 * A later rule overrides an earlier one for the labels it matches, which is
 * how {@code hide stereotype} followed by {@code show <<name>> stereotype}
 * keeps a single stereotype visible.
 */
final class StereotypeVisibility {

	private static final class Rule {
		private final String gender;
		private final boolean show;

		private Rule(String gender, boolean show) {
			this.gender = gender;
			this.show = show;
		}
	}

	private final List<Rule> rules = new ArrayList<>();

	void add(String gender, boolean show) {
		if (gender != null && gender.startsWith("<<") == false)
			gender = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(gender);
		rules.add(new Rule(gender, show));
	}

	boolean isLabelShown(String label, String participantCode, String participantName) {
		boolean shown = true;
		for (Rule rule : rules)
			if (applies(rule, label, participantCode, participantName))
				shown = rule.show;
		return shown;
	}

	private static boolean applies(Rule rule, String label, String participantCode, String participantName) {
		if (rule.gender == null)
			return true;
		// <<name>> selects that stereotype label on every participant.
		if (rule.gender.startsWith("<<"))
			return rule.gender.equals(label);
		return rule.gender.equals(participantCode) || rule.gender.equals(participantName);
	}

}
