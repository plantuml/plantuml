/*
 * Implementation of plantuml_canvas.h on top of plutovg.
 */
#include "plantuml_canvas.h"

#include <stdlib.h>
#include <string.h>

#include <plutovg.h>

#include "plantuml_png.h"

/* Generated at build time from the TTF files (see embed_font.cmake) */
extern const unsigned char plantuml_font_liberation_sans_regular[];
extern const unsigned int plantuml_font_liberation_sans_regular_size;
extern const unsigned char plantuml_font_liberation_sans_bold[];
extern const unsigned int plantuml_font_liberation_sans_bold_size;
extern const unsigned char plantuml_font_liberation_sans_italic[];
extern const unsigned int plantuml_font_liberation_sans_italic_size;
extern const unsigned char plantuml_font_liberation_sans_bold_italic[];
extern const unsigned int plantuml_font_liberation_sans_bold_italic_size;

#define FONT_COUNT 4

typedef struct {
	plutovg_surface_t* surface;
	plutovg_canvas_t* canvas;
	plutovg_font_face_t* face;
	float font_size;
	/* Whether a clip is set: it lives in a saved state, restored to remove it */
	int clipped;
	unsigned char* png;
	int png_size;
} plantuml_canvas_t;

#define CANVAS(handle) ((plantuml_canvas_t*) (intptr_t) (handle))

/* Font faces are loaded once, from static data, and never released */
static plutovg_font_face_t* font_faces[FONT_COUNT];

static plutovg_font_face_t* get_font_face(int32_t font_id) {
	const unsigned char* data;
	unsigned int size;
	switch (font_id) {
	case PLANTUML_FONT_SANS:
		data = plantuml_font_liberation_sans_regular;
		size = plantuml_font_liberation_sans_regular_size;
		break;
	case PLANTUML_FONT_SANS_BOLD:
		data = plantuml_font_liberation_sans_bold;
		size = plantuml_font_liberation_sans_bold_size;
		break;
	case PLANTUML_FONT_SANS_ITALIC:
		data = plantuml_font_liberation_sans_italic;
		size = plantuml_font_liberation_sans_italic_size;
		break;
	case PLANTUML_FONT_SANS_BOLD_ITALIC:
		data = plantuml_font_liberation_sans_bold_italic;
		size = plantuml_font_liberation_sans_bold_italic_size;
		break;
	default:
		return NULL;
	}
	if (font_faces[font_id] == NULL)
		font_faces[font_id] = plutovg_font_face_load_from_data(data, size, 0, NULL, NULL);
	return font_faces[font_id];
}

int64_t plantuml_canvas_create(int32_t width, int32_t height) {
	plantuml_canvas_t* result = calloc(1, sizeof(plantuml_canvas_t));
	if (result == NULL)
		return 0;
	result->surface = plutovg_surface_create(width, height);
	if (result->surface == NULL) {
		free(result);
		return 0;
	}
	result->canvas = plutovg_canvas_create(result->surface);
	plutovg_canvas_set_miter_limit(result->canvas, 10);
	plantuml_canvas_set_stroke((int64_t) (intptr_t) result, 1, 0, 0);
	return (int64_t) (intptr_t) result;
}

void plantuml_canvas_destroy(int64_t handle) {
	plantuml_canvas_t* c = CANVAS(handle);
	if (c == NULL)
		return;
	plutovg_canvas_destroy(c->canvas);
	plutovg_surface_destroy(c->surface);
	free(c->png);
	free(c);
}

void plantuml_canvas_scale(int64_t handle, double factor) {
	plutovg_canvas_scale(CANVAS(handle)->canvas, (float) factor, (float) factor);
}

static plutovg_color_t to_color(int32_t argb) {
	const uint32_t v = (uint32_t) argb;
	plutovg_color_t color;
	color.r = ((v >> 16) & 0xFF) / 255.f;
	color.g = ((v >> 8) & 0xFF) / 255.f;
	color.b = (v & 0xFF) / 255.f;
	color.a = ((v >> 24) & 0xFF) / 255.f;
	return color;
}

