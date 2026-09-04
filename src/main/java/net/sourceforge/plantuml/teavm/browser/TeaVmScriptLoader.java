package net.sourceforge.plantuml.teavm.browser;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

public final class TeaVmScriptLoader {
	// ::remove file when JAVA8

	private static final Object LOCK = new Object();
	private static volatile boolean loadSuccess;
	private static volatile String loadError;
	private static volatile boolean loadComplete;

	@JSFunctor
	public interface Ok extends JSObject {
		void invoke();
	}

	@JSFunctor
	public interface Err extends JSObject {
		void invoke(String message);
	}

	/**
	 * Loads a JS file once. Multiple concurrent calls are coalesced. The state is
	 * stored on the global object.
	 * <p>
	 * Every lazily loaded support script comes through here: the stdlib bundles
	 * ({@code <lib>.min.js}) and also {@code themes.js}, {@code emoji.js} and
	 * {@code openiconic.js}, which all have the same problem this method's two
	 * host-provided globals solve. They are checked in this order:
	 * <ul>
	 * <li>{@code PLANTUML_STDLIB_LOADER}: a function {@code (url, onOk, onErr)}
	 * that delivers the script's content itself, in whatever way fits the host.
	 * For a stdlib bundle it populates {@code PLANTUML_STDLIB} /
	 * {@code PLANTUML_STDLIB_JSON} / {@code PLANTUML_STDLIB_INFO} for the
	 * library before calling {@code onOk}, or calls {@code onErr} with a
	 * message. Returning {@code false} (strictly) declines the URL, and loading
	 * proceeds through the script tag as if no hook were set, so a host that
	 * only handles stdlib bundles can decline {@code themes.js} and friends.
	 * The hook is the only way to load these files where a script tag cannot
	 * work: a Web Worker (no document), a browser extension that may fetch
	 * remote data but not execute remote code, or a non-browser runtime.
	 * Mirrors how a host can pre-populate {@code PLANTUML_THEMES} for
	 * {@link #getTheme(String)}.</li>
	 * <li>{@code PLANTUML_STDLIB_BASE}: a URL prefix for the script tag, so a
	 * page that imports the engine from a CDN can point the loading of all of
	 * these files at wherever they are hosted. Without it the URL stays
	 * relative, which resolves against the consuming document, not the
	 * engine's location.</li>
	 * </ul>
	 * A host may instead pre-populate the globals a script would have set and
	 * mark {@code __pl_script_state[url] = { state: 'loaded' }}; the fast path
	 * then skips loading entirely. With neither global set, the behaviour is
	 * exactly what it always was.
	 */
	@JSBody(params = { "url", "onOk", "onErr" }, script = "var w = (typeof globalThis !== 'undefined') ? globalThis"
			+ " : ((typeof self !== 'undefined') ? self : window);"
			+ "w.__pl_script_state = w.__pl_script_state || Object.create(null);" + "var st = w.__pl_script_state[url];"
			+

			"if (st && st.state === 'loaded') { onOk(); return; }"
			+ "if (st && st.state === 'loading') { st.ok.push(onOk); st.err.push(onErr); return; }" +

			"st = w.__pl_script_state[url] = { state: 'loading', ok: [onOk], err: [onErr] };" +

			"var ok = function() {" + "  st.state = 'loaded';" + "  var list = st.ok; st.ok = []; st.err = [];"
			+ "  for (var i = 0; i < list.length; i++) list[i]();" + "};" +

			"var fail = function(message) {" + "  st.state = 'error';" + "  var list = st.err; st.ok = []; st.err = [];"
			+ "  for (var i = 0; i < list.length; i++) list[i](message);" + "};" +

			"if (typeof w.PLANTUML_STDLIB_LOADER === 'function') {"
			+ "  var handled = w.PLANTUML_STDLIB_LOADER(url, ok, function(message) { fail(message || ('Loader failed for ' + url)); });"
			+ "  if (handled !== false) return;" + "}" +

			"var full = (typeof w.PLANTUML_STDLIB_BASE === 'string') ? (w.PLANTUML_STDLIB_BASE + url) : url;" +

			"var s = document.createElement('script');" + "s.src = full;" + "s.async = true;" +

			"s.onload = ok;" +

			"s.onerror = function() { fail('Failed to load ' + full); };" +

			"document.head.appendChild(s);")
	public static native void loadOnce(String url, Ok onOk, Err onErr);

	/**
	 * Retrieves the raw lines array for a .puml file from a loaded stdlib library.
	 *
	 * @param namespace the library name (e.g. "aws", "c4")
	 * @param path      the relative path within the library (e.g. "compute/ec2")
	 * @return the JS array of lines, or null if not found
	 */
	@JSBody(params = { "namespace",
			"path" }, script = "var g = (typeof globalThis !== 'undefined') ? globalThis"
					+ " : ((typeof self !== 'undefined') ? self : this);"
					+ "var ns = g.PLANTUML_STDLIB && g.PLANTUML_STDLIB[namespace];"
					+ "return (ns && ns[path]) || null;")
	public static native JSObject getRaw_PLANTUML_STDLIB(String namespace, String path);

