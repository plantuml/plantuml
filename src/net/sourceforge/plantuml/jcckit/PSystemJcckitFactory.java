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
 * Revision $Revision: 3837 $
 *
 */
package net.sourceforge.plantuml.jcckit;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.DiagramType;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.PSystemBasicFactory;

public class PSystemJcckitFactory implements PSystemBasicFactory {

	private StringBuilder data;
	private boolean first;
	private int width;
	private int height;

	private final DiagramType diagramType;

	public PSystemJcckitFactory(DiagramType diagramType) {
		this.diagramType = diagramType;
	}


	public void init(String startLine) {
		this.data = null;
		this.width = 640;
		this.height = 400;
		if (diagramType == DiagramType.UML) {
			first = true;
		} else if (diagramType == DiagramType.JCCKIT) {
			first = false;
			extractDimension(startLine);
			data = new StringBuilder();
		} else {
			throw new IllegalStateException(diagramType.name());
		}

	}

	private void extractDimension(String startLine) {
		final Pattern p = Pattern.compile("\\((\\d+),(\\d+)\\)");
		final Matcher m = p.matcher(startLine);
		final boolean ok = m.find();
		if (ok) {
			width = Integer.parseInt(m.group(1));
			height = Integer.parseInt(m.group(2));
		}
	}
	
	String getDimension() {
		return ""+width+"-"+height;
	}


	public PSystemJcckit getSystem() {
		final Properties p = new Properties();
		try {
			p.load(new StringReader(data.toString()));
		} catch (IOException e) {
			Log.error("Error " + e);
			e.printStackTrace();
			return null;
		}
		return new PSystemJcckit(p, width, height);
	}

	public boolean executeLine(String line) {
		if (first && line.startsWith("jcckit")) {
			data = new StringBuilder();
			extractDimension(line);
			return true;
		}
		first = false;
		if (data == null) {
			return false;
		}
		data.append(line.trim());
		data.append("\n");
		return true;
	}

	public DiagramType getDiagramType() {
		return diagramType;
	}

}
