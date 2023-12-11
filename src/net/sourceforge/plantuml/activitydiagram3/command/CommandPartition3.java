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
package net.sourceforge.plantuml.activitydiagram3.command;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.activitydiagram3.ActivityDiagram3;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.FtileGroup;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.decoration.symbol.USymbol;
import net.sourceforge.plantuml.decoration.symbol.USymbols;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandPartition3 extends SingleLineCommand2<ActivityDiagram3> {

	public CommandPartition3() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandPartition3.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("TYPE", "(partition|package|rectangle|card|group)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexOptional(//
						new RegexConcat( //
								color("BACK1").getRegex(), //
								RegexLeaf.spaceOneOrMore())), //
				new RegexLeaf("NAME", "([%g][^%g]+[%g]|.*?)"), //
				new RegexOptional(//
						new RegexConcat( //
								RegexLeaf.spaceOneOrMore(), //
								color("BACK2").getRegex())), //
				StereotypePattern.optional("STEREO"), //
				new RegexLeaf("\\{?"), //
				RegexLeaf.end());
	}

	private USymbol getUSymbol(String type) {
		if ("partition".equalsIgnoreCase(type))
			return USymbols.PARTITION;

		if ("package".equalsIgnoreCase(type))
			return USymbols.PACKAGE;

		if ("rectangle".equalsIgnoreCase(type))
			return USymbols.RECTANGLE;

		if ("card".equalsIgnoreCase(type))
			return USymbols.CARD;

		if ("group".equalsIgnoreCase(type))
			return USymbols.GROUP;

		throw new IllegalArgumentException();
	}

	private static ColorParser color(String id) {
		return ColorParser.simpleColor(ColorType.BACK, id);
	}

	@Override
	protected CommandExecutionResult executeArg(ActivityDiagram3 diagram, LineLocation location, RegexResult arg)
			throws NoSuchColorException {
		final String partitionTitle = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("NAME", 0));

		final String b1 = arg.get("BACK1", 0);
		final Colors colors = color(b1 == null ? "BACK2" : "BACK1").getColor(arg,
				diagram.getSkinParam().getIHtmlColorSet());

		final USymbol symbol = getUSymbol(arg.get("TYPE", 0));
		final String stereo = arg.get("STEREO", 0);
		final Stereotype stereotype = stereo == null ? null : Stereotype.build(stereo);

		// Warning : titleColor unused in FTileGroupW

		final Style stylePartition = FtileGroup.getStyleSignature(symbol).withTOBECHANGED(stereotype)
				.getMergedStyle(diagram.getSkinParam().getCurrentStyleBuilder());
		HColor backColor = colors.getColor(ColorType.BACK);
		if (backColor == null)
			backColor = stylePartition.value(PName.BackGroundColor).asColor(diagram.getSkinParam().getIHtmlColorSet());

//		final HColor borderColor = stylePartition.value(PName.LineColor).asColor(diagram.getSkinParam().getIHtmlColorSet());
//		final HColor titleColor = HColors.BLUE;// stylePartition.value(PName.FontColor).asColor(diagram.getSkinParam().getIHtmlColorSet());
//		final double roundCorner = stylePartition.value(PName.RoundCorner).asDouble();

		diagram.startGroup(Display.getWithNewlines(partitionTitle), backColor, symbol, stylePartition);

		return CommandExecutionResult.ok();
	}

}
