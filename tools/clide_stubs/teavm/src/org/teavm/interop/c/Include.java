package org.teavm.interop.c;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Compile-only stub of TeaVM's {@code @Include}, which makes the C backend emit
 * an {@code #include} for the header the annotated class's imports need. Pure
 * marker here (see clide_stubs/teavm/README.md).
 */
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface Include {
	String value();

	boolean isSystem() default true;
}
