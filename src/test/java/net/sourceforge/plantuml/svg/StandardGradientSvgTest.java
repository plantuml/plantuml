package net.sourceforge.plantuml.svg;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.FileFormat;
import test.utils.PlantUmlTestUtils;

class StandardGradientSvgTest {

	@Test
	void standardGradientPoliciesEmitExpectedVectors() throws IOException {
		final String svg = PlantUmlTestUtils.exportDiagram(
				"@startuml",
				"skinparam shadowing false",
				"rectangle A #ffd200|8cfcff",  // #8cfcff

				"rectangle B #ffd200-8cfcff",
				"rectangle C #ffd200/8cfcff",
				"rectangle D #ffd200\\8cfcff",
				"@enduml")
				.asString(FileFormat.SVG);


		final Path output = Paths.get("target/test-output/svg-sprites/standard-gradient.svg");
		Files.createDirectories(output.getParent());
		Files.writeString(output, svg, StandardCharsets.UTF_8);

		final List<GradientVector> gradients = extractLinearGradientVectors(svg);
		assertTrue(hasHorizontalGradient(gradients), failureMessage("horizontal", gradients));
		assertTrue(hasVerticalGradient(gradients), failureMessage("vertical", gradients));
		assertTrue(hasDiagonalTlBrGradient(gradients), failureMessage("diagonal TL-BR", gradients));
		assertTrue(hasDiagonalBlTrGradient(gradients), failureMessage("diagonal BL-TR", gradients));
	}

	private List<GradientVector> extractLinearGradientVectors(String svg) {
		final List<GradientVector> vectors = new ArrayList<GradientVector>();
		final Pattern pattern = Pattern.compile("<linearGradient\\b[^>]*>");
		final Matcher matcher = pattern.matcher(svg);
		while (matcher.find()) {
			final String tag = matcher.group();
			final Double x1 = extractPercent(tag, "x1");
			final Double y1 = extractPercent(tag, "y1");
			final Double x2 = extractPercent(tag, "x2");
			final Double y2 = extractPercent(tag, "y2");
			if (x1 != null && y1 != null && x2 != null && y2 != null)
				vectors.add(new GradientVector(x1, y1, x2, y2));
		}
		return vectors;
	}

	private Double extractPercent(String tag, String attr) {
		final Pattern attrPattern = Pattern.compile(attr + "=\\\"([^\\\"]+)\\\"");
		final Matcher matcher = attrPattern.matcher(tag);
		if (matcher.find() == false)
			return null;

		final String raw = matcher.group(1).trim();
		final String value = raw.endsWith("%") ? raw.substring(0, raw.length() - 1) : raw;
		try {
			return Double.valueOf(Double.parseDouble(value));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private boolean hasHorizontalGradient(List<GradientVector> gradients) {
		for (GradientVector vector : gradients) {
			final double dx = vector.x2 - vector.x1;
			final double dy = vector.y2 - vector.y1;
			if (Math.abs(dy) <= 1.0 && Math.abs(dx) >= 50.0)
				return true;
		}
		return false;
	}

	private boolean hasVerticalGradient(List<GradientVector> gradients) {
		for (GradientVector vector : gradients) {
			final double dx = vector.x2 - vector.x1;
			final double dy = vector.y2 - vector.y1;
			if (Math.abs(dx) <= 1.0 && Math.abs(dy) >= 50.0)
				return true;
		}
		return false;
	}

	private boolean hasDiagonalTlBrGradient(List<GradientVector> gradients) {
		for (GradientVector vector : gradients) {
			final double dx = vector.x2 - vector.x1;
			final double dy = vector.y2 - vector.y1;
			if (dx >= 50.0 && dy >= 50.0)
				return true;
		}
		return false;
	}

	private boolean hasDiagonalBlTrGradient(List<GradientVector> gradients) {
		for (GradientVector vector : gradients) {
			final double dx = vector.x2 - vector.x1;
			final double dy = vector.y2 - vector.y1;
			if (dx >= 50.0 && dy <= -50.0)
				return true;
		}
		return false;
	}

	private String failureMessage(String label, List<GradientVector> gradients) {
		final StringBuilder sb = new StringBuilder();
		sb.append("Missing ").append(label).append(" gradient vector in SVG output. ");
		sb.append("Vectors=");
		for (GradientVector vector : gradients) {
			final double dx = vector.x2 - vector.x1;
			final double dy = vector.y2 - vector.y1;
			sb.append(" [x1=").append(vector.x1).append(", y1=").append(vector.y1)
					.append(", x2=").append(vector.x2).append(", y2=").append(vector.y2)
					.append(", dx=").append(dx).append(", dy=").append(dy).append("]");
		}
		return sb.toString();
	}

	private static class GradientVector {
		private final double x1;
		private final double y1;
		private final double x2;
		private final double y2;

		private GradientVector(double x1, double y1, double x2, double y2) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}
	}
}
