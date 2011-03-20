/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 6104 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.graphic.GraphicStrings;

class AbstractGraphviz2 implements Graphviz {

	private final OS os;
	private final GraphvizLayoutStrategy strategy;
	private final String dotString;
	private final String[] type;


	AbstractGraphviz2(OS os, GraphvizLayoutStrategy strategy, String dotString, String... type) {
		if (type == null) {
			throw new IllegalArgumentException();
		}
		this.os = os;
		this.strategy = strategy;
		this.dotString = dotString;
		this.type = type;
	}


	final public void createPng(OutputStream os) throws IOException, InterruptedException {
		if (dotString == null) {
			throw new IllegalArgumentException();
		}

		if (illegalDotExe()) {
			createPngNoGraphviz(os, new FileFormatOption(FileFormat.valueOf(type[0].toUpperCase())));
			return;
		}
		final String cmd = getCommandLine();
		ProcessRunner p = null;
		try {
			Log.info("Starting Graphviz process " + cmd);
			Log.info("DotString size: " + dotString.length());
			p = new ProcessRunner(cmd);
			p.run(dotString.getBytes(), os);
			Log.info("Ending process ok");
		} catch (Throwable e) {
			e.printStackTrace();
			Log.error("Error: " + e);
			Log.error("The command was " + cmd);
			Log.error("");
			Log.error("Try java -jar plantuml.jar -testdot to figure out the issue");
			Log.error("");
		} finally {
			Log.info("Ending Graphviz process");
		}
		if (OptionFlags.getInstance().isCheckDotError() && p != null && p.getError().length() > 0) {
			Log.error("GraphViz error stream : " + p.getError());
			if (OptionFlags.getInstance().isCheckDotError()) {
				throw new IllegalStateException("Dot error " + p.getError());
			}
		}
		if (OptionFlags.getInstance().isCheckDotError() && p != null && p.getOut().length() > 0) {
			Log.error("GraphViz out stream : " + p.getOut());
			if (OptionFlags.getInstance().isCheckDotError()) {
				throw new IllegalStateException("Dot out " + p.getOut());
			}
		}

	}


	private String getCommandLine() {
		final StringBuilder sb = new StringBuilder();
		sb.append(os.getCommand(strategy));
		appendImageType(sb);
		return sb.toString();
	}


	public String dotVersion() throws IOException, InterruptedException {
		final String cmd = os.getCommand(strategy)+" -V";
		return executeCmd(cmd);
	}


	public File getDotExe() {
		return os.getExecutable(strategy);
	}

	private boolean illegalDotExe() {
		final File exe = getDotExe();
		return exe == null || exe.isFile() == false || exe.canRead() == false;
	}

	private String executeCmd(final String cmd) throws IOException, InterruptedException {
		final ProcessRunner p = new ProcessRunner(cmd);
		p.run(null, null);
		final StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotEmpty(p.getOut())) {
			sb.append(p.getOut());
		}
		if (StringUtils.isNotEmpty(p.getError())) {
			if (sb.length() > 0) {
				sb.append(' ');
			}
			sb.append(p.getError());
		}
		return sb.toString().replace('\n', ' ').trim();
	}

	final private void createPngNoGraphviz(OutputStream os, FileFormatOption format) throws IOException {
		final List<String> msg = new ArrayList<String>();
		final File exe = getDotExe();
		msg.add("Dot Executable: " + exe);
		if (exe != null) {
			if (exe.exists() == false) {
				msg.add("File does not exist");
			} else if (exe.isDirectory()) {
				msg.add("It should be an executable, not a directory");
			} else if (exe.isFile() == false) {
				msg.add("Not a valid file");
			} else if (exe.canRead() == false) {
				msg.add("File cannot be read");
			}
		}
		msg.add("Cannot find Graphviz. You should try");
		msg.add(" ");
		msg.add("@startuml");
		msg.add("testdot");
		msg.add("@enduml");
		msg.add(" ");
		msg.add(" or ");
		msg.add(" ");
		msg.add("java -jar plantuml.jar -testdot");
		final GraphicStrings errorResult = new GraphicStrings(msg);
		errorResult.writeImage(os, format);
	}

	private final void appendImageType(final StringBuilder sb) {
		for (String t : type) {
			sb.append(" -T" + t + " ");
		}
	}

}
