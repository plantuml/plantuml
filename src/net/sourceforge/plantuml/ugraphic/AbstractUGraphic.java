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
package net.sourceforge.plantuml.ugraphic;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.graphic.SpecialText;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;

public abstract class AbstractUGraphic<O> extends AbstractCommonUGraphic {

	private final O graphic;

	private final Map<Class<? extends UShape>, UDriver<O>> drivers = new HashMap<Class<? extends UShape>, UDriver<O>>();

	public AbstractUGraphic(ColorMapper colorMapper, O graphic) {
		super(colorMapper);
		this.graphic = graphic;
	}

	protected AbstractUGraphic(AbstractUGraphic<O> other) {
		super(other);
		this.graphic = other.graphic;
		// this.drivers.putAll(other.drivers);
	}

	protected final O getGraphicObject() {
		return graphic;
	}

	protected boolean manageHiddenAutomatically() {
		return true;
	}

	final protected void registerDriver(Class<? extends UShape> cl, UDriver<O> driver) {
		this.drivers.put(cl, driver);
	}

	public final void draw(UShape shape) {
		if (shape instanceof SpecialText) {
			((SpecialText) shape).getTitle().drawU(this);
			return;
		}
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
			driver.draw(shape, getTranslateX(), getTranslateY(), getColorMapper(), getParam(), graphic);
		} else {
			driver.draw(shape, getTranslateX(), getTranslateY(), getColorMapper(), getParam(), graphic);
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
