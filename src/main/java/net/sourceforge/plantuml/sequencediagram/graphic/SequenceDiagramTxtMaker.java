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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 4935 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.asciiart.TextSkin;
import net.sourceforge.plantuml.asciiart.TextStringBounder;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.ugraphic.txt.UGraphicTxt;

public class SequenceDiagramTxtMaker implements FileMaker {

	private final SequenceDiagram diagram;
	private final DrawableSet drawableSet;
	private final Dimension2D fullDimension;
	private final StringBounder dummyStringBounder = new TextStringBounder();
	private final UGraphicTxt ug = new UGraphicTxt();
	private final FileFormat fileFormat;
	private final Skin skin;

	public SequenceDiagramTxtMaker(SequenceDiagram sequenceDiagram, FileFormat fileFormat) {
		this.fileFormat = fileFormat;
		this.diagram = sequenceDiagram;
		this.skin = new TextSkin(fileFormat);

		final DrawableSetInitializer initializer = new DrawableSetInitializer(skin, sequenceDiagram.getSkinParam(),
				sequenceDiagram.isShowFootbox(), sequenceDiagram.getAutonewpage());

		for (Participant p : sequenceDiagram.participants().values()) {
			initializer.addParticipant(p, null);
		}
		for (Event ev : sequenceDiagram.events()) {
			initializer.addEvent(ev);
//			if (ev instanceof Message) {
//				// TODO mieux faire
//				final Message m = (Message) ev;
//				for (LifeEvent lifeEvent : m.getLiveEvents()) {
//					if (lifeEvent.getType() == LifeEventType.DESTROY
//					/*
//					 * || lifeEvent.getType() == LifeEventType.CREATE
//					 */) {
//						initializer.addEvent(lifeEvent);
//					}
//				}
//			}
		}
		drawableSet = initializer.createDrawableSet(dummyStringBounder);
		// final List<Newpage> newpages = new ArrayList<Newpage>();
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
		//drawableSet.drawU_REMOVEDME_4243(ug, 0, fullDimension.getWidth(), page, diagram.isShowFootbox());
		drawableSet.drawU22(ug, 0, fullDimension.getWidth(), page, diagram.isShowFootbox());
	}


	public ImageData createOne(OutputStream os, int index, boolean isWithMetadata) throws IOException {
		if (fileFormat == FileFormat.UTXT) {
			final PrintStream ps = new PrintStream(os, true, "UTF-8");
			ug.getCharArea().print(ps);
		} else {
			final PrintStream ps = new PrintStream(os);
			ug.getCharArea().print(ps);
		}
		return new ImageDataSimple(1, 1);
	}

	public int getNbPages() {
		return 1;
	}
}
