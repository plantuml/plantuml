package net.sourceforge.plantuml;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Color;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import net.sourceforge.plantuml.dot.DotSplines;
import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.svg.LengthAdjust;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.Rankdir;
import net.sourceforge.plantuml.skin.ActorStyle;
import net.sourceforge.plantuml.skin.ComponentStyle;
import net.sourceforge.plantuml.skin.CornerParam;
import net.sourceforge.plantuml.skin.LineParam;
import net.sourceforge.plantuml.skin.Padder;
import net.sourceforge.plantuml.skin.PaddingParam;
import net.sourceforge.plantuml.skin.SkinParam;
import net.sourceforge.plantuml.skin.SplitParam;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.svek.ConditionEndStyle;
import net.sourceforge.plantuml.svek.ConditionStyle;
import net.sourceforge.plantuml.svek.PackageStyle;
import net.sourceforge.plantuml.text.Guillemet;

class SkinParamTest {

	//
	// Test Cases
	//

	/**
	 * A long and verbose test method! But it helps us to avoid accidentally
	 * changing a default style.
	 */
	@ParameterizedTest
	@EnumSource(UmlDiagramType.class)
	public void testDefaultValues(UmlDiagramType umlDiagramType) {

		final SkinParam skinParam = SkinParam.create(umlDiagramType);
		final Stereotype fooStereotype = Stereotype.build("<<foo>>");

		assertThat(skinParam.actorStyle()).isEqualTo(ActorStyle.STICKMAN);

		assertThat(skinParam.getAllSpriteNames()).isEmpty();

		assertThat(skinParam.getBackgroundColor()).isEqualTo(HColors.WHITE);

		assertThat(skinParam.getCircledCharacter(fooStereotype)).isEqualTo('\0');

		assertThat(skinParam.getCircledCharacterRadius()).isEqualTo(11);

		assertThat(skinParam.classAttributeIconSize()).isEqualTo(10);

		assertThat(skinParam.colorArrowSeparationSpace()).isZero();

		// assertThat(skinParam.getColorMapper()).isEqualTo(ColorMapper.IDENTITY);

		assertThat(skinParam.componentStyle()).isEqualTo(ComponentStyle.UML2);

		assertThat(skinParam.getConditionEndStyle()).isEqualTo(ConditionEndStyle.DIAMOND);

		assertThat(skinParam.getConditionStyle()).isEqualTo(ConditionStyle.INSIDE_HEXAGON);

		assertThat(skinParam.getDefaultSkin()).isEqualTo("plantuml.skin");

		assertThat(skinParam.getDefaultTextAlignment(HorizontalAlignment.LEFT)).isEqualTo(HorizontalAlignment.LEFT);

		assertThat(skinParam.getDiagonalCorner(CornerParam.agent, null)).isZero();
		assertThat(skinParam.getDiagonalCorner(CornerParam.archimate, null)).isZero();
		assertThat(skinParam.getDiagonalCorner(CornerParam.card, null)).isZero();
		assertThat(skinParam.getDiagonalCorner(CornerParam.component, null)).isZero();
		assertThat(skinParam.getDiagonalCorner(CornerParam.DEFAULT, null)).isZero();
		assertThat(skinParam.getDiagonalCorner(CornerParam.rectangle, null)).isZero();
		assertThat(skinParam.getDiagonalCorner(CornerParam.diagramBorder, null)).isZero();
		assertThat(skinParam.getDiagonalCorner(CornerParam.titleBorder, null)).isZero();

		assertThat(skinParam.getDiagonalCorner(CornerParam.agent, fooStereotype)).isZero();
		assertThat(skinParam.getDiagonalCorner(CornerParam.archimate, fooStereotype)).isZero();
		assertThat(skinParam.getDiagonalCorner(CornerParam.card, fooStereotype)).isZero();
		assertThat(skinParam.getDiagonalCorner(CornerParam.component, fooStereotype)).isZero();
		assertThat(skinParam.getDiagonalCorner(CornerParam.DEFAULT, fooStereotype)).isZero();
		assertThat(skinParam.getDiagonalCorner(CornerParam.diagramBorder, fooStereotype)).isZero();
		assertThat(skinParam.getDiagonalCorner(CornerParam.rectangle, fooStereotype)).isZero();
		assertThat(skinParam.getDiagonalCorner(CornerParam.titleBorder, fooStereotype)).isZero();

		assertThat(skinParam.displayGenericWithOldFashion()).isFalse();

		assertThat(skinParam.getDotSplines()).isEqualTo(DotSplines.SPLINES);

		assertThat(skinParam.getDpi()).isEqualTo(96);

		assertThat(skinParam.fixCircleLabelOverlapping()).isFalse();

		assertThat(skinParam.forceSequenceParticipantUnderlined()).isFalse();

		assertThat(skinParam.getHyperlinkColor()).isEqualTo(HColors.BLUE);

		assertThat(skinParam.getlengthAdjust()).isEqualTo(LengthAdjust.SPACING);

		assertThat(skinParam.groupInheritance()).isEqualTo(Integer.MAX_VALUE);

		assertThat(skinParam.guillemet()).isEqualTo(Guillemet.GUILLEMET);

		assertThat(skinParam.handwritten()).isFalse();

		assertThat(skinParam.hoverPathColor()).isNull();

		assertThat(skinParam.isUseVizJs()).isFalse();

		final LineBreakStrategy lineBreakStrategy = skinParam.maxMessageSize();
		assertThat(lineBreakStrategy.isAuto()).isFalse();
		assertThat(lineBreakStrategy.getMaxWidth()).isZero();

		assertThat(skinParam.maxAsciiMessageLength()).isEqualTo(-1);

		// assertThat(skinParam.minClassWidth()).isZero();

		assertThat(skinParam.getMonospacedFamily()).isEqualTo("monospaced");

		assertThat(skinParam.getNodesep()).isZero();

		assertThat(skinParam.packageStyle()).isEqualTo(PackageStyle.FOLDER);

		assertThat(skinParam.getPadding()).isZero();
		assertThat(skinParam.getPadding(PaddingParam.BOX)).isZero();
		assertThat(skinParam.getPadding(PaddingParam.PARTICIPANT)).isZero();

		assertThat(skinParam.getPreserveAspectRatio()).isEqualTo("none");

		assertThat(skinParam.getRankdir()).isEqualTo(Rankdir.TOP_TO_BOTTOM);

		assertThat(skinParam.getRanksep()).isZero();

		assertThat(skinParam.responseMessageBelowArrow()).isFalse();

		assertThat(skinParam.getRoundCorner(CornerParam.agent, null)).isZero();
		assertThat(skinParam.getRoundCorner(CornerParam.archimate, null)).isZero();
		assertThat(skinParam.getRoundCorner(CornerParam.card, null)).isZero();
		assertThat(skinParam.getRoundCorner(CornerParam.component, null)).isZero();
		assertThat(skinParam.getRoundCorner(CornerParam.DEFAULT, null)).isZero();
		assertThat(skinParam.getRoundCorner(CornerParam.rectangle, null)).isZero();
		assertThat(skinParam.getRoundCorner(CornerParam.diagramBorder, null)).isZero();
		assertThat(skinParam.getRoundCorner(CornerParam.titleBorder, null)).isZero();

		assertThat(skinParam.getRoundCorner(CornerParam.agent, fooStereotype)).isZero();
		assertThat(skinParam.getRoundCorner(CornerParam.archimate, fooStereotype)).isZero();
		assertThat(skinParam.getRoundCorner(CornerParam.card, fooStereotype)).isZero();
		assertThat(skinParam.getRoundCorner(CornerParam.component, fooStereotype)).isZero();
		assertThat(skinParam.getRoundCorner(CornerParam.DEFAULT, fooStereotype)).isZero();
		assertThat(skinParam.getRoundCorner(CornerParam.diagramBorder, fooStereotype)).isZero();
		assertThat(skinParam.getRoundCorner(CornerParam.rectangle, fooStereotype)).isZero();
		assertThat(skinParam.getRoundCorner(CornerParam.titleBorder, fooStereotype)).isZero();

		assertThat(skinParam.sameClassWidth()).isFalse();

		assertThat(skinParam.sequenceDiagramPadder()).isEqualTo(Padder.NONE);

		assertThat(skinParam.shadowing(null)).isTrue();
		assertThat(skinParam.shadowing(fooStereotype)).isTrue();

		assertThat(skinParam.shadowingForNote(null)).isTrue();
		assertThat(skinParam.shadowingForNote(fooStereotype)).isTrue();

		final SplitParam splitParam = skinParam.getSplitParam();
		assertThat(splitParam.getBorderColor()).isNull();
		assertThat(splitParam.getExternalColor()).isNull();
		assertThat(splitParam.getExternalMargin()).isZero();

		assertThat(skinParam.getStereotypeAlignment()).isEqualTo(HorizontalAlignment.CENTER);

		assertThat(skinParam.stereotypePositionTop()).isTrue();

		assertThat(skinParam.strictUmlStyle()).isFalse();

		assertThat(skinParam.svgDimensionStyle()).isTrue();

		assertThat(skinParam.getSvgLinkTarget()).isEqualTo("_top");

		assertThat(skinParam.swimlaneWidth()).isZero();

		final LineBreakStrategy swimlaneWrapTitleWidth = skinParam.swimlaneWrapTitleWidth();
		assertThat(swimlaneWrapTitleWidth.isAuto()).isFalse();
		assertThat(swimlaneWrapTitleWidth.getMaxWidth()).isZero();

		assertThat(skinParam.getTabSize()).isEqualTo(8);

		assertThat(skinParam.getThickness(LineParam.activityBorder, null)).isNull();
		assertThat(skinParam.getThickness(LineParam.agentBorder, null)).isNull();
		assertThat(skinParam.getThickness(LineParam.archimateBorder, null)).isNull();
		assertThat(skinParam.getThickness(LineParam.arrow, null)).isNull();
		assertThat(skinParam.getThickness(LineParam.cardBorder, null)).isNull();
		assertThat(skinParam.getThickness(LineParam.classBorder, null)).isNull();
		assertThat(skinParam.getThickness(LineParam.componentBorder, null)).isNull();
		assertThat(skinParam.getThickness(LineParam.designedDomainBorder, null)).isNull();
		assertThat(skinParam.getThickness(LineParam.diagramBorder, null)).isNull();
		assertThat(skinParam.getThickness(LineParam.domainBorder, null)).isNull();
		assertThat(skinParam.getThickness(LineParam.hexagonBorder, null)).isNull();
		assertThat(skinParam.getThickness(LineParam.legendBorder, null)).isNull();
		assertThat(skinParam.getThickness(LineParam.machineBorder, null)).isNull();
		assertThat(skinParam.getThickness(LineParam.noteBorder, null)).isNull();
		assertThat(skinParam.getThickness(LineParam.objectBorder, null)).isNull();
		assertThat(skinParam.getThickness(LineParam.packageBorder, null)).isNull();
		assertThat(skinParam.getThickness(LineParam.partitionBorder, null)).isNull();
		assertThat(skinParam.getThickness(LineParam.queueBorder, null)).isNull();
		assertThat(skinParam.getThickness(LineParam.rectangleBorder, null)).isNull();
		assertThat(skinParam.getThickness(LineParam.requirementBorder, null)).isNull();
		assertThat(skinParam.getThickness(LineParam.sequenceActorBorder, null)).isNull();
		assertThat(skinParam.getThickness(LineParam.sequenceArrow, null)).isNull();
		assertThat(skinParam.getThickness(LineParam.sequenceDividerBorder, null)).isNull();
		assertThat(skinParam.getThickness(LineParam.sequenceGroupBorder, null)).isNull();
		assertThat(skinParam.getThickness(LineParam.sequenceLifeLineBorder, null)).isNull();
		assertThat(skinParam.getThickness(LineParam.sequenceParticipantBorder, null)).isNull();
		assertThat(skinParam.getThickness(LineParam.sequenceReferenceBorder, null)).isNull();
		assertThat(skinParam.getThickness(LineParam.swimlaneBorder, null)).isNull();
		assertThat(skinParam.getThickness(LineParam.titleBorder, null)).isNull();
		assertThat(skinParam.getThickness(LineParam.usecaseBorder, null)).isNull();

		assertThat(skinParam.getThickness(LineParam.activityBorder, fooStereotype)).isNull();
		assertThat(skinParam.getThickness(LineParam.agentBorder, fooStereotype)).isNull();
		assertThat(skinParam.getThickness(LineParam.archimateBorder, fooStereotype)).isNull();
		assertThat(skinParam.getThickness(LineParam.arrow, fooStereotype)).isNull();
		assertThat(skinParam.getThickness(LineParam.cardBorder, fooStereotype)).isNull();
		assertThat(skinParam.getThickness(LineParam.classBorder, fooStereotype)).isNull();
		assertThat(skinParam.getThickness(LineParam.componentBorder, fooStereotype)).isNull();
		assertThat(skinParam.getThickness(LineParam.designedDomainBorder, fooStereotype)).isNull();
		assertThat(skinParam.getThickness(LineParam.diagramBorder, fooStereotype)).isNull();
		assertThat(skinParam.getThickness(LineParam.domainBorder, fooStereotype)).isNull();
		assertThat(skinParam.getThickness(LineParam.hexagonBorder, fooStereotype)).isNull();
		assertThat(skinParam.getThickness(LineParam.legendBorder, fooStereotype)).isNull();
		assertThat(skinParam.getThickness(LineParam.machineBorder, fooStereotype)).isNull();
		assertThat(skinParam.getThickness(LineParam.noteBorder, fooStereotype)).isNull();
		assertThat(skinParam.getThickness(LineParam.objectBorder, fooStereotype)).isNull();
		assertThat(skinParam.getThickness(LineParam.packageBorder, fooStereotype)).isNull();
		assertThat(skinParam.getThickness(LineParam.partitionBorder, fooStereotype)).isNull();
		assertThat(skinParam.getThickness(LineParam.queueBorder, fooStereotype)).isNull();
		assertThat(skinParam.getThickness(LineParam.rectangleBorder, fooStereotype)).isNull();
		assertThat(skinParam.getThickness(LineParam.requirementBorder, fooStereotype)).isNull();
		assertThat(skinParam.getThickness(LineParam.sequenceActorBorder, fooStereotype)).isNull();
		assertThat(skinParam.getThickness(LineParam.sequenceArrow, fooStereotype)).isNull();
		assertThat(skinParam.getThickness(LineParam.sequenceDividerBorder, fooStereotype)).isNull();
		assertThat(skinParam.getThickness(LineParam.sequenceGroupBorder, fooStereotype)).isNull();
		assertThat(skinParam.getThickness(LineParam.sequenceLifeLineBorder, fooStereotype)).isNull();
		assertThat(skinParam.getThickness(LineParam.sequenceParticipantBorder, fooStereotype)).isNull();
		assertThat(skinParam.getThickness(LineParam.sequenceReferenceBorder, fooStereotype)).isNull();
		assertThat(skinParam.getThickness(LineParam.swimlaneBorder, fooStereotype)).isNull();
		assertThat(skinParam.getThickness(LineParam.titleBorder, fooStereotype)).isNull();
		assertThat(skinParam.getThickness(LineParam.usecaseBorder, fooStereotype)).isNull();

		final TikzFontDistortion tikzFontDistortion = skinParam.getTikzFontDistortion();
		assertThat(tikzFontDistortion.getDistortion()).isEqualTo(4.0);
		assertThat(tikzFontDistortion.getMagnify()).isEqualTo(1.20);

		assertThat(skinParam.useOctagonForActivity(null)).isFalse();
		assertThat(skinParam.useOctagonForActivity(fooStereotype)).isFalse();

		assertThat(skinParam.useRankSame()).isFalse();

		assertThat(skinParam.useSwimlanes(umlDiagramType)).isFalse();

		assertThat(skinParam.useUnderlineForHyperlink()).isNotNull();
	}

