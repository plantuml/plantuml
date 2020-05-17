/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
 * Contribution :  Hisashi Miyashita
 *
 */
package net.sourceforge.plantuml.descdiagram.command;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.LinkArrow;

public class Labels {

	private String firstLabel;
	private String secondLabel;
	private final StringWithArrow stringWithArrow;

	public Labels(RegexResult arg) {
		this.firstLabel = arg.get("FIRST_LABEL", 0);
		this.secondLabel = arg.get("SECOND_LABEL", 0);
		String labelLink = arg.get("LABEL_LINK", 0);

		if (labelLink != null) {
			labelLink = init(labelLink);
		}

		this.stringWithArrow = new StringWithArrow(labelLink);

	}

	private String init(String labelLink) {
		if (firstLabel == null && secondLabel == null) {
			final Pattern2 p1 = MyPattern.cmpile("^[%g]([^%g]+)[%g]([^%g]+)[%g]([^%g]+)[%g]$");
			final Matcher2 m1 = p1.matcher(labelLink);
			if (m1.matches()) {
				firstLabel = m1.group(1);
				secondLabel = m1.group(3);
				return StringUtils
						.trin(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(StringUtils.trin(m1.group(2))));
			}
			final Pattern2 p2 = MyPattern.cmpile("^[%g]([^%g]+)[%g]([^%g]+)$");
			final Matcher2 m2 = p2.matcher(labelLink);
			if (m2.matches()) {
				firstLabel = m2.group(1);
				secondLabel = null;
				return StringUtils
						.trin(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(StringUtils.trin(m2.group(2))));
			}
			final Pattern2 p3 = MyPattern.cmpile("^([^%g]+)[%g]([^%g]+)[%g]$");
			final Matcher2 m3 = p3.matcher(labelLink);
			if (m3.matches()) {
				firstLabel = null;
				secondLabel = m3.group(2);
				return StringUtils
						.trin(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(StringUtils.trin(m3.group(1))));
			}
		}
		return StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(labelLink, "\"");
	}

	public final String getFirstLabel() {
		return firstLabel;
	}

	public final String getSecondLabel() {
		return secondLabel;
	}

	public final String getLabelLink() {
		return stringWithArrow.getLabel();
	}

	public final LinkArrow getLinkArrow() {
		return stringWithArrow.getLinkArrow();
	}

	public Display getDisplay() {
		return stringWithArrow.getDisplay();
	}

}
