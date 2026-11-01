/*
 * Implementation of plantuml_canvas.h on top of plutovg.
 */
#include "plantuml_canvas.h"

#include <math.h>
#include <stdlib.h>
#include <string.h>

#include <plutovg.h>

#include "plantuml_fonts.h"
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

/*
 * The characters the embedded font has no glyph for are drawn with a font of the
 * machine, when there is one (plantuml_fonts.h). The usual text has none, and
 * is measured and drawn by plutovg as a whole: the output does not depend on
 * the machine. The text that has some is measured and drawn glyph by glyph.
 */

/* The face that draws a code point */
static plutovg_font_face_t* face_of(plantuml_canvas_t* c, plutovg_codepoint_t codepoint) {
	if (plantuml_face_lacks(c->face, codepoint) == 0)
		return c->face;
	plutovg_font_face_t* fallback = plantuml_fallback_face(codepoint);
	return fallback != NULL ? fallback : c->face;
}

static int needs_fallback(plantuml_canvas_t* c, const uint16_t* text, int32_t length) {
	plutovg_text_iterator_t it;
	plutovg_text_iterator_init(&it, text, length, PLUTOVG_TEXT_ENCODING_UTF16);
	while (plutovg_text_iterator_has_next(&it))
		if (plantuml_face_lacks(c->face, plutovg_text_iterator_next(&it)))
			return 1;
	return 0;
}

/* plutovg_font_face_text_extents(), a face per code point */
static float mixed_text_extents(plantuml_canvas_t* c, const uint16_t* text, int32_t length,
		plutovg_rect_t* extents) {
	plutovg_text_iterator_t it;
	plutovg_text_iterator_init(&it, text, length, PLUTOVG_TEXT_ENCODING_UTF16);
	plutovg_rect_t box = { 0, 0, 0, 0 };
	int first = 1;
	float total = 0.f;
	while (plutovg_text_iterator_has_next(&it)) {
		const plutovg_codepoint_t codepoint = plutovg_text_iterator_next(&it);
		float advance;
		plutovg_rect_t glyph;
		plutovg_font_face_get_glyph_metrics(face_of(c, codepoint), c->font_size, codepoint, &advance, NULL, &glyph);
		glyph.x += total;
		total += advance;
		if (first) {
			box = glyph;
			first = 0;
			continue;
		}
		const float x1 = fminf(box.x, glyph.x);
		const float y1 = fminf(box.y, glyph.y);
		const float x2 = fmaxf(box.x + box.w, glyph.x + glyph.w);
		const float y2 = fmaxf(box.y + box.h, glyph.y + glyph.h);
		box.x = x1;
		box.y = y1;
		box.w = x2 - x1;
		box.h = y2 - y1;
	}
	if (extents != NULL)
		*extents = box;
	return total;
}

/* plutovg_canvas_fill_text(), a face per code point */
static void fill_text(plantuml_canvas_t* c, const uint16_t* text, int32_t length, float x, float y) {
	if (needs_fallback(c, text, length) == 0) {
		plutovg_canvas_fill_text(c->canvas, text, length, PLUTOVG_TEXT_ENCODING_UTF16, x, y);
		return;
	}
	plutovg_text_iterator_t it;
	plutovg_text_iterator_init(&it, text, length, PLUTOVG_TEXT_ENCODING_UTF16);
	plutovg_canvas_new_path(c->canvas);
	float advance = 0.f;
	while (plutovg_text_iterator_has_next(&it)) {
		const plutovg_codepoint_t codepoint = plutovg_text_iterator_next(&it);
		plutovg_canvas_set_font_face(c->canvas, face_of(c, codepoint));
		advance += plutovg_canvas_add_glyph(c->canvas, codepoint, x + advance, y);
	}
	plutovg_canvas_set_font_face(c->canvas, c->face);
	plutovg_canvas_fill(c->canvas);
}

double plantuml_canvas_text_width(int64_t handle, const uint16_t* text, int32_t length) {
	plantuml_canvas_t* c = CANVAS(handle);
	if (c->face == NULL)
		return 0;
	if (needs_fallback(c, text, length))
		return mixed_text_extents(c, text, length, NULL);
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
	if (c->face != NULL) {
		if (needs_fallback(c, text, length))
			mixed_text_extents(c, text, length, &extents);
		else
			plutovg_font_face_text_extents(c->face, c->font_size, text, length, PLUTOVG_TEXT_ENCODING_UTF16,
					&extents);
	}
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
		fill_text(c, text, length, (float) x, (float) y);
		return;
	}
	plutovg_canvas_save(c->canvas);
	plutovg_canvas_translate(c->canvas, (float) x, (float) y);
	plutovg_canvas_rotate(c->canvas, (float) angle);
	fill_text(c, text, length, 0, 0);
	plutovg_canvas_restore(c->canvas);
}

