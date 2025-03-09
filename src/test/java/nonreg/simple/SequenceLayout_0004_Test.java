package nonreg.simple;

import nonreg.BasicTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
!pragma teoz true
participant A
participant B
participant C
participant D
participant E
group
A->? : M1
?->E : M2 [Grouped]
end
@enduml
"""

 */
public class SequenceLayout_0004_Test extends BasicTest {

	@Test
	void testIssueForumIssue18832() throws IOException {
		checkImage("(5 participants)");
	}

}
