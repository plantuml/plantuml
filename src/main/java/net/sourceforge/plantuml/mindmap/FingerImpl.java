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
package net.sourceforge.plantuml.mindmap;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileBoxOld;
import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.Rankdir;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.skin.SkinParamColors;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;

public class FingerImpl implements Finger, UDrawable {

	private final Idea idea;
	private final ISkinParam skinParam;
	private final int direction;
	private boolean drawPhalanx = true;

	private final List<FingerImpl> nail = new ArrayList<>();
	private Tetris tetris = null;

	public static FingerImpl build(Idea idea, ISkinParam skinParam, boolean direction) {
		final FingerImpl result = new FingerImpl(idea, skinParam, direction);
		for (Idea child : idea.getChildren())
			result.addInNail(build(child, skinParam, direction));

		return result;
	}

	private boolean isTopToBottom() {
		return skinParam.getRankdir() == Rankdir.TOP_TO_BOTTOM;
	}

	public void addInNail(FingerImpl child) {
		nail.add(child);
	}

	private FingerImpl(Idea idea, ISkinParam skinParam, boolean direction) {
		this.idea = idea;
		this.skinParam = skinParam;
		this.direction = direction ? 1 : -1;
	}

	private ClockwiseTopRightBottomLeft getMargin() {
		return getStyle().getMargin();
	}

	public void drawU(final UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final TextBlock phalanx = getPhalanx();
		final XDimension2D dimPhalanx = phalanx.calculateDimension(stringBounder);
		if (drawPhalanx) {
			final double posX;
			final double posY;
			if (isTopToBottom()) {
				posX = -getPhalanxThickness(stringBounder) / 2;
				posY = direction == 1 ? 0 : -dimPhalanx.getHeight();
			} else {
				posX = direction == 1 ? 0 : -dimPhalanx.getWidth();
				posY = -getPhalanxThickness(stringBounder) / 2;
			}
			phalanx.drawU(ug.apply(new UTranslate(posX, posY)));
		}
		final XPoint2D p1;
		if (isTopToBottom())
			p1 = new XPoint2D(0, direction * dimPhalanx.getHeight());
		else
			p1 = new XPoint2D(direction * dimPhalanx.getWidth(), 0);

		for (int i = 0; i < nail.size(); i++) {
			final FingerImpl child = nail.get(i);
			final SymetricalTeePositioned stp = getTetris(stringBounder).getElements().get(i);
			final XPoint2D p2;
			if (isTopToBottom())
				p2 = new XPoint2D(stp.getY(), direction * (dimPhalanx.getHeight() + getX12()));
			else
				p2 = new XPoint2D(direction * (dimPhalanx.getWidth() + getX12()), stp.getY());

			child.drawU(ug.apply(UTranslate.point(p2)));
			final HColor linkColor = getLinkColor();
			if (linkColor.isTransparent() == false)
				drawLine(ug.apply(linkColor).apply(getUStroke()), p1, p2);
		}

	}

	private HColor getLinkColor() {
		final Style styleArrow = getStyleArrow();
		return styleArrow.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
	}

	private UStroke getUStroke() {
		final Style styleArrow = getStyleArrow();
		return styleArrow.getStroke();
	}

	private void drawLine(UGraphic ug, XPoint2D p1, XPoint2D p2) {
		final UPath path = UPath.none();
		path.moveTo(p1);
		if (isTopToBottom()) {
			final double delta1 = direction * 3;
			final double delta2 = direction * 10;
			path.lineTo(p1.getX(), p1.getY() + delta1);
			path.cubicTo(p1.getX(), p1.getY() + delta2, p2.getX(), p2.getY() - delta2, p2.getX(), p2.getY() - delta1);
		} else {
			final double delta1 = direction * 10;
			final double delta2 = direction * 25;
			path.lineTo(p1.getX() + delta1, p1.getY());
			path.cubicTo(p1.getX() + delta2, p1.getY(), p2.getX() - delta2, p2.getY(), p2.getX() - delta1, p2.getY());
		}
		path.lineTo(p2);
		ug.draw(path);
	}

