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
package net.sourceforge.plantuml.mindmap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.annotation.DuplicateCode;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.MergeStrategy;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.wbs.WElement;

class Idea {

	private final Display label;
	private final int level;
	private final Idea parent;
	private final List<Idea> children = new ArrayList<>();
	private final IdeaShape shape;
	private final HColor backColor;
	private final StyleBuilder styleBuilder;
	private final Stereotype stereotype;

	@DuplicateCode(reference = "WElement")
	private StyleSignatureBasic getDefaultStyleDefinitionNode(int level) {
		if (level == 0)
			if (shape == IdeaShape.NONE)
				return StyleSignatureBasic
						.of(SName.root, SName.element, SName.mindmapDiagram, SName.node, SName.rootNode, SName.boxless)
						.addStereotype(stereotype).addLevel(level);
			else
				return StyleSignatureBasic
						.of(SName.root, SName.element, SName.mindmapDiagram, SName.node, SName.rootNode)
						.addStereotype(stereotype).addLevel(level);

		if (shape == IdeaShape.NONE && children.size() == 0)
			return StyleSignatureBasic
					.of(SName.root, SName.element, SName.mindmapDiagram, SName.node, SName.leafNode, SName.boxless)
					.addStereotype(stereotype).addLevel(level);

		if (shape == IdeaShape.NONE)
			return StyleSignatureBasic.of(SName.root, SName.element, SName.mindmapDiagram, SName.node, SName.boxless)
					.addStereotype(stereotype).addLevel(level);

		if (children.size() == 0)
			return StyleSignatureBasic.of(SName.root, SName.element, SName.mindmapDiagram, SName.node, SName.leafNode)
					.addStereotype(stereotype).addLevel(level);

		return StyleSignatureBasic.of(SName.root, SName.element, SName.mindmapDiagram, SName.node)
				.addStereotype(stereotype).addLevel(level);
	}

	private static final int STEP_BY_PARENT = WElement.STEP_BY_PARENT;

	public Style getStyle() {
		int deltaPriority = STEP_BY_PARENT * 1000;
		Style result = styleBuilder.getMergedStyleSpecial(getDefaultStyleDefinitionNode(level), deltaPriority);
		for (Idea up = parent; up != null; up = up.parent) {
			final StyleSignatureBasic ss = up.getDefaultStyleDefinitionNode(level).addStar();
			deltaPriority -= STEP_BY_PARENT;
			final Style styleParent = styleBuilder.getMergedStyleSpecial(ss, deltaPriority);
			result = result.mergeWith(styleParent, MergeStrategy.OVERWRITE_EXISTING_VALUE);
		}
		return result;
	}

	public Style getStyleArrow() {
		final StyleSignatureBasic defaultStyleDefinitionArrow = StyleSignatureBasic
				.of(SName.root, SName.element, SName.mindmapDiagram, SName.arrow).addStereotype(stereotype)
				.addLevel(level);
		return defaultStyleDefinitionArrow.getMergedStyle(styleBuilder);
	}

	public static Idea createIdeaSimple(StyleBuilder styleBuilder, HColor backColor, Display label, IdeaShape shape,
			Stereotype stereotype) {
		return new Idea(styleBuilder, backColor, 0, null, label, shape, stereotype);
	}

	public Idea createIdea(StyleBuilder styleBuilder, HColor backColor, int newLevel, Display newDisplay,
			IdeaShape newShape, Stereotype stereotype) {
		final Idea result = new Idea(styleBuilder, backColor, newLevel, this, newDisplay, newShape, stereotype);
		this.children.add(result);
		return result;
	}

	private Idea(StyleBuilder styleBuilder, HColor backColor, int level, Idea parent, Display label, IdeaShape shape,
			Stereotype stereotype) {
		this.backColor = backColor;
		this.styleBuilder = styleBuilder;
		this.label = label;
		this.level = level;
		this.parent = parent;
		this.shape = shape;
		this.stereotype = stereotype;
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

	public final HColor getBackColor() {
		return backColor;
	}

	public final StyleBuilder getStyleBuilder() {
		return styleBuilder;
	}

	public final Stereotype getStereotype1() {
		return stereotype;
	}

}
