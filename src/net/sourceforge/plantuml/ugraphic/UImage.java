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

import java.awt.Color;
import java.awt.image.BufferedImage;

public class UImage implements UShape {

	private final MutableImage image;
	private final String formula;
	private final String rawFileName;

	public UImage(MutableImage image) {
		this(image, null, null);
	}

	private UImage(MutableImage image, String rawFileName, String formula) {
		this.image = image;
		this.formula = formula;
		this.rawFileName = rawFileName;
	}

	public final UImage withRawFileName(String rawFileName) {
		return new UImage(image, rawFileName, formula);
	}

	public final UImage withFormula(String formula) {
		return new UImage(image, rawFileName, formula);
	}

	public final String getRawFileName() {
		return rawFileName;
	}

	public final String getFormula() {
		return formula;
	}

	public UImage scale(double scale) {
		return new UImage(image.withScale(scale), rawFileName, formula);
	}

	public final BufferedImage getImage(double withScale) {
		return image.withScale(withScale).getImage();
		// return bufferedImage.getImage();
	}

	public int getWidth() {
		return image.getImage().getWidth() - 1;
	}

	public int getHeight() {
		return image.getImage().getHeight() - 1;
	}

	public UImage muteColor(Color newColor) {
		return new UImage(image.muteColor(newColor), rawFileName, formula);
	}

	public UImage muteTransparentColor(Color newColor) {
		return new UImage(image.muteTransparentColor(newColor), rawFileName, formula);
	}

}
