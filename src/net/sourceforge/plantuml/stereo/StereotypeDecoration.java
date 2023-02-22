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

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Parser;
import net.sourceforge.plantuml.klimt.sprite.SpriteUtils;
import net.sourceforge.plantuml.regex.Matcher2;
import net.sourceforge.plantuml.regex.MyPattern;
import net.sourceforge.plantuml.regex.Pattern2;
import net.sourceforge.plantuml.regex.RegexComposed;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.text.Guillemet;

public class StereotypeDecoration {
	private final static RegexComposed circleChar = new RegexConcat( //
			new RegexLeaf("\\<\\<"), //
			RegexLeaf.spaceZeroOrMore(), //
			new RegexLeaf("\\(?"), //
			new RegexLeaf("CHAR", "(\\S)"), //
			RegexLeaf.spaceZeroOrMore(), //
			new RegexLeaf(","), //
			RegexLeaf.spaceZeroOrMore(), //
			new RegexLeaf("COLOR", "(#[0-9a-fA-F]{6}|\\w+)"), //
			RegexLeaf.spaceZeroOrMore(), //
			new RegexOptional(new RegexLeaf("LABEL", "[),](.*?)")), //
			new RegexLeaf("\\>\\>") //
	);

	private final static RegexComposed circleSprite = new RegexConcat( //
			new RegexLeaf("\\<\\<"), //
			RegexLeaf.spaceZeroOrMore(), //
			new RegexLeaf("\\(?\\$"), //
			new RegexLeaf("NAME", "(" + SpriteUtils.SPRITE_NAME + ")"), //
			new RegexLeaf("SCALE", "((?:\\{scale=|\\*)([0-9.]+)\\}?)?"), //
			RegexLeaf.spaceZeroOrMore(), //
			new RegexOptional( //
					new RegexConcat( //
							new RegexLeaf(","), //
							RegexLeaf.spaceZeroOrMore(), //
							new RegexLeaf("COLOR", "(#[0-9a-fA-F]{6}|\\w+)") //
					)), //
			RegexLeaf.spaceZeroOrMore(), //
			new RegexOptional(new RegexLeaf("LABEL", "[),](.*?)")), //
			new RegexLeaf("\\>\\>") //
	);

	public static final String PREFIX = "%";

	final String label;
	final HColor htmlColor;
	final char character;
	final String spriteName;
	final double spriteScale;

	@Override
	public String toString() {
		return "label='" + label + "' spriteName='" + spriteName + "'";
	}

	public List<String> getStyleNames() {
		final List<String> result = new ArrayList<>();
		for (String s : cutLabels(label, Guillemet.NONE))
			result.add(PREFIX + s);
		if (spriteName == null)
			return Collections.unmodifiableList(result);

		final int idx = spriteName.lastIndexOf('/');
		if (idx != -1)
			result.add(PREFIX + spriteName.substring(idx + 1));
		return Collections.unmodifiableList(result);
	}

	private StereotypeDecoration(String label, HColor htmlColor, char character, String spriteName,
			double spriteScale) {
		this.label = label;
		this.htmlColor = htmlColor;
		this.character = character;
		this.spriteName = spriteName;
		this.spriteScale = spriteScale;
	}

	static StereotypeDecoration buildSimple(String name) {
		final String spriteName;
		final double spriteScale;
		if (name.startsWith("<<$") && name.endsWith(">>")) {
			final RegexResult mCircleSprite = StereotypeDecoration.circleSprite.matcher(name);
			spriteName = mCircleSprite.get("NAME", 0);
			spriteScale = Parser.getScale(mCircleSprite.get("SCALE", 0), 1);
		} else {
			spriteName = null;
			spriteScale = 0;
		}
		return new StereotypeDecoration(name, null, '\0', spriteName, spriteScale);
	}

	public static StereotypeDecoration buildComplex(String full, HColorSet htmlColorSet) throws NoSuchColorException {

		String label = "";
		HColor htmlColor = null;
		char character = '\0';
		String spriteName = null;
		double spriteScale = 0;

		final List<String> list = cutLabels(full, Guillemet.DOUBLE_COMPARATOR);
		for (String name : list) {
			final RegexResult mCircleChar = circleChar.matcher(name);
			final RegexResult mCircleSprite = circleSprite.matcher(name);

			if (mCircleSprite != null) {
				if (StringUtils.isNotEmpty(mCircleSprite.get("LABEL", 0)))
					name = "<<" + mCircleSprite.get("LABEL", 0) + ">>";
				else
					name = "";

				final String colName = mCircleSprite.get("COLOR", 0);
				final HColor col = colName == null ? null : htmlColorSet.getColorLEGACY(colName);
				htmlColor = col == null ? HColors.BLACK : col;
				character = '\0';
				spriteName = mCircleSprite.get("NAME", 0);
				spriteScale = Parser.getScale(mCircleSprite.get("SCALE", 0), 1);
			} else if (mCircleChar != null) {
				if (StringUtils.isNotEmpty(mCircleChar.get("LABEL", 0)))
					name = "<<" + mCircleChar.get("LABEL", 0) + ">>";
				else
					name = "";

				final String colName = mCircleChar.get("COLOR", 0);
				htmlColor = colName == null ? null : htmlColorSet.getColorLEGACY(colName);
				character = mCircleChar.get("CHAR", 0).charAt(0);
			}

			label = label + name;
		}

		return new StereotypeDecoration(label, htmlColor, character, spriteName, spriteScale);
	}

	static List<String> cutLabels(final String label, Guillemet guillemet) {
		final List<String> result = new ArrayList<>();
		final Pattern2 p = MyPattern.cmpile("\\<{2,3}.*?\\>{2,3}");
		final Matcher2 m = p.matcher(label);
		while (m.find()) {
			final String group = m.group();
			if (group.startsWith("<<<") == false)
				result.add(guillemet.manageGuillemetStrict(group));
		}
		return Collections.unmodifiableList(result);
	}

}
