package org.teavm.interop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Compile-only stub of TeaVM's {@code @Unmanaged}, which tells the C backend
 * that a class or method does not touch the garbage-collected heap. Pure
 * marker here (see clide_stubs/teavm/README.md).
 */
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface Unmanaged {
}
