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
package net.sourceforge.plantuml.cucadiagram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.atmp.InnerStrategy;
import net.sourceforge.plantuml.EmbeddedDiagram;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.PlacementStrategy;
import net.sourceforge.plantuml.klimt.geom.PlacementStrategyVisibility;
import net.sourceforge.plantuml.klimt.geom.PlacementStrategyY1Y2Center;
import net.sourceforge.plantuml.klimt.geom.PlacementStrategyY1Y2Left;
import net.sourceforge.plantuml.klimt.geom.PlacementStrategyY1Y2Right;
import net.sourceforge.plantuml.klimt.geom.ULayoutGroup;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XRectangle2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockLineBefore;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.TextBlockWithUrl;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.svek.Ports;
import net.sourceforge.plantuml.svek.WithPorts;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.utils.CharHidder;

public class MethodsOrFieldsArea extends AbstractTextBlock implements TextBlock, WithPorts {

	public TextBlock asBlockMemberImpl() {
		return new TextBlockLineBefore(style.value(PName.LineThickness).asDouble(),
				TextBlockUtils.withMargin(this, 6, 4));
	}

	private final ISkinParam skinParam;

	private final Display members;
	private final HorizontalAlignment align;
	private final List<EmbeddedDiagram> embeddeds = new ArrayList<>();

	private final Entity leaf;
	private final Style style;

	public MethodsOrFieldsArea(Display members, ISkinParam skinParam, Entity leaf, Style style) {
		this(members, skinParam, HorizontalAlignment.LEFT, leaf, style);
	}

	public MethodsOrFieldsArea(Display members, ISkinParam skinParam, HorizontalAlignment align, Entity leaf,
			Style style) {
		this.style = style;
		this.leaf = leaf;

		this.align = align;
		this.skinParam = skinParam;

		final List<CharSequence> result = new ArrayList<>();
		final Iterator<CharSequence> it = members.iterator();

		while (it.hasNext()) {
			final CharSequence cs = it.next();
			final String type = EmbeddedDiagram.getEmbeddedType(StringUtils.trinNoTrace(cs));
			if (type != null)
				embeddeds.add(EmbeddedDiagram.createAndSkip(type, it, skinParam));
			else
				result.add(cs);

		}

		this.members = Display.create(result);
	}

	private boolean hasSmallIcon() {
		if (skinParam.classAttributeIconSize() == 0)
			return false;

		for (CharSequence cs : members) {
			if (cs instanceof Member == false)
				continue;
			final Member m = (Member) cs;
			if (m.getVisibilityModifier() != null)
				return true;

		}
		return false;
	}

	@Override
	public XDimension2D calculateDimension(StringBounder stringBounder) {
		final XDimension2D dim1 = calculateDimensionOnlyMembers(stringBounder);
		double x = dim1.getWidth();
		double y = dim1.getHeight();
		for (EmbeddedDiagram embedded : embeddeds) {
			final XDimension2D dim = embedded.calculateDimension(stringBounder);
			x = Math.max(dim.getWidth(), x);
			y += dim.getHeight();
		}

		return new XDimension2D(x, y);
	}

	private XDimension2D calculateDimensionOnlyMembers(StringBounder stringBounder) {
		double smallIcon = 0;
		if (hasSmallIcon())
			smallIcon = skinParam.getCircledCharacterRadius() + 3;

		double x = 0;
		double y = 0;
		for (CharSequence cs : members) {
			final TextBlock bloc = createTextBlock(cs);
			final XDimension2D dim = bloc.calculateDimension(stringBounder);
			x = Math.max(dim.getWidth(), x);
			y += dim.getHeight();
		}
		x += smallIcon;

		return new XDimension2D(x, y);
	}

