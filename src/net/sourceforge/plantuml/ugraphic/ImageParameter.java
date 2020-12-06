/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.ugraphic;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.CornerParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineParam;
import net.sourceforge.plantuml.SvgCharSizeHack;
import net.sourceforge.plantuml.anim.Animation;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class ImageParameter {

	private final ColorMapper colorMapper;
	private final boolean useHandwritten;
	private final Animation animation;
	private final double dpiFactor;
	private final String metadata;
	private final String warningOrError;
	private final ClockwiseTopRightBottomLeft margins;
	private final HColor backcolor;
	private final boolean svgDimensionStyle;
	private final SvgCharSizeHack svgCharSizeHack;

	private final UStroke borderStroke;
	private final HColor borderColor;
	private final double borderCorner;

	public ImageParameter(ColorMapper colorMapper, boolean useHandwritten, Animation animation, double dpiFactor,
			String metadata, String warningOrError, ClockwiseTopRightBottomLeft margins, HColor backcolor) {
		this.colorMapper = colorMapper;
		this.useHandwritten = useHandwritten;
		this.animation = animation;
		this.dpiFactor = dpiFactor;
		this.metadata = metadata;
		this.warningOrError = warningOrError;
		this.margins = margins;
		this.backcolor = backcolor;
		this.svgDimensionStyle = true;

		this.borderColor = null;
		this.borderCorner = 0;
		this.borderStroke = null;
		this.svgCharSizeHack = SvgCharSizeHack.NO_HACK;
	}

	public ImageParameter(ISkinParam skinParam, Animation animation, double dpiFactor, String metadata,
			String warningOrError, ClockwiseTopRightBottomLeft margins, HColor backcolor) {
		this.colorMapper = skinParam.getColorMapper();
		this.useHandwritten = skinParam.handwritten();
		this.animation = animation;
		this.dpiFactor = dpiFactor;
		this.metadata = metadata;
		this.warningOrError = warningOrError;
		this.margins = margins;
		this.backcolor = backcolor;
		this.svgDimensionStyle = skinParam.svgDimensionStyle();

		final Rose rose = new Rose();
		this.borderColor = rose.getHtmlColor(skinParam, ColorParam.diagramBorder);
		this.borderCorner = skinParam.getRoundCorner(CornerParam.diagramBorder, null);
		final UStroke thickness = skinParam.getThickness(LineParam.diagramBorder, null);
		if (thickness == null && borderColor != null) {
			this.borderStroke = new UStroke();
		} else {
			this.borderStroke = thickness;
		}

		this.svgCharSizeHack = skinParam;

	}

	public final ColorMapper getColorMapper() {
		return colorMapper;
	}

	public final boolean isUseHandwritten() {
		return useHandwritten;
	}

	public final Animation getAnimation() {
		return animation;
	}

	public final double getDpiFactor() {
		return dpiFactor;
	}

	public final String getMetadata() {
		return metadata;
	}

	public final String getWarningOrError() {
		return warningOrError;
	}

	public final ClockwiseTopRightBottomLeft getMargins() {
		return margins;
	}

	public final HColor getBackcolor() {
		return backcolor;
	}

	public final boolean isSvgDimensionStyle() {
		return svgDimensionStyle;
	}

	public final UStroke getBorderStroke() {
		return borderStroke;
	}

	public final HColor getBorderColor() {
		return borderColor;
	}

	public final double getBorderCorner() {
		return borderCorner;
	}

	public final SvgCharSizeHack getSvgCharSizeHack() {
		return svgCharSizeHack;
	}

}
