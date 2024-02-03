package net.sourceforge.plantuml.tim.stdlib;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.IndicativeSentencesGeneration;
import org.junit.jupiter.api.RepeatedTest;

import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.EaterExceptionLocated;
import net.sourceforge.plantuml.tim.TFunction;
import net.sourceforge.plantuml.tim.expression.TValue;

/**
 * Tests the builtin function.
 */
@IndicativeSentencesGeneration(separator = ": ", generator = ReplaceUnderscores.class)

class RandomFunctionTest {
	TFunction cut = new RandomFunction();
	final String cutName = "Random";
	final String repetitionLabel = "[{currentRepetition}/{totalRepetitions}] ";

	@RepeatedTest(value = 10, name = repetitionLabel + cutName + "()")
	void test_with_no_argument() throws EaterException, EaterExceptionLocated {
		final TValue tValue = cut.executeReturnFunction(null, null, null, Collections.emptyList(), null);
		assertThat(tValue.toInt()).isIn(0, 1);
	}

	@RepeatedTest(value = 10, name = repetitionLabel + cutName + "(7)")
	void test_with_one_argument() throws EaterException, EaterExceptionLocated {
		final TValue tValue = cut.executeReturnFunction(null, null, null, Arrays.asList(TValue.fromInt(7)), null);
		assertThat(tValue.toInt()).isBetween(0, 7-1);
	}

	@RepeatedTest(value = 10, name = repetitionLabel + cutName + "(0, 7)")
	void test_with_two_argument_first_zero() throws EaterException, EaterExceptionLocated {
		final TValue tValue = cut.executeReturnFunction(null, null, null, Arrays.asList(TValue.fromInt(0), TValue.fromInt(7)), null);
		assertThat(tValue.toInt()).isBetween(0, 7-1);
	}

	@RepeatedTest(value = 10, name = repetitionLabel + cutName + "(3, 7)")
	void test_with_two_argument() throws EaterException, EaterExceptionLocated {
		final TValue tValue = cut.executeReturnFunction(null, null, null, Arrays.asList(TValue.fromInt(3), TValue.fromInt(7)), null);
		assertThat(tValue.toInt()).isBetween(3, 7-1);
	}
}