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
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;
import net.sourceforge.plantuml.version.PSystemVersion;

public class PSystemDonors extends AbstractPSystem {

	private static final int COLS = 6;
	private static final int FREE_LINES = 6;

	public static final String DONORS = "6yOA0AmEU9ELAujmujswMQFg3ojH8OBbUK9n65Z4qzs0-0FSEptkDz9VH5UwzqqpeYr_pV7p3A4OY7ey"
			+ "VrqDs_nl3nw_gV8W-DyBGKadBY2ohSIweUlD3WZJeVilasvFjuwB0W9KP3adE_G0tjyCv_ccyT5Bpu0b"
			+ "LFqKtTV-xt8YSVTG2BCqMu7Jfn7gnbPqZkNou-hIK-rEggXkLUMOJkKSmrMXKLuzyBM51faKxfhWsw75"
			+ "h1Zz42cPkh056QNHGq-Rj7v9tkYe_KUzWJOTqpLbv4meZrwYqVbOwSYsWUuN9By2oU0BCGqie98ZUe5Y"
			+ "juqQaYGF5hhcO3IsuSYM9o44yj21V_X7fJYIF2KpXs55aX829HdV9W18gzIZ1MUhcm7LUxB5Y2QfkjWV"
			+ "Xll7ONhAgOE6Hy0YnP8oG3K5UHDw8IVxh5iwPryLC_pbjqdkpHAWuR9bGDpCw5jyD4cC_zbJiu3VDMgB"
			+ "frIski_-Fqw78cdAhE4FhOroZX3U3mjXB6ujG3e2FTSd9icVJzat5k8GgQtqHhlExLV2AC8b7p9KhCjF"
			+ "iUGrvnzTScCpubaE5kCJ_8NOb0oqr9LACFI1LdsYTRx0QPeQdvUy6je_0wwfTxjtdDfBsPmL_tV3oioe"
			+ "141dkkW5tGWOcD6q6Xui80BJGjU6mWJ82jHsark-Mxqahds2uCsa9Agm4T8B_lfpESk5Yv3nfZkQc05C"
			+ "xrBAHPAsO8HcROAUHfLIj2n2lysaPjgqCNMVrC7ZhC4O-34TLxaOVbe8dCEmNYsQdStQvvX3S8llPJvr"
			+ "7yguYkzi2-y5Dwdv9WDsImjQHAgEgcnRsIt3MGE3MRmb3ms9tSDEBXwwVy6PBDh4Z3R78REXSZaPfVGH"
			+ "9pLDchyLoDhsQcUb1gIN82IucT4D7czD7gd59PAa6SmWnYQ0hBxun2v5k94mqIqlEvlh4A6rMBq8BUHt"
			+ "AKrWgQABI3TsTlS0dZ4zYkp3UI_CiJJMBknZ0RLaDq59EZ1wJs5vsfmWGyOQz6oq1dQwrB50BlrR3PiE"
			+ "XRTDTDQumSGKqbqb-c3H8j6eY6lenIU50tpDorFg6MCTT7odbmt60RA4902gC9iZlmGjZPUrQ1dhBVf_"
			+ "R5aEA-VFlzUHHKwzuvbt5K3FGKRy1odh4xgOTGDlmxnbDim13fZ9H4zskFBGqhk1f0NxIPrLr9LfHdB5"
			+ "1yijQOjQVdoe-jzEz-5YDlX651UbiBpxbA3PScW4bMBx_YyM9pam_YcgpCdagjIfYH4JOdj7SOfZnrvA"
			+ "kAvPWGQz4pA1jwUkjzCqTsbplg2CORW6vAT7fCacUsX1BKsRouN0Td7Mz-Qs9fdiVqcBVehMpdbM3yCd"
			+ "sx-fNQ3Xsvz2kw4K62uIDnpmqMDERf3CDnJQZQixod8nRUUY-uPT8jZ0VpC_8Wi9honhbOaov-7MSl5b"
			+ "8P6r7CZ1Mye-qnkiB_2gCW8oVM6E2G40";

	/*
	 * Special thanks to our sponsors and donors:
	 * 
	 * - Noam Tamim
	 */

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat, long seed)
			throws IOException {
		final UDrawable result = getGraphicStrings();
		final ImageBuilder imageBuilder = new ImageBuilder(new ColorMapperIdentity(), 1.0, HColorUtils.WHITE,
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
