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
package net.sourceforge.plantuml.svek;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.graphic.UGraphicDelegator;
import net.sourceforge.plantuml.ugraphic.UChange;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class UGraphicForSnake extends UGraphicDelegator {

	private final double dx;
	private final double dy;
	private final List<PendingSnake> snakes;

	@Override
	public String toString() {
		return "UGraphicForSnake " + getUg();
	}

	public UTranslate getTranslation() {
		return new UTranslate(dx, dy);
	}

	static class PendingSnake {
		private final Snake snake;
		private final UGraphic ug;
		private final double dx;
		private final double dy;

		private PendingSnake(Snake snake, UGraphic ug, double dx, double dy) {
			this.snake = snake;
			this.ug = ug;
			this.dx = dx;
			this.dy = dy;
		}

		void drawInternal() {
			snake.drawInternal(ug);
		}

		void removeEndDecorationIfTouches(List<PendingSnake> snakes) {
			for (PendingSnake other : snakes) {
				if (moved().touches(other.moved())) {
					this.snake.removeEndDecoration();
					return;
				}
			}
		}

		private Snake moved() {
			return snake.move(dx, dy);
		}

		@Override
		public String toString() {
			return "dx=" + dx + " dy=" + dy + " " + snake.move(dx, dy).toString();
		}

		public PendingSnake merge(PendingSnake newItem) {
			// if (snake.isMergeable() == false || newItem.snake.isMergeable() == false) {
			// return null;
			// }
			final Snake s1 = snake.move(dx, dy);
			final Snake s2 = newItem.snake.move(newItem.dx, newItem.dy);
			final Snake merge = s1.merge(s2, ug.getStringBounder());
			if (merge == null) {
				return null;
			}
			return new PendingSnake(merge.move(-dx, -dy), ug, dx, dy);
		}

	}

	public UGraphicForSnake(UGraphic ug) {
		this(ug, 0, 0, new ArrayList<PendingSnake>());
	}

	private UGraphicForSnake(UGraphic ug, double dx, double dy, List<PendingSnake> snakes) {
		super(ug);
		this.dx = dx;
		this.dy = dy;
		this.snakes = snakes;
	}

	public void draw(UShape shape) {
		if (shape instanceof Snake) {
			final Snake snake = (Snake) shape;
			addPendingSnake(snake);
		} else {
			getUg().draw(shape);
		}
	}

	private void addPendingSnake(final Snake snake) {
		final PendingSnake newItem = new PendingSnake(snake, getUg(), dx, dy);
		for (int pos = 0; pos < snakes.size(); pos++) {
			final PendingSnake merge = snakes.get(pos).merge(newItem);
			if (merge != null) {
				snakes.set(pos, merge);
				return;
			}
		}
		snakes.add(newItem);
	}

	@Override
	public void flushUg() {
		for (PendingSnake snake : snakes) {
			snake.removeEndDecorationIfTouches(snakes);
			snake.drawInternal();
		}
		snakes.clear();
	}

	public UGraphic apply(UChange change) {
		double newdx = dx;
		double newdy = dy;
		if (change instanceof UTranslate) {
			newdx += ((UTranslate) change).getDx();
			newdy += ((UTranslate) change).getDy();
		}
		return new UGraphicForSnake(getUg().apply(change), newdx, newdy, snakes);
	}

}