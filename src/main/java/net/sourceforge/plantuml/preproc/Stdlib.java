/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
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
 *
 */
package net.sourceforge.plantuml.preproc;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import net.sourceforge.plantuml.FileUtils;
import net.sourceforge.plantuml.Lazy;
import net.sourceforge.plantuml.emoji.SvgNanoParser;
import net.sourceforge.plantuml.klimt.sprite.Sprite;
import net.sourceforge.plantuml.klimt.sprite.SpriteMonochrome;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.preproc.spm.SpmChannel;
import net.sourceforge.plantuml.preproc.spm.SpmEntry;
import net.sourceforge.plantuml.preproc.spm.SpmImageEntry;
import net.sourceforge.plantuml.preproc.spm.SpmImageTable;
import net.sourceforge.plantuml.preproc.spm.SpmTable;
import net.sourceforge.plantuml.preproc.spm.SpmToc;
import net.sourceforge.plantuml.utils.Log;
// ::uncomment when __CORE__
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import static com.plantuml.api.cheerpj.StaticMemory.cheerpjPath;
// ::done

public class Stdlib {

	// ::uncomment when __CORE__
//	public static InputStream getResourceAsStream(String fullname) {
//			return null;
//	}
	// ::done

	// ::comment when __CORE__
	private static final Map<String, Stdlib> all = new ConcurrentHashMap<String, Stdlib>();

	private final Map<String, SoftReference<byte[]>> cache = new ConcurrentHashMap<>();
	final private List<Integer> colors = new ArrayList<>();

	private final String name;
	private final Map<String, String> info = new HashMap<String, String>();
	private final Lazy<SpmToc> pumlToc;
	private final Lazy<SpmToc> jsonToc;
	private final Lazy<SpmTable> spritesTable;
	private final Lazy<SpmTable> svgTable;
	private final Lazy<SpmImageTable> imagesTable;

	private Stdlib(String name) throws IOException {
		this.name = name;
		try (final InputStream is = getInternalInputStream(SpmChannel.INFO)) {
			final List<String> pathInfo = FileUtils.readStrings(is);
			for (String s : pathInfo)
				if (s.contains("=")) {
					final String data[] = s.split("=");
					if (data.length == 2)
						this.info.put(data[0], data[1]);
				}
		}

		this.pumlToc = new Lazy<>(() -> {
			try (DataInputStream is = new DataInputStream(getInternalInputStream(SpmChannel.TEXT_TOC))) {
				return new SpmToc(is);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}

		});

		this.jsonToc = new Lazy<>(() -> {
			try (DataInputStream is = new DataInputStream(getInternalInputStream(SpmChannel.JSON_TOC))) {
				return new SpmToc(is);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}

		});

		this.spritesTable = new Lazy<>(() -> {
			try (DataInputStream is = new DataInputStream(getInternalInputStream(SpmChannel.SPRITE_TAB))) {
				return new SpmTable(is);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}

		});

		this.svgTable = new Lazy<>(() -> {
			try (DataInputStream is = new DataInputStream(getInternalInputStream(SpmChannel.SVG_TAB))) {
				return new SpmTable(is);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}

		});

		this.imagesTable = new Lazy<>(() -> {
			try (DataInputStream is = new DataInputStream(getInternalInputStream(SpmChannel.IMAGE_TAB))) {
				return new SpmImageTable(is);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}

		});

	}

	public static byte[] getPumlResource(String fullname) {
		fullname = fullname.toLowerCase().replace(".puml", "");
		final int last = fullname.indexOf('/');
		if (last == -1)
			return null;

		try {
			final Stdlib folder = retrieve(fullname.substring(0, last));
			if (folder == null || folder.info.size() == 0)
				return null;

			return folder.loadPumlResource(fullname.substring(last + 1));
		} catch (IOException e) {
			Logme.error(e);
			return null;
		}
	}

	public static byte[] getJsonResource(String fullname) {
		final int last = fullname.indexOf('/');
		if (last == -1)
			return null;

		try {
			final Stdlib folder = retrieve(fullname.substring(0, last));
			if (folder == null || folder.info.size() == 0)
				return null;

			return folder.loadJsonResource(fullname.substring(last + 1));

		} catch (IOException e) {
			Logme.error(e);
			return null;
		}
	}