void plantuml_canvas_set_color(int64_t handle, int32_t argb) {
	const plutovg_color_t color = to_color(argb);
	plutovg_canvas_set_rgba(CANVAS(handle)->canvas, color.r, color.g, color.b, color.a);
}

void plantuml_canvas_set_linear_gradient(int64_t handle, double x1, double y1, double x2, double y2,
		const double* offsets, const int32_t* colors, int32_t n, int32_t spread) {
	plutovg_gradient_stop_t stops[16];
	if (n > 16)
		n = 16;
	for (int i = 0; i < n; i++) {
		stops[i].offset = (float) offsets[i];
		stops[i].color = to_color(colors[i]);
	}
	const plutovg_spread_method_t method = spread == 1 ? PLUTOVG_SPREAD_METHOD_REFLECT
			: spread == 2 ? PLUTOVG_SPREAD_METHOD_REPEAT : PLUTOVG_SPREAD_METHOD_PAD;
	plutovg_canvas_set_linear_gradient(CANVAS(handle)->canvas, (float) x1, (float) y1, (float) x2, (float) y2, method,
			stops, n, NULL);
}

void plantuml_canvas_set_stroke(int64_t handle, double width, double dash_on, double dash_off) {
	plutovg_canvas_t* canvas = CANVAS(handle)->canvas;
	plutovg_canvas_set_line_width(canvas, (float) width);
	plutovg_canvas_set_line_join(canvas, PLUTOVG_LINE_JOIN_MITER);
	if (dash_on == 0) {
		plutovg_canvas_set_line_cap(canvas, PLUTOVG_LINE_CAP_SQUARE);
		plutovg_canvas_set_dash_array(canvas, NULL, 0);
	} else {
		const float dashes[2] = { (float) dash_on, (float) dash_off };
		plutovg_canvas_set_line_cap(canvas, PLUTOVG_LINE_CAP_BUTT);
		plutovg_canvas_set_dash(canvas, 0, dashes, 2);
	}
}

void plantuml_canvas_set_clip(int64_t handle, double x, double y, double width, double height) {
	plantuml_canvas_t* c = CANVAS(handle);
	if (c->clipped)
		plutovg_canvas_restore(c->canvas);
	plutovg_canvas_save(c->canvas);
	plutovg_canvas_clip_rect(c->canvas, (float) x, (float) y, (float) width, (float) height);
	c->clipped = 1;
}

void plantuml_canvas_reset_clip(int64_t handle) {
	plantuml_canvas_t* c = CANVAS(handle);
	if (c->clipped)
		plutovg_canvas_restore(c->canvas);
	c->clipped = 0;
}

void plantuml_canvas_move_to(int64_t handle, double x, double y) {
	plutovg_canvas_move_to(CANVAS(handle)->canvas, (float) x, (float) y);
}

void plantuml_canvas_line_to(int64_t handle, double x, double y) {
	plutovg_canvas_line_to(CANVAS(handle)->canvas, (float) x, (float) y);
}

void plantuml_canvas_cubic_to(int64_t handle, double x1, double y1, double x2, double y2, double x3, double y3) {
	plutovg_canvas_cubic_to(CANVAS(handle)->canvas, (float) x1, (float) y1, (float) x2, (float) y2, (float) x3,
			(float) y3);
}

void plantuml_canvas_close_path(int64_t handle) {
	plutovg_canvas_close_path(CANVAS(handle)->canvas);
}

void plantuml_canvas_rect(int64_t handle, double x, double y, double width, double height, double rx, double ry) {
	plantuml_canvas_t* c = CANVAS(handle);
	if (rx > 0 || ry > 0)
		plutovg_canvas_round_rect(c->canvas, (float) x, (float) y, (float) width, (float) height, (float) rx,
				(float) ry);
	else
		plutovg_canvas_rect(c->canvas, (float) x, (float) y, (float) width, (float) height);
}

void plantuml_canvas_ellipse(int64_t handle, double cx, double cy, double rx, double ry) {
	plutovg_canvas_ellipse(CANVAS(handle)->canvas, (float) cx, (float) cy, (float) rx, (float) ry);
}

