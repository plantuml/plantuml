package net.sourceforge.plantuml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.awt.Color;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.dot.DotSplines;
import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.Rankdir;
import net.sourceforge.plantuml.nio.PathSystem;
import net.sourceforge.plantuml.preproc.ConfigurationStore;
import net.sourceforge.plantuml.skin.ActorStyle;
import net.sourceforge.plantuml.skin.ComponentStyle;
import net.sourceforge.plantuml.skin.CornerParam;
import net.sourceforge.plantuml.skin.LineParam;
import net.sourceforge.plantuml.skin.Padder;
import net.sourceforge.plantuml.skin.PaddingParam;
import net.sourceforge.plantuml.skin.Pragma;
import net.sourceforge.plantuml.skin.SkinParam;
import net.sourceforge.plantuml.skin.SplitParam;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.LengthAdjust;
import net.sourceforge.plantuml.svek.ConditionEndStyle;
import net.sourceforge.plantuml.svek.ConditionStyle;
import net.sourceforge.plantuml.svek.PackageStyle;
import net.sourceforge.plantuml.text.Guillemet;
import net.sourceforge.plantuml.tikz.TikzFontDistortion;

class SkinParamTest {

	//
	// Test Cases
	//

	/**
	 * A long and verbose test method! But it helps us to avoid accidentally
	 * changing a default style.
	 */
	@ParameterizedTest
	@EnumSource(DiagramType.class)
	public void testDefaultValues(DiagramType diagramType) {

		final SkinParam skinParam = SkinParam.create(Collections.emptyMap(), PathSystem.fetch(), diagramType,
				Pragma.createEmpty(), ConfigurationStore.createEmpty());
		final Stereotype fooStereotype = Stereotype.build("<<foo>>");

		assertEquals(ActorStyle.STICKMAN, skinParam.actorStyle());

		assertTrue(skinParam.getAllSpriteNames().isEmpty());

		assertEquals(HColors.WHITE, skinParam.getBackgroundColor());

		assertEquals('\0', skinParam.getCircledCharacter(fooStereotype));

		assertEquals(11, skinParam.getCircledCharacterRadius());

		assertEquals(10, skinParam.classAttributeIconSize());

		assertEquals(0, skinParam.colorArrowSeparationSpace());

		// assertEquals(ColorMapper.IDENTITY, skinParam.getColorMapper());

		assertEquals(ComponentStyle.UML2, skinParam.componentStyle());

		assertEquals(ConditionEndStyle.DIAMOND, skinParam.getConditionEndStyle());

		assertEquals(ConditionStyle.INSIDE_HEXAGON, skinParam.getConditionStyle());

		assertEquals("plantuml.skin", skinParam.getDefaultSkin());

		assertEquals(HorizontalAlignment.LEFT, skinParam.getDefaultTextAlignment(HorizontalAlignment.LEFT));

		assertEquals(0, skinParam.getDiagonalCorner(CornerParam.agent, null));
		assertEquals(0, skinParam.getDiagonalCorner(CornerParam.archimate, null));
		assertEquals(0, skinParam.getDiagonalCorner(CornerParam.card, null));
		assertEquals(0, skinParam.getDiagonalCorner(CornerParam.component, null));
		assertEquals(0, skinParam.getDiagonalCorner(CornerParam.DEFAULT, null));
		assertEquals(0, skinParam.getDiagonalCorner(CornerParam.rectangle, null));
		assertEquals(0, skinParam.getDiagonalCorner(CornerParam.diagramBorder, null));
		assertEquals(0, skinParam.getDiagonalCorner(CornerParam.titleBorder, null));

		assertEquals(0, skinParam.getDiagonalCorner(CornerParam.agent, fooStereotype));
		assertEquals(0, skinParam.getDiagonalCorner(CornerParam.archimate, fooStereotype));
		assertEquals(0, skinParam.getDiagonalCorner(CornerParam.card, fooStereotype));
		assertEquals(0, skinParam.getDiagonalCorner(CornerParam.component, fooStereotype));
		assertEquals(0, skinParam.getDiagonalCorner(CornerParam.DEFAULT, fooStereotype));
		assertEquals(0, skinParam.getDiagonalCorner(CornerParam.diagramBorder, fooStereotype));
		assertEquals(0, skinParam.getDiagonalCorner(CornerParam.rectangle, fooStereotype));
		assertEquals(0, skinParam.getDiagonalCorner(CornerParam.titleBorder, fooStereotype));

		assertFalse(skinParam.displayGenericWithOldFashion());

		assertEquals(DotSplines.SPLINES, skinParam.getDotSplines());

		assertEquals(96, skinParam.getDpi());

		assertFalse(skinParam.fixCircleLabelOverlapping());

		assertFalse(skinParam.forceSequenceParticipantUnderlined());

		assertEquals(HColors.BLUE, skinParam.getHyperlinkColor());

		assertEquals(LengthAdjust.SPACING, skinParam.getlengthAdjust());

		assertEquals(Integer.MAX_VALUE, skinParam.groupInheritance());

		assertEquals(Guillemet.GUILLEMET, skinParam.guillemet());

		// assertFalse(skinParam.handwrittenTOBEDELETED());

		assertNull(skinParam.hoverPathColor());

		assertFalse(skinParam.isUseVizJs());

		final LineBreakStrategy lineBreakStrategy = skinParam.maxMessageSize();
		assertFalse(lineBreakStrategy.isAuto());
		assertEquals(0, lineBreakStrategy.getMaxWidth());

		assertEquals(-1, skinParam.maxAsciiMessageLength());

		// assertEquals(0, skinParam.minClassWidth());

		assertEquals("monospaced", skinParam.getMonospacedFamily());

		assertEquals(0, skinParam.getNodesep());

		assertEquals(PackageStyle.FOLDER, skinParam.packageStyle());

		skinParam.getPadding().isZero();
		skinParam.getPaddingTOBEREMOVED(PaddingParam.BOX).isZero();
		skinParam.getPaddingTOBEREMOVED(PaddingParam.PARTICIPANT).isZero();

		assertEquals("none", skinParam.getPreserveAspectRatio());

		assertEquals(Rankdir.TOP_TO_BOTTOM, skinParam.getRankdir());

		assertEquals(0, skinParam.getRanksep());

		assertFalse(skinParam.responseMessageBelowArrow());

		assertEquals(0, skinParam.getRoundCorner(CornerParam.agent, null));
		assertEquals(0, skinParam.getRoundCorner(CornerParam.archimate, null));
		assertEquals(0, skinParam.getRoundCorner(CornerParam.card, null));
		assertEquals(0, skinParam.getRoundCorner(CornerParam.component, null));
		assertEquals(0, skinParam.getRoundCorner(CornerParam.DEFAULT, null));
		assertEquals(0, skinParam.getRoundCorner(CornerParam.rectangle, null));
		assertEquals(0, skinParam.getRoundCorner(CornerParam.diagramBorder, null));
		assertEquals(0, skinParam.getRoundCorner(CornerParam.titleBorder, null));

		assertEquals(0, skinParam.getRoundCorner(CornerParam.agent, fooStereotype));
		assertEquals(0, skinParam.getRoundCorner(CornerParam.archimate, fooStereotype));
		assertEquals(0, skinParam.getRoundCorner(CornerParam.card, fooStereotype));
		assertEquals(0, skinParam.getRoundCorner(CornerParam.component, fooStereotype));
		assertEquals(0, skinParam.getRoundCorner(CornerParam.DEFAULT, fooStereotype));
		assertEquals(0, skinParam.getRoundCorner(CornerParam.diagramBorder, fooStereotype));
		assertEquals(0, skinParam.getRoundCorner(CornerParam.rectangle, fooStereotype));
		assertEquals(0, skinParam.getRoundCorner(CornerParam.titleBorder, fooStereotype));

		assertFalse(skinParam.sameClassWidth());

		assertEquals(Padder.NONE, skinParam.sequenceDiagramPadder());

		assertTrue(skinParam.shadowing(null));
		assertTrue(skinParam.shadowing(fooStereotype));

		assertTrue(skinParam.shadowingForNote(null));
		assertTrue(skinParam.shadowingForNote(fooStereotype));

		final SplitParam splitParam = skinParam.getSplitParam();
		assertNull(splitParam.getBorderColor());
		assertNull(splitParam.getExternalColor());
		assertEquals(0, splitParam.getExternalMargin());

		assertEquals(HorizontalAlignment.CENTER, skinParam.getStereotypeAlignment());

		assertTrue(skinParam.stereotypePositionTop());

		assertFalse(skinParam.strictUmlStyle());

		assertTrue(skinParam.svgDimensionStyle());

		assertEquals("_top", skinParam.getSvgLinkTarget());

		assertEquals(0, skinParam.swimlaneWidth());

		final LineBreakStrategy swimlaneWrapTitleWidth = skinParam.swimlaneWrapTitleWidth();
		assertFalse(swimlaneWrapTitleWidth.isAuto());
		assertEquals(0, swimlaneWrapTitleWidth.getMaxWidth());

		assertEquals(8, skinParam.getTabSize());

		assertNull(skinParam.getThickness(LineParam.activityBorder, null));
		assertNull(skinParam.getThickness(LineParam.agentBorder, null));
		assertNull(skinParam.getThickness(LineParam.archimateBorder, null));
		assertNull(skinParam.getThickness(LineParam.arrow, null));
		assertNull(skinParam.getThickness(LineParam.cardBorder, null));
		assertNull(skinParam.getThickness(LineParam.classBorder, null));
		assertNull(skinParam.getThickness(LineParam.componentBorder, null));
		assertNull(skinParam.getThickness(LineParam.designedDomainBorder, null));
		assertNull(skinParam.getThickness(LineParam.diagramBorder, null));
		assertNull(skinParam.getThickness(LineParam.domainBorder, null));
		assertNull(skinParam.getThickness(LineParam.hexagonBorder, null));
		assertNull(skinParam.getThickness(LineParam.legendBorder, null));
		assertNull(skinParam.getThickness(LineParam.machineBorder, null));
		assertNull(skinParam.getThickness(LineParam.noteBorder, null));
		assertNull(skinParam.getThickness(LineParam.objectBorder, null));
		assertNull(skinParam.getThickness(LineParam.packageBorder, null));
		assertNull(skinParam.getThickness(LineParam.partitionBorder, null));
		assertNull(skinParam.getThickness(LineParam.queueBorder, null));
		assertNull(skinParam.getThickness(LineParam.rectangleBorder, null));
		assertNull(skinParam.getThickness(LineParam.requirementBorder, null));
		assertNull(skinParam.getThickness(LineParam.sequenceActorBorder, null));
		assertNull(skinParam.getThickness(LineParam.sequenceArrow, null));
		assertNull(skinParam.getThickness(LineParam.sequenceDividerBorder, null));
		assertNull(skinParam.getThickness(LineParam.sequenceGroupBorder, null));
		assertNull(skinParam.getThickness(LineParam.sequenceLifeLineBorder, null));
		assertNull(skinParam.getThickness(LineParam.sequenceParticipantBorder, null));
		assertNull(skinParam.getThickness(LineParam.sequenceReferenceBorder, null));
		assertNull(skinParam.getThickness(LineParam.swimlaneBorder, null));
		assertNull(skinParam.getThickness(LineParam.titleBorder, null));
		assertNull(skinParam.getThickness(LineParam.usecaseBorder, null));

		assertNull(skinParam.getThickness(LineParam.activityBorder, fooStereotype));
		assertNull(skinParam.getThickness(LineParam.agentBorder, fooStereotype));
		assertNull(skinParam.getThickness(LineParam.archimateBorder, fooStereotype));
		assertNull(skinParam.getThickness(LineParam.arrow, fooStereotype));
		assertNull(skinParam.getThickness(LineParam.cardBorder, fooStereotype));
		assertNull(skinParam.getThickness(LineParam.classBorder, fooStereotype));
		assertNull(skinParam.getThickness(LineParam.componentBorder, fooStereotype));
		assertNull(skinParam.getThickness(LineParam.designedDomainBorder, fooStereotype));
		assertNull(skinParam.getThickness(LineParam.diagramBorder, fooStereotype));
		assertNull(skinParam.getThickness(LineParam.domainBorder, fooStereotype));
		assertNull(skinParam.getThickness(LineParam.hexagonBorder, fooStereotype));
		assertNull(skinParam.getThickness(LineParam.legendBorder, fooStereotype));
		assertNull(skinParam.getThickness(LineParam.machineBorder, fooStereotype));
		assertNull(skinParam.getThickness(LineParam.noteBorder, fooStereotype));
		assertNull(skinParam.getThickness(LineParam.objectBorder, fooStereotype));
		assertNull(skinParam.getThickness(LineParam.packageBorder, fooStereotype));
		assertNull(skinParam.getThickness(LineParam.partitionBorder, fooStereotype));
		assertNull(skinParam.getThickness(LineParam.queueBorder, fooStereotype));
		assertNull(skinParam.getThickness(LineParam.rectangleBorder, fooStereotype));
		assertNull(skinParam.getThickness(LineParam.requirementBorder, fooStereotype));
		assertNull(skinParam.getThickness(LineParam.sequenceActorBorder, fooStereotype));
		assertNull(skinParam.getThickness(LineParam.sequenceArrow, fooStereotype));
		assertNull(skinParam.getThickness(LineParam.sequenceDividerBorder, fooStereotype));
		assertNull(skinParam.getThickness(LineParam.sequenceGroupBorder, fooStereotype));
		assertNull(skinParam.getThickness(LineParam.sequenceLifeLineBorder, fooStereotype));
		assertNull(skinParam.getThickness(LineParam.sequenceParticipantBorder, fooStereotype));
		assertNull(skinParam.getThickness(LineParam.sequenceReferenceBorder, fooStereotype));
		assertNull(skinParam.getThickness(LineParam.swimlaneBorder, fooStereotype));
		assertNull(skinParam.getThickness(LineParam.titleBorder, fooStereotype));
		assertNull(skinParam.getThickness(LineParam.usecaseBorder, fooStereotype));

		final TikzFontDistortion tikzFontDistortion = skinParam.getTikzFontDistortion();
		assertEquals(4.0, tikzFontDistortion.getDistortion());
		assertEquals(1.20, tikzFontDistortion.getMagnify());

		assertFalse(skinParam.useOctagonForActivity(null));
		assertFalse(skinParam.useOctagonForActivity(fooStereotype));

		assertFalse(skinParam.useRankSame());

		assertFalse(skinParam.useSwimlanes(diagramType));

		assertNotNull(skinParam.useUnderlineForHyperlink());
	}

