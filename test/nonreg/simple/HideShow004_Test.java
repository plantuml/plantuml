package nonreg.simple;

import nonreg.BasicTest;

import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Diagram under test (has to be in tripple quotes """):

"""
@startuml
    ' hide all attributes and methods...
    hide members

    '... but show the methods in class B
    show B methods

    class A {
        - name: String
        + id: long
        # doSomething(): void
    }
    class B {
        ~run(): boolean
    }
    class C
    class D
@enduml
"""

*/

public class HideShow004_Test extends BasicTest {

	@Test
	void testHideShowClassMembers() throws IOException {
		checkImage("(4 entities)");
	}

}
