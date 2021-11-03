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
package net.sourceforge.plantuml.svek;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.UseStyle;
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
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.cucadiagram.SuperGroup;
import net.sourceforge.plantuml.cucadiagram.dot.DotData;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.svek.image.EntityImageState;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public final class GroupPngMakerState {

	private final CucaDiagram diagram;
	private final IGroup group;
	private final StringBounder stringBounder;

	class InnerGroupHierarchy implements GroupHierarchy {

		public Set<SuperGroup> getAllSuperGroups() {
			throw new UnsupportedOperationException();
		}

		public IGroup getRootGroup() {
			throw new UnsupportedOperationException();
		}

		public SuperGroup getRootSuperGroup() {
			throw new UnsupportedOperationException();
		}

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

	public GroupPngMakerState(CucaDiagram diagram, IGroup group, StringBounder stringBounder) {
		this.diagram = diagram;
		this.stringBounder = stringBounder;
		this.group = group;
		if (group.isGroup() == false) {
			throw new IllegalArgumentException();
		}
	}

	private List<Link> getPureInnerLinks() {
		final List<Link> result = new ArrayList<>();
		for (Link link : diagram.getLinks()) {
			if (EntityUtils.isPureInnerLink12(group, link)) {
				result.add(link);
			}
		}
		return result;
	}

	private Style getStyleStateHeader() {
		return StyleSignature.of(SName.root, SName.element, SName.stateDiagram, SName.state, SName.header)
				.with(group.getStereotype()).getMergedStyle(diagram.getSkinParam().getCurrentStyleBuilder());
	}

	private Style getStyleState() {
		return StyleSignature.of(SName.root, SName.element, SName.stateDiagram, SName.state).with(group.getStereotype())
				.getMergedStyle(diagram.getSkinParam().getCurrentStyleBuilder());
	}

	public IEntityImage getImage() {
		final Display display = group.getDisplay();
		final ISkinParam skinParam = diagram.getSkinParam();

		final FontConfiguration fontConfiguration;
		final double rounded;
		double shadowing = 0;

		if (UseStyle.useBetaStyle()) {
			rounded = getStyleState().value(PName.RoundCorner).asDouble();
			shadowing = getStyleState().value(PName.Shadowing).asDouble();
			fontConfiguration = getStyleStateHeader().getFontConfiguration(skinParam.getThemeStyle(),
					skinParam.getIHtmlColorSet());
		} else {
			rounded = IEntityImage.CORNER;
			fontConfiguration = new FontConfiguration(skinParam, FontParam.STATE, group.getStereotype());
			if (skinParam.shadowing(group.getStereotype()))
				shadowing = 3.0;
		}

		final TextBlock title = display.create(fontConfiguration, HorizontalAlignment.CENTER, diagram.getSkinParam());

		if (group.size() == 0 && group.getChildren().size() == 0) {
			return new EntityImageState(group, diagram.getSkinParam());
		}
		final List<Link> links = getPureInnerLinks();

		final DotData dotData = new DotData(group, links, group.getLeafsDirect(), diagram.getUmlDiagramType(),
				skinParam, new InnerGroupHierarchy(), diagram.getColorMapper(), diagram.getEntityFactory(),
				diagram.isHideEmptyDescriptionForState(), DotMode.NORMAL, diagram.getNamespaceSeparator(),
				diagram.getPragma());

		final GeneralImageBuilder svek2 = new GeneralImageBuilder(false, dotData, diagram.getEntityFactory(),
				diagram.getSource(), diagram.getPragma(), stringBounder, SName.stateDiagram);

		if (group.getGroupType() == GroupType.CONCURRENT_STATE) {
			// return new InnerStateConcurrent(svek2.createFile());
			return svek2.buildImage(null, new String[0]);
		}

		if (group.getGroupType() != GroupType.STATE) {
			throw new UnsupportedOperationException(group.getGroupType().toString());
		}

		HColor borderColor = group.getColors(skinParam).getColor(ColorType.LINE);
		if (borderColor == null) {
			if (UseStyle.useBetaStyle())
				borderColor = getStyleState().value(PName.LineColor).asColor(skinParam.getThemeStyle(),
						skinParam.getIHtmlColorSet());
			else
				borderColor = getColor(ColorParam.stateBorder, group.getStereotype());
		}
		final Stereotype stereo = group.getStereotype();
		final HColor tmp = group.getColors(skinParam).getColor(ColorType.BACK);
		final HColor backColor;
		if (tmp == null)
			if (UseStyle.useBetaStyle())
				backColor =

						getStyleState().value(PName.BackGroundColor).asColor(skinParam.getThemeStyle(),
								skinParam.getIHtmlColorSet());
			else
				backColor = getColor(ColorParam.stateBackground, stereo);
		else
			backColor = tmp;

		final TextBlock attribute = GeneralImageBuilder.stateHeader((IEntity) group, null, skinParam);

		final Stereotype stereotype = group.getStereotype();
		final boolean withSymbol = stereotype != null && stereotype.isWithOOSymbol();

		final boolean containsOnlyConcurrentStates = containsOnlyConcurrentStates(dotData);
		final IEntityImage image = containsOnlyConcurrentStates ? buildImageForConcurrentState(dotData)
				: svek2.buildImage(null, new String[0]);
		UStroke stroke = group.getColors(skinParam).getSpecificLineStroke();
		if (stroke == null) {
			stroke = new UStroke(1.5);
		}
		return new InnerStateAutonom(image, title, attribute, borderColor, backColor, group.getUrl99(), withSymbol,
				stroke, rounded, shadowing);

	}

	private IEntityImage buildImageForConcurrentState(DotData dotData) {
		final List<IEntityImage> inners = new ArrayList<>();
		for (ILeaf inner : dotData.getLeafs()) {
			inners.add(inner.getSvekImage());
		}
		return new CucaDiagramFileMakerSvek2InternalImage(inners, dotData.getTopParent().getConcurrentSeparator(),
				dotData.getSkinParam(), group.getStereotype());

	}

	private boolean containsOnlyConcurrentStates(DotData dotData) {
		for (ILeaf leaf : dotData.getLeafs()) {
			if (leaf instanceof IGroup == false) {
				return false;
			}
			if (((IGroup) leaf).getLeafType() != LeafType.STATE_CONCURRENT) {
				return false;
			}
		}
		return true;
	}

	private final Rose rose = new Rose();

	private HColor getColor(ColorParam colorParam, Stereotype stereo) {
		final ISkinParam skinParam = diagram.getSkinParam();
		return rose.getHtmlColor(skinParam, stereo, colorParam);
	}
}
