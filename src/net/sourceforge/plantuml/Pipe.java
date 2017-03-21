/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.preproc.Defines;

public class Pipe {

	private final Option option;
	private final InputStream is;
	private final PrintStream ps;
	private boolean closed = false;
	private final String charset;

	public Pipe(Option option, PrintStream ps, InputStream is, String charset) {
		this.option = option;
		this.is = is;
		this.ps = ps;
		this.charset = charset;
	}

	public boolean managePipe() throws IOException {
		boolean error = false;

		do {
			final String source = readOneDiagram();
			if (source == null) {
				ps.flush();
				return error;
			}
			final SourceStringReader sourceStringReader = new SourceStringReader(new Defines(), source,
					option.getConfig());

			if (option.isSyntax()) {
				final Diagram system = sourceStringReader.getBlocks().get(0).getDiagram();
				if (system instanceof UmlDiagram) {
					ps.println(((UmlDiagram) system).getUmlDiagramType().name());
					ps.println(system.getDescription());
				} else if (system instanceof PSystemError) {
					error = true;
					ps.println("ERROR");
					final PSystemError sys = (PSystemError) system;
					ps.println(sys.getHigherErrorPosition());
					for (ErrorUml er : sys.getErrorsUml()) {
						ps.println(er.getError());
					}
				} else {
					ps.println("OTHER");
					ps.println(system.getDescription());
				}
			} else if (option.isPipeMap()) {
				final String result = sourceStringReader.getCMapData(0, option.getFileFormatOption());
				ps.println(result);
			} else {
				final DiagramDescription result = sourceStringReader.generateImage(ps, 0, option.getFileFormatOption());
				if (option.getPipeDelimitor() != null) {
					ps.println(option.getPipeDelimitor());
				}
				if (result != null && "(error)".equalsIgnoreCase(result.getDescription())) {
					error = true;
					System.err.println("ERROR");
					final Diagram system = sourceStringReader.getBlocks().get(0).getDiagram();
					final PSystemError sys = (PSystemError) system;
					System.err.println(sys.getHigherErrorPosition());
					for (ErrorUml er : sys.getErrorsUml()) {
						System.err.println(er.getError());
					}
				}
			}
			ps.flush();
		} while (closed == false);
		return error;
	}

	private boolean isFinished(String s) {
		return s == null || s.startsWith("@end");
	}

	private String readOneDiagram() throws IOException {
		final StringBuilder sb = new StringBuilder();
		while (true) {
			final String s = readOneLine();
			if (s == null) {
				closed = true;
			} else {
				sb.append(s);
				sb.append("\n");
			}
			if (isFinished(s)) {
				break;
			}
		}
		if (sb.length() == 0) {
			return null;
		}
		String source = sb.toString();
		if (source.startsWith("@start") == false) {
			source = "@startuml\n" + source + "\n@enduml";
		}
		return source;
	}

	private String readOneLine() throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while (true) {
			final int read = is.read();
			if (read == -1) {
				if (baos.size() == 0) {
					return null;
				}
				break;
			}
			if (read != '\r') {
				baos.write(read);
			}
			if (read == '\n') {
				break;
			}
		}
		if (charset == null) {
			return new String(baos.toByteArray());
		}
		return new String(baos.toByteArray(), charset);

	}
}
