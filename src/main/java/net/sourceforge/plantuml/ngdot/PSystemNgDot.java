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
package net.sourceforge.plantuml.ngdot;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.ngdot.draw.DotRenderer;
import net.sourceforge.plantuml.ngdot.model.DotGraph;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;

/**
 * A diagram written in the DOT language ({@code @startdot}), drawn by
 * PlantUML itself (Smetana layout, UGraphic drawing) instead of the Graphviz
 * executable. Always used by the TeaVM builds; on the JVM, only with
 * {@link TitledDiagram#FORCE_SMETANA} (see
 * {@code directdot.PSystemDotSelectorFactory}). See PLAN-NGDOT.md.
 */
public class PSystemNgDot extends TitledDiagram {

	private final DotGraph graph;

	public PSystemNgDot(UmlSource source, DotGraph graph, PreprocessingArtifact preprocessing) {
		super(source, DiagramType.DOT, null, preprocessing);
		this.graph = graph;
	}

	public DotGraph getGraph() {
		return graph;
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Dot)");
	}

	@Override
	public TextBlock getTextBlock(int num, FileFormatOption fileFormatOption) {
		return new DotRenderer(graph, getSkinParam());
	}

}
