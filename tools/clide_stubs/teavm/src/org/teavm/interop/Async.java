package org.teavm.interop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Compile-only stub of TeaVM's {@code @Async}, which turns an asynchronous JS
 * call into an apparently-synchronous native Java method. Pure marker here
 * (see clide_stubs/teavm/README.md).
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface Async {
}
