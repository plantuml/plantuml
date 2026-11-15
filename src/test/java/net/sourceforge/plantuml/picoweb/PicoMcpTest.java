package net.sourceforge.plantuml.picoweb;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.json.Json;
import net.sourceforge.plantuml.json.JsonObject;
import net.sourceforge.plantuml.json.JsonValue;

/** Unit tests of {@link PicoMcp}: no socket, the requests are handed over directly. */
class PicoMcpTest {

	private static final String OK = "@startuml\nAlice -> Bob\n@enduml";
	private static final String KO = "@startuml\nclass A {\n@enduml";

	private static PicoMcp.Answer get(String path) throws IOException {
		return PicoMcp.handle("GET", path, new byte[0], null);
	}

	private static PicoMcp.Answer post(String path, String body) throws IOException {
		return PicoMcp.handle("POST", path, body.getBytes(UTF_8), null);
	}

	private static JsonObject object(PicoMcp.Answer answer) {
		assertEquals(200, answer.status);
		assertEquals("application/json", answer.contentType);
		return Json.parse(answer.text).asObject();
	}

	private static String rpcBody(Object id, String method, String params) {
		return "{\"jsonrpc\":\"2.0\"" + (id == null ? "" : ",\"id\":" + id) + ",\"method\":\"" + method + "\""
				+ (params == null ? "" : ",\"params\":" + params) + "}";
	}

	private static String callParams(String tool, String source) {
		return Json.object().add("name", tool).add("arguments", Json.object().add("source", source)).toString();
	}

	private static JsonObject rpc(String body) throws IOException {
		return object(post("/mcp", body));
	}

	// ---- REST endpoints ----

	@Test
	void unknownRequestsAreNotOurs() throws IOException {
		assertNull(get("/nothing"));
		assertNull(post("/nothing", OK));
		assertNull(get("/check_syntax"));
		assertNull(post("/plantuml_version", ""));
	}

	@Test
	void version() throws IOException {
		final PicoMcp.Answer answer = get("/plantuml_version");
		assertEquals(200, answer.status);
		assertEquals("text/plain", answer.contentType);
		assertTrue(answer.text.startsWith("PlantUML version"));
	}

	@Test
	void plantumlPrefixIsAccepted() throws IOException {
		assertNotNull(get("/plantuml/plantuml_version"));
		assertTrue(object(post("/plantuml/check_syntax", OK)).get("valid").asBoolean());
	}

	@Test
	void checkSyntaxOk() throws IOException {
		final JsonObject json = object(post("/check_syntax", OK));
		assertTrue(json.get("valid").asBoolean());
		assertEquals("SequenceDiagram", json.get("diagramType").asString());
		assertEquals(3, json.get("lineCount").asInt());
		assertEquals(0, json.get("warnings").asArray().size());
		assertNull(json.get("svg"));
		assertNull(json.get("errorMessage"));
	}

	@Test
	void checkSyntaxError() throws IOException {
		final JsonObject json = object(post("/check_syntax", KO));
		assertFalse(json.get("valid").asBoolean());
		assertNull(json.get("diagramType"));
		assertEquals(2, json.get("errorLineNumber").asInt());
		assertEquals("class A {", json.get("errorLine").asString());
		assertTrue(json.get("errorMessage").asString().length() > 0);
	}

	@Test
	void sourceWithoutStartIsReported() throws IOException {
		final JsonObject json = object(post("/check_syntax", "Alice -> Bob"));
		assertFalse(json.get("valid").asBoolean());
		assertTrue(json.get("errorMessage").asString().contains("@start"));
	}

	@Test
	void severalDiagramsAreReported() throws IOException {
		final JsonObject json = object(post("/check_syntax", OK + "\n" + OK));
		assertFalse(json.get("valid").asBoolean());
		assertTrue(json.get("errorMessage").asString().contains("exactly one"));
	}

	@Test
	void jsonAndRawBodiesAreEquivalent() throws IOException {
		final String viaJson = post("/check_syntax", Json.object().add("source", OK).toString()).text;
		assertEquals(post("/check_syntax", OK).text, viaJson);
	}

