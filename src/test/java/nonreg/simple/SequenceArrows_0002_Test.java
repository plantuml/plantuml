package nonreg.simple;

import nonreg.BasicTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
!pragma teoz true
participant Jim as j
participant Alice as a
participant Bob   as b
participant Tom as c
activate j
activate a
activate b
activate a
activate b
activate b
activate b
a ->     b : ""->   ""
a ->>    b : ""->>  ""
a -\     b : ""-\   ""
a -\\    b : ""-\\\\""
a -/     b : ""-/   ""
a -//    b : ""-//  ""
a ->x    b : ""->x  ""
a x->    b : ""x->  ""
a o->    b : ""o->  ""
a ->o    b : ""->o  ""
a o->o   b : ""o->o ""
a <->    b : ""<->  ""
a o<->o  b : ""o<->o""
a x<->x  b : ""x<->x""
a ->>o   b : ""->>o ""
a -\o    b : ""-\o  ""
a -\\o   b : ""-\\\\o""
a -/o    b : ""-/o  ""
a -//o   b : ""-//o ""
a x->o   b : ""x->o ""

a <-     b : ""<-  ""
a <<-    b : ""<<-  ""
a /-     b : ""/-   ""
a //-    b : ""//-""
a \-     b : ""\\-""
a \\-    b : ""\\\\-""
a x<-    b : ""x<-  ""
a <-x    b : ""<-x  ""
a <-o    b : ""<-o  ""
a o<-    b : ""o<-  ""
a o<-o   b : ""o<-o ""
a <->    b : ""<->  ""
a o<->o  b : ""o<->o""
a x<->x  b : ""x<->x""
a o<<-   b : ""o<<- ""
a o/-    b : ""o/-  ""
a o//-   b : ""o//-""
a o\-    b : ""o\\-  ""
a o\\-   b : ""o\\\\-""
a o<-x   b : ""o<-x ""

a ->     a : ""->   ""
a ->>    a : ""->>  ""
a -\     a : ""-\   ""
a -\\    a : ""-\\\\""
a -/     a : ""-/   ""
a -//    a : ""-//  ""
a o->    a : ""o->  ""
a ->o    a : ""->o  ""
a o->o   a : ""o->o ""
a <->    a : ""<->  ""
a o<->o  a : ""o<->o""
a ->>o   a : ""->>o ""
a -\o    a : ""-\o  ""
a -\\o   a : ""-\\\\o""
a -/o    a : ""-/o  ""
a -//o   a : ""-//o ""

b <-     b : ""<-""
b o<-     b : ""o<-""
b o<-o     b : ""o<-o""
b <<-    b : ""<<-""
b /-     b : ""/-""
b //-    b : ""//""
b \-     b : ""\-""
b \\-    b : ""\\\\-""
b <-o    b : ""<-o""
b o<-    b : ""o<-""
b o<-o   b : ""o<-o""
b o<<-   b : ""o<<-""
b o/-    b : ""o/-""
b o//-   b : ""o//-""
b o\-    b : ""o\\-""
b o\\-   b : ""o\\\\-""

a <->    a : "" <->""
a o<->o  a : "" o<->o""
a x<->x  a : ""  x<->x""
a x->o   a : ""x->o ""
a ->x    a : ""->x  ""
a x->    a : ""x->  ""
a x<-    a : ""x<-""
a <-X    a : ""<-x""
a o<-x   a : "" o<-x ""
b x<-    b : ""x<-""
b <-X    b : ""<-x""
b o<-x   b : "" o<-x ""

[->      a : ""[->   ""
[->>     a : ""[->>  ""
[-\      a : ""[-\   ""
[-\\     a : ""[-\\\\""
[-/      a : ""[-/   ""
[-//     a : ""[-//  ""
[->x     a : ""[->x  ""
[x->     a : ""[x->  ""
[o->     a : ""[o->  ""
[->o     a : ""[->o  ""
[o->o    a : ""[o->o ""
[<->     a : ""[<->  ""
[o<->o   a : ""[o<->o""
[x<->x   a : ""[x<->x""
[->>o    a : ""[->>o ""
[-\o     a : ""[-\o  ""
[-\\o    a : ""[-\\\\o""
[-/o     a : ""[-/o  ""
[-//o    a : ""[-//o ""
[x->o    a : ""[x->o ""

a ->]      : ""->]   ""
a ->>]     : ""->>]  ""
a -\]      : ""-\]   ""
a -\\]     : ""-\\\\]""
a -/]      : ""-/]   ""
a -//]     : ""-//]  ""
a ->x]     : ""->x]  ""
a x->]     : ""x->]  ""
a o->]     : ""o->]  ""
a ->o]     : ""->o]  ""
a o->o]    : ""o->o] ""
a <->]     : ""<->]  ""
a o<->o]   : ""o<->o]""
a x<->x]   : ""x<->x]""
a ->>o]    : ""->>o] ""
a -\o]     : ""-\o]  ""
a -\\o]    : ""-\\\\o]""
a -/o]     : ""-/o]  ""
a -//o]    : ""-//o] ""
a x->o]    : ""x->o] ""
@enduml
"""

 */
public class SequenceArrows_0002_Test extends BasicTest {

	@Test
	void testIssue1683() throws IOException {
		checkImage("(4 participants)");
	}

}
