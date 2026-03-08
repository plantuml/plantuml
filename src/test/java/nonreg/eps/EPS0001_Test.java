package nonreg.eps;

import java.io.IOException;

import org.junit.jupiter.api.Test;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
Alice->Bob : hello
@enduml
"""

*/
public class EPS0001_Test extends EpsTest {

	@Test
	void testSimpleSequence() throws IOException {
		checkEpsAndDescription("(2 participants)");
	}
}
