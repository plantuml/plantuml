/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
 * Revision $Revision: 5183 $
 *
 */
package net.sourceforge.plantuml.ugraphic;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.StringBounder;

public class SlotFinderX implements UGraphic {

	public boolean isSpecialTxt() {
		return false;
	}

	public UGraphic apply(UChange change) {
		if (change instanceof UTranslate) {
			return new SlotFinderX(stringBounder, xslot, yslot, translate.compose((UTranslate) change));
		} else if (change instanceof UStroke) {
			return new SlotFinderX(this);
		} else if (change instanceof UChangeBackColor) {
			return new SlotFinderX(this);
		} else if (change instanceof UChangeColor) {
			return new SlotFinderX(this);
		}
		throw new UnsupportedOperationException();
	}

	private final SlotSet xslot;
	private final SlotSet yslot;
	private final StringBounder stringBounder;
	private final UTranslate translate;

	public SlotFinderX(StringBounder stringBounder) {
		this(stringBounder, new SlotSet(), new SlotSet(), new UTranslate());
	}

	private SlotFinderX(StringBounder stringBounder, SlotSet xslot, SlotSet yslot, UTranslate translate) {
		this.stringBounder = stringBounder;
		this.xslot = xslot;
		this.yslot = yslot;
		this.translate = translate;
	}

	private SlotFinderX(SlotFinderX other) {
		this(other.stringBounder, other.xslot, other.yslot, other.translate);
	}

	public StringBounder getStringBounder() {
		return stringBounder;
	}

	public UParam getParam() {
		return new UParamNull();
	}

	public void draw(UShape shape) {
		final double x = translate.getDx();
		final double y = translate.getDy();
		if (shape instanceof URectangle) {
			drawRectangle(x, y, (URectangle) shape);
		} else if (shape instanceof UPolygon) {
			drawPolygon(x, y, (UPolygon) shape);
		} else if (shape instanceof UEllipse) {
			drawEllipse(x, y, (UEllipse) shape);
		} else if (shape instanceof UText) {
			drawText(x, y, (UText) shape);
		} else if (shape instanceof UEmpty) {
			drawEmpty(x, y, (UEmpty) shape);
		}
	}

	private void drawEmpty(double x, double y, UEmpty shape) {
		xslot.addSlot(x, x + shape.getWidth());
		yslot.addSlot(y, y + shape.getHeight());
	}

	private void drawText(double x, double y, UText shape) {
		final TextLimitFinder finder = new TextLimitFinder(stringBounder, false);
		finder.apply(new UTranslate(x, y)).draw(shape);
		xslot.addSlot(finder.getMinX(), finder.getMaxX());
		yslot.addSlot(finder.getMinY(), finder.getMaxY());
	}

	private void drawEllipse(double x, double y, UEllipse shape) {
		xslot.addSlot(x, x + shape.getWidth());
		yslot.addSlot(y, y + shape.getHeight());
	}

	private void drawPolygon(double x, double y, UPolygon shape) {
		xslot.addSlot(x + shape.getMinX(), x + shape.getMaxX());
		yslot.addSlot(y + shape.getMinY(), y + shape.getMaxY());
	}

	private void drawRectangle(double x, double y, URectangle shape) {
		xslot.addSlot(x, x + shape.getWidth());
		yslot.addSlot(y, y + shape.getHeight());
	}

	public ColorMapper getColorMapper() {
		return new ColorMapperIdentity();
	}

	public void startUrl(Url url) {
	}

	public void closeAction() {
	}

	public SlotSet getXSlotSet() {
		return xslot;
	}

	public SlotSet getYSlotSet() {
		return yslot;
	}

	public void flushUg() {
	}

}
