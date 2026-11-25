package net.sourceforge.plantuml.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.core.Diagram;

/**
 * The two values of "!pragma svgsize" may be separated by any blank that %s
 * accepts: a tab or a non-breaking space was not seen as a separator, and the
 * pragma was silently ignored. Only the SVG text measurement uses it, which the
 * Vega tests do not go through, hence this test on the skin parameters.
 */
class CommandPragmaTest {

	@ParameterizedTest
	@ValueSource(strings = { "i WWW", "i\tWWW", "i WWW", "i \t WWW" })
	void svgsize_values_separated_by_any_blank(String values) {
		final String source = "@startuml\n!pragma svgsize " + values + "\nclass A\n@enduml\n";
		final Diagram diagram = new SourceStringReader(source).getBlocks().get(0).getDiagram();
		assertInstanceOf(TitledDiagram.class, diagram);
		assertEquals("WWW", ((TitledDiagram) diagram).getSkinParam().transformStringForSizeHack("i"));
	}

}
