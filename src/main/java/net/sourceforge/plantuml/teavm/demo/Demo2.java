package net.sourceforge.plantuml.teavm.demo;

import org.teavm.jso.JSBody;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.xml.Element;

import net.sourceforge.plantuml.teavm.SvgGraphicsTeaVM;

/**
 * Hello World example for TeaVM integration testing.
 * Demonstrates DOM manipulation and SVG generation using TeaVM's JSO API.
 */
public class Demo2 {
    
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
        container.setAttribute("style", "max-width: 800px; margin: 40px auto; padding: 40px; " +
                "background: white; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);");
        
        // Create heading
        HTMLElement heading = document.createElement("h1");
        heading.setAttribute("style", "color: #333; margin: 0 0 20px 0;");
        heading.setInnerHTML("Hello2 from PlantUML + TeaVM!");
        container.appendChild(heading);
        
        // Create paragraph
        HTMLElement paragraph = document.createElement("p");
        paragraph.setAttribute("style", "color: #555; font-size: 1.1em; line-height: 1.6;");
        paragraph.setInnerHTML("TeaVM is successfully compiling Java to JavaScript!");
        container.appendChild(paragraph);
        
        // Create SVG demo section
        HTMLElement svgSection = document.createElement("div");
        svgSection.setAttribute("style", "margin-top: 30px; padding: 20px; " +
                "background: #f9f9f9; border-radius: 4px;");
        
        HTMLElement svgTitle = document.createElement("h3");
        svgTitle.setAttribute("style", "color: #555; margin: 0 0 15px 0;");
        svgTitle.setInnerHTML("SVG Generation Demo:");
        svgSection.appendChild(svgTitle);
        
        // Create SVG using SvgGraphicsTeaVM
        SvgGraphicsTeaVM svg = new SvgGraphicsTeaVM(300, 150);
        
        // Draw a simple diagram
        svg.setFillColor("#E3F2FD");
        svg.setStrokeColor("#1976D2");
        svg.setStrokeWidth(2);
        svg.drawRectangle(20, 20, 100, 50, 5, 5);
        
        svg.setFillColor("#333");
        svg.drawText("Class A", 45, 50, "Arial", 14);
        
        svg.setFillColor("#E8F5E9");
        svg.setStrokeColor("#388E3C");
        svg.drawRectangle(180, 20, 100, 50, 5, 5);
        
        svg.setFillColor("#333");
        svg.drawText("Class B", 205, 50, "Arial", 14);
        
        // Draw arrow
        svg.setStrokeColor("#666");
        svg.setStrokeWidth(1.5);
        svg.drawLine(120, 45, 180, 45);
        svg.drawPolygon(175, 40, 180, 45, 175, 50);
        
        // Draw circle with centered text
        svg.setFillColor("#FFF3E0");
        svg.setStrokeColor("#F57C00");
        double circleCx = 150;
        double circleCy = 110;
        double circleR = 25;
        svg.drawCircle(circleCx, circleCy, circleR);
        
        // Center "Node" text in circle
        String nodeText = "Node";
        int nodeFontSize = 12;
        double[] nodeMetrics = SvgGraphicsTeaVM.measureText(nodeText, "Arial", nodeFontSize);
        double nodeWidth = nodeMetrics[0];
        double nodeHeight = nodeMetrics[1];
        double nodeX = circleCx - nodeWidth / 2;
        double nodeY = circleCy + nodeHeight / 2;  // baseline is at bottom of text
        
        svg.setFillColor("#333");
        svg.drawText(nodeText, nodeX, nodeY, "Arial", nodeFontSize);
        
        // Append SVG to section
        Element svgElement = svg.getSvgRoot();
        appendSvgElement(svgSection, svgElement);
        
        container.appendChild(svgSection);
        

        // Create info paragraph
        HTMLElement info = document.createElement("p");
        info.setAttribute("style", "color: #888; font-size: 0.9em; margin-top: 30px; " +
                "padding-top: 20px; border-top: 1px solid #eee;");
        info.setInnerHTML("This page is running Java code compiled to JavaScript by TeaVM. " +
                "The SVG above was generated programmatically using SvgGraphicsTeaVM.");
        container.appendChild(info);
        
        body.appendChild(container);
        
        // Log to console
        consoleLog("PlantUML TeaVM Hello World is running!");
        consoleLog("SVG generated: " + svg.toSvgString().substring(0, 100) + "...");
    }
    
    @JSBody(params = {"parent", "svgElement"}, script = "parent.appendChild(svgElement);")
    private static native void appendSvgElement(HTMLElement parent, Element svgElement);
    
    @JSBody(params = "message", script = "console.log(message);")
    private static native void consoleLog(String message);
    
    @JSBody(script = "return performance.now();")
    private static native double now();
}
