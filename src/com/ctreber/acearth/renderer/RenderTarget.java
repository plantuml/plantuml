package com.ctreber.acearth.renderer;

import java.awt.Color;

/**
 * <p>.
 * 
 * <p>
 * &copy; 2002 Christian Treber, ct@ctreber.com
 * 
 * @author Christian Treber, ct@ctreber.com
 * 
 */
public interface RenderTarget {

	public void setPixel(int pX, int pY, int pA, int pR, int pG, int pB);

	public void setPixel(int pX, int pY, Color pColor);

	public int getImageWidth();

	public int getImageHeight();

}
