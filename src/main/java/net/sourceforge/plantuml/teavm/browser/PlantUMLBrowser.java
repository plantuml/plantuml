package net.sourceforge.plantuml.teavm.browser;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.xml.Element;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.Scale;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.UgDiagram;
import net.sourceforge.plantuml.core.AbstractDiagram;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramChromeFactory12026;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.drawing.hand.UGraphicHandwritten;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.teavm.PSystemBuilder2;
import net.sourceforge.plantuml.teavm.StringBounderTeaVM;
import net.sourceforge.plantuml.teavm.SvgGraphicsTeaVM;
import net.sourceforge.plantuml.teavm.UGraphicTeaVM;

/**
 * PlantUML rendering engine for browser environments, compiled to JavaScript
 * via TeaVM.
 * 
 * <h2>Overview</h2>
 * 
 * This class provides a bridge between JavaScript code running in a browser and
 * the PlantUML Java rendering engine. It exposes a global JavaScript function
 * {@code plantumlRender(lines, elementId)} that web pages can call to render
 * diagrams.
 * 
 * <h2>Architecture: JavaScript-driven paradigm</h2>
 * 
 * The design follows a "JS-driven" architecture where:
 * <ul>
 * <li><b>JavaScript handles:</b> UI events, user input, debouncing, line
 * splitting, DOM element selection, and overall application flow</li>
 * <li><b>Java handles:</b> PlantUML parsing and SVG rendering only</li>
 * </ul>
 * 
 * This separation keeps the Java code minimal and allows maximum flexibility
 * for web developers to integrate PlantUML however they want.
 * 
 * <h2>Why we need a worker thread</h2>
 * 
 * TeaVM compiles Java to JavaScript, but JavaScript is single-threaded and
 * event-driven. To support Java's synchronous blocking APIs (like
 * {@code Thread.sleep()} or {@code Object.wait()}), TeaVM uses a
 * coroutine-based approach that transforms blocking calls into asynchronous
 * JavaScript Promises.
 * 
 * <h3>The Viz.js constraint</h3>
 * 
 * PlantUML uses Viz.js (a JavaScript port of GraphViz) to render class
 * diagrams, component diagrams, and other diagrams that require graph layout.
 * Viz.js has an asynchronous API:
 * 
 * <pre>
 * Viz.instance().then(viz => viz.renderString(dot, options))
 * </pre>
 * 
 * Our {@code GraphVizjsTeaVMEngine} class uses TeaVM's {@code @Async}
 * annotation to make this async call appear synchronous to Java code. However,
 * this only works when called from a "TeaVM coroutine context" - essentially,
 * from within a TeaVM thread.
 * 
 * <h3>What happens without the worker thread</h3>
 * 
 * If JavaScript calls our render function directly (e.g., from a
 * {@code setTimeout} callback or an event listener), the call happens in a
 * "native JS context", not a TeaVM coroutine context. When the code reaches the
 * Viz.js async call, TeaVM throws:
 * 
 * <pre>
 * Error: Suspension point reached from non-threading context
 * (perhaps, from native JS method).
 * See https://teavm.org/docs/runtime/coroutines.html
 * </pre>
 * 
 * <h3>The solution: a dedicated worker thread</h3>
 * 
 * We solve this by:
 * <ol>
 * <li>Starting a background thread at initialization (in {@code main()})</li>
 * <li>Having the JS-callable function just queue a render request and wake the
 * thread</li>
 * <li>The worker thread performs the actual rendering in the correct coroutine
 * context</li>
 * </ol>
 * 
 * This pattern ensures all PlantUML rendering (including Viz.js calls) happens
 * in a context where TeaVM's async-to-sync transformation works correctly.
 * 
 * <h2>Usage from JavaScript</h2>
 * 
 * <pre>
 * // Initialize (call once when page loads)
 * main();
 * 
 * // Render a diagram
 * const source = "@startuml\nAlice -> Bob : hello\n@enduml";
 * const lines = source.split(/\r\n|\r|\n/);
 * plantumlRender(lines, "output-div-id");
 * </pre>
 * 
 * <h2>Thread safety</h2>
 * 
 * The class uses a simple producer-consumer pattern:
 * <ul>
 * <li>Producer: {@code requestRender()} called from JS, sets pending request
 * and notifies</li>
 * <li>Consumer: {@code workerLoop()} waits for requests, processes them one at
 * a time</li>
 * </ul>
 * 
 * If multiple render requests arrive while one is being processed, only the
 * latest request is kept (the previous pending request is overwritten). This is
 * intentional: when a user is typing, we only care about rendering the latest
 * version.
 * 
 * @see net.sourceforge.plantuml.teavm.GraphVizjsTeaVMEngine
 */
public class PlantUMLBrowser {
	// ::remove file when __MIT__ __EPL__ __BSD__ __ASL__ __LGPL__ __GPLV2__
	// ::remove file when JAVA8

	// =========================================================================
	// Rendering configuration
	// =========================================================================

