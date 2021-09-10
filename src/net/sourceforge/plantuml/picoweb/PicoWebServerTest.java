package net.sourceforge.plantuml.picoweb;

import net.sourceforge.plantuml.json.Json;
import net.sourceforge.plantuml.json.JsonObject;

import javax.imageio.ImageIO;
import javax.imageio.stream.MemoryCacheImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

import static java.nio.charset.StandardCharsets.UTF_8;
import static net.sourceforge.plantuml.code.TranscoderUtil.getDefaultTranscoder;

// Newer Java versions have nice built-in HTTP classes in the jdk.incubator.httpclient / java.net.http packages
// but PlantUML supports older Java versions so the tests here use a kludgy approach to HTTP.

// Multi-line strings here start with a "" so that IDE auto-indenting will leave the rest of the string nicely
// aligned

public class PicoWebServerTest {

	public static void main(String[] args) throws Exception {
		startServer();
		test_basic_http();
		test_GET_png();
		test_GET_svg();
		test_POST_render();
		test_unknown_path();
	}

	//
	// Test Cases
	//

	private static void test_basic_http() throws Exception {
		assert httpRaw(
				""
		).equals("" +
				"HTTP/1.1 400 Bad Request\n" +
				"Content-type: text/plain\n" +
				"Content-length: 16\n" +
				"\n" +
				"Bad request line"
		);

		assert httpRaw(
				"GET"
		).equals("" +
				"HTTP/1.1 400 Bad Request\n" +
				"Content-type: text/plain\n" +
				"Content-length: 16\n" +
				"\n" +
				"Bad request line"
		);

		assert httpRaw("" +
				"GET /foo HTTP/1.1\n" +
				"Content-Length: bar\n"
		).equals("" +
				"HTTP/1.1 400 Bad Request\n" +
				"Content-type: text/plain\n" +
				"Content-length: 22\n" +
				"\n" +
				"Invalid content length"
		);

		assert httpRaw("" +
				"GET /foo HTTP/1.1\n" +
				"Content-Length: -1\n"
		).equals("" +
				"HTTP/1.1 400 Bad Request\n" +
				"Content-type: text/plain\n" +
				"Content-length: 23\n" +
				"\n" +
				"Negative content length"
		);

		assert httpRaw("" +
				"GET /foo HTTP/1.1\n" +
				"Content-Length: 3\n" +
				"\n" +
				"12"
		).equals("" +
				"HTTP/1.1 400 Bad Request\n" +
				"Content-type: text/plain\n" +
				"Content-length: 14\n" +
				"\n" +
				"Body too short"
		);
	}

	private static void test_GET_png() throws Exception {
		HttpURLConnection response;

		response = httpGet("/png/" + getDefaultTranscoder().encode("A -> B"));
		assert response.getResponseCode() == 200;
		assert response.getHeaderField("X-PlantUML-Diagram-Error") == null;
		assert response.getHeaderField("X-PlantUML-Diagram-Error-Line") == null;
		assert response.getContentType().equals("image/png");
		assert readStreamAsImage(response.getInputStream()) != null;

		response = httpGet("/plantuml/png/" + getDefaultTranscoder().encode("A -> B"));
		assert response.getResponseCode() == 200;
		assert response.getHeaderField("X-PlantUML-Diagram-Error") == null;
		assert response.getHeaderField("X-PlantUML-Diagram-Error-Line") == null;
		assert response.getContentType().equals("image/png");
		assert readStreamAsImage(response.getInputStream()) != null;

		response = httpGet("/png/" + getDefaultTranscoder().encode("foo"));
		assert response.getResponseCode() == 400;
		assert response.getHeaderField("X-PlantUML-Diagram-Error").equals("Syntax Error?");
		assert response.getHeaderField("X-PlantUML-Diagram-Error-Line").equals("2");
		assert response.getContentType().equals("image/png");
		assert readStreamAsImage(response.getErrorStream()) != null;
	}

