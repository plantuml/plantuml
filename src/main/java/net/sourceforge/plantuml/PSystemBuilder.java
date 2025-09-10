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
package net.sourceforge.plantuml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.plantuml.api.cheerpj.WasmLog;

import net.sourceforge.plantuml.activitydiagram.ActivityDiagramFactory;
import net.sourceforge.plantuml.activitydiagram3.ActivityDiagramFactory3;
import net.sourceforge.plantuml.api.PSystemFactory;
import net.sourceforge.plantuml.board.BoardDiagramFactory;
import net.sourceforge.plantuml.bpm.BpmDiagramFactory;
import net.sourceforge.plantuml.cheneer.ChenEerDiagramFactory;
import net.sourceforge.plantuml.chronology.ChronologyDiagramFactory;
import net.sourceforge.plantuml.classdiagram.ClassDiagramFactory;
import net.sourceforge.plantuml.cli.GlobalConfig;
import net.sourceforge.plantuml.cli.GlobalConfigKey;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.dedication.PSystemDedicationFactory;
import net.sourceforge.plantuml.definition.PSystemDefinitionFactory;
import net.sourceforge.plantuml.descdiagram.DescriptionDiagramFactory;
import net.sourceforge.plantuml.directdot.PSystemDotFactory;
import net.sourceforge.plantuml.ditaa.PSystemDitaaFactory;
import net.sourceforge.plantuml.donors.PSystemDonorsFactory;
import net.sourceforge.plantuml.donors.PSystemSkinparameterListFactory;
import net.sourceforge.plantuml.ebnf.PSystemEbnfFactory;
import net.sourceforge.plantuml.eggs.PSystemAppleTwoFactory;
import net.sourceforge.plantuml.eggs.PSystemCharlieFactory;
import net.sourceforge.plantuml.eggs.PSystemColorsFactory;
import net.sourceforge.plantuml.eggs.PSystemEggFactory;
import net.sourceforge.plantuml.eggs.PSystemPathFactory;
import net.sourceforge.plantuml.eggs.PSystemRIPFactory;
import net.sourceforge.plantuml.eggs.PSystemWelcomeFactory;
import net.sourceforge.plantuml.emoji.PSystemListEmojiFactory;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.error.PSystemErrorUtils;
import net.sourceforge.plantuml.error.PSystemUnsupported;
import net.sourceforge.plantuml.filesdiagram.FilesDiagramFactory;
import net.sourceforge.plantuml.flowdiagram.FlowDiagramFactory;
import net.sourceforge.plantuml.font.PSystemListFontsFactory;
import net.sourceforge.plantuml.gitlog.GitDiagramFactory;
import net.sourceforge.plantuml.hcl.HclDiagramFactory;
import net.sourceforge.plantuml.help.HelpFactory;
import net.sourceforge.plantuml.jcckit.PSystemJcckitFactory;
import net.sourceforge.plantuml.jsondiagram.JsonDiagramFactory;
import net.sourceforge.plantuml.klimt.creole.legacy.PSystemCreoleFactory;
import net.sourceforge.plantuml.klimt.sprite.ListSpriteDiagramFactory;
import net.sourceforge.plantuml.klimt.sprite.PSystemListArchimateSpritesFactory;
import net.sourceforge.plantuml.klimt.sprite.StdlibDiagramFactory;
import net.sourceforge.plantuml.math.PSystemLatexFactory;
import net.sourceforge.plantuml.math.PSystemMathFactory;
import net.sourceforge.plantuml.mindmap.MindMapDiagramFactory;
import net.sourceforge.plantuml.nwdiag.NwDiagramFactory;
import net.sourceforge.plantuml.openiconic.PSystemListOpenIconicFactory;
import net.sourceforge.plantuml.openiconic.PSystemOpenIconicFactory;
import net.sourceforge.plantuml.oregon.PSystemOregonFactory;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.project.GanttDiagramFactory;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regexdiagram.PSystemRegexFactory;
import net.sourceforge.plantuml.salt.PSystemSaltFactory;
import net.sourceforge.plantuml.security.SecurityProfile;
import net.sourceforge.plantuml.security.SecurityUtils;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagramFactory;
import net.sourceforge.plantuml.statediagram.StateDiagramFactory;
import net.sourceforge.plantuml.stats.StatsUtilsIncrement;
import net.sourceforge.plantuml.sudoku.PSystemSudokuFactory;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.timingdiagram.TimingDiagramFactory;
import net.sourceforge.plantuml.utils.Log;
import net.sourceforge.plantuml.version.PSystemLicenseFactory;
import net.sourceforge.plantuml.version.PSystemVersionFactory;
import net.sourceforge.plantuml.wbs.WBSDiagramFactory;
import net.sourceforge.plantuml.wire.WireDiagramFactory;
import net.sourceforge.plantuml.yaml.YamlDiagramFactory;

/**
 * Builds a diagram from pre-processed PlantUML source.
 *
 * <p>
 * Tries each of the factories (enumerated in the static block below) until one
 * succeeds.
 *
 * @see AbstractPSystem
 */
public class PSystemBuilder {
	// ::remove file when __HAXE__

	public static final long startTime = System.currentTimeMillis();

