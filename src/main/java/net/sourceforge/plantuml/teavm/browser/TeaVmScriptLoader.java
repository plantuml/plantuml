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
	 * stored on window.
	 */
	@JSBody(params = { "url", "onOk", "onErr" }, script = "var w = window;"
			+ "w.__pl_script_state = w.__pl_script_state || Object.create(null);" + "var st = w.__pl_script_state[url];"
			+

			"if (st && st.state === 'loaded') { onOk(); return; }"
			+ "if (st && st.state === 'loading') { st.ok.push(onOk); st.err.push(onErr); return; }" +

			"st = w.__pl_script_state[url] = { state: 'loading', ok: [onOk], err: [onErr] };" +

			"var s = document.createElement('script');" + "s.src = url;" + "s.async = true;" +

			"s.onload = function() {" + "  st.state = 'loaded';" + "  var list = st.ok; st.ok = []; st.err = [];"
			+ "  for (var i = 0; i < list.length; i++) list[i]();" + "};" +

			"s.onerror = function() {" + "  st.state = 'error';" + "  var list = st.err; st.ok = []; st.err = [];"
			+ "  for (var i = 0; i < list.length; i++) list[i]('Failed to load ' + url);" + "};" +

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
			"path" }, script = "var ns = window.PLANTUML_STDLIB && window.PLANTUML_STDLIB[namespace];"
					+ "return (ns && ns[path]) || null;")
	public static native JSObject getRaw_PLANTUML_STDLIB(String namespace, String path);

	/**
	 * 
	 * @param namespace
	 * @param path
	 * @return
	 */
	@JSBody(params = { "namespace",
			"path" }, script = "var ns = window.PLANTUML_STDLIB_JSON && window.PLANTUML_STDLIB_JSON[namespace];"
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
	@JSBody(params = "namespace", script = "return (window.PLANTUML_STDLIB_INFO && window.PLANTUML_STDLIB_INFO[namespace]) || null;")
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

	@JSBody(params = "url", script = "var st = window.__pl_script_state && window.__pl_script_state[url];"
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
