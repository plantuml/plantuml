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
 */
package net.sourceforge.plantuml.chart;

import java.util.List;

import net.sourceforge.plantuml.Previous;
import net.sourceforge.plantuml.chart.command.CommandChartArea;
import net.sourceforge.plantuml.chart.command.CommandChartBar;
import net.sourceforge.plantuml.chart.command.CommandChartGrid;
import net.sourceforge.plantuml.chart.command.CommandChartLegend;
import net.sourceforge.plantuml.chart.command.CommandChartLine;
import net.sourceforge.plantuml.chart.command.CommandChartOrientation;
import net.sourceforge.plantuml.chart.command.CommandChartScatter;
import net.sourceforge.plantuml.chart.command.CommandChartStackMode;
import net.sourceforge.plantuml.chart.command.CommandChartXAxis;
import net.sourceforge.plantuml.chart.command.CommandChartYAxis;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommonCommands;
import net.sourceforge.plantuml.command.PSystemCommandFactory;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.skin.UmlDiagramType;

public class ChartDiagramFactory extends PSystemCommandFactory {

	public ChartDiagramFactory() {
		super(DiagramType.CHART);
	}

	@Override
	protected void initCommandsList(List<Command> cmds) {
		// Add chart-specific commands first to take priority
		cmds.add(new CommandChartXAxis());
		cmds.add(new CommandChartYAxis());
		cmds.add(new CommandChartBar());
		cmds.add(new CommandChartLine());
		cmds.add(new CommandChartArea());
		cmds.add(new CommandChartScatter());
		cmds.add(new CommandChartLegend());
		cmds.add(new CommandChartGrid());
		cmds.add(new CommandChartStackMode());
		cmds.add(new CommandChartOrientation());
		// Add common commands last so they don't override chart-specific ones
		CommonCommands.addCommonCommands1(cmds);
	}

	@Override
	public ChartDiagram createEmptyDiagram(UmlSource source, Previous previous, PreprocessingArtifact preprocessing) {
		return new ChartDiagram(source, preprocessing);
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.CHART;
	}
}
