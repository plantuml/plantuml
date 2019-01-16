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
import java.awt.Insets;
import java.lang.reflect.InvocationTargetException;

import javax.swing.Icon;

public class TeXIconBuilder {

	private Icon icon;

	public TeXIconBuilder(String tex, Color foregroundColor) throws ClassNotFoundException, NoSuchMethodException,
			SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		// TeXFormula formula = new TeXFormula(latex);
		final Class<?> clTeXFormula = Class.forName("org.scilab.forge.jlatexmath.TeXFormula");
		final Object formula = clTeXFormula.getConstructor(String.class).newInstance(tex);

		// TeXIcon icon = formula.new TeXIconBuilder().setStyle(TeXConstants.STYLE_DISPLAY).setSize(20).build();
		final Class<?> clTeXIconBuilder = clTeXFormula.getClasses()[0];
		final Object builder = clTeXIconBuilder.getConstructors()[0].newInstance(formula);
		clTeXIconBuilder.getMethod("setStyle", int.class).invoke(builder, 0);
		clTeXIconBuilder.getMethod("setSize", float.class).invoke(builder, (float) 20);
		icon = (Icon) clTeXIconBuilder.getMethod("build").invoke(builder);

		final int margin = 1;
		final Insets insets = new Insets(margin, margin, margin, margin);
		icon.getClass().getMethod("setInsets", insets.getClass()).invoke(icon, insets);
		icon.getClass().getMethod("setForeground", foregroundColor.getClass()).invoke(icon, foregroundColor);
	}

	public Icon getIcon() {
		return icon;
	}

}
