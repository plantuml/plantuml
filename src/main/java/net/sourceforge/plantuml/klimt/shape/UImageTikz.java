package net.sourceforge.plantuml.klimt.shape;

import java.util.Objects;

import net.sourceforge.plantuml.klimt.UShape;

public class UImageTikz implements UShape {

	private final String tikzCode;
	private final double width;
	private final double height;

	private boolean hasUrl;
	private double scaleFactor = 1;

	public UImageTikz(String tikzCode, double width, double height) {
		this.tikzCode = Objects.requireNonNull(tikzCode);
		this.width = width;
		this.height = height;
		scan(tikzCode);
	}

	private void scan(String tikzCode) {
		boolean beforeTikzPicture = true;
		for (final String line : tikzCode.split("\n")) {
			final String trimmed = line.trim();
			if (beforeTikzPicture && trimmed.startsWith("\\scalebox{"))
				scaleFactor = parseScaleFactor(trimmed);

			if (trimmed.startsWith("\\begin{tikzpicture}"))
				beforeTikzPicture = false;

			if (containsUrl(trimmed))
				hasUrl = true;
		}
	}

	public String getTikzCode() {
		return tikzCode;
	}

	public boolean hasUrl() {
		return hasUrl;
	}

	public double getWidth() {
		return width * scaleFactor;
	}

	public double getHeight() {
		return height * scaleFactor;
	}

	private static double parseScaleFactor(String texLine) {
		final int start = texLine.indexOf('{') + 1;
		final int end = texLine.indexOf('}', start);
		if (start > 0 && end > start) {
			try {
				return Double.parseDouble(texLine.substring(start, end));
			} catch (NumberFormatException e) {
				// ignore
			}
		}
		return 1;
	}

	private static boolean containsUrl(String texLine) {
		return texLine.contains("\\href{") || texLine.contains("\\hyperref[")
				|| texLine.contains("href node") || texLine.contains("hyperref node");
	}

}
