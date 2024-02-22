package net.sourceforge.plantuml.tim.stdlib;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
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

class GetAllThemeTest {
	TFunction cut = new GetAllTheme();
	final String cutName = "GetAllTheme";

	@Test
	void Test_without_Param() throws EaterExceptionLocated {
		final List<TValue> empty = new ArrayList<>();
		final TValue tValue = cut.executeReturnFunction(null, null, null, empty, null);
		assertThat(tValue.toString()).contains("_none_", "amiga", "vibrant");
	}
}
