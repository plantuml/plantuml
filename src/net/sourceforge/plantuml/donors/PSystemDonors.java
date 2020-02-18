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

	private static final int COLS = 6;
	private static final int FREE_LINES = 6;

	public static final String DONORS = "6qOA02mFU3XMJbc44wzsfuUeUc2VR6qyRpkVqESmi8ZJNK3y1jWxFUutqbz4LxhtJJEYBLjJd6SKnOAW"
			+ "nzlacc6RUtiWLXFpLN9dtkWNveKX2Mr8WEjmHyIdof0IVEycQbqbNzfRkOJbCk0By7OGIqNtNoPkJxUE"
			+ "2t6mGuLG3YdksVytfxRkNbM58LrMhTFH5Ih0gi0ZOdS_QdDfrCPtKvlUX09qAd45DrMLy7hZfYBDuAxZ"
			+ "N3gqw1GZpI8bNZUE43SwC_JKfF1ByaH6vZ_g1RJfNAufaJEXF4Q9HDTjVqVs7PG_BNb1H48ks2ImW4gE"
			+ "m0cANXinFY8oQEZgIMDihcRC4oyCAUZ4lloZKTIufsHw6bfyH5s1ahYlSG3Iatgqu6nXDA2i9rkCQ9Ag"
			+ "C_CbtByEorDwpJJS3nPHbhG9hYd8cz0JFk6hBfcSuwMeuIy_JVB8rmGQBbi6t9iEVOjhaqIvdMsQkVDf"
			+ "KssUqjdgQkD_DeVSDMNLyfjHH2D7SEzl2obON5e0FmAzD2SgoPzb7rd8Xd1jfHVITQvUkHeHBjcGgM9T"
			+ "_eWjh-d_wE9ocn7Bus2HpvwlCqt9G4jTYLo6lj3AvhRE5qfAK-FpoAEbupFXcgpPlUFqFihcDF-_RYip"
			+ "un80dPbY5dJze1YifjNmP0CHc1QwFUGcG5v0TPZSoiFg4R_E1mHlAYCHXGrAB-Hzi-MGYnGciBmxIdG0"
			+ "9ayfxU4Zve48HUs2daQLUgGiGh_TfATjsPYuJ_fWVzPW3FAOpjOu2Nyi18umpAf5ixvR-m8z1LUvNEPL"
			+ "Ty_Fgm3M5juBBbBz90DwfGKZ8hL6DFQzx99mQA0mYxFahI6-PrXZvQ7x1vpMeatCQ3CVCWsI6ybgfRSO"
			+ "tZKP_kOXgzcnMkGvi4V4PaWpenMyFveyKejBV4Hqi9mWMm0Ivk8LU_72dIjLkPoSRAvJcEgnMWiLyhla"
			+ "4rYgmCBJWxNo7i0piaUnSpZlbJKVZTUGuda1bNAOe2wTA7Ea56NQMY33rghrRBGEjf_gM31lurivAmw5"
			+ "jqrqrhX1mHGsdHAuqOYHA2SQ5NhPay80V_twbNfoKmSPl_QooJYriY6MGgMSByiNfw3nTWoj-rlqVzks"
			+ "lQsSF_zUDvkSUkFjAmNGSv0S_nxAhAEBajNzDFWRR9WCSC1AAjeuuSf3Isy5aXRinpk3gFFjZEQTd2ot"
			+ "f1QzSlvGsT_zoIm7LtF3YejwYdBu4gTiUsi45M_x_ZS6MpaC_4LKcPFXwgzUBICAnFOsuZoDVOzIt5cL"
			+ "11BCZii4pdxrqscQXupPHoYp7Yu2-Vc8T3c4XnRKqhGuJWHMnHdUUwTroOJ_9JU9kEgdh2tLGq_ZL6vg"
			+ "5sZuTYMA6qQ1uI9cny3uTfEJ5RZvfeTwgBOVwVGN4oSkAeRTtdDs0W00";

	/*
	 * Special thanks to our sponsors and donors:
	 * 
	 * - Noam Tamim
	 */

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
		final List<TextBlock> cols = getCols(getDonors(), COLS, FREE_LINES);
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				final TextBlockBackcolored header = GraphicStrings
						.createBlackOnWhite(Arrays.asList("<b>Special thanks to our sponsors and donors !"));
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
		final Transcoder t = new TranscoderImpl(new AsciiEncoder(), new StringCompressorNone(),
				new CompressionBrotli());
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
