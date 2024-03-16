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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import net.sourceforge.plantuml.activitydiagram3.Branch;
import net.sourceforge.plantuml.activitydiagram3.ForkStyle;
import net.sourceforge.plantuml.activitydiagram3.Instruction;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.PositionedNote;
import net.sourceforge.plantuml.decoration.Rainbow;
import net.sourceforge.plantuml.decoration.symbol.USymbol;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.url.Url;

public class FtileFactoryDelegator implements FtileFactory {

	private final FtileFactory factory;

	private final Rose rose = new Rose();

	final public StyleSignatureBasic getDefaultStyleDefinitionActivity() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.activity);
	}

	final public StyleSignatureBasic getDefaultStyleDefinitionDiamond() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.activity, SName.diamond);
	}

	final public StyleSignatureBasic getDefaultStyleDefinitionArrow() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.arrow);
	}

	protected final Rainbow getInLinkRenderingColor(Ftile tile) {
		Rainbow color;
		final LinkRendering linkRendering = tile.getInLinkRendering();
		if (linkRendering == null) {
			final Style style = getDefaultStyleDefinitionArrow().getMergedStyle(skinParam().getCurrentStyleBuilder());
			return Rainbow.build(style, skinParam().getIHtmlColorSet());
		} else {
			color = linkRendering.getRainbow();
		}
		if (color.size() == 0) {
			final Style style = getDefaultStyleDefinitionArrow().getMergedStyle(skinParam().getCurrentStyleBuilder());
			return Rainbow.build(style, skinParam().getIHtmlColorSet());
		}
		return color;
	}

	protected final TextBlock getTextBlock(Display display) {
		// DUP3945
		if (Display.isNull(display))
			return null;

		final Style style = getDefaultStyleDefinitionArrow().getMergedStyle(skinParam().getCurrentStyleBuilder());
		final FontConfiguration fontConfiguration = style.getFontConfiguration(skinParam().getIHtmlColorSet());

		return display.create7(fontConfiguration, HorizontalAlignment.LEFT, skinParam(), CreoleMode.SIMPLE_LINE);
	}

	protected Display getInLinkRenderingDisplay(Ftile tile) {
		final LinkRendering linkRendering = tile.getInLinkRendering();
		if (linkRendering == null)
			return Display.NULL;

		return linkRendering.getDisplay();
	}

	public FtileFactoryDelegator(FtileFactory factory) {
		this.factory = factory;
	}

	@Override
	public Ftile start(Swimlane swimlane) {
		return factory.start(swimlane);
	}

	@Override
	public Ftile end(Swimlane swimlane) {
		return factory.end(swimlane);
	}

	@Override
	public Ftile stop(Swimlane swimlane) {
		return factory.stop(swimlane);
	}

	@Override
	public Ftile spot(Swimlane swimlane, String spot, HColor color) {
		return factory.spot(swimlane, spot, color);
	}

	@Override
	public Ftile activity(Display label, Swimlane swimlane, BoxStyle style, Colors colors, Stereotype stereotype) {
		return factory.activity(label, swimlane, style, colors, stereotype);
	}

	@Override
	public Ftile addNote(Ftile ftile, Swimlane swimlane, Collection<PositionedNote> notes,
			VerticalAlignment verticalAlignment) {
		return factory.addNote(ftile, swimlane, notes, verticalAlignment);
	}

	@Override
	public Ftile addUrl(Ftile ftile, Url url) {
		return factory.addUrl(ftile, url);
	}

	@Override
	public Ftile decorateIn(Ftile ftile, LinkRendering linkRendering) {
		return factory.decorateIn(ftile, Objects.requireNonNull(linkRendering));
	}

	@Override
	public Ftile decorateOut(Ftile ftile, LinkRendering linkRendering) {
		return factory.decorateOut(ftile, Objects.requireNonNull(linkRendering));
	}

	@Override
	public Ftile assembly(Ftile tile1, Ftile tile2) {
		return factory.assembly(tile1, tile2);
	}

	@Override
	public Ftile repeat(BoxStyle boxStyleIn, Swimlane swimlane, Swimlane swimlaneOut, Display startLabel, Ftile repeat,
			Display test, Display yes, Display out, Colors colors, Ftile backward, boolean noOut,
			LinkRendering incoming1, LinkRendering incoming2, StyleBuilder currentStyleBuilder) {
		return factory.repeat(boxStyleIn, swimlane, swimlaneOut, startLabel, repeat, test, yes, out, colors, backward,
				noOut, incoming1, incoming2, currentStyleBuilder);
	}

	@Override
	public Ftile createWhile(LinkRendering outColor, Swimlane swimlane, Ftile whileBlock, Display test, Display yes,
			HColor color, Instruction specialOut, Ftile back, LinkRendering incoming1, LinkRendering incoming2,
			StyleBuilder styleBuilder) {
		return factory.createWhile(outColor, swimlane, whileBlock, test, yes, color, specialOut, back, incoming1,
				incoming2, styleBuilder);
	}

	@Override
	public Ftile createIf(Swimlane swimlane, List<Branch> thens, Branch elseBranch, LinkRendering afterEndwhile,
			LinkRendering topInlinkRendering, Url url, Collection<PositionedNote> notes, Stereotype stereotype,
			StyleBuilder currentStyleBuilder) {
		return factory.createIf(swimlane, thens, elseBranch, afterEndwhile, topInlinkRendering, url, notes, stereotype,
				currentStyleBuilder);
	}

	@Override
	public Ftile createSwitch(Swimlane swimlane, List<Branch> branches, LinkRendering afterEndwhile,
			LinkRendering topInlinkRendering, Display labelTest) {
		return factory.createSwitch(swimlane, branches, afterEndwhile, topInlinkRendering, labelTest);
	}

	@Override
	public Ftile createParallel(List<Ftile> all, ForkStyle style, String label, Swimlane in, Swimlane out) {
		return factory.createParallel(all, style, label, in, out);
	}

	@Override
	public Ftile createGroup(Ftile list, Display name, HColor backColor, PositionedNote note, USymbol type,
			Style style) {
		return factory.createGroup(list, name, backColor, note, type, style);
	}

	@Override
	public StringBounder getStringBounder() {
		return factory.getStringBounder();
	}

	protected final Rose getRose() {
		return rose;
	}

	@Override
	public final ISkinParam skinParam() {
		return factory.skinParam();
	}

	protected FtileFactory getFactory() {
		return factory;
	}

}
