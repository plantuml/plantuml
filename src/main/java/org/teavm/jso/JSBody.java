package org.teavm.jso;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mock for org.teavm.jso.JSBody.
 * <p>
 * In the real TeaVM runtime, this annotation inlines JavaScript code
 * into the generated output. Here it serves only as a compilation stub
 * so that the main source tree compiles without the TeaVM dependency.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface JSBody {

	String[] params() default {};

	String script();

}