	final public Diagram createPSystem(List<StringLocated> source, List<StringLocated> rawSource,
			Previous previous, PreprocessingArtifact preprocessing) {

		WasmLog.log("..compiling diagram...");

		final long now = System.currentTimeMillis();

		Diagram result = null;
		try {
			final DiagramType type = DiagramType.getTypeFromArobaseStart(source.get(0).getString());
			final UmlSource umlSource = UmlSource.createWithRaw(source, type == DiagramType.UML, rawSource);

			for (StringLocated s : source) {
				if (s.getPreprocessorError() != null) {
					// Dead code : should not append
					assert false;
					Log.error("Preprocessor Error: " + s.getPreprocessorError());
					final ErrorUml err = new ErrorUml(ErrorUmlType.SYNTAX_ERROR, s.getPreprocessorError(), 0,
							s.getLocation(), null);
					return PSystemErrorUtils.buildV2(umlSource, err, Collections.<String>emptyList(), source, preprocessing);
				}
			}

			final DiagramType diagramType = umlSource.getDiagramType();
			if (diagramType == DiagramType.UNKNOWN)
				return new PSystemUnsupported(umlSource, preprocessing);

			final List<PSystemError> errors = new ArrayList<>();
			for (PSystemFactory systemFactory : factories) {
				if (diagramType != systemFactory.getDiagramType())
					continue;

				// WasmLog.log("...trying " + systemFactory.getClass().getName() + " ...");
				final Diagram sys = systemFactory.createSystem(umlSource, previous, preprocessing);
				if (isOk(sys)) {
					result = sys;
					return sys;
				}
				errors.add((PSystemError) sys);
			}
			if (errors.size() == 0)
				return new PSystemUnsupported(umlSource, preprocessing);

			result = PSystemErrorUtils.merge(errors);
			return result;
		} finally {
			WasmLog.log("...parsing ok...");
			// ::comment when __CORE__
			if (result != null && GlobalConfig.getInstance().boolValue(GlobalConfigKey.ENABLE_STATS)) {
				StatsUtilsIncrement.onceMoreParse(System.currentTimeMillis() - now, result.getClass());
			}
			Log.info(() -> "Compilation duration " + (System.currentTimeMillis() - now));
			RegexConcat.printCacheInfo();
			// ::done
		}
	}

	private static final List<PSystemFactory> factories = new ArrayList<>();

	static {
		factories.add(new PSystemWelcomeFactory());
		factories.add(new PSystemColorsFactory());
		factories.add(new SequenceDiagramFactory());
		factories.add(new ClassDiagramFactory());
		factories.add(new ActivityDiagramFactory());
		factories.add(new DescriptionDiagramFactory());
		factories.add(new StateDiagramFactory());
		factories.add(new ActivityDiagramFactory3());

		// ::comment when __CORE__
		factories.add(new BpmDiagramFactory(DiagramType.BPM));
		// ::done

		// factories.add(new PostIdDiagramFactory());
		factories.add(new PSystemLicenseFactory());
		factories.add(new PSystemVersionFactory());
		// ::comment when __CORE__
		factories.add(new PSystemDonorsFactory());
		factories.add(new PSystemSkinparameterListFactory());
		factories.add(new PSystemListFontsFactory());
		factories.add(new PSystemListEmojiFactory());
		factories.add(new PSystemOpenIconicFactory());
		factories.add(new PSystemListOpenIconicFactory());
		factories.add(new PSystemListArchimateSpritesFactory());
		// ::done
		factories.add(new PSystemSaltFactory(DiagramType.UML));
		factories.add(new PSystemSaltFactory(DiagramType.SALT));
		// ::comment when __CORE__
		factories.add(new PSystemDotFactory(DiagramType.DOT));
		factories.add(new PSystemDotFactory(DiagramType.UML));
		// ::done
		factories.add(new NwDiagramFactory(DiagramType.UML));
		factories.add(new NwDiagramFactory(DiagramType.NW));
		factories.add(new MindMapDiagramFactory());
		factories.add(new WBSDiagramFactory());

		// ::uncomment when __CORE__
		// factories.add(new PSystemSudokuFactory());
		// ::done

		// ::comment when __CORE__ or __MIT__ or __EPL__ or __BSD__ or __ASL__ or __LGPL__
		factories.add(new PSystemJcckitFactory());
		factories.add(new PSystemSudokuFactory());
		// ::done
		// ::comment when __CORE__ or __MIT__ or __EPL__ or __BSD__ or __ASL__
		factories.add(new PSystemDitaaFactory());
		// ::done

		// ::comment when __CORE__
		factories.add(new PSystemDefinitionFactory());
		factories.add(new ListSpriteDiagramFactory());
		factories.add(new StdlibDiagramFactory());
		factories.add(new PSystemMathFactory(DiagramType.MATH));
		factories.add(new PSystemLatexFactory(DiagramType.LATEX));
		factories.add(new PSystemCreoleFactory());
		factories.add(new PSystemEggFactory());
		factories.add(new PSystemAppleTwoFactory());
		factories.add(new PSystemRIPFactory());
		if (SecurityUtils.getSecurityProfile() == SecurityProfile.UNSECURE)
			factories.add(new PSystemPathFactory());
		factories.add(new PSystemOregonFactory());
		// ::done

		factories.add(new PSystemCharlieFactory());

		factories.add(new GanttDiagramFactory());
		factories.add(new ChronologyDiagramFactory());
		factories.add(new FlowDiagramFactory());

		// ::comment when __CORE__
		factories.add(new PSystemDedicationFactory());
		// ::done

		factories.add(new TimingDiagramFactory());

		// ::comment when __CORE__
		factories.add(new HelpFactory());
		factories.add(new WireDiagramFactory());
		// ::done

		factories.add(new JsonDiagramFactory());
		factories.add(new GitDiagramFactory());
		// ::comment when __CORE__
		factories.add(new FilesDiagramFactory());
		factories.add(new BoardDiagramFactory());
		// ::done
		factories.add(new YamlDiagramFactory());
		factories.add(new HclDiagramFactory());
		factories.add(new PSystemEbnfFactory());
		factories.add(new PSystemRegexFactory());

		factories.add(new ChenEerDiagramFactory());
	}

	private boolean isOk(Diagram ps) {
		if (ps == null || ps instanceof PSystemError)
			return false;

		return true;
	}

}
