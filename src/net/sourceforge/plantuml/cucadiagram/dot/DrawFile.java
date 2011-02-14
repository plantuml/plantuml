/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 3977 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.FileUtils;
import net.sourceforge.plantuml.Log;

public class DrawFile {

	private static final Map<Object, DrawFile> cache = new HashMap<Object, DrawFile>();

	private final LazyFile png2;
	private final LazyCached<String> svg2;
	private final LazyFile eps2;

	private int widthPng = -1;
	private int heightPng = -1;

	public static DrawFile create(Lazy<File> png, Lazy<String> svg, Lazy<File> eps, Object signature) {
		DrawFile result = cache.get(signature);
		if (result == null) {
			result = new DrawFile(png, svg, eps);
			cache.put(signature, result);
			Log.info("DrawFile cache size = " + cache.size());
			FileUtils.deleteOnExit(result);
		}
		return result;
	}

	public static DrawFile createFromFile(File fPng, String svg, File fEps) {
		final DrawFile result = new DrawFile(fPng, svg, fEps);
		FileUtils.deleteOnExit(result);
		return result;
	}

	private DrawFile(Lazy<File> png, Lazy<String> svg, Lazy<File> eps) {
		this.png2 = new LazyFile(png);
		this.svg2 = new LazyCached<String>(svg);
		this.eps2 = new LazyFile(eps);
	}

	private DrawFile(File fPng, String svg, File fEps) {
		this(new Unlazy<File>(fPng), new Unlazy<String>(svg), new Unlazy<File>(fEps));
//		if (svg.contains("\\")) {
//			System.err.println("svg="+svg);
//			throw new IllegalArgumentException();
//		}
	}

	public File getPngOrEps(boolean isEps) throws IOException {
		if (isEps) {
			if (eps2 == null) {
				throw new UnsupportedOperationException("No eps for " + getPng().getAbsolutePath());
			}
			return getEps();
		} else {
			return getPng();
		}
	}

	public File getPng() throws IOException {
		return png2.getNow();
	}

	public String getSvg() throws IOException {
		return svg2.getNow();
	}

	public File getEps() throws IOException {
		return eps2.getNow();
	}

	private void initSize() throws IOException {
		final BufferedImage im = ImageIO.read(getPng());
		widthPng = im.getWidth();
		heightPng = im.getHeight();
	}

	public void delete() {
		Thread.yield();
		if (png2 != null && png2.isLoaded()) {
			try {
				Log.info("Deleting temporary file " + getPng());
				final boolean ok = getPng().delete();
				if (ok == false) {
					Log.error("Cannot delete: " + getPng());
				}
			} catch (IOException e) {
				e.printStackTrace();
				Log.error("Problem deleting PNG file");
			}
		}
		if (eps2 != null && eps2.isLoaded()) {
			try {
				Log.info("Deleting temporary file " + getEps());
				final boolean ok2 = getEps().delete();
				if (ok2 == false) {
					Log.error("Cannot delete: " + getEps());
				}
			} catch (IOException e) {
				e.printStackTrace();
				Log.error("Problem deleting EPS file");
			}

		}
	}

	public final int getWidthPng() throws IOException {
		if (widthPng == -1) {
			initSize();
		}
		return widthPng;
	}

	public final int getHeightPng() throws IOException {
		if (widthPng == -1) {
			initSize();
		}
		return heightPng;
	}

	// @Override
	// public String toString() {
	// if (svg == null) {
	// return getPng().toString();
	// }
	// return png + " " + svg.length();
	// }

}
