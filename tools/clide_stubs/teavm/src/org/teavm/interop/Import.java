package org.teavm.interop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Compile-only stub of TeaVM's {@code @Import}, which binds a {@code native}
 * Java method to an external (C or WebAssembly) function. Pure marker here
 * (see clide_stubs/teavm/README.md).
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface Import {
	String module() default "";

	String name();
}
