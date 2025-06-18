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
 *
 */
package com.plantuml.api.cheerpj;

import net.sourceforge.plantuml.utils.Log;

//::revert when __CORE__
//import com.leaningtech.client.Document;
//import com.leaningtech.client.Element;
//import com.leaningtech.client.Global;
//::done

public class WasmLog {

	public static long start;

	public static void log(final String message) {
		// ::revert when __CORE__
		Log.info(() -> message);
//		try {
//			if (start > 0) {
//				final long duration = System.currentTimeMillis() - start;
//				message = "(" + duration + " ms) " + message;
//			}
//			System.err.print(message);
//			if (StaticMemory.elementIdDebugJava == null)
//				return;
//			final Document document = Global.document;
//			if (document == null)
//				return;
//			final Element messageJava = document.getElementById(Global.JSString(StaticMemory.elementIdDebugJava));
//			if (messageJava == null)
//				return;
//			messageJava.set_textContent(Global.JSString(message));
//		} catch (Throwable t) {
//			System.err.print("Error " + t);
//			t.printStackTrace();
//		}
		// ::done
	}

}
