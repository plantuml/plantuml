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
import net.sourceforge.plantuml.Scale;
import net.sourceforge.plantuml.SvgCharSizeHack;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.UseStyle;
import net.sourceforge.plantuml.anim.Animation;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.svg.LengthAdjust;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

// TODO merge these fields into ImageBuilder
public class ImageParameter {

	private final ColorMapper colorMapper;
	private final boolean useHandwritten;
	private final Animation animation;
	private final Scale scale;
	private int dpi;
	private final boolean svgDimensionStyle;
	private final SvgCharSizeHack svgCharSizeHack;
	private final LengthAdjust lengthAdjust;

	private final UStroke borderStroke;
	private final HColor borderColor;
	private final double borderCorner;

	public ImageParameter() {
		this.colorMapper = new ColorMapperIdentity();
		this.useHandwritten = false;
		this.animation = null;
		this.scale = null;
		this.dpi = 96;
		this.svgDimensionStyle = true;

		this.borderColor = null;
		this.borderCorner = 0;
		this.borderStroke = null;
		this.svgCharSizeHack = SvgCharSizeHack.NO_HACK;
		this.lengthAdjust = LengthAdjust.defaultValue();
	}

	public static HColor getBackgroundColor(TitledDiagram diagram) {
		if (UseStyle.useBetaStyle()) {
			final Style style = StyleSignature
					.of(SName.root, SName.document, diagram.getUmlDiagramType().getStyleName())
					.getMergedStyle(diagram.getSkinParam().getCurrentStyleBuilder());

			HColor backgroundColor = style.value(PName.BackGroundColor)
					.asColor(diagram.getSkinParam().getIHtmlColorSet());
			if (backgroundColor == null) {
				backgroundColor = HColorUtils.transparent();
			}
			return backgroundColor;

		}
		return diagram.getSkinParam().getBackgroundColor(false);
	}

	public ImageParameter(TitledDiagram diagram) {
		final ISkinParam skinParam = diagram.getSkinParam();
		this.colorMapper = skinParam.getColorMapper();
		this.useHandwritten = skinParam.handwritten();
		this.animation = diagram.getAnimation();
		this.scale = diagram.getScale();
		this.dpi = skinParam.getDpi();
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
		this.lengthAdjust = skinParam.getlengthAdjust();

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

	public Scale getScale() {
		return scale;
	}

	public int getDpi() {
		return dpi;
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

	public final LengthAdjust getlengthAdjust() {
		return lengthAdjust;
	}

	public static ClockwiseTopRightBottomLeft calculateDiagramMargin(TitledDiagram diagram) {
		if (UseStyle.useBetaStyle()) {
			final Style style = StyleSignature.of(SName.root, SName.document)
					.getMergedStyle(diagram.getSkinParam().getCurrentStyleBuilder());
			if (style.hasValue(PName.Margin)) {
				return style.getMargin();
			}
		}
		return diagram.getDefaultMargins();
	}
}