	private static final StringBounder STRING_BOUNDER = new StringBounderTeaVM();
	private static final ColorMapper COLOR_MAPPER = ColorMapper.TEAVM;
	private static final HColor BACK = HColors.WHITE;

	// =========================================================================
	// Worker thread synchronization
	//
	// We use a simple wait/notify pattern. The worker thread waits on LOCK until
	// pendingLines becomes non-null, then processes the request and sets it back
	// to null.
	// =========================================================================

	/** Lock object for synchronizing between JS requests and worker thread. */
	private static final Object LOCK = new Object();

	/**
	 * The PlantUML source lines to render, or null if no request is pending. Set by
	 * requestRender(), cleared by workerLoop() after processing.
	 */
	private static volatile String[] pendingLines;

	/** The DOM element ID where the SVG should be inserted, or null for renderToString requests. */
	private static volatile String pendingElementId;

	/** Success callback for renderToString requests, or null for render-to-div requests. */
	private static volatile StringCallback pendingOnSuccess;

	/** Error callback for renderToString requests, or null for render-to-div requests. */
	private static volatile StringCallback pendingOnError;

	// =========================================================================
	// Initialization
	// =========================================================================

	/**
	 * Entry point called from JavaScript to initialize the PlantUML renderer.
	 * 
	 * This method:
	 * <ol>
	 * <li>Starts the background worker thread that will handle all rendering</li>
	 * <li>Registers the global {@code window.plantumlRender} function</li>
	 * </ol>
	 * 
	 * Must be called once before any rendering can occur.
	 */
	public static void main(String[] args) {
		// Start worker thread FIRST - it needs to be ready to receive requests.
		// The thread runs forever, waiting for render requests.
		new Thread(PlantUMLBrowser::workerLoop, "plantuml-render").start();

		// Expose the API on window.plantuml so it doesn't pollute the global namespace.
		// JavaScript callers must invoke window.plantumlLoad() once (TeaVM entry point,
		// renamed via entryPointName in build.gradle.kts) to start the worker thread and populate:
		//   window.plantuml.render(lines, elementId)                    — render SVG into a DOM element
		//   window.plantuml.renderToString(lines, onSuccess, onError)   — return SVG as a string
		registerNamespace(PlantUMLBrowser::requestRender, PlantUMLBrowser::requestRenderToString);

		BrowserLog.jsStatusDuration();
	}

	// =========================================================================
	// JavaScript API registration
	// =========================================================================

	/**
	 * Creates the {@code window.plantuml} namespace and registers all API methods on it:
	 * <ul>
	 * <li>{@code window.plantuml.render(lines, elementId)} — render SVG into a DOM element</li>
	 * <li>{@code window.plantuml.renderToString(lines, callback)} — call {@code callback(svgString)}</li>
	 * </ul>
	 *
	 * {@code window.plantuml.loadWorker} is set to a no-op after this call completes,
	 * so callers that do {@code window.plantuml.loadWorker()} to initialize will still work
	 * (the worker is already running by the time this method is called from {@code main}).
	 */
	@JSBody(params = { "renderCb", "renderToStringCb" }, script =
		"var ns = window.plantuml = window.plantuml || {};" +
		"ns.render = renderCb;" +
		"ns.renderToString = renderToStringCb;")
	private static native void registerNamespace(RenderCallback renderCb, RenderToStringCallback renderToStringCb);

	/** Callback for {@code window.plantuml.render(lines, elementId)}. */
	@JSFunctor
	public interface RenderCallback extends JSObject {
		void call(String[] lines, String elementId);
	}

	/** Callback for {@code window.plantuml.renderToString(lines, onSuccess, onError)}. */
	@JSFunctor
	public interface RenderToStringCallback extends JSObject {
		void call(String[] lines, StringCallback onSuccess, StringCallback onError);
	}

	/** Single-string JS callback, used for both success (SVG) and error (message). */
	@JSFunctor
	public interface StringCallback extends JSObject {
		void call(String value);
	}

	// =========================================================================
	// Request handling (called from JavaScript)
	// =========================================================================

	/**
	 * Called from JavaScript to request a diagram rendering.
	 * 
	 * This method does NOT perform the rendering itself - it only queues the
	 * request and wakes up the worker thread. This is necessary because:
	 * 
	 * <ol>
	 * <li>This method is called from a native JS context (event handler,
	 * setTimeout, etc.)</li>
	 * <li>Viz.js async calls require a TeaVM coroutine context</li>
	 * <li>The worker thread provides that coroutine context</li>
	 * </ol>
	 * 
	 * If a previous request is still pending (worker hasn't picked it up yet), it
	 * will be overwritten. This is the desired behavior for live-typing scenarios.
	 * 
	 * @param lines     the PlantUML source code, split into lines by the JavaScript
	 *                  caller
	 * @param elementId the ID of the HTML element where the SVG should be rendered
	 */
	private static void requestRender(String[] lines, String elementId) {
		synchronized (LOCK) {
			pendingLines = lines;
			pendingElementId = elementId;
			pendingOnSuccess = null;
			pendingOnError = null;
			LOCK.notify();
		}
	}

