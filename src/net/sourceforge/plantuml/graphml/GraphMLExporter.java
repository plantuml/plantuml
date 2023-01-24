
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
package net.sourceforge.plantuml.graphml;

import static java.nio.charset.StandardCharsets.UTF_8;
import static net.sourceforge.plantuml.graphml.GraphMLKeyDefinition.GML_KEY_DIAG_CAPTION;
import static net.sourceforge.plantuml.graphml.GraphMLKeyDefinition.GML_KEY_DIAG_FOOTER;
import static net.sourceforge.plantuml.graphml.GraphMLKeyDefinition.GML_KEY_DIAG_HEADER;
import static net.sourceforge.plantuml.graphml.GraphMLKeyDefinition.GML_KEY_DIAG_SOURCE_FILE;
import static net.sourceforge.plantuml.graphml.GraphMLKeyDefinition.GML_KEY_DIAG_TITLE;
import static net.sourceforge.plantuml.graphml.GraphMLKeyDefinition.GML_KEY_DIAG_TYPE;
import static net.sourceforge.plantuml.graphml.GraphMLKeyDefinition.GML_KEY_EDGE_TYPE;
import static net.sourceforge.plantuml.graphml.GraphMLKeyDefinition.GML_KEY_ENTITY_TYPE;
import static net.sourceforge.plantuml.graphml.GraphMLKeyDefinition.GML_KEY_ID;
import static net.sourceforge.plantuml.graphml.GraphMLKeyDefinition.GML_KEY_JSON;
import static net.sourceforge.plantuml.graphml.GraphMLKeyDefinition.GML_KEY_LABEL;
import static net.sourceforge.plantuml.graphml.GraphMLKeyDefinition.GML_KEY_LINK_DIRECTION;
import static net.sourceforge.plantuml.graphml.GraphMLKeyDefinition.GML_KEY_LINK_MIDDLE_DECOR;
import static net.sourceforge.plantuml.graphml.GraphMLKeyDefinition.GML_KEY_LINK_SOURCE_DECOR;
import static net.sourceforge.plantuml.graphml.GraphMLKeyDefinition.GML_KEY_LINK_SOURCE_LABEL;
import static net.sourceforge.plantuml.graphml.GraphMLKeyDefinition.GML_KEY_LINK_STYLE;
import static net.sourceforge.plantuml.graphml.GraphMLKeyDefinition.GML_KEY_LINK_TARGET_DECOR;
import static net.sourceforge.plantuml.graphml.GraphMLKeyDefinition.GML_KEY_LINK_TARGET_LABEL;
import static net.sourceforge.plantuml.graphml.GraphMLKeyDefinition.GML_KEY_MEMBER_ABSTRACT;
import static net.sourceforge.plantuml.graphml.GraphMLKeyDefinition.GML_KEY_MEMBER_STATIC;
import static net.sourceforge.plantuml.graphml.GraphMLKeyDefinition.GML_KEY_MEMBER_VISIBILITY;
import static net.sourceforge.plantuml.graphml.GraphMLKeyDefinition.GML_KEY_PUML_ID;
import static net.sourceforge.plantuml.graphml.GraphMLKeyDefinition.GML_KEY_PUML_PATH;
import static net.sourceforge.plantuml.graphml.GraphMLKeyDefinition.GML_KEY_TYPE;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import net.sourceforge.plantuml.genericdiagram.data.GenericDiagram;
import net.sourceforge.plantuml.genericdiagram.data.GenericEdge;
import net.sourceforge.plantuml.genericdiagram.data.GenericGroup;
import net.sourceforge.plantuml.genericdiagram.data.GenericLeaf;
import net.sourceforge.plantuml.genericdiagram.data.GenericLink;
import net.sourceforge.plantuml.genericdiagram.data.GenericMember;
import net.sourceforge.plantuml.genericdiagram.data.GenericModelElement;
import net.sourceforge.plantuml.genericdiagram.data.GenericStereotype;
import net.sourceforge.plantuml.genericdiagram.genericprocessing.IGenericDiagramVisitor;
import net.sourceforge.plantuml.utils.Log;
import net.sourceforge.plantuml.xmi.XmlDiagramTransformer;
import net.sourceforge.plantuml.xml.XmlFactories;


