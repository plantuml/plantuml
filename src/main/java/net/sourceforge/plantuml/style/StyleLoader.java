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
package net.sourceforge.plantuml.style;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.style.parser.StyleParsingException;
import net.sourceforge.plantuml.style.parser2.MergedStyleNode;
import net.sourceforge.plantuml.style.parser2.MergedStyleSheet;
import net.sourceforge.plantuml.style.parser2.RawStyleParser;
import net.sourceforge.plantuml.style.parser2.RawStyleSheet;
import net.sourceforge.plantuml.style.parser2.StyleQuery;
import net.sourceforge.plantuml.teavm.EmbeddedResources;
import net.sourceforge.plantuml.teavm.TeaVM;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.LineLocationImpl;
import net.sourceforge.plantuml.utils.Log;

public final class StyleLoader {
	private static final ConcurrentMap<String, StyleBuilder> cache = new ConcurrentHashMap<>();

	private StyleLoader() {
	}

	public static StyleBuilder loadSkin(String filename) throws IOException, StyleParsingException {
		try {
			if (cache.size() >= 30)
				cache.clear();

			final StyleBuilder builder = cache.computeIfAbsent(filename, key -> {
				try {
					return loadSkinSlow(key);
				} catch (IOException | StyleParsingException e) {
					throw new RuntimeException(e);
				}
			});
			return builder.cloneMe();
		} catch (RuntimeException ex) {
			Throwable cause = ex.getCause();
			if (cause instanceof IOException)
				throw (IOException) cause;
			if (cause instanceof StyleParsingException)
				throw (StyleParsingException) cause;
			throw ex;
		}
	}

	/**
	 * Parses a fragment of .skin/{@code <style>} text into flat legacy {@link Style} objects,
	 * assigning each property an increasing priority via {@code counter} -- the direct
	 * replacement for {@code new net.sourceforge.plantuml.style.parser.StyleParser(counter).parse(lines)},
	 * now going through the {@code parser2} tokenizer ({@link RawStyleParser}) instead of the
	 * legacy hand-written one. {@code counter} is almost always the {@link StyleBuilder} the
	 * result will be loaded or muted into, exactly as before: continuing its counter is what
	 * guarantees an overlay's declarations always outrank whatever that builder already holds.
	 *
	 * <p>
	 * Parsing itself (tokenizing, comma-list handling, {@code @media} detection, {@code
	 * var(--name)} substitution) is {@link RawStyleParser}'s job; {@link MergedStyleNode} then
	 * merges duplicate selectors and folds a dark declaration into its light counterpart, exactly
	 * as a whole {@link MergedStyleSheet} would -- only into a throwaway, one-shot tree here
	 * (this call's {@code counter} is external and not carried across calls, so there is no
	 * sheet identity worth keeping around afterwards) -- before {@link LegacyStyleFlattener}
	 * turns it back into the flat shape every caller of this method still expects.
	 */
	public static Collection<Style> parseStyleText(BlocLines lines, AutomaticCounter counter)
			throws StyleParsingException {
		if (lines.size() == 0)
			return Collections.emptyList();

		final RawStyleSheet raw = RawStyleParser.parse(lines);
		final MergedStyleNode root = MergedStyleNode.newTopLevelContainer();
		MergedStyleSheet.mergeInto(root, raw, counter);
		return LegacyStyleFlattener.flatten(root);
	}

	private static StyleBuilder loadSkinSlow(String filename) throws IOException, StyleParsingException {
		final StyleBuilder styleBuilder = new StyleBuilder();

		final InputStream internalIs = getInputStreamForStyle(filename);
		if (internalIs == null) {
			Log.error("No .skin file seems to be available");
			throw new NoStyleAvailableException();
		}
		final BlocLines lines2 = BlocLines.load(internalIs, new LineLocationImpl(filename, null));
		final Collection<Style> styles = parseStyleText(lines2, styleBuilder);
		// A file that parses without any error but that defines no style at all is not
		// a style sheet: this happens with legacy skinparam files, where every line is
		// silently ignored because the key is unknown. Accepting it here would give an
		// empty StyleBuilder, and every later getStyle() would return null.
		if (styles.isEmpty())
			throw new StyleParsingException("No style found in " + filename);

		for (Style newStyle : styles)
			styleBuilder.loadInternal(newStyle.getSignature(), newStyle);

		return styleBuilder;
	}

	public static InputStream getInputStreamForStyle(String filename) throws IOException {

		if (TeaVM.isTeaVM()) {
			return EmbeddedResources.openPlantumlSkin();
		} else {
			InputStream is = null;

			SFile localFile = new SFile(filename);
			Log.info(() -> "Trying to load style " + filename);
			try {
				if (localFile.exists() == false)
					localFile = FileSystem.getInstance().getFile(filename);
			} catch (IOException e) {
				Log.info(() -> "Cannot open file. " + e);
			}

			final SFile localFile2 = localFile;
			if (localFile.exists()) {
				Log.info(() -> "File found : " + localFile2.getPrintablePath());
				is = localFile.openFile();
			} else {
				Log.info(() -> "File not found : " + localFile2.getPrintablePath());
				final String res = "/skin/" + filename;
				is = StyleLoader.class.getResourceAsStream(res);
				if (is != null)
					Log.info(() -> "... but " + filename + " found inside the .jar");

			}
			return is;
		}
	}

	/**
	 * Properties that any complete style sheet has to define on its root style.
	 *
	 * A .skin file that does not define all of them is only a fragment, meant to be
	 * merged on top of an existing style sheet: this is the case of strictuml.skin,
	 * which is loaded by "skinparam style strictuml". Using such a fragment with the
	 * "skin" command would replace the whole style sheet by almost nothing, and the
	 * drawing would then fail with puzzling errors, for example a missing FontSize
	 * ending up as "IllegalArgumentException: width=0.0".
	 *
	 * Shadowing, RoundCorner, DiagonalCorner and HyperLinkColor are deliberately not
	 * required here: they all have a harmless default.
	 */
	private static final PName[] MANDATORY_ROOT_PROPERTIES = { PName.FontName, PName.FontSize, PName.FontStyle,
			PName.FontColor, PName.LineColor, PName.LineThickness, PName.BackGroundColor, PName.HorizontalAlignment };

	/**
	 * Returns the mandatory root properties that this style sheet does not define.
	 *
	 * An empty list means the style sheet is complete, and so can be used on its own
	 * by the "skin" command.
	 */
	public static List<PName> getMissingRootProperties(StyleBuilder styleBuilder) {
		final Style root = styleBuilder == null ? null
				: styleBuilder.getMergedStyle(StyleQuery.of(Collections.singletonList(SName.root)));

		final List<PName> result = new ArrayList<>();
		for (PName property : MANDATORY_ROOT_PROPERTIES)
			if (root == null || root.hasValue(property) == false)
				result.add(property);

		return Collections.unmodifiableList(result);
	}

	// Kept only for net.sourceforge.plantuml.style.parser2.StyleMerge, whose own
	// int-priority-based scheme is out of scope for this refactor.
	public static final int DELTA_PRIORITY_FOR_STEREOTYPE = 1000;

	public static Map<PName, Value> addStereotypeCount(Map<PName, Value> tmp, int count) {
		final Map<PName, Value> result = new EnumMap<>(PName.class);
		for (Entry<PName, Value> ent : tmp.entrySet())
			result.put(ent.getKey(), ((ValueImpl) ent.getValue()).withStereotypeCount(count));

		return result;
	}

}