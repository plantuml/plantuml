#include "plantuml_stdin.h"

#ifdef _WIN32
#include <fcntl.h>
#include <io.h>
#else
#include <errno.h>
#include <unistd.h>
#endif

int32_t plantuml_stdin_read(int8_t* dest, int32_t length) {
	if (length <= 0)
		return 0;

#ifdef _WIN32
	static int binary = 0;
	int count;
	if (binary == 0) {
		/* No translation of the line endings: Java decodes the bytes itself */
		_setmode(0, _O_BINARY);
		binary = 1;
	}
	count = _read(0, dest, (unsigned int)length);
	if (count < 0)
		return -2;
	return count == 0 ? -1 : (int32_t)count;
#else
	for (;;) {
		ssize_t count = read(0, dest, (size_t)length);
		if (count < 0) {
			if (errno == EINTR)
				continue;
			return -2;
		}
		return count == 0 ? -1 : (int32_t)count;
	}
#endif
}

int32_t plantuml_stdout_write(const int8_t* src, int32_t length) {
#ifdef _WIN32
	static int binary = 0;
	if (binary == 0) {
		_setmode(1, _O_BINARY);
		binary = 1;
	}
#endif
	while (length > 0) {
#ifdef _WIN32
		int count = _write(1, src, (unsigned int)length);
		if (count <= 0)
			return -1;
#else
		ssize_t count = write(1, src, (size_t)length);
		if (count < 0) {
			if (errno == EINTR)
				continue;
			return -1;
		}
#endif
		src += count;
		length -= (int32_t)count;
	}
	return 0;
}
