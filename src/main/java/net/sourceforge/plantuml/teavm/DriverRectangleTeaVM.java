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
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UDriver;
import net.sourceforge.plantuml.klimt.shape.URectangle;

public class DriverRectangleTeaVM implements UDriver<URectangle, SvgGraphicsTeaVM> {

	private final ClipContainer clipContainer;

	public DriverRectangleTeaVM(ClipContainer clipContainer) {
		this.clipContainer = clipContainer;
	}

	@Override
	public void draw(URectangle rect, double x, double y, ColorMapper mapper, UParam param, SvgGraphicsTeaVM svg) {
		final double rx = rect.getRx();
		final double ry = rect.getRy();
		final double width = rect.getWidth();
		final double height = rect.getHeight();

		applyFillColor(svg, mapper, param);
		applyStrokeColor(svg, mapper, param);
		svg.setStrokeWidth(param.getStroke().getThickness());

		svg.drawRectangle(x, y, width, height, rx / 2, ry / 2);
	}

	public static void applyFillColor(SvgGraphicsTeaVM svg, ColorMapper mapper, UParam param) {
		final HColor background = param.getBackcolor();
		if (background == null) {
			svg.setFillColor("none");
		} else {
			svg.setFillColor(background.toSvg(mapper));
		}
	}

	public static void applyStrokeColor(SvgGraphicsTeaVM svg, ColorMapper mapper, UParam param) {
		final HColor color = param.getColor();
		if (color == null) {
			svg.setStrokeColor("none");
		} else {
			svg.setStrokeColor(color.toSvg(mapper));
		}
	}
}
