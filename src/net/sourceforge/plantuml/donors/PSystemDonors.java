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

	public static final String DONORS = "6-i802mER3hSGmjfrjohVvkCORGYHh0yJoWE0ujukXiGVu1xfyVzHlgBw4htlcb6jFNQmfn110UWdZZ_"
			+ "PcXvrr_ZDrzV9MDSrT8L9a3W7UFIll6NjuMOAhRVCg6zl_suK8UqLWYkm4tb_F_tAc1EA22BiodcdmEq"
			+ "1guzkkpTJwszbbxi_OAsG6CQ_TYoKfRQXf6lQs0DFOsDunrHiYh6wY95MnjRW9PFt4ZKHky4dqYj_gCM"
			+ "gKbhTWtfBW74CbB4chTb2_jE3f_2m8b0BJiK8CA1X4DsaOExFf400fKqQiH6CHlhAJxoH0mHUgYm2__8"
			+ "IV7PGvP76JeGgEq60bg_GH20Pr52YSqs7mRaxc4j2Xeir9cvqIFTNmnEkT9fk9JiK8kW5b0586-2BkXr"
			+ "zPbBd6LiYXAUl8NnQei1SfRr0JYUhEZhBeM1ydltm4oOouLgwo7CATSnVdLqm4yD6TdxJUlZcCL4p6zp"
			+ "GOOlBC3v0kYbrb0n_huv7op4GsXCgGivSGuVxxC8X5MMWaLkOefhhfzq6Pirn7phmc9xYRxE36KPHk1A"
			+ "nHx0HxJSFgZp1NJLKFyyvAhh-1Isnct1tZ9yEcdE5V_lkYMPKRg0Dfhm8RfIecJf1QjyCIaYS0NGbm4F"
			+ "IN8ZLLycZ_9b-P1utvy4yUQ40v7Qh61_KCveN7AOGhomrf4gLW3JNQ9U23Hkf1j-dO3U1Yg7o0Xt_Puc"
			+ "x66JnjIvmHMtko7685-au-MB7fkvP5ngw9PfiskdzWEE0jo1Dychxitagm2MejuTYwNnD0RSDSnf1kay"
			+ "8kzxsf8UbpfAoxBVugekyMnk1tyUEhtWB1PDCQED7p8d51-JKLEt6CzJWyPlayWwxH56dGqC3m7XG8Pb"
			+ "V0fOLqoUgUG8WSfmXaCk9adejCuhTfc0knUgOUspFNKj8TPJLny3rSQFE1iPKCawuGJY6Eu6EsFZAB9V"
			+ "Dk-LKJVrRGY6Bw10318Sf4BPHWWgm-OKCgJtISzadEJplBJBGhwDRpPUOIXyWaHtpJROPGhnA97Tg1i0"
			+ "N1-sIhpuCJ42-1bllu6URQw3dd-ZfvN50Mmg2G5JOMk7VmdsLAwRDOdr5hrkQ-rjsVdpxtLa6gTatyOg"
			+ "0VGOPDD_4GHn4dMjy1RquuBF4cWiP0HsTR1jAxU67WSO5BIfPPghjcL6V3hBolNK3e_q3-N-xv194DZD"
			+ "9oIzE0ILeqy9LcjlTQKoqJxznk8Y1ul_4rLQChdAkieP2IHU0000";

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
