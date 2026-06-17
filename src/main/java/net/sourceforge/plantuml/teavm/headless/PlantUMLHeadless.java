package net.sourceforge.plantuml.teavm.headless;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import org.teavm.jso.JSExport;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

import net.sourceforge.plantuml.explain.DiagramExplainer;
import net.sourceforge.plantuml.explain.Explanation;
import net.sourceforge.plantuml.mcp.DiagramRendererTeaVM;
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
 * <li>{@link #renderSvg(String, ResultCallback)} &mdash; parses and renders a
 * single diagram to a deterministic SVG. Asynchronous: the result JSON is
 * delivered through a callback, because diagrams that need Graphviz call the
 * Viz.js engine, whose {@code @Async} bridge can only suspend from a TeaVM
 * worker thread (see {@link net.sourceforge.plantuml.teavm.browser.PlantUMLBrowser}
 * for the same constraint).</li>
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
	 * Callback used to deliver the render result JSON back to JavaScript.
	 */
	@JSFunctor
	public interface ResultCallback extends JSObject {
		void call(String json);
	}

	// =========================================================================
	// Render worker thread
	//
	// Rendering must run on a TeaVM thread so that the Viz.js @Async bridge
	// (GraphVizjsTeaVMEngine) can suspend. An @JSExport called directly from
	// JavaScript runs in a native JS context, where suspension is illegal. So
	// renderSvg() only queues a request and wakes the worker; the worker does the
	// actual rendering in a coroutine context and invokes the callback.
	// =========================================================================

	private static final Object LOCK = new Object();

	private static volatile boolean workerStarted = false;

	private static final class RenderRequest {
		final String source;
		final ResultCallback callback;

		RenderRequest(String source, ResultCallback callback) {
			this.source = source;
			this.callback = callback;
		}
	}

	// A real queue (not a single slot like PlantUMLBrowser): an MCP server may
	// receive several independent render_diagram calls, and none must be dropped.
	private static final Deque<RenderRequest> QUEUE = new ArrayDeque<>();

	private static void ensureWorkerStarted() {
		if (workerStarted == false) {
			synchronized (LOCK) {
				if (workerStarted == false) {
					new Thread(PlantUMLHeadless::workerLoop, "plantuml-headless-render").start();
					workerStarted = true;
				}
			}
		}
	}

	private static void workerLoop() {
		while (true) {
			final RenderRequest request;
			synchronized (LOCK) {
				while (QUEUE.isEmpty()) {
					try {
						LOCK.wait();
					} catch (InterruptedException e) {
						// Not expected; just retry.
					}
				}
				request = QUEUE.removeFirst();
			}

			// Render OUTSIDE the lock so new requests can be queued meanwhile.
			String json;
			try {
				json = renderSvgToJson(request.source);
			} catch (Throwable e) {
				json = errorJson("Could not render the diagram source: " + e);
			}
			request.callback.call(json);
		}
	}

	/**
	 * Parses and renders a single PlantUML diagram to a deterministic SVG.
	 *
	 * <p>
	 * Asynchronous: the result is delivered as a JSON string through
	 * {@code callback}. On success the JSON has the shape:
	 *
	 * <pre>
	 * {
	 *   "valid": true,
	 *   "diagramType": "SequenceDiagram",
	 *   "lineCount": 4,
	 *   "warnings": ["..."],
	 *   "svg": "&lt;svg ...&gt;...&lt;/svg&gt;"
	 * }
	 * </pre>
	 *
	 * On failure, it has the same error shape as {@link #checkSyntax(String)} (no
	 * {@code svg} field).
	 *
	 * @param source   the raw PlantUML source (a single {@code @start.../@end...}
	 *                 diagram)
	 * @param callback invoked once with the result JSON
	 */
	@JSExport
	public static void renderSvg(String source, ResultCallback callback) {
		ensureWorkerStarted();
		synchronized (LOCK) {
			QUEUE.addLast(new RenderRequest(source, callback));
			LOCK.notify();
		}
	}

	/**
	 * Synchronous core of {@link #renderSvg(String, ResultCallback)}, run on the
	 * worker thread. Returns the result JSON string.
	 */
	private static String renderSvgToJson(String source) {
		final McpResult result;
		try {
			result = new DiagramRendererTeaVM().render(source);
		} catch (IOException e) {
			return errorJson("Could not render the diagram source: " + e.getMessage());
		}

		if (result.isOk())
			return renderOkJson(result);

		return errorJson(result);
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

	private static String renderOkJson(McpResult result) {
		final JSObject json = TeaVmJson.newObject();
		TeaVmJson.putBoolean(json, "valid", true);
		TeaVmJson.putString(json, "diagramType", result.getDiagramType());
		TeaVmJson.putInt(json, "lineCount", result.getLineCount());
		TeaVmJson.putObject(json, "warnings", toJsArray(result.getWarnings()));
		TeaVmJson.addStringSafe(json, "svg", result.getSvg());
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
