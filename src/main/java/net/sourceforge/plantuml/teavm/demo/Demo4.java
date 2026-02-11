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
 * PlantUML TeaVM API.
 * 
 * GraphViz async requires TeaVM coroutine context, so we use a worker thread.
 * JS handles UI/debouncing, Java handles rendering.
 */
public class Demo4 {

    private static final StringBounder STRING_BOUNDER = new StringBounderTeaVM();
    private static final ColorMapper COLOR_MAPPER = ColorMapper.TEAVM;
    private static final HColor BACK = HColors.WHITE;
    private static final PSystemBuilder2 BUILDER = new PSystemBuilder2();

    private static final Object LOCK = new Object();
    private static volatile String[] pendingLines;
    private static volatile String pendingElementId;

    public static void main(String[] args) {
        new Thread(Demo4::workerLoop, "plantuml-render").start();
        registerRender(Demo4::requestRender);
        consoleLog("PlantUML TeaVM loaded.");
    }

    private static void workerLoop() {
        while (true) {
            String[] lines;
            String elementId;

            synchronized (LOCK) {
                while (pendingLines == null) {
                    try {
                        LOCK.wait();
                    } catch (InterruptedException e) {
                    }
                }
                lines = pendingLines;
                elementId = pendingElementId;
                pendingLines = null;
            }

            doRender(lines, elementId);
        }
    }

    @JSBody(params = "cb", script = "window.plantumlRender = cb;")
    private static native void registerRender(RenderCallback cb);

    @JSFunctor
    public interface RenderCallback extends JSObject {
        void call(String[] lines, String elementId);
    }

    private static void requestRender(String[] lines, String elementId) {
        synchronized (LOCK) {
            pendingLines = lines;
            pendingElementId = elementId;
            LOCK.notify();
        }
    }

    private static void doRender(String[] lines, String elementId) {
        HTMLElement out = HTMLDocument.current().getElementById(elementId);
        if (out == null)
            return;

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

    @JSBody(params = { "p", "svg" }, script = "p.appendChild(svg);")
    private static native void appendSvgElement(HTMLElement p, Element svg);

    @JSBody(params = "el", script = "while(el.firstChild)el.removeChild(el.firstChild);")
    private static native void removeAllChildren(HTMLElement el);
}
