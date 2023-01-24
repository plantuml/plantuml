/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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

import java.util.Map;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.awt.geom.XDimension2D;
import net.sourceforge.plantuml.awt.geom.XPoint2D;
import net.sourceforge.plantuml.awt.geom.XRectangle2D;
import net.sourceforge.plantuml.baraye.IEntity;
import net.sourceforge.plantuml.baraye.ILeaf;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.cucadiagram.BodyFactory;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.Bibliotekon;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.svek.SvekNode;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.utils.Direction;

public class EntityImageTips extends AbstractEntityImage {

	private final ISkinParam skinParam;

	private final HColor noteBackgroundColor;
	private final HColor borderColor;

	private final Bibliotekon bibliotekon;
	private final Style style;

	private final double ySpacing = 10;

	public EntityImageTips(ILeaf entity, ISkinParam skinParam, Bibliotekon bibliotekon, UmlDiagramType type) {
		super(entity, EntityImageNote.getSkin(skinParam, entity));
		this.skinParam = skinParam;
		this.bibliotekon = bibliotekon;

		style = getDefaultStyleDefinition(type.getStyleName()).getMergedStyle(skinParam.getCurrentStyleBuilder());
		if (entity.getColors().getColor(ColorType.BACK) == null)
			this.noteBackgroundColor = style.value(PName.BackGroundColor).asColor(skinParam.getIHtmlColorSet());
		else
			this.noteBackgroundColor = entity.getColors().getColor(ColorType.BACK);

		this.borderColor = style.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());

	}

	private StyleSignature getDefaultStyleDefinition(SName sname) {
		return StyleSignatureBasic.of(SName.root, SName.element, sname, SName.note).withTOBECHANGED(getStereo());
	}

	private Position getPosition() {
		if (getEntity().getCodeGetName().endsWith(Position.RIGHT.name()))
			return Position.RIGHT;

		return Position.LEFT;
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		double width = 0;
		double height = 0;
		for (Map.Entry<String, Display> ent : getEntity().getTips().entrySet()) {
			final Display display = ent.getValue();
			final XDimension2D dim = getOpale(display).calculateDimension(stringBounder);
			height += dim.getHeight();
			height += ySpacing;
			width = Math.max(width, dim.getWidth());
		}
		return new XDimension2D(width, height);
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		final IEntity other = bibliotekon.getOnlyOther(getEntity());

		final SvekNode nodeMe = bibliotekon.getNode(getEntity());
		final SvekNode nodeOther = bibliotekon.getNode(other);
		final XPoint2D positionMe = nodeMe.getPosition();
		if (nodeOther == null) {
			System.err.println("Error in EntityImageTips");
			return;
		}
		final XPoint2D positionOther = nodeOther.getPosition();
		bibliotekon.getNode(getEntity());
		final Position position = getPosition();
		Direction direction = position.reverseDirection();
		double height = 0;
		for (Map.Entry<String, Display> ent : getEntity().getTips().entrySet()) {
			final Display display = ent.getValue();
			final XRectangle2D memberPosition = nodeOther.getImage().getInnerPosition(ent.getKey(), stringBounder,
					InnerStrategy.STRICT);
			if (memberPosition == null)
				return;

			final Opale opale = getOpale(display);
			final XDimension2D dim = opale.calculateDimension(stringBounder);
			final XPoint2D pp1 = new XPoint2D(0, dim.getHeight() / 2);
			double x = positionOther.getX() - positionMe.getX();
			if (direction == Direction.RIGHT && x < 0)
				direction = direction.getInv();

			if (direction == Direction.LEFT)
				x += memberPosition.getMaxX();
			else
				x += 4;

			final double y = positionOther.getY() - positionMe.getY() - height + memberPosition.getCenterY();
			final XPoint2D pp2 = new XPoint2D(x, y);
			opale.setOpale(direction, pp1, pp2);
			opale.drawU(ug);
			ug = ug.apply(UTranslate.dy(dim.getHeight() + ySpacing));
			height += dim.getHeight();
			height += ySpacing;
		}

	}

	private Opale getOpale(final Display display) {

		final double shadowing = style.value(PName.Shadowing).asDouble();
		final FontConfiguration fc = style.getFontConfiguration(skinParam.getIHtmlColorSet());
		final UStroke stroke = style.getStroke();

		final TextBlock textBlock = BodyFactory.create3(display, skinParam, HorizontalAlignment.LEFT, fc,
				style.wrapWidth(), style);
		return new Opale(shadowing, borderColor, noteBackgroundColor, textBlock, true, stroke);
	}

}
