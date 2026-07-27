package org.teavm.jso;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Compile-only stub of TeaVM's {@code @JSExport}, which marks a static method
 * as exported to the generated JS module. Pure marker here - no behavior
 * (see clide_stubs/teavm/README.md).
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface JSExport {
}
