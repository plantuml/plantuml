package net.sourceforge.plantuml.ugraphic.fontspritesheet;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.graphic.StringBounderRaw;
import net.sourceforge.plantuml.ugraphic.UFont;

class FontSpriteSheetStringBounder extends StringBounderRaw {

	private final FontSpriteSheetManager manager;

	FontSpriteSheetStringBounder(FontSpriteSheetManager manager) {
		this.manager = manager;
	}

	@Override
	protected Dimension2D calculateDimensionInternal(UFont font, String text) {
		return manager.findNearestSheet(font.getUnderlayingFont()).calculateDimension(text);
	}

	@Override
	public double getDescent(UFont font, String text) {
		return manager.findNearestSheet(font.getUnderlayingFont()).getDescent();
	}
}
