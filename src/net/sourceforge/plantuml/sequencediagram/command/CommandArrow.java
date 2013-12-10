/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 5424 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.command;

import java.util.StringTokenizer;

import net.sourceforge.plantuml.classdiagram.command.CommandLinkClass;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.sequencediagram.LifeEventType;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ArrowDecoration;
import net.sourceforge.plantuml.skin.ArrowHead;
import net.sourceforge.plantuml.skin.ArrowPart;

public class CommandArrow extends SingleLineCommand2<SequenceDiagram> {

	public CommandArrow() {
		super(getRegexConcat());
	}

	public static String getColorOrStylePattern() {
		return "(?:\\[((?:#\\w+|dotted|dashed|bold|hidden)(?:,#\\w+|,dotted|,dashed|,bold|,hidden)*)\\])?";
	}

	static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexOr("PART1", //
						new RegexLeaf("PART1CODE", "([\\p{L}0-9_.@]+)"), //
						new RegexLeaf("PART1LONG", "\"([^\"]+)\""), //
						new RegexLeaf("PART1LONGCODE", "\"([^\"]+)\"\\s*as\\s+([\\p{L}0-9_.@]+)"), //
						new RegexLeaf("PART1CODELONG", "([\\p{L}0-9_.@]+)\\s+as\\s*\"([^\"]+)\"")), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("ARROW_DRESSING1", "( [ox]|(?: [ox])?<<?|(?: [ox])?//?|(?: [ox])?\\\\\\\\?)?"), //
				new RegexOr(new RegexConcat( //
						new RegexLeaf("ARROW_BODYA1", "(-+)"), //
						new RegexLeaf("ARROW_STYLE1", getColorOrStylePattern()), //
						new RegexLeaf("ARROW_BODYB1", "(-*)")), //
						new RegexConcat( //
								new RegexLeaf("ARROW_BODYA2", "(-*)"), //
								new RegexLeaf("ARROW_STYLE2", getColorOrStylePattern()), //
								new RegexLeaf("ARROW_BODYB2", "(-+)"))), //
				new RegexLeaf("ARROW_DRESSING2", "(>>?(?:[ox] )?|//?(?:[ox] )?|\\\\\\\\?(?:[ox] )?|[ox] )?"), //
				new RegexLeaf("\\s*"), //
				new RegexOr("PART2", //
						new RegexLeaf("PART2CODE", "([\\p{L}0-9_.@]+)"), //
						new RegexLeaf("PART2LONG", "\"([^\"]+)\""), //
						new RegexLeaf("PART2LONGCODE", "\"([^\"]+)\"\\s*as\\s+([\\p{L}0-9_.@]+)"), //
						new RegexLeaf("PART2CODELONG", "([\\p{L}0-9_.@]+)\\s+as\\s*\"([^\"]+)\"")), new RegexLeaf(
						"\\s*"), //
				new RegexLeaf("ACTIVATION", "(?:([+*!-]+)?)"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("LIFECOLOR", "(?:(#\\w+)?)"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("MESSAGE", "(?::\\s*(.*))?$"));
	}

	private Participant getOrCreateParticipant(SequenceDiagram system, RegexResult arg2, String n) {
		final String code;
		final Display display;
		if (arg2.get(n + "CODE", 0) != null) {
			code = arg2.get(n + "CODE", 0);
			display = Display.getWithNewlines(code);
		} else if (arg2.get(n + "LONG", 0) != null) {
			code = arg2.get(n + "LONG", 0);
			display = Display.getWithNewlines(code);
		} else if (arg2.get(n + "LONGCODE", 0) != null) {
			display = Display.getWithNewlines(arg2.get(n + "LONGCODE", 0));
			code = arg2.get(n + "LONGCODE", 1);
		} else if (arg2.get(n + "CODELONG", 0) != null) {
			code = arg2.get(n + "CODELONG", 0);
			display = Display.getWithNewlines(arg2.get(n + "CODELONG", 1));
			return system.getOrCreateParticipant(code, display);
		} else {
			throw new IllegalStateException();
		}
		return system.getOrCreateParticipant(code, display);
	}

