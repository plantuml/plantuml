/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2021, Arnaud Roques
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
package net.sourceforge.plantuml.tim.stdlib;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.FileUtils;
import net.sourceforge.plantuml.json.Json;
import net.sourceforge.plantuml.json.JsonValue;
import net.sourceforge.plantuml.json.ParseException;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.security.SURL;
import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.EaterExceptionLocated;
import net.sourceforge.plantuml.tim.TContext;
import net.sourceforge.plantuml.tim.TFunctionSignature;
import net.sourceforge.plantuml.tim.TMemory;
import net.sourceforge.plantuml.tim.expression.TValue;
import net.sourceforge.plantuml.utils.LineLocation;

/**
 * Loads JSON data from file or URL source.
 * <p>
 * Supports three parameters for datasource, default JSON value and charset. The
 * datasource will be checked against the security rules.
 * <p>
 * Examples:<br/>
 * 
 * <pre>
 *     &#64; startuml
 *     ' loads a local file
 *     !$JSON_LOCAL_RELATIVE=%load_json("file.json")
 *
 *     ' loads a local file from an absolute file path
 *     !$JSON_LOCAL_ABS=%load_json("c:/loaded/data/file.json")
 *
 *     ' tries to load a local file and returns an empty JSON
 *     !$JSON_LOCAL_REL_EMPTY=%load_json("file-not-existing.json")
 *
 *     ' tries to load a local file and returns an default JSON
 *     !$DEF_JSON={"status":"No data found"}
 *     !$JSON_LOCAL_REL_DEF=%load_json("file-not-existing.json", $DEF_JSON)
 *
 *     ' loads a local file with a specific charset (default is UTF-8)
 *     !$JSON_LOCAL_RELATIVE_CHARSET=%load_json("file.json", "{}", "iso-8859-1")
 *
 *     ' loads a remote JSON from an endpoint (and default, if not reachable)
 *     !$STATUS_NO_CONNECTION={"status": "No connection"}
 *     !$JSON_REMOTE_DEF=%load_json("https://localhost:7778/management/health", $STATUS_NO_CONNECTION)
 *     status -&gt; $JSON_REMOTE_DEF.status
 *     &#64; enduml
 * </pre>
 * 
 * @author Aljoscha Rittner
 */
public class LoadJson extends SimpleReturnFunction {

	private static final String VALUE_CHARSET_DEFAULT = "UTF-8";

	private static final String VALUE_DEFAULT_DEFAULT = "{}";

	public TFunctionSignature getSignature() {
		return new TFunctionSignature("%load_json", 3);
	}

	public boolean canCover(int nbArg, Set<String> namedArgument) {
		return nbArg == 1 || nbArg == 2 || nbArg == 3;
	}

	public TValue executeReturnFunction(TContext context, TMemory memory, LineLocation location, List<TValue> values,
			Map<String, TValue> named) throws EaterException, EaterExceptionLocated {
		final String path = values.get(0).toString();
		try {
			String data = loadStringData(path, getCharset(values));
			if (data == null)
				data = getDefaultJson(values);

			JsonValue jsonValue = Json.parse(data);
			return TValue.fromJson(jsonValue);
		} catch (ParseException pe) {
			Logme.error(pe);
			throw EaterException.unlocated("JSON parse issue in source " + path + " on location " + pe.getLocation());
		} catch (UnsupportedEncodingException e) {
			Logme.error(e);
			throw EaterException.unlocated("JSON encoding issue in source " + path + ": " + e.getMessage());
		}
	}

	/**
	 * Returns the JSON default, if the data source contains no data.
	 * 
	 * @param values value parameters
	 * @return the defined default JSON or {@code "{}"}
	 */
	private String getDefaultJson(List<TValue> values) {
		if (values.size() > 1)
			return values.get(1).toString();

		return VALUE_DEFAULT_DEFAULT;
	}

	/**
	 * Returns the charset name (if set)
	 * 
	 * @param values value parameters
	 * @return defined charset or {@code "UTF-8"}
	 */
	private String getCharset(List<TValue> values) {
		if (values.size() == 3)
			return values.get(2).toString();

		return VALUE_CHARSET_DEFAULT;
	}

	/**
	 * Loads String data from a data source {@code path} (file or URL) and expects
	 * the data encoded in {@code charset}.
	 * 
	 * @param path    path to data source (http(s)-URL or file).
	 * @param charset character set to encode the string data
	 * @return the decoded String from the data source
	 * @throws EaterException if something went wrong on reading data
	 */
	private String loadStringData(String path, String charset) throws EaterException, UnsupportedEncodingException {

		byte[] byteData = null;
		if (path.startsWith("http://") || path.startsWith("https://")) {
			final SURL url = SURL.create(path);
			if (url != null)
				byteData = url.getBytes();
			// ::comment when __CORE__
		} else {
			try {
				final SFile file = FileSystem.getInstance().getFile(path);
				if (file != null && file.exists() && file.canRead() && !file.isDirectory()) {
					final ByteArrayOutputStream out = new ByteArrayOutputStream(1024 * 8);
					FileUtils.copyToStream(file, out);
					byteData = out.toByteArray();
				}
			} catch (IOException e) {
				Logme.error(e);
			}
			// ::done
		}

		if (byteData == null || byteData.length == 0)
			return null; // no length, no data (we want the default)

		return new String(byteData, charset);

	}
}
