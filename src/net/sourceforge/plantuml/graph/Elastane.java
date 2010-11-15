/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 4626 $
 *
 */
package net.sourceforge.plantuml.graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.geom.Box;
import net.sourceforge.plantuml.geom.CollectionUtils;
import net.sourceforge.plantuml.geom.Point2DInt;
import net.sourceforge.plantuml.geom.PolylineBreakeable;
import net.sourceforge.plantuml.geom.XMoveable;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;

public class Elastane {

	private static final int STEP = 4;
	private static final int OUTER_BOX = 20;

	final private Color red = new Color(Integer.parseInt("A80036", 16));

	private final Galaxy4 galaxy;

	private final double margin = 30;

	private int minX = Integer.MAX_VALUE;
	private int maxX = Integer.MIN_VALUE;
	private int minY = Integer.MAX_VALUE;
	private int maxY = Integer.MIN_VALUE;

	Elastane(Galaxy4 galaxy) {
		this.galaxy = galaxy;
	}

	private final Map<ANode, Box> boxes = new LinkedHashMap<ANode, Box>();
	private final Map<ALink, PolylineBreakeable> lines = new LinkedHashMap<ALink, PolylineBreakeable>();

	public void addBox(ANode node, int w, int h) {
		final int row = node.getRow();
		final int col = galaxy.getBoard().getCol(node);
		final Point2DInt pos = galaxy.getMainPoint(row, col);
		final Box box = new Box(pos.getXint() - w / 2, pos.getYint() - h / 2, w, h);
		boxes.put(node, box);
		assert boxOverlap() == false;
	}

	private double getCost() {
		if (boxOverlap()) {
			return Double.MAX_VALUE;
		}
		double result = 0;
		for (ALink alink : galaxy.getLines().keySet()) {
			final PolylineBreakeable orig = lines.get(alink);
			final PolylineBreakeable p = orig;
			result += getLength(p);
			final Box b1 = boxes.get(alink.getNode1());
			final Box b2 = boxes.get(alink.getNode2());
			result += getCostBoxIntersect(p, b1, b2);

			for (ALink alink2 : galaxy.getLines().keySet()) {
				if (alink == alink2) {
					continue;
				}
				final PolylineBreakeable other = lines.get(alink2);
				if (p.doesTouch(other)) {
					result += getLength(other);
					// return Double.MAX_VALUE;
				}
				// result += p.getDistance(other);
			}
		}

		return result;
	}

	private double getLength(final PolylineBreakeable mutedPolyline) {
		final double len = mutedPolyline.getLength();
		assert len > 0;
		return Math.log(1 + len);
	}

	private double getCostBoxIntersect(PolylineBreakeable polyline, Box b1, Box b2) {
		double result = 0;
		for (Box b : boxes.values()) {
			if (b == b1 || b == b2) {
				continue;
			}
			if (polyline.intersectBox(b)) {
				final double dist = polyline.getDistance(b);
				assert dist >= 0;
				assert dist != Double.MAX_VALUE;
				// System.err.println("dist=" + dist + " exp=" + (100000 -
				// dist));
				result += 100000 - dist;
			}

		}
		return result;
	}

	private void initLines() {
		for (ALink alink : galaxy.getLines().keySet()) {
			final PolylineBreakeable p = galaxy.getPolyline(alink);
			final Box b1 = boxes.get(alink.getNode1());
			final Box b2 = boxes.get(alink.getNode2());
			lines.put(alink, p.copy(b1, b2));
		}

	}

	private boolean boxOverlap() {
		final List<Box> all = new ArrayList<Box>();
		for (Box b : boxes.values()) {
			all.add(b.outerBox(OUTER_BOX));
		}
		for (int i = 0; i < all.size() - 1; i++) {
			for (int j = i + 1; j < all.size(); j++) {
				if (all.get(i).intersectBox(all.get(j))) {
					return true;
				}
			}
		}
		return false;
	}

	private void moveX(Collection<XMoveable> boxes, int delta, double initCost, boolean trace) {
		for (XMoveable b : boxes) {
			b.moveX(delta);
		}
		// if (trace) {
		// final double diff = getCost() - initCost;
		// System.err.println("moving " + boxes + " " + delta + " diff=" +
		// diff);
		// }
		// for (Map.Entry<ALink, Polyline> entry : lines.entrySet()) {
		// if (isConcerned(entry.getKey(), boxes)) {
		// entry.getValue().moveX(delta);
		// }
		//
		// }
	}

	private boolean isConcerned(ALink link, Collection<XMoveable> moved) {
		final Box b1 = boxes.get(link.getNode1());
		final Box b2 = boxes.get(link.getNode2());
		return moved.contains(b1) && moved.contains(b2);
	}

	private boolean onePass(Collection<? extends Collection<XMoveable>> subLists) {
		boolean changed = false;
		for (Collection<XMoveable> toMove : subLists) {
			final double initCost = getCost();

			assert reversable(initCost, toMove);

			moveX(toMove, STEP, initCost, true);
			if (getCost() < initCost) {
				changed = true;
			} else {
				moveX(toMove, -STEP, initCost, false);
				moveX(toMove, -STEP, initCost, true);
				if (getCost() < initCost) {
					changed = true;
				} else {
					moveX(toMove, STEP, initCost, false);
					assert getCost() == initCost : "c1=" + getCost() + " init=" + initCost;
				}
			}
			assert getCost() <= initCost;

		}
		// System.err.println("COSTB=" + getCost());
		return changed;
	}

