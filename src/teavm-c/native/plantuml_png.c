/*
 * Implementation of plantuml_png.h. The quantization is a port of
 * net.sourceforge.plantuml.png.quant (Quantify555, Cube555, QuantUtils):
 * keep both in step.
 */
#include "plantuml_png.h"

#include <stdlib.h>
#include <string.h>

#include "miniz.h"

/* ---------------------------------------------------------------------- */
/* Quantify555                                                            */
/* ---------------------------------------------------------------------- */

/* 32*32*32 RGB555 cubes + 1 extra slot for the fully transparent color */
#define CUBE_COUNT_RGB555 (32 * 32 * 32)
#define TRANSPARENT_CUBE CUBE_COUNT_RGB555
#define TOTAL_CUBE_SLOTS (CUBE_COUNT_RGB555 + 1)
#define MIN_CUBES_THRESHOLD 32
#define SUB_COLORS 512

static int is_transparent(uint32_t argb) {
	return ((argb >> 24) & 0xFF) < 127;
}

static int get_cube_index(uint32_t argb) {
	if (is_transparent(argb))
		return TRANSPARENT_CUBE;
	const int r5 = (argb >> 19) & 0x1F;
	const int g5 = (argb >> 11) & 0x1F;
	const int b5 = (argb >> 3) & 0x1F;
	return (r5 << 10) | (g5 << 5) | b5;
}

/* Lower 3 bits of each channel: the sub-color inside the cube (9 bits) */
static int get_sub_color(uint32_t argb) {
	if (is_transparent(argb))
		return 0;
	const int r8 = (argb >> 16) & 0xFF;
	const int g8 = (argb >> 8) & 0xFF;
	const int b8 = argb & 0xFF;
	return ((r8 & 0x07) << 6) | ((g8 & 0x07) << 3) | (b8 & 0x07);
}

static uint32_t representative_argb(int cube_index, int sub512) {
	if (cube_index == TRANSPARENT_CUBE)
		return 0x00000000;
	const uint32_t r8 = (((cube_index >> 10) & 0x1F) << 3) | ((sub512 >> 6) & 0x07);
	const uint32_t g8 = (((cube_index >> 5) & 0x1F) << 3) | ((sub512 >> 3) & 0x07);
	const uint32_t b8 = ((cube_index & 0x1F) << 3) | (sub512 & 0x07);
	return (0xFFu << 24) | (r8 << 16) | (g8 << 8) | b8;
}

int plantuml_quantify555(const uint32_t* pixels, int width, int height, int stride, plantuml_palette_t* result) {
	result->size = 0;
	result->cube_to_palette = malloc(TOTAL_CUBE_SLOTS * sizeof(int16_t));
	/* Frequencies of the sub-colors of at most 256 cubes (Cube555.counts) */
	uint32_t* counts = calloc(256 * SUB_COLORS, sizeof(uint32_t));
	if (result->cube_to_palette == NULL || counts == NULL) {
		free(counts);
		plantuml_palette_free(result);
		return 0;
	}
	/* First the slot of each cube in counts, then its index in the palette */
	int16_t* slots = result->cube_to_palette;
	for (int i = 0; i < TOTAL_CUBE_SLOTS; i++)
		slots[i] = -1;

	int nb_cubes = 0;
	for (int y = 0; y < height; y++) {
		const uint32_t* row = pixels + (size_t) y * stride;
		for (int x = 0; x < width; x++) {
			const uint32_t argb = row[x];
			const int cube = get_cube_index(argb);
			if (slots[cube] < 0) {
				/* Abort if too many distinct cubes are found (same test as Java) */
				if (nb_cubes++ > 255) {
					free(counts);
					plantuml_palette_free(result);
					return 0;
				}
				slots[cube] = (int16_t) (nb_cubes - 1);
			}
			counts[slots[cube] * SUB_COLORS + get_sub_color(argb)]++;
		}
	}
	/* Not enough cubes: risk of over-quantization */
	if (nb_cubes < MIN_CUBES_THRESHOLD) {
		free(counts);
		plantuml_palette_free(result);
		return 0;
	}

	/* The palette, in increasing cube order, each cube with its most frequent
	 * sub-color (the first one on a tie, as Cube555.best) */
	for (int cube = 0; cube < TOTAL_CUBE_SLOTS; cube++) {
		const int slot = slots[cube];
		if (slot < 0)
			continue;
		const uint32_t* c = counts + slot * SUB_COLORS;
		uint32_t best = 0;
		int best_index = 0;
		for (int i = 0; i < SUB_COLORS; i++)
			if (c[i] > best) {
				best = c[i];
				best_index = i;
			}
		result->palette[result->size] = representative_argb(cube, best_index);
		slots[cube] = (int16_t) result->size++;
	}
	free(counts);
	return 1;
}

int plantuml_quantify555_index(const plantuml_palette_t* palette, uint32_t argb) {
	return palette->cube_to_palette[get_cube_index(argb)];
}

void plantuml_palette_free(plantuml_palette_t* palette) {
	free(palette->cube_to_palette);
	palette->cube_to_palette = NULL;
}

