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
	public static final String DONORS = "6v8802mEp3EZtv0iGcz-fvJYI4HCdvyvFoLZ70fX42Pji_Rj7u5_xF8MOOgGwCb2hYywcqt8cZ7fvZdB"
			+ "UKOHB9z6h9SwR7EpNwh8AC6pcc_-6SNudZjfQ4BQdR1i1_B_wLu53WLaSJR5GnWEsFmBC_QWEUnUABLh"
			+ "wMBt2ze2ZMdqOyjAyTtoFPIoRgV0R9HytwmWOV4FnnLLiXJZBf5YgXDROEAE6SdMwKxm3RVRVzHDQj7Q"
			+ "ceQ4Zm1Y6obiROzzBIuBEJw5WH74e0umG8G3AOVw8ATuUy41HAyQCUApcCPwgtUUA4321XLQy8-SLESM"
			+ "iZpBa0bYS3CGCFaEd03iJKabcEtS1kHfWHP53PRgrdKd8ZFU34y9MJHSHGuOHD0Ag4AGDq47-2vjorkC"
			+ "nvWLCVpHFHWVtX3aJHQUI2yM-dhhmK3usLqrazNjJcrg8SGbxtMSjJR0iuQC7EsXzxXaCnxhrrmAmsyj"
			+ "G7uBz5BlY1X_Szqi945mBgatd7ME7D-c28Gsbef5xcBAQy8PNNQsfgZlBLYOb_phrg4immXTgRW5y0ij"
			+ "ZvDrlb3SDDH_BxaQEdu5rdWqt5k1ijFcOlvxsItDZD83jDc5pT1L4U6USMXLA2P31AuG-ZxW8KdEfDh7"
			+ "v57zMBx4F_Od8Rn5uO2LDZFQdrLN98eSLa4Mb3lQn08mtIJQ0M9q9s44r0NTXYhdy0nttRi9UTfcO-hQ"
			+ "eOPZDy8OrBVf9FaWOZUNZ9A67bas2ukr7t2Mu1mkajVFFHor2c0zqhlhPcc-mK2yXJdT8js6vML9koMY"
			+ "g2D4bjCMh-hYh-wsykUXCtyuvx5ew8tpJvvVXun7yoLzQQb7bBbhqFtj25dUpaXHPjWEr-503MHy2hXU"
			+ "DNccaYCGlUaCXqPi967M1w-T1OTm1nVHMATiBqrBaAtdwcEW6Vo8tCcGIZrm9n7hk0jmXl4eij-Ozyge"
			+ "k_eyf4Gje60C4vobGjX7I54DsxOWGyRuwHcCJhxoeIqMy6dioSmw53vXebt31ZQc48iMzOEu2S3rtjUe"
			+ "bpGcIi3NlFocUioiWvu_iwKLuW1EAGa1g63uy1rdfqgRhMcij-Zzs3HzBHj__MUkCi4f5VkyMu1K1p7v"
			+ "BuSWCX7VbTqZy5daOudHHaRWM2lRMBcqz3Z0i22JbXbgcfyPyUbQADVN1ZHhVIZFVvHLdh6dvmK0";

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
		final List<TextBlock> cols = getCols(getDonors(), 7, 5);
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