	private boolean reversable(double initCost, Collection<XMoveable> toMove) {
		moveX(toMove, STEP, 0, false);
		moveX(toMove, -STEP, 0, false);
		assert getCost() == initCost;
		moveX(toMove, STEP, 0, false);
		moveX(toMove, -STEP * 2, 0, false);
		moveX(toMove, STEP, 0, false);
		assert getCost() == initCost;
		return true;
	}

	private Collection<XMoveable> convertANodeSet(Set<ANode> nodesSet, Map<ALink, Collection<XMoveable>> linkMoveables) {
		final Collection<XMoveable> result = new HashSet<XMoveable>();
		for (ANode n : nodesSet) {
			assert boxes.get(n) != null;
			result.add(boxes.get(n));
		}
		// for (ALink link : galaxy.getBoard().getAllLinks(nodesSet)) {
		// result.addAll(linkMoveables.get(link));
		//
		// }
		return result;
	}

	public void draw(final Graphics2D g2d) {
		final AffineTransform at = g2d.getTransform();
		g2d.translate(-minX + margin, -minY + margin);

		g2d.setColor(red);
		for (Map.Entry<ALink, PolylineBreakeable> ent : lines.entrySet()) {
			final ALink alink = ent.getKey();
			final Link l = (Link) alink.getUserData();
			final GeneralPathFactory factory = new GeneralPathFactory(l.getType());
			final Box b1 = boxes.get(alink.getNode1());
			final Box b2 = boxes.get(alink.getNode2());
			final PolylineBreakeable polyline = ent.getValue();
			final Shape shape = factory.getLink(polyline, b1, b2);

			final String label = l.getLabel();
			if (label != null) {
				final Point2DInt center = polyline.getFirst().getCenter();
				final TextBlock textBlock = TextBlockUtils.create(Arrays.asList(label), g2d.getFont(), Color.BLACK,
						HorizontalAlignement.LEFT);
				final Dimension2D dim = textBlock.calculateDimension(StringBounderUtils.asStringBounder(g2d));
				textBlock.drawTOBEREMOVED(g2d, center.getXint() - dim.getWidth() / 2, center.getYint() - dim.getHeight() / 2);
			}

			g2d.setColor(red);
			g2d.draw(shape);
		}

		g2d.setColor(Color.BLACK);
		for (Map.Entry<ANode, Box> ent : boxes.entrySet()) {
			final ANode node = ent.getKey();
			final AbstractEntityImage image = images(node.getRow(), galaxy.getBoard().getCol(node));
			assert image != null;
			final Box box = ent.getValue();
			g2d.translate(box.getX(), box.getY());
			image.draw(g2d);
			g2d.translate(-box.getX(), -box.getY());
		}
		g2d.setTransform(at);
	}

	public void init() {
		initLines();
		final Set<Set<ANode>> nodesGroups = new HashSet<Set<ANode>>();
		final Collection<ANode> nodes = galaxy.getBoard().getNodes();
		for (ANode root : nodes) {
			for (int i = 0; i < galaxy.getBoard().getLinks().size(); i++) {
				final Set<ANode> group = galaxy.getBoard().getConnectedNodes(root, i);
				if (group.size() < nodes.size()) {
					nodesGroups.add(group);
				}
			}
		}
		final Collection<Collection<XMoveable>> xmoveableGroups = new ArrayList<Collection<XMoveable>>();
		final Map<ALink, Collection<XMoveable>> linkMoveables = new HashMap<ALink, Collection<XMoveable>>();

		for (Map.Entry<ALink, PolylineBreakeable> entry : lines.entrySet()) {
			final PolylineBreakeable p = entry.getValue();
			final List<XMoveable> freedoms = p.getFreedoms();
			// System.err.println("freedoms=" + freedoms);
			if (freedoms.size() > 0) {
				linkMoveables.put(entry.getKey(), freedoms);
				// xmoveableGroups.addAll(CollectionUtils.selectUpTo(freedoms,
				// freedoms.size()));
				xmoveableGroups.addAll(CollectionUtils.selectUpTo(freedoms, 1));
			}
		}

		for (Set<ANode> nodesSet : nodesGroups) {
			xmoveableGroups.add(convertANodeSet(nodesSet, linkMoveables));
		}

		assert getCost() != Double.MAX_VALUE;
		for (int i = 0; i < 3000; i++) {
			final boolean changed = onePass(xmoveableGroups);
			if (changed == false) {
				break;
			}
		}

		for (Box box : boxes.values()) {
			minX = Math.min(minX, box.getMinX());
			maxX = Math.max(maxX, box.getMaxX());
			minY = Math.min(minY, box.getMinY());
			maxY = Math.max(maxY, box.getMaxY());
		}

		for (PolylineBreakeable polyline : lines.values()) {
			minX = Math.min(minX, polyline.getMinX());
			maxX = Math.max(maxX, polyline.getMaxX());
			minY = Math.min(minY, polyline.getMinY());
			maxY = Math.max(maxY, polyline.getMaxY());
		}

	}

	private AbstractEntityImage images(int r, int c) {
		final ANode n = galaxy.getBoard().getNodeAt(r, c);
		if (n == null) {
			return null;
		}
		return new EntityImageFactory().createEntityImage((Entity) n.getUserData());
	}

	public Dimension2D getDimension() {
		final Dimension2DDouble dim = new Dimension2DDouble(maxX - minX + 2 * margin, maxY - minY + 2 * margin);
		Log.info("Dim=" + dim);
		return dim;
	}

	// private Point2DInt transform(Point2DInt src) {
	// return new Point2DInt(xMargin + widthCell / 2 + src.getXint() *
	// (widthCell + xMargin), yMargin + heightCell / 2
	// + src.getYint() * (heightCell + yMargin));
	// }

}
