package net.sourceforge.plantuml.style.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;

class StyleParserTest {

	@Test
	void testParseSingleLine() throws StyleParsingException {

		final StyleParser parser = new StyleParser();
		final Style style = parser.parseSingleLine("BackGroundColor: lightblue; FontColor: red;");

		assertEquals("[]  [] {FontColor=red/null (2), BackGroundColor=lightblue/null (1)}", style.toString());

		assertEquals("red", style.value(PName.FontColor).asString());
		assertEquals("lightblue", style.value(PName.BackGroundColor).asString());
	}

}
