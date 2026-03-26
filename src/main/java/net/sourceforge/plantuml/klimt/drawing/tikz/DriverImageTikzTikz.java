package net.sourceforge.plantuml.klimt.drawing.tikz;

import net.sourceforge.plantuml.klimt.UParam;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.drawing.UDriver;
import net.sourceforge.plantuml.klimt.shape.UImageTikz;
import net.sourceforge.plantuml.tikz.TikzGraphics;

public class DriverImageTikzTikz implements UDriver<UImageTikz, TikzGraphics> {

	public void draw(UImageTikz shape, double x, double y, ColorMapper mapper, UParam param, TikzGraphics tikz) {
		tikz.tikzImage(shape, x, y);
	}

}
