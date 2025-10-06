package net.sourceforge.plantuml.picoweb;

import static java.nio.charset.StandardCharsets.UTF_8;
import static net.sourceforge.plantuml.code.TranscoderUtil.getDefaultTranscoder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.Isolated;
import org.junitpioneer.jupiter.StdErr;
import org.junitpioneer.jupiter.StdIo;

import net.sourceforge.plantuml.dot.GraphvizRuntimeEnvironment;
import net.sourceforge.plantuml.json.Json;
import net.sourceforge.plantuml.json.JsonObject;
import net.sourceforge.plantuml.log.Logme;

@DisabledOnOs(OS.WINDOWS)
@Execution(ExecutionMode.SAME_THREAD)
@Isolated
class PicowebTest {

	private int port;
	private HttpClient http;

	private ExecutorService executor;

	@BeforeEach
	void startServer() throws Exception {
		GraphvizRuntimeEnvironment.getInstance().setDotExecutable("this_exe_do_not_exist");
		
		PicoWebServer.enableStop = true;
		final ServerSocket serverSocket = new ServerSocket(0, 0, InetAddress.getByName("localhost"));
		port = serverSocket.getLocalPort();

		executor = Executors.newSingleThreadExecutor();

		executor.submit(() -> {
			try {
				PicoWebServer.serverLoop(serverSocket);
			} catch (IOException e) {
				Logme.error(e);
			}
		});

		http = HttpClient.newBuilder() //
				.connectTimeout(Duration.ofSeconds(3)) //
				.followRedirects(Redirect.NEVER) //
				.build();
	}

	@AfterEach
	void stopServer() throws Exception {
		GraphvizRuntimeEnvironment.getInstance().setDotExecutable(null);

		http_get("/stopserver");
		executor.shutdownNow();
	}

	@Test
	void get_language_returns_java_keywords() throws Exception {
		final HttpResponse<byte[]> resp = http_get("/language");
		assertStatus(resp, 200);
		assertTrue(body(resp).contains("abstract"));
	}

	@Test
	void get_txt_with_valid_source_returns_ascii() throws Exception {
		final String ok = getDefaultTranscoder().encode("A -> B");
		final HttpResponse<byte[]> resp = http_get("/txt/" + ok);
		assertStatus(resp, 200);
		assertContentType(resp.headers(), "text/plain");
		assertHeader(resp.headers(), "access-control-allow-origin");
		assertHeader(resp.headers(), "server");
		assertNoHeader(resp.headers(), "X-PlantUML-Diagram-Error");
		assertTrue(body(resp).contains("|A|"));
		assertTrue(body(resp).contains("|B|"));
	}

	@Test
	void get_svg_with_valid_source_returns_svg_root() throws Exception {
		final String ok = getDefaultTranscoder().encode("A -> B");
		final HttpResponse<byte[]> resp = http_get("/svg/" + ok);
		assertStatus(resp, 200);
		assertContentType(resp.headers(), "image/svg+xml");
		assertHeader(resp.headers(), "access-control-allow-origin");
		assertHeader(resp.headers(), "server");
		assertNoHeader(resp.headers(), "X-PlantUML-Diagram-Error");
		assertTrue(body(resp).startsWith("<svg "));
	}

	@Test
	void get_svg_via_plantuml_alias_returns_svg_root() throws Exception {
		final String ok = getDefaultTranscoder().encode("A -> B");
		final HttpResponse<byte[]> resp = http_get("/plantuml/svg/" + ok);
		assertStatus(resp, 200);
		assertContentType(resp.headers(), "image/svg+xml");
		assertHeader(resp.headers(), "access-control-allow-origin");
		assertHeader(resp.headers(), "server");
		assertNoHeader(resp.headers(), "X-PlantUML-Diagram-Error");
		assertTrue(body(resp).startsWith("<svg "));
	}

	@Test
	void get_png_with_valid_source_decodes_image() throws Exception {
		final String ok = getDefaultTranscoder().encode("A -> B");
		final HttpResponse<byte[]> resp = http_get("/png/" + ok);
		assertStatus(resp, 200);
		assertContentType(resp.headers(), "image/png");
		assertHeader(resp.headers(), "access-control-allow-origin");
		assertHeader(resp.headers(), "server");
		assertNoHeader(resp.headers(), "X-PlantUML-Diagram-Error");
		final BufferedImage image = asImage(resp);
		assertTrue(image.getWidth() > 10);
		assertTrue(image.getHeight() > 10);
	}

	@Test
	void get_png_via_plantuml_alias_decodes_image() throws Exception {
		final String ok = getDefaultTranscoder().encode("A -> B");
		final HttpResponse<byte[]> resp = http_get("/plantuml/png/" + ok);
		assertStatus(resp, 200);
		assertContentType(resp.headers(), "image/png");
		assertHeader(resp.headers(), "access-control-allow-origin");
		assertHeader(resp.headers(), "server");
		assertNoHeader(resp.headers(), "X-PlantUML-Diagram-Error");
		final BufferedImage image = asImage(resp);
		assertTrue(image.getWidth() > 10);
		assertTrue(image.getHeight() > 10);
	}

	@Test
	void get_png_with_invalid_source_returns_400_and_error_headers() throws Exception {
		final String ok = getDefaultTranscoder().encode("foo");
		final HttpResponse<byte[]> resp = http_get("/png/" + ok);
		assertStatus(resp, 400);
		assertContentType(resp.headers(), "image/png");
		assertHeader(resp.headers(), "access-control-allow-origin");
		assertHeader(resp.headers(), "server");
		assertHeader(resp.headers(), "X-PlantUML-Diagram-Error");
		assertHeader(resp.headers(), "X-PlantUML-Diagram-Error-Line");
		final BufferedImage image = asImage(resp);
		assertTrue(image.getWidth() > 10);
		assertTrue(image.getHeight() > 10);
	}

