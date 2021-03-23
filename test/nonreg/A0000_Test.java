package nonreg;

import java.io.File;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/*

@startuml
Alice -> Bob : Hello
@enduml


 */
class A0000_Test {

	@Test
	void testSimple() throws IOException {
		final File source = new File("test/nonreg", getClass().getSimpleName() + ".java");
		assertTrue(source.exists());
		assertTrue(source.canRead());
	}

}
