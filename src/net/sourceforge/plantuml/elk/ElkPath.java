/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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

import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.cucadiagram.ICucaDiagram;
import net.sourceforge.plantuml.decoration.LinkDecor;
import net.sourceforge.plantuml.decoration.LinkType;

/*
 * You can choose between real "org.eclipse.elk..." classes or proxied "net.sourceforge.plantuml.elk.proxy..."
 * 
 * Using proxied classes allows to compile PlantUML without having ELK available on the classpath.
 * Since GraphViz is the default layout engine up to now, we do not want to enforce the use of ELK just for compilation.
 * (for people not using maven)
 * 
 * If you are debugging, you should probably switch to "org.eclipse.elk..." classes
 * 
 */

/*
import org.eclipse.elk.graph.ElkBendPoint;
import org.eclipse.elk.graph.ElkEdge;
import org.eclipse.elk.graph.ElkEdgeSection;
import org.eclipse.elk.graph.ElkLabel;
*/

import net.sourceforge.plantuml.elk.proxy.graph.ElkBendPoint;
import net.sourceforge.plantuml.elk.proxy.graph.ElkEdge;
import net.sourceforge.plantuml.elk.proxy.graph.ElkEdgeSection;
import net.sourceforge.plantuml.elk.proxy.graph.ElkLabel;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.skin.LineParam;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactory;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryExtends;

public class ElkPath implements UDrawable {
    // ::remove folder when __HAXE__

	private final Link link;
	private final ElkEdge edge;

	private final ICucaDiagram diagram;
	private final TextBlock centerLabel;
	private final TextBlock headLabel;
	private final TextBlock tailLabel;

	private final SName styleName;

	private final double magicY2;

	public ElkPath(ICucaDiagram diagram, SName styleName, Link link, ElkEdge edge, TextBlock centerLabel,
			TextBlock tailLabel, TextBlock headLabel, double magicY2) {
		this.link = link;
		this.edge = edge;

		this.diagram = diagram;
		this.centerLabel = centerLabel;
		this.tailLabel = tailLabel;
		this.headLabel = headLabel;
		this.styleName = styleName;
		this.magicY2 = magicY2;

	}

	private Style getStyle() {
		final StyleSignatureBasic signature = StyleSignatureBasic.of(SName.root, SName.element, styleName, SName.arrow);
		return signature.getMergedStyle(diagram.getCurrentStyleBuilder());
	}

	public void drawU(UGraphic ug) {

		if (link.isHidden())
			return;

		final ISkinParam skinParam = diagram.getSkinParam();

		HColor color = getStyle().value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());

		if (this.link.getColors() != null) {
			final HColor newColor = this.link.getColors().getColor(ColorType.ARROW, ColorType.LINE);
			if (newColor != null)
				color = newColor;
		} else if (this.link.getSpecificColor() != null) {
			color = this.link.getSpecificColor();
		}

		final LinkType linkType = link.getType();
		UStroke stroke = linkType.getStroke3(skinParam.getThickness(LineParam.arrow, null));
		if (link.getColors() != null && link.getColors().getSpecificLineStroke() != null)
			stroke = link.getColors().getSpecificLineStroke();

		ug = ug.apply(stroke).apply(color);

		final List<ElkEdgeSection> sections = edge.getSections();
		if (sections.size() == 0) {
			System.err.println("Strange: no section?");
			System.err.println("Maybe a 'Long hierarchical edge' " + edge.isHierarchical());
			return;
		} else {
			drawSections(ug, sections);
		}

		final UDrawable extremityFactory1 = getDecors(link.getType().getDecor1(), Math.PI / 2, HColors.WHITE);
		final UDrawable extremityFactory2 = getDecors(link.getType().getDecor2(), -Math.PI / 2, HColors.WHITE);

		if (extremityFactory1 != null) {
			final double x = sections.get(0).getEndX();
			final double y = sections.get(0).getEndY();
			extremityFactory1.drawU(ug.apply(new UTranslate(x, y)));
		}

		if (extremityFactory2 != null) {
			final double x = sections.get(0).getStartX();
			final double y = sections.get(0).getStartY();
			extremityFactory2.drawU(ug.apply(new UTranslate(x, y)));
		}

		drawLabels(ug);

	}

	private UDrawable getDecors(LinkDecor decors, double angle, HColor backColor) {
		// For legacy reason, extends are treated differently
		if (decors == LinkDecor.EXTENDS)
			return new ExtremityFactoryExtends(backColor).createUDrawable(new XPoint2D(0, 0), angle, null);

		final ExtremityFactory extremityFactory = decors.getExtremityFactoryLegacy(backColor);
		if (extremityFactory == null)
			return null;

		return extremityFactory.createUDrawable(new XPoint2D(0, 0), angle, null);
	}

	private void drawLabels(UGraphic ug) {
		for (ElkLabel label : edge.getLabels()) {
			final double x = label.getX();
			final double y = label.getY();
			final TextBlock labelLink;
			// Nasty trick: we store the type of label (center/head/tail) in the text
			final String type = label.getText();
			if ("X".equals(type))
				labelLink = centerLabel;
			else if ("1".equals(type))
				labelLink = tailLabel;
			else if ("2".equals(type))
				labelLink = headLabel;
			else
				continue;

			labelLink.drawU(ug.apply(new UTranslate(x, y)));
		}
	}

	private void drawSections(UGraphic ug, final Collection<ElkEdgeSection> sections) {
		for (ElkEdgeSection section : sections) {
			final Collection<ElkBendPoint> points = section.getBendPoints();

			double x1 = section.getStartX();
			double y1 = section.getStartY();

			for (ElkBendPoint pt : points) {
				drawLine(ug, x1, y1, pt.getX(), pt.getY());
				x1 = pt.getX();
				y1 = pt.getY();
			}

			drawLine(ug, x1, y1, section.getEndX(), section.getEndY() + magicY2);
		}
	}

	private void drawLine(UGraphic ug, double x1, double y1, double x2, double y2) {
		final ULine line = new ULine(x2 - x1, y2 - y1);
		ug.apply(new UTranslate(x1, y1)).draw(line);
	}

}