	private byte[] loadPumlResource(String file) throws IOException {
		final SoftReference<byte[]> ref = cache.get(file);
		byte[] data = (ref != null) ? ref.get() : null;

		if (data != null)
			return data;

		final SpmEntry entry = pumlToc.get().getEntry(file);

		if (entry == null)
			return null;

		try (InputStream is = getInternalInputStream(SpmChannel.TEXT_DAT)) {
			FileUtils.skipExactly(is, entry.getPos());
			data = FileUtils.readExactly(is, entry.getLen());
		}
		cache.put(file, new SoftReference<>(data));
		return data;

	}

	private byte[] loadJsonResource(String file) throws IOException {
		final SpmEntry entry = jsonToc.get().getEntry(file);

		if (entry == null)
			return null;

		try (InputStream is = getInternalInputStream(SpmChannel.JSON_DAT)) {
			FileUtils.skipExactly(is, entry.getPos());
			return FileUtils.readExactly(is, entry.getLen());
		}

	}

	public static Stdlib retrieve(final String name) throws IOException {
		Stdlib result = all.get(name);
		if (result == null) {
			result = new Stdlib(name);
			final String link = result.getLinkFromInfo();
			if (link != null)
				result = retrieve(link);

			all.put(name, result);

		}
		return result;
	}

	private String getLinkFromInfo() {
		return info.get("link");
	}

	private static int read1byte(InputStream is) throws IOException {
		return is.read() & 0xFF;
	}

	private static int read2bytes(InputStream is) throws IOException {
		return (read1byte(is) << 8) + read1byte(is);
	}

	private BufferedImage readOneImage(int width, int height, InputStream is) throws IOException {
		final BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int y = 0; y < height; y += 1)
			for (int x = 0; x < width; x += 1) {
				final int rgb = colors.get(read2bytes(is));
				result.setRGB(x, y, rgb);
			}

		return result;

	}

	private Sprite readSprite(int width, int height, InputStream inputStream) throws IOException {
		final SpriteMonochrome sprite = new SpriteMonochrome(width, height, 16);
		final int nbLines = (height + 1) / 2;

		for (int j = 0; j < nbLines; j++) {

			for (int i = 0; i < width; i++) {
				final int b = inputStream.read();
				final int b1 = (b & 0xF0) >> 4;
				final int b2 = (b & 0x0F);
				sprite.setGray(i, j * 2, b1);
				sprite.setGray(i, j * 2 + 1, b2);

			}
		}

		return sprite;
	}

	private InputStream getInternalInputStream(SpmChannel channel) throws IOException {
		return channel.getInternalInputStream(name);
	}

	public static void extractStdLib() throws IOException {
		for (String name : getAllFolderNames()) {
			final Stdlib folder = Stdlib.retrieve(name);
			folder.extractMeFull();
		}
	}

	public static Collection<String> getAllFolderNames() throws IOException {
		final InputStream home = SpmChannel.inputStream("stdlib/home.spm");
		if (home == null)
			throw new IOException("Cannot access to /stdlib files");

		try (final BufferedReader br = new BufferedReader(new InputStreamReader(home))) {
			return new TreeSet<>(br.lines().collect(Collectors.toList()));
		}
	}

	private void extractMeFull() throws IOException {
//		final DataInputStream dataStream = getDataStream();
//		if (dataStream == null)
//			return;
//
//		dataStream.readUTF();
//		final InputStream spriteStream = getSpriteStream();
//		try {
//			while (true) {
//				final String filename = dataStream.readUTF();
//				if (filename.equals(SEPARATOR))
//					return;
//
//				final SFile f = new SFile("stdlib/" + name + "/" + filename + ".puml");
//				f.getParentFile().mkdirs();
//				final PrintWriter fos = f.createPrintWriter();
//				while (true) {
//					final String s = dataStream.readUTF();
//					if (s.equals(SEPARATOR))
//						break;
//
//					fos.println(s);
//					if (isSpriteLine(s)) {
//						final Matcher m = sizePattern.matcher(s);
//						final boolean ok = m.find();
//						if (ok == false)
//							throw new IOException(s);
//
//						final int width = Integer.parseInt(m.group(1));
//						final int height = Integer.parseInt(m.group(2));
//						final String sprite = readSprite(width, height, spriteStream);
//						fos.println(sprite);
//						fos.println("}");
//					}
//				}
//				fos.close();
//			}
//		} finally {
//			dataStream.close();
//			spriteStream.close();
//		}
	}

//	public Collection<String> getAllFilenamesWithSprites() throws IOException {
//		final Set<String> result = new TreeSet<>();
//
//		dataStream.readUTF();
//		try {
//			while (true) {
//				final String filename = dataStream.readUTF();
//				if (filename.equals(SEPARATOR))
//					return result;
//
//				while (true) {
//					final String s = dataStream.readUTF();
//					if (s.equals(SEPARATOR))
//						break;
//					if (isSpriteLine(s))
//						result.add(filename);
//				}
//			}
//		} finally {
//			dataStream.close();
//		}
//	}