	@Test
	public void test_circledCharacterRadius() {
		final SkinParam skinParam = createSkinParam("circledCharacterRadius", "123");
		assertThat(skinParam.getCircledCharacterRadius()).isEqualTo(123);
	}

	@Test
	public void test_classAttributeIconSize() {
		final SkinParam skinParam = createSkinParam("classAttributeIconSize", "123");
		assertThat(skinParam.classAttributeIconSize()).isEqualTo(123);
	}

	@Test
	public void test_defaultMonospacedFontName() {
		final SkinParam skinParam = createSkinParam("defaultMonospacedFontName", "foo");
		assertThat(skinParam.getMonospacedFamily()).isEqualTo("foo");
	}

	@Test
	public void test_dpi() {
		final SkinParam skinParam = createSkinParam("dpi", "123");
		assertThat(skinParam.getDpi()).isEqualTo(123);
	}

	@ParameterizedTest
	@CsvSource({ "true,        true", "tRUe,        true", "TRUE,        true", "other_value, false", })
	public void test_fixCircleLabelOverlapping(String paramValue, boolean expected) {
		final SkinParam skinParam = createSkinParam("fixCircleLabelOverlapping", paramValue);
		assertThat(skinParam.fixCircleLabelOverlapping()).isEqualTo(expected);
	}

