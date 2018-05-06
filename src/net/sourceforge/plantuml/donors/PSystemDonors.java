/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
	public static final String DONORS = "6rO702mEpFCVHV0StDQFZQ4KYXIQJO-ICGuxFM7I4cYu9-jpVDvE66nDSPvH5Fk0vi0crmphPDt6pjY2"
			+ "uBcYk88Aa1TYgjX-ouJsTXreG_9MC8x0dygP_yi5DYP8xwonAVBmEA0r5cwBnhJ_ZygwwBzeW4wmQLIs"
			+ "B2eDyYodLy4aXy6fi-EKKF9GZEahKfJronQWb8u5cMrdTOGVP9jzenQfIMis3PbP0ehBI95eFZPQsNTo"
			+ "-rx62I1QVZzWAy4E9PAR732Ml16Z8zC3WjGeRiO6QjvvmeC93LHemnSbgQaj93BAm417xXueeFHdd83O"
			+ "9sMKv6p3su3yBR95HOEWZkytflF7XsKfUtvfE4OUa7FG2g02o7-W6_wqDdE9SHcR0ZFaNHZ7ib78QwgN"
			+ "KTuyqDTQ5mRWPV9aHk-N7stV5j8vvJh7hyu6UTMGbKTx66kySWdk-SzS2ksB1LG_WBRjEn3Z_SFvpK9t"
			+ "28yfTI6LS_pE_IO2eXjBOQ4tSSbgzfztwHP3HVDepc8ySh-EjnA17LnNucxeSYr_YZedb6mQwjTBNXqw"
			+ "hYBN-DNcVObUaqr3_7-jbZA3T0UjxiA6ws8493WMLkNYeel0OT2_8RSakP7Q3fD7_R1yYP_t9yczroA1"
			+ "A6mws3_LP-0cDxim6xfqOXCF0AysYNQZe_0WCmYh0nq7bMTas5t_kqdFiAanQTxfre5XDuRwHxgxSZDz"
			+ "CvTq2qE_B9hjkzep70NSYXFAuNUr_hOAPN89xwP5Q7oImVejp1c1z1bHzZrjoNHJ1ymiZmMlwkBltUta"
			+ "pqFd_xwzMzHmTFvxjtzqVPl9p-RKKeyeQzTWsDlWiZPhuqLr0MrBvNcWEpCk0gvN9XigkOMEp72MGyoJ"
			+ "95JsWuDZwC5DdQ1jFR8pDIrXRZrMoS2Mv8VKdJGgjGDFsETsU-SPeqM1_SbvErOzrBSXnTy558aXmLCg"
			+ "m7s45FMmEPrCDywsjBkJxtfhymN3DQfnuKw3meybchJSOF4fm8oT_80qdff8j97NSIPA0B_Zz1lq3DdF" + "hgxRk8wMLW00";

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
		final List<TextBlock> cols = getCols(getDonors(), 4, 5);
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
