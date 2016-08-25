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
 *
 * Original Author:  Arnaud Roques
 * 
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

	protected AbstractUGraphic(AbstractUGraphic<O> other) {
		super(other);
		this.g2d = other.g2d;
		// this.drivers.putAll(other.drivers);
	}

	protected final O getGraphicObject() {
		return g2d;
	}

	protected boolean manageHiddenAutomatically() {
		return true;
	}

	final protected void registerDriver(Class<? extends UShape> cl, UDriver<O> driver) {
		this.drivers.put(cl, driver);
	}

	public final void draw(UShape shape) {
		if (shape instanceof UEmpty) {
			return;
		}
		if (shape instanceof UComment) {
			drawComment((UComment) shape);
			return;
		}
		final UDriver<O> driver = drivers.get(shape.getClass());
		if (driver == null) {
			throw new UnsupportedOperationException(shape.getClass().toString() + " " + this.getClass());
		}
		if (getParam().isHidden() && manageHiddenAutomatically()) {
			return;
		}
		beforeDraw();
		if (shape instanceof Scalable) {
			final double scale = getParam().getScale();
			shape = ((Scalable) shape).getScaled(scale);
			driver.draw(shape, getTranslateX(), getTranslateY(), getColorMapper(), getParam(), g2d);
		} else {
			driver.draw(shape, getTranslateX(), getTranslateY(), getColorMapper(), getParam(), g2d);
		}
		afterDraw();
	}

	protected void drawComment(UComment shape) {
	}

	protected void beforeDraw() {
	}

	protected void afterDraw() {
	}

}
