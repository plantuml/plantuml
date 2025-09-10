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
 * Contribution :  Hisashi Miyashita
 * 
 *
 */
package net.sourceforge.plantuml.decoration;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cli.GlobalConfig;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactory;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryArrow;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryArrowAndCircle;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryCircle;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryCircleConnect;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryCircleCross;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryCircleCrowfoot;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryCircleLine;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryCrowfoot;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryDiamond;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryDoubleLine;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryExtendsLike;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryHalfArrow;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryLineCrowfoot;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryNotNavigable;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryParenthesis;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryPlus;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactorySquare;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryTriangle;

public enum LinkDecor {

	NONE(null, null, 2, false, 0), //
	EXTENDS(decors1("<|", "^"), decors2("|>", "^"), 30, false, 2), //
	COMPOSITION(decors1("*"), decors2("*"), 15, true, 1.3), //
	AGREGATION(decors1("o"), decors2("o"), 15, false, 1.3), //
	NOT_NAVIGABLE(decors1("x"), decors2("x"), 1, false, 0.5), //

	REDEFINES(decors1("<||"), decors2("||>"), 30, false, 2), //
	DEFINEDBY(decors1("<|:"), decors2(":|>"), 30, false, 2), //

	CROWFOOT(decors1("}"), decors2("{"), 10, true, 0.8), //
	CIRCLE_CROWFOOT(decors1("}o"), decors2("o{"), 14, false, 0.8), //
	CIRCLE_LINE(decors1("|o"), decors2("o|"), 10, false, 0.8), //
	DOUBLE_LINE(decors1("||"), decors2("||"), 7, false, 0.7), //
	LINE_CROWFOOT(decors1("}|"), decors2("|{"), 10, false, 0.8), //

	ARROW(decors1("<", "<_"), decors2(">", "_>"), 10, true, 0.5), //
	ARROW_TRIANGLE(decors1("<<"), decors2(">>"), 10, true, 0.8), //
	ARROW_AND_CIRCLE(null, null, 10, false, 0.5), //

	CIRCLE(decors1("0"), decors2("0"), 0, false, 0.5), //
	CIRCLE_FILL(decors1("@"), decors2("@"), 0, false, 0.5), //
	CIRCLE_CONNECT(decors1("0)"), decors2("(0"), 0, false, 0.5), //
	PARENTHESIS(decors1(")"), decors2("("), 0, false, GlobalConfig.USE_INTERFACE_EYE2 ? 0.5 : 1.0), //
	SQUARE(decors1("#"), decors2("#"), 0, false, 0.5), //

	CIRCLE_CROSS(null, null, 0, false, 0.5), //
	PLUS(decors1("+"), decors2("+"), 0, false, 1.5), //
	HALF_ARROW_UP(null, decors2("\\\\"), 0, false, 1.5), //
	HALF_ARROW_DOWN(null, decors2("//"), 0, false, 1.5), //
	SQUARE_toberemoved(null, null, 30, false, 0);

	private final double arrowSize;
	private final int margin;
	private final boolean fill;
	private final String[] decors1;
	private final String[] decors2;

	private final static Map<String, LinkDecor> DECORS1 = new HashMap<>();
	private final static Map<String, LinkDecor> DECORS2 = new HashMap<>();

	/**
	 * Helper method to associate one or more string patterns ("decorators") with
	 * the "left" or "start" side of a link extremity. Used for improved readability
	 * in enum construction, allowing clear distinction between left and right
	 * decorations.
	 */
	private static String[] decors1(String... tmp) {
		return tmp;
	}

	/**
	 * Helper method to associate one or more string patterns ("decorators") with
	 * the "right" or "end" side of a link extremity. Symmetrical to
	 * {@link #decors1}, this improves clarity when specifying both sides in enum
	 * entries and makes maintenance easier.
	 */
	private static String[] decors2(String... tmp) {
		return tmp;
	}

	private LinkDecor(String[] decors1, String[] decors2, int margin, boolean fill, double arrowSize) {
		this.margin = margin;
		this.fill = fill;
		this.arrowSize = arrowSize;
		this.decors1 = decors1;
		this.decors2 = decors2;

	}

