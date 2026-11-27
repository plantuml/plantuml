package net.sourceforge.plantuml.ngdot.attr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.klimt.awt.XColor;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.color.HColors;

class DotColorsTest {

	private static int rgb(String value) {
		final HColor color = DotColors.parse(value, HColorSet.instance());
		final XColor c = color.toColor(ColorMapper.IDENTITY);
		return (c.getRed() << 16) | (c.getGreen() << 8) | c.getBlue();
	}

	@Test
	void hex() {
		assertEquals(0xFFCC00, rgb("#ffcc00"));
		assertEquals(0xFFCC00, rgb("#FFCC00"));
		assertEquals(0x102030, rgb("#10203080"));
		assertSame(HColors.none(), DotColors.parse("#12345600", HColorSet.instance()));
		assertNull(DotColors.parse("#12345", HColorSet.instance()));
		assertNull(DotColors.parse("#gg0000", HColorSet.instance()));
	}

	@Test
	void names() {
		assertEquals(0xADD8E6, rgb("lightblue"));
		assertEquals(0xADD8E6, rgb("LightBlue"));
		assertEquals(0xFF0000, rgb("/x11/red"));
		assertNull(DotColors.parse("nosuchcolor", HColorSet.instance()));
		assertNull(DotColors.parse("/blues9/3", HColorSet.instance()));
	}

	@Test
	void x11ValuesWin() {
		assertEquals(0xBEBEBE, rgb("gray"));
		assertEquals(0xBEBEBE, rgb("grey"));
		assertEquals(0x00FF00, rgb("green"));
		assertEquals(0xB03060, rgb("maroon"));
		assertEquals(0xA020F0, rgb("purple"));
	}

	@Test
	void grayLevelsAndVariants() {
		assertEquals(0x000000, rgb("gray0"));
		assertEquals(0x7F7F7F, rgb("grey50"));
		assertEquals(0xFFFFFF, rgb("gray100"));
		assertNull(DotColors.parse("gray101", HColorSet.instance()));
		assertEquals(0xFF0000, rgb("red1"));
		assertEquals(0xEE0000, rgb("red2"));
		assertEquals(0xCD0000, rgb("red3"));
		assertEquals(0x8B0000, rgb("red4"));
	}

	@Test
	void listsKeepTheFirstColor() {
		assertEquals(0xFF0000, rgb("red:blue"));
		assertEquals(0xFF0000, rgb("red;0.3:blue"));
		assertEquals(0x0000FF, rgb(" blue ; 0.5 "));
	}

	@Test
	void transparentAndUnsupported() {
		assertSame(HColors.none(), DotColors.parse("transparent", HColorSet.instance()));
		assertSame(HColors.none(), DotColors.parse("none", HColorSet.instance()));
		assertSame(HColors.none(), DotColors.parse("invis", HColorSet.instance()));
		// HSV is not supported: no color, the theme applies
		assertNull(DotColors.parse("0.6 0.4 0.9", HColorSet.instance()));
		assertNull(DotColors.parse("0.6,0.4,0.9", HColorSet.instance()));
		assertNull(DotColors.parse("", HColorSet.instance()));
		assertNull(DotColors.parse(null, HColorSet.instance()));
	}

}
