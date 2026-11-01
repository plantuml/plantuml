/* TeXFormulaSettingsParser.java
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

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Parses predefined TeXFormula's from an XML-file.
 */
public class TeXFormulaSettingsParser {

    public static final String RESOURCE_NAME = "TeXFormulaSettings.xml";
    public static final String CHARTODEL_MAPPING_EL = "Map";

    private Element root;

    public TeXFormulaSettingsParser() throws ResourceParseException {
        this(GlueSettingsParser.class.getResourceAsStream(RESOURCE_NAME), RESOURCE_NAME);
    }

    public TeXFormulaSettingsParser(InputStream file, String name) throws ResourceParseException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringElementContentWhitespace(true);
            factory.setIgnoringComments(true);
            root = factory.newDocumentBuilder().parse(file).getDocumentElement();
        } catch (Exception e) { // JDOMException or IOException
            throw new XMLResourceParseException(name, e);
        }
    }

    public void parseSymbolToFormulaMappings(String[] mappings, String[] textMappings) throws ResourceParseException {
        Element charToSymbol = (Element)root.getElementsByTagName("CharacterToFormulaMappings").item(0);
        if (charToSymbol != null) // element present
            addFormulaToMap(charToSymbol.getElementsByTagName("Map"), mappings, textMappings);
    }

    public void parseSymbolMappings(String[] mappings, String[] textMappings) throws ResourceParseException {
        Element charToSymbol = (Element)root.getElementsByTagName("CharacterToSymbolMappings").item(0);
        if (charToSymbol != null) // element present
            addToMap(charToSymbol.getElementsByTagName("Map"), mappings, textMappings);
    }

    private static void addToMap(NodeList mapList, String[] tableMath, String[] tableText) throws ResourceParseException {
        for (int i = 0; i < mapList.getLength(); i++) {
            Element map = (Element) mapList.item(i);
            String ch = map.getAttribute("char");
            String symbol = map.getAttribute("symbol");
            String text = map.getAttribute("text");
            // both attributes are required!
            if (ch.equals("")) {
                throw new XMLResourceParseException(RESOURCE_NAME, map.getTagName(), "char", null);
            } else if (symbol.equals("")) {
                throw new XMLResourceParseException(RESOURCE_NAME, map.getTagName(), "symbol", null);
            }

            if (ch.length() == 1) {// valid element found
                tableMath[ch.charAt(0)] =  symbol;
            } else {
                // only single-character mappings allowed, ignore others
                throw new XMLResourceParseException(RESOURCE_NAME, map.getTagName(), "char", "must have a value that contains exactly 1 character!");
            }

            if (tableText != null && !text.equals("")) {
                tableText[ch.charAt(0)] = text;
            }
        }
    }

    private static void addFormulaToMap(NodeList mapList, String[] tableMath, String[] tableText) throws ResourceParseException {
        for (int i = 0; i < mapList.getLength(); i++) {
            Element map = (Element)mapList.item(i);
            String ch = map.getAttribute("char");
            String formula = map.getAttribute("formula");
            String text = map.getAttribute("text");
            // both attributes are required!
            if (ch.equals(""))
                throw new XMLResourceParseException(RESOURCE_NAME, map.getTagName(),
                                                    "char", null);
            else if (formula.equals(""))
                throw new XMLResourceParseException(RESOURCE_NAME, map.getTagName(),
                                                    "formula", null);
            if (ch.length() == 1) {// valid element found
                tableMath[ch.charAt(0)] = formula;
            } else
                // only single-character mappings allowed, ignore others
                throw new XMLResourceParseException(RESOURCE_NAME, map.getTagName(),
                                                    "char",
                                                    "must have a value that contains exactly 1 character!");

            if (tableText != null && !text.equals("")) {
                tableText[ch.charAt(0)] = text;
            }
        }
    }
}
