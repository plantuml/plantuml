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
	
	public static final String DONORS = "6nC90AmEEBmtNeGqQuvtvvyfOnYjY14ipnEA4uiuxGR47-3Ujh6l87u5mINjNXY1P6bpXlCCebWGDPxr"
			+ "KuTjRlPNQ4jzbOnnKnDcmWH88JoNxPv-tYvLP9GWfehjjqnexsy_BjGXhHM2At1JEVztQO1veO0qJgTu"
			+ "29S3r7yAbjuekTPi_jATxOLcRJPqN81ADTfOiwDOi-L3HEbheJ6HsMkCWcoRQyUUKV8enbSYnLeRMs2L"
			+ "anOKQcCunITQ3_zH2sZIcbr3UhQ0OXaf6itRlZRs7PoVmQ0HmgnqnA3iKEBGNM2iKD-e8KnAi20tYcbi"
			+ "9END4pL2EA32B_oZ92bd3raPPTY0CDi31Qn-JH28PrD12MTxNWCAzs2BXWPBTMvPwIxt3yDJeImQhWKT"
			+ "PWvoWLG0UHDq8P-hprpIk8rDCSEBxiPuzKr2tcJd-UFDWxxfj34mVkuUcYdBSg3QUaZXbEiOlpewvAU6"
			+ "ZEpzfexOPWdX-MqkJFlI0fqVG2-rXednD-UNXMI8Z2breQVEwPDzYa1vPIaiUXLJNWlzeilLRI2UHmyM"
			+ "s5HVvrfPmGvTcTWDVK8hdCCwN-1K3VNFGwwwZa_1DM6tUzkKCsbpgVX_wvPc9ka1EcYYZT3L4YDFtxIg"
			+ "5vCXWfT17uRS0d83gY-JH-Lb-P3usXzGU4iYWIfjpcY_KPanNt9OH7ZXlJDLF01cDubyGLWyI0_YEe7U"
			+ "XYgRwB2xVa-Jac7JniovKIgMkmvZL3wcavMBxjLSu2iDmx9i3ktMVi1Hm0FOQLvr5dUj0fosz9vOB8qd"
			+ "6KZNCAS6qdvKsVUq9TsNEaPcMM_nhYxnRcu7VnwwV63V5YqmhNGTiY0qRngHKxSuprE3wczFo0PjJRFE"
			+ "3ivL8KAWc1cN0VUx9YigEO8m6Svmw7a2mgPrNh7A3UvUcCwrpixKjKHOprnz3DIG7tKsO5IwJgG07-Uz"
			+ "W6V0eyJ-SDuhenxgsn1sBQ1431DifCBOHWYhXCqPP4Jlbvvvd-1zNjfZeTx6DpkVSIX-RA8xnWs0LcSq"
			+ "4xgl6v29qCXNgPSy9ax0pxZzXdemws3ZR_IqOXp4bf8Oe0he9_mI7DJEMpIDzHPzRcljRTdvywyvCicf"
			+ "sDUnMX0z1q7-goYearYEAkz6Vb_ulC9Y8ICWri4ELruQlH1WM13Iibogjdj6uKmfbfkfdJ9AVQZmVv9B"
			+ "4CJbZIWlDc6LqQS4gxMtUbAvRLz-n-4j1u__4rMUYUJIbSL8bWjXYlCqtiT89QnR8Go-3fy0";

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
