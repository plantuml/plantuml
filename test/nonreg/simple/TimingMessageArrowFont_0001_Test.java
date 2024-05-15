package nonreg.simple;

import nonreg.BasicTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Test diagram MUST be put between triple quotes

"""
@startuml

skinparam defaultFontName "SansSerif"
skinparam defaultFontSize 10
skinparam defaultFontColor green

robust "DNS Resolver" as DNS
robust "Web Browser" as WB
concise "Web User" as WU

@0
WU is Idle
WB is Idle
DNS is Idle

@+100
WU -> WB : URL
WU is Waiting
WB is Processing

@+200
WB is Waiting
WB -> DNS@+50 : Resolve URL

@+100
DNS is Processing

@+300
DNS is Idle

@WU
@200 <-> @+150 : {150 ms}
@enduml
"""

 */
public class TimingMessageArrowFont_0001_Test extends BasicTest {

	@Test
	void testIssue1746() throws IOException {
		checkImage("(Timing Diagram)");
	}

}
