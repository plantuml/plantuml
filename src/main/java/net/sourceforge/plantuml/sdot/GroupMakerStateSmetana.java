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
package net.sourceforge.plantuml.sdot;

import net.atmp.CucaDiagram;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.GroupType;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.svek.IEntityImage;
import net.sourceforge.plantuml.svek.InnerStateAutonom;
import net.sourceforge.plantuml.svek.image.EntityImageState;

// Smetana counterpart of GroupMakerState.
//
// For a composite state group, it lays out the group's content with a nested
// Smetana pass (rooted on the group itself) and wraps the result in an
// InnerStateAutonom, so the group can be turned into a leaf by
// CucaDiagramSimplifierStateSmetana.
//
// NB: concurrent-region aggregation (ConcurrentStates) is not handled yet; the
// simplifier deliberately does not route concurrent states here for now.
public final class GroupMakerStateSmetana {

	private final CucaDiagram diagram;
	private final Entity group;
	private final StringBounder stringBounder;

	public GroupMakerStateSmetana(CucaDiagram diagram, Entity group, StringBounder stringBounder) {
		this.diagram = diagram;
		this.group = group;
		this.stringBounder = stringBounder;
		if (group.isGroup() == false)
			throw new IllegalArgumentException();
	}

	public IEntityImage getImage() {
		if (group.countChildren() == 0 && group.groups().size() == 0)
			return new EntityImageState(group);

		if (group.getGroupType() == GroupType.CONCURRENT_STATE)
			return subLayout();

		if (group.getGroupType() != GroupType.STATE)
			throw new UnsupportedOperationException(group.getGroupType().toString());

		final IEntityImage image = subLayout();
		return new InnerStateAutonom(image, group);
	}

	private IEntityImage subLayout() {
		final CucaDiagramFileMakerSmetana maker = new CucaDiagramFileMakerSmetana(diagram, group);
		return maker.getImage(stringBounder);
	}

}
