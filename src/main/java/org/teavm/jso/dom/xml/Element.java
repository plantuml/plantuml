package org.teavm.jso.dom.xml;

import org.teavm.jso.JSObject;

/**
 * Mock for org.teavm.jso.dom.xml.Element.
 * <p>
 * In the real TeaVM runtime, this interface represents a DOM Element node.
 * Here it serves only as a compilation stub.
 */
public interface Element extends JSObject {

	void setAttribute(String name, String value);

	void appendChild(Element child);

	void setTextContent(String text);

}
