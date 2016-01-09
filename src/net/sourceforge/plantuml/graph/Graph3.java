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
import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.geom.InflationTransform;
import net.sourceforge.plantuml.geom.Kingdom;
import net.sourceforge.plantuml.geom.LineSegmentInt;
import net.sourceforge.plantuml.geom.Point2DInt;
import net.sourceforge.plantuml.geom.Pointable;
import net.sourceforge.plantuml.geom.Polyline;
import net.sourceforge.plantuml.geom.PolylineImpl;
import net.sourceforge.plantuml.geom.XMoveable;
import net.sourceforge.plantuml.geom.kinetic.Frame;
import net.sourceforge.plantuml.geom.kinetic.Path;
import net.sourceforge.plantuml.geom.kinetic.Point2DCharge;
import net.sourceforge.plantuml.geom.kinetic.World;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;

public class Graph3 {

	final private static Graphics2D dummyGraphics2D;

	private final int spaceWidth = 40;
	private final int spaceHeight = 40;
	private final int minDistBetweenPoint = 20;

	// private final int boxWidth = 20;
	// private final int boxHeight = 20;

	private final double margin = 30;
	private final Board board;
	private final List<PolylineImpl> polylines = new ArrayList<PolylineImpl>();
	private final Map<ANode, ANodePoint> nodePoints = new LinkedHashMap<ANode, ANodePoint>();

	private int maxRow;
	private int maxCol;

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

	private Collection<XMoveable> convertANodeSet(Set<ANode> nodesSet) {
		final Collection<XMoveable> result = new HashSet<XMoveable>();
		for (ANode n : nodesSet) {
			assert nodePoints.get(n) != null;
			result.add(nodePoints.get(n));
		}
		return result;
	}

	private int addedWidth = 0;
	private int addedHeight = 0;

	public Graph3(Board board) {
		board.normalize();
		this.board = board;
		for (ANode n : board.getNodes()) {
			maxRow = Math.max(maxRow, n.getRow());
			maxCol = Math.max(maxCol, board.getCol(n));
		}
		for (ANode n : board.getNodes()) {
			nodePoints.put(n, new ANodePoint(n));
		}

		// for (ALink link : board.getLinks()) {
		// final Pointable pp1 = nodePoints.get(link.getNode1());
		// final Pointable pp2 = nodePoints.get(link.getNode2());
		// polylines.add(new PolylineImpl(pp1, pp2));
		// }
		// manyPasses(board);
		// polylines.clear();

		computePolylines(board);

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

		for (PolylineImpl p : polylines) {
			p.inflate(inflationTransform);
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
			final Frame frame = new Frame(x, y, (int) dim.getWidth(), (int) dim.getHeight());
			frames.put(point, frame);
			world.addFrame(frame);
		}

		for (PolylineImpl polyline : polylines) {
			final Frame f1 = frames.get(polyline.getStart());
			final Frame f2 = frames.get(polyline.getEnd());
			final Path path = new Path(f1, f2);
			for (Point2DInt pt : polyline.getIntermediates()) {
				path.addIntermediate(new Point2DCharge(pt.getX(), pt.getY()));
			}
			world.addPath(path);
		}

		world.renderContinue();
		Log.info("Starting moving");
		final long start = System.currentTimeMillis();
		final int limit = 100;
		for (int i = 0; i < limit; i++) {
			Log.info("i=" + i);
			final double move = world.onePass();
			if (move < 1) {
				Log.info("i=" + i + " " + move);
				// break;
			}
			if (i == limit - 1) {
				Log.info("Aborting");
			}
		}
		final long duration = System.currentTimeMillis() - start;
		Log.info("Ending moving (" + duration + " ms)");

	}

	private final World world = new World();
	private final Map<ANodePoint, Frame> frames = new LinkedHashMap<ANodePoint, Frame>();

