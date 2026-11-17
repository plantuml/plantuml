package net.sourceforge.plantuml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.font.UFontContext;
import net.sourceforge.plantuml.klimt.font.UFontFace;
import net.sourceforge.plantuml.nio.PathSystem;
import net.sourceforge.plantuml.preproc.ConfigurationStore;
import net.sourceforge.plantuml.skin.Pragma;
import net.sourceforge.plantuml.skin.SkinParam;
import net.sourceforge.plantuml.stereo.Stereotype;

class SkinFontResolutionTest {

	private SkinParam create(DiagramType type) {
		return SkinParam.create(Collections.emptyMap(), PathSystem.fetch(), type, Pragma.createEmpty(),
				ConfigurationStore.createEmpty());
	}

	@ParameterizedTest
	@EnumSource(FontParam.class)
	void preservesDefaultsForEveryFontAndPackageTitle(FontParam param) {
		for (DiagramType type : new DiagramType[] { DiagramType.CLASS, DiagramType.ACTIVITY }) {
			final SkinParam skin = create(type);
			for (boolean title : new boolean[] { false, true }) {
				final UFont font = skin.getFont(null, title, param);
				assertEquals(param.getDefaultSize(skin), font.getSize());
				assertEquals(param.getDefaultFontFace(skin, title), font.getFontFace());
				assertEquals(param.getDefaultFamily(), font.getFamily("", UFontContext.PDF));
			}
		}
	}

	@Test
	void stereotypeOverridesSpecificAndGlobalValues() {
		final SkinParam skin = create(DiagramType.CLASS);
		skin.setParam("defaultfontsize", "18");
		skin.setParam("defaultfontname", "Serif");
		skin.setParam("defaultfontstyle", "italic");
		skin.setParam("defaultfontcolor", "red");
		skin.setParam("classfontsize", "20");
		skin.setParam("classfontname", "Monospaced");
		skin.setParam("classfontstyle", "bold");
		skin.setParam("classfontcolor", "blue");
		skin.setParam("classfontsize<<special>>", "24");
		skin.setParam("classfontname<<special>>", "\"Dialog\"");
		skin.setParam("classfontstyle<<special>>", "BOLD ITALIC");
		skin.setParam("classfontcolor<<special>>", "#00FF00");
		final Stereotype special = Stereotype.build("<<special>>");
		final UFont font = skin.getFont(special, false, FontParam.CLASS);
		assertEquals(24, font.getSize());
		assertEquals("Dialog", font.getFamily("", UFontContext.PDF));
		assertEquals(UFontFace.boldItalic(), font.getFontFace());
		assertEquals(HColors.GREEN, skin.getFontHtmlColor(special, FontParam.CLASS));
		assertEquals(20, skin.getFont(null, false, FontParam.CLASS).getSize());
		assertEquals(HColors.BLUE, skin.getFontHtmlColor(null, FontParam.CLASS));
	}

	@Test
	void preservesExistingLookupCacheAndInvalidSizeFallback() {
		final SkinParam skin = create(DiagramType.CLASS);
		skin.setParam("defaultfontsize", "18");
		skin.setParam("classfontsize", "invalid");
		skin.setParam("arrowfontsize", "30");
		assertEquals(18, skin.getFont(null, false, FontParam.CLASS, FontParam.ARROW).getSize());
		skin.setParam("classfontsize", "22");
		// SkinParam already caches queried values; extraction must not change that contract.
		assertEquals(18, skin.getFont(null, false, FontParam.CLASS).getSize());
		skin.setParam("classfontsize<<special>>", "invalid");
		assertEquals(18, skin.getFont(Stereotype.build("<<special>>"), false, FontParam.CLASS).getSize());
		final SkinParam invalid = create(DiagramType.CLASS);
		invalid.setParam("classfontsize", "invalid");
		invalid.setParam("defaultfontsize", "invalid");
		assertEquals(FontParam.CLASS.getDefaultSize(invalid), invalid.getFont(null, false, FontParam.CLASS).getSize());
	}

	@Test
	void circledCharacterIgnoresGlobalFamilyAndUsesConfiguredSizeForRadius() {
		final SkinParam skin = create(DiagramType.CLASS);
		skin.setParam("defaultfontname", "Dialog");
		skin.setParam("circledcharacterfontsize", "30");
		assertEquals(FontParam.CIRCLED_CHARACTER.getDefaultFamily(),
				skin.getFont(null, false, FontParam.CIRCLED_CHARACTER).getFamily("", UFontContext.PDF));
		assertEquals(16, skin.getCircledCharacterRadius());
	}
}
