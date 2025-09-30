package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdErr;
import org.junitpioneer.jupiter.StdIo;

import net.sourceforge.plantuml.Run;

class RunFlagCheckOnlyTest extends AbstractCliTest {

	@StdIo
	@Test
	void testError(StdErr err) throws Exception {

		syntax_error(tempDir, "test.txt");

		assertExit(200, () -> {
			Run.main(new String[] { "-checkonly", tempDir.toAbsolutePath().toString() });
		});

		assertTrue(err.capturedString().contains("Some diagram description contains errors"));

//		System.out.println("tempDir:");
//		Files.list(tempDir).map(Path::getFileName).forEach(System.out::println);

		assertLs("[test.txt]", tempDir);

	}

	@Test
	void testAllOk() throws Exception {

		aliceBob_hello(tempDir, "test1.txt");
		aliceBob_hello(tempDir, "test2.txt");
		aliceBob_hello(tempDir, "test3.txt");
		aliceBob_hello(tempDir, "test4.txt");

		Run.main(new String[] { "-checkonly", tempDir.toAbsolutePath().toString() });

		assertLs("[test1.txt, test2.txt, test3.txt, test4.txt]", tempDir);

	}

}
