package nonreg.simple;

import nonreg.BasicTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
!pragma teoz true
'Issue 1099
  A <[#red]-o? ++
  A -> B -- : very long\nmulti-line
& B -> C ++ #red: single
note over C: got it
Deactivate C
====
  A <-o? ++ #green
  A -> B -- : very long\nmulti-line
& B -> C ++ #green : same\nheight
note over C: got it
Deactivate C
====
  A <-o? ++ #red
  'activate A
  A -> B -- : single
& B -> C ++ : very long\nmulti-line
note over C: got it
Deactivate C
@enduml
"""

 */
public class TeozTimelineIssues_0007_Test extends BasicTest {

	@Test
	void testIssue1099() throws IOException {
		checkImage("(3 participants)");
	}

}
