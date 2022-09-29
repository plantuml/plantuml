package net.sourceforge.plantuml.project.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static net.sourceforge.plantuml.project.time.Month.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class MonthTest {

	@ParameterizedTest
	@MethodSource("months")
	void monthExposesJavaType(Month plantUmlMonth, java.time.Month javaTimeMonth) {
		assertThat(plantUmlMonth.getJavaTimeMonth()).isEqualTo(javaTimeMonth);
	}

	static Stream<Arguments> months() {
		return Stream.<Arguments>builder()
			.add(of(JANUARY, java.time.Month.JANUARY))
			.add(of(FEBRUARY, java.time.Month.FEBRUARY))
			.add(of(MARCH, java.time.Month.MARCH))
			.add(of(APRIL, java.time.Month.APRIL))
			.add(of(MAY, java.time.Month.MAY))
			.add(of(JUNE, java.time.Month.JUNE))
			.add(of(JULY, java.time.Month.JULY))
			.add(of(AUGUST, java.time.Month.AUGUST))
			.add(of(SEPTEMBER, java.time.Month.SEPTEMBER))
			.add(of(OCTOBER, java.time.Month.OCTOBER))
			.add(of(NOVEMBER, java.time.Month.NOVEMBER))
			.add(of(DECEMBER, java.time.Month.DECEMBER))
			.build();
	}
}