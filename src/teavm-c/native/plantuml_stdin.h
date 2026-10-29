/*
 * Standard input and output, called from Java through TeaVM's @Import (see
 * net.sourceforge.plantuml.teavm.c.NativeStdin).
 *
 * The classlib of the TeaVM C backend has a System.in (ConsoleInputStream), but
 * its read() always throws, and its System.out is a text stream (on Windows it
 * turns \n into \r\n and loses bytes of a PNG): they cannot be used for -pipe.
 */
#ifndef PLANTUML_STDIN_H
#define PLANTUML_STDIN_H

#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

/* Reads up to length bytes from the standard input into dest, blocking until at
 * least one byte is available (like read(2): a pipe gives what it has, it does
 * not wait for length bytes). Returns the number of bytes read, -1 at the end of
 * the input, -2 on error. */
int32_t plantuml_stdin_read(int8_t* dest, int32_t length);

/* Writes length bytes of src to the standard output, as they are: no translation
 * of the line endings (Windows), no conversion of the bytes >= 0x80 (the images
 * of -pipe are binary). Returns 0, or -1 on error. */
int32_t plantuml_stdout_write(const int8_t* src, int32_t length);

#ifdef __cplusplus
}
#endif

#endif
