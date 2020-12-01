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
import net.sourceforge.plantuml.code.NoPlantumlCompressionException;
import net.sourceforge.plantuml.code.StringCompressorNone;
import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.code.TranscoderImpl;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.AffineTransformType;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.PixelImage;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;
import net.sourceforge.plantuml.version.PSystemVersion;

public class PSystemDonors extends AbstractPSystem {

	private static final int COLS = 6;
	private static final int FREE_LINES = 6;

	public static final String DONORS = "6wCC0AmFkBap0wp4H6CjU-RENbb-8cPf4i7oFA4uiOZJNK3y0UwT7lSRwI-YAxltJJEYrckJSWPHVH3q"
			+ "W7UtXVQXzBy-27EYV0Z-tGWU80D4ITH6ZIpt9Djjcof3Il3lEr_v9UWTth51ehbAgiMHwq-_8GFlEsFp"
			+ "x3I-UFLeWZO2FmRsrQ4rLzSG2yNoF54A9IqJsjIaONRtQP2UJ-3_u5cxQ4k_PeqqL6171HxxoYzdEnzs"
			+ "JLR525Eai5iX1RRUcC6TazxBPACF2h_K3w7DvWfBMSu4ucX3FRg-NXlUDcBu6ecF0HWX5cGyC0TIZfb5"
			+ "575kkb5WYC31EOlPM7t9aIUJ4Cn32tdpXtfIuf7opEbmKM1w10ZOdaO1ajR8hGbdymy3qaMoHTWILvqp"
			+ "Rio-SLYUaesauTF4Wic2Me0QWlmFkg3JOesF9cUx6cn9FKroxzQ2-lE0XxWsA_eAIvE4-LfxQJOKLv-o"
			+ "HAgnZS_HNlC8mDrIAqTy6NrqSWJrVlCgnEf72yYFG0_LX2Nvi-m72v4GrJRiG-ywsquUimW7PCWqiGxU"
			+ "nPRNP2zqSJjBIx82BABtYQzpck21BNJ5vM7W2IrTBkjy0fEMLkyNFHhMxXEkqOdTg-IUw5gmbFxxLHzc"
			+ "1iK1cYun2xhJm22CfbMes404TWlT7t1J85T2kRiknUxZ8dta9m3UXFIWAsm6ue5uVf5oqo54DAHhUueL"
			+ "mFXJIbqKM1POC6aJT3MgjDFBQhN7wMgPlcoCkq_Qu7cjmHYAOpfIVM6-UrMuXQ5EDPfbcxLtCQVW4ZuE"
			+ "yghtpCjL09SMtdaVNFEF38WplJ8YKc-w-hOD_yZShC8mIslEpQfqL_M3cuVs7RYBHGtCA9E7J0gDEuce"
			+ "gLy9Vbeo_Cs3hDHVRIZlO7Mw3WrXPiI2sqkJ2sfoH85XEikXia70h5gyuXHFkAPW3QkLp1OsHUGPg_iX"
			+ "Gtu7V1AMQcQXSMFNS1VW6P57WJoSzyYgPVNQXCu5PCvcWhQqOAgbK96-5iY0iIOzzTQ8xKyBBA1BUuoE"
			+ "nGw5BvjehNA365F0T1F2Bnn4egWXKg8NEKa4y3z_xb9FDDU17zyPmoVZGBA790HK8UuEFL5wYjMZsp3M"
			+ "Q_JzUYat7y6VV-tH9YUFtVFJ1A9ra2Z-9o00HpaIFNWQ-6loYSYx6TPa1UCYmvMBbHuB92pempkxgFEM"
			+ "6T4xCLckA1Uj8Z-KxhUu5YucXjYGug8TLBHOKjBBpMfGyMplUzVX8WU2VuagMP5Srieo6GasWLcSksDV"
			+ "RHZgjk86U50TbGbQk-ktqontZJbV8Qbfk138zrlaLD8Yj61DJKrz8T0cnlxUfFsKfyL_Cg8VJslPSLKF"
			+ "xeCSNN8NqV1DYO5sJ4zYk8Yp8O1yUSu92znyqqHUL0jAmzAw6NRqO8NyqY_XXOGzjl41IPPNQLQv8GVP"
			+ "GlPqDL0VK5GJB921HREkIImevNX0YgJ0dBDbH1Of1st0Spq3u1G5zlVt6eIJ9PfVqAozFQEGp4MPc_4C"
			+ "8ZQSdq729bCs5t9WNvYvRFMAt70jqc47lXtVSRx-4NNr809n80gJu0LSfCEDMMPfs1JFRogvZqj5tao-"
			+ "calHLYe3ssKckm4s0qeyEGvYbaQ5ujU1d_q3MyxlbN_lD-3vQG00";

	/*
	 * Special thanks to our sponsors and donors:
	 * 
	 * - Noam Tamim
	 */

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat, long seed)
			throws IOException {
		final UDrawable result = getGraphicStrings();
		final ImageBuilder imageBuilder = ImageBuilder.buildA(new ColorMapperIdentity(), false, null, getMetadata(),
				null, 1.0, HColorUtils.WHITE);
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
				ug = ug.apply(UTranslate.dy(header.calculateDimension(stringBounder).getHeight()));
				double x = 0;
				double lastX = 0;
				double y = 0;
				for (TextBlock tb : cols) {
					final Dimension2D dim = tb.calculateDimension(stringBounder);
					tb.drawU(ug.apply(UTranslate.dx(x)));
					lastX = x;
					x += dim.getWidth() + 10;
					y = Math.max(y, dim.getHeight());
				}
				final UImage logo = new UImage(
						new PixelImage(PSystemVersion.getPlantumlImage(), AffineTransformType.TYPE_BILINEAR));
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
		final Transcoder t = TranscoderImpl.utf8(new AsciiEncoder(), new StringCompressorNone(),
				new CompressionBrotli());
		try {
			final String s = t.decode(DONORS).replace('*', '.');
			final StringTokenizer st = new StringTokenizer(s, BackSlash.NEWLINE);
			while (st.hasMoreTokens()) {
				lines.add(st.nextToken());
			}
		} catch (NoPlantumlCompressionException e) {
			e.printStackTrace();
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
