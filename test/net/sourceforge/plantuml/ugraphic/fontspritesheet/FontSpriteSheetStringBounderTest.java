package net.sourceforge.plantuml.ugraphic.fontspritesheet;

import static net.sourceforge.plantuml.ugraphic.fontspritesheet.FontSpriteSheetMaker.JETBRAINS_FONT_FAMILY;
import static net.sourceforge.plantuml.ugraphic.fontspritesheet.FontSpriteSheetMaker.registerJetBrainsFonts;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UFont;

class FontSpriteSheetStringBounderTest {

	@BeforeAll
	static void before_all() throws Exception {
		registerJetBrainsFonts();
	}

	//
	// Test Cases
	//

	@ParameterizedTest(name = "{arguments}")
	@MethodSource("allSpriteSheets")
	void test_getDescent(FontSpriteSheet sheet) {
		final UFont uFont = createUFont(sheet);
		final String string = "foo";

		assertThat(spriteSheetBounder.getDescent(uFont, string))
				.isEqualTo(normalBounder.getDescent(uFont, string));
	}

	//
	// Test DSL
	//

	private static final FontSpriteSheetManager MANAGER = FontSpriteSheetManager.instance();

	private final StringBounder normalBounder = FileFormat.PNG.getDefaultStringBounder();

	private final StringBounder spriteSheetBounder = MANAGER.createStringBounder();

	private static List<FontSpriteSheet> allSpriteSheets() {
		return MANAGER.allSheets();
	}

	private UFont createUFont(FontSpriteSheet sheet) {
		return new UFont(JETBRAINS_FONT_FAMILY, sheet.getStyle(), sheet.getPointSize());
	}

}
