package net.sourceforge.plantuml.jaws;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MultilinesBlocTest {

	private static String merge(String first, String... others) {
		final MultilinesBloc block = new MultilinesBloc(0, first);
		for (String s : others)
			block.add(s);
		return block.getMerged().replace(Jaws.BLOCK_E1_NEWLINE, '|');
	}

	@Test
	void test_common_indentation_removed_with_spaces() {
		assertEquals("head|first|  second", merge("head", "    first", "      second"));
	}

	@Test
	void test_common_indentation_removed_with_tabs() {
		assertEquals("head|first|\tsecond", merge("head", "\tfirst", "\t\tsecond"));
	}

}
