package nonreg.simple;

import nonreg.BasicTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
!pragma teoz true
    opt
        A->B: message
    end
    & opt
        C->D: message
    end

    alt
        A->B: message
    end
    & alt
        C->D: message
    end

    alt
        A->B: message
    else default
        B->B: other
    end
    & alt
        C->D: message
    else default
        D->D: other
    end
    B -[hidden]-> C: "              "
@enduml
"""

 */
public class TeozAltElseParallel_0004_Test extends BasicTest {

	@Test
	void testIssue269b() throws IOException {
		checkImage("(4 participants)");
	}

}
