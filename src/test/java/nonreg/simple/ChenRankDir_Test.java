package nonreg.simple;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import nonreg.BasicTest;

/*

Test diagram MUST be put between triple quotes

"""
@startchen
left to right direction

entity Person {
}
entity Location {
}
relationship Birthplace {
}

Person -N- Birthplace
Birthplace -1- Location

@endchen
"""

 */
public class ChenRankDir_Test extends BasicTest {

	@Test
	void testSimple() throws IOException {
		checkImage("(3 entities)");
	}

}