	/**
	 * Retrieves the raw text of a bundled theme, as published by themes.js into
	 * the PLANTUML_THEMES map.
	 * <p>
	 * Reads from the global object rather than from window, so that a host which
	 * pre-populates PLANTUML_THEMES (on globalThis) can use themes inside a Web
	 * Worker, where there is no document to append a script tag to, or in a
	 * non-browser JS runtime.
	 * @param name the theme name (e.g. "amiga")
	 * @return the whole .puml theme file as a string, or null if not present
	 */
	@JSBody(params = "name", script = "var g = (typeof globalThis !== 'undefined') ? globalThis"
			+ " : ((typeof self !== 'undefined') ? self : this);"
			+ "var t = g && g.PLANTUML_THEMES;" + "return (t && t[name]) ? t[name] : null;")
	public static native String getTheme(String name);

	/**
	 * Whether the PLANTUML_THEMES map exists at all, regardless of its content.
	 * <p>
	 * This is what distinguishes "themes.js was never loaded", which is a problem
	 * with the page's deployment, from "themes.js is loaded but has no theme of
	 * that name", which is a typo in the diagram text.
	 */
	@JSBody(params = {}, script = "var g = (typeof globalThis !== 'undefined') ? globalThis"
			+ " : ((typeof self !== 'undefined') ? self : this);"
			+ "return !!(g && g.PLANTUML_THEMES);")
	public static native boolean hasThemes();

	/**
	 * Writes a warning to the browser console, where the page author will see it.
	 */
	@JSBody(params = "message", script = "if (typeof console !== 'undefined' && console.warn) console.warn(message);")
	public static native void consoleWarn(String message);

	/**
	 * 
	 * @param namespace
	 * @param path
	 * @return
	 */
	@JSBody(params = { "namespace",
			"path" }, script = "var g = (typeof globalThis !== 'undefined') ? globalThis"
					+ " : ((typeof self !== 'undefined') ? self : this);"
					+ "var ns = g.PLANTUML_STDLIB_JSON && g.PLANTUML_STDLIB_JSON[namespace];"
					+ "return (ns && ns[path]) || null;")
	public static native JSObject getRaw_PLANTUML_STDLIB_JSON(String namespace, String path);

	/**
	 * Retrieves the JSON info object for a loaded stdlib library.
	 * <p>
	 * This reads from {@code window.PLANTUML_STDLIB_INFO[namespace]}, which is
	 * populated by the generated JS files with metadata from each library's
	 * README.md YAML header (name, version, etc.).
	 *
	 * @param namespace the library name (e.g. "aws", "c4")
	 * @return the JS info object, or null if not found
	 */
	// Mirrors getRaw_PLANTUML_STDLIB but for the INFO metadata map
	@JSBody(params = "namespace", script = "var g = (typeof globalThis !== 'undefined') ? globalThis"
			+ " : ((typeof self !== 'undefined') ? self : this);"
			+ "return (g.PLANTUML_STDLIB_INFO && g.PLANTUML_STDLIB_INFO[namespace]) || null;")
	public static native JSObject getRaw_PLANTUML_STDLIB_INFO(String namespace);

	/**
	 * Returns the keys of a JS object as a comma-separated string.
	 * <p>
	 * Useful for iterating over properties of a JSObject from Java side, since
	 * TeaVM does not allow direct enumeration of JS object keys.
	 *
	 * @param obj a JS object
	 * @return comma-separated keys, or empty string if null/empty
	 */
	@JSBody(params = "obj", script = "return obj ? Object.keys(obj).join(',') : '';")
	public static native String getObjectKeys(JSObject obj);

	/**
	 * Reads a single string property from a JS object by key.
	 *
	 * @param obj a JS object
	 * @param key the property name
	 * @return the property value as a string, or null
	 */
	@JSBody(params = { "obj", "key" }, script = "return (obj && obj[key] != null) ? String(obj[key]) : null;")
	public static native String getStringProperty(JSObject obj, String key);

	@JSBody(params = "lines", script = "return lines.join('\\n');")
	public static native String joinLines(JSObject lines);

	/**
	 * Serializes a JS object into a JSON string using the native
	 * {@code JSON.stringify}.
	 *
	 * @param obj a JS object
	 * @return the JSON string representation, or null if the object is null
	 */
	@JSBody(params = "obj", script = "return obj == null ? null : JSON.stringify(obj);")
	public static native String stringify(JSObject obj);

	@JSBody(params = "url", script = "var g = (typeof globalThis !== 'undefined') ? globalThis"
			+ " : ((typeof self !== 'undefined') ? self : this);"
			+ "var st = g.__pl_script_state && g.__pl_script_state[url];"
			+ "return !!(st && st.state === 'loaded');")
	private static native boolean isLoaded(String url);

	/**
	 * Loads a script synchronously. Blocks until the script is loaded. MUST be
	 * called from a TeaVM thread context (not from native JS).
	 */
	public static void loadOnceSync(String url) {
		// Fast path: already loaded
		if (isLoaded(url))
			return;

		synchronized (LOCK) {
			loadComplete = false;
			loadSuccess = false;
			loadError = null;

			loadOnce(url, () -> {
				synchronized (LOCK) {
					loadSuccess = true;
					loadComplete = true;
					LOCK.notify();
				}
			}, (msg) -> {
				synchronized (LOCK) {
					loadSuccess = false;
					loadError = msg;
					loadComplete = true;
					LOCK.notify();
				}
			});

			while (!loadComplete) {
				try {
					LOCK.wait();
				} catch (InterruptedException e) {
					// retry
				}
			}

			if (!loadSuccess)
				throw new RuntimeException(loadError);
		}
	}

	private TeaVmScriptLoader() {
	}
}
