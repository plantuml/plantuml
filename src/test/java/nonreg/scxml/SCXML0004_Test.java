package nonreg.scxml;

import java.io.IOException;

import org.junit.jupiter.api.Test;

/*


https://github.com/plantuml/plantuml/issues/1101


Test diagram MUST be put between triple quotes

"""
@startuml

state module {
	state flop
	state Somp {
	  state entry1 <<inputPin>>
	  state entry2 <<inputPin>>
	  state sin
	  sin -> sin2
	}
}
@enduml
"""

Expected result MUST be put between triple brackets

{{{
<?xml version="1.0" encoding="UTF-8"?><scxml xmlns="http://www.w3.org/2005/07/scxml" version="1.0">
    <state id="module">
        <state id="flop"/>
        <state id="Somp">
            <state id="entry1" stereotype="inputPin"/>
            <state id="entry2" stereotype="inputPin"/>
            <state id="sin">
                <transition target="sin2"/>
            </state>
            <state id="sin2"/>
        </state>
    </state>
</scxml>
}}}
 */
public class SCXML0004_Test extends ScXmlTest {

	@Test
	void testSimple() throws IOException {
		checkXmlAndDescription("(5 entities)");
	}

}
