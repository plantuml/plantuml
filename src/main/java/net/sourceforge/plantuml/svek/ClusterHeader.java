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
 * Contribution :  Hisashi Miyashita
 * Contribution :  Serge Wenger
 * 
 *
 */
package net.sourceforge.plantuml.svek;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.abel.DisplayPositioned;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.GroupType;
import net.sourceforge.plantuml.activitydiagram3.ftile.EntityImageLegend;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.decoration.symbol.USymbol;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.parser2.StyleQuery;

public final class ClusterHeader {

	private int titleAndAttributeWidth = 0;
	private int titleAndAttributeHeight = 0;
	private final TextBlock title;
	private final TextBlock stereo;
	private final Entity g;

	public ClusterHeader(Entity g, PortionShower portionShower, StringBounder stringBounder) {

		final ISkinParam skinParam = g.getSkinParam();

		this.g = g;
		this.title = getTitleBlock();
		this.stereo = getStereoBlock(portionShower);
		final TextBlock stereoAndTitle = TextBlockUtils.mergeTB(stereo, title, getTitleHorizontalAlignment());
		final XDimension2D dimLabel = stereoAndTitle.calculateDimension(stringBounder);
		if (dimLabel.getWidth() > 0) {
			final XDimension2D dimAttribute = g.getStateDescription(skinParam).calculateDimension(stringBounder);
			final double attributeHeight = dimAttribute.getHeight();
			final double attributeWidth = dimAttribute.getWidth();
			final double marginForFields = attributeHeight > 0 ? IEntityImage.MARGIN : 0;
			final USymbol uSymbol = g.getUSymbol();
			final int suppHeightBecauseOfShape = uSymbol == null ? 0 : uSymbol.suppHeightBecauseOfShape();
			final int suppWidthBecauseOfShape = uSymbol == null ? 0 : uSymbol.suppWidthBecauseOfShape();

			this.titleAndAttributeWidth = (int) Math.max(dimLabel.getWidth(), attributeWidth) + suppWidthBecauseOfShape;
			this.titleAndAttributeHeight = (int) (dimLabel.getHeight() + attributeHeight + marginForFields
					+ suppHeightBecauseOfShape);
		}

	}

	public final int getTitleAndAttributeWidth() {
		return titleAndAttributeWidth;
	}

	public final int getTitleAndAttributeHeight() {
		return titleAndAttributeHeight;
	}

	public final TextBlock getTitle() {
		return title;
	}

	public final TextBlock getStereo() {
		return stereo;
	}

	private TextBlock getTitleBlock() {
		final Display label = g.getDisplay();
		if (label == null)
			return TextBlockUtils.empty(0, 0);

		final Style style = getStyle();

		final FontConfiguration fontConfiguration = style.getFontConfiguration(g.getSkinParam().getIHtmlColorSet(),
				g.getColors());

		final HorizontalAlignment alignment = style.getHorizontalAlignment();
		// final HorizontalAlignment alignment = getTitleHorizontalAlignment();
		// final HorizontalAlignment alignment = HorizontalAlignment.CENTER;
		TextBlock result = label.create(fontConfiguration, alignment, g.getSkinParam());

		final VisibilityModifier modifier = g.getVisibilityModifier();
		if (modifier != null) {
			final Rose rose = new Rose();
			final HColor back = rose.getHtmlColor(g.getSkinParam(), modifier.getBackground());
			final HColor fore = rose.getHtmlColor(g.getSkinParam(), modifier.getForeground());
			final TextBlock uBlock = TextBlockUtils.withMargin(
					modifier.getUBlock(g.getSkinParam().classAttributeIconSize(), fore, back, false), 0, 0, 4, 0);

			result = TextBlockUtils.mergeLR(uBlock, result, VerticalAlignment.CENTER);
		}

		return result;
	}

