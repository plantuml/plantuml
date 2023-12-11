/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
package net.sourceforge.plantuml.version;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.atmp.PixelImage;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.PlainDiagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.AffineTransformType;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.GraphicStrings;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.klimt.shape.UImage;

public class PSystemLicense extends PlainDiagram implements UDrawable {

	@Override
	protected UDrawable getRootDrawable(FileFormatOption fileFormatOption) {
		return this;
	}

	public static PSystemLicense create(UmlSource source) throws IOException {
		return new PSystemLicense(source);
	}

	public PSystemLicense(UmlSource source) {
		super(source);
	}

	private TextBlock getGraphicStrings(List<String> strings) {
		return GraphicStrings.createBlackOnWhite(strings);
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(License)");
	}

	@Override
	public void exportDiagramGraphic(UGraphic ug, FileFormatOption fileFormatOption) {
		final LicenseInfo licenseInfo = LicenseInfo.retrieveQuick();
		getTextBlock(licenseInfo).drawU(ug);
	}

	public void drawU(UGraphic ug) {

		final LicenseInfo licenseInfo = LicenseInfo.retrieveQuick();
		// ::comment when __CORE__
		final BufferedImage logo = LicenseInfo.retrieveDistributorImage(licenseInfo);

		if (logo == null) {
			// ::done
			getTextBlock(licenseInfo).drawU(ug);
			// ::comment when __CORE__
		} else {
			final List<String> strings1 = new ArrayList<>();
			final List<String> strings2 = new ArrayList<>();

			strings1.addAll(License.getCurrent().getText1(licenseInfo));
			strings2.addAll(License.getCurrent().getText2(licenseInfo));

			final TextBlock result1 = getGraphicStrings(strings1);
			result1.drawU(ug);
			ug = ug.apply(UTranslate.dy(4 + result1.calculateDimension(ug.getStringBounder()).getHeight()));
			UImage im = new UImage(new PixelImage(logo, AffineTransformType.TYPE_BILINEAR));
			ug.apply(UTranslate.dx(20)).draw(im);

			ug = ug.apply(UTranslate.dy(im.getHeight()));
			final TextBlock result2 = getGraphicStrings(strings2);
			result2.drawU(ug);
		}
		// ::done
	}

	protected TextBlock getTextBlock(final LicenseInfo licenseInfo) {
		final List<String> strings = new ArrayList<>();
		strings.addAll(License.getCurrent().getText1(licenseInfo));
		strings.addAll(License.getCurrent().getText2(licenseInfo));
		return getGraphicStrings(strings);
	}

}
