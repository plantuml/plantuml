/* XmlElement.java
 * =========================================================================
 * This file is NOT part of upstream JLaTeXMath, it was added for PLAN.md
 * (phase 3, step 3d). A small element of an XML file, for the reader that replaces javax.xml and org.w3c.dom (not in the TeaVM class library).
 * It is released under the same terms as the library: GNU GPL version 2 or
 * later, with the GNU linking exception (see the header of the other files of
 * this package).
 * =========================================================================
 */

package org.scilab.forge.jlatexmath;

import java.util.ArrayList;
import java.util.List;

/**
 * An element of an XML file: its name, its attributes and its child elements. It
 * has what the parsers of the library used of {@code org.w3c.dom.Element}, with
 * the same results: {@link #getAttribute} gives "" for an attribute that is not
 * there, and {@link #getElementsByTagName} gives all the descendants of that
 * name, in the order of the document. The text between the elements is not kept
 * (the files have none that matters).
 */
public final class XmlElement {

    private final String tagName;
    private final List<String> attributeNames = new ArrayList<String>();
    private final List<String> attributeValues = new ArrayList<String>();
    private final List<XmlElement> children = new ArrayList<XmlElement>();

    XmlElement(String tagName) {
        this.tagName = tagName;
    }

    void addAttribute(String name, String value) {
        for (int i = 0; i < attributeNames.size(); i++) {
            if (attributeNames.get(i).equals(name)) {
                // the last one wins, as DOM does when the file is not valid
                attributeValues.set(i, value);
                return;
            }
        }
        attributeNames.add(name);
        attributeValues.add(value);
    }

    void addChild(XmlElement child) {
        children.add(child);
    }

    public String getTagName() {
        return tagName;
    }

    /** The value of the attribute, or "" if there is none. */
    public String getAttribute(String name) {
        for (int i = 0; i < attributeNames.size(); i++) {
            if (attributeNames.get(i).equals(name)) {
                return attributeValues.get(i);
            }
        }
        return "";
    }

    public boolean hasAttribute(String name) {
        return attributeNames.contains(name);
    }

    /** The names of the attributes, in the order of the file. */
    public List<String> getAttributeNames() {
        return new ArrayList<String>(attributeNames);
    }

    /** The child elements, in the order of the file. */
    public List<XmlElement> getChildren() {
        return new ArrayList<XmlElement>(children);
    }

    /** All the descendants with this name (not the element itself), in the order of the file. */
    public XmlNodeList getElementsByTagName(String name) {
        final List<XmlElement> result = new ArrayList<XmlElement>();
        collect(name, result);
        return new XmlNodeList(result);
    }

    private void collect(String name, List<XmlElement> result) {
        for (int i = 0; i < children.size(); i++) {
            final XmlElement child = children.get(i);
            if (child.tagName.equals(name)) {
                result.add(child);
            }
            child.collect(name, result);
        }
    }
}
