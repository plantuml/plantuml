package net.sourceforge.plantuml.yaml.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

/** Block scalars, quotes, plain scalars on several lines and the errors of YamlParser. */
class YamlParserSubsetTest {

	private static Monomorph parse(String... lines) {
		return new YamlParser().parse(Arrays.asList(lines));
	}

	private static YamlSyntaxException error(String... lines) {
		return assertThrows(YamlSyntaxException.class, () -> parse(lines));
	}

	@Test
	void blockKeepsTheInnerIndentationAndTheComments() {
		final Monomorph result = parse("text: |", "  def f(x):", "    # a comment", "    return x", "# after", "z: 1");
		assertEquals("def f(x):\n  # a comment\n  return x\n", result.getMapValue("text").getValue());
		assertEquals("1", result.getMapValue("z").getValue());
	}

	@Test
	void blockEndsAtTheFirstLineThatIsNotDeeperEvenIfItIsAComment() {
		final Monomorph result = parse("a:", "  text: |", "    one", "  # not in the block", "  b: 2");
		assertEquals("one\n", result.getMapValue("a").getMapValue("text").getValue());
		assertEquals("2", result.getMapValue("a").getMapValue("b").getValue());
	}

	@Test
	void blockKeepsInnerEmptyLinesAndDropsTrailingOnes() {
		final Monomorph result = parse("t: |", "  a", "", "  b", "", "", "z: 1");
		assertEquals("a\n\nb\n", result.getMapValue("t").getValue());
	}

	@Test
	void foldedBlock() {
		final Monomorph result = parse("t: >", "  a", "  b", "", "  c", "z: 1");
		assertEquals("a b\nc\n", result.getMapValue("t").getValue());
	}

	@Test
	void blockInAListItem() {
		final Monomorph result = parse("l:", "  - name: x", "    text: |", "      one", "      two", "  - name: y");
		final Monomorph first = result.getMapValue("l").getElementAt(0);
		assertEquals("x", first.getMapValue("name").getValue());
		assertEquals("one\ntwo\n", first.getMapValue("text").getValue());
		assertEquals("y", result.getMapValue("l").getElementAt(1).getMapValue("name").getValue());
	}

	@Test
	void plainScalarOnSeveralLines() {
		final Monomorph result = parse("t: first", "  second", "z: 1");
		assertEquals("first second", result.getMapValue("t").getValue());
		assertEquals("1", result.getMapValue("z").getValue());
	}

	@Test
	void quotes() {
		final Monomorph result = parse("a: 'it''s'", "b: \"say \\\"hi\\\"\"", "c: \"x\\ny\"", "d: \"a: b\"");
		assertEquals("it's", result.getMapValue("a").getValue());
		assertEquals("say \"hi\"", result.getMapValue("b").getValue());
		assertEquals("x\ny", result.getMapValue("c").getValue());
		assertEquals("a: b", result.getMapValue("d").getValue());
	}

	@Test
	void duplicateKeyIsAnErrorOnItsLine() {
		final YamlSyntaxException e = error("a: 1", "b: 2", "a: 3");
		assertEquals(3, e.getLine());
		assertTrue(e.getMessage().contains("duplicate key 'a'"));
	}

	@Test
	void unexpectedIndentationIsAnErrorOnItsLine() {
		final YamlSyntaxException e = error("a: 1", "  b: 2");
		assertEquals(2, e.getLine());
	}

	@Test
	void textWithoutKeyIsAnErrorOnItsLine() {
		assertEquals(1, error("hello").getLine());
	}

	@Test
	void listInListAndFlowMappingAreRejected() {
		assertEquals(2, error("l:", "  - - a", "    - b").getLine());
		assertEquals(2, error("l:", "  -", "    - a").getLine());
		assertEquals(1, error("m: {a: 1}").getLine());
	}

	@Test
	void everyErrorIsAYamlSyntaxException() {
		// these used to leak IllegalStateException or UnsupportedOperationException
		error("a: 1", "- b");
		error("- a", "b: 1");
	}

}