	@Test
	void badJsonBodyIsABadRequest() {
		assertThrows(BadRequest400.class, () -> post("/check_syntax", "{\"nope\": 1}"));
		assertThrows(BadRequest400.class, () -> post("/check_syntax", "{\"source\": 1}"));
		assertThrows(BadRequest400.class, () -> post("/check_syntax", "{broken"));
	}

	@Test
	void renderDiagram() throws IOException {
		final JsonObject json = object(post("/render_diagram", OK));
		assertTrue(json.get("valid").asBoolean());
		assertEquals("SequenceDiagram", json.get("diagramType").asString());
		final String svg = json.get("svg").asString();
		assertTrue(svg.startsWith("<svg"));
		assertTrue(svg.contains("Alice"));
	}

	@Test
	void renderDiagramError() throws IOException {
		final JsonObject json = object(post("/render_diagram", KO));
		assertFalse(json.get("valid").asBoolean());
		assertNull(json.get("svg"));
		assertEquals(2, json.get("errorLineNumber").asInt());
	}

	@Test
	void explainDiagram() throws IOException {
		final PicoMcp.Answer answer = post("/explain_diagram", OK);
		assertEquals(200, answer.status);
		final JsonObject first = Json.parse(answer.text).asArray().get(0).asObject();
		assertEquals("Alice -> Bob", first.get("input").asArray().get(0).asString());
		assertEquals("Message from 'Alice' to 'Bob'", first.get("explain").asString());
		assertEquals(2, first.get("lineNumber").asInt());
	}

	@Test
	void explainWithoutStartIsAnExplanationNotAnError() throws IOException {
		final JsonObject first = Json.parse(post("/explain_diagram", "oops").text).asArray().get(0).asObject();
		assertEquals(0, first.get("input").asArray().size());
		assertNull(first.get("lineNumber"));
		assertTrue(first.get("explain").asString().contains("@start"));
	}

	// ---- JSON-RPC ----

	@Test
	void initialize() throws IOException {
		final JsonObject json = rpc(rpcBody(1, "initialize", "{\"protocolVersion\":\"2025-06-18\"}"));
		assertEquals("2.0", json.get("jsonrpc").asString());
		assertEquals(1, json.get("id").asInt());
		final JsonObject result = json.get("result").asObject();
		assertEquals("2025-06-18", result.get("protocolVersion").asString());
		assertNotNull(result.get("capabilities").asObject().get("tools"));
		assertNotNull(result.get("serverInfo").asObject().get("name"));
	}

	@Test
	void initializeWithoutVersionHasADefault() throws IOException {
		final JsonObject result = rpc(rpcBody(1, "initialize", null)).get("result").asObject();
		assertTrue(result.get("protocolVersion").isString());
	}

	@Test
	void idIsEchoedWhateverItsType() throws IOException {
		assertEquals("abc", rpc(rpcBody("\"abc\"", "ping", null)).get("id").asString());
		assertEquals(0, rpc(rpcBody(0, "ping", null)).get("id").asInt());
		assertEquals(0, rpc(rpcBody(1, "ping", null)).get("result").asObject().size());
	}

	@Test
	void notificationsGetNoAnswer() throws IOException {
		final PicoMcp.Answer answer = post("/mcp", rpcBody(null, "notifications/initialized", null));
		assertEquals(202, answer.status);
		assertEquals("", answer.text);
	}

	@Test
	void toolsList() throws IOException {
		final JsonValue tools = rpc(rpcBody(1, "tools/list", null)).get("result").asObject().get("tools");
		assertEquals(4, tools.asArray().size());
		final String[] names = { "plantuml_version", "check_syntax", "render_diagram", "explain_diagram" };
		for (int i = 0; i < names.length; i++) {
			final JsonObject tool = tools.asArray().get(i).asObject();
			assertEquals(names[i], tool.get("name").asString());
			assertTrue(tool.get("description").asString().length() > 0);
			final JsonObject schema = tool.get("inputSchema").asObject();
			assertEquals("object", schema.get("type").asString());
			if (i == 0)
				assertNull(schema.get("required"));
			else
				assertEquals("source", schema.get("required").asArray().get(0).asString());
		}
	}

