package org.teavm.jso.dom.html;

import org.teavm.jso.dom.xml.Document;
import org.teavm.jso.dom.xml.Element;

/**
 * Compile-only stub of TeaVM's {@code HTMLDocument}. The only method that
 * ever needs a real body is {@link #current()}: it is the single factory
 * PlantUML's TeaVM sources use to obtain a document in the first place, so
 * making it throw guarantees nothing downstream can ever get hold of a real
 * instance either - createElement/getElementById stay abstract on purpose
 * (see clide_stubs/teavm/README.md).
 */
public interface HTMLDocument extends Document {

	static HTMLDocument current() {
		throw new UnsupportedOperationException(
				"TeaVM stub: HTMLDocument.current() is only meant to satisfy the compiler, never to run.");
	}

	Element createElement(String tagName);

	HTMLElement getElementById(String elementId);

}
