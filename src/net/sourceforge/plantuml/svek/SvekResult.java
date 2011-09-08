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
 * Revision $Revision: 6711 $
 *
 */
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.cucadiagram.dot.DotData;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.posimo.Moveable;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public final class SvekResult implements IEntityImage, Moveable {

	private final Rose rose = new Rose();

	private final HtmlColor clusterBorder;
	private Dimension2D dim;
	private final DotData dotData;
	private final DotStringFactory dotStringFactory;

	public SvekResult(Dimension2D dim, DotData dotData, DotStringFactory dotStringFactory, HtmlColor clusterBorder) {
		this.dim = dim;
		this.dotData = dotData;
		this.dotStringFactory = dotStringFactory;
		this.clusterBorder = clusterBorder;
	}

	public void drawU(UGraphic ug, double x, double y) {
		for (Cluster cluster : dotStringFactory.getAllSubCluster()) {
			cluster.drawU(ug, x, y, clusterBorder, dotData);
		}

		for (Shape shape : dotStringFactory.getShapes()) {
			final double minX = shape.getMinX();
			final double minY = shape.getMinY();
			shape.getImage().drawU(ug, x + minX, y + minY);
		}

		for (Line line : dotStringFactory.getLines()) {
			// line.patchLineForCluster(dotStringFactory.getAllSubCluster());
			final HtmlColor color = rose.getHtmlColor(dotData.getSkinParam(), getArrowColorParam(), null);
			line.drawU(ug, x, y, color);
		}

	}

	private ColorParam getArrowColorParam() {
		if (dotData.getUmlDiagramType() == UmlDiagramType.CLASS) {
			return ColorParam.classArrow;
		} else if (dotData.getUmlDiagramType() == UmlDiagramType.OBJECT) {
			return ColorParam.objectArrow;
		} else if (dotData.getUmlDiagramType() == UmlDiagramType.USECASE) {
			return ColorParam.usecaseArrow;
		} else if (dotData.getUmlDiagramType() == UmlDiagramType.ACTIVITY) {
			return ColorParam.activityArrow;
		} else if (dotData.getUmlDiagramType() == UmlDiagramType.COMPONENT) {
			return ColorParam.componentArrow;
		} else if (dotData.getUmlDiagramType() == UmlDiagramType.STATE) {
			return ColorParam.stateArrow;
		}
		throw new IllegalStateException();
	}

	public HtmlColor getBackcolor() {
		return dotData.getSkinParam().getBackgroundColor();
	}

	public Dimension2D getDimension(StringBounder stringBounder) {
		return dim;
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	public int getShield() {
		return 0;
	}

	public void moveSvek(double deltaX, double deltaY) {
		dotStringFactory.moveSvek(deltaX, deltaY);
		dim = Dimension2DDouble.delta(dim, deltaX > 0 ? deltaX : 0, deltaY > 0 ? deltaY : 0);
	}

}
