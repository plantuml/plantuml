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
import java.util.Set;

import net.sourceforge.plantuml.json.Json;
import net.sourceforge.plantuml.json.JsonArray;
import net.sourceforge.plantuml.json.JsonObject;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.preproc.Stdlib;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.TContext;
import net.sourceforge.plantuml.tim.TFunctionSignature;
import net.sourceforge.plantuml.tim.TMemory;
import net.sourceforge.plantuml.tim.expression.TValue;

@Deprecated
public class GetAllStdlib extends SimpleReturnFunction {
	// Maybe the function %get_all_stdlib() will be removed and replaced by
	// %get_stdlib()

	private static final TFunctionSignature SIGNATURE = new TFunctionSignature("%get_all_stdlib", 1);

	public TFunctionSignature getSignature() {
		return SIGNATURE;
	}

	@Override
	public boolean canCover(int nbArg, Set<String> namedArgument) {
		return nbArg == 0 || nbArg == 1;
	}

	@Override
	public TValue executeReturnFunction(TContext context, TMemory memory, StringLocated location, List<TValue> values,
			Map<String, TValue> named) throws EaterException {

		switch (values.size()) {
		case 0:
			final JsonArray result = new JsonArray();
			try {
				for (String name : Stdlib.getAllFolderNames()) {
					result.add(name);
				}
				return TValue.fromJson(result);
			} catch (IOException e) {
				Logme.error(e);
				return TValue.fromJson(result);
			}

		case 1:
			final JsonObject res = new JsonObject();
			try {
				// Inspired by Stdlib.addInfoVersion
				for (String name : Stdlib.getAllFolderNames()) {
					final Stdlib folder = Stdlib.retrieve(name);
					final JsonObject object = Json.object() //
							.add("name", name) //
							.add("version", folder.getVersion()) //
							.add("source", folder.getSource());
					res.add(name, object);
				}
				return TValue.fromJson(res);
			} catch (IOException e) {
				Logme.error(e);
				return TValue.fromJson(res);
			}

		default:
			assert false; // Should not append because of canCover()
			throw new EaterException("Error on get_all_stdlib: Too many arguments", location);
		}
	}
}
