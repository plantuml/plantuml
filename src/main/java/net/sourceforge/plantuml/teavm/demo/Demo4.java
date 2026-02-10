package net.sourceforge.plantuml.teavm.demo;

import java.util.ArrayList;
import java.util.List;

import org.teavm.jso.JSBody;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLTextAreaElement;
import org.teavm.jso.dom.xml.Element;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.api.PSystemFactory;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.mindmap.MindMapDiagramFactory;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.teavm.PSystemBuilder2;
import net.sourceforge.plantuml.teavm.StringBounderTeaVM;
import net.sourceforge.plantuml.teavm.SvgGraphicsTeaVM;
import net.sourceforge.plantuml.teavm.UGraphicTeaVM;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.utils.LineLocationImpl;
import net.sourceforge.plantuml.wbs.WBSDiagramFactory;

public class Demo4 {

	private static final StringBounder STRING_BOUNDER = new StringBounderTeaVM();
	private static final ColorMapper COLOR_MAPPER = ColorMapper.TEAVM;
	private static final HColor BACK = HColors.WHITE;
	private final static PSystemBuilder2 BUILDER = new PSystemBuilder2();

	public static void main(String[] args) {
		HTMLDocument doc = HTMLDocument.current();

		HTMLTextAreaElement textarea = (HTMLTextAreaElement) doc.getElementById("src");
		HTMLElement out = doc.getElementById("out");

		textarea.setValue(defaultSource());

		render(out, textarea.getValue());

		textarea.addEventListener("input",
				(EventListener<Event>) evt -> render(out, textarea.getValue()));
	}

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
				+ "Alice -> Bob : hello\n"
				+ "@enduml\n";
	}

	private static String defaultSource1() {
		return ""
				+ "@startmindmap\n"
				+ "* Hello from TeaVM\n"
				+ "** Java\n"
				+ "** JavaScript\n"
				+ "** Live update\n"
				+ "@endmindmap\n";
	}

	@JSBody(params = "s", script = "return s.split(/\\r\\n|\\r|\\n/);")
	private static native String[] splitLines(String s);

	@JSBody(params = { "parent", "svg" }, script = "parent.appendChild(svg);")
	private static native void appendSvgElement(HTMLElement parent, Element svg);

	@JSBody(params = "el", script = "while (el.firstChild) el.removeChild(el.firstChild);")
	private static native void removeAllChildren(HTMLElement el);
}
