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
package net.sourceforge.plantuml.sequencediagram;

import java.util.Set;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.SkinParamBackcolored;
import net.sourceforge.plantuml.SpecificBackcolorable;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.EntityPortion;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.WithStyle;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class Participant implements SpecificBackcolorable, WithStyle {

	private final String code;
	private Display display;
	private final ParticipantType type;

	private int initialLife = 0;

	private Stereotype stereotype;
	private boolean stereotypePositionTop;
	private final Set<EntityPortion> hiddenPortions;
	private final int order;
	private final StyleBuilder styleBuilder;

	// private Style style;

	public StyleSignature getDefaultStyleDefinition() {
		return type.getDefaultStyleDefinition().addClickable(getUrl());
	}

	public Style[] getUsedStyles() {
		if (SkinParam.USE_STYLES() == false) {
			return null;
		}
		final StyleSignature signature = getDefaultStyleDefinition().with(stereotype);
		Style tmp = signature.getMergedStyle(styleBuilder);
		tmp = tmp.eventuallyOverride(getColors(null));
		Style stereo = getDefaultStyleDefinition().withStereotype(stereotype).getMergedStyle(styleBuilder);
		if (tmp != null) {
			stereo = tmp.mergeWith(stereo);
		}
		return new Style[] { tmp, stereo };
	}

	public Participant(ParticipantType type, String code, Display display, Set<EntityPortion> hiddenPortions,
			int order, StyleBuilder styleBuilder) {
		this.hiddenPortions = hiddenPortions;
		this.styleBuilder = styleBuilder;
		this.order = order;
		if (type == null) {
			throw new IllegalArgumentException();
		}
		if (code == null || code.length() == 0) {
			throw new IllegalArgumentException();
		}
		if (Display.isNull(display) || display.size() == 0) {
			throw new IllegalArgumentException();
		}
		this.code = code;
		this.type = type;
		this.display = display;
		// if (SkinParam.USE_STYLES()) {
		// this.style = getDefaultStyleDefinition().getMergedStyle(styleBuilder);
		// }
	}

	public String getCode() {
		return code;
	}

	@Override
	public String toString() {
		return getCode();
	}

	public Display getDisplay(boolean underlined) {
		Display result = underlined ? display.underlined() : display;
		if (stereotype != null && hiddenPortions.contains(EntityPortion.STEREOTYPE) == false) {
			if (stereotypePositionTop) {
				result = result.addFirst(stereotype);
			} else {
				result = result.add(stereotype);
			}
		}
		return result;
	}

	public ParticipantType getType() {
		return type;
	}

	public final void setStereotype(Stereotype stereotype, boolean stereotypePositionTop) {
		if (this.stereotype != null) {
			throw new IllegalStateException();
		}
		if (stereotype == null) {
			throw new IllegalArgumentException();
		}
		this.stereotype = stereotype;
		this.stereotypePositionTop = stereotypePositionTop;

		// if (SkinParam.USE_STYLES()) {
		// for (Style style : stereotype.getStyles(styleBuilder)) {
		// this.style = this.style.mergeWith(style);
		// }
		// }
	}

	public final int getInitialLife() {
		return initialLife;
	}

	private SymbolContext liveBackcolors;

	public final void incInitialLife(SymbolContext colors) {
		initialLife++;
		this.liveBackcolors = colors;
	}

	public SymbolContext getLiveSpecificBackColors() {
		return liveBackcolors;
	}

	public Colors getColors(ISkinParam skinParam) {
		return colors;
	}

	public void setSpecificColorTOBEREMOVED(ColorType type, HColor color) {
		if (color != null) {
			this.colors = colors.add(type, color);
		}
	}

	private Colors colors = Colors.empty();

	public void setColors(Colors colors) {
		this.colors = colors;
	}

	private Url url;

	public final Url getUrl() {
		return url;
	}

	public final void setUrl(Url url) {
		this.url = url;
	}

	public final Stereotype getStereotype() {
		return stereotype;
	}

	public ColorParam getBackgroundColorParam() {
		return type.getBackgroundColorParam();
	}

	public SkinParamBackcolored getSkinParamBackcolored(ISkinParam skinParam) {
		HColor specificBackColor = getColors(skinParam).getColor(ColorType.BACK);
		final boolean clickable = getUrl() != null;
		final HColor stereoBackColor = skinParam.getHtmlColor(getBackgroundColorParam(), getStereotype(), clickable);
		if (stereoBackColor != null && specificBackColor == null) {
			specificBackColor = stereoBackColor;
		}
		final SkinParamBackcolored result = new SkinParamBackcolored(skinParam, specificBackColor, clickable);
		final HColor stereoBorderColor = skinParam.getHtmlColor(ColorParam.participantBorder, getStereotype(),
				clickable);
		if (stereoBorderColor != null) {
			result.forceColor(ColorParam.participantBorder, stereoBorderColor);
		}
		return result;
	}

	public int getOrder() {
		return order;
	}

}
