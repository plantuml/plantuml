/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 *
 * If you like this project or if you find it useful, you can support us at:
 *
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
package net.sourceforge.plantuml.klimt.drawing.svg;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.preproc.ConfigurationStore;
import net.sourceforge.plantuml.preproc.OptionKey;
import net.sourceforge.plantuml.skin.Pragma;

public class SvgOption {

	private LengthAdjust lengthAdjust = LengthAdjust.defaultValue();
	private String preserveAspectRatio = "none";
	private String hover;
	private boolean svgDimensionStyle = true;
	private XDimension2D minDim = new XDimension2D(0, 0);
	private HColor backcolor;
	private double scale = 1.0;
	private ColorMapper colorMapper = ColorMapper.IDENTITY;
	private String linkTarget;
	private String font;
	private String title;
	private String interactiveBaseFilename;
	private final Map<String, String> rootAttributes = new LinkedHashMap<>();
	private ConfigurationStore<OptionKey> options;

	public String getInteractiveBaseFilename() {
		return interactiveBaseFilename;
	}

	public static SvgOption basic() {
		return new SvgOption();
	}

	private SvgOption() {
	}

	// This method will be removed once Pragma SVGNEWDATA will be removed

	public Pragma pragma;

	@Deprecated
	public SvgOption withPragma(Pragma pragma) {
		this.pragma = pragma;
		return this;
	}

	public SvgOption withConfigurationStore(ConfigurationStore<OptionKey> options) {
		this.options = options;
		return this;
	}

	private String getValueFromOptions(OptionKey key) {
		if (options == null)
			return null;
		return options.getValue(key);
	}

	public String getTitle() {
		final String optionTitle = getValueFromOptions(OptionKey.SVG_TITLE);
		if (optionTitle != null)
			return optionTitle;
		return title;
	}

	public String getDesc() {
		return getValueFromOptions(OptionKey.SVG_DESC);
	}

	public SvgOption withTitle(Display titleDisplay) {
		if (titleDisplay.size() > 0) {
			title = StreamSupport.stream(titleDisplay.spliterator(), false) //
	            .map(CharSequence::toString) //
	            .collect(Collectors.joining("\n"));
		}
		return this;
	}



	public SvgOption withInteractive(String interactiveBaseFilename) {
		this.interactiveBaseFilename = interactiveBaseFilename;
		return this;
	}

	public SvgOption withLengthAdjust(LengthAdjust lengthAdjust) {
		this.lengthAdjust = lengthAdjust;
		return this;
	}

	public SvgOption withPreserveAspectRatio(String preserveAspectRatio) {
		this.preserveAspectRatio = preserveAspectRatio;
		return this;
	}

	public SvgOption withHoverPathColorRGB(String hover) {
		this.hover = hover;
		return this;
	}

	public SvgOption withSvgDimensionStyle(boolean svgDimensionStyle) {
		this.svgDimensionStyle = svgDimensionStyle;
		return this;
	}

	public SvgOption withMinDim(XDimension2D minDim) {
		this.minDim = minDim;
		return this;
	}

	public SvgOption withBackcolor(HColor backcolor) {
		this.backcolor = backcolor;
		return this;
	}

	public SvgOption withScale(double scale) {
		this.scale = scale;
		return this;
	}

	public SvgOption withColorMapper(ColorMapper colorMapper) {
		this.colorMapper = colorMapper;
		return this;
	}

	public SvgOption withLinkTarget(String linkTarget) {
		this.linkTarget = linkTarget;
		return this;
	}

	public SvgOption withFont(String font) {
		this.font = font;
		return this;
	}

	public Map<String, String> getRootAttributes() {
		return Collections.unmodifiableMap(rootAttributes);
	}

	public SvgOption withRootAttribute(String key, String value) {
		this.rootAttributes.put(key, value);
		return this;
	}

	public String getPreserveAspectRatio() {
		return preserveAspectRatio;
	}

	public boolean isInteractive() {
		return interactiveBaseFilename != null;
	}

	public LengthAdjust getLengthAdjust() {
		return lengthAdjust;
	}

	public String getHover() {
		return hover;
	}

	public boolean getSvgDimensionStyle() {
		return svgDimensionStyle;
	}

	public XDimension2D getMinDim() {
		return minDim;
	}

	public HColor getBackcolor() {
		return backcolor;
	}

	public double getScale() {
		return scale;
	}

	public ColorMapper getColorMapper() {
		return colorMapper;
	}

	public String getLinkTarget() {
		return linkTarget;
	}

	public String getFont() {
		return font;
	}

}
