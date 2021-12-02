package net.sourceforge.plantuml.emojitwo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.emojitwo.data.Dummy;
import net.sourceforge.plantuml.openiconic.SvgPath;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

// Shorcut from https://github.com/ikatyang/emoji-cheat-sheet/blob/master/README.md
// Emojji from https://github.com/EmojiTwo/emojitwo
public class EmojiTwo {

	private final static Map<String, EmojiTwo> ALL = new HashMap<>();

	static {
		new EmojiTwo("2600");
		new EmojiTwo("1f600", "smile");
		new EmojiTwo("1f607", "innocent");
		new EmojiTwo("1f60b", "yum");
	}

	private final List<String> data = new ArrayList<>();
	private final String unicode;

	private EmojiTwo(String unicode) {
		this(unicode, null);
	}

	private EmojiTwo(String unicode, String shortcut) {
		this.unicode = unicode;
		ALL.put(unicode, this);
		if (shortcut != null)
			ALL.put(shortcut, this);
	}

	public static String pattern() {
		final StringBuilder sb = new StringBuilder("\\<:(");
		for (String s : ALL.keySet()) {
			if (sb.toString().endsWith("(") == false)
				sb.append("|");
			sb.append(s);
		}
		sb.append("):\\>");
		return sb.toString();
	}

	public static EmojiTwo retrieve(String name) {
		return ALL.get(name.toLowerCase());
	}

	private HColor extractFill(String s) {
		final String col = extractData("fill", s);
		if (col == null)
			return null;
		return HColorSet.instance().getColorOrWhite(col);
	}

	private String extractData(String name, String s) {
		final Pattern p = Pattern.compile(name + "=\"([^\"]+)\"");
		final Matcher m = p.matcher(s);
		if (m.find())
			return m.group(1);

		return null;
	}

	private synchronized void loadIfNeed() throws IOException {
		if (data.size() > 0)
			return;
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(Dummy.class.getResourceAsStream(unicode + ".svg")))) {
			String s = null;
			while ((s = br.readLine()) != null) {
				if (s.contains("<path")) {
					data.add(s);
				} else if (s.contains("<g ")) {
					data.add(s);
				} else if (s.contains("<circle ")) {
					data.add(s);
				} else {
					// System.err.println("???=" + s);
				}
			}
		}
	}

	public void drawU(UGraphic ug, double scale) {
		try {
			loadIfNeed();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (String s : data) {
			if (s.contains("<path")) {
				// System.err.println("**path=" + s);

				final HColor fill = extractFill(s);
				if (fill != null)
					ug = ug.apply(fill).apply(fill.bg());

				final int x1 = s.indexOf("d=\"");
				final int x2 = s.indexOf('"', x1 + 3);
				final String tmp = s.substring(x1 + 3, x2);
				// System.err.println("tmp=" + tmp);
				final SvgPath svgPath = new SvgPath(tmp);
				svgPath.drawMe(ug, scale);
			} else if (s.contains("<g ")) {
				// System.err.println("**g=" + s);
				final HColor fill = extractFill(s);
				if (fill != null)
					ug = ug.apply(fill).apply(fill.bg());
			} else if (s.contains("<circle ")) {
				final double cx = Double.parseDouble(extractData("cx", s)) * scale;
				final double cy = Double.parseDouble(extractData("cy", s)) * scale;
				final double r = Double.parseDouble(extractData("r", s)) * scale;
				final HColor fill = extractFill(s);
				if (fill != null)
					ug = ug.apply(fill).apply(fill.bg());

				ug.apply(new UTranslate(cx - r, cy - r)).draw(new UEllipse(r * 2, r * 2));

			} else {
				// System.err.println("**?=" + s);
			}
		}

	}

}
