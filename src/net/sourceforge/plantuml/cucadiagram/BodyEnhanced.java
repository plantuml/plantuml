/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.creole.CreoleParser;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockLineBefore;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.TextBlockVertical2;
import net.sourceforge.plantuml.svek.Ports;
import net.sourceforge.plantuml.svek.WithPorts;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class BodyEnhanced extends AbstractTextBlock implements TextBlock, WithPorts {

	private TextBlock area2;
	private final FontConfiguration titleConfig;
	private final List<String> rawBody;
	private final FontParam fontParam;
	private final ISkinParam skinParam;
	private final boolean lineFirst;
	private final HorizontalAlignment align;
	private final boolean manageHorizontalLine;
	private final boolean manageModifier;
	private final List<Url> urls = new ArrayList<Url>();
	private final boolean manageUrl;
	private final Stereotype stereotype;
	private final ILeaf entity;

	public BodyEnhanced(List<String> rawBody, FontParam fontParam, ISkinParam skinParam, boolean manageModifier,
			Stereotype stereotype, ILeaf entity) {
		this.rawBody = new ArrayList<String>(rawBody);
		this.stereotype = stereotype;
		this.fontParam = fontParam;
		this.skinParam = skinParam;
		this.manageUrl = true;

		this.titleConfig = new FontConfiguration(skinParam, fontParam, stereotype);
		this.lineFirst = true;
		this.align = HorizontalAlignment.LEFT;
		this.manageHorizontalLine = true;
		this.manageModifier = manageModifier;
		this.entity = entity;
	}

	public BodyEnhanced(Display display, FontParam fontParam, ISkinParam skinParam, HorizontalAlignment align,
			Stereotype stereotype, boolean manageHorizontalLine, boolean manageModifier, boolean manageUrl, ILeaf entity) {
		this.entity = entity;
		this.manageUrl = manageUrl;
		this.stereotype = stereotype;
		this.rawBody = new ArrayList<String>();
		for (CharSequence s : display) {
			this.rawBody.add(s.toString());
		}
		this.fontParam = fontParam;
		this.skinParam = skinParam;

		this.titleConfig = new FontConfiguration(skinParam, fontParam, stereotype);
		this.lineFirst = false;
		this.align = align;
		this.manageHorizontalLine = manageHorizontalLine;
		this.manageModifier = manageModifier;

	}

	private TextBlock decorate(StringBounder stringBounder, TextBlock b, char separator, TextBlock title) {
		if (separator == 0) {
			return b;
		}
		if (title == null) {
			return new TextBlockLineBefore(TextBlockUtils.withMargin(b, 6, 4), separator);
		}
		final Dimension2D dimTitle = title.calculateDimension(stringBounder);
		final TextBlock raw = new TextBlockLineBefore(TextBlockUtils.withMargin(b, 6, 6, dimTitle.getHeight() / 2, 4),
				separator, title);
		return TextBlockUtils.withMargin(raw, 0, 0, dimTitle.getHeight() / 2, 0);
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return getArea(stringBounder).calculateDimension(stringBounder);
	}

	private TextBlock getArea(StringBounder stringBounder) {
		if (area2 != null) {
			return area2;
		}
		urls.clear();
		final List<TextBlock> blocks = new ArrayList<TextBlock>();

		char separator = lineFirst ? '_' : 0;
		TextBlock title = null;
		List<Member> members = new ArrayList<Member>();
		for (ListIterator<String> it = rawBody.listIterator(); it.hasNext();) {
			final String s = it.next();
			if (manageHorizontalLine && isBlockSeparator(s)) {
				blocks.add(decorate(stringBounder, new MethodsOrFieldsArea(members, fontParam, skinParam, align,
						stereotype, entity), separator, title));
				separator = s.charAt(0);
				title = getTitle(s, skinParam);
				members = new ArrayList<Member>();
			} else if (CreoleParser.isTreeStart(s)) {
				if (members.size() > 0) {
					blocks.add(decorate(stringBounder, new MethodsOrFieldsArea(members, fontParam, skinParam, align,
							stereotype, entity), separator, title));
				}
				members = new ArrayList<Member>();
				final List<String> allTree = buildAllTree(s, it);
				final TextBlock bloc = Display.create(allTree).create(fontParam.getFontConfiguration(skinParam), align,
						skinParam, CreoleMode.FULL);
				blocks.add(bloc);
			} else {
				final Member m = new MemberImpl(s, MemberImpl.isMethod(s), manageModifier, manageUrl);
				members.add(m);
				if (m.getUrl() != null) {
					urls.add(m.getUrl());
				}
			}
		}
		blocks.add(decorate(stringBounder, new MethodsOrFieldsArea(members, fontParam, skinParam, align, stereotype, entity),
				separator, title));

		if (blocks.size() == 1) {
			this.area2 = blocks.get(0);
		} else {
			this.area2 = new TextBlockVertical2(blocks, align);
		}

		return area2;
	}

	private static List<String> buildAllTree(String init, ListIterator<String> it) {
		final List<String> result = new ArrayList<String>();
		result.add(init);
		while (it.hasNext()) {
			final String s = it.next();
			if (CreoleParser.isTreeStart(StringUtils.trinNoTrace(s))) {
				result.add(s);
			} else {
				it.previous();
				return result;
			}

		}
		return result;
	}

	public static boolean isBlockSeparator(String s) {
		if (s.startsWith("--") && s.endsWith("--")) {
			return true;
		}
		if (s.startsWith("==") && s.endsWith("==")) {
			return true;
		}
		if (s.startsWith("..") && s.endsWith("..") && s.equals("...") == false) {
			return true;
		}
		if (s.startsWith("__") && s.endsWith("__")) {
			return true;
		}
		return false;
	}

	private TextBlock getTitle(String s, ISkinSimple spriteContainer) {
		if (s.length() <= 4) {
			return null;
		}
		s = StringUtils.trin(s.substring(2, s.length() - 2));
		return Display.getWithNewlines(s).create(titleConfig, HorizontalAlignment.LEFT, spriteContainer);
	}

	public Ports getPorts(StringBounder stringBounder) {
		final TextBlock area = getArea(stringBounder);
		if (area instanceof WithPorts) {
			return ((WithPorts) area).getPorts(stringBounder);
		}
		return new Ports();
	}

	public void drawU(UGraphic ug) {
		getArea(ug.getStringBounder()).drawU(ug);
	}

	public List<Url> getUrls() {
		return Collections.unmodifiableList(urls);
	}

	public Rectangle2D getInnerPosition(String member, StringBounder stringBounder) {
		return getArea(stringBounder).getInnerPosition(member, stringBounder);
	}

}
