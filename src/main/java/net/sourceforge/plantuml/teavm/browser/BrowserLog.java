package net.sourceforge.plantuml.teavm.browser;

import org.teavm.jso.JSBody;

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
		// ::uncomment when __TEAVM__
		// final String message = getMessage(msg);
		// jsLog(message);
		// ::done
	}

	private static String getMessage(String msg) {
		final long durationMs = System.currentTimeMillis() - START;
		return String.format("[%6d ms] %s", durationMs, msg);
	}

	public static void jsStatusDuration() {
		final String msg = START == 0 ? "" : Version.fullDescription();
		jsStatus(getMessage(msg));
	}

	@JSBody(params = "msg", script = "console.log(msg);")
	public static native void jsLog(String msg);

	@JSBody(params = "msg", script = //
	"console.log(msg);" + //
			"var el = document.getElementById('status');" + //
			"if (el) { el.textContent = msg; }" //
	)
	public static native void jsStatus(String msg);

}
