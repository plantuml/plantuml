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
 * Contribution   :  Serge Wenger
 * 
 *
 */
package net.sourceforge.plantuml.statediagram.command;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.abel.LinkArg;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.decoration.LinkDecor;
import net.sourceforge.plantuml.decoration.LinkType;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.statediagram.StateDiagram;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.utils.Direction;
import net.sourceforge.plantuml.utils.LineLocation;

abstract class CommandLinkStateCommon extends SingleLineCommand2<StateDiagram> {
	// ::remove folder when __HAXE__

	CommandLinkStateCommon(IRegex pattern) {
		super(pattern);
	}
	
	@Override
	public boolean isEligibleFor(ParserPass pass) {
		return pass == ParserPass.TWO;
	}


	protected static RegexLeaf getStatePattern(String name) {
		return new RegexLeaf(3,
				name, "([%pLN_.:]+|[%pLN_.:]+\\[H\\*?\\]|\\[\\*\\]|\\[H\\*?\\]|(?:==+)(?:[%pLN_.:]+)(?:==+))[%s]*(\\<\\<.*\\>\\>)?[%s]*(#\\w+)?");
	}

	@Override
	protected CommandExecutionResult executeArg(StateDiagram diagram, LineLocation location, RegexResult arg, ParserPass currentPass)
			throws NoSuchColorException {
		final String ent1 = arg.get("ENT1", 0);
		final String ent2 = arg.get("ENT2", 0);

		final Entity cl1 = getEntityStart(location, diagram, ent1);
		if (cl1 == null)
			return CommandExecutionResult
					.error("The state " + ent1 + " has been created in a concurrent state : it cannot be used here.");

		final Entity cl2 = getEntityEnd(location, diagram, ent2);
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

		final Display label = Display.getWithNewlines(diagram.getPragma(), arg.get("LABEL", 0));

		// Check if we should use node style for this transition
		final boolean useNodeStyle = shouldUseNodeStyle(diagram, arg);

		// Check if we have a non-empty label that should become an intermediate transition node
		if (useNodeStyle && label != null && !Display.isNull(label) && !label.toString().trim().isEmpty()) {
			// Create intermediate transition node for the label
			return createTransitionWithIntermediateNode(diagram, location, cl1, cl2, label, linkType, lenght, dir, arg);
		} else {
			// Original direct link behavior for unlabeled transitions or when node style is not requested
			final LinkArg linkArg = LinkArg.build(label, lenght, diagram.getSkinParam().classAttributeIconSize() > 0);
			Link link = new Link(location, diagram, diagram.getSkinParam().getCurrentStyleBuilder(), cl1, cl2,
					linkType, linkArg);
			if (dir == Direction.LEFT || dir == Direction.UP)
				link = link.getInv();

			link.applyStyle(arg.getLazzy("ARROW_STYLE", 0));
			diagram.addLink(link);

			return CommandExecutionResult.ok();
		}
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

	private Entity getEntityStart(LineLocation location, StateDiagram diagram, final String code) {
		if (code.startsWith("[*]"))
			return diagram.getStart(location);

		return getEntity(location, diagram, code);
	}

	private Entity getEntityEnd(LineLocation location, StateDiagram diagram, final String code) {
		if (code.startsWith("[*]"))
			return diagram.getEnd(location);

		return getEntity(location, diagram, code);
	}

	private Entity getEntity(LineLocation location, StateDiagram diagram, final String code) {
		if (code.equalsIgnoreCase("[H]"))
			return diagram.getHistorical(location);

		if (code.endsWith("[H]"))
			return diagram.getHistorical(location, code.substring(0, code.length() - 3));

		if (code.equalsIgnoreCase("[H*]"))
			return diagram.getDeepHistory(location);

		if (code.endsWith("[H*]"))
			return diagram.getDeepHistory(location, code.substring(0, code.length() - 4));

		if (code.startsWith("=") && code.endsWith("=")) {
			final String codeString1 = removeEquals(code);
			final Quark<Entity> quark = diagram.quarkInContext(true, diagram.cleanId(codeString1));
			if (quark.getData() != null)
				return quark.getData();
			return diagram.reallyCreateLeaf(location, quark, Display.getWithNewlines(quark), LeafType.SYNCHRO_BAR, null);
		}

		if (diagram.getCurrentGroup().getName().equals(code))
			return diagram.getCurrentGroup();

		final Quark<Entity> quark = diagram.quarkInContext(true, diagram.cleanId(code));
		if (diagram.checkConcurrentStateOk(quark) == false)
			return null;

		if (quark.getData() != null)
			return quark.getData();
		return diagram.reallyCreateLeaf(location, quark, Display.getWithNewlines(diagram.getPragma(), quark.getName()), LeafType.STATE, null);
	}

	private String removeEquals(String code) {
		while (code.startsWith("="))
			code = code.substring(1);

		while (code.endsWith("="))
			code = code.substring(0, code.length() - 1);

		return code;
	}

	private boolean shouldUseNodeStyle(StateDiagram diagram, RegexResult arg) {
		// First check if "node" keyword is in the arrow style (per-transition override)
		final String arrowStyle = arg.getLazzy("ARROW_STYLE", 0);
		if (arrowStyle != null && arrowStyle.toLowerCase().contains("node")) {
			return true;
		}

		// Fall back to global skinparam setting
		final String globalSetting = diagram.getSkinParam().getValue("statediagramedgelabelstyle");
		if (globalSetting != null && globalSetting.equalsIgnoreCase("node")) {
			return true;
		}

		// Default to normal (original behavior)
		return false;
	}

	private CommandExecutionResult createTransitionWithIntermediateNode(StateDiagram diagram, LineLocation location,
			Entity source, Entity target, Display label, LinkType linkType, int length, Direction dir, RegexResult arg)
			throws NoSuchColorException {

		// Generate unique ID for the transition node
		String transitionNodeId = generateTransitionNodeId(source, target, label);

		// Create the intermediate transition node
		final Quark<Entity> transitionQuark = diagram.quarkInContext(true, diagram.cleanId(transitionNodeId));
		final Entity transitionNode = diagram.reallyCreateLeaf(location, transitionQuark, label, LeafType.STATE_TRANSITION_LABEL, null);

		// Set transition node stereotype to make it visually distinct
		transitionNode.setStereotype(Stereotype.build("<<transition>>"));

		// STEP 1: Create invisible direct link from source to target for positioning
		// This ensures states are positioned based on their direct relationships
		// Use fixed length=3 to get minlen=2, providing minimal space for transition node between states
		final LinkArg positioningLinkArg = LinkArg.build(Display.NULL, 3, diagram.getSkinParam().classAttributeIconSize() > 0);
		Link positioningLink = new Link(location, diagram, diagram.getSkinParam().getCurrentStyleBuilder(), source, target,
				linkType, positioningLinkArg);
		positioningLink.setInvis(true);  // Make it invisible so it only affects layout
		positioningLink.setWeight(10.0);  // High weight to strongly influence positioning
		// Keep constraint=true (default) so it positions the states

		if (dir == Direction.LEFT || dir == Direction.UP)
			positioningLink = positioningLink.getInv();

		// STEP 2: Create first link: source -> transition node (no arrow decoration on intermediate link)
		// Use length=2 to get minlen=1, which places transition node at rank(source)+1
		final LinkType firstLinkType = new LinkType(LinkDecor.NONE, LinkDecor.NONE);
		final LinkArg firstLinkArg = LinkArg.build(Display.NULL, 2, diagram.getSkinParam().classAttributeIconSize() > 0);
		Link firstLink = new Link(location, diagram, diagram.getSkinParam().getCurrentStyleBuilder(), source, transitionNode,
				firstLinkType, firstLinkArg);
		// Keep constraint=true but use low weight so the invisible edge dominates horizontal positioning
		firstLink.setWeight(0.1);

		// STEP 3: Create second link: transition node -> target (with original arrow decoration)
		// Use length=2 to get minlen=1, placing target at rank(transition)+1 = rank(source)+2
		// This matches the invisible edge minlen=2
		final LinkArg secondLinkArg = LinkArg.build(Display.NULL, 2, diagram.getSkinParam().classAttributeIconSize() > 0);
		Link secondLink = new Link(location, diagram, diagram.getSkinParam().getCurrentStyleBuilder(), transitionNode, target,
				linkType, secondLinkArg);
		// Keep constraint=true so both edges participate in ranking consistently
		secondLink.setWeight(0.1);

		// Handle direction for visible links
		if (dir == Direction.LEFT || dir == Direction.UP) {
			firstLink = firstLink.getInv();
			secondLink = secondLink.getInv();
		}

		// Apply styles (but filter out "node" keyword to avoid issues)
		final String arrowStyle = arg.getLazzy("ARROW_STYLE", 0);
		if (arrowStyle != null) {
			// Remove "node" from the style string since it's not a visual style
			final String filteredStyle = arrowStyle.replaceAll("(?i),?node,?", "").replaceAll(",,", ",").replaceAll("^,|,$", "");
			if (!filteredStyle.isEmpty()) {
				firstLink.applyStyle(filteredStyle);
				secondLink.applyStyle(filteredStyle);
			}
		}

		// Add all links to the diagram: invisible positioning link first, then visible node-style links
		diagram.addLink(positioningLink);
		diagram.addLink(firstLink);
		diagram.addLink(secondLink);

		return CommandExecutionResult.ok();
	}

	private String generateTransitionNodeId(Entity source, Entity target, Display label) {
		// Generate a unique ID for the transition node based on source, target, and label
		String sourceId = source.getName();
		String targetId = target.getName();
		String labelText = label.toString().replaceAll("[^a-zA-Z0-9_]", "_");
		return "transition_" + sourceId + "_" + targetId + "_" + labelText + "_" + System.currentTimeMillis();
	}

}
