package net.sourceforge.plantuml.teavm.demo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.teavm.jso.JSBody;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.xml.Element;

import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.mindmap.CommandMindMapOrgmode;
import net.sourceforge.plantuml.mindmap.MindMapDiagramFactory;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.regex.Pattern2;
import net.sourceforge.plantuml.teavm.StringBounderTeaVM;
import net.sourceforge.plantuml.teavm.SvgGraphicsTeaVM;
import net.sourceforge.plantuml.teavm.UGraphicTeaVM;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.utils.LineLocationImpl;

/**
 * Hello World example for TeaVM integration testing. Demonstrates DOM
 * manipulation and SVG generation using TeaVM's JSO API.
 */
public class Demo4 {

	/**
	 * Entry point for TeaVM application.
	 */
	public static void main(String[] args) {
		HTMLDocument document = HTMLDocument.current();
		HTMLElement body = document.getBody();

		// Remove loading message
		HTMLElement loading = document.getElementById("loading");
		if (loading != null) {
			loading.getParentNode().removeChild(loading);
		}

		// Create main container
		HTMLElement container = document.createElement("div");
		container.setAttribute("style", "max-width: 800px; margin: 40px auto; padding: 40px; "
				+ "background: white; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);");

		// Create heading
		HTMLElement heading = document.createElement("h1");
		heading.setAttribute("style", "color: #333; margin: 0 0 20px 0;");
		// heading.setInnerHTML("Hello4 from PlantUML + TeaVM!");
		container.appendChild(heading);

		// Create paragraph
		HTMLElement paragraph = document.createElement("p");
		paragraph.setAttribute("style", "color: #555; font-size: 1.1em; line-height: 1.6;");
		// paragraph.setInnerHTML("TeaVM is successfully compiling Java to JavaScript!");
		container.appendChild(paragraph);

		// Create SVG demo section
		HTMLElement svgSection = document.createElement("div");
		svgSection.setAttribute("style",
				"margin-top: 30px; padding: 20px; " + "background: #f9f9f9; border-radius: 4px;");

		HTMLElement svgTitle = document.createElement("h3");
		svgTitle.setAttribute("style", "color: #555; margin: 0 0 15px 0;");
		// svgTitle.setInnerHTML("SVG Generation Demo:");
		svgSection.appendChild(svgTitle);

		// Create SVG using SvgGraphicsTeaVM
		SvgGraphicsTeaVM svg = new SvgGraphicsTeaVM(800, 800);

		StringBounder stringBounder = new StringBounderTeaVM();
		// consoleLog("stringBounder= " + stringBounder.toString());
		ColorMapper colorMapper = ColorMapper.TEAVM;
		// consoleLog("colorMapper= " + colorMapper.toString());
		HColor back = HColors.BLACK;
		// consoleLog("back= " + back.toString());

		UGraphicTeaVM ug = UGraphicTeaVM.build(back, colorMapper, stringBounder, svg);
		// consoleLog("ug= " + ug.toString());

		final URectangle rect = URectangle.build(30, 30);
		// consoleLog("rect= " + rect.toString());

		InputStream is = Demo4.class.getClassLoader().getResourceAsStream("skin/plantuml.skin");
		System.out.println("is="+is);

		// ug.apply(HColors.BLUE).apply(HColors.RED.bg()).apply(new UTranslate(10, 10)).draw(rect);
		
		// Matcher2 pattern=^([ 	]*[*#]+)(?:\[(#\w+)\])?(_)?[\s ]*(.*)$ input=* Hello from TeaVM
		// Pattern p = Pattern.compile("^(.*)$");
//		Pattern p = Pattern.compile("^([ 	]*[*#]+)(?:\\[(#\\w+)\\])?(_)?[\\s ]*(.*)$");
//		String ss = "* Hello from TeaVM";
//		Matcher m = p.matcher(ss);
//		System.out.println("TESTINIT "+m.find());
//
//		Pattern2 test1 = Pattern2.cmpile("toto");
//		
//		final String aregex = "\\p{L}";
//		System.out.println("aregex = "+aregex);
//		Pattern  atest = Pattern.compile(aregex, Pattern.CASE_INSENSITIVE);
//		System.out.println("atest1 = "+atest);
//		System.out.println("atest2 = "+atest);

//		final String bregex = "\\p{Number}";
//		System.out.println("bregex = "+bregex);
//		Pattern  btest = Pattern.compile(bregex, Pattern.CASE_INSENSITIVE);
//		System.out.println("btest1 = "+btest);
//		System.out.println("btest2 = "+btest);

//		Pattern2 test20 = Pattern2.cmpile("[%pLN]");
//		
//		CommandMindMapOrgmode cmd = new CommandMindMapOrgmode();
//		System.out.println("------------");
//		System.out.println(CommandMindMapOrgmode.getRegexConcat().getPatternAsString());
//		System.out.println("------------");

		MindMapDiagramFactory factory = new MindMapDiagramFactory();
		List<StringLocated> list = new ArrayList<>();
		list.add(new StringLocated("@startmindmap", new LineLocationImpl("foo1", null)));
		list.add(new StringLocated("* Hello from TeaVM", new LineLocationImpl("foo2", null)));
		list.add(new StringLocated("@endmindmap", new LineLocationImpl("foo3", null)));
		System.err.println("list=" + list);
		UmlSource source = UmlSource.create(list, false);
		System.err.println("source=" + source);
		Diagram diagram = factory.createSystem(null, source, null, new PreprocessingArtifact());
		// ::uncomment when __TEAVM__
		// System.err.println("diagram=" + diagram);
		// ::done
		diagram.exportDiagramGraphic(ug, null);

		// Append SVG to section
		Element svgElement = svg.getSvgRoot();
		appendSvgElement(svgSection, svgElement);

		container.appendChild(svgSection);

		// Create info paragraph
		HTMLElement info = document.createElement("p");
		info.setAttribute("style",
				"color: #888; font-size: 0.9em; margin-top: 30px; " + "padding-top: 20px; border-top: 1px solid #eee;");
		info.setInnerHTML("This page is running Java code compiled to JavaScript by TeaVM. "
				+ "The SVG above was generated programmatically using SvgGraphicsTeaVM.");
		container.appendChild(info);

		body.appendChild(container);

		// Log to console
		consoleLog("PlantUML TeaVM Hello World is running! " + stringBounder.toString());
		consoleLog("SVG generated: " + svg.toSvgString().substring(0, 100) + "...");
	}

	@JSBody(params = { "parent", "svgElement" }, script = "parent.appendChild(svgElement);")
	private static native void appendSvgElement(HTMLElement parent, Element svgElement);

	@JSBody(params = "message", script = "console.log(message);")
	private static native void consoleLog(String message);

	@JSBody(script = "return performance.now();")
	private static native double now();
}