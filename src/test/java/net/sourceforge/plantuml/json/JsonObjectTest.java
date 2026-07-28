package net.sourceforge.plantuml.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.IndicativeSentencesGeneration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@IndicativeSentencesGeneration(separator = ": ", generator = ReplaceUnderscores.class)
class JsonObjectTest {

    private JsonObject object;

    @BeforeEach
    void setUp() {
        object = new JsonObject();
    }

	@Nested
	class Merge_Test {
        @Test
        void merge_fails_With_Null() {
            assertEquals("object is null", assertThrows(NullPointerException.class, () -> object.merge(null)).getMessage());
        }

        @Test
        void merge_appends_Members() {
            object.add("a", 1).add("b", 1);
            object.merge(Json.object().add("c", 2).add("d", 2));

            assertEquals(Json.object().add("a", 1).add("b", 1).add("c", 2).add("d", 2), object);
        }

        @Test
        void merge_replaces_Members() {
            object.add("a", 1).add("b", 1).add("c", 1);
            object.merge(Json.object().add("b", 2).add("d", 2));

            assertEquals(Json.object().add("a", 1).add("b", 2).add("c", 1).add("d", 2), object);
        }

        @Test
        void merge_replaces_Members_With_no_deep() {
            object.add("a", 1).add("b", Json.object().add("x", 1).add("y", 1)).add("c", Json.object().add("A", 1));
            object.merge(Json.object().add("b", Json.object().add("y", 2).add("z", 1)).add("c", 1)
                    .add("d", Json.object().add("B", 1)));

            assertEquals(Json.object().add("a", 1).add("b", Json.object().add("y", 2).add("z", 1)).add("c", 1)
                    .add("d", Json.object().add("B", 1)), object);
        }
    }

	@Nested
	class DeepMerge_Test {
        @Test
        void deepMerge_fails_With_Null() {
            assertEquals("object is null", assertThrows(NullPointerException.class, () -> object.deepMerge(null)).getMessage());
        }

        @Test
        void deepMerge_appends_Members() {
            object.add("a", 1).add("b", 1);
            object.deepMerge(Json.object().add("c", 2).add("d", 2));

            assertEquals(Json.object().add("a", 1).add("b", 1).add("c", 2).add("d", 2), object);
        }

        @Test
        void deepMerge_replaces_Members() {
            object.add("a", 1).add("b", 1).add("c", 1);
            object.deepMerge(Json.object().add("b", 2).add("d", 2));

            assertEquals(Json.object().add("a", 1).add("b", 2).add("c", 1).add("d", 2), object);
        }

        @Test
        void deepMerge_merges_Member_Object() {
            object.add("a", 1).add("b", Json.object().add("x", 1).add("y", 1)).add("c", Json.object().add("A", 1));
            object.deepMerge(Json.object().add("b", Json.object().add("y", 2).add("z", 1)).add("c", 1)
                    .add("d", Json.object().add("B", 1)));

            assertEquals(Json.object().add("a", 1).add("b", Json.object().add("x", 1).add("y", 2).add("z", 1)).add("c", 1)
                    .add("d", Json.object().add("B", 1)), object);
        }
    }
}
