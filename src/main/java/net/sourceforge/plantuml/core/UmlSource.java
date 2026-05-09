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
package net.sourceforge.plantuml.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.creole.atom.AtomImg;
import net.sourceforge.plantuml.nio.PathSystem;
import net.sourceforge.plantuml.regex.Matcher2;
import net.sourceforge.plantuml.regex.Pattern2;
import net.sourceforge.plantuml.teavm.browser.BrowserLog;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.utils.BoyerMoore;
import net.sourceforge.plantuml.utils.SignatureUtils;
import net.sourceforge.plantuml.utils.StartUtils;
import net.sourceforge.plantuml.version.IteratorCounter2;
import net.sourceforge.plantuml.version.IteratorCounter2Impl;

/**
 * Represents the textual source of some diagram. The source should start with a
 * <code>@startfoo</code> and end with <code>@endfoo</code>.
 * <p>
 * So the diagram does not have to be a UML one.
 *
 * @author Arnaud Roques
 *
 */
final public class UmlSource {

	final private List<StringLocated> source;
	final private List<StringLocated> rawSource;
	final private PathSystem pathSystem = PathSystem.fetch();
	final private Map<String, String> md5map = new HashMap<>();

	private long seedCache;
	private boolean seedCacheValid;

	public UmlSource removeInitialNoise() {
		final int size = source.size();
		int cut = 1;
		while (cut < size && isNoise(source.get(cut).getString()))
			cut++;

		if (cut == 1)
			return this;

		final List<StringLocated> trimmed = new ArrayList<>(size - cut + 1);
		trimmed.add(source.get(0));
		trimmed.addAll(source.subList(cut, size));

		return new UmlSource(trimmed, rawSource);
	}

	private static boolean isNoise(String line) {
		if (line.isEmpty())
			return true;
		switch (line.charAt(0)) {
		case 's':
			return line.startsWith("skinparam ") || line.startsWith("skinparamlocked ");
		case '!':
			return line.startsWith("!pragma ");
		default:
			return false;
		}
	}

	public boolean containsIgnoreCase(String searched) {
		for (StringLocated s : source)
			if (StringUtils.goLowerCase(s.getString()).contains(searched))
				return true;

		return false;
	}

	private UmlSource(List<StringLocated> source, List<StringLocated> rawSource) {
		this.source = source;
		this.rawSource = rawSource;
	}

	public static UmlSource create(List<StringLocated> source, boolean checkEndingBackslash) {
		return createWithRaw(source, checkEndingBackslash, new ArrayList<>());
	}

	/**
	 * Build the source from a text.
	 * 
	 * @param source               the source of the diagram
	 * @param checkEndingBackslash <code>true</code> if an ending backslash means
	 *                             that a line has to be collapsed with the
	 *                             following one.
	 */
	public static UmlSource createWithRaw(List<StringLocated> source, boolean checkEndingBackslash,
			List<StringLocated> rawSource) {
		final UmlSource result = new UmlSource(new ArrayList<>(), rawSource);
		result.loadInternal(source, checkEndingBackslash);
		return result;
	}

	private void loadInternal(List<StringLocated> source, boolean checkEndingBackslash) {
		if (checkEndingBackslash) {
			final StringBuilder pending = new StringBuilder();
			for (StringLocated cs : source) {
				final String s = cs.getString();
				if (StringUtils.endsWithBackslash(s)) {
					pending.append(s, 0, s.length() - 1);
				} else {
					pending.append(s);
					this.source.add(new StringLocated(pending.toString(), cs.getLocation()));
					pending.setLength(0);
				}
			}
		} else {
			this.source.addAll(source);
		}
	}

	/**
	 * Retrieve the type of the diagram. This is based on the first line
	 * <code>@startfoo</code>.
	 *
	 * @return the type of the diagram.
	 */
	public Collection<DiagramType> getDiagramTypes() {
		return DiagramType.findStartTypes(source.get(0).getString());
	}

	/**
	 * Allows to iterator over the source.
	 *
	 * @return a iterator that allow counting line number.
	 */
	public IteratorCounter2 iterator2() {
		return new IteratorCounter2Impl(source);
	}

//	public Iterator<StringLocated> iteratorRaw() {
//		return Collections.unmodifiableCollection(rawSource).iterator();
//	}

	/**
	 * @deprecated Use {@link #getPlainString(String)} instead, like
	 *             <code>getPlainString("\n")</code>
	 */
	@Deprecated()
	public String getPlainString() {
		return getPlainString("\n");
	}

	/**
	 * Return the source as a single String.
	 *
	 * @return the whole diagram source
	 */
	public String getPlainString(String separator) {
		final StringBuilder sb = new StringBuilder();
		for (StringLocated s : source) {
			sb.append(s.getString());
			sb.append(separator);
		}
		return sb.toString();
	}

