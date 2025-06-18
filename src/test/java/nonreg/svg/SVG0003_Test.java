package nonreg.svg;

import org.junit.jupiter.api.Test;

import java.io.IOException;

/*
Test diagram MUST be put between triple quotes

"""
@startuml
' The rendered groups with a <title> are
' for the swimlanes, so should always have
' `fill-opacity="0.00000"`

' Should render: `<rect fill="none" .../>`
participant transparent_keyword #transparent

' Should render: `<rect fill="none" .../>`
participant white_with_zero_alpha #FFFFFF00
participant black_with_zero_alpha #00000000
participant gray_with_zero_alpha #CCCCCC00

' Should render: `<rect fill="{color}" fill-opacity="0.00392" .../>`
participant white_with_low_alpha #FFFFFF01
participant black_with_low_alpha #00000001
participant gray_with_low_alpha #CCCCCC01

' Should render: `<rect fill="{color}" fill-opacity="0.99608" .../>`
participant white_with_high_alpha #FFFFFFFE
participant black_with_high_alpha #000000FE
participant gray_with_high_alpha #CCCCCCFE

' Should render: `<rect fill="{color}" .../>`
participant white_with_full_alpha #FFFFFFFF
participant black_with_full_alpha #000000FF
participant gray_with_full_alpha #CCCCCCFF

' Should render: `<rect fill="{color}" .../>`
participant white_with_unspecified_alpha #FFFFFF
participant black_with_unspecified_alpha #000000
participant gray_with_unspecified_alpha #CCCCCC

white_with_zero_alpha -> black_with_zero_alpha: Hello
@enduml
"""

Expected result MUST be put between triple brackets

{{{
<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" contentStyleType="text/css" data-diagram-type="SEQUENCE" preserveAspectRatio="none" version="1.1" zoomAndPan="magnify">
  <defs/>
  <g>
    <g>
      <title>transparent_keyword</title>
      <rect fill="#000000" fill-opacity="0.00000"/>
      <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5,5;"/>
    </g>
    <g>
      <title>white_with_zero_alpha</title>
      <rect fill="#000000" fill-opacity="0.00000"/>
      <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5,5;"/>
    </g>
    <g>
      <title>black_with_zero_alpha</title>
      <rect fill="#000000" fill-opacity="0.00000"/>
      <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5,5;"/>
    </g>
    <g>
      <title>gray_with_zero_alpha</title>
      <rect fill="#000000" fill-opacity="0.00000"/>
      <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5,5;"/>
    </g>
    <g>
      <title>white_with_low_alpha</title>
      <rect fill="#000000" fill-opacity="0.00000"/>
      <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5,5;"/>
    </g>
    <g>
      <title>black_with_low_alpha</title>
      <rect fill="#000000" fill-opacity="0.00000"/>
      <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5,5;"/>
    </g>
    <g>
      <title>gray_with_low_alpha</title>
      <rect fill="#000000" fill-opacity="0.00000"/>
      <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5,5;"/>
    </g>
    <g>
      <title>white_with_high_alpha</title>
      <rect fill="#000000" fill-opacity="0.00000"/>
      <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5,5;"/>
    </g>
    <g>
      <title>black_with_high_alpha</title>
      <rect fill="#000000" fill-opacity="0.00000"/>
      <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5,5;"/>
    </g>
    <g>
      <title>gray_with_high_alpha</title>
      <rect fill="#000000" fill-opacity="0.00000"/>
      <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5,5;"/>
    </g>
    <g>
      <title>white_with_full_alpha</title>
      <rect fill="#000000" fill-opacity="0.00000"/>
      <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5,5;"/>
    </g>
    <g>
      <title>black_with_full_alpha</title>
      <rect fill="#000000" fill-opacity="0.00000"/>
      <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5,5;"/>
    </g>
    <g>
      <title>gray_with_full_alpha</title>
      <rect fill="#000000" fill-opacity="0.00000"/>
      <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5,5;"/>
    </g>
    <g>
      <title>white_with_unspecified_alpha</title>
      <rect fill="#000000" fill-opacity="0.00000"/>
      <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5,5;"/>
    </g>
    <g>
      <title>black_with_unspecified_alpha</title>
      <rect fill="#000000" fill-opacity="0.00000"/>
      <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5,5;"/>
    </g>
    <g>
      <title>gray_with_unspecified_alpha</title>
      <rect fill="#000000" fill-opacity="0.00000"/>
      <line style="stroke:#181818;stroke-width:0.5;stroke-dasharray:5,5;"/>
    </g>
    <g class="participant participant-head" data-participant="transparent_keyword">
      <rect fill="none" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">transparent_keyword</text>
    </g>
    <g class="participant participant-tail" data-participant="transparent_keyword">
      <rect fill="none" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">transparent_keyword</text>
    </g>
    <g class="participant participant-head" data-participant="white_with_zero_alpha">
      <rect fill="none" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">white_with_zero_alpha</text>
    </g>
    <g class="participant participant-tail" data-participant="white_with_zero_alpha">
      <rect fill="none" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">white_with_zero_alpha</text>
    </g>
    <g class="participant participant-head" data-participant="black_with_zero_alpha">
      <rect fill="none" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">black_with_zero_alpha</text>
    </g>
    <g class="participant participant-tail" data-participant="black_with_zero_alpha">
      <rect fill="none" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">black_with_zero_alpha</text>
    </g>
    <g class="participant participant-head" data-participant="gray_with_zero_alpha">
      <rect fill="none" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">gray_with_zero_alpha</text>
    </g>
    <g class="participant participant-tail" data-participant="gray_with_zero_alpha">
      <rect fill="none" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">gray_with_zero_alpha</text>
    </g>
    <g class="participant participant-head" data-participant="white_with_low_alpha">
      <rect fill="#FFFFFF" fill-opacity="0.00392" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">white_with_low_alpha</text>
    </g>
    <g class="participant participant-tail" data-participant="white_with_low_alpha">
      <rect fill="#FFFFFF" fill-opacity="0.00392" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">white_with_low_alpha</text>
    </g>
    <g class="participant participant-head" data-participant="black_with_low_alpha">
      <rect fill="#000000" fill-opacity="0.00392" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">black_with_low_alpha</text>
    </g>
    <g class="participant participant-tail" data-participant="black_with_low_alpha">
      <rect fill="#000000" fill-opacity="0.00392" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">black_with_low_alpha</text>
    </g>
    <g class="participant participant-head" data-participant="gray_with_low_alpha">
      <rect fill="#CCCCCC" fill-opacity="0.00392" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">gray_with_low_alpha</text>
    </g>
    <g class="participant participant-tail" data-participant="gray_with_low_alpha">
      <rect fill="#CCCCCC" fill-opacity="0.00392" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">gray_with_low_alpha</text>
    </g>
    <g class="participant participant-head" data-participant="white_with_high_alpha">
      <rect fill="#FFFFFF" fill-opacity="0.99608" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">white_with_high_alpha</text>
    </g>
    <g class="participant participant-tail" data-participant="white_with_high_alpha">
      <rect fill="#FFFFFF" fill-opacity="0.99608" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">white_with_high_alpha</text>
    </g>
    <g class="participant participant-head" data-participant="black_with_high_alpha">
      <rect fill="#000000" fill-opacity="0.99608" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">black_with_high_alpha</text>
    </g>
    <g class="participant participant-tail" data-participant="black_with_high_alpha">
      <rect fill="#000000" fill-opacity="0.99608" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">black_with_high_alpha</text>
    </g>
    <g class="participant participant-head" data-participant="gray_with_high_alpha">
      <rect fill="#CCCCCC" fill-opacity="0.99608" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">gray_with_high_alpha</text>
    </g>
    <g class="participant participant-tail" data-participant="gray_with_high_alpha">
      <rect fill="#CCCCCC" fill-opacity="0.99608" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">gray_with_high_alpha</text>
    </g>
    <g class="participant participant-head" data-participant="white_with_full_alpha">
      <rect fill="#FFFFFF" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">white_with_full_alpha</text>
    </g>
    <g class="participant participant-tail" data-participant="white_with_full_alpha">
      <rect fill="#FFFFFF" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">white_with_full_alpha</text>
    </g>
    <g class="participant participant-head" data-participant="black_with_full_alpha">
      <rect fill="#000000" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">black_with_full_alpha</text>
    </g>
    <g class="participant participant-tail" data-participant="black_with_full_alpha">
      <rect fill="#000000" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">black_with_full_alpha</text>
    </g>
    <g class="participant participant-head" data-participant="gray_with_full_alpha">
      <rect fill="#CCCCCC" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">gray_with_full_alpha</text>
    </g>
    <g class="participant participant-tail" data-participant="gray_with_full_alpha">
      <rect fill="#CCCCCC" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">gray_with_full_alpha</text>
    </g>
    <g class="participant participant-head" data-participant="white_with_unspecified_alpha">
      <rect fill="#FFFFFF" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">white_with_unspecified_alpha</text>
    </g>
    <g class="participant participant-tail" data-participant="white_with_unspecified_alpha">
      <rect fill="#FFFFFF" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">white_with_unspecified_alpha</text>
    </g>
    <g class="participant participant-head" data-participant="black_with_unspecified_alpha">
      <rect fill="#000000" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">black_with_unspecified_alpha</text>
    </g>
    <g class="participant participant-tail" data-participant="black_with_unspecified_alpha">
      <rect fill="#000000" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">black_with_unspecified_alpha</text>
    </g>
    <g class="participant participant-head" data-participant="gray_with_unspecified_alpha">
      <rect fill="#CCCCCC" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">gray_with_unspecified_alpha</text>
    </g>
    <g class="participant participant-tail" data-participant="gray_with_unspecified_alpha">
      <rect fill="#CCCCCC" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">gray_with_unspecified_alpha</text>
    </g>
    <g class="message" data-participant-1="white_with_zero_alpha" data-participant-2="black_with_zero_alpha">
      <polygon fill="#181818" style="stroke:#181818;stroke-width:1;"/>
      <line style="stroke:#181818;stroke-width:1;"/>
      <text fill="#000000" font-family="sans-serif" font-size="13" lengthAdjust="spacing">Hello</text>
    </g>
  </g>
</svg>
}}}

 */
public class SVG0003_Test extends SvgTest {

	@Test
	void testSequenceDiagramUsesCorrectNonOpaqueColours() throws IOException {
		checkXmlAndDescription("(16 participants)");
	}

}
