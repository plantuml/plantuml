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
 */
package net.sourceforge.plantuml.ugraphic.txt;

import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.asciiart.TextStringBounder;
import net.sourceforge.plantuml.asciiart.TranslatedCharArea;
import net.sourceforge.plantuml.asciiart.UmlCharArea;
import net.sourceforge.plantuml.asciiart.UmlCharAreaImpl;
import net.sourceforge.plantuml.graphic.FontStyle;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.AbstractCommonUGraphic;
import net.sourceforge.plantuml.ugraphic.ClipContainer;
import net.sourceforge.plantuml.ugraphic.UGraphic2;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UText;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;

public class UGraphicTxt extends AbstractCommonUGraphic implements ClipContainer, UGraphic2 {

	private final UmlCharArea charArea;

	@Override
	protected AbstractCommonUGraphic copyUGraphic() {
		return new UGraphicTxt(this);
	}

	private UGraphicTxt(UGraphicTxt other) {
		super(other);
		this.charArea = other.charArea;
	}

	public UGraphicTxt() {
		super(new ColorMapperIdentity());
		this.charArea = new UmlCharAreaImpl();
	}

	public StringBounder getStringBounder() {
		return new TextStringBounder();
	}

	public void draw(UShape shape) {
		// final UClip clip = getClip();
		if (shape instanceof UText) {
			final UText txt = (UText) shape;
			final int y = ((int) (getTranslateY() + txt.getDescent())) / 10;
			if (txt.getFontConfiguration().containsStyle(FontStyle.WAVE)) {
				charArea.drawHLine('^', y, getDx(), txt.getText().length());
				charArea.drawStringLR(txt.getText(), getDx(), y + 1);
			} else {
				charArea.drawStringLR(txt.getText(), getDx(), y);
			}
			return;
		} else if (shape instanceof UImage) {
			return;
		}
		return;
		// throw new UnsupportedOperationException("cl=" + shape.getClass());
	}

	public final UmlCharArea getCharArea() {
		return new TranslatedCharArea(charArea, getDx(), getDy());
	}

	private int getDy() {
		return (int) getTranslateY();
	}

	private int getDx() {
		return (int) getTranslateX();
	}

	public void startUrl(Url url) {
	}

	public void closeAction() {
	}

	public Dimension2D getDimension() {
		return new Dimension2DDouble(0, 0);
	}

	public void writeImageTOBEMOVED(OutputStream os, String metadata, int dpi) throws IOException {
		final PrintStream ps = new PrintStream(os, true, "UTF-8");
		getCharArea().print(ps);
	}

}
