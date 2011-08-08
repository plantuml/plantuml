/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 6711 $
 *
 */
package net.sourceforge.plantuml.svek;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.SkinParamBackcolored;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Group;
import net.sourceforge.plantuml.cucadiagram.GroupHierarchy;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.cucadiagram.dot.DotData;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.sequencediagram.GroupingType;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UFont;

public final class GroupPngMaker2 {

	private final CucaDiagram diagram;
	private final Group group;

	class InnerGroupHierarchy implements GroupHierarchy {

		public Collection<Group> getChildrenGroups(Group parent) {
			if (parent == null) {
				return diagram.getChildrenGroups(group);
			}
			return diagram.getChildrenGroups(parent);
		}

		public boolean isEmpty(Group g) {
			return diagram.isEmpty(g);
		}

	}

	public GroupPngMaker2(CucaDiagram diagram, Group group) {
		this.diagram = diagram;
		this.group = group;
	}

	private List<Link> getPureInnerLinks() {
		final List<Link> result = new ArrayList<Link>();
		for (Link link : diagram.getLinks()) {
			final IEntity e1 = link.getEntity1();
			final IEntity e2 = link.getEntity2();
			if (e1.getParent() == group && e1.getType() != EntityType.GROUP && e2.getParent() == group
					&& e2.getType() != EntityType.GROUP) {
				result.add(link);
			}
		}
		return result;
	}

	public IEntityImage getImage() throws IOException, InterruptedException {
		final String display = group.getDisplay();
		final TextBlock title = TextBlockUtils.create(StringUtils.getWithNewlines(display), new FontConfiguration(
				getFont(FontParam.STATE), HtmlColor.BLACK), HorizontalAlignement.CENTER);

		if (group.entities().size() == 0) {
			return new EntityImageState(group.getEntityCluster(), diagram.getSkinParam());
		}
		final List<Link> links = getPureInnerLinks();
		ISkinParam skinParam = diagram.getSkinParam();
		if (OptionFlags.PBBACK && group.getBackColor() != null) {
			skinParam = new SkinParamBackcolored(skinParam, null, group.getBackColor());
		}
		final DotData dotData = new DotData(group, links, group.entities(), diagram.getUmlDiagramType(), skinParam,
				group.getRankdir(), new InnerGroupHierarchy(), diagram.getColorMapper());

		final CucaDiagramFileMakerSvek2 svek2 = new CucaDiagramFileMakerSvek2(dotData);

		if (group.getType() == GroupType.CONCURRENT_STATE) {
			return new InnerStateConcurrent(svek2.createFile());
		} else if (group.getType() == GroupType.INNER_ACTIVITY) {
			final HtmlColor borderColor = getColor(ColorParam.stateBorder, null);
			return new InnerActivity(svek2.createFile(), borderColor);
		} else if (group.getType() == GroupType.STATE) {
			final HtmlColor borderColor = getColor(ColorParam.stateBorder, null);
			final HtmlColor backColor = getColor(ColorParam.stateBackground, null);
			return new InnerStateAutonom(svek2.createFile(), title, borderColor, backColor);
		}

		throw new UnsupportedOperationException(group.getType().toString());

	}

	private UFont getFont(FontParam fontParam) {
		final ISkinParam skinParam = diagram.getSkinParam();
		return skinParam.getFont(fontParam, null);
	}

	private final Rose rose = new Rose();

	protected final HtmlColor getColor(ColorParam colorParam, Stereotype stereo) {
		final String s = stereo == null ? null : stereo.getLabel();
		final ISkinParam skinParam = diagram.getSkinParam();
		return rose.getHtmlColor(skinParam, colorParam, s);
	}
}
