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

import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.activitydiagram3.ftile.BoxStyle;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.parser.StyleParser;
import net.sourceforge.plantuml.style.parser.StyleParsingException;

public class Stereogroup {

	private static final String KEY = "STEREOGROUP";

	public final static Stereogroup NONE = new Stereogroup(null);

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

	public boolean isEmpty() {
		return definition == null;
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

	public LeafType getLeafType() {
		final List<String> labels = getLabels();
		if (labels.size() == 0)
			return null;

		switch (labels.get(0).toLowerCase()) {
		case "choice":
			return LeafType.STATE_CHOICE;
		case "fork":
		case "join":
			return LeafType.STATE_FORK_JOIN;
		case "start":
			return LeafType.CIRCLE_START;
		case "end":
			return LeafType.CIRCLE_END;
		case "history":
			return LeafType.PSEUDO_STATE;
		case "history*":
			return LeafType.DEEP_HISTORY;
		default:
			return null;
		}
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

	public Style mute(Style style, HColorSet colorSet) throws NoSuchColorException {
		return style.eventuallyOverride(getInnerColors(colorSet));
	}

	public HColor getHColor(StyleSignature styleSignature, PName pname, StyleBuilder styleBuilder, HColorSet colorSet)
			throws NoSuchColorException {
		final Style style = styleSignature.withTOBECHANGED(this).getMergedStyle(styleBuilder);
		final Colors colors = getInnerColors(colorSet);
		return colors.getColor(style, pname, colorSet);
	}

	public Colors getInnerColors(HColorSet colorSet) throws NoSuchColorException {
		Colors colors = Colors.empty();
		for (String label : getLabels()) {
			if (label.startsWith("###")) {
				label = label.substring(3);
				final HColor textColor = colorSet.getColorOrNull(label);
				if (textColor != null)
					colors = colors.add(ColorType.TEXT, textColor);

			} else if (label.startsWith("##")) {
				label = label.substring(2);

				if (label.charAt(0) == '[') {
					int x = label.indexOf(']');
					if (x != -1) {
						colors = colors.addLegacyStroke(label.substring(1, x));
						label = label.substring(x + 1);
					} else
						label = "";
				}

				if (label.length() > 0) {
					final HColor lineColor = colorSet.getColorOrNull(label);
					if (lineColor != null)
						colors = colors.add(ColorType.LINE, lineColor);
				}

			} else if (label.startsWith("#")) {
				colors = colors.mergeWith(new Colors(label, colorSet, ColorType.BACK));
			} else if (label.contains(":") && label.contains(";")) {
				try {
					final Style style = new StyleParser().parseSingleLine(label);
					colors = colors.applyStyle(style, colorSet);
				} catch (StyleParsingException e) {
					// Ignore for now...
				}
			}
		}
		return colors;
	}

}
