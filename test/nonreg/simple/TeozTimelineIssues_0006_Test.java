package nonreg.simple;

import nonreg.BasicTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
!pragma teoz true
'Issue #656
A -> B++: Get data
& A -> C++:
& A -> D++:
B -->> A--: Data
& C -->> A--:
& D -->> A--:
@enduml
"""

 */
public class TeozTimelineIssues_0006_Test extends BasicTest {

	@Test
	void testIssue656() throws IOException {
		checkImage("(4 participants)");
	}

}
