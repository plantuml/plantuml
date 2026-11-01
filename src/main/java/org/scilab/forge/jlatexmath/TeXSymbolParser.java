/* TeXSymbolParser.java
 * =========================================================================
 * This file is originally part of the JMathTeX Library - http://jmathtex.sourceforge.net
 *
 * Copyright (C) 2004-2007 Universiteit Gent
 * Copyright (C) 2009 DENIZET Calixte
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program (see
 * the META-INF directory in the source jar). This license can also be
 * found on the GNU website at http://www.gnu.org/licenses/gpl.html.
 *
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 *
 * Linking this library statically or dynamically with other modules
 * is making a combined work based on this library. Thus, the terms
 * and conditions of the GNU General Public License cover the whole
 * combination.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce
 * an executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under terms
 * of your choice, provided that you also meet, for each linked independent
 * module, the terms and conditions of the license of that module.
 * An independent module is a module which is not derived from or based
 * on this library. If you modify this library, you may extend this exception
 * to your version of the library, but you are not obliged to do so.
 * If you do not wish to do so, delete this exception statement from your
 * version.
 *
 */

package org.scilab.forge.jlatexmath;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Parses TeX symbol definitions from an XML-file.
 */
public class TeXSymbolParser {

    public static final String RESOURCE_NAME = "TeXSymbols.xml",
                               DELIMITER_ATTR = "del", TYPE_ATTR = "type";

    private static Map<String,Integer> typeMappings = new HashMap<String,Integer>();

    private Element root;

    public TeXSymbolParser() throws ResourceParseException {
        this(TeXSymbolParser.class.getResourceAsStream(RESOURCE_NAME), RESOURCE_NAME);
    }

    public TeXSymbolParser(InputStream file, String name) throws ResourceParseException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringElementContentWhitespace(true);
            factory.setIgnoringComments(true);
            root = factory.newDocumentBuilder().parse(file).getDocumentElement();
            // set possible valid symbol type mappings
            setTypeMappings();
        } catch (Exception e) { // JDOMException or IOException
            throw new XMLResourceParseException(name, e);
        }
    }

    public Map<String,SymbolAtom> readSymbols() throws ResourceParseException {
        Map<String,SymbolAtom> res = new HashMap<String,SymbolAtom>();
        // iterate all "symbol"-elements
        NodeList list = root.getElementsByTagName("Symbol");
        for (int i = 0; i < list.getLength(); i++) {
            Element symbol = (Element)list.item(i);
            // retrieve and check required attributes
            String name = getAttrValueAndCheckIfNotNull("name", symbol), type = getAttrValueAndCheckIfNotNull(
                              TYPE_ATTR, symbol);
            // retrieve optional attribute
            String del = symbol.getAttribute(DELIMITER_ATTR);
            boolean isDelimiter = (del != null && del.equals("true"));
            // check if type is known
            Object typeVal = typeMappings.get(type);
            if (typeVal == null) // unknown type
                throw new XMLResourceParseException(RESOURCE_NAME, "Symbol",
                                                    "type", "has an unknown value '" + type + "'!");
            // add symbol to the hash table
            res.put(name, new SymbolAtom(name, ((Integer) typeVal).intValue(),
                                         isDelimiter));
        }
        return res;
    }

    private void setTypeMappings() {
        typeMappings.put("ord", TeXConstants.TYPE_ORDINARY);
        typeMappings.put("op", TeXConstants.TYPE_BIG_OPERATOR);
        typeMappings.put("bin", TeXConstants.TYPE_BINARY_OPERATOR);
        typeMappings.put("rel", TeXConstants.TYPE_RELATION);
        typeMappings.put("open", TeXConstants.TYPE_OPENING);
        typeMappings.put("close", TeXConstants.TYPE_CLOSING);
        typeMappings.put("punct", TeXConstants.TYPE_PUNCTUATION);
        typeMappings.put("acc", TeXConstants.TYPE_ACCENT);
    }

    private static String getAttrValueAndCheckIfNotNull(String attrName,
            Element element) throws ResourceParseException {
        String attrValue = element.getAttribute(attrName);
        if (attrValue.equals(""))
            throw new XMLResourceParseException(RESOURCE_NAME, element.getTagName(),
                                                attrName, null);
        return attrValue;
    }
}