uint32_t plantuml_compress_packed_argb(uint32_t argb) {
	return (argb & 0xFEFEFEFEu) | ((argb >> 7) & 0x01010101u);
}

/* ---------------------------------------------------------------------- */
/* PNG writing                                                            */
/* ---------------------------------------------------------------------- */

typedef struct {
	unsigned char* data;
	size_t size;
	size_t capacity;
	int failed;
} buffer_t;

static void append(buffer_t* b, const void* data, size_t size) {
	if (b->failed)
		return;
	if (b->size + size > b->capacity) {
		size_t capacity = b->capacity == 0 ? 64 * 1024 : b->capacity;
		while (capacity < b->size + size)
			capacity *= 2;
		unsigned char* grown = realloc(b->data, capacity);
		if (grown == NULL) {
			b->failed = 1;
			return;
		}
		b->data = grown;
		b->capacity = capacity;
	}
	memcpy(b->data + b->size, data, size);
	b->size += size;
}

static void append_int(buffer_t* b, uint32_t value) {
	const unsigned char bytes[4] = { (unsigned char) (value >> 24), (unsigned char) (value >> 16),
			(unsigned char) (value >> 8), (unsigned char) value };
	append(b, bytes, 4);
}

static void append_chunk(buffer_t* b, const char* type, const unsigned char* data, size_t size) {
	append_int(b, (uint32_t) size);
	append(b, type, 4);
	if (size > 0)
		append(b, data, size);
	mz_ulong crc = mz_crc32(MZ_CRC32_INIT, (const unsigned char*) type, 4);
	if (size > 0)
		crc = mz_crc32(crc, data, size);
	append_int(b, (uint32_t) crc);
}

static mz_bool put_compressed(const void* data, int len, void* user) {
	buffer_t* b = user;
	append(b, data, (size_t) len);
	return b->failed == 0;
}

/* Adaptive filtering, as ImageIO does for non indexed images (with the usual
 * heuristic of libpng): the filter whose bytes, read as signed, have the
 * smallest sum of absolute values (none, sub, up, average, Paeth) */
static int paeth(int a, int b, int c) {
	const int p = a + b - c;
	const int pa = abs(p - a);
	const int pb = abs(p - b);
	const int pc = abs(p - c);
	if (pa <= pb && pa <= pc)
		return a;
	if (pb <= pc)
		return b;
	return c;
}

static void filter_row(const unsigned char* row, const unsigned char* previous, int length, int bpp,
		unsigned char* candidates[5], unsigned char** best_row, int* best_type) {
	long best_cost = -1;
	for (int type = 0; type < 5; type++) {
		unsigned char* out = candidates[type];
		long cost = 0;
		for (int i = 0; i < length; i++) {
			const int raw = row[i];
			const int left = i >= bpp ? row[i - bpp] : 0;
			const int up = previous[i];
			const int up_left = i >= bpp ? previous[i - bpp] : 0;
			int value;
			switch (type) {
			case 0:
				value = raw;
				break;
			case 1:
				value = raw - left;
				break;
			case 2:
				value = raw - up;
				break;
			case 3:
				value = raw - ((left + up) >> 1);
				break;
			default:
				value = raw - paeth(left, up, up_left);
				break;
			}
			out[i] = (unsigned char) value;
			const int signed_value = (signed char) out[i];
			cost += signed_value < 0 ? -signed_value : signed_value;
		}
		if (best_cost < 0 || cost < best_cost) {
			best_cost = cost;
			*best_row = out;
			*best_type = type;
		}
	}
}

/* Premultiplied to straight alpha, with the rounding of plutovg's own PNG
 * writer; an opaque image is an RGB one in PlantUML: alpha forced to 255 */
static void unpremultiply(uint32_t* pixels, int width, int height, int stride_pixels, int opaque) {
	for (int y = 0; y < height; y++) {
		uint32_t* row = pixels + (size_t) y * stride_pixels;
		for (int x = 0; x < width; x++) {
			const uint32_t pixel = row[x];
			uint32_t a = (pixel >> 24) & 0xFF;
			uint32_t r = (pixel >> 16) & 0xFF;
			uint32_t g = (pixel >> 8) & 0xFF;
			uint32_t b = pixel & 0xFF;
			if (a == 0) {
				r = g = b = 0;
			} else if (a != 255) {
				r = (r * 255) / a;
				g = (g * 255) / a;
				b = (b * 255) / a;
			}
			if (opaque)
				a = 255;
			row[x] = (a << 24) | (r << 16) | (g << 8) | b;
		}
	}
}

