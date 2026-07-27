package org.teavm.jso.dom.xml;

import org.teavm.jso.JSObject;

/**
 * Compile-only stub of TeaVM's {@code Element} (generic XML/SVG DOM node).
 * Declares only the methods PlantUML's TeaVM sources actually call
 * (setAttribute, appendChild, setTextContent); no bodies to give since these
 * are interface methods and nothing in this jar ever produces a real
 * instance to call them on (see clide_stubs/teavm/README.md).
 */
public interface Element extends JSObject {

	void setAttribute(String name, String value);

	void appendChild(Element child);

	void setTextContent(String text);

}
