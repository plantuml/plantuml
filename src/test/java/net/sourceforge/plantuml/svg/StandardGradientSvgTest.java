package net.sourceforge.plantuml.svg;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
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
				"rectangle A #ffd200|8cfcff",
				"rectangle B #ffd200-8cfcff",
				"rectangle C #ffd200/8cfcff",
				"rectangle D #ffd200\\8cfcff",
				"@enduml")
				.asString(FileFormat.SVG);

		final List<String> gradients = extractLinearGradientTags(svg);
		assertTrue(hasGradientWith(gradients, "x1=\"0%\"", "y1=\"50%\"", "x2=\"100%\"", "y2=\"50%\""),
				"Missing horizontal gradient (|) vector in SVG output");
		assertTrue(hasGradientWith(gradients, "x1=\"50%\"", "y1=\"0%\"", "x2=\"50%\"", "y2=\"100%\""),
				"Missing vertical gradient (-) vector in SVG output");
		assertTrue(hasGradientWith(gradients, "x1=\"0%\"", "y1=\"0%\"", "x2=\"100%\"", "y2=\"100%\""),
				"Missing diagonal gradient (/) vector in SVG output");
		assertTrue(hasGradientWith(gradients, "x1=\"0%\"", "y1=\"100%\"", "x2=\"100%\"", "y2=\"0%\""),
				"Missing diagonal gradient (\\) vector in SVG output");
	}

	private List<String> extractLinearGradientTags(String svg) {
		final List<String> tags = new ArrayList<String>();
		final Pattern pattern = Pattern.compile("<linearGradient\\b[^>]*>");
		final Matcher matcher = pattern.matcher(svg);
		while (matcher.find()) {
			tags.add(matcher.group());
		}
		return tags;
	}

	private boolean hasGradientWith(List<String> tags, String... attrs) {
		for (String tag : tags) {
			boolean matches = true;
			for (String attr : attrs) {
				if (tag.contains(attr) == false) {
					matches = false;
					break;
				}
			}
			if (matches)
				return true;
		}
		return false;
	}
}
