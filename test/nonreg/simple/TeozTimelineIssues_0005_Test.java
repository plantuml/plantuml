package nonreg.simple;

import nonreg.BasicTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
!pragma teoz true
dummy -> A ++: first step

A -> A: test
A -> B --++: second step

B -> B: test
B -> C--: third step

@enduml
"""

 */
public class TeozTimelineIssues_0005_Test extends BasicTest {

	@Test
	void testIssue1723() throws IOException {
		checkImage("(4 participants)");
	}

}
