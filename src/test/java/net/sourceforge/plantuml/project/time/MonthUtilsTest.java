package net.sourceforge.plantuml.project.time;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.plantuml.ubrex.UnicodeBracketedExpression;

class MonthUtilsTest {

	@Test
	void testGetUbrex() {
		final UnicodeBracketedExpression ubrex = MonthUtils.getUbrex();

		final String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December", "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT",
				"NOV", "DEC" };

		for (String month : months)
			assertTrue(ubrex.match(month, 0).startMatch(), month);

	}

	@Test
	void testGetUbrexKo() {
		final UnicodeBracketedExpression ubrex = MonthUtils.getUbrex();

		final String[] months = { "foo", "14January" };

		for (String month : months)
			assertFalse(ubrex.match(month, 0).startMatch(), month);

	}

}
