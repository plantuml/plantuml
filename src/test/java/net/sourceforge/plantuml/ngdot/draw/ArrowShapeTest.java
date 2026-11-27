package net.sourceforge.plantuml.ngdot.draw;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ArrowShapeTest {

	private static String parts(String name) {
		return ArrowShape.parse(name).getParts().toString();
	}

	@Test
	void simpleNames() {
		assertEquals("[NORMAL]", parts("normal"));
		assertEquals("[inv-NORMAL]", parts("inv"));
		assertEquals("[inv-CROW]", parts("vee"));
		assertEquals("[CROW]", parts("crow"));
		assertEquals("[oDOT]", parts("odot"));
		assertEquals("[lTEE]", parts("ltee"));
		assertEquals("[orBOX]", parts("orbox"));
		assertEquals("[NORMAL]", parts(null));
	}

	@Test
	void synonyms() {
		assertEquals("[oNORMAL]", parts("empty"));
		assertEquals("[oinv-NORMAL]", parts("invempty"));
		assertEquals("[inv-CROW]", parts("open"));
		assertEquals("[linv-CROW]", parts("halfopen"));
		assertEquals("[oDIAMOND]", parts("ediamond"));
	}

	@Test
	void sequences() {
		assertEquals("[lTEE, olDIAMOND]", parts("lteeoldiamond"));
		assertEquals("[inv-NORMAL, DOT]", parts("invdot"));
		assertEquals("[DOT, GAP, NORMAL]", parts("dotnonenormal"));
		// At most four shapes
		assertFalse(ArrowShape.parse("dotdotdotdotdot").isKnown());
	}

	@Test
	void none() {
		assertTrue(ArrowShape.parse("none").isNone());
		assertFalse(ArrowShape.parse("nonenormal").isNone());
	}

	@Test
	void unknownIsNormal() {
		final ArrowShape arrow = ArrowShape.parse("nosuch");
		assertFalse(arrow.isKnown());
		assertEquals("[NORMAL]", arrow.getParts().toString());
	}

	@Test
	void lengths() {
		assertEquals(10, ArrowShape.parse("normal").getLength(1), 1e-9);
		assertEquals(20, ArrowShape.parse("normal").getLength(2), 1e-9);
		assertEquals(5, ArrowShape.parse("tee").getLength(1), 1e-9);
		assertEquals(12, ArrowShape.parse("diamond").getLength(1), 1e-9);
		assertEquals(8, ArrowShape.parse("dot").getLength(1), 1e-9);
		assertEquals(18, ArrowShape.parse("invdot").getLength(1), 1e-9);
	}

}
