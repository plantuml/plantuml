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
package net.sourceforge.plantuml.activitydiagram3.ftile.vertical;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.decoration.Rainbow;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.creole.command.Command;
import net.sourceforge.plantuml.klimt.creole.command.CommandCreoleEmoji;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class FtileBoxEmoji extends AbstractFtile {

	private static final int MARGIN = 5;
	private final TextBlock emoji;
	private final TextBlock name;

	private final LinkRendering inRendering;
	private final Swimlane swimlane;

	static public StyleSignatureBasic getStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.activity);
	}

	static public StyleSignatureBasic getStyleSignatureArrow() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.arrow);
	}

	@Override
	final public LinkRendering getInLinkRendering() {
		return inRendering;
	}

	public Set<Swimlane> getSwimlanes() {
		if (swimlane == null)
			return Collections.emptySet();

		return Collections.singleton(swimlane);
	}

	public Swimlane getSwimlaneIn() {
		return swimlane;
	}

	public Swimlane getSwimlaneOut() {
		return swimlane;
	}

	public static FtileBoxEmoji create(ISkinParam skinParam, Display label, Swimlane swimlane, Stereotype stereotype,
			StyleBuilder styleBuilder) {
		if (styleBuilder == null)
			styleBuilder = skinParam.getCurrentStyleBuilder();

		final Style style = getStyleSignature().withTOBECHANGED(stereotype).getMergedStyle(styleBuilder);
		final Style styleArrow = getStyleSignatureArrow().getMergedStyle(styleBuilder);
		return new FtileBoxEmoji(skinParam, label, swimlane, style, styleArrow);
	}

	private FtileBoxEmoji(ISkinParam skinParam, Display label, Swimlane swimlane, Style style, Style styleArrow) {
		super(skinParam);
		this.swimlane = swimlane;

		this.inRendering = LinkRendering.create(Rainbow.build(styleArrow, getIHtmlColorSet()));

		final FontConfiguration fc = style.getFontConfiguration(getIHtmlColorSet());

		final Command commandCreoleEmoji = CommandCreoleEmoji.create();
		final String s = label.get(0).toString();
		if (commandCreoleEmoji.matchingSize(s) == 0) {
			this.emoji = label.create(fc, HorizontalAlignment.LEFT, skinParam);
			this.name = TextBlockUtils.EMPTY_TEXT_BLOCK;
		} else {
			final String remaining = commandCreoleEmoji.executeAndGetRemaining(s, null);

			this.emoji = Display.create(s.substring(0, s.length() - remaining.length())).create(fc,
					HorizontalAlignment.LEFT, skinParam);
			this.name = Display.create(s.substring(remaining.length())).create(fc, HorizontalAlignment.LEFT, skinParam);
		}

	}

	public void drawU(UGraphic ug) {
		emoji.drawU(ug);

		if (name != TextBlockUtils.EMPTY_TEXT_BLOCK) {
			final double delta = emoji.calculateDimension(ug.getStringBounder()).getWidth() + MARGIN;
			name.drawU(ug.apply(UTranslate.dx(delta)));
		}
	}

	@Override
	protected FtileGeometry calculateDimensionFtile(StringBounder stringBounder) {
		XDimension2D dimEmoji = emoji.calculateDimension(stringBounder);
		XDimension2D dimName = name.calculateDimension(stringBounder);

		final double width = dimEmoji.getWidth() + MARGIN + dimName.getWidth();
		final double height = Math.max(dimEmoji.getHeight(), dimName.getHeight());

		return new FtileGeometry(width, height, dimEmoji.getWidth() / 2, 0, dimEmoji.getHeight());
	}

	public Collection<Ftile> getMyChildren() {
		return Collections.emptyList();
	}

}
