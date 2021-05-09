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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UseStyle;
import net.sourceforge.plantuml.activitydiagram3.Branch;
import net.sourceforge.plantuml.activitydiagram3.ForkStyle;
import net.sourceforge.plantuml.activitydiagram3.Instruction;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.PositionedNote;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.Rainbow;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class FtileFactoryDelegator implements FtileFactory {

	private final FtileFactory factory;

	private final Rose rose = new Rose();

	final public StyleSignature getDefaultStyleDefinitionActivity() {
		return StyleSignature.of(SName.root, SName.element, SName.activityDiagram, SName.activity);
	}

	final public StyleSignature getDefaultStyleDefinitionDiamond() {
		return StyleSignature.of(SName.root, SName.element, SName.activityDiagram, SName.activity, SName.diamond);
	}

	final public StyleSignature getDefaultStyleDefinitionArrow() {
		return StyleSignature.of(SName.root, SName.element, SName.activityDiagram, SName.arrow);
	}

	protected final Rainbow getInLinkRenderingColor(Ftile tile) {
		Rainbow color;
		final LinkRendering linkRendering = tile.getInLinkRendering();
		if (linkRendering == null) {
			if (UseStyle.useBetaStyle()) {
				final Style style = getDefaultStyleDefinitionArrow()
						.getMergedStyle(skinParam().getCurrentStyleBuilder());
				return Rainbow.build(style, skinParam().getIHtmlColorSet(), skinParam().getThemeStyle());
			} else {
				color = Rainbow.build(skinParam());
			}
		} else {
			color = linkRendering.getRainbow();
		}
		if (color.size() == 0) {
			if (UseStyle.useBetaStyle()) {
				final Style style = getDefaultStyleDefinitionArrow()
						.getMergedStyle(skinParam().getCurrentStyleBuilder());
				return Rainbow.build(style, skinParam().getIHtmlColorSet(), skinParam().getThemeStyle());
			} else {
				color = Rainbow.build(skinParam());
			}
		}
		return color;
	}

	protected final TextBlock getTextBlock(Display display) {
		// DUP3945
		if (Display.isNull(display)) {
			return null;
		}
		final FontConfiguration fontConfiguration;
		if (UseStyle.useBetaStyle()) {
			final Style style = getDefaultStyleDefinitionArrow().getMergedStyle(skinParam().getCurrentStyleBuilder());
			fontConfiguration = style.getFontConfiguration(skinParam().getThemeStyle(), skinParam().getIHtmlColorSet());
		} else {
			fontConfiguration = new FontConfiguration(skinParam(), FontParam.ARROW, null);
		}
		return display.create7(fontConfiguration, HorizontalAlignment.LEFT, skinParam(), CreoleMode.SIMPLE_LINE);
	}

	protected Display getInLinkRenderingDisplay(Ftile tile) {
		final LinkRendering linkRendering = tile.getInLinkRendering();
		if (linkRendering == null) {
			return Display.NULL;
		}
		return linkRendering.getDisplay();
	}

	public FtileFactoryDelegator(FtileFactory factory) {
		this.factory = factory;
	}

	public Ftile start(Swimlane swimlane) {
		return factory.start(swimlane);
	}

	public Ftile end(Swimlane swimlane) {
		return factory.end(swimlane);
	}

	public Ftile stop(Swimlane swimlane) {
		return factory.stop(swimlane);
	}

	public Ftile spot(Swimlane swimlane, String spot, HColor color) {
		return factory.spot(swimlane, spot, color);
	}

	public Ftile activity(Display label, Swimlane swimlane, BoxStyle style, Colors colors, Stereotype stereotype) {
		return factory.activity(label, swimlane, style, colors, stereotype);
	}

	public Ftile addNote(Ftile ftile, Swimlane swimlane, Collection<PositionedNote> notes) {
		return factory.addNote(ftile, swimlane, notes);
	}

	public Ftile addUrl(Ftile ftile, Url url) {
		return factory.addUrl(ftile, url);
	}

	public Ftile decorateIn(Ftile ftile, LinkRendering linkRendering) {
		return factory.decorateIn(ftile, Objects.requireNonNull(linkRendering));
	}

	public Ftile decorateOut(Ftile ftile, LinkRendering linkRendering) {
		return factory.decorateOut(ftile, Objects.requireNonNull(linkRendering));
	}

	public Ftile assembly(Ftile tile1, Ftile tile2) {
		return factory.assembly(tile1, tile2);
	}

	public Ftile repeat(BoxStyle boxStyleIn, Swimlane swimlane, Swimlane swimlaneOut, Display startLabel, Ftile repeat,
			Display test, Display yes, Display out, Colors colors, Ftile backward, boolean noOut,
			LinkRendering incoming1, LinkRendering incoming2) {
		return factory.repeat(boxStyleIn, swimlane, swimlaneOut, startLabel, repeat, test, yes, out, colors, backward,
				noOut, incoming1, incoming2);
	}

	public Ftile createWhile(LinkRendering outColor, Swimlane swimlane, Ftile whileBlock, Display test, Display yes,
			HColor color, Instruction specialOut, Ftile back, LinkRendering incoming1, LinkRendering incoming2) {
		return factory.createWhile(outColor, swimlane, whileBlock, test, yes, color, specialOut, back, incoming1,
				incoming2);
	}

	public Ftile createIf(Swimlane swimlane, List<Branch> thens, Branch elseBranch, LinkRendering afterEndwhile,
			LinkRendering topInlinkRendering, Url url) {
		return factory.createIf(swimlane, thens, elseBranch, afterEndwhile, topInlinkRendering, url);
	}

	public Ftile createSwitch(Swimlane swimlane, List<Branch> branches, LinkRendering afterEndwhile,
			LinkRendering topInlinkRendering, Display labelTest) {
		return factory.createSwitch(swimlane, branches, afterEndwhile, topInlinkRendering, labelTest);
	}

	public Ftile createParallel(List<Ftile> all, ForkStyle style, String label, Swimlane in, Swimlane out) {
		return factory.createParallel(all, style, label, in, out);
	}

	public Ftile createGroup(Ftile list, Display name, HColor backColor, HColor titleColor, PositionedNote note,
			HColor borderColor, USymbol type, double roundCorner) {
		return factory.createGroup(list, name, backColor, titleColor, note, borderColor, type, roundCorner);
	}

	public StringBounder getStringBounder() {
		return factory.getStringBounder();
	}

	protected final Rose getRose() {
		return rose;
	}

	public final ISkinParam skinParam() {
		return factory.skinParam();
	}

	protected FtileFactory getFactory() {
		return factory;
	}

}
