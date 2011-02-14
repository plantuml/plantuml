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
 * Revision $Revision: 5031 $
 *
 */
package net.sourceforge.plantuml.activitydiagram.command;

import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.activitydiagram.ActivityDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexPartialMatch;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;

public class CommandLinkLongActivity extends CommandMultilines2<ActivityDiagram> {

	public CommandLinkLongActivity(final ActivityDiagram diagram) {
		super(
				diagram,
				getRegexConcat(),
				"(?i)^\\s*([^\"]*)\"(?:\\s+as\\s+([\\p{L}0-9_.]+))?\\s*(\\<\\<.*\\>\\>)?\\s*(#\\w+)?$");
	}
	
	static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"),
				new RegexOr("FIRST", true,
						new RegexLeaf("STAR", "(\\(\\*(top)?\\))"),
						new RegexLeaf("CODE", "([\\p{L}0-9_.]+)"),
						new RegexLeaf("BAR", "(?:==+)\\s*([\\p{L}0-9_.]+)\\s*(?:==+)"),
						new RegexLeaf("QUOTED", "\"([^\"]+)\"(?:\\s+as\\s+([\\p{L}0-9_.]+))?")),
				new RegexLeaf("\\s*"),
				new RegexLeaf("STEREOTYPE", "(\\<\\<.*\\>\\>)?"),
				new RegexLeaf("\\s*"),
				new RegexLeaf("BACKCOLOR", "(#\\w+)?"),
				new RegexLeaf("\\s*"),
				new RegexLeaf("ARROW", "([=-]+(?:(left|right|up|down|le?|ri?|up?|do?)(?=[-=]))?[=-]*\\>)"),
				new RegexLeaf("\\s*"),
				new RegexLeaf("BRACKET", "(?:\\[([^\\]*]+[^\\]]*)\\])?"),
				new RegexLeaf("\\s*"),
				new RegexLeaf("DESC", "\"([^\"]*?)"),
				new RegexLeaf("\\s*"),
				new RegexLeaf("$"));
	}

	public CommandExecutionResult execute(List<String> lines) {
		StringUtils.trim(lines, false);
		final Map<String, RegexPartialMatch> line0 = getStartingPattern().matcher(lines.get(0).trim());
		final IEntity entity1 = CommandLinkActivity.getEntity(getSystem(), line0, true);
		if (line0.get("STEREOTYPE").get(0) != null) {
			entity1.setStereotype(new Stereotype(line0.get("STEREOTYPE").get(0)));
		}
		if (line0.get("BACKCOLOR").get(0)!=null) {
			entity1.setSpecificBackcolor(line0.get("BACKCOLOR").get(0));
		}
		final StringBuilder sb = new StringBuilder();

		if (StringUtils.isNotEmpty(line0.get("DESC").get(0))) {
			sb.append(line0.get("DESC").get(0));
			sb.append("\\n");
		}
		for (int i = 1; i < lines.size() - 1; i++) {
			sb.append(lines.get(i));
			if (i < lines.size() - 2) {
				sb.append("\\n");
			}
		}

		final List<String> lineLast = StringUtils.getSplit(getEnding(), lines.get(lines.size() - 1));
		if (StringUtils.isNotEmpty(lineLast.get(0))) {
			if (sb.toString().endsWith("\\n") == false) {
				sb.append("\\n");
			}
			sb.append(lineLast.get(0));
		}

		final String display = sb.toString();
		final String code = lineLast.get(1) == null ? display : lineLast.get(1);

		final Entity entity2 = getSystem().createEntity(code, display, EntityType.ACTIVITY);
		if (lineLast.get(2)!=null) {
			entity2.setStereotype(new Stereotype(lineLast.get(2)));
		}
		if (lineLast.get(3)!=null) {
			entity2.setSpecificBackcolor(lineLast.get(3));
		}

		if (entity1 == null || entity2 == null) {
			return CommandExecutionResult.error("No such entity");
		}

		final String arrow = StringUtils.manageArrowForCuca(line0.get("ARROW").get(0));
		final int lenght = arrow.length() - 1;

		final String linkLabel = line0.get("BRACKET").get(0);

		Link link = new Link(entity1, entity2, new LinkType(LinkDecor.ARROW, LinkDecor.NONE), linkLabel, lenght);
		final Direction direction = StringUtils.getArrowDirection(line0.get("ARROW").get(0));
		if (direction == Direction.LEFT || direction == Direction.UP) {
			link = link.getInv();
		}

		getSystem().addLink(link);

		return CommandExecutionResult.ok();
	}

}
