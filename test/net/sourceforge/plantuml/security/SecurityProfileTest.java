package net.sourceforge.plantuml.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Checks some aspects in {@link SecurityUtils}.
 */
class SecurityProfileTest {

	
	@ParameterizedTest
	@CsvSource(
			value = {
					// SecurityProfile  variable, expected result
					"  SANDBOX,   path.separator, true",
					"  ALLOWLIST, path.separator, true",
					"  INTERNET,  path.separator, true",
					"  LEGACY,    path.separator, true",
					"  UNSECURE,  path.separator, true",

					"  SANDBOX,   line.separator, true",
					"  ALLOWLIST, line.separator, true",
					"  INTERNET,  line.separator, true",
					"  LEGACY,    line.separator, true",
					"  UNSECURE,  line.separator, true",
					
					"  SANDBOX,   plantuml.security.foo, false",
					"  ALLOWLIST, plantuml.security.foo, false",
					"  INTERNET,  plantuml.security.foo, false",
					"  LEGACY,    plantuml.security.foo, false",
					"  UNSECURE,  plantuml.security.foo, false",

					"  SANDBOX,   plantuml.dummy, true",
					"  ALLOWLIST, plantuml.dummy, true",
					"  INTERNET,  plantuml.dummy, true",
					"  LEGACY,    plantuml.dummy, true",
					"  UNSECURE,  plantuml.dummy, true",

					"  SANDBOX,   custom.name, false",
					"  ALLOWLIST, custom.name, false",
					"  INTERNET,  custom.name, false",
					"  LEGACY,    custom.name, false",
					"  UNSECURE,  custom.name, true",

			},
			nullValues = { "NULL" })
	void testCanWeReadThisEnvironmentVariable(String profileName, String variableName, String expected) {
		SecurityProfile profile = SecurityProfile.valueOf(profileName);
		if ("true".equalsIgnoreCase(expected))
			assertTrue(profile.canWeReadThisEnvironmentVariable(variableName));
		else
			assertFalse(profile.canWeReadThisEnvironmentVariable(variableName));
	}

}
