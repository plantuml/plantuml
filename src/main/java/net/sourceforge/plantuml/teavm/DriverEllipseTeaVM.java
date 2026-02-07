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
import net.sourceforge.plantuml.klimt.shape.UEllipse;

public class DriverEllipseTeaVM implements UDriver<UEllipse, SvgGraphicsTeaVM> {

	private final ClipContainer clipContainer;

	public DriverEllipseTeaVM(ClipContainer clipContainer) {
		this.clipContainer = clipContainer;
	}

	@Override
	public void draw(UEllipse ellipse, double x, double y, ColorMapper mapper, UParam param, SvgGraphicsTeaVM svg) {
		DriverRectangleTeaVM.applyFillColor(svg, mapper, param);
		DriverRectangleTeaVM.applyStrokeColor(svg, mapper, param);
		svg.setStrokeWidth(param.getStroke().getThickness());

		final double width = ellipse.getWidth();
		final double height = ellipse.getHeight();

		// UEllipse uses width/height, but SVG ellipse uses rx/ry (radii)
		// And x,y is top-left corner for UEllipse, but center for SVG
		final double cx = x + width / 2;
		final double cy = y + height / 2;
		final double rx = width / 2;
		final double ry = height / 2;

		if (Math.abs(rx - ry) < 0.01) {
			// It's a circle
			svg.drawCircle(cx, cy, rx);
		} else {
			svg.drawEllipse(cx, cy, rx, ry);
		}
	}
}
