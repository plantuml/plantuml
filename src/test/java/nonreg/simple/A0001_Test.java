package nonreg.simple;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import nonreg.BasicTest;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
Bob->Bob: hello1
activate Bob
Bob->Bob: hello2
destroy Bob
Bob->Bob: this is an\nexample of long\nmessage
Bob->Alice: And this\nis an other on\nvery long too
@enduml
"""

 */
public class A0001_Test extends BasicTest {

	@Test
	void testSimple() throws IOException {
		checkImage("(2 participants)");
	}

}