	@ParameterizedTest
	@CsvSource({ "old,         true", "oLd,         true", "OLD,         true", "other_value, false", })
	public void test_genericDisplay(String paramValue, boolean expected) {
		final SkinParam skinParam = createSkinParam("genericDisplay", paramValue);
		assertThat(skinParam.displayGenericWithOldFashion()).isEqualTo(expected);
	}

	@ParameterizedTest
	@CsvSource({ "0, MAX_VALUE", "1, MAX_VALUE", "2, 2", "123, 123" })
	public void test_groupInheritance(String paramValue, String expectedValue) {
		final SkinParam skinParam = createSkinParam("groupInheritance", paramValue);
		assertThat(skinParam.groupInheritance()).isEqualTo(intFromCsv(expectedValue));
	}

	@ParameterizedTest
	@CsvSource({ "true,        true", "tRUe,        true", "TRUE,        true", "other_value, false", })
	public void test_handwritten(String paramValue, boolean expected) {
		final SkinParam skinParam = createSkinParam("handwritten", paramValue);
		assertThat(skinParam.handwritten()).isEqualTo(expected);
	}

	@ParameterizedTest
	@CsvSource({ "false,       false", "fALSe,       false", "FALSE,       false", "other_value, true", })
	public void test_hyperlinkUnderline(String paramValue, boolean expected) {
		final SkinParam skinParam = createSkinParam("hyperlinkUnderline", paramValue);
		if (expected)
			assertThat(skinParam.useUnderlineForHyperlink()).isNotNull();
		else
			assertThat(skinParam.useUnderlineForHyperlink()).isNull();
	}

