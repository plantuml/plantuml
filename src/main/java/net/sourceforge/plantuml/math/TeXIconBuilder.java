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
import java.awt.Insets;

import javax.swing.Icon;

import net.sourceforge.plantuml.klimt.awt.XColor;
// ::comment when __MIT__ __EPL__ __BSD__ __ASL__ __LGPL__
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;
// ::done

public class TeXIconBuilder {

	private Icon icon;

	public TeXIconBuilder(String tex, XColor foregroundColor2) throws ClassNotFoundException {
		// ::comment when __MIT__ __EPL__ __BSD__ __ASL__ __LGPL__
		final TeXFormula formula = new TeXFormula(tex);
		final TeXIcon teXIcon = formula.new TeXIconBuilder().setStyle(TeXConstants.STYLE_DISPLAY).setSize(20).build();

		final int margin = 1;
		teXIcon.setInsets(new Insets(margin, margin, margin, margin));
		final Color awtColor = foregroundColor2.toAwtColor();
		teXIcon.setForeground(awtColor);
		icon = teXIcon;
		// ::done
		// ::uncomment when __MIT__ __EPL__ __BSD__ __ASL__ __LGPL__
//		// JLaTeXMath is GPL: this license variant does not contain it
//		throw new ClassNotFoundException("org.scilab.forge.jlatexmath.TeXFormula");
		// ::done
	}

	public Icon getIcon() {
		return icon;
	}

}