	private static void test_GET_svg() throws Exception {
		HttpURLConnection response;

		response = httpGet("/svg/" + getDefaultTranscoder().encode("A -> B"));
		assert response.getResponseCode() == 200;
		assert response.getHeaderField("X-PlantUML-Diagram-Error") == null;
		assert response.getHeaderField("X-PlantUML-Diagram-Error-Line") == null;
		assert response.getContentType().equals("image/svg+xml");
		assert readStreamAsString(response.getInputStream()).startsWith("<?xml ");

		response = httpGet("/plantuml/svg/" + getDefaultTranscoder().encode("A -> B"));
		assert response.getResponseCode() == 200;
		assert response.getHeaderField("X-PlantUML-Diagram-Error") == null;
		assert response.getHeaderField("X-PlantUML-Diagram-Error-Line") == null;
		assert response.getContentType().equals("image/svg+xml");
		assert readStreamAsString(response.getInputStream()).startsWith("<?xml ");

		response = httpGet("/svg/" + getDefaultTranscoder().encode("foo"));
		assert response.getResponseCode() == 400;
		assert response.getHeaderField("X-PlantUML-Diagram-Error").equals("Syntax Error?");
		assert response.getHeaderField("X-PlantUML-Diagram-Error-Line").equals("2");
		assert response.getContentType().equals("image/svg+xml");
		assert readStreamAsString(response.getErrorStream()).startsWith("<?xml ");
	}

	private static void test_POST_render() throws Exception {
		HttpURLConnection response;

		// Defaults to png when no format is specified
		response = httpPostJson("/render", renderRequestJson("A -> B"));
		assert response.getResponseCode() == 200;
		assert response.getHeaderField("X-PlantUML-Diagram-Error") == null;
		assert response.getHeaderField("X-PlantUML-Diagram-Error-Line") == null;
		assert response.getContentType().equals("image/png");
		assert readStreamAsImage(response.getInputStream()) != null;

		response = httpPostJson("/render", renderRequestJson("A -> B", "-tsvg"));
		assert response.getResponseCode() == 200;
		assert response.getHeaderField("X-PlantUML-Diagram-Error") == null;
		assert response.getHeaderField("X-PlantUML-Diagram-Error-Line") == null;
		assert response.getContentType().equals("image/svg+xml");
		assert readStreamAsString(response.getInputStream()).startsWith("<?xml ");

		response = httpPostJson("/render", renderRequestJson("A -> B", "-ttxt"));
		assert response.getResponseCode() == 200;
		assert response.getHeaderField("X-PlantUML-Diagram-Error") == null;
		assert response.getHeaderField("X-PlantUML-Diagram-Error-Line") == null;
		assert response.getContentType().equals("text/plain");
		assert readStreamAsString(response.getInputStream()).equals("" +
				"     ,-.          ,-.\n" +
				"     |A|          |B|\n" +
				"     `+'          `+'\n" +
				"      |            | \n" +
				"      |----------->| \n" +
				"     ,+.          ,+.\n" +
				"     |A|          |B|\n" +
				"     `-'          `-'\n"
		);

		response = httpPostJson("/render", renderRequestJson("foo", "-ttxt"));
		assert response.getResponseCode() == 200;
		assert response.getHeaderField("X-PlantUML-Diagram-Error").equals("Syntax Error?");
		assert response.getHeaderField("X-PlantUML-Diagram-Error-Line").equals("2");
		assert response.getContentType().equals("text/plain");
		assert readStreamAsString(response.getInputStream()).equals("" +
				"[From string (line 2) ]\n" +
				"                       \n" +
				"@startuml              \n" +
				"foo                    \n" +
				"^^^^^                  \n" +
				" Syntax Error?         \n"
		);

		response = httpPostJson("/render", renderRequestJson("@startuml", "-ttxt"));
		assert response.getResponseCode() == 200;
		assert response.getHeaderField("X-PlantUML-Diagram-Error").equals("No @startuml/@enduml found");
		assert response.getHeaderField("X-PlantUML-Diagram-Error-Line").equals("0");
		assert response.getContentType().equals("text/plain");
		assert readStreamAsString(response.getInputStream()).equals("" +
				"                               \n" +
				"                               \n" +
				"     No @startuml/@enduml found\n"
		);

		response = httpPostJson("/render", "");
		assert response.getResponseCode() == 400;
		assert response.getHeaderField("X-PlantUML-Diagram-Error") == null;
		assert response.getHeaderField("X-PlantUML-Diagram-Error-Line") == null;
		assert response.getContentType().equals("text/plain");
		assert readStreamAsString(response.getErrorStream()).equals("No request body");

		response = httpPostJson("/render", "123abc");
		assert response.getResponseCode() == 400;
		assert response.getHeaderField("X-PlantUML-Diagram-Error") == null;
		assert response.getHeaderField("X-PlantUML-Diagram-Error-Line") == null;
		assert response.getContentType().equals("text/plain");
		assert readStreamAsString(response.getErrorStream()).contains("Error parsing request json: Unexpected character at 1:4\n");
	}

