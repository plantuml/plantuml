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
package net.sourceforge.plantuml.openiconic;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// ::comment when JAVA8
import org.teavm.jso.JSBody;
// ::done

import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockMemoized;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.teavm.TeaVM;
import net.sourceforge.plantuml.teavm.browser.TeaVmScriptLoader;

public class OpenIconic {

	private final static Pattern patternTranslate = Pattern.compile("translate\\((\\d+)\\s*(\\d*)\\)");

	private SvgPath svgPath;
	private List<String> rawData = new ArrayList<>();
	private final String id;

	public static OpenIconic retrieve(String name) {
		// ::comment when JAVA8
		if (TeaVM.isTeaVM())
			return retrieveFromJs(name);
		// ::done
		final InputStream is = getResource(name);
		if (is == null)
			return null;

		try {
			return new OpenIconic(is, name);
		} catch (IOException e) {
			Logme.error(e);
			return null;
		}
	}

	// ::comment when JAVA8
	private static OpenIconic retrieveFromJs(String name) {
		loadOpeniconicJsIfNeeded();
		final String pathLine = jsGetPathLine(name);
		if (pathLine == null)
			return null;
		final String svg = "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"8\" height=\"8\" viewBox=\"0 0 8 8\">\n"
				+ pathLine + "\n" + "</svg>\n";

		final InputStream is = new ByteArrayInputStream(svg.getBytes(StandardCharsets.UTF_8));
		try {
			return new OpenIconic(is, name);
		} catch (IOException e) {
			Logme.error(e);
			return null;
		}
	}

	private static volatile boolean openiconicJsLoaded = false;

	private static void loadOpeniconicJsIfNeeded() {
		if (openiconicJsLoaded)
			return;
		// Same as themes (ThemeUtils.loadJsTheme): data that is already registered, by a
		// bundler that inlined openiconic.js or by the host, is used as is, and
		// openiconic.js is only fetched when nothing is there.
		if (jsHasOpeniconic() == false)
			TeaVmScriptLoader.loadOnceSync("openiconic.js");
		openiconicJsLoaded = true;
	}

	// Read from the global object rather than from window (see Emoji).
	private static final String GLOBAL = "var g = (typeof globalThis !== 'undefined') ? globalThis"
			+ " : ((typeof self !== 'undefined') ? self : this);";

	@JSBody(script = GLOBAL + "return !!(g && g.PLANTUML_OPENICONIC);")
	private static native boolean jsHasOpeniconic();

	@JSBody(params = "name", script = GLOBAL + "var o = g && g.PLANTUML_OPENICONIC;"
			+ "return (o && o[name]) ? o[name] : null;")
	private static native String jsGetPathLine(String name);
	// ::done

	OpenIconic(String name) throws IOException {
		this(getResource(name), name);
	}

	private static InputStream getResource(String name) {
		return OpenIconic.class.getResourceAsStream("/openiconic/" + name + ".svg");
	}

	private OpenIconic(InputStream is, String id) throws IOException {
		this.id = id;
		UTranslate translate = UTranslate.none();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			String s = null;
			while ((s = br.readLine()) != null) {
				rawData.add(s);
				if (s.contains("transform=\""))
					translate = getTranslate(s);
			}
		}
		if (rawData.size() != 3 && rawData.size() != 4)
			throw new IllegalStateException();

		for (String s : rawData)
			if (s.contains("<path")) {
				final int x1 = s.indexOf('"');
				final int x2 = s.indexOf('"', x1 + 1);
				svgPath = new SvgPath(s.substring(x1 + 1, x2), translate);
			}
	}

	private static UTranslate getTranslate(String s) {
		final Matcher matcher = patternTranslate.matcher(s);

		if (matcher.find()) {
			final String xStr = matcher.group(1);
			final String yStr = matcher.group(2);
			final int x = Integer.parseInt(xStr);
			final int y = (yStr == null || yStr.isEmpty()) ? 0 : Integer.parseInt(yStr);

			return new UTranslate(x, y);
		}

		return UTranslate.none();
	}

