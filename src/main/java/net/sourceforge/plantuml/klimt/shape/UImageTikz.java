package net.sourceforge.plantuml.klimt.shape;

import java.util.Objects;

import net.sourceforge.plantuml.klimt.UShape;
import net.sourceforge.plantuml.klimt.color.HColor;

public class UImageTikz implements UShape {

	private final String tikzCode;
	private final double width;
	private final double height;
	private final HColor backgroundColor;

	private boolean hasUrl;
	private double scaleFactor = 1;

	public UImageTikz(String tikzCode, double width, double height, HColor backgroundColor) {
		this.tikzCode = Objects.requireNonNull(tikzCode);
		this.width = width;
		this.height = height;
		this.backgroundColor = backgroundColor;
		scan(tikzCode);
	}

	private void scan(String tikzCode) {
		for (final String line : tikzCode.split("\n")) {
			final String trimmed = line.trim();
			if (trimmed.startsWith("\\scalebox{"))
				scaleFactor = parseScaleFactor(trimmed);

			if (containsUrl(trimmed))
				hasUrl = true;
		}
	}

	public String getTikzCode() {
		return tikzCode;
	}

	public HColor getBackgroundColor() {
		return backgroundColor;
	}

	public boolean hasUrl() {
		return hasUrl;
	}

	public double getScaleFactor() {
		return scaleFactor;
	}

	public double getWidth() {
		return width * scaleFactor;
	}

	public double getHeight() {
		return height * scaleFactor;
	}

	public double getRawWidth() {
		return width;
	}

	public double getRawHeight() {
		return height;
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