	@Test
	void callVersion() throws IOException {
		final JsonObject result = rpc(rpcBody(1, "tools/call", "{\"name\":\"plantuml_version\"}")).get("result")
				.asObject();
		assertFalse(result.get("isError").asBoolean());
		final JsonObject content = result.get("content").asArray().get(0).asObject();
		assertEquals("text", content.get("type").asString());
		assertTrue(content.get("text").asString().startsWith("PlantUML version"));
	}

	private static JsonObject callResult(String tool, String source) throws IOException {
		return rpc(rpcBody(7, "tools/call", callParams(tool, source))).get("result").asObject();
	}

	private static JsonValue callPayload(JsonObject result) {
		return Json.parse(result.get("content").asArray().get(0).asObject().get("text").asString());
	}

	@Test
	void callCheckSyntax() throws IOException {
		final JsonObject ok = callResult("check_syntax", OK);
		assertFalse(ok.get("isError").asBoolean());
		assertTrue(callPayload(ok).asObject().get("valid").asBoolean());

		final JsonObject ko = callResult("check_syntax", KO);
		assertTrue(ko.get("isError").asBoolean());
		assertEquals(2, callPayload(ko).asObject().get("errorLineNumber").asInt());
	}

	@Test
	void callRenderDiagram() throws IOException {
		final JsonObject ok = callResult("render_diagram", OK);
		assertFalse(ok.get("isError").asBoolean());
		assertTrue(callPayload(ok).asObject().get("svg").asString().startsWith("<svg"));
		assertTrue(callResult("render_diagram", KO).get("isError").asBoolean());
	}

	@Test
	void callExplainDiagramIsNeverAnError() throws IOException {
		final JsonObject result = callResult("explain_diagram", OK);
		assertFalse(result.get("isError").asBoolean());
		assertEquals(2, callPayload(result).asArray().get(0).asObject().get("lineNumber").asInt());
		final JsonObject ko = callResult("explain_diagram", KO);
		assertFalse(ko.get("isError").asBoolean());
		assertEquals(0, callPayload(ko).asArray().size());
	}

	@Test
	void toolResultMatchesTheRestEndpoint() throws IOException {
		assertEquals(post("/check_syntax", KO).text,
				callResult("check_syntax", KO).get("content").asArray().get(0).asObject().get("text").asString());
	}

	private static int errorCode(String body) throws IOException {
		final JsonObject json = rpc(body);
		assertNull(json.get("result"));
		return json.get("error").asObject().get("code").asInt();
	}

	@Test
	void protocolErrors() throws IOException {
		assertEquals(-32601, errorCode(rpcBody(1, "no/such/method", null)));
		assertEquals(-32602, errorCode(rpcBody(1, "tools/call", "{\"name\":\"nope\"}")));
		assertEquals(-32602, errorCode(rpcBody(1, "tools/call", "{\"name\":\"check_syntax\"}")));
		assertEquals(-32602,
				errorCode(rpcBody(1, "tools/call", "{\"name\":\"check_syntax\",\"arguments\":{\"source\":3}}")));
		assertEquals(-32600, errorCode("[1,2]"));
		assertEquals(-32700, errorCode("garbage"));
	}

	@Test
	void parseErrorHasANullId() throws IOException {
		final JsonObject json = rpc("garbage");
		assertTrue(json.get("id").isNull());
		assertEquals("2.0", json.get("jsonrpc").asString());
	}

	@Test
	void mcpOnlyAcceptsPost() throws IOException {
		assertEquals(405, get("/mcp").status);
		assertEquals(405, PicoMcp.handle("DELETE", "/mcp", new byte[0], null).status);
	}

	@Test
	void onlyLocalOriginsAreAccepted() throws IOException {
		final byte[] body = rpcBody(1, "ping", null).getBytes(UTF_8);
		for (String origin : new String[] { null, "http://localhost:3000", "http://127.0.0.1", "http://[::1]:80" })
			assertEquals(200, PicoMcp.handle("POST", "/mcp", body, origin).status);
		for (String origin : new String[] { "http://evil.com", "https://localhost.evil.com", "http://127.0.0.1.evil.com" })
			assertThrows(BadRequest400.class, () -> PicoMcp.handle("POST", "/mcp", body, origin));
	}
}
