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
package net.sourceforge.plantuml.teavm;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.DefinitionsContainer;
import net.sourceforge.plantuml.UgDiagram;
import net.sourceforge.plantuml.activitydiagram.ActivityDiagramFactory;
import net.sourceforge.plantuml.activitydiagram3.ActivityDiagramFactory3;
import net.sourceforge.plantuml.annotation.DuplicateCode;
import net.sourceforge.plantuml.api.PSystemFactory;
import net.sourceforge.plantuml.chart.ChartDiagramFactory;
import net.sourceforge.plantuml.classdiagram.ClassDiagramFactory;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.descdiagram.DescriptionDiagramFactory;
import net.sourceforge.plantuml.ebnf.PSystemEbnfFactory;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.error.PSystemErrorPreprocessor;
import net.sourceforge.plantuml.error.PSystemErrorUtils;
import net.sourceforge.plantuml.error.PSystemUnsupported;
import net.sourceforge.plantuml.explain.Explanation;
import net.sourceforge.plantuml.explain.PSystemExplain;
import net.sourceforge.plantuml.gantt.GanttDiagramFactory;
import net.sourceforge.plantuml.jaws.Jaws;
import net.sourceforge.plantuml.jsondiagram.JsonDiagramFactory;
import net.sourceforge.plantuml.klimt.creole.legacy.PSystemCreoleFactory;
import net.sourceforge.plantuml.mindmap.MindMapDiagramFactory;
import net.sourceforge.plantuml.nio.PathSystem;
import net.sourceforge.plantuml.nwdiag.NwDiagramFactory;
import net.sourceforge.plantuml.packetdiag.PacketDiagramFactory;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.regexdiagram.PSystemRegexFactory;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagramFactory;
import net.sourceforge.plantuml.statediagram.StateDiagramFactory;
import net.sourceforge.plantuml.sudoku.PSystemSudokuFactory;
import net.sourceforge.plantuml.teavm.browser.BrowserLog;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.tim.TimLoader;
import net.sourceforge.plantuml.timingdiagram.TimingDiagramFactory;
import net.sourceforge.plantuml.utils.LineLocationImpl;
import net.sourceforge.plantuml.version.PSystemVersionFactory;
import net.sourceforge.plantuml.wbs.WBSDiagramFactory;
import net.sourceforge.plantuml.yaml.YamlDiagramFactory;

public class PSystemBuilder2 {

	private final static PSystemBuilder2 singleton = new PSystemBuilder2();

	private final List<PSystemFactory> factories = new ArrayList<>();
	private PSystemFactory lastFactory;

	private PSystemBuilder2() {
		factories.add(new SequenceDiagramFactory());
		factories.add(new ClassDiagramFactory());
		factories.add(new ActivityDiagramFactory());
		factories.add(new DescriptionDiagramFactory());
		factories.add(new StateDiagramFactory());
		factories.add(new ActivityDiagramFactory3());
		factories.add(new PSystemVersionFactory());
		factories.add(new GanttDiagramFactory());
		// factories.add(new PSystemDotFactory(DiagramType.UML));
		factories.add(new MindMapDiagramFactory());
		factories.add(new WBSDiagramFactory());
		factories.add(new NwDiagramFactory());
		// ::comment when __MIT__ __EPL__ __BSD__ __ASL__ __LGPL__
		factories.add(new PSystemSudokuFactory());
		// ::done
		factories.add(new PSystemCreoleFactory());
		factories.add(new TimingDiagramFactory());
		factories.add(new ChartDiagramFactory());
		factories.add(new PacketDiagramFactory());
		factories.add(new JsonDiagramFactory());
		factories.add(new YamlDiagramFactory());
		factories.add(new PSystemEbnfFactory());
		factories.add(new PSystemRegexFactory());
		// ::comment when __MIT__ __EPL__ __BSD__ __ASL__ __LGPL__
		factories.add(new PSystemSudokuFactory());
		// ::done
	}

	public static PSystemBuilder2 getInstance() {
		return singleton;
	}

	public void reset() {
		lastFactory = null;
	}

	public Diagram createDiagram(String[] split) {
		BrowserLog.consoleLog(PSystemBuilder2.class, "createDiagram start");
		final List<StringLocated> rawSource = new ArrayList<>();
		LineLocationImpl location = new LineLocationImpl("textarea", null);
		for (String s : clean(split)) {
			location = location.oneLineRead();
			rawSource.add(new StringLocated(s, location));
		}

		final PathSystem pathSystem = PathSystem.fetch();
		final Defines defines = Defines.createEmpty();
		final Charset charset = java.nio.charset.StandardCharsets.UTF_8;
		final DefinitionsContainer definitions = null;

		BrowserLog.consoleLog(PSystemBuilder2.class, "wip3");
		final TimLoader timLoader = new TimLoader(pathSystem, defines, charset, definitions, rawSource.get(0));
		BrowserLog.consoleLog(PSystemBuilder2.class, "wip4");
		timLoader.load(rawSource);
		BrowserLog.consoleLog(PSystemBuilder2.class, "createDiagram ok");
		List<StringLocated> tmp = timLoader.getResultList();
		Jaws.mutateExpands1(tmp);

		final PreprocessingArtifact preprocessing = timLoader.getPreprocessingArtifact();

		if (timLoader.isPreprocessorError())
			return new PSystemErrorPreprocessor(tmp, timLoader.getDebug(), preprocessing);

		return createDiagramFromPreprocessed(tmp, preprocessing);
	}

