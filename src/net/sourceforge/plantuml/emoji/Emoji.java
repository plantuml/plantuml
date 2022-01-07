package net.sourceforge.plantuml.emoji;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.emoji.data.Dummy;
import net.sourceforge.plantuml.openiconic.SvgPath;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.ColorChangerMonochrome;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorNone;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;
import net.sourceforge.plantuml.ugraphic.color.HColorSimple;

// Emojji from https://twemoji.twitter.com/
// Shorcut from https://api.github.com/emojis

public class Emoji {

	private final static Map<String, Emoji> ALL = new HashMap<>();

	static {
		final InputStream is = Dummy.class.getResourceAsStream("emoji.txt");
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			String s = null;
			while ((s = br.readLine()) != null) {
				new Emoji(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, Emoji> getAll() {
		return Collections.unmodifiableMap(new TreeMap<>(ALL));
	}

	private final List<String> data = new ArrayList<>();
	private int minGray = 999;
	private int maxGray = -1;
	private final String unicode;
	private final String shortcut;

	private Emoji(String unicode) {
		final int x = unicode.indexOf(';');
		if (x == -1) {
			this.shortcut = null;
		} else {
			this.shortcut = unicode.substring(x + 1);
			ALL.put(this.shortcut, this);
			unicode = unicode.substring(0, x);
		}
		this.unicode = unicode;
		ALL.put(unicode, this);
	}

	public static String pattern() {
		final StringBuilder sb = new StringBuilder("\\<(#\\w+)?:(");
		for (String s : ALL.keySet()) {
			if (sb.toString().endsWith("(") == false)
				sb.append("|");
			sb.append(s);
		}
		sb.append("):\\>");
		return sb.toString();
	}

	public static Emoji retrieve(String name) {
		return ALL.get(name.toLowerCase());
	}

	private String extractData(String name, String s) {
		final Pattern p = Pattern.compile(name + "=\"([^\"]+)\"");
		final Matcher m = p.matcher(s);
		if (m.find())
			return m.group(1);

		return null;
	}

	private synchronized void loadIfNeed() throws IOException {
		if (data.size() > 0)
			return;

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(Dummy.class.getResourceAsStream(unicode + ".svg")))) {
			final String singleLine = br.readLine();

			final Pattern p = Pattern.compile("\\<[^<>]+\\>");
			final Matcher m = p.matcher(singleLine);
			while (m.find()) {
				final String s = m.group(0);
				if (s.contains("<path") || s.contains("<g ") || s.contains("<g>") || s.contains("</g>")
						|| s.contains("<circle ") || s.contains("<ellipse "))
					data.add(s);
				else
					System.err.println("???=" + s);
			}
		}
	}

	public void drawU(UGraphic ug, double scale, HColor colorForMonochrome) {
		try {
			loadIfNeed();
		} catch (IOException e) {
			e.printStackTrace();
		}
		UGraphicWithScale ugs = new UGraphicWithScale(ug, scale);

		synchronized (this) {
			if (colorForMonochrome != null && maxGray == -1)
				computeMinMaxGray();
		}

		final List<UGraphicWithScale> stack = new ArrayList<>();
		for (String s : data) {
			if (s.contains("<path ")) {
				drawPath(ugs, s, colorForMonochrome);
			} else if (s.contains("</g>")) {
				ugs = stack.remove(0);
			} else if (s.contains("<g>")) {
				stack.add(0, ugs);
			} else if (s.contains("<g ")) {
				stack.add(0, ugs);
				ugs = applyFill(ugs, s, colorForMonochrome);
				ugs = applyTransform(ugs, s);

			} else if (s.contains("<circle ")) {
				drawCircle(ugs, s, colorForMonochrome);
			} else if (s.contains("<ellipse ")) {
				drawEllipse(ugs, s, colorForMonochrome);
			} else {
				System.err.println("**?=" + s);
			}
		}
	}

	private void computeMinMaxGray() {
		for (String s : data) {
			if (s.contains("<path ")) {
				final int gray = getGray(justExtractColor(s));
				minGray = Math.min(minGray, gray);
				maxGray = Math.max(maxGray, gray);
			} else if (s.contains("</g>")) {
				// Nothing
			} else if (s.contains("<g>")) {
				// Nothing
			} else if (s.contains("<g ")) {
				final int gray = getGray(justExtractColor(s));
				minGray = Math.min(minGray, gray);
				maxGray = Math.max(maxGray, gray);
			} else if (s.contains("<circle ")) {
				final int gray = getGray(justExtractColor(s));
				minGray = Math.min(minGray, gray);
				maxGray = Math.max(maxGray, gray);
			} else if (s.contains("<ellipse ")) {
				final int gray = getGray(justExtractColor(s));
				minGray = Math.min(minGray, gray);
				maxGray = Math.max(maxGray, gray);
			} else {
				// Nothing
			}
		}
	}

	private int getGray(HColor col) {
		final Color tmp = new ColorChangerMonochrome().getChangedColor(col);
		return tmp.getGreen();
	}

	private UGraphicWithScale applyFill(UGraphicWithScale ugs, String s, HColor colorForMonochrome) {
		final String fillString = extractData("fill", s);
		if (fillString == null)
			return ugs;

		if (fillString.equals("none")) {
			final String strokeString = extractData("stroke", s);
			if (strokeString == null)
				return ugs;
			ugs = ugs.apply(new HColorNone().bg());
			final HColor stroke = getTrueColor(strokeString, colorForMonochrome);
			ugs = ugs.apply(stroke);
			final String strokeWidth = extractData("stroke-width", s);
			if (strokeWidth != null) {
				ugs = ugs.apply(new UStroke(Double.parseDouble(strokeWidth)));

			}

		} else {
			final HColor fill = getTrueColor(fillString, colorForMonochrome);
			ugs = ugs.apply(fill).apply(fill.bg());
		}

		return ugs;
	}

	private HColor justExtractColor(String s) {
		final String fillString = extractData("fill", s);
		if (fillString == null)
			return null;

		if (fillString.equals("none")) {
			final String strokeString = extractData("stroke", s);
			if (strokeString == null)
				return null;

			final HColor stroke = getTrueColor(strokeString, null);
			return stroke;

		} else {
			final HColor fill = getTrueColor(fillString, null);
			return fill;
		}

	}

	private HColor getTrueColor(String code, HColor colorForMonochrome) {
		final HColorSimple result = (HColorSimple) HColorSet.instance().getColorOrWhite(code);
		if (colorForMonochrome == null)
			return result;
		return result.asMonochrome((HColorSimple) colorForMonochrome, this.minGray, this.maxGray);
	}

	private void drawCircle(UGraphicWithScale ugs, String s, HColor colorForMonochrome) {
		ugs = applyFill(ugs, s, colorForMonochrome);
		ugs = applyTransform(ugs, s);

		final double scalex = ugs.getAffineTransform().getScaleX();
		final double scaley = ugs.getAffineTransform().getScaleY();

		final double deltax = ugs.getAffineTransform().getTranslateX();
		final double deltay = ugs.getAffineTransform().getTranslateY();

		final double cx = Double.parseDouble(extractData("cx", s)) * scalex;
		final double cy = Double.parseDouble(extractData("cy", s)) * scaley;
		final double rx = Double.parseDouble(extractData("r", s)) * scalex;
		final double ry = Double.parseDouble(extractData("r", s)) * scaley;

		final UTranslate translate = new UTranslate(deltax + cx - rx, deltay + cy - ry);
		ugs.apply(translate).draw(new UEllipse(rx * 2, ry * 2));
	}

	private void drawEllipse(UGraphicWithScale ugs, String s, HColor colorForMonochrome) {

		ugs = applyFill(ugs, s, colorForMonochrome);
		ugs = applyTransform(ugs, s);

		final double scalex = ugs.getAffineTransform().getScaleX();
		final double scaley = ugs.getAffineTransform().getScaleY();

		final double deltax = ugs.getAffineTransform().getTranslateX();
		final double deltay = ugs.getAffineTransform().getTranslateY();

		final double cx = Double.parseDouble(extractData("cx", s)) * scalex;
		final double cy = Double.parseDouble(extractData("cy", s)) * scaley;
		final double rx = Double.parseDouble(extractData("rx", s)) * scalex;
		final double ry = Double.parseDouble(extractData("ry", s)) * scaley;

		final UTranslate translate = new UTranslate(deltax + cx - rx, deltay + cy - ry);
		ugs.apply(translate).draw(new UEllipse(rx * 2, ry * 2));
	}

	private void drawPath(UGraphicWithScale ugs, String s, HColor colorForMonochrome) {
		s = s.replace("id=\"", "ID=\"");
		ugs = applyFill(ugs, s, colorForMonochrome);
		ugs = applyTransform(ugs, s);

		final int x1 = s.indexOf("d=\"");
		final int x2 = s.indexOf('"', x1 + 3);
		final String tmp = s.substring(x1 + 3, x2);

		final SvgPath svgPath = new SvgPath(tmp);
		svgPath.drawMe(ugs.getUg(), ugs.getAffineTransform());

	}

	private UGraphicWithScale applyTransform(UGraphicWithScale ugs, String s) {
		final String transform = extractData("transform", s);
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
		final Pattern p3 = Pattern.compile(
				"matrix\\(([-.0-9]+)[ ,]+([-.0-9]+)[ ,]+([-.0-9]+)[ ,]+([-.0-9]+)[ ,]+([-.0-9]+)[ ,]+([-.0-9]+)\\)");
		final Matcher m3 = p3.matcher(transform);
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

	private UGraphicWithScale applyRotate(UGraphicWithScale ugs, final String transform) {
		final Pattern p3 = Pattern.compile("rotate\\(([-.0-9]+)[ ,]+([-.0-9]+)[ ,]+([-.0-9]+)\\)");
		final Matcher m3 = p3.matcher(transform);
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

		final Pattern p3 = Pattern.compile("translate\\(([-.0-9]+)[ ,]+([-.0-9]+)\\)");
		final Matcher m3 = p3.matcher(transform);
		if (m3.find()) {
			x = Double.parseDouble(m3.group(1));
			y = Double.parseDouble(m3.group(2));
		} else {
			final Pattern p4 = Pattern.compile("translate\\(([-.0-9]+)\\)");
			final Matcher m4 = p4.matcher(transform);
			if (m4.find()) {
				x = Double.parseDouble(m4.group(1));
				y = Double.parseDouble(m4.group(1));
			}
		}
		return new UTranslate(x, y);
	}

	private double[] getScale(String transform) {
		final double scale[] = new double[] { 1, 1 };
		final Pattern p1 = Pattern.compile("scale\\(([-.0-9]+)\\)");
		final Matcher m1 = p1.matcher(transform);
		if (m1.find()) {
			scale[0] = Double.parseDouble(m1.group(1));
			scale[1] = scale[0];
		} else {
			final Pattern p2 = Pattern.compile("scale\\(([-.0-9]+)[ ,]+([-.0-9]+)\\)");
			final Matcher m2 = p2.matcher(transform);
			if (m2.find()) {
				scale[0] = Double.parseDouble(m2.group(1));
				scale[1] = Double.parseDouble(m2.group(2));
			}
		}
		return scale;
	}

	public String getShortcut() {
		return shortcut;
	}

}
