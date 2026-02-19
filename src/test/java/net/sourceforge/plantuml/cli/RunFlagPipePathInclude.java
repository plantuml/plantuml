package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.Isolated;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import net.sourceforge.plantuml.Run;
import net.sourceforge.plantuml.nio.NFolder;

@Execution(ExecutionMode.SAME_THREAD)
@Isolated
class RunFlagPipePathInclude extends AbstractCliTest {

	@TempDir
	public Path tempDir;

	private String saveIncludePath;

	@BeforeEach
	void setUp() {
		this.saveIncludePath = System.getProperty(NFolder.PATHS_INCLUDES);
	}

	@AfterEach
	void tearDown() {
		if (this.saveIncludePath == null)
			System.setProperty(NFolder.PATHS_INCLUDES, "");
		else
			System.setProperty(NFolder.PATHS_INCLUDES, this.saveIncludePath);
	}

	@StdIo({ "@startuml", "!include style.iuml", "Alice->Bob: hello", "@enduml" })
	@Test
	void test1(StdOut out) throws Exception {

		final Path styleIuml = tempDir.resolve("style.iuml");
		Files.writeString(styleIuml, String.join(System.lineSeparator(), "Alice->Bob: dummy"));

		System.setProperty(NFolder.PATHS_INCLUDES, tempDir.toAbsolutePath().toString());

		assertLs("[style.iuml]", tempDir);

		Run.main(new String[] { "-pipe", "-svg" });

		assertTrue(out.capturedString().contains("<svg"));
		assertTrue(out.capturedString().contains("Alice"));
		assertTrue(out.capturedString().contains("Bob"));
		assertTrue(out.capturedString().contains("hello"));
		assertTrue(out.capturedString().contains("dummy"));
		assertTrue(out.capturedString().contains("</svg>"));
	}


}