/* plutovg keeps premultiplied pixels, Java gives (and expects) straight ones */
static uint32_t premultiply(uint32_t argb) {
	const uint32_t a = argb >> 24;
	if (a == 255)
		return argb;
	if (a == 0)
		return 0;
	const uint32_t r = (((argb >> 16) & 0xFF) * a + 127) / 255;
	const uint32_t g = (((argb >> 8) & 0xFF) * a + 127) / 255;
	const uint32_t b = ((argb & 0xFF) * a + 127) / 255;
	return (a << 24) | (r << 16) | (g << 8) | b;
}

static uint32_t unpremultiply(uint32_t argb) {
	const uint32_t a = argb >> 24;
	if (a == 255 || a == 0)
		return argb;
	uint32_t r = (((argb >> 16) & 0xFF) * 255 + a / 2) / a;
	uint32_t g = (((argb >> 8) & 0xFF) * 255 + a / 2) / a;
	uint32_t b = ((argb & 0xFF) * 255 + a / 2) / a;
	if (r > 255)
		r = 255;
	if (g > 255)
		g = 255;
	if (b > 255)
		b = 255;
	return (a << 24) | (r << 16) | (g << 8) | b;
}

void plantuml_canvas_draw_image(int64_t handle, const int32_t* argb, int32_t width, int32_t height, double x,
		double y, double scale) {
	plantuml_canvas_t* c = CANVAS(handle);
	if (width <= 0 || height <= 0)
		return;
	plutovg_surface_t* image = plutovg_surface_create(width, height);
	if (image == NULL)
		return;
	const int stride = plutovg_surface_get_stride(image);
	unsigned char* data = plutovg_surface_get_data(image);
	for (int j = 0; j < height; j++) {
		uint32_t* row = (uint32_t*) (data + (size_t) j * stride);
		const int32_t* source = argb + (size_t) j * width;
		for (int i = 0; i < width; i++)
			row[i] = premultiply((uint32_t) source[i]);
	}
	/* The paint is part of the saved state: restored with it */
	plutovg_canvas_save(c->canvas);
	plutovg_canvas_translate(c->canvas, (float) x, (float) y);
	plutovg_canvas_scale(c->canvas, (float) scale, (float) scale);
	plutovg_canvas_set_texture(c->canvas, image, PLUTOVG_TEXTURE_TYPE_PLAIN, 1.f, NULL);
	plutovg_canvas_fill_rect(c->canvas, 0, 0, (float) width, (float) height);
	plutovg_canvas_restore(c->canvas);
	plutovg_surface_destroy(image);
}

static uint32_t* decoded_image = NULL;
static size_t decoded_image_count = 0;

int32_t plantuml_image_decode(const int8_t* data, int32_t length, int32_t* size) {
	free(decoded_image);
	decoded_image = NULL;
	decoded_image_count = 0;
	if (length <= 0)
		return -1;
	plutovg_surface_t* image = plutovg_surface_load_from_image_data(data, length);
	if (image == NULL)
		return -1;
	const int width = plutovg_surface_get_width(image);
	const int height = plutovg_surface_get_height(image);
	const int stride = plutovg_surface_get_stride(image);
	const unsigned char* source = plutovg_surface_get_data(image);
	const size_t count = (size_t) width * (size_t) height;
	uint32_t* pixels = (uint32_t*) malloc(count * sizeof(uint32_t));
	if (pixels == NULL) {
		plutovg_surface_destroy(image);
		return -1;
	}
	for (int j = 0; j < height; j++) {
		const uint32_t* row = (const uint32_t*) (source + (size_t) j * stride);
		for (int i = 0; i < width; i++)
			pixels[(size_t) j * width + i] = unpremultiply(row[i]);
	}
	plutovg_surface_destroy(image);
	decoded_image = pixels;
	decoded_image_count = count;
	size[0] = width;
	size[1] = height;
	return 0;
}

void plantuml_image_decoded_copy(int32_t* dest) {
	if (decoded_image_count > 0)
		memcpy(dest, decoded_image, decoded_image_count * sizeof(uint32_t));
	free(decoded_image);
	decoded_image = NULL;
	decoded_image_count = 0;
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

static unsigned char* image_png = NULL;
static size_t image_png_size = 0;

int32_t plantuml_png_image_encode(const int32_t* argb, int32_t width, int32_t height, int32_t opaque) {
	free(image_png);
	image_png = NULL;
	image_png_size = 0;
	if (width <= 0 || height <= 0)
		return -1;
	const size_t count = (size_t) width * (size_t) height;
	uint32_t* pixels = (uint32_t*) malloc(count * sizeof(uint32_t));
	if (pixels == NULL)
		return -1;
	for (size_t i = 0; i < count; i++)
		pixels[i] = (uint32_t) argb[i];
	unsigned char* png;
	size_t size;
	const int failed = plantuml_png_encode_lossless(pixels, width, height, width * 4, opaque, &png, &size);
	free(pixels);
	if (failed != 0 || size > 0x7FFFFFFF)
		return -1;
	image_png = png;
	image_png_size = size;
	return (int32_t) size;
}

void plantuml_png_image_copy(int8_t* dest) {
	if (image_png_size > 0)
		memcpy(dest, image_png, image_png_size);
	free(image_png);
	image_png = NULL;
	image_png_size = 0;
}
