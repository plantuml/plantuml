package nonreg.graphml.component;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import nonreg.graphml.GraphmlTest;

/*

Test diagram MUST be put between triple quotes

"""
@startuml
component comp1
@enduml
"""

Expected result MUST be put between triple brackets

{{{
<?xml version="1.0" encoding="UTF-8"?><graphml xmlns="http://graphml.graphdrawing.org/xmlns" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">
    <graph edgedefault="undirected">
        <node id="comp1"/>
    </graph>
</graphml>
}}}

 */
public class GML0000_Test extends GraphmlTest {

	@Test
	void testSimple() throws IOException {
		final String xml = getXmlAndCheckXmlAndDescription("(1 entities)");

		// Those tests are ULGY: please improve them!
		assertTrue(xml.contains("<graphml "));
		assertTrue(xml.contains("<graph edgedefault=\"undirected\">"));
		assertTrue(xml.contains("<node id=\"comp1\"/>"));
	}

}
