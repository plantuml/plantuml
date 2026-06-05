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

import java.io.IOException;
import java.util.List;

import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.Diagram;

/**
 * Syntax checker for a single PlantUML diagram, intended to back an MCP
 * <code>check_syntax</code> tool (and reusable from TeaVM/browser builds).
 *
 * <p>
 * Unlike the rendering pipeline, this class never produces an image: it parses
 * the source and reports a structured diagnostic. When the source is valid it
 * reports the detected diagram type and a few counters; when it is not, it
 * reports the offending line (1-based) and the error message.
 *
 * <p>
 * Scope for now: a single diagram per call (the first {@code @start.../@end...}
 * block). Preprocessing (e.g. {@code !include}, stdlib) is expected to run, but
 * under a sandboxed security profile by default; opening the filesystem or the
 * network to arbitrary includes must remain an explicit opt-in of the caller.
 *
 * <p>
 * All methods are stubs for now (TDD): they compile but do nothing useful yet.
 */
public class SyntaxChecker {

	/**
	 * Checks the syntax of the first diagram found in the given source.
	 *
	 * @param source the raw PlantUML source (expected to contain one diagram)
	 * @return a structured result; never {@code null}
	 * @throws IOException
	 */
	public McpResult check(String source) throws IOException {
		if (source.startsWith("@start") == false)
			return McpResult.noAtStart();

		final SourceStringReader ss = new SourceStringReader(source, UTF_8);
		final List<BlockUml> blocks = ss.getBlocks();
		if (blocks.size() != 1)
			return McpResult.badInput();

		final BlockUml blockUml = blocks.get(0);
		final Diagram diagram = blockUml.getDiagram();
		return new McpResult(diagram);
	}

}
