/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 *
 */
package net.sourceforge.plantuml.teavm;

import org.teavm.jso.JSBody;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.xml.Element;
import org.teavm.jso.dom.xml.Document;

/**
 * SVG Graphics implementation for TeaVM.
 * Uses browser's native DOM API instead of javax.xml.
 */
public class SvgGraphicsTeaVM {

    private static final String SVG_NS = "http://www.w3.org/2000/svg";
    
    private final Element svgRoot;
    private final Element defs;
    private final Element mainGroup;
    private final Document document;
    
    private String fillColor = "black";
    private String strokeColor = "black";
    private double strokeWidth = 1.0;
    
    public SvgGraphicsTeaVM(int width, int height) {
        this.document = HTMLDocument.current();
        
        // Create SVG root element
        this.svgRoot = createSvgElement("svg");
        svgRoot.setAttribute("xmlns", SVG_NS);
        svgRoot.setAttribute("version", "1.1");
        svgRoot.setAttribute("width", String.valueOf(width));
        svgRoot.setAttribute("height", String.valueOf(height));
        svgRoot.setAttribute("viewBox", "0 0 " + width + " " + height);
        
        // Create defs for gradients, filters, etc.
        this.defs = createSvgElement("defs");
        svgRoot.appendChild(defs);
        
        // Create main group for all drawings
        this.mainGroup = createSvgElement("g");
        svgRoot.appendChild(mainGroup);
    }
    
    @JSBody(params = {"tagName"}, script = 
        "return document.createElementNS('http://www.w3.org/2000/svg', tagName);")
    private static native Element createSvgElement(String tagName);
    
    public Element getSvgRoot() {
        return svgRoot;
    }
    
    public void setFillColor(String color) {
        this.fillColor = color != null ? color : "none";
    }
    
    public void setStrokeColor(String color) {
        this.strokeColor = color != null ? color : "none";
    }
    
    public void setStrokeWidth(double width) {
        this.strokeWidth = width;
    }
    
    public void drawRectangle(double x, double y, double width, double height) {
        drawRectangle(x, y, width, height, 0, 0);
    }
    
    public void drawRectangle(double x, double y, double width, double height, double rx, double ry) {
        Element rect = createSvgElement("rect");
        rect.setAttribute("x", format(x));
        rect.setAttribute("y", format(y));
        rect.setAttribute("width", format(width));
        rect.setAttribute("height", format(height));
        if (rx > 0) rect.setAttribute("rx", format(rx));
        if (ry > 0) rect.setAttribute("ry", format(ry));
        applyStyles(rect);
        mainGroup.appendChild(rect);
    }
    
    public void drawCircle(double cx, double cy, double r) {
        Element circle = createSvgElement("circle");
        circle.setAttribute("cx", format(cx));
        circle.setAttribute("cy", format(cy));
        circle.setAttribute("r", format(r));
        applyStyles(circle);
        mainGroup.appendChild(circle);
    }
    
    public void drawEllipse(double cx, double cy, double rx, double ry) {
        Element ellipse = createSvgElement("ellipse");
        ellipse.setAttribute("cx", format(cx));
        ellipse.setAttribute("cy", format(cy));
        ellipse.setAttribute("rx", format(rx));
        ellipse.setAttribute("ry", format(ry));
        applyStyles(ellipse);
        mainGroup.appendChild(ellipse);
    }
    
    public void drawLine(double x1, double y1, double x2, double y2) {
        Element line = createSvgElement("line");
        line.setAttribute("x1", format(x1));
        line.setAttribute("y1", format(y1));
        line.setAttribute("x2", format(x2));
        line.setAttribute("y2", format(y2));
        applyStrokeStyle(line);
        mainGroup.appendChild(line);
    }
    
    public void drawPolyline(double... points) {
        Element polyline = createSvgElement("polyline");
        polyline.setAttribute("points", formatPoints(points));
        polyline.setAttribute("fill", "none");
        applyStrokeStyle(polyline);
        mainGroup.appendChild(polyline);
    }
    
    public void drawPolygon(double... points) {
        Element polygon = createSvgElement("polygon");
        polygon.setAttribute("points", formatPoints(points));
        applyStyles(polygon);
        mainGroup.appendChild(polygon);
    }
    
    public void drawPath(String pathData) {
        Element path = createSvgElement("path");
        path.setAttribute("d", pathData);
        applyStyles(path);
        mainGroup.appendChild(path);
    }
    
    public void drawText(String text, double x, double y, String fontFamily, int fontSize) {
        Element textElem = createSvgElement("text");
        textElem.setAttribute("x", format(x));
        textElem.setAttribute("y", format(y));
        textElem.setAttribute("font-family", fontFamily);
        textElem.setAttribute("font-size", String.valueOf(fontSize));
        textElem.setAttribute("fill", fillColor);
        textElem.setTextContent(text);
        mainGroup.appendChild(textElem);
    }
    
    public Element createGroup() {
        Element group = createSvgElement("g");
        mainGroup.appendChild(group);
        return group;
    }
    