	static {
		for (LinkDecor decor : values()) {
			if (decor.decors1 != null)
				for (String s : decor.decors1)
					DECORS1.put(s, decor);

			if (decor.decors2 != null)
				for (String s : decor.decors2)
					DECORS2.put(s, decor);
		}
	}

	public int getMargin() {
		return margin;
	}

	public boolean isFill() {
		return fill;
	}

	public double getArrowSize() {
		return arrowSize;
	}

	public boolean isExtendsLike() {
		return this == EXTENDS || this == REDEFINES || this == DEFINEDBY;
	}

	public ExtremityFactory getExtremityFactoryComplete(HColor backgroundColor) {
		if (this == EXTENDS)
			return new ExtremityFactoryTriangle(null, 18, 6, 18);

		return getExtremityFactoryLegacy(backgroundColor);
	}

	public ExtremityFactory getExtremityFactoryLegacy(HColor backgroundColor) {
		switch (this) {
		case PLUS:
			return new ExtremityFactoryPlus(backgroundColor);
		case REDEFINES:
			return new ExtremityFactoryExtendsLike(backgroundColor, false);
		case DEFINEDBY:
			return new ExtremityFactoryExtendsLike(backgroundColor, true);
		case HALF_ARROW_UP:
			return new ExtremityFactoryHalfArrow(1);
		case HALF_ARROW_DOWN:
			return new ExtremityFactoryHalfArrow(-1);
		case ARROW_TRIANGLE:
			return new ExtremityFactoryTriangle(null, 8, 3, 8);
		case CROWFOOT:
			return new ExtremityFactoryCrowfoot();
		case CIRCLE_CROWFOOT:
			return new ExtremityFactoryCircleCrowfoot();
		case LINE_CROWFOOT:
			return new ExtremityFactoryLineCrowfoot();
		case CIRCLE_LINE:
			return new ExtremityFactoryCircleLine();
		case DOUBLE_LINE:
			return new ExtremityFactoryDoubleLine();
		case CIRCLE_CROSS:
			return new ExtremityFactoryCircleCross(backgroundColor);
		case ARROW:
			return new ExtremityFactoryArrow();
		case ARROW_AND_CIRCLE:
			return new ExtremityFactoryArrowAndCircle(backgroundColor);
		case NOT_NAVIGABLE:
			return new ExtremityFactoryNotNavigable();
		case AGREGATION:
			return new ExtremityFactoryDiamond(false);
		case COMPOSITION:
			return new ExtremityFactoryDiamond(true);
		case CIRCLE:
			return new ExtremityFactoryCircle(false, backgroundColor);
		case CIRCLE_FILL:
			return new ExtremityFactoryCircle(true, backgroundColor);
		case SQUARE:
			return new ExtremityFactorySquare(backgroundColor);
		case PARENTHESIS:
			return new ExtremityFactoryParenthesis();
		case CIRCLE_CONNECT:
			return new ExtremityFactoryCircleConnect(backgroundColor);
		default:
			return null;
		}
	}

	public static LinkDecor lookupDecors1(String s) {
		if (s == null)
			return LinkDecor.NONE;
		return DECORS1.getOrDefault(StringUtils.trin(s), LinkDecor.NONE);
	}

	public static LinkDecor lookupDecors2(String s) {
		if (s == null)
			return LinkDecor.NONE;
		return DECORS2.getOrDefault(StringUtils.trin(s), LinkDecor.NONE);
	}

	public static String getRegexDecors1() {
		return buildRegexFromDecorKeys(DECORS1.keySet());
	}

	public static String getRegexDecors2() {
		return buildRegexFromDecorKeys(DECORS2.keySet());
	}

	private static String buildRegexFromDecorKeys(Set<String> keys) {
		// Sort by descending length to prevent prefix conflicts (e.g., "||" before "|")
		// Use Pattern.quote to escape any special regex characters
		// Join all keys with "|" to build the final regular expression
		return keys.stream().sorted(Comparator.<String>comparingInt(String::length).reversed()).map(key -> {
			final String quoted = Pattern.quote(key);
			final boolean startsWithO = key.startsWith("o");
			final boolean endsWithO = key.endsWith("o");
			if (startsWithO && endsWithO)
				return "\\b" + quoted + "\\b";
			if (startsWithO)
				return "\\b" + quoted;
			if (endsWithO)
				return quoted + "\\b";

			return quoted;
		}).collect(Collectors.joining("|", "(", ")?"));
	}

}