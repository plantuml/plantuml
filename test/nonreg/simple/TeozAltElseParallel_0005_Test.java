package nonreg.simple;

import nonreg.BasicTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
!pragma teoz true

participant "Random Name" as foo

hide footbox

foo -> foo : test

& opt message received
    alt REQUEST
        bossrpcp -> bossrpcp : request
    else RESPONSE
        bossrpcp -> bossrpcp : respond
    end
end

@enduml
"""

 */
public class TeozAltElseParallel_0005_Test extends BasicTest {

	@Test
	void testIssue591() throws IOException {
		checkImage("(2 participants)");
	}

}
