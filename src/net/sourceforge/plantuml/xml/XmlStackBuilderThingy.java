package net.sourceforge.plantuml.xml;

import java.util.Stack;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/** This class SUUUCKS, but it could be better! */

public class XmlStackBuilderThingy extends Stack<Element> {
	static final long serialVersionUID = 1l;
	private Document doc;
	private Element ns;
	private Element root;	// non-null when this isn't the whole document builder

	public XmlStackBuilderThingy(Document doc) {
		super();
		this.doc = doc;
		this.root = null;
	}

	private XmlStackBuilderThingy(Document doc, Element root) {
		super();
		this.doc = doc;
		this.root = root;
		ns = super.push(root);
	}

	public static boolean isNamespace(Element e) {
		return e.getTagName() == "UML:Namespace.ownedElement";
	}

	/**
	 * Return a cute stack for adding crap to the last namespace.
	 */
	public XmlStackBuilderThingy getNamespace() {
		if (ns == null) {
			throw new RuntimeException("no namespace");
		}
		return new XmlStackBuilderThingy(doc, ns);
	}

	@Override
	public Element pop() {
		Element e = super.pop();
		if (isNamespace(e)) {
			ns = findNamespace();
		}
		return e;
	}

	@Override
	public Element push(Element e) {
		if (!this.empty())
			super.peek().appendChild(e);
		else if (root == null)
			this.doc.appendChild(e);
		else
			throw new RuntimeException("namespace view and namespace has been popped.");

		super.push(e);
		if (isNamespace(e))
			ns = e;
		return e;
	}

	public Element pushNamespace() {
		return push(doc.createElement("UML:Namespace.ownedElement"));
	}

	public Element findNamespace() {
		for (int i = size() - 1; i >= 0; i--) {
			Element e = get(i);
			if (isNamespace(e))
				return get(i);
		}
		return null;
	}
}

