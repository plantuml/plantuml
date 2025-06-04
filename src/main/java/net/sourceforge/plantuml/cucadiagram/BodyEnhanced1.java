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
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.atmp.InnerStrategy;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.creole.Parser;
import net.sourceforge.plantuml.klimt.creole.legacy.CreoleParser;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XRectangle2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.TextBlockVertical2;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.svek.Ports;
import net.sourceforge.plantuml.svek.WithPorts;
import net.sourceforge.plantuml.url.Url;

public class BodyEnhanced1 extends BodyEnhancedAbstract implements TextBlock, WithPorts {

	private final Display rawBody2;

	private final ISkinParam skinParam;
	private final boolean lineFirst;
	private final List<Url> urls = new ArrayList<>();

	private final Entity entity;
	private final boolean inEllipse;
	private final Style style;

	BodyEnhanced1(HorizontalAlignment align, List<CharSequence> rawBody, ISkinParam skinParam, Entity entity,
			Style style) {
		super(align, style.getFontConfiguration(skinParam.getIHtmlColorSet(), entity.getColors()), style);
		this.style = style;
		this.rawBody2 = Display.create(rawBody);

		this.skinParam = skinParam;

		this.lineFirst = true;

		this.entity = entity;
		this.inEllipse = false;
	}

	BodyEnhanced1(HorizontalAlignment align, Display display, ISkinParam skinParam, Entity entity, Style style) {
		super(align, style.getFontConfiguration(skinParam.getIHtmlColorSet(), entity.getColors()), style);

		this.style = style;
		this.entity = entity;

		this.skinParam = skinParam;

		this.lineFirst = false;

		final LeafType leafType = entity.getLeafType();
		this.inEllipse = leafType == LeafType.USECASE || leafType == LeafType.USECASE_BUSINESS;

		if (inEllipse && display.size() > 0 && isBlockSeparator(display.get(0).toString()))
			display = display.add("");

		this.rawBody2 = display;

	}

	@Override
	protected double getMarginX() {
		return 6;
	}

	private static boolean isTreeOrTable(String s) {
		s = StringUtils.trinNoTrace(s);
		return Parser.isTreeStart(s) || CreoleParser.isTableLine(s);
	}

	@Override
	final protected TextBlock getArea(StringBounder stringBounder) {
		if (area != null)
			return area;

		urls.clear();
		final List<TextBlock> blocks = new ArrayList<>();

		char separator = lineFirst ? '_' : 0;
		TextBlock title = null;
		Display display = null;
		for (ListIterator<CharSequence> it = rawBody2.iterator(); it.hasNext();) {
			final CharSequence cs = it.next();
			final String s = cs.toString();
			if (isBlockSeparator(s)) {
				if (display == null)
					display = Display.empty();
				blocks.add(buildTextBlock(display, separator, title, stringBounder));
				separator = s.charAt(0);
				title = getTitle(s, skinParam);
				display = null;
			} else if (isTreeOrTable(s)) {
				final boolean isTable = CreoleParser.isTableLine(s);
				if (display == null)
					display = Display.empty();
				blocks.add(buildTextBlock(display, separator, title, stringBounder));

				separator = 0;
				title = null;
				display = null;
				final List<CharSequence> allTree = buildTreeOrTable(s, it);
				final FontConfiguration fontConfiguration = style.getFontConfiguration(skinParam.getIHtmlColorSet());
				TextBlock bloc = Display.create(allTree).create7(fontConfiguration, align, skinParam, CreoleMode.FULL);
				if (isTable)
					bloc = TextBlockUtils.withMargin(bloc, 10, 10, 0, 5);

				blocks.add(bloc);
			} else {
				if (display == null)
					display = Display.empty();
				display = display.add(cs);
				if (cs instanceof Member && ((Member) cs).getUrl() != null)
					urls.add(((Member) cs).getUrl());

			}
//			}
		}

		if (display == null)
			display = Display.empty();
		if (inEllipse && display.size() == 0)
			display = display.add("");

		blocks.add(buildTextBlock(display, separator, title, stringBounder));

		if (blocks.size() == 1)
			this.area = blocks.get(0);
		else
			this.area = new TextBlockVertical2(blocks, align);

		final double minClassWidth = style.value(PName.MinimumWidth).asDouble();
		if (minClassWidth > 0)
			this.area = TextBlockUtils.withMinWidth(this.area, minClassWidth, align);

		return area;
	}

	private TextBlock buildTextBlock(Display display, char separator, TextBlock title, StringBounder stringBounder) {
		// TextBlock result = display.create9(titleConfig, align, skinParam,
		// LineBreakStrategy.NONE);
		TextBlock result = new MethodsOrFieldsArea(display, skinParam, align, entity, style);
		result = decorate(result, separator, title, stringBounder);
		return result;
	}

	private static final Pattern p = Pattern.compile("^(\\s+)");
	
	private static List<CharSequence> buildTreeOrTable(String init, ListIterator<CharSequence> it) {
		final List<CharSequence> result = new ArrayList<>();
		final Matcher m = p.matcher(init);
		String start = "";
		if (m.find())
			start = m.group(1);

		result.add(purge(init, start));
		while (it.hasNext()) {
			String s = it.next().toString();
			if (isTreeOrTable(s)) {
				result.add(purge(s, start));
			} else {
				it.previous();
				return result;
			}

		}
		return result;
	}

	private static String purge(String s, String start) {
		if (s.startsWith(start))
			return s.substring(start.length());

		return s;
	}

	@Override
	public Ports getPorts(StringBounder stringBounder) {
		final TextBlock area = getArea(stringBounder);
		if (area instanceof WithPorts)
			return ((WithPorts) area).getPorts(stringBounder);

		return new Ports();
	}

	public List<Url> getUrls() {
		return Collections.unmodifiableList(urls);
	}

	public XRectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
		return getArea(stringBounder).getInnerPosition(member, stringBounder, strategy);
	}

}
