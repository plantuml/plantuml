package com.ctreber.acearth;

import java.util.HashMap;
import java.util.Map;

import com.ctreber.aclib.gui.MOBoolean;
import com.ctreber.aclib.gui.MODouble;
import com.ctreber.aclib.gui.MOEnum;
import com.ctreber.aclib.gui.MOInteger;
import com.ctreber.aclib.gui.MOString;
import com.ctreber.aclib.gui.MonitoredObject;

/**
 * <p>
 * </p>
 * 
 * <p>
 * &copy; 2002 Christian Treber, ct@ctreber.com (06.10.2002)
 * </p>
 * 
 * @author Christian Treber, ct@ctreber.com
 * 
 */
public class Configuration {
	private Map fValues = new HashMap();

	/**
	 * <p>
	 * Item must be added before it can be set or get.
	 * 
	 * @param pID
	 *            Item name.
	 * @param pObject
	 *            Item value container.
	 */
	public void add(String pID, MonitoredObject pObject) {
		fValues.put(pID, pObject);
	}

	public void setString(String pID, String pValue) {
		((MOString) fValues.get(pID)).set(pValue);
	}

	public void setBoolean(String pID, boolean pValue) {
		((MOBoolean) fValues.get(pID)).set(pValue);
	}

	public void setInt(String pID, int pValue) {
		((MOInteger) fValues.get(pID)).set(pValue);
	}

	public void setDouble(String pID, double pValue) {
		((MODouble) fValues.get(pID)).set(pValue);
	}

	public void setEnum(String pID, Object pValue) {
		((MOEnum) fValues.get(pID)).set(pValue);
	}

	public String getString(String pID) {
		return ((MOString) fValues.get(pID)).get();
	}

	public boolean getBoolean(String pID) {
		return ((MOBoolean) fValues.get(pID)).get();
	}

	public int getInt(String pID) {
		return ((MOInteger) fValues.get(pID)).get();
	}

	public double getDouble(String pID) {
		return ((MODouble) fValues.get(pID)).get();
	}

	public boolean is(String pID, Object pValue) {
		return ((MOEnum) fValues.get(pID)).is(pValue);
	}

	public MOBoolean getMOBoolean(String pID) {
		return (MOBoolean) getMO(pID);
	}

	public MOString getMOString(String pID) {
		return (MOString) getMO(pID);
	}

	public MOEnum getMOEnum(String pID) {
		return (MOEnum) getMO(pID);
	}

	public MOInteger getMOInteger(String pID) {
		return (MOInteger) getMO(pID);
	}

	public MODouble getMODouble(String pID) {
		return (MODouble) getMO(pID);
	}

	public MonitoredObject getMO(String pID) {
		final MonitoredObject lMO = (MonitoredObject) fValues.get(pID);
		if (lMO == null) {
			throw new IllegalArgumentException("Unknown conf item '" + pID + "'");
		}

		return lMO;
	}
}
