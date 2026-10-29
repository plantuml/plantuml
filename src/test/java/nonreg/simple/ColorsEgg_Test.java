package nonreg.simple;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import nonreg.BasicTest;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
colors
@enduml
"""

 */
public class ColorsEgg_Test extends BasicTest {

	@Test
	void testSimple() throws IOException {
		checkImage("(Colors)");
	}

}
