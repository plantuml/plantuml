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

package net.sourceforge.plantuml.teavm.browser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

import net.sourceforge.plantuml.nio.InputFile;
import net.sourceforge.plantuml.nio.NFolder;

/**
 * Reads the file of a local <code>!include</code>, <code>!include_once</code>,
 * <code>!include_many</code> or <code>!includesub</code>, or of a
 * <code>!theme ... from</code> a local folder, in the browser engine, through
 * a function the host provides.
 * <p>
 * The browser engine has no file system, so on its own such an include can
 * only fail. A host that does have files (an editor extension, a desktop
 * application, a documentation build running the engine) sets
 * <code>PLANTUML_FILE_LOADER</code> on the global object:
 *
 * <pre>
 * globalThis.PLANTUML_FILE_LOADER = function (path, from, onOk, onErr, request) { ... };
 * </pre>
 * <ul>
 * <li><code>path</code>: the file name the directive asks for, after variables
 * are expanded and a selector (<code>file!tag</code>) is split off; for
 * <code>!theme NAME from DIR</code>, <code>DIR/puml-theme-NAME.puml</code>.
 * It is handed over as it is: the engine resolves nothing.</li>
 * <li><code>from</code>: the file whose include is being resolved —
 * <code>null</code> for the diagram itself, otherwise the identifier the host
 * gave to the delivered file being evaluated, so that a relative name can be
 * resolved against that file. A local theme keeps its caller's
 * <code>from</code> for directives in its body, matching the Java build:
 * <code>null</code> if the diagram called it, or the calling file's identifier.
 * A standard-library file or a bundled theme being
 * evaluated never asks the host (see
 * {@link net.sourceforge.plantuml.nio.PathSystem#withoutHostFiles()}).</li>
 * <li><code>onOk(id, text)</code>: delivers the file. <code>id</code> is any
 * non-empty string that identifies it for the host (typically its absolute
 * path or URI), compared as it is. The include strategies count the identifier
 * together with the selector, so two diagrams of one file are two includes.
 * The body of an include or an includesub uses this identifier as
 * <code>from</code>; a local theme instead keeps its caller's context.</li>
 * <li><code>onErr(reason)</code>: the include fails. The reason is written to
 * the console.</li>
 * <li><code>request.kind</code>: <code>"include"</code> (also for
 * <code>!include_once</code> and <code>!include_many</code>),
 * <code>"includesub"</code> or <code>"theme"</code>. Hosts can distinguish
 * resource policies and diagnostics without inferring the operation from
 * the file name. This describes this request, not whether the file's contents
 * will be evaluated. The host still delivers the whole file; the engine
 * handles selectors, sub extraction and repeated includes. Existing loaders
 * can ignore the fifth argument.</li>
 * </ul>
 * The first outcome wins: a second callback, an exception or a rejection after
 * it changes nothing. Returning <code>false</code> (strictly) before either
 * callback declines the file, which then fails as if no loader were set, and a
 * callback arriving later is ignored. A loader may be an <code>async</code>
 * function: the rejection of the promise it returns fails the include, while
 * the value it fulfils with is ignored, so a file is only ever delivered
 * through <code>onOk</code>. A host that neither calls a callback, nor
 * returns <code>false</code>, nor rejects, leaves the rendering waiting: there
 * is no timeout in the engine. The engine never reads anything itself: which
 * files may be read is decided by the host.
 * <p>
 * The same shape as <code>PLANTUML_STDLIB_LOADER</code> in
 * {@link TeaVmScriptLoader}. With no loader set, a local include fails, and
 * so does an includesub of a file, which used to be ignored in the browser.
 * An includesub of a section in the diagram itself needs no loader.
 */
public final class TeaVmFileLoader {
	// ::remove file when JAVA8

	@JSFunctor
	public interface Loaded extends JSObject {
		void invoke(String id, String text);
	}

	/**
	 * Whether the host has set <code>PLANTUML_FILE_LOADER</code>.
	 */
	@JSBody(params = {}, script = "var g = (typeof globalThis !== 'undefined') ? globalThis"
			+ " : ((typeof self !== 'undefined') ? self : this);"
			+ "return typeof g.PLANTUML_FILE_LOADER === 'function';")
	private static native boolean isAvailable();

