package com.ctreber.aclib.gui;

import java.util.HashSet;

/**
 * <p>
 * Monitored enumeration value.
 * </p>
 * 
 * <p>
 * &copy; 2002 Christian Treber, ct@ctreber.com
 * </p>
 * 
 * @author Christian Treber, ct@ctreber.com
 * 
 */
public class MOEnum extends MonitoredObject {
	private HashSet fValidValues = new HashSet();
	/**
	 * <p>
	 * null if no value selected
	 */
	private Object fValue;

	public void addValidValue(Object pValue) {
		fValidValues.add(pValue);
	}

	public void set(Object pValue) {
		if (pValue != null) {
			checkValue(pValue);
		}

		fValue = pValue;
		fireValueChanged();
	}

	public Object get() {
		return fValue;
	}

	public boolean is(Object pObject) {
		checkValue(pObject);

		return this.equals(pObject);
	}

	public int hashCode() {
		if (fValue == null) {
			return 0;
		}

		return fValue.hashCode();
	}

	private void checkValue(Object pValue) {
		if (!fValidValues.contains(pValue)) {
			throw new IllegalArgumentException("Illegal enum value '" + pValue + "'");
		}
	}

	public boolean equals(Object obj) {
		if (obj instanceof MOEnum) {
			MOEnum lOther = (MOEnum) obj;
			if (fValue == null) {
				return lOther.fValue == null;
			}

			return fValue.equals(lOther.fValue);
		}

		if (fValue == null) {
			return obj.equals(null);
		}

		return fValue.equals(obj);
	}

	public HashSet getValidValues() {
		return fValidValues;
	}

	public boolean checkRange() {
		return true;
	}
}
