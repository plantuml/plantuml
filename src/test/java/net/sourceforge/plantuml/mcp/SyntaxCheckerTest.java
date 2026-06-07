package net.sourceforge.plantuml.mcp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * TDD specification for {@link SyntaxChecker}.
 *
 * <p>
 * These tests describe the intended behaviour before the implementation exists.
 * They are expected to fail until {@link SyntaxChecker#check(String)} and
 * {@link McpResult} are implemented.
 */
class SyntaxCheckerTest {

	private final SyntaxChecker checker = new SyntaxChecker();

	// --- Warning ---

	@Test
	void test_warning() throws IOException {
		final String source = "@startuml\nskinparam handwritten true\nAlice -> Bob\n@enduml";
		final McpResult result = checker.check(source);
		assertNotNull(result);
		assertTrue(result.isOk());
		assertEquals(1, result.getWarnings().size());
		assertEquals("[Please use '!option handwritten true' to enable handwritten ]", result.getWarnings().toString());
	}

	// --- Valid diagrams ---

	@Test
	void test_valid_sequence_is_ok() throws IOException {
		final String source = "@startuml\nAlice -> Bob\n@enduml";
		final McpResult result = checker.check(source);
		assertNotNull(result);
		assertTrue(result.isOk());
	}

	@Test
	void test_valid_sequence_reports_no_error() throws IOException {
		final String source = "@startuml\nAlice -> Bob\n@enduml";
		final McpResult result = checker.check(source);
		assertEquals(-1, result.getErrorLineNumber());
		assertEquals("", result.getErrorMessage());
	}

	@Test
	void test_valid_sequence_detects_diagram_type() throws IOException {
		final String source = "@startuml\nAlice -> Bob : hello\n@enduml";
		final McpResult result = checker.check(source);
		assertNotNull(result.getDiagramType());
		assertTrue(result.getDiagramType().toLowerCase().contains("sequence"));
	}

	@Test
	void test_valid_class_diagram_is_ok() throws IOException {
		final String source = "@startuml\nclass Foo\nclass Bar\nFoo --> Bar\n@enduml";
		final McpResult result = checker.check(source);
		assertTrue(result.isOk());
	}

	@Test
	void test_valid_diagram_line_count() throws IOException {
		final String source = "@startuml\nAlice -> Bob\nBob -> Alice\n@enduml";
		final McpResult result = checker.check(source);
		assertEquals(4, result.getLineCount());
	}

	@Test
	void test_valid_diagram_has_no_null_warnings() throws IOException {
		final String source = "@startuml\nAlice -> Bob\n@enduml";
		final McpResult result = checker.check(source);
		assertNotNull(result.getWarnings());
		assertEquals(0, result.getWarnings().size());
	}

	// --- Invalid diagrams ---

	@Test
	void test_garbage_line_is_not_ok() throws IOException {
		final String source = "@startuml\nAlice -> Bob\n%%% not valid %%%\n@enduml";
		final McpResult result = checker.check(source);
		assertFalse(result.isOk());
		assertEquals("%%% not valid %%%", result.getErrorLine());
	}

	@Test
	void test_error_has_message() throws IOException {
		final String source = "@startuml\n%%% not valid %%%\n@enduml";
		final McpResult result = checker.check(source);
		assertNotNull(result.getErrorMessage());
		assertFalse(result.getErrorMessage().isEmpty());
		assertEquals("%%% not valid %%%", result.getErrorLine());
	}

	@Test
	void test_error_line_is_one_based() throws IOException {
		// The offending line is the second physical line (1-based), so getErrorLine()
		// must report 2, not the 0-based position that the core uses internally.
		final String source = "@startuml\n%%% not valid %%%\n@enduml";
		final McpResult result = checker.check(source);
		assertEquals(2, result.getErrorLineNumber());
		assertEquals("%%% not valid %%%", result.getErrorLine());
	}

	@Test
	void test_no_start_tag_is_not_ok() throws IOException {
		final String source = "Alice -> Bob";
		final McpResult result = checker.check(source);
		assertFalse(result.isOk());
		assertEquals("The input must start with a @start... directive (for example @startuml)",
				result.getErrorMessage());
		assertFalse(result.isOk());
	}

	@Test
	void test_empty_source_is_not_ok() throws IOException {
		final McpResult result = checker.check("");
		assertNotNull(result);
		assertEquals("The input must start with a @start... directive (for example @startuml)",
				result.getErrorMessage());
		assertFalse(result.isOk());
	}

}
