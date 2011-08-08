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
package net.sourceforge.plantuml.directdot;

import java.io.UnsupportedEncodingException;

import net.sourceforge.plantuml.DiagramType;
import net.sourceforge.plantuml.PSystemBasicFactory;

public class PSystemDotFactory implements PSystemBasicFactory {

	private StringBuilder data;
	private boolean first;
	private final DiagramType diagramType;

	public PSystemDotFactory(DiagramType diagramType) {
		this.diagramType = diagramType;
	}

	public void init(String startLine) {
		data = null;
		first = true;
	}

	public boolean executeLine(String line) {
		if (first && line.matches("digraph\\s+\\w+\\s+\\{")) {
			data = new StringBuilder(line);
			data.append("\n");
			return true;
		}
		first = false;
		if (data == null) {
			return false;
		}
		data.append(line);
		data.append("\n");
		return true;
	}

	public PSystemDot getSystem() {
		try {
			return new PSystemDot(data.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public DiagramType getDiagramType() {
		return diagramType;
	}

}
