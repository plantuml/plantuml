/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 *
 * If you like this project or if you find it useful, you can support us at:
 *
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 *
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 *
 *
 */
package net.sourceforge.plantuml.picoweb;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.util.List;

import net.sourceforge.plantuml.explain.DiagramExplainer;
import net.sourceforge.plantuml.explain.Explanation;
import net.sourceforge.plantuml.json.Json;
import net.sourceforge.plantuml.json.JsonArray;
import net.sourceforge.plantuml.json.JsonObject;
import net.sourceforge.plantuml.json.JsonValue;
import net.sourceforge.plantuml.mcp.DiagramRenderer;
import net.sourceforge.plantuml.mcp.McpResult;
import net.sourceforge.plantuml.mcp.SyntaxChecker;
import net.sourceforge.plantuml.utils.LineLocation;
import net.sourceforge.plantuml.version.Version;

/**
 * The tools of plantuml-mcp-js/server.js as plain HTTP endpoints of the
 * PicoWebServer, with the same JSON answers:
 * <ul>
 * <li>GET /plantuml_version (text)</li>
 * <li>POST /check_syntax</li>
 * <li>POST /render_diagram</li>
 * <li>POST /explain_diagram</li>
 * <li>POST /mcp: the same tools through JSON-RPC 2.0 (MCP, Streamable HTTP without
 * session nor SSE), written by hand: initialize, ping, tools/list, tools/call</li>
 * </ul>
 * The body of a POST is either the raw diagram source or {"source": "..."}.
 */
final class PicoMcp {

	private PicoMcp() {
	}

	/** @return the answer, or null if this request is not one of ours. */
	static Answer handle(String method, String path, byte[] body, String origin) throws IOException {
		final String tool = path.startsWith("/plantuml/") ? path.substring("/plantuml".length()) : path;
		if (tool.equals("/mcp")) {
			checkOrigin(origin);
			// No server-initiated stream (SSE) here
			if (method.equals("POST") == false)
				return new Answer(405, "text/plain", "Method Not Allowed");
			return rpc(body);
		}
		if (method.equals("GET") && tool.equals("/plantuml_version"))
			return new Answer(200, "text/plain", Version.fullDescription());
		if (method.equals("POST") == false)
			return null;
		if (tool.equals("/check_syntax"))
			return json(check(source(body)));
		if (tool.equals("/render_diagram"))
			return json(render(source(body)));
		if (tool.equals("/explain_diagram"))
			return json(explain(source(body)));
		return null;
	}

	private static Answer json(JsonValue value) {
		return new Answer(200, "application/json", value.toString());
	}

	/**
	 * A web page of the user's browser can call a local server (DNS rebinding):
	 * the MCP specification asks to refuse any Origin that is not local.
	 */
	private static void checkOrigin(String origin) throws BadRequest400 {
		if (origin == null)
			return;
		final String host = origin.replaceFirst("^[a-zA-Z]+://", "").replaceFirst(":\\d+$", "");
		if (host.equals("localhost") || host.equals("127.0.0.1") || host.equals("[::1]"))
			return;
		throw new BadRequest400("Origin not allowed: " + PicoWebServer.oneLine(origin));
	}

	// ---- JSON-RPC 2.0 / MCP (what the SDK does for server.js) ----

	private static final String[] TOOLS = { "plantuml_version", "check_syntax", "render_diagram", "explain_diagram" };

	private static final String[] DESCRIPTIONS = { //
			"Returns the version of the embedded PlantUML library.", //
			"Checks the syntax of a single PlantUML diagram without rendering it. Returns a JSON object "
					+ "containing: 'valid' (boolean), 'diagramType' (if valid), 'lineCount', 'warnings' (list of "
					+ "non-fatal warnings), and on failure 'errorLineNumber' (1-based line number), "
					+ "'errorMessage', 'errorLine' (the offending source line, when available), and 'errorContext'.", //
			"Renders a single PlantUML diagram to a deterministic SVG. Returns a JSON object containing: "
					+ "'valid' (boolean), and when valid 'diagramType', 'lineCount', 'warnings' (list of "
					+ "non-fatal warnings) and 'svg' (the rendered SVG as a string). On failure it has the same "
					+ "error shape as check_syntax.", //
			"Explains how a single PlantUML diagram is parsed, line by line. Returns a JSON array of "
					+ "objects, each containing: 'input' (the source line(s) that produced the explanation), "
					+ "'explain' (a human-readable explanation), and 'lineNumber' (1-based line number, when "
					+ "available)." };

	private static Answer rpc(byte[] body) throws IOException {
		final JsonObject request;
		try {
			final JsonValue parsed = Json.parse(new String(body, UTF_8));
			if (parsed.isObject() == false)
				return rpcError(null, -32600, "Invalid Request");
			request = parsed.asObject();
		} catch (RuntimeException e) {
			return rpcError(null, -32700, "Parse error");
		}
		final JsonValue id = request.get("id");
		// A notification (initialized, cancelled...) has no id and gets no answer
		if (id == null)
			return new Answer(202, "text/plain", "");
		final JsonValue m = request.get("method");
		final JsonValue p = request.get("params");
		final String method = m != null && m.isString() ? m.asString() : "";
		final JsonObject params = p != null && p.isObject() ? p.asObject() : Json.object();

		final JsonValue result;
		if (method.equals("initialize")) {
			final JsonValue version = params.get("protocolVersion");
			result = Json.object() //
					.add("protocolVersion", version != null && version.isString() ? version.asString() : "2025-03-26") //
					.add("capabilities", Json.object().add("tools", Json.object())) //
					.add("serverInfo", Json.object().add("name", "plantuml-picoweb").add("version",
							Version.versionString()));
		} else if (method.equals("ping")) {
			result = Json.object();
		} else if (method.equals("tools/list")) {
			result = Json.object().add("tools", toolList());
		} else if (method.equals("tools/call")) {
			return callTool(id, params);
		} else {
			return rpcError(id, -32601, "Method not found: " + method);
		}
		return rpcResult(id, result);
	}

