package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.Isolated;

import net.sourceforge.plantuml.Run;
import net.sourceforge.plantuml.code.NoPlantumlCompressionException;

@Execution(ExecutionMode.SAME_THREAD)
@Isolated
class RunFlagOverwriteTest extends AbstractCliTest {

	Path file;
	Path ofileSvg;

	@BeforeEach
	void setUp() {
		GlobalConfig.getInstance().put(GlobalConfigKey.OVERWRITE, false);
		file = tempDir.resolve("test.txt");
		ofileSvg = tempDir.resolve("test.svg");
	}

	@AfterEach
	void tearDown() {
		// Reset overwrite flag after each test
		GlobalConfig.getInstance().put(GlobalConfigKey.OVERWRITE, false);
	}

	@Test
	void testNormal() throws IOException, InterruptedException {

		alice_bob_hello1();
		runSvg();

		assertLs("[test.svg, test.txt]", tempDir);

		assertTrue(containsHello1(ofileSvg));
		assertFalse(containsHello2(ofileSvg));

		alice_bob_hello2();
		runSvg();
		// After regeneration, hello1 should be replaced by hello2
		assertFalse(containsHello1(ofileSvg));
		assertTrue(containsHello2(ofileSvg));

	}

	@Test
	void testReadOnly() throws IOException, InterruptedException {

		alice_bob_hello1();
		runSvg();

		assertLs("[test.svg, test.txt]", tempDir);

		assertTrue(containsHello1(ofileSvg));
		assertFalse(containsHello2(ofileSvg));

		ofileSvg.toFile().setWritable(false);

		alice_bob_hello2();
		runSvg();
		// Second run: try to overwrite with hello2 (should fail due to read-only)
		assertTrue(containsHello1(ofileSvg));
		assertFalse(containsHello2(ofileSvg));

	}

	@Test
	void testReadOnlyAndOverwrite() throws IOException, InterruptedException {

		alice_bob_hello1();
		runSvg();

		assertLs("[test.svg, test.txt]", tempDir);

		assertTrue(containsHello1(ofileSvg));
		assertFalse(containsHello2(ofileSvg));

		ofileSvg.toFile().setWritable(false);

		alice_bob_hello2();
		runSvgOverwrite();
		// Second run with -overwrite: should force overwrite even if file is read-only
		assertFalse(containsHello1(ofileSvg));
		assertTrue(containsHello2(ofileSvg));

	}

	private void alice_bob_hello1() throws IOException {
		Files.writeString(file, String.join(System.lineSeparator(), "@startuml", "alice->bob : hello1", "@enduml"));
	}

	private void alice_bob_hello2() throws IOException {
		Files.writeString(file, String.join(System.lineSeparator(), "@startuml", "alice->bob : hello2", "@enduml"));
	}

	private boolean containsHello1(Path ofileSvg) throws IOException {
		return new String(Files.readAllBytes(ofileSvg), java.nio.charset.StandardCharsets.UTF_8).contains("hello1");
	}

	private boolean containsHello2(Path ofileSvg) throws IOException {
		return new String(Files.readAllBytes(ofileSvg), java.nio.charset.StandardCharsets.UTF_8).contains("hello2");
	}

	private void runSvg() throws NoPlantumlCompressionException, IOException, InterruptedException {
		Run.main(new String[] { "-svg", file.toAbsolutePath().toString() });
	}

	private void runSvgOverwrite() throws NoPlantumlCompressionException, IOException, InterruptedException {
		Run.main(new String[] { "-overwrite", "-svg", file.toAbsolutePath().toString() });
	}

}
