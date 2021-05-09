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

	public static final String DONORS = "6wOD0AmFjCllA6nYeh4EstErPvQF9MScYM1vdb2SMCJfBY3-0FTEZ_kDz1VHbTtxfXbHwxL9EOEeFWXw"
			+ "mBlRGljG-byVX3bBNWHtfWGNa0AYEBf4VgGRsKwVL1bIWcUfwEzlcRYztJekL2Q9lX7AWU9-EMJWRVfi"
			+ "dft6eD4BL1_-zf1sN_s_gRDrUIY4MRfRWH4O5OrmN5ycSw__erfKE0KxXnfs-Z6Tkg0w7AfLYSssDqMq"
			+ "akQEMQ6-dOzQJIwAaLAa-6iZ3RyOJ6ZCoUoJv9T-qVYbRiA7rkeQOjm1n3bJEhfkw-ryF8Zb5qby2F84"
			+ "Nr2im1n8ET8lejcvwK73mu6XVXVLsDB2FKzI894DBUJDFugaq7k8iylWfk5L0GA_Tr21P0ngr8Ip0tS3"
			+ "Akd15fqQkEfYVs5fMOVbAJsJXgSH2oyBQW4r14aCUa5dz9LRfkvvA1m9UKhYEMqLwi-2Tz0s5_IDBIK1"
			+ "wslxgLcIF-AKhOSq7RdF-LTd1xnhoChLRyOSaPoWAIzpGPblBL0_0Zqq9so9xrNkBCGXr4VgfW-TQmwr"
			+ "Yd12BYaLw-Hbjhsc_w4hPx914PuE5laz-UjYKnnieQibDS0RMYKqwtm2IGtrzvSyNZhV1TUKIDmR4Mr4"
			+ "TWtHVp-tD3CK1-ZWDBEXcoPO1gkqwYYs888zGiS7t2Ga1-5ycbpsf-MNw2q_0V1c4a5ts0WqWT-_k3Ba"
			+ "o8KqaAbhqrm0R3sjr8k6poCCAdG8kXLLmkbjxleP9erjoyQMPT8kdhSRZ94Vqt75XUKobyW8mnArQDOV"
			+ "rWxHBi5LFaxoglTCSXNWKk4zQkFUlsJ0pZ1d859ikkFnIDjY6UgmcCMUwwKkkgVRoS53jnoucqLpc54c"
			+ "3va6WoE9gActYFhGOFYRG5Qprnfqds1hkCuC7cQamUkfoGqrEQRXiPtbEDaYH8QvUCKf8bnGi9Tb9pcw"
			+ "bf1onEfnICZlH9z4f4fVA8tOkTnLU4RsACZ3UO-iUjIFAPXSG9KnJH2M5gmvGz0UjXR8m5f5psmRnEwN"
			+ "TYoOs_pan8f3yHKJRJTkK305PeOmRXX4eZBGAAKNdoGYyDqVTwidrpgiuxjxMZ4EaXQI2AX2cSxyGJLJ"
			+ "Jpr2qrYlyVzjhRqiEt_-jKSsEJNPQ-yXYDP1J00Yl42NSdyGmLFWwys6cO1Jh62Y9mbNhbbw6aXOi8K-"
			+ "k8awsMOqRxnORbmkMdj-g9AVvdQuc0s-8ST5E6YVVOaghRtKWuhOlkLTXviS1lukgWoBvB9NpsGaT1Ag"
			+ "EBV7RZCLpLXbW1NCWTe4zkx-h4pZAMFkLo1LXYj0ljkG8QdHGmjhQbeH5m9hf7DxRsuRuep-BnZeHxJM"
			+ "pxrwQ3p9rVKN4uLlHmZZd4ZYSf5u8CAufUP4Wnw-2I8VL0jbO6d733jti49-Wp_66mb7xE03JZtceZpj"
			+ "XHnZ2zaxPu4_8NWFeO4iB5RgLSH2ASS3APA2EQjbB4FQILjmLcy0dQ9W_F_UXd1wela5cLtcrHl9N8wv"
			+ "brGGwN1_5ebDIxO6vC2-bBbiwuhSphb8Xvtu7_YEyvsFg5iN4IY1A2N1QpCXtztOfzh4flHShy5nNER5"
			+ "mUGNiYlQ-PA1hR7OWYndCr3m902rWqA5oglmg_qJrvmpvDzxzr8OQCBpr6rquxaNJ8iLI_2KDA3c9alW"
			+ "slTxRh8uK3ZHsE32KdEZXqazMkPJxRXfivuCAf6xL4pq2BERbgZo4rrFgiiXKxTUKBCajAux4tJosnRa" + "qhLppmip9EnB2W00";

	/*
	 * Special thanks to our sponsors and donors:
	 * 
	 * - Noam Tamim
	 */

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
