package nonreg.simple;

import nonreg.BasicTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
!pragma teoz true

'!theme crt-amber
'skinparam backgroundColor #000000

participant "Random Name" as foo

hide footbox

'foo -> foo : test
  opt message received
    alt REQUEST
        bossrpcp -> bossrpcp : request
    else RESPONSE
        bossrpcp -> bossrpcp : respond
    else AGAIN
     bossrpcp -> bossrpcp : request
    end
end
& foo -> foo : test

@enduml
"""

 */
public class TeozAltElseParallel_0001_Test extends BasicTest {

	@Test
	void testIssue591b() throws IOException {
		checkImage("(2 participants)");
	}

}
