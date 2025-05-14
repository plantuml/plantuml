package net.sourceforge.plantuml.openiconic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class StringDecipherTest {

	@ParameterizedTest(name = "[{index}] input=\"{0}\" ⇒ expected=\"{1}\"")
	@CsvSource({ "'C10,10,20,20,30,30', '[C, 10, 10, 20, 20, 30, 30]'", //
			"'M-10,-20L-30,40', '[M, -10, -20, L, -30, 40]'", //
			"'.1.2.3', '[.1, .2, .3]'", //
			"'C10.5,20e-1,30', '[C, 10.5, 20e-1, 30]'", //
			"'L.5-.5', '[L, .5, -.5]'", //
			"'L.5e+2-.25', '[L, .5e+2, -.25]'", //
			"'-1.2e3.4e5.6', '[-1.2e3, .4e5, .6]'", //
			"'M0-1.0E-2,3', '[M, 0, -1.0E-2, 3]'", //
			"'10-20.30e2', '[10, -20.30e2]'" //
	})

	void test1(String input, String expected) {
		assertEquals(expected, StringDecipher.decipher(input).toString());
	}

}