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

	public static final String DONORS = "6y8B02mFk3cOSJIBSEBLlhdPHgj_M8aA4IpF4uh3m2AUxWR47-3UwN6_aVuYkjAzRveHRVbhZfzd3DL0"
			+ "qEFnwshOvdzxy6_EaXV1DrE22qW1-V5gD_8XxyXnUgJAa1B4Kj7zbudRqwtZGcMSg4Oe1ufR6zvtnkRP"
			+ "QNpnwj44RGH-3Eoh_pTrbNLvLHMc1TsjC0QZeX4kOoh6dWhScl-V2gXm2lOT2lJqOpfrSHqELLIYkRcy"
			+ "LjAS3BZHDSwXmvAg6AE9b5njf90p1pFqv6JqK_gXZzw_j0leq5fTGpndGF6OAJssRdixikzW-hMAWqGw"
			+ "n8BD0dSevQWyGV5pqus7j0WIkdQHZMquyfidCHXgO8M--KCpET9xI6IN8SL3HWqKvBoD0TBer6C9Psk_"
			+ "1bJVWor6D0ZLvN7ZxhyEorDkDXfU0HOP7jG2MWZoFzW9dz5NNZ8vvwQ85V8snNFOLipNaW_JDXLzYu-c"
			+ "G3XjFpIBn5McR3saSUHrpBywEkHTGrPU_MQCKSWZcClBN0hkbnQe7u4UMXEHuckLNnOI4AuZzQ9tdTkl"
			+ "r2p2AHqg5UlqIsnvpVr3bpBOZ6Zk3XPp7_svt9K8zj2LIJNqXbPzcdK-KP8Qwk-7lBfqlWkkgLUxTvpM"
			+ "PzgSP_zttTBCK0xGcILcGRS21CBLj1ekDX31OQ7BG-u2j07bq-HMF2qVOeyyGV3cCeEki97I2_4y2Nd8"
			+ "HGWpqA4xKbG0iwUCUlAGTOE7PXk2RaMLV7gvktw6ISksPMDccRJ1Uhl36CKndT0ySR_C1HvXM6yMpUvk"
			+ "xIFi4jpcS9fFtRMQLmbjR-6zu2AqVyY0ds7EY4YbwPhxddQ9UwWZC4kmuAakjfVRmJyFtR_W58jMCADD"
			+ "EsGET2uaewRTelIXmV2t2bd7jkr5VO2juJe_X9cH1i-N9hUqv18FQTjPJe8l05NMuXExLE4y8PfMaCpH"
			+ "jOJ94Qid8KD-f_e4bgfoOUd6Z-cknZEcHq4UpdjarQs-JIbk2wWbfWdoia9OCwGemjO26RXMqhFS1zZz"
			+ "r8u5VvkV7DXoABvjekt63HQcm7GNxeq4aMXs92LwYIbHmFV-U4ezCzV1ndVtj68Sf1QI6AZ27Cx_mHXf"
			+ "l6vJqrYlqVzjhRqiEt_-lOviSkhrUrzT43q7cV4VfG0TvGHcy3RmDUGpoTiBArcX76IuSgtIqq1XmNvw"
			+ "vqVKrJAZVoUAvPRaebMq3rN_tOzo39oD3IikyYcAsPSOQUsb7bIgxVk_M2no4Folg38dawktRZL6GcUG"
			+ "nRaRkyrKp7sj3YnO7PC9WZtrsUaQJnbpRv4g3JU0VBz7GvBHGmjRQjeyJmIsg_Fpti8s93BwRp2Ydwgr"
			+ "UtohX-63z76g23HyXqCQ5ym4mqNoWGAvEUT4Wsw-yP0NrK8DBEsOOPSANkXlvaT48U6ORNmWo6qkbhkz"
			+ "a4Fsa7rcDOeEAAeB3RIms69d4WjLZWSgae8visKyZzf1ct1rxm0wHS7vVzw6S6wY-GMPLURP6ybSuRaN"
			+ "Pn3fS7ucYLrJjcwqOTzekMphYjoHjKY7jVzt0000";

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
