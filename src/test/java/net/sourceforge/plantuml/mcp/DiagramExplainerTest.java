package net.sourceforge.plantuml.mcp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.command.Explanation;

class DiagramExplainerTest {

	private final DiagramExplainer explainer = new DiagramExplainer();

	@Test
	void test1() throws IOException {
		final String source = """
				@startuml
				Alice -> Bob
				@enduml
								""";
		final List<Explanation> result = explainer.explain(source);
		assertEquals("[[Alice -> Bob] ==> Message from 'Alice' to 'Bob']", result.toString());
	}

	@Test
	void test2() throws IOException {
		final String source = """
				@startuml
				actor Alice
				Alice -> Bob
				@enduml
								""";
		final List<Explanation> result = explainer.explain(source);
		assertEquals("[[actor Alice] ==> Declaring actor 'Alice', [Alice -> Bob] ==> Message from 'Alice' to 'Bob']",
				result.toString());
	}

	@Test
	void test3() throws IOException {
		final String source = """
				@startuml
				actor Alice
				Alice -> Bob : hello
				@enduml
								""";
		final List<Explanation> result = explainer.explain(source);
		assertEquals(
				"[[actor Alice] ==> Declaring actor 'Alice', [Alice -> Bob : hello] ==> Message from 'Alice' to 'Bob']",
				result.toString());
	}

	@Test
	void testClassSimple() throws IOException {
		final String source = """
				@startuml
				class Foo
				@enduml
								""";
		final List<Explanation> result = explainer.explain(source);
		assertEquals("[[class Foo] ==> Creating class 'Foo']", result.toString());
	}

	@Test
	void testInterface() throws IOException {
		final String source = """
				@startuml
				interface Bar
				@enduml
								""";
		final List<Explanation> result = explainer.explain(source);
		assertEquals("[[interface Bar] ==> Creating interface 'Bar']", result.toString());
	}

	@Test
	void testClassWithStereotype() throws IOException {
		final String source = """
				@startuml
				class Foo <<service>>
				@enduml
								""";
		final List<Explanation> result = explainer.explain(source);
		assertEquals("[[class Foo <<service>>] ==> Creating class 'Foo', stereotype <<service>>]", result.toString());
	}

	@Test
	void testClassWithExtends() throws IOException {
		final String source = """
				@startuml
				class Foo extends Bar
				@enduml
								""";
		final List<Explanation> result = explainer.explain(source);
		assertEquals("[[class Foo extends Bar] ==> Creating class 'Foo', extends Bar]", result.toString());
	}

	@Test
	void testTwoClasses() throws IOException {
		final String source = """
				@startuml
				class Foo
				class Bar
				@enduml
								""";
		final List<Explanation> result = explainer.explain(source);
		assertEquals("[[class Foo] ==> Creating class 'Foo', [class Bar] ==> Creating class 'Bar']", result.toString());
	}

	@Test
	void test4() throws IOException {
		final String source = """
				@startuml
				class Foo {
				  + foo
				  - bar
				}
				@enduml
								""";
		final List<Explanation> result = explainer.explain(source);
		assertEquals("[[class Foo {,   + foo,   - bar, }] ==> Creating class 'Foo', with 3 body lines]",
				result.toString());
	}

}
