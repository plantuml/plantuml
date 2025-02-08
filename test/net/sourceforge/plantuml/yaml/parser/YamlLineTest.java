package net.sourceforge.plantuml.yaml.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class YamlLineTest {

	@Test
	void testSimple() {
		YamlLine yamlLine = YamlLine.build("  name: vibrant");
		assertEquals(2, yamlLine.getIndent());
		assertEquals(YamlLineType.KEY_AND_VALUE, yamlLine.getType());
		assertEquals("name", yamlLine.getKey());
		assertEquals("vibrant", yamlLine.getValue());
		assertFalse(yamlLine.isListItem());
	}

	@Test
	void testQuotedKey() {
		YamlLine yamlLine = YamlLine.build("  \"name\": vibrant");
		assertEquals(2, yamlLine.getIndent());
		assertEquals(YamlLineType.KEY_AND_VALUE, yamlLine.getType());
		assertEquals("name", yamlLine.getKey());
		assertEquals("vibrant", yamlLine.getValue());
		assertFalse(yamlLine.isListItem());
	}

	@Test
	void testQuotedValue() {
		YamlLine yamlLine = YamlLine.build("  name: \"vibrant\"");
		assertEquals(2, yamlLine.getIndent());
		assertEquals(YamlLineType.KEY_AND_VALUE, yamlLine.getType());
		assertEquals("name", yamlLine.getKey());
		assertEquals("vibrant", yamlLine.getValue());
		assertFalse(yamlLine.isListItem());
	}

	@Test
	void testQuotedKeyAndValue() {
		YamlLine yamlLine = YamlLine.build("  \"name\": \"vibrant\"");
		assertEquals(2, yamlLine.getIndent());
		assertEquals(YamlLineType.KEY_AND_VALUE, yamlLine.getType());
		assertEquals("name", yamlLine.getKey());
		assertEquals("vibrant", yamlLine.getValue());
		assertFalse(yamlLine.isListItem());
	}

	@Test
	void testKeyWithSpaces() {
		YamlLine yamlLine = YamlLine.build("  \"full name\": \"John Doe\"");
		assertEquals(2, yamlLine.getIndent());
		assertEquals(YamlLineType.KEY_AND_VALUE, yamlLine.getType());
		assertEquals("full name", yamlLine.getKey());
		assertEquals("John Doe", yamlLine.getValue());
		assertFalse(yamlLine.isListItem());
	}

	@Test
	void testValueWithComment() {
		YamlLine yamlLine = YamlLine.build("  name: vibrant # primary color");
		assertEquals(2, yamlLine.getIndent());
		assertEquals(YamlLineType.KEY_AND_VALUE, yamlLine.getType());
		assertEquals("vibrant", yamlLine.getValue());
		assertEquals("name", yamlLine.getKey());
		assertFalse(yamlLine.isListItem());
	}

	@Test
	void testCommentLineOnly() {
		YamlLine yamlLine = YamlLine.build("  # This is a comment");
		assertEquals(YamlLineType.EMPTY_LINE, yamlLine.getType());
		assertFalse(yamlLine.isListItem());
	}

	@Test
	void testNoIndent() {
		YamlLine yamlLine = YamlLine.build("name: vibrant");
		assertEquals(0, yamlLine.getIndent());
		assertEquals("name", yamlLine.getKey());
		assertEquals(YamlLineType.KEY_AND_VALUE, yamlLine.getType());
		assertEquals("vibrant", yamlLine.getValue());
		assertFalse(yamlLine.isListItem());
	}

	@Test
	void testExtraSpacesAroundColon() {
		YamlLine yamlLine = YamlLine.build("  name  :    vibrant  ");
		assertEquals(2, yamlLine.getIndent());
		assertEquals("name", yamlLine.getKey());
		assertEquals(YamlLineType.KEY_AND_VALUE, yamlLine.getType());
		assertEquals("vibrant", yamlLine.getValue());
		assertFalse(yamlLine.isListItem());
	}

	@Test
	void testMissingValue() {
		YamlLine yamlLine = YamlLine.build("  name:");
		assertEquals(2, yamlLine.getIndent());
		assertEquals("name", yamlLine.getKey());
		assertEquals(YamlLineType.KEY_ONLY, yamlLine.getType());
		assertFalse(yamlLine.isListItem());
	}

	@Test
	void testSharpInString() {
		YamlLine yamlLine = YamlLine.build("  \"full name\": \"John # Doe\"");
		assertEquals(2, yamlLine.getIndent());
		assertEquals("full name", yamlLine.getKey());
		assertEquals(YamlLineType.KEY_AND_VALUE, yamlLine.getType());
		assertEquals("John # Doe", yamlLine.getValue());
		assertFalse(yamlLine.isListItem());
	}

	@Test
	void testMissingColon() {
		YamlLine yamlLine = YamlLine.build("  name vibrant");
		assertEquals(YamlLineType.NO_KEY_ONLY_TEXT, yamlLine.getType());
		assertEquals("name vibrant", yamlLine.getValue());
		assertFalse(yamlLine.isListItem());

	}

	@Test
	void testIsListItem() {
		YamlLine yamlLine = YamlLine.build("    - primary: ffcc00");
		assertEquals(6, yamlLine.getIndent());
		assertTrue(yamlLine.isListItem());
		assertEquals("primary", yamlLine.getKey());
		assertEquals(YamlLineType.KEY_AND_VALUE, yamlLine.getType());
		assertEquals("ffcc00", yamlLine.getValue());
	}

	@Test
	void testBlockStyle1() {
		YamlLine yamlLine = YamlLine.build("  desc: |");
		assertEquals(2, yamlLine.getIndent());
		assertEquals("desc", yamlLine.getKey());
		assertEquals(YamlLineType.KEY_AND_BLOCK_STYLE, yamlLine.getType());
		assertFalse(yamlLine.isListItem());
	}

	@Test
	void testNoBlockStyle1() {
		YamlLine yamlLine = YamlLine.build("  desc: '|'");
		assertEquals(2, yamlLine.getIndent());
		assertEquals("desc", yamlLine.getKey());
		assertEquals(YamlLineType.KEY_AND_VALUE, yamlLine.getType());
		assertEquals("|", yamlLine.getValue());
		assertFalse(yamlLine.isListItem());
	}

	@Test
	void testList01() {
		YamlLine yamlLine = YamlLine.build("- name: vibrant");
		assertEquals(2, yamlLine.getIndent());
		assertEquals("name", yamlLine.getKey());
		assertEquals("vibrant", yamlLine.getValue());
		assertTrue(yamlLine.isListItem());
		assertEquals(YamlLineType.KEY_AND_VALUE, yamlLine.getType());
	}

	@Test
	void testList02() {
		YamlLine yamlLine = YamlLine.build("  - name: vibrant");
		assertEquals(4, yamlLine.getIndent());
		assertEquals("name", yamlLine.getKey());
		assertEquals("vibrant", yamlLine.getValue());
		assertTrue(yamlLine.isListItem());
		assertEquals(YamlLineType.KEY_AND_VALUE, yamlLine.getType());
	}

	@Test
	void testList03() {
		YamlLine yamlLine = YamlLine.build("  - red");
		assertEquals(4, yamlLine.getIndent());
		assertEquals("red", yamlLine.getValue());
		assertTrue(yamlLine.isListItem());
		assertEquals(YamlLineType.PLAIN_ELEMENT_LIST, yamlLine.getType());
	}

	@Test
	void testList04() {
		YamlLine yamlLine = YamlLine.build("colors: ['red', 'blue', 'green']");
		assertEquals(0, yamlLine.getIndent());
		assertEquals(3, yamlLine.getValues().size());
		assertEquals("red", yamlLine.getValues().get(0));
		assertEquals("blue", yamlLine.getValues().get(1));
		assertEquals("green", yamlLine.getValues().get(2));
		assertFalse(yamlLine.isListItem());
		assertEquals(YamlLineType.KEY_AND_FLOW_SEQUENCE, yamlLine.getType());
	}

	@Test
	void testList05() {
		YamlLine yamlLine = YamlLine.build("colors: [red, blue, green, 'yellow', \"orange\"]");
		assertEquals(0, yamlLine.getIndent());
		assertEquals(5, yamlLine.getValues().size());
		assertEquals("red", yamlLine.getValues().get(0));
		assertEquals("blue", yamlLine.getValues().get(1));
		assertEquals("green", yamlLine.getValues().get(2));
		assertEquals("yellow", yamlLine.getValues().get(3));
		assertEquals("orange", yamlLine.getValues().get(4));
		assertFalse(yamlLine.isListItem());
		assertEquals(YamlLineType.KEY_AND_FLOW_SEQUENCE, yamlLine.getType());
	}

	@Test
	void testList06() {
		YamlLine yamlLine = YamlLine.build("colors: [red, dark blue, green, 'yel\"low', \"or\\\"ange\"]");
		assertEquals(0, yamlLine.getIndent());
		assertEquals(5, yamlLine.getValues().size());
		assertEquals("red", yamlLine.getValues().get(0));
		assertEquals("dark blue", yamlLine.getValues().get(1));
		assertEquals("green", yamlLine.getValues().get(2));
		assertEquals("yel\"low", yamlLine.getValues().get(3));
		assertEquals("or\"ange", yamlLine.getValues().get(4));
		assertFalse(yamlLine.isListItem());
		assertEquals(YamlLineType.KEY_AND_FLOW_SEQUENCE, yamlLine.getType());
	}

	@Test
	void testListDash() {
		YamlLine yamlLine = YamlLine.build("  -");
		assertEquals(3, yamlLine.getIndent());
		assertTrue(yamlLine.isListItem());
		assertEquals(YamlLineType.PLAIN_DASH, yamlLine.getType());
	}

}
