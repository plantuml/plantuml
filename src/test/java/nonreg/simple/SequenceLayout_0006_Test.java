package nonreg.simple;

import nonreg.BasicTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
A -> B : a
note right: Note
activate B
B --> A : b
deactivate B
@enduml
"""

 */
public class SequenceLayout_0006_Test extends BasicTest {

	@Test
	void testTeozIssueFromPR1777c() throws IOException {
		checkImage("(2 participants)");
	}

}
