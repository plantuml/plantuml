/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.script.ScriptException;

import net.sourceforge.plantuml.klimt.MutableImage;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.UImageSvg;

public class AsciiMath implements ScientificEquation {
	// ::remove folder when __CORE__

	private final LatexBuilder builder;
	private final String tex;

	public AsciiMath(String form) throws ScriptException, NoSuchMethodException {
		this.tex = new ASCIIMathTeXImg().getTeX(form);
		this.builder = new LatexBuilder(tex);
	}

	public XDimension2D getDimension() {
		return builder.getDimension();
	}

	public UImageSvg getSvg(double scale, Color foregroundColor, Color backgroundColor)
			throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, IOException {
		return builder.getSvg(scale, foregroundColor, backgroundColor);
	}

	public MutableImage getImage(Color foregroundColor, Color backgroundColor)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return builder.getImage(foregroundColor, backgroundColor);
	}

	public String getSource() {
		return tex;
	}

}
