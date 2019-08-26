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
package net.sourceforge.plantuml.graphic;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.SpriteContainer;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.sprite.Sprite;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class SingleLine extends AbstractTextBlock implements Line {

	private final List<TextBlock> blocs = new ArrayList<TextBlock>();
	private final HorizontalAlignment horizontalAlignment;

	public static SingleLine withSomeHtmlTag(String text, FontConfiguration fontConfiguration,
			HorizontalAlignment horizontalAlignment, SpriteContainer spriteContainer) {
		return new SingleLine(text, fontConfiguration, horizontalAlignment, spriteContainer);
	}

	public static SingleLine rawText(String text, FontConfiguration fontConfiguration) {
		return new SingleLine(text, fontConfiguration);
	}

	private SingleLine(String text, FontConfiguration fontConfiguration) {
		if (text.length() == 0) {
			text = " ";
		}
		this.horizontalAlignment = HorizontalAlignment.LEFT;
		this.blocs.add(new TileText(text, fontConfiguration, null));
	}

	private SingleLine(String text, FontConfiguration fontConfiguration, HorizontalAlignment horizontalAlignment,
			SpriteContainer spriteContainer) {
		if (text.length() == 0) {
			text = " ";
		}
		this.horizontalAlignment = horizontalAlignment;
		final Splitter lineSplitter = new Splitter(text);

		for (HtmlCommand cmd : lineSplitter.getHtmlCommands(false)) {
			if (cmd instanceof Text) {
				final String s = ((Text) cmd).getText();
				blocs.add(new TileText(s, fontConfiguration, null));
			} else if (cmd instanceof TextLink) {
				final String s = ((TextLink) cmd).getText();
				final Url url = ((TextLink) cmd).getUrl();
				// blocs.add(new TileText(s, fontConfiguration.add(FontStyle.UNDERLINE), url));
				blocs.add(new TileText(s, fontConfiguration, url));
			} else if (cmd instanceof Img) {
				blocs.add(((Img) cmd).createMonoImage());
			} else if (cmd instanceof SpriteCommand) {
				final Sprite sprite = spriteContainer.getSprite(((SpriteCommand) cmd).getSprite());
				if (sprite != null) {
					blocs.add(sprite.asTextBlock(fontConfiguration.getColor(), 1));
				}
			} else if (cmd instanceof FontChange) {
				fontConfiguration = ((FontChange) cmd).apply(fontConfiguration);
			}
		}
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		double width = 0;
		double height = 0;
		for (TextBlock b : blocs) {
			final Dimension2D size2D = b.calculateDimension(stringBounder);
			width += size2D.getWidth();
			height = Math.max(height, size2D.getHeight());
		}
		return new Dimension2DDouble(width, height);
	}

	// private double maxDeltaY(Graphics2D g2d) {
	// double result = 0;
	// final Dimension2D dim = calculateDimension(StringBounderUtils.asStringBounder(g2d));
	// for (TextBlock b : blocs) {
	// if (b instanceof TileText == false) {
	// continue;
	// }
	// final Dimension2D dimBloc = b.calculateDimension(StringBounderUtils.asStringBounder(g2d));
	// final double deltaY = dim.getHeight() - dimBloc.getHeight() + ((TileText) b).getFontSize2D();
	// result = Math.max(result, deltaY);
	// }
	// return result;
	// }

	private double maxDeltaY(UGraphic ug) {
		double result = 0;
		final Dimension2D dim = calculateDimension(ug.getStringBounder());
		for (TextBlock b : blocs) {
			if (b instanceof TileText == false) {
				continue;
			}
			final Dimension2D dimBloc = b.calculateDimension(ug.getStringBounder());
			final double deltaY = dim.getHeight() - dimBloc.getHeight() + ((TileText) b).getFontSize2D();
			result = Math.max(result, deltaY);
		}
		return result;
	}

	public void drawU(UGraphic ug) {
		final double deltaY = maxDeltaY(ug);
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dim = calculateDimension(stringBounder);
		double x = 0;
		for (TextBlock b : blocs) {
			if (b instanceof TileText) {
				b.drawU(ug.apply(new UTranslate(x, deltaY)));
			} else {
				final double dy = dim.getHeight() - b.calculateDimension(stringBounder).getHeight();
				b.drawU(ug.apply(new UTranslate(x, dy)));
			}
			x += b.calculateDimension(stringBounder).getWidth();
		}
	}

	public HorizontalAlignment getHorizontalAlignment() {
		return horizontalAlignment;
	}

}
