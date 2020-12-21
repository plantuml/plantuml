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

	public static final String DONORS = "6wyC0AmFkBap0wp4H6CjU-RENbb-8cPf4i7oFA4uiOZJNK3y0UwT7lSRwI-YAxltJJEYrckJSWPHVH3q"
			+ "W7UtXVQXzBy-27EYV0Z-tGWU80D4ITH6ZIpt9Djjcof3Il3lEr_v9UWTth51ehbAgiMHwq-_8GFlEsFp"
			+ "x3I-UFLeWZO2FmRsrVcVQ_NQ7kb2GV9veXHAkanefoODi_-N3j9O0j_0itRHbhPsqaY72koem6DV-ULh"
			+ "vyEZoQeOMOeKGIka8D0RCmHoqdk3RFH1uPVw8RJPN64fovb076seHzVNQnFRHWpVAF61m0sn81E1EP1o"
			+ "p2oYo7FJZG9N10w-MSp6MagEF9c4O1vQo9i_r9CIZvHdJeUB0iyZG23mD0gGj4O1JJXRSXYGEP8j4fQu"
			+ "wi9nOmwFm_AKUYGDdoOMF1RK0jGGv7_G1PqMQtmqETlLO4lgQP9xjqxKdn6FSsrMz5MN9WdpjVRIR2Ak"
			+ "CMM9L6CRdwEzvX60kwLMZlWo-XZa2EhzvbM8rOyMa1-17ge9I_9dsGyMeY2gRTY7tdLsdpnc4Gx8a6bY"
			+ "7RoBBQ-fN-WITLQGP0LOvE_ChmkRu84jT2NaOU09BNCkwtm2avPMxnSz6jRk4wunYzshuvxeQ5XA__sg"
			+ "3pE3uW0jb9W5tMbWK4RJAbvi80AxXHuFk2cGAw7SNPTYzt6HFl8J06zIUj0LDaY24CylIcuwX8Y6TEr9"
			+ "iGAOU4gfYmBV0WjsROAkHfL-UbcjzZXTBTENZGdtIHiyhpKOGt5CHwalp7Ul2hSmDAj6itgpzYxc55p9"
			+ "nq5UzPx5kGhWqiAxueDh_f41UQPNHaGQJLVVjk4VcPjL66QXvzoiIdTLFxXvwF21krZKnOmoUSXKq7YG"
			+ "Y9hwbU2d9SFVOiYg_DMDyWvMf-km46P6FBYzDBcW9aSKkAQp71OR1CognIjEYONh2TQmw6Isi2aYprZT"
			+ "3ndoE-2Ji5Gp2qq0kuQx0C_8FGhauRn7LYshroPqBY17DXFqfGLJBOMAzBL01emrwUcqHcn_Mc01MzfZ"
			+ "TAnmA7pPH6kL6nIO0gQRa7xX81Bb3T4IlQX98e3_-NENUlAv3kBupXW-6GUIFIGXe0f5TzY9qbEw7js6"
			+ "iLwZxzTBkVa8_lnhZpOv1UsUdoOGhOFayS-401pbAFJWQU2loISdxrvOanMSYmnNBbPwF92mO1ZlxA8k"
			+ "NcR4xiHakPfSj8hyKERVubguc1nYGugBVb3HOab9BpUhGIMsVVXTXuiSAFubgfH4SbchpcGa-0HcSUwE"
			+ "VRLXgDig6X11TLGce7VNRwTPxXfpla9Iqt0PaE-toAcaHMZ1cfhQ-qAWMupzlIdxAK-B_sH4Fx7MwkEg"
			+ "7W87ERhSBgBXcn58EwOdCLmat110lppdX0TklkaXBwe5fM5ftGmxJh12VeMNy4B27jluW2BBgpIht923"
			+ "RY5xeHge3oYg2Hx8mA93roGMb78y84LIOCxPCZABb8CsuBcU0N2A0llxUmt2gHFDBsZMtfvHIEPYpCru"
			+ "1X4VpiyWOPCfcnyvi2_BNBPwHMwY5kcm0z-ExpXV_uYwUX218X1bIV2YBjBXndopD6mgvZTrtCUbDcyc"
			+ "ty2bQEkKWUqoazq0cm4bdWH0xA2WGiKhylDyOCivYVnxlp4LDcl2KsOyim00";

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
		final ImageParameter imageParameter = new ImageParameter(new ColorMapperIdentity(), false, null, 1.0,
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
