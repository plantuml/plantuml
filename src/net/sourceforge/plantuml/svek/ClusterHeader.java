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

import java.util.List;

import net.sourceforge.plantuml.abel.DisplayPositioned;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.EntityPortion;
import net.sourceforge.plantuml.abel.GroupType;
import net.sourceforge.plantuml.activitydiagram3.ftile.EntityImageLegend;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.decoration.symbol.USymbol;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public final class ClusterHeader {

	private int titleAndAttributeWidth = 0;
	private int titleAndAttributeHeight = 0;
	final private TextBlock title;
	final private TextBlock stereo;

	public ClusterHeader(Entity g, ISkinParam skinParam, PortionShower portionShower, StringBounder stringBounder) {

		this.title = getTitleBlock(g, skinParam);
		this.stereo = getStereoBlock(g, skinParam, portionShower);
		final TextBlock stereoAndTitle = TextBlockUtils.mergeTB(stereo, title, HorizontalAlignment.CENTER);
		final XDimension2D dimLabel = stereoAndTitle.calculateDimension(stringBounder);
		if (dimLabel.getWidth() > 0) {
			final XDimension2D dimAttribute = ((Entity) g).getStateHeader(skinParam).calculateDimension(stringBounder);
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

	private TextBlock getTitleBlock(Entity g, ISkinParam skinParam) {
		final Display label = g.getDisplay();
		if (label == null)
			return TextBlockUtils.empty(0, 0);

		final SName sname = skinParam.getUmlDiagramType().getStyleName();
		final StyleSignatureBasic signature;
		final USymbol uSymbol = g.getUSymbol();
		if (g.getGroupType() == GroupType.STATE)
			signature = StyleSignatureBasic.of(SName.root, SName.element, SName.stateDiagram, SName.state,
					SName.composite, SName.title);
		else if (uSymbol != null)
			signature = StyleSignatureBasic.of(SName.root, SName.element, sname, uSymbol.getSName(), SName.composite,
					SName.title);
		else if (g.getGroupType() == GroupType.PACKAGE)
			signature = StyleSignatureBasic.of(SName.root, SName.element, sname, SName.package_, SName.title);
		else
			signature = StyleSignatureBasic.of(SName.root, SName.element, sname, SName.composite, SName.title);

		final Style style = signature //
				.withTOBECHANGED(g.getStereotype()) //
				.with(g.getStereostyles()) //
				.getMergedStyle(skinParam.getCurrentStyleBuilder());

		final FontConfiguration fontConfiguration = style.getFontConfiguration(skinParam.getIHtmlColorSet(),
				g.getColors());

		final HorizontalAlignment alignment = HorizontalAlignment.CENTER;
		return label.create(fontConfiguration, alignment, skinParam);
	}

	private TextBlock getStereoBlock(Entity g, ISkinParam skinParam, PortionShower portionShower) {
		final TextBlock stereo = getStereoBlockWithoutLegend(g, portionShower, skinParam);
		final DisplayPositioned legend = g.getLegend();
		if (legend == null || legend.isNull())
			return stereo;

		final TextBlock legendBlock = EntityImageLegend.create(legend.getDisplay(), skinParam);
		return DecorateEntityImage.add(legendBlock, stereo, legend.getHorizontalAlignment(),
				legend.getVerticalAlignment());
	}

	private TextBlock getStereoBlockWithoutLegend(Entity g, PortionShower portionShower, ISkinParam skinParam) {
		final Stereotype stereotype = g.getStereotype();
		// final DisplayPositionned legend = g.getLegend();
		if (stereotype == null)
			return TextBlockUtils.empty(0, 0);

		final TextBlock tmp = stereotype.getSprite(skinParam);
		if (tmp != null)
			return tmp;

		final List<String> stereos = stereotype.getLabels(skinParam.guillemet());
		if (stereos == null)
			return TextBlockUtils.empty(0, 0);

		final boolean show = portionShower.showPortion(EntityPortion.STEREOTYPE, g);
		if (show == false)
			return TextBlockUtils.empty(0, 0);

		final Style style = Cluster
				.getDefaultStyleDefinition(skinParam.getUmlDiagramType().getStyleName(), g.getUSymbol(), g.getGroupType())
				.forStereotypeItself(g.getStereotype()).getMergedStyle(skinParam.getCurrentStyleBuilder());

		final FontConfiguration fontConfiguration = style.getFontConfiguration(skinParam.getIHtmlColorSet());
		return Display.create(stereos).create(fontConfiguration, HorizontalAlignment.CENTER, skinParam);

	}

}
