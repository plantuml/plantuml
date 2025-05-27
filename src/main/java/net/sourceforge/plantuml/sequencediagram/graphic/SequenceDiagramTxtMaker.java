/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
package net.sourceforge.plantuml.sequencediagram.graphic;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.asciiart.TextSkin;
import net.sourceforge.plantuml.asciiart.TextStringBounder;
import net.sourceforge.plantuml.asciiart.UmlCharArea;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.drawing.txt.UGraphicTxt;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.security.SecurityUtils;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;

public class SequenceDiagramTxtMaker implements FileMaker {
	// ::remove file when __CORE__

	private final SequenceDiagram diagram;
	private final DrawableSet drawableSet;
	private final XDimension2D fullDimension;
	private final StringBounder dummyStringBounder = new TextStringBounder();
	private final UGraphicTxt ug = new UGraphicTxt();
	private final FileFormat fileFormat;
	private final TextSkin skin;

	public SequenceDiagramTxtMaker(SequenceDiagram sequenceDiagram, FileFormat fileFormat) {
		this.fileFormat = fileFormat;
		this.diagram = sequenceDiagram;
		this.skin = new TextSkin(fileFormat);

		final DrawableSetInitializer initializer = new DrawableSetInitializer(skin, sequenceDiagram.getSkinParam(),
				sequenceDiagram.isShowFootbox(), /*sequenceDiagram.getAutonewpage(),*/ sequenceDiagram.getCounter());

		for (Participant p : sequenceDiagram.participants()) {
			initializer.addParticipant(p, null);
		}
		for (Event ev : sequenceDiagram.events()) {
			initializer.addEvent(ev);
			// if (ev instanceof Message) {
			// // TODO mieux faire
			// final Message m = (Message) ev;
			// for (LifeEvent lifeEvent : m.getLiveEvents()) {
			// if (lifeEvent.getType() == LifeEventType.DESTROY
			// /*
			// * || lifeEvent.getType() == LifeEventType.CREATE
			// */) {
			// initializer.addEvent(lifeEvent);
			// }
			// }
			// }
		}
		drawableSet = initializer.createDrawableSet(dummyStringBounder);
		// final List<Newpage> newpages = new ArrayList<>();
		// for (Event ev : drawableSet.getAllEvents()) {
		// if (ev instanceof Newpage) {
		// newpages.add((Newpage) ev);
		// }
		// }
		fullDimension = drawableSet.getDimension();
		final double headerHeight = drawableSet.getHeadHeight(dummyStringBounder);
		final double tailHeight = drawableSet.getTailHeight(dummyStringBounder, diagram.isShowFootbox());
		final double newpage2 = fullDimension.getHeight() - (diagram.isShowFootbox() ? tailHeight : 0) - headerHeight;
		final Page page = new Page(headerHeight, 0, newpage2, tailHeight, 0, null);
		// drawableSet.drawU_REMOVEDME_4243(ug, 0, fullDimension.getWidth(), page,
		// diagram.isShowFootbox());

		final Display title = diagram.getTitle().getDisplay();

		final UGraphicTxt ug2;
		if (title.isWhite()) {
			ug2 = ug;
		} else {
			ug2 = (UGraphicTxt) ug.apply(UTranslate.dy(title.asList().size() + 1));
		}
		drawableSet.drawU22(ug2, 0, fullDimension.getWidth(), page, diagram.isShowFootbox());
		if (title.isWhite() == false) {
			final int widthTitle = StringUtils.getWcWidth(title);
			final UmlCharArea charArea = ug.getCharArea();
			charArea.drawStringsLRSimple(title.asList(), (int) ((fullDimension.getWidth() - widthTitle) / 2), 0);
		}

	}

	public ImageData createOne(OutputStream os, int index, boolean isWithMetadata) throws IOException {
		if (fileFormat == FileFormat.UTXT) {
			final PrintStream ps = SecurityUtils.createPrintStream(os, true, UTF_8);
			ug.getCharArea().print(ps);
		} else {
			final PrintStream ps = SecurityUtils.createPrintStream(os);
			ug.getCharArea().print(ps);
		}
		return new ImageDataSimple(1, 1);
	}

	public int getNbPages() {
		return 1;
	}

	@Override
	public void createOneGraphic(UGraphic ug) {
		throw new UnsupportedOperationException();
	}

}
