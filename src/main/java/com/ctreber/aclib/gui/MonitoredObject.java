package com.ctreber.aclib.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * </p>
 * 
 * <p>
 * &copy; 2002 Christian Treber, ct@ctreber.com
 * </p>
 * 
 * @author Christian Treber, ct@ctreber.com
 * 
 */
abstract public class MonitoredObject {
	private List fListeners = new ArrayList();

	public void addChangeListener(MOChangeListener pListener) {
		fListeners.add(pListener);
	}

	public void removeChangeListener(MOChangeListener pListener) {
		fListeners.remove(pListener);
	}

	void fireValueChanged() {
		final Iterator lIt = fListeners.iterator();
		while (lIt.hasNext()) {
			MOChangeListener lListener = (MOChangeListener) lIt.next();
			lListener.valueChanged(this);
		}
	}

	/**
	 * <p>
	 * Check value agains (possibly defined) constraints.
	 * 
	 * @return True if value is within range or range is not checked.
	 */
	abstract public boolean checkRange();
}
