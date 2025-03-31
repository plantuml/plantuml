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
package net.sourceforge.plantuml.activitydiagram.command;

import com.plantuml.ubrex.UnicodeBracketedExpression;
import com.plantuml.ubrex.builder.UBrexConcat;
import com.plantuml.ubrex.builder.UBrexLeaf;
import com.plantuml.ubrex.builder.UBrexNamed;
import com.plantuml.ubrex.builder.UBrexOptional;
import com.plantuml.ubrex.builder.UBrexOr;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.abel.LinkArg;
import net.sourceforge.plantuml.activitydiagram.ActivityDiagram;
import net.sourceforge.plantuml.classdiagram.command.CommandLinkClass;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.UBrexSingleLineCommand2;
import net.sourceforge.plantuml.decoration.LinkDecor;
import net.sourceforge.plantuml.decoration.LinkType;
import net.sourceforge.plantuml.descdiagram.command.CommandLinkElement;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.utils.Direction;
import net.sourceforge.plantuml.utils.LineLocation;

public class UBrexCommandIf extends UBrexSingleLineCommand2<ActivityDiagram> {

	public UBrexCommandIf() {
		super(getRegexConcat());
	}

	static UnicodeBracketedExpression getRegexConcat() {
		final UBrexConcat simple = UBrexConcat.build( //
				UBrexLeaf.spaceZeroOrMore(), //
				new UBrexOptional(UBrexConcat.build( //
						new UBrexNamed("ARROW_BODY1", new UBrexLeaf("〇+「-.」")), //
						new UBrexLeaf("〇?〘 [ 〶$ARROW_STYLE1=〘" + CommandLinkElement.UBREX_LINE_STYLE + "〙] 〙"), //
						new UBrexNamed("ARROW_DIRECTION",
								new UBrexLeaf("〇?【 *┇left┇right┇up┇down┇l〇?e┇r〇?i┇u〇?p┇d〇?o】")), //
						new UBrexLeaf("〇?〘 [  〶$ARROW_STYLE2=〘" + CommandLinkElement.UBREX_LINE_STYLE + "〙] 〙"), //
						new UBrexNamed("ARROW_BODY2", new UBrexLeaf("〇*「-.」")), //
						new UBrexLeaf(">") //
				)), // "02onStart()" --> if "" then
				UBrexLeaf.spaceZeroOrMore(), //
				new UBrexOptional(new UBrexLeaf("[ 〶$BRACKET=〘〇+「〤]*」  〇*「〤]」〙 ]")), UBrexLeaf.spaceZeroOrMore(), //
				new UBrexOr(//
						new UBrexLeaf("if 〇*〴s 〴g  〶$IF1=〇*〴G 〴g 〇*〴s 〇?〘as 〇+〴s 〶$ASIF1=〇+「〴an_.」 〇+〴s 〙〘then〙"), //
						new UBrexLeaf("if 〇+〴s 〶$IF2=〇l+〴. 〘then〙 "), //
						new UBrexLeaf("if 〇*〴s 〴g  〶$IF1=〇*〴G 〴g 〇*〴s 〇?〘as 〇+〴s 〶$ASIF1=〇+「〴an_.」 〇+〴s 〙"), //
						new UBrexLeaf("if 〇+〴s 〶$IF2=〇+〴.") //
				));

		return UBrexConcat.build( //
				new UBrexOr(simple, //
						UBrexConcat.build(new UBrexNamed("STAR", new UBrexLeaf("(* 〇?〘top〙)")), simple), //
						UBrexConcat.build(new UBrexNamed("BAR", new UBrexLeaf("=〇+= 〇*〴s 〇+「〴an_.」〇*〴s =〇+=")), simple), //
						UBrexConcat.build(new UBrexLeaf("〴g 〶$QUOTED1=〇*〴G 〴g 〇?〘〇+〴s as 〇+〴s 〶$QUOTED2=〇+「〴an_.」 〙 "),
								simple),
						UBrexConcat.build(new UBrexNamed("CODE", new UBrexLeaf("〇+「〴an_.」")), simple) //
				), //
				UBrexLeaf.end()); //
	}

	@Override
	protected CommandExecutionResult executeArg(ActivityDiagram diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) {
		final Entity entity1 = CommandLinkActivity.ubrexGetEntityForIfOnly(location, diagram, arg);
		if (entity1 == null)
			return CommandExecutionResult.error("No if possible at this point");

		final String ifCode;
		final String ifLabel;
		if (arg.get("IF2", 0) == null) {
			ifCode = arg.get("ASIF1", 0);
			ifLabel = arg.get("IF1", 0);
		} else {
			ifCode = null;
			ifLabel = arg.get("IF2", 0);
		}
		diagram.startIf(location, ifCode);

		int lenght = 2;

		if (arg.get("ARROW_BODY1", 0) != null) {
//			final String arrow = StringUtils.manageArrowForCuca(arg.get("ARROW", 0));
//			lenght = arrow.length() - 1;
			final String arrowBody1 = CommandLinkClass.notNull(arg.get("ARROW_BODY1", 0));
			final String arrowBody2 = CommandLinkClass.notNull(arg.get("ARROW_BODY2", 0));
			final String arrowDirection = CommandLinkClass.notNull(arg.get("ARROW_DIRECTION", 0));

			final String arrow = StringUtils.manageArrowForCuca(arrowBody1 + arrowDirection + arrowBody2 + ">");
			lenght = arrow.length() - 1;
		}

		final Entity branch = diagram.getCurrentContext().getBranch();

		final LinkArg linkArg = LinkArg.build(Display.getWithNewlines(diagram.getPragma(), arg.get("BRACKET", 0)),
				lenght);
		Link link = new Link(location, diagram, diagram.getSkinParam().getCurrentStyleBuilder(), entity1, branch,
				new LinkType(LinkDecor.ARROW, LinkDecor.NONE), linkArg.withQuantifier(null, ifLabel)
						.withDistanceAngle(diagram.getLabeldistance(), diagram.getLabelangle()));
		if (arg.get("ARROW", 0) != null) {
			final Direction direction = StringUtils.getArrowDirection(arg.get("ARROW", 0));
			if (direction == Direction.LEFT || direction == Direction.UP)
				link = link.getInv();

		}

		link.applyStyle(arg.getLazzy("ARROW_STYLE", 0));
		diagram.addLink(link);

		return CommandExecutionResult.ok();
	}

}
