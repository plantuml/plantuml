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
package net.sourceforge.plantuml.emoji;

import java.awt.Font;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.ColorUtils;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.klimt.shape.UImageSvg;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.shape.UText;
import net.sourceforge.plantuml.klimt.sprite.Sprite;
import net.sourceforge.plantuml.openiconic.SvgPath;

// Emojji from https://twemoji.twitter.com/
// Shorcut from https://api.github.com/emojis

public class SvgNanoParser implements Sprite, GrayLevelRange {

	private static final Pattern P_TEXT_OR_DRAW = Pattern
			.compile("(\\<text .*?\\</text\\>)|(\\<(svg|path|g|circle|ellipse)[^<>]*\\>)|(\\</[^<>]*\\>)");

	private static final Pattern P_TEXT = Pattern.compile("\\<text[^<>]*\\>(.*?)\\</text\\>");
	private static final Pattern P_FONT_SIZE = Pattern.compile("^(\\d+)p[tx]$");

	private static final Pattern P_MATRIX = Pattern.compile(
			"matrix\\(([-.0-9]+)[ ,]+([-.0-9]+)[ ,]+([-.0-9]+)[ ,]+([-.0-9]+)[ ,]+([-.0-9]+)[ ,]+([-.0-9]+)\\)");

	private static final Pattern P_ROTATE = Pattern.compile("rotate\\(([-.0-9]+)[ ,]+([-.0-9]+)[ ,]+([-.0-9]+)\\)");

	private static final Pattern P_TRANSLATE1 = Pattern.compile("translate\\(([-.0-9]+)[ ,]+([-.0-9]+)\\)");
	private static final Pattern P_TRANSLATE2 = Pattern.compile("translate\\(([-.0-9]+)\\)");

	private static final Pattern P_SCALE1 = Pattern.compile("scale\\(([-.0-9]+)\\)");
	private static final Pattern P_SCALE2 = Pattern.compile("scale\\(([-.0-9]+)[ ,]+([-.0-9]+)\\)");

	private static final String equals_something = "=\"([^\"]+)\"";
	private static final Pattern DATA_CX = Pattern.compile("cx" + equals_something);
	private static final Pattern DATA_CY = Pattern.compile("cy" + equals_something);
	private static final Pattern DATA_FILL = Pattern.compile("fill" + equals_something);
	private static final Pattern DATA_FONT_FAMILY = Pattern.compile("font-family" + equals_something);
	private static final Pattern DATA_FONT_SIZE = Pattern.compile("font-size" + equals_something);
	private static final Pattern DATA_R = Pattern.compile("r" + equals_something);
	private static final Pattern DATA_RX = Pattern.compile("rx" + equals_something);
	private static final Pattern DATA_RY = Pattern.compile("ry" + equals_something);
	private static final Pattern DATA_STROKE = Pattern.compile("stroke" + equals_something);
	private static final Pattern DATA_STROKE_WIDTH = Pattern.compile("stroke-width" + equals_something);
	private static final Pattern DATA_STYLE = Pattern.compile("style" + equals_something);
	private static final Pattern DATA_TRANSFORM = Pattern.compile("transform" + equals_something);
	private static final Pattern DATA_X = Pattern.compile("x" + equals_something);
	private static final Pattern DATA_Y = Pattern.compile("y" + equals_something);

	private static final String colon_something = ":([^;\"]+)";
	private static final Pattern STYLE_FILL = Pattern.compile(Pattern.quote("fill") + colon_something);
	private static final Pattern STYLE_FONT_SIZE = Pattern.compile(Pattern.quote("font-size") + colon_something);
	private static final Pattern STYLE_FONT_FAMILY = Pattern.compile(Pattern.quote("font-family") + colon_something);

	private final List<String> data = new ArrayList<>();
	private int minGray = 999;
	private int maxGray = -1;
	private List<String> svg;

	private String extract(Pattern p, String s) {
		final Matcher m = p.matcher(s);
		if (m.find())
			return m.group(1);

		return null;
	}

	public SvgNanoParser(String svg) {
		this(Collections.singletonList(svg));
	}

	public SvgNanoParser(List<String> svg) {
		this.svg = svg;
	}

