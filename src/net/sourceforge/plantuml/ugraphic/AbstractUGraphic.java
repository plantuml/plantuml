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
import java.util.Objects;

import net.sourceforge.plantuml.graphic.SpecialText;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public abstract class AbstractUGraphic<O> extends AbstractCommonUGraphic {

	private final O graphic;

	// It would be nice to do something like this but not sure how:
	//     Map<Class<SHAPE>, UDriver<SHAPE, O>>
	// See https://stackoverflow.com/questions/416540/java-map-with-values-limited-by-keys-type-parameter
	private final Map<Class<? extends UShape>, UDriver<?, O>> drivers = new HashMap<>();

	public AbstractUGraphic(HColor defaultBackground, ColorMapper colorMapper, StringBounder stringBounder, O graphic) {
		super(Objects.requireNonNull(defaultBackground), colorMapper, stringBounder);
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

	final protected <SHAPE extends UShape> void registerDriver(Class<SHAPE> cl, UDriver<SHAPE, O> driver) {
		this.drivers.put(cl, driver);
	}

	private static final UDriver<?,?> NOOP_DRIVER = new UDriver<UShape, Object>() {
		@Override
		public void draw(UShape shape, double x, double y, ColorMapper mapper, UParam param, Object object) {
		}
	};
	
	@SuppressWarnings("unchecked")
	final protected <SHAPE extends UShape> void ignoreShape(Class<SHAPE> cl) {
		registerDriver(cl, (UDriver<SHAPE, O>) NOOP_DRIVER);
	}
	
	public final <SHAPE extends UShape> void draw(SHAPE shape) {
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

		@SuppressWarnings("unchecked")
		final UDriver<SHAPE, O> driver = (UDriver<SHAPE, O>) drivers.get(shape.getClass());

		if (driver == null) {
			throw new UnsupportedOperationException(shape.getClass().toString() + " " + this.getClass());
		}
		if (getParam().isHidden() && manageHiddenAutomatically()) {
			return;
		}
		beforeDraw();
		driver.draw(shape, getTranslateX(), getTranslateY(), getColorMapper(), getParam(), graphic);
		afterDraw();
	}

	protected void drawComment(UComment shape) {
	}

	protected void beforeDraw() {
	}

	protected void afterDraw() {
	}

}
