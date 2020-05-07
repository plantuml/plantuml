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
package net.sourceforge.plantuml.timingdiagram;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandFootboxIgnored;
import net.sourceforge.plantuml.command.UmlDiagramFactory;
import net.sourceforge.plantuml.timingdiagram.command.CommandAnalog;
import net.sourceforge.plantuml.timingdiagram.command.CommandAtPlayer;
import net.sourceforge.plantuml.timingdiagram.command.CommandAtTime;
import net.sourceforge.plantuml.timingdiagram.command.CommandBinary;
import net.sourceforge.plantuml.timingdiagram.command.CommandChangeStateByPlayerCode;
import net.sourceforge.plantuml.timingdiagram.command.CommandChangeStateByTime;
import net.sourceforge.plantuml.timingdiagram.command.CommandClock;
import net.sourceforge.plantuml.timingdiagram.command.CommandConstraint;
import net.sourceforge.plantuml.timingdiagram.command.CommandDefineStateLong;
import net.sourceforge.plantuml.timingdiagram.command.CommandDefineStateShort;
import net.sourceforge.plantuml.timingdiagram.command.CommandHideTimeAxis;
import net.sourceforge.plantuml.timingdiagram.command.CommandHighlight;
import net.sourceforge.plantuml.timingdiagram.command.CommandModeCompact;
import net.sourceforge.plantuml.timingdiagram.command.CommandNote;
import net.sourceforge.plantuml.timingdiagram.command.CommandNoteLong;
import net.sourceforge.plantuml.timingdiagram.command.CommandPixelHeight;
import net.sourceforge.plantuml.timingdiagram.command.CommandRobustConcise;
import net.sourceforge.plantuml.timingdiagram.command.CommandScalePixel;
import net.sourceforge.plantuml.timingdiagram.command.CommandTicks;
import net.sourceforge.plantuml.timingdiagram.command.CommandTimeMessage;

public class TimingDiagramFactory extends UmlDiagramFactory {

	@Override
	public TimingDiagram createEmptyDiagram() {
		return new TimingDiagram();
	}

	@Override
	protected List<Command> createCommands() {

		final List<Command> cmds = new ArrayList<Command>();

		addCommonCommands1(cmds);
		cmds.add(new CommandFootboxIgnored());
		cmds.add(new CommandRobustConcise());
		cmds.add(new CommandClock());
		cmds.add(new CommandAnalog());
		cmds.add(new CommandBinary());
		cmds.add(new CommandDefineStateShort());
		cmds.add(new CommandDefineStateLong());
		cmds.add(new CommandChangeStateByPlayerCode());
		cmds.add(new CommandChangeStateByTime());
		cmds.add(new CommandAtTime());
		cmds.add(new CommandAtPlayer());
		cmds.add(new CommandTimeMessage());
		cmds.add(new CommandNote());
		cmds.add(new CommandNoteLong());
		cmds.add(new CommandConstraint());
		cmds.add(new CommandScalePixel());
		cmds.add(new CommandHideTimeAxis());
		cmds.add(new CommandHighlight());
		cmds.add(new CommandModeCompact());
		cmds.add(new CommandTicks());
		cmds.add(new CommandPixelHeight());

		return cmds;
	}

}
