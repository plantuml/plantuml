package nonreg.simple;

import nonreg.BasicTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
!pragma teoz true
'Issue #656 -- modified
A -> B++: Get data
& B -> C++:a\n2
& C -> D++:
B -->> A--: Data
& C -->> A--:
& D -->> C--:a\n2
@enduml
"""

 */
public class TeozTimelineIssues_0008_Test extends BasicTest {

	@Test
	void testIssue656b() throws IOException {
		checkImage("(4 participants)");
	}

}
