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

	public static final String DONORS = "6xKB02mFkBap0p4nqLYNlhbPJVbOScQJ29Rd2SLXO15FTmFY3t3lzBZVI7yHNUdUDys8jcnDSPunH2m8"
			+ "UZnDhXcskRS7GbifBu8ZbU02Ka1y6oVdVUp8-_H9bI4bYAMIzTjKJ9zqAHiYuxFU2Ds0pdaRtdV6vjbf"
			+ "VF7gqGHj17uCxAl_DwULTVUgAin0bzMCcL7H4weOKYCYHVn_LEEoKnlVJNKQI08wTN45DzMfW5SdF0OM"
			+ "QGQtZFDgqcHKJ68JAQ7QA24a1ZEqvQJsEVAB31A_r1leqbfTGo9tGF6OAH9TZ-NsxJjeVhd4Y-0Flc2p"
			+ "m1nAEOelKFpSY97myw2XPHUziLL7bZmfWA4CMjYRFnJ9aTv3ChiCBH81DL0GyZOEI4QXDaquExOOK6KF"
			+ "jkZJ85MN-asuSnsMf_JSQ7WHM0HOS1PGGv3_GI_uZBxobiazDqKj8K-JU5LRZ_gLU2zjCw9l-j4aK3xR"
			+ "JyrSVkKfMm_fN7cVyw_E3ddNa9MhFumwCda4TpdCfM5-Qu7w4UYsEL59V4zPM4X0kAxK6-awDrwflGZ7"
			+ "R4YfMCTVOijh-X-w8fMDYULqiCXFzzUbfcImXQvCgg4tj2hJhFE9bjHGVt_9ewNpNN1DfTczW_GUQNEU"
			+ "_zzrIpD3SO2EJJ4BkaMW6AocLM-nWGXE2vqVSXCW6r2-J6xvo_9B_54V4BovH20A6v9syET7o4CkN9Y1"
			+ "LTsKOWGmUqevBn911oBKwX1qAwga8sNk-XacFTkMZPbbqWVNxGPZvCTqGd4HVfaBFCAmKYsQzTtQ9dO9"
			+ "hl8wpglkDTglujZUmdlAJMh_aW4_mvmHaN8ZYdrFkoKyr56OnLxoL1VVq-tauA4x5vp1ecNCQBCTCWqA"
			+ "Pv9nfZvYzA71y7SKifhT6vjw1U4xOIkucP46pvScrzJa4Wc_xIp7mnQ04dFnYbrHuP92rImxbQjh2PCZ"
			+ "Lez2XloEzGaibMdXnjFFvgw5PsIFWZgSTyUgexvDQTuMK5rC4wIbXJ9dI9E7hGKoi5QdPwaDiFkb7GlM"
			+ "DZzPiU9GVDj4suqR54o1wIBS1mqYKIvebEWb9ea1lklZL-h9VHriV7TFAyP1qa8I0rN8rLa_E3KDdbje"
			+ "6kir-d-ijSEoy-VVwyWcf_Quzrq4qNEG7Fzk2j1HJY22fu6ljtmoUIV1gh58JqhcoZLAJmC92tRIExEe"
			+ "HuSPhJjUB3SZ5xry-r2LFvrSWOlvu1jMNAJJvBClSTBQeHvKKTjtlh4uo67WxwBAX4okSlNQ60cTGIzE"
			+ "QkmsKt6NLGgmO7PS9h3tgy-QQRnap7t7KjtmE91lZiYGOlJGmfgQriQ5m4RfpEyjR4Coe_yBHlIBQgsV"
			+ "U_NGUr9-ZnI1Xczda75691WkaWuCZ9lxd6ZGpJSCyiBKdEXqiVZAHLSCEr-mmdvZFo8B2IziQwQ9iDVH"
			+ "hURKnrmyfnt8CRQGVMmscbx8gmWDP66nJgzEB9HoF90YAPYpjumNaNRI6rpQEu1EKJ3-VxSXd5veVaHc"
			+ "bNdrHZ6NS-x5FOXqk3-DnAPTDZEowLsdAt9Z10y0";

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
