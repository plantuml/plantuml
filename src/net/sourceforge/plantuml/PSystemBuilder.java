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
 * Revision $Revision: 5207 $
 * 
 */
package net.sourceforge.plantuml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.acearth.PSystemXearthFactory;
import net.sourceforge.plantuml.activitydiagram.ActivityDiagramFactory;
import net.sourceforge.plantuml.activitydiagram2.ActivityDiagramFactory2;
import net.sourceforge.plantuml.classdiagram.ClassDiagramFactory;
import net.sourceforge.plantuml.componentdiagram.ComponentDiagramFactory;
import net.sourceforge.plantuml.compositediagram.CompositeDiagramFactory;
import net.sourceforge.plantuml.directdot.PSystemDotFactory;
import net.sourceforge.plantuml.ditaa.PSystemDitaaFactory;
import net.sourceforge.plantuml.eggs.PSystemEggFactory;
import net.sourceforge.plantuml.eggs.PSystemLostFactory;
import net.sourceforge.plantuml.eggs.PSystemPathFactory;
import net.sourceforge.plantuml.eggs.PSystemRIPFactory;
import net.sourceforge.plantuml.jcckit.PSystemJcckitFactory;
import net.sourceforge.plantuml.objectdiagram.ObjectDiagramFactory;
import net.sourceforge.plantuml.oregon.PSystemOregonFactory;
import net.sourceforge.plantuml.postit.PostIdDiagramFactory;
import net.sourceforge.plantuml.printskin.PrintSkinFactory;
import net.sourceforge.plantuml.project.PSystemProjectFactory;
import net.sourceforge.plantuml.salt.PSystemSaltFactory;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagramFactory;
import net.sourceforge.plantuml.statediagram.StateDiagramFactory;
import net.sourceforge.plantuml.sudoku.PSystemSudokuFactory;
import net.sourceforge.plantuml.usecasediagram.UsecaseDiagramFactory;
import net.sourceforge.plantuml.version.PSystemVersionFactory;

public class PSystemBuilder {

	final public PSystem createPSystem(final List<String> strings) throws IOException, InterruptedException {

		final List<PSystemFactory> factories = getAllFactories();

		final UmlSource umlSource = new UmlSource(strings);
		final DiagramType diagramType = umlSource.getDiagramType();
		final List<PSystemError> errors = new ArrayList<PSystemError>();
		for (PSystemFactory systemFactory : factories) {
			if (diagramType != systemFactory.getDiagramType()) {
				continue;
			}
			final PSystem sys = new PSystemSingleBuilder(umlSource, systemFactory).getPSystem();
			if (isOk(sys)) {
				return sys;
			}
			errors.add((PSystemError) sys);
		}

		final PSystemError err = merge(errors);
		if (OptionFlags.getInstance().isQuiet() == false) {
			err.print(System.err);
		}
		return err;

	}

	private List<PSystemFactory> getAllFactories() {
		final List<PSystemFactory> factories = new ArrayList<PSystemFactory>();
		factories.add(new SequenceDiagramFactory());
		factories.add(new ClassDiagramFactory());
		factories.add(new ActivityDiagramFactory());
		factories.add(new ActivityDiagramFactory2());
		factories.add(new UsecaseDiagramFactory());
		factories.add(new ComponentDiagramFactory());
		factories.add(new StateDiagramFactory());
		factories.add(new ActivityDiagramFactory2());
		factories.add(new CompositeDiagramFactory());
		factories.add(new ObjectDiagramFactory());
		factories.add(new PostIdDiagramFactory());
		factories.add(new PrintSkinFactory());
		factories.add(new PSystemVersionFactory());
		factories.add(new PSystemSaltFactory(DiagramType.SALT));
		factories.add(new PSystemSaltFactory(DiagramType.UML));
		factories.add(new PSystemDotFactory(DiagramType.DOT));
		factories.add(new PSystemDotFactory(DiagramType.UML));
		factories.add(new PSystemDitaaFactory(DiagramType.DITAA));
		factories.add(new PSystemDitaaFactory(DiagramType.UML));
		factories.add(new PSystemJcckitFactory(DiagramType.JCCKIT));
		factories.add(new PSystemJcckitFactory(DiagramType.UML));
		factories.add(new PSystemSudokuFactory());
		factories.add(new PSystemEggFactory());
		factories.add(new PSystemRIPFactory());
		factories.add(new PSystemLostFactory());
		factories.add(new PSystemPathFactory());
		factories.add(new PSystemOregonFactory());
		factories.add(new PSystemXearthFactory());
		factories.add(new PSystemProjectFactory());
		return factories;
	}

	private PSystemError merge(Collection<PSystemError> ps) {
		UmlSource source = null;
		final List<ErrorUml> errors = new ArrayList<ErrorUml>();
		for (PSystemError system : ps) {
			if (system.getSource() != null && source == null) {
				source = system.getSource();
			}
			errors.addAll(system.getErrorsUml());
		}
		if (source == null) {
			throw new IllegalStateException();
		}
		return new PSystemError(source, errors);
	}

	private boolean isOk(PSystem ps) {
		if (ps == null || ps instanceof PSystemError) {
			return false;
		}
		return true;
	}

}
