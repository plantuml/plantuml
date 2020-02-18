/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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
package net.sourceforge.plantuml.activitydiagram3;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.activitydiagram3.command.CommandActivity3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandActivityLegacy1;
import net.sourceforge.plantuml.activitydiagram3.command.CommandActivityLong3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandArrow3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandArrowLong3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandBackward3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandBreak;
import net.sourceforge.plantuml.activitydiagram3.command.CommandCase;
import net.sourceforge.plantuml.activitydiagram3.command.CommandCircleSpot3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandElse3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandElseIf2;
import net.sourceforge.plantuml.activitydiagram3.command.CommandElseLegacy1;
import net.sourceforge.plantuml.activitydiagram3.command.CommandEnd3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandEndPartition3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandEndSwitch;
import net.sourceforge.plantuml.activitydiagram3.command.CommandEndif3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandFork3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandForkAgain3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandForkEnd3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandGoto;
import net.sourceforge.plantuml.activitydiagram3.command.CommandGroup3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandGroupEnd3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandIf2;
import net.sourceforge.plantuml.activitydiagram3.command.CommandIf4;
import net.sourceforge.plantuml.activitydiagram3.command.CommandIfLegacy1;
import net.sourceforge.plantuml.activitydiagram3.command.CommandKill3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandLabel;
import net.sourceforge.plantuml.activitydiagram3.command.CommandLink3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandNote3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandNoteLong3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandPartition3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandRepeat3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandRepeatWhile3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandRepeatWhile3Multilines;
import net.sourceforge.plantuml.activitydiagram3.command.CommandSplit3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandSplitAgain3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandSplitEnd3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandStart3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandStop3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandSwimlane;
import net.sourceforge.plantuml.activitydiagram3.command.CommandSwimlane2;
import net.sourceforge.plantuml.activitydiagram3.command.CommandSwitch;
import net.sourceforge.plantuml.activitydiagram3.command.CommandWhile3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandWhileEnd3;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandDecoratorMultine;
import net.sourceforge.plantuml.command.CommandFootboxIgnored;
import net.sourceforge.plantuml.command.UmlDiagramFactory;

public class ActivityDiagramFactory3 extends UmlDiagramFactory {

	private final ISkinSimple skinParam;

	public ActivityDiagramFactory3(ISkinSimple skinParam) {
		this.skinParam = skinParam;
	}

	@Override
	protected List<Command> createCommands() {

		final List<Command> cmds = new ArrayList<Command>();
		cmds.add(new CommandFootboxIgnored());

		addCommonCommands1(cmds);
		cmds.add(new CommandSwimlane());
		cmds.add(new CommandSwimlane2());
		cmds.add(new CommandPartition3());
		cmds.add(new CommandEndPartition3());
		cmds.add(new CommandGroup3());
		cmds.add(new CommandGroupEnd3());
		cmds.add(new CommandArrow3());
		cmds.add(new CommandArrowLong3());
		cmds.add(new CommandRepeat3());
		cmds.add(new CommandActivity3());
		cmds.add(new CommandIf4());
		cmds.add(new CommandIf2());
		cmds.add(new CommandDecoratorMultine(new CommandIf2(), 50));
		cmds.add(new CommandIfLegacy1());
		cmds.add(new CommandElseIf2());
		cmds.add(new CommandElse3());
		cmds.add(new CommandDecoratorMultine(new CommandElse3(), 50));
		cmds.add(new CommandElseLegacy1());
		cmds.add(new CommandEndif3());

		cmds.add(new CommandSwitch());
		cmds.add(new CommandCase());
		cmds.add(new CommandEndSwitch());

		cmds.add(new CommandRepeatWhile3());
		cmds.add(new CommandRepeatWhile3Multilines());
		cmds.add(new CommandBackward3());
		cmds.add(new CommandWhile3());
		cmds.add(new CommandWhileEnd3());
		
		cmds.add(new CommandFork3());
		cmds.add(new CommandForkAgain3());
		cmds.add(new CommandForkEnd3());
		
		cmds.add(new CommandSplit3());
		cmds.add(new CommandSplitAgain3());
		cmds.add(new CommandSplitEnd3());
		// cmds.add(new CommandGroup3());
		// cmds.add(new CommandGroupEnd3());
		cmds.add(new CommandStart3());
		cmds.add(new CommandStop3());
		cmds.add(new CommandCircleSpot3());
		cmds.add(new CommandBreak());
		cmds.add(new CommandEnd3());
		cmds.add(new CommandKill3());
		cmds.add(new CommandLink3());
		cmds.add(new CommandNote3());
		cmds.add(new CommandNoteLong3());

		cmds.add(new CommandActivityLong3());
		cmds.add(new CommandActivityLegacy1());

		cmds.add(new CommandLabel());
		cmds.add(new CommandGoto());
		cmds.add(new CommandDecoratorMultine(new CommandElseIf2(), 50));

		return cmds;
	}

	@Override
	public ActivityDiagram3 createEmptyDiagram() {
		return new ActivityDiagram3(skinParam);
	}

}
