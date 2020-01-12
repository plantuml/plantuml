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

	public static final String DONORS = "6_C902mFU3XMJbc44wzsvvsjcZxOY0eHBCyJYiF08fxk1iGVuDxfSR-H_YAwqhrlcX5jsPhYFACe5WGz"
			+ "tYRN3DlSsmEncfYlaZlpHhymBmn9Q4C2N8Sx8ZvLXfGWbehklqpSdcuT5-9WXmgX75BSio7JYHWNmTFc"
			+ "zIrbijRaGSBf_cywBUuU2c6MPYkSZqQe_GEd6AjhBdS_Yheqwk8kLQrkXHhJgOjZk2eqogi7lhPGCYey"
			+ "DCvtG8jPCTb3f4JhnX6YHcU6aPgqpiaJUMh_KI-WJUToJOcS2UKnIYIwRYkTsNVG_OBabn0AkM2Bm08g"
			+ "7BuJj7Ljn4X2CcWOwibeRBbconCt377enBxyer7KkATaUXhQ965Ne2JgQnoGz4eZMiJiz6P09SzeCQD9"
			+ "KbVcJNXy7PQcQ1qqt0iMOPQq2Qi9v4te2P_mLPTCpl5ILF2cfoIFPflGKDasuDxCwDjSInBbVxSnS-Qx"
			+ "HxLvIcUhgut_sHnorvHLosz66-qSmRs_BQHWSR50VW2zD2SgoPzbtrX8GhYtqWiPdFklNAC8d3qo5HR_"
			+ "4LjQe__MbMniHIoF6mjyN5zNcfB1bbo5N8Q-fbNABPqlb9IcnkSXZvQEpoJMbCpgdQUFKJAtyl-xOILc"
			+ "Li90LwQORRfUK3HMiPgUR4m4fq7tXjm4o0ke3fDRULWznA_p0Pct9ICHkGt9AEJhB-MGYnGciAO7Iim0"
			+ "5ayfxI69U212JJlGsr1fHTeYzDjDUjgMZ8Nxf3yyxfePepoYaneduO-5mE669JLlfhkqxz4JU8ulpwlk"
			+ "8tMi0bpBlJTSfFeP3AmhBHY1r1tHsFUoIyAXWH9Px2Ll3V4zmneN3ps-u4mM7M96c-EGvQ5oEncjz1R3"
			+ "koP3tocJpTkhlyWRIBm8wo4T6OtW_M7oI2qkaOHqi1m_Mm1Yvk8LkrBWcaBLBJidc-iKfhglFeHAyEDy"
			+ "0gFoBJp3MElkHN46DCgS4_DUMVFHU0qfFXPGFOS8MjA9CaTAKQQjPMJCMeEUWzR1zbFT5lJR-5R3gaBn"
			+ "RH7TiTW0Q7t6DAFtS0Cy4Ifg5Rpaf330Nxz-JG_6EiJujPVDY8DaSqaCeGAQqtznMfflnz0IrblqVzks"
			+ "lQsSF_zUHtPm6esxhog8deB0_rsKd8EchlEf-c_WWoJYW9MOjN4IAo-DtWWa9JYE3rPHJqqpwdToHhaj"
			+ "jUYTnuSg_NQgn84vDoo_QAKeXoznohQtXbEbP3_-nk8aXuP_YYfhChagfJKnefhOsL6Sfx7lKOgxhbf1"
			+ "17FW4e6zN_zqckOXO_QBbJaC1qD-FY9T3k6kXRMOriPfWVtYhEyjRLqouN_92ByKQzhEwc6wSVnRsWNA"
			+ "lXr9mdWKi7PAd2Ua1lyb0m00";

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
