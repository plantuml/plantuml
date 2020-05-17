package net.sourceforge.plantuml.creole.rosetta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReaderDokuwiki extends ReaderAbstractWiki implements ReaderWiki {

	@Override
	protected String singleLineFormat(String wiki) {
		final boolean endingSlashSlash = wiki.endsWith("\\\\");
		if (endingSlashSlash) {
			wiki = wiki.substring(0, wiki.length() - 2);
		}

		// Raw %% and ''
		wiki = rawCode(wiki, "''%%", "%%''");
		wiki = rawCode(wiki, "%%", "%%");
		wiki = rawCode(wiki, "''", "''");

		// Protect/escape some character
		for (char c : "^|<".toCharArray()) {
			wiki = wiki.replace("\\" + c, "" + WikiLanguage.toF7(c));
		}

		// Horizontal lines
		wiki = wiki.replaceAll("^___+$", WikiLanguage.UNICODE.tag("hr"));
		wiki = wiki.replaceAll("^---+$", WikiLanguage.UNICODE.tag("hr"));
		wiki = wiki.replaceAll("^\\*\\*\\*+$", WikiLanguage.UNICODE.tag("hr"));

		// Strike
		wiki = wiki.replaceAll("\\<del\\>([^<>]+?)\\</del\\>",
				WikiLanguage.UNICODE.tag("strike") + "$1" + WikiLanguage.UNICODE.slashTag("strike"));
		wiki = wiki.replaceAll("\\<strike\\>([^<>]+?)\\</strike\\>",
				WikiLanguage.UNICODE.tag("strike") + "$1" + WikiLanguage.UNICODE.slashTag("strike"));
		wiki = wiki.replaceAll("~~([^~]+?)~~",
				WikiLanguage.UNICODE.tag("strike") + "$1" + WikiLanguage.UNICODE.slashTag("strike"));

		// break
		wiki = wiki.replace("\\\\ ", WikiLanguage.UNICODE.tag("br"));

		// Em & Strong
		wiki = wiki.replaceAll("//(.+?)//",
				WikiLanguage.UNICODE.tag("em") + "$1" + WikiLanguage.UNICODE.slashTag("em"));
		wiki = wiki.replaceAll("\\*\\*(.+?)\\*\\*",
				WikiLanguage.UNICODE.tag("strong") + "$1" + WikiLanguage.UNICODE.slashTag("strong"));
		wiki = wiki.replace(WikiLanguage.UNICODE.tag("strong") + WikiLanguage.UNICODE.tag("em"),
				WikiLanguage.UNICODE.tag("strongem"));
		wiki = wiki.replace(WikiLanguage.UNICODE.tag("em") + WikiLanguage.UNICODE.tag("strong"),
				WikiLanguage.UNICODE.tag("strongem"));
		wiki = wiki.replace(WikiLanguage.UNICODE.slashTag("strong") + WikiLanguage.UNICODE.slashTag("em"),
				WikiLanguage.UNICODE.slashTag("strongem"));
		wiki = wiki.replace(WikiLanguage.UNICODE.slashTag("em") + WikiLanguage.UNICODE.slashTag("strong"),
				WikiLanguage.UNICODE.slashTag("strongem"));

		// Underscore
		wiki = wiki.replaceAll("__(.+?)__", WikiLanguage.UNICODE.tag("u") + "$1" + WikiLanguage.UNICODE.slashTag("u"));

		if (endingSlashSlash) {
			wiki += WikiLanguage.UNICODE.tag("br");
		}

		return wiki;
	}

	public List<String> transform(List<String> raw) {
		final List<String> uhtml = new ArrayList<String>();
		for (int i = 0; i < raw.size(); i++) {
			String current = raw.get(i);

			if (current.startsWith("FIXME ")) {
				continue;
			}

			current = current.replaceFirst("^\\s+@start", "@start");
			current = current.replaceFirst("^\\s+@end", "@end");

			final AutoGroup autoGroup = getAutoGroup(raw, i);
			if (autoGroup != null) {
				i += autoGroup.getSkipped() - 1;
				autoGroup.exportHtml(uhtml);
				continue;
			}

			final StartEndGroup startEndGroup = getStartEndGroup(raw, i);
			if (startEndGroup != null) {
				i += startEndGroup.getSkipped() - 1;
				startEndGroup.exportHtml(uhtml);
				continue;
			}

			if (current.length() == 0) {
				uhtml.add(WikiLanguage.UNICODE.tag("p"));
			} else {
				uhtml.add(singleLineFormat(current));
			}
		}
		return Collections.unmodifiableList(uhtml);
	}

	private StartEndGroup getStartEndGroup(List<String> raw, int i) {
		if (raw.get(i).equals("<code>")) {
			return new StartEndGroup(raw, i, "</code>");
		}
		if (raw.get(i).startsWith("<uml")) {
			return new StartEndGroup(raw, i, "</uml>");
		}
		if (raw.get(i).equals("<plantuml>")) {
			return new StartEndGroup(raw, i, "</plantuml>");
		}
		return null;
	}

	private AutoGroup getAutoGroup(List<String> raw, int i) {
		AutoGroup group = getAutoGroup(raw, i, "  * ", "    * ");
		if (group != null) {
			return group;
		}
		group = getAutoGroup(raw, i, "  - ", "    - ");
		if (group != null) {
			return group;
		}
		group = getAutoGroup(raw, i, "\t");
		if (group != null) {
			return group;
		}
		group = getAutoGroup(raw, i, "> ");
		if (group != null) {
			return group;
		}
		group = getAutoGroup(raw, i, "] ");
		if (group != null) {
			return group;
		}
		group = getAutoGroup(raw, i, "    ");
		if (group != null) {
			return group;
		}
		group = getAutoGroup(raw, i, "^");
		if (group != null) {
			return group;
		}
		return null;
	}

	private AutoGroup getAutoGroup(List<String> raw, int i, String... headers) {
		if (raw.get(i).startsWith(headers[0]) == false) {
			return null;
		}
		final AutoGroup result = new AutoGroup(headers);
		while (i < raw.size() && result.isInTheGroup(raw.get(i))) {
			result.addLine(raw.get(i));
			i++;
		}
		return result;
	}

	class AutoGroup {

		private final List<String> lines = new ArrayList<String>();
		private int skip = 0;
		private final String[] headers;

		private AutoGroup(String[] headers) {
			this.headers = headers;
		}

		private void addLine(String s) {
			if (headers[0].equals("> ") && s.equals(">")) {
				lines.add("");
			} else if (headers[0].equals("] ") && s.equals("]")) {
				lines.add("");
			} else if (headers[0].equals("^")) {
				lines.add(s);
			} else {
				lines.add(s.substring(headers[0].length()));
			}
			skip++;
		}

		private int getSkipped() {
			return skip;
		}

		private void exportHtml(List<String> uhtml) {
			if (headers[0].equals("  * ")) {
				exportList(uhtml, "ul");
			} else if (headers[0].equals("  - ")) {
				exportList(uhtml, "ol");
			} else if (headers[0].equals("    ")) {
				exportCode(uhtml);
			} else if (headers[0].equals("\t")) {
				exportCode(uhtml);
			} else if (headers[0].equals("> ")) {
				exportBlockquote(uhtml);
			} else if (headers[0].equals("] ")) {
				exportFieldset(uhtml);
			} else if (headers[0].equals("^")) {
				exportTable(uhtml);
			} else {
				throw new UnsupportedOperationException();
			}
		}

		private void exportCode(List<String> uhtml) {
			exportCodeInternal(uhtml, "codepre", lines);
		}

		private void exportList(List<String> uhtml, String type) {
			uhtml.add(WikiLanguage.UNICODE.tag(type));
			for (String s : lines) {
				int level = 0;
				if (s.startsWith("* ") || s.startsWith("- ")) {
					level++;
					s = s.substring(2);
				}
				uhtml.add(WikiLanguage.UNICODE.tag(type + "li", "level", "" + level) + singleLineFormat(s)
						+ WikiLanguage.UNICODE.slashTag(type + "li"));
			}
			uhtml.add(WikiLanguage.UNICODE.slashTag(type));
		}

		private void exportFieldset(List<String> uhtml) {
			uhtml.add(WikiLanguage.UNICODE.tag("fieldset"));
			uhtml.addAll(transform(lines));
			uhtml.add(WikiLanguage.UNICODE.slashTag("fieldset"));
		}

		private void exportBlockquote(List<String> uhtml) {
			uhtml.add(WikiLanguage.UNICODE.tag("blockquote"));
			uhtml.addAll(transform(lines));
			uhtml.add(WikiLanguage.UNICODE.slashTag("blockquote"));
		}

		private void exportTable(List<String> uhtml) {
			uhtml.add(WikiLanguage.UNICODE.tag("table"));
			int i = 0;
			int sizeHeader = 0;
			for (String s : lines) {
				final String sep = i == 0 ? "^" : "|";
				final String tagHeader = i == 0 ? "th" : "tr";
				final String cellHeader = i == 0 ? "tdh" : "td";
				uhtml.add(WikiLanguage.UNICODE.tag(tagHeader));

				s = cleanAndHideBackslashSeparator(s, sep, "\uF500");

				final String cols[] = s.split("\\" + sep);
				// System.err.println("cols1=" + Arrays.asList(cols));
				if (i == 0) {
					sizeHeader = cols.length;
				} else if (cols.length != sizeHeader) {
					System.err.println("lines=" + lines);
					System.err.println("WARNING!!! " + sizeHeader + " " + cols.length);
					throw new IllegalStateException("WARNING!!! " + sizeHeader + " " + cols.length);
				}
				for (String cell : cols) {
					cell = cell.trim();
					// if (cell.length() > 0) {
					uhtml.add(WikiLanguage.UNICODE.tag(cellHeader));
					// uhtml.add(singleLineFormat(cell));
					uhtml.add(WikiLanguage.restoreAllCharsF7(cell.replace('\uF500', sep.charAt(0))));
					uhtml.add(WikiLanguage.UNICODE.slashTag(cellHeader));
					// }

				}
				uhtml.add(WikiLanguage.UNICODE.slashTag(tagHeader));
				i++;
			}
			uhtml.add(WikiLanguage.UNICODE.slashTag("table"));
		}

		public boolean isInTheGroup(String line) {
			if (headers[0].equals("^") && line.startsWith("|")) {
				return true;
			}
			if (headers[0].equals("> ") && line.startsWith(">")) {
				return true;
			}
			if (headers[0].equals("] ") && line.startsWith("]")) {
				return true;
			}
			for (String header : headers) {
				if (line.startsWith(header)) {
					return true;
				}
			}
			return false;
		}
	}

	class StartEndGroup {

		private final List<String> lines = new ArrayList<String>();
		private int skip = 0;
		private final String first;

		private StartEndGroup(List<String> raw, int i, String end) {
			this.first = raw.get(i);
			skip++;
			i++;
			while (i < raw.size() && raw.get(i).equals(end) == false) {
				lines.add(raw.get(i));
				i++;
				skip++;
			}
			skip++;
		}

		private void exportHtml(List<String> uhtml) {
			if (first.equals("<code>")) {
				exportCodeInternal(uhtml, "codepre", lines);
			} else if (first.startsWith("<uml")) {
				exportCodeInternal(uhtml, "imageuml", lines);
			} else if (first.equals("<plantuml>")) {
				exportCodeInternalEnsureStartuml(uhtml, "codeandimg", lines);
			} else {
				throw new UnsupportedOperationException();
			}
		}

		private int getSkipped() {
			return skip;
		}
	}

}
