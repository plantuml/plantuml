package nonreg.simple;

import nonreg.BasicTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
Test --> Test: Text
note right: the location of the Comment is correct

Test --> Test: Text
note left: the location of the Comment is correct

Test <-- Test: Text
note right: the location of the Comment is [now correct]

Test <-- Test: Text
note left: the location of the Comment is [now correct]
@enduml
"""

 */
public class SequenceLayout_0003_Test extends BasicTest {

	@Test
	void testIssue1679() throws IOException {
		checkImage("(1 participants)");
	}

}
