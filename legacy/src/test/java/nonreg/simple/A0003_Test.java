package nonreg.simple;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import nonreg.BasicTest;

/*

"""
@startgantt
scale 2
printscale weekly
2020/10/26 to 2020/11/01 are colored in salmon
sunday are closed
saturday are closed

Project starts the 2020-10-15
[Prototype design] as [TASK1] lasts 25 days
[TASK1] is colored in Lavender/LightBlue
[Testing] lasts 5 days
[TASK1]->[Testing]
@endgantt
"""

 */
public class A0003_Test extends BasicTest {

	@Test
	void testSimple() throws IOException {
		checkImage("(Project)");
	}
}
