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
 *
 */
package net.sourceforge.plantuml.svek.image;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.EntityPosition;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.Bibliotekon;
import net.sourceforge.plantuml.svek.Cluster;
import net.sourceforge.plantuml.svek.SvekNode;

public class EntityImageStateBorder extends AbstractEntityImageBorder {

	public EntityImageStateBorder(Entity leaf, ISkinParam skinParam, Cluster stateParent, final Bibliotekon bibliotekon,
			SName sname) {
		super(leaf, skinParam, stateParent, bibliotekon, FontParam.STATE);
	}

	@Override
	protected StyleSignatureBasic getSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.stateDiagram, SName.state);
	}

	private boolean upPosition() {
		if (parent == null)
			return false;

		final XPoint2D clusterCenter = parent.getRectangleArea().getPointCenter();
		final SvekNode node = bibliotekon.getNode(getEntity());
		return node.getMinY() < clusterCenter.getY();
	}

	final public void drawU(UGraphic ug) {
		final TextBlock desc = getDesc();
		double y = 0;
		final XDimension2D dimDesc = desc.calculateDimension(ug.getStringBounder());
		final double x = 0 - (dimDesc.getWidth() - 2 * EntityPosition.RADIUS) / 2;
		if (upPosition())
			y -= 2 * EntityPosition.RADIUS + dimDesc.getHeight();
		else
			y += 2 * EntityPosition.RADIUS;

		desc.drawU(ug.apply(new UTranslate(x, y)));

		final Style style = getStyle();

		final HColor borderColor = style.value(PName.LineColor).asColor(getSkinParam().getIHtmlColorSet());
		HColor backcolor = getEntity().getColors().getColor(ColorType.BACK);
		if (backcolor == null)
			backcolor = style.value(PName.BackGroundColor).asColor(getSkinParam().getIHtmlColorSet());
		if (backcolor.isTransparent())
			backcolor = getSkinParam().getBackgroundColor();

		ug = ug.apply(getUStroke()).apply(borderColor);
		ug = ug.apply(backcolor.bg());

		entityPosition.drawSymbol(ug, rankdir);
	}

	private UStroke getUStroke() {
		return UStroke.withThickness(1.5);
	}

	public double getMaxWidthFromLabelForEntryExit(StringBounder stringBounder) {
		final TextBlock desc = getDesc();
		final XDimension2D dimDesc = desc.calculateDimension(stringBounder);
		return dimDesc.getWidth();
	}

}
