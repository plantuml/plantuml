/*
 * Implementation of plantuml_fonts.h.
 */
#include "plantuml_fonts.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#ifdef _WIN32
#include <windows.h>
#define LIST_SEPARATOR ';'
#else
#include <dirent.h>
#include <sys/stat.h>
#define LIST_SEPARATOR ':'
#endif

/* Limits: a machine can have thousands of fonts, and a font can be big */
#define MAX_FILES 20000
#define MAX_DEPTH 6
#define MAX_FILE_SIZE (100L * 1024 * 1024)
#define LOAD_BUDGET (192L * 1024 * 1024)

/* The fonts of PLANTUML_FONT_DIRS come before all the others */
#define CUSTOM_BONUS 100000
/* Bold, light, narrow... fonts come after the regular ones */
#define STYLE_PENALTY 1000

enum { FILE_NEW, FILE_LOADED, FILE_UNUSABLE };

typedef struct {
	char* path;
	long size;
	int rank;
	int state;
	plutovg_font_face_t* face;
} font_file_t;

static font_file_t* files = NULL;
static int file_count = 0;
static int file_capacity = 0;
static int scanned = 0;
static long loaded_bytes = 0;

/*
 * Fonts to try first, by the name of their file (lowercase letters and digits
 * only, without extension): the ones that cover the most. The other fonts come
 * after them, the ones that are not for text last.
 */
static const char* const PREFERRED[] = { "notosanscjk", "sourcehansans", "notosanssc", "notosansjp", "notosanskr",
		"notosanstc", "notosanshk", "notosans", "notosansarabic", "notosanssymbols", "notosansmath", "dejavusans",
		"freesans", "arialuni", "arial", "msyh", "msjh", "yugoth", "meiryo", "msgothic", "malgun", "simsun",
		"segoeui", "seguisym", "pingfang", "hiragino", "applegothic", "stheiti", "applesymbols", "wqy",
		"droidsansfallback", "ipag", "vlgothic", "unifont", NULL };

/* Never worth reading: color bitmaps (drawn blank), the font whose glyphs are all boxes */
static const char* const SKIPPED[] = { "coloremoji", "seguiemj", "lastresort", NULL };

static const char* const STYLES[] = { "bold", "italic", "oblique", "light", "thin", "black", "medium", "condensed",
		"extra", "semi", "demi", "heavy", "narrow", "mono", "display", NULL };

static char* duplicate(const char* s) {
	const size_t n = strlen(s) + 1;
	char* result = (char*) malloc(n);
	if (result != NULL)
		memcpy(result, s, n);
	return result;
}

static char lower(char c) {
	return (c >= 'A' && c <= 'Z') ? (char) (c - 'A' + 'a') : c;
}

static int has_font_extension(const char* name) {
	const char* dot = strrchr(name, '.');
	if (dot == NULL)
		return 0;
	char ext[5];
	size_t i = 0;
	for (dot++; *dot != '\0' && i < 4; dot++, i++)
		ext[i] = lower(*dot);
	if (*dot != '\0')
		return 0;
	ext[i] = '\0';
	return strcmp(ext, "ttf") == 0 || strcmp(ext, "otf") == 0 || strcmp(ext, "ttc") == 0 || strcmp(ext, "otc") == 0;
}

static int contains_any(const char* key, const char* const* words) {
	for (int i = 0; words[i] != NULL; i++)
		if (strstr(key, words[i]) != NULL)
			return 1;
	return 0;
}

/* The rank of a font, lower is tried first; -1 for a font that is skipped */
static int compute_rank(const char* name) {
	char key[256];
	size_t n = 0;
	for (const char* p = name; *p != '\0' && n < sizeof(key) - 1; p++) {
		if (*p == '.' && has_font_extension(p))
			break;
		const char c = lower(*p);
		if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9'))
			key[n++] = c;
	}
	key[n] = '\0';
	if (contains_any(key, SKIPPED))
		return -1;
	int rank = 0;
	while (PREFERRED[rank] != NULL && strstr(key, PREFERRED[rank]) == NULL)
		rank++;
	if (contains_any(key, STYLES))
		rank += STYLE_PENALTY;
	return rank;
}

