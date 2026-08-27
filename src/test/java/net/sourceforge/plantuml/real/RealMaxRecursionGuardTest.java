package net.sourceforge.plantuml.real;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

// Non-regression for plantuml/plantuml#2834: the RealMax recursion guard used
// to capture a JVM stack trace (new Throwable().fillInStackTrace()) on every
// loop iteration purely to measure how deep the call stack had grown. That
// was replaced with a cheap thread-local depth counter (see RealMax), and
// this test pins that the guard still fires for a genuinely cyclic Real graph
// -- and, separately, that an ordinary (non-cyclic, if deep) max/min chain is
// unaffected.
class RealMaxRecursionGuardTest {

	// A Real whose delegate can be wired up after construction, needed to
	// build a genuine cycle: normal Real graphs are wired once at construction
	// time and cannot reference themselves.
	private static class MutableRealHolder implements Real {

		private Real delegate;

		void setDelegate(Real delegate) {
			this.delegate = delegate;
		}

		public double getCurrentValue() {
			return delegate.getCurrentValue();
		}

		public String getName() {
			return "holder";
		}

		public Real addFixed(double delta) {
			throw new UnsupportedOperationException();
		}

		public Real addAtLeast(double delta) {
			throw new UnsupportedOperationException();
		}

		public void ensureBiggerThan(Real other) {
			throw new UnsupportedOperationException();
		}

		public void printCreationStackTrace() {
		}

	}

	@Test
	void a_cyclic_max_still_throws_infinite_recursion() {
		final RealOrigin origin = RealUtils.createOrigin();
		final MutableRealHolder holder = new MutableRealHolder();
		final Real cyclicMax = RealUtils.max(origin, holder);
		// Close the cycle: evaluating the holder re-enters cyclicMax before it
		// has produced (and cached) a value, forever.
		holder.setDelegate(cyclicMax);

		final IllegalStateException ex = assertThrows(IllegalStateException.class, cyclicMax::getCurrentValue);
		assertEquals("Infinite recursion?", ex.getMessage());
	}

	@Test
	void a_deep_but_non_cyclic_max_chain_still_resolves() {
		// A long (but finite, non-cyclic) chain of nested max()s -- deeper than
		// any real diagram nests them (a 500-arrow sequence diagram measures
		// only a handful of levels), but still well under the guard's
		// threshold -- must not trip it: only genuine re-entrancy should.
		final int depth = 500;
		Real current = RealUtils.createOrigin();
		for (int i = 0; i < depth; i++)
			current = RealUtils.max(current, current.addFixed(1));

		assertEquals((double) depth, current.getCurrentValue());
	}

}