	private static void test_unknown_path() throws Exception {
		HttpURLConnection response = httpGet("/foo");
		assert response.getResponseCode() == 302;
		assert response.getHeaderField("Location").equals("/plantuml/png/oqbDJyrBuGh8ISmh2VNrKGZ8JCuFJqqAJYqgIotY0aefG5G00000");
	}

	//
	// Test DSL
	//

	private static HttpURLConnection httpGet(String path) throws Exception {
		return urlConnection(path);
	}

	private static HttpURLConnection httpPost(String path, String contentType, byte[] content) throws Exception {
		HttpURLConnection conn = urlConnection(path);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", contentType);
		conn.setRequestProperty("Content-Length", Integer.toString(content.length));
		conn.setDoOutput(true);
		conn.getOutputStream().write(content);
		return conn;
	}

	private static HttpURLConnection httpPostJson(String path, String json) throws Exception {
		return httpPost(path, "application/json; utf-8", json.getBytes(UTF_8));
	}

	private static String httpRaw(String request) throws Exception {
		try (Socket socket = socketConnection()) {
			socket.getOutputStream().write(request.getBytes(UTF_8));
			socket.shutdownOutput();
			return readStreamAsString(socket.getInputStream()).replaceAll("\r\n", "\n");
		}
	}

	private static BufferedImage readStreamAsImage(InputStream in) throws Exception {
		return ImageIO.read(new MemoryCacheImageInputStream(in));
	}

	private static String readStreamAsString(InputStream in) throws IOException {
		byte[] buffer = new byte[1024];
		int length;
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while ((length = in.read(buffer)) != -1) {
			baos.write(buffer, 0, length);
		}
		return baos.toString(UTF_8.name());
	}

	private static String renderRequestJson(String source, String... options) {
		final JsonObject object = Json.object();
		object.add("source", source);
		if (options.length != 0) {
			object.add("options", Json.array(options));
		}
		return object.toString();
	}

	//
	// System under test
	//

	private static int port;

	private static void startServer() throws Exception {
		final ServerSocket serverSocket = new ServerSocket(0);
		port = serverSocket.getLocalPort();

		Thread serverLoopThread = new Thread("PicoWebServerLoop") {
			@Override
			public void run() {
				try {
					PicoWebServer.serverLoop(serverSocket);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		serverLoopThread.setDaemon(true);
		serverLoopThread.start();
	}

	private static Socket socketConnection() throws IOException {
		return new Socket("localhost", port);
	}

	private static HttpURLConnection urlConnection(String path) throws Exception {
		final HttpURLConnection conn = (HttpURLConnection) new URL("http://localhost:" + port + path).openConnection();
		conn.setInstanceFollowRedirects(false);
		return conn;
	}
}
