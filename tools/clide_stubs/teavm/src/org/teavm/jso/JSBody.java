package org.teavm.jso;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Compile-only stub of TeaVM's {@code @JSBody}. The real annotation tells the
 * TeaVM compiler to replace the annotated native method with the given
 * JavaScript snippet; here it is just a shape for javac/jdtls to resolve
 * against - the annotated methods stay {@code native} and are never actually
 * invoked (see clide_stubs/teavm/README.md).
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface JSBody {

	String[] params() default {};

	String script();

}
