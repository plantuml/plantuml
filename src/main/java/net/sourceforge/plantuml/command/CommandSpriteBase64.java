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

import java.io.IOException;

import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.annotation.Explain;
import net.sourceforge.plantuml.klimt.sprite.SpriteImage;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.security.SImageIO;
import net.sourceforge.plantuml.utils.Base64Coder;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandSpriteBase64 extends SingleLineCommand2<TitledDiagram> {

	public static final CommandSpriteBase64 ME = new CommandSpriteBase64();

	private CommandSpriteBase64() {
		super(getRegexConcat());
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandSpriteBase64.class.getName(), //
				RegexLeaf.start(), //
				new RegexLeaf("sprite"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("\\$?"), //
				new RegexLeaf(1, "NAME", "([-.%pLN_]+)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("data:image/png;base64,"), //
				new RegexLeaf(1, "DATA", "([A-Za-z0-9+/=]+)"), //
				RegexLeaf.end());
	}

	@Override
	@Explain
	protected String explainArg(LineLocation location, RegexResult arg) {
		// Defines a sprite from an inline PNG image encoded as a data URI;
		// executeArg fails if the Base64 payload is not a decodable PNG.
		return "Defining the sprite '" + arg.get("NAME", 0) + "' from an inline Base64 encoded PNG image ("
				+ arg.get("DATA", 0).length() + " characters)";
	}

	@Override
	protected CommandExecutionResult executeArg(TitledDiagram system, LineLocation location, RegexResult arg,
			ParserPass currentPass) {
		final String base64 = arg.get("DATA", 0);
		final byte[] bytes = Base64Coder.decode(base64);
		try {
			system.addSprite(arg.get("NAME", 0), new SpriteImage(SImageIO.read(bytes)));
			return CommandExecutionResult.ok();
		} catch (IOException e) {
			return CommandExecutionResult.error("Cannot decode Base64 PNG sprite.");
		}
	}

}
