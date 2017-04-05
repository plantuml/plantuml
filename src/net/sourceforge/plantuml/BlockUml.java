/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
package net.sourceforge.plantuml;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.code.AsciiEncoder;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.utils.StartUtils;
import net.sourceforge.plantuml.version.Version;

public class BlockUml {

	private final List<CharSequence2> data;
	private final int startLine;
	private Diagram system;
	private final Defines localDefines;

	BlockUml(String... strings) {
		this(convert(strings), 0, Defines.createEmpty());
	}

	public String getFlashData() {
		final StringBuilder sb = new StringBuilder();
		for (CharSequence2 line : data) {
			sb.append(line);
			sb.append('\r');
			sb.append('\n');
		}
		return sb.toString();
	}

	public static List<CharSequence2> convert(String... strings) {
		return convert(Arrays.asList(strings));
	}

	public static List<CharSequence2> convert(List<String> strings) {
		final List<CharSequence2> result = new ArrayList<CharSequence2>();
		LineLocationImpl location = new LineLocationImpl("block", null);
		for (String s : strings) {
			location = location.oneLineRead();
			result.add(new CharSequence2Impl(s, location));
		}
		return result;
	}

	public BlockUml(List<CharSequence2> strings, int startLine, Defines defines) {
		this.startLine = startLine;
		this.localDefines = defines;
		final CharSequence2 s0 = strings.get(0).trin();
		if (StartUtils.startsWithSymbolAnd("start", s0) == false) {
			throw new IllegalArgumentException();
		}
		this.data = new ArrayList<CharSequence2>(strings);
	}

	public String getFileOrDirname(FileFormat defaultFileFormat) {
		if (OptionFlags.getInstance().isWord()) {
			return null;
		}
		final Matcher2 m = StartUtils.patternFilename.matcher(StringUtils.trin(data.get(0).toString()));
		final boolean ok = m.find();
		if (ok == false) {
			return null;
		}
		String result = m.group(1);
		final int x = result.indexOf(',');
		if (x != -1) {
			result = result.substring(0, x);
		}
		for (int i = 0; i < result.length(); i++) {
			final char c = result.charAt(i);
			if ("<>|".indexOf(c) != -1) {
				return null;
			}
		}
		if (result.startsWith("file://")) {
			result = result.substring("file://".length());
		}
		if (result.contains(".") == false) {
			result = result + defaultFileFormat.getFileSuffix();
		}
		return result;
	}

	public Diagram getDiagram() {
		if (system == null) {
			system = new PSystemBuilder().createPSystem(data);
		}
		return system;
	}

	public final int getStartLine() {
		return startLine;
	}

	public final List<CharSequence2> getData() {
		return data;
	}

	private String internalEtag() {
		try {
			final AsciiEncoder coder = new AsciiEncoder();
			final MessageDigest msgDigest = MessageDigest.getInstance("MD5");
			for (CharSequence s : data) {
				msgDigest.update(s.toString().getBytes("UTF-8"));
			}
			final byte[] digest = msgDigest.digest();
			return coder.encode(digest);
		} catch (Exception e) {
			e.printStackTrace();
			return "NOETAG";
		}
	}

	public String etag() {
		return Version.etag() + internalEtag();
	}

	public long lastModified() {
		return (Version.compileTime() / 1000L / 60) * 1000L * 60 + Version.beta() * 1000L * 3600;
	}

	public boolean isStartDef(String name) {
		final String signature = "@startdef(id=" + name + ")";
		return data.get(0).toString().equalsIgnoreCase(signature);
	}

	public List<? extends CharSequence> getDefinition() {
		if (data.get(0).toString().startsWith("@startdef") == false) {
			throw new IllegalStateException();
		}
		return Collections.unmodifiableList(data.subList(1, data.size() - 1));
	}

	public Defines getLocalDefines() {
		return localDefines;
	}

}
