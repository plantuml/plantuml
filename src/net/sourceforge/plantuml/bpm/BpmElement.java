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
package net.sourceforge.plantuml.bpm;

import net.atmp.InnerStrategy;
import net.sourceforge.plantuml.activitydiagram3.ftile.BoxStyle;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileBox;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileCircleStart;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDiamond;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XRectangle2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.skin.ColorParam;
import net.sourceforge.plantuml.skin.SkinParamUtils;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

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
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				raw.drawU(ug);
				ug = ug.apply(HColors.RED);
				for (Where w : Where.values()) {
					if (have(w)) {
						drawLine(ug, w, raw.calculateDimension(ug.getStringBounder()));
					}
				}
			}

			public XRectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
				return raw.getInnerPosition(member, stringBounder, strategy);
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return raw.calculateDimension(stringBounder);
			}

			public MinMax getMinMax(StringBounder stringBounder) {
				return raw.getMinMax(stringBounder);
			}
		};
	}

	private void drawLine(UGraphic ug, Where w, XDimension2D total) {
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

	private StyleSignatureBasic getSignatureCircle() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.circle);
	}

	private Style getStyle(ISkinParam skinParam) {
		return getSignatureCircle().getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	public TextBlock toTextBlockInternal(ISkinParam skinParam) {
		if (type == BpmElementType.START) {
			return new FtileCircleStart(skinParam, null, getStyle(skinParam));
		}
		if (type == BpmElementType.MERGE) {
			final HColor borderColor = SkinParamUtils.getColor(skinParam, null, ColorParam.activityBorder);
			final HColor backColor = SkinParamUtils.getColor(skinParam, null, ColorParam.activityBackground);
			return new FtileDiamond(skinParam, backColor, borderColor, null);
		}
		if (type == BpmElementType.DOCKED_EVENT) {
			final UFont font = UFont.serif(14);
			return FtileBox.create(skinParam, display, null, BoxStyle.PLAIN, null);
		}
		final UFont font = UFont.serif(14);
		final FontConfiguration fc = FontConfiguration.create(font, HColors.RED, HColors.RED, null);
		if (Display.isNull(display)) {
			return Display.getWithNewlines(type.toString()).create(fc, HorizontalAlignment.LEFT, skinParam);
		}
		return display.create(fc, HorizontalAlignment.LEFT, skinParam);
	}

	private XDimension2D dimension;

	public XDimension2D getDimension(StringBounder stringBounder, ISkinParam skinParam) {
		if (dimension == null) {
			dimension = toTextBlock(skinParam).calculateDimension(stringBounder);
		}
		return dimension;
	}

	public final String getId() {
		return id;
	}

}
