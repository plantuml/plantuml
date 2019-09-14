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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;

public abstract class AbstractParallelFtilesBuilder {

	protected final double barHeight = 6;

	private final Rose rose = new Rose();

	private final ISkinParam skinParam;
	private final StringBounder stringBounder;

	private final List<Ftile> list;
	private final Ftile middle;
	private final FtileGeometry middleDimension;

	public StyleSignature getDefaultStyleDefinition() {
		return StyleSignature.of(SName.root, SName.element, SName.activityDiagram, SName.activity);
	}

	final public StyleSignature getDefaultStyleDefinitionArrow() {
		return StyleSignature.of(SName.root, SName.element, SName.activityDiagram, SName.arrow);
	}

	public AbstractParallelFtilesBuilder(ISkinParam skinParam, StringBounder stringBounder, final List<Ftile> list,
			Ftile middle) {
		this.skinParam = skinParam;
		this.stringBounder = stringBounder;
		this.list = list;
		this.middle = middle;
		this.middleDimension = middle.calculateDimension(getStringBounder());
	}

	public final Ftile build() {
		final Ftile step1 = doStep1();
		return doStep2(step1);
	}

	protected abstract Ftile doStep1();

	protected abstract Ftile doStep2(Ftile step1);

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
		if (Display.isNull(display)) {
			return null;
		}
		final FontConfiguration fontConfiguration;
		if (SkinParam.USE_STYLES()) {
			final Style style = getDefaultStyleDefinitionArrow().getMergedStyle(skinParam().getCurrentStyleBuilder());
			fontConfiguration = style.getFontConfiguration(skinParam().getIHtmlColorSet());
		} else {
			fontConfiguration = new FontConfiguration(skinParam(), FontParam.ARROW, null);
		}
		return display.create(fontConfiguration, HorizontalAlignment.LEFT, skinParam(), CreoleMode.SIMPLE_LINE);
	}

	protected TextBlock getTextBlock(LinkRendering linkRendering) {
		// DUP1433
		final Display display = linkRendering.getDisplay();
		return getTextBlock(display);
	}

	protected final List<Ftile> getList() {
		return Collections.unmodifiableList(list);
	}

	protected final Ftile getMiddle() {
		return middle;
	}

	protected final double getHeightOfMiddle() {
		return middleDimension.getHeight();
	}

	protected Swimlane swimlaneOutForStep2() {
		return list.get(list.size() - 1).getSwimlaneOut();
	}

}
