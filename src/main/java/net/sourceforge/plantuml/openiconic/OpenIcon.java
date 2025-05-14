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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.openiconic.data.DummyIcon;
import net.sourceforge.plantuml.security.SFile;

public class OpenIcon {

	private final static Pattern patternTranslate = Pattern.compile("translate\\((\\d+)\\s*(\\d*)\\)");

	private SvgPath svgPath;
	private List<String> rawData = new ArrayList<>();
	private final String id;

	public static OpenIcon retrieve(String name) {
		final InputStream is = getResource(name);
		if (is == null)
			return null;

		try {
			return new OpenIcon(is, name);
		} catch (IOException e) {
			Logme.error(e);
			return null;
		}
	}

	OpenIcon(String name) throws IOException {
		this(getResource(name), name);
	}

	private static InputStream getResource(String name) {
		// System.err.println("OPENING " + name);
		return DummyIcon.class.getResourceAsStream(name + ".svg");
	}

	private OpenIcon(InputStream is, String id) throws IOException {
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

	// ::comment when __CORE__
	void saveCopy(SFile fnew) throws IOException {
		try (PrintWriter pw = fnew.createPrintWriter()) {
			pw.println(rawData.get(0));
			pw.println(svgPath.toSvg());
			pw.println(rawData.get(rawData.size() - 1));
		}
	}
	// ::done

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
		return new AbstractTextBlock() {
			public void drawU(UGraphic ug) {
				final HColor textColor = color.getAppropriateColor(ug.getParam().getBackcolor());
				ug = ug.apply(textColor).apply(textColor.bg());
				svgPath.drawMe(ug, factor);
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return getDimension(factor);
			}
		};
	}

}
