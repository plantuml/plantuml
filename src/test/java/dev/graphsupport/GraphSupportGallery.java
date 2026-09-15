package dev.graphsupport;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceFileReader;
import net.sourceforge.plantuml.dot.GraphvizRuntimeEnvironment;
import net.sourceforge.plantuml.preproc.Defines;

/**
 * Renders the gallery with every layout engine and writes the results next to each other, so the
 * output of graph-support can be judged by eye against Graphviz and Smetana.
 *
 * Nothing is asserted: a layout is a matter of degree, not of pass or fail. Run it with
 *
 * <pre>
 * ./gradlew test --tests dev.graphsupport.GraphSupportGallery
 * </pre>
 *
 * and open {@code outputdev/graph-support-gallery/index.html}. Without a dot executable the
 * Graphviz column is left out rather than filled in by whichever engine takes over.
 */
public class GraphSupportGallery {

	private static final Path CASES = Paths.get("src", "test", "resources", "graphsupport", "gallery");
	private static final File OUTPUT = new File("outputdev/graph-support-gallery").getAbsoluteFile();

	private static final String[][] ENGINES = {
			{ "graphviz", "Graphviz" },
			{ "graph-support", "graph-support" },
			{ "smetana", "Smetana" } };

	@Test
	public void renderGallery() throws IOException {
		final List<Path> cases = cases();
		final boolean graphviz = dotIsAvailable();
		if (graphviz == false)
			System.out.println("No dot executable: rendering without the Graphviz column");

		for (String[] engine : ENGINES)
			if (graphviz || "graphviz".equals(engine[0]) == false)
				for (Path source : cases)
					render(source, engine[0]);

		writeIndex(cases, graphviz);
		System.out.println("Gallery written to " + new File(OUTPUT, "index.html"));
	}

	/**
	 * Only a real executable counts. Rendering without a pragma would silently fall through to
	 * whichever engine takes over, and that output would then be labelled as Graphviz.
	 */
	private static boolean dotIsAvailable() {
		final File dot = GraphvizRuntimeEnvironment.getInstance().getDotExe();
		return dot != null && dot.isFile() && dot.canRead() && dot.canExecute();
	}

	private static List<Path> cases() throws IOException {
		final List<Path> cases = new ArrayList<>();
		try (java.util.stream.Stream<Path> files = Files.list(CASES)) {
			files.filter(path -> path.getFileName().toString().endsWith(".puml")).forEach(cases::add);
		}
		Collections.sort(cases);
		return cases;
	}

	/** Graphviz is the default, so only the other two engines need a pragma. */
	private static void render(Path source, String engine) throws IOException {
		final File directory = new File(OUTPUT, engine);
		directory.mkdirs();
		final List<String> config = "graphviz".equals(engine) ? Collections.<String>emptyList()
				: Arrays.asList("!pragma layout " + engine);
		final File file = source.toFile();
		new SourceFileReader(false, Defines.createWithFileName(file), file, directory, config, "UTF-8",
				new FileFormatOption(FileFormat.SVG)).getGeneratedImages();
	}

	private static void writeIndex(List<Path> cases, boolean graphviz) throws IOException {
		final StringBuilder html = new StringBuilder();
		html.append("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n<meta charset=\"utf-8\">\n");
		html.append("<title>graph-support gallery</title>\n<style>\n");
		html.append("body{margin:0;font:14px/1.6 -apple-system,BlinkMacSystemFont,'Segoe UI',sans-serif;color:#1f2328}\n");
		html.append("header{padding:18px 24px;border-bottom:1px solid #d7dae0}\n");
		html.append("h1{margin:0;font-size:17px}\n.sub{color:#6b7280;font-size:13px}\n");
		html.append("section{padding:18px 24px;border-bottom:1px solid #eceef1}\n");
		html.append("h2{font-size:14px;margin:0 0 10px;font-family:ui-monospace,monospace}\n");
		html.append(".cols{display:grid;grid-template-columns:repeat(auto-fit,minmax(240px,1fr));gap:16px;align-items:start}\n");
		html.append(".card{border:1px solid #d7dae0;border-radius:8px;overflow:hidden}\n");
		html.append(".cap{padding:6px 10px;background:#fafbfc;border-bottom:1px solid #d7dae0;font-size:12px}\n");
		html.append(".body{padding:10px;overflow:auto;max-height:420px}\n.body img{max-width:100%}\n");
		html.append(".missing{padding:10px;color:#6b7280;font-size:12px}\n");
		html.append("pre{background:#f6f8fa;padding:10px;border-radius:6px;font-size:12px;overflow:auto}\n");
		html.append(".what{margin:0 0 8px;color:#57606a;font-size:13px}\n");
		html.append("</style>\n</head>\n<body>\n<header><h1>graph-support gallery</h1>\n");
		html.append("<div class=\"sub\">").append(cases.size());
		html.append(" cases, each rendered by every layout engine. Nothing is asserted; compare by eye.");
		if (graphviz == false)
			html.append(" No dot executable was found, so the Graphviz column is left out.");
		html.append("</div>\n</header>\n");

		for (Path source : cases) {
			final String id = stripExtension(source.getFileName().toString());
			final String text = new String(Files.readAllBytes(source), StandardCharsets.UTF_8);
			html.append("<section>\n<h2>").append(escape(id)).append("</h2>\n");
			html.append("<p class=\"what\">").append(escape(description(text))).append("</p>\n");
			html.append("<pre>").append(escape(text));
			html.append("</pre>\n<div class=\"cols\">\n");
			for (String[] engine : ENGINES) {
				if (graphviz == false && "graphviz".equals(engine[0]))
					continue;

				final File svg = new File(new File(OUTPUT, engine[0]), id + ".svg");
				html.append("<div class=\"card\"><div class=\"cap\">").append(escape(engine[1])).append("</div>");
				if (svg.isFile())
					html.append("<div class=\"body\"><img src=\"").append(engine[0]).append('/').append(id)
							.append(".svg\" alt=\"").append(escape(engine[1])).append("\"></div>");
				else
					html.append("<div class=\"missing\">not rendered</div>");
				html.append("</div>\n");
			}
			html.append("</div>\n</section>\n");
		}
		html.append("</body>\n</html>\n");
		OUTPUT.mkdirs();
		Files.write(new File(OUTPUT, "index.html").toPath(), html.toString().getBytes(StandardCharsets.UTF_8));
	}

	/** The first comment line of a case says what it is meant to show. */
	private static String description(String text) {
		for (String line : text.split("\n"))
			if (line.startsWith("'"))
				return line.substring(1).trim();

		return "";
	}

	private static String stripExtension(String name) {
		return name.substring(0, name.lastIndexOf('.'));
	}

	private static String escape(String text) {
		return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
	}
}
