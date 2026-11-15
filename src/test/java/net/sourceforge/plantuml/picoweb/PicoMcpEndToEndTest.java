package net.sourceforge.plantuml.picoweb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.json.JsonArray;
import net.sourceforge.plantuml.json.JsonObject;

/**
 * A real server on a real socket, driven by a real (if tiny) MCP client: the
 * session an LLM tool would hold with the PicoWebServer.
 */
class PicoMcpEndToEndTest {

	private static final String OK = "@startuml\nAlice -> Bob : hello\n@enduml";
	private static final String KO = "@startuml\nclass A {\n@enduml";

	private ServerSocket serverSocket;
	private ExecutorService executor;
	private int port;
	private McpTestClient client;

	@BeforeEach
	void start() throws Exception {
		serverSocket = new ServerSocket(0, 0, InetAddress.getByName("localhost"));
		port = serverSocket.getLocalPort();
		executor = Executors.newSingleThreadExecutor();
		executor.submit(() -> {
			PicoWebServer.serverLoop(serverSocket);
			return null;
		});
		client = new McpTestClient(port);
	}

	@AfterEach
	void stop() throws Exception {
		serverSocket.close();
		executor.shutdownNow();
	}

	@Test
	void fullSession() throws Exception {
		final JsonObject init = client.initialize();
		assertEquals("2025-06-18", init.get("protocolVersion").asString());

		final JsonArray tools = client.listTools();
		assertEquals(4, tools.size());

		final McpTestClient.ToolResult version = client.callTool("plantuml_version", null);
		assertFalse(version.isError);
		assertTrue(version.text.startsWith("PlantUML version"));

		final McpTestClient.ToolResult check = client.callTool("check_syntax", OK);
		assertFalse(check.isError);
		assertEquals("SequenceDiagram", check.json().asObject().get("diagramType").asString());

		final McpTestClient.ToolResult render = client.callTool("render_diagram", OK);
		assertFalse(render.isError);
		assertTrue(render.json().asObject().get("svg").asString().contains("hello"));

		final McpTestClient.ToolResult explain = client.callTool("explain_diagram", OK);
		assertFalse(explain.isError);
		assertEquals(2, explain.json().asArray().get(0).asObject().get("lineNumber").asInt());
	}

	@Test
	void anErroneousDiagramIsAToolErrorNotAProtocolError() throws Exception {
		client.initialize();
		final McpTestClient.ToolResult result = client.callTool("check_syntax", KO);
		assertTrue(result.isError);
		assertEquals(2, result.json().asObject().get("errorLineNumber").asInt());
	}

	@Test
	void protocolErrorsAreThrown() throws Exception {
		client.initialize();
		assertEquals(-32602,
				assertThrows(McpTestClient.RpcException.class, () -> client.callTool("nope", OK)).code);
		assertEquals(-32602,
				assertThrows(McpTestClient.RpcException.class, () -> client.callTool("check_syntax", null)).code);
		assertEquals(-32601,
				assertThrows(McpTestClient.RpcException.class, () -> client.request("resources/list", null)).code);
	}

	@Test
	void severalClientsAtTheSameTime() throws Exception {
		final ExecutorService pool = Executors.newFixedThreadPool(4);
		try {
			final java.util.List<java.util.concurrent.Future<Boolean>> futures = new java.util.ArrayList<>();
			for (int i = 0; i < 8; i++)
				futures.add(pool.submit(() -> {
					final McpTestClient c = new McpTestClient(port);
					c.initialize();
					return c.callTool("render_diagram", OK).json().asObject().get("valid").asBoolean();
				}));
			for (java.util.concurrent.Future<Boolean> f : futures)
				assertTrue(f.get());
		} finally {
			pool.shutdownNow();
		}
	}

	@Test
	void anEvilOriginIsRefused() throws Exception {
		final HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/mcp"))
				.header("Origin", "http://evil.example")
				.POST(HttpRequest.BodyPublishers.ofString("{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"ping\"}"))
				.build();
		final HttpResponse<String> response = HttpClient.newHttpClient().send(request,
				HttpResponse.BodyHandlers.ofString());
		assertEquals(400, response.statusCode());
	}

	@Test
	void restEndpointsStillWorkNextToMcp() throws Exception {
		final HttpResponse<String> response = HttpClient.newHttpClient().send(
				HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/plantuml_version")).build(),
				HttpResponse.BodyHandlers.ofString());
		assertEquals(200, response.statusCode());
		assertTrue(response.body().startsWith("PlantUML version"));

		final HttpResponse<String> serverinfo = HttpClient.newHttpClient().send(
				HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/serverinfo")).build(),
				HttpResponse.BodyHandlers.ofString());
		assertEquals(200, serverinfo.statusCode());
	}
}
