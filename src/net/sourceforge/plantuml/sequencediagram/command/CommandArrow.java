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
 * 
 *
 */
package net.sourceforge.plantuml.sequencediagram.command;

import java.util.StringTokenizer;

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.classdiagram.command.CommandLinkClass;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.descdiagram.command.CommandLinkElement;
import net.sourceforge.plantuml.sequencediagram.LifeEventType;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.skin.ArrowBody;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ArrowDecoration;
import net.sourceforge.plantuml.skin.ArrowHead;
import net.sourceforge.plantuml.skin.ArrowPart;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

public class CommandArrow extends SingleLineCommand2<SequenceDiagram> {

	private static final String ANCHOR = "(\\{([\\p{L}0-9_]+)\\}[%s]+)?";

	public CommandArrow() {
		super(getRegexConcat());
	}

	public static String getColorOrStylePattern() {
		return "(?:\\[(" + CommandLinkElement.LINE_STYLE + ")\\])?";
	}

	static IRegex getRegexConcat() {
		return RegexConcat
				.build(CommandArrow.class.getName(),
						RegexLeaf.start(), //
						new RegexLeaf("PARALLEL", "(&[%s]*)?"), //
						new RegexLeaf("ANCHOR", ANCHOR), //
						new RegexOr("PART1", //
								new RegexLeaf("PART1CODE", "([\\p{L}0-9_.@]+)"), //
								new RegexLeaf("PART1LONG", "[%g]([^%g]+)[%g]"), //
								new RegexLeaf("PART1LONGCODE", "[%g]([^%g]+)[%g][%s]*as[%s]+([\\p{L}0-9_.@]+)"), //
								new RegexLeaf("PART1CODELONG", "([\\p{L}0-9_.@]+)[%s]+as[%s]*[%g]([^%g]+)[%g]")), //
						new RegexLeaf("PART1ANCHOR", ANCHOR), //
						RegexLeaf.spaceZeroOrMore(), //
						new RegexLeaf("ARROW_DRESSING1",
								"([%s][ox]|(?:[%s][ox])?<<?|(?:[%s][ox])?//?|(?:[%s][ox])?\\\\\\\\?)?"), //
						new RegexOr(new RegexConcat( //
								new RegexLeaf("ARROW_BODYA1", "(-+)"), //
								new RegexLeaf("ARROW_STYLE1", getColorOrStylePattern()), //
								new RegexLeaf("ARROW_BODYB1", "(-*)")), //
								new RegexConcat( //
										new RegexLeaf("ARROW_BODYA2", "(-*)"), //
										new RegexLeaf("ARROW_STYLE2", getColorOrStylePattern()), //
										new RegexLeaf("ARROW_BODYB2", "(-+)"))), //
						new RegexLeaf("ARROW_DRESSING2",
								"(>>?(?:[ox][%s])?|//?(?:[ox][%s])?|\\\\\\\\?(?:[ox][%s])?|[ox][%s])?"), //
						RegexLeaf.spaceZeroOrMore(), //
						new RegexOr("PART2", //
								new RegexLeaf("PART2CODE", "([\\p{L}0-9_.@]+)"), //
								new RegexLeaf("PART2LONG", "[%g]([^%g]+)[%g]"), //
								new RegexLeaf("PART2LONGCODE", "[%g]([^%g]+)[%g][%s]*as[%s]+([\\p{L}0-9_.@]+)"), //
								new RegexLeaf("PART2CODELONG", "([\\p{L}0-9_.@]+)[%s]+as[%s]*[%g]([^%g]+)[%g]")), //
						new RegexLeaf("PART2ANCHOR", ANCHOR), //
						RegexLeaf.spaceZeroOrMore(), //
						new RegexLeaf("ACTIVATION", "(?:([+*!-]+)?)"), //
						RegexLeaf.spaceZeroOrMore(), //
						new RegexLeaf("LIFECOLOR", "(?:(#\\w+)?)"), //
						RegexLeaf.spaceZeroOrMore(), //
						new RegexLeaf("URL", "(" + UrlBuilder.getRegexp() + ")?"), //
						RegexLeaf.spaceZeroOrMore(), new RegexLeaf("MESSAGE", "(?::[%s]*(.*))?"), RegexLeaf.end());
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
	protected CommandExecutionResult executeArg(SequenceDiagram diagram, LineLocation location, RegexResult arg) {

		Participant p1;
		Participant p2;

		final String dressing1 = StringUtils.goLowerCase(CommandLinkClass.notNull(arg.get("ARROW_DRESSING1", 0)));
		final String dressing2 = StringUtils.goLowerCase(CommandLinkClass.notNull(arg.get("ARROW_DRESSING2", 0)));

		final boolean circleAtStart;
		final boolean circleAtEnd;

		final boolean hasDressing2 = contains(dressing2, ">", "\\", "/", "x");
		final boolean hasDressing1 = contains(dressing1, "x", "<", "\\", "/");
		final boolean reverseDefine;
		if (hasDressing2) {
			p1 = getOrCreateParticipant(diagram, arg, "PART1");
			p2 = getOrCreateParticipant(diagram, arg, "PART2");
			circleAtStart = dressing1.contains("o");
			circleAtEnd = dressing2.contains("o");
			reverseDefine = false;
		} else if (hasDressing1) {
			p2 = getOrCreateParticipant(diagram, arg, "PART1");
			p1 = getOrCreateParticipant(diagram, arg, "PART2");
			circleAtStart = dressing2.contains("o");
			circleAtEnd = dressing1.contains("o");
			reverseDefine = true;
		} else {
			return CommandExecutionResult.error("Illegal sequence arrow");

		}

		final boolean sync = contains(dressing1, "<<", "\\\\", "//") || contains(dressing2, ">>", "\\\\", "//");

		final boolean dotted = getLength(arg) > 1;

		final Display labels;
		if (arg.get("MESSAGE", 0) == null) {
			labels = Display.create("");
		} else {
			// final String message = UrlBuilder.multilineTooltip(arg.get("MESSAGE", 0));
			final String message = arg.get("MESSAGE", 0);
			labels = Display.getWithNewlines(message);
		}

		ArrowConfiguration config = hasDressing1 && hasDressing2 ? ArrowConfiguration.withDirectionBoth()
				: ArrowConfiguration.withDirectionNormal();
		if (dotted) {
			config = config.withBody(ArrowBody.DOTTED);
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
			config = config.withDecoration2(ArrowDecoration.CIRCLE);
		}
		if (circleAtStart) {
			config = config.withDecoration1(ArrowDecoration.CIRCLE);
		}
		if (reverseDefine) {
			if (dressing1.contains("x")) {
				config = config.withHead2(ArrowHead.CROSSX);
			}
			if (dressing2.contains("x")) {
				config = config.withHead1(ArrowHead.CROSSX);
			}
		} else {
			if (dressing1.contains("x")) {
				config = config.withHead1(ArrowHead.CROSSX);
			}
			if (dressing2.contains("x")) {
				config = config.withHead2(ArrowHead.CROSSX);
			}
		}
		if (reverseDefine) {
			config = config.reverseDefine();
		}

		config = applyStyle(arg.getLazzy("ARROW_STYLE", 0), config);

		final String activationSpec = arg.get("ACTIVATION", 0);

		if (activationSpec != null && activationSpec.charAt(0) == '*') {
			diagram.activate(p2, LifeEventType.CREATE, null);
		}

		final String messageNumber = diagram.getNextMessageNumber();
		final Message msg = new Message(diagram.getSkinParam().getCurrentStyleBuilder(), p1, p2,
				diagram.manageVariable(labels), config, messageNumber);
		final String url = arg.get("URL", 0);
		if (url != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
			final Url urlLink = urlBuilder.getUrl(url);
			msg.setUrl(urlLink);
		}

		final boolean parallel = arg.get("PARALLEL", 0) != null;
		if (parallel) {
			msg.goParallel();
		}
		msg.setAnchor(arg.get("ANCHOR", 1));
		msg.setPart1Anchor(arg.get("PART1ANCHOR", 1));
		msg.setPart2Anchor(arg.get("PART2ANCHOR", 1));

		final String error = diagram.addMessage(msg);
		if (error != null) {
			return CommandExecutionResult.error(error);
		}

		final HColor activationColor = diagram.getSkinParam().getIHtmlColorSet()
				.getColorIfValid(arg.get("LIFECOLOR", 0));

		if (activationSpec != null) {
			switch (activationSpec.charAt(0)) {
			case '+':
				diagram.activate(p2, LifeEventType.ACTIVATE, activationColor);
				break;
			case '-':
				diagram.activate(p1, LifeEventType.DEACTIVATE, null);
				break;
			case '!':
				diagram.activate(p2, LifeEventType.DESTROY, null);
				break;
			default:
				break;
			}
		} else if (diagram.isAutoactivate()
				&& (config.getHead() == ArrowHead.NORMAL || config.getHead() == ArrowHead.ASYNC)) {
			if (config.isDotted()) {
				diagram.activate(p1, LifeEventType.DEACTIVATE, null);
			} else {
				diagram.activate(p2, LifeEventType.ACTIVATE, activationColor);
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
				config = config.withBody(ArrowBody.DOTTED);
				// link.goDashed();
			} else if (s.equalsIgnoreCase("bold")) {
				// link.goBold();
			} else if (s.equalsIgnoreCase("dotted")) {
				config = config.withBody(ArrowBody.DOTTED);
				// link.goDotted();
			} else if (s.equalsIgnoreCase("hidden")) {
				config = config.withBody(ArrowBody.HIDDEN);
				// link.goHidden();
			} else {
				config = config.withColor(HColorSet.instance().getColorIfValid(s));
			}
		}
		return config;
	}

}
