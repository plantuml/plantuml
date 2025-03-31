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
import java.util.Objects;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.cucadiagram.BodyFactory;
import net.sourceforge.plantuml.klimt.UGroup;
import net.sourceforge.plantuml.klimt.UGroupType;
import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.creole.Stencil;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.drawing.UGraphicStencil;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XLine2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.DotPath;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockEmpty;
import net.sourceforge.plantuml.sdot.SmetanaEdge;
import net.sourceforge.plantuml.skin.ColorParam;
import net.sourceforge.plantuml.skin.CornerParam;
import net.sourceforge.plantuml.skin.SkinParamBackcolored;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.svek.SvekEdge;
import net.sourceforge.plantuml.svek.SvekNode;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.utils.Direction;

public class EntityImageNote extends AbstractEntityImage implements Stencil {

	private final HColor noteBackgroundColor;
	private final HColor borderColor;
	private final double shadowing;
	private final int marginX1 = 6;
	private final int marginX2 = 15;
	private final int marginY = 5;

	private final Style style;

	private final TextBlock textBlock;

	public EntityImageNote(Entity entity) {
		super(entity);

		final Display strings = entity.getDisplay();

		this.style = getDefaultStyleDefinition(getStyleName()).getMergedStyle(getSkinParam().getCurrentStyleBuilder());
		if (entity.getColors().getColor(ColorType.BACK) == null)
			this.noteBackgroundColor = style.value(PName.BackGroundColor).asColor(getSkinParam().getIHtmlColorSet());
		else
			this.noteBackgroundColor = entity.getColors().getColor(ColorType.BACK);

		this.borderColor = style.value(PName.LineColor).asColor(getSkinParam().getIHtmlColorSet());
		this.shadowing = style.getShadowing();

		final FontConfiguration fontConfiguration = style.getFontConfiguration(getSkinParam().getIHtmlColorSet());
		final HorizontalAlignment horizontalAlignment = style.getHorizontalAlignment();

		if (strings.size() == 1 && strings.get(0).length() == 0)
			textBlock = new TextBlockEmpty();
		else
			textBlock = BodyFactory.create3(strings, getSkinParam(), horizontalAlignment, fontConfiguration,
					style.wrapWidth(), style);

	}

	private static ISkinParam getISkinParam(ISkinParam skinParam, Entity entity) {
		if (entity.getColors() != null)
			return entity.getColors().mute(skinParam);

		return skinParam;
	}

	static ISkinParam getSkin(ISkinParam skinParam, Entity entity) {
		final Stereotype stereotype = entity.getStereotype();
		HColor back = entity.getColors().getColor(ColorType.BACK);
		if (back != null)
			return new SkinParamBackcolored(skinParam, back);

		back = getColorStatic(skinParam, ColorParam.noteBackground, stereotype);
		if (back != null)
			return new SkinParamBackcolored(skinParam, back);

		return skinParam;
	}

	private static HColor getColorStatic(ISkinParam skinParam, ColorParam colorParam, Stereotype stereo) {
		final Rose rose = new Rose();
		return rose.getHtmlColor(skinParam, stereo, colorParam);
	}

	final public double getPreferredWidth(StringBounder stringBounder) {
		final double result = getTextWidth(stringBounder);
		return result;
	}