static void add_file(const char* path, const char* name, long size, int bonus) {
	if (file_count >= MAX_FILES || size <= 0 || size > MAX_FILE_SIZE || has_font_extension(name) == 0)
		return;
	const int rank = compute_rank(name);
	if (rank < 0)
		return;
	if (file_count == file_capacity) {
		const int capacity = file_capacity == 0 ? 256 : file_capacity * 2;
		font_file_t* grown = (font_file_t*) realloc(files, (size_t) capacity * sizeof(font_file_t));
		if (grown == NULL)
			return;
		files = grown;
		file_capacity = capacity;
	}
	font_file_t* f = &files[file_count];
	f->path = duplicate(path);
	if (f->path == NULL)
		return;
	f->size = size;
	f->rank = rank - bonus;
	f->state = FILE_NEW;
	f->face = NULL;
	file_count++;
}

static void scan_dir(const char* dir, int depth, int bonus) {
	if (depth > MAX_DEPTH)
		return;
	char path[2048];
#ifdef _WIN32
	WIN32_FIND_DATAA data;
	if (snprintf(path, sizeof(path), "%s\\*", dir) >= (int) sizeof(path))
		return;
	HANDLE handle = FindFirstFileA(path, &data);
	if (handle == INVALID_HANDLE_VALUE)
		return;
	do {
		if (data.cFileName[0] == '.')
			continue;
		if (snprintf(path, sizeof(path), "%s\\%s", dir, data.cFileName) >= (int) sizeof(path))
			continue;
		if (data.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY)
			scan_dir(path, depth + 1, bonus);
		else
			add_file(path, data.cFileName, (long) data.nFileSizeLow, bonus);
	} while (FindNextFileA(handle, &data));
	FindClose(handle);
#else
	DIR* d = opendir(dir);
	if (d == NULL)
		return;
	struct dirent* entry;
	while ((entry = readdir(d)) != NULL) {
		if (entry->d_name[0] == '.')
			continue;
		if (snprintf(path, sizeof(path), "%s/%s", dir, entry->d_name) >= (int) sizeof(path))
			continue;
		struct stat info;
		if (stat(path, &info) != 0)
			continue;
		if (S_ISDIR(info.st_mode))
			scan_dir(path, depth + 1, bonus);
		else if (S_ISREG(info.st_mode))
			add_file(path, entry->d_name, (long) info.st_size, bonus);
	}
	closedir(d);
#endif
}

static void scan_under(const char* base, const char* suffix, int bonus) {
	char path[2048];
	if (base == NULL || base[0] == '\0')
		return;
	if (snprintf(path, sizeof(path), "%s%s", base, suffix) < (int) sizeof(path))
		scan_dir(path, 0, bonus);
}

static int compare_files(const void* a, const void* b) {
	const font_file_t* fa = (const font_file_t*) a;
	const font_file_t* fb = (const font_file_t*) b;
	if (fa->rank != fb->rank)
		return fa->rank < fb->rank ? -1 : 1;
	/* A total order, so that the same machine always picks the same font */
	return strcmp(fa->path, fb->path);
}

static void ensure_scanned(void) {
	if (scanned)
		return;
	scanned = 1;

	const char* custom = getenv("PLANTUML_FONT_DIRS");
	if (custom != NULL && custom[0] != '\0') {
		char* copy = duplicate(custom);
		char* start = copy;
		while (start != NULL && *start != '\0') {
			char* end = strchr(start, LIST_SEPARATOR);
			if (end != NULL)
				*end = '\0';
			scan_dir(start, 0, CUSTOM_BONUS);
			start = end == NULL ? NULL : end + 1;
		}
		free(copy);
	}

#if defined(_WIN32)
	const char* windir = getenv("WINDIR");
	scan_under(windir != NULL ? windir : "C:\\Windows", "\\Fonts", 0);
	scan_under(getenv("LOCALAPPDATA"), "\\Microsoft\\Windows\\Fonts", 0);
#elif defined(__APPLE__)
	scan_dir("/System/Library/Fonts", 0, 0);
	scan_dir("/Library/Fonts", 0, 0);
	scan_under(getenv("HOME"), "/Library/Fonts", 0);
#else
	scan_dir("/usr/share/fonts", 0, 0);
	scan_dir("/usr/local/share/fonts", 0, 0);
	scan_under(getenv("HOME"), "/.fonts", 0);
	scan_under(getenv("HOME"), "/.local/share/fonts", 0);
#endif

	if (file_count > 1)
		qsort(files, (size_t) file_count, sizeof(font_file_t), compare_files);
}

