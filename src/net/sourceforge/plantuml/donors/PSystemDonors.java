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

	public static final String DONORS = "UDfTL4josp0CtUCKN6lIGoXsxDQ_PEmqdSaEbc2BDaMu9ELKFaUlaaLdkki5VAH2RcNIuKeO0k1x3m_AJVR_a8Cd2kHKRWDRZGKyKnOFrp3Z63eFUL-AQnYxU4Kjh6YDFcYqQi6LXToMxDIOAPoe-zni9ADFHmipJ1wo0OQw3xI1FBRc8woQ2A65iSvkZsMOz2WR3wj0Xugi6Z0VXD-mVuSyJEiFfUnWm4PRjUJ64qoQ3_VPZeL87nmqp1FKMAUXF9KR-e6s53a7bPQkMtIjC5rUQTijB5BquGIFHX-bOAYBlI2_tcWB3_xQsYOWCZh2y5Jhu5ermufBX4ad-ck5qZx9oyZ4SSdPjoAJnI1vWazmJVw1f_qDArjAw0hHP8Hhr-xvpU-rD39IzkY2Fi7dc5qop7LHaL7tg4sRCZ2d58ptC26qdoPOV6_8gs6pTYYpD97l2-y25PMQElGO8Mr9B9fCxArpbgIeAHzrQfIBLRv6mXfXcPYtUoBbcUsa-f3OYlUTClViqA8vtAbN2iW73qiXYAvKKzsD5aQ6NQVt_CzlOov3ZuOmYJd5JhEhdHpsQdTJhsuPUNZ2tXvBVGCuDrgj44jP7RSbzOtibjoTckVFNrPvj7R65iPqip8fSrk8HMhr8gY7Ojffp7JKJLQMPKO43yVK-KU4HMn2TGyoIsYC6os-Q6fI8sueH7qB3wDqCXSkqGtsbCecS1TwAnvQwJgke02IJTvqIgDiTG8wCmREFy_lIVlp-_aN3AgRrMvbyPhrvKVWhEp2Nu66JIy0";

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
		final List<TextBlock> cols = getCols(getDonors(), 3, 5);
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
