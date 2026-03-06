package net.sourceforge.plantuml.svg;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.jupiter.api.Test;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
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

	private List<GradientVector> extractLinearGradientVectors(String svg) throws IOException {
		final List<GradientVector> vectors = new ArrayList<GradientVector>();
		try {
			final SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(true);
			final SAXParser parser = factory.newSAXParser();
			parser.parse(new InputSource(new StringReader(svg)), new DefaultHandler() {
				@Override
				public void startElement(String uri, String localName, String qName, Attributes attrs)
						throws SAXException {
					final String name = localName == null || localName.isEmpty() ? qName : localName;
					if ("linearGradient".equalsIgnoreCase(name) == false)
						return;

					final Double x1 = extractPercent(attrs.getValue("x1"));
					final Double y1 = extractPercent(attrs.getValue("y1"));
					final Double x2 = extractPercent(attrs.getValue("x2"));
					final Double y2 = extractPercent(attrs.getValue("y2"));
					if (x1 != null && y1 != null && x2 != null && y2 != null)
						vectors.add(new GradientVector(x1, y1, x2, y2));
				}
			});
		} catch (Exception e) {
			throw new IOException("Failed to parse SVG output for gradient vectors", e);
		}
		return vectors;
	}

	private Double extractPercent(String raw) {
		if (raw == null)
			return null;

		raw = raw.trim();
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
		if (gradients.isEmpty())
			sb.append(" (no vectors extracted; check SVG output formatting)");
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
