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

import java.util.List;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.componentdiagram.ComponentDiagram;
import net.sourceforge.plantuml.cucadiagram.Group;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;

public class CommandLinkComponent extends SingleLineCommand<ComponentDiagram> {

	public CommandLinkComponent(ComponentDiagram diagram) {
		super(
				diagram,
				"(?i)^([\\p{L}0-9_.]+|:[^:]+:|\\[[^\\]*]+[^\\]]*\\]|\\(\\)\\s*[\\p{L}0-9_.]+|\\(\\)\\s*\"[^\"]+\")\\s*"
						+ "(?:("
						+ "([=-]+(?:left|right|up|down|le?|ri?|up?|do?)?[=-]*|\\.+(?:left|right|up|down|le?|ri?|up?|do?)?\\.*)([\\]>]|\\|[>\\]])?"
						+ ")|("
						+ "([\\[<]|[<\\[]\\|)?([=-]*(?:left|right|up|down|le?|ri?|up?|do?)?[=-]+|\\.*(?:left|right|up|down|le?|ri?|up?|do?)?\\.+)"
						+ "))"
						+ "\\s*([\\p{L}0-9_.]+|:[^:]+:|\\[[^\\]*]+[^\\]]*\\]|\\(\\)\\s*[\\p{L}0-9_.]+|\\(\\)\\s*\"[^\"]+\")\\s*(?::\\s*([^\"]+))?$");
	}

	@Override
	protected CommandExecutionResult executeArg(List<String> arg) {
		if (getSystem().isGroup(arg.get(0)) && getSystem().isGroup(arg.get(7))) {
			return executePackageLink(arg);
		}
		if (getSystem().isGroup(arg.get(0)) || getSystem().isGroup(arg.get(7))) {
			return CommandExecutionResult.error("Package can be only linked to other package");
		}

		final IEntity cl1 = getSystem().getOrCreateClass(arg.get(0));
		final IEntity cl2 = getSystem().getOrCreateClass(arg.get(7));

		final LinkType linkType = arg.get(1) != null ? getLinkTypeNormal(arg) : getLinkTypeInv(arg);
		final String queue = arg.get(1) != null ? arg.get(2) : arg.get(6);

		final Link link = new Link(cl1, cl2, linkType, arg.get(8), queue.length());
		getSystem().addLink(link);
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executePackageLink(List<String> arg) {
		final Group cl1 = getSystem().getGroup(arg.get(0));
		final Group cl2 = getSystem().getGroup(arg.get(7));

		final LinkType linkType = arg.get(1) != null ? getLinkTypeNormal(arg) : getLinkTypeInv(arg);
		final String queue = arg.get(1) != null ? arg.get(2) : arg.get(6);

		final Link link = new Link(cl1.getEntityCluster(), cl2.getEntityCluster(), linkType, arg.get(8), queue.length());
		getSystem().addLink(link);
		return CommandExecutionResult.ok();
	}

	private LinkType getLinkTypeNormal(List<String> arg) {
		final String queue = arg.get(2);
		final String key = arg.get(3);
		LinkType linkType = getLinkTypeFromKey(key);

		if (queue.startsWith(".")) {
			linkType = linkType.getDashed();
		}
		return linkType;
	}

	private LinkType getLinkTypeInv(List<String> arg) {
		final String queue = arg.get(6);
		final String key = arg.get(5);
		LinkType linkType = getLinkTypeFromKey(key);

		if (queue.startsWith(".")) {
			linkType = linkType.getDashed();
		}
		return linkType.getInv();
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
