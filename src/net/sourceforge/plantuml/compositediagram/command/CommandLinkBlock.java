/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
 */
package net.sourceforge.plantuml.compositediagram.command;

import java.util.List;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.compositediagram.CompositeDiagram;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;

public class CommandLinkBlock extends SingleLineCommand<CompositeDiagram> {

	public CommandLinkBlock() {
		super("(?i)^([\\p{L}0-9_.]+)[%s]*(\\[\\]|\\*\\))?([=-]+|\\.+)(\\[\\]|\\(\\*)?[%s]*([\\p{L}0-9_.]+)[%s]*(?::[%s]*(\\S*+))?$");
	}

	@Override
	protected CommandExecutionResult executeArg(CompositeDiagram diagram, List<String> arg) {
		final IEntity cl1 = diagram.getOrCreateLeaf(Code.of(arg.get(0)), null, null);
		final IEntity cl2 = diagram.getOrCreateLeaf(Code.of(arg.get(4)), null, null);

		final String deco1 = arg.get(1);
		final String deco2 = arg.get(3);
		LinkType linkType = new LinkType(getLinkDecor(deco1), getLinkDecor(deco2));
		
		if ("*)".equals(deco1)) {
			linkType = linkType.getInterfaceProvider();
		} else if ("(*".equals(deco2)) {
			linkType = linkType.getInterfaceUser();
		}

		final String queue = arg.get(2);

		final Link link = new Link(cl1, cl2, linkType, Display.getWithNewlines(arg.get(5)), queue.length());
		diagram.addLink(link);
		return CommandExecutionResult.ok();
	}

	private LinkDecor getLinkDecor(String s) {
		if ("[]".equals(s)) {
			return LinkDecor.SQUARRE_toberemoved;
		}
		return LinkDecor.NONE;
	}

}
