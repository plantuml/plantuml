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
package net.sourceforge.plantuml.golem;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class TilesField extends AbstractTextBlock implements TextBlock {

	private int size = 1;
	private final Tile root = new Tile(0);
	private final Map<Tile, Position> positions = new HashMap<Tile, Position>();
	private final List<Path> paths = new ArrayList<Path>();

	public TilesField() {
		positions.put(root, new Position(0, 0, 1, 1));
	}

	public Tile getRoot() {
		return root;
	}

	public Tile createTile(Tile start, TileGeometry position) {
		final Tile result = new Tile(size++);
		final Position p = getFreePosition(start, position);
		positions.put(result, p);
		paths.add(buildPath(start.getArea(position), result.getArea(position.opposite())));
		return result;
	}

	public void addPath(Tile start, Tile dest, TileGeometry startDirection) {
		paths.add(buildPath(start.getArea(startDirection), dest.getArea(startDirection.opposite())));
	}

	private Path buildPath(TileArea tileArea1, TileArea tileArea2) {
		if (isAdjoining(tileArea1, tileArea2)) {
			return Path.build(tileArea1, tileArea2);
		}
		final Tile tile1 = tileArea1.getTile();
		final Tile tile2 = tileArea2.getTile();
		final Position pos1 = getPosition(tile1);
		final Position pos2 = getPosition(tile2);
		final TileGeometry geom1 = tileArea1.getGeometry();
		final TileGeometry geom2 = tileArea2.getGeometry();
		if (pos1.getYmin() == pos2.getYmin() && pos1.getYmax() == pos2.getYmax() && geom1 == TileGeometry.WEST
				&& geom2 == TileGeometry.EAST) {
			return Path.build(tileArea1, tileArea2);

		}
		throw new IllegalArgumentException();
	}

	private boolean isAdjoining(TileArea tileArea1, TileArea tileArea2) {
		final Tile tile1 = tileArea1.getTile();
		final Tile tile2 = tileArea2.getTile();
		final Position pos1 = getPosition(tile1);
		final Position pos2 = getPosition(tile2);
		final TileGeometry geom1 = tileArea1.getGeometry();
		final TileGeometry geom2 = tileArea2.getGeometry();
		if (pos1.equals(pos2)) {
			assert tile1 == tile2;
			if (geom1 == geom2) {
				throw new IllegalArgumentException();
			}
			return true;
		}
		if (geom1.equals(geom2.opposite()) == false) {
			return false;
		}
		switch (geom1) {
		case EAST:
			return pos1.getYmin() == pos2.getYmin() && pos1.getYmax() == pos2.getYmax()
					&& pos1.getXmax() + 1 == pos2.getXmin();
		case WEST:
			return pos1.getYmin() == pos2.getYmin() && pos1.getYmax() == pos2.getYmax()
					&& pos1.getXmin() == pos2.getXmax() + 1;
		case SOUTH:
			return pos1.getXmin() == pos2.getXmin() && pos1.getXmax() == pos2.getXmax()
					&& pos1.getYmax() + 1 == pos2.getYmin();
		case NORTH:
			return pos1.getXmin() == pos2.getXmin() && pos1.getXmax() == pos2.getXmax()
					&& pos1.getYmin() == pos2.getYmax() + 1;
		case CENTER:
			return false;

		default:
			throw new IllegalStateException();
		}

	}

	private Tile getTileAt(Position p) {
		for (Map.Entry<Tile, Position> ent : positions.entrySet()) {
			if (p.equals(ent.getValue())) {
				return ent.getKey();
			}
		}
		return null;
	}

	private Position getFreePosition(Tile start, TileGeometry position) {
		final Position p = getPosition(start).move(position, 2);
		while (isOccuped(p)) {
			// p = p.move(TileGeometry.EAST, 2);
			moveAllToEast(p);
		}
		return p;
	}

	private void moveAllToEast(Position startingPosition) {
		final List<Position> toMove = new ArrayList<Position>();
		for (Position p : positions.values()) {
			if (p.getXmax() < startingPosition.getXmin()) {
				continue;
			}
			if (p.getYmax() < startingPosition.getYmin()) {
				continue;
			}
			toMove.add(p);
		}
		for (Position p : toMove) {
			positions.put(getTileAt(p), p.move(TileGeometry.EAST, 2));
		}

	}

	private boolean isOccuped(Position test) {
		for (Position p : positions.values()) {
			if (p.equals(test)) {
				return true;
			}
		}
		return false;
	}

	public Position getPosition(Tile tile) {
		final Position result = positions.get(tile);
		if (result == null) {
			throw new IllegalArgumentException();
		}
		return result;
	}

	private int getXmin() {
		int result = Integer.MAX_VALUE;
		for (Position p : positions.values()) {
			final int v = p.getXmin();
			if (v < result) {
				result = v;
			}
		}
		return result;
	}

	private int getYmin() {
		int result = Integer.MAX_VALUE;
		for (Position p : positions.values()) {
			final int v = p.getYmin();
			if (v < result) {
				result = v;
			}
		}
		return result;
	}

	private int getXmax() {
		int result = Integer.MIN_VALUE;
		for (Position p : positions.values()) {
			final int v = p.getXmax();
			if (v > result) {
				result = v;
			}
		}
		return result;
	}

	private int getYmax() {
		int result = Integer.MIN_VALUE;
		for (Position p : positions.values()) {
			final int v = p.getYmax();
			if (v > result) {
				result = v;
			}
		}
		return result;
	}

	public List<Path> getPaths() {
		return Collections.unmodifiableList(paths);
	}

	// -----------
	public void drawU(UGraphic ug) {
		double x = 0;
		double y = 0;
		final int xmin = getXmin();
		final int ymin = getYmin();
		final Dimension2D dimSingle = root.calculateDimension(ug.getStringBounder());
		x -= xmin * dimSingle.getWidth() / 2;
		y -= ymin * dimSingle.getHeight() / 2;
		for (Map.Entry<Tile, Position> ent : positions.entrySet()) {
			final Position p = ent.getValue();
			final Tile t = ent.getKey();
			final double xt = p.getXmin() * dimSingle.getWidth() / 2;
			final double yt = p.getYmin() * dimSingle.getHeight() / 2;
			t.drawU(ug.apply(new UTranslate((x + xt), (y + yt))));
		}
		ug = ug.apply(HColorUtils.RED);
		for (Path p : paths) {
			final TileArea start = p.getStart();
			final TileArea dest = p.getDest();
			final Point2D pstart = getPoint2D(dimSingle, start);
			final Point2D pdest = getPoint2D(dimSingle, dest);
			ug.apply(new UTranslate(x + pstart.getX(), y + pstart.getY())).draw(new ULine(pdest.getX() - pstart.getX(), pdest.getY()
			- pstart.getY()));
		}
	}

	private Point2D getPoint2D(Dimension2D dimSingle, TileArea area) {
		final Position p = getPosition(area.getTile());
		double xt = p.getXmin() * dimSingle.getWidth() / 2;
		double yt = p.getYmin() * dimSingle.getHeight() / 2;
		xt += dimSingle.getWidth() / 2;
		yt += dimSingle.getHeight() / 2;
		final double coef = 0.33;
		switch (area.getGeometry()) {
		case NORTH:
			yt -= dimSingle.getHeight() * coef;
			break;
		case SOUTH:
			yt += dimSingle.getHeight() * coef;
			break;
		case EAST:
			xt += dimSingle.getWidth() * coef;
			break;
		case WEST:
			xt -= dimSingle.getWidth() * coef;
			break;
		default:
			throw new IllegalStateException();
		}
		return new Point2D.Double(xt, yt);
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final int xmin = getXmin();
		final int xmax = getXmax();
		final int ymin = getYmin();
		final int ymax = getYmax();
		final int width = (xmax - xmin) / 2 + 1;
		final int height = (ymax - ymin) / 2 + 1;
		final Dimension2D dimSingle = root.calculateDimension(stringBounder);
		return new Dimension2DDouble(width * dimSingle.getWidth(), height * dimSingle.getHeight());
	}

}
