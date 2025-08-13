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

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
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
import net.sourceforge.plantuml.emoji.SvgNanoParser;
import net.sourceforge.plantuml.klimt.sprite.Sprite;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.preproc.spm.SpmChannel;
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

	private static final ConcurrentMap<String, Stdlib> all = new ConcurrentHashMap<>();

	private final List<Integer> colors = new ArrayList<>();
	private final Map<String, byte[]> puml = new HashMap<>();
	private final Map<String, byte[]> json = new HashMap<>();
	private final Map<String, StdlibSprite> sprites = new HashMap<>();
	private final Map<String, SvgNanoParser> svgs = new HashMap<>();
	private final List<FutureBufferedImage> images = new ArrayList<>();

	private final String name;
	private final Map<String, String> info = new HashMap<String, String>();

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
		synchronized (puml) {
			initMapIfNeeded(puml, SpmChannel.PUML);
			return puml.get(file);
		}
	}

	private byte[] loadJsonResource(String file) throws IOException {
		synchronized (json) {
			initMapIfNeeded(json, SpmChannel.JSON);
			return json.get(file);
		}
	}

	private void initMapIfNeeded(Map<String, byte[]> map, SpmChannel channel) throws IOException {
		if (map.size() == 0) {
			try (InputStream is = getInternalInputStream(channel); DataInputStream dis = new DataInputStream(is)) {
				final int nb = dis.readInt();
				for (int i = 0; i < nb; i++) {
					final String name = dis.readUTF();
					final int len = dis.readInt();
					final byte data[] = FileUtils.readExactly(dis, len);
					map.put(name.toLowerCase(), data);
				}
			}
		}
	}

	public static Stdlib retrieve(final String name) throws IOException {
		return all.computeIfAbsent(name, key -> {
			try {
				final Stdlib result = new Stdlib(key);
				final String link = result.getLinkFromInfo();
				if (link != null)
					return retrieve(link);

				return result;
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		});
	}

	private String getLinkFromInfo() {
		return info.get("link");
	}

	static private int read1byte(InputStream is) throws IOException {
		return is.read() & 0xFF;
	}

	static int read2bytes(InputStream is) throws IOException {
		return (read1byte(is) << 8) + read1byte(is);
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

	public Sprite readSprite(String name) throws IOException {
		synchronized (sprites) {
			if (sprites.size() == 0) {
				try (InputStream is = getInternalInputStream(SpmChannel.SPRITE);
						DataInputStream dis = new DataInputStream(is)) {
					final int nb = dis.readInt();
					for (int i = 0; i < nb; i++) {
						final String spriteName = dis.readUTF();
						final int width = dis.readInt();
						final int height = dis.readInt();

						final int nbLines = (height + 1) / 2;
						final byte data[] = FileUtils.readExactly(dis, width * nbLines);
						final StdlibSprite sprite = new StdlibSprite(width, height, data);
						sprites.put(spriteName, sprite);
					}
				}
			}
			return sprites.get(name);
		}
	}

	public Sprite readSvgSprite(String name) throws IOException {
		synchronized (svgs) {
			if (svgs.size() == 0) {
				try (InputStream is = getInternalInputStream(SpmChannel.SVG);
						DataInputStream dis = new DataInputStream(is)) {
					final int nb = dis.readInt();
					for (int i = 0; i < nb; i++) {
						final String spriteName = dis.readUTF();
						final String svg = dis.readUTF();
						svgs.put(spriteName, new SvgNanoParser(svg));
					}
				}
			}
			return svgs.get(name);
		}

	}

	public FutureBufferedImage readDataImagePng(int num) throws IOException {
		synchronized (images) {
			if (images.size() == 0) {
				try (InputStream is = getInternalInputStream(SpmChannel.IMAGE);
						DataInputStream dis = new DataInputStream(is)) {
					final int nb = dis.readInt();
					readColorTable(dis);
					for (int i = 0; i < nb; i++) {
						final int width = dis.readInt();
						final int height = dis.readInt();
						final byte data[] = FileUtils.readExactly(dis, width * height * 2);
						images.add(new FutureBufferedImage(colors, width, height, data));
					}
				}
			}
			return images.get(num);
		}

	}

	private void readColorTable(InputStream is) throws IOException {
		final int size = read2bytes(is);
		for (int i = 0; i < size; i++) {
			final int alpha = read1byte(is);
			final int red = read1byte(is);
			final int green = read1byte(is);
			final int blue = read1byte(is);
			final int rgb = (alpha << 24) + (red << 16) + (green << 8) + blue;
			colors.add(rgb);
		}
	}

}
