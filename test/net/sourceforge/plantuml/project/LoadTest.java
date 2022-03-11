package net.sourceforge.plantuml.project;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class LoadTest {

	private static final int SAMPLE_VALUE = 10;

	@Test
	void isValueObject() {
		Assertions.assertThat(Load.inWinks(SAMPLE_VALUE))
			.isEqualTo(Load.inWinks(SAMPLE_VALUE));
	}
}