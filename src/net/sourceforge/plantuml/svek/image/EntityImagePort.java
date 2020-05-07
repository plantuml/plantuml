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
 * Creator:  Hisashi Miyashita
 *
 * 
 */

package net.sourceforge.plantuml.svek.image;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamUtils;
import net.sourceforge.plantuml.cucadiagram.EntityPosition;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.svek.Bibliotekon;
import net.sourceforge.plantuml.svek.Cluster;
import net.sourceforge.plantuml.svek.Node;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.Shadowable;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class EntityImagePort extends AbstractEntityImageBorder {
	public EntityImagePort(ILeaf leaf, ISkinParam skinParam, Cluster parent, final Bibliotekon bibliotekon) {
		super(leaf, skinParam, parent, bibliotekon, FontParam.BOUNDARY);
	}

	private boolean upPosition() {
		final Point2D clusterCenter = parent.getClusterPosition().getPointCenter();
		final Node node = bibliotekon.getNode(getEntity());
		return node.getMinY() < clusterCenter.getY();
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		double sp = EntityPosition.RADIUS * 2;
		return new Dimension2DDouble(sp, sp);
	}

	public double getMaxWidthFromLabelForEntryExit(StringBounder stringBounder) {
		final Dimension2D dimDesc = desc.calculateDimension(stringBounder);
		return dimDesc.getWidth();
	}

	private void drawSymbol(UGraphic ug) {
		final Shadowable rect = new URectangle(EntityPosition.RADIUS * 2, EntityPosition.RADIUS * 2);
		ug.draw(rect);
	}

	final public void drawU(UGraphic ug) {
		double y = 0;
		final Dimension2D dimDesc = desc.calculateDimension(ug.getStringBounder());
		final double x = 0 - (dimDesc.getWidth() - 2 * EntityPosition.RADIUS) / 2;

		if (upPosition()) {
			y -= 2 * EntityPosition.RADIUS + dimDesc.getHeight();
		} else {
			y += 2 * EntityPosition.RADIUS;
		}
		desc.drawU(ug.apply(new UTranslate(x, y)));

		ug = ug.apply(new UStroke(1.5))
				.apply(SkinParamUtils.getColor(getSkinParam(), getStereo(), ColorParam.stateBorder).bg());
		HColor backcolor = getEntity().getColors(getSkinParam()).getColor(ColorType.BACK);
		if (backcolor == null) {
			backcolor = SkinParamUtils.getColor(getSkinParam(), getStereo(), ColorParam.stateBackground);
		}
		ug = ug.apply(backcolor);

		drawSymbol(ug);
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}
}
