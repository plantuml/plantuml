/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2026, Jamison Jiang
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
 * Original Author:  Jamison Jiang
 *
 *
 */
package net.sourceforge.plantuml.svek.layout;

import net.sourceforge.plantuml.log.Logme;

public final class SvekLayoutBuilders {

	private static final String GRAPH_SUPPORT = "net.sourceforge.plantuml.graphsupport.GraphSupportSvekLayoutBuilder";

	/**
	 * Returns null when this distribution does not ship the graph-support engine.
	 */
	public static SvekLayoutBuilder graphSupport() {
		try {
			return (SvekLayoutBuilder) Class.forName(GRAPH_SUPPORT).getDeclaredConstructor().newInstance();
		} catch (ClassNotFoundException e) {
			// Distributions that cannot embed the engine exclude the adapter.
			return null;
		} catch (NoClassDefFoundError e) {
			// Distributions that compile the adapter without shipping the engine.
			return null;
		} catch (ReflectiveOperationException e) {
			Logme.error(e);
			return null;
		} catch (LinkageError e) {
			Logme.error(e);
			return null;
		}
	}

	private SvekLayoutBuilders() {
	}
}
