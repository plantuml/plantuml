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
 */
package net.sourceforge.plantuml.timingdiagram;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.graphic.color.Colors;

public abstract class Player implements TimeProjected {

	protected final ISkinParam skinParam;
	protected final TimingRuler ruler;
	private final boolean compact;
	private final Display title;
	protected int suggestedHeight;

	public Player(String title, ISkinParam skinParam, TimingRuler ruler, boolean compact) {
		this.skinParam = skinParam;
		this.compact = compact;
		this.ruler = ruler;
		this.title = Display.getWithNewlines(title);
	}

	public boolean isCompact() {
		return compact;
	}

	final protected FontConfiguration getFontConfiguration() {
		return new FontConfiguration(skinParam, FontParam.TIMING, null);
	}

	final protected TextBlock getTitle() {
		return title.create(getFontConfiguration(), HorizontalAlignment.LEFT, skinParam);
	}

	public abstract void addNote(TimeTick now, Display note, Position position);

	public abstract void defineState(String stateCode, String label);

	public abstract void setState(TimeTick now, String comment, Colors color, String... states);

	public abstract void createConstraint(TimeTick tick1, TimeTick tick2, String message);

	public abstract TextBlock getPart1(double fullAvailableWidth, double specialVSpace);

	public abstract UDrawable getPart2();

	public abstract double getFullHeight(StringBounder stringBounder);

	public final void setHeight(int height) {
		this.suggestedHeight = height;
	}

}
