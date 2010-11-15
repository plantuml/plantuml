/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 5443 $
 *
 */
package net.sourceforge.plantuml.componentdiagram.command;

import java.util.Map;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexPartialMatch;
import net.sourceforge.plantuml.componentdiagram.ComponentDiagram;
import net.sourceforge.plantuml.cucadiagram.Group;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;

public class CommandLinkComponent2 extends SingleLineCommand2<ComponentDiagram> {

	public CommandLinkComponent2(ComponentDiagram diagram) {
		super(diagram, getRegex());
	}

	static RegexConcat getRegex() {
		return new RegexConcat(new RegexLeaf("^"), getRegexGroup("G1"), new RegexLeaf("\\s*"), new RegexOr(
				new RegexLeaf("AR_TO_RIGHT",
						"(([-=.]+)(left|right|up|down|le?|ri?|up?|do?)?([-=.]*?\\.*)([\\]>]|\\|[>\\]])?)"),
				new RegexLeaf("AR_TO_LEFT",
						"(([\\[<]|[<\\[]\\|)?([-=.]*)(left|right|up|down|le?|ri?|up?|do?)?([-=.]+))")),
				new RegexLeaf("\\s*"), getRegexGroup("G2"), new RegexLeaf("\\s*"), new RegexLeaf("END",
						"(?::\\s*([^\"]+))?$"));
	}

	private static RegexLeaf getRegexGroup(String name) {
		return new RegexLeaf(name,
				"([\\p{L}0-9_.]+|:[^:]+:|\\[[^\\]*]+[^\\]]*\\]|\\(\\)\\s*[\\p{L}0-9_.]+|\\(\\)\\s*\"[^\"]+\")");
	}

	@Override
	protected CommandExecutionResult executeArg(Map<String, RegexPartialMatch> arg) {
		final String g1 = arg.get("G1").get(0);
		final String g2 = arg.get("G2").get(0);

		if (getSystem().isGroup(g1) && getSystem().isGroup(g2)) {
			return executePackageLink(arg);
		}
		if (getSystem().isGroup(g1) || getSystem().isGroup(g2)) {
			return CommandExecutionResult.error("Package can be only linked to other package");
		}

		final IEntity cl1 = getSystem().getOrCreateClass(g1);
		final IEntity cl2 = getSystem().getOrCreateClass(g2);

		final LinkType linkType;
		String queue;
		if (arg.get("AR_TO_RIGHT").get(0) != null) {
			queue = arg.get("AR_TO_RIGHT").get(1) + arg.get("AR_TO_RIGHT").get(3);
			linkType = getLinkTypeNormal(queue, arg.get("AR_TO_RIGHT").get(4));
		} else {
			queue = arg.get("AR_TO_LEFT").get(2) + arg.get("AR_TO_LEFT").get(4);
			linkType = getLinkTypeNormal(queue, arg.get("AR_TO_LEFT").get(1)).getInv();
		}
		final Direction dir = getDirection(arg);
		
		if (dir == Direction.LEFT || dir == Direction.RIGHT) {
			queue = "-";
		}

		Link link = new Link(cl1, cl2, linkType, arg.get("END").get(0), queue.length());
		
		if (dir == Direction.LEFT || dir == Direction.UP) {
			link = link.getInv();
		}

		
		getSystem().addLink(link);
		return CommandExecutionResult.ok();
	}
	
	private Direction getDirection(Map<String, RegexPartialMatch> arg) {
		if (arg.get("AR_TO_RIGHT").get(2) != null) {
			return StringUtils.getQueueDirection(arg.get("AR_TO_RIGHT").get(2));
		}
		if (arg.get("AR_TO_LEFT").get(3) != null) {
			return StringUtils.getQueueDirection(arg.get("AR_TO_LEFT").get(3)).getInv();
		}
		return null;
	}



	private CommandExecutionResult executePackageLink(Map<String, RegexPartialMatch> arg) {
		final String g1 = arg.get("G1").get(0);
		final String g2 = arg.get("G2").get(0);

		final Group cl1 = getSystem().getGroup(g1);
		final Group cl2 = getSystem().getGroup(g2);

		final LinkType linkType;
		final String queue;
		if (arg.get("AR_TO_RIGHT").get(0) != null) {
			queue = arg.get("AR_TO_RIGHT").get(1) + arg.get("AR_TO_RIGHT").get(3);
			linkType = getLinkTypeNormal(queue, arg.get("AR_TO_RIGHT").get(4));
		} else {
			queue = arg.get("AR_TO_LEFT").get(2) + arg.get("AR_TO_LEFT").get(4);
			linkType = getLinkTypeNormal(queue, arg.get("AR_TO_LEFT").get(1)).getInv();

		}

		final Link link = new Link(cl1.getEntityCluster(), cl2.getEntityCluster(), linkType, arg.get("END").get(0),
				queue.length());
		getSystem().addLink(link);
		return CommandExecutionResult.ok();
	}

	private LinkType getLinkTypeNormal(String queue, String key) {
		LinkType linkType = getLinkTypeFromKey(key);

		if (queue.startsWith(".")) {
			linkType = linkType.getDashed();
		}
		return linkType;
	}

	private LinkType getLinkTypeFromKey(String k) {
		if (k == null) {
			return new LinkType(LinkDecor.NONE, LinkDecor.NONE);
		}
		if (k.equals("<") || k.equals(">")) {
			return new LinkType(LinkDecor.ARROW, LinkDecor.NONE);
		}
		if (k.equals("<|") || k.equals("|>")) {
			return new LinkType(LinkDecor.EXTENDS, LinkDecor.NONE);
		}
		return null;
	}

}
