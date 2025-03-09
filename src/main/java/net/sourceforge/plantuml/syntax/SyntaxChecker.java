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
package net.sourceforge.plantuml.syntax;

import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.text.BackSlash;
import net.sourceforge.plantuml.utils.LineLocation;
import net.sourceforge.plantuml.utils.LineLocationImpl;

public class SyntaxChecker {
	// ::remove folder when __HAXE__
	// ::remove file when __CORE__

	public static SyntaxResult checkSyntax(List<String> source) {
		final StringBuilder sb = new StringBuilder();
		for (String s : source) {
			sb.append(s);
			sb.append(BackSlash.NEWLINE);
		}
		return checkSyntax(sb.toString());
	}

	public static SyntaxResult checkSyntax(String source) {
		OptionFlags.getInstance().setQuiet(true);
		final SyntaxResult result = new SyntaxResult();

		if (source.startsWith("@startuml\n") == false) {
			result.setError(true);
			result.setLineLocation(new LineLocationImpl("", null).oneLineRead());
			result.addErrorText("No @startuml/@enduml found");
			return result;
		}
		if (source.endsWith("@enduml\n") == false && source.endsWith("@enduml") == false) {
			result.setError(true);
			result.setLineLocation(lastLineNumber(source));
			result.addErrorText("No @enduml found");
			return result;
		}
		final SourceStringReader sourceStringReader = new SourceStringReader(Defines.createEmpty(), source,
				Collections.<String>emptyList());

		final List<BlockUml> blocks = sourceStringReader.getBlocks();
		if (blocks.size() == 0) {
			result.setError(true);
			result.setLineLocation(lastLineNumber(source));
			result.addErrorText("No @enduml found");
			return result;
		}
		final Diagram system = blocks.get(0).getDiagram();
		result.setCmapData(system.hasUrl());
		if (system instanceof UmlDiagram) {
			result.setUmlDiagramType(((UmlDiagram) system).getUmlDiagramType());
			result.setDescription(system.getDescription().getDescription());
		} else if (system instanceof PSystemError) {
			result.setError(true);
			final PSystemError sys = (PSystemError) system;
			result.setLineLocation(sys.getLineLocation());
			result.setSystemError(sys);
			for (ErrorUml er : sys.getErrorsUml())
				result.addErrorText(er.getError());
		} else {
			result.setDescription(system.getDescription().getDescription());
		}
		return result;
	}

	public static SyntaxResult checkSyntaxFair(String source) {
		final SyntaxResult result = new SyntaxResult();
		final SourceStringReader sourceStringReader = new SourceStringReader(Defines.createEmpty(), source,
				Collections.<String>emptyList());

		final List<BlockUml> blocks = sourceStringReader.getBlocks();
		if (blocks.size() == 0) {
			result.setError(true);
			result.setLineLocation(lastLineNumber(source));
			result.addErrorText("No @enduml found");
			return result;
		}

		final Diagram system = blocks.get(0).getDiagram();
		result.setCmapData(system.hasUrl());
		if (system instanceof UmlDiagram) {
			result.setUmlDiagramType(((UmlDiagram) system).getUmlDiagramType());
			result.setDescription(system.getDescription().getDescription());
		} else if (system instanceof PSystemError) {
			result.setError(true);
			final PSystemError sys = (PSystemError) system;
			result.setLineLocation(sys.getLineLocation());
			for (ErrorUml er : sys.getErrorsUml()) {
				result.addErrorText(er.getError());
			}
			result.setSystemError(sys);
		} else {
			result.setDescription(system.getDescription().getDescription());
		}
		return result;
	}

	private static LineLocation lastLineNumber(String source) {
		LineLocationImpl result = new LineLocationImpl("", null).oneLineRead();
		for (int i = 0; i < source.length(); i++)
			if (source.charAt(i) == '\n')
				result = result.oneLineRead();

		return result;
	}
}
