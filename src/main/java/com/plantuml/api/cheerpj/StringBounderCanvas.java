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
package com.plantuml.api.cheerpj;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import net.sourceforge.plantuml.klimt.font.StringBounderRaw;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;

public class StringBounderCanvas extends StringBounderRaw {

	private final Graphics2D g2d;

	public StringBounderCanvas(Graphics2D g2d) {
		super(g2d.getFontRenderContext());
		this.g2d = g2d;
	}

	@Override
	public boolean matchesProperty(String propertyName) {
		return false;
	}

	@Override
	protected XDimension2D calculateDimensionInternal(UFont font, String text) {
		final Font javaFont = font.getUnderlayingFont(text);
		final FontMetrics fm = g2d.getFontMetrics(javaFont);
		final Rectangle2D rect = fm.getStringBounds(text, g2d);
		return new XDimension2D(rect.getWidth(), rect.getHeight());
	}

}
