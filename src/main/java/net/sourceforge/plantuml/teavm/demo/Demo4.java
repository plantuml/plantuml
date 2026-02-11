package net.sourceforge.plantuml.teavm.demo;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.xml.Element;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.teavm.PSystemBuilder2;
import net.sourceforge.plantuml.teavm.StringBounderTeaVM;
import net.sourceforge.plantuml.teavm.SvgGraphicsTeaVM;
import net.sourceforge.plantuml.teavm.UGraphicTeaVM;

/**
 * PlantUML TeaVM API - exports functions callable from JavaScript.
 * 
 * JavaScript handles all UI and scheduling (debounce, etc.).
 * Java provides the render worker that runs in TeaVM coroutine context
 * (required for GraphViz async).
 * 
 * Architecture:
 * 1. JS calls plantumlRequestRender(lines, elementId) to request a render
 * 2. Java worker thread wakes up and performs the render
 * 3. Debouncing/throttling is done in JS, not Java
 */
public class Demo4 {

    private static final StringBounder STRING_BOUNDER = new StringBounderTeaVM();
    private static final ColorMapper COLOR_MAPPER = ColorMapper.TEAVM;
    private static final HColor BACK = HColors.WHITE;
    private static final PSystemBuilder2 BUILDER = new PSystemBuilder2();

    // Render request state
    private static final Object LOCK = new Object();
    private static volatile boolean workerStarted;
    private static volatile String[] pendingLines;
    private static volatile String pendingElementId;
    private static volatile boolean hasRequest;

    /**
     * Main entry point - starts worker and registers JS API.
     */
    public static void main(String[] args) {
        startWorker();
        registerRequestRender(Demo4::requestRender);
        consoleLog("PlantUML TeaVM loaded. Call plantumlRequestRender(linesArray, elementId).");
    }

    @JSBody(params = "callback", script = "window.plantumlRequestRender = callback;")
    private static native void registerRequestRender(RequestRenderCallback callback);

    @JSFunctor
    public interface RequestRenderCallback extends JSObject {
        void request(String[] lines, String elementId);
    }

    /**
     * Called from JavaScript to request a render.
     * JS is responsible for debouncing and splitting lines.
     */
    private static void requestRender(String[] lines, String elementId) {
        pendingLines = lines;
        pendingElementId = elementId;
        hasRequest = true;

        synchronized (LOCK) {
            LOCK.notifyAll();
        }
    }

    private static void startWorker() {
        if (workerStarted)
            return;
        workerStarted = true;
        new Thread(Demo4::renderLoop, "plantuml-render").start();
    }

    private static void renderLoop() {
        while (true) {
            synchronized (LOCK) {
                while (!hasRequest) {
                    try {
                        LOCK.wait();
                    } catch (InterruptedException e) {
                        // ignore
                    }
                }
                hasRequest = false;
            }

            final String[] lines = pendingLines;
            final String elementId = pendingElementId;
            if (lines == null || elementId == null)
                continue;

            doRender(elementId, lines);
        }
    }

    private static void doRender(String elementId, String[] lines) {
        HTMLDocument doc = HTMLDocument.current();
        HTMLElement out = doc.getElementById(elementId);
        if (out == null) {
            consoleLog("PlantUML: element not found: " + elementId);
            return;
        }

        try {
            removeAllChildren(out);

            SvgGraphicsTeaVM svg = new SvgGraphicsTeaVM(900, 900);
            UGraphicTeaVM ug = UGraphicTeaVM.build(BACK, COLOR_MAPPER, STRING_BOUNDER, svg);

            Diagram diagram = BUILDER.createDiagram(lines);
            diagram.exportDiagramGraphic(ug, new FileFormatOption(FileFormat.SVG));

            appendSvgElement(out, svg.getSvgRoot());
        } catch (Exception e) {
            out.setTextContent(String.valueOf(e));
        }
    }

    @JSBody(params = "msg", script = "console.log(msg);")
    private static native void consoleLog(String msg);

    @JSBody(params = { "parent", "svg" }, script = "parent.appendChild(svg);")
    private static native void appendSvgElement(HTMLElement parent, Element svg);

    @JSBody(params = "el", script = "while (el.firstChild) el.removeChild(el.firstChild);")
    private static native void removeAllChildren(HTMLElement el);
}
