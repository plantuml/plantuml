package nonreg.scxml;

import java.io.IOException;

import org.junit.jupiter.api.Test;

/*


Test diagram MUST be put between triple quotes

"""
@startuml
state s1
state s2
[*] --> s1

s1 --> s2 : play
@enduml
"""

Expected result MUST be put between triple brackets

{{{
<?xml version="1.0" encoding="UTF-8"?><scxml xmlns="http://www.w3.org/2005/07/scxml" initial="start" version="1.0">
    <state id="s1">
        <transition event="play" target="s2"/>
    </state>
    <state id="s2"/>
    <state id="start">
        <transition target="s1"/>
    </state>
</scxml>
}}}

 */
public class SCXML0001_Test extends ScXmlTest {

	@Test
	void testSimple() throws IOException {
		checkXmlAndDescription("(3 entities)");
	}

}
