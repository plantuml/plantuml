package org.scilab.forge.jlatexmath;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XmlReader replaces javax.xml (the TeaVM class library does not have it). It must
 * give what the JDK gives for the XML files of the library.
 */
class XmlReaderTest {

	private static final Path RESOURCES = Paths.get("src", "main", "resources", "org", "scilab", "forge", "jlatexmath");

	private static List<Path> xmlFiles() throws IOException {
		try (Stream<Path> walk = Files.walk(RESOURCES)) {
			return walk.filter(p -> p.toString().endsWith(".xml")).sorted().collect(Collectors.toList());
		}
	}

	/** The child elements of a DOM element (the text and the comments are dropped). */
	private static List<Element> children(Element e) {
		final List<Element> result = new ArrayList<Element>();
		final NodeList nodes = e.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++)
			if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE)
				result.add((Element) nodes.item(i));
		return result;
	}

	private static void assertSameTree(Element dom, XmlElement mine, String where) {
		assertEquals(dom.getTagName(), mine.getTagName(), where);
		// the attributes, with the same values (the order of a DOM is not the one of the file)
		assertEquals(dom.getAttributes().getLength(), mine.getAttributeNames().size(), where);
		for (int i = 0; i < dom.getAttributes().getLength(); i++) {
			final String name = dom.getAttributes().item(i).getNodeName();
			assertEquals(dom.getAttribute(name), mine.getAttribute(name), where + " @" + name);
			assertTrue(mine.hasAttribute(name), where + " @" + name);
		}
		final List<Element> domChildren = children(dom);
		final List<XmlElement> myChildren = mine.getChildren();
		assertEquals(domChildren.size(), myChildren.size(), where + " children of " + dom.getTagName());
		for (int i = 0; i < domChildren.size(); i++)
			assertSameTree(domChildren.get(i), myChildren.get(i), where + "/" + dom.getTagName() + "[" + i + "]");
	}

	@Test
	void testEveryXmlFileOfTheLibraryIsReadAsTheJdkReadsIt() throws Exception {
		final List<Path> files = xmlFiles();
		assertTrue(files.size() >= 60, "the XML files of the library: " + files.size());
		for (Path file : files) {
			final Element dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file.toFile())
					.getDocumentElement();
			final XmlElement mine = XmlReader.parse(new ByteArrayInputStream(Files.readAllBytes(file)));
			assertSameTree(dom, mine, file.getFileName().toString());
		}
	}

	@Test
	void testGetElementsByTagNameIsTheOneOfTheDom() throws Exception {
		for (Path file : xmlFiles()) {
			final Element dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file.toFile())
					.getDocumentElement();
			final XmlElement mine = XmlReader.parse(new ByteArrayInputStream(Files.readAllBytes(file)));
			for (String name : Arrays.asList("Char", "Kern", "Symbol", "Map", "Glue", "Font", "Style", "Mapping",
					"SymbolMapping", "NoSuchTag")) {
				final NodeList expected = dom.getElementsByTagName(name);
				final XmlNodeList actual = mine.getElementsByTagName(name);
				assertEquals(expected.getLength(), actual.getLength(), file.getFileName() + " " + name);
				for (int i = 0; i < expected.getLength(); i += 97)
					assertEquals(((Element) expected.item(i)).getAttribute("code"),
							actual.item(i).getAttribute("code"), file.getFileName() + " " + name + " #" + i);
			}
		}
	}

	private static XmlElement parse(String xml) {
		return XmlReader.parse(xml);
	}

	@Test
	void testReferencesInValues() {
		final XmlElement e = parse("<a x=\"&#305;&#x41;&#128512;\" y='&lt;&gt;&amp;&quot;&apos;'/>");
		assertEquals("ıA😀", e.getAttribute("x"));
		assertEquals("<>&\"'", e.getAttribute("y"));
	}

	@Test
	void testWhatIsSkipped() {
		final XmlElement e = parse("﻿<?xml version='1.0'?>\n<!-- a\ncomment -->\n<!DOCTYPE a [ <!ENTITY x 'y'> ]>\n"
				+ "<a>text <!-- c --> <b k=\"v\"/> <![CDATA[ <c/> ]]> <?pi x?> <d></d> more</a>\n<!-- end -->\n");
		assertEquals("a", e.getTagName());
		assertEquals(2, e.getChildren().size());
		assertEquals("b", e.getChildren().get(0).getTagName());
		assertEquals("v", e.getChildren().get(0).getAttribute("k"));
		assertEquals("d", e.getChildren().get(1).getTagName());
	}

	@Test
	void testMissingAttributeIsEmpty() {
		final XmlElement e = parse("<a b=\"1\"/>");
		assertEquals("", e.getAttribute("c"));
		assertEquals(false, e.hasAttribute("c"));
		assertEquals(Arrays.asList("b"), e.getAttributeNames());
		assertNull(e.getElementsByTagName("x").item(0));
		assertEquals(0, e.getElementsByTagName("x").getLength());
	}

	@Test
	void testValuesAreNormalizedLikeTheJdkDoes() throws Exception {
		final String xml = "<a v=\"x\ny\tz\r\nw\"/>";
		final Element dom = DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))).getDocumentElement();
		assertEquals(dom.getAttribute("v"), parse(xml).getAttribute("v"));
	}

	@Test
	void testDescendantsInTheOrderOfTheFile() {
		final XmlElement e = parse("<r><a n='1'><a n='2'/></a><b><a n='3'/></b><a n='4'/></r>");
		final XmlNodeList list = e.getElementsByTagName("a");
		assertEquals(4, list.getLength());
		assertEquals("1", list.item(0).getAttribute("n"));
		assertEquals("2", list.item(1).getAttribute("n"));
		assertEquals("3", list.item(2).getAttribute("n"));
		assertEquals("4", list.item(3).getAttribute("n"));
	}

	@Test
	void testTheTextBetweenTheElementsIsNotLookedAt() {
		// no parser of the library reads it, so it is dropped as it is, references included
		final XmlElement e = parse("<a>&nope; text <b/> &#xZZ;</a>");
		assertEquals(1, e.getChildren().size());
	}

	@Test
	void testErrors() {
		for (String bad : new String[] { "", "text", "<a>", "<a></b>", "<a b=1/>", "<a b=\"1/>", "<a b/>",
				"<a b=\"&#xZZ;\"/>", "<a/><b/>", "<a b=\"<\"/>", "<!-- never ended <a/>", "<a", "<a><!-- x </a>" })
			assertThrows(IllegalArgumentException.class, () -> parse(bad), bad);
		assertThrows(IllegalArgumentException.class, () -> XmlReader.parse((java.io.InputStream) null));
	}

}
