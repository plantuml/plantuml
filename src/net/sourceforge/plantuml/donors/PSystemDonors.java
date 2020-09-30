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
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.AffineTransformType;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.PixelImage;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;
import net.sourceforge.plantuml.version.PSystemVersion;

public class PSystemDonors extends AbstractPSystem {

	private static final int COLS = 6;
	private static final int FREE_LINES = 6;

	public static final String DONORS = "6rqC0AmEU9ELAujmujswMQFgmN9H8OBbUK9n65Z4qzs0-0FSEptkDz9VH5UwzqqpeYsRKvfd5TM2eE5u"
			+ "ctROvizU27Dcl0Zkp17qVFW0KHArbH-f8zkv1sAfPFqs5TCdNSg6oFYiDq8te7FUXlUTiNbs6b-yUZH1"
			+ "6u4V0_lg_owThRjNLM6MxbQj4JNgT8KRcxdN_r45TKJ1Jb2Wfn_JgHogmw6A2koem6DV-LKtpaUD9gjY"
			+ "GYbIdBKH0gSOpD2Gazudo2zzKFYXta2RrkeQOha7AOwPqgFhiTgEVGV3BvJuaB05Nv1PO4vAEKmNA7vk"
			+ "kg5Wuu53tIfcOqjAZZpHG-255lBc3qLIqdg8PvN1HS6kX1GuFOm2aj6eGHFE3Xm6LDo3BJeqSDNbVcDk"
			+ "deVbgNWc3JU2XPq5jO0QWlmFUa6dzvMtJEvpKzWIyZQ9LxULg3zBtaxThEWRNPe4pDVMKpEjNrdAraCQ"
			+ "Mzxd_AkpaxnfoChP7qOVgHn1lH_caZ6_jK3j27JVcB0alhzqFrX8GAYsr9lUTQu-ARC89sH8gLWdRsBB"
			+ "Q_4_TFaw6n39sS4IVjVzN6wA1rleogIOz8PMSQfrFf2J6khlBtarTRuBhYaMkpUFUw9JeI7wVzSjpGp5"
			+ "0Jemn2pe6e4XpAPLUx6127OBdPta9a0se7oPtFANvPVe8ouaU7E9e2jif3I2Fx-4tENWGdIawveKAu3P"
			+ "KqgzA3YSOA5SXw2RKIMPNkQkds7IicpPcBbFsk1vgy4O-J6TK5oOR-O2zp0iLelc-6xjGwmIFCh7IJvr"
			+ "d_sv2d1fuJtYnhL-oO2VOSw8I9dfYdrFkyNSrM6OHPrpLHVTm-tavg6R5vp5ebdCQ9E7B0QDKycegOy8"
			+ "RMWm_Es7RCnUgxDQ1vl3TKfmCoCDhYzDRgZ94GMRvIp7p1O0bx7un2w_S0d16fQQoDQrXCWHgySXGtud"
			+ "sWIMohJGD63dS5VWcR17GHpEUs3LgRvDoSu5L17J14bfmOmPakLXQu4Cp3NfcMa3x7PfnuAopKzs774e"
			+ "lcsYhSOD4Ko1wIRS1mv4OhWXAT5BJ181-FvSdb8lFDV1ndVrj68S92qaCL15k3lb1wMdxhg5fh5Uel_7"
			+ "MZiiEt_-lOviScgqUzrD43q7SG68y08DvNuXWhS1xuCDtO03Ld3HamXNhbfw6aXOi9dMsKNTlyoetF5Y"
			+ "kJ6vQENveOf_kYX7WTE-8SL5Cba--n8bhHtgGUMtVVCNYuiS1FyILOPaSbKRhsGaT09cSMx7hZCLTTj8"
			+ "0nQCZie4wfvwUZvCuoLZhbSeDGqF0_bkYmm9UWyjhAbfULm8h9RE_jx2DiJJ-xzWH3-_rl7pgXuQJc9q"
			+ "VRj0mxScf75E91WkaWy4oDLpJZHevfi4yg9QA0DBssROMOCLyXlu4Lv8E68R7mXocelcR0Too1Qo3un6"
			+ "rWVoQa83MLYiq6iI2wKS3wH82kQSBUDPf9sq0Njw1D0e2iR_xoq85myqFoEpzddrHZ2NPkx6CuXqk7y6"
			+ "YKsdRFN8XlrGvhBDAt8xnY8STkA_uplEjp-YRfv440d4K1AyLXFIuSOErIOsGyzr6jp7PMxSJ7u1bwAT"
			+ "9ndO6eejo58s0oMU1000";

	/*
	 * Special thanks to our sponsors and donors:
	 * 
	 * - Noam Tamim
	 */

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat, long seed)
			throws IOException {
		final UDrawable result = getGraphicStrings();
		final ImageBuilder imageBuilder = ImageBuilder.buildA(new ColorMapperIdentity(), false, null, getMetadata(),
				null, 1.0, HColorUtils.WHITE);
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
