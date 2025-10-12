package net.sourceforge.plantuml.syntax;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdErr;
import org.junitpioneer.jupiter.StdIo;

class LanguageDescriptorTest {

	@Test
	@StdIo
	void testGetObfuscate(StdErr err) {
		new LanguageDescriptor().getObfuscate();
		// Lazy test to check that there are no warning
		assertEquals("", err.capturedString());
	}

}
