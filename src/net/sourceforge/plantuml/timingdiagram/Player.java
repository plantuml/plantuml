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
 */
package net.sourceforge.plantuml.timingdiagram;

import net.sourceforge.plantuml.klimt.Fashion;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.utils.Position;

public abstract class Player implements TimeProjected {

	protected final ISkinParam skinParam;
	protected final TimingRuler ruler;
	private final boolean compact;
	private final Display title;
	protected int suggestedHeight;
	protected final Stereotype stereotype;
	private final HColor generalBackgroundColor;

	public Player(String title, ISkinParam skinParam, TimingRuler ruler, boolean compact, Stereotype stereotype,
			HColor generalBackgroundColor) {
		this.generalBackgroundColor = generalBackgroundColor;
		this.stereotype = stereotype;
		this.skinParam = skinParam;
		this.compact = compact;
		this.ruler = ruler;
		this.title = Display.getWithNewlines(skinParam.getPragma(), title);
	}

	public boolean isCompact() {
		return compact;
	}

	public HColor getGeneralBackgroundColor() {
		return generalBackgroundColor;
	}

	protected abstract StyleSignature getStyleSignature();

	final protected Style getStyle() {
		return getStyleSignature().getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	final protected FontConfiguration getFontConfiguration() {
		return FontConfiguration.create(skinParam, StyleSignatureBasic
				.of(SName.root, SName.element, SName.timingDiagram).getMergedStyle(skinParam.getCurrentStyleBuilder()));
	}

	final protected UStroke getStroke() {
		final Style style = getStyleSignature().getMergedStyle(skinParam.getCurrentStyleBuilder());
		return style.getStroke();
	}

	final protected Fashion getContext() {

		final Style style = getStyleSignature().getMergedStyle(skinParam.getCurrentStyleBuilder());
		final HColor lineColor = style.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
		final HColor backgroundColor = style.value(PName.BackGroundColor).asColor(skinParam.getIHtmlColorSet());

		return new Fashion(backgroundColor, lineColor).withStroke(getStroke());
	}

	final protected TextBlock getTitle() {
		return title.create(getFontConfiguration(), HorizontalAlignment.LEFT, skinParam);
	}

	public abstract void addNote(TimeTick now, Display note, Position position, Stereotype stereotype);

	public abstract void defineState(String stateCode, String label);

	public abstract void setState(TimeTick now, String comment, Colors color, String... states);

	public abstract void createConstraint(TimeTick tick1, TimeTick tick2, String message, ArrowConfiguration config);

	public abstract TextBlock getPart1(double fullAvailableWidth, double specialVSpace);

	public abstract UDrawable getPart2();

	public abstract double getFullHeight(StringBounder stringBounder);

	public final void setHeight(int height) {
		this.suggestedHeight = height;
	}

}
