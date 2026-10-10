package net.sourceforge.plantuml.teavm.browser;

// ::comment when JAVA8
import org.teavm.jso.JSBody;
// ::done

import net.sourceforge.plantuml.teavm.TeaVM;
import net.sourceforge.plantuml.version.Version;

public class BrowserLog {

	private static long START = System.currentTimeMillis();

	public static void reset() {
		START = System.currentTimeMillis();
		consoleMessage("==============================");
	}

	public static void consoleLog(Class<?> clazz, String msg) {
		consoleMessage("[" + clazz.getSimpleName() + "] " + msg);
	}

	private static void consoleMessage(String msg) {
		// ::comment when JAVA8
		if (TeaVM.isTeaVM() && isDebug()) {
			final String message = getMessage(msg);
			jsLog(message);
		}
		// ::done
	}

	private static String getMessage(String msg) {
		final long durationMs = System.currentTimeMillis() - START;
		return String.format("[%6d ms] %s", durationMs, msg);
	}

	public static void jsStatusDuration() {
		// ::comment when JAVA8
		if (isDebug() == false)
			return;
		final String msg = START == 0 ? "" : Version.fullDescription();
		jsStatus(getMessage(msg));
		// ::done
	}

	// ::comment when JAVA8
	/**
	 * Debug output (console lines, and the text written into the element with
	 * {@code id="status"}) is opt-in: the host sets
	 * {@code globalThis.PLANTUML_DEBUG = true} before rendering. By default the
	 * engine is silent and leaves the host page's DOM alone.
	 */
	@JSBody(script = "return globalThis.PLANTUML_DEBUG === true;")
	public static native boolean isDebug();

	@JSBody(params = "msg", script = "console.log(msg);")
	public static native void jsLog(String msg);

	@JSBody(params = "msg", script = //
	"console.log(msg);" + //
			"var el = document.getElementById('status');" + //
			"if (el) { el.textContent = msg; }" //
	)
	public static native void jsStatus(String msg);
	// ::done

}
