package net.sourceforge.plantuml.xmi;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;

public abstract class XmiSequenceDiagram {
    // ::remove folder when __HAXE__

	protected final SequenceDiagram diagram;

	public abstract void build();

	protected final Document document;

	public XmiSequenceDiagram(SequenceDiagram diagram, Document document) {
		super();
		this.diagram = diagram;
		this.document = document;
	}

	protected Element createElement(String tag, String[][] attributes) {
		return setAttributes(document.createElement(tag), attributes);
	}

	protected Element setAttribute(Element element, String name, String value) {
		element.setAttribute(name, value);
		return element;
	}

	protected Element setAttributes(Element element, String[][] attributes) {
		for (String[] attr : attributes) {
			element.setAttribute(attr[0], attr[1]);
		}
		return element;
	}

	protected String getDisplayString(Display display) {
		return String.join("\n", display.asList());
	}

	protected String getXmiId(String tag, Object object) {
		return Integer.toHexString(tag.hashCode()) + "_" + Integer.toHexString(object.hashCode());
	}
}