	private Style getStyle() {
		final StyleQuery signature = getSignature();
		return g.getSkinParam().getCurrentStyleBuilder().getMergedStyle(signature //
				.withTOBECHANGED(g.getStereotype()) //
				.with(g.getStereostyles()));
	}

	private StyleQuery getSignature() {
		final SName sname = g.getSkinParam().getDiagramType().getStyleName();
		final StyleQuery signature;
		final USymbol uSymbol = g.getUSymbol();
		if (g.getGroupType() == GroupType.STATE)
			signature = StyleQuery
					.of(Arrays.asList(SName.root, SName.element, SName.stateDiagram, SName.state, SName.name));
		else if (uSymbol != null) {
			final List<SName> names = new ArrayList<SName>(Arrays.asList(SName.root, SName.element, sname));
			names.addAll(Arrays.asList(uSymbol.getSNames()));
			names.add(SName.composite);
			names.add(SName.title);
			signature = StyleQuery.of(names);
		} else if (g.getGroupType() == GroupType.PACKAGE)
			signature = StyleQuery.of(Arrays.asList(SName.root, SName.element, sname, SName.package_, SName.title));
		else
			signature = StyleQuery.of(Arrays.asList(SName.root, SName.element, sname, SName.composite, SName.title));
		return signature;
	}

	public HorizontalAlignment getTitleHorizontalAlignment() {
		return getStyle().getHorizontalAlignment();
	}

	private TextBlock getStereoBlock(PortionShower portionShower) {
		final TextBlock stereo = getStereoBlockWithoutLegend(portionShower);
		final DisplayPositioned legend = g.getLegend();
		if (legend == null || legend.isNull())
			return stereo;

		final TextBlock legendBlock = EntityImageLegend.create(legend.getDisplay(), g.getSkinParam());
		return DecorateEntityImage.add(null, legendBlock, stereo, legend.getHorizontalAlignment(),
				legend.getVerticalAlignment());
	}

	private TextBlock getStereoBlockWithoutLegend(PortionShower portionShower) {
		final Stereotype stereotype = g.getStereotype();
		// final DisplayPositionned legend = g.getLegend();
		if (stereotype == null)
			return TextBlockUtils.empty(0, 0);

		// A composite state without a USymbol (i.e. not a pseudo-state like <<choice>> or
		// <<start>>) is painted by Cluster.drawUState(), which only draws the title and never
		// this stereotype block -- the stereotype there is purely a style-class selector, the
		// same way a leaf state's stereotype never renders as text (see EntityImageState).
		// Reserving height/width for a block that is never painted leaves a blank gap above the
		// title (issue #2783), so keep the two in sync by not measuring it here either.
		final ISkinParam skinParam = g.getSkinParam();
		if (skinParam.getDiagramType() == DiagramType.STATE && g.getUSymbol() == null)
			return TextBlockUtils.empty(0, 0);

		final TextBlock tmp = stereotype.getSprite(skinParam);
		if (tmp != null)
			return tmp;

		final List<String> stereos = stereotype.getLabels(skinParam.guillemet());
		if (stereos == null)
			return TextBlockUtils.empty(0, 0);

		final List<String> visibleStereotypes = portionShower.getVisibleStereotypeLabels(g);
		if (visibleStereotypes == null || visibleStereotypes.isEmpty())
			return TextBlockUtils.empty(0, 0);

		final Style style = skinParam.getCurrentStyleBuilder()
				.getMergedStyle(Cluster
						.getDefaultStyleDefinition(skinParam.getDiagramType().getStyleName(), g.getUSymbol(),
								g.getGroupType())
						.forStereotypeItself(g.getStereotype()));

		final FontConfiguration fontConfiguration = style.getFontConfiguration(skinParam.getIHtmlColorSet());
		final HorizontalAlignment horizontalAlignment = getTitleHorizontalAlignment();
		return Display.create(visibleStereotypes).create(fontConfiguration, horizontalAlignment, skinParam);

	}

}
