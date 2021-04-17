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
package net.sourceforge.plantuml.elk;

import org.eclipse.elk.graph.ElkBendPoint;
import org.eclipse.elk.graph.ElkEdge;
import org.eclipse.elk.graph.ElkEdgeSection;
import org.eclipse.elk.graph.ElkLabel;
import org.eclipse.emf.common.util.EList;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.LineParam;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class ElkPath implements UDrawable {

	private final Link link;
	private final ElkEdge edge;

	private final CucaDiagram diagram;
	private final TextBlock centerLabel;
	private final TextBlock headLabel;
	private final TextBlock tailLabel;
	private final Rose rose = new Rose();

	public ElkPath(Link link, ElkEdge edge, CucaDiagram diagram, TextBlock centerLabel, TextBlock tailLabel,
			TextBlock headLabel) {
		this.link = link;
		this.edge = edge;

		this.diagram = diagram;
		this.centerLabel = centerLabel;
		this.tailLabel = tailLabel;
		this.headLabel = headLabel;
	}

	private ColorParam getArrowColorParam() {
		if (diagram.getUmlDiagramType() == UmlDiagramType.CLASS) {
			return ColorParam.arrow;
		} else if (diagram.getUmlDiagramType() == UmlDiagramType.OBJECT) {
			return ColorParam.arrow;
		} else if (diagram.getUmlDiagramType() == UmlDiagramType.DESCRIPTION) {
			return ColorParam.arrow;
		} else if (diagram.getUmlDiagramType() == UmlDiagramType.ACTIVITY) {
			return ColorParam.arrow;
		} else if (diagram.getUmlDiagramType() == UmlDiagramType.STATE) {
			return ColorParam.arrow;
		}
		throw new IllegalStateException();
	}

	public void drawU(UGraphic ug) {

		if (link.isHidden()) {
			return;
		}

		HColor color = rose.getHtmlColor(diagram.getSkinParam(), null, getArrowColorParam());

		if (this.link.getColors() != null) {
			final HColor newColor = this.link.getColors().getColor(ColorType.ARROW, ColorType.LINE);
			if (newColor != null) {
				color = newColor;
			}

		} else if (this.link.getSpecificColor() != null) {
			color = this.link.getSpecificColor();
		}

		final LinkType linkType = link.getType();
		UStroke stroke = linkType.getStroke3(diagram.getSkinParam().getThickness(LineParam.arrow, null));
		if (link.getColors() != null && link.getColors().getSpecificLineStroke() != null) {
			stroke = link.getColors().getSpecificLineStroke();
		}
		ug = ug.apply(stroke).apply(color);

		final EList<ElkEdgeSection> sections = edge.getSections();
		if (sections.size() == 0) {
			System.err.println("Strange: no section?");
			System.err.println("Maybe a 'Long hierarchical edge' " + edge.isHierarchical());
		} else {
			drawSections(ug, sections);
		}

		drawLabels(ug);

	}

	private void drawLabels(UGraphic ug) {
		for (ElkLabel label : edge.getLabels()) {
			final double x = label.getX();
			final double y = label.getY();
			final TextBlock labelLink;
			// Nasty trick: we store the type of label (center/head/tail) in the text
			final String type = label.getText();
			if ("X".equals(type)) {
				labelLink = centerLabel;
			} else if ("1".equals(type)) {
				labelLink = tailLabel;
			} else if ("2".equals(type)) {
				labelLink = headLabel;
			} else {
				continue;
			}
			labelLink.drawU(ug.apply(new UTranslate(x, y)));
		}
	}

	private void drawSections(UGraphic ug, final EList<ElkEdgeSection> sections) {
		for (ElkEdgeSection section : sections) {
			final EList<ElkBendPoint> points = section.getBendPoints();

			double x1 = section.getStartX();
			double y1 = section.getStartY();

			for (ElkBendPoint pt : points) {
				drawLine(ug, x1, y1, pt.getX(), pt.getY());
				x1 = pt.getX();
				y1 = pt.getY();
			}

			drawLine(ug, x1, y1, section.getEndX(), section.getEndY());

		}
	}

	private void drawLine(UGraphic ug, final double x1, final double y1, final double x2, final double y2) {
		final ULine line = new ULine(x2 - x1, y2 - y1);
		ug.apply(new UTranslate(x1, y1)).draw(line);
	}

}
