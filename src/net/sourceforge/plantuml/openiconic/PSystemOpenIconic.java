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
package net.sourceforge.plantuml.openiconic;

import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class PSystemOpenIconic extends AbstractPSystem {

	private final String iconName;
	private final double factor;

	public PSystemOpenIconic(String iconName, double factor) {
		this.iconName = iconName;
		this.factor = factor;
	}

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat, long seed)
			throws IOException {
		final OpenIcon icon = OpenIcon.retrieve(iconName);
		// final Dimension2D dim = new Dimension2DDouble(100, 100);

		final int margin1;
		final int margin2;
		if (SkinParam.USE_STYLES()) {
			margin1 = SkinParam.zeroMargin(5);
			margin2 = SkinParam.zeroMargin(5);
		} else {
			margin1 = 5;
			margin2 = 5;
		}
		final ImageBuilder imageBuilder = ImageBuilder.buildB(new ColorMapperIdentity(), false, ClockwiseTopRightBottomLeft.margin1margin2((double) margin1, (double) margin2),
		null, null, null, 1.0, null);
		imageBuilder.setUDrawable(icon.asTextBlock(HColorUtils.BLACK, factor));
		return imageBuilder.writeImageTOBEMOVED(fileFormat, seed, os);

		// UGraphic2 ug = fileFormat.createUGraphic(dim);
		// ug = (UGraphic2) ug.apply(new UTranslate(10, 10));
		// // ug = ug.apply(UChangeColor.nnn(HtmlColorUtils.BLACK));
		// // ug.draw(new URectangle(7, 6));
		// icon.asTextBlock(HtmlColorUtils.BLACK, factor).drawU(ug);
		// ug.writeImageTOBEMOVED(os, null, 96);
		// return new ImageDataSimple(dim);
	}

	// private GraphicStrings getGraphicStrings() throws IOException {
	// final UFont font = new UFont("SansSerif", Font.PLAIN, 12);
	// final GraphicStrings result = new GraphicStrings(strings, font, HtmlColorUtils.BLACK, HtmlColorUtils.WHITE,
	// UAntiAliasing.ANTI_ALIASING_ON);
	// return result;
	// }

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Open iconic)");
	}

}
