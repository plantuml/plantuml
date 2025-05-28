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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sourceforge.plantuml.json.Json;
import net.sourceforge.plantuml.json.JsonObject;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.preproc.Stdlib;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.TContext;
import net.sourceforge.plantuml.tim.TFunctionSignature;
import net.sourceforge.plantuml.tim.TMemory;
import net.sourceforge.plantuml.tim.expression.TValue;

public class GetStdlib extends SimpleReturnFunction {

	private static final TFunctionSignature SIGNATURE = new TFunctionSignature("%get_stdlib", 1);

	public TFunctionSignature getSignature() {
		return SIGNATURE;
	}

	@Override
	public boolean canCover(int nbArg, Set<String> namedArgument) {
		return nbArg == 0 || nbArg == 1 || nbArg == 2;
	}

	@Override
	public TValue executeReturnFunction(TContext context, TMemory memory, StringLocated location, List<TValue> values,
			Map<String, TValue> named) throws EaterException {

		final JsonObject result = Json.object();
		try {
			if (values.size() == 0) {
				for (String folderName : Stdlib.getAllFolderNames()) {
					// This can be optimized: no need to load the full folder
					final Stdlib folder = Stdlib.retrieve(folderName);
					final JsonObject metadata = Json.object();
					// Key in README.md will be in lowercase anyway
					for (Entry<String, String> ent : folder.getMetadata().entrySet())
						metadata.add(ent.getKey().toLowerCase(), ent.getValue());

					result.add(folderName, metadata);
				}
			} else if (values.size() == 1) {
				final String folderName = values.get(0).toString();
				// This can be optimized: no need to load the full folder
				final Stdlib folder = Stdlib.retrieve(folderName);
				// Key in README.md will be in lowercase anyway
				for (Entry<String, String> ent : folder.getMetadata().entrySet())
					result.add(ent.getKey().toLowerCase(), ent.getValue());
			} else if (values.size() == 2) {
				final String folderName = values.get(0).toString();
				final String key = values.get(1).toString().toLowerCase();
				final Stdlib folder = Stdlib.retrieve(folderName);
				String value = folder.getMetadata().get(key);
				// Temporary, we check the upercase key if needed
				if (value == null)
					value = folder.getMetadata().get(key.toUpperCase());
				if (value == null)
					value = "";
				return TValue.fromString(value);
			}
		} catch (IOException e) {
			Logme.error(e);
		}
		return TValue.fromJson(result);

	}
}
