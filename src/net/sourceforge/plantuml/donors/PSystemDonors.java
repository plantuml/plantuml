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

	public static final String DONORS = "6ySB02mFUBXRGOc9nbfsvvsjZ8zbDKqYM9ud58U1HJpT3OW_mBtJutuZ_KLqfNlVD2FQiZN5USOGiI3e"
			+ "yJIvPjZctXvupCdFe-IxmsY4GPYio__BbYQxT2r0gWmfmRDSR_uPnVYUEsbeodQv4793qY-yxupDizFu"
			+ "ujMZ2Te8_1ZOL_zlwf1nUQWAmz1iXJ4O5OrGdJ_q56ijjVR_KOqBfs3VeOeUIuxfrCLrE5JHYCwA5uhL"
			+ "E0UtZFFHfCMecD0cKefgeuGYEPYXhIRLvyWL6TpyKZz0cwxjcX8v4efzbaXqV-rftt60ziibFtJoGopO"
			+ "8h20Ye_i9oXvRSIuoHT1GyjSzCPM7Ldo90E6KkZ2tloXB8wufsJcChIuAN062ehSne78I6hJ0hC3Ym5L"
			+ "Ug95doOfkZ5kmftFOLcArKR3iy2YW2Nd06ioyZ_e4pxTTtraSioRefQKfqdUozQZSTMzbsipec_wqIHH"
			+ "lhPtoTBZIb4rJ-bizUdpjRf3xfgowl4leqOyUt2tqq8Q5XyfY1-6RXiJbUJFj8M558HhA_qWNUR6AsSl"
			+ "mZ4RifIjus-nPJNsyYvViJ6nB7SMUFTytMXAXbhAAkGqTBjMPIxjVAAbD3N-B_BeqVc_m9fAita7wJrB"
			+ "vpB_lnjMQiOI3Zesn2pNpGB5ucbIzIusBaBJJAU6t0J83QZk9hVYRdKHVzG7vDvICX65pRO4YVPKSfUB"
			+ "4APZfPjAZ02MJ2dd-XjR4FcgrS7eLaI9HSeYz35CUgRjgx1D-k1ckxanYQCwfNmIVbi8tCEmKYsQsQ5j"
			+ "6tCnNiR7SLvrh_v-bRJjBRodV4ZDAnZ8AoqOOIaveZoEie-4kmOAihXBNXhYUuHr3XwwSy4PB5h6Z3RR"
			+ "8Gj3SZmPP-bN31-Q37lxNJPihurDNi3xghCJmiZ8WzTBacjgSOcRV3DOJWxR08gDnIjs-PcB25LRRYgR"
			+ "wnAcHgmUXGhu7VC9p9LjuSfZlTsru0nO5DN1l3lMM1gtAStR0ghBgOAOTA6CcT8KQNix6NfMfsUf3R3z"
			+ "gUuBpZOVLTWqABsjeks63QPFeUaKwHSD8X4IhGgvrAGmm7Tz_4eywEiGupkzR24EaHQI5A02oZaVE9K6"
			+ "ppsrbFKQ_N-jjTFA-VFlTMI3KtlSyvD2w1a8ujz3WHpX539v6lXwymVW7G6hOfPs2h1oZTppG5A3dNXd"
			+ "5dN_CADp9nRApQX5hzW-LELltww0nNA83IikOccAwLkSidPg19LVzvrlB0voMFYxg38Xawkdh3P6mcVG"
			+ "YnCT-vgbk7FD116CZai5xdxrMJFDkumvNx6iRZWuyiqH6L9q3osg9MbjFZsmKPpvlPLjIARxNp8Ydrzg"
			+ "rlVgeRTJ_Xnf0is_jTJx6LbWcCWuCB2-tEV40pVU2AWBKpl-wMB1bOkk67PvmHRs6_iHMPxmmhPMcGXp"
			+ "dswLeoyByQnsUewr9UzZez7qGQ9IU23R5-lqsOd5iWoFP84KhjbR9ej8w_I6Ixg3S8Z2-VxU1dEilFaH"
			+ "cTNcqnk5NDhSYtiG-N1ycgIcNJOxSfCRwuxSRIC1-ge0";

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
