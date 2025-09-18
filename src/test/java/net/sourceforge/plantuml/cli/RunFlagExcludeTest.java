package net.sourceforge.plantuml.cli;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.Run;

class RunFlagExcludeTest extends AbstractCliTest {

	@Test
	void testAllOk() throws Exception {

		for (int i = 0; i < 4; i++)
			aliceBob_hello(tempDir, "test" + i + ".txt");

		Run.main(new String[] { "-svg", tempDir.toAbsolutePath().toString() });

		assertLs("[test0.svg, test0.txt, test1.svg, test1.txt, test2.svg, test2.txt, test3.svg, test3.txt]", tempDir);

	}

	@Test
	void testExclude3() throws Exception {

		for (int i = 0; i < 4; i++)
			aliceBob_hello(tempDir, "test" + i + ".txt");

		Run.main(new String[] { "-svg", "-x", "**/test3.*", tempDir.toAbsolutePath().toString() });

		assertLs("[test0.svg, test0.txt, test1.svg, test1.txt, test2.svg, test2.txt, test3.txt]", tempDir);

	}

	@Test
	void testExclude23() throws Exception {

		for (int i = 0; i < 4; i++)
			aliceBob_hello(tempDir, "test" + i + ".txt");

		Run.main(new String[] { "-svg", "-x", "**/test2.*", "-x", "**/test3.*", tempDir.toAbsolutePath().toString() });

		assertLs("[test0.svg, test0.txt, test1.svg, test1.txt, test2.txt, test3.txt]", tempDir);

	}

}
