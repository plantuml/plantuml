package net.sourceforge.plantuml.teavm.browser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

import net.sourceforge.plantuml.nio.InputFile;
import net.sourceforge.plantuml.nio.NFolder;

/**
 * Reads the file of a local <code>!include</code>, <code>!include_once</code>
 * or <code>!include_many</code> in the browser engine, through a function the
 * host provides.
 * <p>
 * The browser engine has no file system, so on its own such an include can
 * only fail. A host that does have files (an editor extension, a desktop
 * application, a documentation build running the engine) sets
 * <code>PLANTUML_FILE_LOADER</code> on the global object:
 *
 * <pre>
 * globalThis.PLANTUML_FILE_LOADER = function (path, from, onOk, onErr) { ... };
 * </pre>
 * <ul>
 * <li><code>path</code>: the file name written after the directive, with
 * variables expanded.</li>
 * <li><code>from</code>: <code>null</code> for an include written in the
 * diagram itself; otherwise the identifier the host gave to the file that
 * contains the include, so that a relative name can be resolved against that
 * file rather than the diagram.</li>
 * <li><code>onOk(id, text)</code>: delivers the file. <code>id</code> is any
 * string that identifies it for the host (typically its absolute path or URI).
 * <code>!include_once</code> compares identifiers to recognise a file included
 * twice, and the includes written in that file receive it as
 * <code>from</code>.</li>
 * <li><code>onErr(message)</code>: the include fails. The message is written
 * to the console.</li>
 * </ul>
 * Returning <code>false</code> (strictly), without calling either callback,
 * declines the file, which then fails as if no loader were set. The engine
 * never reads anything itself: which files may be read is decided by the host.
 * <p>
 * The same shape as <code>PLANTUML_STDLIB_LOADER</code> in
 * {@link TeaVmScriptLoader}. With no loader set, local includes fail exactly
 * as before.
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
	 * it. A missing identifier, a text that is not a string or an exception
	 * becomes a failure, and any call to a callback after the first is ignored,
	 * so the waiting side is released exactly once.
	 */
	@JSBody(params = { "path", "from", "onOk", "onErr" }, script = "var g = (typeof globalThis !== 'undefined') ? globalThis"
			+ " : ((typeof self !== 'undefined') ? self : this);"
			+ "var settled = false;"
			+ "var fail = function(message) {"
			+ "  if (settled) return; settled = true;"
			+ "  onErr(message ? String(message) : ('PLANTUML_FILE_LOADER failed for ' + path));"
			+ "};"
			+ "var ok = function(id, text) {"
			+ "  if (settled) return;"
			+ "  if (typeof id !== 'string' || id === '' || typeof text !== 'string') {"
			+ "    fail('PLANTUML_FILE_LOADER must pass a non-empty string id and a string text for ' + path); return;"
			+ "  }"
			+ "  settled = true; onOk(id, text);"
			+ "};"
			+ "try {"
			+ "  return g.PLANTUML_FILE_LOADER(path, from, ok, fail) !== false;"
			+ "} catch (e) { fail(e && e.message ? e.message : e); return true; }")
	private static native boolean load(String path, String from, Loaded onOk, TeaVmScriptLoader.Err onErr);

	/**
	 * The file the host delivers for <code>path</code>, or <code>null</code> when
	 * no loader is set, when the host declines, or when it fails (the reason is
	 * written to the console, and the include then reports the usual "cannot
	 * include"). Blocks until the host answers, so it MUST be called from a
	 * TeaVM thread context, like {@link TeaVmScriptLoader#loadOnceSync(String)}.
	 *
	 * @param path   the file name written after the directive
	 * @param folder where the include is written: <code>null</code> for the
	 *               diagram itself, or the folder of a file this loader
	 *               delivered
	 */
	public static InputFile getInputFile(String path, NFolder folder) {
		if (isAvailable() == false)
			return null;

		final String from = folder instanceof HostFolder ? ((HostFolder) folder).from : null;
		final Answer answer = new Answer();
		synchronized (answer) {
			final boolean handled = load(path, from, (id, text) -> {
				synchronized (answer) {
					answer.id = id;
					answer.text = text;
					answer.complete = true;
					answer.notify();
				}
			}, (message) -> {
				synchronized (answer) {
					answer.error = message;
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
	 * What <code>!include_once</code> compares to recognise a file this loader
	 * delivered when it is included again: the host's identifier, as a
	 * {@link File} so that it sits with the files the Java build records.
	 * <code>null</code> for any other file.
	 */
	public static File getIdentity(InputFile file) {
		if (file instanceof HostFile)
			return new File(((HostFile) file).id);

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
		public NFolder getParentFolder() {
			return new HostFolder(id);
		}
	}

	/**
	 * The folder of a delivered file. It holds the identifier of that file, not
	 * a directory: only the host knows how identifiers map to directories, so
	 * the engine hands the identifier back as <code>from</code> and lets the
	 * host resolve the name.
	 */
	private static final class HostFolder implements NFolder {

		private final String from;

		private HostFolder(String from) {
			this.from = from;
		}

		@Override
		public InputFile getInputFile(Path nameOrPath) {
			return TeaVmFileLoader.getInputFile(nameOrPath.toString(), this);
		}

		@Override
		public NFolder getSubfolder(Path nameOrPath) {
			throw new UnsupportedOperationException("The browser engine does not change directory through a file loader");
		}
	}

	private TeaVmFileLoader() {
	}
}
