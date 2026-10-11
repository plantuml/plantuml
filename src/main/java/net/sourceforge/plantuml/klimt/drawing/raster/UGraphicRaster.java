/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 *
 * If you like this project or if you find it useful, you can support us at:
 *
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 *
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 *
 */
package net.sourceforge.plantuml.klimt.drawing.raster;

import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.klimt.UClip;
import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.AbstractCommonUGraphic;
import net.sourceforge.plantuml.klimt.drawing.AbstractUGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.shape.DotPath;
import net.sourceforge.plantuml.klimt.shape.UCenteredCharacter;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.klimt.shape.UImage;
import net.sourceforge.plantuml.klimt.shape.UImageSvg;
import net.sourceforge.plantuml.klimt.shape.UImageTikz;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.UPixel;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.shape.UText;

/**
 * PNG output without AWT: the drivers of {@code UGraphicG2d}, drawing on a
 * {@link RasterCanvas} instead of a {@code Graphics2D} (the TeaVM C build has
 * no AWT).
 *
 * <p>
 * Not supported yet: shadows, patterns, images, circled characters, links
 * (image map).
 */
public class UGraphicRaster extends AbstractUGraphic<RasterCanvas> {

	private final double dpiFactor;

	@Override
	protected AbstractCommonUGraphic copyUGraphic() {
		return new UGraphicRaster(this);
	}

	private UGraphicRaster(UGraphicRaster other) {
		super(other.getStringBounder());
		copy(other);
		this.dpiFactor = other.dpiFactor;
	}

	public UGraphicRaster(HColor defaultBackground, ColorMapper colorMapper, StringBounder stringBounder,
			RasterCanvas canvas, double dpiFactor) {
		super(stringBounder);
		copy(defaultBackground, colorMapper, canvas);
		this.dpiFactor = dpiFactor;
		if (dpiFactor != 1.0)
			canvas.scale(dpiFactor);
		register();
	}

	private void register() {
		registerDriver(URectangle.class, new DriverRectangleRaster());
		registerDriver(UText.class, new DriverTextRaster(getStringBounder()));
		registerDriver(ULine.class, new DriverLineRaster());
		registerDriver(UPixel.class, new DriverPixelRaster());
		registerDriver(UPolygon.class, new DriverPolygonRaster());
		registerDriver(UEllipse.class, new DriverEllipseRaster());
		registerDriver(DotPath.class, new DriverDotPathRaster());
		registerDriver(UPath.class, new DriverPathRaster());
		ignoreShape(UImage.class);
		ignoreShape(UImageSvg.class);
		ignoreShape(UImageTikz.class);
		ignoreShape(UCenteredCharacter.class);
	}

	@Override
	protected void beforeDraw() {
		super.beforeDraw();
		final UClip clip = getClip();
		if (clip == null)
			getGraphicObject().resetClip();
		else
			getGraphicObject().setClip(clip.getX(), clip.getY(), clip.getWidth(), clip.getHeight());
	}

	@Override
	public void writeToStream(OutputStream os, String metadata, int dpi) throws IOException {
		final RasterCanvas canvas = getGraphicObject();
		try {
			os.write(PngChunks.addPlantUmlChunks(canvas.toPng(), metadata, dpi));
		} finally {
			canvas.dispose();
		}
	}

	@Override
	public double dpiFactor() {
		return dpiFactor;
	}

}
