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
 * Revision $Revision: 4826 $
 *
 */
package net.sourceforge.plantuml.eps;

import java.io.File;
import java.io.IOException;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.dot.ProcessRunner;

abstract class AbstractInkscape implements Inkscape {

	abstract protected File specificExe();

	AbstractInkscape() {
		throw new UnsupportedOperationException("Not used anymore");
	}

	final public void createEps(File svg, File eps) throws IOException, InterruptedException {
		final StringBuilder cmd = new StringBuilder();
		appendFilePath(cmd, searchDotExe());
		cmd.append(" -E ");
		appendFilePath(cmd, eps);
		cmd.append(" ");
		appendFilePath(cmd, svg);
		eps.delete();
		if (eps.exists()) {
			throw new IOException("Cannot delete " + eps);
		}
		String result = executeCmd(cmd.toString());
		if (eps.exists() == false) {
			throw new IOException("File not created " + eps);
		}
	}

	private File searchDotExe() {
		final String getenv = InkscapeUtils.getenvInkscape();
		if (getenv == null) {
			return specificExe();
		}
		return new File(getenv);
	}

	abstract protected void appendFilePath(final StringBuilder sb, File file);

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

}
