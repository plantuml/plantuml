package net.sourceforge.plantuml.preproc;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.brotli.BrotliInputStream;

public class StdlibOld {

	static private final Map<String, StdlibOld> all = new ConcurrentHashMap<String, StdlibOld>();

	static class Portion {
		int position;
		int length;

		Portion(int position, int length) {
			this.position = position;
			this.length = length;
		}

		public byte[] of(DataInputStream is) throws IOException {
			is.skipBytes(position);
			final byte result[] = new byte[length];
			is.readFully(result);
			is.close();
			return result;
		}
	}

	private final Map<String, Portion> data = new HashMap<String, Portion>();
	private final Map<String, SoftReference<byte[]>> cache = new ConcurrentHashMap<String, SoftReference<byte[]>>();
	private final String name;
	private final Map<String, String> info = new HashMap<String, String>();

	public static InputStream getResourceAsStream(String fullname) {
		fullname = fullname.toLowerCase().replace(".puml", "");
		final int last = fullname.indexOf('/');
		if (last == -1) {
			return null;
		}
		try {
			final StdlibOld folder = retrieve(fullname.substring(0, last));
			if (folder == null) {
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
		return StdlibOld.class.getResourceAsStream(res);
	}

	private static StdlibOld retrieve(final String name) throws IOException {
		StdlibOld result = all.get(name);
		if (result == null) {
			result = new StdlibOld(name);
			all.put(name, result);
		}
		return result;
	}

	private StdlibOld(String name) throws IOException {
		this.name = name;
		Log.info("Opening archive " + name);
		int pos = 0;
		final DataInputStream is = getDataIndex();
		fillMap(is.readUTF());
		while (true) {
			final String filename = is.readUTF();
			if (filename.length() == 0) {
				break;
			}
			final int len = is.readInt();
			this.data.put(filename.toLowerCase(), new Portion(pos, len));
			pos += len;
		}
		is.close();
	}

	private void fillMap(String infoString) {
		for (String s : infoString.split("\n")) {
			if (s.contains("=")) {
				final String data[] = s.split("=");
				this.info.put(data[0], data[1]);
			}
		}
	}

	private DataInputStream getDataIndex() {
		final InputStream raw = getInternalInputStream(name, "-idx.repx");
		if (raw == null) {
			return null;
		}
		return new DataInputStream(raw);
	}

	private DataInputStream getFullTexts() throws IOException {
		final InputStream raw = getInternalInputStream(name, "-dat.repx");
		if (raw == null) {
			return null;
		}
		return new DataInputStream(new BrotliInputStream(raw));
	}

	private byte[] getData(String filename) throws IOException {
		final SoftReference<byte[]> ref = cache.get(filename);
		byte[] result = ref == null ? null : ref.get();
		final String full = name + "/" + filename + ".puml";
		if (result == null) {
			result = getDataSlow(filename);
			cache.put(filename, new SoftReference<byte[]>(result));
			Log.info("Reading " + full);
		} else {
			Log.info("Retrieving " + full);
		}
		return result;
	}

	private byte[] getDataSlow(String filename) throws IOException {
		final Portion portion = data.get(filename);
		return portion.of(getFullTexts());
	}

	public static void extractStdLib() throws IOException {
		for (String name : getAll()) {
			final StdlibOld folder = new StdlibOld(name);
			folder.extractMeFull(new File("stdlib", name));
		}
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

	private void extractMeFull(File dir) throws IOException {
		final DataInputStream idx = getDataIndex();
		final DataInputStream txt = getFullTexts();
		final String infoString = idx.readUTF();
		while (true) {
			final String filename = idx.readUTF();
			if (filename.length() == 0) {
				idx.close();
				txt.close();
				return;
			}
			final int len = idx.readInt();
			final File f = new File("stdlib/" + name + "/" + filename + ".puml");
			f.getParentFile().mkdirs();
			final FileOutputStream fos = new FileOutputStream(f);
			final byte data[] = new byte[len];
			txt.readFully(data);
			fos.write(data);
			fos.close();
		}
	}

	public static void addInfoVersion(List<String> strings) {
		try {
			for (String name : getAll()) {
				final StdlibOld folder = new StdlibOld(name);
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
