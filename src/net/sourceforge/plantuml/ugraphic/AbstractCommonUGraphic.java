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
 * Revision $Revision: 6578 $
 *
 */
package net.sourceforge.plantuml.ugraphic;

public abstract class AbstractCommonUGraphic implements UGraphic {

	private final UParam param = new UParam();
	private double dx;
	private double dy;
	private final ColorMapper colorMapper;

	public AbstractCommonUGraphic(ColorMapper colorMapper) {
		this.colorMapper = colorMapper;
	}

	final public UParam getParam() {
		return param;
	}

	final public void translate(double dx, double dy) {
		this.dx += dx;
		this.dy += dy;
	}

	final public void setTranslate(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	final public double getTranslateX() {
		return dx;
	}

	final public double getTranslateY() {
		return dy;
	}

	final public ColorMapper getColorMapper() {
		return colorMapper;
	}

}
