package net.sourceforge.plantuml.preproc;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.brotli.BrotliInputStream;

public class Stdlib6 {

	static private final Map<String, Stdlib6> all = new ConcurrentHashMap<String, Stdlib6>();
	private static final String SEPARATOR = "\uF8FF";

	private final Map<String, List<String>> raw = new HashMap<String, List<String>>();
	// private final Map<String, SoftReference<byte[]>> cache = new ConcurrentHashMap<String, SoftReference<byte[]>>();
	private final String name;
	private final Map<String, String> info = new HashMap<String, String>();

	public static InputStream getResourceAsStream(String fullname) {
		fullname = fullname.toLowerCase().replace(".puml", "");
		final int last = fullname.indexOf('/');
		if (last == -1) {
			return null;
		}
		try {
			final Stdlib6 folder = retrieve(fullname.substring(0, last));
			if (folder == null || folder.raw.size() == 0) {
				return null;
			}
			final byte[] data = folder.getData(fullname.substring(last + 1));
			if (data == null) {
				return null;
			}
			return new ByteArrayInputStream(data);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static InputStream getInternalInputStream(String fullname, String extension) {
		final String res = "/stdlib/" + fullname + extension;
		return Stdlib6.class.getResourceAsStream(res);
	}

	private static Stdlib6 retrieve(final String name) throws IOException {
		Stdlib6 result = all.get(name);
		if (result == null) {
			result = new Stdlib6(name);
			all.put(name, result);
		}
		return result;
	}

	private final Pattern sizePattern = Pattern.compile("\\[(\\d+)x(\\d+)/16\\]");

	private Stdlib6(String name) throws IOException {
		this.name = name;
		Log.info("Opening archive " + name);
		final DataInputStream dataStream = getDataStream();
		if (dataStream == null) {
			return;
		}
		fillMap(dataStream.readUTF());
		// final InputStream spriteStream = getSpriteStream();
		int pos = 0;
		while (true) {
			final String filename = dataStream.readUTF();
			// System.err.println("filename=" + filename);
			if (filename.equals(SEPARATOR)) {
				break;
			}
			final List<String> text = new ArrayList<String>();
			while (true) {
				final String s = dataStream.readUTF();
				if (s.equals(SEPARATOR)) {
					break;
				}
				text.add(s);
				if (isSpriteLine(s)) {
					final Matcher m = sizePattern.matcher(s);
					final boolean ok = m.find();
					if (ok == false) {
						throw new IOException(s);
					}
					final int width = Integer.parseInt(m.group(1));
					final int height = Integer.parseInt(m.group(2));
					text.add("" + pos);
					pos += spriteSize(width, height);
					// System.err.println("Sprite! " + width + " " + height + " " + s);
					text.add("}");
				}
			}
			raw.put(filename.toLowerCase(), text);
			// System.err.println("data=" + text.size());
			// final int len = is.readInt();
			// this.data.put(filename.toLowerCase(), new Portion(pos, len));
			// pos += len;
		}
		dataStream.close();
	}

	private int spriteSize(int width, int height) {
		final int nbLines = (height + 1) / 2;
		return width * nbLines;
	}

	private String readSprite(int width, int height, InputStream inputStream) throws IOException {
		final StringBuilder result = new StringBuilder();
		final int nbLines = (height + 1) / 2;
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

	private void fillMap(String infoString) {
		for (String s : infoString.split("\n")) {
			if (s.contains("=")) {
				final String data[] = s.split("=");
				this.info.put(data[0], data[1]);
			}
		}
	}

	private DataInputStream getDataStream() throws IOException {
		final InputStream raw = getInternalInputStream(name, "-dat2.repx");
		if (raw == null) {
			return null;
		}
		return new DataInputStream(new BrotliInputStream(raw));
	}

	private InputStream getSpriteStream() throws IOException {
		final InputStream raw = getInternalInputStream(name, "-spr.repx");
		if (raw == null) {
			return null;
		}
		return new BrotliInputStream(raw);
	}

	private byte[] getData(String filename) throws IOException {
		final List<String> result = raw.get(filename);
		if (result == null) {
			return null;
		}
		final StringBuilder sb = new StringBuilder();
		for (Iterator<String> it = result.iterator(); it.hasNext();) {
			final String s = it.next();
			sb.append(s);
			sb.append("\n");
			if (isSpriteLine(s)) {
				final Matcher m = sizePattern.matcher(s);
				final boolean ok = m.find();
				if (ok == false) {
					throw new IOException(s);
				}
				final int width = Integer.parseInt(m.group(1));
				final int height = Integer.parseInt(m.group(2));
				final InputStream spriteStream = getSpriteStream();
				final int pos = Integer.parseInt(it.next());
				// System.err.println("pos=" + pos);
				spriteStream.skip(pos);
				sb.append(readSprite(width, height, spriteStream));
				sb.append("\n");
				spriteStream.close();
			}
		}
		return sb.toString().getBytes("UTF-8");
	}

	public static void extractStdLib() throws IOException {
		// for (String name : getAll()) {
		// final Stdlib6 folder = new Stdlib6(name);
		// folder.extractMeFull(new File("stdlib", name));
		// }
	}

	private static List<String> getAll() throws IOException {
		final List<String> result = new ArrayList<String>();
		final InputStream home = getInternalInputStream("home", ".repx");
		final BufferedReader br = new BufferedReader(new InputStreamReader(home));
		String name;
		while ((name = br.readLine()) != null) {
			result.add(name);
		}
		return Collections.unmodifiableList(result);
	}

	// private void extractMeFull(File dir) throws IOException {
	// final DataInputStream idx = getDataIndex();
	// final DataInputStream txt = getFullTexts();
	// final String infoString = idx.readUTF();
	// while (true) {
	// final String filename = idx.readUTF();
	// if (filename.length() == 0) {
	// idx.close();
	// txt.close();
	// return;
	// }
	// final int len = idx.readInt();
	// final File f = new File("stdlib/" + name + "/" + filename + ".puml");
	// f.getParentFile().mkdirs();
	// final FileOutputStream fos = new FileOutputStream(f);
	// final byte data[] = new byte[len];
	// txt.readFully(data);
	// fos.write(data);
	// fos.close();
	// }
	// }

	public static void addInfoVersion(List<String> strings) {
		try {
			for (String name : getAll()) {
				final Stdlib6 folder = new Stdlib6(name);
				strings.add("* " + name + " (Version " + folder.getVersion() + ")");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String getVersion() {
		return info.get("VERSION");
	}
}
