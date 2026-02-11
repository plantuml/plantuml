package net.sourceforge.plantuml.teavm.demo;

import org.teavm.jso.JSBody;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLTextAreaElement;
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

public class Demo4 {

    private static final StringBounder STRING_BOUNDER = new StringBounderTeaVM();
    private static final ColorMapper COLOR_MAPPER = ColorMapper.TEAVM;
    private static final HColor BACK = HColors.WHITE;
    private static final PSystemBuilder2 BUILDER = new PSystemBuilder2();

    // Debounce / scheduler state (no JS timer)
    private static final Object LOCK = new Object();
    private static volatile boolean workerStarted;
    private static volatile boolean dirty;
    private static volatile String pendingText;

    public static void main(String[] args) {
        HTMLDocument doc = HTMLDocument.current();

        HTMLTextAreaElement textarea = (HTMLTextAreaElement) doc.getElementById("src");
        HTMLElement out = doc.getElementById("out");

        textarea.setValue(defaultSource());

        // first render: OK from main
        render(out, textarea.getValue());

        // event: DO NOT call render here, just enqueue
        textarea.addEventListener("input", (EventListener<Event>) evt -> {
            enqueueRender(out, textarea.getValue());
        });
    }

    private static void enqueueRender(HTMLElement out, String text) {
        pendingText = text;
        dirty = true;

        startWorkerIfNeeded(out);

        synchronized (LOCK) {
            LOCK.notifyAll();
        }
    }

    private static void startWorkerIfNeeded(HTMLElement out) {
        if (workerStarted) {
            return;
        }
        workerStarted = true;

        new Thread(() -> renderLoop(out), "plantuml-render-loop").start();
    }

    private static void renderLoop(HTMLElement out) {
        String lastRendered = null;

        while (true) {
            // Wait until there is work
            synchronized (LOCK) {
                while (!dirty) {
                    try {
                        LOCK.wait();
                    } catch (InterruptedException e) {
                        // ignore
                    }
                }
            }

            // Debounce: wait for typing to stabilize
            while (true) {
                dirty = false;
                try {
                    Thread.sleep(150); // tune as you like
                } catch (InterruptedException e) {
                    // ignore
                }
                if (!dirty) {
                    break; // stable
                }
            }

            final String text = pendingText;
            if (text == null || text.equals(lastRendered)) {
                continue;
            }

            // IMPORTANT: this runs inside TeaVM Thread/coroutine context
            render(out, text);
            lastRendered = text;
        }
    }

    // GraphViz async happens here -> must be called from TeaVM threading/coroutine context
    private static void render(HTMLElement out, String text) {
        try {
            removeAllChildren(out);

            SvgGraphicsTeaVM svg = new SvgGraphicsTeaVM(900, 900);
            UGraphicTeaVM ug = UGraphicTeaVM.build(BACK, COLOR_MAPPER, STRING_BOUNDER, svg);

            String[] split = splitLines(text);

            Diagram diagram = BUILDER.createDiagram(split);
            diagram.exportDiagramGraphic(ug, new FileFormatOption(FileFormat.SVG));

            appendSvgElement(out, svg.getSvgRoot());
        } catch (Exception e) {
            out.setTextContent(e.toString());
        }
    }

    private static String defaultSource() {
        return ""
                + "@startuml\n"
                + "class a\n"
                + "class b\n"
                + "class c\n"
                + "a --> b\n"
                + "a --> c\n"
                + "@enduml\n";
    }

    @JSBody(params = "s", script = "return s.split(/\\r\\n|\\r|\\n/);")
    private static native String[] splitLines(String s);

    @JSBody(params = { "parent", "svg" }, script = "parent.appendChild(svg);")
    private static native void appendSvgElement(HTMLElement parent, Element svg);

    @JSBody(params = "el", script = "while (el.firstChild) el.removeChild(el.firstChild);")
    private static native void removeAllChildren(HTMLElement el);
}
