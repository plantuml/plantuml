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
package net.sourceforge.plantuml;

import static java.nio.charset.StandardCharsets.UTF_8;
import static net.sourceforge.plantuml.utils.CharsetUtils.charsetOrDefault;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.code.AsciiEncoder;
import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.code.TranscoderUtil;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.error.PSystemErrorPreprocessor;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.preproc.FileWithSuffix;
import net.sourceforge.plantuml.preproc2.PreprocessorModeSet;
import net.sourceforge.plantuml.regex.Matcher2;
import net.sourceforge.plantuml.style.ISkinSimple;
import net.sourceforge.plantuml.text.BackSlash;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.tim.TimLoader;
import net.sourceforge.plantuml.utils.LineLocationImpl;
import net.sourceforge.plantuml.utils.StartUtils;
import net.sourceforge.plantuml.version.Version;

public class BlockUml {
	// ::remove file when __HAXE__

	private final List<StringLocated> rawSource;
	private final List<StringLocated> data;
	private List<StringLocated> debug;
	private Diagram system;
	private final Defines localDefines;
	private final ISkinSimple skinParam;
	private final Set<FileWithSuffix> included = new HashSet<>();

	public Set<FileWithSuffix> getIncluded() {
		return Collections.unmodifiableSet(included);
	}

	@Deprecated
	BlockUml(String... strings) {
		this(convert(strings), Defines.createEmpty(), null, null, null);
	}

	// ::comment when __CORE__
	public String getEncodedUrl() throws IOException {
		final Transcoder transcoder = TranscoderUtil.getDefaultTranscoder();
		final String source = getDiagram().getSource().getPlainString("\n");
		final String encoded = transcoder.encode(source);
		return encoded;
	}
	// ::done

	public String getFlashData() {
		final StringBuilder sb = new StringBuilder();
		for (StringLocated line : data) {
			sb.append(line.getString());
			sb.append('\r');
			sb.append(BackSlash.CHAR_NEWLINE);
		}
		return sb.toString();
	}

	public static List<StringLocated> convert(String... strings) {
		return convert(Arrays.asList(strings));
	}

	public static List<StringLocated> convert(List<String> strings) {
		final List<StringLocated> result = new ArrayList<>();
		LineLocationImpl location = new LineLocationImpl("block", null);
		for (String s : strings) {
			location = location.oneLineRead();
			result.add(new StringLocated(s, location));
		}
		return result;
	}

	private boolean preprocessorError;

	/**
	 * @deprecated being kept for backwards compatibility, perhaps other projects
	 *             are using this?
	 */
	@Deprecated
	public BlockUml(List<StringLocated> strings, Defines defines, ISkinSimple skinParam, PreprocessorModeSet mode) {
		this(strings, defines, skinParam, mode, charsetOrDefault(mode.getCharset()));
	}

	public BlockUml(List<StringLocated> strings, Defines defines, ISkinSimple skinParam, PreprocessorModeSet mode,
			Charset charset) {
		this.rawSource = new ArrayList<>(strings);
		this.localDefines = defines;
		this.skinParam = skinParam;
		final String s0 = strings.get(0).getTrimmed().getString();
		if (StartUtils.startsWithSymbolAnd("start", s0) == false)
			throw new IllegalArgumentException();

		if (mode == null) {
			this.data = new ArrayList<>(strings);
		} else {
			final TimLoader timLoader = new TimLoader(mode.getImportedFiles(), defines, charset,
					(DefinitionsContainer) mode);
			this.included.addAll(timLoader.load(strings));
			this.data = timLoader.getResultList();
			this.debug = timLoader.getDebug();
			this.preprocessorError = timLoader.isPreprocessorError();
		}
	}

	// ::comment when __CORE__
	public String getFileOrDirname() {
		if (OptionFlags.getInstance().isWord())
			return null;

		final Matcher2 m = StartUtils.patternFilename.matcher(StringUtils.trin(data.get(0).getString()));
		final boolean ok = m.find();
		if (ok == false)
			return null;

		String result = m.group(1);
		final int x = result.indexOf(',');
		if (x != -1)
			result = result.substring(0, x);

		for (int i = 0; i < result.length(); i++) {
			final char c = result.charAt(i);
			if ("<>|".indexOf(c) != -1)
				return null;

		}
		if (result.startsWith("file://"))
			result = result.substring("file://".length());

		result = result.replaceAll("\\.\\w\\w\\w$", "");
		return result;
	}
	// ::done

	public Diagram getDiagram() {
		if (system == null) {
			if (preprocessorError)
				system = new PSystemErrorPreprocessor(data, debug);
			else
				system = new PSystemBuilder().createPSystem(data, rawSource,
						skinParam == null ? Collections.<String, String>emptyMap() : skinParam.values());
		}
		return system;
	}

	public final List<StringLocated> getData() {
		return data;
	}

	// ::comment when __CORE__
	private String internalEtag() {
		try {
			final AsciiEncoder coder = new AsciiEncoder();
			final MessageDigest msgDigest = MessageDigest.getInstance("MD5");
			for (StringLocated s : data)
				msgDigest.update(s.getString().getBytes(UTF_8));

			final byte[] digest = msgDigest.digest();
			return coder.encode(digest);
		} catch (Exception e) {
			Logme.error(e);
			return "NOETAG";
		}
	}

	public String etag() {
		return Version.etag() + internalEtag();
	}
	// ::done

	public long lastModified() {
		return (Version.compileTime() / 1000L / 60) * 1000L * 60 + Version.beta() * 1000L * 3600;
	}

	public boolean isStartDef(String name) {
		final String signature = "@startdef(id=" + name + ")";
		return data.get(0).getString().equalsIgnoreCase(signature);
	}

	public List<String> getDefinition(boolean withHeader) {
		final List<String> result = new ArrayList<>();
		for (StringLocated s : data)
			result.add(s.getString());

		if (withHeader)
			return Collections.unmodifiableList(result);

		return Collections.unmodifiableList(result.subList(1, result.size() - 1));
	}

	public Defines getLocalDefines() {
		return localDefines;
	}

}