	private static void requestRenderToString(String[] lines, StringCallback onSuccess, StringCallback onError) {
		synchronized (LOCK) {
			pendingLines = lines;
			pendingElementId = null;
			pendingOnSuccess = onSuccess;
			pendingOnError = onError;
			LOCK.notify();
		}
	}

	// =========================================================================
	// Worker thread
	// =========================================================================

	/**
	 * Main loop for the worker thread. Runs forever, processing render requests.
	 * 
	 * This method executes in a TeaVM coroutine context, which means:
	 * <ul>
	 * <li>{@code LOCK.wait()} is properly transformed to async JS</li>
	 * <li>Viz.js async calls (via @Async annotation) work correctly</li>
	 * </ul>
	 * 
	 * The loop:
	 * <ol>
	 * <li>Waits until a render request is available (pendingLines != null)</li>
	 * <li>Captures and clears the request atomically</li>
	 * <li>Performs the rendering (may involve async Viz.js calls)</li>
	 * <li>Repeats forever</li>
	 * </ol>
	 */
	private static void workerLoop() {
		while (true) {
			String[] lines;
			String elementId;
			StringCallback onSuccess;
			StringCallback onError;

			synchronized (LOCK) {
				while (pendingLines == null) {
					try {
						LOCK.wait();
					} catch (InterruptedException e) {
						// Interruption is not expected, but if it happens, just retry
					}
				}

				// Capture the request and clear the pending state atomically.
				lines = pendingLines;
				elementId = pendingElementId;
				onSuccess = pendingOnSuccess;
				onError = pendingOnError;
				pendingLines = null;
				pendingElementId = null;
				pendingOnSuccess = null;
				pendingOnError = null;
			}

			// Perform rendering OUTSIDE the synchronized block so new requests
			// can be queued while we're rendering.
			if (onSuccess != null)
				doRenderToString(lines, onSuccess, onError);
			else
				doRender(lines, elementId);
		}
	}

	// =========================================================================
	// Rendering
	// =========================================================================

	/** Parses and renders PlantUML source lines to an SVG graphics context. */
	private static SvgGraphicsTeaVM buildSvg(String[] lines) throws Exception {
		final SvgGraphicsTeaVM svg = new SvgGraphicsTeaVM();
		UGraphic ug = UGraphicTeaVM.build(BACK, COLOR_MAPPER, STRING_BOUNDER, svg);
		final Diagram diagram = PSystemBuilder2.getInstance().createDiagram(lines);
		final FileFormatOption fileFormat = new FileFormatOption(FileFormat.SVG);

		if (diagram instanceof UgDiagram == false)
			throw new RuntimeException("Unsupported diagram type");

		final Scale scale = ((AbstractDiagram) diagram).getScale();
		final UgDiagram ugDiagram = (UgDiagram) diagram;
		TextBlock tb = ugDiagram.getTextBlock12026(0, fileFormat);

		if (diagram instanceof TitledDiagram)
			tb = DiagramChromeFactory12026.create(tb, (TitledDiagram) ugDiagram,
					((TitledDiagram) ugDiagram).getSkinParam(), ugDiagram.getWarnings());

		if (ugDiagram.isHandwritten())
			ug = new UGraphicHandwritten(ug);

		tb.drawU(ug);

		final XDimension2D dim = tb.calculateDimension(STRING_BOUNDER);
		final double scaleFactor = scale == null ? 1.0 : scale.getScale(dim.getWidth(), dim.getHeight());
		svg.updateSvgSize(dim.getWidth(), dim.getHeight(), scaleFactor);
		return svg;
	}

	private static void doRender(String[] lines, String elementId) {
		final HTMLElement out = HTMLDocument.current().getElementById(elementId);
		if (out == null)
			return;

		try {
			removeAllChildren(out);
			appendSvgElement(out, buildSvg(lines).getSvgRoot());
		} catch (Exception e) {
			out.setTextContent(String.valueOf(e));
		}
		BrowserLog.jsStatusDuration();
	}

	private static void doRenderToString(String[] lines, StringCallback onSuccess, StringCallback onError) {
		try {
			onSuccess.call(serializeSvg(buildSvg(lines).getSvgRoot()));
		} catch (Exception e) {
			onError.call(String.valueOf(e));
		}
	}

	// =========================================================================
	// JavaScript interop utilities
	// =========================================================================

	/** Appends an SVG element as a child of a DOM element. */
	@JSBody(params = { "p", "svg" }, script = "p.appendChild(svg);")
	private static native void appendSvgElement(HTMLElement p, Element svg);

	/** Removes all child nodes from a DOM element. */
	@JSBody(params = "el", script = "while(el.firstChild)el.removeChild(el.firstChild);")
	private static native void removeAllChildren(HTMLElement el);

	/** Serializes an SVG DOM element to a string. */
	@JSBody(params = "svg", script = "return new XMLSerializer().serializeToString(svg);")
	private static native String serializeSvg(Element svg);

}
