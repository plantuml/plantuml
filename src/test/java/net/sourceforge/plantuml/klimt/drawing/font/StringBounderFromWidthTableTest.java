package net.sourceforge.plantuml.klimt.drawing.font;

import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFontFactory;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;

class StringBounderFromWidthTableTest {

	private static final double EPSILON = 0.001;

	// The width table (UnicodeFontWidthSansSerif.SANS_SERIF) is generated at font
	// size 16 and stores advances in tenths of a point. The known reference widths
	// below are therefore expressed at size 16 (where the scaling factor is 1).

	private static final StringBounder SB = new StringBounderFromWidthTable(FileFormat.SVG);

	@Test
	void testFileFormatIsKept() {
		assertEquals(FileFormat.SVG, SB.getFileFormat());
	}

	@Test
	void testHeightEqualsFontSize() {
		assertEquals(16, SB.calculateDimension(UFontFactory.sansSerif(16), "anything").getHeight());
		assertEquals(42, SB.calculateDimension(UFontFactory.sansSerif(42), "anything").getHeight());
	}

	@Test
	void testSingleCharAtReferenceSize() {
		// '!' (U+0021) has a tabulated advance of 4.4 pt at size 16.
		final XDimension2D dim = SB.calculateDimension(UFontFactory.sansSerif(16), "!");
		assertEquals(4.4, dim.getWidth(), EPSILON);
		assertEquals(16, dim.getHeight());
	}

	@Test
	void testWidthScalesLinearlyWithFontSize() {
		// The reference size is 16, so factor = size / 16.
		assertEquals(4.4, SB.calculateDimension(UFontFactory.sansSerif(16), "!").getWidth(), EPSILON);
		assertEquals(8.8, SB.calculateDimension(UFontFactory.sansSerif(32), "!").getWidth(), EPSILON);
		assertEquals(2.2, SB.calculateDimension(UFontFactory.sansSerif(8), "!").getWidth(), EPSILON);
	}

	@Test
	void testMultiCharIsSumOfAdvances() {
		// 'H'=11.6 'e'=8.9 'l'=3.6 'l'=3.6 'o'=8.9 -> 36.6 pt at size 16.
		final XDimension2D dim = SB.calculateDimension(UFontFactory.sansSerif(16), "Hello");
		assertEquals(36.6, dim.getWidth(), EPSILON);
	}

	@Test
	void testKnownGlyphWidths() {
		assertEquals(10.7, SB.calculateDimension(UFontFactory.sansSerif(16), "A").getWidth(), EPSILON);
		assertEquals(3.6, SB.calculateDimension(UFontFactory.sansSerif(16), "i").getWidth(), EPSILON);
		assertEquals(15.1, SB.calculateDimension(UFontFactory.sansSerif(16), "W").getWidth(), EPSILON);
		assertEquals(16.3, SB.calculateDimension(UFontFactory.sansSerif(16), "@").getWidth(), EPSILON);
	}

	@Test
	void testSpaceHasZeroWidth() {
		// The tabulated advance for U+0020 is 0.
		assertEquals(0.0, SB.calculateDimension(UFontFactory.sansSerif(16), " ").getWidth(), EPSILON);
	}

	@Test
	void testEmptyStringHasZeroWidth() {
		final XDimension2D dim = SB.calculateDimension(UFontFactory.sansSerif(16), "");
		assertEquals(0, dim.getWidth());
		assertEquals(16, dim.getHeight());
	}

	@Test
	void testSingleValueBlockUsesConstantWidth() {
		// Block 0x07 collapses to a single value {81} -> every code point in that
		// block has an advance of 8.1 pt at size 16.
		final XDimension2D dim = SB.calculateDimension(UFontFactory.sansSerif(16), "\u0700");
		assertEquals(8.1, dim.getWidth(), EPSILON);
	}

	@Test
	void testCharOutsideTableUsesFallbackWidth() {
		// The table covers blocks 0x00..0xFE. A code point in block 0xFF (>= U+FF00)
		// falls back to a fixed advance of 13 pt at size 16.
		assertEquals(13.0, SB.calculateDimension(UFontFactory.sansSerif(16), "\uFF21").getWidth(), EPSILON);
		assertEquals(26.0, SB.calculateDimension(UFontFactory.sansSerif(32), "\uFF21").getWidth(), EPSILON);
	}

}
