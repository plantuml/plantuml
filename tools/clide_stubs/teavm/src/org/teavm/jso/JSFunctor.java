package org.teavm.jso;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Compile-only stub of TeaVM's {@code @JSFunctor}, applied to single-method
 * {@code JSObject} interfaces used as JS callback types. Pure marker here
 * (see clide_stubs/teavm/README.md).
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface JSFunctor {
}
