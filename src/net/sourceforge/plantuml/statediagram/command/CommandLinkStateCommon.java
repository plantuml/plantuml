/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.baraye.EntityImp;
import net.sourceforge.plantuml.baraye.Quark;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkArg;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.statediagram.StateDiagram;
import net.sourceforge.plantuml.ugraphic.color.NoSuchColorException;
import net.sourceforge.plantuml.utils.Direction;
import net.sourceforge.plantuml.utils.LineLocation;

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

		final EntityImp cl1 = getEntityStart(diagram, ent1);
		if (cl1 == null)
			return CommandExecutionResult
					.error("The state " + ent1 + " has been created in a concurrent state : it cannot be used here.");

		final EntityImp cl2 = getEntityEnd(diagram, ent2);
		if (cl2 == null)
			return CommandExecutionResult
					.error("The state " + ent2 + " has been created in a concurrent state : it cannot be used here.");

		if (arg.get("ENT1", 1) != null)
			cl1.setStereotype(Stereotype.build(arg.get("ENT1", 1)));

		if (arg.get("ENT1", 2) != null) {
			final String s = arg.get("ENT1", 2);
			cl1.setSpecificColorTOBEREMOVED(ColorType.BACK, diagram.getSkinParam().getIHtmlColorSet().getColor(s));
		}
		if (arg.get("ENT2", 1) != null) {
			cl2.setStereotype(Stereotype.build(arg.get("ENT2", 1)));
		}
		if (arg.get("ENT2", 2) != null) {
			final String s = arg.get("ENT2", 2);
			cl2.setSpecificColorTOBEREMOVED(ColorType.BACK, diagram.getSkinParam().getIHtmlColorSet().getColor(s));
		}

		String queue = arg.get("ARROW_BODY1", 0) + arg.get("ARROW_BODY2", 0);
		final Direction dir = getDirection(arg);

		if (dir == Direction.LEFT || dir == Direction.RIGHT)
			queue = "-";

		final int lenght = queue.length();

		final boolean crossStart = arg.get("ARROW_CROSS_START", 0) != null;
		final boolean circleEnd = arg.get("ARROW_CIRCLE_END", 0) != null;
		final LinkType linkType = new LinkType(circleEnd ? LinkDecor.ARROW_AND_CIRCLE : LinkDecor.ARROW,
				crossStart ? LinkDecor.CIRCLE_CROSS : LinkDecor.NONE);

		final Display label = Display.getWithNewlines(arg.get("LABEL", 0));
		final LinkArg linkArg = LinkArg.build(label, lenght, diagram.getSkinParam().classAttributeIconSize() > 0);
		Link link = new Link(diagram.getEntityFactory(), diagram.getSkinParam().getCurrentStyleBuilder(), cl1, cl2,
				linkType, linkArg);
		if (dir == Direction.LEFT || dir == Direction.UP)
			link = link.getInv();

		link.applyStyle(arg.getLazzy("ARROW_STYLE", 0));
		diagram.addLink(link);

		return CommandExecutionResult.ok();
	}

	private Direction getDirection(RegexResult arg) {
		final String arrowDirection = arg.get("ARROW_DIRECTION", 0);
		if (arrowDirection != null)
			return StringUtils.getQueueDirection(arrowDirection);

		return getDefaultDirection();
	}

	protected Direction getDefaultDirection() {
		return null;
	}

	private EntityImp getEntityStart(StateDiagram diagram, final String code) {
		if (code.startsWith("[*]"))
			return diagram.getStart();

		return getEntity(diagram, code);
	}

	private EntityImp getEntityEnd(StateDiagram diagram, final String code) {
		if (code.startsWith("[*]"))
			return diagram.getEnd();

		return getEntity(diagram, code);
	}

	private EntityImp getEntity(StateDiagram diagram, final String code) {
		if (code.equalsIgnoreCase("[H]"))
			return diagram.getHistorical();

		if (code.endsWith("[H]"))
			return diagram.getHistorical(code.substring(0, code.length() - 3));

		if (code.equalsIgnoreCase("[H*]"))
			return diagram.getDeepHistory();

		if (code.endsWith("[H*]"))
			return diagram.getDeepHistory(code.substring(0, code.length() - 4));

		if (code.startsWith("=") && code.endsWith("=")) {
			final String codeString1 = removeEquals(code);
			final Quark quark = diagram.quarkInContext(diagram.cleanIdForQuark(codeString1), false);
			if (quark.getData() != null)
				return (EntityImp) quark.getData();
			return diagram.reallyCreateLeaf(quark, Display.getWithNewlines(quark), LeafType.SYNCHRO_BAR, null);
		}

		if (diagram.currentQuark().getName().equals(code) && diagram.currentQuark().getData() != null)
			return (EntityImp) diagram.currentQuark().getData();

		final Quark quark = diagram.quarkInContext(diagram.cleanIdForQuark(code), false);
		if (diagram.checkConcurrentStateOk(quark) == false)
			return null;

		if (quark.getData() != null)
			return (EntityImp) quark.getData();
		return diagram.reallyCreateLeaf(quark, Display.getWithNewlines(quark.getName()), LeafType.STATE, null);
	}

	private String removeEquals(String code) {
		while (code.startsWith("="))
			code = code.substring(1);

		while (code.endsWith("="))
			code = code.substring(0, code.length() - 1);

		return code;
	}

}
