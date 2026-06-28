/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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

class LatexTextMetrics implements AutoCloseable {

	private final Process process;
	private final PrintWriter writer;
	private final BufferedReader reader;

	private static final String TEMPLATE_PREFIX = "{\\sbox0{";
	private static final String TEMPLATE_SUFFIX = "}\\typeout{\\the\\wd0,\\the\\ht0,\\the\\dp0}}";
	private static final Pattern PATTERN = Pattern.compile("\\*?[\\d.]+pt,[\\d.]+pt,[\\d.]+pt");
	private final LruCache lruCache = new LruCache(128);

	public LatexTextMetrics(LatexEngine latexEngine, String preamble) {
		if (latexEngine == LatexEngine.NONE || latexEngine == LatexEngine.UNKNOWN_OR_NOT_INSTALLED)
			throw new IllegalArgumentException();

		final String command = latexEngine.getCommand();

		try {
			final File tempDir = Files.createTempDirectory("plantuml-latex-").toFile();
			tempDir.deleteOnExit();
			final ProcessBuilder pb = new ProcessBuilder(command, "-halt-on-error").directory(tempDir);
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

		this.writer.println("\\documentclass[tikz]{standalone}\n" + "\\usepackage{amsmath}\n"
				+ ((preamble != null && !preamble.isEmpty()) ? preamble + "\n" : "") + "\\begin{document}\n"
				+ "\\typeout{latex_query_start}");

		final String output = expect("*latex_query_start", null);
		if (!output.trim().endsWith("*latex_query_start"))
			throw new IllegalArgumentException(command + " fail, message: " + output + System.lineSeparator()
					+ "please install " + command + ", and package `amsmath`, `tikz`");

	}

	private String expect(String s, String end) {
		final StringBuilder sb = new StringBuilder();
		while (true) {
			String line;
			try {
				line = this.reader.readLine();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			if (line == null)
				break;

			sb.append(line);
			sb.append(System.lineSeparator());
			if ((end == null && line.startsWith(s)) || (end != null && line.endsWith(end)))
				break;

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
		final String output = this.expect("*", "pt");
		String line = output.trim();
		int index = line.lastIndexOf(System.lineSeparator());
		if (index > 0)
			line = line.substring(index + 1);

		if (!PATTERN.matcher(line).matches()) {
			System.err.println("[error] cannot get width, height, depth, text: " + s + ", message: " + output.trim());
			throw new IllegalArgumentException(
					"cannot get width, height, depth, text: " + s + ", message: " + output.trim());
		}
		if (!line.startsWith("*"))
			System.err.println("[warning] cannot get width, height, depth, text: " + s + ", message: " + output.trim());

		final String[] pts = line.replace("*", "").split(",", 3);
		final double width = Double.parseDouble(pts[0].replace("pt", ""));
		final double height = Double.parseDouble(pts[1].replace("pt", ""));
		final double depth = Double.parseDouble(pts[2].replace("pt", ""));
		return new double[] { width, height, depth };
	}

	public static String protectText(String text) {
		final String tempBackslash = "\uFFFF";
		return text.replace("\\", tempBackslash) //
				.replace("#", "\\#") //
				.replace("$", "\\$") //
				.replace("%", "\\%") //
				.replace("&", "\\&") //
				.replace("_", "\\_") //
				.replace("{", "\\{") //
				.replace("}", "\\}") //
				.replace("^", "\\^{}") //
				.replace("~", "\\~{}") //
				.replace("\u00AB", "\\guillemotleft{}") //
				.replace("\u00BB", "\\guillemotright{}") //
				.replace("\t", "~~~~~~~~") // #1016
				.replaceAll("^\\s+|\\s+$", "~") //
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
