package org.scilab.forge.jlatexmath;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;

class GlyphOutlinesTest {

	/** Records what the reader gives. */
	private static class Recorder implements GlyphOutlines.Sink {
		final List<String> calls = new ArrayList<String>();

		public void moveTo(int x, int y) {
			calls.add("M" + x + "," + y);
		}

		public void lineTo(int x, int y) {
			calls.add("L" + x + "," + y);
		}

		public void quadTo(int controlX, int controlY, int x, int y) {
			calls.add("Q" + controlX + "," + controlY + " " + x + "," + y);
		}

		public void close() {
			calls.add("Z");
		}
	}

	private static void varint(DataOutputStream out, int value) throws IOException {
		int v = (value << 1) ^ (value >> 31);
		while ((v & ~0x7F) != 0) {
			out.writeByte((v & 0x7F) | 0x80);
			v >>>= 7;
		}
		out.writeByte(v);
	}

	/** The file of the format of tools/jlatexmath-glyphs/GenerateGlyphOutlines. */
	private static byte[] sample() throws IOException {
		final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		final DataOutputStream out = new DataOutputStream(bytes);
		out.writeBytes("JLMG");
		out.writeByte(1);
		out.writeShort(1); // one font
		out.writeUTF("jlm_test");
		out.writeShort(1000); // unitsPerEm
		out.writeShort(3); // glyphs
		// code 32: blank
		out.writeShort(32);
		out.writeByte(4);
		// code 65: a triangle, then a curve, with negative and big (2 bytes) deltas
		out.writeShort(65);
		out.writeByte(0); // moveTo 10,20
		varint(out, 10);
		varint(out, 20);
		out.writeByte(1); // lineTo 210,20 (delta 200)
		varint(out, 200);
		varint(out, 0);
		out.writeByte(2); // quadTo control 260,-30 (delta 50,-50) to 310,20 (delta 50,50 from the control)
		varint(out, 50);
		varint(out, -50);
		varint(out, 50);
		varint(out, 50);
		out.writeByte(1); // lineTo 10,20 (delta -300)
		varint(out, -300);
		varint(out, 0);
		out.writeByte(3);
		out.writeByte(4);
		// code 66: a point with a big delta
		out.writeShort(66);
		out.writeByte(0);
		varint(out, -1234);
		varint(out, 5678);
		out.writeByte(3);
		out.writeByte(4);
		out.flush();
		return bytes.toByteArray();
	}

	@Test
	void testGlyphsOfASyntheticFile() throws IOException {
		final GlyphOutlines outlines = GlyphOutlines.parse(sample());
		assertEquals(new HashSet<String>(Arrays.asList("jlm_test")), new HashSet<String>(outlines.getFontNames()));

		final GlyphOutlines.GlyphFont font = outlines.getFont("jlm_test.ttf"); // with or without .ttf
		assertNotNull(font);
		assertSame(font, outlines.getFont("jlm_test"));
		assertEquals("jlm_test", font.getName());
		assertEquals(1000, font.getUnitsPerEm());
		assertEquals(3, font.getNumberOfGlyphs());
		assertTrue(font.hasGlyph(65));
		assertFalse(font.hasGlyph(67));

		final Recorder triangle = new Recorder();
		assertTrue(font.visit(65, triangle));
		assertEquals(Arrays.asList("M10,20", "L210,20", "Q260,-30 310,20", "L10,20", "Z"), triangle.calls);

		final Recorder big = new Recorder();
		assertTrue(font.visit(66, big));
		assertEquals(Arrays.asList("M-1234,5678", "Z"), big.calls);
	}

	private static void assertSame(Object expected, Object actual) {
		assertTrue(expected == actual);
	}

	@Test
	void testBlankGlyphDrawsNothingAndMissingGlyphIsReported() throws IOException {
		final GlyphOutlines.GlyphFont font = GlyphOutlines.parse(sample()).getFont("jlm_test");
		final Recorder blank = new Recorder();
		assertTrue(font.visit(32, blank));
		assertTrue(blank.calls.isEmpty());

		final Recorder missing = new Recorder();
		assertFalse(font.visit(999, missing));
		assertTrue(missing.calls.isEmpty());
	}

	@Test
	void testUnknownFont() throws IOException {
		assertNull(GlyphOutlines.parse(sample()).getFont("nope"));
	}

	@Test
	void testBadFiles() {
		assertThrows(IllegalArgumentException.class, () -> GlyphOutlines.parse(new byte[] { 1, 2, 3 }));
		assertThrows(IllegalArgumentException.class,
				() -> GlyphOutlines.parse(new byte[] { 'X', 'L', 'M', 'G', 1, 0, 0 }));
		// version 2 does not exist
		assertThrows(IllegalArgumentException.class,
				() -> GlyphOutlines.parse(new byte[] { 'J', 'L', 'M', 'G', 2, 0, 0 }));
	}

	@Test
	void testTheThreeFilesOfTheJar() {
		// base fonts, then the alphabets, found next to the class that registers them
		final GlyphOutlines base = GlyphOutlines.forBase(null);
		assertEquals(30, base.getFontNames().size());
		final GlyphOutlines.GlyphFont cmr10 = base.getFont("jlm_cmr10");
		assertNotNull(cmr10);
		assertTrue(cmr10.getUnitsPerEm() > 0);
		assertTrue(cmr10.hasGlyph('A'));

		// an 'A' has an outline: some points, and it is between 0 and the em
		final Recorder a = new Recorder();
		assertTrue(cmr10.visit('A', a));
		assertTrue(a.calls.size() > 5);
		assertTrue(a.calls.get(0).startsWith("M"));
		assertEquals("Z", a.calls.get(a.calls.size() - 1));

		assertEquals(8, GlyphOutlines.forBase(new org.scilab.forge.jlatexmath.cyrillic.CyrillicRegistration()).getFontNames().size());
		assertEquals(8, GlyphOutlines.forBase(new org.scilab.forge.jlatexmath.greek.GreekRegistration()).getFontNames().size());
		// the same instance each time: read once
		assertSame(base, GlyphOutlines.forBase(null));
	}

}
