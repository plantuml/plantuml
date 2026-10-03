package net.sourceforge.plantuml.tim.builtin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.preproc.Environment;
import net.sourceforge.plantuml.text.StringLocated;

/**
 * %filename(), %filename_no_extension() and %dirpath() read the environment
 * through the context when they are called.
 */
class EnvironmentFunctionsTest {

	private static String preprocess(Defines defines, String line) {
		final String source = "@startuml\n" + line + "\n@enduml";
		final SourceStringReader reader = new SourceStringReader(defines, source, StandardCharsets.UTF_8,
				Collections.<String>emptyList(), null);
		final List<StringLocated> data = reader.getBlocks().get(0).getData();
		return data.get(1).getString();
	}

	@Test
	void functionsGiveTheValuesOfTheDefines() {
		final Defines defines = Defines.createEmpty();
		defines.overrideFilename("foo.bar.puml");
		defines.overrideDirPath("C:\\some\\dir");

		assertEquals("foo.bar.puml", preprocess(defines, "%filename()"));
		assertEquals("foo.bar", preprocess(defines, "%filename_no_extension()"));
		assertEquals("C:/some/dir", preprocess(defines, "%dirpath()"));
	}

	@Test
	void functionsGiveAnEmptyStringWithoutValue() {
		final Defines defines = Defines.createEmpty();

		assertEquals("", preprocess(defines, "%filename()"));
		assertEquals("", preprocess(defines, "%filename_no_extension()"));
		assertEquals("", preprocess(defines, "%dirpath()"));
		assertEquals("", preprocess(defines, "%filedate()"));
	}

	@Test
	void eachDiagramSeesItsOwnValues() {
		final Defines first = Defines.createEmpty();
		first.overrideFilename("first.puml");
		final Defines second = Defines.createEmpty();
		second.overrideFilename("second.puml");

		assertEquals("first.puml", preprocess(first, "%filename()"));
		assertEquals("second.puml", preprocess(second, "%filename()"));
		assertEquals("first.puml", preprocess(first, "%filename()"));
	}

	@Test
	void environmentCopyIsASnapshot() {
		final Environment original = new Environment();
		original.put("filename", "a.puml");
		final Environment copy = original.copy();
		original.put("filename", "b.puml");

		assertNotSame(original, copy);
		assertEquals("a.puml", copy.get("filename"));
		assertEquals("b.puml", original.get("filename"));
		assertNull(copy.get("dirpath"));
	}

	@Test
	void definesGiveASnapshotOfTheirEnvironment() {
		final Defines defines = Defines.createEmpty();
		defines.overrideFilename("a.puml");
		final Environment snapshot = defines.getEnvironment();
		defines.overrideFilename("b.puml");

		assertEquals("a.puml", snapshot.get("filename"));
		assertEquals("b.puml", defines.getEnvironmentValue("filename"));
	}
}
