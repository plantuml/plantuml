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
 * Revision $Revision: 3837 $
 *
 */
package net.sourceforge.plantuml.oregon;

import net.sourceforge.plantuml.command.PSystemBasicFactory;

public class PSystemOregonFactory extends PSystemBasicFactory<PSystemOregon> {

	// public PSystemOregon getSystem() {
	// final Keyboard keyboard;
	// if (inputs == null) {
	// keyboard = new KeyboardList("");
	// } else {
	// keyboard = new KeyboardList(inputs);
	// }
	// system = new PSystemOregon(keyboard);
	// return system;
	// }

	@Override
	public PSystemOregon executeLine(PSystemOregon system, String line) {
		if (system == null && line.equalsIgnoreCase("run oregon trail")) {
			return new PSystemOregon();
		}
		if (system == null) {
			return null;
		}
		system.add(line);
		return system;
	}

}
