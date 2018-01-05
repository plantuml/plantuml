package com.ctreber.acearth.renderer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ctreber.acearth.shader.Shader;

/**
 * <p>
 * Uses defined RowTypeRenderers and Shader to render to render target.
 * 
 * <p>
 * &copy; 2002 Christian Treber, ct@ctreber.com
 * 
 * @author Christian Treber, ct@ctreber.com
 * 
 */
public class Renderer {
	private Shader fShader;
	private RenderTarget fRenderTarget;
	private List fRowTypeRenderers = new ArrayList();

	public Renderer(RenderTarget pRenderTarget) {
		fRenderTarget = pRenderTarget;
	}

	public void render() {
		final Iterator lIt = fRowTypeRenderers.iterator();
		while (lIt.hasNext()) {
			RowTypeRenderer lRowRenderer = (RowTypeRenderer) lIt.next();
			lRowRenderer.startNewRun();
		}
		renderRows();
	}

	private void renderRows() {
		for (int lRowNo = 0; lRowNo < fRenderTarget.getImageHeight(); lRowNo++) {
			int[] lPixelTypes = getPixelTypes(lRowNo);
			renderRow(lRowNo, lPixelTypes);
		}
	}

	/**
	 * <p>
	 * Get pixel types for whole row from all registered RowRenderers.
	 * 
	 * @param pRowNo
	 *            Row number.
	 * @return Pixel types for row.
	 */
	private int[] getPixelTypes(int pRowNo) {
		// Create the types array
		final int[] lPixelTypes = new int[fRenderTarget.getImageWidth()];

		final Iterator lIt = fRowTypeRenderers.iterator();
		while (lIt.hasNext()) {
			RowTypeRenderer lRowRenderer = (RowTypeRenderer) lIt.next();
			lRowRenderer.getPixelTypes(pRowNo, lPixelTypes);
		}

		return lPixelTypes;
	}

	/**
	 * <p>
	 * With help of Shader, render pixel types to actual colored pixels.
	 * 
	 * @param pRowNo
	 * @param pPixelTypes
	 */
	private void renderRow(int pRowNo, int[] pPixelTypes) {
		// For each pixel in row, render it.
		final Color[] lPixelColors = fShader.getShadedColors(pRowNo, pPixelTypes);
		for (int lColNo = 0; lColNo < fRenderTarget.getImageWidth(); lColNo++) {
			fRenderTarget.setPixel(lColNo, pRowNo, lPixelColors[lColNo]);
		}
	}

	public void setShader(Shader pShader) {
		fShader = pShader;
	}

	public void setRenderTarget(RenderTarget pRenderTarget) {
		fRenderTarget = pRenderTarget;
	}

	public void addRowTypeRenderer(RowTypeRenderer pRowRenderer) {
		fRowTypeRenderers.add(pRowRenderer);
	}
}
