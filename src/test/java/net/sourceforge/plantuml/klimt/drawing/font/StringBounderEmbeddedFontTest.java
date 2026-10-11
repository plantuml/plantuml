/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
package net.sourceforge.plantuml.klimt.drawing.font;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.font.UFontFace;
import net.sourceforge.plantuml.klimt.font.UFontFactory;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;

class StringBounderEmbeddedFontTest {

	private static final StringBounder SB = new StringBounderEmbeddedFont(FileFormat.PNG);

	private static final File FONTS = new File("src/teavm-c/native/fonts");

	private static UFont sans(UFontFace face, int size) {
		return UFontFactory.build("SansSerif", face, size);
	}

	// Measured with Java on Windows (StringBounderAwt), where "SansSerif" is Arial
	// with the line height of a composite font (values from the rockuml project)
	@Test
	void testMeasuredWithJavaOnWindows() {
		final XDimension2D hello = SB.calculateDimension(sans(UFontFace.normal(), 14), "Hello world");
		assertEquals(69.248046875, hello.getWidth());
		assertEquals(17.609375, hello.getHeight());
		assertEquals(136.9306640625,
				SB.calculateDimension(sans(UFontFace.bold(), 14), "This is a second line").getWidth());
		final XDimension2D arial = SB.calculateDimension(UFontFactory.build("Arial", UFontFace.normal(), 14),
				"Hello world");
		assertEquals(69.248046875, arial.getWidth());
		assertEquals(16.0986328125, arial.getHeight());
	}

	@Test
	void testNeitherKerningNorLigatures() {
		assertEquals(18.67578125, SB.calculateDimension(sans(UFontFace.normal(), 14), "AV").getWidth());
		assertEquals(7.0, SB.calculateDimension(sans(UFontFace.normal(), 14), "fi").getWidth());
	}

	@Test
	void testOtherScriptsInLogicalFontsUseTheWidthTable() {
		// Java falls back on fonts for other scripts, one em wide for CJK
		assertEquals(28.0, SB.calculateDimension(sans(UFontFace.normal(), 14), "中文").getWidth());
	}

	@Test
	void testInvisibleCharacters() {
		final UFont font = sans(UFontFace.normal(), 14);
		final double aa = SB.calculateDimension(font, "aa").getWidth();
		for (char c : new char[] { '\t', '\n', '\r', ' ', ' ' })
			assertEquals(aa, SB.calculateDimension(font, "a" + c + "a").getWidth());
	}

	@Test
	void testEmptyText() {
		final XDimension2D dim = SB.calculateDimension(sans(UFontFace.normal(), 14), "");
		assertEquals(0, dim.getWidth());
		assertEquals(0, dim.getHeight());
	}

	@Test
	void testDescent() {
		assertEquals(450.0 / 2048 * 14, SB.getDescent(sans(UFontFace.normal(), 14), "Hxgjp"));
		assertEquals(434.0 / 2048 * 14,
				SB.getDescent(UFontFactory.build("Arial", UFontFace.normal(), 14), "Hxgjp"));
	}

	@Test
	void testFirstFamily() {
		assertEquals("arial", StringBounderEmbeddedFont.firstFamily("Arial"));
		assertEquals("liberation sans", StringBounderEmbeddedFont.firstFamily("\"Liberation Sans\", Arial"));
		assertEquals("sans-serif", StringBounderEmbeddedFont.firstFamily("sans-serif"));
	}

