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
package net.sourceforge.plantuml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.acearth.PSystemXearthFactory;
import net.sourceforge.plantuml.activitydiagram.ActivityDiagramFactory;
import net.sourceforge.plantuml.activitydiagram3.ActivityDiagramFactory3;
import net.sourceforge.plantuml.api.PSystemFactory;
import net.sourceforge.plantuml.bpm.BpmDiagramFactory;
import net.sourceforge.plantuml.classdiagram.ClassDiagramFactory;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.creole.PSystemCreoleFactory;
import net.sourceforge.plantuml.dedication.PSystemDedicationFactory;
import net.sourceforge.plantuml.definition.PSystemDefinitionFactory;
import net.sourceforge.plantuml.descdiagram.DescriptionDiagramFactory;
import net.sourceforge.plantuml.directdot.PSystemDotFactory;
import net.sourceforge.plantuml.ditaa.PSystemDitaaFactory;
import net.sourceforge.plantuml.donors.PSystemDonorsFactory;
import net.sourceforge.plantuml.donors.PSystemSkinparameterListFactory;
import net.sourceforge.plantuml.eggs.PSystemAppleTwoFactory;
import net.sourceforge.plantuml.eggs.PSystemCharlieFactory;
import net.sourceforge.plantuml.eggs.PSystemColorsFactory;
import net.sourceforge.plantuml.eggs.PSystemEggFactory;
import net.sourceforge.plantuml.eggs.PSystemRIPFactory;
import net.sourceforge.plantuml.eggs.PSystemWelcomeFactory;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.error.PSystemErrorUtils;
import net.sourceforge.plantuml.flowdiagram.FlowDiagramFactory;
import net.sourceforge.plantuml.font.PSystemListFontsFactory;
import net.sourceforge.plantuml.help.HelpFactory;
import net.sourceforge.plantuml.jcckit.PSystemJcckitFactory;
import net.sourceforge.plantuml.math.PSystemLatexFactory;
import net.sourceforge.plantuml.math.PSystemMathFactory;
import net.sourceforge.plantuml.mindmap.MindMapDiagramFactory;
import net.sourceforge.plantuml.nwdiag.NwDiagramFactory;
import net.sourceforge.plantuml.openiconic.PSystemListOpenIconicFactory;
import net.sourceforge.plantuml.openiconic.PSystemOpenIconicFactory;
import net.sourceforge.plantuml.oregon.PSystemOregonFactory;
import net.sourceforge.plantuml.project.GanttDiagramFactory;
import net.sourceforge.plantuml.salt.PSystemSaltFactory;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagramFactory;
import net.sourceforge.plantuml.sprite.ListSpriteDiagramFactory;
import net.sourceforge.plantuml.sprite.PSystemListInternalSpritesFactory;
import net.sourceforge.plantuml.sprite.StdlibDiagramFactory;
import net.sourceforge.plantuml.statediagram.StateDiagramFactory;
import net.sourceforge.plantuml.stats.StatsUtilsIncrement;
import net.sourceforge.plantuml.sudoku.PSystemSudokuFactory;
import net.sourceforge.plantuml.timingdiagram.TimingDiagramFactory;
import net.sourceforge.plantuml.version.License;
import net.sourceforge.plantuml.version.PSystemLicenseFactory;
import net.sourceforge.plantuml.version.PSystemVersionFactory;
import net.sourceforge.plantuml.wbs.WBSDiagramFactory;
import net.sourceforge.plantuml.wire.WireDiagramFactory;

public class PSystemBuilder {

	public static final long startTime = System.currentTimeMillis();

	final public Diagram createPSystem(ISkinSimple skinParam, final List<StringLocated> strings2) {

		final long now = System.currentTimeMillis();

		Diagram result = null;
		try {
			final DiagramType type = DiagramType.getTypeFromArobaseStart(strings2.get(0).getString());
			final UmlSource umlSource = new UmlSource(strings2, type == DiagramType.UML);

			for (StringLocated s : strings2) {
				if (s.getPreprocessorError() != null) {
					// Dead code : should not append
					Log.error("Preprocessor Error: " + s.getPreprocessorError());
					final ErrorUml err = new ErrorUml(ErrorUmlType.SYNTAX_ERROR, s.getPreprocessorError(), /* cpt */
							s.getLocation());
					// return PSystemErrorUtils.buildV1(umlSource, err, Collections.<String>
					// emptyList());
					return PSystemErrorUtils.buildV2(umlSource, err, Collections.<String>emptyList(), strings2);
				}
			}

			final DiagramType diagramType = umlSource.getDiagramType();
			final List<PSystemError> errors = new ArrayList<PSystemError>();
			final List<PSystemFactory> factories = getAllFactories(skinParam);
			for (PSystemFactory systemFactory : factories) {
				if (diagramType != systemFactory.getDiagramType()) {
					continue;
				}
				final Diagram sys = systemFactory.createSystem(umlSource);
				if (isOk(sys)) {
					result = sys;
					return sys;
				}
				errors.add((PSystemError) sys);
			}

			final PSystemError err = PSystemErrorUtils.merge(errors);
			result = err;
			return err;
		} finally {
			if (result != null && OptionFlags.getInstance().isEnableStats()) {
				StatsUtilsIncrement.onceMoreParse(System.currentTimeMillis() - now, result.getClass());
			}
			Log.info("Compilation duration " + (System.currentTimeMillis() - now));
			RegexConcat.printCacheInfo();
		}
	}

