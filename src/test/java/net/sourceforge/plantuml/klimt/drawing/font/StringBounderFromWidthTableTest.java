package net.sourceforge.plantuml.klimt.drawing.font;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

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
		assertThat(SB.getFileFormat()).isEqualTo(FileFormat.SVG);
	}

	@Test
	void testHeightEqualsFontSize() {
		assertThat(SB.calculateDimension(UFontFactory.sansSerif(16), "anything").getHeight()).isEqualTo(16);
		assertThat(SB.calculateDimension(UFontFactory.sansSerif(42), "anything").getHeight()).isEqualTo(42);
	}

	@Test
	void testSingleCharAtReferenceSize() {
		// '!' (U+0021) has a tabulated advance of 4.4 pt at size 16.
		final XDimension2D dim = SB.calculateDimension(UFontFactory.sansSerif(16), "!");
		assertThat(dim.getWidth()).isCloseTo(4.4, within(EPSILON));
		assertThat(dim.getHeight()).isEqualTo(16);
	}

	@Test
	void testWidthScalesLinearlyWithFontSize() {
		// The reference size is 16, so factor = size / 16.
		assertThat(SB.calculateDimension(UFontFactory.sansSerif(16), "!").getWidth()).isCloseTo(4.4, within(EPSILON));
		assertThat(SB.calculateDimension(UFontFactory.sansSerif(32), "!").getWidth()).isCloseTo(8.8, within(EPSILON));
		assertThat(SB.calculateDimension(UFontFactory.sansSerif(8), "!").getWidth()).isCloseTo(2.2, within(EPSILON));
	}

	@Test
	void testMultiCharIsSumOfAdvances() {
		// 'H'=11.6 'e'=8.9 'l'=3.6 'l'=3.6 'o'=8.9 -> 36.6 pt at size 16.
		final XDimension2D dim = SB.calculateDimension(UFontFactory.sansSerif(16), "Hello");
		assertThat(dim.getWidth()).isCloseTo(36.6, within(EPSILON));
	}

	@Test
	void testKnownGlyphWidths() {
		assertThat(SB.calculateDimension(UFontFactory.sansSerif(16), "A").getWidth()).isCloseTo(10.7, within(EPSILON));
		assertThat(SB.calculateDimension(UFontFactory.sansSerif(16), "i").getWidth()).isCloseTo(3.6, within(EPSILON));
		assertThat(SB.calculateDimension(UFontFactory.sansSerif(16), "W").getWidth()).isCloseTo(15.1, within(EPSILON));
		assertThat(SB.calculateDimension(UFontFactory.sansSerif(16), "@").getWidth()).isCloseTo(16.3, within(EPSILON));
	}

	@Test
	void testSpaceHasZeroWidth() {
		// The tabulated advance for U+0020 is 0.
		assertThat(SB.calculateDimension(UFontFactory.sansSerif(16), " ").getWidth()).isCloseTo(0.0, within(EPSILON));
	}

	@Test
	void testEmptyStringHasZeroWidth() {
		final XDimension2D dim = SB.calculateDimension(UFontFactory.sansSerif(16), "");
		assertThat(dim.getWidth()).isEqualTo(0);
		assertThat(dim.getHeight()).isEqualTo(16);
	}

	@Test
	void testSingleValueBlockUsesConstantWidth() {
		// Block 0x07 collapses to a single value {81} -> every code point in that
		// block has an advance of 8.1 pt at size 16.
		final XDimension2D dim = SB.calculateDimension(UFontFactory.sansSerif(16), "\u0700");
		assertThat(dim.getWidth()).isCloseTo(8.1, within(EPSILON));
	}

	@Test
	void testCharOutsideTableUsesFallbackWidth() {
		// The table covers blocks 0x00..0xFE. A code point in block 0xFF (>= U+FF00)
		// falls back to a fixed advance of 13 pt at size 16.
		assertThat(SB.calculateDimension(UFontFactory.sansSerif(16), "\uFF21").getWidth()).isCloseTo(13.0,
				within(EPSILON));
		assertThat(SB.calculateDimension(UFontFactory.sansSerif(32), "\uFF21").getWidth()).isCloseTo(26.0,
				within(EPSILON));
	}

}
