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
 * Revision $Revision: 10765 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.io.File;

class GraphvizLinux extends AbstractGraphviz {

	GraphvizLinux(String dotString, String... type) {
		super(dotString, type);
	}

	@Override
	protected File specificDotExe() {
		final File usrLocalBinDot = new File("/usr/local/bin/dot");

		if (usrLocalBinDot.exists()) {
			return usrLocalBinDot;
		}
		final File usrBinDot = new File("/usr/bin/dot");
		return usrBinDot;
	}

}
