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
package net.sourceforge.plantuml.cucadiagram;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.EmbeddedDiagram;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockLineBefore;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.TextBlockWithUrl;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.svek.Ports;
import net.sourceforge.plantuml.svek.WithPorts;
import net.sourceforge.plantuml.ugraphic.PlacementStrategy;
import net.sourceforge.plantuml.ugraphic.PlacementStrategyVisibility;
import net.sourceforge.plantuml.ugraphic.PlacementStrategyY1Y2Center;
import net.sourceforge.plantuml.ugraphic.PlacementStrategyY1Y2Left;
import net.sourceforge.plantuml.ugraphic.PlacementStrategyY1Y2Right;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULayoutGroup;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.utils.CharHidder;

public class MethodsOrFieldsArea extends AbstractTextBlock implements TextBlock, WithPorts {

	public TextBlock asBlockMemberImpl() {
		return new TextBlockLineBefore(TextBlockUtils.withMargin(this, 6, 4));
	}

	private final FontParam fontParam;
	private final ISkinParam skinParam;
	private final Rose rose = new Rose();
	private final Display members;
	private final HorizontalAlignment align;
	private final Stereotype stereotype;
	private final ILeaf leaf;
	private final Style style;

	public MethodsOrFieldsArea(Display members, FontParam fontParam, ISkinParam skinParam, Stereotype stereotype,
			ILeaf leaf, Style style) {
		this(members, fontParam, skinParam, HorizontalAlignment.LEFT, stereotype, leaf, style);
	}

	public MethodsOrFieldsArea(Display members, FontParam fontParam, ISkinParam skinParam, HorizontalAlignment align,
			Stereotype stereotype, ILeaf leaf, Style style) {
		this.style = style;
		this.leaf = leaf;
		this.stereotype = stereotype;
		this.align = align;
		this.skinParam = skinParam;
		this.fontParam = fontParam;
		this.members = members;
	}

	private boolean hasSmallIcon() {
		if (skinParam.classAttributeIconSize() == 0) {
			return false;
		}
		for (CharSequence cs : members) {
			if (cs instanceof Member == false)
				continue;
			final Member m = (Member) cs;
			if (m.getVisibilityModifier() != null) {
				return true;
			}
		}
		return false;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		double smallIcon = 0;
		if (hasSmallIcon()) {
			smallIcon = skinParam.getCircledCharacterRadius() + 3;
		}
		double x = 0;
		double y = 0;
		for (CharSequence cs : members) {
			final TextBlock bloc = createTextBlock(cs);
			final Dimension2D dim = bloc.calculateDimension(stringBounder);
			x = Math.max(dim.getWidth(), x);
			y += dim.getHeight();
		}
		x += smallIcon;
		return new Dimension2DDouble(x, y);
	}

	public Ports getPorts(StringBounder stringBounder) {
		final Ports result = new Ports();
		double y = 0;
		final Election election = new Election();
		for (CharSequence cs : members) {
			if (cs instanceof Member) {
				final Member m = (Member) cs;
				election.addCandidate(m.getDisplay(false), m);
			} else {
				election.addCandidate(cs.toString(), cs);
			}
		}
		final Map<CharSequence, String> memberWithPort = election.getAllElected(leaf.getPortShortNames());
		for (CharSequence cs : members) {
			final TextBlock bloc = createTextBlock(cs);
			final Dimension2D dim = bloc.calculateDimension(stringBounder);
			final String port = memberWithPort.get(cs);
			if (port != null) {
				result.add(port, y, dim.getHeight());
			}
			y += dim.getHeight();
		}
		return result;
	}

	private TextBlock createTextBlock(CharSequence cs) {

		FontConfiguration config;
		if (style != null) {
			config = new FontConfiguration(skinParam, style);
		} else {
			config = new FontConfiguration(skinParam, fontParam, stereotype);
		}

		if (cs instanceof Member) {
			final Member m = (Member) cs;
			final boolean withVisibilityChar = skinParam.classAttributeIconSize() == 0;
			String s = m.getDisplay(withVisibilityChar);
			if (withVisibilityChar && s.startsWith("#")) {
				s = CharHidder.addTileAtBegin(s);
			}
			if (m.isAbstract()) {
				config = config.italic();
			}
			if (m.isStatic()) {
				config = config.underline();
			}

			TextBlock bloc = Display.getWithNewlines(s).create8(config, align, skinParam, CreoleMode.SIMPLE_LINE,
					skinParam.wrapWidth());
			bloc = TextBlockUtils.fullInnerPosition(bloc, m.getDisplay(false));
			return new TextBlockTracer(m, bloc);
		}

		if (cs instanceof EmbeddedDiagram) {
			return ((EmbeddedDiagram) cs).asDraw(skinParam);
		}

		return Display.getWithNewlines(cs.toString()).create8(config, align, skinParam, CreoleMode.SIMPLE_LINE,
				skinParam.wrapWidth());

	}

