package net.sourceforge.plantuml.json;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

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
            assertThatNullPointerException().isThrownBy(() -> object.merge(null)).withMessage("object is null");
        }

        @Test
        void merge_appends_Members() {
            object.add("a", 1).add("b", 1);
            object.merge(Json.object().add("c", 2).add("d", 2));

            assertThat(object).isEqualTo(Json.object().add("a", 1).add("b", 1).add("c", 2).add("d", 2));
        }

        @Test
        void merge_replaces_Members() {
            object.add("a", 1).add("b", 1).add("c", 1);
            object.merge(Json.object().add("b", 2).add("d", 2));

            assertThat(object).isEqualTo(Json.object().add("a", 1).add("b", 2).add("c", 1).add("d", 2));
        }

        @Test
        void merge_replaces_Members_With_no_deep() {
            object.add("a", 1).add("b", Json.object().add("x", 1).add("y", 1)).add("c", Json.object().add("A", 1));
            object.merge(Json.object().add("b", Json.object().add("y", 2).add("z", 1)).add("c", 1)
                    .add("d", Json.object().add("B", 1)));

            assertThat(object).isEqualTo(Json.object().add("a", 1).add("b", Json.object().add("y", 2).add("z", 1)).add("c", 1)
                    .add("d", Json.object().add("B", 1)));
        }
    }

	@Nested
	class DeepMerge_Test {
        @Test
        void deepMerge_fails_With_Null() {
            assertThatNullPointerException().isThrownBy(() -> object.deepMerge(null)).withMessage("object is null");
        }

        @Test
        void deepMerge_appends_Members() {
            object.add("a", 1).add("b", 1);
            object.deepMerge(Json.object().add("c", 2).add("d", 2));

            assertThat(object).isEqualTo(Json.object().add("a", 1).add("b", 1).add("c", 2).add("d", 2));
        }

        @Test
        void deepMerge_replaces_Members() {
            object.add("a", 1).add("b", 1).add("c", 1);
            object.deepMerge(Json.object().add("b", 2).add("d", 2));

            assertThat(object).isEqualTo(Json.object().add("a", 1).add("b", 2).add("c", 1).add("d", 2));
        }

        @Test
        void deepMerge_merges_Member_Object() {
            object.add("a", 1).add("b", Json.object().add("x", 1).add("y", 1)).add("c", Json.object().add("A", 1));
            object.deepMerge(Json.object().add("b", Json.object().add("y", 2).add("z", 1)).add("c", 1)
                    .add("d", Json.object().add("B", 1)));

            assertThat(object).isEqualTo(Json.object().add("a", 1).add("b", Json.object().add("x", 1).add("y", 2).add("z", 1)).add("c", 1)
                    .add("d", Json.object().add("B", 1)));
        }
    }
}
