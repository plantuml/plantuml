/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
 */
package net.sourceforge.plantuml.teavm.js;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

import net.sourceforge.plantuml.teavm.js.browser.PendingResources;
import net.sourceforge.plantuml.teavm.js.browser.ScriptLoaderJs;

/**
 * Renders DOT graphs to SVG with Viz.js, synchronously.
 * <p>
 * In {@code @viz-js/viz} 3.x, only {@code Viz.instance()} is asynchronous (it
 * loads the WebAssembly module); {@code renderString} on the instance is
 * synchronous. So the instance is created once, from JavaScript
 * ({@link #prepare(ScriptLoaderJs.Ok)}), and kept on the global object as
 * {@code __plantumlVizInstance}; rendering then just calls it. Nothing waits
 * in Java: when the instance is not ready yet, rendering notes it as missing
 * and fails, and the diagram is rendered again once it is ready (see
 * {@link PendingResources}).
 * <p>
 * Usage:
 * <pre>
 * String svg = GraphVizEngineJs.renderDotToSvg("digraph G { A -> B; }");
 * </pre>
 */
public class GraphVizEngineJs {

	/**
	 * Where the Viz.js object comes from. A host that bundles viz-global.js (a UMD)
	 * into its own file does not get a global {@code Viz}: the UMD attaches it to
	 * the bundle's exports. Such a host sets {@code globalThis.PLANTUML_VIZ = Viz}
	 * itself; it takes precedence over the global {@code Viz} that loading
	 * viz-global.js with a script tag defines.
	 */
	private static final String FIND_VIZ_STMT = "var V = globalThis.PLANTUML_VIZ || (typeof Viz !== 'undefined' ? Viz : null);";

	private static final String NOT_LOADED = "Viz is not loaded: this diagram type needs the Graphviz layout engine (viz-global.js). Load viz-global.js before rendering.";

	// ::remove file when JAVA8

	/**
	 * True when the Viz.js bridge (viz-global.js) is NOT loaded on the page,
	 * so the caller should fall back to the Smetana layout engine. Logs a
	 * one-time console note the first time the fallback is taken.
	 * <p>
	 * A Viz.js that is loaded but whose instance cannot be created (the
	 * WebAssembly module fails to load) is not a fallback: the diagram reports
	 * that failure, naming Viz.
	 */
	@JSBody(script = FIND_VIZ_STMT + "var missing = !V || typeof V.instance !== 'function';" +
			"if (missing && !globalThis.__plantumlSmetanaFallbackNoted) {" +
			"  globalThis.__plantumlSmetanaFallbackNoted = true;" +
			"  console.info('PlantUML: viz-global.js is not loaded, falling back to the Smetana layout engine');" +
			"}" +
			"return missing;")
	public static native boolean vizMissingFallback();

	/**
	 * Whether a layout can call {@link #renderDotToSvg(String)} now: the
	 * instance is there, or creating it failed (which {@code renderDotToSvg}
	 * then reports). When it is not there yet, it is noted as missing and this
	 * returns <code>false</code>: the attempt will be discarded and rendered
	 * again once the instance is ready, so the caller should stop at once rather
	 * than compute a layout for nothing.
	 */
	public static boolean isAvailable() {
		if (isReady() || getFailure(NOT_LOADED) != null)
			return true;
		PendingResources.requireViz();
		return false;
	}

	/**
	 * Forgets why creating the instance failed, so that the next request tries
	 * again: a network failure while loading the WebAssembly module may be
	 * transient. Called at the start of each request.
	 */
	@JSBody(script = "delete globalThis.__plantumlVizError;")
	public static native void forgetFailure();

	/**
	 * Renders a DOT graph description to SVG format, with the {@code dot}
	 * engine.
	 *
	 * @param dotSource the graph description in DOT format
	 * @return the rendered SVG as a string
	 * @throws RuntimeException when Viz.js is not loaded, failed to load, is
	 *                          still loading, or fails on this graph
	 */
	public static String renderDotToSvg(String dotSource) {
		return renderDotToSvg(dotSource, "dot");
	}

