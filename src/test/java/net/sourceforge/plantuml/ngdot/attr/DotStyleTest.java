package net.sourceforge.plantuml.ngdot.attr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.ngdot.model.DotAttributes;
import net.sourceforge.plantuml.ngdot.model.DotValue;

class DotStyleTest {

	private static DotStyle style(String style, String penwidth) {
		final DotAttributes attributes = new DotAttributes();
		if (style != null)
			attributes.put("style", DotValue.of(style));
		if (penwidth != null)
			attributes.put("penwidth", DotValue.of(penwidth));
		return DotStyle.of(attributes);
	}

	@Test
	void flags() {
		final DotStyle style = style("Filled, rounded,diagonals ,invis", null);
		assertTrue(style.isFilled());
		assertTrue(style.isRounded());
		assertTrue(style.isDiagonals());
		assertTrue(style.isInvisible());
		assertFalse(style(null, null).isFilled());
	}

	@Test
	void strokes() {
		final UStroke theme = UStroke.withThickness(1.5);
		assertEquals(1.5, style(null, null).getStroke(theme).getThickness(), 1e-9);
		assertEquals(2, style("bold", null).getStroke(theme).getThickness(), 1e-9);
		assertEquals(4, style("bold", "4").getStroke(theme).getThickness(), 1e-9);
		assertEquals(3, style("setlinewidth(3)", null).getStroke(theme).getThickness(), 1e-9);

		final UStroke dashed = style("dashed", null).getStroke(UStroke.simple());
		assertEquals(5, dashed.getDashVisible(), 1e-9);
		assertEquals(2, dashed.getDashSpace(), 1e-9);
		final UStroke dotted = style("dotted", null).getStroke(UStroke.simple());
		assertEquals(1, dotted.getDashVisible(), 1e-9);
		assertEquals(5, dotted.getDashSpace(), 1e-9);
	}

}
