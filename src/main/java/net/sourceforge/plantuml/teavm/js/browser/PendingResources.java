/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2026, Arnaud Roques
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
package net.sourceforge.plantuml.teavm.js.browser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

import net.sourceforge.plantuml.teavm.js.GraphVizEngineJs;

/**
 * The resources a rendering needs from the JavaScript side, which only arrive
 * asynchronously: support scripts (<code>emoji.js</code>, <code>themes.js</code>,
 * <code>&lt;lib&gt;.min.js</code>...), files of the host's
 * <code>PLANTUML_FILE_LOADER</code>, and the Viz.js instance.
 *
 * <h2>Why</h2>
 *
 * The Java code of PlantUML is synchronous: an <code>!include</code> reads its
 * file and goes on. Waiting for JavaScript in the middle of it would need
 * TeaVM's coroutines (threads, <code>Object.wait()</code>, <code>@Async</code>),
 * which turn every method above the waiting point into a state machine. The
 * engine uses none of them. Instead, a rendering is an <b>attempt</b> that can
 * be thrown away:
 * <ol>
 * <li>A loader that does not have its resource yet does not wait: it notes it
 * here as missing, and fails as it fails when the resource does not exist.</li>
 * <li>At the end of the attempt, {@link RenderQueue} looks at the missing
 * resources. None: the result is delivered. Otherwise the attempt is discarded,
 * everything missing is loaded with plain JavaScript callbacks
 * ({@link #loadMissing(ScriptLoaderJs.Ok)}), and the rendering starts again
 * from the beginning.</li>
 * </ol>
 * Each round discovers the next level: the includes of an included file, a
 * theme chosen by a variable, the bundle a stdlib library links to. Parsing and
 * rendering rebuild everything from the source lines, so a discarded attempt
 * leaves nothing behind but what is kept here.
 * <p>
 * An attempt that has missed a resource does not stop at the include that
 * missed it: once {@link #hasMissing()} is true, an include, an includesub or
 * a theme that cannot be read is skipped (see
 * {@link net.sourceforge.plantuml.nio.PathSystem#isDiscardedTeaVMAttempt()}),
 * so the attempt goes on and asks for the files of the following includes as
 * well. Every file of one level is thus loaded in the same round, and a
 * diagram of many includes side by side takes one more rendering, not one per
 * file. The price is that a round may ask for a file the delivered rendering
 * does not use: when an <code>!if</code> depends on a value set in a file that
 * is not there yet, the attempt takes the branch it sees, and its includes are
 * asked for too.
 *
 * <h2>What is kept, and for how long</h2>
 *
 * What the loaders answered, success or failure, is kept for the current
 * request only ({@link #startRequest()}): the next request asks the host
 * again, so an edited file or a fixed network is seen at the next rendering.
 * A failure is kept too, so that a resource that cannot be loaded is not
 * loaded again at every round. Loaded scripts are global to the page anyway,
 * and so is the Viz.js instance.
 * <p>
 * A host that never answers does not leave the engine waiting forever: after
 * {@link #ROUND_TIMEOUT_MS} the resources still pending count as failed, with a
 * message saying so, and the rendering goes on (a late answer is ignored).
 * Nothing blocks meanwhile: the timer is a JavaScript one.
 * <p>
 * Outside an attempt (the synchronous exports of the headless build, such as
 * <code>checkSyntax</code>) nothing is noted: a resource that is not there yet
 * simply fails.
 */
public final class PendingResources {
	// ::remove file when JAVA8

	/**
	 * How many times a request may be rendered again because resources were
	 * missing. Each round loads every resource the attempt asked for, and an
	 * attempt reads on past the includes it could not read, so this is a depth
	 * (nested includes, links between libraries), not a count of files.
	 */
	public static final int MAX_ROUNDS = 20;

	/**
	 * How long a round waits for the host and the network before the resources
	 * still pending count as failed.
	 */
	public static final int ROUND_TIMEOUT_MS = 60000;

	private static final String SCRIPT = "script";
	private static final String FILE = "file";
	private static final String VIZ = "viz";

	/**
	 * The answer of a loader. A successful one has no error; for a file, it also
	 * has the identifier and text the host delivered.
	 */
	public static final class Answer {
		private final String id;
		private final String text;
		private final String error;
		private final boolean declined;

