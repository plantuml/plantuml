package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Isolated;

import net.sourceforge.plantuml.Run;
import net.sourceforge.plantuml.dot.GraphvizUtils;

@Isolated
class RunFlagPlantumlLimitSizeTest extends AbstractCliTest {

	@BeforeEach
	void setUp() {
		assertEquals(4096, GraphvizUtils.getenvImageLimit());
		System.setProperty("PLANTUML_LIMIT_SIZE", "32");
		assertEquals(32, GraphvizUtils.getenvImageLimit());
	}

	@AfterEach
	void tearDown() {
		System.setProperty("PLANTUML_LIMIT_SIZE", "4096");
		assertEquals(4096, GraphvizUtils.getenvImageLimit());
	}

	@Test
	void testPlantumlLimitSize() throws IOException, InterruptedException {
		Path file = aliceBob_hello(tempDir, "test.txt");

		assertEquals("32", System.getProperty("PLANTUML_LIMIT_SIZE"));
		assertEquals(32, GraphvizUtils.getenvImageLimit());

		Run.main(new String[] { "-DPLANTUML_LIMIT_SIZE=4200", "-svg", file.toAbsolutePath().toString() });
		assertEquals(4200, GraphvizUtils.getenvImageLimit());

		assertLs("[test.svg, test.txt]", tempDir);

	}

}
