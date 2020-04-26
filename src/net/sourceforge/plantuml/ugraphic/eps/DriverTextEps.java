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
package net.sourceforge.plantuml.ugraphic.eps;

import java.awt.Color;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.TikzFontDistortion;
import net.sourceforge.plantuml.eps.EpsGraphics;
import net.sourceforge.plantuml.eps.EpsGraphicsMacroAndText;
import net.sourceforge.plantuml.eps.EpsStrategy;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.FontStyle;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.ClipContainer;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UClip;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UText;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class DriverTextEps implements UDriver<EpsGraphics> {

	private final StringBounder stringBounder;
	private final ClipContainer clipContainer;
	private final FontRenderContext fontRenderContext;
	private final EpsStrategy strategy;

	public DriverTextEps(ClipContainer clipContainer, EpsStrategy strategy) {
		this.stringBounder = FileFormat.PNG.getDefaultStringBounder(TikzFontDistortion.getDefault());
		this.clipContainer = clipContainer;
		this.fontRenderContext = TextBlockUtils.getFontRenderContext();
		this.strategy = strategy;
	}

	public void draw(UShape ushape, double x, double y, ColorMapper mapper, UParam param, EpsGraphics eps) {

		final UClip clip = clipContainer.getClip();
		if (clip != null && clip.isInside(x, y) == false) {
			return;
		}

		final UText shape = (UText) ushape;

		if (strategy == EpsStrategy.WITH_MACRO_AND_TEXT) {
			drawAsText(shape, x, y, param, eps, mapper);
			return;
		}

		final FontConfiguration fontConfiguration = shape.getFontConfiguration();
		final UFont font = fontConfiguration.getFont();

		final TextLayout textLayout = new TextLayout(shape.getText(), font.getFont(), fontRenderContext);
		// System.err.println("text=" + shape.getText());

		MinMax dim = null;

		if (fontConfiguration.containsStyle(FontStyle.BACKCOLOR)) {
			final Color extended = mapper.toColor(fontConfiguration.getExtendedColor());
			if (extended != null) {
				eps.setStrokeColor(extended);
				eps.setFillColor(extended);
				eps.setStrokeWidth(1, 0, 0);
				if (dim == null) {
					dim = getMinMax(x, y, getOutline(textLayout).getPathIterator(null));
				}
				eps.epsRectangle(dim.getMinX() - 1, dim.getMinY() - 1, dim.getWidth() + 2, dim.getHeight() + 2, 0, 0);
			}
		}

		eps.setStrokeColor(mapper.toColor(fontConfiguration.getColor()));
		drawPathIterator(eps, x, y, getOutline(textLayout));

		if (fontConfiguration.containsStyle(FontStyle.UNDERLINE)) {
			final HColor extended = fontConfiguration.getExtendedColor();
			if (extended != null) {
				eps.setStrokeColor(mapper.toColor(extended));
			}
			if (dim == null) {
				dim = getMinMax(x, y, getOutline(textLayout).getPathIterator(null));
			}
			eps.setStrokeWidth(1.1, 0, 0);
			eps.epsLine(x, y + 1.5, x + dim.getWidth(), y + 1.5);
			eps.setStrokeWidth(1, 0, 0);
		}
		if (fontConfiguration.containsStyle(FontStyle.WAVE)) {
			if (dim == null) {
				dim = getMinMax(x, y, getOutline(textLayout).getPathIterator(null));
			}
			final int ypos = (int) (y + 2.5) - 1;
			final HColor extended = fontConfiguration.getExtendedColor();
			if (extended != null) {
				eps.setStrokeColor(mapper.toColor(extended));
			}
			eps.setStrokeWidth(1.1, 0, 0);
			for (int i = (int) x; i < x + dim.getWidth() - 5; i += 6) {
				eps.epsLine(i, ypos - 0, i + 3, ypos + 1);
				eps.epsLine(i + 3, ypos + 1, i + 6, ypos - 0);
			}
			eps.setStrokeWidth(1, 0, 0);
		}
		if (fontConfiguration.containsStyle(FontStyle.STRIKE)) {
			final HColor extended = fontConfiguration.getExtendedColor();
			if (extended != null) {
				eps.setStrokeColor(mapper.toColor(extended));
			}
			if (dim == null) {
				dim = getMinMax(x, y, getOutline(textLayout).getPathIterator(null));
			}
			// final FontMetrics fm = font.getFontMetrics();
			final double ypos = (dim.getMinY() + dim.getMaxY() * 2) / 3;
			eps.setStrokeWidth(1.3, 0, 0);
			eps.epsLine(x, ypos, x + dim.getWidth(), ypos);
			eps.setStrokeWidth(1, 0, 0);
		}

	}

	private Shape getOutline(final TextLayout textLayout) {
		return textLayout.getOutline(null);
	}

	private void drawAsText(UText shape, double x, double y, UParam param, EpsGraphics eps, ColorMapper mapper) {
		final FontConfiguration fontConfiguration = shape.getFontConfiguration();
		// final FontMetrics fm = g2dummy.getFontMetrics(fontConfiguration.getFont().getFont());
		// final double ypos = y - fm.getDescent() + 0.5;
		final double ypos = y - 1;

		eps.setStrokeColor(mapper.toColor(fontConfiguration.getColor()));
		((EpsGraphicsMacroAndText) eps).drawText(shape.getText(), fontConfiguration, x, ypos);

	}

	static void drawPathIterator(EpsGraphics eps, double x, double y, Shape shape) {
		final List<Integer> breaks = analyze(shape);
		if (breaks.size() == 0) {
			final PathIterator path = shape.getPathIterator(null);
			drawSingle(eps, x, y, path);
			return;
		}
		// System.err.println("breaks=" + breaks);
		final PathIterator path = new PathIteratorLimited(shape, 0, breaks.get(0));
		drawSingle(eps, x, y, path);
		for (int i = 0; i < breaks.size() - 1; i++) {
			final PathIterator path2 = new PathIteratorLimited(shape, breaks.get(i) + 1, breaks.get(i + 1));
			drawSingle(eps, x, y, path2);
		}
		final PathIterator path3 = new PathIteratorLimited(shape, breaks.get(breaks.size() - 1) + 1, Integer.MAX_VALUE);
		drawSingle(eps, x, y, path3);

	}

	private static void drawSingle(EpsGraphics eps, double x, double y, final PathIterator path) {
		eps.newpath();
		final double coord[] = new double[6];
		while (path.isDone() == false) {
			final int code = path.currentSegment(coord);
			if (code == PathIterator.SEG_MOVETO) {
				eps.moveto(coord[0] + x, coord[1] + y);
			} else if (code == PathIterator.SEG_LINETO) {
				eps.lineto(coord[0] + x, coord[1] + y);
			} else if (code == PathIterator.SEG_CLOSE) {
				eps.closepath();
			} else if (code == PathIterator.SEG_CUBICTO) {
				eps.curveto(coord[0] + x, coord[1] + y, coord[2] + x, coord[3] + y, coord[4] + x, coord[5] + y);
			} else if (code == PathIterator.SEG_QUADTO) {
				eps.quadto(coord[0] + x, coord[1] + y, coord[2] + x, coord[3] + y);
			} else {
				throw new UnsupportedOperationException("code=" + code);
			}

			path.next();
		}

		eps.fill(path.getWindingRule());
	}

	private static List<Integer> analyze(Shape shape) {
		int count = PathIteratorLimited.count(shape);
		final List<Integer> closings = getClosings(shape.getPathIterator(null));
		final List<Integer> result = new ArrayList<Integer>();
		for (Integer cl : closings) {
			if (cl + 2 >= count) {
				break;
			}
			final PathIterator path1 = new PathIteratorLimited(shape, 0, cl);
			final PathIterator path2 = new PathIteratorLimited(shape, cl + 1, Integer.MAX_VALUE);
			final double max1 = getMinMax(0, 0, path1).getMaxX();
			final double min2 = getMinMax(0, 0, path2).getMinX();
			if (min2 > max1) {
				result.add(cl);
			}
		}
		return result;
	}

	private static List<Integer> getClosings(PathIterator path) {
		final List<Integer> result = new ArrayList<Integer>();
		int current = 0;
		final double coord[] = new double[6];
		while (path.isDone() == false) {
			final int code = path.currentSegment(coord);
			if (code == PathIterator.SEG_CLOSE) {
				result.add(current);
			}
			current++;
			path.next();
		}
		return result;
	}

	static private MinMax getMinMax(double x, double y, PathIterator path) {

		MinMax result = MinMax.getEmpty(false);

		final double coord[] = new double[6];
		while (path.isDone() == false) {
			final int code = path.currentSegment(coord);
			if (code == PathIterator.SEG_MOVETO) {
				result = result.addPoint(coord[0] + x, coord[1] + y);
			} else if (code == PathIterator.SEG_LINETO) {
				result = result.addPoint(coord[0] + x, coord[1] + y);
			} else if (code == PathIterator.SEG_CLOSE) {
			} else if (code == PathIterator.SEG_CUBICTO) {
				result = result.addPoint(coord[0] + x, coord[1] + y);
				result = result.addPoint(coord[2] + x, coord[3] + y);
				result = result.addPoint(coord[4] + x, coord[5] + y);
			} else if (code == PathIterator.SEG_QUADTO) {
				result = result.addPoint(coord[0] + x, coord[1] + y);
				result = result.addPoint(coord[2] + x, coord[3] + y);
			} else {
				throw new UnsupportedOperationException("code=" + code);
			}
			path.next();
		}

		return result;

	}

}
