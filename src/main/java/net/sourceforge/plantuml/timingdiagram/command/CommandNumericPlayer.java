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
 * Contribution: The-Lum
 *
 */
package net.sourceforge.plantuml.timingdiagram.command;

import java.util.Arrays;
import java.util.Collection;

import net.sourceforge.plantuml.annotation.Explain;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.timingdiagram.AbstractNumericPlayer;
import net.sourceforge.plantuml.timingdiagram.TimingDiagram;
import net.sourceforge.plantuml.utils.LineLocation;

/**
 * Declares a player whose states are numbers. The three accepted forms are:
 *
 * <pre>
 * analog "Display" as CODE [between 0 and 3]
 * analog "Display" between 0 and 3 as CODE
 * analog CODE [between 0 and 3]
 * </pre>
 *
 * Each of them may start with <code>compact</code> and carry a stereotype.
 * Subclasses provide the keyword and create the actual player.
 */
public abstract class CommandNumericPlayer extends SingleLineCommand2<TimingDiagram> {

	private final String keyword;
	private final Collection<String> firstTokens;

	protected CommandNumericPlayer(String keyword) {
		super(getRegexConcat(keyword));
		this.keyword = keyword;
		this.firstTokens = Arrays.asList("compact", keyword);
	}

	protected abstract AbstractNumericPlayer createPlayer(TimingDiagram diagram, String code, String full,
			boolean compact, Stereotype stereotype);

	private static IRegex getRegexConcat(String keyword) {
		return RegexConcat.build(CommandNumericPlayer.class.getName() + keyword, RegexLeaf.start(), //
				new RegexOptional( //
						new RegexConcat( //
								new RegexLeaf(1, "COMPACT", "(compact)"), //
								RegexLeaf.spaceOneOrMore())), //
				new RegexLeaf(keyword), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexOr(quotedDisplay(), codeOnly()), //
				RegexLeaf.end());
	}

	// "Display" ...
	private static IRegex quotedDisplay() {
		return new RegexConcat( //
				new RegexLeaf(1, "FULL", "[%g]([^%g]+)[%g]"), //
				StereotypePattern.optional("STEREOTYPE1"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexOr(asCodeThenBounds(), boundsThenAsCode()));
	}

	// ... as CODE [between 0 and 3]
	private static IRegex asCodeThenBounds() {
		return new RegexConcat( //
				asCode("CODE1"), //
				StereotypePattern.optional("STEREOTYPE2"), //
				optionalBounds(1), //
				StereotypePattern.optional("STEREOTYPE3"));
	}

	// ... between 0 and 3 as CODE
	private static IRegex boundsThenAsCode() {
		return new RegexConcat( //
				bounds(2), //
				RegexLeaf.spaceOneOrMore(), //
				asCode("CODE2"), //
				StereotypePattern.optional("STEREOTYPE4"));
	}

	// CODE [between 0 and 3]
	private static IRegex codeOnly() {
		return new RegexConcat( //
				code("CODE3"), //
				StereotypePattern.optional("STEREOTYPE5"), //
				optionalBounds(3), //
				StereotypePattern.optional("STEREOTYPE6"));
	}

	private static IRegex asCode(String name) {
		return new RegexConcat( //
				new RegexLeaf("as"), //
				RegexLeaf.spaceOneOrMore(), //
				code(name));
	}

	private static IRegex code(String name) {
		return new RegexLeaf(1, name, "([%pLN_.@]+)");
	}

	private static IRegex optionalBounds(int index) {
		return new RegexOptional( //
				new RegexConcat( //
						RegexLeaf.spaceOneOrMore(), //
						bounds(index)));
	}

	private static IRegex bounds(int index) {
		return new RegexConcat( //
				new RegexOr(new RegexLeaf("between"), new RegexLeaf("from")), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(1, "START" + index, "(-?[0-9]*\\.?[0-9]+)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexOr(new RegexLeaf("and"), new RegexLeaf("to")), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(1, "END" + index, "(-?[0-9]*\\.?[0-9]+)"));
	}

	@Override
	@Explain
	protected String explainArg(LineLocation location, RegexResult arg) {
		final String full = arg.get("FULL", 0);
		final String stereotype = arg.getLazzy("STEREOTYPE", 0);
		final String start = arg.getLazzy("START", 0);
		final String end = arg.getLazzy("END", 0);

		final StringBuilder sb = new StringBuilder();
		if (arg.get("COMPACT", 0) != null)
			sb.append("Creating a compact ");
		else
			sb.append("Creating an ");
		sb.append(keyword).append(" timing player '").append(arg.getLazzy("CODE", 0)).append("'");
		if (full != null)
			sb.append(" displayed as \"").append(full).append("\"");
		if (stereotype != null)
			sb.append(" stereotyped ").append(stereotype);
		// Bounds are only applied when both START and END are present
		if (start != null && end != null)
			sb.append(" with bounds from ").append(start).append(" to ").append(end);
		return sb.toString();
	}

	@Override
	final protected CommandExecutionResult executeArg(TimingDiagram diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) {
		final String code = arg.getLazzy("CODE", 0);
		final String full = arg.get("FULL", 0);
		final String stereotype = arg.getLazzy("STEREOTYPE", 0);

		final AbstractNumericPlayer player = createPlayer(diagram, code, full == null ? "" : full,
				arg.get("COMPACT", 0) != null, stereotype == null ? null : Stereotype.build(stereotype));

		final String start = arg.getLazzy("START", 0);
		final String end = arg.getLazzy("END", 0);
		if (start != null && end != null)
			player.setBounds(start, end);

		return CommandExecutionResult.ok();
	}

	@Override
	public final Collection<String> mandatoryFirstTokensFast() {
		return firstTokens;
	}

}
