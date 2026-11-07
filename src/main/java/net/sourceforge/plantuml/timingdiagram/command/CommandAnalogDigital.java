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
import net.sourceforge.plantuml.timingdiagram.PlayerAnalogDigital;
import net.sourceforge.plantuml.timingdiagram.TimingDiagram;
import net.sourceforge.plantuml.timingdiagram.TimingType;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandAnalogDigital extends SingleLineCommand2<TimingDiagram> {

	public CommandAnalogDigital() {
		super(getRegexConcat());
	}

	private static final Collection<String> FIRST_TOKENS = Arrays.asList("analog", "compact", "digital");

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandAnalogDigital.class.getName(), RegexLeaf.start(), //
				new RegexOptional( //
						new RegexConcat( //
								new RegexLeaf(1, "COMPACT", "(compact)"), //
								RegexLeaf.spaceOneOrMore())), //
				new RegexLeaf(1, "TYPE", "(analog|digital)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexOr( //
                    new RegexConcat( //
                            new RegexLeaf(1, "FULL", "[%g]([^%g]+)[%g]"), //
                            StereotypePattern.optional("STEREOTYPE1"), //
                            new RegexOr( //
                                    new RegexConcat( //
                                            RegexLeaf.spaceOneOrMore(), //
                                            new RegexLeaf("as"), //
                                            RegexLeaf.spaceOneOrMore(), //
                                            new RegexLeaf(1, "CODE", "([%pLN_.@]+)"), //
                                            StereotypePattern.optional("STEREOTYPE2"), //
                                            optionalBoundsRegex(1), //
                                            StereotypePattern.optional("STEREOTYPE3")), //
                                    new RegexConcat( //
                                            RegexLeaf.spaceOneOrMore(), //
                                            boundsRegex(2), //
                                            RegexLeaf.spaceOneOrMore(), //
                                            new RegexLeaf("as"), //
                                            RegexLeaf.spaceOneOrMore(), //
                                            new RegexLeaf(1, "CODE1", "([%pLN_.@]+)"), //
                                            StereotypePattern.optional("STEREOTYPE4")))), //
                    new RegexConcat( //
                            new RegexLeaf(1, "CODE2", "([%pLN_.@]+)"), //
                            StereotypePattern.optional("STEREOTYPE5"), //
                            optionalBoundsRegex(3), //
                            StereotypePattern.optional("STEREOTYPE6"))), //
				RegexLeaf.end());
	}

	private static IRegex optionalBoundsRegex(int group) {
		return new RegexOptional( //
				new RegexConcat( //
						RegexLeaf.spaceOneOrMore(), //
						boundsRegex(group)));
	}

	private static IRegex boundsRegex(int group) {
		return new RegexConcat( //
				new RegexOr(new RegexLeaf("between"), new RegexLeaf("from")), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(1, "START" + group, "(-?[0-9]*\\.?[0-9]+)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexOr(new RegexLeaf("and"), new RegexLeaf("to")), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(1, "END" + group, "(-?[0-9]*\\.?[0-9]+)"));
	}

	@Override
	final protected CommandExecutionResult executeArg(TimingDiagram diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) {
		final String compact = arg.get("COMPACT", 0);
		final String code = arg.getLazzy("CODE", 0);
		String full = arg.get("FULL", 0);
		if (full == null)
			full = "";

		final String stereotypeString = arg.getLazzy("STEREOTYPE", 0);
		Stereotype stereotype = null;
		if (stereotypeString != null)
			stereotype = Stereotype.build(stereotypeString);

		final TimingType type = TimingType.valueOf(arg.get("TYPE", 0).toUpperCase());
		final PlayerAnalogDigital player = diagram.createPlayerAnalogDigital(type, code, full, compact != null, stereotype);

		final String start = arg.getLazzy("START", 0);
		final String end = arg.getLazzy("END", 0);
		if (start != null && end != null)
			player.setBounds(start, end);

		return CommandExecutionResult.ok();
	}

	@Override
	public Collection<String> mandatoryFirstTokensFast() {
		return FIRST_TOKENS;
	}

}
