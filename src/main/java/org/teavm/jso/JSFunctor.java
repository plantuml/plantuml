package org.teavm.jso;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mock for org.teavm.jso.JSFunctor.
 * <p>
 * In the real TeaVM runtime, this annotation marks a functional interface
 * so that TeaVM generates a JavaScript function wrapper. Here it serves
 * only as a compilation stub.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface JSFunctor {

}
