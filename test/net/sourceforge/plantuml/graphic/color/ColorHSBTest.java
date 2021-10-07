package net.sourceforge.plantuml.graphic.color;

import static java.awt.Color.BLUE;
import static java.awt.Color.GREEN;
import static java.awt.Color.RED;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ColorHSBTest {

	@Test
	void test_toString() {
		assertThat(new ColorHSB(0xAA000000))
				.hasToString("ColorHSB[a=AA r=00 g=00 b=00 / h=0.000000 s=0.000000 b=0.000000]");

		assertThat(new ColorHSB(RED))
				.hasToString("ColorHSB[a=FF r=FF g=00 b=00 / h=0.000000 s=1.000000 b=1.000000]");

		assertThat(new ColorHSB(GREEN))
				.hasToString("ColorHSB[a=FF r=00 g=FF b=00 / h=0.333333 s=1.000000 b=1.000000]");

		assertThat(new ColorHSB(BLUE))
				.hasToString("ColorHSB[a=FF r=00 g=00 b=FF / h=0.666667 s=1.000000 b=1.000000]");

		assertThat(new ColorHSB(0xFFFF8080))
				.hasToString("ColorHSB[a=FF r=FF g=80 b=80 / h=0.000000 s=0.498039 b=1.000000]");

		assertThat(new ColorHSB(0xFF7F0000))
				.hasToString("ColorHSB[a=FF r=7F g=00 b=00 / h=0.000000 s=1.000000 b=0.498039]");
	}
}
