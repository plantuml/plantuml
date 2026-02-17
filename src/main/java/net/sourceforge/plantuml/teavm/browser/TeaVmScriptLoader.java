package net.sourceforge.plantuml.teavm.browser;

//::uncomment when __TEAVM__
//import org.teavm.jso.JSBody;
//import org.teavm.jso.JSFunctor;
//import org.teavm.jso.JSObject;
//::done

public final class TeaVmScriptLoader {

 // ::uncomment when __TEAVM__

// private static final Object LOCK = new Object();
// private static volatile boolean loadSuccess;
// private static volatile String loadError;
// private static volatile boolean loadComplete;
//
// @JSFunctor
// public interface Ok extends JSObject {
//     void invoke();
// }
//
// @JSFunctor
// public interface Err extends JSObject {
//     void invoke(String message);
// }
//
// /**
//  * Loads a JS file once. Multiple concurrent calls are coalesced.
//  * The state is stored on window.
//  */
// @JSBody(params = { "url", "onOk", "onErr" }, script =
//         "var w = window;" +
//         "w.__pl_script_state = w.__pl_script_state || Object.create(null);" +
//         "var st = w.__pl_script_state[url];" +
//
//         "if (st && st.state === 'loaded') { onOk(); return; }" +
//         "if (st && st.state === 'loading') { st.ok.push(onOk); st.err.push(onErr); return; }" +
//
//         "st = w.__pl_script_state[url] = { state: 'loading', ok: [onOk], err: [onErr] };" +
//
//         "var s = document.createElement('script');" +
//         "s.src = url;" +
//         "s.async = true;" +
//
//         "s.onload = function() {" +
//         "  st.state = 'loaded';" +
//         "  var list = st.ok; st.ok = []; st.err = [];" +
//         "  for (var i = 0; i < list.length; i++) list[i]();" +
//         "};" +
//
//         "s.onerror = function() {" +
//         "  st.state = 'error';" +
//         "  var list = st.err; st.ok = []; st.err = [];" +
//         "  for (var i = 0; i < list.length; i++) list[i]('Failed to load ' + url);" +
//         "};" +
//
//         "document.head.appendChild(s);"
// )
// public static native void loadOnce(String url, Ok onOk, Err onErr);
//
// 
//	@JSBody(params = { "namespace", "path" }, script =
//			"var ns = window.PLANTUML_STDLIB && window.PLANTUML_STDLIB[namespace];" +
//			"return (ns && ns[path]) || null;")
//	public static native JSObject getRaw(String namespace, String path);
//
//	@JSBody(params = "lines", script = "return lines.join('\\n');")
//	public static native String joinLines(JSObject lines);
//
//	@JSBody(params = "url", script =
//			"var st = window.__pl_script_state && window.__pl_script_state[url];" +
//			"return !!(st && st.state === 'loaded');")
//	private static native boolean isLoaded(String url);
//
//	/**
//	 * Loads a script synchronously. Blocks until the script is loaded.
//	 * MUST be called from a TeaVM thread context (not from native JS).
//	 */
//	public static void loadOnceSync(String url) {
//		// Fast path: already loaded
//		if (isLoaded(url))
//			return;
//
//		synchronized (LOCK) {
//			loadComplete = false;
//			loadSuccess = false;
//			loadError = null;
//
//			loadOnce(url,
//				() -> {
//					synchronized (LOCK) {
//						loadSuccess = true;
//						loadComplete = true;
//						LOCK.notify();
//					}
//				},
//				(msg) -> {
//					synchronized (LOCK) {
//						loadSuccess = false;
//						loadError = msg;
//						loadComplete = true;
//						LOCK.notify();
//					}
//				}
//			);
//
//			while (!loadComplete) {
//				try {
//					LOCK.wait();
//				} catch (InterruptedException e) {
//					// retry
//				}
//			}
//
//			if (!loadSuccess)
//				throw new RuntimeException(loadError);
//		}
//	}

 // ::done

 private TeaVmScriptLoader() {}
}


