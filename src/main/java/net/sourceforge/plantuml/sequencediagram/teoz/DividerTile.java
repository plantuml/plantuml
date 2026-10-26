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
package net.sourceforge.plantuml.sequencediagram.teoz;

import net.sourceforge.plantuml.asciiverse.ADimension2D;
import net.sourceforge.plantuml.asciiverse.InfinitePlan;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.sequencediagram.Divider;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ISkinParam;

public class DividerTile extends AbstractTile implements Tile {

	private final Rose skin;
	private final ISkinParam skinParam;
	private final Divider divider;
	private final Real xorigin;
	private final TileArguments tileArguments;
	private final YGauge yGauge;

	public Event getEvent() {
		return divider;
	}

	public DividerTile(Divider divider, TileArguments tileArguments, YGauge currentY) {
		super(tileArguments.getStringBounder(), currentY);
		this.tileArguments = tileArguments;
		this.divider = divider;
		this.skin = tileArguments.getSkin();
		this.skinParam = tileArguments.getSkinParam();
		this.xorigin = tileArguments.getXOrigin();
		this.yGauge = YGauge.create(currentY.getMax(), getPreferredHeight());
	}

	@Override
	public YGauge getYGauge() {
		return yGauge;
	}

	private Component getComponent(StringBounder stringBounder) {
		final Component comp = skin.createComponent(divider.getUsedStyles(), ComponentType.DIVIDER, null, skinParam,
				divider.getText());
		return comp;
	}

	public void drawU(UGraphic ug) {
		// Self-translate prologue: absolute gauge position
		ug = ug.apply(UTranslate.dy(getYGauge().getMin().getCurrentValue()));
		final StringBounder stringBounder = ug.getStringBounder();
		final Component comp = getComponent(stringBounder);
		final XDimension2D dim = comp.getPreferredDimension(stringBounder);
		final Area area = Area.create(
				tileArguments.getBorder2() - tileArguments.getBorder1() - xorigin.getCurrentValue(), dim.getHeight());

		ug = ug.apply(UTranslate.dx(tileArguments.getBorder1()));
		comp.drawU(ug, area, (Context2D) ug);
	}

	public double getPreferredHeight() {
		final Component comp = getComponent(getStringBounder());
		final XDimension2D dim = comp.getPreferredDimension(getStringBounder());
		return dim.getHeight();
	}

	public void addConstraints() {
		// final Component comp = getComponent(stringBounder);
		// final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		// final double width = dim.getWidth();
	}

	public Real getMinX() {
		return xorigin;
	}

	public Real getMaxX() {
		final Component comp = getComponent(getStringBounder());
		final XDimension2D dim = comp.getPreferredDimension(getStringBounder());
		return xorigin.addFixed(dim.getWidth());
	}

	// ===================== ASCII =====================
	//
	// ASCII counterpart of drawU(), so that a divider ("== text ==", or
	// "== ==" without text) no longer reaches the AsciiBlock "not migrated"
	// default and crashes the whole txt/utxt export. Like the pixel
	// ComponentRoseDivider: a double line across the whole diagram, with the
	// text centered on it. The lifelines cross the line, except where the
	// text hides them, as the pixel text box does.

	// Same 2-cell widening past the outermost participant boxes as
	// NewpageTile.ASCII_MARGIN, the other full-width separator
	private static final int ASCII_MARGIN = 2;

	// The double line left visible on each side of the text, at least
	private static final int ASCII_MIN_LINE = 2;

	// The text as a single line padded with one space on each side, multi-line
	// text flattened the same way as GroupingTile.asciiTitle(); empty for a
	// divider without text. Trimmed: CommandDivider keeps the spaces before
	// the closing "==", which are invisible in pixels but not here.
	private String asciiLabel() {
		final StringBuilder sb = new StringBuilder();
		for (CharSequence cs : divider.getText()) {
			final String line = cs.toString().trim();
			if (line.isEmpty())
				continue;
			if (sb.length() > 0)
				sb.append(' ');
			sb.append(line);
		}
		if (sb.length() == 0)
			return "";

		return " " + sb + " ";
	}

	// Spans every participant, like NewpageTile.asciiColumns(). A text too
	// wide for that span extends it to the right, as the pixel divider widens
	// the diagram from its left edge (see getMaxX()) instead of pushing any
	// participant away.
	private int[] asciiColumns() {
		int lo = Integer.MAX_VALUE;
		int hi = Integer.MIN_VALUE;
		for (LivingSpace ls : tileArguments.getLivingSpaces().values()) {
			lo = Math.min(lo, (int) ls.getAsciiPosB().getCurrentValue());
			hi = Math.max(hi, (int) ls.getAsciiPosD().getCurrentValue() - 1);
		}
		if (lo == Integer.MAX_VALUE)
			return new int[] { 0, 0 };

		final int left = lo - ASCII_MARGIN;
		final int right = Math.max(hi + ASCII_MARGIN, left + asciiLabel().length() + 2 * ASCII_MIN_LINE - 1);
		return new int[] { left, right };
	}

	// The line, plus one blank row below it: the same breathing room a message
	// leaves after its arrow (see CommunicationTile.asciiDimension())
	@Override
	public ADimension2D asciiDimension() {
		final int[] cols = asciiColumns();
		return new ADimension2D(cols[1] - cols[0] + 1, 2);
	}

	@Override
	public void asciiDraw(InfinitePlan plan) {
		final int[] cols = asciiColumns();
		plan.drawDoubleHLine(cols[0], cols[1], 0);

		final String label = asciiLabel();
		if (label.isEmpty() == false) {
			final int width = cols[1] - cols[0] + 1;
			final InfinitePlan text = plan.move(cols[0] + (width - label.length()) / 2, 0);
			// Opaque, so that the lifelines drawn afterwards do not show
			// through the spaces of the text (see InfinitePlan.opacify())
			text.opacify(label.length(), 1);
			text.drawString(label);
		}

		for (LivingSpace ls : tileArguments.getLivingSpaces().values())
			plan.move((int) ls.getAsciiLifeColumn().getCurrentValue(), 0).muteLifelineAcrossDoubleHLine();
	}

}
