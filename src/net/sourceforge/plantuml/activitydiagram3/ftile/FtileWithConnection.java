/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 8475 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileDecorate;
import net.sourceforge.plantuml.ugraphic.UGraphic;

class FtileWithConnection extends FtileDecorate {

	private final List<Connection> connections = new ArrayList<Connection>();

	FtileWithConnection(Ftile ftile, Collection<Connection> connections) {
		super(ftile);
		if (connections == null || connections.size() == 0) {
			throw new IllegalArgumentException();
		}
		this.connections.addAll(connections);
	}

	@Override
	public String toString() {
		return super.toString() + " " + connections;
	}

	public FtileWithConnection(Ftile ftile, Connection connection) {
		this(ftile, Arrays.asList(connection));
		if (connection == null) {
			throw new IllegalArgumentException();
		}
	}

	public void drawU(UGraphic ug) {
		getFtileDelegated().drawU(ug);
		for (Connection c : connections) {
			ug.draw(c);
		}
	}

	public Collection<Connection> getInnerConnections() {
		final List<Connection> result = new ArrayList<Connection>(super.getInnerConnections());
		result.addAll(connections);
		return Collections.unmodifiableList(connections);
	}

}
