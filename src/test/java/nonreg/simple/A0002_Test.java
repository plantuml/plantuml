package nonreg.simple;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import nonreg.BasicTest;

/*

"""
@startuml
<style>
activityDiagram {
  note {
    MaximumWidth 100
  }
}
</style>
|Actor 1|
start
:foo1;
note right
  A Long Long Long Long Long Long Long Long Long Long Long Long Long Long note
  This note is on several
  //lines// and can
  contain <b>HTML</b>
  ====
  * Calling the method ""foo()"" is <back:red>prohibited overlap
end note
floating note left: This is a note
|Actor 2|
:foo2;
note right
  <back:red> KO for this note
  A Long Long Long Long Long Long Long Long Long Long Long Long Long Long note
  This note is on several
  //lines// and can
  contain <b>HTML</b>
  ====
  * Calling the method ""foo()"" is prohibited
end note
stop

@enduml
"""

 */
public class A0002_Test extends BasicTest {

	@Test
	void testSimple() throws IOException {
		checkImage("activity3");
	}
}
