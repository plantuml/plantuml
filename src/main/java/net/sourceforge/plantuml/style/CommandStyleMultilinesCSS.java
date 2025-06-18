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

import net.sourceforge.plantuml.Lazy;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.Trim;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.Pattern2;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.skin.SkinParam;
import net.sourceforge.plantuml.style.parser.StyleParser;
import net.sourceforge.plantuml.style.parser.StyleParsingException;
import net.sourceforge.plantuml.utils.BlocLines;

public class CommandStyleMultilinesCSS extends CommandMultilines2<TitledDiagram> {
	// ::remove file when __HAXE__

	private final static Lazy<Pattern2> END = new Lazy<>(() -> Pattern2.cmpile("^[%s]*\\</?style\\>[%s]*$"));

	public static final CommandStyleMultilinesCSS ME = new CommandStyleMultilinesCSS();

	private CommandStyleMultilinesCSS() {
		super(getRegexConcat(), MultilinesStrategy.REMOVE_STARTING_QUOTE, Trim.BOTH, END);
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandStyleMultilinesCSS.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("\\<style\\>"), //
				RegexLeaf.end() //
		);
	}

	@Override
	protected CommandExecutionResult executeNow(TitledDiagram diagram, BlocLines lines, ParserPass currentPass) {
		try {
			final StyleBuilder styleBuilder = diagram.getSkinParam().getCurrentStyleBuilder();
			diagram.getSkinParam().muteStyle(StyleParser.parse(lines.subExtract(1, 1), styleBuilder));

			((SkinParam) diagram.getSkinParam()).applyPendingStyleMigration();
			return CommandExecutionResult.ok();
		} catch (StyleParsingException e) {
			return CommandExecutionResult.error("Error in style definition: " + e.getMessage());
		} catch (NoStyleAvailableException e) {
			// Logme.error(e);
			return CommandExecutionResult.error("General failure: no style available.");
		}
	}

}
