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
package net.sourceforge.plantuml.math;

import java.awt.Color;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import net.sourceforge.plantuml.BackSlash;
import net.sourceforge.plantuml.SvgString;

public class AsciiMath implements ScientificEquation {

	private static final String ASCIIMATH_PARSER_JS_LOCATION = "/net/sourceforge/plantuml/math/";

	private static String JAVASCRIPT_CODE;

	private final LatexBuilder builder;
	private final String tex;

	static {
		try {
			final BufferedReader br = new BufferedReader(new InputStreamReader(
					AsciiMath.class.getResourceAsStream(ASCIIMATH_PARSER_JS_LOCATION + "ASCIIMathTeXImg.js"), "UTF-8"));
			final StringBuilder sb = new StringBuilder();
			String s = null;
			while ((s = br.readLine()) != null) {
				sb.append(s);
				sb.append(BackSlash.NEWLINE);
			}
			br.close();
			JAVASCRIPT_CODE = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public AsciiMath(String form) throws ScriptException, NoSuchMethodException {
		final ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
		engine.eval(JAVASCRIPT_CODE);
		final Invocable inv = (Invocable) engine;
		this.tex = patchColor((String) inv.invokeFunction("plantuml", form));
		this.builder = new LatexBuilder(tex);
	}

	private String patchColor(String latex) {
		return latex.replace("\\color{", "\\textcolor{");
	}

	public Dimension2D getDimension() {
		return builder.getDimension();
	}

	public SvgString getSvg(double scale, Color foregroundColor, Color backgroundColor) throws ClassNotFoundException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, InstantiationException, IOException {
		return builder.getSvg(scale, foregroundColor, backgroundColor);
	}

	public BufferedImage getImage(double scale, Color foregroundColor, Color backgroundColor)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return builder.getImage(scale, foregroundColor, backgroundColor);
	}

	public String getSource() {
		return tex;
	}

}
