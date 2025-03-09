package nonreg.simple;

import nonreg.BasicTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
component A
component B
component C
component D

A -up-> B : > up arrow **missing**
B <-down- A : < up arrow works
B -right-> C : > right arrow works
C -down-> D : > down arrow works
D -left-> A : > left arrow **missing**
A <-right- D : < left arrow works

@enduml
"""

 */
public class ComponentExtraArrows_0001_Test extends BasicTest {

	@Test
	void testFormumIssue18816() throws IOException {
		checkImage("(4 entities)");
	}

}
