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
package net.sourceforge.plantuml.wbs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.AbstractCommonUGraphic;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.utils.Direction;

class ITFComposed extends WBSTextBlock implements ITF {

	private final List<ITF> left;
	private final List<ITF> right;

	private final TextBlock main;

	final private double delta1x = 10;
	final private double marginBottom;// = 15;
	private final WElement idea;

	private ITFComposed(ISkinParam skinParam, WElement idea, List<ITF> left, List<ITF> right) {
		super(skinParam, idea.getStyleBuilder(), idea.getLevel(), idea.getStereotype());
		this.idea = idea;
		this.left = left;
		this.right = right;
		this.main = buildMain(idea);
		final Style style = idea.getStyle();
		this.marginBottom = style.getMargin().getBottom();
	}

	public static ITF build2(ISkinParam skinParam, WElement idea) {
		if (idea.isLeaf())
			return new ITFLeaf(idea, idea.withBackColor(skinParam));

		final List<ITF> left = new ArrayList<>();
		final List<ITF> right = new ArrayList<>();
		for (WElement child : idea.getChildren(Direction.LEFT))
			left.add(build2(skinParam, child));

		for (WElement child : idea.getChildren(Direction.RIGHT))
			right.add(build2(skinParam, child));

		return new ITFComposed(skinParam, idea, left, right);
	}

	final protected double getw1(StringBounder stringBounder) {
		final XDimension2D mainDim = main.calculateDimension(stringBounder);
		final double mainWidth = mainDim.getWidth();
		return Math.max(mainWidth / 2, delta1x + getCollWidth(stringBounder, left));
	}

	final public XPoint2D getT1(StringBounder stringBounder) {
		final double x = getw1(stringBounder);
		final double y = 0;
		return new XPoint2D(x, y);
	}

	final public XPoint2D getT2(StringBounder stringBounder) {
		final XDimension2D mainDim = main.calculateDimension(stringBounder);
		final double x = getw1(stringBounder);
		final double y = mainDim.getHeight();
		return new XPoint2D(x, y);
	}

	final public XPoint2D getF1(StringBounder stringBounder) {
		final XDimension2D mainDim = main.calculateDimension(stringBounder);
		final double x = getw1(stringBounder) - mainDim.getWidth() / 2;
		final double y = mainDim.getHeight() / 2;
		return new XPoint2D(x, y);
	}

	final public XPoint2D getF2(StringBounder stringBounder) {
		final XDimension2D mainDim = main.calculateDimension(stringBounder);
		final double x = getw1(stringBounder) + mainDim.getWidth() / 2;
		final double y = mainDim.getHeight() / 2;
		return new XPoint2D(x, y);
	}

	public final XDimension2D calculateDimension(StringBounder stringBounder) {
		final XDimension2D mainDim = main.calculateDimension(stringBounder);
		final double mainWidth = mainDim.getWidth();
		final double height = mainDim.getHeight() + Math.max(getCollHeight(stringBounder, left, marginBottom),
				getCollHeight(stringBounder, right, marginBottom));
		final double width = Math.max(mainWidth / 2, delta1x + getCollWidth(stringBounder, left))
				+ Math.max(mainWidth / 2, delta1x + getCollWidth(stringBounder, right));
		return new XDimension2D(width, height);
	}

	public void drawU(final UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		final XDimension2D mainDim = main.calculateDimension(stringBounder);

		if (ug instanceof AbstractCommonUGraphic) {
			final UTranslate translate = ((AbstractCommonUGraphic) ug).getTranslate();
			idea.setGeometry(translate, mainDim);
		}

		final double wx = getw1(stringBounder) - mainDim.getWidth() / 2;
		main.drawU(ug.apply(UTranslate.dx(wx)));
		final double x = getw1(stringBounder);
		double y = mainDim.getHeight();
		double lastY1 = y;
		final HColor lineColor = getLinkColor();
		for (ITF child : left) {
			y += marginBottom;
			final XDimension2D childDim = child.calculateDimension(stringBounder);
			lastY1 = y + child.getF2(stringBounder).getY();
			drawLine(ug.apply(lineColor), x - childDim.getWidth() - delta1x + child.getF2(stringBounder).getX(), lastY1, x, lastY1);
			child.drawU(ug.apply(new UTranslate(x - childDim.getWidth() - delta1x, y)));
			y += childDim.getHeight();
		}

		y = mainDim.getHeight();
		double lastY2 = y;
		for (ITF child : right) {
			y += marginBottom;
			final XDimension2D childDim = child.calculateDimension(stringBounder);
			lastY2 = y + child.getF1(stringBounder).getY();
			drawLine(ug.apply(lineColor), x, lastY2, x + delta1x + child.getF1(stringBounder).getX(), lastY2);
			child.drawU(ug.apply(new UTranslate(x + delta1x, y)));
			y += childDim.getHeight();

		}
		drawLine(ug.apply(lineColor), x, mainDim.getHeight(), x, Math.max(lastY1, lastY2));
	}

	private HColor getLinkColor() {
		final Style styleArrow = idea.getStyle().getSignature().withTOBECHANGED(idea.getStereotype())
				.getMergedStyle(idea.getStyleBuilder());
		return styleArrow.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
	}

	final private double getCollWidth(StringBounder stringBounder, Collection<? extends TextBlock> all) {
		double result = 0;
		for (TextBlock child : all)
			result = Math.max(result, child.calculateDimension(stringBounder).getWidth());

		return result;
	}

	final private double getCollHeight(StringBounder stringBounder, Collection<? extends TextBlock> all,
			double deltay) {
		double result = 0;
		for (TextBlock child : all)
			result += deltay + child.calculateDimension(stringBounder).getHeight();

		return result;
	}

}
