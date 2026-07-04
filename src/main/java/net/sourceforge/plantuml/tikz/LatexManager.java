package net.sourceforge.plantuml.tikz;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class LatexManager implements AutoCloseable {

	private final Process process;
	private final PrintWriter writer;
	private final BufferedReader reader;

	private static final String TEMPLATE_PREFIX = "{\\sbox0{";
	private static final String TEMPLATE_SUFFIX = "}\\typeout{\\the\\wd0,\\the\\ht0,\\the\\dp0}}";
	private static final Pattern PATTERN = Pattern.compile("\\*?[\\d.]+pt,[\\d.]+pt,[\\d.]+pt");
	private final LruCache lruCache = new LruCache(128);

	private static final String[] CANDIDATE_TEX_SYSTEMS = { "lualatex", "xelatex", "pdflatex" };

	public LatexManager(String system, String preamble) {
		final String command = resolveTexSystem(system);
		if (!command.endsWith("latex")) {
			throw new IllegalArgumentException("command " + command + " is unsupported");
		}
		try {
			File tempDir = Files.createTempDirectory("plantuml-latex-").toFile();
			tempDir.deleteOnExit();
			final ProcessBuilder pb = new ProcessBuilder(command, "-halt-on-error")
							.directory(tempDir);
			// Avoid inheriting a relative TEXMF_OUTPUT_DIRECTORY (set by TeX Live for
			// sub-processes), which would make the measurement process write outside its
			// temp dir and hang. See issue #2764.
			pb.environment().remove("TEXMF_OUTPUT_DIRECTORY");
			this.process = pb.start();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.writer = new PrintWriter(new OutputStreamWriter(this.process.getOutputStream()), true);
		this.reader = new BufferedReader(new InputStreamReader(this.process.getInputStream()));
		this.writer.println("\\documentclass[tikz]{standalone}\n" +
						"\\usepackage{amsmath}\n" +
						((preamble != null && !preamble.isEmpty()) ? preamble + "\n" : "") +
						"\\begin{document}\n" +
						"\\typeout{latex_query_start}");
		String output = expect("*latex_query_start", null);
		if (!output.trim().endsWith("*latex_query_start")) {
			throw new IllegalArgumentException(command + " fail, message: " + output + System.lineSeparator()
							+ "please install " + command + ", and package `amsmath`, `tikz`");
		}
	}

	/**
	 * Resolves the LaTeX engine used to measure text for TikZ output.
	 * <p>
	 * An explicit {@code !pragma tex_system} always wins. Otherwise the first
	 * engine found on the {@code PATH} is used, preferring lualatex, then xelatex,
	 * then pdflatex -- so machines that ship only lualatex or pdflatex no longer
	 * silently produce empty output. lualatex and xelatex yield identical TikZ
	 * metrics, so preferring lualatex changes nothing visible where either exists.
	 * When none is found the first candidate is returned, so the caller still gets
	 * the usual error (or falls back to the AWT font-based string bounder). See
	 * issue #2764.
	 */
	static String resolveTexSystem(String system) {
		if (system != null && !system.isEmpty())
			return system;
		for (String candidate : CANDIDATE_TEX_SYSTEMS)
			if (isExecutableOnPath(candidate))
				return candidate;
		return CANDIDATE_TEX_SYSTEMS[0];
	}

	private static boolean isExecutableOnPath(String name) {
		final String path = System.getenv("PATH");
		if (path == null)
			return false;
		final boolean windows = File.separatorChar == '\\';
		for (String dirname : path.split(File.pathSeparator)) {
			if (dirname.isEmpty())
				continue;
			final File exe = new File(dirname, name);
			if (exe.isFile() && exe.canExecute())
				return true;
			if (windows)
				for (String ext : new String[] { ".cmd", ".bat", ".exe" }) {
					final File exeWithExt = new File(dirname, name + ext);
					if (exeWithExt.isFile() && exeWithExt.canExecute())
						return true;
				}
		}
		return false;
	}

	private String expect(String s, String end) {
		StringBuilder sb = new StringBuilder();
		while (true) {
			String line;
			try {
				line = this.reader.readLine();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			if (line == null) {
				break;
			}
			sb.append(line);
			sb.append(System.lineSeparator());
			if ((end == null && line.startsWith(s)) || (end != null && line.endsWith(end))) {
				break;
			}
		}
		return sb.toString();
	}

	public double[] getWidthHeightDepth(String s) {
		double[] value = this.lruCache.get(s);
		if (value == null) {
			value = doGetWidthHeightDepth(s);
			this.lruCache.put(s, value);
		}
		return value;
	}

	protected double[] doGetWidthHeightDepth(String s) {
		this.writer.println(TEMPLATE_PREFIX + s + TEMPLATE_SUFFIX);
		String output = this.expect("*", "pt");
		String line = output.trim();
		int index = line.lastIndexOf(System.lineSeparator());
		if (index > 0) {
			line = line.substring(index + 1);
		}
		if (!PATTERN.matcher(line).matches()) {
			System.err.println("[error] cannot get width, height, depth, text: " + s + ", message: " + output.trim());
			throw new IllegalArgumentException("cannot get width, height, depth, text: " + s + ", message: " + output.trim());
		}
		if (!line.startsWith("*")) {
			System.err.println("[warning] cannot get width, height, depth, text: " + s + ", message: " + output.trim());
		}
		String[] pts = line.replace("*", "").split(",", 3);
		double width = Double.parseDouble(pts[0].replace("pt", ""));
		double height = Double.parseDouble(pts[1].replace("pt", ""));
		double depth = Double.parseDouble(pts[2].replace("pt", ""));
		return new double[] {width, height, depth};
	}

	public static String protectText(String text) {
		final String tempBackslash = "\uFFFF";
		return text.replace("\\", tempBackslash)
				.replace("#", "\\#")
				.replace("$", "\\$")
				.replace("%", "\\%")
				.replace("&", "\\&")
				.replace("_", "\\_")
				.replace("{", "\\{")
				.replace("}", "\\}")
				.replace("^", "\\^{}")
				.replace("~", "\\~{}")
				.replace("\u00AB", "\\guillemotleft{}")
				.replace("\u00BB", "\\guillemotright{}")
				.replace("\t", "~~~~~~~~") // #1016
				.replaceAll("^\\s+|\\s+$", "~")
				.replace(tempBackslash, "\\textbackslash{}");
	}

	@Override
	public void close() {
		this.process.destroy();
	}

	private static class LruCache extends LinkedHashMap<String, double[]> {

		private final int maxSize;

		public LruCache(int maxSize) {
			super(maxSize + 1, 1.0F, true);
			this.maxSize = maxSize;
		}

		@Override
		protected boolean removeEldestEntry(Map.Entry<String, double[]> eldest) {
			return this.size() >= this.maxSize;
		}

	}

}
