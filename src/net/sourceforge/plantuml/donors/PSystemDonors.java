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
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.AffineTransformType;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.ImageParameter;
import net.sourceforge.plantuml.ugraphic.PixelImage;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;
import net.sourceforge.plantuml.version.PSystemVersion;

public class PSystemDonors extends AbstractPSystem {

	private static final int COLS = 6;
	private static final int FREE_LINES = 6;

	public static final String DONORS = "6neD0AmFkBap0wp4H6CjU-RENbb-8cPf4i7oFA4uiOZJNK3y0UwT7lSRwI-YAxltJJEYrckJSaPHV13q"
			+ "W7UtXVQXzBy-27EYV0Z-tGWU80D4ITH6ZIpt9Djjcof3Il3lEr_v9UWTth51ehbAgiMHwq-_8GFlEsFp"
			+ "x3GsNZsQe2y2FmRsrPzVStLjuA5OdYSeWP8k4zg9ecxlNzf8nq_W_vEaQXTjwOuqQQIX0ZigyDXNVjMr"
			+ "yr6ZoQeOCKMAhnNIyEcD2GqvwNs2RD2Wy4Fz4Dvihh2KSIOGHniYT7szjcFRYE4NIdpOvGwnm93074Wv"
			+ "qXTHzDnGK70BOAYRnMniIUN8anHGP4CBUVE7UbBnI3bpEbmKF1u0WSypg42aDMhGXBFvmu2A8zcYmn9N"
			+ "dHatbkPnM9xIHjBmD6BXOK4he8OW_uCkyBHOuwEfirqDZgIU9hbtMudwiztRkJOh-aRN9WdhQ-kbMMnS"
			+ "KSeIgQvjV8xscaS0xfPQEUBBw2EK0pJxpQkAh7wqGFm8T5zDs98_otwma0bXRSC-KDUv-l2OHJWcGwQ9"
			+ "TVnkjhsclT35TZOWOGLOfFVahpDDS50BNOay0ZoXLSnbdGyKqjBg_QB7mzfzmZN54lU6HEzzXANqtw_w"
			+ "C3CK1-X0cLdGJHCidT6qQY-s889wX8uEk2aaLyAvkox7xkCYVUGd0DvKUa0E6pkD4CylKsaw2g4QqdLD"
			+ "cXN0v6cbBGfUInYKQnDqCwe4GsMrskDqTQOl6nFtIHyyRpKO8t5CPwelB7UlYhGmB5QDPcspTe3c55pX"
			+ "upYlUizShWAqjl0UyS4z_yY0dkcLWKYH8VTj6_wHfLc5nIpMN3UhqbtL3suUkld0NIoQnOmoUSXKQ1nA"
			+ "H4lzIkId9SFV76HLVwqkzWusf-kC46567DnUcbnJasCARjDPZeCDGcIenIjESSBv1NjOLf8jR0gLPwmU"
			+ "XOJyZlaa8bNwGcd0hk6km3EIHq4UpdjWrR9wRIh91LIHpGH1QS6gbYgEzBL01iQQzCoqHcn_Mc31MDfZ"
			+ "T2mx5BrYegtA3JAcm4mJwXS6aIWrr4lfPKuI4VYVFtUf9yrraECxO_Xa74Jsa4I0AkJkZ1DLJztjw3QC"
			+ "zH9_lrxApKVmvrzxjCcfqUxvQOBGEeWAG0ylu5Bk5thmDF3Ns30JS8WLLFGYuSfLIuy32GisuvrTrBbb"
			+ "HkCc9ikDoKMlo0zL_5lS2XVJ8JRek0YcE5eigTBBpMfGSRPlVjVX8eUEVuagGoAvhDKo6GaUXLcScsDV"
			+ "RHYQjd834bH7RG9hT_rsckMkCUSha9g6oq2-tP1JgHQXXMfgMjuN0gl5sD_Rj9zoMlmd0_quhRNZgXuQ"
			+ "3d9qVRk9XczRsDiPFOdX8cP3XFwLEISSUFZ6Zxoe5ah3qhOPTgvWXVm6NiOB2OViu0EJMbwbh793Zhw5"
			+ "x1tLA3y1H3N1WGmiLkYQn48fnmCfae8vPyjeXfI3Fk2idW2Sec1_Vsz1E2tH_8ncxUqPXMGksznDPX3n"
			+ "S7u6YPfCsUgH0_jYvhBDAt8xQf4EE_Rj-8xpxO-ecmSHo84e9i5hTQ4FD_QQfM5JVAxJu3ukS_DcyWku"
			+ "5EqyPM1hc6n1bh8Pg7WIWDX1faBvrVwh-OWjvnJvztvZAWmquNdYDBhnN0is8rjjH4eQq7AHXT_wViTD"
			+ "JWvCd1BPydFDONPw64xHwHK0";

	/*
	 * Special thanks to our sponsors and donors:
	 * 
	 * - Noam Tamim
	 */

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat, long seed)
			throws IOException {
		final UDrawable result = getGraphicStrings();
		HColor backcolor = HColorUtils.WHITE;
		final ImageParameter imageParameter = new ImageParameter(new ColorMapperIdentity(), false, null,
				getMetadata(), null, ClockwiseTopRightBottomLeft.none(), backcolor);
		final ImageBuilder imageBuilder = ImageBuilder.build(imageParameter);
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
