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
 * Revision $Revision: 6009 $
 *
 */
package net.sourceforge.plantuml.skin;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;

class GrayComponent extends AbstractComponent {

	private static final Font NORMAL = new Font("SansSerif", Font.PLAIN, 7);

	private final ComponentType type;

	public GrayComponent(ComponentType type) {
		this.type = type;
	}

	@Override
	protected void drawInternalU(UGraphic ug, Dimension2D dimensionToUse) {
		final StringBounder stringBounder = ug.getStringBounder();
		ug.getParam().setBackcolor(Color.LIGHT_GRAY);
		ug.getParam().setColor(Color.BLACK);
		ug.draw(0, 0, new URectangle(getPreferredWidth(stringBounder), getPreferredHeight(stringBounder)));

		final String n = type.name();
		final int split = 9;
		final List<String> strings = new ArrayList<String>();
		for (int i = 0; i < n.length(); i += split) {
			strings.add(n.substring(i, Math.min(i + split, n.length())));
		}

		final TextBlock textBlock = TextBlockUtils.create(strings, new FontConfiguration(NORMAL, Color.BLACK),
				HorizontalAlignement.LEFT);
		textBlock.drawU(ug, 0, 0);
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return 42;
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return 42;
	}

}
