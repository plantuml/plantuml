package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.Isolated;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import net.sourceforge.plantuml.Run;
import net.sourceforge.plantuml.stats.StatsUtils;

@Execution(ExecutionMode.SAME_THREAD)
@Isolated
class RunStatsTest extends AbstractCliTest {

	final File html = new File("plantuml-stats.html");
	final File xml = new File("plantuml-stats.xml");

	@BeforeEach
	void setUp() {
		cleanAll();
	}

	@AfterEach
	void tearDown() {
		cleanAll();
	}

	private void cleanAll() {
		html.delete();
		xml.delete();
		assertFalse(html.exists());
		assertFalse(xml.exists());
		StatsUtils.setXmlStats(false);
		StatsUtils.setHtmlStats(false);
		StatsUtils.setRealTimeStats(false);
	}

	@Test
	@StdIo
	void test1(StdOut out) throws IOException, InterruptedException {

		aliceBob_hello(tempDir, "test.txt");

		for (int i = 0; i < 12; i++)
			Run.main(new String[] { "-enablestats", "-svg", tempDir.toAbsolutePath().toString() });

		Run.main(new String[] { "-dumpstats" });

		assertTrue(out.capturedString().contains("ID"));
		assertTrue(out.capturedString().contains("Mean(ms)"));

		assertTrue(out.capturedString().contains("Generated"));
	}

	@Test
	void testHtml() throws IOException, InterruptedException {

		aliceBob_hello(tempDir, "test.txt");

		for (int i = 0; i < 12; i++)
			Run.main(new String[] { "-enablestats", "-svg", tempDir.toAbsolutePath().toString() });

		Run.main(new String[] { "-dumphtmlstats" });

		assertTrue(html.exists());
		assertFalse(xml.exists());

	}

	@Test
	void testRealTimeXml() throws IOException, InterruptedException {

		aliceBob_hello(tempDir, "test.txt");

		for (int i = 0; i < 12; i++)
			Run.main(new String[] { "-enablestats", "-realtimestats", "-xmlstats", "-svg",
					tempDir.toAbsolutePath().toString() });

		assertFalse(html.exists());
		assertTrue(xml.exists());

	}

	@Test
	void testRealTimeHtml() throws IOException, InterruptedException {

		aliceBob_hello(tempDir, "test.txt");

		for (int i = 0; i < 12; i++)
			Run.main(new String[] { "-realtimestats", "-htmlstats", "-svg", tempDir.toAbsolutePath().toString() });

		assertTrue(html.exists());
		assertFalse(xml.exists());

	}

}
