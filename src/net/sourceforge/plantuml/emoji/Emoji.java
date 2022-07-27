package net.sourceforge.plantuml.emoji;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sourceforge.plantuml.emoji.data.Dummy;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.color.HColor;

// Emojji from https://twemoji.twitter.com/
// Shorcut from https://api.github.com/emojis

public class Emoji {
	private final static Map<String, Emoji> ALL = new HashMap<>();

	static {
		final InputStream is = Dummy.class.getResourceAsStream("emoji.txt");
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			String s = null;
			while ((s = br.readLine()) != null) {
				new Emoji(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, Emoji> getAll() {
		return Collections.unmodifiableMap(new TreeMap<>(ALL));
	}

	private SvgNanoParser nano;

	private final String unicode;
	private final String shortcut;

	private Emoji(String unicode) {
		final int x = unicode.indexOf(';');
		if (x == -1) {
			this.shortcut = null;
		} else {
			this.shortcut = unicode.substring(x + 1);
			ALL.put(this.shortcut, this);
			unicode = unicode.substring(0, x);
		}
		this.unicode = unicode;
		ALL.put(unicode, this);
	}

	public static String pattern() {
		final StringBuilder sb = new StringBuilder("\\<(#\\w+)?:(");
		for (String s : ALL.keySet()) {
			if (sb.toString().endsWith("(") == false)
				sb.append("|");
			sb.append(s);
		}
		sb.append("):\\>");
		return sb.toString();
	}

	public static Emoji retrieve(String name) {
		return ALL.get(name.toLowerCase());
	}

	private synchronized void loadIfNeed() throws IOException {
		if (nano != null)
			return;

		final List<String> data = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(Dummy.class.getResourceAsStream(unicode + ".svg")))) {
			final String singleLine = br.readLine();
			data.add(singleLine);
		}
		this.nano = new SvgNanoParser(data, false);
	}

	public void drawU(UGraphic ug, double scale, HColor colorForMonochrome) {
		try {
			loadIfNeed();
		} catch (IOException e) {
			e.printStackTrace();
		}
		nano.drawU(ug, scale, colorForMonochrome);
	}

	public String getShortcut() {
		return shortcut;
	}

}
