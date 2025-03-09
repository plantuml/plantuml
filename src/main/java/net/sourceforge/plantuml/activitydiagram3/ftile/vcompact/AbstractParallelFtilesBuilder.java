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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileHeightFixedCentered;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileHeightFixedMarged;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public abstract class AbstractParallelFtilesBuilder {

	protected final double barHeight = 6;

	private final Rose rose = new Rose();

	private final ISkinParam skinParam;
	private final StringBounder stringBounder;
	protected final List<Ftile> list99 = new ArrayList<>();

	public StyleSignatureBasic getStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.activity);
	}

	final public StyleSignatureBasic getStyleSignatureArrow() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.arrow);
	}

	public AbstractParallelFtilesBuilder(ISkinParam skinParam, StringBounder stringBounder, List<Ftile> all) {
		this.skinParam = skinParam;
		this.stringBounder = stringBounder;
		this.list99.addAll(decorateAllTiles(all));
	}

	private List<Ftile> decorateAllTiles(List<Ftile> all) {
		final double maxHeight = computeMaxHeight(all);
		final double ymargin1 = getSuppSpace1(all, getStringBounder());
		final double ymargin2 = getSuppSpace2(all, getStringBounder());
		final List<Ftile> result = new ArrayList<>();
		for (Ftile ftile : all) {
			final Ftile newFtile = computeNewFtile(ftile, maxHeight, ymargin1, ymargin2);
			result.add(newFtile);
		}
		return result;
	}

	private double getSuppSpace1(List<Ftile> all, StringBounder stringBounder) {
		double result = 0;
		for (Ftile child : all) {
			final TextBlock text = getTextBlock(child.getInLinkRendering().getDisplay());
			if (text == null)
				continue;

			final XDimension2D dim = text.calculateDimension(stringBounder);
			result = Math.max(result, dim.getHeight());

		}
		return result;
	}

	private double getSuppSpace2(List<Ftile> all, StringBounder stringBounder) {
		double result = 0;
		for (Ftile child : all) {
			final TextBlock text = getTextBlock(child.getOutLinkRendering().getDisplay());
			if (text == null)
				continue;

			final XDimension2D dim = text.calculateDimension(stringBounder);
			result = Math.max(result, dim.getHeight());
		}
		return result;
	}


	private Ftile computeNewFtile(Ftile ftile, double maxHeight, double ymargin1, double ymargin2) {
		final double spaceArroundBlackBar = 20;
		final double xMargin = 14;
		Ftile tmp;
		tmp = FtileUtils.addHorizontalMargin(ftile, xMargin, xMargin + getSuppForIncomingArrow(ftile));
		tmp = new FtileHeightFixedCentered(tmp, maxHeight + 2 * spaceArroundBlackBar);
		tmp = new FtileHeightFixedMarged(ymargin1, tmp, ymargin2);
		return tmp;
	}

	private double getSuppForIncomingArrow(Ftile ftile) {
		final double x1 = getXSuppForDisplay(ftile, ftile.getInLinkRendering().getDisplay());
		final double x2 = getXSuppForDisplay(ftile, ftile.getOutLinkRendering().getDisplay());
		return Math.max(x1, x2);
	}

	private double getXSuppForDisplay(Ftile ftile, Display label) {
		final TextBlock text = getTextBlock(label);
		if (text == null)
			return 0;

		final double textWidth = text.calculateDimension(getStringBounder()).getWidth();
		final FtileGeometry ftileDim = ftile.calculateDimension(getStringBounder());
		final double pos2 = ftileDim.getLeft() + textWidth;
		if (pos2 > ftileDim.getWidth())
			return pos2 - ftileDim.getWidth();

		return 0;
	}

	final protected double computeMaxHeight(List<Ftile> all) {
		double height = 0;
		for (Ftile tmp : all)
			height = Math.max(height, tmp.calculateDimension(getStringBounder()).getHeight());

		return height;
	}

	public final Ftile build(Ftile inner) {
		final Ftile step1 = doStep1(inner);
		return doStep2(inner, step1);
	}

	protected abstract Ftile doStep1(Ftile inner);

	protected abstract Ftile doStep2(Ftile inner, Ftile step1);

	protected StringBounder getStringBounder() {
		return stringBounder;
	}

	protected Rose getRose() {
		return rose;
	}

	protected ISkinParam skinParam() {
		return skinParam;
	}

	protected final TextBlock getTextBlock(Display display) {
		// DUP3945
		if (Display.isNull(display))
			return null;

		final Style style = getStyleSignatureArrow().getMergedStyle(skinParam().getCurrentStyleBuilder());
		final FontConfiguration fontConfiguration = style.getFontConfiguration(skinParam().getIHtmlColorSet());

		return display.create7(fontConfiguration, HorizontalAlignment.LEFT, skinParam(), CreoleMode.SIMPLE_LINE);
	}

	protected TextBlock getTextBlock(LinkRendering linkRendering) {
		// DUP1433
		final Display display = linkRendering.getDisplay();
		return getTextBlock(display);
	}

	protected final double getHeightOfMiddle(Ftile middle) {
		return middle.calculateDimension(getStringBounder()).getHeight();
	}

	protected Swimlane swimlaneOutForStep2() {
		return list99.get(list99.size() - 1).getSwimlaneOut();
	}

}
