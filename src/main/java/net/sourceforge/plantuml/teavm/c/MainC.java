/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
 */
package net.sourceforge.plantuml.teavm.c;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.UgDiagram;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.mcp.DiagramRendererTeaVM;
import net.sourceforge.plantuml.mcp.McpResult;

/**
 * Entry point of the experimental TeaVM C backend.
 *
 * <pre>
 * plantuml-c [input.puml [output.svg]]
 * </pre>
 *
 * Reads a PlantUML source from the given file (the standard input if there is
 * none) and writes the SVG in the given file (the standard output if there is
 * none). Passing the files is the safest way on Windows, where the console
 * encodes the standard streams. Only mindmap diagrams are supported for now
 * (see {@link PSystemBuilderC}).
 *
 * On a diagram error, the message is written on stderr and nothing on stdout.
 * There is no non-zero exit code: {@code System.exit} does not exist in the
 * classlib of the TeaVM C backend.
 */
public class MainC {

	public static void main(String[] args) throws IOException {
		final List<String> lines = new ArrayList<>();
		final InputStream is = args.length > 0 ? new FileInputStream(args[0]) : System.in;
		final BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
		String line;
		while ((line = reader.readLine()) != null)
			lines.add(line);

		final Diagram diagram = PSystemBuilderC.getInstance().createDiagram(lines.toArray(new String[0]));

		if (diagram instanceof PSystemError || diagram instanceof UgDiagram == false) {
			System.err.println(diagram == null ? "No diagram" : new McpResult(diagram).getErrorMessage());
			return;
		}

		final String svg;
		try {
			svg = DiagramRendererTeaVM.buildSvg((UgDiagram) diagram);
		} catch (Exception e) {
			System.err.println("Rendering failed: " + e);
			return;
		}

		if (args.length > 1) {
			try (OutputStream os = new FileOutputStream(args[1])) {
				os.write(svg.getBytes(StandardCharsets.UTF_8));
			}
		} else
			System.out.println(svg);
	}

}
