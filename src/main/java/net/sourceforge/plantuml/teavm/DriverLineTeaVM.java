/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 *
 */
package net.sourceforge.plantuml.teavm;

import net.sourceforge.plantuml.klimt.ClipContainer;
import net.sourceforge.plantuml.klimt.UParam;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.drawing.UDriver;
import net.sourceforge.plantuml.klimt.shape.ULine;

public class DriverLineTeaVM implements UDriver<ULine, SvgGraphicsTeaVM> {

	private final ClipContainer clipContainer;

	public DriverLineTeaVM(ClipContainer clipContainer) {
		this.clipContainer = clipContainer;
	}

	@Override
	public void draw(ULine line, double x, double y, ColorMapper mapper, UParam param, SvgGraphicsTeaVM svg) {
		DriverRectangleTeaVM.applyStrokeColor(svg, mapper, param);
		svg.setStrokeWidth(param.getStroke().getThickness());

		svg.drawLine(x, y, x + line.getDX(), y + line.getDY());
	}
}
