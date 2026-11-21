package net.sourceforge.plantuml.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class PythonLineTest {

	@Test
	void aNameAssignedWithoutDollar() {
		assertEquals("x", PythonLine.nameAssignedWithoutDollar("!x = 1"));
		assertEquals("t", PythonLine.nameAssignedWithoutDollar("!t=\"this is t1\\nthis is t2\""));
		assertEquals("Response", PythonLine.nameAssignedWithoutDollar("  !Response = \"FOO\""));
		assertEquals("ijk", PythonLine.nameAssignedWithoutDollar("!ijk\t= \"foo\""));
		assertEquals("foo", PythonLine.nameAssignedWithoutDollar("!foo = $myjson.root"));
		assertEquals("a_at_AtoB", PythonLine.nameAssignedWithoutDollar("!a_at_AtoB = \"a\""));
		// a Python keyword cannot be assigned in Python: before Python it was a legacy variable
		assertEquals("global", PythonLine.nameAssignedWithoutDollar("!global = \"changed value\""));
	}

	@Test
	void otherLinesHaveNone() {
		// legacy assignments, already told apart by isLegacyAffectation
		assertNull(PythonLine.nameAssignedWithoutDollar("!$x = 1"));
		assertNull(PythonLine.nameAssignedWithoutDollar("!x ?= 1"));
		assertNull(PythonLine.nameAssignedWithoutDollar("!global $x = 1"));
		// Python only
		assertNull(PythonLine.nameAssignedWithoutDollar("!x == 1"));
		assertNull(PythonLine.nameAssignedWithoutDollar("!x += 1"));
		assertNull(PythonLine.nameAssignedWithoutDollar("!x.y = 1"));
		assertNull(PythonLine.nameAssignedWithoutDollar("!x[0] = 1"));
		assertNull(PythonLine.nameAssignedWithoutDollar("!a, b = 1, 2"));
		assertNull(PythonLine.nameAssignedWithoutDollar("!f(1)"));
		assertNull(PythonLine.nameAssignedWithoutDollar("!1 = 2"));
		assertNull(PythonLine.nameAssignedWithoutDollar("!x"));
		// not a "!" line
		assertNull(PythonLine.nameAssignedWithoutDollar("x = 1"));
	}

}
