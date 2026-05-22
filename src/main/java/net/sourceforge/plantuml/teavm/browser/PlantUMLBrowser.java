package net.sourceforge.plantuml.teavm.browser;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSExport;
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
import net.sourceforge.plantuml.core.DiagramChromeFactory;
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
 * PlantUML rendering engine for browser environments, compiled to a JavaScript
 * ES2015 module via TeaVM.
 *
 * <h2>Overview</h2>
 *
 * This class provides a bridge between JavaScript code running in a browser and
 * the PlantUML Java rendering engine. The compiled output ({@code plantuml.js})
 * is an ES2015 module that exports two functions: {@code render} and
 * {@code renderToString}.
 *
 * <h2>Usage from JavaScript</h2>
 *
 * <pre>
 * &lt;script type="module"&gt;
 *   import { render, renderToString } from './plantuml.js';
 *
 *   // Render directly into a DOM element
 *   const source = "@startuml\nAlice -&gt; Bob : hello\n@enduml";
 *   const lines = source.split(/\r\n|\r|\n/);
 *   render(lines, "diagram-output-id");
 *
 *   // Or get the SVG as a string
 *   renderToString(lines,
 *     svg =&gt; console.log(svg),
 *     err =&gt; console.error(err)
 *   );
 * &lt;/script&gt;
 * </pre>
 *
 * Both functions accept an optional {@code options} object as the last
 * argument, e.g. {@code { dark: true }} to enable dark-mode rendering.
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
 * Viz.instance().then(viz =&gt; viz.renderString(dot, options))
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
 * <li>Lazily starting a background thread on the first call to {@link #render}
 * or {@link #renderToString}</li>
 * <li>Having the exported function just queue a render request and wake the
 * thread</li>
 * <li>The worker thread performs the actual rendering in the correct coroutine
 * context</li>
 * </ol>
 *
 * This pattern ensures all PlantUML rendering (including Viz.js calls) happens
 * in a context where TeaVM's async-to-sync transformation works correctly.
 *
 * <h2>Thread safety</h2>
 *
 * The class uses a simple producer-consumer pattern:
 * <ul>
 * <li>Producer: {@link #render} / {@link #renderToString} called from JS, sets
 * pending request and notifies</li>
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

	/** Maximum width or height (in pixels) before refusing to render. */
	private static final int MAX_SVG_SIZE = 4096;

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
	 * Whether the worker thread has been started. Guarded by {@link #LOCK} for the
	 * start transition; read without locking on the fast path.
	 */
	private static volatile boolean workerStarted = false;

	/**
	 * The PlantUML source lines to render, or null if no request is pending. Set by
	 * the exported entry points, cleared by workerLoop() after processing.
	 */
	private static volatile String[] pendingLines;

	/**
	 * The DOM element ID where the SVG should be inserted, or null for
	 * renderToString requests.
	 */
	private static volatile String pendingElementId;

	/**
	 * Success callback for renderToString requests, or null for render-to-div
	 * requests.
	 */
	private static volatile StringCallback pendingOnSuccess;

	/**
	 * Error callback for renderToString requests, or null for render-to-div
	 * requests.
	 */
	private static volatile StringCallback pendingOnError;

	/** Whether the pending request should use dark mode rendering. */
	private static volatile boolean pendingDarkMode;

	// =========================================================================
	// Lazy worker initialization
	// =========================================================================

	/**
	 * Starts the worker thread on the first call. Subsequent calls are no-ops.
	 *
	 * Because the TeaVM JS output is an ES2015 module, there is no {@code main()}
	 * entry point that runs automatically at load time, so we defer thread creation
	 * until the first render request arrives.
	 */
	private static void ensureWorkerStarted() {
		if (workerStarted == false) {
			synchronized (LOCK) {
				if (workerStarted == false) {
					new Thread(PlantUMLBrowser::workerLoop, "plantuml-render").start();
					workerStarted = true;
				}
			}
		}
	}

	// =========================================================================
	// Exported entry points (called from JavaScript)
	// =========================================================================

	/**
	 * Single-string JS callback, used for both success (SVG) and error (message).
	 */
	@JSFunctor
	public interface StringCallback extends JSObject {
		void call(String value);
	}

	/**
	 * Renders a PlantUML diagram into the DOM element identified by
	 * {@code elementId}.
	 *
	 * <p>
	 * This method does NOT perform the rendering itself — it only queues the
	 * request and wakes up the worker thread. This is necessary because:
	 *
	 * <ol>
	 * <li>This method is called from a native JS context (event handler,
	 * setTimeout, etc.)</li>
	 * <li>Viz.js async calls require a TeaVM coroutine context</li>
	 * <li>The worker thread provides that coroutine context</li>
	 * </ol>
	 *
	 * <p>
	 * This call is asynchronous: it returns immediately, and the SVG is inserted
	 * later from the worker thread.
	 *
	 * <p>
	 * If a previous request is still pending (worker hasn't picked it up yet), it
	 * will be overwritten. This is the desired behavior for live-typing scenarios.
	 *
	 * @param lines     the PlantUML source code, split into lines by the JavaScript
	 *                  caller
	 * @param elementId the {@code id} of the HTML element where the SVG should be
	 *                  rendered
	 * @param options   optional JS object with rendering options (e.g. {@code {
	 *                  dark: true }}); may be {@code null}
	 */
	@JSExport
	public static void render(String[] lines, String elementId, JSObject options) {
		ensureWorkerStarted();
		synchronized (LOCK) {
			pendingLines = lines;
			pendingElementId = elementId;
			pendingOnSuccess = null;
			pendingOnError = null;
			pendingDarkMode = isDark(options);
			LOCK.notify();
		}
	}

	/**
	 * Renders a PlantUML diagram and delivers the resulting SVG as a string via the
	 * {@code onSuccess} callback. Errors go to {@code onError}.
	 *
	 * <p>
	 * Same queueing and asynchronous behavior as {@link #render}: this method only
	 * queues the request; the worker thread performs the actual rendering and
	 * invokes the callback. A previous pending request will be overwritten.
	 *
	 * @param lines     the PlantUML source code, split into lines by the JavaScript
	 *                  caller
	 * @param onSuccess callback invoked with the SVG string when rendering succeeds
	 * @param onError   callback invoked with an error message when rendering fails
	 * @param options   optional JS object with rendering options (e.g. {@code {
	 *                  dark: true }}); may be {@code null}
	 */
	@JSExport
	public static void renderToString(String[] lines, StringCallback onSuccess, StringCallback onError,
			JSObject options) {
		ensureWorkerStarted();
		synchronized (LOCK) {
			pendingLines = lines;
			pendingElementId = null;
			pendingOnSuccess = onSuccess;
			pendingOnError = onError;
			pendingDarkMode = isDark(options);
			LOCK.notify();
		}
	}

	// =========================================================================
	// Options extraction (called from JavaScript)
	// =========================================================================

	/**
	 * Extracts the {@code dark} boolean property from a JavaScript options object.
	 * Returns {@code false} if the object is null/undefined or if the property is
	 * absent.
	 */
	@JSBody(params = "opts", script = "return (opts && opts.dark === true);")
	private static native boolean isDark(JSObject opts);

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
			boolean darkMode;

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
				darkMode = pendingDarkMode;
				pendingLines = null;
				pendingElementId = null;
				pendingOnSuccess = null;
				pendingOnError = null;
				pendingDarkMode = false;
			}

			// Perform rendering OUTSIDE the synchronized block so new requests
			// can be queued while we're rendering.
			if (onSuccess != null)
				doRenderToString(lines, onSuccess, onError, darkMode);
			else
				doRender(lines, elementId, darkMode);
		}
	}

	// =========================================================================
	// Rendering
	// =========================================================================

	/** Parses and renders PlantUML source lines to an SVG graphics context. */
	private static SvgGraphicsTeaVM buildSvg(String[] lines, boolean darkMode) throws Exception {
		final ColorMapper colorMapper = darkMode ? ColorMapper.TEAVM_DARK : ColorMapper.TEAVM_LIGHT;

		final Diagram diagram = PSystemBuilder2.getInstance().createDiagram(lines);
		final FileFormatOption fileFormat = new FileFormatOption(FileFormat.SVG);

		if (diagram instanceof UgDiagram == false)
			throw new RuntimeException("Unsupported diagram type");

		final Scale scale = ((AbstractDiagram) diagram).getScale();
		final UgDiagram ugDiagram = (UgDiagram) diagram;
		TextBlock tb = ugDiagram.getTextBlock(0, fileFormat);

		HColor tbBackcolor = tb.getBackcolor();
		final SvgGraphicsTeaVM svg;

		if (tbBackcolor == null) {
			svg = new SvgGraphicsTeaVM();
			tbBackcolor = darkMode ? HColors.BLACK : HColors.WHITE;
		} else {
			svg = new SvgGraphicsTeaVM(tbBackcolor.toSvg(colorMapper));
		}

		UGraphic ug = UGraphicTeaVM.build(tbBackcolor, colorMapper, STRING_BOUNDER, svg);

		if (diagram instanceof TitledDiagram)
			tb = DiagramChromeFactory.create(tb, (TitledDiagram) ugDiagram,
					((TitledDiagram) ugDiagram).getSkinParam(), ugDiagram.getWarnings());

		if (ugDiagram.isHandwritten())
			ug = new UGraphicHandwritten(ug);

		tb.drawU(ug);

		final XDimension2D dim = tb.calculateDimension(STRING_BOUNDER);

		if (dim.getWidth() > MAX_SVG_SIZE || dim.getHeight() > MAX_SVG_SIZE)
			throw new RuntimeException("Diagram too large for browser rendering: " + (int) dim.getWidth() + "x"
					+ (int) dim.getHeight() + " (max " + MAX_SVG_SIZE + ")");

		final double scaleFactor = scale == null ? 1.0 : scale.getScale(dim.getWidth(), dim.getHeight());
		svg.updateSvgSize(dim.getWidth(), dim.getHeight(), scaleFactor);
		return svg;
	}

	private static void doRender(String[] lines, String elementId, boolean darkMode) {
		final HTMLElement out = HTMLDocument.current().getElementById(elementId);
		if (out == null)
			return;

		try {
			BrowserLog.reset();
			final SvgGraphicsTeaVM svg = buildSvg(lines, darkMode);
			removeAllChildren(out);
			appendSvgElement(out, svg.getSvgRoot());
		} catch (Exception e) {
			out.setTextContent(String.valueOf(e));
		}
		BrowserLog.jsStatusDuration();
	}

	private static void doRenderToString(String[] lines, StringCallback onSuccess, StringCallback onError,
			boolean darkMode) {
		try {
			onSuccess.call(serializeSvg(buildSvg(lines, darkMode).getSvgRoot()));
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
