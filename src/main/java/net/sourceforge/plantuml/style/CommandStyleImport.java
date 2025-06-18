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
package net.sourceforge.plantuml.style;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.security.SURL;
import net.sourceforge.plantuml.style.parser.StyleParser;
import net.sourceforge.plantuml.style.parser.StyleParsingException;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandStyleImport extends SingleLineCommand2<TitledDiagram> {
	// ::remove file when __HAXE__

	public static final CommandStyleImport ME = new CommandStyleImport();

	private CommandStyleImport() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandStyleImport.class.getName(), //
				RegexLeaf.start(), //
				new RegexLeaf("\\<style"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("\\w+"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("="), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("[%q%g]?"), //
				new RegexLeaf(1, "PATH", "([^%q%g]*)"), //
				new RegexLeaf("[%q%g]?"), //
				new RegexLeaf("\\>"), RegexLeaf.end()); //
	}

	@Override
	protected CommandExecutionResult executeArg(TitledDiagram diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) {
		final String path = arg.get("PATH", 0);
		try {
			BlocLines lines = null;
			if (path.startsWith("http://") || path.startsWith("https://")) {
				SURL url = SURL.create(path);
				try (InputStream remoteInputStream = url.openStream()) {
					if (remoteInputStream != null)
						lines = BlocLines.load(remoteInputStream, location);
				}
			} else {
				final SFile styleFile = FileSystem.getInstance().getFile(path);
				if (styleFile.exists()) {
					lines = BlocLines.load(styleFile, location);
				} else {
					final InputStream internalIs = StyleLoader.class.getResourceAsStream("/skin/" + path);
					if (internalIs != null)
						lines = BlocLines.load(internalIs, location);
				}
			}

			if (lines == null || lines.size() == 0)
				return CommandExecutionResult.error("Cannot read: " + path);

			final StyleBuilder styleBuilder = diagram.getSkinParam().getCurrentStyleBuilder();
			diagram.getSkinParam().muteStyle(StyleParser.parse(lines, styleBuilder));

		} catch (MalformedURLException e) {
			return CommandExecutionResult.error("Invalid URL to style definition: " + e.getMessage());
		} catch (StyleParsingException e) {
			return CommandExecutionResult.error("Error in style definition: " + e.getMessage());
		} catch (IOException e) {
			return CommandExecutionResult.error("Cannot read: " + path);
		}
		return CommandExecutionResult.ok();
	}
}
