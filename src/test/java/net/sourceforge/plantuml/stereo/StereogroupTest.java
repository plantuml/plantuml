package net.sourceforge.plantuml.stereo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class StereogroupTest {

	@Test
	void testGetLabelsWithSingleStereotype() {
		Stereogroup stereogroup = Stereogroup.build("<<foo>>");
		List<String> result = stereogroup.getLabels();
		assertEquals(List.of("foo"), result);
	}

	@Test
	void testGetLabelsWithTwoStereotypes() {
		Stereogroup stereogroup = Stereogroup.build("<<foo>> <<dummy>>");
		List<String> result = stereogroup.getLabels();
		assertEquals(List.of("foo", "dummy"), result);
	}

	@Test
	void testGetLabelsWithThreeStereotypes() {
		Stereogroup stereogroup = Stereogroup.build("<<alpha>> <<beta>> <<gamma>>");
		List<String> result = stereogroup.getLabels();
		assertEquals(List.of("alpha", "beta", "gamma"), result);
	}

	@Test
	void testGetLabelsWithNullDefinition() {
		Stereogroup stereogroup = Stereogroup.build((String) null);
		List<String> result = stereogroup.getLabels();
		assertTrue(result.isEmpty());
	}

	@Test
	void testGetLabelsWithStereotypesWithoutSpaces() {
		Stereogroup stereogroup = Stereogroup.build("<<foo>><<bar>>");
		List<String> result = stereogroup.getLabels();
		assertEquals(List.of("foo", "bar"), result);
	}

	@Test
	void testGetLabelsWithStereotypeContainingSpaces() {
		Stereogroup stereogroup = Stereogroup.build("<<hello world>>");
		List<String> result = stereogroup.getLabels();
		assertEquals(List.of("hello world"), result);
	}

}
