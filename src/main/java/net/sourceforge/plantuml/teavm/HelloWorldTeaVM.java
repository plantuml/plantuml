package net.sourceforge.plantuml.teavm;

import org.teavm.jso.JSBody;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * Simple Hello World example for TeaVM integration testing.
 * This class demonstrates basic DOM manipulation using TeaVM's JSO API.
 */
public class HelloWorldTeaVM {
    
    /**
     * Entry point for TeaVM application.
     * This method is called when the JavaScript is loaded in the browser.
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
        
        // Create paragraph
        HTMLElement paragraph = document.createElement("p");
        paragraph.setAttribute("style", "color: #555; font-size: 1.1em; line-height: 1.6;");
        paragraph.setInnerHTML("TeaVM is successfully compiling Java to JavaScript!");
        
        // Create info paragraph
        HTMLElement info = document.createElement("p");
        info.setAttribute("style", "color: #888; font-size: 0.9em; margin-top: 30px; " +
                "padding-top: 20px; border-top: 1px solid #eee;");
        info.setInnerHTML("This page is running Java code compiled to JavaScript by TeaVM.");
        
        // Append elements
        container.appendChild(heading);
        container.appendChild(paragraph);
        container.appendChild(info);
        body.appendChild(container);
        
        // Log to console
        consoleLog("PlantUML TeaVM Hello World is running!");
    }
    
    /**
     * Native JavaScript console.log binding.
     * @param message The message to log
     */
    @JSBody(params = "message", script = "console.log(message);")
    private static native void consoleLog(String message);
}