	private Tetris getTetris(StringBounder stringBounder) {
		if (tetris == null) {
			tetris = new Tetris(idea.getLabel().toString());
			for (FingerImpl child : nail)
				tetris.add(child.asSymetricalTee(stringBounder));

			tetris.balance();
		}
		return tetris;
	}

	private SymetricalTee asSymetricalTee(StringBounder stringBounder) {
		final double thickness1 = getPhalanxThickness(stringBounder);
		final double elongation1 = getPhalanxElongation(stringBounder);
		if (nail.size() == 0)
			return new SymetricalTee(thickness1, elongation1, 0, 0);

		final double thickness2 = getNailThickness(stringBounder);
		final double elongation2 = getNailElongation(stringBounder);
		return new SymetricalTee(thickness1, elongation1 + getX1(), thickness2, getX2() + elongation2);
	}

	private double getX1() {
		if (isTopToBottom())
			return getMargin().getTop();
		else
			return getMargin().getLeft();
	}

	private double getX2() {
		if (isTopToBottom())
			return getMargin().getBottom() + 5;
		else
			return getMargin().getRight() + 30;
	}

	public double getX12() {
		return getX1() + getX2();
	}

	public double getPhalanxThickness(StringBounder stringBounder) {
		if (isTopToBottom())
			return getPhalanx().calculateDimension(stringBounder).getWidth();
		return getPhalanx().calculateDimension(stringBounder).getHeight();
	}

	public double getPhalanxElongation(StringBounder stringBounder) {
		if (isTopToBottom())
			return getPhalanx().calculateDimension(stringBounder).getHeight();
		return getPhalanx().calculateDimension(stringBounder).getWidth();
	}

	private TextBlock getPhalanx() {
		if (drawPhalanx == false)
			return TextBlockUtils.empty(0, 0);

		final Style style = getStyle();

		if (idea.getShape() == IdeaShape.BOX) {
			final ISkinParam foo = new SkinParamColors(skinParam,
					Colors.empty().add(ColorType.BACK, idea.getBackColor()));
			final TextBlock box = FtileBoxOld.createMindMap(style, foo, idea.getLabel());
			final ClockwiseTopRightBottomLeft margin = getMargin();
			if (isTopToBottom())
				return TextBlockUtils.withMargin(box, margin.getLeft(), margin.getRight(), 0, 0);
			else
				return TextBlockUtils.withMargin(box, 0, 0, margin.getTop(), margin.getBottom());
		}

		assert idea.getShape() == IdeaShape.NONE;
		final TextBlock text = idea.getLabel().create0(style.getFontConfiguration(skinParam.getIHtmlColorSet()),
				style.getHorizontalAlignment(), skinParam, style.wrapWidth(), CreoleMode.FULL, null, null);
		if (direction == 1)
			return TextBlockUtils.withMargin(text, 3, 0, 1, 1);

		return TextBlockUtils.withMargin(text, 0, 3, 1, 1);
	}

	private Style getStyle() {
		if (nail.size() != idea.getChildren().size())
			throw new IllegalStateException();

		return idea.getStyle();
	}

	private Style getStyleArrow() {
		return idea.getStyleArrow();
	}

	public double getNailThickness(StringBounder stringBounder) {
		return getTetris(stringBounder).getHeight();
	}

	public double getNailElongation(StringBounder stringBounder) {
		return getTetris(stringBounder).getWidth();
	}

	public double getFullThickness(StringBounder stringBounder) {
		final double thickness1 = getPhalanxThickness(stringBounder);
		final double thickness2 = getNailThickness(stringBounder);
		return Math.max(thickness1, thickness2);
	}

	public double getFullElongation(StringBounder stringBounder) {
		return getPhalanxElongation(stringBounder) + getNailElongation(stringBounder);
	}

	public void doNotDrawFirstPhalanx() {
		this.drawPhalanx = false;
	}

}
