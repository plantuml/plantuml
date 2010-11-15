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
 * Revision $Revision: 4207 $
 *
 */
package net.sourceforge.plantuml.eps;

import java.awt.geom.PathIterator;
import java.util.HashMap;
import java.util.Map;

public class EpsGraphicsMacro extends EpsGraphics {

	private final PostScriptData data = new PostScriptData();
	private final PostScriptCommandMacro rquadto = new PostScriptCommandMacro("rquadto");

	public EpsGraphicsMacro() {
		super();
		rquadto.add(new PostScriptCommandRaw("3 index 3 index 4 2 roll rcurveto"));
	}

	@Override
	protected void append(String s) {
		if (s.indexOf("  ") != -1) {
			throw new IllegalArgumentException(s);
		}
		data.add(new PostScriptCommandRaw(s));
	}

	@Override
	protected String getBodyString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(rquadto.getPostStringDefinition());
		for (PostScriptCommandMacro macro : macros.keySet()) {
			sb.append(macro.getPostStringDefinition());
		}
		sb.append(data.toPostString());
		return sb.toString();
	}

	// FONT
	private double posX;
	private double posY;
	private int macroCpt;
	private final Map<PostScriptCommandMacro, String> macros = new HashMap<PostScriptCommandMacro, String>();

	@Override
	public void newpath() {
		append("0 setlinewidth");
		append("[] 0 setdash");
		appendColor(getColor());
		append("newpath");
	}

	@Override
	public void closepath() {
		macroInProgress.add(new PostScriptCommandRaw("closepath"));
		closeMacro();
	}

	@Override
	public void fill(int windingRule) {
		if (windingRule == PathIterator.WIND_EVEN_ODD) {
			append("eofill");
		} else if (windingRule == PathIterator.WIND_NON_ZERO) {
			append("fill");
		}
	}

	private PostScriptCommandMacro macroInProgress = null;

	@Override
	public void moveto(double x1, double y1) {
		data.add(new PostScriptCommandMoveTo(x1, y1));
		this.posX = x1;
		this.posY = y1;
		openMacro();
		ensureVisible(x1, y1);
	}

	@Override
	public void lineto(double x1, double y1) {
		final PostScriptCommand cmd = new PostScriptCommandLineTo(x1 - posX, y1 - posY);
		macroInProgress.add(cmd);
		this.posX = x1;
		this.posY = y1;
		ensureVisible(x1, y1);
	}

	@Override
	public void curveto(double x1, double y1, double x2, double y2, double x3, double y3) {
		final PostScriptCommandCurveTo cmd = new PostScriptCommandCurveTo(x1 - posX, y1 - posY, x2 - posX, y2 - posY,
				x3 - posX, y3 - posY);
		macroInProgress.add(cmd);
		this.posX = x3;
		this.posY = y3;
		ensureVisible(x1, y1);
		ensureVisible(x2, y2);
		ensureVisible(x3, y3);
	}

	@Override
	public void quadto(double x1, double y1, double x2, double y2) {
		final PostScriptCommandQuadTo cmd = new PostScriptCommandQuadTo(x1 - posX, y1 - posY, x2 - posX, y2 - posY);
		macroInProgress.add(cmd);
		this.posX = x2;
		this.posY = y2;
		ensureVisible(x1, y1);
		ensureVisible(x2, y2);
	}

	private void openMacro() {
		if (macroInProgress != null) {
			throw new IllegalStateException();
		}
		macroInProgress = new PostScriptCommandMacro(macroName());
	}

	private String macroName() {
		return "P$" + Integer.toString(macroCpt, 36);
	}

	private void closeMacro() {
		if (macroInProgress == null) {
			throw new IllegalStateException();
		}
		final String existingName = macros.get(macroInProgress);
		if (existingName == null) {
			macros.put(macroInProgress, macroInProgress.getName());
			append(macroName());
			macroCpt++;
		} else {
			append(existingName);
		}
		macroInProgress = null;
	}

}