void plantuml_canvas_fill(int64_t handle) {
	plutovg_canvas_fill(CANVAS(handle)->canvas);
}

void plantuml_canvas_stroke(int64_t handle) {
	plutovg_canvas_stroke(CANVAS(handle)->canvas);
}

int32_t plantuml_canvas_set_font(int64_t handle, int32_t font_id, double size) {
	plantuml_canvas_t* c = CANVAS(handle);
	plutovg_font_face_t* face = get_font_face(font_id);
	if (face == NULL)
		return -1;
	c->face = face;
	c->font_size = (float) size;
	plutovg_canvas_set_font(c->canvas, face, (float) size);
	return 0;
}

double plantuml_canvas_text_width(int64_t handle, const uint16_t* text, int32_t length) {
	plantuml_canvas_t* c = CANVAS(handle);
	if (c->face == NULL)
		return 0;
	return plutovg_font_face_text_extents(c->face, c->font_size, text, length, PLUTOVG_TEXT_ENCODING_UTF16, NULL);
}

void plantuml_canvas_font_metrics(int64_t handle, double* metrics) {
	plantuml_canvas_t* c = CANVAS(handle);
	float ascent = 0, descent = 0, line_gap = 0;
	if (c->face != NULL)
		plutovg_font_face_get_metrics(c->face, c->font_size, &ascent, &descent, &line_gap, NULL);
	/* stb_truetype gives the descent below the baseline as a negative value */
	metrics[0] = ascent;
	metrics[1] = -descent;
	metrics[2] = line_gap;
}

void plantuml_canvas_text_bounds(int64_t handle, const uint16_t* text, int32_t length, double* bounds) {
	plantuml_canvas_t* c = CANVAS(handle);
	plutovg_rect_t extents = { 0, 0, 0, 0 };
	if (c->face != NULL)
		plutovg_font_face_text_extents(c->face, c->font_size, text, length, PLUTOVG_TEXT_ENCODING_UTF16, &extents);
	/* plutovg gives the box with y going down from the baseline, as Java2D */
	bounds[0] = extents.x;
	bounds[1] = extents.y;
	bounds[2] = extents.w;
	bounds[3] = extents.h;
}

void plantuml_canvas_fill_text(int64_t handle, const uint16_t* text, int32_t length, double x, double y,
		double angle) {
	plantuml_canvas_t* c = CANVAS(handle);
	if (c->face == NULL)
		return;
	if (angle == 0) {
		plutovg_canvas_fill_text(c->canvas, text, length, PLUTOVG_TEXT_ENCODING_UTF16, (float) x, (float) y);
		return;
	}
	plutovg_canvas_save(c->canvas);
	plutovg_canvas_translate(c->canvas, (float) x, (float) y);
	plutovg_canvas_rotate(c->canvas, (float) angle);
	plutovg_canvas_fill_text(c->canvas, text, length, PLUTOVG_TEXT_ENCODING_UTF16, 0, 0);
	plutovg_canvas_restore(c->canvas);
}

int32_t plantuml_canvas_encode_png(int64_t handle, int32_t opaque) {
	plantuml_canvas_t* c = CANVAS(handle);
	free(c->png);
	c->png = NULL;
	c->png_size = 0;
	/* Encoded from the pixels of the surface, which it changes: once only */
	unsigned char* png;
	size_t size;
	if (plantuml_png_encode((uint32_t*) plutovg_surface_get_data(c->surface), plutovg_surface_get_width(c->surface),
			plutovg_surface_get_height(c->surface), plutovg_surface_get_stride(c->surface), opaque, &png, &size) != 0
			|| size > 0x7FFFFFFF) {
		return -1;
	}
	c->png = png;
	c->png_size = (int) size;
	return c->png_size;
}

void plantuml_canvas_copy_png(int64_t handle, int8_t* dest) {
	plantuml_canvas_t* c = CANVAS(handle);
	if (c->png_size > 0)
		memcpy(dest, c->png, c->png_size);
}
