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
package net.sourceforge.plantuml.activitydiagram3.ftile.vertical;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class FtileBlackBlock extends AbstractFtile {

	private final double labelMargin = 5;

	private double width;
	private double height;
	private TextBlock label = TextBlockUtils.empty(0, 0);

	private final Swimlane swimlane;

	public FtileBlackBlock(ISkinParam skinParam, Swimlane swimlane) {
		super(skinParam);
		this.swimlane = swimlane;
	}

	public void setBlackBlockDimension(double width, double height) {
		this.height = height;
		this.width = width;
	}

	public void setLabel(TextBlock label) {
		this.label = Objects.requireNonNull(label);
	}

	@Override
	protected FtileGeometry calculateDimensionFtile(StringBounder stringBounder) {
		double supp = label.calculateDimension(stringBounder).getWidth();
		if (supp > 0)
			supp += labelMargin;

		return new FtileGeometry(width + supp, height, width / 2, 0, height);
	}

	private StyleSignatureBasic getSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.activityBar);
	}

	public void drawU(UGraphic ug) {
		final URectangle rect = URectangle.build(width, height).rounded(5).ignoreForCompressionOnX();

		final Style style = getSignature().getMergedStyle(skinParam().getCurrentStyleBuilder());
		final double shadowing = style.getShadowing();
		rect.setDeltaShadow(shadowing);
		final HColor colorBar = style.value(PName.BackGroundColor).asColor(getIHtmlColorSet());

		ug.apply(colorBar).apply(colorBar.bg()).draw(rect);
		final XDimension2D dimLabel = label.calculateDimension(ug.getStringBounder());
		label.drawU(ug.apply(new UTranslate(width + labelMargin, -dimLabel.getHeight() / 2)));
	}

	public Set<Swimlane> getSwimlanes() {
		return Collections.singleton(swimlane);
	}

	public Swimlane getSwimlaneIn() {
		return swimlane;
	}

	public Swimlane getSwimlaneOut() {
		return swimlane;
	}

	@Override
	public Collection<Ftile> getMyChildren() {
		return Collections.emptyList();
	}

}
