/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * Revision $Revision: 8066 $
 *
 */
package net.sourceforge.plantuml.graphic;

import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class SymbolContext {

	private final HtmlColor backColor;
	private final HtmlColor foreColor;
	private final UStroke stroke;
	private final boolean shadowing;
	private final double deltaShadow;

	private SymbolContext(HtmlColor backColor, HtmlColor foreColor, UStroke stroke, boolean shadowing,
			double deltaShadow) {
		this.backColor = backColor;
		this.foreColor = foreColor;
		this.stroke = stroke;
		this.shadowing = shadowing;
		this.deltaShadow = deltaShadow;
		// if (backColor instanceof HtmlColorTransparent) {
		// throw new UnsupportedOperationException();
		// }
	}

	@Override
	public String toString() {
		return super.toString() + " backColor=" + backColor + " foreColor=" + foreColor;
	}

	final public UGraphic apply(UGraphic ug) {
		return applyStroke(applyColors(ug));
	}

	public UGraphic applyColors(UGraphic ug) {
		return ug.apply(new UChangeColor(foreColor)).apply(new UChangeBackColor(backColor));
	}

	public UGraphic applyStroke(UGraphic ug) {
		return ug.apply(stroke);
	}

	public SymbolContext(HtmlColor backColor, HtmlColor foreColor) {
		this(backColor, foreColor, new UStroke(), false, 0);
	}

	public SymbolContext withShadow(boolean newShadow) {
		return new SymbolContext(backColor, foreColor, stroke, newShadow, deltaShadow);
	}

	public SymbolContext withDeltaShadow(double deltaShadow) {
		return new SymbolContext(backColor, foreColor, stroke, shadowing, deltaShadow);
	}

	public SymbolContext withStroke(UStroke newStroke) {
		return new SymbolContext(backColor, foreColor, newStroke, shadowing, deltaShadow);
	}

	public SymbolContext withBackColor(HtmlColor backColor) {
		return new SymbolContext(backColor, foreColor, stroke, shadowing, deltaShadow);
	}

	public HtmlColor getBackColor() {
		return backColor;
	}

	public HtmlColor getForeColor() {
		return foreColor;
	}

	public UStroke getStroke() {
		return stroke;
	}

	public boolean isShadowing() {
		return shadowing || deltaShadow > 0;
	}

	public double getDeltaShadow() {
		return deltaShadow;
	}

}