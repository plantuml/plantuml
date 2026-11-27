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
package net.sourceforge.plantuml.directdot;

import net.sourceforge.plantuml.Previous;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.api.PSystemFactory;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.ngdot.PSystemNgDotFactory;
import net.sourceforge.plantuml.nio.PathSystem;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;

/**
 * Chooses how a {@code @startdot} diagram is drawn on the JVM.
 * <p>
 * By default, as before, the DOT text is given to the Graphviz executable
 * ({@link PSystemDotFactory}). With {@link TitledDiagram#FORCE_SMETANA} (set by
 * the Vega tests), it is parsed and drawn by PlantUML itself
 * ({@link PSystemNgDotFactory}), which is also what the TeaVM builds always
 * use. This lets the new rendering mature on the Vega references while the
 * normal usage does not change (see PLAN-NGDOT.md).
 * <p>
 * The choice is made for each diagram, not once, because the tests set the
 * flag after the factories are created.
 */
public class PSystemDotSelectorFactory implements PSystemFactory {

	private final PSystemDotFactory graphviz = new PSystemDotFactory();
	private final PSystemNgDotFactory ngdot = new PSystemNgDotFactory();

	@Override
	public Diagram createSystem(PathSystem pathSystem, UmlSource source, Previous previous,
			PreprocessingArtifact preprocessing) {
		if (TitledDiagram.FORCE_SMETANA)
			return ngdot.createSystem(pathSystem, source, previous, preprocessing);
		return graphviz.createSystem(pathSystem, source, previous, preprocessing);
	}

	@Override
	public DiagramType getDiagramType() {
		return DiagramType.DOT;
	}

}
