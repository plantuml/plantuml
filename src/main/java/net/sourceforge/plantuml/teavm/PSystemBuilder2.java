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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.activitydiagram3.ActivityDiagramFactory3;
import net.sourceforge.plantuml.api.PSystemFactory;
import net.sourceforge.plantuml.classdiagram.ClassDiagramFactory;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.error.PSystemErrorUtils;
import net.sourceforge.plantuml.error.PSystemUnsupported;
import net.sourceforge.plantuml.mindmap.MindMapDiagramFactory;
import net.sourceforge.plantuml.nwdiag.NwDiagramFactory;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagramFactory;
import net.sourceforge.plantuml.sudoku.PSystemSudokuFactory;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.timingdiagram.TimingDiagramFactory;
import net.sourceforge.plantuml.utils.LineLocationImpl;
import net.sourceforge.plantuml.wbs.WBSDiagramFactory;

public class PSystemBuilder2 {

	private final List<PSystemFactory> factories = new ArrayList<>();

	public PSystemBuilder2() {
		factories.add(new SequenceDiagramFactory());
		factories.add(new ActivityDiagramFactory3());
		factories.add(new ClassDiagramFactory());
		// factories.add(new PSystemVersionFactory());
		factories.add(new MindMapDiagramFactory());
		factories.add(new WBSDiagramFactory());
		factories.add(new NwDiagramFactory(DiagramType.UML));
		factories.add(new PSystemSudokuFactory());
		factories.add(new TimingDiagramFactory());

	}

	public Diagram createDiagram(String[] split) {
		final List<StringLocated> lines = new ArrayList<>();
		for (String s : clean(split))
			lines.add(new StringLocated(s, new LineLocationImpl("textarea", null)));

		final UmlSource source = UmlSource.create(lines, false);
		final DiagramType diagramType = source.getDiagramType();

		final PreprocessingArtifact preprocessing = new PreprocessingArtifact();
		final List<PSystemError> errors = new ArrayList<>();
		for (PSystemFactory systemFactory : factories) {
			if (diagramType != systemFactory.getDiagramType())
				continue;

			final Diagram sys = systemFactory.createSystem(null, source, null, preprocessing);
			if (isOk(sys))
				return sys;
			errors.add((PSystemError) sys);
		}

		if (errors.size() == 0)
			return new PSystemUnsupported(source, preprocessing);

		return PSystemErrorUtils.merge(errors);

	}

	private boolean isOk(Diagram ps) {
		if (ps == null || ps instanceof PSystemError)
			return false;

		return true;
	}

	private List<String> clean(String[] tab) {
		final List<String> lines = new ArrayList<>();
		for (String s : tab)
			lines.add(s);

		while (lines.size() > 2 && lines.get(lines.size() - 1).trim().isEmpty())
			lines.remove(lines.size() - 1);

		return lines;
	}

	public static void main(String[] args) {
		System.err.println("Hello!");

		final String source[] = new String[] { "@startuml", "class A", "class B extends A", "@enduml" };

		final Diagram diagram = new PSystemBuilder2().createDiagram(source);

		System.err.println("diagram=" + diagram);
	}

}