	public String getRawString(String separator) {
		final StringBuilder sb = new StringBuilder();
		for (StringLocated s : rawSource) {
			sb.append(s.getString());
			sb.append(separator);
		}
		return sb.toString();
	}

	public long seed() {
		if (seedCacheValid)
			return seedCache;

		long h = 1125899906842597L; // prime
		for (StringLocated sl : source) {
		    h = 31 * h + sl.getString().hashCode();
		    h = 31 * h + '\n';
		}
		seedCache = h;
		seedCacheValid = true;
		return h;
	}

	/**
	 * Return the number of line in the diagram.
	 */
	public int getTotalLineCount() {
		return source.size();
	}

	public boolean getTotalLineCountLessThan5() {
		return getTotalLineCount() < 5;
	}

	/**
	 * Check if a source diagram description is empty. Does not take comment line
	 * into account.
	 *
	 * @return <code>true</code> if the diagram does not contain information.
	 */
	private static final Pattern COMMENT_LINE = Pattern.compile("\\s*'.*");

	public boolean isEmpty() {
		for (StringLocated sl : source) {
			final String line = sl.getString();

			if (StartUtils.isStartDirective(line))
				continue;
			if (StartUtils.isEndDirective(line))
				continue;
			if (COMMENT_LINE.matcher(line).matches())
				continue;
			if (StringUtils.trin(line).length() != 0)
				return false;

		}
		return true;
	}

	private static final Pattern2 TITLE = Pattern2.cmpile("^[%s]*title[%s]+(.+)$");

	/**
	 * Retrieve the title, if defined in the diagram source. Never return
	 * <code>null</code>.
	 */
	public Display getTitle() {
		for (StringLocated s : source) {
			final Matcher2 m = TITLE.matcher(s.getString(), 0);
			final boolean ok = m.matches();
			if (ok)
				return Display.create(m.group(1));
		}
		return Display.empty();
	}

	public boolean isStartDef() {
		return source.get(0).getString().startsWith("@startdef");
	}

	private static final Pattern ID = Pattern.compile("id=([\\w]+)\\b");

	public String getId() {
		final Matcher m = ID.matcher(source.get(0).getString());
		if (m.find())
			return m.group(1);

		return null;
	}

	public PathSystem getPathSystem() {
		return pathSystem;
	}

	public static final String BASE64_TAG_START = AtomImg.DATA_IMAGE_PNG_BASE64;
	public static final String BASE64_TAG_REPLACEMENT = "data:image/png;md5,";
	private static final BoyerMoore BASE64_BM = new BoyerMoore(BASE64_TAG_START);

	public void patchBase64() {
		seedCacheValid = false;
		for (int i = 0; i < source.size(); i++) {
			final StringLocated original = source.get(i);
			final String line = original.getString();
			final String patched = patchBase64Line(BASE64_BM, line);
			if (patched != line)
				source.set(i, new StringLocated(patched, original.getLocation(), original.getPreprocessorError()));

		}
	}

	/**
	 * Replaces inline base64 PNG images with a compact MD5 digest form.
	 * <p>
	 * Transforms {@code <img data:image/png;base64,XXXX{scale=1}>} into
	 * {@code <img data:image/png;md5,YYYY{scale=1}>} where YYYY is the MD5 hex
	 * digest of the base64 data (XXXX).
	 * <p>
	 * This keeps lines short enough for the regex-based command parser, which is
	 * critical in TeaVM where the JS regex engine has a limited call stack.
	 */
	public String patchBase64Line(BoyerMoore bm, String line) {
		int from = 0;
		StringBuilder sb = null;
		while (true) {
			final int start = bm.searchIn(line, from);
			if (start == -1)
				break;

			final int dataStart = start + BASE64_TAG_START.length();

			if (sb == null)
				sb = new StringBuilder();

			// Scan forward to find the end of legitimate base64 characters
			int base64End = dataStart;
			while (base64End < line.length() && isBase64Char(line.charAt(base64End)))
				base64End++;

			final String base64Data = line.substring(dataStart, base64End);
			final String md5 = SignatureUtils.getMD5Hex(base64Data);
			md5map.put(md5, base64Data);

			sb.append(line, from, start);
			sb.append(BASE64_TAG_REPLACEMENT);
			sb.append(md5);
			from = base64End;
		}
		if (sb == null)
			return line;

		sb.append(line, from, line.length());
		return sb.toString();
	}

	private static boolean isBase64Char(char c) {
		return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '+' || c == '/'
				|| c == '=';
	}

	public Map<String, String> getMd5map() {
		return md5map;
	}

}
