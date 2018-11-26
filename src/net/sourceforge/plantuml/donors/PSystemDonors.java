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
	public static final String DONORS = "6rK809m7nctnXhJewjSHZX8n3MVAYw086_hm054IjRdwqUp4_JWT81c0qvE5N5zqDfkGDMFIpNEMyuoY"
			+ "M3uDM2zrsEPcFuYbilsNYTjr7R2bcYiX7iHwwS_VkmUE0bdSfipZmCR6EPRTasMlw6Rt1Lf0zRVHovPz"
			+ "IWKtWSqlH9CQulutnzUhP56CxXGfihgn1SYueqQmRtIEyGNrnd_K8ZLfpQOXxZM0M4OAMVEsokHnaFqd"
			+ "C50Y9r5tC8250uc3Vv63lZ-X05AnQjZmZs8orbgzvma799HG8GN_o4brRY7XKGOFv2Ik1W8oFu4HWBrM"
			+ "K4dCrkm6P6aHBP8Q17N5Ui4Zih-OcZBdISDvv0w925e1LG2v2Bh0LsapboZNiIiiUFokZ7Ux542kiWO0"
			+ "wSc2ljgwCc3-rqzchBgygMyjf7FATOv_dGtmh24pztOpkdXaCHRpnrmAmnSBa5-0JcqRBCRpY_FEGWm4"
			+ "nvIweCpPStoTHC2ainGMVX5BMZF_hKlLMb74jsOi-W_xwugXX644LoAk0NnDo-8adIyKTmtrxqEkTXtV"
			+ "8jPO9DIReMUdpEdizx9RYXcb6sXn4pfCbqUO6_QLLQ9OD14O0Vezu259xgJws-HHVLi-nDxs9y6yQU60"
			+ "v3PZxE-gCnn53giW2zAsjui5m6ILQ1ToKZVYXaCTw2get4CZtFNNJRg7JHaJvutUyLWFCmRr9Jf6ll2u"
			+ "c4j6SKEBp9lDXxItE0gu4L-SruzzgCzL0ElgtbMBq7W50_eUvWn3UWt9nv6snAEemumijmNFwcBFpUta"
			+ "vg4ZVpXcjgZdZUCF7dyUCXDgtzkdbhf5cBi6GzyQapNPorEKMrWDrL616f5n7t0-CpbIoH5oOiEP3e6O"
			+ "926txRns5DJWaYSM-dNBzjIq1Dcxh-u2tS4FoniPgFWxvY3YxE-KEAFNAB9VCU_Ch9lwFgJ4kO2SCKHm"
			+ "bmhcDaAAQjYSHeRi-wYdEphvp9SsMqXiuvSjZoaKFY6YTOiD1fj2DEhmRzX50AxVfavUp9W80NoFqs_G"
			+ "qqzraBEDbbOH1t1pIGY42drxr1_6ZZ9jjwQmBj7hjctxM3R--i-SPGMda5_6gWEaAaZl_sm8B1DsKzPT"
			+ "qVU2JvkJMiWC7EfaErREZ9uAC2IeTqi0";

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
		final List<TextBlock> cols = getCols(getDonors(), 6, 5);
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