	@DuplicateCode(reference = "PSystemBuilder")
	public Diagram createDiagramFromPreprocessed(List<StringLocated> source, PreprocessingArtifact preprocessing) {
		final UmlSource umlSource = UmlSource.create(source, false);

		if (umlSource.getTotalLineCount() < 10)
			lastFactory = null;

		umlSource.patchBase64();

		final Collection<DiagramType> explainTypes = umlSource.getExplainTypes();
		final Collection<DiagramType> diagramTypes;
		if (explainTypes == null)
			diagramTypes = umlSource.getDiagramTypes();
		else
			diagramTypes = explainTypes;

		final UgDiagram basicCheck = PSystemErrorUtils.checkBasicError(diagramTypes, source, preprocessing, umlSource);
		if (basicCheck != null)
			return null;

		final List<PSystemError> errors = new ArrayList<>();

		if (lastFactory != null && diagramTypes.contains(lastFactory.getDiagramType())) {
			final Diagram sys = lastFactory.createSystem(null, umlSource, null, preprocessing);
			if (isOk(sys)) {
				if (explainTypes != null) {
					final List<Explanation> explanations = lastFactory.explain(null, umlSource, null, preprocessing);
					return new PSystemExplain(umlSource, preprocessing, explanations);
				}
				return sys;
			}
			errors.add((PSystemError) sys);
		}

		for (PSystemFactory f : factories) {
			if (!diagramTypes.contains(f.getDiagramType()))
				continue;
			if (f == lastFactory)
				continue;
			BrowserLog.consoleLog(PSystemBuilder2.class, "trying " + f.getClass());

			final Diagram sys = f.createSystem(null, umlSource, null, preprocessing);
			if (isOk(sys)) {
				BrowserLog.consoleLog(PSystemBuilder2.class, "ok!");
				this.lastFactory = f;
				if (explainTypes != null) {
					final List<Explanation> explanations = f.explain(null, umlSource, null, preprocessing);
					return new PSystemExplain(umlSource, preprocessing, explanations);
				}
				return sys;
			}
			errors.add((PSystemError) sys);
		}

		if (errors.size() == 0)
			return new PSystemUnsupported(umlSource, preprocessing);

		return PSystemErrorUtils.merge(errors);
	}

	private boolean isOk(Diagram ps) {
		if (ps == null || ps instanceof PSystemError || ps instanceof PSystemUnsupported)
			return false;

		return true;
	}

	/**
	 * Explains, line by line, how the given preprocessed source is parsed.
	 *
	 * <p>
	 * TeaVM counterpart of {@link net.sourceforge.plantuml.PSystemBuilder#explain}.
	 * It builds the {@link UmlSource} the same way as
	 * {@link #createDiagramFromPreprocessed(List, PreprocessingArtifact)}, finds
	 * the first factory that produces a valid diagram for the detected type, and
	 * delegates to its {@code explain}. {@code pathSystem} and {@code previous} are
	 * {@code null} here, as everywhere else in this builder.
	 *
	 * @param source        the preprocessed source lines
	 * @param preprocessing the preprocessing artifact
	 * @return the list of explanations, or {@code null} if no factory matched
	 */
	@DuplicateCode(reference = "PSystemBuilder")
	public List<Explanation> explain(List<StringLocated> source, PreprocessingArtifact preprocessing) {
		final UmlSource umlSource = UmlSource.create(source, false);
		umlSource.patchBase64();

		final Collection<DiagramType> diagramTypes = umlSource.getDiagramTypes();

		for (PSystemFactory systemFactory : factories) {
			if (diagramTypes.contains(systemFactory.getDiagramType()) == false)
				continue;

			final Diagram sys = systemFactory.createSystem(null, umlSource, null, preprocessing);
			if (isOk(sys))
				return systemFactory.explain(null, umlSource, null, preprocessing);
		}

		return null;
	}

	private List<String> clean(String[] tab) {
		final List<String> lines = new ArrayList<>();
		for (String s : tab)
			lines.add(s);

		while (lines.size() > 2 && lines.get(lines.size() - 1).trim().isEmpty())
			lines.remove(lines.size() - 1);

		return lines;
	}

}
