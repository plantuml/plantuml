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
 * Revision $Revision: 6398 $
 *
 */
package net.sourceforge.plantuml.ugraphic;

import java.awt.Color;

public class UParam {

	private Color color = null;
	private Color backcolor = null;
	private UStroke stroke = new UStroke(1);
	private UGradient gradient = null;
	private ColorChanger colorChanger = new ColorChangerIdentity();

	public void reset() {
		color = null;
		backcolor = null;
		gradient = null;
		stroke = new UStroke(1);
		colorChanger = new ColorChangerIdentity();
	}

	public final UGradient getGradient() {
		return gradient;
	}

	public final void setGradient(UGradient gradient) {
		this.gradient = gradient;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return colorChanger.getChangedColor(color);
	}

	public void setBackcolor(Color color) {
		this.backcolor = color;
	}

	public Color getBackcolor() {
		return colorChanger.getChangedColor(backcolor);
	}

	public void setStroke(UStroke stroke) {
		this.stroke = stroke;
	}

	public UStroke getStroke() {
		return stroke;
	}

	private void setColorChanger(ColorChanger colorChanger) {
		if (colorChanger == null) {
			throw new IllegalArgumentException();
		}
		this.colorChanger = colorChanger;
	}

}
