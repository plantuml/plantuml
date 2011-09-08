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
 * Revision $Revision: 5183 $
 *
 */
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Member;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.Shadowable;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class EntityImageState extends AbstractEntityImage {

	public static final int CORNER = 25;
	final private TextBlock desc;
	final private TextBlock fields;

	final public static int MARGIN = 5;
	final public static int MARGIN_LINE = 5;
	final private static int MIN_WIDTH = 50;
	final private static int MIN_HEIGHT = 50;

	public EntityImageState(IEntity entity, ISkinParam skinParam) {
		super(entity, skinParam);
		final Stereotype stereotype = entity.getStereotype();

		this.desc = TextBlockUtils.create(entity.getDisplay2(), new FontConfiguration(getFont(FontParam.STATE,
				stereotype), getFontColor(FontParam.STATE, stereotype)), HorizontalAlignement.CENTER);

		final List<String> list = new ArrayList<String>();
		for (Member att : entity.getFieldsToDisplay()) {
			list.add(att.getDisplayWithVisibilityChar());
		}

		this.fields = TextBlockUtils.create(list, new FontConfiguration(getFont(FontParam.STATE_ATTRIBUTE, stereotype),
				getFontColor(FontParam.STATE_ATTRIBUTE, stereotype)), HorizontalAlignement.LEFT);

	}

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
		final Dimension2D dim = Dimension2DDouble.mergeTB(desc.calculateDimension(stringBounder), fields
				.calculateDimension(stringBounder));
		final Dimension2D result = Dimension2DDouble.delta(dim, MARGIN * 2 + 2 * MARGIN_LINE);
		return Dimension2DDouble.atLeast(result, MIN_WIDTH, MIN_HEIGHT);
	}

	public void drawU(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimTotal = getDimension(stringBounder);
		final Dimension2D dimDesc = desc.calculateDimension(stringBounder);

		final double widthTotal = dimTotal.getWidth();
		final double heightTotal = dimTotal.getHeight();
		final Shadowable rect = new URectangle(widthTotal, heightTotal, CORNER, CORNER);
		if (getSkinParam().shadowing()) {
			rect.setDeltaShadow(4);
		}

		ug.getParam().setStroke(new UStroke(1.5));
		ug.getParam().setColor(getColor(ColorParam.stateBorder, getStereo()));
		HtmlColor backcolor = getEntity().getSpecificBackColor();
		if (backcolor == null) {
			backcolor = getColor(ColorParam.stateBackground, getStereo());
		}
		ug.getParam().setBackcolor(backcolor);

		ug.draw(xTheoricalPosition, yTheoricalPosition, rect);

		final double yLine = yTheoricalPosition + MARGIN + dimDesc.getHeight() + MARGIN_LINE;
		ug.draw(xTheoricalPosition, yLine, new ULine(widthTotal, 0));

		ug.getParam().setStroke(new UStroke());

		final double xDesc = (widthTotal - dimDesc.getWidth()) / 2;
		final double yDesc = yTheoricalPosition + MARGIN;
		desc.drawU(ug, xTheoricalPosition + xDesc, yDesc);

		final double xFields = xTheoricalPosition + MARGIN;
		final double yFields = yLine + MARGIN_LINE;
		fields.drawU(ug, xFields, yFields);

	}

	public ShapeType getShapeType() {
		return ShapeType.ROUND_RECTANGLE;
	}

	public int getShield() {
		return 0;
	}

}
