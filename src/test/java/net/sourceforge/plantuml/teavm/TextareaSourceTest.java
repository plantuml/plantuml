package net.sourceforge.plantuml.teavm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.text.StringLocated;

class TextareaSourceTest {

	private static List<StringLocated> create(String text) {
		return TextareaSource.create(text.split("\n", -1), "textarea");
	}

	private static List<String> strings(List<StringLocated> lines) {
		final List<String> result = new ArrayList<>();
		for (StringLocated s : lines)
			result.add(s.getString());
		return result;
	}

	private static List<Integer> positions(List<StringLocated> lines) {
		final List<Integer> result = new ArrayList<>();
		for (StringLocated s : lines)
			result.add(s.getLocation().getPosition());
		return result;
	}

	private static List<String> list(String... s) {
		final List<String> result = new ArrayList<>();
		for (String x : s)
			result.add(x);
		return result;
	}

	private static List<Integer> list(Integer... s) {
		final List<Integer> result = new ArrayList<>();
		for (Integer x : s)
			result.add(x);
		return result;
	}

	@Test
	void unchanged() {
		final List<StringLocated> r = create("@startuml\na->b\n@enduml");
		assertEquals(list("@startuml", "a->b", "@enduml"), strings(r));
		assertEquals(list(0, 1, 2), positions(r));
	}

	@Test
	void trailingBlankLinesRemoved() {
		assertEquals(list("@startuml", "a->b", "@enduml"), strings(create("@startuml\na->b\n@enduml\n\n  \n")));
	}

	@Test
	void leadingBlankLinesKeepPositions() {
		final List<StringLocated> r = create("\n\n@startuml\na->b\n@enduml");
		assertEquals(list("@startuml", "a->b", "@enduml"), strings(r));
		assertEquals(list(2, 3, 4), positions(r));
	}

	@Test
	void textBeforeStartIgnored() {
		// Lines after @enduml are left untouched, as for any other source
		assertEquals(list("@startuml", "a->b", "@enduml", "```"),
				strings(create("```plantuml\n@startuml\na->b\n@enduml\n```")));
	}

	@Test
	void indentedStart() {
		final List<StringLocated> r = create("\n  @startuml\na->b\n@enduml");
		assertEquals(list("@startuml", "a->b", "@enduml"), strings(r));
		assertEquals(list(1, 2, 3), positions(r));
	}

	@Test
	void nonBreakingSpaceBeforeStart() {
		final List<StringLocated> r = create("\u00A0\n\u00A0@startuml\na->b\n@enduml");
		assertEquals(list("@startuml", "a->b", "@enduml"), strings(r));
		assertEquals(list(1, 2, 3), positions(r));
	}

	@Test
	void missingHeaderAndTrailer() {
		final List<StringLocated> r = create("a->b");
		assertEquals(list("@startuml", "a->b", "@enduml"), strings(r));
		assertEquals(list(-1, 0, 1), positions(r));
	}

	@Test
	void missingHeaderWithLeadingBlankLines() {
		final List<StringLocated> r = create("\n\na->b\nb->c\n\n");
		assertEquals(list("@startuml", "a->b", "b->c", "@enduml"), strings(r));
		assertEquals(list(-1, 2, 3, 4), positions(r));
	}

	@Test
	void missingHeaderOnly() {
		assertEquals(list("@startuml", "a->b", "@enduml"), strings(create("a->b\n@enduml")));
	}

	@Test
	void missingTrailerOnly() {
		assertEquals(list("@startuml", "a->b", "@enduml"), strings(create("@startuml\na->b")));
	}

	@Test
	void missingTrailerKeepsDiagramType() {
		assertEquals(list("@startmindmap", "* root", "@endmindmap"), strings(create("@startmindmap\n* root")));
		assertEquals(list("@startuml(id=foo)", "a->b", "@enduml"), strings(create("@startuml(id=foo)\na->b")));
	}

	@Test
	void emptyEditor() {
		assertEquals(list("@startuml", "@enduml"), strings(create("")));
		assertEquals(list("@startuml", "@enduml"), strings(create("\n  \n")));
	}

}