	public void drawU(UGraphic ug, double scale, HColor fontColor, HColor forcedColor) {
		final ColorResolver colorResolver = new ColorResolver(fontColor, forcedColor, this);
		UGraphicWithScale ugs = new UGraphicWithScale(ug, colorResolver, scale);

		final List<UGraphicWithScale> stack = new ArrayList<>();
		final Deque<String> stackG = new ArrayDeque<>();
		for (String s : getData()) {
			if (s.startsWith("<path ")) {
				drawPath(ugs, s, stackG);
			} else if (s.startsWith("</g>")) {
				ugs = stack.remove(0);
				stackG.removeFirst();
			} else if (s.startsWith("<g>")) {
				stack.add(0, ugs);
				stackG.addFirst(s);
			} else if (s.startsWith("<g ")) {
				stack.add(0, ugs);
				stackG.addFirst(s);
				ugs = applyFillAndStroke(ugs, s, stackG);
				ugs = applyTransform(ugs, s);
			} else if (s.startsWith("<circle ")) {
				drawCircle(ugs, s, stackG);
			} else if (s.startsWith("<ellipse ")) {
				drawEllipse(ugs, s, stackG);
			} else if (s.startsWith("<text ")) {
				drawText(ugs, s, stackG);
			} else {
				System.err.println("**?=" + s);
			}
		}
	}

	private synchronized Collection<String> getData() {
		if (data.isEmpty()) {
			for (String singleLine : svg) {
				final Matcher m = P_TEXT_OR_DRAW.matcher(singleLine);
				while (m.find()) {
					final String s = m.group(0);
					if (s.startsWith("<path") || s.startsWith("<g ") || s.startsWith("<g>") || s.startsWith("</g>")
							|| s.startsWith("<circle ") || s.startsWith("<ellipse ") || s.startsWith("<text "))
						data.add(s);
					else if (s.startsWith("<svg") || s.startsWith("</svg")) {
						// Ignore
					} else
						System.err.println("???=" + s);
				}
			}
		}
		return Collections.unmodifiableCollection(data);
	}

	private UGraphicWithScale applyFillAndStroke(UGraphicWithScale ugs, String s, Deque<String> stackG) {
		final String fillString = getFillString(s, stackG);
		final String strokeString = extract(DATA_STROKE, s);

		final String strokeWidth = extract(DATA_STROKE_WIDTH, s);
		if (strokeWidth != null) {
			final double scale = ugs.getEffectiveScale();
			ugs = ugs.apply(UStroke.withThickness(scale * Double.parseDouble(strokeWidth)));
		}

		if (strokeString != null) {
			final HColor stroke = ugs.getTrueColor(strokeString);
			ugs = ugs.apply(stroke);
			if (fillString == null)
				return ugs.apply(ugs.getDefaultColor().bg());
		}

		if ("none".equals(fillString)) {
			ugs = ugs.apply(HColors.none().bg());
		} else {
			final HColor fill = fillString == null ? ugs.getDefaultColor() : ugs.getTrueColor(fillString);

			if (strokeString == null)
				ugs = ugs.apply(fill);
			ugs = ugs.apply(fill.bg());
		}

		return ugs;
	}

	private void drawCircle(UGraphicWithScale ugs, String s, Deque<String> stackG) {
		ugs = applyFillAndStroke(ugs, s, stackG);
		ugs = applyTransform(ugs, s);

		final double scalex = ugs.getAffineTransform().getScaleX();
		final double scaley = ugs.getAffineTransform().getScaleY();

		final double deltax = ugs.getAffineTransform().getTranslateX();
		final double deltay = ugs.getAffineTransform().getTranslateY();

		final double cx = Double.parseDouble(extract(DATA_CX, s)) * scalex;
		final double cy = Double.parseDouble(extract(DATA_CY, s)) * scaley;
		final double rx = Double.parseDouble(extract(DATA_R, s)) * scalex;
		final double ry = Double.parseDouble(extract(DATA_R, s)) * scaley;

		final UTranslate translate = new UTranslate(deltax + cx - rx, deltay + cy - ry);
		ugs.apply(translate).draw(UEllipse.build(rx * 2, ry * 2));
	}

