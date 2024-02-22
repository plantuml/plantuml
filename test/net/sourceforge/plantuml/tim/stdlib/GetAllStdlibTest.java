package net.sourceforge.plantuml.tim.stdlib;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.IndicativeSentencesGeneration;
import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.tim.EaterExceptionLocated;
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
	void Test_without_Param() throws EaterExceptionLocated {
		final List<TValue> empty = new ArrayList<>();
		final TValue tValue = cut.executeReturnFunction(null, null, null, empty, null);
		assertThat(tValue.toString()).contains("archimate", "aws", "tupadr3");
	}

	@Test
	void Test_with_one_argument() throws EaterExceptionLocated {
		final TValue tValue = cut.executeReturnFunction(null, null, null, Arrays.asList(TValue.fromInt(0)), null);
		assertThat(tValue.toString()).contains("archimate", "https://github.com/plantuml-stdlib/Archimate-PlantUML");
	}
}
