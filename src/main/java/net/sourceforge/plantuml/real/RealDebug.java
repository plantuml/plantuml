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
package net.sourceforge.plantuml.real;

import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.teavm.TeaVM;

// Layout (RealMax/RealMin/PositiveForce chains) can create many thousands of
// objects for a single large diagram: one RealMax/RealMoveable per gauge tile,
// one PositiveForce per constraint. Capturing a stack trace in every
// constructor "just in case" it is needed later for a diagnostic message is
// affordable on the JVM (fillInStackTrace() is cheap there) but is much more
// costly on targets compiled through TeaVM, where each capture walks a native
// JavaScript stack instead of a JVM one. Since these captures only exist to
// help track down an "Infinite recursion?" crash and are never used on the
// happy path, they are opt-in on the JVM: enable with -DPLANTUML_REAL_DEBUG=true
// (or the environment variable of the same name) while investigating such a
// crash.
//
// create() is the single gate for this: it hands back a real RealDebug
// (holding an already-captured stack trace) only when the flag above is set,
// and null otherwise. A RealMax/RealMoveable/PositiveForce that doesn't need
// one therefore pays for neither the capture nor an extra allocation -- its
// creationPoint field is just null, exactly as if RealDebug didn't exist --
// which is what keeps this opt-in rather than paid by every instance.
// printCreationStackTrace(RealDebug) is the matching read side: it prints the
// "rerun with -DPLANTUML_REAL_DEBUG=true" fallback for a null RealDebug (the
// common case) or forwards the captured trace to Logme.error(Throwable) for a
// real one.
//
// Under TeaVM, create() unconditionally returns null, not just by default:
// its only consumer, Logme.error(Throwable), is itself a no-op there (see
// Logme.java's own "if (!TeaVM.isTeaVM())" guard), so a captured stack trace
// could never be shown to anyone in a browser build even if PLANTUML_REAL_DEBUG
// were set -- there is no debugging capability being traded away, only dead
// work being skipped. TeaVM.isTeaVM() resolves to a compile-time constant, so
// `!TeaVM.isTeaVM() && CAPTURE_CREATION_POINT` in create() folds to a constant
// false under TeaVM and the whole branch -- including the call that would have
// allocated a RealDebug -- is dead-code-eliminated from the generated
// JavaScript rather than merely short-circuited at runtime -- same mechanism
// as FileFormat.imDummy. (CAPTURE_CREATION_POINT itself is still evaluated,
// once, at class-init time under TeaVM too, since nothing marks the
// System.getProperty/getenv calls below as JVM-only; that single read has no
// measurable cost, unlike the per-instance capture it used to gate directly.)
class RealDebug {

	static private final boolean CAPTURE_CREATION_POINT = isTrue(System.getProperty("PLANTUML_REAL_DEBUG"))
			|| isTrue(System.getenv("PLANTUML_REAL_DEBUG"));

	private final Throwable creationPoint;

	// Returns null (no allocation) unless PLANTUML_REAL_DEBUG is set and we're
	// not under TeaVM -- see the class comment above for why that keeps this
	// free on the common path in both environments.
	public static RealDebug create() {
		if (!TeaVM.isTeaVM() && CAPTURE_CREATION_POINT)
			return new RealDebug();
		return null;
	}

	private RealDebug() {
		creationPoint = new Throwable();
		creationPoint.fillInStackTrace();
	}

	// debug is null whenever create() didn't capture anything (flag off, or
	// TeaVM) -- see the class comment above.
	public static void printCreationStackTrace(RealDebug debug) {
		if (debug == null)
			System.err.println("(creation point not captured; rerun with -DPLANTUML_REAL_DEBUG=true to capture it)");
		else
			Logme.error(debug.creationPoint);
	}

	private static boolean isTrue(final String value) {
		return value != null && (value.equalsIgnoreCase("true") || value.equals("1"));
	}

}
