package nonreg.scxml;

import java.io.IOException;

import org.junit.jupiter.api.Test;

/*


https://github.com/plantuml/plantuml/issues/1100


Test diagram MUST be put between triple quotes

"""
@startuml
state counter{
state count_start
state count_done
state "count_val[3:0]"
[*] -> count_idle
count_idle --> count_ongoing: count_start
state count_idle: count_val := 0
state count_ongoing: count_val := count_val +1
count_ongoing -> count_finish: count_val != MAX_VAL
state count_finish: count_done:=1
count_finish -> count_idle
}
@enduml
"""

Expected result MUST be put between triple brackets

{{{
<?xml version="1.0" encoding="UTF-8"?><scxml xmlns="http://www.w3.org/2005/07/scxml" initial="startcounter" version="1.0">
    <state id="counter">
        <state id="count_start"/>
        <state id="count_done"/>
        <state id="count_val[3:0]"/>
        <state id="startcounter">
            <transition target="count_idle"/>
        </state>
        <state id="count_idle">
            <transition event="count_start" target="count_ongoing"/>
        </state>
        <state id="count_ongoing">
            <transition event="count_val != MAX_VAL" target="count_finish"/>
        </state>
        <state id="count_finish">
            <transition target="count_idle"/>
        </state>
    </state>
</scxml>
}}}
 */
public class SCXML0002_Test extends ScXmlTest {

	@Test
	void testSimple() throws IOException {
		checkXmlAndDescription("(7 entities)");
	}

}
