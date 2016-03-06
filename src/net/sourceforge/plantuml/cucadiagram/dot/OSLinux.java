/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * Revision $Revision: 6104 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.io.File;

import net.sourceforge.plantuml.StringUtils;

class OSLinux extends OS {

	@Override
	File getExecutable(GraphvizLayoutStrategy strategy) {
		final File result = strategy.getSystemForcedExecutable();
		if (result != null) {
			return result;
		}
		final String fileName = getFileName(strategy);
		final File usrLocalBin = new File("/usr/local/bin/" + fileName);

		if (usrLocalBin.exists()) {
			return usrLocalBin;
		}
		final File usrBin = new File("/usr/bin/" + fileName);
		return usrBin;
	}

	@Override
	String getFileName(GraphvizLayoutStrategy strategy) {
		return StringUtils.goLowerCase(strategy.name());
	}

	@Override
	public String getCommand(GraphvizLayoutStrategy strategy) {
		return getExecutable(strategy).getAbsolutePath();
	}

}
