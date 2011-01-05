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
 * Revision $Revision: 5024 $
 *
 */
package net.sourceforge.plantuml.activitydiagram.command;

import java.util.Map;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.activitydiagram.ActivityDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexPartialMatch;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;

public class CommandLinkActivity extends SingleLineCommand2<ActivityDiagram> {

	public CommandLinkActivity(ActivityDiagram diagram) {
		super(
				diagram, getRegexConcat());
	}
	
	static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"),
					new RegexOr("FIRST", true,
							new RegexLeaf("STAR", "(\\(\\*\\))"),
							new RegexLeaf("CODE", "([\\p{L}0-9_.]+)"),
							new RegexLeaf("BAR", "(?:==+)\\s*([\\p{L}0-9_.]+)\\s*(?:==+)"),
							new RegexLeaf("QUOTED", "\"([^\"]+)\"(?:\\s+as\\s+([\\p{L}0-9_.]+))?")),
					new RegexLeaf("\\s*"),
					new RegexLeaf("STEREOTYPE", "(\\<\\<.*\\>\\>)?"),
					new RegexLeaf("\\s*"),
					new RegexLeaf("BACKCOLOR", "(#\\w+)?"),
					new RegexLeaf("\\s*"),
					new RegexLeaf("ARROW", "([=-]+(?:left|right|up|down|le?|ri?|up?|do?)?[=-]*\\>)"),
					new RegexLeaf("\\s*"),
					new RegexLeaf("BRACKET", "(?:\\[([^\\]*]+[^\\]]*)\\])?"),
					new RegexLeaf("\\s*"),
					new RegexOr("FIRST2",
							new RegexLeaf("STAR2", "(\\(\\*\\))"),
							new RegexLeaf("OPENBRACKET2", "(\\{)"),
							new RegexLeaf("CODE2", "([\\p{L}0-9_.]+)"),
							new RegexLeaf("BAR2", "(?:==+)\\s*([\\p{L}0-9_.]+)\\s*(?:==+)"),
							new RegexLeaf("QUOTED2", "\"([^\"]+)\"(?:\\s+as\\s+([\\p{L}0-9_.]+))?")),
					new RegexLeaf("\\s*"),
					new RegexLeaf("STEREOTYPE2", "(\\<\\<.*\\>\\>)?"),
					new RegexLeaf("\\s*"),
					new RegexLeaf("BACKCOLOR2", "(#\\w+)?"),
					new RegexLeaf("$"));
	}
	
	@Override
	protected CommandExecutionResult executeArg(Map<String, RegexPartialMatch> arg2) {
		final IEntity entity1 = getEntity(getSystem(), arg2, true);
		if (arg2.get("STEREOTYPE").get(0) != null) {
			entity1.setStereotype(new Stereotype(arg2.get("STEREOTYPE").get(0)));
		}
		if (arg2.get("BACKCOLOR").get(0) != null) {
			entity1.setSpecificBackcolor(arg2.get("BACKCOLOR").get(0));
		}

		final IEntity entity2 = getEntity(getSystem(), arg2, false);
		if (arg2.get("BACKCOLOR2").get(0) != null) {
			entity2.setSpecificBackcolor(arg2.get("BACKCOLOR2").get(0));
		}
		if (arg2.get("STEREOTYPE2").get(0) != null) {
			entity2.setStereotype(new Stereotype(arg2.get("STEREOTYPE2").get(0)));
		}

		final String linkLabel = arg2.get("BRACKET").get(0);

		final String arrow = StringUtils.manageArrowForCuca(arg2.get("ARROW").get(0));
		final int lenght = arrow.length() - 1;

		Link link = new Link(entity1, entity2, new LinkType(LinkDecor.ARROW, LinkDecor.NONE), linkLabel, lenght);
		final Direction direction = StringUtils.getArrowDirection(arg2.get("ARROW").get(0));
		if (direction == Direction.LEFT || direction == Direction.UP) {
			link = link.getInv();
		}

		getSystem().addLink(link);

		return CommandExecutionResult.ok();

	}




	static IEntity getEntity(ActivityDiagram system, Map<String, RegexPartialMatch> arg, final boolean start) {
		final String suf = start ? "" : "2";

		final RegexPartialMatch openBracket = arg.get("OPENBRACKET" + suf);
		if (openBracket!=null && openBracket.get(0) != null) {
			return system.createInnerActivity();
		}
		if (arg.get("STAR" + suf).get(0) != null) {
			if (start) {
				return system.getStart();
			}
			return system.getEnd();
		}
		final String code = arg.get("CODE" + suf).get(0);
		if (code != null) {
			return system.getOrCreate(code, code, getTypeIfExisting(system, code));
		}
		final String bar = arg.get("BAR" + suf).get(0);
		if (bar != null) {
			return system.getOrCreate(bar, bar, EntityType.SYNCHRO_BAR);
		}
		final RegexPartialMatch quoted = arg.get("QUOTED" + suf);
		if (quoted.get(0) != null) {
			final String quotedCode = quoted.get(1) == null ? quoted.get(0) : quoted.get(1);
			return system.getOrCreate(quotedCode, quoted.get(0), getTypeIfExisting(system,
					quotedCode));
		}
		final String first = arg.get("FIRST" + suf).get(0);
		if (first == null) {
			return system.getLastEntityConsulted();
		}
		throw new UnsupportedOperationException();
	}
	

	static EntityType getTypeIfExisting(ActivityDiagram system, String code) {
		if (system.entityExist(code)) {
			final IEntity ent = system.entities().get(code);
			if (ent.getType() == EntityType.BRANCH) {
				return EntityType.BRANCH;
			}
		}
		return EntityType.ACTIVITY;
	}
	
	static EntityType getTypeFromString(String type, final EntityType circle) {
		if (type == null) {
			return EntityType.ACTIVITY;
		}
		if (type.equals("*")) {
			return circle;
		}
		if (type.startsWith("=")) {
			return EntityType.SYNCHRO_BAR;
		}
		throw new IllegalArgumentException();
	}


}