	@Test
	public void test_maxAsciiMessageLength() {
		final SkinParam skinParam = createSkinParam("maxAsciiMessageLength", "123");
		assertThat(skinParam.maxAsciiMessageLength()).isEqualTo(123);
	}

//	@Test
//	public void test_minClassWidth() {
//		final SkinParam skinParam = createSkinParam("minClassWidth", "123");
//		assertThat(skinParam.minClassWidth()).isEqualTo(123);
//	}

	@Test
	public void test_nodeSep() {
		final SkinParam skinParam = createSkinParam("nodeSep", "123");
		assertThat(skinParam.getNodesep()).isEqualTo(123);
	}

	@Test
	public void test_preserveAspectRatio() {
		final SkinParam skinParam = createSkinParam("preserveAspectRatio", "foo");
		assertThat(skinParam.getPreserveAspectRatio()).isEqualTo("foo");
	}

	@Test
	public void test_rankSep() {
		final SkinParam skinParam = createSkinParam("rankSep", "123");
		assertThat(skinParam.getRanksep()).isEqualTo(123);
	}

	@ParameterizedTest
	@CsvSource({ "true,        true", "tRUe,        true", "TRUE,        true", "other_value, false", })
	public void test_responseMessageBelowArrow(String paramValue, boolean expected) {
		final SkinParam skinParam = createSkinParam("responseMessageBelowArrow", paramValue);
		assertThat(skinParam.responseMessageBelowArrow()).isEqualTo(expected);
	}

