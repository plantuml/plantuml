package nonreg.simple;

import nonreg.BasicTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
!pragma teoz true

group #ffa Group 1
    Particpant_A -> Particpant_B
    activate Particpant_A
    Particpant_A <- Particpant_B
    deactivate Particpant_A
end

group #ffa Group 2
    Particpant_A -> Particpant_B++
    Particpant_A <- Particpant_B--
end

group #ffa Group 3
    Particpant_A -> Particpant_B++
    activate Particpant_A
    Particpant_A <- Particpant_B--
    deactivate Particpant_A
end

group #ffa Group 3b
    Particpant_A -> Particpant_B++
    activate Particpant_A
        Particpant_A -> Particpant_B++
                Particpant_A -> Particpant_B++
                        Particpant_A -> Particpant_B++
                        Particpant_A <- Particpant_B--
                Particpant_A <- Particpant_B--
        Particpant_A <- Particpant_B--
    Particpant_A <- Particpant_B--
    deactivate Particpant_A
end

group #ffa Group 3b2
    Particpant_A -> Particpant_B++
    activate Particpant_A
    Particpant_A <- Particpant_B
    Particpant_A -> Particpant_B !!
    deactivate Particpant_A
end

group #ffa Group 3b3
    Particpant_A -> Particpant_B++
    activate Particpant_A
    Particpant_A <- Particpant_B !!
    deactivate Particpant_B
end

group #ffa Group 3c
    Particpant_A -> Particpant_B++
    activate Particpant_A
        Particpant_B -> Particpant_A++
                Particpant_B -> Particpant_A++
                                Particpant_B -> Particpant_A++
                                Particpant_B <- Particpant_A--
                Particpant_B <- Particpant_A--
        Particpant_B <- Particpant_A--
    Particpant_A <- Particpant_B--
    deactivate Particpant_A
end


group #ffa Group 4
    Particpant_A -> Particpant_B
    Particpant_A <- Particpant_B
end
@enduml
"""

 */
public class TeozTimelineIssues_0009_Test extends BasicTest {

	@Test
	void testIssue1789() throws IOException {
		checkImage("(2 participants)");
	}

}
