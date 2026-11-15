package net.sourceforge.plantuml.picoweb;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import net.sourceforge.plantuml.json.Json;
import net.sourceforge.plantuml.json.JsonArray;
import net.sourceforge.plantuml.json.JsonObject;
import net.sourceforge.plantuml.json.JsonValue;

/**
 * A minimal MCP client over Streamable HTTP, only for the tests: it does what
 * an MCP client does (initialize, notifications/initialized, tools/list,
 * tools/call), and nothing else.
 */
final class McpTestClient {

	/** The failure of a JSON-RPC call (an "error" member instead of a "result"). */
	static final class RpcException extends Exception {
		private static final long serialVersionUID = 1L;
		final int code;

		RpcException(int code, String message) {
			super(message);
			this.code = code;
		}
	}

	/** What a tool answered. */
	static final class ToolResult {
		final String text;
		final boolean isError;

		ToolResult(String text, boolean isError) {
			this.text = text;
			this.isError = isError;
		}

		JsonValue json() {
			return Json.parse(text);
		}
	}

	private final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
	private final URI endpoint;
	private int nextId = 1;

	McpTestClient(int port) {
		this.endpoint = URI.create("http://localhost:" + port + "/mcp");
	}

	HttpResponse<String> post(String body) throws IOException, InterruptedException {
		final HttpRequest request = HttpRequest.newBuilder(endpoint).timeout(Duration.ofSeconds(60))
				.header("Content-Type", "application/json").header("Accept", "application/json, text/event-stream")
				.POST(HttpRequest.BodyPublishers.ofString(body)).build();
		return http.send(request, HttpResponse.BodyHandlers.ofString());
	}

	/** One request, one answer: returns "result", or throws the "error". */
	JsonObject request(String method, JsonObject params) throws Exception {
		final int id = nextId++;
		final JsonObject message = Json.object().add("jsonrpc", "2.0").add("id", id).add("method", method);
		if (params != null)
			message.add("params", params);
		final HttpResponse<String> response = post(message.toString());
		if (response.statusCode() != 200)
			throw new IOException("HTTP " + response.statusCode() + ": " + response.body());
		final JsonObject answer = Json.parse(response.body()).asObject();
		if (answer.get("id").asInt() != id)
			throw new IOException("Answer to another request: " + response.body());
		final JsonValue error = answer.get("error");
		if (error != null)
			throw new RpcException(error.asObject().get("code").asInt(), error.asObject().get("message").asString());
		return answer.get("result").asObject();
	}

	void notification(String method) throws Exception {
		final HttpResponse<String> response = post(
				Json.object().add("jsonrpc", "2.0").add("method", method).toString());
		if (response.statusCode() != 202 || response.body().isEmpty() == false)
			throw new IOException("A notification must get an empty 202, got " + response.statusCode());
	}

	/** The handshake every MCP session starts with. */
	JsonObject initialize() throws Exception {
		final JsonObject result = request("initialize", Json.object().add("protocolVersion", "2025-06-18")
				.add("capabilities", Json.object()).add("clientInfo", Json.object().add("name", "test-client")
						.add("version", "0")));
		notification("notifications/initialized");
		return result;
	}

	JsonArray listTools() throws Exception {
		return request("tools/list", null).get("tools").asArray();
	}

	ToolResult callTool(String name, String source) throws Exception {
		final JsonObject params = Json.object().add("name", name);
		if (source != null)
			params.add("arguments", Json.object().add("source", source));
		final JsonObject result = request("tools/call", params);
		final String text = result.get("content").asArray().get(0).asObject().get("text").asString();
		return new ToolResult(text, result.get("isError").asBoolean());
	}
}
