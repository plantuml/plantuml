package nonreg.simple;

import nonreg.BasicTest;

import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Diagram under test (has to be in tripple quotes """):

"""
@startuml
<style>
root {
    .highlight {
        BackGroundColor LightGray
    }
}
</style>

hide <<highlight>> stereotype

rectangle "System in focus" <<Module>> <<highlight>> {
    class Class
    class ImportantClass <<highlight>>
    class ClassThreadSafe <<thread-safe>> <<Singleton>>
    class ImportantClassThreadSafe <<thread-safe>> <<highlight>>
}

package other <<Stereotype A>> <<Stereotype B>> {
    class ExternalClassThreadSafe <<thread-safe>> <<external>>
    class ExternalImportantClass <<external>> <<highlight>>
}
@enduml
"""

*/

public class HideShow002_Test extends BasicTest {

	@Test
	void testIssue1735() throws IOException {
		checkImage("(6 entities)");
	}

}