//	void saveCopy(SFile fnew) throws IOException {
//		try (PrintWriter pw = fnew.createPrintWriter()) {
//			pw.println(rawData.get(0));
//			pw.println(svgPath.toSvg());
//			pw.println(rawData.get(rawData.size() - 1));
//		}
//	}

	private XDimension2D getDimension(double factor) {
		final String width = getNumber(rawData.get(0), "width");
		final String height = getNumber(rawData.get(0), "height");
		return new XDimension2D(Integer.parseInt(width) * factor, Integer.parseInt(height) * factor);
	}

	private String getNumber(String s, String arg) {
		int x1 = s.indexOf(arg);
		if (x1 == -1)
			throw new IllegalArgumentException();

		x1 = s.indexOf("\"", x1);
		if (x1 == -1)
			throw new IllegalArgumentException();

		final int x2 = s.indexOf("\"", x1 + 1);
		if (x2 == -1)
			throw new IllegalArgumentException();

		return s.substring(x1 + 1, x2);
	}

	public TextBlock asTextBlock(final HColor color, final double factor) {
		return new TextBlockMemoized() {
			public void drawU(UGraphic ug) {
				final HColor textColor = color.getAppropriateColor(ug.getParam().getBackcolor());
				ug = ug.apply(textColor).apply(textColor.bg()).apply(UStroke.withThickness(0));
				svgPath.drawMe(ug, factor);
			}

			@Override
			public XDimension2D calculateDimensionSlow(StringBounder stringBounder) {
				return getDimension(factor);
			}
		};
	}

//	public static void main(String[] args) throws IOException {
//		final String outputFile = (args.length > 0) ? args[0] : "src/main/teavm/generated/openiconic.js";
//
//		try (PrintWriter pw = new PrintWriter(new FileWriter(outputFile))) {
//			pw.println("// openiconic.js - Generated by OpenIconic.main()");
//			pw.println("// Do not edit manually");
//			pw.println("(function () {");
//			pw.println("window.PLANTUML_OPENICONIC = window.PLANTUML_OPENICONIC || {};");
//			pw.println();
//
//			final InputStream is = OpenIconic.class.getResourceAsStream("/openiconic/all.txt");
//			if (is == null)
//				throw new IOException("Cannot find /openiconic/all.txt resource");
//
//			int count = 0;
//			try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
//				String name;
//				while ((name = br.readLine()) != null) {
//					name = name.trim();
//					if (name.isEmpty())
//						continue;
//
//					final String pathLine = readPathLine(name);
//					if (pathLine == null) {
//						System.err.println("WARNING: No <path> found for " + name);
//						continue;
//					}
//					pw.println("window.PLANTUML_OPENICONIC[\"" + name + "\"]=\"" + escapeForJs(pathLine) + "\";");
//					count++;
//				}
//			}
//
//			pw.println();
//			pw.println("})();");
//			System.out.println("Generated " + outputFile + " with " + count + " icons.");
//		}
//	}
//
//	private static String readPathLine(String name) {
//		final InputStream svgStream = OpenIconic.class.getResourceAsStream("/openiconic/" + name + ".svg");
//		if (svgStream == null)
//			return null;
//
//		try (BufferedReader br = new BufferedReader(new InputStreamReader(svgStream))) {
//			final StringBuilder sb = new StringBuilder();
//			boolean inPath = false;
//			String line;
//			while ((line = br.readLine()) != null) {
//				final String trimmed = line.trim();
//				if (!inPath && trimmed.contains("<path")) {
//					inPath = true;
//					sb.append(trimmed);
//					if (trimmed.endsWith("/>") || trimmed.endsWith("</path>"))
//						return sb.toString();
//				} else if (inPath) {
//					sb.append(' ').append(trimmed);
//					if (trimmed.endsWith("/>") || trimmed.endsWith("</path>"))
//						return sb.toString();
//				}
//			}
//			return sb.length() == 0 ? null : sb.toString();
//		} catch (IOException e) {
//			System.err.println("ERROR reading " + name + ".svg: " + e.getMessage());
//			return null;
//		}
//	}
//
//	private static String escapeForJs(String s) {
//		return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
//	}

}