	/**
	 * Asks the host for a file. Returns <code>false</code> when the host declines
	 * it before any callback; <code>true</code> otherwise. When it returns
	 * <code>true</code>, the caller waits until a callback or a rejected promise
	 * settles the request. If neither happens, the caller keeps waiting because
	 * the engine has no timeout. A missing identifier, a text that is not a
	 * string, an exception, a rejected promise and a reason that cannot be turned
	 * into a string all become a failure; anything after the first outcome is
	 * ignored.
	 */
	@JSBody(params = { "path", "from", "kind", "onOk", "onErr" }, script = "var g = (typeof globalThis !== 'undefined') ? globalThis"
			+ " : ((typeof self !== 'undefined') ? self : this);"
			+ "var settled = false;"
			+ "var reasonOf = function(e) {"
			+ "  try { return (e && e.message !== undefined) ? String(e.message) : ((e === undefined || e === null) ? '' : String(e)); }"
			+ "  catch (x) { return ''; }"
			+ "};"
			+ "var fail = function(reason) {"
			+ "  if (settled) return;"
			+ "  settled = true;"
			+ "  onErr(reasonOf(reason) || ('PLANTUML_FILE_LOADER failed for ' + path));"
			+ "};"
			+ "var ok = function(id, text) {"
			+ "  if (settled) return;"
			+ "  if (typeof id !== 'string' || id === '' || typeof text !== 'string') {"
			+ "    fail('PLANTUML_FILE_LOADER must pass a non-empty string id and a string text for ' + path); return;"
			+ "  }"
			+ "  settled = true; onOk(id, text);"
			+ "};"
			+ "var result;"
			+ "try { result = g.PLANTUML_FILE_LOADER(path, from, ok, fail, { kind: kind }); }"
			+ "catch (e) { fail(e); return true; }"
			+ "if (result !== null && (typeof result === 'object' || typeof result === 'function')) {"
			+ "  try { Promise.resolve(result).then(null, fail); } catch (e) { fail(e); }"
			+ "}"
			+ "if (result === false && settled === false) { settled = true; return false; }"
			+ "return true;")
	private static native boolean load(String path, String from, String kind, Loaded onOk, TeaVmScriptLoader.Err onErr);

	/**
	 * The file the host delivers for <code>path</code>, or <code>null</code> when
	 * no loader is set, when the host declines, or when it fails (the reason is
	 * written to the console, and the include then reports the usual "cannot
	 * include"). Blocks until the host answers, so it MUST be called from a
	 * TeaVM thread context, like {@link TeaVmScriptLoader#loadOnceSync(String)}.
	 *
	 * @param path   the file name the directive asks for
	 * @param folder where the include is written: <code>null</code> for the
	 *               diagram itself, or the folder of a file this loader
	 *               delivered. Any other folder has no host to ask.
	 * @param kind   <code>"include"</code>, <code>"includesub"</code> or
	 *               <code>"theme"</code>
	 */
	public static InputFile getInputFile(String path, NFolder folder, String kind) {
		if (isAvailable() == false)
			return null;
		if (folder != null && folder instanceof HostFolder == false)
			return null;

		final String from = folder == null ? null : ((HostFolder) folder).from;
		final Answer answer = new Answer();
		synchronized (answer) {
			final boolean handled = load(path, from, kind, (id, text) -> {
				synchronized (answer) {
					answer.id = id;
					answer.text = text;
					answer.complete = true;
					answer.notify();
				}
			}, (reason) -> {
				synchronized (answer) {
					answer.error = reason;
					answer.complete = true;
					answer.notify();
				}
			});
			if (handled == false)
				return null;

			while (answer.complete == false) {
				try {
					answer.wait();
				} catch (InterruptedException e) {
					// retry
				}
			}
		}
		if (answer.error != null) {
			TeaVmScriptLoader.consoleWarn("PlantUML: cannot include " + path + ": " + answer.error);
			return null;
		}
		return new HostFile(answer.id, answer.text);
	}

	/**
	 * The identifier the host gave to a file this loader delivered, or
	 * <code>null</code> for any other file. It is what an include strategy
	 * compares, as it is: two identifiers are the same file only when the
	 * strings are equal.
	 */
	public static String getId(InputFile file) {
		if (file instanceof HostFile)
			return ((HostFile) file).id;

		return null;
	}

	private static final class Answer {
		private boolean complete;
		private String id;
		private String text;
		private String error;
	}

	private static final class HostFile implements InputFile {

		private final String id;
		private final String text;

		private HostFile(String id, String text) {
			this.id = id;
			this.text = text;
		}

		@Override
		public InputStream newInputStream() {
			return new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
		}

		@Override
		public Reader getReader(Charset charset) {
			// The host delivered text, not bytes: there is nothing to decode.
			return new StringReader(text);
		}

		@Override
		public NFolder getParentFolder() {
			return new HostFolder(id);
		}
	}

	/**
	 * Marks the includes written in a delivered file: it carries that file's
	 * identifier, which the host receives as <code>from</code>, and nothing
	 * else. It is not a directory — only the host knows how identifiers map to
	 * directories — so the engine's own lookups through it fail as an ordinary
	 * "cannot include" rather than reaching the host with a name it would have
	 * normalised.
	 */
	private static final class HostFolder implements NFolder {

		private final String from;

		private HostFolder(String from) {
			this.from = from;
		}

		@Override
		public InputFile getInputFile(Path nameOrPath) throws IOException {
			throw new IOException("The browser engine resolves no path through a delivered file");
		}

		@Override
		public NFolder getSubfolder(Path nameOrPath) throws IOException {
			throw new IOException("The browser engine resolves no path through a delivered file");
		}
	}

	private TeaVmFileLoader() {
	}
}
