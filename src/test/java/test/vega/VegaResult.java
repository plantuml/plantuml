package test.vega;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.json.JsonArray;
import net.sourceforge.plantuml.json.JsonObject;

public record VegaResult(Path path, VegaStatus status, long durationMs, Class<?> diagramClass, Throwable e, String tag,
		boolean allowFailure, String description) {

	// What in a stack frame depends on the compiler rather than on the code:
	// javac and the Eclipse compiler number the lines of a multi-line expression
	// differently, and name the synthetic method of a lambda differently
	// (lambda$testAllPumlFiles$10 vs lambda$11). Left in vega.json, the same test
	// run from Ant and from an IDE rewrites the file even though nothing changed.
	private static final Pattern LINE_NUMBER = Pattern.compile("\\.java:\\d+\\)");
	private static final Pattern LAMBDA_NAME = Pattern.compile("lambda\\$(?:[A-Za-z_][A-Za-z0-9_]*\\$)?\\d+");

	static String stableFrame(final String frame) {
		return LAMBDA_NAME.matcher(LINE_NUMBER.matcher(frame).replaceAll(".java)")).replaceAll("lambda\\$");
	}

	public JsonObject toJsonObject() {
		final JsonObject entry = new JsonObject() //
				.add("file", path.toString().replace('\\', '/')) //
				.add("folder", path.getParent().toString().replace('\\', '/')) //
				.add("status", status.name().toLowerCase());
//				.add("duration_ms", durationMs);

		if (description != null)
			entry.add("description", description);

		if (tag != null)
			entry.add("tag", tag);

		if (allowFailure)
			entry.add("allow-failure", "true");

		if (diagramClass != null)
			entry.add("diagram_class", diagramClass.getSimpleName());

		if (e != null && e.getMessage() != null)
			entry.add("message", e.getMessage());

		if (e != null) {
			final StringWriter sw = new StringWriter();
			try (PrintWriter pw = new PrintWriter(sw)) {
				e.printStackTrace(pw);
			}

			final List<String> tmp = new ArrayList<>();
			int pos = 0;

			for (String s : sw.toString().split("\\R")) {
				if (s.startsWith("\tat "))
					s = s.substring(4);
				tmp.add(s);
				if (s.contains("test.vega."))
					pos = tmp.size();
			}

			final JsonArray stacktrace = new JsonArray();
			for (String line : tmp.subList(0, pos))
				stacktrace.add(stableFrame(line));

			entry.add("stacktrace", stacktrace);
		}
		return entry;
	}

}
