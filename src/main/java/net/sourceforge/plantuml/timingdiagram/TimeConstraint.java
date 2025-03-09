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
 */
package net.sourceforge.plantuml.timingdiagram;

import java.util.List;
import java.util.Objects;

import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.utils.Direction;

public class TimeConstraint {

	private final TimeTick tick1;
	private final TimeTick tick2;
	private final Display label;
	private final ISkinParam skinParam;
	private final StyleBuilder styleBuilder;
	private final ArrowConfiguration config;
	private final double marginx;

	public TimeConstraint(double marginx, TimeTick tick1, TimeTick tick2, String label, ISkinParam skinParam,
			ArrowConfiguration config) {
		this.marginx = marginx;
		this.tick1 = Objects.requireNonNull(tick1);
		this.tick2 = Objects.requireNonNull(tick2);
		this.label = Display.getWithNewlines(skinParam.getPragma(), label);
		this.skinParam = skinParam;
		this.styleBuilder = skinParam.getCurrentStyleBuilder();
		this.config = config;
	}

	public final boolean containsStrict(TimeTick other) {
		return tick1.compareTo(other) < 0 && tick2.compareTo(other) > 0;
	}

	public final TimeTick getTick1() {
		return tick1;
	}

	public final TimeTick getTick2() {
		return tick2;
	}

	public final Display getLabel() {
		return label;
	}

	private TextBlock getTextBlock(Display display) {
		return display.create(getFontConfiguration(), HorizontalAlignment.LEFT, skinParam);
	}

	private FontConfiguration getFontConfiguration() {
		return getStyle().getFontConfiguration(skinParam.getIHtmlColorSet());
	}

	public void drawU(UGraphic ug, TimingRuler ruler) {
		final HColor arrowColor = getArrowColor();
		ug = ug.apply(arrowColor).apply(arrowColor.bg());
		final double x1 = ruler.getPosInPixel(tick1) + marginx;
		final double x2 = ruler.getPosInPixel(tick2) - marginx;
		ug = ug.apply(UTranslate.dx(x1));
		final double len = x2 - x1;
		ug.apply(getUStroke()).draw(ULine.hline(len));

		if (len > 10) {
			ug.draw(getPolygon(Direction.LEFT, new XPoint2D(0, 0)));
			ug.draw(getPolygon(Direction.RIGHT, new XPoint2D(len, 0)));
		} else {
			ug.draw(getPolygon(Direction.RIGHT, new XPoint2D(0, 0)));
			ug.draw(getPolygon(Direction.LEFT, new XPoint2D(len, 0)));
		}

		final TextBlock text = getTextBlock(label);
		final XDimension2D dimText = text.calculateDimension(ug.getStringBounder());
		final double x = (len - dimText.getWidth()) / 2;
		text.drawU(ug.apply(new UTranslate(x, -getConstraintHeight(ug.getStringBounder()))));
	}

	private HColor getArrowColor() {
		final HColor configColor = config.getColor();
		if (configColor != null)
			return configColor;
		return getStyle().value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
	}

	private Style getStyle() {
		return getStyleSignature().getMergedStyle(styleBuilder);
	}

	private UStroke getUStroke() {
		return getStyle().getStroke();
	}

	private StyleSignatureBasic getStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.timingDiagram, SName.constraintArrow);
	}

	public double getConstraintHeight(StringBounder stringBounder) {
		final TextBlock text = getTextBlock(label);
		final XDimension2D dimText = text.calculateDimension(stringBounder);
		return dimText.getHeight() + getTopMargin();

	}

	public static double getTopMargin() {
		return 5;
	}

	private UPolygon getPolygon(Direction dir, XPoint2D end) {
		final double dx = 8;
		final double dy = 4;
		final XPoint2D pt1;
		final XPoint2D pt2;
		if (dir == Direction.RIGHT) {
			pt1 = end.move(-dx, dy);
			pt2 = end.move(-dx, -dy);
		} else {
			pt1 = end.move(dx, dy);
			pt2 = end.move(dx, -dy);
		}

		final UPolygon polygon = new UPolygon();
		polygon.addPoint(pt1);
		polygon.addPoint(pt2);
		polygon.addPoint(end);

		return polygon;
	}

	public static double getHeightForConstraints(StringBounder stringBounder, List<TimeConstraint> constraints) {
		if (constraints.size() == 0)
			return 0;

		double result = 0;
		for (TimeConstraint constraint : constraints)
			result = Math.max(result, constraint.getConstraintHeight(stringBounder));

		return result;
	}

}
