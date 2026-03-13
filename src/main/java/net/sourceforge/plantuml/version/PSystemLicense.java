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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.atmp.PixelImage;
import net.sourceforge.plantuml.UgSimpleDiagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.AffineTransformType;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.awt.PortableImage;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.GraphicStrings;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UImage;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;

public class PSystemLicense extends UgSimpleDiagram {

	public static PSystemLicense create(UmlSource source, PreprocessingArtifact preprocessing) throws IOException {
		return new PSystemLicense(source, preprocessing);
	}

	public PSystemLicense(UmlSource source, PreprocessingArtifact preprocessing) {
		super(source, preprocessing);
	}

	private TextBlock getGraphicStrings(List<String> strings) {
		return GraphicStrings.createBlackOnWhite(strings);
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(License)");
	}

	public void drawU(UGraphic ug) {
		getTextBlock().drawU(ug);
	}

	@Override
	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return getTextBlock().calculateDimension(stringBounder);
	}

	private TextBlock getTextBlock() {
		final LicenseInfo licenseInfo = LicenseInfo.retrieveQuick();
		final PortableImage logo = LicenseInfo.retrieveDistributorImage(licenseInfo);

		if (logo == null) {
			final List<String> strings = new ArrayList<>();
			strings.addAll(License.getCurrent().getText1(licenseInfo));
			strings.addAll(License.getCurrent().getText2(licenseInfo));
			return getGraphicStrings(strings);
		}

		final TextBlock result1 = getGraphicStrings(License.getCurrent().getText1(licenseInfo));
		final TextBlock result2 = getGraphicStrings(License.getCurrent().getText2(licenseInfo));
		final UImage im = new UImage(new PixelImage(logo, AffineTransformType.TYPE_BILINEAR));

		return new TextBlock() {
			@Override
			public void drawU(UGraphic ug) {
				result1.drawU(ug);
				ug = ug.apply(UTranslate.dy(4 + result1.calculateDimension(ug.getStringBounder()).getHeight()));
				ug.apply(UTranslate.dx(20)).draw(im);
				ug = ug.apply(UTranslate.dy(im.getHeight()));
				result2.drawU(ug);
			}

			@Override
			public XDimension2D calculateDimension(StringBounder stringBounder) {
				final XDimension2D dim1 = result1.calculateDimension(stringBounder);
				final XDimension2D dim2 = result2.calculateDimension(stringBounder);
				final double width = Math.max(Math.max(dim1.getWidth(), dim2.getWidth()), 20 + im.getWidth());
				final double height = dim1.getHeight() + 4 + im.getHeight() + dim2.getHeight();
				return new XDimension2D(width, height);
			}
		};
	}

}
