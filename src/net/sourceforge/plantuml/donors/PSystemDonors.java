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

	public static final String DONORS = "6wO902mFU3XMJYceJdotTlPGzS6YgH3yYpFbZw2FTx01CWIrH4crEd9zz1eWg82keQF7tBxJutuZ_KLq"
			+ "fNlVD2FQiZN5UKPHB0Xwl4sk6RQvjmV2d2KlWZjJWfuF5n0bqTtfBCiTANQVa85C9s9S14-RrrZgP2tv"
			+ "1mcd-zyr5RAlgiAGBgiwVxf2SL807Z2x_5ETowPoBbUrwKqeGAVkNC5DLOUvTgAlJWmIOEBrR-TRrMHM"
			+ "JF0GASwsS0HEepD3CnUjCx8Xi_OVzGzeip73Kf5RGd7KBedEFo-TsJRGVOu44nAxADWmvb3AOLgWUyYD"
			+ "cCGcWeRwB4PZipLRdkXXS4CB-_AFHbBZaShCuMYJsDq31KxVnW58hzJK96RxDmFAH4QB6KjITM7SX3ld"
			+ "OJ4LwGSDDuB2pe9Ma1g2l0bTyEan-rEJMrqD1Lxo3CczsIxKATi6jCsClirrIAByxpRDjF4UKowHwcpz"
			+ "UQxtSGx8tL8hp_XZj3784TpxoOiALfvQ03y2lLGBbUJdf-mR2oaGzzRjYJmwLwxSHn4Ocp9CBFOgjbYB"
			+ "_5YNjx4LMLOO2_helYuq8GCjTELoD_HjMfaqTBvGafPMBXzwT5gz369DnaplFFqP7NCv_xzIlCDCYXle"
			+ "IedPfci4HUGSMaqN6nE1QQ77Gsu2iWBbxheLk_SF-NK-8FEMPGGYjpcZVL_nGjrr4M8wrdGrnGnW_fJI"
			+ "5ed3WWW5xOBUHfL-fAqKknwkdkaJO-w-w9F7NJDZ40SwHMCHldeHtDqme-hDaGtjUymXU8Wlowkl-_0T"
			+ "gm3MuTuR_aZDds0Wd-c54P568nhRrlsfSBU24bdK9HShybt5prmyT2TudScYGyoysXo7B0oL1qbQwfyC"
			+ "FomPVFjCDkolIf4tu7bfc8PmP7mCBXzDUjJa4edDT9PZPYS0bofUiTzymdMXwfQQaoriYCd3Lnz29lWn"
			+ "le2bCXMU1jQwUr6SGOmIvyIyLrOjAsjLyR60YbaXw4iBfMggADBdD3BYh47FGUjW-yt25kYbNdl3c51u"
			+ "6wAx6XkmJH7JLKp_Q0AUY2gA9NefayG0lkR3hzL3Onry_6Pq7n47oEwI4485QewUE2tDjsDO1-ij-l_p"
			+ "PJbv3_xytslQmQaLztnM0NI2aF_hA1W7JLtjCmflNJwSE1om0gasJcBbfQMdGG91v9QTFdMTPgHtOaIv"
			+ "BLdqZkD3PRvI8Gv8OyFoYtwAeiQKaxwSh3YL3_lE6-4aXyZhdwBAcXHNfP6Q6C4cTdOKvsdiLhlXheih"
			+ "yA0UN12e-_NZqyqiTuqrNq1g61uqvESZSkZGmxMWfgQ5iIom8iRwtYVzKAR7RyQcW5qbW3dJ0000";

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