    public String createLinearGradient(String id, String color1, String color2, boolean horizontal) {
        Element gradient = createSvgElement("linearGradient");
        gradient.setAttribute("id", id);
        if (horizontal) {
            gradient.setAttribute("x1", "0%");
            gradient.setAttribute("y1", "0%");
            gradient.setAttribute("x2", "100%");
            gradient.setAttribute("y2", "0%");
        } else {
            gradient.setAttribute("x1", "0%");
            gradient.setAttribute("y1", "0%");
            gradient.setAttribute("x2", "0%");
            gradient.setAttribute("y2", "100%");
        }
        
        Element stop1 = createSvgElement("stop");
        stop1.setAttribute("offset", "0%");
        stop1.setAttribute("stop-color", color1);
        gradient.appendChild(stop1);
        
        Element stop2 = createSvgElement("stop");
        stop2.setAttribute("offset", "100%");
        stop2.setAttribute("stop-color", color2);
        gradient.appendChild(stop2);
        
        defs.appendChild(gradient);
        return "url(#" + id + ")";
    }
    
    private void applyStyles(Element element) {
        element.setAttribute("fill", fillColor);
        applyStrokeStyle(element);
    }
    
    private void applyStrokeStyle(Element element) {
        element.setAttribute("stroke", strokeColor);
        element.setAttribute("stroke-width", format(strokeWidth));
    }
    
    private String format(double value) {
        if (value == (int) value) {
            return String.valueOf((int) value);
        }
        return String.format("%.2f", value).replace(',', '.');
    }
    
    private String formatPoints(double... points) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < points.length; i += 2) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(format(points[i])).append(",").append(format(points[i + 1]));
        }
        return sb.toString();
    }
    
    /**
     * Returns the SVG as a string (serialized XML).
     */
    @JSBody(params = {"element"}, script = 
        "return new XMLSerializer().serializeToString(element);")
    public static native String serializeToString(Element element);
    
    public String toSvgString() {
        return serializeToString(svgRoot);
    }
    
    // ========================================================================
    // Text measurement methods
    // ========================================================================
    
    /**
     * Measures text dimensions using Canvas API.
     * This is fast and doesn't require adding elements to the DOM.
     * 
     * @param text The text to measure
     * @param fontFamily Font family (e.g., "Arial")
     * @param fontSize Font size in pixels
     * @param fontWeight Font weight (e.g., "normal", "bold")
     * @return Array with [width, height]
     */
    @JSBody(params = {"text", "fontFamily", "fontSize", "fontWeight"}, script = 
        "var canvas = document.createElement('canvas');" +
        "var ctx = canvas.getContext('2d');" +
        "ctx.font = fontWeight + ' ' + fontSize + 'px ' + fontFamily;" +
        "var metrics = ctx.measureText(text);" +
        "var width = metrics.width;" +
        "var height = metrics.actualBoundingBoxAscent + metrics.actualBoundingBoxDescent;" +
        "if (!height) height = fontSize * 1.2;" +  // Fallback for older browsers
        "return [width, height];")
    public static native double[] measureTextCanvas(String text, String fontFamily, int fontSize, String fontWeight);
    
    /**
     * Measures text using Canvas with normal weight.
     */
    public static double[] measureText(String text, String fontFamily, int fontSize) {
        return measureTextCanvas(text, fontFamily, fontSize, "normal");
    }
    
    /**
     * Gets text width only.
     */
    public static double getTextWidth(String text, String fontFamily, int fontSize) {
        return measureText(text, fontFamily, fontSize)[0];
    }
    
    /**
     * Gets text height only.
     */
    public static double getTextHeight(String text, String fontFamily, int fontSize) {
        return measureText(text, fontFamily, fontSize)[1];
    }
    
    /**
     * Measures text using SVG getBBox() method.
     * More accurate but requires the SVG to be in the DOM.
     * 
     * @param text The text to measure
     * @param fontFamily Font family
     * @param fontSize Font size in pixels
     * @return Array with [width, height, x, y] from bounding box
     */
    @JSBody(params = {"text", "fontFamily", "fontSize"}, script = 
        "var svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');" +
        "svg.style.position = 'absolute';" +
        "svg.style.visibility = 'hidden';" +
        "document.body.appendChild(svg);" +
        "var textEl = document.createElementNS('http://www.w3.org/2000/svg', 'text');" +
        "textEl.setAttribute('font-family', fontFamily);" +
        "textEl.setAttribute('font-size', fontSize);" +
        "textEl.textContent = text;" +
        "svg.appendChild(textEl);" +
        "var bbox = textEl.getBBox();" +
        "var result = [bbox.width, bbox.height, bbox.x, bbox.y];" +
        "document.body.removeChild(svg);" +
        "return result;")
    public static native double[] measureTextSvgBBox(String text, String fontFamily, int fontSize);
    
    /**
     * Detailed text metrics using Canvas API.
     * Returns more information about text positioning.
     * 
     * @return Array with [width, actualBoundingBoxAscent, actualBoundingBoxDescent, 
     *         fontBoundingBoxAscent, fontBoundingBoxDescent]
     */
    @JSBody(params = {"text", "fontFamily", "fontSize", "fontWeight"}, script = 
        "var canvas = document.createElement('canvas');" +
        "var ctx = canvas.getContext('2d');" +
        "ctx.font = fontWeight + ' ' + fontSize + 'px ' + fontFamily;" +
        "var m = ctx.measureText(text);" +
        "return [" +
        "  m.width," +
        "  m.actualBoundingBoxAscent || fontSize * 0.8," +
        "  m.actualBoundingBoxDescent || fontSize * 0.2," +
        "  m.fontBoundingBoxAscent || fontSize * 0.8," +
        "  m.fontBoundingBoxDescent || fontSize * 0.2" +
        "];")
    public static native double[] getDetailedTextMetrics(String text, String fontFamily, int fontSize, String fontWeight);
}
