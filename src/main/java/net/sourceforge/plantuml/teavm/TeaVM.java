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
 */
package net.sourceforge.plantuml.teavm;

// ::comment when JAVA8
// import org.teavm.interop.PlatformMarker;
// ::done

public class TeaVM {

	/**
	 * Platform marker method used to distinguish TeaVM compilation from standard
	 * JVM execution.
	 *
	 * When compiling to JavaScript, TeaVM resolves this method statically to
	 * {@code true}. As a result, conditional branches like:
	 *
	 * <pre>
	 * if (TeaVM.isTeaVM()) {
	 * 	// TeaVM-specific code
	 * } else {
	 * 	// JVM-specific code
	 * }
	 * </pre>
	 * 
	 * are evaluated at compile time, and the unreachable branch is completely
	 * removed from the generated JavaScript output (dead code elimination).
	 * 
	 * When running on the JVM, this method simply returns {@code false} and no code
	 * elimination occurs: both branches remain present in the bytecode.
	 * 
	 * This mechanism is intended purely as a compile-time optimization hint for
	 * TeaVM, not as a runtime platform check.
	 */
	// ::comment when JAVA8
	@org.teavm.interop.PlatformMarker
	// ::done
	public static boolean isTeaVM() {
		return false;
	}

}
