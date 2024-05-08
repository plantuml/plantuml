package nonreg.simple;

import nonreg.BasicTest;

import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Diagram under test (has to be in tripple quotes """):

"""
@startuml
hide stereotype

class C <<foo>> <<bar>>

rectangle foo <<foo>> <<boo>> {
    class B
}

package bar <<boo>> {
    class A
}
@enduml
"""

*/

public class HideShow003_Test extends BasicTest {

	@Test
	void testIssue1768() throws IOException {
		checkImage("(3 entities)");
	}

}
