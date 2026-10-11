/*
 * Thin C facade over plutovg, used by the PNG output of the TeaVM C build.
 *
 * Every function is called from Java through TeaVM's @Import (see
 * net.sourceforge.plantuml.teavm.c.NativeCanvas), so the signatures only
 * use what TeaVM can pass to C without any glue:
 *
 * - a canvas is an int64_t handle (a pointer kept in a Java long);
 * - text is UTF-16: a Java char[] (passed as a pointer to its data) and a
 *   length, never a C string (TeaVM's String to C conversion depends on the
 *   C locale);
 * - colors are 0xAARRGGBB, like java.awt.Color.getRGB();
 * - coordinates are in pixels, y going down, like Java2D, before the scale of
 *   plantuml_canvas_scale.
 *
 * Java arrays are passed as pointers into the Java heap: they are only valid
 * during the call, and C never keeps them.
 *
 * The C side never touches the file system: the PNG is encoded in memory and
 * copied back into a Java byte[] (Java writes the file, with PlantUML's own
 * PNG chunks).
 */
#ifndef PLANTUML_CANVAS_H
#define PLANTUML_CANVAS_H

#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

/* Embedded fonts (font_id of plantuml_canvas_set_font): Liberation Sans */
#define PLANTUML_FONT_SANS 0
#define PLANTUML_FONT_SANS_BOLD 1
#define PLANTUML_FONT_SANS_ITALIC 2
#define PLANTUML_FONT_SANS_BOLD_ITALIC 3

/* Creates a transparent canvas; returns 0 on failure */
int64_t plantuml_canvas_create(int32_t width, int32_t height);
void plantuml_canvas_destroy(int64_t canvas);

/* Scales everything drawn afterwards (call it once, before drawing) */
void plantuml_canvas_scale(int64_t canvas, double factor);

/* Paint: a plain color, or a linear gradient (spread: 0 pad, 1 reflect,
 * 2 repeat) whose n stops are given by offsets (0 to 1) and colors */
void plantuml_canvas_set_color(int64_t canvas, int32_t argb);
void plantuml_canvas_set_linear_gradient(int64_t canvas, double x1, double y1, double x2, double y2,
		const double* offsets, const int32_t* colors, int32_t n, int32_t spread);

/* Stroke, as java.awt.BasicStroke: without dashes (dash_on = 0), square caps;
 * with dashes, butt caps; miter joins (limit 10) in both cases */
void plantuml_canvas_set_stroke(int64_t canvas, double width, double dash_on, double dash_off);

/* Rectangular clip, replacing the previous one; reset_clip removes it */
void plantuml_canvas_set_clip(int64_t canvas, double x, double y, double width, double height);
void plantuml_canvas_reset_clip(int64_t canvas);

/* Path building: each call adds to the current path */
void plantuml_canvas_move_to(int64_t canvas, double x, double y);
void plantuml_canvas_line_to(int64_t canvas, double x, double y);
void plantuml_canvas_cubic_to(int64_t canvas, double x1, double y1, double x2, double y2, double x3, double y3);
void plantuml_canvas_close_path(int64_t canvas);
/* rx, ry: radii of the corners */
void plantuml_canvas_rect(int64_t canvas, double x, double y, double width, double height, double rx, double ry);
void plantuml_canvas_ellipse(int64_t canvas, double cx, double cy, double rx, double ry);

/* Painting: both consume the current path */
void plantuml_canvas_fill(int64_t canvas);
void plantuml_canvas_stroke(int64_t canvas);

/* Text. set_font returns 0, or -1 for an unknown font_id */
int32_t plantuml_canvas_set_font(int64_t canvas, int32_t font_id, double size);
/* Advance width of the text with the current font */
double plantuml_canvas_text_width(int64_t canvas, const uint16_t* text, int32_t length);
/* metrics[0] = ascent (> 0), metrics[1] = descent (> 0), metrics[2] = line gap */
void plantuml_canvas_font_metrics(int64_t canvas, double* metrics);
/* Draws the text with its baseline at y, like Graphics2D.drawString(), rotated
 * by angle (radians, clockwise on screen) around (x, y) */
void plantuml_canvas_fill_text(int64_t canvas, const uint16_t* text, int32_t length, double x, double y,
		double angle);

/* PNG (see plantuml_png.h): encode returns the size in bytes (or -1), copy_png
 * copies the encoded bytes into dest, which must hold that many bytes. opaque:
 * the image has an opaque background (PlantUML then writes an RGB or indexed
 * image). Encoding changes the pixels: draw nothing afterwards. */
int32_t plantuml_canvas_encode_png(int64_t canvas, int32_t opaque);
void plantuml_canvas_copy_png(int64_t canvas, int8_t* dest);

#ifdef __cplusplus
}
#endif

#endif
