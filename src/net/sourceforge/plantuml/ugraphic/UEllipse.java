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
 * Revision $Revision: 7143 $
 *
 */
package net.sourceforge.plantuml.ugraphic;

public class UEllipse extends AbstractShadowable {

	private final double width;
	private final double height;
	private final double start;
	private final double extend;

	public UEllipse(double width, double height) {
		this(width, height, 0, 0);
	}

	public UEllipse(double width, double height, double start, double extend) {
		this.width = width;
		this.height = height;
		this.start = start;
		this.extend = extend;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public final double getStart() {
		return start;
	}

	public final double getExtend() {
		return extend;
	}
}
