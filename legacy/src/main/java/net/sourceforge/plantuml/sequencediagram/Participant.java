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
package net.sourceforge.plantuml.sequencediagram;

import java.util.Objects;
import java.util.Set;

import net.sourceforge.plantuml.abel.EntityPortion;
import net.sourceforge.plantuml.abel.SpecificBackcolorable;
import net.sourceforge.plantuml.klimt.Fashion;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.MergeStrategy;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.style.WithStyle;
import net.sourceforge.plantuml.url.Url;

public class Participant implements SpecificBackcolorable, WithStyle {
    // ::remove folder when __HAXE__

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

	public StyleSignatureBasic getStyleSignature() {
		return type.getStyleSignature().addClickable(getUrl());
	}

	public Style[] getUsedStyles() {

		final StyleSignature signature = getStyleSignature().withTOBECHANGED(stereotype);
		Style tmp = signature.getMergedStyle(styleBuilder);
		tmp = tmp.eventuallyOverride(getColors());
		Style stereo = getStyleSignature().forStereotypeItself(stereotype).getMergedStyle(styleBuilder);
		if (tmp != null)
			stereo = tmp.mergeWith(stereo, MergeStrategy.OVERWRITE_EXISTING_VALUE);

		return new Style[] { tmp, stereo };
	}

	public Participant(ParticipantType type, String code, Display display, Set<EntityPortion> hiddenPortions, int order,
			StyleBuilder styleBuilder) {
		this.hiddenPortions = hiddenPortions;
		this.styleBuilder = styleBuilder;
		this.order = order;
		this.code = Objects.requireNonNull(code);
		if (code.length() == 0)
			throw new IllegalArgumentException();

		if (Display.isNull(display) || display.size() == 0)
			throw new IllegalArgumentException();

		this.type = Objects.requireNonNull(type);
		this.display = display;
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
			if (stereotypePositionTop)
				result = result.addFirst(stereotype);
			else
				result = result.add(stereotype);

		}
		return result;
	}

	public ParticipantType getType() {
		return type;
	}

	public final void setStereotype(Stereotype stereotype, boolean stereotypePositionTop) {
		if (this.stereotype != null)
			throw new IllegalStateException();

		this.stereotype = Objects.requireNonNull(stereotype);
		this.stereotypePositionTop = stereotypePositionTop;
	}

	public final int getInitialLife() {
		return initialLife;
	}

	private Fashion liveBackcolors;

	public final void incInitialLife(Fashion colors) {
		initialLife++;
		this.liveBackcolors = colors;
	}

	public Fashion getLiveSpecificBackColors() {
		return liveBackcolors;
	}

	public Colors getColors() {
		return colors;
	}

	public void setSpecificColorTOBEREMOVED(ColorType type, HColor color) {
		if (color != null)
			this.colors = colors.add(type, color);

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

	public int getOrder() {
		return order;
	}

}