	// The widths measured with Java on Windows (tools/font-metrics), any OS
	@Test
	void testMeasuredWindowsWidths() throws IOException {
		final UFontFace[] faces = { UFontFace.normal(), UFontFace.bold(), UFontFace.italic(),
				UFontFace.boldItalic() };
		final StringBounder without = StringBounderEmbeddedFont.withoutWindowsWidths(FileFormat.PNG);
		int count = 0;
		for (String line : Files.readAllLines(Paths.get("tools/font-metrics/sansserif-windows.txt"),
				StandardCharsets.UTF_8)) {
			if (line.startsWith("#"))
				continue;
			final String[] fields = line.split(" ");
			for (int cp = Integer.parseInt(fields[0], 16); cp <= Integer.parseInt(fields[1], 16); cp++) {
				final String text = String.valueOf((char) cp);
				for (int style = 0; style < 4; style++) {
					final UFont font = sans(faces[style], 2048);
					final double expected = fields[2 + style].equals("=")
							? without.calculateDimension(font, text).getWidth()
							: Integer.parseInt(fields[2 + style]);
					assertEquals(expected, SB.calculateDimension(font, text).getWidth(), line);
				}
				count++;
			}
		}
		assertTrue(count > 50000, "" + count);
	}

	// Failures of testSameAsStringBounderAwtOnWindows before the measured widths:
	// U+2013 comes from a fallback font, not from Arial
	@Test
	void testEnDashOfLogicalFont() {
		final String text = "Accents: \u00e9\u00e0\u00e7\u00f6\u00c9 \u20ac \u2013";
		assertEquals(85.6005859375, SB.calculateDimension(sans(UFontFace.normal(), 10), text).getWidth());
		assertEquals(119.8408203125, SB.calculateDimension(sans(UFontFace.italic(), 14), text).getWidth());
		assertEquals(90.908203125, SB.calculateDimension(sans(UFontFace.bold(), 10), text).getWidth());
		assertEquals(199.998046875, SB.calculateDimension(sans(UFontFace.boldItalic(), 22), text).getWidth());
		// Arial itself has the character
		assertEquals(1139.0 / 2048 * 14,
				SB.calculateDimension(UFontFactory.build("Arial", UFontFace.normal(), 14), "\u2013").getWidth());
	}

	// The generated tables against Java2D measuring the very same TTF files, as
	// physical fonts (any OS)
	@Test
	void testTablesMatchJava2D() throws IOException, FontFormatException {
		final String[] files = { "Regular", "Bold", "Italic", "BoldItalic" };
		final EmbeddedFontMetrics[] tables = { LiberationSansMetrics.REGULAR, LiberationSansMetrics.BOLD,
				LiberationSansMetrics.ITALIC, LiberationSansMetrics.BOLD_ITALIC };
		final FontRenderContext frc = new FontRenderContext(null, true, true);
		final Random random = new Random(42);
		for (int f = 0; f < files.length; f++) {
			final Font base = Font.createFont(Font.TRUETYPE_FONT,
					new File(FONTS, "LiberationSans-" + files[f] + ".ttf"));
			final EmbeddedFontMetrics table = tables[f];
			for (int size : new int[] { 9, 12, 14, 17, 31 }) {
				final Font font = base.deriveFont((float) size);
				final String where = files[f] + " " + size;

				final LineMetrics lm = font.getLineMetrics("Hxgjp", frc);
				assertEquals(lm.getAscent(), (float) (table.getAscent() * size / 2048.0), where);
				assertEquals(lm.getDescent(), (float) (table.getDescent() * size / 2048.0), where);
				assertEquals(lm.getLeading(), (float) (table.getLineGap() * size / 2048.0), where);

				final List<Integer> mapped = new ArrayList<>();
				for (int cp = 0x20; cp < 0x10000; cp++) {
					final int advance = table.getAdvance(cp);
					if (Character.isSurrogate((char) cp) || cp == 0x2028 || cp == 0x2029)
						continue;
					assertEquals(font.canDisplay(cp), advance >= 0, where + " " + Integer.toHexString(cp));
					// Java2D measures U+05C3 alone as 0 (not in a word): skip it
					if (advance >= 0 && cp != 0x05C3) {
						mapped.add(cp);
						final double java = font.getStringBounds(String.valueOf((char) cp), frc).getWidth();
						assertEquals(java, advance * size / 2048.0, where + " " + Integer.toHexString(cp));
					}
				}
				assertTrue(mapped.size() > 2000, where);

				// Strings: the advances are summed exactly as Java2D does, as long as no
				// character makes Java2D shape the whole text (see isComplex)
				final List<Integer> simple = new ArrayList<>();
				for (int cp : mapped)
					if (isComplex(cp) == false)
						simple.add(cp);
				for (int n = 0; n < 200; n++) {
					final StringBuilder sb = new StringBuilder();
					final int length = 1 + random.nextInt(100);
					for (int i = 0; i < length; i++)
						sb.append((char) (int) simple.get(random.nextInt(simple.size())));
					final String s = sb.toString();
					double sum = 0;
					for (int i = 0; i < s.length(); i++)
						sum += table.getAdvance(s.charAt(i)) / 2048.0;
					assertEquals(font.getStringBounds(s, frc).getWidth(), sum * size, where + " \"" + s + "\"");
				}
			}
		}
	}

