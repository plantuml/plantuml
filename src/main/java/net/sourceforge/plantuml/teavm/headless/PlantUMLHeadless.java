package net.sourceforge.plantuml.teavm.headless;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.List;

import org.teavm.jso.JSExport;

import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.version.Version;
import net.sourceforge.plantuml.warning.Warning;

/**
 * Headless TeaVM entry point for the PlantUML MCP server (Node/stdio).
 *
 * <p>
 * Unlike {@link net.sourceforge.plantuml.teavm.browser.PlantUMLBrowser}, this
 * class exposes no DOM- or Graphviz-dependent code: just plain
 * text-returning functions usable from a plain Node process, with no browser
 * and no Java runtime.
 *
 * <p>
 * The exported functions return their result as a JSON string. Building the
 * JSON here (in Java) keeps all formatting logic on the JVM side (testable with
 * a normal JVM) and lets the Node wrapper simply {@code JSON.parse} the result.
 * It also avoids pulling any JSON library into the TeaVM graph.
 *
 * <p>
 * Tools so far:
 * <ul>
 * <li>{@link #version()} &mdash; the embedded PlantUML version (validates the
 * full chain: TeaVM build -&gt; JS artifact -&gt; Node wrapper -&gt; MCP stdio
 * transport -&gt; tool discovery by the client).</li>
 * <li>{@link #checkSyntax(String)} &mdash; parses a single diagram and reports a
 * structured diagnostic, without rendering. Mirrors the Java
 * {@code net.sourceforge.plantuml.mcp.SyntaxChecker}/{@code McpResult}
 * behaviour, but is self-contained and DOM-free.</li>
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
	 *   "errorMessage": "..."
	 * }
	 * </pre>
	 *
	 * @param source the raw PlantUML source (a single {@code @start.../@end...}
	 *               diagram)
	 * @return a JSON string describing the diagnostic; never {@code null}
	 */
	@JSExport
	public static String checkSyntax(String source) {
		if (source == null || source.startsWith("@start") == false)
			return errorJson(-1,
					"The input must start with a @start... directive (for example @startuml)", 0);

		final SourceStringReader ss = new SourceStringReader(source, UTF_8);
		final List<BlockUml> blocks = ss.getBlocks();
		if (blocks.size() != 1)
			return errorJson(-1, "Expected exactly one diagram in the source", 0);

		final Diagram diagram = blocks.get(0).getDiagram();
		final int lineCount = diagram.getSource().getTotalLineCount();

		if (diagram instanceof PSystemError) {
			final PSystemError error = (PSystemError) diagram;
			final ErrorUml firstError = error.getFirstError();
			int errorLine = -1;
			String errorMessage = null;
			if (firstError != null) {
				// getPosition() is 0-based; the public contract exposes a 1-based line.
				errorLine = firstError.getPosition() + 1;
				errorMessage = firstError.getError();
			}
			return errorJson(errorLine, errorMessage, lineCount);
		}

		final String diagramType = diagram.getClass().getSimpleName();
		return okJson(diagramType, lineCount, warningsOf(diagram));
	}

	private static List<String> warningsOf(Diagram diagram) {
		final List<String> result = new java.util.ArrayList<>();
		if (diagram instanceof TitledDiagram) {
			final TitledDiagram titledDiagram = (TitledDiagram) diagram;
			for (Warning w : titledDiagram.getWarnings())
				result.add(w.asSingleLine());
		}
		return result;
	}

	private static String okJson(String diagramType, int lineCount, List<String> warnings) {
		final StringBuilder sb = new StringBuilder();
		sb.append('{');
		sb.append("\"valid\":true");
		sb.append(",\"diagramType\":").append(jsonString(diagramType));
		sb.append(",\"lineCount\":").append(lineCount);
		sb.append(",\"warnings\":").append(jsonStringArray(warnings));
		sb.append('}');
		return sb.toString();
	}

	private static String errorJson(int errorLine, String errorMessage, int lineCount) {
		final StringBuilder sb = new StringBuilder();
		sb.append('{');
		sb.append("\"valid\":false");
		sb.append(",\"lineCount\":").append(lineCount);
		sb.append(",\"warnings\":[]");
		if (errorLine > 0)
			sb.append(",\"errorLine\":").append(errorLine);
		sb.append(",\"errorMessage\":").append(jsonString(errorMessage));
		sb.append('}');
		return sb.toString();
	}

	private static String jsonStringArray(List<String> values) {
		final StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (int i = 0; i < values.size(); i++) {
			if (i > 0)
				sb.append(',');
			sb.append(jsonString(values.get(i)));
		}
		sb.append(']');
		return sb.toString();
	}

	/**
	 * Renders a Java string as a JSON string literal (quotes included), or the
	 * literal {@code null} when the value is {@code null}. Escapes the characters
	 * required by the JSON spec.
	 */
	private static String jsonString(String value) {
		if (value == null)
			return "null";
		final StringBuilder sb = new StringBuilder();
		sb.append('"');
		for (int i = 0; i < value.length(); i++) {
			final char c = value.charAt(i);
			switch (c) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			default:
				if (c < 0x20)
					sb.append(String.format("\\u%04x", (int) c));
				else
					sb.append(c);
			}
		}
		sb.append('"');
		return sb.toString();
	}

}
