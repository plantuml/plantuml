/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.cucadiagram.dot.DotData;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.posimo.Moveable;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UHidden;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public final class SvekResult implements IEntityImage, Moveable {

	private final Rose rose = new Rose();

	private ClusterPosition dim;
	private final DotData dotData;
	private final DotStringFactory dotStringFactory;

	public SvekResult(ClusterPosition dim, DotData dotData, DotStringFactory dotStringFactory) {
		this.dim = dim;
		this.dotData = dotData;
		this.dotStringFactory = dotStringFactory;
	}

	public void drawU(UGraphic ug) {

		for (Cluster cluster : dotStringFactory.getBibliotekon().allCluster()) {
			cluster.drawU(ug, dotData, new UStroke(1.5));
		}

		final HtmlColor color = rose.getHtmlColor(dotData.getSkinParam(), getArrowColorParam(), null);
		
		for (Shape shape : dotStringFactory.getBibliotekon().allShapes()) {
			final double minX = shape.getMinX();
			final double minY = shape.getMinY();
			final UGraphic ug2 = shape.isHidden() ? ug.apply(UHidden.HIDDEN) : ug;
			final IEntityImage image = shape.getImage();
			image.drawU(ug2.apply(new UTranslate(minX, minY)));
			if (image instanceof Untranslated) {
				((Untranslated) image).drawUntranslated(ug.apply(new UChangeColor(color)), minX, minY);
			}
			// shape.getImage().drawNeighborhood(ug2, minX, minY);
		}

		for (Line line : dotStringFactory.getBibliotekon().allLines()) {
			final UGraphic ug2 = line.isHidden() ? ug.apply(UHidden.HIDDEN) : ug;
			line.drawU(ug2, color);
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