	@Test
	public void test_circledCharacterRadius() {
		final SkinParam skinParam = createSkinParam("circledCharacterRadius", "123");
		assertEquals(123, skinParam.getCircledCharacterRadius());
	}

	@Test
	public void test_classAttributeIconSize() {
		final SkinParam skinParam = createSkinParam("classAttributeIconSize", "123");
		assertEquals(123, skinParam.classAttributeIconSize());
	}

	@Test
	public void test_defaultMonospacedFontName() {
		final SkinParam skinParam = createSkinParam("defaultMonospacedFontName", "foo");
		assertEquals("foo", skinParam.getMonospacedFamily());
	}

	@Test
	public void test_dpi() {
		final SkinParam skinParam = createSkinParam("dpi", "123");
		assertEquals(123, skinParam.getDpi());
	}

	@ParameterizedTest
	@CsvSource({ "true,        true", "tRUe,        true", "TRUE,        true", "other_value, false", })
	public void test_fixCircleLabelOverlapping(String paramValue, boolean expected) {
		final SkinParam skinParam = createSkinParam("fixCircleLabelOverlapping", paramValue);
		assertEquals(expected, skinParam.fixCircleLabelOverlapping());
	}

	@ParameterizedTest
	@CsvSource({ "old,         true", "oLd,         true", "OLD,         true", "other_value, false", })
	public void test_genericDisplay(String paramValue, boolean expected) {
		final SkinParam skinParam = createSkinParam("genericDisplay", paramValue);
		assertEquals(expected, skinParam.displayGenericWithOldFashion());
	}

