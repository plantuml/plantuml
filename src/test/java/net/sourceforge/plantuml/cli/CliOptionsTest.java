package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

class CliOptionsTest {

	@Test
	void test1() throws InterruptedException, IOException {
		final CliOptions options = new CliOptions("-graphvizdot", "foo.exe");
		assertEquals(null, options.flags.getString(CliFlag.OUTPUT_DIR));

		assertEquals(null, options.getOutputDir());
	}

	@Test
	void test2() throws InterruptedException, IOException {
		final CliOptions options = new CliOptions("-graphvizdot", "foo.exe", "-output", "mydir");
		assertEquals("mydir", options.flags.getString(CliFlag.OUTPUT_DIR));
		
		assertEquals(new File("mydir"), options.getOutputDir());
		
	}

	@Test
	void test3() throws InterruptedException, IOException {
		final CliOptions options = new CliOptions("-graphvizdot", "foo.exe", "-ofile", "myfile");
		assertEquals(null, options.flags.getString(CliFlag.OUTPUT_DIR));

		assertEquals(null, options.getOutputDir());		
	}

}