	private void drawEllipse(UGraphicWithScale ugs, String s, Deque<String> stackG) {
		final boolean debug = false;
		ugs = applyFillAndStroke(ugs, s, stackG);
		ugs = applyTransform(ugs, s);

		final double cx = Double.parseDouble(extract(DATA_CX, s));
		final double cy = Double.parseDouble(extract(DATA_CY, s));
		final double rx = Double.parseDouble(extract(DATA_RX, s));
		final double ry = Double.parseDouble(extract(DATA_RY, s));

		UPath path = UPath.none();
		path.moveTo(0, ry);

		if (debug)
			path.lineTo(rx, 0);
		else
			path.arcTo(rx, ry, 0, 0, 1, rx, 0);

		if (debug)
			path.lineTo(2 * rx, ry);
		else
			path.arcTo(rx, ry, 0, 0, 1, 2 * rx, ry);

		if (debug)
			path.lineTo(rx, 2 * ry);
		else
			path.arcTo(rx, ry, 0, 0, 1, rx, 2 * ry);

		if (debug)
			path.lineTo(0, ry);
		else
			path.arcTo(rx, ry, 0, 0, 1, 0, ry);

		path.closePath();

		path = path.translate(cx - rx, cy - ry);
		path = path.affine(ugs.getAffineTransform(), ugs.getAngle(), ugs.getInitialScale());

		ugs.draw(path);

	}

	private void drawText(UGraphicWithScale ugs, String s, Deque<String> stackG) {
		final double x = Double.parseDouble(extract(DATA_X, s));
		final double y = Double.parseDouble(extract(DATA_Y, s));
		final String fontColor = getFillString(s, stackG);
		final int fontSize = getTextFontSize(s);

		final Matcher m = P_TEXT.matcher(s);
		if (m.find()) {
			final String text = m.group(1);
			final HColor color = HColorSet.instance().getColorOrWhite(fontColor);
			String fontFamily = getTextFontFamily(s, stackG);
			if (fontFamily == null)
				fontFamily = "SansSerif";
			final UFont font = UFont.build(fontFamily, Font.PLAIN, fontSize);
			final FontConfiguration fc = FontConfiguration.create(font, color, color, null);
			final UText utext = UText.build(text, fc);
			UGraphic ug = ugs.getUg();
			ug = ug.apply(new UTranslate(x, y));
			ug.draw(utext);
		}
	}

	private String getTextFontFamily(String s, Deque<String> stackG) {
		String family = extract(DATA_FONT_FAMILY, s);
		if (family == null) {
			final String style = extract(DATA_STYLE, s);
			if (style != null)
				family = extract(STYLE_FONT_FAMILY, style);
		}
		if (family == null && stackG != null) {
			for (String g : stackG) {
				family = getTextFontFamily(g, null);
				if (family != null)
					return family;
			}
		}
		return family;
	}

	private String getFillString(String s, Deque<String> stackG) {
		String color = extract(DATA_FILL, s);
		if (color == null) {
			final String style = extract(DATA_STYLE, s);
			if (style != null)
				color = extract(STYLE_FILL, style);
		}

		if (color == null && stackG != null)
			for (String g : stackG) {
				color = getFillString(g, null);
				if (color != null)
					return color;
			}

		return color;
	}

	private int getTextFontSize(String s) {
		String fontSize = extract(DATA_FONT_SIZE, s);
		if (fontSize == null) {
			final String style = extract(DATA_STYLE, s);
			if (style != null)
				fontSize = extract(STYLE_FONT_SIZE, style);

		}
		if (fontSize == null)
			// Not perfect, by let's take a default value
			return 14;

		final Matcher matcher = P_FONT_SIZE.matcher(fontSize);

		if (matcher.matches())
			return Integer.parseInt(fontSize.replaceAll("[a-z]", ""));

		return Integer.parseInt(fontSize);
	}

	private void drawPath(UGraphicWithScale ugs, String s, Deque<String> stackG) {
		s = s.replace("id=\"", "ID=\"");
		ugs = applyFillAndStroke(ugs, s, stackG);
		ugs = applyTransform(ugs, s);

		final int x1 = s.indexOf("d=\"");
		final int x2 = s.indexOf('"', x1 + 3);
		final String tmp = s.substring(x1 + 3, x2);

		final SvgPath svgPath = new SvgPath(tmp, UTranslate.none());
		svgPath.drawMe(ugs.getUg(), ugs.getAffineTransform());

	}

