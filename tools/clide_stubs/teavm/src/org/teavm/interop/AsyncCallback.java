package org.teavm.interop;

/**
 * Compile-only stub of TeaVM's {@code AsyncCallback}, the callback interface
 * that {@code @Async}-annotated methods receive under the hood. Left as a
 * plain interface (no bodies to stub): nothing in this jar ever hands out a
 * real instance, so these methods can never actually be invoked
 * (see clide_stubs/teavm/README.md).
 */
public interface AsyncCallback<T> {

	void complete(T result);

	void error(Throwable error);

}
