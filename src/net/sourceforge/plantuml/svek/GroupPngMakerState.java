/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.EntityUtils;
import net.sourceforge.plantuml.cucadiagram.GroupHierarchy;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.Member;
import net.sourceforge.plantuml.cucadiagram.MethodsOrFieldsArea;
import net.sourceforge.plantuml.cucadiagram.Rankdir;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.cucadiagram.dot.DotData;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockEmpty;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.TextBlockWidth;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.svek.image.EntityImageState;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UStroke;

public final class GroupPngMakerState {

	private final CucaDiagram diagram;
	private final IGroup group;

	class InnerGroupHierarchy implements GroupHierarchy {

		public Collection<IGroup> getChildrenGroups(IGroup parent) {
			if (EntityUtils.groupRoot(parent)) {
				return diagram.getChildrenGroups(group);
			}
			return diagram.getChildrenGroups(parent);
		}

		public boolean isEmpty(IGroup g) {
			return diagram.isEmpty(g);
		}

	}

	public GroupPngMakerState(CucaDiagram diagram, IGroup group) {
		this.diagram = diagram;
		this.group = group;
		if (group.isGroup() == false) {
			throw new IllegalArgumentException();
		}
	}

	private List<Link> getPureInnerLinks() {
		final List<Link> result = new ArrayList<Link>();
		for (Link link : diagram.getLinks()) {
			if (EntityUtils.isPureInnerLink12(group, link)) {
				result.add(link);
			}
		}
		return result;
	}

	public IEntityImage getImage() {
		final Display display = group.getDisplay();
		final ISkinParam skinParam = diagram.getSkinParam();
		final TextBlock title = TextBlockUtils.create(display, new FontConfiguration(getFont(FontParam.STATE),
				HtmlColorUtils.BLACK, skinParam.getHyperlinkColor(), skinParam.useUnderlineForHyperlink()),
				HorizontalAlignment.CENTER, diagram.getSkinParam());

		if (group.size() == 0) {
			return new EntityImageState(group, diagram.getSkinParam());
		}
		final List<Link> links = getPureInnerLinks();

		// boolean hasVerticalLine = false;
		// for (ILeaf leaf : group.getLeafsDirect()) {
		// if (leaf.getEntityType() == LeafType.STATE_CONCURRENT) {
		// hasVerticalLine = true;
		// }
		// }

		final DotData dotData = new DotData(group, links, group.getLeafsDirect(), diagram.getUmlDiagramType(),
				skinParam, new InnerGroupHierarchy(), diagram.getColorMapper(), diagram.getEntityFactory(),
				diagram.isHideEmptyDescriptionForState(), DotMode.NORMAL, diagram.getNamespaceSeparator(),
				diagram.getPragma());

		final CucaDiagramFileMakerSvek2 svek2 = new CucaDiagramFileMakerSvek2(dotData, diagram.getEntityFactory(),
				diagram.getSource(), diagram.getPragma());
		UStroke stroke = group.getSpecificLineStroke();
		if (stroke == null) {
			stroke = new UStroke(1.5);
		}

		if (group.getGroupType() == GroupType.CONCURRENT_STATE) {
			// return new InnerStateConcurrent(svek2.createFile());
			return svek2.createFile();
		} else if (group.getGroupType() == GroupType.STATE) {
			HtmlColor borderColor = group.getSpecificLineColor();
			if (borderColor == null) {
				borderColor = getColor(ColorParam.stateBorder, group.getStereotype());
			}
			final Stereotype stereo = group.getStereotype();
			final HtmlColor backColor = group.getSpecificBackColor() == null ? getColor(ColorParam.stateBackground,
					stereo) : group.getSpecificBackColor();
			final List<Member> members = ((IEntity) group).getBodier().getFieldsToDisplay();
			final TextBlockWidth attribute;
			if (members.size() == 0) {
				attribute = new TextBlockEmpty();
			} else {
				attribute = new MethodsOrFieldsArea(members, FontParam.STATE_ATTRIBUTE, diagram.getSkinParam());
			}

			final Stereotype stereotype = group.getStereotype();
			final boolean withSymbol = stereotype != null && stereotype.isWithOOSymbol();

			final boolean containsOnlyConcurrentStates = containsOnlyConcurrentStates(dotData);
			final IEntityImage image = containsOnlyConcurrentStates ? svek2.createFileForConcurrentState() : svek2
					.createFile();
			return new InnerStateAutonom(image, title, attribute, borderColor, backColor, skinParam.shadowing(),
					group.getUrl99(), withSymbol, stroke);
		}

		throw new UnsupportedOperationException(group.getGroupType().toString());

	}

	private boolean containsOnlyConcurrentStates(DotData dotData) {
		for (ILeaf leaf : dotData.getLeafs()) {
			if (leaf instanceof IGroup == false) {
				return false;
			}
			if (((IGroup) leaf).getEntityType() != LeafType.STATE_CONCURRENT) {
				return false;
			}
		}
		return true;
	}

	private UFont getFont(FontParam fontParam) {
		final ISkinParam skinParam = diagram.getSkinParam();
		return skinParam.getFont(fontParam, null, false);
	}

	private final Rose rose = new Rose();

	protected final HtmlColor getColor(ColorParam colorParam, Stereotype stereo) {
		final ISkinParam skinParam = diagram.getSkinParam();
		return rose.getHtmlColor(skinParam, colorParam, stereo);
	}
}
