/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
 * Revision $Revision: 18280 $
 *
 */
package net.sourceforge.plantuml.graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.geom.InflationTransform;
import net.sourceforge.plantuml.geom.Point2DInt;
import net.sourceforge.plantuml.geom.Pointable;
import net.sourceforge.plantuml.geom.XMoveable;
import net.sourceforge.plantuml.geom.kinetic.Frame;
import net.sourceforge.plantuml.graph2.CubicCurveFactory;
import net.sourceforge.plantuml.graph2.MyCurve;
import net.sourceforge.plantuml.graph2.RectanglesCollection;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;

public class Graph4 {

	final private static Graphics2D dummyGraphics2D;

	private final int spaceWidth = 40;
	private final int spaceHeight = 40;

	private final double margin = 30;
	private final Board board;

	private final Map<ANode, ANodePoint> nodePoints = new LinkedHashMap<ANode, ANodePoint>();
	private final Map<ANodePoint, Frame> frames = new LinkedHashMap<ANodePoint, Frame>();

	private int maxRow;
	private int maxCol;

	private int addedWidth = 0;
	private int addedHeight = 0;

	static {
		final EmptyImageBuilder builder = new EmptyImageBuilder(10, 10, Color.WHITE);
		dummyGraphics2D = builder.getGraphics2D();
	}

	class ANodePoint implements Pointable, XMoveable {
		final private ANode node;
		private int deltaX = 0;
		private int deltaY = 0;

		public ANodePoint(ANode node) {
			this.node = node;
		}

		public Point2DInt getPosition() {
			return new Point2DInt(board.getCol(node) * spaceWidth + deltaX, node.getRow() * spaceHeight + deltaY);
		}

		public void moveX(int delta) {
			this.deltaX += delta;
		}

		public void moveY(int delta) {
			this.deltaY += delta;
		}

		public ANode getNode() {
			return node;
		}

	}

	public Graph4(Board board) {
		board.normalize();
		this.board = board;
		for (ANode n : board.getNodes()) {
			maxRow = Math.max(maxRow, n.getRow());
			maxCol = Math.max(maxCol, board.getCol(n));
		}
		for (ANode n : board.getNodes()) {
			nodePoints.put(n, new ANodePoint(n));
		}

		final InflationTransform inflationTransform = new InflationTransform();
		for (ANodePoint nodePoint : nodePoints.values()) {
			final Point2DInt p = nodePoint.getPosition();
			final AbstractEntityImage image = getImage(nodePoint.getNode());

			int widthCell = (int) image.getDimension(StringBounderUtils.asStringBounder(dummyGraphics2D)).getWidth();
			int heightCell = (int) image.getDimension(StringBounderUtils.asStringBounder(dummyGraphics2D)).getHeight();
			if (widthCell % 2 == 1) {
				widthCell++;
			}
			if (heightCell % 2 == 1) {
				heightCell++;
			}

			inflationTransform.addInflationX(p.getXint(), widthCell);
			addedWidth += widthCell;
			inflationTransform.addInflationY(p.getYint(), heightCell);
			addedHeight += heightCell;
		}

		for (ANodePoint nodePoint : nodePoints.values()) {
			final Point2DInt pos = nodePoint.getPosition();
			final Point2DInt pos2 = inflationTransform.inflatePoint2DInt(pos);
			nodePoint.moveX(pos2.getXint() - pos.getXint());
			nodePoint.moveY(pos2.getYint() - pos.getYint());
		}

		// Kinematic
		for (ANodePoint point : nodePoints.values()) {
			final double x = point.getPosition().getX();
			final double y = point.getPosition().getY();
			final Dimension2D dim = getImage(point.getNode()).getDimension(
					StringBounderUtils.asStringBounder(dummyGraphics2D));
			final int width = (int) dim.getWidth();
			final int height = (int) dim.getHeight();
			final Frame frame = new Frame(x - width / 2, y - height / 2, width, height);
			frames.put(point, frame);
		}

	}

