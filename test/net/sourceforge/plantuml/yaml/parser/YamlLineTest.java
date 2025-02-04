package net.sourceforge.plantuml.yaml.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class YamlLineTest {

	@Test
	void testSimple() {
		YamlLine yamlLine = YamlLine.build("  name: vibrant").get();
		assertEquals(2, yamlLine.getIndent());
		assertEquals("name", yamlLine.getKey());
		assertEquals("vibrant", yamlLine.getValue());
	}

	@Test
	void testQuotedKey() {
		YamlLine yamlLine = YamlLine.build("  \"name\": vibrant").get();
		assertEquals(2, yamlLine.getIndent());
		assertEquals("name", yamlLine.getKey());
		assertEquals("vibrant", yamlLine.getValue());
	}

	@Test
	void testQuotedValue() {
		YamlLine yamlLine = YamlLine.build("  name: \"vibrant\"").get();
		assertEquals(2, yamlLine.getIndent());
		assertEquals("name", yamlLine.getKey());
		assertEquals("vibrant", yamlLine.getValue());
	}

	@Test
	void testQuotedKeyAndValue() {
		YamlLine yamlLine = YamlLine.build("  \"name\": \"vibrant\"").get();
		assertEquals(2, yamlLine.getIndent());
		assertEquals("name", yamlLine.getKey());
		assertEquals("vibrant", yamlLine.getValue());
	}

	@Test
	void testKeyWithSpaces() {
		YamlLine yamlLine = YamlLine.build("  \"full name\": \"John Doe\"").get();
		assertEquals(2, yamlLine.getIndent());
		assertEquals("full name", yamlLine.getKey());
		assertEquals("John Doe", yamlLine.getValue());
	}

	@Test
	void testValueWithComment() {
		YamlLine yamlLine = YamlLine.build("  name: vibrant # primary color").get();
		assertEquals(2, yamlLine.getIndent());
		assertEquals("vibrant", yamlLine.getValue());
		assertEquals("name", yamlLine.getKey());
	}

	@Test
	void testCommentLineOnly() {
		assertFalse(YamlLine.build("  # This is a comment").isPresent());
	}

	@Test
	void testNoIndent() {
		YamlLine yamlLine = YamlLine.build("name: vibrant").get();
		assertEquals(0, yamlLine.getIndent());
		assertEquals("name", yamlLine.getKey());
		assertEquals("vibrant", yamlLine.getValue());
	}

	@Test
	void testExtraSpacesAroundColon() {
		YamlLine yamlLine = YamlLine.build("  name  :    vibrant  ").get();
		assertEquals(2, yamlLine.getIndent());
		assertEquals("name", yamlLine.getKey());
		assertEquals("vibrant", yamlLine.getValue());
	}

	@Test
	void testMissingValue() {
		YamlLine yamlLine = YamlLine.build("  name:").get();
		assertEquals(2, yamlLine.getIndent());
		assertEquals("name", yamlLine.getKey());
		assertEquals("", yamlLine.getValue());
	}

	@Test
	void testSharpInString() {
		YamlLine yamlLine = YamlLine.build("  \"full name\": \"John # Doe\"").get();
		assertEquals(2, yamlLine.getIndent());
		assertEquals("full name", yamlLine.getKey());
		assertEquals("John # Doe", yamlLine.getValue());
	}

	@Test
	void testMissingColon() {
		assertFalse(YamlLine.build("  name vibrant").isPresent());
	}
	
	
	@Test
	void testIsListItem() {
		YamlLine yamlLine = YamlLine.build("    - primary: ffcc00").get();
		assertEquals(6, yamlLine.getIndent());
		assertTrue(yamlLine.isListItem());
		assertEquals("primary", yamlLine.getKey());
		assertEquals("ffcc00", yamlLine.getValue());
	}

}
