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
package net.sourceforge.plantuml.tim.builtin;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.TContext;
import net.sourceforge.plantuml.tim.TFunctionSignature;
import net.sourceforge.plantuml.tim.TMemory;
import net.sourceforge.plantuml.tim.expression.TValue;

public class DateFunction extends SimpleReturnFunction {

	private static final TFunctionSignature SIGNATURE = new TFunctionSignature("%date", 3);

	private static final Set<String> KNOWN_TIME_ZONE_IDS = new HashSet<>(Arrays.asList(TimeZone.getAvailableIDs()));

	public TFunctionSignature getSignature() {
		return SIGNATURE;
	}

	@Override
	public boolean canCover(int nbArg, Set<String> namedArgument) {
		return nbArg == 0 || nbArg == 1 || nbArg == 2 || nbArg == 3;
	}

	@Override
	public TValue executeReturnFunction(TContext context, TMemory memory, StringLocated location, List<TValue> values,
			Map<String, TValue> named) throws EaterException {
		if (values.size() == 0)
			return TValue.fromString(new Date().toString());

		final String format = values.get(0).toString();
		final long now;
		if (values.size() >= 2)
			now = 1000L * values.get(1).toInt();
		else
			now = System.currentTimeMillis();

		final TimeZone timeZone;
		if (values.size() == 3) {
			final String tzId = values.get(2).toString();
			// SimpleDateFormat#setTimeZone silently falls back to GMT for unknown ids,
			// so validate explicitly and fail loudly to help users diagnose typos.
			if (KNOWN_TIME_ZONE_IDS.contains(tzId) == false)
				throw new EaterException("Unknown time zone: " + tzId, location);
			timeZone = TimeZone.getTimeZone(tzId);
		} else {
			timeZone = TimeZone.getDefault();
		}

		try {
			final SimpleDateFormat formatter = new SimpleDateFormat(format);
			formatter.setTimeZone(timeZone);
			return TValue.fromString(formatter.format(now));
		} catch (Exception e) {
			throw new EaterException("Bad date pattern", location);
		}
	}
}
