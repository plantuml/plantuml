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

	public static final String DONORS = "6omC02mFkBap0p4nqLYNlhbPJVbOScQJ29Rd2SLXO15FTmFY3t3lzBZVI7yHNUdUDys8jcnDSPung1O4"
			+ "FPwQQuPjxks1q5RAY-189RY0513Vnibv7ploFdrIPKW9Ogde_il4xUdMSQ4oJbGZb0F5hGtlEsFpx3I-"
			+ "UFLeWZO2FmRsrVzTqygwUrKLXdHPrPWQTRh28Oo87E7_KRUfMzJ6oTIcqGCwbNC5DzMfYDFX0Gjea1Ku"
			+ "P6qTvwLPawgOI1Cfdjf8mIC6CxJdPFLvyaCVT7wf5z2bjRg6UEw0uZ5JUdJTDjg_xs3x5H8l4ixW2pOB"
			+ "pA6Kmtg2ukUc6nqd3n9QTv6DhRxoboSc6FpGGj_yeKYkz1uIsML8qN6fWGAFjp50CYQrQyBPeJy3gkh1"
			+ "5lqQXEfolt5thSFoL38R3RU0XOi5jO0QWlmFUi9dz9MNJEwvAM89UPh4Sza6rA-Izz4s5NqphoQ1yBRz"
			+ "qCpWLvcozP36bTSn_-hea7SDMRdwpLYZa4SmbvUv5CmlBL0_0Zqq9oA9hob_B2GWN5VgHUywDvtKBC8f"
			+ "7L8grkcNsF9QyWzTWSN6HNCwMABFng_BJObW2rsPL4DlQ5MbMUSJ9QcX_dx8ewNpNN1DbTgzGxGUQNEU"
			+ "_TzrIpD3SO2EJp4BkXOG2AjfrLTi88B3GfU7t0J81fHFarl-j7o8FV83WZUN2Bh2Hae7-FDDo4CEBin0"
			+ "bsvEKG7CdbBgISVjW8Sw7O9kHPLyUfcxVeP9oxPbOsQPjC5rky4O-J6TK9ouN-O2Zp2iKelcv6xj8-mI"
			+ "FCxBQLvrhy6yYcvx2-zf5w7zGmRy37D6H2fDL-nxsYNkeOx0BDg2fxhOdcu71m_TjU0KYrOmeqqxP0cO"
			+ "N4X6JRr5wKE3uUyWP1DsRohg2smDrwd1pOmqUBuqkQKcHtISjhCS6ri0lERY5Rk2mYb2j2qjcQjh2PCZ"
			+ "Laz2XlnEzGaibNLXQSEExQx6Cr6FWZoSzyYgexvDAMuBg3wc2VBIGfWpfCZ3rW8PM2lfMMu3x7vgnuAw"
			+ "pKyk77CelcsYxSODCKo1wIdS6maYCJgHbEWb9ea2lklZL-f5SnriV7TFAyP1qa8I0rL4brD_C5OQgbje"
			+ "6kir-h-jjPTbvy-_rv5DJXr-xsiAekSWBW0HNg2Zd404JmDVR_cYvAs4bQnG3Z9SkLRfQI1XmIvwvrVK"
			+ "WsL6zOuNoytAHIjl7whgkxjo39pr3IikyYdosPSOQUsb7bH1stUziLZa8FWFKMLI9bUZoMgCXCuWYtCt"
			+ "TfkfcEV69h1WTamcqFQhpvffF6NCVKTIQNW4oFTxP4WoUcXXJKthuaHWatH-trlOXaH6_mr6z0jgdJmU"
			+ "rKFxGUvkj0WqVEj36XT4Wk4Y-K21t9npea4ttoZ8YsgX32pjc67d25ReR_n7H27Xc6ry8CWn5yjfB-IG"
			+ "BMGVdusY5-GrXGQoi5YlPn8BfHmFf4WAvioMyJpI3jg6VRq3w1G5u_zz6y8b4yqloAmopzv8vjBFlJY3"
			+ "I8VxiqbichAr8Hls8osNRLsHspyNwR1J_vtOSRxp4PMuY55oBX4yeGIViVncv3Wkqt4m-Ka0";

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
