package net.sourceforge.plantuml.svg.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.svg.parser.ISvgSpriteParser;
import net.sourceforge.plantuml.svg.parser.SvgNanoParser;
import net.sourceforge.plantuml.svg.parser.SvgSaxParser;
import net.sourceforge.plantuml.svg.parser.SvgSpriteParserFactory;
import net.sourceforge.plantuml.skin.Pragma;

public class SvgSpriteParserFactoryTest {

	private static final String SAMPLE_SVG = "<svg xmlns=\"http://www.w3.org/2000/svg\"></svg>";

	@Test
	public void testDefaultParserIsNano() {
		String original = System.getProperty("plantuml.svg.parser");
		try {
			System.clearProperty("plantuml.svg.parser");
			ISvgSpriteParser parser = SvgSpriteParserFactory.create(SAMPLE_SVG);
			assertTrue(parser instanceof SvgNanoParser, "Default parser should be Nano");
		} finally {
			restoreProperty(original);
		}
	}

	@Test
	public void testExplicitSaxParser() {
		String original = System.getProperty("plantuml.svg.parser");
		try {
			System.setProperty("plantuml.svg.parser", "sax");
			ISvgSpriteParser parser = SvgSpriteParserFactory.create(SAMPLE_SVG);
			assertTrue(parser instanceof SvgSaxParser, "Property 'sax' should select SAX parser");
		} finally {
			restoreProperty(original);
		}
	}

	@Test
	public void testExplicitNanoParser() {
		String original = System.getProperty("plantuml.svg.parser");
		try {
			System.setProperty("plantuml.svg.parser", "nano");
			ISvgSpriteParser parser = SvgSpriteParserFactory.create(SAMPLE_SVG);
			assertTrue(parser instanceof SvgNanoParser, "Property 'nano' should select Nano parser");
		} finally {
			restoreProperty(original);
		}
	}

	@Test
	public void testPragmaParserOverridesDefault() {
		Pragma pragma = Pragma.createEmpty();
		pragma.define("svgparser", "sax");
		ISvgSpriteParser parser = SvgSpriteParserFactory.create(SAMPLE_SVG, pragma);
		assertTrue(parser instanceof SvgSaxParser, "Pragma 'svgparser sax' should select SAX parser");
	}

	private void restoreProperty(String original) {
		if (original == null) {
			System.clearProperty("plantuml.svg.parser");
		} else {
			System.setProperty("plantuml.svg.parser", original);
		}
	}
}
