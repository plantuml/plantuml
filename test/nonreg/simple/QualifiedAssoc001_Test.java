package nonreg.simple;

import nonreg.BasicTest;

import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Diagram under test (has to be in tripple quotes """):

"""
@startuml
class HashMap<K,V>
    HashMap [a1] <|-u-> [e] V1
    HashMap [b2] *.r.> [f] V2
    HashMap [c3] o.d.> [g] V3
    HashMap [d4] +-l-> [h] V4

    MoreComplex [x: String] ..> X
    MoreComplex [y: int] --> Y
    MoreComplex [z: boolean] ..> Z
@enduml
"""

*/

public class QualifiedAssoc001_Test extends BasicTest {

	@Test
	void testIssue1491() throws IOException {
		checkImage("(9 entities)");
	}

}
