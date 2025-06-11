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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.atmp.CucaDiagram;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.EntityUtils;
import net.sourceforge.plantuml.abel.GroupType;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.cucadiagram.GroupHierarchy;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.dot.DotData;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.svek.image.EntityImageState;

public final class GroupMakerState {

	private final CucaDiagram diagram;
	private final Entity group;
	private final StringBounder stringBounder;
	private final DotMode dotMode;

	class InnerGroupHierarchy implements GroupHierarchy {

		public Entity getRootGroup() {
			throw new UnsupportedOperationException();
		}

		public Collection<Entity> getChildrenGroups(Entity parent) {
			if (parent.isRoot())
				return filter(diagram.getChildrenGroups(group));

			return filter(diagram.getChildrenGroups(parent));
		}

		private Collection<Entity> filter(Collection<Entity> groups) {
			final List<Entity> result = new ArrayList<>();
			for (Entity g : groups)
				if (g.getGroupType() != GroupType.CONCURRENT_STATE)
					result.add(g);

			return result;
		}

		public boolean isEmpty(Entity g) {
			return diagram.isEmpty(g);
		}

	}

	public GroupMakerState(CucaDiagram diagram, Entity group, StringBounder stringBounder, DotMode dotMode) {
		this.diagram = diagram;
		this.stringBounder = stringBounder;
		this.group = group;
		this.dotMode = dotMode;
		if (group.isGroup() == false)
			throw new IllegalArgumentException();

	}

	private List<Link> getPureInnerLinks() {
		final List<Link> result = new ArrayList<>();
		for (Link link : diagram.getLinks())
			if (EntityUtils.isPureInnerLink12(group, link))
				result.add(link);

		return result;
	}

	public IEntityImage getImage() {
		final ISkinParam skinParam = diagram.getSkinParam();

		if (group.countChildren() == 0 && group.groups().size() == 0)
			return new EntityImageState(group);

		if (group.getGroupType() == GroupType.CONCURRENT_STATE)
			return createGeneralImageBuilder(group.leafs()).buildImage(stringBounder, null, new String[0], false);

		if (group.getGroupType() != GroupType.STATE)
			throw new UnsupportedOperationException(group.getGroupType().toString());

		final IEntityImage image;
		if (containsSomeConcurrentStates()) {
			final List<IEntityImage> inners = new ArrayList<>();
			inners.add(createGeneralImageBuilder(filter(group.leafs())).buildImage(stringBounder, null, new String[0],
					false));
			for (Entity inner : group.leafs())
				if (inner.getLeafType() == LeafType.STATE_CONCURRENT)
					inners.add(inner.getSvekImage());
			image = new ConcurrentStates(inners, group.getConcurrentSeparator(), skinParam, group.getStereotype());
		} else {
			image = createGeneralImageBuilder(filter(group.leafs())).buildImage(stringBounder, null, new String[0],
					false);
		}

		return new InnerStateAutonom(image, group);

	}

	protected GraphvizImageBuilder createGeneralImageBuilder(Collection<Entity> leafs) {

		final List<Link> links = getPureInnerLinks();
		final Bibliotekon bibliotekon = new Bibliotekon(links);

		final DotData dotData = new DotData(diagram, group, links, leafs, new InnerGroupHierarchy(), PortionShower.ALL);

		final Cluster root = new Cluster(group.getLocation(), diagram, bibliotekon.getColorSequence(), dotData.getRootGroup());

		final ClusterManager clusterManager = new ClusterManager(bibliotekon, root);
		final DotStringFactory dotStringFactory = new DotStringFactory(bibliotekon, root, diagram.getUmlDiagramType(),
				diagram.getSkinParam());
		return new GraphvizImageBuilder(dotData, diagram.getSource(), diagram.getPragma(), SName.stateDiagram, dotMode,
				dotStringFactory, clusterManager);
	}

	private Collection<Entity> filter(Collection<Entity> leafs) {
		final List<Entity> result = new ArrayList<>();
		for (Entity leaf : leafs)
			if (leaf.getLeafType() != LeafType.STATE_CONCURRENT)
				result.add(leaf);

		return result;
	}

	private boolean containsSomeConcurrentStates() {
		for (Entity entity : group.leafs())
			if (entity.getLeafType() == LeafType.STATE_CONCURRENT)
				return true;

		return false;
	}

}
