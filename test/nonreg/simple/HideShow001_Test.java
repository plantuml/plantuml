package nonreg.simple;

import nonreg.BasicTest;

import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Diagram under test (has to be in tripple quotes """):

"""
@startuml
    hide <<focus>> stereotype
    class Access <<Entity>> <<focus>>
@enduml
"""

*/

public class HideShow001_Test extends BasicTest {

	@Test
	void testIssue1580() throws IOException {
		checkImage("(1 entities)");
	}

}
