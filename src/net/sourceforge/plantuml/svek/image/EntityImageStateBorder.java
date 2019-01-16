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
package net.sourceforge.plantuml.svek.image;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamUtils;
import net.sourceforge.plantuml.cucadiagram.EntityPosition;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Rankdir;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.Bibliotekon;
import net.sourceforge.plantuml.svek.Cluster;
import net.sourceforge.plantuml.svek.Shape;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class EntityImageStateBorder extends AbstractEntityImage {

	private final TextBlock desc;
	private final Cluster stateParent;
	private final EntityPosition entityPosition;
	private final Bibliotekon bibliotekon;
	private final Rankdir rankdir;

	public EntityImageStateBorder(ILeaf leaf, ISkinParam skinParam, Cluster stateParent, final Bibliotekon bibliotekon) {
		super(leaf, skinParam);
		this.bibliotekon = bibliotekon;
		this.rankdir = skinParam.getRankdir();

		this.entityPosition = leaf.getEntityPosition();
		if (entityPosition == EntityPosition.NORMAL) {
			throw new IllegalArgumentException();
		}
		this.stateParent = stateParent;
		final Stereotype stereotype = leaf.getStereotype();

		this.desc = leaf.getDisplay().create(new FontConfiguration(getSkinParam(), FontParam.STATE, stereotype),
				HorizontalAlignment.CENTER, skinParam);
	}

	private boolean upPosition() {
		final Point2D clusterCenter = stateParent.getClusterPosition().getPointCenter();
		final Shape sh = bibliotekon.getShape(getEntity());
		return sh.getMinY() < clusterCenter.getY();
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return entityPosition.getDimension(rankdir);
	}

	public double getMaxWidthFromLabelForEntryExit(StringBounder stringBounder) {
		final Dimension2D dimDesc = desc.calculateDimension(stringBounder);
		return dimDesc.getWidth();
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

		ug = ug.apply(new UStroke(1.5)).apply(
				new UChangeColor(SkinParamUtils.getColor(getSkinParam(), getStereo(), ColorParam.stateBorder)));
		HtmlColor backcolor = getEntity().getColors(getSkinParam()).getColor(ColorType.BACK);
		if (backcolor == null) {
			backcolor = SkinParamUtils.getColor(getSkinParam(), getStereo(), ColorParam.stateBackground);
		}
		ug = ug.apply(new UChangeBackColor(backcolor));

		entityPosition.drawSymbol(ug, rankdir);

	}

	public ShapeType getShapeType() {
		return entityPosition.getShapeType();
	}

}
