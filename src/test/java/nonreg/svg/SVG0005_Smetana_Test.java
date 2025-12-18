package nonreg.svg;

import org.junit.jupiter.api.Test;

import java.io.IOException;
/*
Test diagram MUST be put between triple quotes

"""
@startuml
!pragma svginteractive true

component c1 {
  portout p1
}

component c2 {
  portin p2
}

p1 --> p2

@enduml
"""

Expected result MUST be put between triple brackets

{{{
<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" contentStyleType="text/css" data-diagram-type="DESCRIPTION" preserveAspectRatio="none" version="1.1" zoomAndPan="magnify">
  <defs>
    <style type="text/css"/>
    <script/>
  </defs>
  <g>
    <!--cluster c1-->
    <g class="cluster" data-qualified-name="c1" data-source-line="3" id="ent0002">
      <rect fill="none" style="stroke:#181818;stroke-width:1;"/>
      <rect fill="none" style="stroke:#181818;stroke-width:1;"/>
      <rect fill="none" style="stroke:#181818;stroke-width:1;"/>
      <rect fill="none" style="stroke:#181818;stroke-width:1;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" font-weight="bold" lengthAdjust="spacing">c1</text>
    </g>
    <!--cluster c2-->
    <g class="cluster" data-qualified-name="c2" data-source-line="7" id="ent0004">
      <rect fill="none" style="stroke:#181818;stroke-width:1;"/>
      <rect fill="none" style="stroke:#181818;stroke-width:1;"/>
      <rect fill="none" style="stroke:#181818;stroke-width:1;"/>
      <rect fill="none" style="stroke:#181818;stroke-width:1;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" font-weight="bold" lengthAdjust="spacing">c2</text>
    </g>
    <g class="entity" data-qualified-name="c1.p1" data-source-line="4" id="ent0003">
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">p1</text>
      <rect fill="#F1F1F1" style="stroke:#181818;stroke-width:1.5;"/>
    </g>
    <g class="entity" data-qualified-name="c2.p2" data-source-line="8" id="ent0005">
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">p2</text>
      <rect fill="#F1F1F1" style="stroke:#181818;stroke-width:1.5;"/>
    </g>
    <g class="link" data-entity-1="ent0003" data-entity-2="ent0005" data-link-type="dependency" id="lnk6">
      <polygon fill="#181818" style="stroke:#181818;stroke-width:1;"/>
      <path fill="none" style="stroke:#181818;stroke-width:1;"/>
    </g>
  </g>
</svg>
}}}
*/
public class SVG0005_Smetana_Test extends SvgTest {

	@Test
	void testPortsAreGroupedSimilarlyToComponents() throws IOException {
		checkXmlAndDescription("(2 entities)");
	}
}
