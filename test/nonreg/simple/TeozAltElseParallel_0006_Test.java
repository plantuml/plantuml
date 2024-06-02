package nonreg.simple;

import nonreg.BasicTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
!pragma teoz true
 [-> foo : test
& Bob -> Charlie
& par
     Alice -> Bob: Authentication Request
'& Charlie-> Bob: Authentication Request
end
& foo -> Alice : test

@enduml
"""

 */
public class TeozAltElseParallel_0006_Test extends BasicTest {

	@Test
	void testIssueGroupsInParallelMoreThan2() throws IOException {
		checkImage("(4 participants)");
	}

}
