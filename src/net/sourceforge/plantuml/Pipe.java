/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.preproc.Defines;

public class Pipe {

	private final Option option;
	private final InputStream is;
	private final PrintStream ps;
	private boolean closed = false;
	private final String charset;
	private final Stdrpt stdrpt;

	public Pipe(Option option, PrintStream ps, InputStream is, String charset) {
		this.option = option;
		this.is = is;
		this.ps = ps;
		this.charset = charset;
		this.stdrpt = option.getStdrpt();
	}

	public void managePipe(ErrorStatus error) throws IOException {
		final boolean noStdErr = option.isPipeNoStdErr();
		int nb = 0;
		do {
			final String source = readOneDiagram();
			if (source == null) {
				ps.flush();
				if (nb == 0) {
					// error.goNoData();
				}
				return;
			}
			nb++;
			final Defines defines = option.getDefaultDefines();
			final SourceStringReader sourceStringReader = new SourceStringReader(defines, source, option.getConfig());
			if (option.isComputeurl()) {
				for (BlockUml s : sourceStringReader.getBlocks()) {
					ps.println(s.getEncodedUrl());
				}
			} else if (option.isSyntax()) {
				final Diagram system = sourceStringReader.getBlocks().get(0).getDiagram();
				if (system instanceof UmlDiagram) {
					error.goOk();
					ps.println(((UmlDiagram) system).getUmlDiagramType().name());
					ps.println(system.getDescription());
				} else if (system instanceof PSystemError) {
					error.goWithError();
					stdrpt.printInfo(ps, (PSystemError) system);
				} else {
					error.goOk();
					ps.println("OTHER");
					ps.println(system.getDescription());
				}
			} else if (option.isPipeMap()) {
				final String result = sourceStringReader.getCMapData(option.getImageIndex(),
						option.getFileFormatOption());
				// https://forum.plantuml.net/10049/2019-pipemap-diagrams-containing-links-give-zero-exit-code
				// We don't check errors
				error.goOk();
				if (result == null) {
					ps.println();
				} else {
					ps.println(result);
				}
			} else {
				final OutputStream os = noStdErr ? new ByteArrayOutputStream() : ps;
				final DiagramDescription result = sourceStringReader.outputImage(os, option.getImageIndex(),
						option.getFileFormatOption());
				printInfo(noStdErr ? ps : System.err, sourceStringReader);
				if (result != null && "(error)".equalsIgnoreCase(result.getDescription())) {
					error.goWithError();
				} else {
					error.goOk();
					if (noStdErr) {
						final ByteArrayOutputStream baos = (ByteArrayOutputStream) os;
						baos.close();
						ps.write(baos.toByteArray());
					}
				}
				if (option.getPipeDelimitor() != null) {
					ps.println(option.getPipeDelimitor());
				}
			}
			ps.flush();
		} while (closed == false);
		if (nb == 0) {
			// error.goNoData();
		}
	}

	private void printInfo(final PrintStream output, final SourceStringReader sourceStringReader) {
		final List<BlockUml> blocks = sourceStringReader.getBlocks();
		if (blocks.size() == 0) {
			stdrpt.printInfo(output, null);
		} else {
			stdrpt.printInfo(output, blocks.get(0).getDiagram());
		}
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
			} else if (s.startsWith("@@@format ")) {
				manageFormat(s);
			} else {
				sb.append(s);
				sb.append(BackSlash.NEWLINE);
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

	private void manageFormat(String s) {
		if (s.contains("png")) {
			option.setFileFormatOption(new FileFormatOption(FileFormat.PNG));
		} else if (s.contains("svg")) {
			option.setFileFormatOption(new FileFormatOption(FileFormat.SVG));
		}
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
			if (read != '\r' && read != '\n') {
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
