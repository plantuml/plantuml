package net.sourceforge.plantuml.utils;

import static net.sourceforge.plantuml.utils.MathUtils.roundUp;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MathUtilsTest {

	@Test
	void test_roundUp_double() {
		assertThat(roundUp(0.0))
				.isEqualTo(0);

		assertThat(roundUp(1.0))
				.isEqualTo(1);

		assertThat(roundUp(1.000000000000001))
				.isEqualTo(2);
	}

	@Test
	void test_roundUp_float() {
		assertThat(roundUp(0f))
				.isEqualTo(0);

		assertThat(roundUp(1f))
				.isEqualTo(1);

		assertThat(roundUp(1.0000001f))
				.isEqualTo(2);
	}
}
