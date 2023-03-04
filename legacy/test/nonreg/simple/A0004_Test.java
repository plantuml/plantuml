package nonreg.simple;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import nonreg.BasicTest;

/*

"""
@startuml

(*) --> VerifyReservation

if "" then
  -> [incorrect] sendToAirport
  --> (*)
else
  --> [correct] getPreference
  --> ===Y1===
endif

if "" then
  -->[nobagage] ===Y2===
else
  -->[bagage] ReceiveBaggage
  --> ===Y2===
endif

===Y1=== --> PrintBoadingboard
--> ===Y2===

--> GiveTravelDocumentation
--> (*)

@enduml

"""

 */
public class A0004_Test extends BasicTest {

	@Test
	void testSimple() throws IOException {
		checkImage("(12 activities)");
	}
}
