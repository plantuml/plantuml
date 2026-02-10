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
package net.sourceforge.plantuml.stereo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.activitydiagram3.ftile.BoxStyle;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;

public class Stereogroup {

	private static final String KEY = "STEREOGROUP";

	private String definition;

	private static final Pattern pattern = Pattern.compile("<<([^<>]+)>>");

	public static IRegex optionalStereogroup() {
		final String regex = "(<<[^<>]+>>(?:[%s]*<<[^<>]+>>)*)";
		return new RegexOptional(new RegexLeaf(1, KEY, regex));
	}

	public static Stereogroup build(RegexResult arg) {
		final String full = arg.get(Stereogroup.KEY, 0);
		return new Stereogroup(full);
	}

	public static Stereogroup build(String full) {
		return new Stereogroup(full);
	}

	private Stereogroup(String definition) {
		this.definition = definition;
	}

	public Stereotype buildStereotype() {
		if (definition == null)
			return null;
		return Stereotype.build(definition);
	}

	public BoxStyle getBoxStyle() {
		for (String label : getLabels()) {
			final BoxStyle tmp = BoxStyle.fromString(label);
			if (tmp != BoxStyle.PLAIN)
				return tmp;

		}
		return BoxStyle.PLAIN;
	}

	public List<String> getLabels() {
		if (definition == null)
			return Collections.emptyList();

		final List<String> result = new ArrayList<>();
		final Matcher matcher = pattern.matcher(definition);
		while (matcher.find())
			result.add(matcher.group(1).trim());

		return Collections.unmodifiableList(result);
	}

	public Colors getColors(HColorSet colorSet) throws NoSuchColorException {
		for (String label : getLabels())
			if (label.startsWith("#"))
				return new Colors(label, colorSet, ColorType.BACK);

		return Colors.empty();
	}

}
