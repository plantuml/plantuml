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
package net.sourceforge.plantuml.cucadiagram.dot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.svek.GroupPngMakerActivity;
import net.sourceforge.plantuml.svek.IEntityImage;

public final class CucaDiagramSimplifierActivity {

	private final CucaDiagram diagram;
	private final StringBounder stringBounder;

	public CucaDiagramSimplifierActivity(CucaDiagram diagram, List<String> dotStrings, StringBounder stringBounder)
			throws IOException, InterruptedException {
		this.diagram = diagram;
		this.stringBounder = stringBounder;
		boolean changed;
		do {
			changed = false;
			final Collection<IGroup> groups = new ArrayList<IGroup>(diagram.getGroups(false));
			for (IGroup g : groups) {
				if (diagram.isAutarkic(g)) {
					// final EntityType type;
					// if (g.zgetGroupType() == GroupType.INNER_ACTIVITY) {
					// type = EntityType.ACTIVITY;
					// } else if (g.zgetGroupType() == GroupType.CONCURRENT_ACTIVITY) {
					// type = EntityType.ACTIVITY_CONCURRENT;
					// } else {
					// throw new IllegalStateException();
					// }

					final IEntityImage img = computeImage(g);
					g.overrideImage(img, LeafType.ACTIVITY);

					changed = true;
				}
			}
		} while (changed);
	}

	// private void computeImageGroup(EntityMutable g, EntityMutable proxy, List<String> dotStrings) throws IOException,
	// InterruptedException {
	// final GroupPngMakerActivity maker = new GroupPngMakerActivity(diagram, g);
	// proxy.setSvekImage(maker.getImage());
	// }

	private IEntityImage computeImage(IGroup g) throws IOException, InterruptedException {
		final GroupPngMakerActivity maker = new GroupPngMakerActivity(diagram, g, stringBounder);
		return maker.getImage();
	}

}
