package net.sourceforge.plantuml.tim.stdlib;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.EaterExceptionLocated;
import net.sourceforge.plantuml.tim.expression.TValue;

/**
 * Tests the internal function %getenv.
 */
class GetenvTest {

	/**
	 * Tests getenv should not publish plantuml.security.* environment variables.
	 *
	 * @throws EaterException should not
	 * @throws EaterExceptionLocated should not
	 */
	@ParameterizedTest
	@ValueSource(strings = {
			"plantuml.security.blabla",
			"plantuml.SECURITY.blabla",
			"plantuml.security.credentials.path",
	})
	void executeReturnFunctionSecurityTest(String name) throws EaterException, EaterExceptionLocated {
		System.setProperty("plantuml.security.blabla", "example");
		Getenv cut = new Getenv();

		List<TValue> values = Collections.singletonList(TValue.fromString(name));
		TValue tValue = cut.executeReturnFunction(null, null, null, values, null);
		assertThat (tValue.toString()).isEmpty();
	}

	/**
	 * Tests getenv still returns 'good' variables.
	 *
	 * @throws EaterException should not
	 * @throws EaterExceptionLocated should not
	 */
	@ParameterizedTest
	@ValueSource(strings = {
			"path.separator",
			"line.separator",
	})
	void executeReturnFunctionTest(String name) throws EaterException, EaterExceptionLocated {
		Getenv cut = new Getenv();

		List<TValue> values = Collections.singletonList(TValue.fromString(name));
		TValue tValue = cut.executeReturnFunction(null, null, null, values, null);
		assertThat (tValue.toString()).isNotEmpty();
	}
}