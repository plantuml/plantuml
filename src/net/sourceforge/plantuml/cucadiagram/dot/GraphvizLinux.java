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
package net.sourceforge.plantuml.cucadiagram.dot;

import java.io.File;

import net.sourceforge.plantuml.ISkinParam;

class GraphvizLinux extends AbstractGraphviz {

	GraphvizLinux(ISkinParam skinParam, String dotString, String... type) {
		super(skinParam, dotString, type);
	}

	@Override
	protected File specificDotExe() {
		final File usrLocalBinDot = new File("/usr/local/bin/dot");
		if (usrLocalBinDot.exists()) {
			return usrLocalBinDot;
		}
		final File usrBinDot = new File("/usr/bin/dot");
		if (usrBinDot.exists()) {
			return usrBinDot;
		}
		final File optLocalBinDot = new File("/opt/local/bin/dot");
		return optLocalBinDot;
	}

	@Override
	protected String getExeName() {
		return "dot";
	}

}