	// Characters for which Java2D shapes the whole text with HarfBuzz (the JDK's
	// FontUtilities.isComplexCharCode): then the soft hyphen disappears and some
	// marks move their base, which StringBounderEmbeddedFont does not reproduce
	private static boolean isComplex(int cp) {
		return (cp >= 0x0300 && cp <= 0x036F) || (cp >= 0x0590 && cp <= 0x06FF) || (cp >= 0x0900 && cp <= 0x0E7F)
				|| (cp >= 0x0F00 && cp <= 0x109F) || (cp >= 0x1100 && cp <= 0x11FF) || (cp >= 0x1780 && cp <= 0x17FF)
				|| (cp >= 0x200C && cp <= 0x200D) || (cp >= 0x202A && cp <= 0x202E) || (cp >= 0x206A && cp <= 0x206F);
	}

	// Against PlantUML's own measurement with AWT: only on Windows, where Java's
	// "SansSerif" is Arial (DejaVu Sans on most Linux systems)
	@Test
	@EnabledOnOs(OS.WINDOWS)
	void testSameAsStringBounderAwtOnWindows() {
		final StringBounder awt = FileFormat.PNG.getDefaultStringBounder();
		final String[] texts = { "Hello world", "PlantUML", "This is a second line", "AV fi ffi", "0123456789",
				"Accents: \u00e9\u00e0\u00e7\u00f6\u00c9 \u20ac \u2013", "MindMap root", "a\tb", "x" };
		final UFontFace[] faces = { UFontFace.normal(), UFontFace.bold(), UFontFace.italic(),
				UFontFace.boldItalic() };
		// Every difference is listed, so that one run shows the whole picture
		final StringBuilder differences = new StringBuilder();
		int count = 0;
		int total = 0;
		for (String family : new String[] { "SansSerif", "Arial" })
			for (UFontFace face : faces)
				for (int size : new int[] { 10, 11, 12, 13, 14, 18, 22 })
					for (String text : texts) {
						total++;
						final UFont font = UFontFactory.build(family, face, size);
						final XDimension2D expected = awt.calculateDimension(font, text);
						final XDimension2D actual = SB.calculateDimension(font, text);
						final double expectedDescent = awt.getDescent(font, text);
						final double actualDescent = SB.getDescent(font, text);
						if (expected.getWidth() == actual.getWidth() && expected.getHeight() == actual.getHeight()
								&& expectedDescent == actualDescent)
							continue;
						if (count++ < 60)
							differences.append(String.format("%n%s %s %d \"%s\": width %s/%s (%+.1f units)"
									+ ", height %s/%s, descent %s/%s", family, face, size, escape(text),
									expected.getWidth(), actual.getWidth(),
									(actual.getWidth() - expected.getWidth()) * 2048 / size, expected.getHeight(),
									actual.getHeight(), expectedDescent, actualDescent));
					}
		assertEquals(0, count, count + " of " + total + " measurements differ (StringBounderAwt / embedded):"
				+ differences);
	}

	private static String escape(String text) {
		final StringBuilder sb = new StringBuilder();
		for (char c : text.toCharArray())
			sb.append(c < 0x20 || c > 0x7E ? String.format("\\u%04x", (int) c) : String.valueOf(c));
		return sb.toString();
	}

}
