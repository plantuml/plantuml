/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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

import net.sourceforge.plantuml.awt.geom.Dimension2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.BackSlash;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.PlainDiagram;
import net.sourceforge.plantuml.code.AsciiEncoder;
import net.sourceforge.plantuml.code.CompressionBrotli;
import net.sourceforge.plantuml.code.NoPlantumlCompressionException;
import net.sourceforge.plantuml.code.StringCompressorNone;
import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.code.TranscoderImpl;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.AffineTransformType;
import net.sourceforge.plantuml.ugraphic.PixelImage;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.version.PSystemVersion;

public class PSystemDonors extends PlainDiagram {

	private static final int COLS = 6;
	private static final int FREE_LINES = 6;

	public static final String DONORS = "6t4E03mSS7Dt1Og5J5Uh3hcJits4ZufNcNRmsK-DFu8Mfx004WGrllMtt9JI0K46z1ewUipjEptkDz9V"
			+ "H5UwzqqpHgtN9kKCeleWw07lRWljG-b_VH3cB7aHt9aIN40AYEBeOlcHRkGuVL9bI2baDAVOxcLYNg-G"
			+ "IYa-V-KZz1F8fg3pd6aKYPeNE3rg-odslkRdrrnTGomKo_C49L3IPKAwwSFSckbxFssauoVm-wH15OkY"
			+ "BTsMh94s7MFUrxK07TEkrN8c7qrgLiMugaYnZauQLf5CQCl5v7FaHPyq_gWtuIFgaI4PTGLqbjNHzNDl"
			+ "NzyJyoyNVSJ957n3KGCZO3soDj7YFFIWOUA0eMKNnTYgmf4dwP38WnRouH-5AOoKd5q6Dmsp1a1OlGCL"
			+ "H79GcnhETXm6L9bYYxuGRhhKtrWwxyjAgK9N6fv9RCnCr0Ah58aDkg4xSzs-cger0XoHdXAriFKed-FO"
			+ "Ip-iw9jUIWJhQ-kqBC5VKSekfQRZzftgTvyIm17IyfqFOufGJg0fWocKyMi56LUXkzQ2BJgkSh2G1b4V"
			+ "jxZfm-R6GoiY79Cf5MeTlupjL2lyeSiMR4p2as1nlwTzARM0Gqssgj82-4AhaXuxNq0o5BmSzFYEl-yT"
			+ "hgcK96z8j17EaFJRysn7CnO2T6mPiw6RAR0OTrdLMsn1X7gKZWxGMaW6uMnkbxtP1-aZFm7m5XAYsstY"
			+ "Q0T_VcdAr96BD97GJMci05QVLjHDm-8H36er5TsCAS7qDivnfLjZ7okELSkYJPwt6eo9twZJ51lBPIeo"
			+ "tR1I3sykVCUsbtOfxfCnU7MxJ_Qw2cWfzvvouzulCX3dQZ9GAJBTyUvllcMPGLZCuf_h5PIUDNjoyz2T"
			+ "mNOMYvOnivMfXwm01eUILEiZqLX2ZFIsGxR0hute4K1-ZICbU9YH1wyVIwwfvfY6oIQBwDaYHBQrUSNU"
			+ "sPNpW3qlVv8ZeqJAnLUFGa9-nyOYebNMHcd3vdnNu1btYgAyd7VFgYVuLmrJ5r2TAWg2qeArIaBxwlK0"
			+ "6RftrhFKaZZZ5ISMbBqk1uO4fLUYQEl13O0fD3D4yC304s4WAsMNDZ4YyDqVTvldBdMenzy-xM8S8jqb"
			+ "2L05X5DUK4qqM4U48-ij_l-vTuET-FD_hMcJKxkzzps4q1D8242u5neoBW0XdW9V7pRC0Pp2aX2T95ov"
			+ "Qkrv88h1JiPW5tNxCgDoutUMcv6Rrbu_LEMfAKM5atuW-qKmQAzpIPNkGz2fx77V-KQuoC7O_YMgp4fn"
			+ "xogZxPMHy4aeulYEqyi1pLWOe0Phe8u2_-x-h2pKDDP-HL9vE03vPaDIgPckHTMMriP6OAESsDzRirFE"
			+ "pC_Am3wxrONpgXxQ9Ybkf1XH-DOfBiy84pE9f8a8Ta_9YGSZVED1lgWMIs9fMxkTCLWX_sC_XZSI3jaX"
			+ "3JJgCQKytO0SjadtiLMWFuZt41x8mcATxYmMIoKyaY8fdAjO9ZAaJxGPv_O6CIWAn__t5fGhJ3G_eTcb"
			+ "TdQIkNHhDoaWyg7-pf9Qv6ojoC2-g-QopSjp-zOc4-pO_y2Fdsz_LDry4m4uegiIl6utyk765jKLR45v"
			+ "iuJxk2pMpUINSIdQ9Qg1pVlEHWsm6Iga98A81w12U8txDVq9Myu5yktrbxamKiFpU3NqpAUx0ol8DXfU"
			+ "9GsgNcHnR7s_vIRl7CYSU6nmOPMnqyES7YqprBQRDbl8e9AuSyLOps1sZv8gdygOLChGL8qDDKjYwxAp"
			+ "7Ps1yItaYlMzVMjZXfJw1VEPu1H3JKal3MG20Lb4Ke21v2hPhrfnXepCj4gynSfMqZ3LJMfej1iiYZjB" + "Ms7pORCrtdviIo40";

	/*
	 * Special thanks to our sponsors and donors:
	 * 
	 * - Noam Tamim
	 */

	public PSystemDonors(UmlSource source) {
		super(source);
	}

	@Override
	protected UDrawable getRootDrawable(FileFormatOption fileFormatOption) throws IOException {
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
		final List<TextBlock> result = new ArrayList<>();
		final int maxLine = (lines.size() + (nbCol - 1) + reserved) / nbCol;
		for (int i = 0; i < lines.size(); i += maxLine) {
			final List<String> current = lines.subList(i, Math.min(lines.size(), i + maxLine));
			result.add(GraphicStrings.createBlackOnWhite(current));
		}
		return result;
	}

	private List<String> getDonors() throws IOException {
		final List<String> lines = new ArrayList<>();
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

	public static PSystemDonors create(UmlSource source) {
		return new PSystemDonors(source);
	}

}
