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

	public static final String DONORS = "6qq90AmER5DS9tl8vfhbtlpPbKCCAM84o_E4uZ2mYAUx0V47kBTjx2k8Nq5mIRiNHY1PcfrHNOSGbmZ6"
			+ "UZUdwEgVdbcBbJybk-gCEFN2l32zqB1XCybzPh80tdwg32d1lpLJ_WZwmrDMI5HRTZHHjDaZL_zlEeNa"
			+ "eI8CGxCL7YMZ4Mf_89AnPgUQmzsFQjsrwk8kA7HReSPqYZY6gyBS4Xz5LlOApDboYPUh5efRQHnxGSMj"
			+ "cF0IAQwsiGGkEQY9IaqEPxWXzVKVzGRQRAwmKikzeBYs8KcjV__OEGvmVWw3IXY7MB21COSAX_a4FKMz"
			+ "4OEnYqGJfPuCOnCndpmnm8GNcjZ0FyeYfaTfPaz76yRXaL30vTTm0d5KLLHeTl0ne7YKMdIicAwBuqwp"
			+ "-lhGUqeQYuQRm8A31Mc2L01v4tJ2dn5hV5MvjztGm2lEpZhRPY6lyl3oyUP6tzwuir2-xnwUwUEweTgY"
			+ "bBcDxwszvoqWTolrMl5FEMAPQ-9skNegqFBh1V8Fe9VcHCNwcPU7XUOGzpRiYxZF-PsF8G9ZnMcmsBiO"
			+ "wZNfoIv_iuaipo0Bl8IVYutCn1Q-Sl4mw9TQyJoYyuMGPMNvkEXeMNjCj2PkrhrzikU8kPh_VpTzX9ca"
			+ "4tGoHMtgceXIPkrUjIU35C7JG6-6t0Ho1whJzIfE7nVvptf0z5P51gAqEUFzGd51RMWpn1nOwqA96y3y"
			+ "Af5E6AUJj4HQ0-XjkBA8j6NQy-xAsRvYpBrCSc6xQpHMn0TooBEQtJq56saObx9PEgBzW3s1Hx6pN7Ms"
			+ "m5e5EExflUlDLF-9WNgL9oKXhKUKnp7ygsR34gMoQ9kyMShVC_t6duUkBzZJeaDCAxqEMGog5ur8fNyP"
			+ "_Mv5wUyWPMDUya7Um5TqJ0DXolWKSBjvCgMABHYxThh3mXOWtERYYbK1SFT2TIujAxUmAMRTS_KnK2b-"
			+ "Z4zWgCmBpmhiEEy5EWCzIgoFvhtIvscvDmLR2oXJBGGBQQAShHGKvMkHZ5ZhqdFG1dZxou8BzDQUet6a"
			+ "G_5h8ReZRG2qPb6DqEUw0Pa8bN95l-PZOGJyxQ-ltODH1n__EePFnK6yf2G6f0BQqOtpe7wwferWlKMV"
			+ "Vz-sgu_Wpx_cqH2dQhltjmRHwo38Vu62S3fNp-0mAlDdu0FZT2KzeDJ1IgsyD7eQO4GGqhAUI_kSaHuP"
			+ "K-NsrI9th0yL_sqhnWdAUCFAYqM2eiMIitoxMYPL8FRrEnQd73B-JrHv9f9BLqv68XGxEyIPZVVHXk7c"
			+ "c-36JCSL0crVVxURrQaokv2Srt0WCW00";

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
