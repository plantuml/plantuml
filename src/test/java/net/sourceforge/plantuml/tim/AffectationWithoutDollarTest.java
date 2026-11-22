package net.sourceforge.plantuml.tim;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.warning.Warning;

/**
 * "!name = value", without '$': the legacy assignment it was before Python when
 * the diagram has no other Python and uses the name in its text, a Python
 * assignment otherwise (section 6 of PYTHON_PREPROCESSOR.md).
 */
class AffectationWithoutDollarTest {

	private static final String DEPRECATED = "This diagram is using the variable 't' declared without '$', which is deprecated and will be removed in the future.";

	private static BlockUml block(String... lines) {
		return new SourceStringReader("@startuml\n" + String.join("\n", lines) + "\n@enduml\n").getBlocks().get(0);
	}

	/** The preprocessed lines, between @startuml and @enduml, blank lines left out. */
	private static List<String> preprocessed(BlockUml block) {
		final List<String> result = new ArrayList<>();
		for (StringLocated line : block.getData())
			if (line.getString().trim().isEmpty() == false && line.getString().startsWith("@") == false)
				result.add(line.getString());
		return result;
	}

	private static List<String> warnings(BlockUml block) {
		final Diagram diagram = block.getDiagram();
		final List<String> result = new ArrayList<>();
		for (Warning warning : ((TitledDiagram) diagram).getWarnings())
			result.add(warning.getMessage().get(0));
		return result;
	}

	@Test
	void withoutPythonItIsTheLegacyAssignment() {
		final BlockUml block = block("!t=\"this is t1\\nthis is t2\"", "start", "repeat", ":read data;",
				"repeat while (x) is (t)", "stop");
		assertEquals("[start, repeat, :read data;, repeat while (x) is (this is t1\\nthis is t2), stop]",
				preprocessed(block).toString());
		assertEquals("[" + DEPRECATED + "]", warnings(block).toString());
	}

	@Test
	void aNameAfterBackslashNIsInTheText() {
		// as the legacy preprocessor reads it: "\\nt" is a new line, then "t"
		final BlockUml block = block("!t = \"x\"", "Alice -> Bob : a\\nt");
		assertEquals("[Alice -> Bob : a\\nx]", preprocessed(block).toString());
	}

	@Test
	void besidePythonItIsPython() {
		final BlockUml block = block("!def twice(n):", "!    return 2 * n", "Alice -> Bob : t", "!t = twice(2)",
				"title !{t}");
		assertEquals("[Alice -> Bob : t, title 4]", preprocessed(block).toString());
		assertEquals("[]", warnings(block).toString());
	}

	@Test
	void withAnInterpolationItIsPython() {
		final BlockUml block = block("!t = 42", "Alice -> Bob : t !{t}");
		assertEquals("[Alice -> Bob : t 42]", preprocessed(block).toString());
		assertEquals("[]", warnings(block).toString());
	}

	@Test
	void aNameThatIsNotInTheTextIsPython() {
		// both readings give the same diagram: the line stays Python, without a warning
		final BlockUml block = block("!t = 1", "Alice -> Bob : t1 AMS_t t_2");
		assertEquals("[Alice -> Bob : t1 AMS_t t_2]", preprocessed(block).toString());
		assertEquals("[]", warnings(block).toString());
	}

	@Test
	void aLegacyCallStaysALegacyCall() {
		final BlockUml block = block("!procedure decl($n)", "participant $n", "!endprocedure", "!decl(B)");
		assertEquals("[!participant B]", preprocessed(block).toString());
	}

	@Test
	void aLegacyCallBesideTheAssignmentIsALegacyCall() {
		final BlockUml block = block("!procedure decl($n)", "participant $n", "!endprocedure", "!t = \"A\"",
				"!decl(t)");
		// the "!" stays in front of the line the procedure gives, as before Python
		assertEquals("[!participant A]", preprocessed(block).toString());
	}

}
