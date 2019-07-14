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

	public static final String DONORS = "6rW902mFR3hSmrTODcikzuop9yl7Q5RohVaQVmG9QrfMG8eAo04YsxAZMAlDIBlv0O82w3Lqz9ZRTthS"
			+ "RwI_YAvqzqqpecqsfpJFA8e5GUFvDksmpP-zgJAa53AGwKICX-1fynfBhQmb_o3XTFytdKBoK166eTaA"
			+ "vwCHQlz096FjZgt6TFUZgBjMNTob2jyADQPJn35S5HhbrGDVMoWP5IxzEdyBcfWL4pb4YgUis68UlQ67"
			+ "AxdGEYCBPDR-exu0BLB392Xo2L1iFKcYuwsbdNq7ki-HG8SJboYmcJ479Gxh7MZjt12pn2a23PTcOJGs"
			+ "aRFbYGaC-w05U_WVzKIEMz3AB8Ks9oxNW88zReR3I5gbYYPYTcWpeBX5jEX8O54NnuLmVHmCJSbvq70B"
			+ "KEnYWL9CQW1v4-iTV9gDV57dqZU12bxr3ecZsHbMLzaXPZutPlJjpqC2ol_-oSmOdotbQa6L9LzdVzHf"
			+ "87S96ZdiZTR6AblWNY_gb31_iK1-0BrK2og9xrat5bg0k9UK5-Jcd3-vZY8mDgK45di8KrgJ7zOLQ6n2"
			+ "bhiP2to4dujDi88jk7BvQj3DjE8vfVE5b9GGlttHgo7zLeWrSRDw-wXtJvbh-V-wQugP53VGIGczJTS2"
			+ "58KnMBKB4nF1ow2tGsu4P0EKXzAjE3I_o5_x0fct9lU0o6tEiLygtb0tEWGn3TPq8OKPmFmkHRbpudO7"
			+ "4Kvh0tgNgImZRUuocrBDr25ZxhfG9p_kcnchue0SyhpmeKvmSyEugZVBkxGVC0jubByiFlsSH_1O1JWF"
			+ "zzxpmj9SWe5sXZfQ86r6rFSzzKNX9WubikXRxEBakrnFN3nqFVZioQ9TpF_C-4yW7uFAonPAwfiCRn9e"
			+ "-WwPR4nV-IHlmFyoCCqSZenFCUzV9i_Kn14dJjCnd6ri0B1N5JwnE0Xk1gfkwTbPJ8gORBxwc3aJ_1XV"
			+ "W55P2vuAszdTI-8CQ9JO9-QzCkekldLAFXPG9he8bjA2KZqeAKB7C39YrA3dh3LW-rtC5kYrpsnOKYY-"
			+ "BQ9x6Xi0hLNHJUXxk06U21ML4bwBIS9W_A_nD3qOw-3ZDosr8WwIrIKnSwY0vkW9fwL-sRgaiDwYzrlN"
			+ "zQ9f-FF_oT4CJjMuvgiAeYi8_5-W81nozHvuBIhzz-2J4hSDZ62rdjY9bLT6JpDBI13SibwdRjECz4nK"
			+ "b3kGXTfnVAZuJog6mzInmVABPPAYso0dTRs8ErLGzlKlmaaEXSyxHPKTHLoNoZqn8fdO-L6S-TXdJu6R"
			+ "c-1cwePB1FgU_cwOn45jpHSfDGm7GvwAIAETpri3";

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
