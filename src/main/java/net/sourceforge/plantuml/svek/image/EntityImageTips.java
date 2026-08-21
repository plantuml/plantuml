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

import java.util.Map;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.Tip;
import net.sourceforge.plantuml.cucadiagram.BodyFactory;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.drawing.UGraphicStencil;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.geom.XRectangle2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.Bibliotekon;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.svek.SvekNode;
import net.sourceforge.plantuml.utils.Direction;
import net.sourceforge.plantuml.utils.Position;

public class EntityImageTips extends AbstractEntityImage {

	private final Bibliotekon bibliotekon;

	private final double ySpacing = 10;

	public EntityImageTips(Entity entity, Bibliotekon bibliotekon) {
		super(entity);
		this.bibliotekon = bibliotekon;
	}

	// Several "note ... of Class::member" tips on the same side of the same
	// class are grouped into this single Entity (one per member, see
	// Entity#getTips), but each tip keeps its own Colors/Stereotype: the
	// style, background and border below must therefore be resolved per
	// Tip -- never once for the whole Entity -- otherwise the last tip
	// parsed silently recolors every earlier tip on the same side of the
	// same class (issue #2814).
	private Style getStyleFor(Tip tip) {
		return getStyleSignatureFor(tip.getStereotype()).getMergedStyle(getSkinParam().getCurrentStyleBuilder());
	}

	private StyleSignature getStyleSignatureFor(Stereotype stereotype) {
		return StyleSignatureBasic.of(SName.root, SName.element, getStyleName(), SName.note)
				.withTOBECHANGED(stereotype);
	}

	private HColor getNoteBackgroundColor(Tip tip, Style style) {
		final HColor fromTip = tip.getColors().getColor(ColorType.BACK);
		if (fromTip == null)
			return style.value(PName.BackGroundColor).asColor(getSkinParam().getIHtmlColorSet());

		return fromTip;
	}

	@Override
	public StyleSignature getStyleSignature() {
		return getStyleSignatureFor(getStereo());
	}

	private Position getPosition() {
		if (getEntity().getName().endsWith(Position.RIGHT.name()))
			return Position.RIGHT;

		return Position.LEFT;
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	@Override
	public XDimension2D calculateDimensionSlow(StringBounder stringBounder) {
		double width = 0;
		double height = 0;
		for (Map.Entry<String, Tip> ent : getEntity().getTips().entrySet()) {
			final Tip tip = ent.getValue();
			final XDimension2D dim = getOpale(tip).calculateDimension(stringBounder);
			height += dim.getHeight();
			height += ySpacing;
			width = Math.max(width, dim.getWidth());
		}
		return new XDimension2D(width, height);
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		final Entity tmp = getEntity();
		final Entity other = bibliotekon.getOnlyOther(tmp);
		if (other == null) {
			System.err.println("Error1 in EntityImageTips");
			return;
		}

		final SvekNode nodeMe = bibliotekon.getNode(getEntity());
		final SvekNode nodeOther = bibliotekon.getNode(other);
		if (nodeOther == null) {
			System.err.println("Error2 in EntityImageTips");
			return;
		}
		final XPoint2D positionOther = nodeOther.getPosition();
		bibliotekon.getNode(getEntity());
		final Position position = getPosition();
		Direction direction = position.reverseDirection();
		final XPoint2D positionMe = nodeMe.getPosition();
		double height = 0;
		for (Map.Entry<String, Tip> ent : getEntity().getTips().entrySet()) {
			final Tip tip = ent.getValue();
			final String member = ent.getKey();
			final CharSequence bestMatch = nodeOther.getBestMatch(member);
			if (bestMatch == null)
				return;
			final XRectangle2D memberPosition = nodeOther.getImage().getInnerPosition(bestMatch.toString(),
					stringBounder);
			if (memberPosition == null)
				return;

			final Opale opale = getOpale(tip);
			final XDimension2D dim = opale.calculateDimension(stringBounder);
			final XPoint2D pp1 = new XPoint2D(0, dim.getHeight() / 2);
			double x = positionOther.getX() - positionMe.getX();
			if (direction == Direction.RIGHT && x < 0)
				direction = direction.getInv();

			if (direction == Direction.LEFT)
				x += memberPosition.getMaxX();
			else
				x += memberPosition.getMinX();

			final double y = positionOther.getY() - positionMe.getY() - height + memberPosition.getCenterY();
			final XPoint2D pp2 = new XPoint2D(x, y);
			opale.setOpale(direction, pp1, pp2);
			opale.drawU(UGraphicStencil.create(ug, dim));
			ug = ug.apply(UTranslate.dy(dim.getHeight() + ySpacing));
			height += dim.getHeight();
			height += ySpacing;
		}

	}

	private Opale getOpale(final Tip tip) {

		final Style style = getStyleFor(tip);
		final HColor noteBackgroundColor = getNoteBackgroundColor(tip, style);
		final HColor borderColor = style.value(PName.LineColor).asColor(getSkinParam().getIHtmlColorSet());

		final double shadowing = style.getShadowing();
		final FontConfiguration fc = style.getFontConfiguration(getSkinParam().getIHtmlColorSet());
		final UStroke stroke = style.getStroke();

		final TextBlock textBlock = BodyFactory.create3(tip.getDisplay(), getSkinParam(), HorizontalAlignment.LEFT,
				fc, style.wrapWidth(), style);
		return new Opale(shadowing, borderColor, noteBackgroundColor, textBlock, true, stroke);
	}

}
