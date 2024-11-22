package net.sourceforge.plantuml.tim.builtin;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.IndicativeSentencesGeneration;
import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.TFunction;
import net.sourceforge.plantuml.tim.expression.TValue;

/**
 * Tests the builtin function.
 */
@IndicativeSentencesGeneration(separator = ": ", generator = ReplaceUnderscores.class)

class GetAllStdlibTest {
	TFunction cut = new GetAllStdlib();
	final String cutName = "GetAllStdlib";

	@Test
	void Test_without_Param() throws EaterException {
		final List<TValue> empty = Collections.emptyList();
		final TValue tValue = cut.executeReturnFunction(null, null, null, empty, null);
		assertThat(tValue.toString()).contains("archimate", "aws", "tupadr3");
	}

	@Test
	void Test_with_one_argument() throws EaterException {
		final TValue tValue = cut.executeReturnFunction(null, null, null, Arrays.asList(TValue.fromInt(0)), null);
		assertThat(tValue.toString()).contains("archimate", "https://github.com/plantuml-stdlib/Archimate-PlantUML");
	}
}