	@ParameterizedTest
	@CsvSource({ "0, MAX_VALUE", "1, MAX_VALUE", "2, 2", "123, 123" })
	public void test_groupInheritance(String paramValue, String expectedValue) {
		final SkinParam skinParam = createSkinParam("groupInheritance", paramValue);
		assertEquals(intFromCsv(expectedValue), skinParam.groupInheritance());
	}

//	@ParameterizedTest
//	@CsvSource({ "true,        true", "tRUe,        true", "TRUE,        true", "other_value, false", })
//	public void test_handwritten(String paramValue, boolean expected) {
//		final SkinParam skinParam = createSkinParam("handwritten", paramValue);
//		assertEquals(expected, skinParam.handwrittenTOBEDELETED());
//	}

	@ParameterizedTest
	@CsvSource({ "false,       false", "fALSe,       false", "FALSE,       false", "other_value, true", })
	public void test_hyperlinkUnderline(String paramValue, boolean expected) {
		final SkinParam skinParam = createSkinParam("hyperlinkUnderline", paramValue);
		if (expected)
			assertNotNull(skinParam.useUnderlineForHyperlink());
		else
			assertNull(skinParam.useUnderlineForHyperlink());
	}

	@Test
	public void test_maxAsciiMessageLength() {
		final SkinParam skinParam = createSkinParam("maxAsciiMessageLength", "123");
		assertEquals(123, skinParam.maxAsciiMessageLength());
	}

//	@Test
//	public void test_minClassWidth() {
//		final SkinParam skinParam = createSkinParam("minClassWidth", "123");
//		assertEquals(123, skinParam.minClassWidth());
//	}

