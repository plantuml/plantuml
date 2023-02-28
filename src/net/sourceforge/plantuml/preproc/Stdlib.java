package net.sourceforge.plantuml.preproc;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.brotli.BrotliInputStream;
import net.sourceforge.plantuml.klimt.creole.atom.AtomImg;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.utils.Base64Coder;
import net.sourceforge.plantuml.utils.Log;
// ::uncomment when __CORE__
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import static com.plantuml.api.cheerpj.StaticMemory.cheerpjPath;
// ::done

public class Stdlib {

	// ::uncomment when __CORE__
//	public static InputStream getResourceAsStream(String fullname) {
//		fullname = fullname.replace(".puml", "");
//		fullname = fullname.replace("awslib/", "awslib14/");
//
//		final String fullpath = cheerpjPath + "stdlib/" + fullname + ".puml";
//		System.err.println("Trying to read " + fullpath);
//		// See https://docs.leaningtech.com/cheerpj/File-System-support
//		try {
//			return new FileInputStream(fullpath);
//		} catch (FileNotFoundException e) {
//			System.err.println("Cannot load " + fullpath);
//			return null;
//		}
//	}
	// ::done

	// ::comment when __CORE__
	private static final Map<String, Stdlib> all = new ConcurrentHashMap<String, Stdlib>();
	private static final String SEPARATOR = "\uF8FF";
	private static final Pattern sizePattern = Pattern.compile("\\[(\\d+)x(\\d+)/16\\]");

	private final Map<String, SoftReference<String>> cache = new ConcurrentHashMap<String, SoftReference<String>>();

	private final String name;
	private final Map<String, String> info = new HashMap<String, String>();

	private Stdlib(String name, String info) throws IOException {
		this.name = name;
		fillMap(info);
	}

	private void fillMap(String infoString) {
		for (String s : infoString.split("\n"))
			if (s.contains("=")) {
				final String data[] = s.split("=");
				this.info.put(data[0], data[1]);
			}
	}

	public static InputStream getResourceAsStream(String fullname) {
		fullname = fullname.toLowerCase().replace(".puml", "");
		final int last = fullname.indexOf('/');
		if (last == -1)
			return null;

		try {
			final Stdlib folder = retrieve(fullname.substring(0, last));
			if (folder == null || folder.info.size() == 0)
				return null;

			final String data = folder.loadResource(fullname.substring(last + 1));
			if (data == null)
				return null;

			return new ByteArrayInputStream(data.getBytes(UTF_8));
		} catch (IOException e) {
			Logme.error(e);
			return null;
		}
	}

	public static Stdlib retrieve(final String name) throws IOException {
		Stdlib result = all.get(name);
		if (result == null) {
			final DataInputStream dataStream = getDataStream(name);
			if (dataStream == null)
				return null;

			final String info = dataStream.readUTF();
			dataStream.close();

			final String link = getLinkFromInfo(info);
			if (link == null)
				result = new Stdlib(name, info);
			else
				result = retrieve(link);

			all.put(name, result);
		}
		return result;
	}

	private static String getLinkFromInfo(String infoString) {
		for (String s : infoString.split("\n"))
			if (s.contains("=")) {
				final String data[] = s.split("=");
				if (data[0].equals("LINK"))
					return data[1];
			}
		return null;
	}

	private static int read1byte(InputStream is) throws IOException {
		return is.read() & 0xFF;
	}

	private static int read2bytes(InputStream is) throws IOException {
		return (read1byte(is) << 8) + read1byte(is);
	}

