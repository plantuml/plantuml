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

	public static final String DONORS = "6mOE03mSRygAqa-2sUB1zgHs3y67oAViE8BbUK9nOX6dku3u0zmxFUutqbz4LtRlcsP4hTScvGoY-Y3e"
			+ "0Uzk2-r3wNzz4EOiUH7ScH9SG0g8ukWI-P5kP3jzKcL82RxyZVJFg8c_kaepeSDd0_UtWMiJ21dDARPx"
			+ "cJAlLqYb59-_yX7w138rLbrJbICmjvO2rb-oUYLzo_zDdQtBGo7CqjqAZC2eQ8JBFFOO_vygg70ATWur"
			+ "xFHZEfLKBOTgLMApxKqHhQIvOrQeR_9Hg-QY62r5YdyqKl2BP8R6dCm-ILxqcyOVTHCwN9yAoN9FG6TR"
			+ "FJgVz7QkAx7y8j67W2VuWcm6Hi1of2wY-RdfHiF30mxrkwZ6bfRSyoG9Wane8H_-KPYINf9dLy1Dmwi6"
			+ "8FpUXWAI7DIeXhELkm6LbDYYqy9L5_k5kMKTbgTod3GyXLXucQW5L0aI7tH1PzGyRqtkxWPRfAT4Ozeg"
			+ "bCy2EsYR2_h6bXB1V6qVcYNuAqQvbDGupdlltxKNWDtIygg7CKSuPw0kMoZ4U6k1-X7eiJbXYRwct5d8"
			+ "WgYFqvi-TAuvgLM4Kt9BMQpJbx3bjPmVkgJ91YLv2YpwU-ANOWjui4bNKwe1NsYLf5dd2vYqpD-N7Yt_"
			+ "VI-ufaJith1e8rmXwQ_V4PiP2W4wE8NPq4q136MLLXt51YBOBNHiW7O4qWdbo779H_oIFl8J06yk8Rh2"
			+ "HaWJ-FFBaeSEBc8WKzUcS06m-PIYBXe-3x1GeIdeLbG9fRVny9qvMlOvRMnQ9ktcURl16FandQLOcAzG"
			+ "uH669Nf6SyxRsaVO9NWM7wTvrNkMSnNWKk4zQkFQBpBWvmmP4IawNV5UpsoPXmd3BFQSbodTyyZWuA5R"
			+ "3jpDeZdCA9E7B1i6XnDHhO-4_hOO_fOXcvtN6bIVOEjqdH7SpCY2rrkJMwevek6noI9EDWb8CnUlE2KB"
			+ "5n2iPTd971Kb8j-nUX8ov7UgJy9IfIuqZTXOxYeyetiKvE6y9rOzvayVw5n0bMeJXAG5Crj2-SpyW0oi"
			+ "LVJCjYHs__U51MFhsnCJX-BB9jf-SWF159WUmddW81B9G5gYbpKn0lZkZxlLqsiTvl6tBwSO1qaBIGHK"
			+ "GQOptb1cwgazmiPwY_wVj6MB2t_-jKURd9hisdi8edKG5u08Bz1HBWWXdW9V7pPC1vnWYOiECbovPkbP"
			+ "8B9W2trm4tLvcz6uunVBZSj5QyyFLVBTt0uNyy4tfBW85ULzJIcPjfb1DT8ltdMuoK7nTr5bM2INlJcP"
			+ "Z8HDe8fpT-pMDjHZoG0pc0EL2UpT_LbPQ6ZC_Ggaod02vFijaaf6XHPMQreH2u4rSk7stfQJyepwJ4Rq"
			+ "axNE6Nlrq3Z8rVKN8GhV3WbvGPYuNIHE1EZDDISMTFDD0FcYMYY9fHqTTkvWXVmF_WZUI3Xc6nzOv0cb"
			+ "UPODENIJx8EpG7qWxo4ia8d5Ibr5B9HoF90YAPYpMiQpIDjf2yxe7K2dA1Z_tziGBYVH_8fcbrxLIEPo"
			+ "pBsuWiY6-xj8R56oDI0T-rhcigqpSZld8Xrswd_WEyvtFg9kNKH00b5BWjUMWtHtOvzQ6ZQrvteLnt6P"
			+ "fuF9BsHNjFELWQtnOWkodCr0YIS1X1q85P9NuLVx1wuvvyYlzwbI6AZXUUOMTEEvDp0MAvR4KzA0waqM"
			+ "vTht1-xanGc54ui5BYmLRlGmgLihd6h7JPlrf569NALYl8Snkxcav9yg1fNIDRcze6P9Q5rtbj4Pd6nX"
			+ "Bi_xl7jX1fNw9UfJmHu33TU7RvmXlRAiOXyX50RLk31POW00";

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
