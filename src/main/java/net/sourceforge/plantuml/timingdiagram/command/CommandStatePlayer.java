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
 */
package net.sourceforge.plantuml.timingdiagram.command;

import java.util.Arrays;
import java.util.Collection;

import net.sourceforge.plantuml.annotation.Explain;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.timingdiagram.TimingDiagram;
import net.sourceforge.plantuml.utils.LineLocation;

/**
 * Declares a player whose states are labels:
 *
 * <pre>
 * [compact] robust ["Display" as] CODE [#color]
 * </pre>
 *
 * Subclasses provide the keyword and create the actual player.
 */
public abstract class CommandStatePlayer extends SingleLineCommand2<TimingDiagram> {

	private final String keyword;
	private final Collection<String> firstTokens;

	protected CommandStatePlayer(String keyword) {
		super(getRegexConcat(keyword));
		this.keyword = keyword;
		this.firstTokens = Arrays.asList("compact", keyword);
	}

	protected abstract void createPlayer(TimingDiagram diagram, String code, String full, boolean compact,
			Stereotype stereotype, HColor backColor);

	private static IRegex getRegexConcat(String keyword) {
		return RegexConcat.build(CommandStatePlayer.class.getName() + keyword, RegexLeaf.start(), //
				new RegexOptional( //
						new RegexConcat( //
								new RegexLeaf(1, "COMPACT", "(compact)"), //
								RegexLeaf.spaceOneOrMore())), //
				new RegexLeaf(keyword), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexOptional( //
						new RegexConcat( //
								new RegexLeaf(1, "FULL", "[%g]([^%g]+)[%g]"), //
								StereotypePattern.optional("STEREOTYPE"), //
								new RegexLeaf("as"), //
								RegexLeaf.spaceOneOrMore())), //
				new RegexLeaf(1, "CODE", "([%pLN_.@]+)"), //
				StereotypePattern.optional("STEREOTYPE2"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOr(color().getRegex()), //
				RegexLeaf.end());
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	@Override
	@Explain
	protected String explainArg(LineLocation location, RegexResult arg) {
		final String full = arg.get("FULL", 0);
		final String stereotype = arg.getLazzy("STEREOTYPE", 0);
		final String color = arg.getLazzy("COLOR", 0);

		final StringBuilder sb = new StringBuilder();
		sb.append("Creating a ");
		if (arg.get("COMPACT", 0) != null)
			sb.append("compact ");
		sb.append(keyword).append(" timing player '").append(arg.get("CODE", 0)).append("'");
		if (full != null)
			sb.append(" displayed as \"").append(full).append("\"");
		if (stereotype != null)
			sb.append(" stereotyped ").append(stereotype);
		if (color != null)
			sb.append(" with background color ").append(color);
		return sb.toString();
	}

	@Override
	final protected CommandExecutionResult executeArg(TimingDiagram diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) throws NoSuchColorException {
		final String full = arg.get("FULL", 0);
		final String stereotype = arg.getLazzy("STEREOTYPE", 0);
		final HColor backColor = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet())
				.getColor(ColorType.BACK);

		createPlayer(diagram, arg.get("CODE", 0), full == null ? "" : full, arg.get("COMPACT", 0) != null,
				stereotype == null ? null : Stereotype.build(stereotype), backColor);
		return CommandExecutionResult.ok();
	}

	@Override
	public final Collection<String> mandatoryFirstTokensFast() {
		return firstTokens;
	}

}
