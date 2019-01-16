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
package net.sourceforge.plantuml.posimo;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import net.sourceforge.plantuml.Dimension2DDouble;

public class Cluster implements Clusterable {

	private static int CPT = 1;

	private final Cluster parent;
	private final Collection<Block> blocs = new ArrayList<Block>();
	private final Collection<Cluster> children = new ArrayList<Cluster>();
	private final int uid = CPT++;
	private double x;
	private double y;
	private double width;
	private double height;
	
	private final double titleWidth;
	private final double titleHeight;

//	public Cluster(Cluster parent) {
//		this(parent, 100, 20);
//	}
//	
	public Cluster(Cluster parent, double titleWidth, double titleHeight) {
		this.parent = parent;
		this.titleWidth = titleWidth;
		this.titleHeight = titleHeight;
		if (parent != null) {
			parent.children.add(this);
		}
	}

	public Collection<Cluster> getSubClusters() {
		return Collections.unmodifiableCollection(children);
	}

	public Collection<Block> getRecursiveContents() {
		final Collection<Block> result = new ArrayList<Block>();
		addContentRecurse(result);
		return Collections.unmodifiableCollection(result);
	}

	private void addContentRecurse(Collection<Block> result) {
		result.addAll(blocs);
		for (Cluster c : children) {
			c.addContentRecurse(result);
		}

	}

	public int getUid() {
		return uid;
	}

	public void addBloc(Block b) {
		this.blocs.add(b);
	}

	public Cluster getParent() {
		return parent;
	}

	public Collection<Block> getContents() {
		return Collections.unmodifiableCollection(blocs);
	}

	public Block getBlock(int uid) {
		for (Block b : blocs) {
			if (b.getUid() == uid) {
				return b;
			}
		}
		for (Cluster sub : children) {
			final Block result = sub.getBlock(uid);
			if (result != null) {
				return result;
			}
		}
		return null;
	}
	
	public Point2D getPosition() {
		return new Point2D.Double(x, y);
	}

	public Dimension2D getSize() {
		return new Dimension2DDouble(width, height);
	}

	public final void setX(double x) {
		this.x = x;
	}

	public final void setY(double y) {
		this.y = y;
	}

	public final void setWidth(double width) {
		this.width = width;
	}

	public final void setHeight(double height) {
		this.height = height;
	}

	public final double getTitleWidth() {
		return titleWidth;
	}

	public final double getTitleHeight() {
		return titleHeight;
	}

	public void moveSvek(double deltaX, double deltaY) {
		throw new UnsupportedOperationException();
	}


}
