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
 */
package net.sourceforge.plantuml.version;

import java.io.IOException;

import net.sourceforge.plantuml.DiagramType;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.PSystemBasicFactory;

public class PSystemVersionFactory implements PSystemBasicFactory {

	private PSystemVersion system;

	public void init(String startLine) {
	}

	public boolean executeLine(String line) {
		try {
			if (line.matches("(?i)^(authors?|about)\\s*$")) {
				system = PSystemVersion.createShowAuthors();
				return true;
			}
			if (line.matches("(?i)^version\\s*$")) {
				system = PSystemVersion.createShowVersion();
				return true;
			}
			if (line.matches("(?i)^testdot\\s*$")) {
				system = PSystemVersion.createTestDot();
				return true;
			}
		} catch (IOException e) {
			Log.error("Error " + e);

		}
		return false;
	}

	public PSystemVersion getSystem() {
		return system;
	}
	
	public DiagramType getDiagramType() {
		return DiagramType.UML;
	}


}
