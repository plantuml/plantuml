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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.OptionFlags;

public class DrawFile {

	private static final Map<Object, DrawFile> cache = new HashMap<Object, DrawFile>();
	private static final Collection<DrawFile> toDelete = new HashSet<DrawFile>();

	private final LazyFile png2;
	private final LazyCached<String> svg2;
	private final LazyFile eps2;
	private final String toStringValue;
	private final boolean cached;

	private final AtomicInteger useCounter = new AtomicInteger(0);
	private final AtomicInteger totalUse = new AtomicInteger(0);
	private final AtomicBoolean useable = new AtomicBoolean(true);

	private int widthPng = -1;
	private int heightPng = -1;

	static {
		addHook();
	}

	public static boolean isCacheClean() {
		synchronized (toDelete) {
			for (DrawFile f : toDelete) {
				if (f.useCounter.get() != 0) {
					Log.error("Remaining " + f);
					return false;
				}
			}
			for (DrawFile f : cache.values()) {
				if (f.useCounter.get() != 0) {
					Log.error("Remaining " + f);
					return false;
				}
			}
		}
		return true;
	}

	public static DrawFile create(Lazy<File> png, Lazy<String> svg, Lazy<File> eps, Object signature) {
		DrawFile result = null;
		synchronized (toDelete) {
			if (signature != null) {
				result = cache.get(signature);
			}
			if (result == null) {
				result = new DrawFile(png, svg, eps, signature == null ? null : signature.toString(), signature != null);
				if (signature != null) {
					cache.put(signature, result);
					Log.info("DrawFile cache size = " + cache.size());
				}
				deleteOnExit(result);
			}
			result.useCounter.addAndGet(1);
			result.totalUse.addAndGet(1);
			// checkCacheSize();
		}
		return result;
	}

	private static void checkCacheSize() {
		int min = Integer.MAX_VALUE;
		for (DrawFile f : cache.values()) {
			final int score = f.totalUse.get();
			if (score < min) {
				min = score;
			}
		}
		for (final Iterator<DrawFile> it = cache.values().iterator(); it.hasNext();) {
			final DrawFile f = it.next();
			if (f.useCounter.get() == 0 && f.totalUse.get() == min) {
				it.remove();
			}

		}
	}

	@Override
	public String toString() {
		if (toStringValue == null) {
			return super.toString() + " " + useCounter.get() + " " + totalUse.get();
		}
		return toStringValue + " " + useCounter.get() + " " + totalUse.get();
	}

	public static DrawFile createFromFile(File fPng, String svg, File fEps) {
		final DrawFile result = new DrawFile(fPng, svg, fEps, fPng.getName(), false);
		result.useCounter.addAndGet(1);
		deleteOnExit(result);
		return result;
	}

	private static void deleteOnExit(DrawFile file) {
		synchronized (toDelete) {
			toDelete.add(file);
		}
	}

	private DrawFile(Lazy<File> png, Lazy<String> svg, Lazy<File> eps, String signature, boolean cached) {
		this.png2 = new LazyFile(png);
		this.svg2 = new LazyCached<String>(svg);
		this.eps2 = new LazyFile(eps);
		this.toStringValue = signature;
		this.cached = cached;
	}

	private DrawFile(File fPng, String svg, File fEps, String signature, boolean cached) {
		this(new Unlazy<File>(fPng), new Unlazy<String>(svg), new Unlazy<File>(fEps), signature, cached);
		// if (svg.contains("\\")) {
		// System.err.println("svg="+svg);
		// throw new IllegalArgumentException();
		// }
	}

	public File getPngOrEps(FileFormat format) throws IOException {
		checkUseable();
		if (format.isEps()) {
			if (eps2 == null) {
				throw new UnsupportedOperationException("No eps for " + getPng().getAbsolutePath());
			}
			return getEps();
		}
		return getPng();
	}

	private void checkUseable() {
		if (useable.get() == false) {
			throw new IllegalStateException("Useable false");
		}
	}

	public synchronized File getPng() throws IOException {
		checkUseable();
		return png2.getNow();
	}

	public synchronized String getSvg() throws IOException {
		checkUseable();
		return svg2.getNow();
	}

	public synchronized File getEps() throws IOException {
		checkUseable();
		return eps2.getNow();
	}

	private synchronized void initSize() throws IOException {
		checkUseable();
		final BufferedImage im = ImageIO.read(getPng());
		widthPng = im.getWidth();
		heightPng = im.getHeight();
	}

	public synchronized void deleteDrawFile() {
		if (useable.get() == false) {
			return;
		}
		// checkUseable();
		final int count = useCounter.addAndGet(-1);
		if (count == 0) {
			if (cached && isCacheTooBig() == false) {
				return;
			}
			synchronized (toDelete) {
				deleteNow();
				if (cached) {
					final boolean removedCache = cache.values().remove(this);
					if (removedCache == false) {
						Log.error("Not found in cache " + this);
					}
				}
				final boolean removedToDelete = toDelete.remove(this);
				if (removedToDelete == false) {
					Log.error("Not found in delete list " + this);
				}
			}
		}
	}

	private static boolean isCacheTooBig() {
		return true;
	}

	private synchronized void deleteNow() {
		useable.set(false);
		if (png2 != null && png2.isLoaded()) {
			try {
				Log.info("Deleting temporary file " + png2.getNow());
				final boolean ok = png2.getNow().delete();
				if (ok == false) {
					Log.error("Cannot delete: " + png2.getNow());
				}
			} catch (IOException e) {
				e.printStackTrace();
				Log.error("Problem deleting PNG file");
			}
		}
		if (eps2 != null && eps2.isLoaded()) {
			try {
				Log.info("Deleting temporary file " + eps2.getNow());
				final boolean ok2 = eps2.getNow().delete();
				if (ok2 == false) {
					Log.error("Cannot delete: " + eps2.getNow());
				}
			} catch (IOException e) {
				e.printStackTrace();
				Log.error("Problem deleting EPS file");
			}
		}
	}

	public final int getWidthPng() throws IOException {
		checkUseable();
		if (widthPng == -1) {
			initSize();
		}
		return widthPng;
	}

	public final int getHeightPng() throws IOException {
		checkUseable();
		if (widthPng == -1) {
			initSize();
		}
		return heightPng;
	}

	private static void addHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				if (OptionFlags.getInstance().isKeepTmpFiles() == false) {
					synchronized (toDelete) {
						for (DrawFile f : toDelete) {
							final int cnt = f.useCounter.get();
							if (cnt != 0) {
								Log.info("Warning: useCounter " + cnt + " for " + f);
							}
							f.deleteNow();
						}
					}
				}
			}
		});
	}

}
