package net.sourceforge.plantuml;


import io.github.glytching.junit.extension.random.Random;
import io.github.glytching.junit.extension.random.RandomBeansExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@Extensions({
        @ExtendWith(MockitoExtension.class),
        @ExtendWith(RandomBeansExtension.class)
})
class LineLocationImplTest {
    @Mock
    private LineLocation parent;

    @Mock
    private LineLocation parent2;

    @Random
    private String desc;
    @Random
    private String desc2;

    @Test
    void ctorDestArgMustNotBeNull() {
        // no lambdas in 1.6 ;(
        assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                new LineLocationImpl(desc, null);
            }
        });

        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                new LineLocationImpl(null, null);
            }
        });
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
}
