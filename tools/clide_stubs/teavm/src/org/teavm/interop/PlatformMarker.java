package org.teavm.interop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Compile-only stub of TeaVM's {@code @PlatformMarker}, used on a method whose
 * body the TeaVM compiler replaces per target platform (e.g. {@code isTeaVM()}
 * returning a compile-time constant). Pure marker here
 * (see clide_stubs/teavm/README.md).
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface PlatformMarker {
}
