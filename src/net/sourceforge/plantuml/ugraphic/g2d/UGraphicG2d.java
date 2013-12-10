/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 11479 $
 *
 */
package net.sourceforge.plantuml.ugraphic.g2d;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.EnsureVisible;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.png.PngIO;
import net.sourceforge.plantuml.posimo.DotPath;
import net.sourceforge.plantuml.ugraphic.AbstractCommonUGraphic;
import net.sourceforge.plantuml.ugraphic.AbstractUGraphic;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UAntiAliasing;
import net.sourceforge.plantuml.ugraphic.UCenteredCharacter;
import net.sourceforge.plantuml.ugraphic.UChange;
import net.sourceforge.plantuml.ugraphic.UClip;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UPixel;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UText;

public class UGraphicG2d extends AbstractUGraphic<Graphics2D> implements EnsureVisible {

	private BufferedImage bufferedImage;

	private final double dpiFactor;

	private UAntiAliasing antiAliasing = UAntiAliasing.ANTI_ALIASING_ON;

	private/* final */List<Url> urls = new ArrayList<Url>();
	private Set<Url> allUrls = new HashSet<Url>();

	public final Set<Url> getAllUrlsEncountered() {
		return Collections.unmodifiableSet(allUrls);
	}

	@Override
	public UGraphic apply(UChange change) {
		final UGraphicG2d copy = (UGraphicG2d) super.apply(change);
		if (change instanceof UAntiAliasing) {
			copy.antiAliasing = (UAntiAliasing) change;
		}
		return copy;
	}

	@Override
	protected AbstractCommonUGraphic copyUGraphic() {
		return new UGraphicG2d(this);
	}

	private UGraphicG2d(UGraphicG2d other) {
		super(other);
		this.dpiFactor = other.dpiFactor;
		this.bufferedImage = other.bufferedImage;
		this.urls = other.urls;
		this.allUrls = other.allUrls;
		this.antiAliasing = other.antiAliasing;
		register(dpiFactor);
	}

	public UGraphicG2d(ColorMapper colorMapper, Graphics2D g2d, double dpiFactor) {
		this(colorMapper, g2d, dpiFactor, null);

	}

	public UGraphicG2d(ColorMapper colorMapper, Graphics2D g2d, double dpiFactor, AffineTransform affineTransform) {
		super(colorMapper, g2d);
		this.dpiFactor = dpiFactor;
		if (dpiFactor != 1.0) {
			g2d.scale(dpiFactor, dpiFactor);
		}
		if (affineTransform != null) {
			g2d.transform(affineTransform);
		}
		register(dpiFactor);
	}

	private void register(double dpiFactor) {
		registerDriver(URectangle.class, new DriverRectangleG2d(dpiFactor, this));
		registerDriver(UText.class, new DriverTextG2d(this));
		registerDriver(ULine.class, new DriverLineG2d(dpiFactor));
		registerDriver(UPixel.class, new DriverPixelG2d());
		registerDriver(UPolygon.class, new DriverPolygonG2d(dpiFactor, this));
		registerDriver(UEllipse.class, new DriverEllipseG2d(dpiFactor, this));
		registerDriver(UImage.class, new DriverImageG2d());
		registerDriver(DotPath.class, new DriverDotPathG2d(this));
		registerDriver(UPath.class, new DriverPathG2d(dpiFactor));
		registerDriver(UCenteredCharacter.class, new DriverCenteredCharacterG2d());
	}

	public StringBounder getStringBounder() {
		return StringBounderUtils.asStringBounder(getGraphicObject());
	}

	@Override
	protected void beforeDraw() {
		super.beforeDraw();
		applyClip();
		antiAliasing.apply(getGraphicObject());
	}

	private void applyClip() {
		final UClip uclip = getClip();
		if (uclip == null) {
			getGraphicObject().setClip(null);
		} else {
			final Shape clip = new Rectangle2D.Double(uclip.getX(), uclip.getY(), uclip.getWidth(), uclip.getHeight());
			getGraphicObject().setClip(clip);
		}
	}

	protected final double getDpiFactor() {
		return dpiFactor;
	}

	public void startUrl(Url url) {
		urls.add(url);
		allUrls.add(url);
	}

	public void closeAction() {
		urls.remove(urls.size() - 1);
	}

	public void ensureVisible(double x, double y) {
		for (Url u : urls) {
			u.ensureVisible(x, y);
		}
	}

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	public void setBufferedImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
	}

	public Graphics2D getGraphics2D() {
		return getGraphicObject();
	}

	public void writeImage(OutputStream os, String metadata, int dpi) throws IOException {
		final BufferedImage im = getBufferedImage();
		PngIO.write(im, os, metadata, dpi);
	}

}
