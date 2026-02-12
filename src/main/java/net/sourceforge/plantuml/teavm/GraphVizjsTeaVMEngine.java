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
package net.sourceforge.plantuml.teavm;

// ::uncomment when __TEAVM__
//import org.teavm.interop.Async;
//import org.teavm.interop.AsyncCallback;
//import org.teavm.jso.JSBody;
//import org.teavm.jso.JSFunctor;
//import org.teavm.jso.JSObject;
// ::done

/**
 * TeaVM engine for rendering DOT graphs to SVG using Viz.js.
 * <p>
 * This class provides a bridge between Java code running in TeaVM and the
 * Viz.js JavaScript library. It handles the asynchronous nature of
 * Viz.instance() using TeaVM's @Async annotation.
 * <p>
 * Usage:
 * <pre>
 * String dot = "digraph G { A -> B; }";
 * String svg = GraphVizjsTeaVMEngine.renderDotToSvg(dot);
 * </pre>
 */
public class GraphVizjsTeaVMEngine {

	// ::uncomment when __TEAVM__

//	/**
//	 * Renders a DOT graph description to SVG format.
//	 * <p>
//	 * This method is asynchronous from JavaScript's perspective but appears
//	 * synchronous to the Java caller thanks to TeaVM's @Async mechanism.
//	 *
//	 * @param dotSource the graph description in DOT format
//	 * @return the rendered SVG as a string
//	 */
//	@Async
//	public static native String renderDotToSvg(String dotSource);
//
//	/**
//	 * Internal implementation called by TeaVM's async mechanism.
//	 * Sets up the JavaScript Promise handling and calls the callback when complete.
//	 */
//	private static void renderDotToSvg(String dotSource, AsyncCallback<String> callback) {
//		renderDotToSvgAsync(dotSource, result -> callback.complete(result), error -> callback.error(new RuntimeException(error)));
//	}
//
//	/**
//	 * JavaScript bridge that calls Viz.instance() and renders the DOT source.
//	 * Uses Viz.js API: Viz.instance().then(viz => viz.renderString(dot, options))
//	 */
//	@JSBody(params = { "dotSource", "onSuccess", "onError" }, script = 
//		"Viz.instance().then(function(viz) {" +
//		"  try {" +
//		"    var svg = viz.renderString(dotSource, { format: 'svg', engine: 'dot' });" +
//		"    onSuccess(svg);" +
//		"  } catch(e) {" +
//		"    onError(e.toString());" +
//		"  }" +
//		"}).catch(function(err) {" +
//		"  onError(err.toString());" +
//		"});")
//	private static native void renderDotToSvgAsync(String dotSource, StringCallback onSuccess, StringCallback onError);
//
//	/**
//	 * Renders a DOT graph using a specific GraphViz engine.
//	 *
//	 * @param dotSource the graph description in DOT format
//	 * @param engine    the GraphViz engine to use (dot, neato, fdp, sfdp, twopi, circo)
//	 * @return the rendered SVG as a string
//	 */
//	@Async
//	public static native String renderDotToSvg(String dotSource, String engine);
//
//	/**
//	 * Internal implementation for engine-specific rendering.
//	 */
//	private static void renderDotToSvg(String dotSource, String engine, AsyncCallback<String> callback) {
//		renderDotToSvgWithEngineAsync(dotSource, engine, result -> callback.complete(result), error -> callback.error(new RuntimeException(error)));
//	}
//
//	/**
//	 * JavaScript bridge for engine-specific rendering.
//	 */
//	@JSBody(params = { "dotSource", "engine", "onSuccess", "onError" }, script = 
//		"Viz.instance().then(function(viz) {" +
//		"  try {" +
//		"    var svg = viz.renderString(dotSource, { format: 'svg', engine: engine });" +
//		"    onSuccess(svg);" +
//		"  } catch(e) {" +
//		"    onError(e.toString());" +
//		"  }" +
//		"}).catch(function(err) {" +
//		"  onError(err.toString());" +
//		"});")
//	private static native void renderDotToSvgWithEngineAsync(String dotSource, String engine, StringCallback onSuccess, StringCallback onError);
//
//	/**
//	 * Callback interface for receiving string results from JavaScript.
//	 */
//	@JSFunctor
//	private interface StringCallback extends JSObject {
//		void call(String value);
//	}
	
	// ::done

}
