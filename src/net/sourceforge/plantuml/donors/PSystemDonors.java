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
	public static final String DONORS = "6t0809m7nctnXhJewjSHZX8n3MVAYw086_hm054IjRdQYUQl7wS390DmUhAGwulEPXEoPaowUSvodb4K"
			+ "owS1wwKEszpi1x5KjlyoSRkk0xPKSoN4WrWl7Rft3nm5ihXDWKGK1pOsph7iBYfxHPVT2xI0zl_Vw6NB"
			+ "FkK4kXvWDHpiUNFiN4fgnQW7aIAg6rk0Y3jg-6hZwmXVK7txZrga9gtPDCIz1X3BI25htlOqTXpaxsCO"
			+ "A74Zi7SmW8G3YODzaOEyFg40vdoxOSCpYj9OKtMN9moGa465RFX7Jgfp2mcFCdZWHk1g823o4nW1iR_a"
			+ "KvApbPq16LjaYvW6GLrwl-0Hx2-cfGmvq70MsI4GGIk02Y0dGHVusjhC9Sfrx0eBZzwLyVui0P2Rh0f0"
			+ "UbAZhxok39Y7nPCPRL--LBSMqZdbEiU_pWPurf2PlTsCRxoo68lvOow5uOi5b5-03hK6Y_7yujon40N1"
			+ "CQKkA7Ds7bzB4H2fB8M5NiIIrGn_xv9_B2VYMtCMpPR-TUbGmd04Look0NnTo-YapWt5LKFz-v2hhkDR"
			+ "v1gZX7cJzEpKdCn-Bxkbp23TGNDDw71T5c4kwIkhQB7e8d04wE-0XoIvawflaqVxs_8XzhO_2UvD2WUa"
			+ "i1bW_zG-mx79uIx83VcqgueQm7YLQ1TkX6x43Qiwq7bGUOP6kEklctGFcnfZvqQlM2w66qFxIpfAll2u"
			+ "c4l65eP6MJGR3hNNE0gu3ZkKruzTAePL0DSrl4iMeV5A1kGrp1c6z1cIZoDjYSUc3ZEpF1Oyguizczl9"
			+ "n4C7_t1CPL7R6ySVF7uuP7gy_4gVMkeMOUuQ37ihN5PlQsTH_i1yc8u8x94Pjm7dCvCzbHpXXcyuqy4H"
			+ "aoJ4MkUrew00jt9Y_TOZsrFJ4cJjiRe9L2S_93SoKB4xPX_Y_DqHUKPZKM2_SjwHMJVrVKY9OG7POaZm"
			+ "b0fc6o55UzYSJWRqNkXfZKu-ywND5wBR-EM71mmAFo7HekK6hKy16g-mRzWv0AxziafUr9W80NoFqs_G"
			+ "qvHruBECbbOPPvCRf9J4n0FyOVnIiktMl9wYrstRzh5i_FMVESjAJdw_ZFKA9Bk1DF5FXq0oORzBkolu"
			+ "lk5JtGWjP0PsTRATAyT6JmGO2ch2CjYhzch6skMzM78nOmS0";

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
