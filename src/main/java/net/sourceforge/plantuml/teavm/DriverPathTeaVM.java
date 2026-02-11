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
import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.drawing.UDriver;
import net.sourceforge.plantuml.klimt.geom.USegment;
import net.sourceforge.plantuml.klimt.geom.USegmentType;

public class DriverPathTeaVM implements UDriver<UPath, SvgGraphicsTeaVM> {

	private final ClipContainer clipContainer;

	public DriverPathTeaVM(ClipContainer clipContainer) {
		this.clipContainer = clipContainer;
	}

	@Override
	public void draw(UPath path, double x, double y, ColorMapper mapper, UParam param, SvgGraphicsTeaVM svg) {
		DriverRectangleTeaVM.applyFillColor(svg, mapper, param);
		DriverRectangleTeaVM.applyStrokeColor(svg, mapper, param);
		svg.setStrokeWidth(param.getStroke().getThickness());

		// Build SVG path data string
		final StringBuilder pathData = new StringBuilder();
		
		for (USegment seg : path) {
			final USegmentType type = seg.getSegmentType();
			final double[] coord = seg.getCoord();
			
			if (type == USegmentType.SEG_MOVETO) {
				pathData.append("M").append(format(coord[0] + x)).append(",").append(format(coord[1] + y)).append(" ");
			} else if (type == USegmentType.SEG_LINETO) {
				pathData.append("L").append(format(coord[0] + x)).append(",").append(format(coord[1] + y)).append(" ");
			} else if (type == USegmentType.SEG_QUADTO) {
				pathData.append("Q")
					.append(format(coord[0] + x)).append(",").append(format(coord[1] + y)).append(" ")
					.append(format(coord[2] + x)).append(",").append(format(coord[3] + y)).append(" ");
			} else if (type == USegmentType.SEG_CUBICTO) {
				pathData.append("C")
					.append(format(coord[0] + x)).append(",").append(format(coord[1] + y)).append(" ")
					.append(format(coord[2] + x)).append(",").append(format(coord[3] + y)).append(" ")
					.append(format(coord[4] + x)).append(",").append(format(coord[5] + y)).append(" ");
			} else if (type == USegmentType.SEG_ARCTO) {
				pathData.append("A")
					.append(format(coord[0])).append(",").append(format(coord[1])).append(" ")
					.append(format(coord[2])).append(" ")
					.append(coord[3] == 0 ? "0" : "1").append(" ")
					.append(coord[4] == 0 ? "0" : "1").append(" ")
					.append(format(coord[5] + x)).append(",").append(format(coord[6] + y)).append(" ");
			} else if (type == USegmentType.SEG_CLOSE) {
				pathData.append("Z ");
			}
		}

		svg.drawPath(pathData.toString().trim());
	}

	private String format(double value) {
		if (value == (int) value) {
			return String.valueOf((int) value);
		}
		return String.format("%.2f", value).replace(',', '.');
	}
}
