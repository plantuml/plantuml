/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.acearth.PSystemXearthFactory;
import net.sourceforge.plantuml.activitydiagram.ActivityDiagramFactory;
import net.sourceforge.plantuml.activitydiagram3.ActivityDiagramFactory3;
import net.sourceforge.plantuml.api.PSystemFactory;
import net.sourceforge.plantuml.classdiagram.ClassDiagramFactory;
import net.sourceforge.plantuml.compositediagram.CompositeDiagramFactory;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.creole.PSystemCreoleFactory;
import net.sourceforge.plantuml.cute.PSystemCuteFactory;
import net.sourceforge.plantuml.descdiagram.DescriptionDiagramFactory;
import net.sourceforge.plantuml.directdot.PSystemDotFactory;
import net.sourceforge.plantuml.ditaa.PSystemDitaaFactory;
import net.sourceforge.plantuml.donors.PSystemDonorsFactory;
import net.sourceforge.plantuml.eggs.PSystemAppleTwoFactory;
import net.sourceforge.plantuml.eggs.PSystemCharlieFactory;
import net.sourceforge.plantuml.eggs.PSystemEggFactory;
import net.sourceforge.plantuml.eggs.PSystemLostFactory;
import net.sourceforge.plantuml.eggs.PSystemPathFactory;
import net.sourceforge.plantuml.eggs.PSystemRIPFactory;
import net.sourceforge.plantuml.flowdiagram.FlowDiagramFactory;
import net.sourceforge.plantuml.font.PSystemListFontsFactory;
import net.sourceforge.plantuml.jcckit.PSystemJcckitFactory;
import net.sourceforge.plantuml.jungle.PSystemTreeFactory;
import net.sourceforge.plantuml.logo.PSystemLogoFactory;
import net.sourceforge.plantuml.objectdiagram.ObjectDiagramFactory;
import net.sourceforge.plantuml.openiconic.PSystemListOpenIconicFactory;
import net.sourceforge.plantuml.openiconic.PSystemOpenIconicFactory;
import net.sourceforge.plantuml.oregon.PSystemOregonFactory;
import net.sourceforge.plantuml.postit.PostIdDiagramFactory;
import net.sourceforge.plantuml.printskin.PrintSkinFactory;
import net.sourceforge.plantuml.project2.PSystemProjectFactory2;
import net.sourceforge.plantuml.salt.PSystemSaltFactory;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagramFactory;
import net.sourceforge.plantuml.statediagram.StateDiagramFactory;
import net.sourceforge.plantuml.sudoku.PSystemSudokuFactory;
import net.sourceforge.plantuml.turing.PSystemTuringFactory;
import net.sourceforge.plantuml.ugraphic.sprite.PSystemListInternalSpritesFactory;
import net.sourceforge.plantuml.version.License;
import net.sourceforge.plantuml.version.PSystemLicenseFactory;
import net.sourceforge.plantuml.version.PSystemVersionFactory;

public class PSystemBuilder {

	public static final long startTime = System.currentTimeMillis();

	final public Diagram createPSystem(final List<CharSequence2> strings2) {

		final List<PSystemFactory> factories = getAllFactories();

		final DiagramType type = DiagramType.getTypeFromArobaseStart(strings2.get(0).toString2());

		final UmlSource umlSource = new UmlSource(strings2, type == DiagramType.UML);
		final DiagramType diagramType = umlSource.getDiagramType();
		final List<PSystemError> errors = new ArrayList<PSystemError>();
		for (PSystemFactory systemFactory : factories) {
			if (diagramType != systemFactory.getDiagramType()) {
				continue;
			}
			final Diagram sys = systemFactory.createSystem(umlSource);
			if (isOk(sys)) {
				return sys;
			}
			errors.add((PSystemError) sys);
		}

		final PSystemError err = PSystemError.merge(errors);
		// if (OptionFlags.getInstance().isQuiet() == false) {
		// err.print(System.err);
		// }
		return err;

	}

	private List<PSystemFactory> getAllFactories() {
		final List<PSystemFactory> factories = new ArrayList<PSystemFactory>();
		factories.add(new SequenceDiagramFactory());
		factories.add(new ClassDiagramFactory());
		factories.add(new ActivityDiagramFactory());
		factories.add(new DescriptionDiagramFactory());
		factories.add(new StateDiagramFactory());
		factories.add(new ActivityDiagramFactory3());
		factories.add(new CompositeDiagramFactory());
		// factories.add(new ObjectDiagramFactory());
		factories.add(new PostIdDiagramFactory());
		factories.add(new PrintSkinFactory());
		factories.add(new PSystemLicenseFactory());
		factories.add(new PSystemVersionFactory());
		factories.add(new PSystemDonorsFactory());
		factories.add(new PSystemListFontsFactory());
		factories.add(new PSystemOpenIconicFactory());
		factories.add(new PSystemListOpenIconicFactory());
		factories.add(new PSystemListInternalSpritesFactory());
		factories.add(new PSystemSaltFactory(DiagramType.SALT));
		factories.add(new PSystemSaltFactory(DiagramType.UML));
		factories.add(new PSystemDotFactory(DiagramType.DOT));
		factories.add(new PSystemDotFactory(DiagramType.UML));
		if (License.getCurrent() == License.GPL) {
			factories.add(new PSystemDitaaFactory(DiagramType.DITAA));
			factories.add(new PSystemDitaaFactory(DiagramType.UML));
			factories.add(new PSystemJcckitFactory(DiagramType.JCCKIT));
			factories.add(new PSystemJcckitFactory(DiagramType.UML));
			factories.add(new PSystemLogoFactory());
			factories.add(new PSystemSudokuFactory());
			factories.add(new PSystemTuringFactory());
		}
		factories.add(new PSystemCreoleFactory());
		factories.add(new PSystemEggFactory());
		factories.add(new PSystemAppleTwoFactory());
		factories.add(new PSystemRIPFactory());
		factories.add(new PSystemLostFactory());
		factories.add(new PSystemPathFactory());
		factories.add(new PSystemOregonFactory());
		factories.add(new PSystemCharlieFactory());
		if (License.getCurrent() == License.GPL) {
			factories.add(new PSystemXearthFactory());
		}
		factories.add(new PSystemProjectFactory2());
		factories.add(new FlowDiagramFactory());
		factories.add(new PSystemTreeFactory(DiagramType.JUNGLE));
		factories.add(new PSystemCuteFactory(DiagramType.CUTE));
		return factories;
	}

	private boolean isOk(Diagram ps) {
		if (ps == null || ps instanceof PSystemError) {
			return false;
		}
		return true;
	}

}
