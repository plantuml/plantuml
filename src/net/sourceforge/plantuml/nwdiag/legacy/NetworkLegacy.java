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
package net.sourceforge.plantuml.nwdiag.legacy;

import java.util.LinkedHashMap;
import java.util.Map;

import net.sourceforge.plantuml.nwdiag.core.NServer;
import net.sourceforge.plantuml.nwdiag.core.Network;
import net.sourceforge.plantuml.nwdiag.next.NStage;

public class NetworkLegacy extends Network {

	private final Map<NServerLegacy, String> localServers = new LinkedHashMap<NServerLegacy, String>();
	private final int stage;

	@Override
	public String toString() {
		return super.toString() + "(" + stage + ")";
	}

	public NetworkLegacy(NStage nstage, String name, int stage) {
		super(null, nstage, name);
		this.stage = stage;
	}

	public String getAdress(NServer server) {
		return localServers.get(server);
	}

	public void addServer(NServerLegacy server, Map<String, String> props) {
		String address = props.get("address");
		if (address == null) {
			address = "";
		}
		if (address.length() == 0 && localServers.containsKey(server)) {
			return;
		}
		localServers.put(server, address);
	}

	public boolean constainsLocally(String name) {
		for (NServerLegacy server : localServers.keySet()) {
			if (server.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public final int getStage() {
		return stage;
	}


}
