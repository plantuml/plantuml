/*
 * Fallback to the fonts of the machine, for the characters the embedded font
 * (Liberation Sans) has no glyph for: Chinese, Japanese, Korean, symbols...
 *
 * The embedded font always draws what it can, so the output of the usual Latin
 * text does not depend on the machine. A system font is only used for the
 * code points the embedded font lacks, and only when one is installed (the
 * missing glyph box is drawn otherwise).
 *
 * Fonts are looked for in the usual folders of the system (and in the folders
 * of the PLANTUML_FONT_DIRS environment variable, separated by ':' or by ';' on
 * Windows, which come first). Each font file is read at most once, in an order
 * that puts the fonts with the widest coverage (Noto Sans CJK...) first.
 */
#ifndef PLANTUML_FONTS_H
#define PLANTUML_FONTS_H

#include <stdint.h>

#include <plutovg.h>

#ifdef __cplusplus
extern "C" {
#endif

/* Whether the face has no usable glyph for the code point, and a system font
 * should be looked for. Never for the characters that are not drawn (controls,
 * zero width marks, variation selectors) */
int plantuml_face_lacks(plutovg_font_face_t* face, uint32_t codepoint);

/* A system font with an outline for the code point, or NULL. The faces belong
 * to this module for the life of the process. */
plutovg_font_face_t* plantuml_fallback_face(uint32_t codepoint);

#ifdef __cplusplus
}
#endif

#endif
