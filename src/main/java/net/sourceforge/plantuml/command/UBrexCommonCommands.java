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
package net.sourceforge.plantuml.command;

import java.util.List;

import net.sourceforge.plantuml.classdiagram.command.CommandHideShowByGender;
import net.sourceforge.plantuml.classdiagram.command.CommandHideShowByVisibility;
import net.sourceforge.plantuml.classdiagram.command.CommandNamespaceSeparator;
import net.sourceforge.plantuml.sequencediagram.command.CommandSkin;
import net.sourceforge.plantuml.statediagram.command.CommandHideEmptyDescription;
import net.sourceforge.plantuml.style.CommandStyleImport;
import net.sourceforge.plantuml.style.CommandStyleMultilinesCSS;

public final class UBrexCommonCommands {

	private UBrexCommonCommands() {
	}

	static public void addCommonCommands1(List<Command> cmds) {
		addTitleCommands(cmds);
		addCommonCommands2(cmds);
		addCommonHides(cmds);
	}

	static public void addCommonCommands2(List<Command> cmds) {
		cmds.add(CommandNope.ME);
		cmds.add(CommandPragma.ME);
		cmds.add(CommandAssumeTransparent.ME);

		cmds.add(CommandSkinParam.ME);
		cmds.add(CommandSkinParamJaws.ME);
		cmds.add(CommandSkinParamMultilines.ME);

		cmds.add(CommandSkin.ME);
		cmds.add(CommandMinwidth.ME);
		cmds.add(CommandPage.ME);
		cmds.add(CommandRotate.ME);
		CommonCommands.addCommonScaleCommands(cmds);

		final CommandFactorySprite factorySpriteCommand = CommandFactorySprite.ME;
		cmds.add(factorySpriteCommand.createMultiLine(false));
		cmds.add(factorySpriteCommand.createSingleLine());
		cmds.add(CommandSpriteSvg.ME);
		cmds.add(CommandSpriteStdlib.ME);
		cmds.add(CommandSpriteStdlibSvg.ME);
		cmds.add(CommandSpriteSvgMultiline.ME);
		cmds.add(CommandSpriteFile.ME);

		cmds.add(CommandStyleMultilinesCSS.ME);
		cmds.add(CommandStyleImport.ME);

	}

	static public void addCommonHides(List<Command> cmds) {
		cmds.add(CommandHideEmptyDescription.ME);
		cmds.add(CommandHideShowByVisibility.ME);
		cmds.add(CommandHideShowByGender.ME);
	}

	static public void addTitleCommands(List<Command> cmds) {
		cmds.add(CommandTitle.ME);
		cmds.add(CommandMainframe.ME);
		cmds.add(CommandCaption.ME);
		cmds.add(CommandMultilinesCaption.ME);
		cmds.add(CommandMultilinesTitle.ME);
		cmds.add(CommandMultilinesLegend.ME);
		cmds.add(CommandLegend.ME);

		cmds.add(CommandFooter.ME);
		cmds.add(CommandMultilinesFooter.ME);

		cmds.add(CommandHeader.ME);
		cmds.add(CommandMultilinesHeader.ME);
		cmds.add(CommandNamespaceSeparator.ME);
	}

}
