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

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractUGraphic<O> extends AbstractCommonUGraphic {

	private final O g2d;

	private final Map<Class<? extends UShape>, UDriver<O>> drivers = new HashMap<Class<? extends UShape>, UDriver<O>>();

	public AbstractUGraphic(ColorMapper colorMapper, O g2d) {
		super(colorMapper);
		this.g2d = g2d;
	}

	protected final O getGraphicObject() {
		return g2d;
	}

	final protected void registerDriver(Class<? extends UShape> cl, UDriver<O> driver) {
		this.drivers.put(cl, driver);
	}

	public final void draw(double x, double y, UShape shape) {
		final UDriver<O> driver = drivers.get(shape.getClass());
		if (driver == null) {
			throw new UnsupportedOperationException(shape.getClass().toString() + " " + this.getClass());
		}
		driver.draw(shape, x + getTranslateX(), y + getTranslateY(), getColorMapper(), getParam(), g2d);
	}

}
