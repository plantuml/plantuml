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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.ErrorUmlType;
import net.sourceforge.plantuml.Previous;
import net.sourceforge.plantuml.command.PSystemAbstractFactory;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.error.PSystemErrorUtils;
import net.sourceforge.plantuml.explain.Explanation;
import net.sourceforge.plantuml.ngdot.model.DotGraph;
import net.sourceforge.plantuml.ngdot.parser.DotParser;
import net.sourceforge.plantuml.ngdot.parser.DotSyntaxException;
import net.sourceforge.plantuml.nio.PathSystem;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.utils.StartUtils;
import net.sourceforge.plantuml.version.IteratorCounter2;

/**
 * Builds a {@link PSystemNgDot} from a {@code @startdot} block: the whole
 * block (already preprocessed) is parsed at once by {@link DotParser}, and a
 * syntax error is reported on the faulty line.
 */
public class PSystemNgDotFactory extends PSystemAbstractFactory {

	// ASCII classes only: the regular expressions of the TeaVM class library do not
	// support \p{L} / \p{N}, and the pattern did not compile there (which broke the
	// start of the TeaVM builds). A graph name with non ASCII letters must be quoted.
	private static final Pattern GRAPHVIZ_DOT_HEADER_PATTERN = Pattern.compile("\\s*" // optional leading whitespace
			+ "(strict\\s+)?" // optional 'strict'
			+ "(di)?" // optional 'di'
			+ "graph\\s+" // 'graph' keyword
			+ "(" + "[_a-zA-Z][_a-zA-Z0-9]*" // identifier
			+ "|" + "-?(?:\\.[0-9]+|[0-9]+(?:\\.[0-9]*)?)" // number
			+ "|" + "\"([^\"\\\\]|\\\\\")*\"" // quoted string
			+ ")?" + "\\s*\\{\\s*" // opening brace
	);

	public PSystemNgDotFactory() {
		super(DiagramType.DOT);
	}

	@Override
	public List<Explanation> explain(PathSystem pathSystem, UmlSource source, Previous previous,
			PreprocessingArtifact preprocessing) {
		throw new UnsupportedOperationException("wip " + getClass());
	}

	@Override
	public Diagram createSystem(PathSystem pathSystem, UmlSource source, Previous previous,
			PreprocessingArtifact preprocessing) {
		source = source.removeInitialNoise();
		final IteratorCounter2 it = source.iterator2();
		final StringLocated startLine = it.next();

		final List<StringLocated> lines = new ArrayList<StringLocated>();
		StringLocated last = startLine;
		while (it.hasNext()) {
			final StringLocated s = it.next();
			last = s;
			if (StartUtils.isEndDirective(s.getString()))
				break;
			lines.add(s);
		}

		final StringBuilder text = new StringBuilder();
		boolean empty = true;
		for (StringLocated s : lines) {
			text.append(s.getString());
			text.append('\n');
			if (s.getString().trim().length() > 0)
				empty = false;
		}
		if (empty)
			return buildEmptyError(source, last, it.getTrace(), preprocessing);

		try {
			final DotGraph graph = DotParser.parse(text.toString());
			return new PSystemNgDot(source, graph, preprocessing);
		} catch (DotSyntaxException e) {
			final int index = Math.min(e.getLine(), lines.size() - 1);
			final StringLocated faulty = lines.get(index);
			final List<StringLocated> trace = new ArrayList<StringLocated>();
			trace.add(startLine);
			trace.addAll(lines.subList(0, index + 1));
			final ErrorUml err = new ErrorUml(ErrorUmlType.SYNTAX_ERROR, e.getMessage(), 0, faulty,
					getDiagramType());
			return PSystemErrorUtils.buildV2(source, err, null, trace, preprocessing);
		}
	}

	/**
	 * Tells whether a line looks like the first line of a DOT graph. Used to
	 * suggest {@code @startdot} when such a graph is written in
	 * {@code @startuml}.
	 */
	public static boolean isGraphvizDotHeader(String line) {
		final Matcher matcher = GRAPHVIZ_DOT_HEADER_PATTERN.matcher(line);
		return matcher.matches();
	}

}