public class GraphMLExporter implements IGenericDiagramVisitor, XmlDiagramTransformer {
	Map<String, DataKeyInfo> dataKeyLookup;
	DocumentBuilder builder;
	Document document;
	Element graph;
	Set<String> usedAttributes;
	List<Node> domNodes;

	public GraphMLExporter() {
		initDataKeyMap();
		usedAttributes = new HashSet<>();
		domNodes = new ArrayList<>();
	}

	private void initDataKeyMap() {
		dataKeyLookup = new HashMap<>();
		// all elements
		dataKeyLookup.put(GML_KEY_LABEL,
						DataKeyInfo.with("d0", "string", "node"));
		dataKeyLookup.put(GML_KEY_TYPE,
						DataKeyInfo.with("d1", "string", "node"));
		dataKeyLookup.put(GML_KEY_ENTITY_TYPE,
						DataKeyInfo.with("d2", "string", "node"));
		dataKeyLookup.put(GML_KEY_PUML_ID,
						DataKeyInfo.with("d20", "string", "node"));
		dataKeyLookup.put(GML_KEY_PUML_PATH,
						DataKeyInfo.with("d21", "string", "node"));
		dataKeyLookup.put(GML_KEY_JSON,
						DataKeyInfo.with("d22", "string", "node"));
		// Links
		dataKeyLookup.put(GML_KEY_LINK_STYLE,
						DataKeyInfo.with("d3", "string", "node"));
		dataKeyLookup.put(GML_KEY_LINK_SOURCE_DECOR,
						DataKeyInfo.with("d4", "string", "node"));
		dataKeyLookup.put(GML_KEY_LINK_TARGET_DECOR,
						DataKeyInfo.with("d5", "string", "node"));
		dataKeyLookup.put(GML_KEY_LINK_MIDDLE_DECOR,
						DataKeyInfo.with("d6", "string", "node"));
		dataKeyLookup.put(GML_KEY_LINK_SOURCE_LABEL,
						DataKeyInfo.with("d7", "string", "node"));
		dataKeyLookup.put(GML_KEY_LINK_TARGET_LABEL,
						DataKeyInfo.with("d8", "string", "node"));
		dataKeyLookup.put(GML_KEY_LINK_DIRECTION,
						DataKeyInfo.with("d9", "string", "node"));

		// attributes of class members
		dataKeyLookup.put(GML_KEY_MEMBER_STATIC,
						DataKeyInfo.with("d10", "boolean", "node"));
		dataKeyLookup.put(GML_KEY_MEMBER_ABSTRACT,
						DataKeyInfo.with("d11", "boolean", "node"));
		dataKeyLookup.put(GML_KEY_MEMBER_VISIBILITY,
						DataKeyInfo.with("d12", "string", "node"));

		// Edge
		dataKeyLookup.put(GML_KEY_EDGE_TYPE,
						DataKeyInfo.with("d13", "string", "edge"));

		// Diagram
		dataKeyLookup.put(GML_KEY_DIAG_TITLE,
						DataKeyInfo.with("d14", "string", "node"));
		dataKeyLookup.put(GML_KEY_DIAG_HEADER,
						DataKeyInfo.with("d15", "string", "node"));
		dataKeyLookup.put(GML_KEY_DIAG_FOOTER,
						DataKeyInfo.with("d16", "string", "node"));
		dataKeyLookup.put(GML_KEY_DIAG_CAPTION,
						DataKeyInfo.with("d17", "string", "node"));
		dataKeyLookup.put(GML_KEY_DIAG_TYPE,
						DataKeyInfo.with("d18", "string", "node"));
		dataKeyLookup.put(GML_KEY_DIAG_SOURCE_FILE,
						DataKeyInfo.with("d19", "string", "node"));
	}