	/**
	 * Renders a DOT graph using a specific GraphViz engine.
	 *
	 * @param dotSource the graph description in DOT format
	 * @param engine    the GraphViz engine to use (dot, neato, fdp, sfdp, twopi, circo)
	 * @return the rendered SVG as a string
	 * @throws RuntimeException when Viz.js is not loaded, failed to load, is
	 *                          still loading, or fails on this graph
	 */
	public static String renderDotToSvg(String dotSource, String engine) {
		if (isReady() == false) {
			final String failure = getFailure(NOT_LOADED);
			if (failure != null)
				throw new RuntimeException(failure);
			// The instance is being created: this attempt is discarded, and the diagram
			// is rendered again once it is ready.
			PendingResources.requireViz();
			throw new RuntimeException("Viz is loading");
		}
		final JSObject result = renderString(dotSource, engine);
		final String error = getProperty(result, "error");
		if (error != null)
			throw new RuntimeException(error);
		return getProperty(result, "svg");
	}

	/** Whether the Viz.js instance has been created. */
	@JSBody(script = "return !!globalThis.__plantumlVizInstance;")
	private static native boolean isReady();

	/**
	 * Why there is no instance: Viz.js is not on the page, or creating the
	 * instance failed; <code>null</code> when it is being created, or was never
	 * asked for.
	 */
	@JSBody(params = "notLoaded", script = FIND_VIZ_STMT
			+ "if (!V || typeof V.instance !== 'function') return notLoaded;"
			+ "var e = globalThis.__plantumlVizError;"
			+ "return (typeof e === 'string') ? e : null;")
	private static native String getFailure(String notLoaded);

	/**
	 * Creates the Viz.js instance, once, and calls <code>done</code> when it is
	 * there or when creating it failed (the reason is then kept as
	 * {@code __plantumlVizError}, until the next request: see
	 * {@link #forgetFailure()}), or at once when there is nothing to wait for.
	 * Concurrent calls share the same creation.
	 */
	@JSBody(params = "done", script = FIND_VIZ_STMT
			+ "var g = globalThis;"
			+ "if (g.__plantumlVizInstance || typeof g.__plantumlVizError === 'string'"
			+ "    || !V || typeof V.instance !== 'function') { done(); return; }"
			+ "if (g.__plantumlVizWaiting) { g.__plantumlVizWaiting.push(done); return; }"
			+ "g.__plantumlVizWaiting = [done];"
			+ "var flush = function() {"
			+ "  var list = g.__plantumlVizWaiting; g.__plantumlVizWaiting = null;"
			+ "  for (var i = 0; i < list.length; i++) list[i]();"
			+ "};"
			+ "try {"
			+ "  Promise.resolve(V.instance()).then(function(viz) {"
			+ "    if (viz && typeof viz.renderString === 'function') g.__plantumlVizInstance = viz;"
			+ "    else g.__plantumlVizError = 'Viz.instance() did not give a Viz instance';"
			+ "    flush();"
			+ "  }, function(err) {"
			+ "    g.__plantumlVizError = String(err);"
			+ "    flush();"
			+ "  });"
			+ "} catch (e) {"
			+ "  g.__plantumlVizError = String(e);"
			+ "  flush();"
			+ "}")
	public static native void prepare(ScriptLoaderJs.Ok done);

	/**
	 * Renders with the instance, synchronously. Viz.js throws on a graph it
	 * cannot render; the exception is caught here, so that nothing thrown by
	 * JavaScript crosses into Java, and returned as {@code error}.
	 */
	@JSBody(params = { "dotSource", "engine" }, script = "try {"
			+ "  return { svg: globalThis.__plantumlVizInstance.renderString(dotSource, { format: 'svg', engine: engine }) };"
			+ "} catch (e) {"
			+ "  return { error: String(e) };"
			+ "}")
	private static native JSObject renderString(String dotSource, String engine);

	@JSBody(params = { "obj", "key" }, script = "var v = obj[key]; return (typeof v === 'string') ? v : null;")
	private static native String getProperty(JSObject obj, String key);

}
