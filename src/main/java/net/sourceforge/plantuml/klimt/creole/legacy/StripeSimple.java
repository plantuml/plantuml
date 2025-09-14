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
package net.sourceforge.plantuml.klimt.creole.legacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.emoji.Emoji;
import net.sourceforge.plantuml.jaws.Jaws;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.CreoleContext;
import net.sourceforge.plantuml.klimt.creole.CreoleHorizontalLine;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Stripe;
import net.sourceforge.plantuml.klimt.creole.StripeStyle;
import net.sourceforge.plantuml.klimt.creole.StripeStyleType;
import net.sourceforge.plantuml.klimt.creole.atom.Atom;
import net.sourceforge.plantuml.klimt.creole.atom.AtomEmoji;
import net.sourceforge.plantuml.klimt.creole.atom.AtomImg;
import net.sourceforge.plantuml.klimt.creole.atom.AtomMath;
import net.sourceforge.plantuml.klimt.creole.atom.AtomOpenIcon;
import net.sourceforge.plantuml.klimt.creole.atom.AtomSpace;
import net.sourceforge.plantuml.klimt.creole.atom.AtomSprite;
import net.sourceforge.plantuml.klimt.creole.command.Command;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.FontStyle;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.ImgValign;
import net.sourceforge.plantuml.klimt.sprite.Sprite;
import net.sourceforge.plantuml.math.ScientificEquationSafe;
import net.sourceforge.plantuml.openiconic.OpenIcon;
import net.sourceforge.plantuml.style.ISkinSimple;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.utils.CharHidder;

public class StripeSimple implements Stripe {

	final private Atom header;

	final private List<Atom> atoms = new ArrayList<>();

	final private Map<Character, List<Command>> commands;

	private HorizontalAlignment align = HorizontalAlignment.LEFT;

	public void setCellAlignment(HorizontalAlignment align) {
		this.align = align;
	}

	public HorizontalAlignment getCellAlignment() {
		return align;
	}

	private FontConfiguration fontConfiguration;

	final private StripeStyle style;
	final private ISkinSimple skinParam;

	@Override
	public String toString() {
		return super.toString() + " " + atoms.toString();
	}

	public Atom getLHeader() {
		return header;
	}

	public StripeSimple(FontConfiguration fontConfiguration, StripeStyle style, CreoleContext context,
			ISkinSimple skinParam, CreoleMode modeSimpleLine) {
		this.fontConfiguration = fontConfiguration;
		this.style = style;
		this.skinParam = skinParam;

		if (modeSimpleLine == CreoleMode.FULL)
			this.commands = CommandCreoleBuilder.FULL.getMap();
		else
			this.commands = CommandCreoleBuilder.OTHER.getMap();

		this.header = style.getHeader(fontConfiguration, context);

		if (this.header != null)
			this.atoms.add(this.header);

	}

	public List<Atom> getAtoms() {
		if (atoms.size() == 0)
			atoms.add(AtomTextUtils.createLegacy(" ", fontConfiguration));

		return Collections.unmodifiableList(atoms);
	}

	public FontConfiguration getActualFontConfiguration() {
		return fontConfiguration;
	}

	public void setActualFontConfiguration(FontConfiguration fontConfiguration) {
		this.fontConfiguration = fontConfiguration;
	}

	public void analyzeAndAdd(String line) {
//		if (Objects.requireNonNull(line).contains("" + BackSlash.hiddenNewLine()))
//			throw new IllegalArgumentException(line);

		if (Jaws.TRACE)
			System.err.println("analyzeAndAdd " + line);
		if (line.indexOf(Jaws.BLOCK_E1_NEWLINE) != -1)
			throw new IllegalArgumentException(line);

		line = CharHidder.hide(line);
		if (style.getType() == StripeStyleType.HEADING) {
			fontConfiguration = fontConfigurationForHeading(fontConfiguration, style.getOrder());
			modifyStripe(line);
		} else if (style.getType() == StripeStyleType.HORIZONTAL_LINE) {
			atoms.add(CreoleHorizontalLine.create(fontConfiguration, line, style.getStyle(), skinParam));
		} else {
			modifyStripe(line);
		}
	}

	private static FontConfiguration fontConfigurationForHeading(FontConfiguration fontConfiguration, int order) {
		switch (order) {
		case 0:
			return fontConfiguration.bigger(4).bold();
		case 1:
			return fontConfiguration.bigger(2).bold();
		case 2:
			return fontConfiguration.bigger(1).bold();
		default:
			return fontConfiguration.italic();
		}
	}

	public void addImage(String src, double scale) {
		atoms.add(AtomImg.create(src, ImgValign.TOP, 0, scale, null));
	}

	public void addQrcode(String src, double scale) {
		atoms.add(AtomImg.createQrcode(src, scale));
	}

	public void addSpace(int size) {
		atoms.add(AtomSpace.create(size));
	}

	public void addUrl(Url url) {
		atoms.add(AtomTextUtils.createUrl(url, fontConfiguration, skinParam));
	}

	public void addSprite(String src, double scale, HColor forcedColor) {
		final Sprite sprite = skinParam.getSprite(src);
		if (sprite != null) {
			final HColor backColor = fontConfiguration.containsStyle(FontStyle.BACKCOLOR)
					? fontConfiguration.getExtendedColor()
					: null;
			atoms.add(new AtomSprite(fontConfiguration.getColor(), forcedColor, scale, sprite, null, backColor));
		}

	}

	public void addOpenIcon(String src, double scale, HColor color) {
		final OpenIcon openIcon = OpenIcon.retrieve(src);
		if (openIcon != null)
			atoms.add(new AtomOpenIcon(color, scale, openIcon, fontConfiguration, null));
	}

	public void addEmoji(String emojiName, double scale, String forcedColor) {
		final Emoji emoji = Emoji.retrieve(emojiName);
		if (emoji == null) {
			atoms.add(AtomTextUtils.create("\u00BF" + emojiName + "?", fontConfiguration.changeColor(HColors.RED)));
			return;
		}

		HColor col = null;
		if (forcedColor == null)
			col = null;
		else if (forcedColor.equals("#0") || forcedColor.equals("#000") || forcedColor.equals("#black"))
			col = fontConfiguration.getColor();
		else
			try {
				col = skinParam.getIHtmlColorSet().getColor(forcedColor);
			} catch (NoSuchColorException e) {
				col = null;
			}

		atoms.add(new AtomEmoji(emoji, scale, fontConfiguration.getSize2D(), col));
	}

	// ::comment when __CORE__
	public void addMath(ScientificEquationSafe math) {
		atoms.add(new AtomMath(math, fontConfiguration.getColor(), fontConfiguration.getExtendedColor()));
	}
	// ::done

	private void modifyStripe(String line) {
		final StringBuilder pending = new StringBuilder();

		while (line.length() > 0) {
			final Command cmd = searchCommand(line);
			if (cmd == null) {
				pending.append(line.charAt(0));
				line = line.substring(1);
			} else {
				addPending(pending);
				line = cmd.executeAndGetRemaining(line, this);
			}
		}
		addPending(pending);
	}

	private void addPending(StringBuilder pending) {
		if (pending.length() == 0)
			return;

		atoms.add(AtomTextUtils.createLegacy(pending.toString(), fontConfiguration));
		pending.setLength(0);
	}

	private Command searchCommand(String line) {
		final List<Command> localList = commands.get(line.charAt(0));
		if (localList != null)
			for (Command cmd : localList)
				if (cmd.matchingSize(line) != 0)
					return cmd;

		return null;
	}

	public ISkinSimple getSkinParam() {
		return skinParam;
	}

}
