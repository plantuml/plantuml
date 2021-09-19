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
import java.awt.geom.Rectangle2D;
import java.util.Map;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.UseStyle;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.cucadiagram.BodyFactory;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.Bibliotekon;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.svek.SvekNode;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class EntityImageTips extends AbstractEntityImage {

	final private Rose rose = new Rose();
	private final ISkinParam skinParam;

	private final HColor noteBackgroundColor;
	private final HColor borderColor;

	private final Bibliotekon bibliotekon;

	private final double ySpacing = 10;

	public EntityImageTips(ILeaf entity, ISkinParam skinParam, Bibliotekon bibliotekon, UmlDiagramType type) {
		super(entity, EntityImageNote.getSkin(skinParam, entity));
		this.skinParam = skinParam;
		this.bibliotekon = bibliotekon;

		if (UseStyle.useBetaStyle()) {
			final Style style = getDefaultStyleDefinition(type.getStyleName())
					.getMergedStyle(skinParam.getCurrentStyleBuilder());

			if (entity.getColors(skinParam).getColor(ColorType.BACK) == null) {
				this.noteBackgroundColor = style.value(PName.BackGroundColor).asColor(skinParam.getThemeStyle(),
						skinParam.getIHtmlColorSet());

			} else {
				this.noteBackgroundColor = entity.getColors(skinParam).getColor(ColorType.BACK);
			}

			this.borderColor = style.value(PName.LineColor).asColor(skinParam.getThemeStyle(),
					skinParam.getIHtmlColorSet());

		} else {

			if (entity.getColors(skinParam).getColor(ColorType.BACK) == null) {
				this.noteBackgroundColor = rose.getHtmlColor(skinParam, ColorParam.noteBackground);
			} else {
				this.noteBackgroundColor = entity.getColors(skinParam).getColor(ColorType.BACK);
			}

			this.borderColor = rose.getHtmlColor(skinParam, ColorParam.noteBorder);

		}
	}

	private StyleSignature getDefaultStyleDefinition(SName sname) {
		return StyleSignature.of(SName.root, SName.element, sname, SName.note);
	}

	private Position getPosition() {
		if (getEntity().getCodeGetName().endsWith(Position.RIGHT.name())) {
			return Position.RIGHT;
		}
		return Position.LEFT;
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		double width = 0;
		double height = 0;
		for (Map.Entry<String, Display> ent : getEntity().getTips().entrySet()) {
			final Display display = ent.getValue();
			final Dimension2D dim = getOpale(display).calculateDimension(stringBounder);
			height += dim.getHeight();
			height += ySpacing;
			width = Math.max(width, dim.getWidth());
		}
		return new Dimension2DDouble(width, height);
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		final IEntity other = bibliotekon.getOnlyOther(getEntity());

		final SvekNode nodeMe = bibliotekon.getNode(getEntity());
		final SvekNode nodeOther = bibliotekon.getNode(other);
		final Point2D positionMe = nodeMe.getPosition();
		if (nodeOther == null) {
			System.err.println("Error in EntityImageTips");
			return;
		}
		final Point2D positionOther = nodeOther.getPosition();
		bibliotekon.getNode(getEntity());
		final Position position = getPosition();
		Direction direction = position.reverseDirection();
		double height = 0;
		for (Map.Entry<String, Display> ent : getEntity().getTips().entrySet()) {
			final Display display = ent.getValue();
			final Rectangle2D memberPosition = nodeOther.getImage().getInnerPosition(ent.getKey(), stringBounder,
					InnerStrategy.STRICT);
			if (memberPosition == null) {
				return;
			}
			final Opale opale = getOpale(display);
			final Dimension2D dim = opale.calculateDimension(stringBounder);
			final Point2D pp1 = new Point2D.Double(0, dim.getHeight() / 2);
			double x = positionOther.getX() - positionMe.getX();
			if (direction == Direction.RIGHT && x < 0) {
				direction = direction.getInv();
			}
			if (direction == Direction.LEFT) {
				x += memberPosition.getMaxX();
			} else {
				x += 4;
			}
			final double y = positionOther.getY() - positionMe.getY() - height + memberPosition.getCenterY();
			final Point2D pp2 = new Point2D.Double(x, y);
			opale.setOpale(direction, pp1, pp2);
			opale.drawU(ug);
			ug = ug.apply(UTranslate.dy(dim.getHeight() + ySpacing));
			height += dim.getHeight();
			height += ySpacing;
		}

	}

	private Opale getOpale(final Display display) {
		final FontConfiguration fc = new FontConfiguration(skinParam, FontParam.NOTE, null);
		final TextBlock textBlock = BodyFactory.create3(display, FontParam.NOTE, skinParam, HorizontalAlignment.LEFT,
				fc, LineBreakStrategy.NONE);
		final double shadowing = skinParam.shadowing(getEntity().getStereotype()) ? 4 : 0;
		final Opale opale = new Opale(shadowing, borderColor, noteBackgroundColor, textBlock, true);
		return opale;
	}

}
