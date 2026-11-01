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
package net.sourceforge.plantuml.math;

import net.sourceforge.plantuml.teavm.TeaVM;
// ::comment when JAVA8
import net.sourceforge.plantuml.teavm.c.JlatexmathResources;
import net.sourceforge.plantuml.teavm.js.JlatexmathResourcesJs;
import net.sourceforge.plantuml.teavm.js.browser.ScriptLoaderJs;
// ::done
import org.scilab.forge.jlatexmath.TeXClassLoaderResources;
import org.scilab.forge.jlatexmath.TeXResources;

// ::remove file when __MIT__ or __EPL__ or __BSD__ or __ASL__ or __LGPL__

/**
 * Tells JLaTeXMath where to read its resources (the XML files, the glyph
 * outlines): the files embedded in the executable in the C build (no class loader
 * there), {@code jlatexmath.js} in the browser build, the class loaders on the JVM ({@link TeXClassLoaderResources}). The
 * library itself has no class loader code, so that the C build never reaches it.
 * It is done before the first formula, by {@link TeXIconBuilder}.
 */
final class MathResources {

	private MathResources() {
	}

	static synchronized void ensure() {
		// Done once (and again if something took the provider away)
		if (TeXResources.getProvider() != null)
			return;

		// The marker is tested here, so that TeaVM prunes the class loader code
		// ::comment when JAVA8
		if (TeaVM.isC()) {
			TeXResources.setProvider(new JlatexmathResources());
			return;
		}
		// Browser: the resources come from jlatexmath.js (loaded on demand, like
		// emoji.js). It never waits: when the script is not there yet, this attempt
		// stops here (the formula is shown as text) and the diagram is rendered
		// again once the script is loaded (PendingResources).
		if (TeaVM.isTeaVM()) {
			if (JlatexmathResourcesJs.isAvailable() == false && ScriptLoaderJs.require("jlatexmath.js") == false)
				throw new IllegalStateException("jlatexmath.js is being loaded");
			TeXResources.setProvider(new JlatexmathResourcesJs());
			return;
		}
		// ::done
		TeXResources.setProvider(new TeXClassLoaderResources());
	}

}
