/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * Revision $Revision: 6711 $
 *
 */
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.cucadiagram.dot.DotData;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.posimo.Moveable;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UHidden;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public final class SvekResult implements IEntityImage, Moveable {

	private final Rose rose = new Rose();

	private final HtmlColor clusterBorder;
	private ClusterPosition dim;
	private final DotData dotData;
	private final DotStringFactory dotStringFactory;
	private final boolean hasVerticalLine;

	public SvekResult(ClusterPosition dim, DotData dotData, DotStringFactory dotStringFactory, HtmlColor clusterBorder,
			boolean hasVerticalLine) {
		this.dim = dim;
		this.dotData = dotData;
		this.dotStringFactory = dotStringFactory;
		this.clusterBorder = clusterBorder;
		this.hasVerticalLine = hasVerticalLine;
	}

	public void drawU(UGraphic ug) {
		for (Cluster cluster : dotStringFactory.getBibliotekon().allCluster()) {
			cluster.drawU(ug, clusterBorder, dotData);
		}

		final Set<Double> xdots = new TreeSet<Double>();

		for (Shape shape : dotStringFactory.getBibliotekon().allShapes()) {
			final double minX = shape.getMinX();
			final double minY = shape.getMinY();
			final UGraphic ug2 = shape.isHidden() ? ug.apply(UHidden.HIDDEN) : ug;
			shape.getImage().drawU(ug2.apply(new UTranslate(minX, minY)));
			if (hasVerticalLine) {
				xdots.add(minX);
				xdots.add(minX + shape.getWidth());
			}
		}

		for (Line line : dotStringFactory.getBibliotekon().allLines()) {
			final UGraphic ug2 = line.isHidden() ? ug.apply(UHidden.HIDDEN) : ug;
			final HtmlColor color = rose.getHtmlColor(dotData.getSkinParam(), getArrowColorParam(), null);
			line.drawU(ug2, 0, 0, color);
		}

		final double THICKNESS_BORDER = 1.5;
		final int DASH = 8;

		if (xdots.size() > 0) {
			final double height = calculateDimension(ug.getStringBounder()).getHeight();
			ug = ug.apply(new UStroke(DASH, 10, THICKNESS_BORDER)).apply(new UChangeColor(clusterBorder));
			for (Double xv : middeling(xdots)) {
				ug.apply(new UTranslate(xv, 0)).draw(new ULine(0, height));
			}
		}
	}

	private Collection<Double> middeling(Set<Double> xdots) {
		final List<Double> result = new ArrayList<Double>();
		final Iterator<Double> it = xdots.iterator();
		it.next();
		while (true) {
			if (it.hasNext() == false) {
				return result;
			}
			final double v1 = it.next();
			if (it.hasNext() == false) {
				return result;
			}
			final double v2 = it.next();
			result.add((v1 + v2) / 2);
		}
	}

	private ColorParam getArrowColorParam() {
		if (dotData.getUmlDiagramType() == UmlDiagramType.CLASS) {
			return ColorParam.classArrow;
		} else if (dotData.getUmlDiagramType() == UmlDiagramType.OBJECT) {
			return ColorParam.objectArrow;
		} else if (dotData.getUmlDiagramType() == UmlDiagramType.DESCRIPTION) {
			return ColorParam.usecaseArrow;
		} else if (dotData.getUmlDiagramType() == UmlDiagramType.ACTIVITY) {
			return ColorParam.activityArrow;
		} else if (dotData.getUmlDiagramType() == UmlDiagramType.STATE) {
			return ColorParam.stateArrow;
		}
		throw new IllegalStateException();
	}

	public HtmlColor getBackcolor() {
		return dotData.getSkinParam().getBackgroundColor();
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return dim.getDimension();
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	public int getShield() {
		return 0;
	}

	public void moveSvek(double deltaX, double deltaY) {
		dotStringFactory.moveSvek(deltaX, deltaY);
		dim = dim.delta(deltaX > 0 ? deltaX : 0, deltaY > 0 ? deltaY : 0);
	}

	public boolean isHidden() {
		return false;
	}

}
