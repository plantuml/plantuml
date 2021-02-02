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
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.AffineTransformType;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.ImageParameter;
import net.sourceforge.plantuml.ugraphic.PixelImage;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;
import net.sourceforge.plantuml.version.PSystemVersion;

public class PSystemDonors extends AbstractPSystem {

	private static final int COLS = 6;
	private static final int FREE_LINES = 6;

	public static final String DONORS =
			"6-KC0AmFkBap0wp4H6CjU-RENbb-8cPf4i7oFA4uiOZJNK3y0UwT7lSRwI-YAxltJJEYrckJSWPHVH3q"+
			"W7UtXVQXzBy-27EYV0Z-tGWU80D4ITH6ZIpt9Djjcof3Il3lEr_v9UWTth51ehbAgiMHwq-_8GFlEsFp"+
			"x3GsNZsQe2y2FmRsrV-REbkNLuKmItShC0AZeX4owzXZ_1_Lei8fs7Seq6EhCPrAK1qELLHYiytVvB1A"+
			"6l12LJil5PfCYb5JfFXQ8GsVtfXGavF6HyX67vn_g1_2cyqLbZBE1E9eGnrTN_-tPTk8zkTAl0XoXrXG"+
			"M60Ef1m9YoX-Rdhmmos0eMeMWx45XJLFOY6H0YtapH_gIS4ZvSpfSF73um4G-3o32YIjg4OJpfPN0ygF"+
			"P8iMIrnrSTnOwiTXUIfz90sF9nOU5jG2QWZoF-Y2JeirFfewszNWIEffadkzfQXV1M_csape6roQ9Aot"+
			"jPTczhYYb2LIJTlu7EqrZm3SBRLon9VHH2W7QFQRLw6iVhH0VWIwgYPioPzbNrd8XAYsOHzwrxdyy1X4"+
			"s2T3fecr_swslPR-GjUCOLF22h1epvYlYqrmi8MkJ1u1dj3AvxBE1v2qj7g_wF6mTfzmJRx4lI7HUXzX"+
			"AVttgpxC3CK1sbDc5dJZ1KjdDAsQYos8O8_GqG4tXUHAE7VNvTXzNAIF_0J0cqiFkiD6e1RY-MOgJKSX"+
			"KaFghaWp0PWzhTJ53wyb38gr2ReQLOBfPRNQutGrfYyRC_T96rpN6enH7DEPwWlBtQiYBGppLQCvVRDs"+
			"WEOKF9MN_RpgNQLS1RYqy1xmmJt_o82VwPM1I97fxjis_2DBimgBMQovRbQbkweV73nq_O4xMZI96MNo"+
			"aAL1SIWHDVKhaP-M37zBa4tpUpdQQrYThZD2X1bfS7jfSavDZldXDftbEDWWHA9QlEAK99nViDjb9Td2"+
			"faXvnkfUICXldqyYKYKib1hiQky2FADv5EJXl6jMBQlN9amke99i9WXA2rQr53hIrm8P6AlNCz4QiVrf"+
			"WGLZQOzHCt4elcAYjSeD15DWfeJpXK4aaaAzb5xc9161__bpbthQK0SxlZE6JyP1ieSa0geWzHadgeRQ"+
			"wz5j6Eib_dwzb9kFuC-_zcYJKwtTyrE2Q1t41Q275t0fzmizU1huQsoO13Z42YvwaN3begMdWOG5w_7E"+
			"8khIEADn4vFbXkIYLUI7ol_zkHOkvaDio77n73fQBAdIoyrgK6csht_NuIY7Zh-BAdqYkVmtFPEHq4aO"+
			"nBauzjM6eQbA2bXG7RK9hDtrsscMkyQSTo3L3oT0ljoGKwaMeOKrDItk2u7bOYplRTfFEIt-au5-ajRf"+
			"nruzr1navxfs4mtVjX3Q4Js9uI9nGuJ-bI4d6dZune-yg1PAmzAw6NQkO8NyUbx62mcxxE03arfUfQno"+
			"Guw-XUmjrS2F0A9AQ203YoMwUh5Gol50YgHWpi4odY7beBKukIS0foZOt-zh42uzqNo7Pktj6OLaBjlS"+
			"J0OGwN1-MebD-cpLo85zDNFPwXMvBRL8X-txTdp7-Vf7rDM324WWgadm6btarOszPcdO9CERMHp7PMOU"+
			"J7uEkwATfGniDPCjo5ms0mLF0X0x42eaT-5NyH5ppZdox_r6NnXemlF0QNJZkH9iGBRSY9GqeEOeYppt"+
			"lNsJ4m00";

	/*
	 * Special thanks to our sponsors and donors:
	 * 
	 * - Noam Tamim
	 */

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat, long seed)
			throws IOException {
		final UDrawable result = getGraphicStrings();
		HColor backcolor = HColorUtils.WHITE;
		final ImageParameter imageParameter = new ImageParameter(new ColorMapperIdentity(), false, null, 1.0,
				getMetadata(), null, ClockwiseTopRightBottomLeft.none(), backcolor);
		final ImageBuilder imageBuilder = ImageBuilder.build(imageParameter);
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
