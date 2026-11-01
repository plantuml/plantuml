/* PredefinedTeXFormulaParser.java
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
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Parses and creates predefined TeXFormula objects form an XML-file.
 */
public class PredefinedTeXFormulaParser {

    public static final String RESOURCE_NAME = "PredefinedTeXFormulas.xml";

    private Element root;
    private String type;

    public PredefinedTeXFormulaParser(InputStream file, String type) throws ResourceParseException {
        try {
            this.type = type;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringElementContentWhitespace(true);
            factory.setIgnoringComments(true);
            root = factory.newDocumentBuilder().parse(file).getDocumentElement();
        } catch (Exception e) { // JDOMException or IOException
            throw new XMLResourceParseException("", e);
        }
    }

    public PredefinedTeXFormulaParser(String PredefFile, String type) throws ResourceParseException {
        this(PredefinedTeXFormulaParser.class.getResourceAsStream(PredefFile), type);
    }

    public void parse(Map predefinedTeXFormulas) {
        // get required string attribute
        String enabledAll = getAttrValueAndCheckIfNotNull("enabled", root);
        if ("true".equals(enabledAll)) { // parse formula's
            // iterate all "Font"-elements
            NodeList list = root.getElementsByTagName(this.type);
            for (int i = 0; i < list.getLength(); i++) {
                Element formula = (Element)list.item(i);
                // get required string attribute
                String enabled = getAttrValueAndCheckIfNotNull("enabled", formula);
                if ("true".equals (enabled)) { // parse this formula
                    // get required string attribute
                    String name = getAttrValueAndCheckIfNotNull("name", formula);

                    // parse and build the formula and add it to the table
                    if ("TeXFormula".equals(this.type))
                        predefinedTeXFormulas.put(name, (TeXFormula) new TeXFormulaParser(name, formula, this.type).parse());
                    else
                        predefinedTeXFormulas.put(name, (MacroInfo) new TeXFormulaParser(name, formula, this.type).parse());
                }
            }
        }
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
