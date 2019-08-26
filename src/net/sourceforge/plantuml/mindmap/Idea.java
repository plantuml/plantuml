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
package net.sourceforge.plantuml.mindmap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.style.StyleBuilder;

class Idea {

	// public StyleDefinition getDefaultStyleDefinition() {
	// return StyleDefinition.of(SName.root, SName.element, SName.mindmapDiagram, SName.node);
	// }

	private final Display label;
	private final int level;
	private final Idea parent;
	private final List<Idea> children = new ArrayList<Idea>();
	private final IdeaShape shape;
	private final HtmlColor backColor;
	private final StyleBuilder styleBuilder;

	public Idea(StyleBuilder styleBuilder, Display label, IdeaShape shape) {
		this(styleBuilder, null, 0, null, label, shape);
	}

	public Idea createIdea(StyleBuilder styleBuilder, HtmlColor backColor, int newLevel, Display newDisplay,
			IdeaShape newShape) {
		final Idea result = new Idea(styleBuilder, backColor, newLevel, this, newDisplay, newShape);
		this.children.add(result);
		return result;
	}

	// public Style getStyle(StyleBuilder styleBuilder) {
	// Style result = getDefaultStyleDefinition().getMergedStyle(styleBuilder);
	// if (backColor != null) {
	// result = result.eventuallyOverride(PName.BackGroundColor, backColor);
	// }
	// return result;
	// }

	private Idea(StyleBuilder styleBuilder, HtmlColor backColor, int level, Idea parent, Display label, IdeaShape shape) {
		this.backColor = backColor;
		this.styleBuilder = styleBuilder;
		this.label = label;
		this.level = level;
		this.parent = parent;
		this.shape = shape;
	}

	@Override
	public String toString() {
		return label.toString();
	}

	public final int getLevel() {
		return level;
	}

	public final Display getLabel() {
		return label;
	}

	public Collection<Idea> getChildren() {
		return Collections.unmodifiableList(children);
	}

	public boolean hasChildren() {
		return children.size() > 0;
	}

	public Idea getParent() {
		return parent;
	}

	public final IdeaShape getShape() {
		return shape;
	}

	public final HtmlColor getBackColor() {
		return backColor;
	}

	public final StyleBuilder getStyleBuilder() {
		return styleBuilder;
	}

}
