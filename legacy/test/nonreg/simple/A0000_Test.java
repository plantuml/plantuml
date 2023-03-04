package nonreg.simple;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import nonreg.BasicTest;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
Alice -> Bob : Hello
@enduml
"""

 */
public class A0000_Test extends BasicTest {

	@Test
	void testSimple() throws IOException {
		checkImage("(2 participants)");
	}

}