	@ParameterizedTest
	@CsvSource({ "true,        true", "tRUe,        true", "TRUE,        true", "other_value, false", })
	public void test_sameClassWidth(String paramValue, boolean expected) {
		final SkinParam skinParam = createSkinParam("sameClassWidth", paramValue);
		assertThat(skinParam.sameClassWidth()).isEqualTo(expected);
	}

	@ParameterizedTest
	@CsvSource({ "underline,   true", "undERLine,   true", "UNDERLINE,   true", "other_value, false", })
	public void test_sequenceParticipant(String paramValue, boolean expected) {
		final SkinParam skinParam = createSkinParam("sequenceParticipant", paramValue);
		assertThat(skinParam.forceSequenceParticipantUnderlined()).isEqualTo(expected);
	}

	@Test
	public void testSplitParam() {
		final SkinParam skinParam = createSkinParam("pageBorderColor", "red", "pageExternalColor", "yellow",
				"pageMargin", "123");

		final SplitParam splitParam = skinParam.getSplitParam();
		assertThat(splitParam.getBorderColor()).isEqualTo(Color.RED);
		assertThat(splitParam.getExternalColor()).isEqualTo(Color.YELLOW);
		assertThat(splitParam.getExternalMargin()).isEqualTo(123);
	}

	@ParameterizedTest
	@CsvSource({ "bottom,      false", "boTTom,      false", "BOTTOM,      false", "other_value, true", })
	public void test_stereotypePosition(String paramValue, boolean expected) {
		final SkinParam skinParam = createSkinParam("stereotypePosition", paramValue);
		assertThat(skinParam.stereotypePositionTop()).isEqualTo(expected);
	}

