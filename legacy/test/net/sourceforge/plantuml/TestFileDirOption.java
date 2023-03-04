package net.sourceforge.plantuml;

import static java.nio.charset.StandardCharsets.UTF_8;
import static net.sourceforge.plantuml.test.TestUtils.writeUtf8File;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import net.sourceforge.plantuml.picoweb.PicoWebServer;
import net.sourceforge.plantuml.picoweb.RenderRequest;

public class TestFileDirOption {

	@TempDir
	Path tempDir;

	@BeforeEach
	public void beforeEach() throws Exception {

		writeUtf8File(tempDir.resolve("include.iuml"), INCLUDE);
	}

	//
	// Test Cases
	//

	@Test
	public void test_picoweb_without_filedir_cannot_find_include() throws Exception {

		final String output = renderViaPicoWeb();
		assertThat(output).contains("cannot include include.iuml");
	}

	@Test
	public void test_picoweb_with_filedir_succeeds() throws Exception {

		final String output = renderViaPicoWeb("-filedir", tempDir.toString());
		assertThat(output).contains("included-ok");
	}

	@Test
	public void test_pipe_without_filedir_cannot_find_include() throws Exception {

		final String output = renderViaPipe();
		assertThat(output).contains("cannot include include.iuml");
	}

	@Test
	public void test_pipe_with_filedir_succeeds() throws Exception {

		final String output = renderViaPipe("-filedir", tempDir.toString());
		assertThat(output).contains("included-ok");
	}

	//
	// Test DSL
	//

	private static final String[] COMMON_OPTIONS = {"-tutxt"};

	private static final String DIAGRAM = "" +
			"@startuml\n" +
			"!include include.iuml\n" +
			"a -> b\n" +
			"@enduml\n";

	private static final String INCLUDE = "title included-ok\n";


	private String[] optionArray(String... extraOptions) {

		final List<String> list = newArrayList(COMMON_OPTIONS);
		Collections.addAll(list, extraOptions);
		return list.toArray(new String[0]);
	}

	private String renderViaPicoWeb(String... extraOptions) throws Exception {

		final RenderRequest renderRequest = new RenderRequest(optionArray(extraOptions), DIAGRAM);

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();

		final PicoWebServer picoWebServer = new PicoWebServer(null);

		picoWebServer.handleRenderRequest(renderRequest, new BufferedOutputStream(baos));

		final String httpResponse = new String(baos.toByteArray(), UTF_8);

		return httpResponse.substring(httpResponse.indexOf("\n\r\n") + 3);  // return just the HTTP body
	}

	private String renderViaPipe(String... extraOptions) throws Exception {

		final Option option = new Option(optionArray(extraOptions));

		final ByteArrayInputStream bais = new ByteArrayInputStream(DIAGRAM.getBytes(UTF_8));

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();

		final Pipe pipe = new Pipe(option, new PrintStream(baos), bais, option.getCharset());

		pipe.managePipe(ErrorStatus.init());

		return new String(baos.toByteArray(), UTF_8);
	}

}
