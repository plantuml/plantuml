package net.sourceforge.plantuml.teavm.headless;

import java.io.IOException;
import java.util.List;

import org.teavm.jso.JSExport;
import org.teavm.jso.JSObject;

import net.sourceforge.plantuml.explain.DiagramExplainer;
import net.sourceforge.plantuml.explain.Explanation;
import net.sourceforge.plantuml.mcp.McpResult;
import net.sourceforge.plantuml.mcp.SyntaxChecker;
import net.sourceforge.plantuml.utils.LineLocation;
import net.sourceforge.plantuml.version.Version;

/**
 * Headless TeaVM entry point for the PlantUML MCP server (Node/stdio).
 *
 * <p>
 * Unlike {@link net.sourceforge.plantuml.teavm.browser.PlantUMLBrowser}, this
 * class exposes no DOM- or Graphviz-dependent code: just plain text-returning
 * functions usable from a plain Node process, with no browser and no Java
 * runtime.
 *
 * <p>
 * The actual work is delegated to the shared MCP classes ({@link SyntaxChecker}
 * / {@link McpResult}), so the logic is not duplicated between the Java server
 * and this JS build. This class only adapts their result to a JSON string,
 * built as a native JS object via {@link TeaVmJson} and serialized with
 * {@code JSON.stringify} (escaping handled by the platform). The Node wrapper
 * simply {@code JSON.parse}s the result.
 *
 * <p>
 * Tools so far:
 * <ul>
 * <li>{@link #version()} &mdash; the embedded PlantUML version (validates the
 * full chain: TeaVM build -&gt; JS artifact -&gt; Node wrapper -&gt; MCP stdio
 * transport -&gt; tool discovery by the client).</li>
 * <li>{@link #checkSyntax(String)} &mdash; parses a single diagram and reports
 * a structured diagnostic, without rendering.</li>
 * </ul>
 */
public class PlantUMLHeadless {
	// ::remove file when __MIT__ __EPL__ __BSD__ __ASL__ __LGPL__ __GPLV2__
	// ::remove file when JAVA8

	/**
	 * Returns the full description of the embedded PlantUML version.
	 *
	 * @return the version string, as produced by {@link Version#fullDescription()}
	 */
	@JSExport
	public static String version() {
		return Version.fullDescription();
	}

	/**
	 * Checks the syntax of a single PlantUML diagram, without rendering it.
	 *
	 * <p>
	 * The result is a JSON object with the following shape:
	 *
	 * <pre>
	 * {
	 *   "valid": true,
	 *   "diagramType": "SequenceDiagram",
	 *   "lineCount": 4,
	 *   "warnings": ["..."]
	 * }
	 * </pre>
	 *
	 * or, on failure:
	 *
	 * <pre>
	 * {
	 *   "valid": false,
	 *   "lineCount": 4,
	 *   "warnings": [],
	 *   "errorLine": 2,
	 *   "errorMessage": "...",
	 *   "errorContext": "the offending source line"
	 * }
	 * </pre>
	 *
	 * @param source the raw PlantUML source (a single {@code @start.../@end...}
	 *               diagram)
	 * @return a JSON string describing the diagnostic; never {@code null}
	 */
	@JSExport
	public static String checkSyntax(String source) {
		final McpResult result;
		try {
			result = new SyntaxChecker().check(source);
		} catch (IOException e) {
			return errorJson("Could not read the diagram source: " + e.getMessage());
		}

		if (result.isOk())
			// return okJson(result.getDiagramType(), result.getLineCount(),
			// result.getWarnings());
			return okJson(result);

		return errorJson(result);
//		return errorJson(result.getErrorLineNumber(), result.getErrorMessage(), result.getErrorLine(),
//				result.getLineCount());
	}

	/**
	 * Explains how a single PlantUML diagram is parsed, line by line.
	 *
	 * <p>
	 * The result is a JSON array of objects, each with the shape:
	 *
	 * <pre>
	 * {
	 *   "input": ["Alice -> Bob"],
	 *   "explain": "Message from 'Alice' to 'Bob'",
	 *   "line": 2
	 * }
	 * </pre>
	 *
	 * where {@code input} is the source line(s) that produced the explanation,
	 * {@code explain} is the human-readable text, and {@code line} is the 1-based
	 * line number (omitted when no location applies).
	 *
	 * @param source the raw PlantUML source (a single {@code @start.../@end...}
	 *               diagram)
	 * @return a JSON array string describing the explanations; never {@code null}
	 */
	@JSExport
	public static String explain(String source) {
		final List<Explanation> explanations;
		try {
			explanations = new DiagramExplainer().explain(source);
		} catch (IOException e) {
			final JSObject array = TeaVmJson.newArray();
			TeaVmJson.pushObject(array,
					explanationToJson(null, "Could not read the diagram source: " + e.getMessage(), -1));
			return TeaVmJson.stringify(array);
		}

		final JSObject array = TeaVmJson.newArray();
		for (int i = 0; i < explanations.size(); i++) {
			final Explanation explanation = explanations.get(i);
			final LineLocation location = explanation.getLocation();
			// getPosition() is 0-based; the public contract exposes a 1-based line.
			final int line = location == null ? -1 : location.getPosition() + 1;
			TeaVmJson.pushObject(array, explanationToJson(explanation.getInput(), explanation.getExplain(), line));
		}
		return TeaVmJson.stringify(array);
	}

	private static JSObject explanationToJson(List<String> input, String explain, int line) {
		final JSObject json = TeaVmJson.newObject();
		TeaVmJson.putObject(json, "input", input == null ? TeaVmJson.newArray() : toJsArray(input));
		TeaVmJson.putString(json, "explain", explain);
		if (line > 0)
			TeaVmJson.putInt(json, "lineNumber", line);
		return json;
	}

	private static String okJson(McpResult result) {
		final JSObject json = TeaVmJson.newObject();
		TeaVmJson.putBoolean(json, "valid", true);
		TeaVmJson.putString(json, "diagramType", result.getDiagramType());
		TeaVmJson.putInt(json, "lineCount", result.getLineCount());
		TeaVmJson.putObject(json, "warnings", toJsArray(result.getWarnings()));
		return TeaVmJson.stringify(json);
	}

	private static String errorJson(McpResult result) {
		final JSObject json = TeaVmJson.newObject();
		TeaVmJson.putBoolean(json, "valid", false);
		TeaVmJson.putInt(json, "lineCount", result.getLineCount());
		if (result.getErrorLineNumber() > 0)
			TeaVmJson.putInt(json, "errorLineNumber", result.getErrorLineNumber());
		TeaVmJson.addStringSafe(json, "errorMessage", result.getErrorMessage());
		TeaVmJson.addStringSafe(json, "errorLine", result.getErrorLine());
		TeaVmJson.addStringSafe(json, "errorContext", result.getErrorContext());
		return TeaVmJson.stringify(json);
	}

	private static String errorJson(String errorMessage) {
		final JSObject json = TeaVmJson.newObject();
		TeaVmJson.putBoolean(json, "valid", false);
		TeaVmJson.addStringSafe(json, "errorMessage", errorMessage);
		return TeaVmJson.stringify(json);
	}

	private static JSObject toJsArray(List<String> values) {
		final JSObject array = TeaVmJson.newArray();
		for (int i = 0; i < values.size(); i++)
			TeaVmJson.push(array, values.get(i));
		return array;
	}

}
