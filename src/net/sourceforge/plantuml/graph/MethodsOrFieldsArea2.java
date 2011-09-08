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
 * Revision $Revision: 5608 $
 *
 */
package net.sourceforge.plantuml.graph;

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.Member;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.PlacementStrategyVisibility;
import net.sourceforge.plantuml.ugraphic.PlacementStrategyY1Y2Left;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGroup;

public class MethodsOrFieldsArea2 implements TextBlock {

	private final UFont font;
	private final List<Member> members = new ArrayList<Member>();
	private final ISkinParam skinParam;
	private final HtmlColor color;
	private final Rose rose = new Rose();

	public MethodsOrFieldsArea2(List<Member> attributes, FontParam fontParam, ISkinParam skinParam) {
		this.members.addAll(attributes);
		this.skinParam = skinParam;
		this.font = skinParam.getFont(fontParam, null);
		this.color = rose.getFontColor(skinParam, FontParam.CLASS_ATTRIBUTE);

	}

	private boolean hasSmallIcon() {
		if (skinParam.classAttributeIconSize() == 0) {
			return false;
		}
		for (Member m : members) {
			if (m.getVisibilityModifier() != null) {
				return true;
			}
		}
		return false;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		double x = 0;
		double y = 0;
		for (Member m : members) {
			final String s = getDisplay(m);
			final TextBlock bloc = createTextBlock(s);
			final Dimension2D dim = bloc.calculateDimension(stringBounder);
			y += dim.getHeight();
			x = Math.max(dim.getWidth(), x);
		}
		if (hasSmallIcon()) {
			x += skinParam.getCircledCharacterRadius() + 3;
		}
		return new Dimension2DDouble(x, y);
	}

	private TextBlock createTextBlock(String s) {
		return TextBlockUtils.create(Arrays.asList(s), new FontConfiguration(font, color), HorizontalAlignement.LEFT);
	}

	public void drawTOBEREMOVED(ColorMapper colorMapper, Graphics2D g2d, double x, double y) {
		throw new UnsupportedOperationException();
	}

	public void drawU(UGraphic ug, double x, double y) {
		final Dimension2D dim = calculateDimension(ug.getStringBounder());
		final UGroup group;
		if (hasSmallIcon()) {
			group = new UGroup(new PlacementStrategyVisibility(ug.getStringBounder(), skinParam
					.getCircledCharacterRadius() + 3));
			for (Member att : members) {
				final String s = getDisplay(att);
				final TextBlock bloc = createTextBlock(s);
				final VisibilityModifier modifier = att.getVisibilityModifier();
				group.add(getUBlock(modifier));
				group.add(bloc);
			}
		} else {
			group = new UGroup(new PlacementStrategyY1Y2Left(ug.getStringBounder()));
			for (Member att : members) {
				final String s = getDisplay(att);
				final TextBlock bloc = createTextBlock(s);
				group.add(bloc);
			}
		}
		group.drawU(ug, x, y, dim.getWidth(), dim.getHeight());

	}

	private String getDisplay(Member att) {
		final boolean withVisibilityChar = skinParam.classAttributeIconSize() == 0;
		return att.getDisplay(withVisibilityChar);
	}

	private TextBlock getUBlock(final VisibilityModifier modifier) {
		if (modifier == null) {
			return new TextBlock() {

				public void drawU(UGraphic ug, double x, double y) {
				}

				public void drawTOBEREMOVED(ColorMapper colorMapper, Graphics2D g2d, double x, double y) {
					throw new UnsupportedOperationException();
				}

				public Dimension2D calculateDimension(StringBounder stringBounder) {
					return new Dimension2DDouble(1, 1);
				}
			};
		}
		final HtmlColor back = modifier.getBackground() == null ? null : rose.getHtmlColor(skinParam, modifier
				.getBackground());
		final HtmlColor fore = rose.getHtmlColor(skinParam, modifier.getForeground());

		final TextBlock uBlock = modifier.getUBlock(skinParam.classAttributeIconSize(), fore, back);
		return uBlock;
	}

}
