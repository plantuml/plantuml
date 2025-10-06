package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Isolated;
import org.junitpioneer.jupiter.StdErr;
import org.junitpioneer.jupiter.StdIo;

import net.sourceforge.plantuml.Run;

@Isolated
class RunFlagStdrptTest extends AbstractCliTest {

	@Test
	@StdIo
	void test(StdErr err) throws Exception {
		Path f = syntax_error(tempDir, "test.txt");

		assertExit(200, () -> {
			Run.main(new String[] { "-stdrpt", "-svg", tempDir.toAbsolutePath().toString() });
		});

		assertLs("[test.svg, test.txt]", tempDir);

		assertLineSplitContains(err.capturedString(), "Error line 2 in file: " + f.toAbsolutePath().toString(),
				"Some diagram description contains errors");

	}

	@Test
	@StdIo
	void test1(StdErr err) throws Exception {
		Path f = syntax_error(tempDir, "test.txt");

		assertExit(200, () -> {
			Run.main(new String[] { "-stdrpt:1", "-svg", tempDir.toAbsolutePath().toString() });
		});

		assertLs("[test.svg, test.txt]", tempDir);

		assertLineSplitContains(err.capturedString(), "protocolVersion=1", "status=ERROR", "lineNumber=2",
				"label=Syntax Error?", "Error line 2 in file: " + f.toAbsolutePath().toString(),
				"Some diagram description contains errors");

	}

	@Test
	@StdIo
	void test2(StdErr err) throws Exception {
		syntax_error(tempDir, "test.txt");

		assertExit(200, () -> {
			Run.main(new String[] { "-stdrpt:2", "-svg", tempDir.toAbsolutePath().toString() });
		});

		assertLs("[test.svg, test.txt]", tempDir);

		assertEquals("test.txt:2:error:Syntax Error?", cleanControlChars(err.capturedString()));

	}

}