int plantuml_png_encode(uint32_t* pixels, int width, int height, int stride, int opaque, unsigned char** png,
		size_t* png_size) {
	const int stride_pixels = stride / 4;
	unpremultiply(pixels, width, height, stride_pixels, opaque);

	/* As PngIO: a palette only for an opaque image, QuantifyPacked28 otherwise */
	plantuml_palette_t palette;
	palette.cube_to_palette = NULL;
	const int indexed = opaque && plantuml_quantify555(pixels, width, height, stride_pixels, &palette);

	const int bpp = indexed ? 1 : opaque ? 3 : 4;
	const int length = width * bpp;
	unsigned char* rows = calloc(7, (size_t) length + 1);
	tdefl_compressor* compressor = tdefl_compressor_alloc();
	buffer_t out = { NULL, 0, 0, 0 };
	if (rows == NULL || compressor == NULL) {
		free(rows);
		tdefl_compressor_free(compressor);
		plantuml_palette_free(&palette);
		return -1;
	}
	unsigned char* row = rows;
	unsigned char* previous = rows + length + 1;
	unsigned char* candidates[5];
	for (int i = 0; i < 5; i++)
		candidates[i] = rows + (size_t) (i + 2) * (length + 1) + 1;

	static const unsigned char signature[8] = { 0x89, 'P', 'N', 'G', '\r', '\n', 0x1a, '\n' };
	append(&out, signature, 8);
	unsigned char header[13];
	header[0] = (unsigned char) (width >> 24);
	header[1] = (unsigned char) (width >> 16);
	header[2] = (unsigned char) (width >> 8);
	header[3] = (unsigned char) width;
	header[4] = (unsigned char) (height >> 24);
	header[5] = (unsigned char) (height >> 16);
	header[6] = (unsigned char) (height >> 8);
	header[7] = (unsigned char) height;
	header[8] = 8; /* bit depth */
	header[9] = indexed ? 3 : opaque ? 2 : 6; /* palette, RGB, RGBA */
	header[10] = 0;
	header[11] = 0;
	header[12] = 0;
	append_chunk(&out, "IHDR", header, 13);

	if (indexed) {
		unsigned char plte[256 * 3];
		unsigned char trns[256];
		int last_transparent = -1;
		for (int i = 0; i < palette.size; i++) {
			const uint32_t argb = palette.palette[i];
			plte[i * 3] = (unsigned char) (argb >> 16);
			plte[i * 3 + 1] = (unsigned char) (argb >> 8);
			plte[i * 3 + 2] = (unsigned char) argb;
			trns[i] = (unsigned char) (argb >> 24);
			if (trns[i] != 255)
				last_transparent = i;
		}
		append_chunk(&out, "PLTE", plte, (size_t) palette.size * 3);
		if (last_transparent >= 0)
			append_chunk(&out, "tRNS", trns, (size_t) last_transparent + 1);
	}

	/* IDAT written in place: length patched once compressed */
	const size_t idat_start = out.size;
	append_int(&out, 0);
	append(&out, "IDAT", 4);
	/* Deflate level 6: ImageIO's level for PngIO's quality */
	tdefl_init(compressor, put_compressed, &out, (int) tdefl_create_comp_flags_from_zip_params(6, 15, 0));

	int status = 0;
	for (int y = 0; y < height && status == 0; y++) {
		const uint32_t* source = pixels + (size_t) y * stride_pixels;
		for (int x = 0; x < width; x++) {
			const uint32_t argb = source[x];
			if (indexed) {
				row[x] = (unsigned char) plantuml_quantify555_index(&palette, argb);
				continue;
			}
			const uint32_t packed = plantuml_compress_packed_argb(argb);
			unsigned char* p = row + (size_t) x * bpp;
			p[0] = (unsigned char) (packed >> 16);
			p[1] = (unsigned char) (packed >> 8);
			p[2] = (unsigned char) packed;
			if (bpp == 4)
				p[3] = (unsigned char) (packed >> 24);
		}
		unsigned char* filtered = row;
		int filter = 0;
		if (indexed == 0)
			filter_row(row, previous, length, bpp, candidates, &filtered, &filter);
		const unsigned char type = (unsigned char) filter;
		if (tdefl_compress_buffer(compressor, &type, 1, TDEFL_NO_FLUSH) < 0
				|| tdefl_compress_buffer(compressor, filtered, (size_t) length, TDEFL_NO_FLUSH) < 0)
			status = -1;
		/* The unfiltered row is the previous one of the next row */
		unsigned char* swap = previous;
		previous = row;
		row = swap;
	}
	if (status == 0 && tdefl_compress_buffer(compressor, NULL, 0, TDEFL_FINISH) != TDEFL_STATUS_DONE)
		status = -1;

	if (status == 0 && out.failed == 0) {
		const size_t idat_size = out.size - idat_start - 8;
		out.data[idat_start] = (unsigned char) (idat_size >> 24);
		out.data[idat_start + 1] = (unsigned char) (idat_size >> 16);
		out.data[idat_start + 2] = (unsigned char) (idat_size >> 8);
		out.data[idat_start + 3] = (unsigned char) idat_size;
		const mz_ulong crc = mz_crc32(MZ_CRC32_INIT, out.data + idat_start + 4, idat_size + 4);
		append_int(&out, (uint32_t) crc);
		append_chunk(&out, "IEND", NULL, 0);
	}

	free(rows);
	tdefl_compressor_free(compressor);
	plantuml_palette_free(&palette);
	if (status != 0 || out.failed) {
		free(out.data);
		return -1;
	}
	*png = out.data;
	*png_size = out.size;
	return 0;
}
