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
package net.sourceforge.plantuml.posimo;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class LabelImage {

	// private final Entity entity;
	final private ISkinParam param;
	final private Rose rose;
	final private TextBlock name;

	public LabelImage(Link link, Rose rose, ISkinParam param) {
		if (link == null) {
			throw new IllegalArgumentException();
		}
		// this.entity = entity;
		this.param = param;
		this.rose = rose;
//		this.name = link.getLabel().create(
//				new FontConfiguration(param.getFont(FontParam.CLASS, null, false), HtmlColorUtils.BLACK,
//						param.getHyperlinkColor(), param.useUnderlineForHyperlink()), HorizontalAlignment.CENTER,
//				new SpriteContainerEmpty());
		throw new UnsupportedOperationException();
	}

	public Dimension2D getDimension(StringBounder stringBounder) {
		final Dimension2D dim = name.calculateDimension(stringBounder);
		return dim;
		// return Dimension2DDouble.delta(dim, 2 * margin);
	}

	public void drawU(UGraphic ug, double x, double y) {
		// final Dimension2D dim = getDimension(ug.getStringBounder());
		// ug.getParam().setBackcolor(rose.getHtmlColor(param,
		// ColorParam.classBackground).getColor());
		// ug.getParam().setColor(rose.getHtmlColor(param,
		// ColorParam.classBorder).getColor());
		// ug.draw(x, y, new URectangle(dim.getWidth(), dim.getHeight()));
		name.drawU(ug.apply(new UTranslate(x, y)));
	}
}
