/*
 * PNG encoder of the TeaVM C build: the same choices as PlantUML's PngIO on
 * the JVM (net.sourceforge.plantuml.png), so that the files are alike:
 *
 * - an opaque image that fits in at most 256 colors is reduced to a palette
 *   by the algorithm of Quantify555, and written as an indexed PNG;
 * - otherwise each channel loses its least significant bit, replaced by its
 *   most significant one (QuantifyPacked28), and the image is written as RGB
 *   (opaque) or RGBA;
 * - as ImageIO: no filter for an indexed image, adaptive filtering otherwise,
 *   deflate level 6 (miniz). The bytes differ from ImageIO's (other deflate
 *   implementation), not the pixels.
 *
 * The PNG has no metadata: Java adds PlantUML's chunks (PngChunks).
 */
#ifndef PLANTUML_PNG_H
#define PLANTUML_PNG_H

#include <stddef.h>
#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Encodes the pixels, in place turned from premultiplied to straight alpha
 * (pixels: 0xAARRGGBB as plutovg stores them, stride in bytes). opaque: the
 * image has an opaque background (PlantUML then creates an RGB image).
 * On success, returns 0 and *png (to free with free()) holds *png_size bytes.
 */
int plantuml_png_encode(uint32_t* pixels, int width, int height, int stride, int opaque, unsigned char** png,
		size_t* png_size);

/* Quantify555 (exposed for tests) */
typedef struct {
	int size;
	uint32_t palette[256];
	/* Palette index of each RGB555 cube (and of the transparent cube) */
	int16_t* cube_to_palette;
} plantuml_palette_t;

/* Returns 1 and fills the palette when the image fits, 0 otherwise
 * (pixels: straight 0xAARRGGBB, stride in pixels) */
int plantuml_quantify555(const uint32_t* pixels, int width, int height, int stride, plantuml_palette_t* result);
int plantuml_quantify555_index(const plantuml_palette_t* palette, uint32_t argb);
void plantuml_palette_free(plantuml_palette_t* palette);

/* QuantifyPacked28: QuantUtils.compressPackedARGB */
uint32_t plantuml_compress_packed_argb(uint32_t argb);

#ifdef __cplusplus
}
#endif

#endif