	@Test
	public void test_nodeSep() {
		final SkinParam skinParam = createSkinParam("nodeSep", "123");
		assertEquals(123, skinParam.getNodesep());
	}

	@Test
	public void test_preserveAspectRatio() {
		final SkinParam skinParam = createSkinParam("preserveAspectRatio", "foo");
		assertEquals("foo", skinParam.getPreserveAspectRatio());
	}

	@Test
	public void test_rankSep() {
		final SkinParam skinParam = createSkinParam("rankSep", "123");
		assertEquals(123, skinParam.getRanksep());
	}

	@ParameterizedTest
	@CsvSource({ "true,        true", "tRUe,        true", "TRUE,        true", "other_value, false", })
	public void test_responseMessageBelowArrow(String paramValue, boolean expected) {
		final SkinParam skinParam = createSkinParam("responseMessageBelowArrow", paramValue);
		assertEquals(expected, skinParam.responseMessageBelowArrow());
	}

	@ParameterizedTest
	@CsvSource({ "true,        true", "tRUe,        true", "TRUE,        true", "other_value, false", })
	public void test_sameClassWidth(String paramValue, boolean expected) {
		final SkinParam skinParam = createSkinParam("sameClassWidth", paramValue);
		assertEquals(expected, skinParam.sameClassWidth());
	}

