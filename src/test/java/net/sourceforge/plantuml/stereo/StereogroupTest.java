package net.sourceforge.plantuml.stereo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColorSet;

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

	@Test
	void testGetInnerColorsFromASemicolonSeparatedPropertyList() throws Exception {
		// Stereogroup#getInnerColors used to hand a "Prop:value;Prop:value;" label straight to
		// the legacy character-level net.sourceforge.plantuml.style.parser.StyleParser's own
		// parseSingleLine(), which can read a bare property list at its Context's still-empty
		// root, with no selector at all. RawStyleParser has no such root-level property syntax
		// (a stack-empty property declaration always throws), so getInnerColors now wraps the
		// label in a throwaway selector before handing it to StyleLoader#parseStyleText instead
		// -- this pins down that the two remaining colors this call site ever reads
		// (BackGroundColor, FontColor) still come back correctly, spaces around ':'/';' included.
		final HColorSet colorSet = HColorSet.instance();
		final Stereogroup stereogroup = Stereogroup.build("<<FontColor : red ; BackGroundColor:blue;>>");

		final Colors colors = stereogroup.getInnerColors(colorSet);

		assertEquals(colorSet.getColorOrNull("red"), colors.getColor(ColorType.TEXT));
		assertEquals(colorSet.getColorOrNull("blue"), colors.getColor(ColorType.BACK));
	}

}
