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
package com.plantuml.ubrex.builder;

import com.plantuml.ubrex.Challenge;
import com.plantuml.ubrex.ChallengeOneOrMoreUpToOldVersion;
import com.plantuml.ubrex.CompositeList;

public class UBrexUpto extends UBrexPart {

	public UBrexUpto(UBrexPart what, UBrexPart stop) {
		super(buildChallenge(what, stop));
	}

	private static Challenge buildChallenge(UBrexPart what, UBrexPart stop) {
		final Challenge p1 = what.getChallenge();
		final Challenge p2 = stop.getChallenge();
		final CompositeList result = CompositeList.createEmpty();
		result.addChallenge(new ChallengeOneOrMoreUpToOldVersion(p1, p2));
		result.addChallenge(p2);
		return result;
	}

}
