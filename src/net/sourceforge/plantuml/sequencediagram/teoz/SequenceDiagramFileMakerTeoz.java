/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 9591 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.sequencediagram.graphic.FileMaker;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGraphic2;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class SequenceDiagramFileMakerTeoz implements FileMaker {

	private final SequenceDiagram diagram;
	private final FileFormatOption fileFormatOption;
	private final Skin skin;

	public SequenceDiagramFileMakerTeoz(SequenceDiagram sequenceDiagram, Skin skin, FileFormatOption fileFormatOption) {
		this.diagram = sequenceDiagram;
		this.fileFormatOption = fileFormatOption;
		this.skin = skin;

	}

	public ImageData createOne(OutputStream os, int index, boolean isWithMetadata) throws IOException {
		StringBounder stringBounder = TextBlockUtils.getDummyStringBounder();

		final ISkinParam skinParam = diagram.getSkinParam();

		final Real origin = RealUtils.createOrigin();
		Real currentPos = origin.addAtLeast(0);
		double headHeight = 0;
		LivingSpace last = null;
		LivingSpaces livingSpaces = new LivingSpaces();
		for (Participant p : diagram.participants().values()) {
			final LivingSpace livingSpace = new LivingSpace(p, diagram.getEnglober(p), skin, skinParam, currentPos,
					diagram.events());
			last = livingSpace;
			((LivingSpaces) livingSpaces).put(p, livingSpace);
			final Dimension2D headDim = livingSpace.getHeadPreferredDimension(stringBounder);
			currentPos = livingSpace.getPosD(stringBounder).addAtLeast(0);
			headHeight = Math.max(headHeight, headDim.getHeight());
		}
		// livingSpaces = new LivingSpacesDeltaSimple(livingSpaces);

		final MainTile mainTile = new MainTile(diagram, skin, last.getPosD(stringBounder).addAtLeast(0), livingSpaces,
				origin);
		mainTile.addConstraints(stringBounder);
		origin.compile();

		final double height = mainTile.getPreferredHeight(stringBounder) + 2 * headHeight;

		final Real min1 = mainTile.getMinX(stringBounder);
		final Real max1 = mainTile.getMaxX(stringBounder);
		// System.err.println("min1=" + min1.getCurrentValue());
		// System.err.println("max1=" + max1.getCurrentValue());

		final Dimension2D dim = new Dimension2DDouble(max1.getCurrentValue() - min1.getCurrentValue(), height);
		final UGraphic2 ug = (UGraphic2) fileFormatOption.createUGraphic(dim).apply(
				new UTranslate(-min1.getCurrentValue(), 0));
		stringBounder = ug.getStringBounder();

		final Context2D context = new SimpleContext2D(false);
		drawHeads(ug, livingSpaces, context);
		// mainTile.beforeDrawing(ug.getStringBounder(), livingSpaces.values());
		mainTile.drawU(ug.apply(new UTranslate(0, headHeight)));
		drawLifeLines(ug.apply(new UTranslate(0, headHeight)), mainTile.getPreferredHeight(stringBounder),
				livingSpaces, context);
		drawHeads(ug.apply(new UTranslate(0, mainTile.getPreferredHeight(stringBounder) + headHeight)), livingSpaces,
				context);
		mainTile.drawForeground(ug.apply(new UTranslate(0, headHeight)));

		ug.writeImageTOBEMOVED(os, isWithMetadata ? diagram.getMetadata() : null, diagram.getDpi(fileFormatOption));
		final Dimension2D info = new Dimension2DDouble(dim.getWidth(), dim.getHeight());

		// if (fileFormatOption.getFileFormat() == FileFormat.PNG && ug instanceof UGraphicG2d) {
		// final Set<Url> urls = ((UGraphicG2d) ug).getAllUrlsEncountered();
		// if (urls.size() > 0) {
		// if (scale == 0) {
		// throw new IllegalStateException();
		// }
		// final CMapData cmap = CMapData.cmapString(urls, scale);
		// return new ImageDataComplex(info, cmap, null);
		// }
		// }
		return new ImageDataSimple(info);
	}

	private void drawLifeLines(final UGraphic ug, double height, LivingSpaces livingSpaces, Context2D context) {
		int i = 0;
		for (LivingSpace livingSpace : livingSpaces.values()) {
			// if (i++ == 0) {
			// System.err.println("TEMPORARY SKIPPING OTHERS");
			// continue;
			// }
			// System.err.println("drawing lines " + livingSpace);
			final double x = livingSpace.getPosC(ug.getStringBounder()).getCurrentValue();
			livingSpace.drawLineAndLiveBoxes(ug.apply(new UTranslate(x, 0)), height, context);
		}
	}

	private void drawHeads(final UGraphic ug, LivingSpaces livingSpaces, Context2D context) {
		for (LivingSpace livingSpace : livingSpaces.values()) {
			// System.err.println("drawing heads " + livingSpace);
			final double x = livingSpace.getPosB().getCurrentValue();
			livingSpace.drawHead(ug.apply(new UTranslate(x, 0)), context);
		}
	}

	public int getNbPages() {
		return 1;
	}

}
