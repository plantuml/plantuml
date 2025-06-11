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
package net.sourceforge.plantuml.sequencediagram.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.classdiagram.command.CommandLinkClass;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.descdiagram.command.CommandLinkElement;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.sequencediagram.LifeEventType;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.skin.ArrowBody;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ArrowDecoration;
import net.sourceforge.plantuml.skin.ArrowHead;
import net.sourceforge.plantuml.skin.ArrowPart;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.url.UrlBuilder;
import net.sourceforge.plantuml.url.UrlMode;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandArrow extends SingleLineCommand2<SequenceDiagram> {

	static final String ANCHOR = "(\\{([%pLN_]+)\\}[%s]+)?";

	public CommandArrow() {
		super(getRegexConcat());
	}

	public static String getColorOrStylePattern() {
		return "(?:\\[(" + CommandLinkElement.LINE_STYLE + ")\\])?";
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandArrow.class.getName(), RegexLeaf.start(), //
				new RegexLeaf(1, "PARALLEL", "(&[%s]*)?"), //
				new RegexLeaf(2, "ANCHOR", ANCHOR), //
				new RegexOr("PART1", //
						new RegexLeaf(1, "PART1CODE", "([%pLN_.@]+)"), //
						new RegexLeaf(1, "PART1LONG", "[%g]([^%g]+)[%g]"), //
						new RegexLeaf(2, "PART1LONGCODE", "[%g]([^%g]+)[%g][%s]*as[%s]+([%pLN_.@]+)"), //
						new RegexLeaf(2, "PART1CODELONG", "([%pLN_.@]+)[%s]+as[%s]*[%g]([^%g]+)[%g]")), //
				new RegexLeaf(2, "PART1ANCHOR", ANCHOR), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexOr("ARROW_DRESSING1", //
						new RegexLeaf("[%s][ox]"), //
						new RegexLeaf("(?:[%s][ox]|\\(\\d+\\))?<<?_?"), //
						new RegexLeaf("(?:[%s][ox])?//?"), //
						new RegexLeaf("(?:[%s][ox])?\\\\\\\\?"))), //
				new RegexOr(new RegexConcat( //
						new RegexLeaf(1, "ARROW_BODYA1", "(-+)"), //
						new RegexLeaf(1, "ARROW_STYLE1", getColorOrStylePattern()), //
						new RegexLeaf(1, "ARROW_BODYB1", "(-*)")), //
						new RegexConcat( //
								new RegexLeaf(1, "ARROW_BODYA2", "(-*)"), //
								new RegexLeaf(1, "ARROW_STYLE2", getColorOrStylePattern()), //
								new RegexLeaf(1, "ARROW_BODYB2", "(-+)"))), //
				new RegexOptional(new RegexOr("ARROW_DRESSING2", //
						new RegexLeaf("_?>>?(?:[ox][%s]|\\(\\d+\\))?"), //
						new RegexLeaf("//?(?:[ox][%s])?"), //
						new RegexLeaf("\\\\\\\\?(?:[ox][%s])?"), //
						new RegexLeaf("[ox][%s]"))), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOr("PART2", //
						new RegexLeaf(1, "PART2CODE", "([%pLN_.@]+)"), //
						new RegexLeaf(1, "PART2LONG", "[%g]([^%g]+)[%g]"), //
						new RegexLeaf(2, "PART2LONGCODE", "[%g]([^%g]+)[%g][%s]*as[%s]+([%pLN_.@]+)"), //
						new RegexLeaf(2, "PART2CODELONG", "([%pLN_.@]+)[%s]+as[%s]*[%g]([^%g]+)[%g]")), //
				new RegexLeaf(1, "MULTICAST", "((?:\\s&\\s[%pLN_.@]+)*)"), //
				new RegexLeaf(2, "PART2ANCHOR", ANCHOR), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(1, "ACTIVATION", "(?:(\\+\\+|\\*\\*|!!|--|--\\+\\+|\\+\\+--)?)"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(1, "LIFECOLOR", "(?:(#\\w+)?)"), //
				StereotypePattern.optional("STEREOTYPE"), //
				UrlBuilder.OPTIONAL, //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(1, "MESSAGE", "(?::[%s]*(.*))?"), //
				RegexLeaf.end()).protectSize(2000);
	}

	private List<Participant> getMulticasts(LineLocation location, SequenceDiagram system, RegexResult arg2) {
		final String multicast = arg2.get("MULTICAST", 0);
		if (multicast != null) {
			final List<Participant> result = new ArrayList<>();
			for (String s : multicast.split("&")) {
				s = s.trim();
				if (s.length() == 0)
					continue;

				final Participant participant = system.getOrCreateParticipant(location, s);
				if (participant != null)
					result.add(participant);

			}
			return Collections.unmodifiableList(result);
		}
		return Collections.emptyList();
	}

	private Participant getOrCreateParticipant(LineLocation location, SequenceDiagram system, RegexResult arg2, String n) {
		final String code;
		final Display display;
		if (arg2.get(n + "CODE", 0) != null) {
			code = arg2.get(n + "CODE", 0);
			display = Display.getWithNewlines(system.getPragma(), code);
		} else if (arg2.get(n + "LONG", 0) != null) {
			code = arg2.get(n + "LONG", 0);
			display = Display.getWithNewlines(system.getPragma(), code);
		} else if (arg2.get(n + "LONGCODE", 0) != null) {
			display = Display.getWithNewlines(system.getPragma(), arg2.get(n + "LONGCODE", 0));
			code = arg2.get(n + "LONGCODE", 1);
		} else if (arg2.get(n + "CODELONG", 0) != null) {
			code = arg2.get(n + "CODELONG", 0);
			display = Display.getWithNewlines(system.getPragma(), arg2.get(n + "CODELONG", 1));
			return system.getOrCreateParticipant(location, code, display);
		} else {
			throw new IllegalStateException();
		}
		return system.getOrCreateParticipant(location, code, display);
	}

	private boolean contains(String string, String... totest) {
		for (String t : totest)
			if (string.contains(t))
				return true;

		return false;
	}

	private String getDressing(RegexResult arg, String key) {
		String value = arg.get(key, 0);
		value = CommandLinkClass.notNull(value);
		value = value.replace("_", "");
		return StringUtils.goLowerCase(value);
	}

	private int getInclination(String key) {
		if (key == null)
			return 0;
		final int x1 = key.indexOf('(');
		if (x1 == -1)
			return 0;
		final int x2 = key.indexOf(')');
		if (x2 == -1)
			return 0;
		return Integer.parseInt(key.substring(x1 + 1, x2));
	}

	@Override
	protected CommandExecutionResult executeArg(SequenceDiagram diagram, LineLocation location, RegexResult arg, ParserPass currentPass)
			throws NoSuchColorException {

		final String dressing1 = getDressing(arg, "ARROW_DRESSING1");
		final String dressing2 = getDressing(arg, "ARROW_DRESSING2");
		final int inclination1 = getInclination(arg.get("ARROW_DRESSING1", 0));
		final int inclination2 = getInclination(arg.get("ARROW_DRESSING2", 0));

		// Sorry, this code is note very clear
		final boolean hasDressing1butx = contains(dressing1, "<", "\\", "/");
		final boolean xInDressing1 = dressing1.contains("x");
		final boolean hasDressing2butx = contains(dressing2, ">", "\\", "/");
		final boolean xInDressing2 = dressing2.contains("x");
		final boolean reverseDefine;
		if (hasDressing2butx || (xInDressing1 && xInDressing2))
			reverseDefine = false;
		else if (hasDressing1butx)
			reverseDefine = true;
		else if (xInDressing1 || xInDressing2)
			reverseDefine = false;
		else
			return CommandExecutionResult.error("Illegal sequence arrow");

		final Participant p1;
		final Participant p2;
		final boolean circleAtStart;
		final boolean circleAtEnd;
		final boolean sync1;
		final boolean sync2;
		if (reverseDefine) {
			// Keep the order
			// See https://github.com/plantuml/plantuml/issues/1819#issuecomment-2158524871
			p2 = getOrCreateParticipant(location, diagram, arg, "PART1");
			p1 = getOrCreateParticipant(location, diagram, arg, "PART2");
			circleAtStart = dressing2.contains("o");
			circleAtEnd = dressing1.contains("o");
			sync2 = contains(dressing1, "<<", "\\\\", "//");
			sync1 = contains(dressing2, ">>", "\\\\", "//");
		} else {
			p1 = getOrCreateParticipant(location, diagram, arg, "PART1");
			p2 = getOrCreateParticipant(location, diagram, arg, "PART2");
			circleAtStart = dressing1.contains("o");
			circleAtEnd = dressing2.contains("o");
			sync1 = contains(dressing1, "<<", "\\\\", "//");
			sync2 = contains(dressing2, ">>", "\\\\", "//");
		}


		final boolean dotted = getLength(arg) > 1;

		final Display labels;
		if (arg.get("MESSAGE", 0) == null) {
			labels = Display.create("");
		} else {
			// final String message = UrlBuilder.multilineTooltip(arg.get("MESSAGE", 0));
			final String message = arg.get("MESSAGE", 0);
			labels = Display.getWithNewlines(diagram.getPragma(), message);
		}

		ArrowConfiguration config = hasDressing1butx && hasDressing2butx ? ArrowConfiguration.withDirectionBoth()
				: ArrowConfiguration.withDirectionNormal();
		if (dotted)
			config = config.withBody(ArrowBody.DOTTED);

		if (sync1)
			config = config.withHead1(ArrowHead.ASYNC);
		if (sync2)
			config = config.withHead2(ArrowHead.ASYNC);

		if (dressing2.contains("\\") || dressing1.contains("/"))
			config = config.withPart(ArrowPart.TOP_PART);

		if (dressing2.contains("/") || dressing1.contains("\\"))
			config = config.withPart(ArrowPart.BOTTOM_PART);

		if (circleAtEnd)
			config = config.withDecoration2(ArrowDecoration.CIRCLE);

		if (circleAtStart)
			config = config.withDecoration1(ArrowDecoration.CIRCLE);

		if (reverseDefine) {
			if (xInDressing1)
				config = config.withHead2(ArrowHead.CROSSX);

			if (xInDressing2)
				config = config.withHead1(ArrowHead.CROSSX);

		} else {
			if (xInDressing1)
				config = config.withHead1(ArrowHead.CROSSX);

			if (xInDressing2)
				config = config.withHead2(ArrowHead.CROSSX);

		}
		if (reverseDefine)
			config = config.reverseDefine();

		config = applyStyle(arg.getLazzy("ARROW_STYLE", 0), config);

		config = config.withInclination(inclination1 + inclination2);

		final String activationSpec = arg.get("ACTIVATION", 0);

		if (activationSpec != null && activationSpec.charAt(0) == '*')
			diagram.activate(p2, LifeEventType.CREATE, null);

		final String messageNumber = diagram.getNextMessageNumber();
		final Message msg = new Message(diagram.getSkinParam().getCurrentStyleBuilder(), p1, p2,
				diagram.manageVariable(labels), config, messageNumber);
		msg.setMulticast(getMulticasts(location, diagram, arg));
		final String url = arg.get("URL", 0);
		if (url != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), UrlMode.STRICT);
			final Url urlLink = urlBuilder.getUrl(url);
			msg.setUrl(urlLink);
		}

		if (arg.get("STEREOTYPE", 0) != null) {
			final Stereotype stereotype = Stereotype.build(arg.get("STEREOTYPE", 0));
			msg.getStereotype(stereotype);
		}

		final boolean parallel = arg.get("PARALLEL", 0) != null;
		if (parallel)
			msg.goParallel();

		msg.setAnchor(arg.get("ANCHOR", 1));
		msg.setPart1Anchor(arg.get("PART1ANCHOR", 1));
		msg.setPart2Anchor(arg.get("PART2ANCHOR", 1));

		final CommandExecutionResult status = diagram.addMessage(msg);
		if (status.isOk() == false)
			return status;

		final String s = arg.get("LIFECOLOR", 0);

		final HColor activationColor = s == null ? null : diagram.getSkinParam().getIHtmlColorSet().getColor(s);

		if (activationSpec != null)
			return manageActivations(activationSpec, diagram, p1, p2, activationColor);

		if (diagram.isAutoactivate() && (config.getHead() == ArrowHead.NORMAL || config.getHead() == ArrowHead.ASYNC))
			if (config.isDotted())
				diagram.activate(p1, LifeEventType.DEACTIVATE, null);
			else
				diagram.activate(p2, LifeEventType.ACTIVATE, activationColor);

		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult manageActivations(String spec, SequenceDiagram diagram, Participant p1,
			Participant p2, HColor activationColor) {
		switch (spec.charAt(0)) {
		case '+':
			diagram.activate(p2, LifeEventType.ACTIVATE, activationColor);
			break;
		case '-':
			diagram.activate(p1, LifeEventType.DEACTIVATE, null);
			break;
		case '!':
			diagram.activate(p2, LifeEventType.DESTROY, null);
			break;
		}
		if (spec.length() == 4) {
			switch (spec.charAt(2)) {
			case '+':
				diagram.activate(p2, LifeEventType.ACTIVATE, activationColor);
				break;
			case '-':
				diagram.activate(p1, LifeEventType.DEACTIVATE, null);
				break;
			}
		}
		return CommandExecutionResult.ok();
	}

	private int getLength(RegexResult arg2) {
		String sa = arg2.getLazzy("ARROW_BODYA", 0);
		if (sa == null)
			sa = "";

		String sb = arg2.getLazzy("ARROW_BODYB", 0);
		if (sb == null)
			sb = "";

		return sa.length() + sb.length();
	}

	public static ArrowConfiguration applyStyle(String arrowStyle, ArrowConfiguration config)
			throws NoSuchColorException {
		if (arrowStyle == null)
			return config;

		final StringTokenizer st = new StringTokenizer(arrowStyle, ",");
		while (st.hasMoreTokens()) {
			final String s = st.nextToken();
			if (s.equalsIgnoreCase("dashed")) {
				config = config.withBody(ArrowBody.DOTTED);
			} else if (s.equalsIgnoreCase("bold")) {
			} else if (s.equalsIgnoreCase("dotted")) {
				config = config.withBody(ArrowBody.DOTTED);
			} else if (s.equalsIgnoreCase("hidden")) {
				config = config.withBody(ArrowBody.HIDDEN);
			} else {
				config = config.withColor(HColorSet.instance().getColor(s));
			}
		}
		return config;
	}

}
