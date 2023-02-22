package net.sourceforge.plantuml.xml;

import java.util.Stack;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlStackBuilderThingy extends Stack<Element> {
	static final long serialVersionUID = 1l;
	private Document doc;

	public XmlStackBuilderThingy(Document doc) {
		super();
		this.doc = doc;
	}

	@Override
	public Element push(Element e) {
		if (this.empty())
			this.doc.appendChild(e);
		else
			super.peek().appendChild(e);
		super.push(e);
		return e;
	}
}

