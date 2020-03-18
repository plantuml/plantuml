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
package net.sourceforge.plantuml.skin.rose;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.skin.ArrowComponent;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.Padder;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public abstract class AbstractComponentRoseArrow extends AbstractTextualComponent implements ArrowComponent {

	private final int arrowDeltaX = 10;
	private final int arrowDeltaY = 4;
	private final HColor foregroundColor;
	private final ArrowConfiguration arrowConfiguration;

	public AbstractComponentRoseArrow(Style style, HColor foregroundColor, FontConfiguration font,
			Display stringsToDisplay, ArrowConfiguration arrowConfiguration, ISkinSimple spriteContainer,
			HorizontalAlignment textHorizontalAlignment, LineBreakStrategy maxMessageSize) {
		super(style, maxMessageSize, stringsToDisplay, font, textHorizontalAlignment, 7, 7, 1, spriteContainer, false,
				null, null);
		if (SkinParam.USE_STYLES()) {
			this.foregroundColor = style.value(PName.LineColor).asColor(getIHtmlColorSet());
			final UStroke stroke = style.getStroke();
			this.arrowConfiguration = arrowConfiguration.withThickness(stroke.getThickness());
		} else {
			this.foregroundColor = foregroundColor;
			this.arrowConfiguration = arrowConfiguration;
		}
	}

	@Override
	final protected TextBlock getTextBlock() {
		final Padder padder = getISkinSimple() instanceof ISkinParam ? ((ISkinParam) getISkinSimple())
				.getSequenceDiagramPadder() : Padder.NONE;

		return padder.apply(super.getTextBlock());
	}

	abstract public double getYPoint(StringBounder stringBounder);

	protected final HColor getForegroundColor() {
		return foregroundColor;
	}

	final protected int getArrowDeltaX() {
		return arrowDeltaX;
	}

	final protected int getArrowDeltaY() {
		return arrowDeltaY;
	}

	@Override
	public final double getPaddingY() {
		return 4;
	}

	public final ArrowConfiguration getArrowConfiguration() {
		return arrowConfiguration;
	}

}