/* Characters that are never drawn: no need to look for a font */
static int is_ignorable(uint32_t cp) {
	return cp < 0x20 || (cp >= 0x7F && cp < 0xA0) || (cp >= 0x200B && cp <= 0x200F)
			|| (cp >= 0x202A && cp <= 0x202E) || (cp >= 0x2060 && cp <= 0x2064) || (cp >= 0xFE00 && cp <= 0xFE0F)
			|| cp == 0xFEFF || (cp >= 0xE0100 && cp <= 0xE01EF);
}

/* Characters whose glyph is empty on purpose */
static int is_blank(uint32_t cp) {
	return cp == 0x20 || cp == 0xA0 || cp == 0x1680 || (cp >= 0x2000 && cp <= 0x200A) || cp == 0x202F
			|| cp == 0x205F || cp == 0x3000;
}

static int has_outline(plutovg_font_face_t* face, uint32_t cp) {
	const int size = plutovg_font_face_glyph_outline_size(face, cp);
	return size > 0 || (size == 0 && is_blank(cp));
}

int plantuml_face_lacks(plutovg_font_face_t* face, uint32_t codepoint) {
	if (is_ignorable(codepoint))
		return 0;
	return has_outline(face, codepoint) == 0;
}

/* The code points already looked for, with their font (NULL: none has it) */
typedef struct {
	uint32_t codepoint;
	int used;
	plutovg_font_face_t* face;
} cache_entry_t;

static cache_entry_t* cache = NULL;
static size_t cache_capacity = 0;
static size_t cache_count = 0;

static size_t cache_slot(uint32_t codepoint, size_t capacity) {
	return (size_t) ((codepoint * 2654435761u) & (uint32_t) (capacity - 1));
}

static int cache_find(uint32_t codepoint, plutovg_font_face_t** face) {
	if (cache_capacity == 0)
		return 0;
	size_t i = cache_slot(codepoint, cache_capacity);
	while (cache[i].used) {
		if (cache[i].codepoint == codepoint) {
			*face = cache[i].face;
			return 1;
		}
		i = (i + 1) & (cache_capacity - 1);
	}
	return 0;
}

static void cache_insert(cache_entry_t* table, size_t capacity, const cache_entry_t* entry) {
	size_t i = cache_slot(entry->codepoint, capacity);
	while (table[i].used)
		i = (i + 1) & (capacity - 1);
	table[i] = *entry;
}

static void cache_put(uint32_t codepoint, plutovg_font_face_t* face) {
	if ((cache_count + 1) * 2 > cache_capacity) {
		const size_t capacity = cache_capacity == 0 ? 256 : cache_capacity * 2;
		cache_entry_t* grown = (cache_entry_t*) calloc(capacity, sizeof(cache_entry_t));
		if (grown == NULL)
			return;
		for (size_t i = 0; i < cache_capacity; i++)
			if (cache[i].used)
				cache_insert(grown, capacity, &cache[i]);
		free(cache);
		cache = grown;
		cache_capacity = capacity;
	}
	cache_entry_t entry;
	entry.codepoint = codepoint;
	entry.used = 1;
	entry.face = face;
	cache_insert(cache, cache_capacity, &entry);
	cache_count++;
}

/* The face of a file, read the first time only; NULL if it cannot be used */
static plutovg_font_face_t* face_of_file(font_file_t* f) {
	if (f->state == FILE_LOADED)
		return f->face;
	if (f->state == FILE_UNUSABLE || loaded_bytes + f->size > LOAD_BUDGET)
		return NULL;
	f->face = plutovg_font_face_load_from_file(f->path, 0);
	if (f->face == NULL) {
		f->state = FILE_UNUSABLE;
		return NULL;
	}
	f->state = FILE_LOADED;
	loaded_bytes += f->size;
	return f->face;
}

plutovg_font_face_t* plantuml_fallback_face(uint32_t codepoint) {
	plutovg_font_face_t* found = NULL;
	if (cache_find(codepoint, &found))
		return found;

	ensure_scanned();
	for (int i = 0; i < file_count && found == NULL; i++) {
		plutovg_font_face_t* face = face_of_file(&files[i]);
		if (face != NULL && has_outline(face, codepoint))
			found = face;
	}
	cache_put(codepoint, found);
	return found;
}
