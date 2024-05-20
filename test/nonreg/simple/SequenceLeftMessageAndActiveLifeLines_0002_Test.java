package nonreg.simple;

import nonreg.BasicTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
'!pragma teoz true
'skinparam sequence {
'ArrowColor Green
'}
participant Bob as b
participant Alice as a
activate a
activate b
a <-     a
a o<-     a
a o<-o     a
a <<-    a
a /-     a
a //-    a
a \-     a
a \\-    a
activate b
a <-o    a
a o<-    a
activate b
a o<-o   a
a o<<-   a
activate b
a o/-    a
a o//-   a
a o\-    a
a o\\-   a
@enduml
"""

 */
public class SequenceLeftMessageAndActiveLifeLines_0002_Test extends BasicTest {

	@Test
	void testIssue1683() throws IOException {
		checkImage("(2 participants)");
	}

}
