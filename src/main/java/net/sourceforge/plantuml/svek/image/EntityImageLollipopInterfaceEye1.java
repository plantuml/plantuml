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
package net.sourceforge.plantuml.svek.image;

import java.util.List;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.skin.ColorParam;
import net.sourceforge.plantuml.skin.SkinParamUtils;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.Bibliotekon;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.svek.SvekEdge;
import net.sourceforge.plantuml.url.Url;

public class EntityImageLollipopInterfaceEye1 extends AbstractEntityImage {

	private static final int SIZE = 24;
	private final TextBlock desc;
	private final Bibliotekon bibliotekon;
	final private Url url;

	public EntityImageLollipopInterfaceEye1(Entity entity, Bibliotekon bibliotekon) {
		super(entity);
		this.bibliotekon = bibliotekon;
		final Stereotype stereotype = entity.getStereotype();
		this.desc = entity.getDisplay().create(FontConfiguration.create(getSkinParam(), FontParam.CLASS, stereotype),
				HorizontalAlignment.CENTER, getSkinParam());
		this.url = entity.getUrl99();

	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return new XDimension2D(SIZE, SIZE);
	}

	final public void drawU(UGraphic ug) {
		ug = ug.apply(SkinParamUtils.getColor(getSkinParam(), getStereo(), ColorParam.classBorder));
		ug = ug.apply(SkinParamUtils.getColor(getSkinParam(), getStereo(), ColorParam.classBackground).bg());
		if (url != null) {
			ug.startUrl(url);
		}
		final double sizeSmall = 14;
		final double diff = (SIZE - sizeSmall) / 2;
		final UEllipse circle1 = UEllipse.build(sizeSmall, sizeSmall);
		if (getSkinParam().shadowing(getEntity().getStereotype())) {
			// circle.setDeltaShadow(4);
		}
		ug.apply(UStroke.withThickness(1.5)).apply(new UTranslate(diff, diff)).draw(circle1);
		ug = ug.apply(HColors.none().bg());

		XPoint2D pos = bibliotekon.getNode(getEntity()).getPosition();

		final List<SvekEdge> lines = bibliotekon.getAllLineConnectedTo(getEntity());
		final UTranslate reverse = UTranslate.point(pos).reverse();
		final ConnectedCircle connectedCircle = new ConnectedCircle(SIZE / 2);
		for (SvekEdge line : lines) {
			XPoint2D pt = line.getMyPoint(getEntity());
			pt = reverse.getTranslated(pt);
			connectedCircle.addSecondaryConnection(pt);

		}
		// connectedCircle.drawU(ug.apply(UStroke.withThickness(1.5)));
		connectedCircle.drawU(ug);

		//
		// final Dimension2D dimDesc = desc.calculateDimension(ug.getStringBounder());
		// final double widthDesc = dimDesc.getWidth();
		// // final double totalWidth = Math.max(widthDesc, SIZE);
		//
		// final double x = SIZE / 2 - widthDesc / 2;
		// final double y = SIZE;
		// desc.drawU(ug.apply(new UTranslate(x, y)));
		if (url != null) {
			ug.closeUrl();
		}
	}

	public ShapeType getShapeType() {
		return ShapeType.CIRCLE;
	}

}