	private String loadResource(String file) throws IOException {
		final SoftReference<String> cached = cache.get(file.toLowerCase());
		if (cached != null) {
			final String cachedResult = cached.get();
			if (cachedResult != null) {
				// Log.info("Using cache for " + file);
				return cachedResult;
			}
		}
		Log.info("No cache for " + file);
		final DataInputStream dataStream = getDataStream();
		if (dataStream == null)
			return null;

		dataStream.readUTF();
		final InputStream spriteStream = getSpriteStream();
		if (spriteStream == null) {
			dataStream.close();
			return null;
		}
		InputStream dataImagePngBase64Stream = null;
		final List<Integer> colors = new ArrayList<>();
		try {
			StringBuilder found = null;
			while (true) {
				final String filename = dataStream.readUTF();
				if (filename.equals(SEPARATOR)) {
					Log.info("Not found " + filename);
					return null;
				}
				if (filename.equalsIgnoreCase(file))
					found = new StringBuilder();

				while (true) {
					String s = dataStream.readUTF();
					if (s.equals(SEPARATOR)) {
						if (found != null) {
							final String result = found.toString();
							cache.put(file.toLowerCase(), new SoftReference<>(result));
							return result;
						}
						break;
					}

					if (s.contains(AtomImg.DATA_IMAGE_PNG_BASE64)) {
						if (dataImagePngBase64Stream == null) {
							dataImagePngBase64Stream = getDataImagePngBase64();
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
						final String base64 = readOneImage(dataImagePngBase64Stream, colors);
						s = s.replaceFirst(AtomImg.DATA_IMAGE_PNG_BASE64, AtomImg.DATA_IMAGE_PNG_BASE64 + base64);
					}

					if (found != null) {
						found.append(s);
						found.append("\n");
					}
					if (isSpriteLine(s)) {
						final Matcher m = sizePattern.matcher(s);
						final boolean ok = m.find();
						if (ok == false)
							throw new IOException(s);

						final int width = Integer.parseInt(m.group(1));
						final int height = Integer.parseInt(m.group(2));
						if (found == null) {
							skipSprite(width, height, spriteStream);
						} else {
							final String sprite = readSprite(width, height, spriteStream);
							found.append(sprite);
							found.append("}\n");
						}
					}
				}
			}
		} finally {
			dataStream.close();
			spriteStream.close();
			if (dataImagePngBase64Stream != null)
				dataImagePngBase64Stream.close();
		}

	}

	private String readOneImage(InputStream is, List<Integer> colors) throws IOException {
		final int width = is.read();
		final int height = is.read();

		final BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int y = 0; y < height; y += 1)
			for (int x = 0; x < width; x += 1) {
				final int rgb = colors.get(read2bytes(is));
				result.setRGB(x, y, rgb);
			}

		try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			ImageIO.write(result, "png", baos);
			return new String(Base64Coder.encode(baos.toByteArray()));
		}

	}

	private void skipSprite(int width, int height, InputStream inputStream) throws IOException {
		final int nbLines = (height + 1) / 2;
		inputStream.skip(nbLines * width);
	}

	private String readSprite(int width, int height, InputStream inputStream) throws IOException {
		final int nbLines = (height + 1) / 2;
		final StringBuilder result = new StringBuilder();
		int line = 0;
		for (int j = 0; j < nbLines; j++) {
			final StringBuilder sb1 = new StringBuilder();
			final StringBuilder sb2 = new StringBuilder();
			for (int i = 0; i < width; i++) {
				final int b = inputStream.read();
				final int b1 = (b & 0xF0) >> 4;
				final int b2 = (b & 0x0F);
				sb1.append(toHexString(b1));
				sb2.append(toHexString(b2));
			}
			result.append(sb1.toString());
			result.append("\n");
			line++;
			if (line < height) {
				result.append(sb2.toString());
				result.append("\n");
				line++;
			}
		}
		return result.toString();
	}

	private String toHexString(final int b) {
		return Integer.toHexString(b).toUpperCase();
	}

	private boolean isSpriteLine(String s) {
		return s.trim().startsWith("sprite") && s.trim().endsWith("{");
	}

	private static DataInputStream getDataStream(String name) throws IOException {
		final InputStream raw = getInternalInputStream(name, "-abx.repx");
		if (raw == null)
			return null;

		return new DataInputStream(new BrotliInputStream(raw));
	}

	private DataInputStream getDataStream() throws IOException {
		return getDataStream(name);
	}

	private InputStream getSpriteStream() throws IOException {
		final InputStream raw = getInternalInputStream(name, "-dex.repx");
		if (raw == null)
			return null;
		return new BrotliInputStream(raw);
	}

	private InputStream getDataImagePngBase64() throws IOException {
		final InputStream raw = getInternalInputStream(name, "-ghx.repx");
		if (raw == null)
			return null;
		return new BrotliInputStream(raw);
	}

	private static InputStream getInternalInputStream(String fullname, String extension) {
		final String res = "/stdlib/" + fullname + extension;
		return Stdlib.class.getResourceAsStream(res);
	}

	public static void extractStdLib() throws IOException {
		for (String name : getAll()) {
			final Stdlib folder = Stdlib.retrieve(name);
			folder.extractMeFull();
		}
	}

	private static Collection<String> getAll() throws IOException {
		final Set<String> result = new TreeSet<>();
		final InputStream home = getInternalInputStream("home", ".repx");
		final BufferedReader br = new BufferedReader(new InputStreamReader(home));
		String name;
		while ((name = br.readLine()) != null)
			result.add(name);

		return Collections.unmodifiableCollection(result);
	}

	private void extractMeFull() throws IOException {
		final DataInputStream dataStream = getDataStream();
		if (dataStream == null)
			return;

		dataStream.readUTF();
		final InputStream spriteStream = getSpriteStream();
		try {
			while (true) {
				final String filename = dataStream.readUTF();
				if (filename.equals(SEPARATOR))
					return;

				final SFile f = new SFile("stdlib/" + name + "/" + filename + ".puml");
				f.getParentFile().mkdirs();
				final PrintWriter fos = f.createPrintWriter();
				while (true) {
					final String s = dataStream.readUTF();
					if (s.equals(SEPARATOR))
						break;

					fos.println(s);
					if (isSpriteLine(s)) {
						final Matcher m = sizePattern.matcher(s);
						final boolean ok = m.find();
						if (ok == false)
							throw new IOException(s);

						final int width = Integer.parseInt(m.group(1));
						final int height = Integer.parseInt(m.group(2));
						final String sprite = readSprite(width, height, spriteStream);
						fos.println(sprite);
						fos.println("}");
					}
				}
				fos.close();
			}
		} finally {
			dataStream.close();
			spriteStream.close();
		}
	}

	public List<String> extractAllSprites() throws IOException {
		final List<String> result = new ArrayList<>();
		final DataInputStream dataStream = getDataStream();
		if (dataStream == null)
			return Collections.unmodifiableList(result);

		dataStream.readUTF();
		final InputStream spriteStream = getSpriteStream();
		try {
			while (true) {
				final String filename = dataStream.readUTF();
				if (filename.equals(SEPARATOR))
					return Collections.unmodifiableList(result);

				while (true) {
					final String s = dataStream.readUTF();
					if (s.equals(SEPARATOR))
						break;

					if (isSpriteLine(s)) {
						final Matcher m = sizePattern.matcher(s);
						final boolean ok = m.find();
						if (ok == false)
							throw new IOException(s);

						final int width = Integer.parseInt(m.group(1));
						final int height = Integer.parseInt(m.group(2));
						final String sprite = readSprite(width, height, spriteStream);
						if (s.contains("_LARGE") == false)
							result.add(s + "\n" + sprite + "}");

					}
				}
			}
		} finally {
			dataStream.close();
			spriteStream.close();
		}
	}

	public static void addInfoVersion(List<String> strings, boolean details) {
		try {
			for (String name : getAll()) {
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

	private String getVersion() {
		return info.get("VERSION");
	}

	private String getSource() {
		return info.get("SOURCE");
	}

	public static void printStdLib() {
		final List<String> print = new ArrayList<>();
		addInfoVersion(print, true);
		for (String s : print)
			System.out.println(s.replace("<b>", ""));

	}
	// ::done
}
