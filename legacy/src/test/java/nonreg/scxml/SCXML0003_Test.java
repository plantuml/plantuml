package nonreg.scxml;

import java.io.IOException;

import org.junit.jupiter.api.Test;

/*


https://github.com/plantuml/plantuml/issues/1101


Test diagram MUST be put between triple quotes

"""
@startuml

state module {
state Somp {
  state entry1 <<inputPin>>
  state entry2 <<inputPin>>
  state sin
  sin -> sin2
}

state flop{
  state sig_in <<inputPin>>
  state sig_ff <<outputPin>>
  state flop_0: sig_ff := 0
  state flop_1: sig_ff := 1
  [*] -> flop_0
  flop_0 -> flop_1 : sig_in
  
}

state counter{
  state count_start <<inputPin>>
  state count_done <<outputPin>>  
  state "count_val[3:0]" <<outputPin>>  
  [*] -> count_idle
  count_idle --> count_ongoing: count_start
  state count_idle: count_val := 0
  state count_ongoing: count_val := count_val +1
  count_ongoing -> count_finish: count_val != MAX_VAL
  state count_finish: count_done:=1
  count_finish -> count_idle

}
state ex <<inputPin>>
state exitAx <<inputPin>>

exitAx --> entry1
sig_ff -> entry2 : "!" 


}
@enduml
"""

Expected result MUST be put between triple brackets

{{{
<?xml version="1.0" encoding="UTF-8"?><scxml xmlns="http://www.w3.org/2005/07/scxml" initial="startflop" version="1.0">
    <state id="module">
        <state id="ex" stereotype="inputPin"/>
        <state id="exitAx" stereotype="inputPin">
            <transition target="entry1"/>
        </state>
        <state id="Somp">
            <state id="entry1" stereotype="inputPin"/>
            <state id="entry2" stereotype="inputPin"/>
            <state id="sin">
                <transition target="sin2"/>
            </state>
            <state id="sin2"/>
        </state>
        <state id="flop">
            <state id="sig_in" stereotype="inputPin"/>
            <state id="sig_ff" stereotype="outputPin">
                <transition event="&quot;!&quot;" target="entry2"/>
            </state>
            <state id="flop_0">
                <transition event="sig_in" target="flop_1"/>
            </state>
            <state id="flop_1"/>
            <state id="startflop">
                <transition target="flop_0"/>
            </state>
        </state>
        <state id="counter">
            <state id="count_start" stereotype="inputPin"/>
            <state id="count_done" stereotype="outputPin"/>
            <state id="count_val[3:0]" stereotype="outputPin"/>
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
    </state>
</scxml>
}}}
 */
public class SCXML0003_Test extends ScXmlTest {

	@Test
	void testSimple() throws IOException {
		checkXmlAndDescription("(18 entities)");
	}

}