	@ParameterizedTest
	@CsvSource({ "strictuml,   true", "strICTuml,   true", "STRICTUML,   true", "other_value, false", })
	public void test_style(String paramValue, boolean expected) {
		final SkinParam skinParam = createSkinParam("style", paramValue);
		assertThat(skinParam.strictUmlStyle()).isEqualTo(expected);
	}

	@ParameterizedTest
	@CsvSource({ "false,       false", "fALSe,       false", "FALSE,       false", "other_value, true", })
	public void test_svgDimensionStyle(String paramValue, boolean expected) {
		final SkinParam skinParam = createSkinParam("svgDimensionStyle", paramValue);
		assertThat(skinParam.svgDimensionStyle()).isEqualTo(expected);
	}

	@Test
	public void test_svgLinkTarget() {
		final SkinParam skinParam = createSkinParam("svgLinkTarget", "foo");
		assertThat(skinParam.getSvgLinkTarget()).isEqualTo("foo");
	}

	@ParameterizedTest
	@CsvSource({
			// swimlane swimlanes expected
			"  true,        any_value,   true", "  tRUe,        any_value,   true", "  TRUE,        any_value,   true",
			"  other_value, true,        true", "  other_value, tRUe,        true", "  other_value, TRUE,        true",
			"  other_value, other_value, false", })
	public void test_swimlanes(String swimlane, String swimlanes, boolean expected) {
		final SkinParam skinParam = createSkinParam("swimlane", swimlane, "swimlanes", swimlanes);
		assertThat(skinParam.swimlanes()).isEqualTo(expected);
	}

	@Test
	public void test_tabSize() {
		final SkinParam skinParam = createSkinParam("tabSize", "123");
		assertThat(skinParam.getTabSize()).isEqualTo(123);
	}

	//
	// Test DSL
	//

	private SkinParam createSkinParam(String... keyValuePairs) {
		// Using SEQUENCE here is an arbitrary decision that should not affect test
		// outcome
		final SkinParam skinParam = SkinParam.create(UmlDiagramType.SEQUENCE);
		for (int i = 0; i < keyValuePairs.length; i += 2) {
			skinParam.setParam(StringUtils.goLowerCase(keyValuePairs[i]), keyValuePairs[i + 1]);
		}
		return skinParam;
	}

	private int intFromCsv(String value) {
		return value.equals("MAX_VALUE") ? Integer.MAX_VALUE : Integer.parseInt(value);
	}
}
