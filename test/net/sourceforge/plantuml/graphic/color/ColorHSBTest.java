package net.sourceforge.plantuml.graphic.color;

import static java.lang.Long.parseLong;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ColorHSBTest {

	@ParameterizedTest
	@CsvSource({
			" AA000000 , ColorHSB[a=AA r=00 g=00 b=00 / h=0.000000 s=0.000000 b=0.000000] ",
			" AAFF0000 , ColorHSB[a=AA r=FF g=00 b=00 / h=0.000000 s=1.000000 b=1.000000] ",
			" AA00FF00 , ColorHSB[a=AA r=00 g=FF b=00 / h=0.333333 s=1.000000 b=1.000000]",
			" AA0000FF , ColorHSB[a=AA r=00 g=00 b=FF / h=0.666667 s=1.000000 b=1.000000] ",
			" FFFF8080 , ColorHSB[a=FF r=FF g=80 b=80 / h=0.000000 s=0.498039 b=1.000000] ",
			" FF7F0000 , ColorHSB[a=FF r=7F g=00 b=00 / h=0.000000 s=1.000000 b=0.498039] ",
	})
	void test_toString(String argb, String expectedToString) {
		assertThat(new ColorHSB((int) parseLong(argb, 16)))
				.hasToString(expectedToString);
	}
}