//	public List<String> extractAllSprites() throws IOException {
//		final List<String> result = new ArrayList<>();
//		final DataInputStream dataStream = getDataStream();
//		if (dataStream == null)
//			return Collections.unmodifiableList(result);
//
//		dataStream.readUTF();
//		final InputStream spriteStream = getSpriteStream();
//		try {
//			while (true) {
//				final String filename = dataStream.readUTF();
//				if (filename.equals(SEPARATOR))
//					return Collections.unmodifiableList(result);
//
//				while (true) {
//					final String s = dataStream.readUTF();
//					if (s.equals(SEPARATOR))
//						break;
//
//					if (isSpriteLine(s)) {
//						final Matcher m = sizePattern.matcher(s);
//						final boolean ok = m.find();
//						if (ok == false)
//							throw new IOException(s);
//
//						final int width = Integer.parseInt(m.group(1));
//						final int height = Integer.parseInt(m.group(2));
//						final String sprite = readSprite(width, height, spriteStream);
//						if (s.contains("_LARGE") == false)
//							result.add(s + "\n" + sprite + "}");
//
//					}
//				}
//			}
//		} finally {
//			dataStream.close();
//			spriteStream.close();
//		}
//	}

	public static void addInfoVersion(List<String> strings, boolean details) {
		try {
			for (String name : getAllFolderNames()) {
				final Stdlib folder = Stdlib.retrieve(name);
				if (details) {
					strings.add("<b>" + name);
					strings.add("Version " + folder.getVersion());
					strings.add("Delivered by " + folder.getSource());
					strings.add(" ");
				} else {
					strings.add("* " + name + " (Version " + folder.getVersion() + ")");
				}
			}
		} catch (IOException e) {
			Log.error("Error " + e);
			return;
		}
	}

	public String getVersion() {
		String result = info.get("VERSION");
		if (result == null)
			result = info.get("version");
		return result;
	}

	public String getSource() {
		String result = info.get("SOURCE");
		if (result == null)
			result = info.get("source");
		return result;
	}

	public Map<String, String> getMetadata() {
		return Collections.unmodifiableMap(info);

	}

	public static void printStdLib() {
		final List<String> print = new ArrayList<>();
		addInfoVersion(print, true);
		for (String s : print)
			System.out.println(s.replace("<b>", ""));

	}
	// ::done

	private final ConcurrentMap<Integer, Sprite> cacheSprite = new ConcurrentHashMap<>();

	public Sprite readSprite(int width, int height, int num) {
		final SpmEntry entry = spritesTable.get().getEntry(num);
		if (entry == null)
			return null;

		return cacheSprite.computeIfAbsent(num, k -> {
			try (InputStream data = getInternalInputStream(SpmChannel.SPRITE_DAT)) {
				FileUtils.skipExactly(data, entry.getPos());
				return readSprite(width, height, data);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		});

	}

	public Sprite readSvgSprite(int num) {
		final SpmEntry entry = svgTable.get().getEntry(num);
		if (entry == null)
			return null;

		try (InputStream is = getInternalInputStream(SpmChannel.SVG_DAT)) {
			FileUtils.skipExactly(is, entry.getPos());
			final byte data[] = FileUtils.readExactly(is, entry.getLen());
			final String svg = new String(data, StandardCharsets.UTF_8);
			return new SvgNanoParser(svg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public BufferedImage readDataImagePng(int num) throws IOException {

		if (colors.size() == 0)
			try (InputStream dataImagePngBase64Stream = getInternalInputStream(SpmChannel.IMAGE_COL)) {
				final int size = read2bytes(dataImagePngBase64Stream);
				for (int i = 0; i < size; i++) {
					final int alpha = read1byte(dataImagePngBase64Stream);
					final int red = read1byte(dataImagePngBase64Stream);
					final int green = read1byte(dataImagePngBase64Stream);
					final int blue = read1byte(dataImagePngBase64Stream);
					final int rgb = (alpha << 24) + (red << 16) + (green << 8) + blue;
					colors.add(rgb);
				}
			}

		final SpmImageEntry entry = imagesTable.get().getEntry(num);
		if (entry == null)
			return null;

		try (InputStream data = getInternalInputStream(SpmChannel.IMAGE_DAT)) {
			FileUtils.skipExactly(data, entry.getPos());
			return readOneImage(entry.getWidth(), entry.getHeight(), data);
		}

	}

}