	final public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder);
	}

	private XDimension2D getSize(StringBounder stringBounder, final TextBlock textBlock) {
		return textBlock.calculateDimension(stringBounder);
	}

	final protected double getTextHeight(StringBounder stringBounder) {
		final TextBlock textBlock = getTextBlock();
		final XDimension2D size = getSize(stringBounder, textBlock);
		return size.getHeight() + 2 * marginY;
	}

	final protected TextBlock getTextBlock() {
		return textBlock;
	}

	final protected double getPureTextWidth(StringBounder stringBounder) {
		final TextBlock textBlock = getTextBlock();
		final XDimension2D size = getSize(stringBounder, textBlock);
		return size.getWidth();
	}

	final public double getTextWidth(StringBounder stringBounder) {
		return getPureTextWidth(stringBounder) + marginX1 + marginX2;
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		final double height = getPreferredHeight(stringBounder);
		final double width = getPreferredWidth(stringBounder);
		return new XDimension2D(width, height);
	}

	private StyleSignature getDefaultStyleDefinition(SName sname) {
		return StyleSignatureBasic.of(SName.root, SName.element, sname, SName.note).withTOBECHANGED(getStereo());
	}

	final public void drawU(UGraphic ug) {
		final Url url = getEntity().getUrl99();

		final UGroup group = new UGroup(getEntity().getLocation());
		group.put(UGroupType.CLASS, "entity");
		group.put(UGroupType.ID, "entity_" + getEntity().getName());
		group.put(UGroupType.DATA_ENTITY, getEntity().getName());
		group.put(UGroupType.DATA_UID, getEntity().getUid());
		group.put(UGroupType.DATA_QUALIFIED_NAME, getEntity().getQuark().getQualifiedName());
		ug.startGroup(group);

		if (url != null)
			ug.startUrl(url);

		final UGraphic ug2 = UGraphicStencil.create(ug, this, UStroke.simple());
		if (opaleLink != null) {
			final StringBounder stringBounder = ug.getStringBounder();

			final SmetanaEdge smetanaEdged = smetanaPathes.get(opaleLink);
			final UTranslate move = new UTranslate(-node.getMinX(), -node.getMinY());

			final XPoint2D startPoint = move.getTranslated(smetanaEdged.getStartPoint());
			final XPoint2D endPoint = move.getTranslated(smetanaEdged.getEndPoint());

			final UTranslate force1 = getMagneticBorder().getForceAt(stringBounder, smetanaEdged.getStartPoint());
			final UTranslate force2 = other.getMagneticBorder().getForceAt(stringBounder, smetanaEdged.getEndPoint());

			final double textWidth = getTextWidth(stringBounder);
			final double textHeight = getTextHeight(stringBounder);
			final XPoint2D center = new XPoint2D(textWidth / 2, textHeight / 2);

			XPoint2D pp1 = force2.getTranslated(startPoint);
			XPoint2D pp2 = force1.getTranslated(endPoint);
			if (pp1.distance(center) < pp2.distance(center)) {
				pp1 = force1.getTranslated(endPoint);
				pp2 = force2.getTranslated(startPoint);
			}

			final Direction strategy = getOpaleStrategy(textWidth, textHeight, pp2);
			final Opale opale = new Opale(shadowing, borderColor, noteBackgroundColor, textBlock, true, getStroke());
			opale.setRoundCorner(getRoundCorner());
			opale.setOpale(strategy, pp2, pp1);
			final UGraphic stroked = applyStroke(ug2);
			opale.drawU(Colors.applyStroke(stroked, getEntity().getColors()));

		} else if (opaleLine == null || opaleLine.isOpale() == false) {
			drawNormal(ug2);
		} else {
			final StringBounder stringBounder = ug.getStringBounder();
			DotPath path = opaleLine.getDotPath();

			final UTranslate force1 = getMagneticBorder().getForceAt(stringBounder, path.getStartPoint());
			final UTranslate force2 = other.getMagneticBorder().getForceAt(stringBounder, path.getEndPoint());

			path.moveDelta(-node.getMinX(), -node.getMinY());

			final double textWidth = getTextWidth(stringBounder);
			final double textHeight = getTextHeight(stringBounder);
			final XPoint2D center = new XPoint2D(textWidth / 2, textHeight / 2);
			if (path.getStartPoint().distance(center) > path.getEndPoint().distance(center))
				path = path.reverse();

			final Direction strategy = getOpaleStrategy(textWidth, textHeight, path.getStartPoint());
			final XPoint2D pp1 = force1.getTranslated(path.getStartPoint());
			final XPoint2D pp2 = force2.getTranslated(path.getEndPoint());

			final Opale opale = new Opale(shadowing, borderColor, noteBackgroundColor, textBlock, true, getStroke());
			opale.setRoundCorner(getRoundCorner());
			opale.setOpale(strategy, pp1, pp2);
			final UGraphic stroked = applyStroke(ug2);
			opale.drawU(Colors.applyStroke(stroked, getEntity().getColors()));
		}
		if (url != null)
			ug.closeUrl();

		ug.closeGroup();
	}

	private double getRoundCorner() {
		return getSkinParam().getRoundCorner(CornerParam.DEFAULT, null);
	}

	private void drawNormal(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final UPath polygon = Opale.getPolygonNormal(getTextWidth(stringBounder), getTextHeight(stringBounder),
				getRoundCorner());

		polygon.setDeltaShadow(this.shadowing);

		ug = ug.apply(noteBackgroundColor.bg()).apply(borderColor);
		final UGraphic stroked = applyStroke(ug);
		stroked.draw(polygon);
		ug.draw(Opale.getCorner(getTextWidth(stringBounder), getRoundCorner()));

		getTextBlock().drawU(ug.apply(new UTranslate(marginX1, marginY)));
	}

	private UGraphic applyStroke(UGraphic ug) {
		return ug.apply(style.getStroke());

	}

	private UStroke getStroke() {
		return style.getStroke();
	}

	private Direction getOpaleStrategy(double width, double height, XPoint2D pt) {
		final double d1 = getOrthoDistance(new XLine2D(width, 0, width, height), pt);
		final double d2 = getOrthoDistance(new XLine2D(0, height, width, height), pt);
		final double d3 = getOrthoDistance(new XLine2D(0, 0, 0, height), pt);
		final double d4 = getOrthoDistance(new XLine2D(0, 0, width, 0), pt);
		if (d3 <= d1 && d3 <= d2 && d3 <= d4)
			return Direction.LEFT;

		if (d1 <= d2 && d1 <= d3 && d1 <= d4)
			return Direction.RIGHT;

		if (d4 <= d1 && d4 <= d2 && d4 <= d3)
			return Direction.UP;

		if (d2 <= d1 && d2 <= d3 && d2 <= d4)
			return Direction.DOWN;

		return null;

	}

	private static double getOrthoDistance(XLine2D seg, XPoint2D pt) {
		if (isHorizontal(seg))
			return Math.abs(seg.getP1().getY() - pt.getY());

		if (isVertical(seg))
			return Math.abs(seg.getP1().getX() - pt.getX());

		throw new IllegalArgumentException();
	}

	private static boolean isHorizontal(XLine2D seg) {
		return seg.getP1().getY() == seg.getP2().getY();
	}

	private static boolean isVertical(XLine2D seg) {
		return seg.getP1().getX() == seg.getP2().getX();
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	private SvekEdge opaleLine;
	private Link opaleLink;
	private SvekNode node;
	private SvekNode other;
	private Map<Link, SmetanaEdge> smetanaPathes;

	public void setOpaleLine(SvekEdge line, SvekNode node, SvekNode other) {
		this.opaleLine = line;
		this.node = node;
		this.other = Objects.requireNonNull(other);
	}

	public void setOpaleLink(Link link, SvekNode node, SvekNode other, Map<Link, SmetanaEdge> edges) {
		this.opaleLink = link;
		this.node = node;
		this.other = Objects.requireNonNull(other);
		this.smetanaPathes = edges;
	}

	public double getStartingX(StringBounder stringBounder, double y) {
		return 0;
	}

	public double getEndingX(StringBounder stringBounder, double y) {
		return calculateDimension(stringBounder).getWidth();
	}

}
