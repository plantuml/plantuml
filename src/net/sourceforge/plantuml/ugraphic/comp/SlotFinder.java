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
package net.sourceforge.plantuml.ugraphic.comp;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.TextLimitFinder;
import net.sourceforge.plantuml.ugraphic.UChange;
import net.sourceforge.plantuml.ugraphic.UBackground;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UEmpty;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UParamNull;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UShapeIgnorableForCompression;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UText;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class SlotFinder implements UGraphic {

	public boolean matchesProperty(String propertyName) {
		return false;
	}

	public double dpiFactor() {
		return 1;
	}

	public UGraphic apply(UChange change) {
		if (change instanceof UTranslate) {
			return new SlotFinder(mode, stringBounder, slot, translate.compose((UTranslate) change));
		} else if (change instanceof UStroke) {
			return new SlotFinder(this);
		} else if (change instanceof UBackground) {
			return new SlotFinder(this);
		} else if (change instanceof HColor) {
			return new SlotFinder(this);
		}
		throw new UnsupportedOperationException();
	}

	private final SlotSet slot;

	private final StringBounder stringBounder;
	private final UTranslate translate;
	private final CompressionMode mode;

	public SlotFinder(CompressionMode mode, StringBounder stringBounder) {
		this(mode, stringBounder, new SlotSet(), new UTranslate());
	}

	private SlotFinder(CompressionMode mode, StringBounder stringBounder, SlotSet slot, UTranslate translate) {
		this.stringBounder = stringBounder;
		this.slot = slot;
		this.translate = translate;
		this.mode = mode;
	}

	private SlotFinder(SlotFinder other) {
		this(other.mode, other.stringBounder, other.slot, other.translate);
	}

	public StringBounder getStringBounder() {
		return stringBounder;
	}

	public UParam getParam() {
		return new UParamNull();
	}

	public void draw(UShape sh) {
		final double x = translate.getDx();
		final double y = translate.getDy();
		if (sh instanceof UShapeIgnorableForCompression) {
			final UShapeIgnorableForCompression shape = (UShapeIgnorableForCompression) sh;
			if (shape.isIgnoreForCompressionOn(mode)) {
				shape.drawWhenCompressed(this, mode);
				return;
			}

		}
		if (sh instanceof URectangle) {
			drawRectangle(x, y, (URectangle) sh);
		} else if (sh instanceof UPath) {
			drawPath(x, y, (UPath) sh);
		} else if (sh instanceof UPolygon) {
			drawPolygon(x, y, (UPolygon) sh);
		} else if (sh instanceof UEllipse) {
			drawEllipse(x, y, (UEllipse) sh);
		} else if (sh instanceof UText) {
			final UText text = (UText) sh;
			drawText(x, y, text);
		} else if (sh instanceof UEmpty) {
			drawEmpty(x, y, (UEmpty) sh);
		}
	}

	private void drawPath(double x, double y, UPath shape) {
		if (mode == CompressionMode.ON_X) {
			slot.addSlot(x + shape.getMinX(), x + shape.getMaxX());
		} else {
			slot.addSlot(y + shape.getMinY(), y + shape.getMaxY());
		}

	}

	private void drawEmpty(double x, double y, UEmpty shape) {
		if (mode == CompressionMode.ON_X) {
			slot.addSlot(x, x + shape.getWidth());
		} else {
			slot.addSlot(y, y + shape.getHeight());
		}
	}

	private void drawText(double x, double y, UText shape) {
		final TextLimitFinder finder = new TextLimitFinder(stringBounder, false);
		finder.apply(new UTranslate(x, y)).draw(shape);
		if (mode == CompressionMode.ON_X) {
			slot.addSlot(finder.getMinX(), finder.getMaxX());
		} else {
			slot.addSlot(finder.getMinY(), finder.getMaxY());
		}
	}

	private void drawEllipse(double x, double y, UEllipse shape) {
		if (mode == CompressionMode.ON_X) {
			slot.addSlot(x, x + shape.getWidth());
		} else {
			slot.addSlot(y, y + shape.getHeight());
		}
	}

	private void drawPolygon(double x, double y, UPolygon shape) {
		if (mode == shape.getCompressionMode()) {
			return;
		}
		if (mode == CompressionMode.ON_X) {
			slot.addSlot(x + shape.getMinX(), x + shape.getMaxX());
		} else {
			slot.addSlot(y + shape.getMinY(), y + shape.getMaxY());
		}
	}

	private void drawRectangle(double x, double y, URectangle shape) {
		if (mode == CompressionMode.ON_X) {
			slot.addSlot(x, x + shape.getWidth());
		} else {
			slot.addSlot(y, y + shape.getHeight());
		}
	}

	public ColorMapper getColorMapper() {
		return new ColorMapperIdentity();
	}

	public void startUrl(Url url) {
	}

	public void closeAction() {
	}

	public SlotSet getSlotSet() {
		return slot;
	}

	public void flushUg() {
	}

}
