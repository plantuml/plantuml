package net.sourceforge.plantuml.style.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.style.AutomaticCounterBasic;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleLoader;
import net.sourceforge.plantuml.utils.BlocLines;

class StyleParserTest {

	@Test
	void testParseSingleLine() throws StyleParsingException {
		// The legacy net.sourceforge.plantuml.style.parser.StyleParser this test named is gone
		// now, along with every other production caller of it: its own parseSingleLine() could
		// read a bare "Prop:value;Prop:value;" list straight at its Context's still-empty root,
		// with no selector of its own, and hand back a Style with an empty signature.
		// StyleLoader#parseStyleText has no such root-level property syntax -- a stack-empty
		// property declaration always throws (see RawStyleParser#parsePropertyDeclaration) --
		// so the single line is wrapped in a throwaway selector to give it one instead, exactly
		// the way Stereogroup#getInnerColors does for this very same shape of text (see
		// StereogroupTest#testGetInnerColorsFromASemicolonSeparatedPropertyList). The wrapper
		// name ends up folded into the resulting Style's signature as a stereotype tag -- see
		// the "[wrapper]" below -- which the original, signature-less legacy result never had;
		// nothing that reads this Style (Colors#applyStyle, and this test itself) ever looks at
		// its signature, only at its property values.
		final Collection<Style> styles = StyleLoader.parseStyleText(
				BlocLines.singleString("wrapper { BackGroundColor: lightblue; FontColor: red; }"),
				new AutomaticCounterBasic());

		assertEquals(1, styles.size());
		final Style style = styles.iterator().next();

		assertEquals("[]  [wrapper] {FontColor=red/null (1002), BackGroundColor=lightblue/null (1001)}",
				style.toString());

		assertEquals("red", style.value(PName.FontColor).asString());
		assertEquals("lightblue", style.value(PName.BackGroundColor).asString());
	}

}
