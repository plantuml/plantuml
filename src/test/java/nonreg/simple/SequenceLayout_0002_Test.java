package nonreg.simple;

import nonreg.BasicTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
group
    a -> a : This works fine
end
group
    a <- a : This [now works]
end
@enduml
"""

 */
public class SequenceLayout_0002_Test extends BasicTest {

	@Test
	void testIssue839() throws IOException {
		checkImage("(1 participants)");
	}

}
