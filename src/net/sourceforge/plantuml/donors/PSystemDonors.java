/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
package net.sourceforge.plantuml.donors;

import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.BackSlash;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.code.AsciiEncoder;
import net.sourceforge.plantuml.code.CompressionBrotli;
import net.sourceforge.plantuml.code.StringCompressorNone;
import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.code.TranscoderImpl;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.version.PSystemVersion;

public class PSystemDonors extends AbstractPSystem {

	public static final String DONORS = "6wW70AmEU9ELAuNYZT_MZn6AGOgeeHNOWjgQuZoZA1P0SxnDhXdMoRgDdR45mND5SGKL8Az2C-THCiPX"
			+ "qGYJjjcQVk6-VTu2CLLilsL2UtyTQ4BoLZ2km4tbpF_b0XiJv0R8GZti1NIZZNlZcZIc_NyMPXz_WHRm"
			+ "DBfiMLGwq5cTNHLD233np1odb9A7OjnVaSBNZIs0buu7kTfO7U4RgRFlr0AQj6RJa9are5X6YaJpiT7Q"
			+ "SO3jOnWuqM5T7JOGEvGuw1kC0-eRCKh65JJ8ZE9cRAZcdIS4J3YXmavyKPAQeuLaHXawq65jWGAyFnC4"
			+ "n3uffoHdAsy32hR85ZKDahhmkZDTx1-MKe7yqd0ATB0Sj0Ae0F8Vw8O_PvkvnBcENL4pv5qPvx9no6kz"
			+ "Lx6_UQ2liwuCb9VDYvdnMdvKjnRIEUMwng-k1lcX8IjxUnXhlBA4yFnlBeNsnG8wFe2EjOQAyVV3-Sr2"
			+ "6eJ7bBgGWtFopdOJ0R7AKbZeNLnIBV3pBccnkbWUpLayH_lNXLOoi8Ch5fkXZsi5irldZ9AgeVvvoQkk"
			+ "urFacg1PtfVeHx9fIFp_BSqCqXsqteGFrwM8KgMlhAh5HHU1qw1_Gsu1kGFLq-JHTLg-9Bxt1-JUUv50"
			+ "53OJx9-wPjIdtBo4UM9Bfwfu01Zl4kr6X_CWCuYg0rq7bMTas5s_tQHdsBGnTcxqYdhJRWnT7zDfoitq"
			+ "tLpWCmo3icWE7DRUuYZWFfnG3gsMRwleDjVmRbkanZiPxAzXpWYapuXo76bBfazrb9dbiUHDNUBTt2x-"
			+ "F7JnJ-yMjT1vT_j7wljDVYMrr2F6esimz9PTrczjikXOG6prZ0Kk0tPgjnkJ0vNSGgSsd1KznGbOzxRE"
			+ "mN4jWukcTREIdQcT73Dh1nskINx8qO1HqPr83hwsEoFFU1G5zYVddLZrKD-757yNK2o62PvIeMmZfEWA"
			+ "czF9f76hPzmTl8zRcozKj_7DXIS4XH-RQDDoWzUd0FSK-a5J1v0wgrNoqiR42E1tVFq6FS-j3ZpAQ6cL"
			+ "SNQv8PRIf_S8y1ioyahsjhkX10q0";

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat, long seed)
			throws IOException {
		final UDrawable result = getGraphicStrings();
		final ImageBuilder imageBuilder = new ImageBuilder(new ColorMapperIdentity(), 1.0, HtmlColorUtils.WHITE,
				getMetadata(), null, 0, 0, null, false);
		imageBuilder.setUDrawable(result);
		return imageBuilder.writeImageTOBEMOVED(fileFormat, seed, os);
	}

	private UDrawable getGraphicStrings() throws IOException {
		final List<TextBlock> cols = getCols(getDonors(), 4, 5);
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				final TextBlockBackcolored header = GraphicStrings.createBlackOnWhite(Arrays
						.asList("<b>Special thanks to our sponsors and donors !"));
				header.drawU(ug);
				final StringBounder stringBounder = ug.getStringBounder();
				ug = ug.apply(new UTranslate(0, header.calculateDimension(stringBounder).getHeight()));
				double x = 0;
				double lastX = 0;
				double y = 0;
				for (TextBlock tb : cols) {
					final Dimension2D dim = tb.calculateDimension(stringBounder);
					tb.drawU(ug.apply(new UTranslate(x, 0)));
					lastX = x;
					x += dim.getWidth() + 10;
					y = Math.max(y, dim.getHeight());
				}
				final UImage logo = new UImage(PSystemVersion.getPlantumlImage());
				ug.apply(new UTranslate(lastX, y - logo.getHeight())).draw(logo);
			}
		};
	}

	public static List<TextBlock> getCols(List<String> lines, final int nbCol, final int reserved) throws IOException {
		final List<TextBlock> result = new ArrayList<TextBlock>();
		final int maxLine = (lines.size() + (nbCol - 1) + reserved) / nbCol;
		for (int i = 0; i < lines.size(); i += maxLine) {
			final List<String> current = lines.subList(i, Math.min(lines.size(), i + maxLine));
			result.add(GraphicStrings.createBlackOnWhite(current));
		}
		return result;
	}

	private List<String> getDonors() throws IOException {
		final List<String> lines = new ArrayList<String>();
		final Transcoder t = new TranscoderImpl(new AsciiEncoder(), new StringCompressorNone(), new CompressionBrotli());
		final String s = t.decode(DONORS).replace('*', '.');
		final StringTokenizer st = new StringTokenizer(s, BackSlash.NEWLINE);
		while (st.hasMoreTokens()) {
			lines.add(st.nextToken());
		}
		return lines;
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Donors)");
	}

	public static PSystemDonors create() {
		return new PSystemDonors();
	}

}
