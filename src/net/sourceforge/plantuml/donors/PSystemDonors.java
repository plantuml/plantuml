/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
	public static final String DONORS = "6tC70AmEU9ELAuNYZT_MZn6AGOgeeHNOWjgQuZoZA1P0SxnDhXdMoRgDdR45mND5SGKL8Az2C-THCiPX"
			+ "qGYJjjcQVk6-VTu2CLLilsL2UtyTQ4BoLZ2km4tbpF_b0XiJv0R8GZti1NIpXNlZcZIctJyBxB_e5sW1"
			+ "JwFBbaMbGPxbz2gOv30uCySffUIX6EKhKjYwiGMmql4WLnkhGtmZTVQTMa2JrcmQChC65Cj8aMW-Dbex"
			+ "3h1z10CdEhIhmmPaXn87UODnG7qZHacvWWQPKToC3TMyymI32JTKs8alIb9pj93CA4C7EhGjK83rPu80"
			+ "s2VbbEHiwMr0mHRPegP1KkTUTwQB_SEoL22_D9m07JG7hQ1M0FaFTCEViyrSubp7hgWPygwCSzauvBNH"
			+ "gzZVFD3NMLS6oilcnSnyBJ-gMmlfd79TurTNW_pGa9MzFOmrNbb2UFutbqBxOW4S7q37MaF5-FlX_6OX"
			+ "7S9ZIbt8mJbvPxk905bbAIpuBYvPBV3pBc6nkbWUpLayH-VNkLOoe8Ch4fkXZsi5iwjpHadLKF-yv5NN"
			+ "yIboJT1itfVeHx9fIFp_BMLMqnsqteGFroM8KgElhAh5HHS1qw1_Gsu1kGFLq-JHTLg-9Bxt1-JUUv50"
			+ "53OJx9-wPjIdtBo4UM9Bfwfu01Zj4kr6X_4WCuYg0rq7bMTas5s_tQHdsDGOEJTwHRrfDmQkZtIwSZDz"
			+ "DnVu3CEWB9e3XzfR70NSXnFCeTNYRHN3vXhUROlKU2U3_JNC6O7q6L6E8st9zAcEChCybhofIxoRsyMV"
			+ "Xox-wTsY5ZhlRly8VR_9hwIMUiHuT0s6lKMNzLkRB3eMu5iZ8u7BW9tMxSRaG2MtqAaDPqLFSGBMlQrp"
			+ "S5oBuA9ftQnaPsedrypQGGThaXzqD61KP2UIWw_jpiWp7ekgzYVddLZrKD-757yNK2o614-fKBOHKdI5"
			+ "pUbamhZTC-uEtiUjpHUgs_ZcmXC61HzBj6cvmUjJAlSKya5J1v0wQrNoqiV42E1tVFq6FS-j3ZJAQ6cL" + "SNPmGsH9";

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
		final List<TextBlock> cols = getCols(getDonors(), 4, 5);
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
