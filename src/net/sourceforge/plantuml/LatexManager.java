package net.sourceforge.plantuml;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;

public class LatexManager implements AutoCloseable {

	private final Process process;
	private final PrintWriter writer;
	private final BufferedReader reader;

	private static final String TEMPLATE_PREFIX = "{" +
					"\\catcode`\\^=\\active" +
					"\\catcode`\\%=\\active" +
					"\\sbox0{{" +
					"\\catcode`\\^=\\active\\def^{\\ifmmode\\sp\\else\\^{}\\fi}" +
					"\\catcode`\\%=\\active\\def%{\\%}";
	private static final String TEMPLATE_SUFFIX = "}}" +
					"\\typeout{\\the\\wd0,\\the\\ht0,\\the\\dp0}" +
					"}";

	public LatexManager(String system, String preamble) {
		String command = (system != null && !system.isEmpty()) ? system : "xelatex";
		if (!command.endsWith("latex")) {
			throw new IllegalArgumentException("command " + command + " is unsupported");
		}
		try {
			File tempDir = Files.createTempDirectory("plantuml-latex-").toFile();
			tempDir.deleteOnExit();
			this.process = new ProcessBuilder(command, "-halt-on-error")
							.directory(tempDir)
							.start();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.writer = new PrintWriter(new OutputStreamWriter(this.process.getOutputStream()), true);
		this.reader = new BufferedReader(new InputStreamReader(this.process.getInputStream()));
		this.writer.println("\\documentclass{standalone}\n" +
						"\\usepackage{tikz}\n" +
						"\\usepackage{aeguill}\n" +
						((preamble != null && !preamble.isEmpty()) ? preamble + "\n" : "") +
						"\\begin{document}\n" +
						"\\typeout{latex_query_start}");
		if (expect("*latex_query_start") == null) {
			throw new IllegalArgumentException("please install " + command + ", and package `tikz`, `aeguill` (and `ae`)");
		}
	}

	private String expect(String s) {
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
			if (line.startsWith("*Missing character")) {
				System.err.println(line);
				break;
			}
			sb.append(line);
			sb.append(System.lineSeparator());
			if (line.startsWith(s)) {
				return sb.toString();
			}
		}
		System.err.println(sb);
		return null;
	}

	public double[] getWidthHeightDepth(String s) {
		this.writer.println(TEMPLATE_PREFIX + s + TEMPLATE_SUFFIX);
		String output = this.expect("*");
		if (output == null) {
			throw new IllegalArgumentException("cannot get width, height, depth, text: " + s);
		}
		String[] pts = output.trim().replace("*", "").split(",", 3);
		double width = Double.parseDouble(pts[0].replace("pt", ""));
		double height = Double.parseDouble(pts[1].replace("pt", ""));
		double depth = Double.parseDouble(pts[2].replace("pt", ""));
		return new double[] {width, height, depth};
	}

	@Override
	public void close() {
		this.process.destroy();
	}

}
