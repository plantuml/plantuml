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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.nwdiag.core.NServer;
import net.sourceforge.plantuml.nwdiag.core.Network;

public class NServerLegacy extends NServer {

	private final NetworkLegacy mainNetwork;
	private final ISkinSimple spriteContainer;
	private NServerLegacy sameCol;

	public NServerLegacy(String name, Network network, ISkinSimple spriteContainer) {
		super(name);
		this.mainNetwork = (NetworkLegacy) network;
		this.spriteContainer = spriteContainer;
	}

	public LinkedElement asTextBlock(Map<NetworkLegacy, String> conns, List networks) {
		final Map<NetworkLegacy, TextBlock> conns2 = new LinkedHashMap<NetworkLegacy, TextBlock>();
		for (Entry<NetworkLegacy, String> ent : conns.entrySet()) {
			conns2.put(ent.getKey(), toTextBlock(ent.getValue(), spriteContainer));
		}
		final SymbolContext symbolContext = new SymbolContext(ColorParam.activityBackground.getDefaultValue(),
				ColorParam.activityBorder.getDefaultValue()).withShadow(3);
		final TextBlock desc = toTextBlock(getDescription(), spriteContainer);
		final TextBlock box = getShape().asSmall(TextBlockUtils.empty(0, 0), desc, TextBlockUtils.empty(0, 0),
				symbolContext, HorizontalAlignment.CENTER);
		return new LinkedElement(this, box, conns2, networks);
	}

	public final NetworkLegacy getMainNetwork() {
		return mainNetwork;
	}

	public void sameColThan(NServer sameCol) {
		this.sameCol = (NServerLegacy) sameCol;
	}

	public final NServerLegacy getSameCol() {
		return sameCol;
	}

	private int numCol = -1;

	public void setNumCol(int j) {
		this.numCol = j;
	}

	public final int getNumCol() {
		return numCol;
	}

}