	private static List<PSystemFactory> getAllFactories(ISkinSimple skinParam) {
		final List<PSystemFactory> factories = new ArrayList<PSystemFactory>();
		factories.add(new PSystemWelcomeFactory());
		factories.add(new PSystemColorsFactory());
		factories.add(new SequenceDiagramFactory(skinParam));
		factories.add(new ClassDiagramFactory(skinParam));
		factories.add(new ActivityDiagramFactory(skinParam));
		factories.add(new DescriptionDiagramFactory(skinParam));
		factories.add(new StateDiagramFactory(skinParam));
		factories.add(new ActivityDiagramFactory3(skinParam));
		// factories.add(new CompositeDiagramFactory(skinParam));
		factories.add(new BpmDiagramFactory(DiagramType.BPM));
		// factories.add(new PostIdDiagramFactory());
		factories.add(new PSystemLicenseFactory());
		factories.add(new PSystemVersionFactory());
		factories.add(new PSystemDonorsFactory());
		factories.add(new PSystemSkinparameterListFactory());
		factories.add(new PSystemListFontsFactory());
		factories.add(new PSystemOpenIconicFactory());
		factories.add(new PSystemListOpenIconicFactory());
		factories.add(new PSystemListInternalSpritesFactory());
		factories.add(new PSystemSaltFactory(DiagramType.SALT));
		factories.add(new PSystemSaltFactory(DiagramType.UML));
		factories.add(new PSystemDotFactory(DiagramType.DOT));
		factories.add(new PSystemDotFactory(DiagramType.UML));
		factories.add(new NwDiagramFactory());
		factories.add(new MindMapDiagramFactory());
		factories.add(new WBSDiagramFactory());
		factories.add(new PSystemDitaaFactory(DiagramType.DITAA));
		factories.add(new PSystemDitaaFactory(DiagramType.UML));
		if (License.getCurrent() == License.GPL || License.getCurrent() == License.GPLV2) {
			factories.add(new PSystemJcckitFactory(DiagramType.JCCKIT));
			factories.add(new PSystemJcckitFactory(DiagramType.UML));
			// factories.add(new PSystemLogoFactory());
			factories.add(new PSystemSudokuFactory());
		}
		factories.add(new PSystemDefinitionFactory());
		factories.add(new ListSpriteDiagramFactory(skinParam));
		factories.add(new StdlibDiagramFactory(skinParam));
		factories.add(new PSystemMathFactory(DiagramType.MATH));
		factories.add(new PSystemLatexFactory(DiagramType.LATEX));
		// factories.add(new PSystemStatsFactory());
		factories.add(new PSystemCreoleFactory());
		factories.add(new PSystemEggFactory());
		factories.add(new PSystemAppleTwoFactory());
		factories.add(new PSystemRIPFactory());
		// factories.add(new PSystemLostFactory());
		// factories.add(new PSystemPathFactory());
		factories.add(new PSystemOregonFactory());
		factories.add(new PSystemCharlieFactory());
		if (License.getCurrent() == License.GPL || License.getCurrent() == License.GPLV2) {
			factories.add(new PSystemXearthFactory());
		}
		factories.add(new GanttDiagramFactory(DiagramType.GANTT));
		factories.add(new GanttDiagramFactory(DiagramType.UML));
		factories.add(new FlowDiagramFactory());
		// factories.add(new PSystemTreeFactory(DiagramType.JUNGLE));
		// factories.add(new PSystemCuteFactory(DiagramType.CUTE));
		factories.add(new PSystemDedicationFactory());
		factories.add(new TimingDiagramFactory());
		factories.add(new HelpFactory());
		factories.add(new WireDiagramFactory());
		return factories;
	}

	private boolean isOk(Diagram ps) {
		if (ps == null || ps instanceof PSystemError) {
			return false;
		}
		return true;
	}

}
