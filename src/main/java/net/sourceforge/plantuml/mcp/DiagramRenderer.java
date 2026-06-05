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
package net.sourceforge.plantuml.mcp;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramDescription;

public class DiagramRenderer {

	public McpResult render(String source, String format) throws IOException {
		if (source.startsWith("@start") == false)
			return McpResult.noAtStart();

		final SourceStringReader ss = new SourceStringReader(source, UTF_8);
		final List<BlockUml> blocks = ss.getBlocks();
		if (blocks.size() != 1)
			return McpResult.badInput();

		final BlockUml blockUml = blocks.get(0);
		final Diagram diagram = blockUml.getDiagram();
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final DiagramDescription dr = ss.outputImage(baos, 0, new FileFormatOption(FileFormat.SVG));

		final String svg = new String(baos.toByteArray(), UTF_8);
		return new McpResult(diagram, svg);
	}

}