	private static JsonArray toolList() {
		final JsonArray tools = Json.array();
		for (int i = 0; i < TOOLS.length; i++) {
			final JsonObject properties = Json.object();
			final JsonArray required = Json.array();
			if (i > 0) {
				properties.add("source", Json.object().add("type", "string").add("description",
						"The PlantUML source, including @start.../@end... (a single diagram)"));
				required.add("source");
			}
			final JsonObject schema = Json.object().add("type", "object").add("properties", properties);
			if (i > 0)
				schema.add("required", required);
			tools.add(Json.object().add("name", TOOLS[i]).add("description", DESCRIPTIONS[i]).add("inputSchema",
					schema));
		}
		return tools;
	}

	private static Answer callTool(JsonValue id, JsonObject params) {
		final JsonValue n = params.get("name");
		final String name = n != null && n.isString() ? n.asString() : "";
		int index = -1;
		for (int i = 0; i < TOOLS.length; i++)
			if (TOOLS[i].equals(name))
				index = i;
		if (index == -1)
			return rpcError(id, -32602, "Unknown tool: " + name);

		final String text;
		boolean isError = false;
		try {
			if (index == 0) {
				text = Version.fullDescription();
			} else {
				final JsonValue a = params.get("arguments");
				final JsonValue s = a != null && a.isObject() ? a.asObject().get("source") : null;
				if (s == null || s.isString() == false)
					return rpcError(id, -32602, "Invalid arguments: 'source' must be a string");
				final String source = s.asString();
				final JsonValue json = index == 1 ? check(source) : index == 2 ? render(source) : explain(source);
				text = json.toString();
				final JsonValue valid = json.isObject() ? json.asObject().get("valid") : null;
				isError = valid != null && valid.isFalse();
			}
		} catch (Exception e) {
			return rpcResult(id, toolResult("Tool failed: " + e, true));
		}
		return rpcResult(id, toolResult(text, isError));
	}

	private static JsonObject toolResult(String text, boolean isError) {
		final JsonArray content = Json.array().add(Json.object().add("type", "text").add("text", text));
		return Json.object().add("content", content).add("isError", isError);
	}

	private static Answer rpcResult(JsonValue id, JsonValue result) {
		return json(Json.object().add("jsonrpc", "2.0").add("id", id).add("result", result));
	}

	private static Answer rpcError(JsonValue id, int code, String message) {
		final JsonObject error = Json.object().add("code", code).add("message", message);
		return json(Json.object().add("jsonrpc", "2.0").add("id", id == null ? Json.NULL : id).add("error", error));
	}

	private static String source(byte[] body) throws BadRequest400 {
		final String text = new String(body, UTF_8);
		if (text.trim().startsWith("{") == false)
			return text;
		try {
			final JsonValue source = Json.parse(text).asObject().get("source");
			if (source != null && source.isString())
				return source.asString();
		} catch (RuntimeException e) {
			// Same message as below
		}
		throw new BadRequest400("Expected raw source or a JSON object with a 'source' string");
	}

	private static JsonObject check(String source) throws IOException {
		return toJson(new SyntaxChecker().check(source), false);
	}

	private static JsonObject render(String source) throws IOException {
		return toJson(new DiagramRenderer().render(source, "svg"), true);
	}

	private static JsonObject toJson(McpResult result, boolean withSvg) {
		final JsonObject json = Json.object().add("valid", result.isOk());
		if (result.isOk())
			json.add("diagramType", result.getDiagramType());
		json.add("lineCount", result.getLineCount());
		final JsonArray warnings = Json.array();
		for (String w : result.getWarnings())
			warnings.add(w);
		json.add("warnings", warnings);
		if (result.isOk()) {
			if (withSvg)
				json.add("svg", result.getSvg());
		} else {
			if (result.getErrorLineNumber() > 0)
				json.add("errorLineNumber", result.getErrorLineNumber());
			addIfNotEmpty(json, "errorMessage", result.getErrorMessage());
			addIfNotEmpty(json, "errorLine", result.getErrorLine());
			addIfNotEmpty(json, "errorContext", result.getErrorContext());
		}
		return json;
	}

	private static void addIfNotEmpty(JsonObject json, String key, String value) {
		if (value != null && value.length() > 0)
			json.add(key, value);
	}

	private static JsonArray explain(String source) throws IOException {
		final JsonArray result = Json.array();
		final List<Explanation> explanations = new DiagramExplainer().explain(source);
		// null for a diagram that is a syntax error: nothing to explain
		if (explanations == null)
			return result;
		for (Explanation explanation : explanations) {
			final JsonArray input = Json.array();
			for (String s : explanation.getInput())
				input.add(s);
			final JsonObject item = Json.object().add("input", input).add("explain", explanation.getExplain());
			final LineLocation location = explanation.getLocation();
			if (location != null)
				item.add("lineNumber", location.getPosition() + 1);
			result.add(item);
		}
		return result;
	}

	static final class Answer {
		final int status;
		final String contentType;
		final String text;

		Answer(int status, String contentType, String text) {
			this.status = status;
			this.contentType = contentType;
			this.text = text;
		}
	}
}
