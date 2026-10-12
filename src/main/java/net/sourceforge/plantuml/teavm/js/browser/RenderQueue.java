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
import java.util.List;

import org.teavm.jso.JSBody;

/**
 * The render requests of the JavaScript engines, answered one at a time, in
 * the order received, without any thread.
 *
 * <p>
 * A request is rendered by attempts (see {@link PendingResources}): an attempt
 * that missed resources is discarded, the resources are loaded with JavaScript
 * callbacks, and the request is rendered again, up to
 * {@link PendingResources#MAX_ROUNDS} times. Only the attempt that is kept is
 * delivered: a {@link Job} writes nothing (DOM, callbacks) before
 * {@link Job#deliver()}.
 *
 * <p>
 * Rules:
 * <ul>
 * <li>First in, first out. While the head request waits for its resources, the
 * others wait behind it.</li>
 * <li>A job may replace a waiting one ({@link Job#replaces(Job)}), in place:
 * this is the "latest wins" of <code>render</code>. A job waiting for its
 * resources can be replaced as well; its loads are then wasted.</li>
 * <li>Re-entrance: a request is never rendered from the call that makes it,
 * but from a later JavaScript task. A request made while the queue is working
 * (from a callback of a delivered request, typically) is queued, and answered
 * after the ones already waiting, never in the middle of another
 * rendering.</li>
 * <li>Nothing thrown by a job stops the queue.</li>
 * </ul>
 */
public final class RenderQueue {
	// ::remove file when JAVA8

	/** One request. */
	public static abstract class Job {

		private int rounds;

		/**
		 * Renders the request once. It must keep its result for {@link #deliver()}
		 * and change nothing outside: it may be discarded.
		 */
		protected abstract void attempt();

		/** Delivers the result of the last attempt. Called once. */
		protected abstract void deliver();

		/**
		 * Delivers a failure instead of the result: resources were still missing
		 * after {@link PendingResources#MAX_ROUNDS} rounds. Called once, instead of
		 * {@link #deliver()}.
		 */
		protected abstract void giveUp(String message);

		/** Whether this job replaces <code>waiting</code>, a job not answered yet. */
		protected boolean replaces(Job waiting) {
			return false;
		}
	}

	/** The requests not answered yet, oldest first. */
	private static final List<Job> QUEUE = new ArrayList<Job>();

	/** Whether {@link #pump()} is on the stack. */
	private static boolean running;

	/** Whether the head of the queue waits for its resources. */
	private static boolean loading;

	/** Whether {@link #pump()} is already scheduled. */
	private static boolean scheduled;

	private RenderQueue() {
	}

	/**
	 * Queues a request. It is answered from a later JavaScript task, never from
	 * this call: the caller gets its answer after it has returned, as when a
	 * worker thread answered, and several calls made in the same task are all
	 * queued before the first one is rendered (which is what lets a later
	 * {@code render} replace an earlier one).
	 */
	public static void submit(Job job) {
		boolean replaced = false;
		for (int i = 0; i < QUEUE.size() && replaced == false; i++)
			if (job.replaces(QUEUE.get(i))) {
				QUEUE.set(i, job);
				replaced = true;
			}
		if (replaced == false)
			QUEUE.add(job);
		schedule();
	}

	private static void schedule() {
		if (scheduled || running || loading)
			return;
		scheduled = true;
		defer(() -> {
			scheduled = false;
			pump();
		});
	}

	private static void pump() {
		if (running || loading)
			return;
		running = true;
		try {
			while (QUEUE.isEmpty() == false) {
				final Job job = QUEUE.get(0);
				if (job.rounds == 0)
					PendingResources.startRequest();

				PendingResources.startAttempt();
				try {
					job.attempt();
				} catch (Throwable t) {
					// Jobs catch their own failures; this is a last resort, which must not
					// leave the queue stuck.
					consoleError("PlantUML: request failed: " + t);
				}

				final boolean missing = PendingResources.hasMissing();
				if (missing && job.rounds < PendingResources.MAX_ROUNDS) {
					PendingResources.endAttempt(false);
					job.rounds++;
					loading = true;
					PendingResources.loadMissing(() -> {
						loading = false;
						pump();
					});
					return;
				}

				final String unresolved = missing ? PendingResources.describeMissing() : null;
				PendingResources.endAttempt(true);
				QUEUE.remove(0);
				try {
					if (unresolved == null)
						job.deliver();
					else
						job.giveUp("Rendering abandoned after " + PendingResources.MAX_ROUNDS
								+ " rounds of loading resources; still missing: " + unresolved);
				} catch (Throwable t) {
					consoleError("PlantUML: request failed: " + t);
				}
			}
		} finally {
			running = false;
		}
	}

	@JSBody(params = "callback", script = "setTimeout(function() { callback(); }, 0);")
	private static native void defer(ScriptLoaderJs.Ok callback);

	/** Reports an error to the console, whatever {@code PLANTUML_DEBUG} says. */
	@JSBody(params = "msg", script = "console.error(msg);")
	static native void consoleError(String msg);

}