		private Answer(String id, String text, String error, boolean declined) {
			this.id = id;
			this.text = text;
			this.error = error;
			this.declined = declined;
		}

		private static Answer ok(String id, String text) {
			return new Answer(id, text, null, false);
		}

		private static Answer failed(String error) {
			return new Answer(null, null, error == null ? "" : error, false);
		}

		private static Answer declinedByHost() {
			return new Answer(null, null, null, true);
		}

		public String getId() {
			return id;
		}

		public String getText() {
			return text;
		}

		/** The reason of a failure, or <code>null</code> when the resource is there. */
		public String getError() {
			return error;
		}

		/** Whether the host declined the file (its loader returned <code>false</code>). */
		public boolean isDeclined() {
			return declined;
		}
	}

	/** A resource an attempt asked for and did not get. */
	private static final class Wanted {
		private final String type;
		private final String key;
		private final String path;
		private final String from;
		private final String kind;

		private Wanted(String type, String key, String path, String from, String kind) {
			this.type = type;
			this.key = key;
			this.path = path;
			this.from = from;
			this.kind = kind;
		}

		@Override
		public String toString() {
			if (FILE.equals(type))
				return path + (from == null ? "" : " (from " + from + ")");
			return path;
		}
	}

	/**
	 * What the current request has obtained so far, by key. A new request gets a
	 * new map rather than a cleared one, so that a late answer for an older
	 * request lands in a map nobody reads any more.
	 */
	private static Map<String, Answer> answers = new HashMap<String, Answer>();

	/** What the running attempt needed and did not find, in the order asked. */
	private static final Map<String, Wanted> missing = new LinkedHashMap<String, Wanted>();

	/** Console warnings of the running attempt, written only if it is kept. */
	private static List<String> warnings;

	private static boolean attempting;

	private PendingResources() {
	}

	// =========================================================================
	// Attempts (driven by RenderQueue)
	// =========================================================================

	/**
	 * Forgets the answers of the previous request, and a failure to create the
	 * Viz.js instance, which is tried again.
	 */
	static void startRequest() {
		answers = new HashMap<String, Answer>();
		missing.clear();
		GraphVizEngineJs.forgetFailure();
	}

	static void startAttempt() {
		missing.clear();
		warnings = new ArrayList<String>();
		attempting = true;
	}

	/**
	 * Ends the running attempt. The console warnings it produced are written if
	 * it is the one whose result is delivered, and dropped if it is discarded.
	 */
	static void endAttempt(boolean kept) {
		final List<String> pending = warnings;
		warnings = null;
		attempting = false;
		if (kept)
			for (String message : pending)
				nativeWarn(message);
	}

	/**
	 * Whether the running attempt has missed a resource, and will therefore be
	 * discarded: what it computes from now on is wasted. The includes, themes and
	 * libraries are all resolved while the diagram is parsed, so a rendering
	 * checks this after parsing and stops there (see
	 * {@code PlantUMLBrowser.buildSvg}); only Viz.js is asked for later, by the
	 * layout.
	 */
	public static boolean hasMissing() {
		return missing.isEmpty() == false;
	}

	static String describeMissing() {
		final StringBuilder sb = new StringBuilder();
		for (Wanted wanted : missing.values()) {
			if (sb.length() > 0)
				sb.append(", ");
			sb.append(wanted);
		}
		return sb.toString();
	}

	/**
	 * Writes a warning to the console. During an attempt, it is held until the
	 * attempt is known to be the one that is kept, so that a discarded attempt
	 * does not repeat it.
	 */
	public static void warn(String message) {
		if (warnings != null)
			warnings.add(message);
		else
			nativeWarn(message);
	}

	@JSBody(params = "message", script = "if (typeof console !== 'undefined' && console.warn) console.warn(message);")
	private static native void nativeWarn(String message);

	// =========================================================================
	// Lookups (called by the loaders, during an attempt or not)
	// =========================================================================

	/**
	 * What the request got for a script that is not loaded on the page: a failed
	 * answer, or <code>null</code> when it has not been loaded yet (it is then
	 * noted as missing).
	 */
	static Answer script(String url) {
		return lookup(new Wanted(SCRIPT, SCRIPT + "\n" + url, url, null, null));
	}

