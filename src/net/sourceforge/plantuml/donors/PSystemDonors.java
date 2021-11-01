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

	public static final String DONORS = "6sOE03mSS7Dt1Og5J5Uh3hcJits4ZufNcNRmsK-DFu8Mfx004WGrllMtt9JI0K46z1ewUipjEptkDz9V"
			+ "H5UwzqqpHgtN9kKCeleWw07lRWljG-b_VH3cB7aHt9aIN40AYEBeOlcHRkGuVL9bI2baDAVOxcNAl5uX"
			+ "bL9y_if7w1Ducpuwpvr6qQZ_14uVjNqKUr_pyskkhY4MYkLvWX8ewJ8XtKt-nzoQwNi_RQJZ9_3xf44L"
			+ "Yw8jtPQiaJOTOzxNjG0TqwxLSYOVJMfMnRYhIB6FHXfsaKneoyNaS-H5dpJ-g9lm4VN8a8ow0ZhBwkZy"
			+ "kRUlxudvbui-ucG9lg5eWH6m7bcBQ74UUj2mSC3GiejYR5NXo9DqIEH12tdpZy8KHahEBeERXbK38EpU"
			+ "WGeaEQXD3MSx3mEgJB55NmWtNUeFbixxibAgq9M6Pn8RAnErW2f5OeCkwCwSjw-cQlK0HwIdH2riFShd"
			+ "EFRI3olwffSImVhQEYnBy5UKiakfwRXzf_hTfmHmXBHyvyEO8fGJQCfW2cNyMe76LUYkjM2BZgiSBAH3"
			+ "rCSjDdtOt7XeHOHZCgL2hUENiPrgXJzqAS7693m95lAUzgNK0WussQf92-0BhKfwx7m1oL3mSj3ZE_w-"
			+ "TxYcKfAy8T56E4FIJy-p7SrG2D2nPSo6RgR0OjnbLM-n1H7iKJewG3j2qWbdyt7PctsGVkKd0Dv2GdIx"
			+ "JHpjuDzVkZ9rv29D97HJ6Yk0jQSLTT6mUGI3Mar5DsDACBsDSzpfex7F6SUQPT4cpriDniXlrEcAXUKo"
			+ "5PbkMAcdDnU-OzjBknJtoHYykjqdHrq5k9HxJzcuzuiCnBcQ35IAJ7Uy-vktB3C8YrdySxs2qhDc3suU"
			+ "kZFOpcBHCcQsClMG5S3WK99gVIGQIuYHtWx85lYr4Jq2oAznf0OFC_BWzMF9DTKSqx3OP14ZcuJ8jZQl"
			+ "s3k7SXwmv-LFScIKIBduwb58oE_OM4IqojeeRTYOxmeyGr8KvEMyUrOzmRywC7K1rQcY82WjMAiajA-U"
			+ "3v21UsSzIos9CrvnOK7TwdPWcg3uIXHjFRc1OWhC314-CF14Cf2LiakV617uxe-xpIEb3ktusvTTZ8Ea"
			+ "kIG6gY3Xr3TK4mtM7k78Uel_VsxTO2V-_5zhskJKRi_UGn0zWOG0X7k18UC24E8fyFLcmnp0AIm9qK72"
			+ "bQlMdWUY6Ena37PHTompgZT-PxaPkMdjzKDLdffIL3Zf1x9V18lskgPKwJO4dMgoFxmHBd8mjd-9AhDI"
			+ "tBlAQ4Tb97mIYZY-u_HoG3EM3j22DL17WNztVrOMQXfhlqAfF9m0-MP3KgcPheNLbjR6Gc2ZNDZVMxTJ"
			+ "pipFoi0-XzM5uweUsWOfRgGOKVWsA2xF21CpYQI927PFoOa78tpZGRwe5afYQLkxdJbO8VzZFuOt4WxP"
			+ "8GrqwZ6bFDs07BP9zx5Le3-Gzn0UoCHYdQui5aebF90YAPYhM2Oof4sq6UVs1Z0e2iR_znQKAqmqFw7P"
			+ "fLOsahaCQpSf8FAX_iuIM-HihKZ7lhlciitJS_lM9XFisF_0ZvzlVrJTV1C1TKHt9NZTFiY76zjKLx15"
			+ "ovibtCTbiMuclunBqIvH3MpUTpPgA9k1Gaf2Y7WWL4XVutvDVyAMSu5yUlrbBWpKy3nH3JruRWPMa6qq"
			+ "l4eQKBt8ujpwVijDtZcGEV7OuC8gOwU7EJrQPgXjDsssa44bSkUAiPx1x1ubLJwLCQYKhgaQ2sgMXDPb"
			+ "PmSw0-Cjv8hvlNrhOGQN-YNcCy4f1fgINXh8X0Pa4Kb13KHNoNxLYZEaPwPLuYrMjfA6gMvKGwFTO57S" + "aIUUpER31W00";

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
