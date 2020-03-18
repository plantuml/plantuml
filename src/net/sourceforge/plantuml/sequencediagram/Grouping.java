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

import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.WithStyle;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public abstract class Grouping implements Event, WithStyle {

	private final String title;
	private final GroupingType type;
	private final String comment;
	private final HColor backColorElement;

	// private final StyleBuilder styleBuilder;

	final private Style style;
	final private Style styleHeader;

	public StyleSignature getDefaultStyleDefinition() {
		return StyleSignature.of(SName.root, SName.element, SName.sequenceDiagram, SName.group);
	}

	private StyleSignature getHeaderStyleDefinition() {
		return StyleSignature.of(SName.root, SName.element, SName.sequenceDiagram, SName.groupHeader);
	}

	public Style[] getUsedStyles() {
		return new Style[] {
				style,
				styleHeader == null ? styleHeader : styleHeader.eventuallyOverride(PName.BackGroundColor,
						backColorElement) };
	}

	public Grouping(String title, String comment, GroupingType type, HColor backColorElement,
			StyleBuilder styleBuilder) {
		this.title = title;
		// this.styleBuilder = styleBuilder;
		this.comment = comment;
		this.type = type;
		this.backColorElement = backColorElement;
		this.style = getDefaultStyleDefinition().getMergedStyle(styleBuilder);
		this.styleHeader = getHeaderStyleDefinition().getMergedStyle(styleBuilder);
	}

	@Override
	public final String toString() {
		return super.toString() + " " + type + " " + title;
	}

	final public String getTitle() {
		return title;
	}

	final public GroupingType getType() {
		return type;
	}

	public abstract int getLevel();

	public abstract HColor getBackColorGeneral();

	final public String getComment() {
		return comment;
	}

	public final HColor getBackColorElement() {
		return backColorElement;
	}

	public abstract boolean isParallel();

}
