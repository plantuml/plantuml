/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
package net.sourceforge.plantuml.cli;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import net.sourceforge.plantuml.utils.Peeker;
import net.sourceforge.plantuml.utils.PeekerUtils;

public class CliParser {

	public static CliOptions parse(String... arg) throws InterruptedException, IOException {
		return new CliOptions(arg);
	}

	public static Map<CliFlag, Object> parse2(String... args) {
		final Map<CliFlag, Object> result = new EnumMap<>(CliFlag.class);
		for (final Peeker<String> peeker = PeekerUtils.peeker(Arrays.asList(args)); peeker.peek(0) != null; peeker
				.jump()) {
			final CliFlagValued flag = CliFlagValued.parse(peeker);
			if (flag != null)
				result.put(flag.getFlag(), flag.getValue());
		}

		return result;

	}

}
