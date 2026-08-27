package net.sourceforge.plantuml.asciiverse;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.FileFormat;

// Regression coverage for the encoding half of the newpage/asciiverse fix:
// exportTxt() used to build its PrintStream with SecurityUtils.
// createPrintStream(os) (the 1-arg overload, platform-default charset), so
// every Unicode box-drawing character it wrote (getHLineChar()/getVLineChar()
// and friends, all above the ASCII range) silently became '?' on any JVM
// whose default charset isn't already UTF-8 -- exactly what plantuml-server's
// CI runner hit (its default charset is not UTF-8), while this sandbox's own
// default happens to already be UTF-8, which is precisely why a test that
// only compares against the *ambient* default charset would not have caught
// it here. This test instead pins the byte-level contract directly: whatever
// exportTxt() writes must decode as UTF-8 to the exact drawn characters,
// independent of what Charset.defaultCharset() happens to be on the machine
// running the test.
class InfinitePlanExportTxtTest {

	@Test
	void exportTxtEncodesUnicodeBoxDrawingCharactersAsUtf8() throws IOException {
		final InfinitePlan plan = new InfinitePlan(FileFormat.UTXT);

		// A single row: a Unicode top-left corner-ish shape using the same
		// glyphs InfinitePlan itself draws boxes/lifelines with.
		plan.drawChar(plan.getHLineChar()); // ─
		plan.move(1, 0).drawChar(plan.getVLineChar()); // │

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		plan.exportTxt(baos);

		final byte[] actual = baos.toByteArray();
		final byte[] expected = ("─│" + System.lineSeparator()).getBytes(UTF_8);

		assertArrayEquals(expected, actual, "exportTxt() must emit UTF-8 bytes for Unicode box-drawing characters, "
				+ "regardless of the JVM's default charset");

		// The specific failure mode this guards against: under the old
		// platform-default PrintStream, a non-UTF-8-default JVM would replace
		// each of these multi-byte characters with a single '?' (0x3F) byte.
		// Assert neither drawn character was munged into that placeholder.
		final String decoded = new String(actual, UTF_8);
		assertFalse(decoded.contains("?"), "output should contain no '?' replacement characters: " + decoded);
		assertTrue(decoded.startsWith("─│"), "output should contain the real Unicode glyphs: " + decoded);
	}

}
