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
package net.sourceforge.plantuml.project.draw;

import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.project.LabelStrategy;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.core.TaskAttribute;
import net.sourceforge.plantuml.project.lang.CenterBorderColor;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.utils.Direction;

public interface TaskDraw extends UDrawable {

	public TaskDraw getTrueRow();

	public void setColorsAndCompletion(CenterBorderColor colors, int completion, Url url, Display note);

	public Real getY(StringBounder stringBounder);

	public double getY(StringBounder stringBounder, Direction direction);

	public void drawTitle(UGraphic ug, LabelStrategy labelStrategy, double colTitles, double colBars);

	public double getTitleWidth(StringBounder stringBounder);

	public double getFullHeightTask(StringBounder stringBounder);

	public double getHeightMax(StringBounder stringBounder);

	public Task getTask();

	public FingerPrint getFingerPrint(StringBounder stringBounder);

	public FingerPrint getFingerPrintNote(StringBounder stringBounder);

	public double getX1(TaskAttribute taskAttribute);

	public double getX2(TaskAttribute taskAttribute);

}
