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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Newpage;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.Style;

public class SequenceDiagramFileMakerPuma2 implements FileMaker {

	private final SequenceDiagram diagram;
	private final DrawableSet drawableSet;
	private final XDimension2D fullDimension;
	private final List<Page> pages;
	private final StringBounder stringBounder;

	public SequenceDiagramFileMakerPuma2(SequenceDiagram diagram, Rose skin, FileFormatOption fileFormatOption) {
		this.diagram = diagram;
		this.stringBounder = fileFormatOption.getDefaultStringBounder(diagram.getSkinParam(), diagram.getPragma());
		final DrawableSetInitializer initializer = new DrawableSetInitializer(skin, diagram.getSkinParam(),
				diagram.isShowFootbox(), /* diagram.getAutonewpage(), */ diagram.getCounter());

		for (Participant p : diagram.participants())
			initializer.addParticipant(p, diagram.getEnglober(p));

		for (Event ev : diagram.events()) {
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
		drawableSet = initializer.createDrawableSet(stringBounder);
		final List<Newpage> newpages = new ArrayList<>();
		for (Event ev : drawableSet.getAllEvents())
			if (ev instanceof Newpage)
				newpages.add((Newpage) ev);

		fullDimension = drawableSet.getDimension();
		final Map<Newpage, Double> positions = new LinkedHashMap<Newpage, Double>();
		for (Newpage n : newpages)
			positions.put(n, initializer.getYposition(stringBounder, n));

		pages = create(drawableSet, positions, diagram.isShowFootbox(), diagram.getTitle().getDisplay()).getPages();
	}

	public int getNbPages() {
		return pages.size();
	}

	private PageSplitter create(DrawableSet drawableSet, Map<Newpage, Double> positions, boolean showFootbox,
			Display title) {

		final double headerHeight = drawableSet.getHeadHeight(stringBounder);
		final double tailHeight = drawableSet.getTailHeight(stringBounder, showFootbox);
		final double signatureHeight = 0;
		final double newpageHeight = drawableSet.getSkin()
				.createComponentNewPage(
						new Style[] { ComponentType.NEWPAGE.getStyleSignature()
								.getMergedStyle(drawableSet.getSkinParam().getCurrentStyleBuilder()) },
						drawableSet.getSkinParam())
				.getPreferredHeight(stringBounder);

		return new PageSplitter(fullDimension.getHeight(), headerHeight, positions, tailHeight, signatureHeight,
				newpageHeight, title);
	}

	@Override
	public TextBlock getTextBlock(int num, FileFormatOption fileFormat) {
		final Page page = pages.get(num);

		double delta = 0;
		if (num > 0)
			delta = page.getNewpage1() - page.getHeaderHeight();

		if (delta < 0)
			delta = 0;

		return drawableSet.asTextBlock(delta, fullDimension.getWidth(), page, diagram.isShowFootbox());
	}

}