	private UGraphicWithScale applyTransform(UGraphicWithScale ugs, String s) {
		final String transform = extract(DATA_TRANSFORM, s);
		if (transform == null)
			return ugs;

		if (transform.contains("rotate("))
			return applyRotate(ugs, transform);

		if (transform.contains("matrix("))
			return applyMatrix(ugs, transform);

		final double[] scale = getScale(transform);
		final UTranslate translate = getTranslate(transform);
		ugs = ugs.applyTranslate(translate.getDx(), translate.getDy());

		return ugs.applyScale(scale[0], scale[1]);
	}

	private UGraphicWithScale applyMatrix(UGraphicWithScale ugs, final String transform) {
		final Matcher m3 = P_MATRIX.matcher(transform);
		if (m3.find()) {
			final double v1 = Double.parseDouble(m3.group(1));
			final double v2 = Double.parseDouble(m3.group(2));
			final double v3 = Double.parseDouble(m3.group(3));
			final double v4 = Double.parseDouble(m3.group(4));
			final double v5 = Double.parseDouble(m3.group(5));
			final double v6 = Double.parseDouble(m3.group(6));
			ugs = ugs.applyMatrix(v1, v2, v3, v4, v5, v6);
		} else
			System.err.println("WARNING: " + transform);
		return ugs;
	}

	private UGraphicWithScale applyRotate(UGraphicWithScale ugs, String transform) {
		final Matcher m3 = P_ROTATE.matcher(transform);
		if (m3.find()) {
			final double angle = Double.parseDouble(m3.group(1));
			final double x = Double.parseDouble(m3.group(2));
			final double y = Double.parseDouble(m3.group(3));
			ugs = ugs.applyRotate(angle, x, y);
		} else
			System.err.println("WARNING: " + transform);
		return ugs;
	}

	private UTranslate getTranslate(String transform) {
		double x = 0;
		double y = 0;

		final Matcher m3 = P_TRANSLATE1.matcher(transform);
		if (m3.find()) {
			x = Double.parseDouble(m3.group(1));
			y = Double.parseDouble(m3.group(2));
		} else {
			final Matcher m4 = P_TRANSLATE2.matcher(transform);
			if (m4.find()) {
				x = Double.parseDouble(m4.group(1));
				y = Double.parseDouble(m4.group(1));
			}
		}
		return new UTranslate(x, y);
	}

	private double[] getScale(String transform) {
		final double scale[] = new double[] { 1, 1 };
		final Matcher m1 = P_SCALE1.matcher(transform);
		if (m1.find()) {
			scale[0] = Double.parseDouble(m1.group(1));
			scale[1] = scale[0];
		} else {
			final Matcher m2 = P_SCALE2.matcher(transform);
			if (m2.find()) {
				scale[0] = Double.parseDouble(m2.group(1));
				scale[1] = Double.parseDouble(m2.group(2));
			}
		}
		return scale;
	}

	@Override
	public TextBlock asTextBlock(final HColor fontColor, final HColor forcedColor, final double scale,
			final HColor backColor) {

		final UImageSvg data = new UImageSvg(svg.get(0), scale);
		final double width = data.getWidth();
		final double height = data.getHeight();

		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				if (backColor != null)
					ug.apply(backColor.bg()).apply(backColor)
							.draw(URectangle.build(calculateDimension(ug.getStringBounder())));
				SvgNanoParser.this.drawU(ug, scale, fontColor, forcedColor);
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return new XDimension2D(width, height);
			}
		};
	}

	private void computeMinMaxGray() {
		for (String s : getData()) {
			if (s.contains("<path ") || s.contains("<g ") || s.contains("<circle ") || s.contains("<ellipse ")) {
				final String fillString = getFillString(s, null);
				final String strokeString = extract(DATA_STROKE, s);

				updateMinMax(strokeString);
				updateMinMax(fillString);

			} else {
				// Nothing
			}
		}
	}

	private void updateMinMax(String colorString) {
		if (colorString != null) {
			final HColor color = HColorSet.instance().getColorOrWhite(colorString);
			final int gray = ColorUtils.getGrayScaleColor(color.toColor(ColorMapper.MONOCHROME)).getGreen();
			minGray = Math.min(minGray, gray);
			maxGray = Math.max(maxGray, gray);
		}
	}

	@Override
	public int getMinGrayLevel() {
		if (maxGray == -1)
			computeMinMaxGray();

		return minGray;
	}

	@Override
	public int getMaxGrayLevel() {
		if (maxGray == -1)
			computeMinMaxGray();

		return maxGray;
	}

}
