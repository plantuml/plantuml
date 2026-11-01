/* XmlNodeList.java
 * =========================================================================
 * This file is NOT part of upstream JLaTeXMath, it was added for PLAN.md
 * (phase 3, step 3d). The list that XmlElement.getElementsByTagName gives, as org.w3c.dom.NodeList did.
 * It is released under the same terms as the library: GNU GPL version 2 or
 * later, with the GNU linking exception (see the header of the other files of
 * this package).
 * =========================================================================
 */

package org.scilab.forge.jlatexmath;

import java.util.List;

public final class XmlNodeList {

    private final List<XmlElement> elements;

    XmlNodeList(List<XmlElement> elements) {
        this.elements = elements;
    }

    public int getLength() {
        return elements.size();
    }

    /** The element at this index, or null if there is none. */
    public XmlElement item(int index) {
        if (index < 0 || index >= elements.size()) {
            return null;
        }
        return elements.get(index);
    }
}
