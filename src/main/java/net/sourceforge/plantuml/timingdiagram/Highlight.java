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

import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class Highlight {

	private final TimeTick tickFrom;
	private final TimeTick tickTo;
	private final Display caption;
	private final Colors colors;
	private final ISkinParam skinParam;

	public Highlight(ISkinParam skinParam, TimeTick tickFrom, TimeTick tickTo, Display caption, Colors colors) {
		this.tickFrom = tickFrom;
		this.tickTo = tickTo;
		this.caption = caption;
		this.colors = colors;
		this.skinParam = skinParam;
	}

	private StyleSignatureBasic getStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.timingDiagram, SName.highlight);
	}

	private Style getStyle() {
		return getStyleSignature().getMergedStyle(skinParam.getCurrentStyleBuilder());

	}

	private HColor getBackColor() {
		final HColor result = colors.getColor(ColorType.BACK);
		if (result == null)
			return getStyle().value(PName.BackGroundColor).asColor(skinParam.getIHtmlColorSet());

		return result;
	}

	private HColor getLineColor() {
		final HColor result = colors.getColor(ColorType.LINE);
		if (result == null)
			return getStyle().value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());

		return result;
	}

	private UStroke getUStroke() {
		return getStyle().getStroke();
	}

	public final TimeTick getTickFrom() {
		return tickFrom;
	}

	public final TimeTick getTickTo() {
		return tickTo;
	}

	public final Display getCaption() {
		return caption;
	}

	public TextBlock getCaption(ISkinParam skinParam) {
		final FontConfiguration fc = FontConfiguration.create(skinParam, FontParam.TIMING, null);
		return caption.create(fc, HorizontalAlignment.LEFT, skinParam);
	}

	public void drawHighlightsBack(UGraphic ug, TimingRuler ruler, double height) {
		ug = ug.apply(HColors.none()).apply(getBackColor().bg());
		final double start = ruler.getPosInPixel(this.getTickFrom());
		final double end = ruler.getPosInPixel(this.getTickTo());
		final URectangle rect = URectangle.build(end - start, height);
		ug.apply(UTranslate.dx(start)).draw(rect);
	}

	public void drawHighlightsLines(UGraphic ug, TimingRuler ruler, double height) {
		ug = ug.apply(getUStroke());
		ug = ug.apply(getLineColor());
		final ULine line = ULine.vline(height);
		final double start = ruler.getPosInPixel(this.getTickFrom());
		final double end = ruler.getPosInPixel(this.getTickTo());
		ug.apply(UTranslate.dx(start)).draw(line);
		ug.apply(UTranslate.dx(end)).draw(line);
	}

}