	@Override
	public void visitDiagram(GenericDiagram diagram) {
		try {
			// initial dom structure for graph and attribute definitions
			builder = XmlFactories.newDocumentBuilder();
			document = builder.newDocument();
			document.setXmlVersion("1.0");
			document.setXmlStandalone(true);

			// add the diagram node
			Element domNode = createNode(diagram);
			addCommonAttributes(diagram, domNode);
			addDataNode(domNode, GML_KEY_DIAG_TITLE, diagram.getTitle());
			addDataNode(domNode, GML_KEY_DIAG_HEADER, diagram.getHeader());
			addDataNode(domNode, GML_KEY_DIAG_FOOTER, diagram.getFooter());
			addDataNode(domNode, GML_KEY_DIAG_CAPTION, diagram.getFooter());
			addDataNode(domNode, GML_KEY_DIAG_TYPE, diagram.getDiagramType().toString());
			addDataNode(domNode, GML_KEY_DIAG_SOURCE_FILE, diagram.getSourceFile());
			domNodes.add(domNode);

		} catch (ParserConfigurationException e) {
			Log.error("Fatal error when exporting puml diagram ");
			System.exit(1);
		}

	}

	@Override
	public void visitEdge(GenericEdge edge) {

		//we ignore the diagram in the graphML

		Element domEdge = createEdge(edge);
		domEdge.setAttribute("source", Integer.toString(edge.getSource()));
		domEdge.setAttribute("target", Integer.toString(edge.getTarget()));
		// domEdge.setAttribute("source", edge.getPumlIdSource());
		// domEdge.setAttribute("target", edge.getPumlIdTarget());
		// data values
		String edgeType = edge.getEdgeType().toString();
		Element dataEdgeType = document.createElement("data");
		dataEdgeType.setAttribute("key", dataKeyLookup.get(GML_KEY_EDGE_TYPE).getId());
		dataEdgeType.setTextContent(edgeType);
		domEdge.appendChild(dataEdgeType);
		usedAttributes.add(GML_KEY_EDGE_TYPE);
		domNodes.add(domEdge);

	}

	@Override
	public void visitGroup(GenericGroup group) {
		Element domNode = createNode(group);
		addCommonAttributes(group, domNode);
		domNodes.add(domNode);


	}

	@Override
	public void visitLeaf(GenericLeaf leaf) {

		Element domNode = createNode(leaf);
		addCommonAttributes(leaf, domNode);
		if (leaf.getJson() != null) {
			String json = leaf.getJson();
			addDataNode(domNode, GML_KEY_JSON, json);
		}
		domNodes.add(domNode);

	}

	private void addCommonAttributes(GenericModelElement modelElement, Element domNode) {

		addDataNode(domNode, GML_KEY_TYPE, modelElement.getImplementationType());
		addDataNode(domNode, GML_KEY_ENTITY_TYPE, modelElement.getType().toString());
		addDataNode(domNode, GML_KEY_LABEL, modelElement.getLabel());
		addDataNode(domNode, GML_KEY_PUML_ID, modelElement.getPumlId());
		addDataNode(domNode, GML_KEY_PUML_PATH, modelElement.getFullyQualifiedPumlId());

	}

	private void addDataNode(Element domNode, String attributeName, String attributeValue) {

		if ((attributeValue != null) && (!attributeValue.equals("NULL"))) {
			Element typeData = document.createElement("data");
			typeData.setAttribute("key", dataKeyLookup.get(attributeName).getId());
			typeData.setTextContent(attributeValue);
			domNode.appendChild(typeData);
			usedAttributes.add(attributeName);
		}
	}

