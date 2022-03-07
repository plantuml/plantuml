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
 * 
 *
 */
package net.sourceforge.plantuml.command;

import java.util.List;

import net.sourceforge.plantuml.classdiagram.command.CommandHideShowByGender;
import net.sourceforge.plantuml.classdiagram.command.CommandHideShowByVisibility;
import net.sourceforge.plantuml.sequencediagram.command.CommandSkin;
import net.sourceforge.plantuml.statediagram.command.CommandHideEmptyDescription;
import net.sourceforge.plantuml.style.CommandStyleImport;
import net.sourceforge.plantuml.style.CommandStyleMultilinesCSS;

public final class CommonCommands {

	private CommonCommands() {
	}

	static public void addCommonCommands1(List<Command> cmds) {
		addTitleCommands(cmds);
		addCommonCommands2(cmds);
		addCommonHides(cmds);
	}

	static public void addCommonCommands2(List<Command> cmds) {
		cmds.add(new CommandNope());
		cmds.add(new CommandPragma());
		cmds.add(new CommandAssumeTransparent());

		cmds.add(new CommandSkinParam());
		cmds.add(new CommandSkinParamMultilines());
		cmds.add(new CommandSkin());
		cmds.add(new CommandMinwidth());
		cmds.add(new CommandPage());
		cmds.add(new CommandRotate());
		cmds.add(new CommandScale());
		cmds.add(new CommandScaleWidthAndHeight());
		cmds.add(new CommandScaleWidthOrHeight());
		cmds.add(new CommandScaleMaxWidth());
		cmds.add(new CommandScaleMaxHeight());
		cmds.add(new CommandScaleMaxWidthAndHeight());
		cmds.add(new CommandAffineTransform());
		cmds.add(new CommandAffineTransformMultiline());
		final CommandFactorySprite factorySpriteCommand = new CommandFactorySprite();
		cmds.add(factorySpriteCommand.createMultiLine(false));
		cmds.add(factorySpriteCommand.createSingleLine());
		cmds.add(new CommandSpriteFile());

		cmds.add(new CommandStyleMultilinesCSS());
		cmds.add(new CommandStyleImport());

	}

	static public void addCommonHides(List<Command> cmds) {
		cmds.add(new CommandHideEmptyDescription());
		cmds.add(new CommandHideShowByVisibility());
		cmds.add(new CommandHideShowByGender());
	}

	static public void addTitleCommands(List<Command> cmds) {
		cmds.add(new CommandTitle());
		cmds.add(new CommandMainframe());
		cmds.add(new CommandCaption());
		cmds.add(new CommandMultilinesCaption());
		cmds.add(new CommandMultilinesTitle());
		cmds.add(new CommandMultilinesLegend());
		cmds.add(new CommandLegend());

		cmds.add(new CommandFooter());
		cmds.add(new CommandMultilinesFooter());

		cmds.add(new CommandHeader());
		cmds.add(new CommandMultilinesHeader());
	}

}
