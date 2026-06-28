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
package net.sourceforge.plantuml.sequencediagram.teoz;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.annotation.Fast;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealOrigin;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.sequencediagram.graphic.FileMaker;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ISkinParam;

public class SequenceDiagramFileMakerTeoz implements FileMaker {

	private final SequenceDiagram diagram;

	private final Rose skin;
	private Dolls dolls;
	private final StringBounder stringBounder;

	private final PlayingSpaceWithParticipants body;

	private final Real min1;

	private final LivingSpaces livingSpaces = new LivingSpaces();
	private final double heightEnglober1;
	private final double heightEnglober2;

	public SequenceDiagramFileMakerTeoz(SequenceDiagram diagram, Rose skin, FileFormatOption fileFormatOption,
			int index) {
		this.stringBounder = fileFormatOption.getDefaultStringBounder(diagram.getSkinParam(), diagram.getPragma());
		this.diagram = diagram;
		this.skin = skin;
		this.body = new PlayingSpaceWithParticipants(createMainTile());

		this.min1 = body.getMinX(stringBounder);

		this.heightEnglober1 = dolls.getOffsetForEnglobers(stringBounder);
		this.heightEnglober2 = heightEnglober1 == 0 ? 0 : 10;
	}

	private PlayingSpace createMainTile() {
		final RealOrigin xorigin = RealUtils.createOrigin();
		Real xcurrent = xorigin.addAtLeast(0);
		final RealOrigin yorigin = RealUtils.createOrigin();
		for (Participant p : diagram.participants()) {
			final LivingSpace livingSpace = new LivingSpace(p, diagram.getEnglober(p), skin, getSkinParam(), xcurrent,
					diagram.events());
			livingSpaces.put(p, livingSpace);
			xcurrent = livingSpace.getPosD(stringBounder).addAtLeast(0);
		}

		final TileArguments tileArguments = new TileArguments(stringBounder, livingSpaces, skin, diagram.getSkinParam(),
				xorigin, yorigin);

		this.dolls = new Dolls(tileArguments);
		final PlayingSpace mainTile = new PlayingSpace(diagram, dolls, tileArguments);
		this.livingSpaces.addConstraints(stringBounder);
		mainTile.addConstraints();
		this.dolls.addConstraints(stringBounder);
		xorigin.compileNow();
		if (YGauge.USE_ME)
			System.err.println("COMPILING Y");
		yorigin.compileNow();
		tileArguments.setBordered(mainTile);
		return mainTile;
	}

	public ISkinParam getSkinParam() {
		return diagram.getSkinParam();
	}

	public int getNbPages() {
		return body.getNbPages();
	}

	@Override
	public TextBlock getTextBlock(int num, FileFormatOption fileFormat) {
		return new TextBlock() {

			public void drawU(UGraphic ug) {
				ug = ug.apply(new UTranslate(5, 5));

				body.setIndex(num);
				final UTranslate min1translate = UTranslate.dx(-min1.getCurrentValue());
				ug = ug.apply(min1translate);

				dolls.drawEnglobers(ug,
						body.calculateDimension(stringBounder).getHeight() + heightEnglober1 + heightEnglober2 / 2,
						new SimpleContext2D(true));

				ug = ug.apply(UTranslate.dy(heightEnglober1));
				body.drawU(ug);
				ug = ug.apply(UTranslate.dy(body.calculateDimension(stringBounder).getHeight()));
				ug = ug.apply(UTranslate.dy(heightEnglober2));
			}

			@Fast
			@Override
			public XDimension2D calculateDimension(StringBounder stringBounder) {
				final double totalWidth = body.calculateDimension(stringBounder).getWidth();
				final double totalHeight = body.calculateDimension(stringBounder).getHeight() + heightEnglober1
						+ heightEnglober2;
				return new XDimension2D(totalWidth + 10, totalHeight + 10);
			}

			public HColor getBackcolor() {
				return null;
			}

		};
	}

}
