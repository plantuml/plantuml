package net.sourceforge.plantuml.teavm.js.browser;

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
import net.sourceforge.plantuml.teavm.js.PSystemBuilderJs;
import net.sourceforge.plantuml.teavm.js.StringBounderTeaVM;
import net.sourceforge.plantuml.teavm.js.SvgGraphicsJs;
import net.sourceforge.plantuml.teavm.js.UGraphicJs;

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
 * <h2>No thread, no coroutine</h2>
 *
 * There is no worker thread, and nothing in the engine suspends (no TeaVM
 * coroutine: see tools/teavm-coroutines). What the rendering needs from JavaScript and
 * can only get asynchronously (the Viz.js instance, support scripts such as
 * {@code emoji.js} or {@code <lib>.min.js}, files of
 * {@code PLANTUML_FILE_LOADER}, URLs) is handled by rendering again: an
 * attempt that misses one is discarded, the resources are loaded with
 * JavaScript callbacks, and the diagram is rendered again from its source (see
 * {@link PendingResources} and {@link RenderQueue}).
 *
 * <p>
 * The exported functions only queue the request and return: the rendering
 * starts from a later JavaScript task ({@code setTimeout}), as it did when a
 * worker thread did it, so the callbacks are never called before the function
 * returns.
 *
 * <h2>Order of the answers</h2>
 *
 * Requests are answered one at a time, oldest first. Two rules decide what
 * happens when several are waiting:
 * <ul>
 * <li>{@code renderToString}: every request is kept and answered, in the order
 * received. Each of its callbacks is called exactly once ({@code onSuccess} or
 * {@code onError}); a host that renders many diagrams at the same time gets all
 * of them back.</li>
 * <li>{@code render}: only the latest request is kept, a new one replaces the
 * {@code render} request still waiting, including one waiting for its
 * resources. This is intentional: when a user is typing, we only care about
 * rendering the latest version.</li>
 * </ul>
 *
 * Neither kind drops the other. A callback that throws, or a missing callback,
 * does not stop the engine. A request made from a callback (or while a request
 * waits for its resources) is queued behind the ones already waiting: it is
 * never rendered in the middle of another one.
 *
 * <p>
 * The DOM element of {@code render} is only written with the result of the
 * attempt that is kept, so a discarded attempt never shows.
 *
 * @see net.sourceforge.plantuml.teavm.js.GraphVizEngineJs
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
	// Requests
	// =========================================================================

	/** A {@link #render} request: the SVG goes into a DOM element. */
	private static final class ElementJob extends RenderQueue.Job {
		private final String[] lines;
		private final String elementId;
		private final boolean darkMode;
		private final int maxSvgSize;
		private SvgGraphicsJs svg;
		private Throwable failure;

		private ElementJob(String[] lines, String elementId, boolean darkMode, int maxSvgSize) {
			this.lines = lines;
			this.elementId = elementId;
			this.darkMode = darkMode;
			this.maxSvgSize = maxSvgSize;
		}

		@Override
		protected boolean replaces(RenderQueue.Job waiting) {
			// "Latest wins": a render request still waiting is dropped, in place.
			return waiting instanceof ElementJob;
		}

		@Override
		protected void attempt() {
			BrowserLog.reset();
			svg = null;
			failure = null;
			try {
				svg = buildSvg(lines, darkMode, maxSvgSize);
			} catch (Throwable t) {
				failure = t;
			}
		}

		@Override
		protected void deliver() {
			final HTMLElement out = HTMLDocument.current().getElementById(elementId);
			if (out != null) {
				if (svg != null) {
					removeAllChildren(out);
					appendSvgElement(out, svg.getSvgRoot());
				} else {
					out.setTextContent(String.valueOf(failure));
				}
			}
			BrowserLog.jsStatusDuration();
		}

		@Override
		protected void giveUp(String message) {
			final HTMLElement out = HTMLDocument.current().getElementById(elementId);
			if (out != null)
				out.setTextContent(message);
		}
	}

	/** A {@link #renderToString} request: the SVG goes to a callback. */
	private static final class CallbackJob extends RenderQueue.Job {
		private final String[] lines;
		private final StringCallback onSuccess;
		private final StringCallback onError;
		private final boolean darkMode;
		private final int maxSvgSize;
		private String svg;
		private Throwable failure;

		private CallbackJob(String[] lines, StringCallback onSuccess, StringCallback onError, boolean darkMode,
				int maxSvgSize) {
			this.lines = lines;
			this.onSuccess = onSuccess;
			this.onError = onError;
			this.darkMode = darkMode;
			this.maxSvgSize = maxSvgSize;
		}

		@Override
		protected void attempt() {
			svg = null;
			failure = null;
			try {
				svg = serializeSvg(buildSvg(lines, darkMode, maxSvgSize).getSvgRoot());
			} catch (Throwable t) {
				failure = t;
			}
		}

		/**
		 * Exactly one of the two callbacks is called, once. If the success callback
		 * throws, the error callback is not called as well.
		 */
		@Override
		protected void deliver() {
			if (svg != null)
				PlantUMLBrowser.deliver(onSuccess, svg);
			else
				PlantUMLBrowser.deliver(onError, String.valueOf(failure));
		}

		@Override
		protected void giveUp(String message) {
			PlantUMLBrowser.deliver(onError, message);
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
	 * This call is asynchronous: it returns immediately, and the SVG is inserted
	 * later, once its turn has come and its resources are there (see the class
	 * documentation).
	 *
	 * <p>
	 * If a previous {@code render} request is still waiting, it is replaced by
	 * this one, and its diagram is never drawn. This is the desired behavior for
	 * live-typing scenarios. Waiting {@code renderToString} requests are not
	 * affected.
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
		RenderQueue.submit(new ElementJob(lines, elementId, isDark(options), resolveMaxSvgSize(options)));
	}

	/**
	 * Renders a PlantUML diagram and delivers the resulting SVG as a string via the
	 * {@code onSuccess} callback. Errors go to {@code onError}.
	 *
	 * <p>
	 * Same queueing and asynchronous behavior as {@link #render}: a callback is
	 * never called before this call returns. Unlike {@link #render}, requests are never dropped:
	 * each call is queued behind the ones already waiting, and exactly one of
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
		RenderQueue.submit(new CallbackJob(lines, onSuccess, onError, isDark(options), resolveMaxSvgSize(options)));
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
	// Rendering
	// =========================================================================

	/** Parses and renders PlantUML source lines to an SVG graphics context. */
	private static SvgGraphicsJs buildSvg(String[] lines, boolean darkMode, int maxSvgSize) throws Exception {
		final ColorMapper colorMapper = darkMode ? ColorMapper.TEAVM_DARK : ColorMapper.TEAVM_LIGHT;

		final Diagram diagram = PSystemBuilderJs.getInstance().createDiagram(lines);
		final FileFormatOption fileFormat = new FileFormatOption(FileFormat.SVG);

		if (diagram instanceof UgDiagram == false)
			throw new RuntimeException("Unsupported diagram type");

		final Scale scale = ((AbstractDiagram) diagram).getScale();
		final UgDiagram ugDiagram = (UgDiagram) diagram;
		TextBlock tb = ugDiagram.getTextBlock(0, fileFormat);

		HColor tbBackcolor = tb.getBackcolor();
		if (tbBackcolor == null && diagram instanceof TitledDiagram)
			tbBackcolor = getPaintableDocumentBackground((TitledDiagram) diagram);

		final SvgGraphicsJs svg;

		if (tbBackcolor == null) {
			svg = new SvgGraphicsJs();
			tbBackcolor = darkMode ? HColors.BLACK : HColors.WHITE;
		} else {
			svg = new SvgGraphicsJs(tbBackcolor.toSvg(colorMapper));
		}

		UGraphic ug = UGraphicJs.build(tbBackcolor, colorMapper, STRING_BOUNDER, svg);

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

	/**
	 * Calls a host callback. A missing callback (the host did not pass one) is
	 * ignored, and an exception thrown by the callback is contained, so that it
	 * cannot stop the engine. It is reported with {@code console.error}, as an
	 * error and not as debug output: it is a bug of the host that nothing else
	 * would show.
	 */
	private static void deliver(StringCallback callback, String value) {
		if (callback == null)
			return;
		try {
			callback.call(value);
		} catch (Throwable t) {
			RenderQueue.consoleError("PlantUML: callback threw: " + t);
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
