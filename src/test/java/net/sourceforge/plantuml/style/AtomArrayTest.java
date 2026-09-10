package net.sourceforge.plantuml.style;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link AtomArray}, checked against the {@code TreeSet<StyleAtom>} behavior it is
 * meant to reproduce: sorted natural order, silent dedup on add, and set-equality regardless of
 * build order (see {@link StyleQuery#atoms} and the class javadoc on {@link AtomArray}).
 */
class AtomArrayTest {

	private static final StyleAtom ROOT = StyleAtom.of(SName.root);
	private static final StyleAtom ELEMENT = StyleAtom.of(SName.element);
	private static final StyleAtom ARROW = StyleAtom.of(SName.arrow);
	private static final StyleAtom NODE = StyleAtom.of(SName.node);
	private static final StyleAtom FOO = StyleAtom.ofStereotype("foo");
	private static final StyleAtom BAR = StyleAtom.ofStereotype("bar");

	// -----------------------------------------------------------------------
	// empty() / of()
	// -----------------------------------------------------------------------

	@Test
	@DisplayName("empty() has size 0 and no elements")
	void emptyIsEmpty() {
		assertEquals(0, AtomArray.empty().size());
		assertTrue(AtomArray.empty().isEmpty());
	}

	@Test
	@DisplayName("of() with no arguments is the empty instance")
	void ofNoArgsIsEmpty() {
		assertSame(AtomArray.empty(), AtomArray.of());
	}

	@Test
	@DisplayName("of() sorts its arguments into ascending natural order")
	void ofSortsAtoms() {
		final AtomArray atoms = AtomArray.of(NODE, ROOT, ARROW, ELEMENT);
		assertEquals(4, atoms.size());
		for (int i = 0; i + 1 < atoms.size(); i++)
			assertTrue(atoms.get(i).compareTo(atoms.get(i + 1)) < 0, "not sorted at index " + i);
	}

	@Test
	@DisplayName("of() dedups repeated atoms, same as TreeSet.add")
	void ofDedupsAtoms() {
		final AtomArray atoms = AtomArray.of(ROOT, ELEMENT, ROOT, ELEMENT, ROOT);
		assertEquals(2, atoms.size());
		assertTrue(atoms.contains(ROOT));
		assertTrue(atoms.contains(ELEMENT));
	}

	@Test
	@DisplayName("of() throws on a null atom")
	void ofRejectsNull() {
		assertThrows(IllegalArgumentException.class, () -> AtomArray.of(ROOT, null));
	}

	@Test
	@DisplayName("SName atoms sort before stereotype atoms, per StyleAtom's total order")
	void snameAtomsSortBeforeStereotypes() {
		final AtomArray atoms = AtomArray.of(FOO, ARROW);
		assertEquals(2, atoms.size());
		assertTrue(atoms.get(0).isName());
		assertFalse(atoms.get(1).isName());
	}

	// -----------------------------------------------------------------------
	// plus()
	// -----------------------------------------------------------------------

	@Test
	@DisplayName("plus() with a new atom grows the collection by one")
	void plusAddsNewAtom() {
		final AtomArray before = AtomArray.of(ROOT);
		final AtomArray after = before.plus(ELEMENT);

		assertEquals(1, before.size());
		assertEquals(2, after.size());
		assertTrue(after.contains(ROOT));
		assertTrue(after.contains(ELEMENT));
	}

	@Test
	@DisplayName("plus() with an atom already present is a no-op, returning the same instance")
	void plusExistingAtomIsNoOp() {
		final AtomArray atoms = AtomArray.of(ROOT, ELEMENT);
		assertSame(atoms, atoms.plus(ROOT));
	}

	@Test
	@DisplayName("plus() keeps ascending order regardless of insertion position")
	void plusKeepsSortedOrder() {
		AtomArray atoms = AtomArray.of(ROOT);
		atoms = atoms.plus(NODE);
		atoms = atoms.plus(ELEMENT);
		atoms = atoms.plus(ARROW);

		for (int i = 0; i + 1 < atoms.size(); i++)
			assertTrue(atoms.get(i).compareTo(atoms.get(i + 1)) < 0, "not sorted at index " + i);
	}

	@Test
	@DisplayName("plus() throws on a null atom")
	void plusRejectsNull() {
		assertThrows(IllegalArgumentException.class, () -> AtomArray.empty().plus(null));
	}

	@Test
	@DisplayName("building the same set one atom at a time or in one of() call agree")
	void plusMatchesOfRegardlessOfOrder() {
		final AtomArray built = AtomArray.of(ROOT).plus(NODE).plus(ARROW).plus(ELEMENT);
		final AtomArray direct = AtomArray.of(ELEMENT, ARROW, NODE, ROOT);
		assertEquals(direct, built);
	}

	// -----------------------------------------------------------------------
	// plusAll()
	// -----------------------------------------------------------------------

	@Test
	@DisplayName("plusAll() unions two disjoint collections")
	void plusAllUnionsDisjointSets() {
		final AtomArray left = AtomArray.of(ROOT, ELEMENT);
		final AtomArray right = AtomArray.of(ARROW, NODE);

		final AtomArray merged = left.plusAll(right);
		assertEquals(4, merged.size());
		assertTrue(merged.contains(ROOT));
		assertTrue(merged.contains(ELEMENT));
		assertTrue(merged.contains(ARROW));
		assertTrue(merged.contains(NODE));
		for (int i = 0; i + 1 < merged.size(); i++)
			assertTrue(merged.get(i).compareTo(merged.get(i + 1)) < 0, "not sorted at index " + i);
	}

	@Test
	@DisplayName("plusAll() dedups atoms shared by both collections")
	void plusAllDedupsSharedAtoms() {
		final AtomArray left = AtomArray.of(ROOT, ELEMENT, ARROW);
		final AtomArray right = AtomArray.of(ARROW, NODE);

		final AtomArray merged = left.plusAll(right);
		assertEquals(4, merged.size());
	}

	@Test
	@DisplayName("plusAll() with an empty argument returns this unchanged")
	void plusAllWithEmptyOtherIsNoOp() {
		final AtomArray atoms = AtomArray.of(ROOT, ELEMENT);
		assertSame(atoms, atoms.plusAll(AtomArray.empty()));
	}

	@Test
	@DisplayName("plusAll() called on an empty collection returns the other collection")
	void plusAllOnEmptyReturnsOther() {
		final AtomArray other = AtomArray.of(ROOT, ELEMENT);
		assertSame(other, AtomArray.empty().plusAll(other));
	}

	@Test
	@DisplayName("plusAll() mixing SName and stereotype atoms merges both families correctly")
	void plusAllMixesNameAndStereotypeAtoms() {
		final AtomArray left = AtomArray.of(ROOT, ELEMENT, FOO);
		final AtomArray right = AtomArray.of(ARROW, BAR);

		final AtomArray merged = left.plusAll(right);
		assertEquals(5, merged.size());
		assertTrue(merged.contains(FOO));
		assertTrue(merged.contains(BAR));
	}

	// -----------------------------------------------------------------------
	// contains() / get()
	// -----------------------------------------------------------------------

	@Test
	@DisplayName("contains() is false for an atom that was never added")
	void containsIsFalseForAbsentAtom() {
		final AtomArray atoms = AtomArray.of(ROOT, ELEMENT);
		assertFalse(atoms.contains(ARROW));
	}

	@Test
	@DisplayName("get() returns atoms in the same order the iterator does")
	void getMatchesIterationOrder() {
		final AtomArray atoms = AtomArray.of(NODE, ROOT, ARROW, ELEMENT);
		int i = 0;
		for (StyleAtom atom : atoms)
			assertEquals(atoms.get(i++), atom);
		assertEquals(atoms.size(), i);
	}

	// -----------------------------------------------------------------------
	// iterator()
	// -----------------------------------------------------------------------

	@Test
	@DisplayName("iterator() throws once exhausted")
	void iteratorThrowsWhenExhausted() {
		final Iterator<StyleAtom> it = AtomArray.of(ROOT).iterator();
		assertTrue(it.hasNext());
		it.next();
		assertFalse(it.hasNext());
		assertThrows(NoSuchElementException.class, it::next);
	}

	@Test
	@DisplayName("iterator() on an empty collection has no elements")
	void iteratorOnEmptyHasNoElements() {
		assertFalse(AtomArray.empty().iterator().hasNext());
	}

	// -----------------------------------------------------------------------
	// equals() / hashCode()
	// -----------------------------------------------------------------------

	@Test
	@DisplayName("equals()/hashCode() agree for the same set built via different paths")
	void equalsIsOrderAndPathIndependent() {
		final AtomArray a = AtomArray.of(ROOT, ELEMENT, ARROW);
		final AtomArray b = AtomArray.of(ARROW, ROOT).plus(ELEMENT);
		final AtomArray c = AtomArray.of(ROOT).plusAll(AtomArray.of(ARROW, ELEMENT));

		assertEquals(a, b);
		assertEquals(a, c);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a.hashCode(), c.hashCode());
	}

	@Test
	@DisplayName("equals() is false for different sets, including a strict subset")
	void equalsIsFalseForDifferentSets() {
		final AtomArray a = AtomArray.of(ROOT, ELEMENT, ARROW);
		final AtomArray subset = AtomArray.of(ROOT, ELEMENT);
		final AtomArray other = AtomArray.of(ROOT, ELEMENT, NODE);

		assertNotEquals(a, subset);
		assertNotEquals(a, other);
		assertNotEquals(a, "not an AtomArray");
	}

}
