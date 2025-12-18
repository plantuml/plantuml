package nonreg.svg;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Isolated;

import java.io.IOException;

/*
Test diagram MUST be put between triple quotes

"""
@startuml
!pragma svginteractive true
hide empty members
class A
class B
class C
class D

A [key1] *- "1" B : holds >
B [key2a] o-- [key2b] C : holds >
C [key3] - "1" D : holds >
@enduml
"""

Expected result MUST be put between triple brackets

{{{
<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" contentStyleType="text/css" data-diagram-type="CLASS" preserveAspectRatio="none" version="1.1" zoomAndPan="magnify">
  <defs>
    <style type="text/css"/>
    <script/>
  </defs>
  <g>
    <!--class A-->
    <g class="entity" data-qualified-name="A" data-source-line="3" id="ent0002">
      <rect fill="#F1F1F1" style="stroke:#181818;stroke-width:0.5;"/>
      <ellipse fill="#ADD1B2" style="stroke:#181818;stroke-width:1;"/>
      <path fill="#000000"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">A</text>
    </g>
    <!--class B-->
    <g class="entity" data-qualified-name="B" data-source-line="4" id="ent0003">
      <rect fill="#F1F1F1" style="stroke:#181818;stroke-width:0.5;"/>
      <ellipse fill="#ADD1B2" style="stroke:#181818;stroke-width:1;"/>
      <path fill="#000000"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">B</text>
    </g>
    <!--class C-->
    <g class="entity" data-qualified-name="C" data-source-line="5" id="ent0004">
      <rect fill="#F1F1F1" style="stroke:#181818;stroke-width:0.5;"/>
      <ellipse fill="#ADD1B2" style="stroke:#181818;stroke-width:1;"/>
      <path fill="#000000"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">C</text>
    </g>
    <!--class D-->
    <g class="entity" data-qualified-name="D" data-source-line="6" id="ent0005">
      <rect fill="#F1F1F1" style="stroke:#181818;stroke-width:0.5;"/>
      <ellipse fill="#ADD1B2" style="stroke:#181818;stroke-width:1;"/>
      <path fill="#000000"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">D</text>
    </g>
    <!--reverse link A to B-->
    <g class="link" data-entity-1="ent0002" data-entity-2="ent0003" data-link-type="composition" data-source-line="8" id="lnk6">
      <path codeLine="8" fill="none" id="A-backto-B" style="stroke:#181818;stroke-width:1;"/>
      <polygon fill="#181818" style="stroke:#181818;stroke-width:1;"/>
      <polygon fill="#000000" style="stroke:#000000;stroke-width:1;"/>
      <text fill="#000000" font-family="sans-serif" font-size="13" lengthAdjust="spacing">holds</text>
      <text fill="#000000" font-family="sans-serif" font-size="13" lengthAdjust="spacing">1</text>
      <rect fill="#F1F1F1" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">key1</text>
    </g>
    <!--reverse link B to C-->
    <g class="link" data-entity-1="ent0003" data-entity-2="ent0004" data-link-type="aggregation" data-source-line="9" id="lnk7">
      <path codeLine="9" fill="none" id="B-backto-C" style="stroke:#181818;stroke-width:1;"/>
      <polygon fill="none" style="stroke:#181818;stroke-width:1;"/>
      <polygon fill="#000000" style="stroke:#000000;stroke-width:1;"/>
      <text fill="#000000" font-family="sans-serif" font-size="13" lengthAdjust="spacing">holds</text>
      <rect fill="#F1F1F1" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">key2a</text>
      <rect fill="#F1F1F1" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">key2b</text>
    </g>
    <!--link C to D-->
    <g class="link" data-entity-1="ent0004" data-entity-2="ent0005" data-link-type="association" data-source-line="10" id="lnk8">
      <path codeLine="10" fill="none" id="C-D" style="stroke:#181818;stroke-width:1;"/>
      <polygon fill="#000000" style="stroke:#000000;stroke-width:1;"/>
      <text fill="#000000" font-family="sans-serif" font-size="13" lengthAdjust="spacing">holds</text>
      <text fill="#000000" font-family="sans-serif" font-size="13" lengthAdjust="spacing">1</text>
      <rect fill="#F1F1F1" style="stroke:#181818;stroke-width:0.5;"/>
      <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">key3</text>
    </g>
  </g>
</svg>
}}}
*/
@Isolated
public class SVG0006_Svek_Test extends SvekSvgTest {

	@Test
	void testQualifierKalsAreGroupedWithTheirEdges() throws IOException {
		checkXmlAndDescription("(4 entities)");
	}
}
