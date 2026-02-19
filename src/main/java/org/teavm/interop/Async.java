package org.teavm.interop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mock for org.teavm.interop.Async.
 * <p>
 * In the real TeaVM runtime, this annotation marks a native method whose
 * implementation is provided by a companion method accepting an
 * {@link AsyncCallback}. TeaVM transforms the call into an asynchronous
 * JavaScript Promise chain. Here it serves only as a compilation stub.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Async {

}