	@Override
	public void visitLink(GenericLink genericLink) {

		Element domNode = createNode(genericLink);
		addCommonAttributes(genericLink, domNode);
		addDataNode(domNode, GML_KEY_LINK_STYLE, genericLink.getStyle().toString());
		addDataNode(domNode, GML_KEY_LINK_SOURCE_DECOR, genericLink.getSourceDecor().toString());
		addDataNode(domNode, GML_KEY_LINK_TARGET_DECOR, genericLink.getTargetDecor().toString());
		addDataNode(domNode, GML_KEY_LINK_SOURCE_LABEL, genericLink.getSourceLabel());
		addDataNode(domNode, GML_KEY_LINK_TARGET_LABEL, genericLink.getTargetLabel());
		addDataNode(domNode, GML_KEY_LINK_DIRECTION, genericLink.getDirection().toString());
		domNodes.add(domNode);
	}

	@Override
	public void visitMember(GenericMember member) {

		Element domNode = createNode(member);
		addCommonAttributes(member, domNode);
		addDataNode(domNode, GML_KEY_MEMBER_STATIC, member.isAbstract().toString().toLowerCase());
		addDataNode(domNode, GML_KEY_MEMBER_ABSTRACT, member.isStatic().toString().toLowerCase());
		addDataNode(domNode, GML_KEY_MEMBER_VISIBILITY, member.getVisibility().toString());
		domNodes.add(domNode);

	}

	private Element createNode(GenericModelElement genericModelElement) {

		Element node = document.createElement("node");
		node.setAttribute(GML_KEY_ID, Integer.toString(genericModelElement.getId()));
		usedAttributes.add(GML_KEY_ID);
		return node;
	}


	private Element createEdge(GenericEdge genericEdge) {

		Element edge = document.createElement("edge");
		edge.setAttribute(GML_KEY_ID, Integer.toString(genericEdge.getId()));
		usedAttributes.add(GML_KEY_ID);
		return edge;
	}

	@Override
	public void visitStereotype(GenericStereotype stereotype) {
		Element domNode = createNode(stereotype);
		addCommonAttributes(stereotype, domNode);
		domNodes.add(domNode);
	}

	@Override
	public void transformerXml(OutputStream os) throws TransformerException, ParserConfigurationException {

		final Source source = new DOMSource(document);
		final Result result = new StreamResult(os);

		final Transformer transformer = XmlFactories.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, UTF_8.name());
		transformer.transform(source, result);
	}

	public void finish() {


			final Element graphml = document.createElement("graphml");
			graphml.setAttribute("xmlns", "http://graphml.graphdrawing.org/xmlns");
			graphml.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			graphml.setAttribute("xsi:schemaLocation", "http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd");
			document.appendChild(graphml);

			// add used data keys before exporting
			List<DataKeyInfo> sortedDataKeys = dataKeyLookup.values().stream().sorted().collect(Collectors.toList());
			for (DataKeyInfo info : sortedDataKeys) {
				String attrName = "";
				for (Map.Entry<String, DataKeyInfo> kv : this.dataKeyLookup.entrySet()) {
					if (kv.getValue().equals(info)) {
						attrName = kv.getKey();
					}
				}
				String keyName = info.getId();
				String attrType = info.getType();
				String attrScope = info.getScope();

				if (this.usedAttributes.contains(attrName)) {
					Element keyDef = document.createElement("key");
					keyDef.setAttribute("id", keyName);
					keyDef.setAttribute("for", attrScope);
					keyDef.setAttribute("attr.name", attrName);
					keyDef.setAttribute("attr.type", attrType);
					graphml.appendChild(keyDef);
				}
			}

			// add graph element
			graph = document.createElement("graph");
			graph.setAttribute("edgedefault", "undirected");
			graphml.appendChild(graph);

			// add dom Nodes
			for (Node n : this.domNodes) {
				graph.appendChild(n);
			}

	}
}
