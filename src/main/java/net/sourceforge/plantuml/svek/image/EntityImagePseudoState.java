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
package net.sourceforge.plantuml.svek.image;


import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleQueries;
import net.sourceforge.plantuml.style.StyleQuery;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;

public class EntityImagePseudoState extends AbstractEntityImage {

	private static final int SIZE = 22;
	private final TextBlock desc;
	private final Style style;
	private final Colors colors;

	public EntityImagePseudoState(Entity entity) {
		this(entity, "H");
	}

	@Override
	public StyleQuery getStyleQuery() {
		return StyleQueries.DIAMOND.add(getStyleName());
	}

	/**
	 * Stereotype labels every history pseudo-state answers to, whether it was
	 * written <code>[H]</code>, <code>state h &lt;&lt;history&gt;&gt;</code> or
	 * the deep variant: this is what makes <code>.history { ... }</code> in a
	 * <code>&lt;style&gt;</code> block reach all of them. A label such as
	 * <code>history*</code> cannot be used as a style class name, hence the
	 * separate {@link #getImplicitStereotypes()} of the deep history.
	 */
	protected String[] getImplicitStereotypes() {
		return new String[] { "history" };
	}

	public EntityImagePseudoState(Entity entity, String historyText) {
		super(entity);
		final Stereotype stereotype = entity.getStereotype();

		StyleQuery query = getStyleQuery().withStereotype(stereotype);
		for (String implicit : getImplicitStereotypes())
			query = query.withStereotype(implicit);

		this.style = getSkinParam().getCurrentStyleBuilder().getMergedStyle(query);
		this.colors = entity.getColors();

		final FontConfiguration fontConfiguration = style.getFontConfiguration(getSkinParam().getIHtmlColorSet(),
				colors);

		this.desc = Display.create(historyText).create(fontConfiguration, HorizontalAlignment.CENTER, getSkinParam());
	}

	@Override
	public XDimension2D calculateDimensionSlow(StringBounder stringBounder) {
		return new XDimension2D(SIZE, SIZE);
	}

	final public void drawU(UGraphic ug) {
		final UEllipse circle = UEllipse.build(SIZE, SIZE);

		final HColor borderColor = colors.getColor(style, PName.LineColor, getSkinParam().getIHtmlColorSet());
		final HColor backgroundColor = colors.getColor(style, PName.BackGroundColor,
				getSkinParam().getIHtmlColorSet());
		final double shadow = style.getShadowing();
		final UStroke stroke = style.getStroke(colors);

		circle.setDeltaShadow(shadow);
		ug = ug.apply(stroke);
		ug = ug.apply(backgroundColor.bg()).apply(borderColor);
		ug.draw(circle);
		// ug = ug.apply(UStroke.simple());

		final XDimension2D dimDesc = desc.calculateDimension(ug.getStringBounder());
		final double widthDesc = dimDesc.getWidth();
		final double heightDesc = dimDesc.getHeight();

		final double x = (SIZE - widthDesc) / 2;
		final double y = (SIZE - heightDesc) / 2;
		desc.drawU(ug.apply(new UTranslate(x, y)));
	}

	public ShapeType getShapeType() {
		return ShapeType.CIRCLE;
	}

}
