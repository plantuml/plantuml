package com.ctreber.acearth.plugins;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.ctreber.acearth.ACearth;
import com.ctreber.acearth.gui.PixelCanvas;
import com.ctreber.acearth.projection.Projection;

/**
 * <p></p>
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com (Nov 6, 2002)</p>
 * @author Christian Treber, ct@ctreber.com
 *
 */
abstract public class Plugin implements ActionListener
{
  protected ACearth fParent;
  protected boolean fActiveP = true;
  protected Projection fProjection;
  protected PixelCanvas fRenderTarget;

  public void actionPerformed(ActionEvent e)
  {
  }

  abstract public boolean hasGUIP();

  abstract public void render();

  public void setProjection(Projection pProjection)
  {
    fProjection = pProjection;
  }

  public void setRenderTarget(PixelCanvas pRenderTarget)
  {
    fRenderTarget = pRenderTarget;
  }

  public void setParent(ACearth pParent)
  {
    fParent = pParent;
  }
}
