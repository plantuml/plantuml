#include "plantuml_resources.h"

#include <stdlib.h>
#include <string.h>

#include <brotli/decode.h>

int32_t plantuml_resource_find(const uint16_t* name, int32_t length) {
	for (int32_t i = 0; i < plantuml_resources_count; i++) {
		const char* candidate = plantuml_resources[i].name;
		int32_t j = 0;
		while (j < length && candidate[j] != 0 && (uint16_t)(unsigned char)candidate[j] == name[j])
			j++;
		if (j == length && candidate[j] == 0)
			return i;
	}
	return -1;
}

int32_t plantuml_resource_size(int32_t index) {
	if (index < 0 || index >= plantuml_resources_count)
		return 0;
	return plantuml_resources[index].size;
}

void plantuml_resource_copy(int32_t index, int8_t* dest) {
	if (index < 0 || index >= plantuml_resources_count)
		return;
	memcpy(dest, plantuml_resources[index].data, (size_t)plantuml_resources[index].size);
}

static unsigned char* decoded = NULL;
static size_t decoded_size = 0;

int32_t plantuml_resource_decode_brotli(int32_t index) {
	BrotliDecoderState* state;
	BrotliDecoderResult result;
	size_t capacity;
	size_t available_in;
	const uint8_t* next_in;

	free(decoded);
	decoded = NULL;
	decoded_size = 0;
	if (index < 0 || index >= plantuml_resources_count)
		return -1;

	state = BrotliDecoderCreateInstance(NULL, NULL, NULL);
	if (state == NULL)
		return -1;
	available_in = (size_t)plantuml_resources[index].size;
	next_in = plantuml_resources[index].data;
	/* Usually 4 to 8 times the compressed size */
	capacity = available_in * 6 + 4096;
	decoded = (unsigned char*)malloc(capacity);
	if (decoded == NULL) {
		BrotliDecoderDestroyInstance(state);
		return -1;
	}

	for (;;) {
		size_t available_out = capacity - decoded_size;
		uint8_t* next_out = decoded + decoded_size;
		result = BrotliDecoderDecompressStream(state, &available_in, &next_in, &available_out, &next_out, NULL);
		decoded_size = (size_t)(next_out - decoded);
		if (result == BROTLI_DECODER_RESULT_NEEDS_MORE_OUTPUT) {
			unsigned char* bigger;
			capacity *= 2;
			bigger = (unsigned char*)realloc(decoded, capacity);
			if (bigger == NULL)
				break;
			decoded = bigger;
			continue;
		}
		break;
	}
	BrotliDecoderDestroyInstance(state);
	if (result != BROTLI_DECODER_RESULT_SUCCESS) {
		free(decoded);
		decoded = NULL;
		decoded_size = 0;
		return -1;
	}
	return (int32_t)decoded_size;
}

void plantuml_resource_copy_decoded(int8_t* dest) {
	if (decoded == NULL)
		return;
	memcpy(dest, decoded, decoded_size);
	free(decoded);
	decoded = NULL;
	decoded_size = 0;
}