	private Collection<String> sortBySize(Collection<String> all) {
		final List<String> result = new ArrayList<String>(all);
		Collections.sort(result, new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				final int diff = s2.length() - s1.length();
				if (diff != 0)
					return diff;
				return s1.compareTo(s2);
			}
		});
		return result;
	}

	@Override
	public Ports getPorts(StringBounder stringBounder) {
		final Ports ports = new Ports();
		double y = 0;

		final Collection<String> shortNames = sortBySize(leaf.getPortShortNames());

		for (CharSequence cs : members) {
			final TextBlock bloc = createTextBlock(cs);
			final XDimension2D dim = bloc.calculateDimension(stringBounder);
			final Elected elected = getElected(convert(cs), shortNames);
			if (elected != null)
				ports.add(elected.getShortName(), elected.getScore(), y, dim.getHeight());

			y += dim.getHeight();
		}
		return ports;
	}

	private String convert(CharSequence cs) {
		if (cs instanceof Member)
			return ((Member) cs).getDisplay(false);
		return cs.toString();
	}

	public Elected getElected(String cs, Collection<String> shortNames) {
		for (String shortName : shortNames) {
			final int score = getScore(cs, shortName);
			if (score > 0)
				return new Elected(shortName, score);
		}
		return null;
	}

	private int getScore(String cs, String shortName) {
		if (cs.matches(".*\\b" + shortName + "\\b.*"))
			return 100;

		if (cs.contains(shortName))
			return 50;

		return 0;
	}

	private TextBlock createTextBlock(CharSequence cs) {

		FontConfiguration config = FontConfiguration.create(skinParam, style, leaf.getColors());

		if (cs instanceof Member) {
			final Member m = (Member) cs;
			final boolean withVisibilityChar = skinParam.classAttributeIconSize() == 0;
			String s = m.getDisplay(withVisibilityChar);
			if (withVisibilityChar && s.startsWith("#"))
				s = CharHidder.addTileAtBegin(s);

			if (m.isAbstract())
				config = config.italic();

			if (m.isStatic())
				config = config.underline();

			TextBlock bloc = Display.getWithNewlines(skinParam.getPragma(), s).create8(config, align, skinParam, CreoleMode.SIMPLE_LINE,
					style.wrapWidth());
			bloc = TextBlockUtils.fullInnerPosition(bloc, m.getDisplay(false));
			return new TextBlockTracer(m, bloc);
		}

//		if (cs instanceof EmbeddedDiagram)
//			return ((EmbeddedDiagram) cs).asDraw(skinParam);

		return Display.getWithNewlines(skinParam.getPragma(), cs.toString()).create8(config, align, skinParam, CreoleMode.SIMPLE_LINE,
				style.wrapWidth());

	}

	static class TextBlockTracer extends AbstractTextBlock implements TextBlock {

		private final TextBlock bloc;
		private final Url url;

		public TextBlockTracer(Member m, TextBlock bloc) {
			this.bloc = bloc;
			this.url = m.getUrl();
		}

		public void drawU(UGraphic ug) {
			if (url != null)
				ug.startUrl(url);

			bloc.drawU(ug);
			if (url != null)
				ug.closeUrl();

		}

		public XDimension2D calculateDimension(StringBounder stringBounder) {
			final XDimension2D dim = bloc.calculateDimension(stringBounder);
			return dim;
		}

		@Override
		public XRectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
			return bloc.getInnerPosition(member, stringBounder, strategy);
		}

	}

	private TextBlock getUBlock(final VisibilityModifier modifier, Url url) {
		if (modifier == null) {
			return new AbstractTextBlock() {

				public void drawU(UGraphic ug) {
				}

				@Override
				public XRectangle2D getInnerPosition(String member, StringBounder stringBounder,
						InnerStrategy strategy) {
					return null;
				}

				public XDimension2D calculateDimension(StringBounder stringBounder) {
					return new XDimension2D(1, 1);
				}
			};
		}
		final Style style = modifier.getStyleSignature().getMergedStyle(skinParam.getCurrentStyleBuilder());
		final HColor borderColor = style.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
		final boolean isField = modifier.isField();
		final HColor backColor = isField ? null
				: style.value(PName.BackGroundColor).asColor(skinParam.getIHtmlColorSet());

		final TextBlock uBlock = modifier.getUBlock(skinParam.classAttributeIconSize(), borderColor, backColor,
				url != null);
		return TextBlockWithUrl.withUrl(uBlock, url);
	}

	public boolean contains(String member) {
		for (CharSequence cs : members) {
			final Member att = (Member) cs;
			if (att.getDisplay(false).startsWith(member))
				return true;

		}
		return false;
	}

	@Override
	public XRectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
		final ULayoutGroup group = getLayout(stringBounder);
		final XDimension2D dim = calculateDimension(stringBounder);
		return group.getInnerPosition(member, dim.getWidth(), dim.getHeight(), stringBounder);
	}

	private ULayoutGroup getLayout(final StringBounder stringBounder) {
		final ULayoutGroup group;
		if (hasSmallIcon()) {
			group = new ULayoutGroup(
					new PlacementStrategyVisibility(stringBounder, skinParam.getCircledCharacterRadius() + 3));
			for (CharSequence cs : members) {
				final TextBlock bloc = createTextBlock(cs);
//				if (cs instanceof EmbeddedDiagram) {
//					group.add(getUBlock(null, null));
//				} else {
				final Member att = (Member) cs;
				final VisibilityModifier modifier = att.getVisibilityModifier();
				group.add(getUBlock(modifier, att.getUrl()));
//				}
				group.add(bloc);
			}
		} else {
			final PlacementStrategy placementStrategy;
			if (align == HorizontalAlignment.LEFT)
				placementStrategy = new PlacementStrategyY1Y2Left(stringBounder);
			else if (align == HorizontalAlignment.CENTER)
				placementStrategy = new PlacementStrategyY1Y2Center(stringBounder);
			else
				placementStrategy = new PlacementStrategyY1Y2Right(stringBounder);

			group = new ULayoutGroup(placementStrategy);
			for (CharSequence cs : members) {
				final TextBlock bloc = createTextBlock(cs);
				group.add(bloc);
			}
		}
		return group;
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final ULayoutGroup group = getLayout(stringBounder);
		final XDimension2D dim = calculateDimensionOnlyMembers(stringBounder);
		group.drawU(ug, dim.getWidth(), dim.getHeight());
		ug = ug.apply(UTranslate.dy(dim.getHeight()));

		for (EmbeddedDiagram embedded : embeddeds) {
			embedded.drawU(ug);
			ug = ug.apply(UTranslate.dy(embedded.calculateDimension(stringBounder).getHeight()));
		}
	}

}
