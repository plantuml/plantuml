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
package net.sourceforge.plantuml.anim;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class AnimationScript {

	private final ScriptEngine engine;

	public AnimationScript() {

		final ScriptEngineManager manager = new ScriptEngineManager();
		engine = manager.getEngineByName("js");

		// ScriptEngineManager manager = new ScriptEngineManager();
		// List<ScriptEngineFactory> factories = manager.getEngineFactories();
		// for (ScriptEngineFactory factory : factories) {
		// System.out.println("Name : " + factory.getEngineName());
		// System.out.println("Version : " + factory.getEngineVersion());
		// System.out.println("Language name : " + factory.getLanguageName());
		// System.out.println("Language version : " + factory.getLanguageVersion());
		// System.out.println("Extensions : " + factory.getExtensions());
		// System.out.println("Mime types : " + factory.getMimeTypes());
		// System.out.println("Names : " + factory.getNames());
		//
		// }

	}

	public String eval(String line) throws ScriptException {
		final ScriptContext context = engine.getContext();
		final StringWriter sw = new StringWriter();
		context.setWriter(new PrintWriter(sw));
		engine.eval(line, context);
		final String result = sw.toString();
		return result;
	}
}
