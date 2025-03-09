package nonreg.simple;

import nonreg.BasicTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
!pragma teoz true
Bob -> Alice : message
& note right of Alice: ok
Alice -[hidden]> Alice

activate Bob
Bob -> Alice --: deactivate
& note right of Alice: ok

Bob -> Alice ++: activate
& note right of Alice: ok
deactivate Alice

activate Bob
Bob -> Alice --++: act+deact
& note right of Alice: not ok
deactivate Alice
@enduml
"""

 */
public class TeozTimelineIssues_0004_Test extends BasicTest {

	@Test
	void testIssueForum15191() throws IOException {
		checkImage("(2 participants)");
	}

}
