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
		ISvgSpriteParser parser = SvgSpriteParserFactory.create(SAMPLE_SVG);
		assertTrue(parser instanceof SvgNanoParser, "Default parser should be Nano");
	}

	@Test
	public void testPragmaSaxParser() {
		Pragma pragma = Pragma.createEmpty();
		pragma.define("svgparser", "sax");
		ISvgSpriteParser parser = SvgSpriteParserFactory.create(SAMPLE_SVG, pragma);
		assertTrue(parser instanceof SvgSaxParser, "Pragma 'svgparser sax' should select SAX parser");
	}

	@Test
	public void testPragmaNanoParser() {
		Pragma pragma = Pragma.createEmpty();
		pragma.define("svgparser", "nano");
		ISvgSpriteParser parser = SvgSpriteParserFactory.create(SAMPLE_SVG, pragma);
		assertTrue(parser instanceof SvgNanoParser, "Pragma 'svgparser nano' should select Nano parser");
	}
}
