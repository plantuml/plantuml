package nonreg.simple;

import nonreg.BasicTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
skinparam {
   Maxmessagesize 200
}

activate Test
Test <<-- Test : the arrow and text are located inside the Lifeline because they are counted from the right side of the active member's column bar (Lifeline). Which is an incorrect display, right?
Test <<-- Test : also the arrow is not displayed correctly (issue: #1678). (I wonder if the closing of the Lifeline is displayed correctly? Should it also include the arrow before it, i.e. close after it? If not, how do I close the Life Line after the last arrow?)
deactivate Test

@enduml
"""

 */
public class SequenceLeftMessageAndActiveLifeLines_0003_Test extends BasicTest {

	@Test
	void testIssue1683() throws IOException {
		checkImage("(1 participants)");
	}

}
