package com.ctreber.acearth.renderer;

/**
 * <p>Renders a row of pixel types.</p>
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com (Nov 11, 2002)</p>
 * @author Christian Treber, ct@ctreber.com
 *
 */
public interface RowTypeRenderer
{
  /**
   * <p>Each time when rendering an image, call startNewRun() first.
   */
  public void startNewRun();

  /**
   * <p>Set pixel type for specified row number. Note some pixel types
   * might be already set. The renderer can build on this information
   * or overwrite it.
   *
   * @param pRowNo
   * @param pPixelTypes
   */
  public void getPixelTypes(int pRowNo, final int[] pPixelTypes);
}
