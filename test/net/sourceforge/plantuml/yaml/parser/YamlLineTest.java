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
		assertFalse(yamlLine.isListItem());
	}

	@Test
	void testQuotedKey() {
		YamlLine yamlLine = YamlLine.build("  \"name\": vibrant").get();
		assertEquals(2, yamlLine.getIndent());
		assertEquals("name", yamlLine.getKey());
		assertEquals("vibrant", yamlLine.getValue());
		assertFalse(yamlLine.isListItem());
	}

	@Test
	void testQuotedValue() {
		YamlLine yamlLine = YamlLine.build("  name: \"vibrant\"").get();
		assertEquals(2, yamlLine.getIndent());
		assertEquals("name", yamlLine.getKey());
		assertEquals("vibrant", yamlLine.getValue());
		assertFalse(yamlLine.isListItem());
	}

	@Test
	void testQuotedKeyAndValue() {
		YamlLine yamlLine = YamlLine.build("  \"name\": \"vibrant\"").get();
		assertEquals(2, yamlLine.getIndent());
		assertEquals("name", yamlLine.getKey());
		assertEquals("vibrant", yamlLine.getValue());
		assertFalse(yamlLine.isListItem());
	}

	@Test
	void testKeyWithSpaces() {
		YamlLine yamlLine = YamlLine.build("  \"full name\": \"John Doe\"").get();
		assertEquals(2, yamlLine.getIndent());
		assertEquals("full name", yamlLine.getKey());
		assertEquals("John Doe", yamlLine.getValue());
		assertFalse(yamlLine.isListItem());
	}

	@Test
	void testValueWithComment() {
		YamlLine yamlLine = YamlLine.build("  name: vibrant # primary color").get();
		assertEquals(2, yamlLine.getIndent());
		assertEquals("vibrant", yamlLine.getValue());
		assertEquals("name", yamlLine.getKey());
		assertFalse(yamlLine.isListItem());
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
		assertEquals(YamlValueType.REGULAR, yamlLine.getType());
		assertEquals("vibrant", yamlLine.getValue());
		assertFalse(yamlLine.isListItem());
	}

	@Test
	void testExtraSpacesAroundColon() {
		YamlLine yamlLine = YamlLine.build("  name  :    vibrant  ").get();
		assertEquals(2, yamlLine.getIndent());
		assertEquals("name", yamlLine.getKey());
		assertEquals(YamlValueType.REGULAR, yamlLine.getType());
		assertEquals("vibrant", yamlLine.getValue());
		assertFalse(yamlLine.isListItem());
	}

	@Test
	void testMissingValue() {
		YamlLine yamlLine = YamlLine.build("  name:").get();
		assertEquals(2, yamlLine.getIndent());
		assertEquals("name", yamlLine.getKey());
		assertEquals(YamlValueType.ABSENT, yamlLine.getType());
		assertFalse(yamlLine.isListItem());
	}

	@Test
	void testSharpInString() {
		YamlLine yamlLine = YamlLine.build("  \"full name\": \"John # Doe\"").get();
		assertEquals(2, yamlLine.getIndent());
		assertEquals("full name", yamlLine.getKey());
		assertEquals(YamlValueType.REGULAR, yamlLine.getType());
		assertEquals("John # Doe", yamlLine.getValue());
		assertFalse(yamlLine.isListItem());
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
		assertEquals(YamlValueType.REGULAR, yamlLine.getType());
		assertEquals("ffcc00", yamlLine.getValue());
	}

	@Test
	void testBlockStyle1() {
		YamlLine yamlLine = YamlLine.build("  desc: |").get();
		assertEquals(2, yamlLine.getIndent());
		assertEquals("desc", yamlLine.getKey());
		assertEquals(YamlValueType.BLOCK_STYLE, yamlLine.getType());
		assertFalse(yamlLine.isListItem());
	}

	@Test
	void testNoBlockStyle1() {
		YamlLine yamlLine = YamlLine.build("  desc: '|'").get();
		assertEquals(2, yamlLine.getIndent());
		assertEquals("desc", yamlLine.getKey());
		assertEquals(YamlValueType.REGULAR, yamlLine.getType());
		assertEquals("|", yamlLine.getValue());
		assertFalse(yamlLine.isListItem());
	}

	@Test
	void testList01() {
		YamlLine yamlLine = YamlLine.build("- name: vibrant").get();
		assertEquals(2, yamlLine.getIndent());
		assertEquals("name", yamlLine.getKey());
		assertEquals("vibrant", yamlLine.getValue());
		assertTrue(yamlLine.isListItem());
		assertEquals(YamlValueType.REGULAR, yamlLine.getType());
	}

	@Test
	void testList02() {
		YamlLine yamlLine = YamlLine.build("  - name: vibrant").get();
		assertEquals(4, yamlLine.getIndent());
		assertEquals("name", yamlLine.getKey());
		assertEquals("vibrant", yamlLine.getValue());
		assertTrue(yamlLine.isListItem());
		assertEquals(YamlValueType.REGULAR, yamlLine.getType());
	}

	@Test
	void testList03() {
		YamlLine yamlLine = YamlLine.build("  - red").get();
		assertEquals(4, yamlLine.getIndent());
		assertEquals("red", yamlLine.getValue());
		assertTrue(yamlLine.isListItem());
		assertEquals(YamlValueType.PLAIN_ELEMENT_LIST, yamlLine.getType());
	}

	// @Test
	void testList04() {
		YamlLine yamlLine = YamlLine.build("colors: ['red', 'blue', 'green']").get();
		assertEquals(0, yamlLine.getIndent());
		assertEquals(3, yamlLine.getValues().size());
		assertEquals("red", yamlLine.getValues().get(0));
		assertEquals("blue", yamlLine.getValues().get(1));
		assertEquals("green", yamlLine.getValues().get(2));
		assertFalse(yamlLine.isListItem());
		assertEquals(YamlValueType.FLOW_SEQUENCE, yamlLine.getType());
	}

}
