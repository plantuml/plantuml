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
 */
package net.sourceforge.plantuml.ugraphic.txt;

import java.awt.Font;

import net.sourceforge.plantuml.asciiart.TextStringBounder;
import net.sourceforge.plantuml.asciiart.TranslatedCharArea;
import net.sourceforge.plantuml.asciiart.UmlCharArea;
import net.sourceforge.plantuml.asciiart.UmlCharAreaImpl;
import net.sourceforge.plantuml.graphic.FontStyle;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.AbstractCommonUGraphic;
import net.sourceforge.plantuml.ugraphic.UClip;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UText;

public class UGraphicTxt extends AbstractCommonUGraphic {

	private final UmlCharArea charArea = new UmlCharAreaImpl();
	private int lastPrint = 0;

	public StringBounder getStringBounder() {
		return new TextStringBounder();
	}

	public void draw(double x, double y, UShape shape) {
		if (shape instanceof UText) {
			final UText txt = (UText) shape;
			charArea.drawStringLR(txt.getText(), 0, lastPrint);
			lastPrint++;
			if (txt.getFontConfiguration().containsStyle(FontStyle.WAVE)) {
				charArea.drawHLine('^', lastPrint, 0, txt.getText().length());
				lastPrint++;
			}
			return;
		}
		throw new UnsupportedOperationException();
	}

	public void setClip(UClip clip) {
		// throw new UnsupportedOperationException();
	}

	public void centerChar(double x, double y, char c, Font font) {
		throw new UnsupportedOperationException();
	}

	public final UmlCharArea getCharArea() {
		return new TranslatedCharArea(charArea, (int) getTranslateX(), (int) getTranslateY());
	}

	public void setAntiAliasing(boolean trueForOn) {
	}

	public void setUrl(String url, String tooltip) {
	}

}
