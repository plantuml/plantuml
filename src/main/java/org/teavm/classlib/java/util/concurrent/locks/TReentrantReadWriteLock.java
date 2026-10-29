/*
 * TeaVM C build only (PDF through OpenPDF): minimal stand-in for java.util.concurrent.locks.ReentrantReadWriteLock.
 * TeaVM compiles every reachable method, so classes referenced by OpenPDF must
 * exist even if never executed. Nothing here is meant to work at runtime.
 * Mapped by TeaVM's classlib naming rule (org.teavm.classlib.<package>.T<Name>).
 * See src/teavm-c/README.md.
 */
// ::remove file when JAVA8
package org.teavm.classlib.java.util.concurrent.locks;

public class TReentrantReadWriteLock implements TReadWriteLock {
	private final TLock lock = new TReentrantLock();

	@Override
	public TLock readLock() {
		return lock;
	}

	@Override
	public TLock writeLock() {
		return lock;
	}
}
