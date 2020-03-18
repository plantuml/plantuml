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
 * Original Author:  Adrian Vogt
 *
 */
package net.sourceforge.plantuml.ugraphic.hand;

import java.util.Random;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.posimo.DotPath;
import net.sourceforge.plantuml.ugraphic.UChange;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.svg.UGraphicSvg;

public class UGraphicHandwritten implements UGraphic {

	private final UGraphic ug;
	private final Random rnd = new Random(424242L);

	public UGraphicHandwritten(UGraphic ug) {
		this.ug = ug;
		if (ug instanceof UGraphicSvg) {
			((UGraphicSvg) ug).enlargeClip();
		}
	}

	public StringBounder getStringBounder() {
		return ug.getStringBounder();
	}

	public UParam getParam() {
		return ug.getParam();
	}

	public void draw(UShape shape) {
		// http://www.ufonts.com/fonts/felt-tip-roman.html
		// http://webdesignledger.com/freebies/20-amazing-free-handwritten-fonts-for-your-designs
		if (shape instanceof ULine) {
			drawHand((ULine) shape);
		} else if (shape instanceof URectangle) {
			drawHand((URectangle) shape);
		} else if (shape instanceof UPolygon) {
			drawHand((UPolygon) shape);
		} else if (shape instanceof UEllipse) {
			drawHand((UEllipse) shape);
		} else if (shape instanceof DotPath) {
			drawHand((DotPath) shape);
		} else if (shape instanceof UPath) {
			drawHand((UPath) shape);
		} else {
			ug.draw(shape);
		}
	}

	private void drawHand(UPath shape) {
		final UPathHand uline = new UPathHand(shape, rnd);
		ug.draw(uline.getHanddrawn());
	}

	private void drawHand(DotPath shape) {
		final UDotPathHand uline = new UDotPathHand(shape, rnd);
		ug.draw(uline.getHanddrawn());
	}

	private void drawHand(UPolygon shape) {
		final UPolygonHand hand = new UPolygonHand(shape, rnd);
		ug.draw(hand.getHanddrawn());
	}

	private void drawHand(URectangle shape) {
		final URectangleHand hand = new URectangleHand(shape, rnd);
		ug.draw(hand.getHanddrawn());
	}

	private void drawHand(ULine shape) {
		final ULineHand uline = new ULineHand(shape, rnd);
		ug.draw(uline.getHanddrawn());
	}

	private void drawHand(UEllipse shape) {
		final UEllipseHand uline = new UEllipseHand(shape, rnd);
		ug.draw(uline.getHanddrawn());
	}

	public UGraphic apply(UChange change) {
		return new UGraphicHandwritten(ug.apply(change));
	}

	public ColorMapper getColorMapper() {
		return ug.getColorMapper();
	}

	public void startUrl(Url url) {
		ug.startUrl(url);
	}

	public void closeAction() {
		ug.closeAction();
	}

	public void flushUg() {
		ug.flushUg();
	}

	public boolean matchesProperty(String propertyName) {
		return ug.matchesProperty(propertyName);
	}

}
