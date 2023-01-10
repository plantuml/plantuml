
/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 *
 * If you like this project or if you find it useful, you can support us at:
 *
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
 *
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Thomas Woyke, Robert Bosch GmbH
 *
 */
package nonreg.graphml.component;

import nonreg.graphml.GraphmlTest;

import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
'Connected ports on component
[i]
component C {
  portin p1
  portin p2
  portout po1
  portout po2
  component c1
}
[o]
i --> p1
i --> p2
p1 --> c1
po1 --> o
po2 --> o
c1 --> po1
@enduml
"""

Expected result MUST be put between triple brackets

{{{
<?xml version="1.0" encoding="UTF-8"?><graphml xmlns="http://graphml.graphdrawing.org/xmlns" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">
<key attr.name="label" attr.type="string" for="node" id="d0"/>
<key attr.name="type" attr.type="string" for="node" id="d1"/>
<key attr.name="entityType" attr.type="string" for="node" id="d2"/>
<key attr.name="style" attr.type="string" for="node" id="d3"/>
<key attr.name="sourceDecor" attr.type="string" for="node" id="d4"/>
<key attr.name="targetDecor" attr.type="string" for="node" id="d5"/>
<key attr.name="edgeType" attr.type="string" for="edge" id="d13"/>
<key attr.name="diagramType" attr.type="string" for="node" id="d18"/>
<key attr.name="sourceFile" attr.type="string" for="node" id="d19"/>
<key attr.name="pumlId" attr.type="string" for="node" id="d20"/>
<key attr.name="pumlPath" attr.type="string" for="node" id="d21"/>
<graph edgedefault="undirected">
<node id="1">
<data key="d1">Diagram</data>
<data key="d2">DIAGRAM</data>
<data key="d0">GML0016_Test.puml</data>
<data key="d20">diag0</data>
<data key="d21">./nonreg/graphml/component/GML0016_Test/0/diag0</data>
<data key="d18">COMPONENT</data>
<data key="d19">./nonreg/graphml/component/GML0016_Test.puml</data>
</node>
<node id="4">
<data key="d1">Group</data>
<data key="d2">COMPONENT</data>
<data key="d0">C</data>
<data key="d20">cl0003</data>
<data key="d21">./nonreg/graphml/component/GML0016_Test/0/cl0003</data>
</node>
<node id="2">
<data key="d1">Leaf</data>
<data key="d2">COMPONENT</data>
<data key="d0">i</data>
<data key="d20">cl0002</data>
<data key="d21">./nonreg/graphml/component/GML0016_Test/0/cl0002</data>
</node>
<node id="3">
<data key="d1">Leaf</data>
<data key="d2">COMPONENT</data>
<data key="d0">o</data>
<data key="d20">cl0009</data>
<data key="d21">./nonreg/graphml/component/GML0016_Test/0/cl0009</data>
</node>
<node id="5">
<data key="d1">Leaf</data>
<data key="d2">PORT_IN</data>
<data key="d0">p1</data>
<data key="d20">cl0004</data>
<data key="d21">./nonreg/graphml/component/GML0016_Test/0/cl0004</data>
</node>
<node id="6">
<data key="d1">Leaf</data>
<data key="d2">PORT_IN</data>
<data key="d0">p2</data>
<data key="d20">cl0005</data>
<data key="d21">./nonreg/graphml/component/GML0016_Test/0/cl0005</data>
</node>
<node id="7">
<data key="d1">Leaf</data>
<data key="d2">PORT_OUT</data>
<data key="d0">po1</data>
<data key="d20">cl0006</data>
<data key="d21">./nonreg/graphml/component/GML0016_Test/0/cl0006</data>
</node>
<node id="8">
<data key="d1">Leaf</data>
<data key="d2">PORT_OUT</data>
<data key="d0">po2</data>
<data key="d20">cl0007</data>
<data key="d21">./nonreg/graphml/component/GML0016_Test/0/cl0007</data>
</node>
<node id="9">
<data key="d1">Leaf</data>
<data key="d2">COMPONENT</data>
<data key="d0">c1</data>
<data key="d20">cl0008</data>
<data key="d21">./nonreg/graphml/component/GML0016_Test/0/cl0008</data>
</node>
<node id="10">
<data key="d1">Link</data>
<data key="d2">LINK</data>
<data key="d20">cl0002_LNK10_cl0004</data>
<data key="d21">./nonreg/graphml/component/GML0016_Test/0/cl0002_LNK10_cl0004</data>
<data key="d3">NORMAL</data>
<data key="d4">NONE</data>
<data key="d5">ARROW</data>
</node>
<node id="11">
<data key="d1">Link</data>
<data key="d2">LINK</data>
<data key="d20">cl0002_LNK11_cl0005</data>
<data key="d21">./nonreg/graphml/component/GML0016_Test/0/cl0002_LNK11_cl0005</data>
<data key="d3">NORMAL</data>
<data key="d4">NONE</data>
<data key="d5">ARROW</data>
</node>
<node id="12">
<data key="d1">Link</data>
<data key="d2">LINK</data>
<data key="d20">cl0004_LNK12_cl0008</data>
<data key="d21">./nonreg/graphml/component/GML0016_Test/0/cl0004_LNK12_cl0008</data>
<data key="d3">NORMAL</data>
<data key="d4">NONE</data>
<data key="d5">ARROW</data>
</node>
<node id="13">
<data key="d1">Link</data>
<data key="d2">LINK</data>
<data key="d20">cl0006_LNK13_cl0009</data>
<data key="d21">./nonreg/graphml/component/GML0016_Test/0/cl0006_LNK13_cl0009</data>
<data key="d3">NORMAL</data>
<data key="d4">NONE</data>
<data key="d5">ARROW</data>
</node>
<node id="14">
<data key="d1">Link</data>
<data key="d2">LINK</data>
<data key="d20">cl0007_LNK14_cl0009</data>
<data key="d21">./nonreg/graphml/component/GML0016_Test/0/cl0007_LNK14_cl0009</data>
<data key="d3">NORMAL</data>
<data key="d4">NONE</data>
<data key="d5">ARROW</data>
</node>
<node id="15">
<data key="d1">Link</data>
<data key="d2">LINK</data>
<data key="d20">cl0008_LNK15_cl0006</data>
<data key="d21">./nonreg/graphml/component/GML0016_Test/0/cl0008_LNK15_cl0006</data>
<data key="d3">NORMAL</data>
<data key="d4">NONE</data>
<data key="d5">ARROW</data>
</node>
<edge id="1" source="4" target="5">
<data key="d13">HIERARCHY</data>
</edge>
<edge id="2" source="4" target="6">
<data key="d13">HIERARCHY</data>
</edge>
<edge id="3" source="4" target="7">
<data key="d13">HIERARCHY</data>
</edge>
<edge id="4" source="4" target="8">
<data key="d13">HIERARCHY</data>
</edge>
<edge id="5" source="4" target="9">
<data key="d13">HIERARCHY</data>
</edge>
<edge id="6" source="2" target="10">
<data key="d13">IS_SOURCE</data>
</edge>
<edge id="7" source="5" target="10">
<data key="d13">IS_TARGET</data>
</edge>
<edge id="8" source="2" target="11">
<data key="d13">IS_SOURCE</data>
</edge>
<edge id="9" source="6" target="11">
<data key="d13">IS_TARGET</data>
</edge>
<edge id="10" source="5" target="12">
<data key="d13">IS_SOURCE</data>
</edge>
<edge id="11" source="9" target="12">
<data key="d13">IS_TARGET</data>
</edge>
<edge id="12" source="7" target="13">
<data key="d13">IS_SOURCE</data>
</edge>
<edge id="13" source="3" target="13">
<data key="d13">IS_TARGET</data>
</edge>
<edge id="14" source="8" target="14">
<data key="d13">IS_SOURCE</data>
</edge>
<edge id="15" source="3" target="14">
<data key="d13">IS_TARGET</data>
</edge>
<edge id="16" source="9" target="15">
<data key="d13">IS_SOURCE</data>
</edge>
<edge id="17" source="7" target="15">
<data key="d13">IS_TARGET</data>
</edge>
<edge id="18" source="1" target="4">
<data key="d13">DIAGRAM_CONTAINS</data>
</edge>
<edge id="19" source="1" target="2">
<data key="d13">DIAGRAM_CONTAINS</data>
</edge>
<edge id="20" source="1" target="3">
<data key="d13">DIAGRAM_CONTAINS</data>
</edge>
<edge id="21" source="1" target="5">
<data key="d13">DIAGRAM_CONTAINS</data>
</edge>
<edge id="22" source="1" target="6">
<data key="d13">DIAGRAM_CONTAINS</data>
</edge>
<edge id="23" source="1" target="7">
<data key="d13">DIAGRAM_CONTAINS</data>
</edge>
<edge id="24" source="1" target="8">
<data key="d13">DIAGRAM_CONTAINS</data>
</edge>
<edge id="25" source="1" target="9">
<data key="d13">DIAGRAM_CONTAINS</data>
</edge>
<edge id="26" source="1" target="10">
<data key="d13">DIAGRAM_CONTAINS</data>
</edge>
<edge id="27" source="1" target="11">
<data key="d13">DIAGRAM_CONTAINS</data>
</edge>
<edge id="28" source="1" target="12">
<data key="d13">DIAGRAM_CONTAINS</data>
</edge>
<edge id="29" source="1" target="13">
<data key="d13">DIAGRAM_CONTAINS</data>
</edge>
<edge id="30" source="1" target="14">
<data key="d13">DIAGRAM_CONTAINS</data>
</edge>
<edge id="31" source="1" target="15">
<data key="d13">DIAGRAM_CONTAINS</data>
</edge>
</graph>
</graphml>
}}}

 */
public class GML0016_Test extends GraphmlTest {

	@Test
	void testSimple() throws IOException, InterruptedException {
		checkXmlAndDescription("(7 entities)");
	}

}
