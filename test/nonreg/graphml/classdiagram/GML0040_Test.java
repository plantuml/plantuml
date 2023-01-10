
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
package nonreg.graphml.classdiagram;

import nonreg.graphml.GraphmlTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
' Note on several classes
class class1
class class2
note "text" as N
N .. class1
N .. class2
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
<data key="d0">GML0040_Test.puml</data>
<data key="d20">diag0</data>
<data key="d21">./nonreg/graphml/classdiagram/GML0040_Test/0/diag0</data>
<data key="d18">CLASS</data>
<data key="d19">./nonreg/graphml/classdiagram/GML0040_Test.puml</data>
</node>
<node id="2">
<data key="d1">Leaf</data>
<data key="d2">CLASS</data>
<data key="d0">class1</data>
<data key="d20">cl0002</data>
<data key="d21">./nonreg/graphml/classdiagram/GML0040_Test/0/cl0002</data>
</node>
<node id="3">
<data key="d1">Leaf</data>
<data key="d2">CLASS</data>
<data key="d0">class2</data>
<data key="d20">cl0003</data>
<data key="d21">./nonreg/graphml/classdiagram/GML0040_Test/0/cl0003</data>
</node>
<node id="4">
<data key="d1">Leaf</data>
<data key="d2">NOTE</data>
<data key="d0">text</data>
<data key="d20">cl0004</data>
<data key="d21">./nonreg/graphml/classdiagram/GML0040_Test/0/cl0004</data>
</node>
<node id="5">
<data key="d1">Link</data>
<data key="d2">LINK</data>
<data key="d20">cl0004_LNK5_cl0002</data>
<data key="d21">./nonreg/graphml/classdiagram/GML0040_Test/0/cl0004_LNK5_cl0002</data>
<data key="d3">DASHED</data>
<data key="d4">NONE</data>
<data key="d5">NONE</data>
</node>
<node id="6">
<data key="d1">Link</data>
<data key="d2">LINK</data>
<data key="d20">cl0004_LNK6_cl0003</data>
<data key="d21">./nonreg/graphml/classdiagram/GML0040_Test/0/cl0004_LNK6_cl0003</data>
<data key="d3">DASHED</data>
<data key="d4">NONE</data>
<data key="d5">NONE</data>
</node>
<edge id="1" source="4" target="5">
<data key="d13">IS_SOURCE</data>
</edge>
<edge id="2" source="2" target="5">
<data key="d13">IS_TARGET</data>
</edge>
<edge id="3" source="4" target="6">
<data key="d13">IS_SOURCE</data>
</edge>
<edge id="4" source="3" target="6">
<data key="d13">IS_TARGET</data>
</edge>
<edge id="5" source="1" target="2">
<data key="d13">DIAGRAM_CONTAINS</data>
</edge>
<edge id="6" source="1" target="3">
<data key="d13">DIAGRAM_CONTAINS</data>
</edge>
<edge id="7" source="1" target="4">
<data key="d13">DIAGRAM_CONTAINS</data>
</edge>
<edge id="8" source="1" target="5">
<data key="d13">DIAGRAM_CONTAINS</data>
</edge>
<edge id="9" source="1" target="6">
<data key="d13">DIAGRAM_CONTAINS</data>
</edge>
</graph>
</graphml>
}}}

 */
public class GML0040_Test extends GraphmlTest {

	@Test
	void testSimple() throws IOException, InterruptedException {
		checkXmlAndDescription("(3 entities)");
	}

}
