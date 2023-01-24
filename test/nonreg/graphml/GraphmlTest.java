
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
package nonreg.graphml;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sourceforge.plantuml.utils.Log;

import org.junit.jupiter.api.AfterEach;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.Option;
import net.sourceforge.plantuml.SourceFileReader;

public class GraphmlTest {

	@AfterEach
	public  void cleanupTempFiles() {
		getTempPumlFile().toFile().delete();
		getTempGraphmlFile().toFile().delete();
	}
	private static final String TRIPLE_QUOTE = "\"\"\"";

	protected void checkXmlAndDescription(final String expectedDescription)
			throws IOException, UnsupportedEncodingException , InterruptedException{
		final String actualResult = runPlantUML(expectedDescription);
		final String xmlExpected = readStringFromSourceFile(getDiagramFile(), "{{{", "}}}");

		if (comparableXML(actualResult).equals(comparableXML(xmlExpected)) == false) {
			assertEquals(xmlExpected, actualResult, "Generated GraphML is not ok");
		}
	}

	// for a simple comparision of the XML we create one line strings of each element in the XML file,
	// sort them and append them into a single string ...
	// should be good enough to detect when attributes show up at the wrong element
	private String comparableXML(String xmlString){
		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			dbf.setCoalescing(true);
			dbf.setIgnoringElementContentWhitespace(true);
			dbf.setIgnoringComments(true);
			DocumentBuilder db = dbf.newDocumentBuilder();

			Document doc = db.parse(new ByteArrayInputStream(xmlString.getBytes(UTF_8)));

			List<String> xmlData = new ArrayList<>();
			xmlData.addAll(collectElementStrings(doc, "key"));
			xmlData.addAll(collectElementStrings(doc, "node"));
			xmlData.addAll(collectElementStrings(doc, "edge"));

			List<String> sortedXmlData = xmlData.stream()
							.sorted(Comparator.naturalOrder())
							.collect(Collectors.toList());

			StringBuilder builder = new StringBuilder();
			for(String xml : sortedXmlData){
				builder.append(xml);
			}

			return builder.toString();
		} catch (ParserConfigurationException e) {
			new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (SAXException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	// collect info from all elements of the name given by elementName
	private List<String> collectElementStrings(Document doc, String elementName){

		List<String> elems = new ArrayList<>();
		NodeList nodes = doc.getElementsByTagName(elementName);
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			elems.add(getNodeString(node));
		}
		return elems;
	}

	// aggregate info of a single node: name, attributes and values and data nodes ...
	private String getNodeString(Node node){

		StringBuilder builder = new StringBuilder();
		builder.append(node.getNodeName());
		String attr_str = getAttributeString(node);
		builder.append(attr_str);
		String data_str = getDataString(node);
		builder.append(data_str);
		return builder.toString();
	}

	private String getAttributeString(Node node){
		StringBuilder builder = new StringBuilder();
		SortedMap<String, String> attrMap = getAttributeMap(node);
		addMapContents(attrMap, builder);
		return builder.toString();
	}

	// process "data" child nodes of XML
	private String getDataString(Node node){
		StringBuilder builder = new StringBuilder();
		SortedMap<String, String> dataMap = getDataMap(node);
		addMapContents(dataMap, builder);
		return builder.toString();
	}

	private SortedMap<String, String> getAttributeMap(Node node) {

		NamedNodeMap attrs = node.getAttributes();
		SortedMap<String, String> attributeMap = new TreeMap<>();
		for (int j = 0; j < attrs.getLength(); j++) {
			Node attr = attrs.item(j);
			String key = attr.getNodeName();
			String value = attr.getNodeValue();
			attributeMap.put(key, value);
		}
		return attributeMap;
	}
	private SortedMap<String,String> getDataMap(Node node) {

		SortedMap<String, String> dataEntryMap = new TreeMap<>();
		if (node instanceof Element) {
			Element element = (Element) node;
			NodeList dataElems = element.getElementsByTagName("data");
			for (int i = 0; i < dataElems.getLength(); i++) {
				Node dataElem = dataElems.item(i);
				String key = getAttributeString(dataElem);
				String val = dataElem.getTextContent();
				dataEntryMap.put(key, val);
			}
		}
		return dataEntryMap;
	}

		private void addMapContents(SortedMap<String , String> aMap, StringBuilder builder) {

			for (Entry<String, String> entry : aMap.entrySet()) {
				builder
							.append(entry.getKey())
							.append(entry.getValue());
			}
		}

	private String getLocalFolder() {
		return "test/" + getPackageName().replace(".", "/");
	}

	private String getPackageName() {
		return getClass().getPackage().getName();
	}

	private Path getDiagramFile() {
		return Paths.get(getLocalFolder(), getClass().getSimpleName() + ".java");
	}

	private Path getTempPumlFile() {
		return Paths.get(getLocalFolder(), getClass().getSimpleName() + ".puml");
	}

	private Path getTempGraphmlFile() {
		return Paths.get(getLocalFolder(), getClass().getSimpleName() + ".graphml");
	}

	private String runPlantUML(String expectedDescription) throws IOException, UnsupportedEncodingException, InterruptedException {
		final String diagramText = readStringFromSourceFile(getDiagramFile(), TRIPLE_QUOTE, TRIPLE_QUOTE);

		// i'd like to pass the reference to the original source file into the processing
		// did not find an easy way to add the suggestedFile into the processing chain
		// ==> use workaround to create a temp puml file and call similar to main processing
		final Path tmpTestFile = getTempPumlFile().toAbsolutePath();
		final Path rootDir = Paths.get("test").toAbsolutePath();

		final FileOutputStream tmpTestFileOutStream = new FileOutputStream(tmpTestFile.toString());
		tmpTestFileOutStream.write(diagramText.getBytes(UTF_8));
		tmpTestFileOutStream.close();

		// Essential activities when calling graphML export from command line
		// Reverse Engineered from Run.java

		String[] args = new String[] {"-tgraphml", "-graphml-root-dir", rootDir.toString() , tmpTestFile.toString() };
		final Option option = new Option(args);
		final File outputDir = null;
		final File f = tmpTestFile.toFile();
		SourceFileReader sourceFileReader = new SourceFileReader(option.getDefaultDefines(f), f, outputDir, option.getConfig(),
						option.getCharset(), option.getFileFormatOption());
		sourceFileReader.setCheckMetadata(option.isCheckMetadata());
		final List<GeneratedImage> result = sourceFileReader.getGeneratedImages();
		// Assume that we always have one image per test case
		String descriptionWithFileName = result.get(0).getDescription();
		String description = stripFileNameFromDescription(descriptionWithFileName);
		assertEquals(expectedDescription, description, "Bad description");


		final Path tmpGraphmlFile = getTempGraphmlFile();
		final String xml = String.join("\n", Files.readAllLines(Paths.get(tmpGraphmlFile.toString())));
		return xml;
	}

	private String stripFileNameFromDescription(String description) {
		return description.substring(description.indexOf(']')+2); // +2 due to space after ']'
	}

	private String readStringFromSourceFile(Path path, String startMarker, String endMarker) throws IOException {
		assertTrue(Files.exists(path), "Cannot find " + path);
		assertTrue(Files.isReadable(path), "Cannot read " + path);
		final List<String> allLines = Files.readAllLines(path, UTF_8);
		final int first = allLines.indexOf(startMarker);
		final int last = allLines.lastIndexOf(endMarker);
		assertTrue(first != -1);
		assertTrue(last != -1);
		assertTrue(last > first);
		return packString(allLines.subList(first + 1, last));
	}

	private String packString(Collection<String> list) {
		final StringBuilder sb = new StringBuilder();
		for (String s : list) {
			sb.append(s);
			sb.append("\n");
		}
		return sb.toString();
	}

}
