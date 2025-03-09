package nonreg.simple;

import nonreg.BasicTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
!pragma teoz true
'skinparam style strictuml

Actor Mallory as m
Actor Bob as b
Actor Alice as a

activate b
b ->> a --++ : Hi Alice!
a ->> b --++ : Bye Bob!

b ->> a ++ : Bye Alice!
& b ->> m --++ : Bye Alice!

deactivate a
deactivate m
@enduml
"""

 */
public class TeozTimelineIssues_0001_Test extends BasicTest {

	@Test
	void testIssue1494() throws IOException {
		checkImage("(3 participants)");
	}

}
