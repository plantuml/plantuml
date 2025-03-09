package nonreg.simple;

import nonreg.BasicTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
!pragma teoz true
autonumber
AAA -> BBB++: Message 0
BBB -[#22A722]> DDD: Message 1
deactivate BBB
activate DDD
& DDD -> EEE: Message 2
note right #red: <-- Expect 1 & 2 to be same row
DDD -[#22A722]> FFF++--: Msg 3
& FFF -> GGG--: Msg 4
note right #red: <-- Expect 3 & 4 to be same row
@enduml
"""

 */
public class TeozTimelineIssues_0002_Test extends BasicTest {

	@Test
	void testIssue739() throws IOException {
		checkImage("(6 participants)");
	}

}
