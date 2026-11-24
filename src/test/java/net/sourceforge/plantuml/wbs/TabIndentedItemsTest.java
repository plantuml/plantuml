package net.sourceforge.plantuml.wbs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.mindmap.MindMapDiagram;

/**
 * Items indented with a tab, including the first one, used to throw
 * "UnsupportedOperationException: type=< **>[\t*]" because the tab was turned
 * into a space in each item's marker but not in the first item's marker.
 */
class TabIndentedItemsTest {

	private static Diagram parse(String source) {
		return new SourceStringReader(source).getBlocks().get(0).getDiagram();
	}

	@ParameterizedTest
	@ValueSource(strings = { "\t", "\t\t", " \t" })
	void wbs_items_indented_with_tabs(String indent) {
		final String source = "@startwbs\n" //
				+ indent + "* Business Process\n" //
				+ indent + "** Launch\n" //
				+ indent + "*** Complete\n" //
				+ indent + "** Design\n" //
				+ "@endwbs\n";
		final Diagram diagram = parse(source);
		assertInstanceOf(WBSDiagram.class, diagram);
		assertEquals(parse(source.replace('\t', ' ')).getDescription().getDescription(),
				diagram.getDescription().getDescription());
	}

	@ParameterizedTest
	@ValueSource(strings = { "\t", "\t\t", " \t" })
	void mindmap_items_indented_with_tabs(String indent) {
		final String source = "@startmindmap\n" //
				+ indent + "# Root\n" //
				+ indent + "## Child\n" //
				+ indent + "### Grandchild\n" //
				+ indent + "## Other\n" //
				+ "@endmindmap\n";
		final Diagram diagram = parse(source);
		assertInstanceOf(MindMapDiagram.class, diagram);
	}

}
