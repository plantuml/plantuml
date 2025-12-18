package nonreg.svg;

import org.junit.jupiter.api.Test;

import java.io.IOException;

/*
Test diagram MUST be put between triple quotes

"""
@startuml
actor my_actor
boundary my_boundary
collections my_collections
control my_control
database my_database
entity my_entity
participant my_participant
queue my_queue
my_actor -> my_database: Do something
activate my_actor
my_database --> my_actor: Acknowledge it
deactivate my_actor
@enduml
"""

Expected result MUST be put between triple brackets

{{{
<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" contentStyleType="text/css" data-diagram-type="SEQUENCE" preserveAspectRatio="none" version="1.1" zoomAndPan="magnify">
  <defs/>
  <g>
    <g>
      <title>my_actor</title>
      <rect fill="#FFFFFF" style="stroke:#181818;stroke-width:1;"/>
    </g>
    <g class="participant-lifeline" data-entity-uid="part1" data-qualified-name="my_actor" id="part1-lifeline">
      <g>
        <title>my_actor</title>
        <rect fill="#000000" fill-opacity="0.00000"/>
        <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5,5;"/>
      </g>
    </g>
    <g class="participant-lifeline" data-entity-uid="part2" data-qualified-name="my_boundary" id="part2-lifeline">
      <g>
        <title>my_boundary</title>
        <rect fill="#000000" fill-opacity="0.00000"/>
        <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5,5;"/>
      </g>
    </g>
    <g class="participant-lifeline" data-entity-uid="part3" data-qualified-name="my_collections" id="part3-lifeline">
      <g>
        <title>my_collections</title>
        <rect fill="#000000" fill-opacity="0.00000"/>
        <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5,5;"/>
      </g>
    </g>
    <g class="participant-lifeline" data-entity-uid="part4" data-qualified-name="my_control" id="part4-lifeline">
      <g>
        <title>my_control</title>
        <rect fill="#000000" fill-opacity="0.00000"/>
        <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5,5;"/>
      </g>
    </g>
    <g class="participant-lifeline" data-entity-uid="part5" data-qualified-name="my_database" id="part5-lifeline">
      <g>
        <title>my_database</title>
        <rect fill="#000000" fill-opacity="0.00000"/>
        <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5,5;"/>
      </g>
    </g>
    <g class="participant-lifeline" data-entity-uid="part6" data-qualified-name="my_entity" id="part6-lifeline">
      <g>
        <title>my_entity</title>
        <rect fill="#000000" fill-opacity="0.00000"/>
        <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5,5;"/>
      </g>
    </g>
    <g class="participant-lifeline" data-entity-uid="part7" data-qualified-name="my_participant" id="part7-lifeline">
      <g>
        <title>my_participant</title>
        <rect fill="#000000" fill-opacity="0.00000"/>
        <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5,5;"/>
      </g>
    </g>
    <g class="participant-lifeline" data-entity-uid="part8" data-qualified-name="my_queue" id="part8-lifeline">
      <g>
        <title>my_queue</title>
        <rect fill="#000000" fill-opacity="0.00000"/>
        <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5,5;"/>
      </g>
    </g>
    <g class="participant participant-head" data-entity-uid="part1" data-qualified-name="my_actor" id="part1-head">
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_actor</text>
      <ellipse fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <path fill="none" style="stroke:#181818;stroke-width:0.5;"/>
    </g>
    <g class="participant participant-tail" data-entity-uid="part1" data-qualified-name="my_actor" id="part1-tail">
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_actor</text>
      <ellipse fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <path fill="none" style="stroke:#181818;stroke-width:0.5;"/>
    </g>
    <g class="participant participant-head" data-entity-uid="part2" data-qualified-name="my_boundary" id="part2-head">
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_boundary</text>
      <path fill="none" style="stroke:#181818;stroke-width:0.5;"/>
      <ellipse fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
    </g>
    <g class="participant participant-tail" data-entity-uid="part2" data-qualified-name="my_boundary" id="part2-tail">
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_boundary</text>
      <path fill="none" style="stroke:#181818;stroke-width:0.5;"/>
      <ellipse fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
    </g>
    <g class="participant participant-head" data-entity-uid="part3" data-qualified-name="my_collections" id="part3-head">
      <rect fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <rect fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_collections</text>
    </g>
    <g class="participant participant-tail" data-entity-uid="part3" data-qualified-name="my_collections" id="part3-tail">
      <rect fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <rect fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_collections</text>
    </g>
    <g class="participant participant-head" data-entity-uid="part4" data-qualified-name="my_control" id="part4-head">
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_control</text>
      <ellipse fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <polygon fill="#181818" style="stroke:#181818;stroke-width:1;"/>
    </g>
    <g class="participant participant-tail" data-entity-uid="part4" data-qualified-name="my_control" id="part4-tail">
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_control</text>
      <ellipse fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <polygon fill="#181818" style="stroke:#181818;stroke-width:1;"/>
    </g>
    <g class="participant participant-head" data-entity-uid="part5" data-qualified-name="my_database" id="part5-head">
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_database</text>
      <path fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <path fill="none" style="stroke:#181818;stroke-width:0.5;"/>
    </g>
    <g class="participant participant-tail" data-entity-uid="part5" data-qualified-name="my_database" id="part5-tail">
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_database</text>
      <path fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <path fill="none" style="stroke:#181818;stroke-width:0.5;"/>
    </g>
    <g class="participant participant-head" data-entity-uid="part6" data-qualified-name="my_entity" id="part6-head">
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_entity</text>
      <ellipse fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <line style="stroke:#181818;stroke-width:0.5;"/>
    </g>
    <g class="participant participant-tail" data-entity-uid="part6" data-qualified-name="my_entity" id="part6-tail">
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_entity</text>
      <ellipse fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <line style="stroke:#181818;stroke-width:0.5;"/>
    </g>
    <g class="participant participant-head" data-entity-uid="part7" data-qualified-name="my_participant" id="part7-head">
      <rect fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_participant</text>
    </g>
    <g class="participant participant-tail" data-entity-uid="part7" data-qualified-name="my_participant" id="part7-tail">
      <rect fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_participant</text>
    </g>
    <g class="participant participant-head" data-entity-uid="part8" data-qualified-name="my_queue" id="part8-head">
      <path fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <path fill="none" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_queue</text>
    </g>
    <g class="participant participant-tail" data-entity-uid="part8" data-qualified-name="my_queue" id="part8-tail">
      <path fill="#E2E2F0" style="stroke:#181818;stroke-width:0.5;"/>
      <path fill="none" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">my_queue</text>
    </g>
    <g>
      <title>my_actor</title>
      <rect fill="#FFFFFF" style="stroke:#181818;stroke-width:1;"/>
    </g>
    <g class="message" data-entity-1="part1" data-entity-2="part5" id="msg1">
      <polygon fill="#181818" style="stroke:#181818;stroke-width:1;"/>
      <line style="stroke:#181818;stroke-width:1;"/>
      <text fill="#000000" font-family="sans-serif" font-size="13" lengthAdjust="spacing">Do something</text>
    </g>
    <g class="message" data-entity-1="part5" data-entity-2="part1" id="msg2">
      <polygon fill="#181818" style="stroke:#181818;stroke-width:1;"/>
      <line style="stroke:#181818;stroke-width:1;stroke-dasharray:2,2;"/>
      <text fill="#000000" font-family="sans-serif" font-size="13" lengthAdjust="spacing">Acknowledge it</text>
    </g>
  </g>
</svg>
}}}

 */
public class SVG0001_Test extends SvgTest {

	@Test
	void testSequenceDiagramHasTitledLifelines() throws IOException {
		checkXmlAndDescription("(8 participants)");
	}

}