	private boolean contains(String string, String... totest) {
		for (String t : totest) {
			if (string.contains(t)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected CommandExecutionResult executeArg(SequenceDiagram system, RegexResult arg2) {

		Participant p1;
		Participant p2;

		final String dressing1 = CommandLinkClass.notNull(arg2.get("ARROW_DRESSING1", 0)).toLowerCase();
		final String dressing2 = CommandLinkClass.notNull(arg2.get("ARROW_DRESSING2", 0)).toLowerCase();

		final boolean circleAtStart;
		final boolean circleAtEnd;

		final boolean hasDressing2 = contains(dressing2, ">", "\\", "/", "x");
		final boolean hasDressing1 = contains(dressing1, "x", "<", "\\", "/");
		if (hasDressing2) {
			p1 = getOrCreateParticipant(system, arg2, "PART1");
			p2 = getOrCreateParticipant(system, arg2, "PART2");
			circleAtStart = dressing1.contains("o");
			circleAtEnd = dressing2.contains("o");
		} else if (hasDressing1) {
			p2 = getOrCreateParticipant(system, arg2, "PART1");
			p1 = getOrCreateParticipant(system, arg2, "PART2");
			circleAtStart = dressing2.contains("o");
			circleAtEnd = dressing1.contains("o");
		} else {
			return CommandExecutionResult.error("Illegal sequence arrow");

		}

		final boolean sync = contains(dressing1, "<<", "\\\\", "//") || contains(dressing2, ">>", "\\\\", "//");

		final boolean dotted = getLength(arg2) > 1;

		final Display labels;
		if (arg2.get("MESSAGE", 0) == null) {
			labels = Display.asList("");
		} else {
			labels = Display.getWithNewlines(arg2.get("MESSAGE", 0));
		}

		ArrowConfiguration config = hasDressing1 && hasDressing2 ? ArrowConfiguration.withDirectionBoth()
				: ArrowConfiguration.withDirectionNormal();
		if (dotted) {
			config = config.withDotted();
		}
		if (sync) {
			config = config.withHead(ArrowHead.ASYNC);
		}
		if (dressing2.contains("\\") || dressing1.contains("/")) {
			config = config.withPart(ArrowPart.TOP_PART);
		}
		if (dressing2.contains("/") || dressing1.contains("\\")) {
			config = config.withPart(ArrowPart.BOTTOM_PART);
		}
		if (circleAtEnd) {
			config = config.withDecorationEnd(ArrowDecoration.CIRCLE);
		}
		if (circleAtStart) {
			config = config.withDecorationStart(ArrowDecoration.CIRCLE);
		}
		if (dressing1.contains("x")) {
			config = config.withHead2(ArrowHead.CROSSX);

		}
		if (dressing2.contains("x")) {
			config = config.withHead2(ArrowHead.CROSSX);
		}

		config = applyStyle(arg2.getLazzy("ARROW_STYLE", 0), config);

		final String activationSpec = arg2.get("ACTIVATION", 0);

		if (activationSpec != null && activationSpec.charAt(0) == '*') {
			system.activate(p2, LifeEventType.CREATE, null);
		}

		final String error = system.addMessage(new Message(p1, p2, labels, config, system.getNextMessageNumber()));
		if (error != null) {
			return CommandExecutionResult.error(error);
		}

		final HtmlColor activationColor = HtmlColorUtils.getColorIfValid(arg2.get("LIFECOLOR", 0));

		if (activationSpec != null) {
			switch (activationSpec.charAt(0)) {
			case '+':
				system.activate(p2, LifeEventType.ACTIVATE, activationColor);
				break;
			case '-':
				system.activate(p1, LifeEventType.DEACTIVATE, null);
				break;
			case '!':
				system.activate(p2, LifeEventType.DESTROY, null);
				break;
			default:
				break;
			}
		} else if (system.isAutoactivate() && config.getHead() == ArrowHead.NORMAL) {
			if (config.isDotted()) {
				system.activate(p1, LifeEventType.DEACTIVATE, null);
			} else {
				system.activate(p2, LifeEventType.ACTIVATE, activationColor);
			}

		}
		return CommandExecutionResult.ok();
	}

	private int getLength(RegexResult arg2) {
		String sa = arg2.getLazzy("ARROW_BODYA", 0);
		if (sa == null) {
			sa = "";
		}
		String sb = arg2.getLazzy("ARROW_BODYB", 0);
		if (sb == null) {
			sb = "";
		}
		return sa.length() + sb.length();
	}

	public static ArrowConfiguration applyStyle(String arrowStyle, ArrowConfiguration config) {
		if (arrowStyle == null) {
			return config;
		}
		final StringTokenizer st = new StringTokenizer(arrowStyle, ",");
		while (st.hasMoreTokens()) {
			final String s = st.nextToken();
			if (s.equalsIgnoreCase("dashed")) {
				// link.goDashed();
			} else if (s.equalsIgnoreCase("bold")) {
				// link.goBold();
			} else if (s.equalsIgnoreCase("dotted")) {
				// link.goDotted();
			} else if (s.equalsIgnoreCase("hidden")) {
				// link.goHidden();
			} else {
				config = config.withColor(HtmlColorUtils.getColorIfValid(s));
			}
		}
		return config;
	}

}
