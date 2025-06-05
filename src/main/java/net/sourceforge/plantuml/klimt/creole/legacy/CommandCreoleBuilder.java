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
package net.sourceforge.plantuml.klimt.creole.legacy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.command.Command;
import net.sourceforge.plantuml.klimt.creole.command.CommandCreoleColorAndSizeChange;
import net.sourceforge.plantuml.klimt.creole.command.CommandCreoleColorChange;
import net.sourceforge.plantuml.klimt.creole.command.CommandCreoleEmoji;
import net.sourceforge.plantuml.klimt.creole.command.CommandCreoleExposantChange;
import net.sourceforge.plantuml.klimt.creole.command.CommandCreoleFontFamilyChange;
import net.sourceforge.plantuml.klimt.creole.command.CommandCreoleImg;
import net.sourceforge.plantuml.klimt.creole.command.CommandCreoleLatex;
import net.sourceforge.plantuml.klimt.creole.command.CommandCreoleMath;
import net.sourceforge.plantuml.klimt.creole.command.CommandCreoleMonospaced;
import net.sourceforge.plantuml.klimt.creole.command.CommandCreoleOpenIcon;
import net.sourceforge.plantuml.klimt.creole.command.CommandCreoleQrcode;
import net.sourceforge.plantuml.klimt.creole.command.CommandCreoleSizeChange;
import net.sourceforge.plantuml.klimt.creole.command.CommandCreoleSpace;
import net.sourceforge.plantuml.klimt.creole.command.CommandCreoleSprite;
import net.sourceforge.plantuml.klimt.creole.command.CommandCreoleStyle;
import net.sourceforge.plantuml.klimt.creole.command.CommandCreoleSvgAttributeChange;
import net.sourceforge.plantuml.klimt.creole.command.CommandCreoleUrl;
import net.sourceforge.plantuml.klimt.font.FontPosition;
import net.sourceforge.plantuml.klimt.font.FontStyle;
import net.sourceforge.plantuml.security.SecurityUtils;

public class CommandCreoleBuilder {

	final private Map<Character, List<Command>> commands = new HashMap<>();

	public static CommandCreoleBuilder FULL = new CommandCreoleBuilder(CreoleMode.FULL);
	public static CommandCreoleBuilder OTHER = new CommandCreoleBuilder(null);

	private CommandCreoleBuilder(CreoleMode modeSimpleLine) {

		addCommand(CommandCreoleStyle.createCreole(FontStyle.BOLD));
		addCommand(CommandCreoleStyle.createLegacy(FontStyle.BOLD));
		addCommand(CommandCreoleStyle.createLegacyEol(FontStyle.BOLD));

		addCommand(CommandCreoleStyle.createCreole(FontStyle.ITALIC));
		addCommand(CommandCreoleStyle.createLegacy(FontStyle.ITALIC));
		addCommand(CommandCreoleStyle.createLegacyEol(FontStyle.ITALIC));
		addCommand(CommandCreoleStyle.createLegacy(FontStyle.PLAIN));
		addCommand(CommandCreoleStyle.createLegacyEol(FontStyle.PLAIN));
		if (modeSimpleLine == CreoleMode.FULL)
			addCommand(CommandCreoleStyle.createCreole(FontStyle.UNDERLINE));

		addCommand(CommandCreoleStyle.createLegacy(FontStyle.UNDERLINE));
		addCommand(CommandCreoleStyle.createLegacyEol(FontStyle.UNDERLINE));
		addCommand(CommandCreoleStyle.createCreole(FontStyle.STRIKE));
		addCommand(CommandCreoleStyle.createLegacy(FontStyle.STRIKE));
		addCommand(CommandCreoleStyle.createLegacyEol(FontStyle.STRIKE));
		addCommand(CommandCreoleStyle.createCreole(FontStyle.WAVE));
		addCommand(CommandCreoleStyle.createLegacy(FontStyle.WAVE));
		addCommand(CommandCreoleStyle.createLegacyEol(FontStyle.WAVE));
		addCommand(CommandCreoleStyle.createLegacy(FontStyle.BACKCOLOR));
		addCommand(CommandCreoleStyle.createLegacyEol(FontStyle.BACKCOLOR));
		addCommand(CommandCreoleSizeChange.create());
		addCommand(CommandCreoleSizeChange.createEol());
		addCommand(CommandCreoleColorChange.create());
		addCommand(CommandCreoleColorChange.createEol());
		addCommand(CommandCreoleColorAndSizeChange.create());
		addCommand(CommandCreoleColorAndSizeChange.createEol());
		addCommand(CommandCreoleExposantChange.create(FontPosition.EXPOSANT));
		addCommand(CommandCreoleExposantChange.create(FontPosition.INDICE));
		addCommand(CommandCreoleImg.create());
		addCommand(CommandCreoleQrcode.create());
		addCommand(CommandCreoleOpenIcon.create());
		addCommand(CommandCreoleEmoji.create());
		// ::comment when __CORE__
		addCommand(CommandCreoleMath.create());
		addCommand(CommandCreoleLatex.create());
		// ::done
		addCommand(CommandCreoleSprite.create());
		addCommand(CommandCreoleSpace.create());
		addCommand(CommandCreoleFontFamilyChange.create());
		addCommand(CommandCreoleFontFamilyChange.createEol());
		addCommand(CommandCreoleMonospaced.create());
		addCommand(CommandCreoleUrl.create());
		// ::comment when __CORE__
		if (SecurityUtils.allowSvgText())
			addCommand(CommandCreoleSvgAttributeChange.create());
		// ::done

	}

	private void addCommand(Command cmd) {
		final String starters = cmd.startingChars();
		for (int i = 0; i < starters.length(); i++) {
			final char ch = starters.charAt(i);
			List<Command> localList = commands.get(ch);
			if (localList == null) {
				localList = new ArrayList<Command>();
				commands.put(ch, localList);
			}
			localList.add(cmd);
		}
	}

	public Map<Character, List<Command>> getMap() {
		return commands;
	}

}
