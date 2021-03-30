package nonreg.simple;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import nonreg.BasicTest;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
title title
legend legend
footer footer
header header
caption caption
<style>
    document {
       BackGroundColor orange
    }
    title {
       BackGroundColor yellow
    }
    legend {
       BackGroundColor green
    }
    footer {
       BackGroundColor blue
       FontColor red
       FontSize 15
    }
    header {
       BackGroundColor red
    }
    caption {
       BackGroundColor purple
    }
</style>

class Bob {
hello
}
class Sally

Sally --> Bob
@enduml
"""

 */
public class A0005_Test extends BasicTest {

	@Test
	void testSimple() throws IOException {
		checkImage("(2 entities)");
	}

}