	static class TextBlockTracer extends AbstractTextBlock implements TextBlock {

		private final TextBlock bloc;
		private final Url url;

		public TextBlockTracer(Member m, TextBlock bloc) {
			this.bloc = bloc;
			this.url = m.getUrl();
		}

		public void drawU(UGraphic ug) {
			if (url != null) {
				ug.startUrl(url);
			}
			bloc.drawU(ug);
			if (url != null) {
				ug.closeUrl();
			}
		}

		public Dimension2D calculateDimension(StringBounder stringBounder) {
			final Dimension2D dim = bloc.calculateDimension(stringBounder);
			return dim;
		}

		@Override
		public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
			return bloc.getInnerPosition(member, stringBounder, strategy);
		}

	}

	private TextBlock getUBlock(final VisibilityModifier modifier, Url url) {
		if (modifier == null) {
			return new AbstractTextBlock() {

				public void drawU(UGraphic ug) {
				}

				@Override
				public Rectangle2D getInnerPosition(String member, StringBounder stringBounder,
						InnerStrategy strategy) {
					return null;
				}

				public Dimension2D calculateDimension(StringBounder stringBounder) {
					return new Dimension2DDouble(1, 1);
				}
			};
		}
		final HColor back = modifier.getBackground() == null ? null
				: rose.getHtmlColor(skinParam, modifier.getBackground());
		final HColor fore = rose.getHtmlColor(skinParam, modifier.getForeground());

		final TextBlock uBlock = modifier.getUBlock(skinParam.classAttributeIconSize(), fore, back, url != null);
		return TextBlockWithUrl.withUrl(uBlock, url);
	}

	public boolean contains(String member) {
		for (CharSequence cs : members) {
			final Member att = (Member) cs;
			if (att.getDisplay(false).startsWith(member)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
		final ULayoutGroup group = getLayout(stringBounder);
		final Dimension2D dim = calculateDimension(stringBounder);
		return group.getInnerPosition(member, dim.getWidth(), dim.getHeight(), stringBounder);
	}

	private ULayoutGroup getLayout(final StringBounder stringBounder) {
		final ULayoutGroup group;
		if (hasSmallIcon()) {
			group = new ULayoutGroup(
					new PlacementStrategyVisibility(stringBounder, skinParam.getCircledCharacterRadius() + 3));
			for (CharSequence cs : members) {
				final TextBlock bloc = createTextBlock(cs);
				if (cs instanceof EmbeddedDiagram) {
					group.add(getUBlock(null, null));
				} else {
					final Member att = (Member) cs;
					final VisibilityModifier modifier = att.getVisibilityModifier();
					group.add(getUBlock(modifier, att.getUrl()));
				}
				group.add(bloc);
			}
		} else {
			final PlacementStrategy placementStrategy;
			if (align == HorizontalAlignment.LEFT) {
				placementStrategy = new PlacementStrategyY1Y2Left(stringBounder);
			} else if (align == HorizontalAlignment.CENTER) {
				placementStrategy = new PlacementStrategyY1Y2Center(stringBounder);
			} else {
				placementStrategy = new PlacementStrategyY1Y2Right(stringBounder);
			}
			group = new ULayoutGroup(placementStrategy);
			for (CharSequence cs : members) {
				final TextBlock bloc = createTextBlock(cs);
				group.add(bloc);
			}
		}
		return group;
	}

	public void drawU(UGraphic ug) {
		final ULayoutGroup group = getLayout(ug.getStringBounder());
		final Dimension2D dim = calculateDimension(ug.getStringBounder());
		group.drawU(ug, dim.getWidth(), dim.getHeight());
	}

}
