package net.sourceforge.plantuml.teavm;

import org.teavm.jso.JSBody;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.xml.Element;

/**
 * Hello World example for TeaVM integration testing.
 * Demonstrates DOM manipulation and SVG generation using TeaVM's JSO API.
 */
public class HelloWorldTeaVM {
    
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
        heading.setInnerHTML("Hello from PlantUML + TeaVM!");
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
        
        // Draw circle
        svg.setFillColor("#FFF3E0");
        svg.setStrokeColor("#F57C00");
        svg.drawCircle(150, 110, 25);
        
        svg.setFillColor("#333");
        svg.drawText("Node", 132, 115, "Arial", 12);
        
        // Append SVG to section
        Element svgElement = svg.getSvgRoot();
        appendSvgElement(svgSection, svgElement);
        
        container.appendChild(svgSection);
        
        // Text measurement demo
        HTMLElement measureSection = document.createElement("div");
        measureSection.setAttribute("style", "margin-top: 20px; padding: 15px; " +
                "background: #fff8e1; border-radius: 4px; font-family: monospace; font-size: 13px;");
        
        String testText = "Hello PlantUML!";
        String fontFamily = "Arial";
        int fontSize = 16;
        
        double[] canvasMetrics = SvgGraphicsTeaVM.measureText(testText, fontFamily, fontSize);
        double[] svgMetrics = SvgGraphicsTeaVM.measureTextSvgBBox(testText, fontFamily, fontSize);
        double[] detailed = SvgGraphicsTeaVM.getDetailedTextMetrics(testText, fontFamily, fontSize, "normal");
        
        StringBuilder metricsHtml = new StringBuilder();
        metricsHtml.append("<strong>Text Measurement Demo:</strong><br><br>");
        metricsHtml.append("Text: \"" + testText + "\" (" + fontFamily + " " + fontSize + "px)<br><br>");
        metricsHtml.append("<u>Canvas measureText():</u><br>");
        metricsHtml.append("&nbsp;&nbsp;Width: " + String.format("%.2f", canvasMetrics[0]) + "px<br>");
        metricsHtml.append("&nbsp;&nbsp;Height: " + String.format("%.2f", canvasMetrics[1]) + "px<br><br>");
        metricsHtml.append("<u>SVG getBBox():</u><br>");
        metricsHtml.append("&nbsp;&nbsp;Width: " + String.format("%.2f", svgMetrics[0]) + "px<br>");
        metricsHtml.append("&nbsp;&nbsp;Height: " + String.format("%.2f", svgMetrics[1]) + "px<br><br>");
        metricsHtml.append("<u>Detailed metrics:</u><br>");
        metricsHtml.append("&nbsp;&nbsp;Ascent: " + String.format("%.2f", detailed[1]) + "px<br>");
        metricsHtml.append("&nbsp;&nbsp;Descent: " + String.format("%.2f", detailed[2]) + "px");
        
        measureSection.setInnerHTML(metricsHtml.toString());
        container.appendChild(measureSection);
        
        // Performance benchmark
        HTMLElement perfSection = document.createElement("div");
        perfSection.setAttribute("style", "margin-top: 20px; padding: 15px; " +
                "background: #e8f5e9; border-radius: 4px; font-family: monospace; font-size: 13px;");
        
        int iterations = 10000;
        String[] testStrings = {"A", "Hello", "Hello PlantUML!", "This is a longer text for testing"};
        
        StringBuilder perfHtml = new StringBuilder();
        perfHtml.append("<strong>Performance Benchmark (" + iterations + " iterations):</strong><br><br>");
        
        // Benchmark Canvas (cached)
        double startCanvasCached = now();
        for (int i = 0; i < iterations; i++) {
            SvgGraphicsTeaVM.measureText(testStrings[i % testStrings.length], "Arial", 14);
        }
        double canvasCachedTime = now() - startCanvasCached;
        
        // Benchmark Canvas (no cache)
        double startCanvasNoCache = now();
        for (int i = 0; i < iterations; i++) {
            SvgGraphicsTeaVM.measureTextCanvasNoCache(testStrings[i % testStrings.length], "Arial", 14, "normal");
        }
        double canvasNoCacheTime = now() - startCanvasNoCache;
        
        // Benchmark SVG getBBox
        double startSvg = now();
        for (int i = 0; i < iterations; i++) {
            SvgGraphicsTeaVM.measureTextSvgBBox(testStrings[i % testStrings.length], "Arial", 14);
        }
        double svgTime = now() - startSvg;
        
        perfHtml.append("<u>Canvas (cached):</u><br>");
        perfHtml.append("&nbsp;&nbsp;" + String.format("%.2f", canvasCachedTime) + " ms total<br>");
        perfHtml.append("&nbsp;&nbsp;" + String.format("%.4f", canvasCachedTime / iterations) + " ms/call<br><br>");
        
        perfHtml.append("<u>Canvas (no cache):</u><br>");
        perfHtml.append("&nbsp;&nbsp;" + String.format("%.2f", canvasNoCacheTime) + " ms total<br>");
        perfHtml.append("&nbsp;&nbsp;" + String.format("%.4f", canvasNoCacheTime / iterations) + " ms/call<br><br>");
        
        perfHtml.append("<u>SVG getBBox():</u><br>");
        perfHtml.append("&nbsp;&nbsp;" + String.format("%.2f", svgTime) + " ms total<br>");
        perfHtml.append("&nbsp;&nbsp;" + String.format("%.4f", svgTime / iterations) + " ms/call<br><br>");
        
        // Find fastest
        double fastest = Math.min(canvasCachedTime, Math.min(canvasNoCacheTime, svgTime));
        String winner = canvasCachedTime == fastest ? "Canvas (cached)" : 
                       (canvasNoCacheTime == fastest ? "Canvas (no cache)" : "SVG getBBox");
        perfHtml.append("<strong>Winner: " + winner + "</strong><br>");
        perfHtml.append("Canvas cached vs no-cache: " + String.format("%.1f", canvasNoCacheTime / canvasCachedTime) + "x<br>");
        perfHtml.append("Canvas cached vs SVG: " + String.format("%.1f", svgTime / canvasCachedTime) + "x");
        
        perfSection.setInnerHTML(perfHtml.toString());
        container.appendChild(perfSection);
        
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
