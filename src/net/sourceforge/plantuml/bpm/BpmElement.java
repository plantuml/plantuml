/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
 */
package net.sourceforge.plantuml.bpm;

import java.awt.Font;
import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.BoxStyle;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileBox;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileCircleStart;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamond;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UFont;

public class BpmElement implements Placeable {

	private final String id;
	private final BpmElementType type;
	private final Display display;

	public BpmElement(String id, BpmElementType type, String label) {
		this.id = id;
		this.type = type;
		this.display = Display.getWithNewlines(label);
	}

	public BpmElement(String id, BpmElementType type) {
		this(id, type, null);
	}

	@Override
	public String toString() {
		return type.toString() + "(" + id + ")";
	}

	public BpmElementType getType() {
		return type;
	}

	public final Display getDisplay() {
		return display;
	}

	public TextBlock toTextBlock(ISkinParam skinParam) {
		if (type == BpmElementType.START) {
			return new FtileCircleStart(skinParam, HtmlColorUtils.BLACK, null);
		}
		if (type == BpmElementType.MERGE) {
			final HtmlColor borderColor = SkinParamUtils.getColor(skinParam, ColorParam.activityBorder, null);
			final HtmlColor backColor = SkinParamUtils.getColor(skinParam, ColorParam.activityBackground, null);
			return new FtileDiamond(skinParam, backColor, borderColor, null);
		}
		if (type == BpmElementType.DOCKED_EVENT) {
			final UFont font = new UFont("Serif", Font.PLAIN, 14);
			return new FtileBox(skinParam, display, font, null, BoxStyle.PLAIN);
		}
		final UFont font = new UFont("Serif", Font.PLAIN, 14);
		final FontConfiguration fc = new FontConfiguration(font, HtmlColorUtils.RED, HtmlColorUtils.RED, false);
		if (Display.isNull(display)) {
			return Display.getWithNewlines(type.toString()).create(fc, HorizontalAlignment.LEFT, skinParam);
		}
		return display.create(fc, HorizontalAlignment.LEFT, skinParam);
	}

	private Dimension2D dimension;

	public Dimension2D getDimension(StringBounder stringBounder, ISkinParam skinParam) {
		if (dimension == null) {
			dimension = toTextBlock(skinParam).calculateDimension(stringBounder);
		}
		return dimension;
	}

	public final String getId() {
		return id;
	}
}
