package net.sourceforge.plantuml.style;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link StyleAtom}: the interning of the {@link SName} family, the
 * equals/hashCode contract across both families, and the total order the
 * {@link StyleAtomTrie} walk relies on.
 */
class StyleAtomTest {

	@Test
	@DisplayName("of() returns the same instance for the same SName")
	void ofIsInterned() {
		assertSame(StyleAtom.of(SName.root), StyleAtom.of(SName.root));
		assertNotEquals(StyleAtom.of(SName.root), StyleAtom.of(SName.element));
	}

	@Test
	@DisplayName("of() rejects null")
	void ofRejectsNull() {
		assertThrows(IllegalArgumentException.class, () -> StyleAtom.of(null));
	}

	@Test
	@DisplayName("hashCode() is the SName ordinal, so it does not vary between runs")
	void hashCodeIsOrdinalForSName() {
		for (SName name : SName.values())
			assertEquals(name.ordinal(), StyleAtom.of(name).hashCode());
	}

	@Test
	@DisplayName("ofStereotype() drops '_' and '.' and lower-cases, so variants are one atom")
	void stereotypeIsCleaned() {
		final StyleAtom a = StyleAtom.ofStereotype("Foo");
		final StyleAtom b = StyleAtom.ofStereotype("f.o_o");

		assertEquals(a, b);
		assertEquals(b, a);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals("foo", a.getStereotype());
	}

	@Test
	@DisplayName("equals() separates the two families, in both directions")
	void equalsAcrossFamilies() {
		final StyleAtom sname = StyleAtom.of(SName.root);
		final StyleAtom stereotype = StyleAtom.ofStereotype("root");

		assertTrue(sname.isName());
		assertFalse(stereotype.isName());
		assertNotEquals(sname, stereotype);
		assertNotEquals(stereotype, sname);
		assertNotEquals(stereotype, StyleAtom.ofStereotype("other"));
		assertNotEquals(sname, "not an atom");
	}

	@Test
	@DisplayName("every SName atom sorts before every stereotype atom")
	void snameAtomsSortFirst() {
		final List<StyleAtom> atoms = new ArrayList<>();
		for (SName name : SName.values())
			atoms.add(StyleAtom.of(name));

		atoms.add(StyleAtom.ofStereotype("foo"));
		atoms.add(StyleAtom.ofStereotype("bar"));
		Collections.shuffle(atoms, new Random(42));
		Collections.sort(atoms);

		boolean seenStereotype = false;
		for (StyleAtom atom : atoms)
			if (atom.isName())
				assertFalse(seenStereotype, "an SName atom sorted after a stereotype atom");
			else
				seenStereotype = true;

		assertTrue(seenStereotype);
	}

}
