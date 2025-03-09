package nonreg.scxml;

import java.io.IOException;

import org.junit.jupiter.api.Test;

/*


https://github.com/plantuml/plantuml/issues/1111


Test diagram MUST be put between triple quotes

"""
@startuml

state module {
}

note as PARAMETERS
localparam MAX_VAL 10
parameter COUNT_WIDTH 4
end note


@enduml
"""

Expected result MUST be put between triple brackets

{{{
<?xml version="1.0" encoding="UTF-8"?><scxml xmlns="http://www.w3.org/2005/07/scxml" version="1.0">
    <state id="PARAMETERS" stereotype="note">
        <!--localparam MAX_VAL 10
parameter COUNT_WIDTH 4-->
    </state>
    <state id="module"/>
</scxml>
}}}
 */
public class SCXML0005_Test extends ScXmlTest {

	@Test
	void testSimple() throws IOException {
		checkXmlAndDescription("(1 entities)");
	}

}
