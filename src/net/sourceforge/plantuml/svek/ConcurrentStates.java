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
package net.sourceforge.plantuml.svek;

import java.util.List;

import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public final class ConcurrentStates extends AbstractTextBlock implements IEntityImage {

	private final List<IEntityImage> inners;
	private final Separator separator;
	private final ISkinParam skinParam;
	private final Stereotype stereotype;

	static enum Separator {
		VERTICAL, HORIZONTAL;

		static Separator fromChar(char sep) {
			if (sep == '|')
				return VERTICAL;

			if (sep == '-')
				return HORIZONTAL;

			throw new IllegalArgumentException();
		}

		UTranslate move(XDimension2D dim) {
			if (this == VERTICAL)
				return UTranslate.dx(dim.getWidth());

			return UTranslate.dy(dim.getHeight());
		}

		XDimension2D add(XDimension2D orig, XDimension2D other) {
			if (this == VERTICAL)
				return new XDimension2D(orig.getWidth() + other.getWidth(),
						Math.max(orig.getHeight(), other.getHeight()));

			return new XDimension2D(Math.max(orig.getWidth(), other.getWidth()), orig.getHeight() + other.getHeight());
		}

		void drawSeparator(UGraphic ug, XDimension2D dimTotal) {
			final double THICKNESS_BORDER = 1.5;
			final int DASH = 8;
			ug = ug.apply(new UStroke(DASH, 10, THICKNESS_BORDER));
			if (this == VERTICAL)
				ug.draw(ULine.vline(dimTotal.getHeight() + DASH));
			else
				ug.draw(ULine.hline(dimTotal.getWidth() + DASH));

		}
	}

	public ConcurrentStates(List<IEntityImage> inners, char concurrentSeparator, ISkinParam skinParam,
			Stereotype stereotype) {
		this.separator = Separator.fromChar(concurrentSeparator);
		this.skinParam = skinParam;
		this.stereotype = stereotype;
		this.inners = inners;
	}

	private Style getStyle() {
		return getStyleSignature().getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	private StyleSignatureBasic getStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.stateDiagram, SName.state);
	}

	public void drawU(UGraphic ug) {
		final HColor borderColor = getStyle().value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
		final StringBounder stringBounder = ug.getStringBounder();
		final XDimension2D dimTotal = calculateDimension(stringBounder);

		for (int i = 0; i < inners.size(); i++) {
			final IEntityImage inner = inners.get(i);
			inner.drawU(ug);
			final XDimension2D dim = inner.calculateDimension(stringBounder);
			ug = ug.apply(separator.move(dim));
			if (i < inners.size() - 1)
				separator.drawSeparator(ug.apply(borderColor), dimTotal);

		}

	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		XDimension2D result = new XDimension2D(0, 0);
		for (IEntityImage inner : inners) {
			final XDimension2D dim = inner.calculateDimension(stringBounder);
			result = separator.add(result, dim);
		}
		return result;
	}

	public HColor getBackcolor() {
		return skinParam.getBackgroundColor();
	}

	public double getOverscanX(StringBounder stringBounder) {
		return 0;
	}

	public boolean isHidden() {
		return false;
	}

	public Margins getShield(StringBounder stringBounder) {
		return Margins.NONE;
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

}
