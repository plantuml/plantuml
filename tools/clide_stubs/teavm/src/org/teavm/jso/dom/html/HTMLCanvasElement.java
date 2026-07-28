package org.teavm.jso.dom.html;

import org.teavm.jso.JSObject;

/**
 * Compile-only stub of TeaVM's {@code HTMLCanvasElement}. {@code getContext}
 * returns the generic {@link JSObject} marker rather than the real (broader)
 * TeaVM return type: PlantUML immediately casts the result to
 * {@code CanvasRenderingContext2D}, and an interface-to-interface cast always
 * compiles regardless of a declared inheritance relationship, so this is
 * enough (see clide_stubs/teavm/README.md).
 */
public interface HTMLCanvasElement extends HTMLElement {

	void setWidth(int width);

	void setHeight(int height);

	JSObject getContext(String contextId);

}
