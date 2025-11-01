package net.sourceforge.plantuml.nio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.preproc.Stdlib;

class NFolderStdlibTest {

	private NFolderStdlib root;

	@BeforeEach
	void setUp() throws IOException {
		// retrieve the stdlib once for the test class
		root = new NFolderStdlib(Stdlib.retrieve("osa2"));
	}

	@Test
	@DisplayName("root toString is normalized")
	void rootToString() {
		assertEquals("osa2!", root.toString());
	}

	@Test
	@DisplayName("navigating into 'user' appends segment")
	void userSubfolderToString() throws IOException {
		final NFolderStdlib user = (NFolderStdlib) root.getSubfolder(Path.of("user"));
		assertEquals("osa2!user", user.toString());
	}

	@Test
	@DisplayName("'..' from user goes back to root")
	void parentFromUserToString() throws IOException {
		final NFolderStdlib user = (NFolderStdlib) root.getSubfolder(Path.of("user"));
		final NFolderStdlib back = (NFolderStdlib) user.getSubfolder(Path.of(".."));
		assertEquals("osa2!", back.toString());
	}

	@Test
	@DisplayName("'../server' from user lands on server")
	void serverFromUserToString() throws IOException {
		final NFolderStdlib user = (NFolderStdlib) root.getSubfolder(Path.of("user"));
		final NFolderStdlib server = (NFolderStdlib) user.getSubfolder(Path.of("../server"));
		assertEquals("osa2!server", server.toString());
	}

	@Test
	@DisplayName("read entire UTF-8 content from an InputFile")
	void read1() throws IOException {
		InputFile file = root.getInputFile(Path.of("User/all.puml"));
		assertNotNull(file);

		final String content = readUtf8(file);
		assertNotNull(content);
		assertTrue(content.contains("sprite"));
		assertEquals("osa2!User/all.puml", file.toString());
	}

	@Test
	@DisplayName("read entire UTF-8 content from an InputFile")
	void read2() throws IOException {
		final NFolderStdlib user = (NFolderStdlib) root.getSubfolder(Path.of("user"));
		InputFile file = user.getInputFile(Path.of("all.puml"));
		assertNotNull(file);

		final String content = readUtf8(file);
		assertNotNull(content);
		assertTrue(content.contains("sprite"));
		assertEquals("osa2!user/all.puml", file.toString());
	}

	/* ------------ helpers ------------ */

	private static String readUtf8(InputFile in) throws IOException {
		try (InputStream is = in.newInputStream()) {
			return new String(is.readAllBytes(), StandardCharsets.UTF_8);
		}
	}

}
