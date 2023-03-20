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
package net.sourceforge.plantuml.svek;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.GroupType;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.cucadiagram.GroupHierarchy;
import net.sourceforge.plantuml.cucadiagram.ICucaDiagram;
import net.sourceforge.plantuml.dot.DotData;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.skin.ColorParam;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.image.EntityImageState;

public final class GroupMakerActivity {

	private final ICucaDiagram diagram;
	private final Entity group;
	private final StringBounder stringBounder;

	class InnerGroupHierarchy implements GroupHierarchy {

		public Entity getRootGroup() {
			throw new UnsupportedOperationException();
		}

		public Collection<Entity> getChildrenGroups(Entity parent) {
			if (parent.isRoot())
				return diagram.getChildrenGroups(group);

			return diagram.getChildrenGroups(parent);
		}

		public boolean isEmpty(Entity g) {
			return diagram.isEmpty(g);
		}

	}

	public GroupMakerActivity(ICucaDiagram diagram, Entity group, StringBounder stringBounder) {
		this.diagram = diagram;
		this.group = group;
		this.stringBounder = stringBounder;
	}

	private List<Link> getPureInnerLinks() {
		final List<Link> result = new ArrayList<>();
		for (Link link : diagram.getLinks()) {
			final Entity e1 = link.getEntity1();
			final Entity e2 = link.getEntity2();
			if (e1.getParentContainer() == group && e1.isGroup() == false && e2.getParentContainer() == group
					&& e2.isGroup() == false)
				result.add(link);

		}
		return result;
	}

	final public StyleSignatureBasic getDefaultStyleDefinitionGroup() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.group);
	}

	public IEntityImage getImage() throws IOException, InterruptedException {
		if (group.countChildren() == 0)
			return new EntityImageState(group, diagram.getSkinParam());

		final List<Link> links = getPureInnerLinks();
		final ISkinParam skinParam = diagram.getSkinParam();

		final DotData dotData = new DotData(group, links, group.leafs(), diagram.getUmlDiagramType(), skinParam,
				new InnerGroupHierarchy(), diagram.getEntityFactory(), false, DotMode.NORMAL,
				diagram.getNamespaceSeparator(), diagram.getPragma());

		final GeneralImageBuilder svek2 = new GeneralImageBuilder(dotData, diagram.getEntityFactory(),
				diagram.getSource(), diagram.getPragma(), stringBounder, SName.activityDiagram);

		if (group.getGroupType() == GroupType.INNER_ACTIVITY) {
			final Stereotype stereo = group.getStereotype();
			final HColor borderColor = getColor(ColorParam.activityBorder, stereo);
			final HColor backColor = group.getColors().getColor(ColorType.BACK) == null
					? getColor(ColorParam.background, stereo)
					: group.getColors().getColor(ColorType.BACK);

			final Style style = getDefaultStyleDefinitionGroup().getMergedStyle(skinParam.getCurrentStyleBuilder());
			final double shadowing = style.value(PName.Shadowing).asDouble();

			return new InnerActivity(svek2.buildImage(null, new String[0], false), borderColor, backColor, shadowing);
		}

		throw new UnsupportedOperationException(group.getGroupType().toString());

	}

	private final Rose rose = new Rose();

	protected final HColor getColor(ColorParam colorParam, Stereotype stereo) {
		final ISkinParam skinParam = diagram.getSkinParam();
		return rose.getHtmlColor(skinParam, stereo, colorParam);
	}
}
