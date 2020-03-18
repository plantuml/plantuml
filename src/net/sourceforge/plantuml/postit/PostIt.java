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
package net.sourceforge.plantuml.postit;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.skin.rose.ComponentRoseNote;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class PostIt {

	private final String id;
	private final Display text;

	private Dimension2D minimumDimension;

	public PostIt(String id, Display text) {
		this.id = id;
		this.text = text;
	}

	public String getId() {
		return id;
	}

	public Display getText() {
		return text;
	}

	public Dimension2D getMinimumDimension() {
		return minimumDimension;
	}

	public void setMinimumDimension(Dimension2D minimumDimension) {
		this.minimumDimension = minimumDimension;
	}

	public Dimension2D getDimension(StringBounder stringBounder) {
		double width = getComponent().getPreferredWidth(stringBounder);
		double height = getComponent().getPreferredHeight(stringBounder);

		if (minimumDimension != null && width < minimumDimension.getWidth()) {
			width = minimumDimension.getWidth();
		}
		if (minimumDimension != null && height < minimumDimension.getHeight()) {
			height = minimumDimension.getHeight();
		}

		return new Dimension2DDouble(width, height);
	}

	public void drawU(UGraphic ug) {

		final Component note = getComponent();
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimensionToUse = getDimension(stringBounder);

		note.drawU(ug, new Area(dimensionToUse), new SimpleContext2D(false));

	}

	private Component getComponent() {
		final HColor noteBackgroundColor = HColorSet.instance().getColorIfValid("#FBFB77");
		final HColor borderColor = HColorUtils.MY_RED;

		final SkinParam param = SkinParam.noShadowing(null);
		final UFont fontNote = param.getFont(null, false, FontParam.NOTE);
		final FontConfiguration font2 = fontNote.toFont2(HColorUtils.BLACK, true, HColorUtils.BLUE, 8);
		final ComponentRoseNote note = new ComponentRoseNote(
				null, new SymbolContext(noteBackgroundColor, borderColor).withStroke(new UStroke()), font2, text, 0,
				0, new SpriteContainerEmpty(), 0, HorizontalAlignment.LEFT);
		return note;
	}
}
