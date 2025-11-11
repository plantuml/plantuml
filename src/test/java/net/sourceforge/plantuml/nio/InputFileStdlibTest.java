package net.sourceforge.plantuml.nio;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.preproc.Stdlib;

class InputFileStdlibTest {

	@Test
	void testNewInputStream() throws IOException {

		InputFileStdlib cut = new InputFileStdlib(Stdlib.retrieve("osa2"), Paths.get("ExampleHardware"));
		final InputStream is = cut.newInputStream();
		assertNotNull(is);

	}

}
