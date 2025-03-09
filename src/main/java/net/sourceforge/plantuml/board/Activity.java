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
package net.sourceforge.plantuml.board;

import java.util.concurrent.atomic.AtomicInteger;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.style.ISkinParam;

public class Activity {

	private final BNode node;
	private final ISkinParam skinParam;
	private BNode cursor;

	private final BoardDiagram boardDiagram;

	public Activity(BoardDiagram boardDiagram, String name, ISkinParam skinParam) {
		this.boardDiagram = boardDiagram;
		this.node = new BNode(0, name);
		this.skinParam = skinParam;
		this.cursor = this.node;
	}

	public TextBlock getBox() {
		return new CardBox(Display.create(node.getName()), skinParam);
	}

	public void addRelease(int stage, String label) {
		if (stage <= 0) {
			throw new IllegalArgumentException();
		}
		final BNode newNode = new BNode(stage, label);
		while (true) {
			if (stage > cursor.getStage()) {
				cursor.addChild(newNode);
				cursor = newNode;
				return;
			}
			cursor = cursor.getParent();
		}
	}

	private BArray array;

	private BArray getArray() {
		if (array == null) {
			node.computeX(new AtomicInteger());
			array = new BArray();
			node.initBarray(array);
		}
		return array;
	}

	public double getFullWidth() {
		final BArray array = getArray();
		return (array.getMaxX() + 1) * PostIt.getWidth();
	}

	public int getMaxStage() {
		final BArray array = getArray();
		return array.getMaxY();
	}

	public void drawMe(UGraphic ug) {

		getBox().drawU(ug);

		final BArray array = getArray();

		for (BNode node : array) {
			final double dx = node.getX() * PostIt.getWidth();
			final double dy = node.getStage() * PostIt.getHeight();
			ug.apply(new UTranslate(dx, dy));

			CardBox box = new CardBox(Display.create(node.getName()), skinParam);
			box.drawU(ug.apply(new UTranslate(dx, dy)));

		}

//		for (Entry<Integer, List<PostIt>> ent : postits.entrySet()) {
//			final int line = ent.getKey();
//			final List<PostIt> list = ent.getValue();
//			double dy = boardDiagram.getStageY(ug.getStringBounder(), line);
//			for (PostIt postit : list) {
//				postit.getCard().drawU(ug.apply(UTranslate.dy(dy)));
//				dy += PostIt.getHeight(ug.getStringBounder());
//			}
//
//		}

	}

}
