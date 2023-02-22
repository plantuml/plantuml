package nonreg.graphml.component;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import nonreg.graphml.GraphmlTest;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
component comp1
component comp2
@enduml
"""

Expected result MUST be put between triple brackets

{{{
<?xml version="1.0" encoding="UTF-8"?><graphml xmlns="http://graphml.graphdrawing.org/xmlns" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">
    <graph edgedefault="undirected">
        <node id="comp1"/>
        <node id="comp2"/>
    </graph>
</graphml>
}}}

 */
public class GML0001_Test extends GraphmlTest {

	@Test
	void testSimple() throws IOException {
		checkXmlAndDescription("(2 entities)");
	}

}
