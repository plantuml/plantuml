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
 * Contribution   :  Serge Wenger
 * 
 *
 */
package net.sourceforge.plantuml.statediagram.command;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Ident;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.statediagram.StateDiagram;
import net.sourceforge.plantuml.ugraphic.color.NoSuchColorException;

abstract class CommandLinkStateCommon extends SingleLineCommand2<StateDiagram> {

	CommandLinkStateCommon(IRegex pattern) {
		super(pattern);
	}

	protected static RegexLeaf getStatePattern(String name) {
		return new RegexLeaf(name,
				"([%pLN_.:]+|[%pLN_.:]+\\[H\\*?\\]|\\[\\*\\]|\\[H\\*?\\]|(?:==+)(?:[%pLN_.:]+)(?:==+))[%s]*(\\<\\<.*\\>\\>)?[%s]*(#\\w+)?");
	}

	@Override
	protected CommandExecutionResult executeArg(StateDiagram diagram, LineLocation location, RegexResult arg)
			throws NoSuchColorException {
		final String ent1 = arg.get("ENT1", 0);
		final String ent2 = arg.get("ENT2", 0);

		final IEntity cl1 = getEntityStart(diagram, ent1);
		if (cl1 == null) {
			return CommandExecutionResult
					.error("The state " + ent1 + " has been created in a concurrent state : it cannot be used here.");
		}
		final IEntity cl2 = getEntityEnd(diagram, ent2);
		if (cl2 == null) {
			return CommandExecutionResult
					.error("The state " + ent2 + " has been created in a concurrent state : it cannot be used here.");
		}

		if (arg.get("ENT1", 1) != null) {
			cl1.setStereotype(Stereotype.build(arg.get("ENT1", 1)));
		}
		if (arg.get("ENT1", 2) != null) {
			final String s = arg.get("ENT1", 2);
			cl1.setSpecificColorTOBEREMOVED(ColorType.BACK,
					diagram.getSkinParam().getIHtmlColorSet().getColor(diagram.getSkinParam().getThemeStyle(), s));
		}
		if (arg.get("ENT2", 1) != null) {
			cl2.setStereotype(Stereotype.build(arg.get("ENT2", 1)));
		}
		if (arg.get("ENT2", 2) != null) {
			final String s = arg.get("ENT2", 2);
			cl2.setSpecificColorTOBEREMOVED(ColorType.BACK,
					diagram.getSkinParam().getIHtmlColorSet().getColor(diagram.getSkinParam().getThemeStyle(), s));
		}

		String queue = arg.get("ARROW_BODY1", 0) + arg.get("ARROW_BODY2", 0);
		final Direction dir = getDirection(arg);

		if (dir == Direction.LEFT || dir == Direction.RIGHT) {
			queue = "-";
		}

		final int lenght = queue.length();

		final boolean crossStart = arg.get("ARROW_CROSS_START", 0) != null;
		final boolean circleEnd = arg.get("ARROW_CIRCLE_END", 0) != null;
		final LinkType linkType = new LinkType(circleEnd ? LinkDecor.ARROW_AND_CIRCLE : LinkDecor.ARROW,
				crossStart ? LinkDecor.CIRCLE_CROSS : LinkDecor.NONE);

		final Display label = Display.getWithNewlines(arg.get("LABEL", 0));
		Link link = new Link(cl1, cl2, linkType, label, lenght, diagram.getSkinParam().getCurrentStyleBuilder());
		if (dir == Direction.LEFT || dir == Direction.UP) {
			link = link.getInv();
		}
		link.applyStyle(diagram.getSkinParam().getThemeStyle(), arg.getLazzy("ARROW_STYLE", 0));
		link.setUmlDiagramType(UmlDiagramType.STATE);
		diagram.addLink(link);

		return CommandExecutionResult.ok();
	}

	private Direction getDirection(RegexResult arg) {
		final String arrowDirection = arg.get("ARROW_DIRECTION", 0);
		if (arrowDirection != null) {
			return StringUtils.getQueueDirection(arrowDirection);
		}
		return getDefaultDirection();
	}

	protected Direction getDefaultDirection() {
		return null;
	}

	private IEntity getEntityStart(StateDiagram diagram, final String codeString) {
		if (codeString.startsWith("[*]")) {
			return diagram.getStart();
		}
		return getFoo1(diagram, codeString);
	}

	private IEntity getEntityEnd(StateDiagram diagram, final String codeString) {
		if (codeString.startsWith("[*]")) {
			return diagram.getEnd();
		}
		return getFoo1(diagram, codeString);
	}

	private IEntity getFoo1(StateDiagram diagram, final String codeString) {
		if (codeString.equalsIgnoreCase("[H]")) {
			return diagram.getHistorical();
		}
		if (codeString.endsWith("[H]")) {
			return diagram.getHistorical(codeString.substring(0, codeString.length() - 3));
		}
		if (codeString.equalsIgnoreCase("[H*]")) {
			return diagram.getDeepHistory();
		}
		if (codeString.endsWith("[H*]")) {
			return diagram.getDeepHistory(codeString.substring(0, codeString.length() - 4));
		}
		if (codeString.startsWith("=") && codeString.endsWith("=")) {
			final String codeString1 = removeEquals(codeString);
			final Ident ident1 = diagram.buildLeafIdent(codeString1);
			final Code code1 = diagram.V1972() ? ident1 : diagram.buildCode(codeString1);
			return diagram.getOrCreateLeaf(ident1, code1, LeafType.SYNCHRO_BAR, null);
		}
		final Ident ident = diagram.buildLeafIdent(codeString);
		final Code code = diagram.V1972() ? ident : diagram.buildCode(codeString);
		if (diagram.checkConcurrentStateOk(ident, code) == false) {
			return null;
		}
		return diagram.getOrCreateLeaf(ident, code, null, null);
	}

	private String removeEquals(String code) {
		while (code.startsWith("=")) {
			code = code.substring(1);
		}
		while (code.endsWith("=")) {
			code = code.substring(0, code.length() - 1);
		}
		return code;
	}

}