	/**
	 * What the host's file loader delivered for this file, or <code>null</code>
	 * when it has not been asked yet (it is then noted as missing).
	 */
	static Answer file(String path, String from, String kind) {
		return lookup(new Wanted(FILE, FILE + "\n" + path + "\n" + from + "\n" + kind, path, from, kind));
	}

	/** Notes that the Viz.js instance is needed and not ready yet. */
	public static void requireViz() {
		lookup(new Wanted(VIZ, VIZ, "Viz.js", null, null));
	}

	private static Answer lookup(Wanted wanted) {
		final Answer answer = answers.get(wanted.key);
		if (answer == null && attempting && missing.containsKey(wanted.key) == false)
			missing.put(wanted.key, wanted);
		return answer;
	}

	// =========================================================================
	// Loading (between two attempts)
	// =========================================================================

	/**
	 * Loads every resource the last attempt missed, with JavaScript callbacks,
	 * and calls <code>done</code> once all of them have answered (or after
	 * {@link #ROUND_TIMEOUT_MS}), always from a later JavaScript task, never from
	 * within this call.
	 */
	static void loadMissing(final ScriptLoaderJs.Ok done) {
		final Map<String, Answer> target = answers;
		final List<Wanted> list = new ArrayList<Wanted>(missing.values());
		missing.clear();
		final Round round = new Round(target, list.size(), done);

		for (Wanted wanted : list)
			start(round, wanted);

		round.timer = setTimeout(new ScriptLoaderJs.Ok() {
			@Override
			public void invoke() {
				round.timer = null;
				for (Wanted wanted : list)
					round.settle(wanted.key, Answer.failed("no answer within " + (ROUND_TIMEOUT_MS / 1000)
							+ " s while loading " + wanted.path));
			}
		}, ROUND_TIMEOUT_MS);
		round.armed = true;
		round.finishIfDone();
	}

	private static void start(final Round round, final Wanted wanted) {
		final String key = wanted.key;
		try {
			if (SCRIPT.equals(wanted.type)) {
				ScriptLoaderJs.loadOnce(wanted.path, () -> round.settle(key, Answer.ok(null, null)),
						message -> round.settle(key, Answer.failed(message)));
			} else if (FILE.equals(wanted.type)) {
				final boolean handled = FileLoaderJs.load(wanted.path, wanted.from, wanted.kind,
						(id, text) -> round.settle(key, Answer.ok(id, text)),
						reason -> round.settle(key, Answer.failed(reason)));
				if (handled == false)
					round.settle(key, Answer.declinedByHost());
			} else {
				GraphVizEngineJs.prepare(() -> round.settle(key, Answer.ok(null, null)));
			}
		} catch (Throwable t) {
			round.settle(key, Answer.failed(String.valueOf(t)));
		}
	}

	/** One loading round: the answers it waits for, and what to do after. */
	private static final class Round {
		private final Map<String, Answer> target;
		private final ScriptLoaderJs.Ok done;
		private int pending;
		private boolean armed;
		private boolean finished;
		private JSObject timer;

		private Round(Map<String, Answer> target, int pending, ScriptLoaderJs.Ok done) {
			this.target = target;
			this.pending = pending;
			this.done = done;
		}

		/** Records the first answer for a key; any later one is ignored. */
		private void settle(String key, Answer answer) {
			if (finished || target.containsKey(key))
				return;
			target.put(key, answer);
			pending--;
			finishIfDone();
		}

		private void finishIfDone() {
			// Not before every loader has been started and the timer armed: an answer
			// given synchronously must not end the round while it is being set up.
			if (finished || armed == false || pending > 0)
				return;
			finished = true;
			if (timer != null)
				clearTimeout(timer);
			timer = null;
			later(done);
		}
	}

	@JSBody(params = { "callback", "ms" }, script = "return setTimeout(function() { callback(); }, ms);")
	private static native JSObject setTimeout(ScriptLoaderJs.Ok callback, int ms);

	@JSBody(params = "timer", script = "clearTimeout(timer);")
	private static native void clearTimeout(JSObject timer);

	/** Runs the callback from a later task, never from the current one. */
	@JSBody(params = "callback", script = "Promise.resolve().then(function() { callback(); });")
	private static native void later(ScriptLoaderJs.Ok callback);

}
