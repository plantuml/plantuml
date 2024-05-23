package nonreg.simple;

import nonreg.BasicTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
!pragma teoz true
activate b
b -> a --++ #red : hello
deactivate a
b -> a : hello2
activate a #green
deactivate a
b -> a ++ #green: hello3
@enduml
"""

 */
public class TeozTimelineIssues_0003_Test extends BasicTest {

	@Test
	void testIssueForum13409() throws IOException {
		checkImage("(2 participants)");
	}

}
