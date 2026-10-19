#include "plantuml_resources.h"

#include <string.h>

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
