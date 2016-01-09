/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 18280 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.awt.Font;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.svek.IEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.UAntiAliasing;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class GraphicStrings extends AbstractTextBlock implements IEntityImage {

	private final HtmlColor background;

	private final UFont font;

	private final HtmlColor green;

	private final HtmlColor hyperlinkColor = HtmlColorUtils.BLUE;

	private final boolean useUnderlineForHyperlink = true;

	private final List<String> strings;

	private final BufferedImage image;

	private final GraphicPosition position;

	private final UAntiAliasing antiAliasing;

	private final ColorMapper colorMapper = new ColorMapperIdentity();

	public static GraphicStrings createDefault(List<String> strings, boolean useRed) {
		if (useRed) {
			return new GraphicStrings(strings, new UFont("SansSerif", Font.BOLD, 14), HtmlColorUtils.BLACK,
					HtmlColorUtils.RED_LIGHT, UAntiAliasing.ANTI_ALIASING_ON);
		}
		return new GraphicStrings(strings, new UFont("SansSerif", Font.BOLD, 14), HtmlColorSet.getInstance()
				.getColorIfValid("#33FF02"), HtmlColorUtils.BLACK, UAntiAliasing.ANTI_ALIASING_ON);
	}

	public GraphicStrings(List<String> strings, UFont font, HtmlColor green, HtmlColor background,
			UAntiAliasing antiAliasing) {
		this(strings, font, green, background, antiAliasing, null, null);
	}

	public GraphicStrings(List<String> strings, UFont font, HtmlColor green, HtmlColor background,
			UAntiAliasing antiAliasing, BufferedImage image, GraphicPosition position) {
		this.strings = strings;
		this.font = font;
		this.green = green;
		this.background = background;
		this.image = image;
		this.position = position;
		this.antiAliasing = antiAliasing;
	}

	private double minWidth;

	public void setMinWidth(double minWidth) {
		this.minWidth = minWidth;
	}

	private int maxLine = 0;

	private TextBlock getTextBlock() {
		TextBlock result = null;
		if (maxLine == 0) {
			result = Display.create(strings).create(
					new FontConfiguration(font, green, hyperlinkColor, useUnderlineForHyperlink),
					HorizontalAlignment.LEFT, new SpriteContainerEmpty());
		} else {
			for (int i = 0; i < strings.size(); i += maxLine) {
				final int n = Math.min(i + maxLine, strings.size());
				final TextBlock textBlock1 = Display.create(strings.subList(i, n)).create(
						new FontConfiguration(font, green, hyperlinkColor, useUnderlineForHyperlink),
						HorizontalAlignment.LEFT, new SpriteContainerEmpty());
				if (result == null) {
					result = textBlock1;
				} else {
					result = TextBlockUtils.withMargin(result, 0, 10, 0, 0);
					result = TextBlockUtils.mergeLR(result, textBlock1, VerticalAlignment.TOP);
				}
			}
		}
		result = DateEventUtils.addEvent(result, green);
		return result;
	}

	public void drawU(UGraphic ug) {
		final Dimension2D size = calculateDimension(ug.getStringBounder());
		getTextBlock().drawU(ug.apply(new UChangeColor(green)));

		if (image != null) {
			if (position == GraphicPosition.BOTTOM) {
				ug.apply(new UTranslate((size.getWidth() - image.getWidth()) / 2, size.getHeight() - image.getHeight()))
						.draw(new UImage(image));
			} else if (position == GraphicPosition.BACKGROUND_CORNER_BOTTOM_RIGHT) {
				ug.apply(new UTranslate(size.getWidth() - image.getWidth(), size.getHeight() - image.getHeight()))
						.draw(new UImage(image));
			} else if (position == GraphicPosition.BACKGROUND_CORNER_TOP_RIGHT) {
				ug.apply(new UTranslate(size.getWidth() - image.getWidth() - 1, 1)).draw(new UImage(image));
			}
		}
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		Dimension2D dim = getTextBlock().calculateDimension(stringBounder);
		if (dim.getWidth() < minWidth) {
			dim = new Dimension2DDouble(minWidth, dim.getHeight());
		}
		if (image != null) {
			if (position == GraphicPosition.BOTTOM) {
				dim = new Dimension2DDouble(dim.getWidth(), dim.getHeight() + image.getHeight());
			} else if (position == GraphicPosition.BACKGROUND_CORNER_BOTTOM_RIGHT) {
				dim = new Dimension2DDouble(dim.getWidth() + image.getWidth(), dim.getHeight());
			} else if (position == GraphicPosition.BACKGROUND_CORNER_TOP_RIGHT) {
				dim = new Dimension2DDouble(dim.getWidth() + image.getWidth(), dim.getHeight());
			}
		}
		return dim;
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	public HtmlColor getBackcolor() {
		return background;
	}

	public int getShield() {
		return 0;
	}

	public boolean isHidden() {
		return false;
	}

	public final void setMaxLine(int maxLine) {
		this.maxLine = maxLine;
	}

}
