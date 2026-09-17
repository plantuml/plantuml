package net.sourceforge.plantuml.klimt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Keeps {@link UShapeKind} and the {@link UShape#getShapeKind()} overrides from drifting apart.
 * They are two halves of one mapping -- the enum is what a driver is registered under, the
 * override is what a shape is drawn by -- and a mismatch would not fail to compile: it would
 * silently send a shape to the wrong driver, or to none.
 */
class UShapeKindTest {

	@Test
	@DisplayName("every kind but UNKNOWN names a distinct shape class, and of() finds it back")
	void kindsAndTypesAgree() {
		final Set<Class<?>> seen = new HashSet<>();
		for (UShapeKind kind : UShapeKind.values()) {
			if (kind == UShapeKind.UNKNOWN) {
				assertEquals(null, kind.getType());
				continue;
			}
			assertNotEquals(null, kind.getType(), kind + " has no shape class");
			assertTrue(seen.add(kind.getType()), kind.getType() + " is claimed by two kinds");
			assertSame(kind, UShapeKind.of(kind.getType()));
		}
	}

	@Test
	@DisplayName("every kind's shape class overrides getShapeKind(), and returns that kind")
	void everyShapeClassOverridesGetShapeKind() throws Exception {
		for (UShapeKind kind : UShapeKind.values()) {
			if (kind == UShapeKind.UNKNOWN)
				continue;

			final Class<? extends UShape> type = kind.getType();
			final Method method = type.getMethod("getShapeKind");
			assertSame(type, method.getDeclaringClass(),
					type.getSimpleName() + " does not override getShapeKind()");
		}
	}

	@Test
	@DisplayName("UNKNOWN is the default, and sits at index 0 so an empty array means no driver")
	void unknownIsTheDefault() {
		assertEquals(0, UShapeKind.UNKNOWN.ordinal());
		assertEquals(UShapeKind.values().length, UShapeKind.COUNT);
		assertSame(UShapeKind.UNKNOWN, new UShape() {
		}.getShapeKind());
	}

	@Test
	@DisplayName("of() rejects a class no driver is registered for")
	void ofRejectsUnknownClass() {
		assertThrows(IllegalArgumentException.class, () -> UShapeKind.of(UShape.class));
	}

}
