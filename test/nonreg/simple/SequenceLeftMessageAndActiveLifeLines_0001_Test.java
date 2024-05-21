package nonreg.simple;

import nonreg.BasicTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
!pragma teoz true
skin rose
skinparam sequence {
ArrowColor Green
}
'Participant Test
'Participant Testing
'Participant Test2

Testing <- Testing #red: 1st self message, no activation
    note left
      A note on the self message
    endnote
Testing <- Testing ++: 2nd self message, starting activation
Testing <- Testing : 2.5th self message, no change
    note right
      A note on the self message
    endnote
Testing \\-- Testing ++: 3rd self message, staring another activation
Testing <<- Testing : 4th self message, continuing in the current activation
    note left
      A note on the self message
    endnote
Testing <- Testing --: 5th self message, deactivating once

Testing <- Testing : 6th self message, continuing the activation
Testing <- Testing --: 7th self message, exiting the activation
Testing <- Testing : 8th self message, no activation
@enduml
"""

 */
public class SequenceLeftMessageAndActiveLifeLines_0001_Test extends BasicTest {

	@Test
	void testIssue1683() throws IOException {
		checkImage("(1 participants)");
	}

}
