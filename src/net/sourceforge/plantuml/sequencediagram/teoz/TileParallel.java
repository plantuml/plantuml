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
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class TileParallel extends CommonTile {

	public TileParallel(StringBounder stringBounder) {
		super(stringBounder);
	}

	private final List<Tile> tiles = new ArrayList<>();

	@Override
	final protected void callbackY_internal(double y) {
		for (Tile tile : tiles) {
			tile.callbackY(y);
		}
	}

	public void add(Tile tile) {
		this.tiles.add(tile);
	}

	public void drawU(UGraphic ug) {
		final double yPointAll = getContactPointRelative();
		for (Tile tile : tiles) {
			final double yPoint = tile.getContactPointRelative();
			((UDrawable) tile).drawU(ug.apply(UTranslate.dy(yPointAll - yPoint)));
		}
	}

	public double getContactPointRelative() {
		double result = 0;
		for (Tile tile : tiles) {
			result = Math.max(result, tile.getContactPointRelative());
		}
		return result;
	}

	public double getZZZ() {
		double result = 0;
		for (Tile tile : tiles) {
			result = Math.max(result, tile.getZZZ());
		}
		return result;
	}

	public double getPreferredHeight() {
		return getContactPointRelative() + getZZZ();
	}

	public void addConstraints() {
		for (Tile tile : tiles) {
			tile.addConstraints();
		}
	}

	public Real getMinX() {
		return RealUtils.min(new AbstractCollection<Real>() {
			public Iterator<Real> iterator() {
				return new Iterator<Real>() {
					private final Iterator<Tile> source = tiles.iterator();

					public boolean hasNext() {
						return source.hasNext();
					}

					public Real next() {
						return source.next().getMinX();
					}

					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}

			public int size() {
				return tiles.size();
			}
		});
	}

	public Real getMaxX() {
		return RealUtils.max(new AbstractCollection<Real>() {
			public Iterator<Real> iterator() {
				return new Iterator<Real>() {
					private final Iterator<Tile> source = tiles.iterator();

					public boolean hasNext() {
						return source.hasNext();
					}

					public Real next() {
						return source.next().getMaxX();
					}

					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}

			public int size() {
				return tiles.size();
			}
		});
	}

	public Event getEvent() {
		return null;
	}

	public boolean matchAnchor(String anchor) {
		for (Tile tile : tiles) {
			if (tile.matchAnchor(anchor)) {
				return true;
			}
		}
		return false;
	}

}
