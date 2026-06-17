package net.sourceforge.plantuml.klimt.drawing.font;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFontFactory;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;

class StringBounderFromWidthTableTest {

	@Test
	void testCalculateDimension() {

		final StringBounder sb = new StringBounderFromWidthTable(FileFormat.SVG);
		final XDimension2D dim = sb.calculateDimension(UFontFactory.sansSerif(16), "!");
	}

}
