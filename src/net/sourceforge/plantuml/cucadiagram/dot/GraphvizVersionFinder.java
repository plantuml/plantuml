/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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
 * Revision $Revision: 11325 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.io.File;

import net.sourceforge.plantuml.StringUtils;

public class GraphvizVersionFinder {

	final private File dotExe;

	public GraphvizVersionFinder(File dotExe) {
		this.dotExe = dotExe;
	}

	public GraphvizVersion getVersion() {
		final String s = dotVersion();
		if (s.contains("2.34.0")) {
			return GraphvizVersion.V2_34_0;
		}
		return GraphvizVersion.COMMON;
	}

	public String dotVersion() {
		final String cmd[] = getCommandLine();

		final ProcessRunner p = new ProcessRunner(cmd);
		final ProcessState state = p.run2(null, null);
		if (state != ProcessState.TERMINATED_OK) {
			return "?";
		}
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

	private String[] getCommandLine() {
		return new String[] { dotExe.getAbsolutePath(), "-V" };
	}

}
