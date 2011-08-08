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
 * Revision $Revision: 6576 $
 *
 */
package net.sourceforge.plantuml.ugraphic;

import net.sourceforge.plantuml.graphic.HtmlColor;

public class UParam {

	private HtmlColor color = null;
	private HtmlColor backcolor = null;
	private UStroke stroke = new UStroke(1);
	private UGradient gradient = null;

	public void reset() {
		color = null;
		backcolor = null;
		gradient = null;
		stroke = new UStroke(1);
	}

	public final UGradient getGradient() {
		return gradient;
	}

	public final void setGradient(UGradient gradient) {
		this.gradient = gradient;
	}

	public void setColor(HtmlColor color) {
		this.color = color;
	}

	public HtmlColor getColor() {
		return color;
	}

	public void setBackcolor(HtmlColor color) {
		this.backcolor = color;
	}

	public HtmlColor getBackcolor() {
		return backcolor;
	}

	public void setStroke(UStroke stroke) {
		this.stroke = stroke;
	}

	public UStroke getStroke() {
		return stroke;
	}

}
