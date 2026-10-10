package net.sourceforge.plantuml.teavm.browser;

import java.util.ArrayList;
import java.util.List;

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
 * argument, e.g. {@code { dark: true }} to enable dark-mode rendering, or
 * {@code { maxSvgSize: 9999 } } to raise the maximum SVG width/height (in
 * pixels) beyond the default of {@value #DEFAULT_MAX_SVG_SIZE}. Passing
 * {@code { maxSvgSize: 0 } } disables the size check entirely.
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
 * Requests wait in a queue, oldest first, and the worker answers them one at a
 * time. Two rules decide what happens when several arrive before the worker is
 * free:
 * <ul>
 * <li>{@code renderToString}: every request is kept and answered, in the order
 * received. Each of its callbacks is called exactly once ({@code onSuccess} or
 * {@code onError}); a host that renders many diagrams at the same time gets all
 * of them back.</li>
 * <li>{@code render}: only the latest request is kept, a new one replaces the
 * {@code render} request still waiting. This is intentional: when a user is
 * typing, we only care about rendering the latest version.</li>
 * </ul>
 *
 * Neither kind drops the other. A callback that throws, or a missing callback,
 * does not stop the worker.
 *
 * @see net.sourceforge.plantuml.teavm.GraphVizjsTeaVMEngine
 */
public class PlantUMLBrowser {
	// ::remove file when JAVA8

	// =========================================================================
	// Rendering configuration
	// =========================================================================

	private static final StringBounder STRING_BOUNDER = new StringBounderTeaVM();

	/**
	 * Default maximum width or height (in pixels) before refusing to render.
	 * Callers may override this per-call via the {@code maxSvgSize} rendering
	 * option; a value of {@code 0} disables the check entirely. SVG output has
	 * no raster memory cost, so this default exists only as a sane guard rail,
	 * not a hard technical ceiling.
	 */
	private static final int DEFAULT_MAX_SVG_SIZE = 8192;

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
	 * One render request, as queued by the exported entry points and consumed by
	 * {@link PlantUMLBrowser#workerLoop()}. Immutable: it is built by the caller's thread and read
	 * by the worker.
	 */
	private static final class Request {
		/** The PlantUML source lines to render. */
		private final String[] lines;

		/**
		 * The DOM element ID where the SVG should be inserted, or null for
		 * renderToString requests.
		 */
		private final String elementId;

		/** Success callback of a renderToString request, null for render-to-div. */
		private final StringCallback onSuccess;

		/** Error callback of a renderToString request, null for render-to-div. */
		private final StringCallback onError;

		/** Whether the request should use dark mode rendering. */
		private final boolean darkMode;

		/**
		 * Maximum width/height (in pixels) allowed, resolved from the
		 * {@code maxSvgSize} option ({@link #DEFAULT_MAX_SVG_SIZE} when absent). A
		 * value {@code <= 0} means "no limit".
		 */
		private final int maxSvgSize;

		private final boolean toElement;

		private Request(String[] lines, String elementId, StringCallback onSuccess, StringCallback onError,
				boolean darkMode, int maxSvgSize, boolean toElement) {
			this.lines = lines;
			this.elementId = elementId;
			this.onSuccess = onSuccess;
			this.onError = onError;
			this.darkMode = darkMode;
			this.maxSvgSize = maxSvgSize;
			this.toElement = toElement;
		}

		/** A {@link PlantUMLBrowser#render} request: the SVG goes into a DOM element. */
		private static Request forElement(String[] lines, String elementId, boolean darkMode, int maxSvgSize) {
			return new Request(lines, elementId, null, null, darkMode, maxSvgSize, true);
		}

		/** A {@link PlantUMLBrowser#renderToString} request: the SVG goes to a callback. */
		private static Request forCallbacks(String[] lines, StringCallback onSuccess, StringCallback onError,
				boolean darkMode, int maxSvgSize) {
			return new Request(lines, null, onSuccess, onError, darkMode, maxSvgSize, false);
		}
	}

	/**
	 * Requests waiting for the worker, oldest first. Guarded by {@link #LOCK}.
	 *
	 * <p>
	 * Every {@link #renderToString} request stays in the queue until the worker has
	 * answered it, so each of its callbacks is called exactly once. A
	 * {@link #render} request is the exception: it replaces the {@code render}
	 * request already waiting, if any (see {@link #enqueueRender}). The queue is
	 * not bounded: a host that floods {@code renderToString} gets every diagram
	 * rendered.
	 */
	private static final List<Request> QUEUE = new ArrayList<Request>();

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
	 * If a previous {@code render} request is still waiting (the worker hasn't
	 * picked it up yet), it is replaced by this one, and its diagram is never
	 * drawn. This is the desired behavior for live-typing scenarios. Waiting
	 * {@code renderToString} requests are not affected.
	 *
	 * @param lines     the PlantUML source code, split into lines by the JavaScript
	 *                  caller
	 * @param elementId the {@code id} of the HTML element where the SVG should be
	 *                  rendered
	 * @param options   optional JS object with rendering options (e.g. {@code {
	 *                  dark: true, maxSvgSize: 8192 }}); may be {@code null}.
	 *                  {@code maxSvgSize} overrides {@link #DEFAULT_MAX_SVG_SIZE};
	 *                  {@code 0} disables the size check.
	 */
	@JSExport
	public static void render(String[] lines, String elementId, JSObject options) {
		ensureWorkerStarted();
		final Request request = Request.forElement(lines, elementId, isDark(options), resolveMaxSvgSize(options));
		synchronized (LOCK) {
			enqueueRender(request);
			LOCK.notify();
		}
	}

	/**
	 * Adds a {@link #render} request to the queue, "latest wins": if a
	 * {@code render} request is still waiting, it is replaced (in place) by this
	 * one. Requests of the other kind are never touched. Must be called with
	 * {@link #LOCK} held.
	 */
	private static void enqueueRender(Request request) {
		for (int i = 0; i < QUEUE.size(); i++)
			if (QUEUE.get(i).toElement) {
				QUEUE.set(i, request);
				return;
			}
		QUEUE.add(request);
	}

	/**
	 * Renders a PlantUML diagram and delivers the resulting SVG as a string via the
	 * {@code onSuccess} callback. Errors go to {@code onError}.
	 *
	 * <p>
	 * Same queueing and asynchronous behavior as {@link #render}: this method only
	 * queues the request; the worker thread performs the actual rendering and
	 * invokes the callback. Unlike {@link #render}, requests are never dropped: each
	 * call is queued behind the ones already waiting, and exactly one of
	 * {@code onSuccess} and {@code onError} is called for it, once, in the order of
	 * the calls. A callback that is {@code null} or that throws does not stop the
	 * engine (an exception is reported with {@code console.error}).
	 *
	 * @param lines     the PlantUML source code, split into lines by the JavaScript
	 *                  caller
	 * @param onSuccess callback invoked with the SVG string when rendering succeeds
	 * @param onError   callback invoked with an error message when rendering fails
	 * @param options   optional JS object with rendering options (e.g. {@code {
	 *                  dark: true, maxSvgSize: 8192 }}); may be {@code null}.
	 *                  {@code maxSvgSize} overrides {@link #DEFAULT_MAX_SVG_SIZE};
	 *                  {@code 0} disables the size check.
	 */
	@JSExport
	public static void renderToString(String[] lines, StringCallback onSuccess, StringCallback onError,
			JSObject options) {
		ensureWorkerStarted();
		final Request request = Request.forCallbacks(lines, onSuccess, onError, isDark(options),
				resolveMaxSvgSize(options));
		synchronized (LOCK) {
			QUEUE.add(request);
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

	/**
	 * Extracts the {@code maxSvgSize} numeric property from a JavaScript options
	 * object. Returns {@code -1} as a sentinel when the object is null/undefined
	 * or the property is absent/not a number, meaning "use the default".
	 */
	@JSBody(params = "opts", script = "return (opts && typeof opts.maxSvgSize === 'number') ? opts.maxSvgSize : -1;")
	private static native int extractMaxSvgSize(JSObject opts);

	/**
	 * Resolves the effective max-SVG-size limit from the {@code options} object:
	 * the caller-supplied {@code maxSvgSize} when present, otherwise
	 * {@link #DEFAULT_MAX_SVG_SIZE}. A resolved value {@code <= 0} means
	 * "no limit".
	 */
	private static int resolveMaxSvgSize(JSObject opts) {
		final int value = extractMaxSvgSize(opts);
		return value < 0 ? DEFAULT_MAX_SVG_SIZE : value;
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
			final Request request;

			synchronized (LOCK) {
				while (QUEUE.isEmpty()) {
					try {
						LOCK.wait();
					} catch (InterruptedException e) {
						// Interruption is not expected, but if it happens, just retry
					}
				}

				// Take the oldest request off the queue.
				request = QUEUE.remove(0);
			}

			// Perform rendering OUTSIDE the synchronized block so new requests
			// can be queued while we're rendering.
			try {
				if (request.toElement)
					doRender(request.lines, request.elementId, request.darkMode, request.maxSvgSize);
				else
					doRenderToString(request);
			} catch (Throwable t) {
				// Nothing may end this loop: a worker that dies leaves every later request
				// unanswered, with no error anywhere.
				consoleError("PlantUML: request failed: " + t);
			}
		}
	}

	// =========================================================================
	// Rendering
	// =========================================================================

	/** Parses and renders PlantUML source lines to an SVG graphics context. */
	private static SvgGraphicsTeaVM buildSvg(String[] lines, boolean darkMode, int maxSvgSize) throws Exception {
		final ColorMapper colorMapper = darkMode ? ColorMapper.TEAVM_DARK : ColorMapper.TEAVM_LIGHT;

		final Diagram diagram = PSystemBuilder2.getInstance().createDiagram(lines);
		final FileFormatOption fileFormat = new FileFormatOption(FileFormat.SVG);

		if (diagram instanceof UgDiagram == false)
			throw new RuntimeException("Unsupported diagram type");

		final Scale scale = ((AbstractDiagram) diagram).getScale();
		final UgDiagram ugDiagram = (UgDiagram) diagram;
		TextBlock tb = ugDiagram.getTextBlock(0, fileFormat);

		HColor tbBackcolor = tb.getBackcolor();
		if (tbBackcolor == null && diagram instanceof TitledDiagram)
			tbBackcolor = getPaintableDocumentBackground((TitledDiagram) diagram);

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
					((TitledDiagram) ugDiagram).getSkinParam(), ugDiagram.getWarnings(), ((TitledDiagram) diagram).getTitle());

		if (ugDiagram.isHandwritten())
			ug = new UGraphicHandwritten(ug);

		tb.drawU(ug);

		final XDimension2D dim = tb.calculateDimension(STRING_BOUNDER);

		if (maxSvgSize > 0 && (dim.getWidth() > maxSvgSize || dim.getHeight() > maxSvgSize))
			throw new RuntimeException("Diagram too large for browser rendering: " + (int) dim.getWidth() + "x"
					+ (int) dim.getHeight() + " (max " + maxSvgSize + "; override via the maxSvgSize option, or set it to 0 to disable this check)");

		final double scaleFactor = scale == null ? 1.0 : scale.getScale(dim.getWidth(), dim.getHeight());
		svg.updateSvgSize(dim.getWidth(), dim.getHeight(), scaleFactor);

		// Embed the PlantUML source as a plantuml-src processing instruction, so the
		// generated SVG can be re-imported/edited later, just like the SVGs produced
		// by the classic Java backend and editor.plantuml.com.
		// https://github.com/plantuml/plantuml/issues/2761
		svg.addCommentMetadata(String.join("\n", lines));

		return svg;
	}

	/**
	 * Returns the document background of the diagram when it should be painted,
	 * or null to keep the historic behaviour of painting nothing.
	 * <p>
	 * The document background is where both {@code skinparam backgroundColor} and
	 * a theme background land; it never surfaces on the TextBlock, so it is read
	 * from the merged root.document style, exactly like {@code ImageBuilder.styled()}
	 * does for the Java build.
	 * <p>
	 * The color is judged in its plain (light mapped) form so the decision is the
	 * same in both modes: transparent never paints, and the skin default of white
	 * must keep painting nothing, in dark mode too, where the skin pairs white
	 * with a dark value but the page has always supplied its own backdrop.
	 * Returning null also keeps the historic WHITE/BLACK fallback in
	 * {@code buildSvg} as the contrast reference for automatic colors.
	 */
	private static HColor getPaintableDocumentBackground(TitledDiagram diagram) {
		final HColor documentBackground = diagram.calculateBackColor();
		if (documentBackground == null)
			return null;

		final String plainColor = documentBackground.toSvg(ColorMapper.TEAVM_LIGHT);
		if ("#00000000".equals(plainColor) || "#FFFFFF".equals(plainColor))
			return null;

		return documentBackground;
	}

	private static void doRender(String[] lines, String elementId, boolean darkMode, int maxSvgSize) {
		final HTMLElement out = HTMLDocument.current().getElementById(elementId);
		if (out == null)
			return;

		try {
			BrowserLog.reset();
			final SvgGraphicsTeaVM svg = buildSvg(lines, darkMode, maxSvgSize);
			removeAllChildren(out);
			appendSvgElement(out, svg.getSvgRoot());
		} catch (Exception e) {
			out.setTextContent(String.valueOf(e));
		}
		BrowserLog.jsStatusDuration();
	}

	/**
	 * Renders a {@link #renderToString} request and answers it: exactly one of its
	 * two callbacks is called, once.
	 */
	private static void doRenderToString(Request request) {
		final String svg;
		try {
			svg = serializeSvg(buildSvg(request.lines, request.darkMode, request.maxSvgSize).getSvgRoot());
		} catch (Throwable t) {
			deliver(request.onError, String.valueOf(t));
			return;
		}
		// Outside the try block above: if the success callback throws, the error
		// callback must not be called as well.
		deliver(request.onSuccess, svg);
	}

	/**
	 * Calls a host callback. A missing callback (the host did not pass one) is
	 * ignored, and an exception thrown by the callback is contained, so that it
	 * cannot stop the worker. It is reported with {@code console.error}, as an
	 * error and not as debug output: it is a bug of the host that nothing else
	 * would show.
	 */
	private static void deliver(StringCallback callback, String value) {
		if (callback == null)
			return;
		try {
			callback.call(value);
		} catch (Throwable t) {
			consoleError("PlantUML: callback threw: " + t);
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

	/** Reports an error to the console, whatever {@code PLANTUML_DEBUG} says. */
	@JSBody(params = "msg", script = "console.error(msg);")
	private static native void consoleError(String msg);

	/** Serializes an SVG DOM element to a string. */
	@JSBody(params = "svg", script = "return new XMLSerializer().serializeToString(svg);")
	private static native String serializeSvg(Element svg);

}
