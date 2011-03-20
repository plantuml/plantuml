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
 * Revision $Revision: 4167 $
 *
 */
package net.sourceforge.plantuml.postit;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Dimension2D;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.skin.rose.ComponentRoseNote;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class PostIt {

	private final String id;
	private final List<String> text;

	private Dimension2D minimunDimension;

	public PostIt(String id, List<String> text) {
		this.id = id;
		this.text = text;
	}

	public String getId() {
		return id;
	}

	public List<String> getText() {
		return Collections.unmodifiableList(text);
	}

	public Dimension2D getMinimunDimension() {
		return minimunDimension;
	}

	public void setMinimunDimension(Dimension2D minimunDimension) {
		this.minimunDimension = minimunDimension;
	}

	public Dimension2D getDimension(StringBounder stringBounder) {
		double width = getComponent().getPreferredWidth(stringBounder);
		double height = getComponent().getPreferredHeight(stringBounder);

		if (minimunDimension != null && width < minimunDimension.getWidth()) {
			width = minimunDimension.getWidth();
		}
		if (minimunDimension != null && height < minimunDimension.getHeight()) {
			height = minimunDimension.getHeight();
		}

		return new Dimension2DDouble(width, height);
	}

	public void drawU(UGraphic ug) {

		final Component note = getComponent();
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimensionToUse = getDimension(stringBounder);

		note.drawU(ug, dimensionToUse, new SimpleContext2D(false));

	}

	private Component getComponent() {
		final Color noteBackgroundColor = HtmlColor.getColorIfValid("#FBFB77").getColor();
		final Color borderColor = HtmlColor.getColorIfValid("#A80036").getColor();

		final SkinParam param = new SkinParam();
		final Font fontNote = param.getFont(FontParam.NOTE, null);
		final ComponentRoseNote note = new ComponentRoseNote(noteBackgroundColor, borderColor, Color.BLACK, fontNote,
				text);
		return note;
	}
}
