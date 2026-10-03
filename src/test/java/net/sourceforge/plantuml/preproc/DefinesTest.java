package net.sourceforge.plantuml.preproc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.TMemory;
import net.sourceforge.plantuml.tim.TMemoryGlobal;

class DefinesTest {

	@Test
	void definedValuesAreCopiedToTheMemory() throws EaterException {
		final Defines defines = Defines.createEmpty();
		defines.define("NAME", "value");
		defines.define("EMPTY", "");
		defines.define("NOTHING", null);

		final TMemory memory = new TMemoryGlobal();
		defines.copyTo(memory, null);

		assertEquals("value", memory.getVariable("NAME").toString());
		assertEquals("", memory.getVariable("EMPTY").toString());
		assertEquals("", memory.getVariable("NOTHING").toString());
	}

	@Test
	void redefiningReplacesTheValue() throws EaterException {
		final Defines defines = Defines.createEmpty();
		defines.define("NAME", "first");
		defines.define("NAME", "second");

		final TMemory memory = new TMemoryGlobal();
		defines.copyTo(memory, null);

		assertEquals("second", memory.getVariable("NAME").toString());
	}

	@Test
	void isTrueTellsWhetherANameIsDefined() {
		final Defines defines = Defines.createEmpty();
		defines.define("NAME", "value");

		assertTrue(defines.isTrue("NAME"));
		assertFalse(defines.isTrue("OTHER"));
	}

	@Test
	void cloneKeepsTheDefinesAndTheEnvironment() throws EaterException {
		final Defines defines = Defines.createEmpty();
		defines.overrideFilename("a.puml");
		defines.define("NAME", "value");

		final Defines clone = defines.cloneMe();
		defines.define("LATER", "x");

		assertEquals("a.puml", clone.getEnvironmentValue("filename"));
		assertTrue(clone.isTrue("NAME"));
		assertFalse(clone.isTrue("LATER"));
	}
}
