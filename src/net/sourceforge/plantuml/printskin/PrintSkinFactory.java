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
 * Revision $Revision: 3835 $
 *
 */
package net.sourceforge.plantuml.printskin;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.PSystemBasicFactory;

public class PrintSkinFactory implements PSystemBasicFactory {

	private PrintSkin system;

	// private List<Command> cmds;

	public PrintSkinFactory() {
		reset();
	}

	public void reset() {
	}

	public PrintSkin getSystem() {
		return system;
	}

	static final Pattern p = Pattern.compile("(?i)^testskin\\s+([\\w.]+)\\s*(.*)$");

	public boolean executeLine(String line) {
		final Matcher m = p.matcher(line);
		if (m.find() == false) {
			return false;
		}
		system = new PrintSkin(m.group(1), Arrays.asList(m.group(2)));
		return true;
	}

}
