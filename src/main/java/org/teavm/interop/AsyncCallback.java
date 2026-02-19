package org.teavm.interop;

/**
 * Mock for org.teavm.interop.AsyncCallback.
 * <p>
 * In the real TeaVM runtime, this interface is used together with {@link Async}
 * to bridge synchronous Java code with asynchronous JavaScript Promises.
 * The {@code complete} method delivers the result and {@code error} delivers
 * a failure. Here it serves only as a compilation stub.
 *
 * @param <T> the type of the asynchronous result
 */
public interface AsyncCallback<T> {

	/**
	 * Called when the asynchronous operation completes successfully.
	 *
	 * @param result the result value
	 */
	void complete(T result);

	/**
	 * Called when the asynchronous operation fails.
	 *
	 * @param e the exception describing the failure
	 */
	void error(Throwable e);

}
