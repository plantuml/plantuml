package nonreg.svg;

import org.junit.jupiter.api.Test;

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
<svg contentStyleType="text/css" data-diagram-type="CLASS" preserveAspectRatio="none" version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" zoomAndPan="magnify">
    <defs>
        <style type="text/css"/>
        <script/>
    </defs>
    <g>
        <!--class A-->
        <g class="entity" data-entity="A" data-source-line="3" data-uid="ent0002" id="entity_A">
            <rect fill="#F1F1F1" style="stroke:#181818;stroke-width:0.5;"/>
            <ellipse fill="#ADD1B2" style="stroke:#181818;stroke-width:1;"/>
            <path fill="#000000"/>
            <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">A</text>
        </g>
        <!--class B-->
        <g class="entity" data-entity="B" data-source-line="4" data-uid="ent0003" id="entity_B">
            <rect fill="#F1F1F1" style="stroke:#181818;stroke-width:0.5;"/>
            <ellipse fill="#ADD1B2" style="stroke:#181818;stroke-width:1;"/>
            <path fill="#000000"/>
            <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">B</text>
        </g>
        <!--class C-->
        <g class="entity" data-entity="C" data-source-line="5" data-uid="ent0004" id="entity_C">
            <rect fill="#F1F1F1" style="stroke:#181818;stroke-width:0.5;"/>
            <ellipse fill="#ADD1B2" style="stroke:#181818;stroke-width:1;"/>
            <path fill="#000000"/>
            <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">C</text>
        </g>
        <!--class D-->
        <g class="entity" data-entity="D" data-source-line="6" data-uid="ent0005" id="entity_D">
            <rect fill="#F1F1F1" style="stroke:#181818;stroke-width:0.5;"/>
            <ellipse fill="#ADD1B2" style="stroke:#181818;stroke-width:1;"/>
            <path fill="#000000"/>
            <text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">D</text>
        </g>
        <!--reverse link A to B-->
        <g class="link" data-entity-1="A" data-entity-2="B" data-source-line="8" data-uid="lnk6" id="link_A_B">
            <path codeLine="8" fill="none" id="A-backto-B" style="stroke:#181818;stroke-width:1;"/>
            <polygon fill="#181818" style="stroke:#181818;stroke-width:1;"/>
            <polygon fill="#000000" style="stroke:#000000;stroke-width:1;"/>
            <text fill="#000000" font-family="sans-serif" font-size="13" lengthAdjust="spacing">holds</text>
            <text fill="#000000" font-family="sans-serif" font-size="13" lengthAdjust="spacing">1</text>
			<rect fill="#F1F1F1" style="stroke:#181818;stroke-width:0.5;"/>
			<text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing">key1</text>
        </g>
        <!--reverse link B to C-->
        <g class="link" data-entity-1="B" data-entity-2="C" data-source-line="9" data-uid="lnk7" id="link_B_C">
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
        <g class="link" data-entity-1="C" data-entity-2="D" data-source-line="10" data-uid="lnk8" id="link_C_D">
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
public class SVG0006_Svek_Test extends SvekSvgTest {

	@Test
	void testQualifierKalsAreGroupedWithTheirEdges() throws IOException {
		checkXmlAndDescription("(4 entities)");
	}
}
