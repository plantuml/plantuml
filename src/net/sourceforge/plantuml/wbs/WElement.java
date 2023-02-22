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
package net.sourceforge.plantuml.wbs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.mindmap.IdeaShape;
import net.sourceforge.plantuml.skin.SkinParamColors;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.MergeStrategy;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.utils.Direction;

final public class WElement {

	private final HColor backColor;
	private final Display label;
	private final int level;
	private final String stereotype;
	private final WElement parent;
	private final StyleBuilder styleBuilder;
	private final List<WElement> childrenLeft = new ArrayList<>();
	private final List<WElement> childrenRight = new ArrayList<>();
	private final IdeaShape shape;
	private UTranslate position;
	private XDimension2D dimension;

	private StyleSignatureBasic getDefaultStyleDefinitionNode(int level) {
		final String depth = SName.depth(level);
		if (level == 0)
			return StyleSignatureBasic.of(SName.root, SName.element, SName.wbsDiagram, SName.node, SName.rootNode)
					.addS(stereotype).add(depth);

		if (shape == IdeaShape.NONE && isLeaf())
			return StyleSignatureBasic
					.of(SName.root, SName.element, SName.wbsDiagram, SName.node, SName.leafNode, SName.boxless)
					.addS(stereotype).add(depth);

		if (isLeaf())
			return StyleSignatureBasic.of(SName.root, SName.element, SName.wbsDiagram, SName.node, SName.leafNode)
					.addS(stereotype).add(depth);

		if (shape == IdeaShape.NONE)
			return StyleSignatureBasic.of(SName.root, SName.element, SName.wbsDiagram, SName.node, SName.boxless)
					.addS(stereotype).add(depth);

		return StyleSignatureBasic.of(SName.root, SName.element, SName.wbsDiagram, SName.node).addS(stereotype)
				.add(depth);
	}

	public ISkinParam withBackColor(ISkinParam skinParam) {
		if (backColor == null)
			return skinParam;

		return new SkinParamColors(skinParam, Colors.empty().add(ColorType.BACK, backColor));
	}

	public static final int STEP_BY_PARENT = 1000_1000;

	public Style getStyle() {
		int deltaPriority = STEP_BY_PARENT * 1000;
		Style result = styleBuilder.getMergedStyleSpecial(getDefaultStyleDefinitionNode(level), deltaPriority);
		for (WElement up = parent; up != null; up = up.parent) {
			final StyleSignatureBasic ss = up.getDefaultStyleDefinitionNode(level).addStar();
			deltaPriority -= STEP_BY_PARENT;
			final Style styleParent = styleBuilder.getMergedStyleSpecial(ss, deltaPriority);
			result = result.mergeWith(styleParent, MergeStrategy.OVERWRITE_EXISTING_VALUE);
		}
		return result;
	}

	public WElement(HColor backColor, Display label, String stereotype, StyleBuilder styleBuilder, IdeaShape shape) {
		this(backColor, 0, label, stereotype, null, shape, styleBuilder);
	}

	private WElement(HColor backColor, int level, Display label, String stereotype, WElement parent, IdeaShape shape,
			StyleBuilder styleBuilder) {
		this.label = label;
		this.backColor = backColor;
		this.level = level;
		this.parent = parent;
		this.shape = shape;
		this.styleBuilder = styleBuilder;
		this.stereotype = stereotype;
	}

	public boolean isLeaf() {
		return childrenLeft.size() == 0 && childrenRight.size() == 0;
	}

	public WElement createElement(HColor backColor, int newLevel, Display newLabel, String stereotype,
			Direction direction, IdeaShape shape, StyleBuilder styleBuilder) {
		final WElement result = new WElement(backColor, newLevel, newLabel, stereotype, this, shape, styleBuilder);
		if (direction == Direction.LEFT && newLevel == 1)
			this.childrenRight.add(0, result);
		if (direction == Direction.LEFT)
			this.childrenLeft.add(result);
		else
			this.childrenRight.add(result);

		return result;
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

	public Collection<WElement> getChildren(Direction direction) {
		if (direction == Direction.LEFT)
			return Collections.unmodifiableList(childrenLeft);
		return Collections.unmodifiableList(childrenRight);
	}

	public WElement getParent() {
		return parent;
	}

	public final IdeaShape getShape() {
		return shape;
	}

	public final StyleBuilder getStyleBuilder() {
		return styleBuilder;
	}

	public final void setGeometry(UTranslate position, XDimension2D dimension) {
		this.position = position;
		this.dimension = dimension;
	}

	public final UTranslate getPosition() {
		return position;
	}

	public final XDimension2D getDimension() {
		return dimension;
	}

}
