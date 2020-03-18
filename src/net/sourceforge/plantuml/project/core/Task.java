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
package net.sourceforge.plantuml.project.core;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.project.Load;
import net.sourceforge.plantuml.project.lang.ComplementColors;
import net.sourceforge.plantuml.project.lang.Subject;
import net.sourceforge.plantuml.project.time.Wink;

public interface Task extends Subject, Moment {

	public TaskCode getCode();

	public Wink getStart();

	public Wink getEnd();

	public Load getLoad();

	public void setLoad(Load load);

	public void setStart(Wink start);

	public void setEnd(Wink end);

	public void setColors(ComplementColors colors);

	public void addResource(Resource resource, int percentage);

	public void setDiamond(boolean diamond);

	public boolean isDiamond();

	public void setCompletion(int completion);

	public void setUrl(Url url);

	public double getHeight();

	public double getY();

	public void setY(double y);

	public double getY(Direction direction);

}