	private void computePolylines(Board board) {
		final Collection<ALink> latter = new ArrayList<ALink>();
		final Kingdom kingdom = new Kingdom();
		final List<? extends ALink> links = new ArrayList<ALink>(board.getLinks());
		Collections.sort(links, board.getLinkComparator());
		for (ALink link : links) {
			final Pointable pp1 = nodePoints.get(link.getNode1());
			final Pointable pp2 = nodePoints.get(link.getNode2());
			if (kingdom.isSimpleSegmentPossible(pp1.getPosition(), pp2.getPosition())) {
				Log.println("OK for " + link);
				kingdom.addDirectLink(pp1.getPosition(), pp2.getPosition());
				polylines.add(new PolylineImpl(pp1, pp2));
			} else {
				Log.println("Latter for " + link);
				latter.add(link);
			}
		}

		Log.println("latters=" + latter.size());
		for (ALink link : latter) {
			Log.println("Alatter=" + link);
		}
		for (ALink link : latter) {
			Log.println("Blatter=" + link);
			final Pointable pp1 = nodePoints.get(link.getNode1());
			final Pointable pp2 = nodePoints.get(link.getNode2());
			polylines.add((PolylineImpl) kingdom.getPath(pp1, pp2));
		}
	}

	private void manyPasses(Board board) {
		final Collection<Collection<XMoveable>> xmoveableGroups = getXMoveables(board);

		Log.println("COST_INIT=" + getCost());
		for (int i = 0; i < 300; i++) {
			final boolean changed = onePass(xmoveableGroups);
			if (changed == false) {
				break;
			}
		}
		Log.println("COST_FIN=" + getCost());
	}

	private Collection<Collection<XMoveable>> getXMoveables(Board board) {
		final Set<Set<ANode>> nodesGroups = new HashSet<Set<ANode>>();
		final Collection<ANode> nodes = board.getNodes();
		for (ANode root : nodes) {
			for (int i = 0; i < board.getLinks().size(); i++) {
				final Set<ANode> group = board.getConnectedNodes(root, i);
				if (group.size() < nodes.size()) {
					nodesGroups.add(group);
				}
			}
		}

		final Collection<Collection<XMoveable>> xmoveableGroups = new ArrayList<Collection<XMoveable>>();
		for (Set<ANode> nodesSet : nodesGroups) {
			xmoveableGroups.add(convertANodeSet(nodesSet));
		}
		return xmoveableGroups;
	}

	private void moveX(Collection<XMoveable> boxes, int delta) {
		for (XMoveable b : boxes) {
			b.moveX(delta);
		}
	}

	private static final int STEP = 1;

	private boolean onePass(Collection<? extends Collection<XMoveable>> subLists) {
		boolean changed = false;
		for (Collection<XMoveable> toMove : subLists) {
			final double initCost = getCost();

			assert reversable(initCost, toMove);

			moveX(toMove, STEP);
			if (getCost() < initCost) {
				changed = true;
			} else {
				moveX(toMove, -STEP);
				moveX(toMove, -STEP);
				if (getCost() < initCost) {
					changed = true;
				} else {
					moveX(toMove, STEP);
					assert getCost() == initCost : "c1=" + getCost() + " init=" + initCost;
				}
			}
			assert getCost() <= initCost;

		}
		// Log.println("COSTB=" + getCost());
		return changed;
	}

	private boolean reversable(double initCost, Collection<XMoveable> toMove) {
		moveX(toMove, STEP);
		moveX(toMove, -STEP);
		assert getCost() == initCost;
		moveX(toMove, STEP);
		moveX(toMove, -STEP * 2);
		moveX(toMove, STEP);
		assert getCost() == initCost;
		return true;
	}

	private double getCostOld() {
		if (mindistRespected() == false) {
			return Double.MAX_VALUE;
		}
		double result = 0;
		for (PolylineImpl p : polylines) {
			result += getLength(p);

			for (PolylineImpl other : polylines) {
				if (other == p) {
					continue;
				}
				if (p.doesTouch(other)) {
					result += getLength(other);
				}
			}
		}

		return result;
	}

	private double getCost() {
		double result = 0;
		for (PolylineImpl p1 : polylines) {
			for (PolylineImpl p2 : polylines) {
				result += getCost(p1, p2);
			}
		}

		final List<ANodePoint> all = new ArrayList<ANodePoint>(nodePoints.values());
		for (int i = 0; i < all.size() - 1; i++) {
			for (int j = i + 1; j < all.size(); j++) {
				final double len = new LineSegmentInt(all.get(i).getPosition(), all.get(j).getPosition()).getLength();
				result += minDistBetweenPoint * minDistBetweenPoint / len / len;
			}
		}

		return result;
	}

