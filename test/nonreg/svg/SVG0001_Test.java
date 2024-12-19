package nonreg.svg;

import org.junit.jupiter.api.Test;

import java.io.IOException;

/*
Test diagram MUST be put between triple quotes

"""
@startuml
!pragma svginteractive false
actor my_actor
boundary my_boundary
collections my_collections
control my_control
database my_database
entity my_entity
participant my_participant
queue my_queue
my_actor -> my_database: Do something
my_database --> my_actor: Acknowledge it
@enduml
"""

Expected result MUST be put between triple brackets

{{{
<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" contentStyleType="text/css" data-diagram-type="SEQUENCE" preserveAspectRatio="none" version="1.1" zoomAndPan="magnify">
  <defs/>
  <g>
    <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5.0,5.0;"/>
    <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5.0,5.0;"/>
    <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5.0,5.0;"/>
    <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5.0,5.0;"/>
    <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5.0,5.0;"/>
    <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5.0,5.0;"/>
    <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5.0,5.0;"/>
    <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5.0,5.0;"/>
    <g>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_actor</text>
      <ellipse fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <path fill="none" style="stroke:#181818;stroke-width:0.5;"/>
    </g>
    <g>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_actor</text>
      <ellipse fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <path fill="none" style="stroke:#181818;stroke-width:0.5;"/>
    </g>
    <g>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_boundary</text>
      <path fill="none" style="stroke:#181818;stroke-width:0.5;"/>
      <ellipse fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
    </g>
    <g>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_boundary</text>
      <path fill="none" style="stroke:#181818;stroke-width:0.5;"/>
      <ellipse fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
    </g>
    <g>
      <rect fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <rect fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_collections</text>
    </g>
    <g>
      <rect fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <rect fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_collections</text>
    </g>
    <g>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_control</text>
      <ellipse fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <polygon fill="#181818" style="stroke:#181818;stroke-width:1;"/>
    </g>
    <g>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_control</text>
      <ellipse fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <polygon fill="#181818" style="stroke:#181818;stroke-width:1;"/>
    </g>
    <g>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_database</text>
      <path fill="#E2E2F0" style="stroke:#181818;stroke-width:1.5;"/>
      <path fill="none" style="stroke:#181818;stroke-width:1.5;"/>
    </g>
    <g>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_database</text>
      <path fill="#E2E2F0" style="stroke:#181818;stroke-width:1.5;"/>
      <path fill="none" style="stroke:#181818;stroke-width:1.5;"/>
    </g>
    <g>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_entity</text>
      <ellipse fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <line style="stroke:#181818;stroke-width:0.5;"/>
    </g>
    <g>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_entity</text>
      <ellipse fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <line style="stroke:#181818;stroke-width:0.5;"/>
    </g>
    <g>
      <rect fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_participant</text>
    </g>
    <g>
      <rect fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_participant</text>
    </g>
    <g>
      <path fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <path fill="none" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_queue</text>
    </g>
    <g>
      <path fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <path fill="none" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_queue</text>
    </g>
    <g>
      <polygon fill="#181818" style="stroke:#181818;stroke-width:1;"/>
      <line style="stroke:#181818;stroke-width:1;"/>
      <text fill="#000000" font-family="sans-serif" font-size="13" lengthAdjust="spacing">Do something</text>
    </g>
    <g>
      <polygon fill="#181818" style="stroke:#181818;stroke-width:1;"/>
      <line style="stroke:#181818;stroke-width:1;stroke-dasharray:2.0,2.0;"/>
      <text fill="#000000" font-family="sans-serif" font-size="13" lengthAdjust="spacing">Acknowledge it</text>
    </g>
  </g>
</svg>
}}}

 */
public class SVG0001_Test extends SvgTest {

	@Test
	void testSequenceDiagramHasGroupedParticipantsWithClasses() throws IOException {
		checkXmlAndDescription("(8 participants)");
	}

}
