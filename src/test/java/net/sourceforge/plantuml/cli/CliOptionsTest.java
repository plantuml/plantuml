package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.FileFormat;

class CliOptionsTest {

	@Test
	void test1() throws InterruptedException, IOException, CliParsingException {
		final CliOptions options = new CliOptions("-graphvizdot", "foo.exe");
		assertEquals(null, options.flags.getString(CliFlag.OUTPUT_DIR));

		assertEquals(null, options.getOutputDir());
	}

	@Test
	void test2() throws InterruptedException, IOException, CliParsingException {
		final CliOptions options = new CliOptions("-graphvizdot", "foo.exe", "-output", "mydir");
		assertEquals("mydir", options.flags.getString(CliFlag.OUTPUT_DIR));
		
		assertEquals(new File("mydir"), options.getOutputDir());
		
	}

	@Test
	void testDecimal() throws InterruptedException, IOException, CliParsingException {
		// --decimal N: the number of decimals of the numbers of an SVG (used by the Vega tests of the C build)
		assertEquals(-1, new CliOptions("--svg-vega").getFileFormatOption().getDecimal());
		assertEquals(1, new CliOptions("--svg-vega", "--decimal", "1").getFileFormatOption().getDecimal());
		assertEquals(0, new CliOptions("--decimal", "0", "--svg-vega").getFileFormatOption().getDecimal());
		assertEquals(3, new CliOptions("--svg", "--decimal", " 3 ").getFileFormatOption().getDecimal());
		// a value that is not a number, or is negative, is ignored
		assertEquals(-1, new CliOptions("--svg", "--decimal", "many").getFileFormatOption().getDecimal());
		assertEquals(-1, new CliOptions("--svg", "--decimal", "-2").getFileFormatOption().getDecimal());
		// the other options of the format are kept
		assertEquals(FileFormat.SVG_DETERMINISTIC,
				new CliOptions("--svg-vega", "--decimal", "2").getFileFormatOption().getFileFormat());
	}

	@Test
	void test3() throws InterruptedException, IOException, CliParsingException {
		final CliOptions options = new CliOptions("-graphvizdot", "foo.exe", "-ofile", "myfile");
		assertEquals(null, options.flags.getString(CliFlag.OUTPUT_DIR));

		assertEquals(null, options.getOutputDir());		
	}

}
