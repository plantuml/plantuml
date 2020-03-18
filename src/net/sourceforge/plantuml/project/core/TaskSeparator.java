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

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.project.Load;
import net.sourceforge.plantuml.project.lang.ComplementColors;
import net.sourceforge.plantuml.project.time.Wink;

public class TaskSeparator extends AbstractTask implements Task {

	private final String comment;

	public TaskSeparator(String comment, int id) {
		super(new TaskCode("##" + id));
		this.comment = comment;
	}

	public TaskCode getCode() {
		return code;
	}

	public Wink getStart() {
		throw new UnsupportedOperationException();
	}

	public Wink getEnd() {
		throw new UnsupportedOperationException();
	}

	public void setStart(Wink start) {
		throw new UnsupportedOperationException();
	}

	public void setEnd(Wink end) {
		throw new UnsupportedOperationException();
	}

	public void setColors(ComplementColors colors) {
		throw new UnsupportedOperationException();
	}

	public String getName() {
		return comment;
	}

	public void addResource(Resource resource, int percentage) {
		throw new UnsupportedOperationException();
	}

	public Load getLoad() {
		throw new UnsupportedOperationException();
	}

	public void setLoad(Load load) {
		throw new UnsupportedOperationException();
	}

	public void setDiamond(boolean diamond) {
		throw new UnsupportedOperationException();
	}

	public boolean isDiamond() {
		throw new UnsupportedOperationException();
	}

	public void setCompletion(int completion) {
		throw new UnsupportedOperationException();
	}

	public void setUrl(Url url) {
		throw new UnsupportedOperationException();
	}

}
