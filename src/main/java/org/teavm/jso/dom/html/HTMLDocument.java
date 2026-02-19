package org.teavm.jso.dom.html;

import org.teavm.jso.dom.xml.Document;

/**
 * Mock for org.teavm.jso.dom.html.HTMLDocument.
 * <p>
 * In the real TeaVM runtime, this interface represents the browser's
 * {@code document} object. Here it provides only the methods actually used
 * by PlantUML code, as a compilation stub.
 */
public abstract class HTMLDocument implements Document {

	/**
	 * Returns the current HTML document ({@code window.document}).
	 *
	 * @return the current document instance
	 */
	public static HTMLDocument current() {
		throw new UnsupportedOperationException("TeaVM mock — not available outside browser");
	}

	/**
	 * Returns the element with the specified ID, or {@code null} if not found.
	 *
	 * @param id the element ID to look up
	 * @return the matching element, or {@code null}
	 */
	public abstract HTMLElement getElementById(String id);

}