	private double getCost(PolylineImpl p1, PolylineImpl p2) {
		assert p1.nbSegments() == 1;
		assert p2.nbSegments() == 1;

		final LineSegmentInt seg1 = p1.getFirst();
		final LineSegmentInt seg2 = p2.getFirst();

		final double len1 = seg1.getLength();
		if (p1 == p2) {
			return len1 / minDistBetweenPoint;
		}
		final double len2 = seg2.getLength();

		// return len1 * len2 * Math.exp(-seg1.getDistance(seg2));
		return len1 * len2 / seg1.getDistance(seg2) / minDistBetweenPoint / minDistBetweenPoint;
		// return len1 * len2 * Math.exp(-seg1.getDistance(seg2)) /
		// minDistBetweenPoint / minDistBetweenPoint;
	}

	private boolean mindistRespected() {
		final List<ANodePoint> all = new ArrayList<ANodePoint>(nodePoints.values());
		for (int i = 0; i < all.size() - 1; i++) {
			for (int j = i + 1; j < all.size(); j++) {
				final double len = new LineSegmentInt(all.get(i).getPosition(), all.get(j).getPosition()).getLength();
				if (len <= minDistBetweenPoint) {
					return false;
				}
			}
		}
		return true;
	}

	private double getLength(final Polyline p) {
		final double len = p.getLength();
		assert len > 0;
		return Math.log(1 + len);
	}

	public Dimension2D getDimension() {
		final double width = spaceWidth * maxCol;
		final int height = spaceWidth * maxRow; // + boxHeight * (maxRow + 1);
		return new Dimension2DDouble(width + 2 * margin + addedWidth, height + 2 * margin + addedHeight);

	}

	public void draw(final Graphics2D g2d) {
		g2d.translate(margin, margin);

		for (Path p : world.getPaths()) {
			for (Line2D seg : p.segments()) {
				g2d.setColor(Color.BLUE);
				g2d.draw(seg);
				g2d.setColor(Color.RED);
				g2d.drawOval((int) seg.getX1(), (int) seg.getY1(), 1, 1);
			}
		}

		g2d.setColor(Color.GREEN);
		for (ANodePoint nodePoint : nodePoints.values()) {
			final Frame frame = frames.get(nodePoint);
			final AbstractEntityImage image = getImage(nodePoint.getNode());
			final double width = image.getDimension(StringBounderUtils.asStringBounder(g2d)).getWidth();
			final double height = image.getDimension(StringBounderUtils.asStringBounder(g2d)).getHeight();
			g2d.translate(frame.getX() - width / 2, frame.getY() - height / 2);
			image.draw(new ColorMapperIdentity(), g2d);
			g2d.translate(-frame.getX() + width / 2, -frame.getY() + height / 2);
		}

	}

	public void draw2(final Graphics2D g2d) {
		g2d.translate(margin, margin);

		g2d.setColor(Color.BLUE);
		for (Polyline p : polylines) {
			if (p == null) {
				Log.println("Polyline NULL!!");
				continue;
			}
			for (LineSegmentInt seg : p.segments()) {
				g2d.drawLine(seg.getP1().getXint(), seg.getP1().getYint(), seg.getP2().getXint(), seg.getP2().getYint());
			}
		}

		g2d.setColor(Color.GREEN);
		for (ANodePoint nodePoint : nodePoints.values()) {
			final Point2DInt p = nodePoint.getPosition();
			// Log.println("p=" + p);
			final AbstractEntityImage image = getImage(nodePoint.getNode());
			final int width = (int) (image.getDimension(StringBounderUtils.asStringBounder(g2d)).getWidth());
			final int height = (int) (image.getDimension(StringBounderUtils.asStringBounder(g2d)).getHeight());
			g2d.translate(p.getXint() - width / 2, p.getYint() - height / 2);
			image.draw(new ColorMapperIdentity(), g2d);
			g2d.translate(-p.getXint() + width / 2, -p.getYint() + height / 2);
			// g2d.fillOval(p.getXint() - 2, p.getYint() - 2, 5, 5);
			// g2d.drawRect(p.getXint() - 4, p.getYint() - 4, 8, 8);
		}
	}

	private AbstractEntityImage getImage(ANode n) {
		return new EntityImageFactory().createEntityImage((IEntity) n.getUserData());
	}

}