	public Dimension2D getDimension() {
		final double width = spaceWidth * maxCol;
		final int height = spaceWidth * maxRow;
		return new Dimension2DDouble(width + 2 * margin + addedWidth, height + 2 * margin + addedHeight);

	}

	private final List<MyCurve> alreadyCurve = new ArrayList<MyCurve>();

	public void draw(final Graphics2D g2d) {
		g2d.translate(margin, margin);

		g2d.setColor(Color.BLUE);

		final long start = System.currentTimeMillis();
		alreadyCurve.clear();
		for (ALink link : getSortedLinks()) {
			final ANodePoint p1 = nodePoints.get(link.getNode1());
			final ANodePoint p2 = nodePoints.get(link.getNode2());
			final RectanglesCollection forbidden = getForbidden(link);
			if (forbidden.size() != nodePoints.size() - 2) {
				throw new IllegalStateException();
			}
			final MyCurve line = getCurveLink(p1, p2, forbidden);
			alreadyCurve.add(line);
			line.draw(g2d);
		}
		final long tps5 = System.currentTimeMillis() - start;
		Log.println("TPS5 = " + tps5);

		g2d.setColor(Color.GREEN);
		for (ANodePoint nodePoint : nodePoints.values()) {
			final Frame frame = frames.get(nodePoint);
			final AbstractEntityImage image = getImage(nodePoint.getNode());
			g2d.translate(frame.getX(), frame.getY());
			image.draw(new ColorMapperIdentity(), g2d);
			g2d.translate(-frame.getX(), -frame.getY());
		}

	}

	private List<ALink> getSortedLinks() {
		final Map<ALink, Double> lengths = new HashMap<ALink, Double>();
		for (ALink link : board.getLinks()) {
			final ANodePoint p1 = nodePoints.get(link.getNode1());
			final ANodePoint p2 = nodePoints.get(link.getNode2());
			lengths.put(link, p1.getPosition().distance(p2.getPosition()));
		}
		final List<ALink> all = new ArrayList<ALink>(lengths.keySet());
		Collections.sort(all, new Comparator<ALink>() {
			public int compare(ALink l1, ALink l2) {
				final double diff = lengths.get(l1) - lengths.get(l2);
				return (int) Math.signum(diff);
			}
		});
		return all;
	}

	private MyCurve getCurveLink(final ANodePoint p1, final ANodePoint p2, RectanglesCollection forbidden) {
		final int x1 = p1.getPosition().getXint();
		final int y1 = p1.getPosition().getYint();
		final int x2 = p2.getPosition().getXint();
		final int y2 = p2.getPosition().getYint();
		final CubicCurve2D.Double curve = new CubicCurve2D.Double(x1, y1, x1, y1, x2, y2, x2, y2);
		final MyCurve result = new MyCurve(curve);
		if (result.intersects(forbidden) || result.intersects(alreadyCurve)) {
			final CubicCurveFactory factory = new CubicCurveFactory(p1.getPosition(), p2.getPosition());
			for (Rectangle2D.Double r : forbidden) {
				factory.addForbidden(r);
			}
			for (MyCurve c : alreadyCurve) {
				factory.addForbidden(c);
			}
			return factory.getCubicCurve2D();

		}
		return result;
	}

	private RectanglesCollection getForbidden(ALink link) {
		final RectanglesCollection result = new RectanglesCollection();
		for (Map.Entry<ANode, ANodePoint> entry : nodePoints.entrySet()) {
			final ANode node = entry.getKey();
			if (link.getNode1().equals(node) || link.getNode2().equals(node)) {
				continue;
			}
			final ANodePoint nodePoints = entry.getValue();
			final Frame frame = frames.get(nodePoints);
			result.add(new Rectangle2D.Double(frame.getX(), frame.getY(), frame.getWidth(), frame.getHeight()));
		}

		return result;
	}

	private AbstractEntityImage getImage(ANode n) {
		return new EntityImageFactory().createEntityImage((IEntity) n.getUserData());
	}

}
