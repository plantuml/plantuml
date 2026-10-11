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
package net.sourceforge.plantuml.teavm.c;

import org.teavm.interop.Import;
import org.teavm.interop.Unmanaged;
import org.teavm.interop.c.Include;

/**
 * Raw bindings to the C facade over plutovg (src/teavm-c/native/plantuml_canvas.h).
 *
 * TeaVM's C backend turns each call to these native methods into a direct call
 * to the C function named by {@link Import}, and {@link Include} adds
 * <code>#include "plantuml_canvas.h"</code> to the generated files that call
 * them. Primitive arrays are passed as pointers to their data (only valid
 * during the call), which is how text goes through: as UTF-16 char[], since
 * TeaVM converts a String to a C string with the C locale.
 *
 * These methods never allocate in the Java heap nor call back into Java, hence
 * {@link Unmanaged} (no GC bookkeeping around the calls): this class must only
 * contain native methods. Use {@link PngCanvas}, not this class.
 *
 * Only usable in the C build: on the JVM (or in JavaScript) any call fails.
 */
@Include(value = "plantuml_canvas.h", isSystem = false)
@Unmanaged
final class NativeCanvas {
	// ::remove file when JAVA8

	private NativeCanvas() {
	}

	// Font ids of plantuml_canvas_set_font
	static final int FONT_SANS = 0;
	static final int FONT_SANS_BOLD = 1;
	static final int FONT_SANS_ITALIC = 2;
	static final int FONT_SANS_BOLD_ITALIC = 3;

	@Import(name = "plantuml_canvas_create")
	static native long create(int width, int height);

	@Import(name = "plantuml_canvas_destroy")
	static native void destroy(long canvas);

	@Import(name = "plantuml_canvas_scale")
	static native void scale(long canvas, double factor);

	@Import(name = "plantuml_canvas_set_color")
	static native void setColor(long canvas, int argb);

	@Import(name = "plantuml_canvas_set_linear_gradient")
	static native void setLinearGradient(long canvas, double x1, double y1, double x2, double y2, double[] offsets,
			int[] colors, int n, int spread);

	@Import(name = "plantuml_canvas_set_stroke")
	static native void setStroke(long canvas, double width, double dashOn, double dashOff);

	@Import(name = "plantuml_canvas_set_clip")
	static native void setClip(long canvas, double x, double y, double width, double height);

	@Import(name = "plantuml_canvas_reset_clip")
	static native void resetClip(long canvas);

	@Import(name = "plantuml_canvas_move_to")
	static native void moveTo(long canvas, double x, double y);

	@Import(name = "plantuml_canvas_line_to")
	static native void lineTo(long canvas, double x, double y);

	@Import(name = "plantuml_canvas_cubic_to")
	static native void cubicTo(long canvas, double x1, double y1, double x2, double y2, double x3, double y3);

	@Import(name = "plantuml_canvas_close_path")
	static native void closePath(long canvas);

	@Import(name = "plantuml_canvas_rect")
	static native void rect(long canvas, double x, double y, double width, double height, double rx, double ry);

	@Import(name = "plantuml_canvas_ellipse")
	static native void ellipse(long canvas, double cx, double cy, double rx, double ry);

	@Import(name = "plantuml_canvas_fill")
	static native void fill(long canvas);

	@Import(name = "plantuml_canvas_stroke")
	static native void stroke(long canvas);

	@Import(name = "plantuml_canvas_set_font")
	static native int setFont(long canvas, int fontId, double size);

	@Import(name = "plantuml_canvas_text_width")
	static native double textWidth(long canvas, char[] text, int length);

	@Import(name = "plantuml_canvas_font_metrics")
	static native void fontMetrics(long canvas, double[] metrics);

	@Import(name = "plantuml_canvas_fill_text")
	static native void fillText(long canvas, char[] text, int length, double x, double y, double angle);

	@Import(name = "plantuml_canvas_encode_png")
	static native int encodePng(long canvas, int opaque);

	@Import(name = "plantuml_canvas_copy_png")
	static native void copyPng(long canvas, byte[] dest);

}
