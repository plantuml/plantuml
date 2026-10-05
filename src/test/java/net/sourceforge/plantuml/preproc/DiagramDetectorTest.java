package net.sourceforge.plantuml.preproc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import net.sourceforge.plantuml.nio.InputFile;
import net.sourceforge.plantuml.nio.NFolder;

class DiagramDetectorTest {

	@ParameterizedTest
	@ValueSource(strings = { "(", "2147483648" })
	void an_invalid_selector_closes_the_file(String selector) {
		final TrackedFile file = new TrackedFile("@startuml(id=FIRST)\nAlice -> Bob\n@enduml");

		assertThrows(IllegalArgumentException.class, () -> DiagramDetector.extractFromFile(file, "test", selector));

		assertEquals(file.opened, file.closed);
	}

	@Test
	void a_selected_diagram_stays_open_until_the_caller_closes_it() throws IOException {
		final TrackedFile file = new TrackedFile("@startuml\nAlice -> Bob\n@enduml");

		try (ReadLine reader = DiagramDetector.extractFromFile(file, "test", "0")) {
			assertNotNull(reader);
			assertEquals(1, file.opened - file.closed);
			assertEquals("Alice -> Bob", reader.readLine().getString());
		}

		assertEquals(file.opened, file.closed);
	}

	@Test
	void a_file_without_a_diagram_is_closed() throws IOException {
		final TrackedFile file = new TrackedFile("Alice -> Bob");

		assertNull(DiagramDetector.extractFromFile(file, "test", "0"));

		assertEquals(file.opened, file.closed);
	}

	@Test
	void a_close_failure_does_not_hide_the_invalid_selector() {
		final TrackedFile file = new TrackedFile("@startuml\nAlice -> Bob\n@enduml");
		file.failSecondClose = true;

		final NumberFormatException failure = assertThrows(NumberFormatException.class,
				() -> DiagramDetector.extractFromFile(file, "test", "2147483648"));

		assertEquals(file.opened, file.closed);
		assertEquals(1, failure.getSuppressed().length);
		assertEquals("close failed", failure.getSuppressed()[0].getMessage());
	}

	private static class TrackedFile implements InputFile {
		private final byte[] content;
		private int opened;
		private int closed;
		private boolean failSecondClose;

		private TrackedFile(String content) {
			this.content = content.getBytes(StandardCharsets.UTF_8);
		}

		@Override
		public InputStream newInputStream() {
			opened++;
			return new ByteArrayInputStream(content) {
				@Override
				public void close() throws IOException {
					closed++;
					super.close();
					if (failSecondClose && closed == 2)
						throw new IOException("close failed");
				}
			};
		}

		@Override
		public NFolder getParentFolder() {
			throw new UnsupportedOperationException();
		}
	}
}
