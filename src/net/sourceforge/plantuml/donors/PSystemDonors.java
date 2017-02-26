/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.code.TranscoderImpl;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.DiagramDescriptionImpl;
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

	public static final String DONORS = "UDfbL4josp0CtUCKN6lIGoXsx5J-96EdwKnsi0HRZ2d29IMdyZbwbImwqrqlu2CLSYkJIhaoJ01y3tXFgKd-7NBmI3KvbTgQhSOSdYW9XokOSmYTXxGlnGrCNRYY5jQqGLzhj6h9fONK5kpKbAa-KVTpkvMCFXqjp35woDOmrdsW3QIXDHzXtGG8BKnrCZoM8UjHDXxMDHrAich4V11-u_uTyZ2hFfIoWn4RRTMA6q-GDH_karSM8drmqB28K6CLX_9KQkWxsaB6EIgrTDsXQuNfwahRxc0PewzFy63qKGh6EjyBykkDjd3dhwrj12AX8unFbQvTg-POSe6GTKD_AL7QHta9cR0kEVjMncInbZp19xYol-1fFs1b2mbTBZEPuCQrUtxpUosDZ9Gzk5gVu3va5mmBdPTar2rgqyOCp2a6upraXFPJXlctXhmQDnk7iaiJ-3xpQqrvgMOEFGP8EnA9HXjxwvGbAMh6HnqBvIALBu6mHbX5uksUY7achr9zY6J5-suoj-pGeZdSg1UgaGyULa8GNQ5ckbijJ0oxRjwBtx-CkImz2C94uXGvpQzoSjXFkzjwEMJauH5xUQpq071gj5eZ5c8TjoFrbUoEt8rQf4-VrscGTi8MfZImJCpSvYAHIZqBwh68TXOo7NMR5RFCYU3k62l_YB0CJQZgGIOHZMcZHHTDHMh23TLXlhc7IRoP2vUW1dkAnoPmbted6bhhAbXG08cJjzsaKLmTWKwCWVEFytlK_ln-_WcZSc3jLepNR2uV0cV52_SyW3CbtcuTjMgfArbSXlz_LLRN_yGZVm26X5oY";

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat)
			throws IOException {
		final UDrawable result = getGraphicStrings();
		final ImageBuilder imageBuilder = new ImageBuilder(new ColorMapperIdentity(), 1.0, HtmlColorUtils.WHITE,
				getMetadata(), null, 0, 0, null, false);
		imageBuilder.setUDrawable(result);
		return imageBuilder.writeImageTOBEMOVED(fileFormat, os);
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
		final Transcoder t = new TranscoderImpl();
		final String s = t.decode(DONORS).replace('*', '.');
		final StringTokenizer st = new StringTokenizer(s, "\n");
		while (st.hasMoreTokens()) {
			lines.add(st.nextToken());
		}
		return lines;
	}

	public DiagramDescription getDescription() {
		return new DiagramDescriptionImpl("(Donors)", getClass());
	}

	public static PSystemDonors create() {
		return new PSystemDonors();
	}

}
