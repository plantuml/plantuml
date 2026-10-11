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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.sourceforge.plantuml.UgDiagram;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.mcp.McpResult;
import net.sourceforge.plantuml.preproc.ReadLine;
import net.sourceforge.plantuml.preproc.ReadLineReader;
import net.sourceforge.plantuml.text.StringLocated;

/**
 * Entry point of the experimental TeaVM C backend.
 *
 * <pre>
 * plantuml-c [input.puml [output.svg|output.png]]
 * </pre>
 *
 * Reads a PlantUML source from the given file (the standard input if there is
 * none) and writes the SVG in the given file (the standard output if there is
 * none), or the PNG when the name of the output file ends with ".png". Passing
 * the files is the safest way on Windows, where the console encodes the
 * standard streams. Only mindmap diagrams are supported for now (see
 * {@link PSystemBuilderC}).
 *
 * The SVG is the one {@code java -jar plantuml.jar -tsvg} writes on Windows:
 * same export code ({@link UgDiagram#exportSvg}), and text measured with the
 * metrics of the embedded fonts, which are those of Java's fonts on Windows
 * (see {@code StringBounderEmbeddedFont}). The PNG has the size and the layout
 * of the one {@code -tpng} writes, drawn by plutovg ({@link PngCanvas}) with
 * the drivers of the JVM ({@link UgDiagram#exportPng}): only the antialiasing
 * differs.
 *
 * On a diagram error, the message is written on stderr and nothing on stdout.
 * There is no non-zero exit code: {@code System.exit} does not exist in the
 * classlib of the TeaVM C backend.
 */
public class MainC {
	// ::remove file when JAVA8

	public static void main(String[] args) throws IOException {
		final InputStream is;
		if (args.length > 0) {
			if (new File(args[0]).isFile() == false) {
				System.err.println("File not found: " + args[0]);
				return;
			}
			is = new FileInputStream(args[0]);
		} else {
			is = System.in;
		}

		// Read as the command line reads a file (byte order mark, U+2013...)
		final List<String> lines = new ArrayList<>();
		try (ReadLine reader = ReadLineReader.create(new InputStreamReader(is, StandardCharsets.UTF_8), "c")) {
			StringLocated line;
			while ((line = reader.readLine()) != null)
				lines.add(line.getString());
		}

		final Diagram diagram = PSystemBuilderC.getInstance().createDiagram(lines.toArray(new String[0]));

		if (diagram instanceof PSystemError || diagram instanceof UgDiagram == false) {
			System.err.println(diagram == null ? "No diagram" : new McpResult(diagram).getErrorMessage());
			return;
		}

		final boolean png = args.length > 1 && args[1].toLowerCase(Locale.ROOT).endsWith(".png");
		final byte[] image;
		try {
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			if (png)
				((UgDiagram) diagram).exportPng(baos, PngCanvas::new);
			else
				((UgDiagram) diagram).exportSvg(baos);
			image = baos.toByteArray();
		} catch (Exception e) {
			System.err.println("Rendering failed: " + e);
			return;
		}

		if (args.length > 1) {
			try (OutputStream os = new FileOutputStream(args[1])) {
				os.write(image);
			}
		} else {
			System.out.write(image, 0, image.length);
			System.out.flush();
		}
	}

}