	@ParameterizedTest
	@CsvSource({ "underline,   true", "undERLine,   true", "UNDERLINE,   true", "other_value, false", })
	public void test_sequenceParticipant(String paramValue, boolean expected) {
		final SkinParam skinParam = createSkinParam("sequenceParticipant", paramValue);
		assertEquals(expected, skinParam.forceSequenceParticipantUnderlined());
	}

	@Test
	public void testSplitParam() {
		final SkinParam skinParam = createSkinParam("pageBorderColor", "red", "pageExternalColor", "yellow",
				"pageMargin", "123");

		final SplitParam splitParam = skinParam.getSplitParam();
		assertEquals(Color.RED, splitParam.getBorderColor().toAwtColor());
		assertEquals(Color.YELLOW, splitParam.getExternalColor().toAwtColor());
		assertEquals(123, splitParam.getExternalMargin());
	}

	@ParameterizedTest
	@CsvSource({ "bottom,      false", "boTTom,      false", "BOTTOM,      false", "other_value, true", })
	public void test_stereotypePosition(String paramValue, boolean expected) {
		final SkinParam skinParam = createSkinParam("stereotypePosition", paramValue);
		assertEquals(expected, skinParam.stereotypePositionTop());
	}

	@ParameterizedTest
	@CsvSource({ "strictuml,   true", "strICTuml,   true", "STRICTUML,   true", "other_value, false", })
	public void test_style(String paramValue, boolean expected) {
		final SkinParam skinParam = createSkinParam("style", paramValue);
		assertEquals(expected, skinParam.strictUmlStyle());
	}

	@ParameterizedTest
	@CsvSource({ "false,       false", "fALSe,       false", "FALSE,       false", "other_value, true", })
	public void test_svgDimensionStyle(String paramValue, boolean expected) {
		final SkinParam skinParam = createSkinParam("svgDimensionStyle", paramValue);
		assertEquals(expected, skinParam.svgDimensionStyle());
	}

	@Test
	public void test_svgLinkTarget() {
		final SkinParam skinParam = createSkinParam("svgLinkTarget", "foo");
		assertEquals("foo", skinParam.getSvgLinkTarget());
	}

	@ParameterizedTest
	@CsvSource({
			// swimlane swimlanes expected
			"  true,        any_value,   true", "  tRUe,        any_value,   true", "  TRUE,        any_value,   true",
			"  other_value, true,        true", "  other_value, tRUe,        true", "  other_value, TRUE,        true",
			"  other_value, other_value, false", })
	public void test_swimlanes(String swimlane, String swimlanes, boolean expected) {
		final SkinParam skinParam = createSkinParam("swimlane", swimlane, "swimlanes", swimlanes);
		assertEquals(expected, skinParam.swimlanes());
	}

	@Test
	public void test_tabSize() {
		final SkinParam skinParam = createSkinParam("tabSize", "123");
		assertEquals(123, skinParam.getTabSize());
	}

	//
	// Test DSL
	//

	private SkinParam createSkinParam(String... keyValuePairs) {
		// Using SEQUENCE here is an arbitrary decision that should not affect test
		// outcome
		final SkinParam skinParam = SkinParam.create(Collections.emptyMap(), PathSystem.fetch(), DiagramType.SEQUENCE,
				Pragma.createEmpty(), ConfigurationStore.createEmpty());
		for (int i = 0; i < keyValuePairs.length; i += 2) {
			skinParam.setParam(StringUtils.goLowerCase(keyValuePairs[i]), keyValuePairs[i + 1]);
		}
		return skinParam;
	}

	private int intFromCsv(String value) {
		return value.equals("MAX_VALUE") ? Integer.MAX_VALUE : Integer.parseInt(value);
	}
}