	@Test
	void get_unknown_path_redirects_with_302() throws Exception {
		final HttpResponse<byte[]> resp = http_get("/foo");
		assertStatus(resp, 302);
		assertHeader(resp.headers(), "location");
	}

	@Test
	void post_render_png_with_valid_source_returns_png() throws Exception {
		final HttpResponse<byte[]> resp = http_post_json("/render", renderRequestJson("A -> B"));
		assertStatus(resp, 200);
		assertContentType(resp.headers(), "image/png");
		assertHeader(resp.headers(), "access-control-allow-origin");
		assertHeader(resp.headers(), "server");
		assertNoHeader(resp.headers(), "X-PlantUML-Diagram-Error");
		final BufferedImage image = asImage(resp);
		assertTrue(image.getWidth() > 10);
		assertTrue(image.getHeight() > 10);
	}

	@Test
	void post_render_svg_with_tsvg_option_returns_svg() throws Exception {
		final HttpResponse<byte[]> resp = http_post_json("/render", renderRequestJson("A -> B", "-tsvg"));
		assertStatus(resp, 200);
		assertContentType(resp.headers(), "image/svg+xml");
		assertHeader(resp.headers(), "access-control-allow-origin");
		assertHeader(resp.headers(), "server");
		assertNoHeader(resp.headers(), "X-PlantUML-Diagram-Error");
		assertTrue(body(resp).startsWith("<svg "));
	}

	@Test
	void post_render_with_malformed_json_returns_400_and_message() throws Exception {
		final HttpResponse<byte[]> resp = http_post("/render", "application/json; charset=utf-8", "123abc");
		assertStatus(resp, 400);
		assertContentType(resp.headers(), "text/plain");
		assertTrue(body(resp).contains("Error parsing request json: Unexpected character"));

	}

	@Test
	@StdIo
	void get_png_bad_graphviz(StdErr err) throws Exception {
		final String ok = getDefaultTranscoder().encode("class 1\nclass B");
		final HttpResponse<byte[]> resp = http_get("/png/" + ok);
		assertStatus(resp, 503);
		assertContentType(resp.headers(), "image/png");
		assertHeader(resp.headers(), "access-control-allow-origin");
		assertHeader(resp.headers(), "server");
		assertNoHeader(resp.headers(), "X-PlantUML-Diagram-Error");
		final BufferedImage image = asImage(resp);
		assertTrue(image.getWidth() > 10);
		assertTrue(image.getHeight() > 10);
		// assertTrue(err.capturedString().contains("this_exe_do_not_exist"));
	}

	@Test
	@StdIo
	void get_png_svg_graphviz(StdErr err) throws Exception {
		final String ok = getDefaultTranscoder().encode("class 1\nclass B");
		final HttpResponse<byte[]> resp = http_get("/svg/" + ok);
		assertStatus(resp, 503);
		assertContentType(resp.headers(), "image/svg+xml");
		assertHeader(resp.headers(), "access-control-allow-origin");
		assertHeader(resp.headers(), "server");
		assertNoHeader(resp.headers(), "X-PlantUML-Diagram-Error");
		assertTrue(body(resp).startsWith("<svg "));		
		// assertTrue(err.capturedString().contains("this_exe_do_not_exist"));
	}

	// --------------------------------------

	private HttpResponse<byte[]> http_get(String path) throws Exception {
		final HttpRequest req = HttpRequest.newBuilder() //
				.uri(URI.create("http://localhost:" + port + path)) //
				.timeout(Duration.ofSeconds(5)) //
				.GET() //
				.build();
		return http.send(req, HttpResponse.BodyHandlers.ofByteArray());
	}

	private HttpResponse<byte[]> http_post_json(String path, String json) throws Exception {
		return http_post(path, "application/json; charset=utf-8", json);
	}

	private HttpResponse<byte[]> http_post(String path, String contentType, String body) throws Exception {
		final HttpRequest req = HttpRequest.newBuilder() //
				.uri(URI.create("http://localhost:" + port + path)) //
				.timeout(Duration.ofSeconds(5)) //
				.header("Content-Type", contentType) //
				.POST(HttpRequest.BodyPublishers.ofString(body, UTF_8)) //
				.build();
		return http.send(req, HttpResponse.BodyHandlers.ofByteArray());
	}

	private static String renderRequestJson(String source, String... options) {
		final JsonObject object = Json.object();
		object.add("source", source);
		if (options != null && options.length > 0)
			object.add("options", Json.array(options));

		return object.toString();
	}

	private String body(final HttpResponse<byte[]> resp) {
		return new String(resp.body());
	}

	private static BufferedImage asImage(HttpResponse<byte[]> resp) throws IOException {
		return ImageIO.read(new ByteArrayInputStream(resp.body()));
	}

	private void assertStatus(HttpResponse<?> resp, int expected) {
		assertEquals(expected, resp.statusCode());
	}

	private static void assertContentType(HttpHeaders headers, String expected) {
		final List<String> values = headers.allValues("Content-Type");
		assertTrue(values.contains(expected), values.toString());
	}

	private void assertNoHeader(HttpHeaders headers, String key) {
		assertFalse(headers.map().containsKey(key));
	}

	private void assertHeader(HttpHeaders headers, String key) {
		assertTrue(headers.map().containsKey(key));
	}

}
