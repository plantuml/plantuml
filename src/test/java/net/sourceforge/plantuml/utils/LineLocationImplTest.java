package net.sourceforge.plantuml.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.glytching.junit.extension.random.Random;
import io.github.glytching.junit.extension.random.RandomBeansExtension;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@ExtendWith(RandomBeansExtension.class)
class LineLocationImplTest {

    private final LineLocation parent = new FakeLineLocation();
    private final LineLocation parent2 = new FakeLineLocation();

    @Random
    private String desc;
    @Random
    private String desc2;

    @Test
    void ctorDestArgMustNotBeNull() {
        assertDoesNotThrow(() -> new LineLocationImpl(desc, null));
        assertThrows(NullPointerException.class, () -> new LineLocationImpl(null, null));
    }

    @Test
    void initialPositionIsNegativeOne() {
        LineLocation loc = new LineLocationImpl(desc, null);
        assertEquals(-1, loc.getPosition());
    }

    @Test
    void parentSameAsProvided() {
        LineLocation loc = new LineLocationImpl(desc, parent);
        assertSame(parent, loc.getParent());
    }

    @Test
    void descriptionSameAsProvided() {
        LineLocation loc = new LineLocationImpl(desc, parent);
        assertEquals(desc, loc.getDescription());
    }

    @Test
    void toStringIsAConcatenationofDescAndPosition() {
        LineLocationImpl loc = new LineLocationImpl(desc, parent);
        assertEquals(desc + " : -1", loc.toString());
        assertEquals(desc + " : 0", loc.oneLineRead().toString());
    }

    @Test
    void oneLineReadMovesToNextLine() {
        LineLocationImpl loc = new LineLocationImpl(desc, parent);

        for (int i = 1; i < 5; ++i) {
            LineLocationImpl next = loc.oneLineRead();
            assertEquals(desc, next.getDescription());
            assertSame(parent, next.getParent());
            assertEquals(-1 + i, next.getPosition());
            loc = next;
        }
    }

    @Test
    void comparisonStandardLibraryAlwaysDifferent() {
        LineLocationImpl loc1 = new LineLocationImpl(desc, parent);
        LineLocationImpl loc2 = new LineLocationImpl("<" + desc2, parent);

        assertEquals(1, loc1.compareTo(loc2));
        assertEquals(-1, loc2.compareTo(loc1));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "<"})
    void comparisonDisregardsDescription(String prefix) {
        LineLocationImpl loc1 = new LineLocationImpl(prefix + desc, parent);
        LineLocationImpl loc2 = new LineLocationImpl(prefix + desc2, parent);

        assertEquals(0, loc1.compareTo(loc2));
        assertEquals(0, loc2.compareTo(loc1));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "<"})
    void comparisonDisregardsParent(String prefix) {
        LineLocationImpl loc1 = new LineLocationImpl(prefix + desc, parent);
        LineLocationImpl loc2 = new LineLocationImpl(prefix + desc, parent2);

        assertEquals(0, loc1.compareTo(loc2));
        assertEquals(0, loc2.compareTo(loc1));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "<"})
    void comparisonLooksAtPositionOnly(String prefix) {
        LineLocationImpl loc1 = new LineLocationImpl(prefix + desc, parent);
        LineLocationImpl loc2 = loc1.oneLineRead();
        LineLocationImpl loc3 = loc2.oneLineRead();

        assertEquals(-1, loc1.compareTo(loc2));
        assertEquals(1, loc2.compareTo(loc1));

        assertEquals(-2, loc1.compareTo(loc3));
        assertEquals(2, loc3.compareTo(loc1));
    }

    private static final class FakeLineLocation implements LineLocation {

        @Override
        public int getPosition() {
            return 0;
        }

        @Override
        public String getDescription() {
            return "";
        }

        @Override
        public LineLocation getParent() {
            return null;
        }

        @Override
        public int compareTo(LineLocation other) {
            return 0;
        }

    }
}
