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
 */
package net.sourceforge.plantuml.bpm;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

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
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class BpmElement extends AbstractConnectorPuzzle implements ConnectorPuzzle {

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
		if (id == null) {
			return type.toString() + "(" + display + ")";
		}
		return type.toString() + "(" + id + ")";
	}

	public BpmElementType getType() {
		return type;
	}

	public final Display getDisplay() {
		return display;
	}

	public TextBlock toTextBlock(ISkinParam skinParam) {
		final TextBlock raw = toTextBlockInternal(skinParam);
		return new TextBlock() {

			public void drawU(UGraphic ug) {
				raw.drawU(ug);
				ug = ug.apply(HColorUtils.RED);
				for (Where w : Where.values()) {
					if (have(w)) {
						drawLine(ug, w, raw.calculateDimension(ug.getStringBounder()));
					}
				}
			}

			public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
				return raw.getInnerPosition(member, stringBounder, strategy);
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return raw.calculateDimension(stringBounder);
			}
			
			public MinMax getMinMax(StringBounder stringBounder) {
				return raw.getMinMax(stringBounder);
			}
		};
	}

	private void drawLine(UGraphic ug, Where w, Dimension2D total) {
		final double width = total.getWidth();
		final double height = total.getHeight();
		if (w == Where.WEST) {
			ug.apply(new UTranslate(-10, height / 2)).draw(ULine.hline(10));
		}
		if (w == Where.EAST) {
			ug.apply(new UTranslate(width, height / 2)).draw(ULine.hline(10));
		}
		if (w == Where.NORTH) {
			ug.apply(new UTranslate(width / 2, -10)).draw(ULine.vline(10));
		}
		if (w == Where.SOUTH) {
			ug.apply(new UTranslate(width / 2, height)).draw(ULine.vline(10));
		}
	}

	public TextBlock toTextBlockInternal(ISkinParam skinParam) {
		if (type == BpmElementType.START) {
			return new FtileCircleStart(skinParam, HColorUtils.BLACK, null, null);
		}
		if (type == BpmElementType.MERGE) {
			final HColor borderColor = SkinParamUtils.getColor(skinParam, null, ColorParam.activityBorder);
			final HColor backColor = SkinParamUtils.getColor(skinParam, null, ColorParam.activityBackground);
			return new FtileDiamond(skinParam, backColor, borderColor, null);
		}
		if (type == BpmElementType.DOCKED_EVENT) {
			final UFont font = UFont.serif(14);
			return FtileBox.create(skinParam, display, null, BoxStyle.PLAIN);
		}
		final UFont font = UFont.serif(14);
		final FontConfiguration fc = new FontConfiguration(font, HColorUtils.RED, HColorUtils.RED, false);
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
